package com.gourmetcaree.shop.logic.logic;

import static com.gourmetcaree.common.util.SqlUtils.*;
import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.seasar.extension.jdbc.AutoSelect;
import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.beans.util.Beans;
import org.seasar.s2csv.csv.S2CSVWriteCtrl;
import org.seasar.s2csv.csv.factory.S2CSVCtrlFactory;

import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.csv.ApplicationCustomerCsv;
import com.gourmetcaree.common.csv.ApplicationMailUserCsv;
import com.gourmetcaree.common.logic.ValueToNameConvertLogic;
import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TApplication;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.ApplicationAttributeService;
import com.gourmetcaree.db.common.service.CustomerService;
import com.gourmetcaree.db.common.service.WebService;
import com.gourmetcaree.shop.logic.property.ApplicationCsvProperty;

/**
 * 応募CSVロジック
 * @author Takehiro Nakamori
 *
 */
public class ApplicationCsvLogic extends AbstractShopLogic {

	/** 名称変換ロジック */
	@Resource
	private ValueToNameConvertLogic valueToNameConvertLogic;

	/** 応募属性サービス */
	@Resource
	private ApplicationAttributeService applicationAttributeService;

	/** WEBサービス */
	@Resource
	private WebService webService;

	/** 顧客サービス */
	@Resource
	private CustomerService customerService;

	/** CSVコントローラファクトリ */
	@Resource
	protected S2CSVCtrlFactory s2CSVCtrlFactory;

