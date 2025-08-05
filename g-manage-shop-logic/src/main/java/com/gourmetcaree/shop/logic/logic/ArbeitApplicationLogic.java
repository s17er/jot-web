package com.gourmetcaree.shop.logic.logic;

import static com.gourmetcaree.common.util.SqlUtils.*;
import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.extension.jdbc.SqlSelect;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.s2csv.csv.S2CSVWriteCtrl;
import org.seasar.s2csv.csv.factory.S2CSVCtrlFactory;

import com.gourmetcaree.arbeitsys.service.ArbeitGyotaiService;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.csv.ArbeitApplicationMailUserCsv;
import com.gourmetcaree.common.dto.BaseDto;
import com.gourmetcaree.common.logic.ValueToNameConvertLogic;
import com.gourmetcaree.common.property.PagerProperty;
import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TArbeitApplication;
import com.gourmetcaree.db.common.entity.TShopList;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.common.service.ArbeitApplicationService;
import com.gourmetcaree.db.common.service.MailService;
import com.gourmetcaree.db.common.service.ShopListService;
import com.gourmetcaree.shop.logic.csv.ArbeitApplicationCsv;
import com.gourmetcaree.shop.logic.dto.ArbeitApplicationRetDto;
import com.gourmetcaree.shop.logic.logic.ApplicationLogic.ApplicationSearchProperty;
import com.gourmetcaree.shop.logic.property.ArbeitApplicationCsvProperty;

/**
 *
 * グルメdeバイトの応募者の一覧を取得する
 *
 * @author Yamane
 *
 */
@Component(instance=InstanceType.SINGLETON)
public class ArbeitApplicationLogic extends AbstractShopLogic {

	private static final Logger log = Logger.getLogger(ArbeitApplicationLogic.class);


	/** グルメdeバイトサービス */
	@Resource
	private ArbeitApplicationService arbeitApplicationService;

	@Resource
	ShopListService shopListService;

	@Resource
	private ArbeitGyotaiService arbeitGyotaiService;

	/** メールサービス */
	@Resource
	protected MailService mailService;

	/** CSVコントローラファクトリ */
	@Resource
	private S2CSVCtrlFactory s2CSVCtrlFactory;

	@Resource
	private ValueToNameConvertLogic valueToNameConvertLogic;

	/**
	 * グルメdeバイト
	 * @param property
	 * @return
	 * @throws WNoResultException
	 */
	public ArbeitApplicationRetDto getArbeitApplication(PagerProperty property, ApplicationSearchProperty searchProperty) throws WNoResultException {

		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();

		createApplicationSearchSql(sql, params, searchProperty);
		long count = jdbcManager.getCountBySql(sql.toString(), params.toArray());

		if (count == 0) {
			throw new WNoResultException("検索結果が0件です。");
		}

		PageNavigateHelper pageNavi = new PageNavigateHelper(property.maxRow);
		pageNavi.sortKey = desc(dot("APP", TArbeitApplication.APPLICATION_DATETIME)) + ", " + desc(dot("APP", TArbeitApplication.ID));
		pageNavi.changeAllCount((int)count);
		pageNavi.setPage(property.targetPage);

		sql.append(" ORDER BY ").append(pageNavi.sortKey);

		ArbeitApplicationRetDto dto = new ArbeitApplicationRetDto();
		dto.applicationSelect = jdbcManager.selectBySql(TArbeitApplication.class, sql.toString(), params.toArray())
						.disallowNoResult()
						.limit(pageNavi.limit)
						.offset(pageNavi.offset);

		dto.pageNavi = pageNavi;

		return dto;

	}


