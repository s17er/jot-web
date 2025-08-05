package com.gourmetcaree.admin.service.logic;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.extension.jdbc.SqlSelect;
import org.seasar.framework.beans.util.Beans;
import org.seasar.s2csv.csv.S2CSVWriteCtrl;
import org.seasar.s2csv.csv.factory.S2CSVCtrlFactory;

import com.gourmetcaree.admin.service.property.BaseProperty;
import com.gourmetcaree.arbeitsys.service.ArbeitGyotaiService;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.csv.ArbeitApplicationCsv;
import com.gourmetcaree.common.dto.BaseDto;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.logic.ValueToNameConvertLogic;
import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.constants.MAreaConstants.AreaCd;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;

/**
 * バイト応募ロジック
 * @author Takehiro Nakamori
 *
 */
public class ArbeitApplicationLogic extends AbstractAdminLogic {


	/** CSVのファイル名 */
	private static final String CSV_FILE_NAME = "arbeitApplication";


	/** CSVコントローラファクトリ */
	@Resource
	private S2CSVCtrlFactory s2CSVCtrlFactory;

	/** バイト側業態サービス */
	@Resource
	private ArbeitGyotaiService arbeitGyotaiService;

	/** 名称変換ロジック */
	@Resource
	private ValueToNameConvertLogic valueToNameConvertLogic;

	/**
	 * 検索を行います。
	 * @param searchProperty
	 * @throws WNoResultException
	 */
	public void doSearch(SearchProperty property) throws WNoResultException {
		StringBuffer sql = new StringBuffer(0);
		List<Object> params = new ArrayList<Object>(0);
		createSearchSql(sql, params, property);

		int count = (int) jdbcManager.getCountBySql(sql.toString(), params.toArray());

		if (count == 0) {
			throw new WNoResultException();
		}

		PageNavigateHelper pageNavi = new PageNavigateHelper(property.maxRow);
		pageNavi.changeAllCount(count);
		pageNavi.setPage(property.targetPage);

		addSortKey(sql);

		property.searchList = jdbcManager.selectBySql(SearchListDto.class, sql.toString(), params.toArray())
											.limit(pageNavi.limit)
											.offset(pageNavi.offset)
											.getResultList();

		property.pageNavi = pageNavi;

	}



	/**
	 * CSVの出力を行います。
	 * @param property 検索プロパティ
	 * @throws WNoResultException 検索結果がない場合にスロー
	 * @throws UnsupportedEncodingException エンコードが不正な場合にスロー
	 * @throws IOException CSV書き出しに失敗した場合にスロー
	 */
	public void outputCsv(SearchProperty property) throws WNoResultException, UnsupportedEncodingException, IOException {
		StringBuffer sql = new StringBuffer(0);
		List<Object> params = new ArrayList<Object>(0);
		createSearchSql(sql, params, property);

		long count = jdbcManager.getCountBySql(sql.toString(), params.toArray());
		if (count == 0l) {
			throw new WNoResultException();
		}

		addSortKey(sql);
		outputCsv(jdbcManager.selectBySql(SearchListDto.class, sql.toString(), params.toArray()));
	}




	/**
	 * CSVを出力します。
	 * @param select 検索結果
	 * @throws UnsupportedEncodingException エンコードが不正な場合にスロー
	 * @throws IOException CSVの出力に失敗した場合にスロー
	 */
	private void outputCsv(SqlSelect<SearchListDto> select) throws UnsupportedEncodingException, IOException {
		String fileName = createOutputFileName();


		response.setContentType(GourmetCareeConstants.CSV_OUTPUT_CONTENT_TYPE);
		GourmetCareeUtil.setResponseCsvHeader(response, fileName);
		PrintWriter out = null;
		S2CSVWriteCtrl<ArbeitApplicationCsv> csv_writer = null;

		try {
			out = new PrintWriter(
					new OutputStreamWriter(
							response.getOutputStream(),
							GourmetCareeConstants.CSV_ENCODING));

			csv_writer =
					s2CSVCtrlFactory.getWriteController(ArbeitApplicationCsv.class, out);

			select.iterate(createCsvIterate(csv_writer));

		} finally {
			if (csv_writer != null) {
				csv_writer.close();
			}

			if (out != null) {
				out.close();
			}
		}
	}