	public void outputApplicationCsv(Integer webId, ApplicationCsvProperty property) throws WNoResultException, UnsupportedEncodingException, IOException {
		List<ApplicationCustomerCsv> csvList = createApplicationCsvList(webId);

		response.setContentType("application/octet-stream;charset=Windows-31J");
		response.setHeader("Content-Disposition","attachment; filename=" + createOutputFilePath(property));

		PrintWriter out = null;

		S2CSVWriteCtrl<ApplicationCustomerCsv> csv_writer = null;

		try {
			out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), property.encode));
			csv_writer =
					s2CSVCtrlFactory.getWriteController(ApplicationCustomerCsv.class, out);

			for (ApplicationCustomerCsv csv : csvList) {
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

	/**
	 * 応募メールユーザのCSV出力
	 * @param applicationIds
	 * @param property
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public void outputApplicationMailUsersCsv(List<Integer> applicationIds, ApplicationCsvProperty property) throws UnsupportedEncodingException, IOException {

		List<ApplicationMailUserCsv> csvList = createApplicationMailUserCsvList(applicationIds);

		response.setContentType("application/octet-stream;charset=Windows-31J");
		response.setHeader("Content-Disposition","attachment; filename=" + createOutputFilePath(property));

		PrintWriter out = null;

		S2CSVWriteCtrl<ApplicationMailUserCsv> csv_writer = null;

		try {
			out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), property.encode));
			csv_writer =
					s2CSVCtrlFactory.getWriteController(ApplicationMailUserCsv.class, out);

			for (ApplicationMailUserCsv csv : csvList) {
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


	/**
	 * 応募CSVリストを作成します。
	 * @param webId ウェブID
	 * @return 応募CSVリスト
	 * @throws WNoResultException データが見つからない場合にスロー
	 */
	private List<ApplicationCustomerCsv> createApplicationCsvList(Integer webId) throws WNoResultException {
		List<ApplicationCustomerCsv> csvList =
				getApplicationSelect(webId)
						.iterate(new IterationCallback<TApplication, List<ApplicationCustomerCsv>>() {
							private List<ApplicationCustomerCsv> list = new ArrayList<ApplicationCustomerCsv>();

							@Override
							public List<ApplicationCustomerCsv> iterate(TApplication entity, IterationContext context) {
								if (entity == null) {
									return list;
								}

								ApplicationCustomerCsv csv = new ApplicationCustomerCsv();
								csv = createApplicationCsv(entity);
								if (csv != null) {
									list.add(csv);
								}
								return list;
							}
						});

		if (CollectionUtils.isEmpty(csvList)) {
			throw new WNoResultException();
		}

		return csvList;
	}

	/**
	 * 応募ユーザのCSVリストを作成
	 * @param applicationIds
	 * @return
	 */
	private List<ApplicationMailUserCsv> createApplicationMailUserCsvList(List<Integer> applicationIds) {
		List<ApplicationMailUserCsv> csvList = getApplicationMailSelect(applicationIds)
				.iterate(new IterationCallback<TApplication, List<ApplicationMailUserCsv>>() {
					private List<ApplicationMailUserCsv> list = new ArrayList<ApplicationMailUserCsv>();

					@Override
					public List<ApplicationMailUserCsv> iterate(TApplication entity, IterationContext context) {
						if (entity == null) {
							return list;
						}

						ApplicationMailUserCsv csv = new ApplicationMailUserCsv();
						csv = createApplicationMailUserCsv(entity);
						if (csv != null) {
							list.add(csv);
						}
						return list;
					}
				});


		return csvList;
	}

	/**
	 * 応募に紐づく、メールが存在するかを確認する
	 * @param appId
	 * @return
	 */
	private boolean isExistMail (int appId) {

		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>(0);

		sql.append(" SELECT ");
		sql.append("   APP.* ");
		sql.append(" FROM ");

		sql.append("   t_application APP  ");

		sql.append(" WHERE ");
		sql.append("  APP.id = ? ");
		params.add(appId);

		sql.append(" AND EXISTS (");
		sql.append(" SELECT * FROM t_mail MAIL WHERE ");
		sql.append(" APP.id = MAIL.application_id");
		sql.append("   AND (  ");
		sql.append("     (MAIL.send_kbn = ? AND MAIL.sender_kbn = ?)  ");
		sql.append("     OR (MAIL.send_kbn = ? AND MAIL.sender_kbn IN (?, ?)) ");
		sql.append("   )  ");
		sql.append("   AND MAIL.delete_flg = ? ");
		sql.append(" )  ");

		params.add(MTypeConstants.SendKbn.SEND);
		params.add(MTypeConstants.SenderKbn.CUSTOMER);
		params.add(MTypeConstants.SendKbn.RECEIVE);
		params.add(MTypeConstants.SenderKbn.MEMBER);
		params.add(MTypeConstants.SenderKbn.NO_MEMBER);
		params.add(DeleteFlgKbn.NOT_DELETED);

		sql.append("   AND APP.delete_flg = ? ");
		params.add(DeleteFlgKbn.NOT_DELETED);

		long count = jdbcManager.getCountBySql(sql.toString(), params.toArray());

		if (count == 0l) {
			return false;
		}

		return true;
	}


	/**
	 * 応募CSVを作成します。
	 * @param entity 応募エンティティ
	 * @return 応募CSV
	 */
	private ApplicationCustomerCsv createApplicationCsv(TApplication entity) {
		ApplicationCustomerCsv csvEntity = Beans.createAndCopy(ApplicationCustomerCsv.class, entity)
				.execute();

		if (!isExistMail(entity.id)) {
			return null;
		}

		// 応募区分（通常、気になる）
		csvEntity.applicationKbnName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.ApplicationKbn.TYPE_CD,
				new String[] { String.valueOf(entity.applicationKbn) });

		// 応募日を変換
		csvEntity.applicationDate = DateUtils.getDateStr(entity.applicationDatetime,
				GourmetCareeConstants.DATE_FORMAT_SLASH);

		// 応募時間を変換
		csvEntity.applicationTime = DateUtils.getDateStr(entity.applicationDatetime, GourmetCareeConstants.TIME_FORMAT);

		// エリア名へ変換
		csvEntity.areaCdName = valueToNameConvertLogic
				.convertToAreaName(new String[] { String.valueOf(entity.areaCd) });

		// 顧客名を取得
		if (entity.customerId != null) {
			csvEntity.customerName = customerService.getCustomerName(entity.customerId);
		}
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

		// 電話番号を生成
		if (StringUtils.isNotEmpty(entity.phoneNo1)) {
			csvEntity.phoneNo = entity.phoneNo1 + "-" + entity.phoneNo2 + "-" + entity.phoneNo3;
		}

		// 希望雇用形態を変換
		csvEntity.employPtnKbnName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.EmployPtnKbn.TYPE_CD,
				new String[] { String.valueOf(entity.employPtnKbn) });

		// 会員区分を変換
		csvEntity.memberFlgName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.MemberFlg.TYPE_CD,
				new String[] { String.valueOf(entity.memberFlg) });

		// 生年月日が入っていれば年齢にする
		if (entity.birthday != null) {
			csvEntity.age = String.valueOf(GourmetCareeUtil.convertToAge(entity.birthday));
		}

		// リニューアル移行は職種区分がプルダウンで設定
		if (entity.jobKbn != null) {
			csvEntity.hopeJob = valueToNameConvertLogic.convertToJobName(new String[] { String.valueOf(entity.jobKbn) });
		}

		return csvEntity;
	}

	/**
	 * 応募メールユーザCSVを作成
	 * @param entity
	 * @return
	 */
	private ApplicationMailUserCsv createApplicationMailUserCsv(TApplication entity) {
		ApplicationMailUserCsv csvEntity = Beans.createAndCopy(ApplicationMailUserCsv.class, entity)
				.execute();

		if (!isExistMail(entity.id)) {
			return null;
		}

		// 応募日を変換
		csvEntity.applicationDate = DateUtils.getDateStr(entity.applicationDatetime,
				GourmetCareeConstants.DATE_FORMAT_SLASH);

		// 応募時間を変換
		csvEntity.applicationTime = DateUtils.getDateStr(entity.applicationDatetime, GourmetCareeConstants.TIME_FORMAT);

		// エリア名へ変換
		csvEntity.areaCdName = valueToNameConvertLogic
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

		// 電話番号を生成
		if (StringUtils.isNotEmpty(entity.phoneNo1)) {
			csvEntity.phoneNo = entity.phoneNo1 + "-" + entity.phoneNo2 + "-" + entity.phoneNo3;
		}

		// 希望雇用形態を変換
		csvEntity.employPtnKbnName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.EmployPtnKbn.TYPE_CD,
				new String[] { String.valueOf(entity.employPtnKbn) });

		// 生年月日が入っていれば年齢にする
		if (entity.birthday != null) {
			csvEntity.age = String.valueOf(GourmetCareeUtil.convertToAge(entity.birthday));
		}

		// リニューアル移行は職種区分がプルダウンで設定
		if (entity.jobKbn != null) {
			csvEntity.hopeJob = valueToNameConvertLogic.convertToJobName(new String[] { String.valueOf(entity.jobKbn) });
		}

		// 取得資格名に変換
		int[] qualificationKbnList = applicationAttributeService.getApplicationAttrValue(entity.id, MTypeConstants.QualificationKbn.TYPE_CD);
		csvEntity.qualificationName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.QualificationKbn.TYPE_CD, GourmetCareeUtil.toIntToStringArray(qualificationKbnList));

		return csvEntity;
	}

	/**
	 * 応募データのセレクトを取得します。
	 * @param webId ウェブID
	 * @return 応募データのセレクト
	 */
	private AutoSelect<TApplication> getApplicationSelect(Integer webId) {

		//検索実行日の1年前の日付を取得
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -1);

		SimpleWhere where = new SimpleWhere();
		where.eq(toCamelCase(TApplication.CUSTOMER_ID), getCustomerId());
		if (webId != null) {
			where.eq(toCamelCase(TApplication.WEB_ID), webId);
		}
		where.eq(toCamelCase(TApplication.MISCHIEF_FLG), MTypeConstants.MischiefFlg.NORMAL);	// いたずら応募以外
		where.eq(toCamelCase(TApplication.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);
		where.ge(toCamelCase(TApplication.APPLICATION_DATETIME), cal.getTime());

		return jdbcManager.from(TApplication.class)
						.where(where)
						.orderBy(desc(toCamelCase(TApplication.APPLICATION_DATETIME)));
	}

	/**
	 * 応募メールユーザを取得
	 * @param applicationIds
	 * @return
	 */
	private AutoSelect<TApplication> getApplicationMailSelect(List<Integer> applicationIds) {
		SimpleWhere where = new SimpleWhere();
		where.in(toCamelCase(TApplication.ID), applicationIds);
		where.eq(toCamelCase(TApplication.MISCHIEF_FLG), MTypeConstants.MischiefFlg.NORMAL);
		where.eq(toCamelCase(TApplication.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		return jdbcManager.from(TApplication.class)
				.where(where)
				.orderBy(desc(toCamelCase(TApplication.APPLICATION_DATETIME)));
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
	 * ファイル名を生成する
	 * @return ファイル名
	 */
	private static String createOutputFilePath(ApplicationCsvProperty property) {

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
}