	public void outputCsv(ApplicationSearchProperty property, HttpServletResponse response, String fileName) throws WNoResultException, IOException {
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();


		createApplicationSearchSql(sql, params, property);

		long count = jdbcManager.getCountBySql(sql.toString(), params.toArray());

		if (count == 0l) {
			throw new WNoResultException("CSVの結果がありません。");
		}

		String csvFileName = String.format("%s_%s.csv", new SimpleDateFormat(GourmetCareeConstants.DATETIME_FORMAT_NONSLASH).format(new Date()), fileName);
		response.setContentType(GourmetCareeConstants.CSV_OUTPUT_CONTENT_TYPE);
		response.setHeader(GourmetCareeConstants.CSV_HEADER_PARAM1, GourmetCareeConstants.CSV_HEADER_FILENAME_PREFIX.concat(csvFileName));


		PrintWriter out = null;

		S2CSVWriteCtrl<ArbeitApplicationCsv> csv_writer = null;

		sql.append(" ORDER BY ").append(desc(dot("APP", TArbeitApplication.APPLICATION_DATETIME)) + ", " + desc(dot("APP", TArbeitApplication.ID)));
		SqlSelect<TArbeitApplication> select = jdbcManager.selectBySql(TArbeitApplication.class, sql.toString(), params.toArray());
		try {
			out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), GourmetCareeConstants.CSV_ENCODING));
			csv_writer = s2CSVCtrlFactory.getWriteController(ArbeitApplicationCsv.class, out);

			writeCsv(select, csv_writer);
		} finally {
			if (csv_writer != null) {
				csv_writer.close();
				csv_writer = null;
			}
		}
	}

	/**
	 * アルバイト応募メールユーザCSVを出力
	 * @param arbeitApplicationIds
	 * @param property
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public void outputArbeitApplicationMailUsersCsv(List<Integer> arbeitApplicationIds, ArbeitApplicationCsvProperty property) throws UnsupportedEncodingException, IOException {

		List<ArbeitApplicationMailUserCsv> csvList = createArbeitApplicationMailUserCsvList(arbeitApplicationIds);

		response.setContentType("application/octet-stream;charset=Windows-31J");
		response.setHeader("Content-Disposition","attachment; filename=" + createOutputFilePath(property));

		PrintWriter out = null;

		S2CSVWriteCtrl<ArbeitApplicationMailUserCsv> csv_writer = null;

		try {
			out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), property.encode));
			csv_writer =
					s2CSVCtrlFactory.getWriteController(ArbeitApplicationMailUserCsv.class, out);

			for (ArbeitApplicationMailUserCsv csv : csvList) {
				csv_writer.write(csv);
			}
		} finally {
			if (csv_writer != null) {
				csv_writer.close();
				csv_writer = null;
			}

			if (out != null) {
				out.close();
				out = null;
			}

			csvList = null;
		}
	}


	private void writeCsv(SqlSelect<TArbeitApplication> select, final S2CSVWriteCtrl<ArbeitApplicationCsv> writer) {
		select.iterate(new IterationCallback<TArbeitApplication, Void>() {

			private final SimpleDateFormat dateFormat = new SimpleDateFormat(GourmetCareeConstants.DATE_TIME_FORMAT);

			@Override
			public Void iterate(TArbeitApplication entity, IterationContext context) {
				if (entity == null) {
					return null;
				}

				TShopList shop;

				try {
					shop = shopListService.findById(entity.shopListId);
				} catch (SNoResultException e) {
					shop = null;
					log.warn("店舗が取得できませんでした。ID:" + entity.shopListId, e);
				}
				ArbeitApplicationCsv csv = Beans.createAndCopy(ArbeitApplicationCsv.class, entity).excludes("address").execute();
				if (entity.sexKbn != null) {
					csv.sexKbnStr = valueToNameConvertLogic.convertToTypeName(MTypeConstants.Sex.TYPE_CD, entity.sexKbn);
				}

				if (entity.currentJobKbn != null) {
					csv.currentJobKbnStr = valueToNameConvertLogic.convertToTypeName(MTypeConstants.CurrentJob.TYPE_CD, entity.currentJobKbn);
				}

				if (entity.possibleEntryTermKbn != null) {
					csv.possibleEntryTermKbnStr = valueToNameConvertLogic.convertToTypeName(MTypeConstants.PossibleWorkTermKbn.TYPE_CD, entity.possibleEntryTermKbn);
				}

				if (entity.foodExpKbn != null) {
					csv.foodExpKbnStr = valueToNameConvertLogic.convertToTypeName(MTypeConstants.AriNashiKbn.TYPE_CD, entity.foodExpKbn);
				}

				StringBuilder address = new StringBuilder();
				if (entity.prefecturesCd != null) {
					address.append(valueToNameConvertLogic.convertToPrefecturesName(entity.prefecturesCd));
				}
				address.append(StringUtils.defaultString(entity.municipality, ""))
						.append(StringUtils.defaultString(entity.address, ""));

				csv.address = address.toString();

				if (entity.applicationDatetime != null) {
					csv.applicationDatetimeStr = dateFormat.format(entity.applicationDatetime);
				}


				if (shop != null) {
					csv.shopName = shop.shopName;
				}


				writer.write(csv);


				return null;
			}
		});

	}

	/**
	 * アルバイト応募メール検索SQLを作成します。
	 * @param sql SQL
	 * @param params パラメータ
	 * @param property 検索プロパティ
	 */
	private void createApplicationSearchSql(StringBuffer sql, List<Object> params, ApplicationSearchProperty property) {
		//検索実行日の1年前の日付を取得
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -1);

		sql.append(" SELECT ");
		sql.append("   APP.*  ");
		sql.append(" FROM ");
		sql.append("   t_arbeit_application APP ");
		sql.append(" WHERE ");
		sql.append("   APP.customer_id = ? ");
		sql.append("   AND APP.application_datetime > ? ");
			params.add(getCustomerId());
			params.add(cal.getTime());

		ApplicationLogic.addMailSearchSql(sql, params, property, " APP.id = MAIL.arbeit_application_id ");

		sql.append("   AND APP.delete_flg = ? ");

		params.add(DeleteFlgKbn.NOT_DELETED);

	}

	/**
	 * アルバイト応募テーブルのデータを取得します。
	 * ログインユーザが操作可能なデータのみを取得します。
	 * 取得条件
	 * ・顧客IDにログインユーザが含まれていること
	 * ・応募日時が当日の１年前以上であること
	 * @param id
	 * @return
	 * @throws WNoResultException
	 */
	public TArbeitApplication findByIdFromApplication(int id) throws WNoResultException {

		try {
			//検索実行日の1年前の日付を取得
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.YEAR, -1);

			SimpleWhere where = new SimpleWhere()
								.eq(toCamelCase(TArbeitApplication.CUSTOMER_ID), getCustomerId())
								.ge(toCamelCase(TArbeitApplication.APPLICATION_DATETIME), cal.getTime());

			return arbeitApplicationService.findById(id, where);
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}

	/**
	 * 未読メールのチェックを行う
	 * @param arbeitId グルメdeバイトメールId
	 * @return true:存在する、false:存在しない
	 */
	public boolean isUnopenedApplicantMailExist(int arbeitId) {

		return mailService.isReceivedApplicationMailFromExist(arbeitId, getCustomerId(), MTypeConstants.MailKbn.ARBEIT_APPLICATION, MTypeConstants.MailStatus.UNOPENED);
	}

	/**
	 * ファイル名を生成する
	 * @return ファイル名
	 */
	private static String createOutputFilePath(ArbeitApplicationCsvProperty property) {

		String filename = null;
		String dateStr = null;
		filename = property.faileName;

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

		dateStr = sdf.format(date);

		String outputFileName = new StringBuilder()
		.append(filename)
		.append("_")
		.append(dateStr)
		.append(".csv")
		.toString();

		return outputFileName;
	}

	/**
	 * アルバイト応募メールCSVリストを作成
	 * @param arbeitApplicationIds
	 * @return
	 */
	private List<ArbeitApplicationMailUserCsv> createArbeitApplicationMailUserCsvList(List<Integer> arbeitApplicationIds) {

		List<ArbeitApplicationMailUserCsv> csvList = new ArrayList<>();

		for(SearchListDto dto : getArbeitApplicationMailSelect(arbeitApplicationIds)) {
			ArbeitApplicationMailUserCsv csv = new ArbeitApplicationMailUserCsv();
			csv = createArbeitApplicationMailUserCsv(dto);
			csvList.add(csv);
		}

		return csvList;
	}

	/**
	 * アルバイト応募メールユーザを取得
	 * @param arbeitApplicationIds
	 * @return
	 */
	private List<SearchListDto> getArbeitApplicationMailSelect(List<Integer> arbeitApplicationIds) {
		StringBuffer sql = new StringBuffer(0);
		List<Object> params = new ArrayList<Object>(0);
		createArbeitApplicationMailUserCsvSql(sql, params, arbeitApplicationIds);

		return jdbcManager.selectBySql(SearchListDto.class, sql.toString(), params.toArray())
				.getResultList();
	}

	/**
	 * アルバイト応募メールユーザを取得するSQLを作成
	 * @param sql
	 * @param params
	 * @param arbeitApplicationIds
	 */
	private static void createArbeitApplicationMailUserCsvSql(StringBuffer sql, List<Object> params, List<Integer> arbeitApplicationIds) {
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


		sql.append("   WHERE APP.delete_flg = ?  ");
		sql.append("   AND CUS.delete_flg = ?  ");
		sql.append("   AND SHOP.delete_flg = ? ");
		params.add(DeleteFlgKbn.NOT_DELETED);
		params.add(DeleteFlgKbn.NOT_DELETED);
		params.add(DeleteFlgKbn.NOT_DELETED);

		sql.append(SqlUtils.andInNoCamelize("APP.id", arbeitApplicationIds.size()));
		params.addAll(arbeitApplicationIds);

		sql.append("   ORDER BY APP.application_datetime DESC ");

	}

	/**
	 * アルバイト応募メールユーザCSVを作成
	 * @param entity
	 * @return
	 */
	private ArbeitApplicationMailUserCsv createArbeitApplicationMailUserCsv(SearchListDto entity) {
		ArbeitApplicationMailUserCsv csvEntity = Beans.createAndCopy(ArbeitApplicationMailUserCsv.class, entity)
				.execute();

		// 応募日を変換
		csvEntity.applicationDate = DateUtils.getDateStr(entity.applicationDatetime,
				GourmetCareeConstants.DATE_FORMAT_SLASH);

		// 応募時間を変換
		csvEntity.applicationTime = DateUtils.getDateStr(entity.applicationDatetime, GourmetCareeConstants.TIME_FORMAT);

		if (entity.arbeitGyotaiId != null) {
			csvEntity.arbeitGyotaiName = arbeitGyotaiService.convertIdToName(entity.arbeitGyotaiId);
		}

		// エリア名へ変換
		csvEntity.areaCd = valueToNameConvertLogic
				.convertToAreaName(new String[] { String.valueOf(entity.areaCd) });

		// 性別名へ変換
		csvEntity.sexKbnName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.Sex.TYPE_CD,
				new String[] { String.valueOf(entity.sexKbn) });

		// 都道府県名へ変換
		if (entity.prefecturesCd != null) {
			csvEntity.prefecturesCdName = valueToNameConvertLogic.convertToPrefecturesName(new String[] { String
					.valueOf(entity.prefecturesCd) });
		}

		// 市区町村を生成
		csvEntity.municipality = checkNull(entity.municipality);

		// 住所を生成
		csvEntity.address = checkNull(entity.address);

		// 入社可能時期を変換
		csvEntity.possibleEntryTermKbnName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.PossibleEntryTermKbn.TYPE_CD, new String[]{String.valueOf(entity.possibleEntryTermKbn)});

		// 飲食店の経験
		csvEntity.foodExpKbnName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.FoodExpKbn.TYPE_CD, new String[]{String.valueOf(entity.foodExpKbn)});

		// 現在の状況
		csvEntity.currentJobKbnName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.CurrentJob.TYPE_CD, entity.currentJobKbn);

		return csvEntity;
	}
	/**
	 * 対象文字列がNUllかどうかチェックする
	 * @param target
	 * @return NULLの場合、""を返す
	 */
	private static String checkNull(String target) {

		if (target == null) {
			return "";
		} else {
			return target;
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