	/**
	 * CSV作成用イテレートコールバックを作成します。<br />
	 * イテレート内部でCSV作成ロジックを記述します。
	 * @param writer CSVライター
	 * @return コールバック (CSVを書き出すだけなので、コールバックの返り値はなし)
	 */
	private IterationCallback<SearchListDto, Void> createCsvIterate(final S2CSVWriteCtrl<ArbeitApplicationCsv> writer) {
		return new  IterationCallback<SearchListDto, Void>() {

			@Override
			public Void iterate(SearchListDto dto, IterationContext context) {
				if (dto == null) {
					return null;
				}

				writer.write(convertSearchResultToCsv(dto));

				return null;
			}

		};
	}


	/**
	 * 検索結果をCSVに変換します。
	 * @param dto DTO
	 * @return CSV
	 */
	private ArbeitApplicationCsv convertSearchResultToCsv(SearchListDto dto) {

		ArbeitApplicationCsv csv = Beans.createAndCopy(ArbeitApplicationCsv.class, dto)
				.execute();

		csv.applicationDate = DateUtils.getDateStr(dto.applicationDatetime, GourmetCareeConstants.DATE_FORMAT_SLASH);
		csv.applicationTime = DateUtils.getDateStr(dto.applicationDatetime, "HH:mm");

		if (dto.arbeitGyotaiId != null) {
			csv.arbeitGyotaiName = arbeitGyotaiService.convertIdToName(dto.arbeitGyotaiId);
		}

		if (dto.sexKbn != null) {
			csv.sexKbnName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.Sex.TYPE_CD, dto.sexKbn);
		}

		if (dto.prefecturesCd != null) {
			csv.prefecturesCdName = valueToNameConvertLogic.convertToPrefecturesName(dto.prefecturesCd);
		}

		if (dto.currentJobKbn != null) {
			csv.currentJobKbnName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.CurrentJob.TYPE_CD, dto.currentJobKbn);
		}

		if (dto.possibleEntryTermKbn != null) {
			csv.possibleEntryTermKbnName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.PossibleEntryTermKbn.TYPE_CD, dto.possibleEntryTermKbn);
		}

		if (dto.foodExpKbn != null) {
			csv.foodExpKbnName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.AriNashiKbn.TYPE_CD, dto.foodExpKbn);
		}

		if (dto.terminalKbn != null) {
			csv.terminalKbnName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.TerminalKbn.TYPE_CD, dto.terminalKbn);
		}

		if (dto.areaCd != null) {
			csv.areaCd = valueToNameConvertLogic.convertToAreaName(new String[]{Integer.toString(dto.areaCd)});
		}

		// 自社スタッフの場合は表示項目を制限する
		if (ManageAuthLevel.STAFF.value().equals(getAuthLevel())) {
			csv.address = "";
			csv.name = "";
			csv.nameKana = "";
			csv.phoneNo = "";
			csv.mailAddress = "";
			csv.municipality = GourmetCareeUtil.toMunicipality(csv.municipality);	// 市区町村のみに削除
		}

		return csv;
	}

	/**
	 * 検索SQLを作成します。
	 * @param sql SQL
	 * @param params 条件
	 * @param property 検索プロパティ
	 */
	private static void createSearchSql(StringBuffer sql, List<Object> params, SearchProperty property) {
		sql.append(" SELECT ");
		sql.append("   APP.id ");
		sql.append("   , APP.application_name ");
		sql.append("   , SHOP.arbeit_gyotai_id ");
		sql.append("   , APP.current_job_kbn ");
		sql.append("   , APP.application_datetime ");
		sql.append("   , APP.name ");
		sql.append("   , APP.name_kana ");
		sql.append("   , APP.sex_kbn ");
		sql.append("   , APP.age ");
		sql.append("   , APP.zip_cd ");
		sql.append("   , APP.prefectures_cd ");
		sql.append("   , APP.municipality ");
		sql.append("   , APP.address ");
		sql.append("   , APP.phone_no ");
		sql.append("   , APP.mail_address ");
		sql.append("   , APP.connection_time ");
		sql.append("   , APP.food_exp_kbn ");
		sql.append("   , APP.possible_entry_term_kbn ");
		sql.append("   , APP.application_job ");
		sql.append("   , APP.application_self_pr ");
		sql.append("   , APP.terminal_kbn  ");
		sql.append("   , APP.area_cd ");
		sql.append("   , CUS.customer_name  ");
		sql.append(" FROM ");
		sql.append("   t_arbeit_application APP  ");
		sql.append("   INNER JOIN t_shop_list SHOP  ");
		sql.append("     ON APP.shop_list_id = SHOP.id  ");
		sql.append("   INNER JOIN m_customer CUS  ");
		sql.append("     ON SHOP.customer_id = CUS.id  ");
		sql.append(" WHERE 1=1 ");


		// エリアコード
		if (property.areaCd != null) {
			sql.append(" AND  APP.area_cd = ?  ");
			params.add(property.areaCd);
		}

		// 応募ID
		if (property.applicationId != null) {
			sql.append("   AND APP.id = ?  ");
			params.add(property.applicationId);
		}

		// 店舗名
		if (StringUtils.isNotBlank(property.applicationName)) {
			sql.append(" AND ( ");
			sql.append(SqlUtils.createLikeSearch(
					property.applicationName,
					params,
					"   APP.application_name LIKE ?  "));
			sql.append(" ) ");
		}

		// 性別
		if (property.sexKbn != null) {
			sql.append("   AND APP.sex_kbn = ?  ");
			params.add(property.sexKbn);
		}

		// 都道府県
		if (property.prefecturesCd != null) {
			sql.append("   AND APP.prefectures_cd = ?  ");
			params.add(property.prefecturesCd);
		}


		// バイト側業種
		if (property.arbeitGyotaiId != null) {
			sql.append("   AND SHOP.arbeit_gyotai_id = ?  ");
			params.add(property.arbeitGyotaiId);
		}

		// 現在の職業
		if (property.currentJobKbn != null) {
			sql.append("   AND APP.current_job_kbn = ?  ");
			params.add(property.currentJobKbn);
		}

		// 飲食店勤務の経験区分
		if (property.foodExpKbn != null) {
			sql.append("   AND APP.food_exp_kbn = ?  ");
			params.add(property.foodExpKbn);
		}

		// 端末区分
		if (property.terminalKbn != null) {
			sql.append("   AND APP.terminal_kbn = ? ");
			params.add(property.terminalKbn);
		}


		// 氏名
		if (StringUtils.isNotBlank(property.name)) {
			String name = property.name.replaceAll("　| ", "");

            sql.append("   AND (  ");
            sql.append(SqlUtils.createLikeSearch(
            		name,
                    params,
                    " REPLACE(APP.name, ' ', '') LIKE ?  "
                    ));
            sql.append("   )  ");
		}

		// 氏名フリガナ
        if (StringUtils.isNotBlank(property.nameKana)) {
        	String nameKana = property.nameKana.replaceAll("　| ", "");

            sql.append("   AND (  ");
            sql.append(SqlUtils.createLikeSearch(
            		nameKana,
                    params,
                    " Replace(APP.name_kana, ' ', '') LIKE ? "
                    ));
            sql.append("   )  ");
        }

		// 顧客名
		if (StringUtils.isNotBlank(property.customerName)) {
			sql.append("   AND (  ");
			sql.append(SqlUtils.createLikeSearch(
					property.customerName,
					params,
					"     CUS.customer_name LIKE ?  ",
					"     OR CUS.customer_name_kana LIKE ? "
					));
			sql.append("   )  ");
		}


		// メールアドレス
		if (StringUtils.isNotBlank(property.mailAddress)) {
			sql.append(" AND ( ");
			sql.append(SqlUtils.createLikeSearch(
					property.mailAddress,
					params,
					"  APP.mail_address LIKE ?  "
					));
			sql.append(" ) ");
		}


		// 年齢(下限)
		if (property.lowerAge != null) {
			sql.append("   AND APP.age >= ? ");
			params.add(property.lowerAge);
		}

		// 年齢(上限)
		if (property.upperAge != null) {
			sql.append("   AND APP.age <= ? ");
			params.add(property.upperAge);

		}

		// 応募日時(開始)
		if (property.applicationStartDatetime != null) {
			sql.append("   AND APP.application_datetime >= ? ");
			params.add(property.applicationStartDatetime);

		}

		// 応募日時(終了)
		if (property.applicationEndDatetime != null) {
			sql.append("   AND APP.application_datetime <= ? ");
			params.add(property.applicationEndDatetime);
		}

		sql.append("   AND APP.delete_flg = ?  ");
		sql.append("   AND CUS.delete_flg = ?  ");
		sql.append("   AND SHOP.delete_flg = ? ");
		params.add(DeleteFlgKbn.NOT_DELETED);
		params.add(DeleteFlgKbn.NOT_DELETED);
		params.add(DeleteFlgKbn.NOT_DELETED);

	}

	/**
	 * ソートキーを追加します。
	 * @param sql ソートキー
	 */
	private static void addSortKey(StringBuffer sql) {
		sql.append(" ORDER BY APP.application_datetime DESC");
	}



	/**
	 * 出力ファイル名を作成します。
	 * @return
	 */
	private static String createOutputFileName() {
		String dateStr = null;
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

		dateStr = sdf.format(date);

		String outputFileName = new StringBuilder()
		.append(CSV_FILE_NAME)
		.append("_")
		.append(dateStr)
		.append(".csv")
		.toString();

		return outputFileName;
	}

	/**
	 * 検索プロパティ
	 * @author Takehiro Nakamori
	 *
	 */
	public static class SearchProperty extends BaseProperty {

		/** シリアルバージョンUID */
		private static final long serialVersionUID = -4616508696882249177L;

		/** ID */
		public Integer applicationId;

		/**
		 * エリアコード <br />
		 * 検索項目にはないため、デフォルトで設定しておく。
		 */
		public Integer areaCd = AreaCd.SHUTOKEN_AREA;

		/** 性別区分 */
		public Integer sexKbn;

		/** 都道府県コード */
		public Integer prefecturesCd;

		/** 現在の職業区分 */
		public Integer currentJobKbn;

		/** 飲食店勤務の経験区分 */
		public Integer foodExpKbn;

		/** 応募先名(店舗名) */
		public String applicationName;

		/** 端末区分 */
		public Integer terminalKbn;

		/** 顧客名 */
		public String customerName;

		/** 名前 */
		public String name;

		/** 名前フリガナ */
		public String nameKana;

		/** メールアドレス */
		public String mailAddress;

		/** 年齢(下限) */
		public Integer lowerAge;

		/** 年齢(上限) */
		public Integer upperAge;

		/** 応募日時(開始) */
		public Timestamp applicationStartDatetime;

		/** 応募日時(終了) */
		public Timestamp applicationEndDatetime;

		/** バイト側業態 */
		public Integer arbeitGyotaiId;


		/** 最大表示件数 */
		public int maxRow;

		/** 対象ページ */
		public int targetPage;



		/** 検索結果リスト */
		public List<SearchListDto> searchList;

		/** ページナビ */
		public PageNavigateHelper pageNavi;


		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
		}
	}

	/**
	 * 検索リストDTO
	 * @author Takehiro Nakamori
	 *
	 */
	public static class SearchListDto extends BaseDto {


		/** シリアルバージョンUID */
		private static final long serialVersionUID = 8199171290393504561L;

		/** ID */
		public Integer id;

		/** 顧客ID */
		public Integer customerId;

		/** エリア */
		public Integer areaCd;

		/** 店舗名 */
		public String applicationName;

		/** バイト側業態ID */
		public Integer arbeitGyotaiId;

		/** 現在の職業区分 */
		public Integer currentJobKbn;

		/** 応募日時 */
		public Timestamp applicationDatetime;

		/** 性別区分 */
		public Integer sexKbn;

		/** 年齢 */
		public Integer age;

		/** 郵便番号 */
		public String zipCd;

		/** 都道府県コード */
		public Integer prefecturesCd;

		/** 勤務可能時期区分 */
		public Integer possibleEntryTermKbn;

		/** 市区 */
		public String municipality;

		/** 住所 */
		public String address;

		/** 電話番号 */
		public String phoneNo;

		/** メールアドレス */
		public String mailAddress;

		/** 名前 */
		public String name;

		/** 名前(カナ) */
		public String nameKana;


		/** 飲食店勤務の経験区分 */
		public Integer foodExpKbn;

		/** 端末区分 */
		public Integer terminalKbn;

		/** 顧客名 */
		public String customerName;

		/** 応募職種 */
		public String applicationJob;


		/** 希望連絡時間・連絡方法 */
		public String connectionTime;


		/** 自己ＰＲ・要望 */
		public String applicationSelfPr;


	}


}

