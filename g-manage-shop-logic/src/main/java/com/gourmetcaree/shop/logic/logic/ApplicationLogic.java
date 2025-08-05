package com.gourmetcaree.shop.logic.logic;

import static com.gourmetcaree.common.util.GourmetCareeUtil.*;
import static com.gourmetcaree.common.util.SqlUtils.*;
import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.seasar.extension.jdbc.SqlSelect;
import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.logic.ValueToNameConvertLogic;
import com.gourmetcaree.common.property.BaseProperty;
import com.gourmetcaree.common.property.PagerProperty;
import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.constants.DbFlgValue;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants.MailKbn;
import com.gourmetcaree.db.common.constants.MTypeConstants.MailStatus;
import com.gourmetcaree.db.common.constants.MTypeConstants.SendKbn;
import com.gourmetcaree.db.common.constants.MTypeConstants.SenderKbn;
import com.gourmetcaree.db.common.entity.TApplication;
import com.gourmetcaree.db.common.entity.TArbeitApplication;
import com.gourmetcaree.db.common.entity.TMail;
import com.gourmetcaree.db.common.entity.TObservateApplication;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.common.service.ApplicationService;
import com.gourmetcaree.db.common.service.ArbeitApplicationService;
import com.gourmetcaree.db.common.service.MailService;
import com.gourmetcaree.db.common.service.ObservateApplicationService;
import com.gourmetcaree.shop.logic.dto.ApplicationListRetDto;
import com.gourmetcaree.shop.logic.dto.ApplicationMailRetDto;
import com.gourmetcaree.shop.logic.dto.MailSelectDto;
import com.gourmetcaree.shop.logic.exception.NotExistMailException;
import com.gourmetcaree.shop.logic.mai.GourmetcareeMai;
import com.gourmetcaree.shop.logic.property.ApplicationProperty;
import com.gourmetcaree.shop.logic.property.MailSearchProperty;
import com.gourmetcaree.shop.logic.property.SendMailProperty;

import jp.co.whizz_tech.commons.WztJaStringUtil;
import jp.co.whizz_tech.commons.WztKatakanaUtil;
import jp.co.whizz_tech.commons.WztStringUtil;

/**
 * 応募に関するロジッククラスです。
 * @author Takahiro Ando
 *
 */
@Component(instance=InstanceType.SINGLETON)
public class ApplicationLogic extends AbstractShopLogic {

	/**
	 * 応募サービス
	 */
	@Resource
	ApplicationService applicationService;

	/**
	 * 店舗見学応募サービス
	 */
	@Resource
	private ObservateApplicationService observateApplicationService;

	/** アルバイト応募サービス */
	@Resource
	private ArbeitApplicationService arbeitApplicationService;

	/**
	 * メールサービス
	 */
	@Resource
	MailService mailService;

	/** 名称変換ロジック */
	@Resource
	ValueToNameConvertLogic valueToNameConvertLogic;

	/** メール送信ロジック */
	@Resource
	SendMailLogic sendMailLogic;

	/** メール送信 */
	@Resource
	protected GourmetcareeMai gourmetcareeMai;

	/** メール一覧の区分 */
	public enum MAIL_KBN {

		// 応募メール
		APPLICATION_MAIL(1),
		// スカウトメール
		SCOUT_MAIL(2),
		// 質問メール
		OBSERVATE_APPLICATION(3),
		// グルメdeバイト
		ARBEIT_APPLICATION(4),
		// プレ応募メール
		PRE_APPLICATION_MAIL(9),

		// 応募者リスト
		APPLICATION_MAIL_LIST(5),
		// スカウト者リスト
		SCOUT_MAIL_LIST(6),
		// 質問者リスト
		OBSERVATE_APPLICATION_LIST(7),
		// グルメdeバイト者リスト
		ARBEIT_APPLICATION_LIST(8),
		// プレ応募者リスト
		PRE_APPLICATION_MAIL_LIST(10);



		private int value;

		/**
		 * このクラスのオブジェクトを構築します。
		 * @param value 値
		 */
		private MAIL_KBN(int value) {
			this.value = value;
		}

		/**
		 * 値を返します。
		 * @return 値
		 */
		public int value() {
			return value;
		}


		public static MAIL_KBN getEnum(int value) {
			for (MAIL_KBN kbn : MAIL_KBN.values()) {
				if (kbn.value() == value) {
					return kbn;
				}
			}

			return APPLICATION_MAIL;
		}
	}


	/**
	 * 応募テーブルのデータを取得します。
	 * ログインユーザが操作可能なデータのみを取得します。
	 * 取得条件
	 * ・顧客IDにログインユーザが含まれていること
	 * ・いたずら応募でないこと
	 * ・応募日時が当日の１年前以上であること
	 * @param id
	 * @return
	 * @throws WNoResultException
	 */
	public TApplication findByIdFromApplication(int id) throws WNoResultException {

		try {
			//検索実行日の1年前の日付を取得
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.YEAR, -1);

			SimpleWhere where = new SimpleWhere()
								.eq(toCamelCase(TApplication.CUSTOMER_ID), getCustomerId())
								.eq(toCamelCase(TApplication.MISCHIEF_FLG), MTypeConstants.MischiefFlg.NORMAL)	// いたずら応募以外
								.ge(toCamelCase(TApplication.APPLICATION_DATETIME), cal.getTime());

			return applicationService.findById(id, where);
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}

	/**
	 * 応募テーブルのデータを取得します。
	 * ログインユーザが操作可能なデータのみを取得します。
	 * 取得条件
	 * ・顧客IDにログインユーザが含まれていること
	 * ・いたずら応募でないこと
	 * ・応募日時が当日の１年前以上であること
	 * @param id
	 * @return
	 * @throws WNoResultException
	 */
	public TObservateApplication findByIdFromObservateApplication(int id) throws WNoResultException {

		try {
			//検索実行日の1年前の日付を取得
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.YEAR, -1);

			SimpleWhere where = new SimpleWhere()
								.eq(toCamelCase(TObservateApplication.CUSTOMER_ID), getCustomerId())
								.eq(toCamelCase(TObservateApplication.MISCHIEF_FLG), MTypeConstants.MischiefFlg.NORMAL)	// いたずら応募以外
								.ge(toCamelCase(TObservateApplication.APPLICATION_DATETIME), cal.getTime());

			return observateApplicationService.findById(id, where);
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}


	/**
	 * アルバイト応募テーブルのデータを取得します。
	 * ログインユーザが操作可能なデータのみを取得します。
	 * 取得条件
	 * ・顧客IDにログインユーザが含まれていること
	 * ・いたずら応募でないこと
	 * ・応募日時が当日の１年前以上であること
	 * @param id アルバイト応募ID
	 * @return 該当するアルバイト応募エンティティ
	 * @throws WNoResultException 応募が見つからなかった場合にスロー
	 */
	public TArbeitApplication findByIdFromArbeitApplication(int id) throws WNoResultException {

		try {
			//検索実行日の1年前の日付を取得
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.YEAR, -1);

			SimpleWhere where = new SimpleWhere()
								.eq(toCamelCase(TArbeitApplication.CUSTOMER_ID), getCustomerId())
//								.eq(toCamelCase(TArbeitApplication.MISCHIEF_FLG), MTypeConstants.MischiefFlg.NORMAL)	// いたずら応募以外
								.ge(toCamelCase(TArbeitApplication.APPLICATION_DATETIME), cal.getTime());

			return arbeitApplicationService.findById(id, where);
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}



	/**
	 * メールテーブルのデータを取得します。
	 * ログインユーザが操作可能なデータのみを取得します。
	 * @param id
	 * @return
	 * @throws WNoResultException
	 */
	public TMail findByIdFromMail(int id) throws WNoResultException {


		try {
			TMail entity = mailService.findById(id);

			if (eqInt(MTypeConstants.SendKbn.RECEIVE, entity.sendKbn)
					&& (eqInt(SenderKbn.MEMBER, entity.senderKbn) || eqInt(SenderKbn.NO_MEMBER, entity.senderKbn))
					&& eqInt(getCustomerId(), entity.toId)) {
				return entity;
			} else if (eqInt(MTypeConstants.SendKbn.SEND, entity.sendKbn)
					&& eqInt(SenderKbn.CUSTOMER, entity.senderKbn)
					&& eqInt(getCustomerId(), entity.fromId)) {
				return entity;
			} else {
				throw new WNoResultException();
			}
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}

	/**
	 * 原稿IDで絞った応募者一覧用のデータを取得します。
	 * ウェブデータIDがNULLの場合は顧客IDのみの検索を行います。
	 * 取得するデータは内部でログインユーザの顧客IDを検索条件にセットしています。
	 * @param pageNavi
	 * @param webId
	 * @return
	 * @throws WNoResultException
	 */
	public ApplicationListRetDto getApplicationListData(PagerProperty property, Integer webId) throws WNoResultException {

		ApplicationListRetDto retDto = new ApplicationListRetDto();

		//検索実行日の1年前の日付を取得
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -1);
		try {
			Where where = new SimpleWhere()
			.eq(toCamelCase(TApplication.CUSTOMER_ID), getCustomerId())
			.eq(toCamelCase(TApplication.WEB_ID), webId)
			.eq(toCamelCase(TApplication.MISCHIEF_FLG), MTypeConstants.MischiefFlg.NORMAL)	// いたずら応募以外
			.eq(toCamelCase(TApplication.DELETE_FLG), DeleteFlgKbn.NOT_DELETED)
			.ge(toCamelCase(TApplication.APPLICATION_DATETIME), cal.getTime());

			long count = applicationService.countRecords(where);

			PageNavigateHelper pageNavi = new PageNavigateHelper(property.maxRow);
			pageNavi.sortKey = desc(toCamelCase(TApplication.APPLICATION_DATETIME)) + ", " + desc(toCamelCase(TApplication.ID));
			pageNavi.changeAllCount((int)count);
			pageNavi.setPage(property.targetPage);

			retDto.retList = applicationService.findByCondition(where, pageNavi);
			retDto.pageNavi = pageNavi;

			return retDto;
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}

	/**
	 * 原稿IDで絞った応募者一覧用のデータを取得します。(モバイル応募者一覧)
	 * ウェブデータIDがNULLの場合は顧客IDのみの検索を行います。
	 * 取得するデータは内部でログインユーザの顧客IDを検索条件にセットしています。
	 * @param pageNavi
	 * @param webId
	 * @return
	 * @throws WNoResultException
	 */
	public ApplicationListRetDto getApplicationListDataForMobile(PagerProperty property, Integer webId) throws WNoResultException {

		ApplicationListRetDto retDto = new ApplicationListRetDto();
		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>();

		//検索実行日の1年前の日付を取得
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -1);

		try {
			sql.append(" SELECT ");
			sql.append("   APP.* ");
			sql.append(" FROM ");

			sql.append("   t_application APP  ");

			sql.append(" WHERE ");
			sql.append(" APP.customer_id = ? ");
			sql.append("   AND APP.application_datetime > ? ");
			params.add(getCustomerId());
			params.add(cal.getTime());

			if (webId != null) {
				sql.append("   AND APP.web_id = ? ");
				params.add(webId);
			}

			sql.append("   AND APP.mischief_flg = ?  ");
			params.add(MTypeConstants.MischiefFlg.NORMAL);

			sql.append(" AND EXISTS (");
			sql.append(" SELECT * FROM t_mail MAIL WHERE ");

			sql.append("   APP.id = MAIL.application_id ");
			sql.append("   AND ( ");
			sql.append("     (MAIL.send_kbn = ? AND MAIL.sender_kbn = ?)  ");
			sql.append("     OR (MAIL.send_kbn = ? AND MAIL.sender_kbn IN (?, ?)) ");
			sql.append("   ) ");

			params.add(MTypeConstants.SendKbn.SEND);
			params.add(MTypeConstants.SenderKbn.CUSTOMER);
			params.add(MTypeConstants.SendKbn.RECEIVE);
			params.add(MTypeConstants.SenderKbn.MEMBER);
			params.add(MTypeConstants.SenderKbn.NO_MEMBER);

			sql.append(" AND MAIL.delete_flg = ? ");
			params.add(DeleteFlgKbn.NOT_DELETED);

			sql.append(")");


			long count = jdbcManager.getCountBySql(sql.toString(), params.toArray());

			PageNavigateHelper pageNavi = new PageNavigateHelper(property.maxRow);
			pageNavi.sortKey = desc(toCamelCase(TApplication.APPLICATION_DATETIME)) + ", " + desc(toCamelCase(TApplication.ID));
			pageNavi.changeAllCount((int)count);
			pageNavi.setPage(property.targetPage);

			retDto.pageNavi = pageNavi;

			retDto.retList = jdbcManager.selectBySql(TApplication.class, sql.toString(), params.toArray())
					.disallowNoResult()
					.limit(pageNavi.limit)
					.offset(pageNavi.offset)
					.getResultList();

			return retDto;
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}

	/**
	 * 応募者一覧用のデータを取得します。
	 * @param property ページャプロパティ
	 * @param searchProperty 検索プロパティ
	 * @return
	 * @throws WNoResultException
	 */
	public ApplicationListRetDto getApplicationListData(PagerProperty property, ApplicationSearchProperty searchProperty) throws WNoResultException {

		ApplicationListRetDto retDto = new ApplicationListRetDto();

		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>(0);

		createApplicationSearchSql(sql, params, searchProperty);

		long count = jdbcManager.getCountBySql(sql.toString(), params.toArray());

		if (count == 0l) {
			throw new WNoResultException("検索結果が0件です");
		}

		PageNavigateHelper pageNavi = new PageNavigateHelper(property.maxRow);
		pageNavi.sortKey = desc(dot("APP", TApplication.APPLICATION_DATETIME)) + ", " + desc(dot("APP", TApplication.ID));
		pageNavi.changeAllCount((int)count);
		pageNavi.setPage(property.targetPage);

		retDto.pageNavi = pageNavi;

		sql.append(" ORDER BY ").append(pageNavi.sortKey);

		try {
			retDto.retList = jdbcManager.selectBySql(TApplication.class, sql.toString(), params.toArray())
					.disallowNoResult()
					.limit(pageNavi.limit)
					.offset(pageNavi.offset)
					.getResultList();
		} catch (SNoResultException e) {
			throw new WNoResultException(e);
		}

		return retDto;


	}


	/**
	 * 応募メール検索SQLを作成します。
	 * @param sql SQL
	 * @param params パラメータ
	 * @param property 検索プロパティ
	 */
	private void createApplicationSearchSql(StringBuffer sql, List<Object> params, ApplicationSearchProperty property) {
		//検索実行日の1年前の日付を取得
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -1);



		sql.append(" SELECT ");
		sql.append("   APP.* ");
		sql.append(" FROM ");

		sql.append("   t_application APP  ");

		sql.append(" WHERE ");
		sql.append(" APP.customer_id = ? ");
		sql.append("   AND APP.application_datetime > ? ");
		params.add(getCustomerId());
			params.add(cal.getTime());

		if (property.webId != null) {
			sql.append("   AND APP.web_id = ? ");
			params.add(property.webId);
		}

		sql.append("   AND APP.mischief_flg = ?  ");
		params.add(MTypeConstants.MischiefFlg.NORMAL);



		addMailSearchSql(sql, params, property, "APP.id = MAIL.application_id");

		sql.append("   AND APP.delete_flg = ? ");

		params.add(DeleteFlgKbn.NOT_DELETED);

	}

	/**
	 * メール検索SQLを追加します。
	 * @param sql SQL
	 * @param params パラメータ
	 * @param property プロパティ
	 * @param idSentence ID結合文
	 */
	static void addMailSearchSql(StringBuffer sql, List<Object> params, ApplicationSearchProperty property, String idSentence) {


		sql.append(" AND EXISTS (");
		sql.append(" SELECT * FROM t_mail MAIL WHERE ");

		sql.append(idSentence);


		sql.append("   AND (  ");
		sql.append("     (MAIL.send_kbn = ? AND MAIL.sender_kbn = ?)  ");
		sql.append("     OR (MAIL.send_kbn = ? AND MAIL.sender_kbn IN (?, ?)) ");
		sql.append("   )  ");

		params.add(MTypeConstants.SendKbn.SEND);
		params.add(MTypeConstants.SenderKbn.CUSTOMER);
		params.add(MTypeConstants.SendKbn.RECEIVE);
		params.add(MTypeConstants.SenderKbn.MEMBER);
		params.add(MTypeConstants.SenderKbn.NO_MEMBER);

		if (StringUtils.isNotBlank(property.keyword)) {
			String[] word = property.keyword.split(" |　");
			for (String str : word) {
				if (StringUtils.isNotBlank(str)) {
					sql.append("   AND (MAIL.subject || ' ' || MAIL.body ILIKE ? OR MAIL.subject || ' ' || MAIL.body ILIKE ?) ");
					params.add(
							WztJaStringUtil.zenToHan(WztKatakanaUtil.zenkakuToHankaku(SqlUtils.containPercent(str)), WztJaStringUtil.ALL));
					params.add(
							WztJaStringUtil.zenToHan(WztKatakanaUtil.hankakuToZenkaku(SqlUtils.containPercent(str)), WztJaStringUtil.ALL));
				}
			}
		}

		sql.append(" AND MAIL.delete_flg = ? ");
		params.add(DeleteFlgKbn.NOT_DELETED);

		sql.append(")");
	}



	/**
	 * 応募者一覧用のデータを取得します。(モバイル応募者一覧)
	 * ウェブデータIDは特定せず、顧客に紐付く応募全てを取得します。
	 * 取得するデータは内部でログインユーザの顧客IDを検索条件にセットしています。
	 * @param pageNavi
	 * @return
	 * @throws WNoResultException
	 */
	public ApplicationListRetDto getApplicationListDataForMobile(PagerProperty property) throws WNoResultException {
		Integer nullId = null;
		return getApplicationListDataForMobile(property, nullId);
	}


	/**
	 * 応募者一覧用のデータを取得します。
	 * ウェブデータIDは特定せず、顧客に紐付く応募全てを取得します。
	 * 取得するデータは内部でログインユーザの顧客IDを検索条件にセットしています。
	 * @param pageNavi
	 * @return
	 * @throws WNoResultException
	 */
	public ApplicationListRetDto getApplicationListData(PagerProperty property) throws WNoResultException {
		Integer nullId = null;
		return getApplicationListData(property, nullId);
	}

	/**
	 * 応募者を特定した未読のメールがあるかどうかを取得する。
	 * TO_IDは自動的にログインユーザの顧客IDをセットしている。
	 * @param applicationId
	 * @return true:存在する、false:存在しない
	 */
	public boolean isUnopenedApplicantMailExist(int applicationId) {
		return mailService.isReceivedApplicationMailFromExist(applicationId, getCustomerId(), MTypeConstants.MailStatus.UNOPENED);
	}

	/**
	 * 応募者を特定した未読のメールがあるかどうかを取得する。
	 * TO_IDは自動的にログインユーザの顧客IDをセットしている。
	 * @param applicationId
	 * @param mailKbn メール区分
	 * @return true:存在する、false:存在しない
	 */
	public boolean isUnopenedApplicantMailExist(int applicationId, int mailKbn) {
		return mailService.isReceivedApplicationMailFromExist(applicationId, getCustomerId(), mailKbn, MTypeConstants.MailStatus.UNOPENED);
	}


	/**
	 * 原稿IDで絞った応募者一覧用のデータの件数を取得します。
	 * ウェブデータIDがNULLの場合は顧客IDのみの検索を行います。
	 * 取得するデータは内部でログインユーザの顧客IDを検索条件にセットしています。
	 * @param webId
	 * @return
	 */
	public int countApplicationListData(Integer webId) {

			Where where = new SimpleWhere()
								.eq(toCamelCase(TApplication.CUSTOMER_ID), getCustomerId())
								.eq(toCamelCase(TApplication.WEB_ID), webId)
								.eq(toCamelCase(TApplication.MISCHIEF_FLG), MTypeConstants.MischiefFlg.NORMAL)	// いたずら応募以外
								.eq(toCamelCase(TApplication.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

			return (int)applicationService.countRecords(where);
	}

	/**
	 * 応募者一覧用のデータの件数を取得します。
	 * 取得するデータは内部でログインユーザの顧客IDを検索条件にセットしています。
	 * @return
	 */
	public int countApplicationListData() {
		return countApplicationListData(null);
	}


	/**
	 * 応募メールの一覧用データを取得します。
	 * 取得するデータは内部でログインユーザの顧客IDを検索条件のTO_IDにセットしています。
	 * @param property
	 * @param sendKbn
	 * @return
	 * @throws WNoResultException
	 */
	public ApplicationMailRetDto getApplicationMailListData(PagerProperty property, int sendKbn, MailSearchProperty searchProperty) throws WNoResultException {

		ApplicationMailRetDto retDto = new ApplicationMailRetDto();

		try {

			StringBuilder sb = new StringBuilder("");
			List<Object> params = new ArrayList<Object>();
			createSql(sb, params, sendKbn, MailKbn.APPLICCATION, searchProperty);

			long count = jdbcManager.getCountBySql(sb.toString(), params.toArray());

			PageNavigateHelper pageNavi = new PageNavigateHelper(property.maxRow);
			pageNavi.sortKey = desc(toCamelCase(TMail.SEND_DATETIME)) + ", " + desc(toCamelCase(TMail.ID));
			pageNavi.changeAllCount((int)count);
			pageNavi.setPage(property.targetPage);

			setPage(sb, params, "send_datetime DESC", pageNavi.offset, property.maxRow);

			retDto.pageNavi = pageNavi;

			retDto.retList = jdbcManager
					.selectBySql(MailSelectDto.class, sb.toString(), params.toArray())
					.disallowNoResult()
					.getResultList();

			return retDto;
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}

	/**
	 * 未読の応募メール数を取得する
	 * @return
	 */
	public long getUnReadApplicationMailCount() {
		StringBuilder sb = new StringBuilder("");
		List<Object> params = new ArrayList<Object>();
		createUnReadMailSql(sb, params, MailKbn.APPLICCATION);
		return jdbcManager.getCountBySql(sb.toString(), params.toArray());
	}

	/**
	 * 未読の応募メールがあるか判定する
	 * @return
	 */
	public boolean checkUnReadApplicationMail() {
		return getUnReadApplicationMailCount() > 0;
	}

	/**
	 * 未読の質問メール数を取得する
	 * @return
	 */
	public long getUnReadObservateApplicationMailCount() {
		StringBuilder sb = new StringBuilder("");
		List<Object> params = new ArrayList<Object>();
		createUnReadMailSql(sb, params, MailKbn.OBSERVATE_APPLICATION);
		return jdbcManager.getCountBySql(sb.toString(), params.toArray());
	}

	/**
	 * 未読の質問メールがあるか判定する
	 * @return
	 */
	public boolean checkUnReadObservateApplicationMail() {
		return getUnReadObservateApplicationMailCount() > 0;
	}

	/**
	 * 未読のアルバイトメール数を取得する
	 * @return
	 */
	public long getUnReadArbeitMailCount() {
		StringBuilder sb = new StringBuilder("");
		List<Object> params = new ArrayList<Object>();
		createUnReadMailSql(sb, params, MailKbn.ARBEIT_APPLICATION);
		return jdbcManager.getCountBySql(sb.toString(), params.toArray());
	}

	/**
	 * 未読のアルバイトメールがあるか判定する
	 * @return
	 */
	public boolean checkUnReadArbeitMail() {
		return getUnReadArbeitMailCount() > 0;
	}


	/**
	 * 応募メールの一覧用データを取得します。
	 * 取得するデータは内部でログインユーザの顧客IDを検索条件のTO_IDにセットしています。
	 * @param property
	 * @param sendKbn
	 * @return
	 * @throws WNoResultException
	 */
	public ApplicationMailRetDto getObservateApplicationMailListData(PagerProperty property, int sendKbn, MailSearchProperty searchProperty) throws WNoResultException {

		ApplicationMailRetDto retDto = new ApplicationMailRetDto();

		try {

			StringBuilder sb = new StringBuilder("");
			List<Object> params = new ArrayList<Object>();
			createSql(sb, params, sendKbn, MailKbn.OBSERVATE_APPLICATION, searchProperty);

			long count = jdbcManager.getCountBySql(sb.toString(), params.toArray());

			PageNavigateHelper pageNavi = new PageNavigateHelper(property.maxRow);
			pageNavi.sortKey = desc(toCamelCase(TMail.SEND_DATETIME)) + ", " + desc(toCamelCase(TMail.ID));
			pageNavi.changeAllCount((int)count);
			pageNavi.setPage(property.targetPage);

			setPage(sb, params, "send_datetime DESC", pageNavi.offset, property.maxRow);

			retDto.retList = jdbcManager
								.selectBySql(MailSelectDto.class, sb.toString(), params.toArray())
								.disallowNoResult()
								.getResultList();
			retDto.pageNavi = pageNavi;

			return retDto;
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}


	/**
	 * 応募メールの一覧用データを取得します。
	 * 取得するデータは内部でログインユーザの顧客IDを検索条件のTO_IDにセットしています。
	 * @param property ページャプロパティ
	 * @param sendKbn 送信区分
	 * @param mailKbn メール区分
	 * @return {@link SqlSelect}と、 {@link PageNavigateHelper}を代入したDTO
	 */
	public ApplicationMailRetDto getApplicationMailSelect(PagerProperty property, int sendKbn, int mailKbn, MailSearchProperty searchProperty) {
		ApplicationMailRetDto retDto = new ApplicationMailRetDto();
		StringBuilder sb = new StringBuilder(0);
		List<Object> params = new ArrayList<Object>(0);

		createSql(sb, params, sendKbn, mailKbn, searchProperty);

		long count = jdbcManager.getCountBySql(sb.toString(), params.toArray());

		PageNavigateHelper pageNavi = new PageNavigateHelper(property.maxRow);
		pageNavi.sortKey = desc(toCamelCase(TMail.SEND_DATETIME)) + ", " + desc(toCamelCase(TMail.ID));
		pageNavi.changeAllCount((int)count);
		pageNavi.setPage(property.targetPage);

		if (count == 0l) {
			return retDto;
		}

		setPage(sb, params, "send_datetime DESC", pageNavi.offset, property.maxRow);

		retDto.retList = jdbcManager
				.selectBySql(MailSelectDto.class, sb.toString(), params.toArray())
				.disallowNoResult()
				.getResultList();

		retDto.pageNavi = pageNavi;

		return retDto;


	}

	/**
	 * 未読の応募メールを既読に変更する処理
	 * 取得するデータは内部でログインユーザの顧客IDを検索条件のTO_IDにセットしています。
	 * @param mailId
	 * @throws WNoResultException
	 */
	public void changeMailToOpened(int mailId) throws WNoResultException  {
		mailService.changeMailToOpened(mailId, getCustomerId(), MTypeConstants.MailKbn.APPLICCATION);
	}

	/**
	 * 既読の応募メールを未読に変更する処理
	 * 取得するデータは内部でログインユーザの顧客IDを検索条件のTO_IDにセットしています。
	 * @param mailId
	 * @throws WNoResultException
	 */
	public void changeMailToUnOpened(int mailId) throws WNoResultException  {
		mailService.changeMailToUnOpened(mailId, getCustomerId(), MTypeConstants.MailKbn.APPLICCATION);
	}

	/**
	 * 応募メールを一覧から見えないようにする処理
	 * @param mailIds
	 */
	public void changeMailToUnDisplay(List<Integer> mailIds) {
		mailService.changeApplicationMailToUnDisplay(mailIds, getCustomerId());
	}

	/**
	 * 未読の質問メールを既読に変更する処理
	 * 取得するデータは内部でログインユーザの顧客IDを検索条件のTO_IDにセットしています。
	 * @param mailId
	 * @throws WNoResultException
	 */
	public void changeObservationMailToOpened(int mailId) throws WNoResultException  {
		mailService.changeMailToOpened(mailId, getCustomerId(), MTypeConstants.MailKbn.OBSERVATE_APPLICATION);
	}

	/**
	 * 既読の質問メールを未読に変更する処理
	 * 取得するデータは内部でログインユーザの顧客IDを検索条件のTO_IDにセットしています。
	 * @param mailId
	 * @throws WNoResultException
	 */
	public void changeObservationMailToUnOpened(int mailId) throws WNoResultException  {
		mailService.changeMailToUnOpened(mailId, getCustomerId(), MTypeConstants.MailKbn.OBSERVATE_APPLICATION);
	}

	/**
	 * 質問メールを一覧から見えないようにする処理
	 * @param mailIds
	 */
	public void changeObservationMailToUnDisplay(List<Integer> mailIds) {
		mailService.changeObservateApplicationMailToUnDisplay(mailIds, getCustomerId());
	}

	/**
	 * 未読のグルメdeバイト応募メールを既読に変更する処理
	 * 取得するデータは内部でログインユーザの顧客IDを検索条件のTO_IDにセットしています。
	 * @param mailId
	 * @throws WNoResultException
	 */
	public void changeArbeitMailToOpened(int mailId) throws WNoResultException  {
		mailService.changeMailToOpened(mailId, getCustomerId(), MTypeConstants.MailKbn.ARBEIT_APPLICATION);
	}

	/**
	 * 既読のグルメdeバイト応募メールを未読に変更する処理
	 * 取得するデータは内部でログインユーザの顧客IDを検索条件のTO_IDにセットしています。
	 * @param mailId
	 * @throws WNoResultException
	 */
	public void changeArbeitMailToUnOpened(int mailId) throws WNoResultException  {
		mailService.changeMailToUnOpened(mailId, getCustomerId(), MTypeConstants.MailKbn.ARBEIT_APPLICATION);
	}

	/**
	 * 未読の応募メールを既読に変更する処理
	 * 取得するデータは内部でログインユーザの顧客IDを検索条件のTO_IDにセットしています。
	 * @param mailId
	 * @throws WNoResultException
	 */
	public void changeMailToOpened(int mailId, int mailKbn) throws WNoResultException  {
		mailService.changeMailToOpened(mailId, getCustomerId(), mailKbn);
	}

	/**
	 * 応募テスト確認完了のメールを担当者へ送信します。
	 * @param property 応募プロパティ
	 */
	public void sendAppTestConfMail(ApplicationProperty property) {

		// 引数チェック
		checkEmptyProperty(property);
		if (property.tApplicationTest == null) {
			throw new IllegalArgumentException("引数がnullです。");
		}

		// メールプロパティに値をセット
		SendMailProperty mailProperty = new SendMailProperty();
		mailProperty.tApplicationTest = property.tApplicationTest;

		try {
			// 顧客名を取得
			mailProperty.customerName = valueToNameConvertLogic.convertToCustomerName(new String[]{String.valueOf(property.tApplicationTest.customerId)});

		} catch (WNoResultException e) {
			// 顧客が削除されている場合はメッセージの顧客名に「削除されている顧客の可能性があります」をセット
			mailProperty.customerName = sendMailLogic.getMailProerties().getProperty("errors.delCustomer");

		} catch (NumberFormatException e) {
			// 顧客IDが不正な場合(通常ありえない)は、メッセージの顧客名に「削除されている顧客の可能性があります」をセット
			mailProperty.customerName =sendMailLogic.getMailProerties().getProperty("errors.delCustomer");
		}

		// メール送信処理実行
		sendMailLogic.sendAppTestConfMail(mailProperty);
	}

	/**
	 * プロパティがnullの場合は、エラーを返す。
	 * @param property チェックするプロパティ
	 */
	private void checkEmptyProperty(ApplicationProperty property) {

		// プロパティがnullの場合はエラー
		if (property == null) {
			throw new IllegalArgumentException("引数がnullです。");
		}
	}

	/**
	 * 応募から最初に送信されたメールのIDを取得します。
	 * @param applicationId
	 * @return
	 * @throws WNoResultException
	 */
	public int getFirstApplicationMailId(int applicationId) throws WNoResultException {
		TMail entity = mailService.getFirstApplicationMailToCustomer(applicationId, getCustomerId(), MTypeConstants.MailKbn.APPLICCATION);
		return entity.id;
	}

	/**
	 * メール検索SQLを作成します
	 * @param sb
	 * @param params
	 * @param sendKbn
	 * @param mailKbn
	 */
	public void createSql(StringBuilder sb, List<Object> params, int sendKbn, int mailKbn, MailSearchProperty searchProperty) {

		// 受信箱の場合
		if (sendKbn == MTypeConstants.SendKbn.RECEIVE) {
			sb.append("SELECT * FROM(");
			sb.append("SELECT ");
			if(mailKbn == MailKbn.APPLICCATION) {
				sb.append("    DISTINCT ON (MAIL.application_id) ");
			} else if(mailKbn == MailKbn.OBSERVATE_APPLICATION) {
				sb.append("    DISTINCT ON (MAIL.observate_application_id) ");
			} else if (mailKbn == MailKbn.ARBEIT_APPLICATION) {
				sb.append("    DISTINCT ON (MAIL.arbeit_application_id) ");
			}
			sb.append("    MAIL.id, ");
			sb.append("    MAIL.mail_kbn, ");
			sb.append("    MAIL.send_kbn, ");
			sb.append("    MAIL.sender_kbn, ");
			sb.append("    MAIL.from_id, ");
			sb.append("    MAIL.from_name, ");
			sb.append("    MAIL.to_id, ");
			sb.append("    MAIL.to_name, ");
			sb.append("    MAIL.subject, ");
			sb.append("    MAIL.body, ");
			sb.append("    MAIL.arbeit_application_id, ");
			sb.append("    MAIL.application_id, ");
			sb.append("    MAIL.observate_application_id, ");
			sb.append("    MAIL.scout_history_id, ");
			sb.append("    MAIL.mail_status, ");
			sb.append("    MAIL.send_datetime, ");
			sb.append("    MAIL.reading_datetime, ");
			sb.append("    MAIL.parent_mail_id, ");
			sb.append("    MAIL.access_cd, ");
			sb.append("    MAIL_R.reading_datetime AS receive_reading_datetime, ");
			sb.append("    APP.area_cd,");
			sb.append("    APP.application_name,");
			if(mailKbn != MailKbn.ARBEIT_APPLICATION) {
				sb.append("    APP.birthday, ");
				sb.append("    APP.member_id,");
			}
			if(mailKbn == MailKbn.APPLICCATION) {
				sb.append("    APP.employ_ptn_kbn, ");
				sb.append("    APP.job_kbn, ");
			} else if (mailKbn == MailKbn.ARBEIT_APPLICATION) {
				sb.append("    APP.application_job, ");
				sb.append("    APP.age, ");
			}
			sb.append("    APP.selection_flg, ");
			if(mailKbn == MailKbn.APPLICCATION) {
				sb.append("    APP.memo, ");
				sb.append("    APP.shop_list_id ");
			} else {
				sb.append("    APP.memo ");
			}
			sb.append("FROM ");
			sb.append(" t_mail MAIL ");
			sb.append("    LEFT OUTER JOIN t_mail MAIL_R ");
			sb.append("        ON MAIL.receive_id = MAIL_R.id ");
			if(mailKbn == MailKbn.APPLICCATION) {
				sb.append("    LEFT OUTER JOIN t_application APP ");
				sb.append("        ON MAIL.application_id = APP.id ");
			} else if(mailKbn == MailKbn.OBSERVATE_APPLICATION) {
				sb.append("    LEFT OUTER JOIN t_observate_application APP ");
				sb.append("        ON MAIL.observate_application_id = APP.id ");
			} else if(mailKbn == MailKbn.ARBEIT_APPLICATION) {
				sb.append("    LEFT OUTER JOIN t_arbeit_application APP ");
				sb.append("        ON MAIL.arbeit_application_id = APP.id ");
			}
			sb.append("WHERE ");

			sb.append("    MAIL.to_id = ? ");
			params.add(getCustomerId());

			sb.append("    AND MAIL.send_kbn = ?");
			sb.append("    AND MAIL.mail_kbn = ?");

			sb.append("    AND MAIL.sender_kbn in ( ?");
			sb.append(", ?");
			sb.append(")");
			sb.append("    AND MAIL.delete_flg = ?");
			sb.append("    AND MAIL.display_flg = ?");

			params.add(SendKbn.RECEIVE);
			params.add(mailKbn);
			params.add(SenderKbn.MEMBER);
			params.add(SenderKbn.NO_MEMBER);
			params.add(DeleteFlgKbn.NOT_DELETED);
			params.add(DbFlgValue.TRUE);

			if(searchProperty.where_areaCd.size() > 0) {
				sb.append(SqlUtils.andInNoCamelize("APP.area_cd", searchProperty.where_areaCd.size()));
				params.addAll(searchProperty.where_areaCd);
			}

			if(StringUtils.isNotBlank(searchProperty.where_jobKbn)) {
				sb.append("    AND APP.job_kbn  = ?");
				params.add(Integer.parseInt(searchProperty.where_jobKbn));
			}

			if(StringUtils.isNotBlank(searchProperty.where_employPtnKbn)) {
				sb.append("    AND APP.employ_ptn_kbn  = ?");
				params.add(Integer.parseInt(searchProperty.where_employPtnKbn));
			}

			if(StringUtils.isNotBlank(searchProperty.where_selectionKbn)) {
				sb.append("    AND APP.selection_flg  = ?");
				params.add(Integer.parseInt(searchProperty.where_selectionKbn));
			}

			if(StringUtils.isNotBlank(searchProperty.where_webId)) {
				sb.append("    AND APP.web_id  = ?");
				params.add(Integer.parseInt(searchProperty.where_webId));
			}

			// 1年以内のメールのみを対象
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.YEAR, -1);
			sb.append(" AND MAIL.send_datetime >= ? ");
			params.add(cal.getTime());

			if(mailKbn == MailKbn.APPLICCATION) {
				sb.append(" ORDER BY MAIL.application_id, MAIL.send_datetime DESC");
			} else if(mailKbn == MailKbn.OBSERVATE_APPLICATION) {
				sb.append(" ORDER BY MAIL.observate_application_id, MAIL.send_datetime DESC");
			} else if (mailKbn == MailKbn.ARBEIT_APPLICATION) {
				sb.append(" ORDER BY MAIL.arbeit_application_id, MAIL.send_datetime DESC");
			}

			sb.append(" ) SUB");

			if(searchProperty.where_mailStatus.size() > 0 || StringUtils.isNotBlank(searchProperty.where_keyword)) {
				sb.append(" WHERE ");
			}

			if(searchProperty.where_mailStatus.size() > 0) {
				sb.append(SqlUtils.inNoCamelize("mail_status", searchProperty.where_mailStatus.size()));
				params.addAll(searchProperty.where_mailStatus);
			}

			if(StringUtils.isNotBlank(searchProperty.where_keyword)) {
				Pattern pattern = Pattern.compile(GourmetCareeConstants.ZENBUN_BAD_WORD);
				Matcher matcher = pattern.matcher(searchProperty.where_keyword);
				String keyword = WztStringUtil.trim(
									matcher.replaceAll(searchProperty.where_keyword)
											.replaceAll("　", " "));
				if(searchProperty.where_mailStatus.size() > 0) {
					sb.append(" AND (");
				} else {
					sb.append(" (");
				}
				sb.append(SqlUtils.createLikeSearch(
						keyword,
						params,
						" from_name LIKE ? "));

				sb.append("  OR ");
				sb.append(SqlUtils.createLikeSearch(
						keyword,
						params,
						" subject LIKE ? "));

				sb.append("  OR ");
				sb.append(SqlUtils.createLikeSearch(
						keyword,
						params,
						" body LIKE ? "));

				sb.append(" ) ");
			}

		} else {
			sb.append("SELECT ");
			sb.append("    MAIL.id, ");
			sb.append("    MAIL.mail_kbn, ");
			sb.append("    MAIL.send_kbn, ");
			sb.append("    MAIL.sender_kbn, ");
			sb.append("    MAIL.from_id, ");
			sb.append("    MAIL.from_name, ");
			sb.append("    MAIL.to_id, ");
			sb.append("    MAIL.to_name, ");
			sb.append("    MAIL.subject, ");
			sb.append("    MAIL.body, ");
			sb.append("    MAIL.arbeit_application_id, ");
			sb.append("    MAIL.application_id, ");
			sb.append("    MAIL.observate_application_id, ");
			sb.append("    MAIL.scout_history_id, ");
			sb.append("    MAIL.mail_status, ");
			sb.append("    MAIL.send_datetime, ");
			sb.append("    MAIL.reading_datetime, ");
			sb.append("    MAIL.parent_mail_id, ");
			sb.append("    MAIL.access_cd, ");
			sb.append("    MAIL_R.reading_datetime AS receive_reading_datetime, ");
			sb.append("    APP.area_cd ");
			sb.append("FROM ");
			sb.append(" t_mail MAIL ");
			sb.append("    LEFT OUTER JOIN t_mail MAIL_R ");
			sb.append("        ON MAIL.receive_id = MAIL_R.id ");
			sb.append("    LEFT OUTER JOIN t_application APP ");
			sb.append("        ON MAIL.application_id = APP.id ");
			sb.append("WHERE ");
			sb.append("    MAIL.from_id = ? ");
			params.add(getCustomerId());

			sb.append("    AND MAIL.send_kbn = ?");
			sb.append("    AND MAIL.mail_kbn = ?");

			sb.append("    AND MAIL.sender_kbn in ( ?");
			sb.append(")");
			sb.append("    AND MAIL.delete_flg = ?");

			params.add(SendKbn.SEND);
			params.add(mailKbn);
			params.add(SenderKbn.CUSTOMER);
			params.add(DeleteFlgKbn.NOT_DELETED);

			// 1年以内のメールのみを対象
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.YEAR, -1);
			sb.append(" AND MAIL.send_datetime >= ? ");
			params.add(cal.getTime());
		}
	}

	/**
	 * 未読のメールを取得する
	 * @param sb
	 * @param params
	 * @param mailKbn
	 */
	public void createUnReadMailSql(StringBuilder sb, List<Object> params, int mailKbn) {
		sb.append("SELECT * FROM(");
		sb.append("SELECT ");
		if(mailKbn == MailKbn.APPLICCATION) {
			sb.append("    DISTINCT ON (MAIL.application_id) ");
			sb.append("    MAIL.application_id ");
		} else if(mailKbn == MailKbn.OBSERVATE_APPLICATION) {
			sb.append("    DISTINCT ON (MAIL.observate_application_id) ");
			sb.append("    MAIL.observate_application_id ");
		} else if (mailKbn == MailKbn.ARBEIT_APPLICATION) {
			sb.append("    DISTINCT ON (MAIL.arbeit_application_id) ");
			sb.append("    MAIL.arbeit_application_id ");
		}
		sb.append("FROM ");
		sb.append(" t_mail MAIL ");
		sb.append("WHERE ");
		sb.append("    MAIL.to_id = ? ");
		params.add(getCustomerId());
		sb.append("    AND MAIL.send_kbn = ?");
		sb.append("    AND MAIL.mail_kbn = ?");
		sb.append("    AND MAIL.sender_kbn in ( ?");
		sb.append(", ?");
		sb.append(")");
		sb.append("    AND MAIL.mail_status = ?");
		sb.append("    AND MAIL.delete_flg = ?");

			params.add(SendKbn.RECEIVE);
			params.add(mailKbn);
			params.add(SenderKbn.MEMBER);
			params.add(SenderKbn.NO_MEMBER);
			params.add(MailStatus.UNOPENED);
			params.add(DeleteFlgKbn.NOT_DELETED);

		// 1年以内のメールのみを対象
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -1);
		sb.append(" AND MAIL.send_datetime >= ? ");
		params.add(cal.getTime());

		if(mailKbn == MailKbn.APPLICCATION) {
			sb.append(" ORDER BY MAIL.application_id, MAIL.send_datetime DESC");
		} else if(mailKbn == MailKbn.OBSERVATE_APPLICATION) {
			sb.append(" ORDER BY MAIL.observate_application_id, MAIL.send_datetime DESC");
		} else if (mailKbn == MailKbn.ARBEIT_APPLICATION) {
			sb.append(" ORDER BY MAIL.arbeit_application_id, MAIL.send_datetime DESC");
		}

		sb.append(" ) SUB");
	}

	/**
	 * ページをセット
	 * @param sb
	 * @param params
	 * @param sortKey
	 * @param offset
	 * @param limit
	 */
	public void setPage(StringBuilder sb, List<Object> params, String sortKey, Integer offset, Integer limit) {
		if (StringUtils.isNotBlank(sortKey)) {
			sb.append("    ORDER BY ");
			sb.append(sortKey);
		}

		if (offset != null) {
			sb.append("    OFFSET ? ");
			params.add(offset);
		}

		if (limit != null) {
			sb.append("    LIMIT ? ");
			params.add(limit);
		}

	}


	/**
	 * メール検索SQLを作成します
	 * @param sb
	 * @param params
	 * @param sendKbn
	 * @param mailKbn
	 */
	public void createSqlRecive(StringBuilder sb, List<Object> params, int id, MAIL_KBN kbn) {

		sb.append("SELECT ");
		sb.append("    MAIL.id, ");
		sb.append("    MAIL.mail_kbn, ");
		sb.append("    MAIL.send_kbn, ");
		sb.append("    MAIL.sender_kbn, ");
		sb.append("    MAIL.from_id, ");
		sb.append("    MAIL.from_name, ");
		sb.append("    MAIL.to_id, ");
		sb.append("    MAIL.to_name, ");
		sb.append("    MAIL.subject, ");
		sb.append("    MAIL.body, ");
		sb.append("    MAIL.application_id, ");
		sb.append("    MAIL.observate_application_id, ");
		sb.append("    MAIL.scout_history_id, ");
		sb.append("    MAIL.mail_status, ");
		sb.append("    MAIL.send_datetime, ");
		sb.append("    MAIL.reading_datetime, ");
		sb.append("    MAIL.parent_mail_id, ");
		sb.append("    MAIL.access_cd, ");
		sb.append("    MAIL_R.reading_datetime AS receive_reading_datetime ");
		sb.append("FROM ");
		sb.append(" t_mail MAIL ");
		sb.append("    LEFT OUTER JOIN t_mail MAIL_R ");
		sb.append("        ON MAIL.receive_id = MAIL_R.id ");
		sb.append("WHERE ");

		// 応募
		switch (kbn) {
		case APPLICATION_MAIL:
			sb.append(" MAIL.application_id = ? ");
			params.add(id);
			break;
		// スカウト
		case SCOUT_MAIL:
			sb.append(" MAIL.scout_mail_log_id = ? ");
			params.add(id);
			break;
		// 質問
		case OBSERVATE_APPLICATION:
			sb.append(" MAIL.observate_application_id = ? ");
			params.add(id);
			break;
		// グルメdeバイト
		case ARBEIT_APPLICATION:
			sb.append(" MAIL.arbeit_application_id = ? ");
			params.add(id);
			break;
		default:
			throw new FraudulentProcessException(" メール区分がNULLです。 ");
		}

		sb.append("    AND MAIL.to_id = ? ");
		params.add(getCustomerId());

		sb.append("    AND MAIL.send_kbn = ?");
		sb.append("    AND MAIL.mail_kbn = ?");
		sb.append("    AND MAIL.sender_kbn in ( ?, ? )");
		sb.append("    AND MAIL.delete_flg = ?");

		params.add(SendKbn.RECEIVE);
		params.add(kbn.value);
		params.add(SenderKbn.MEMBER);
		params.add(SenderKbn.NO_MEMBER);
		params.add(DeleteFlgKbn.NOT_DELETED);

		// 1年以内のメールのみを対象
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -1);
		sb.append(" AND MAIL.send_datetime >= ? ");
		params.add(cal.getTime());
	}

	/**
	 * メールの送受信を取得する
	 * @param id t_mailに紐づくID
	 * @param kbn 対象のメール区分（1:応募、2:スカウト、3:質問、4:グルメdeバイト）
	 * @return
	 */
	public ApplicationMailRetDto getApplicationIdEachMail(PagerProperty property, int id, MAIL_KBN kbn) {

		ApplicationMailRetDto retDto = new ApplicationMailRetDto();
		StringBuilder sb = new StringBuilder(0);
		List<Object> params = new ArrayList<Object>();

		createApplicationIdEachMail(sb, params, id, kbn);

		long count = jdbcManager.getCountBySql(sb.toString(), params.toArray());

		PageNavigateHelper pageNavi = new PageNavigateHelper(property.maxRow);
		pageNavi.sortKey = asc(toCamelCase(TMail.SEND_DATETIME)) + ", " + asc(toCamelCase(TMail.ID));
		pageNavi.changeAllCount((int)count);
		pageNavi.setPage(property.targetPage);

		if (count == 0l) {
			return retDto;
		}

		setPage(sb, params, "MAIL.send_datetime ASC", pageNavi.offset, property.maxRow);

		retDto.retList = jdbcManager.selectBySql(MailSelectDto.class, sb.toString(), params.toArray()).getResultList();

		retDto.pageNavi = pageNavi;

		return retDto;
	}

	/**
	 * 応募者毎の送受信のメールを取得するSQLを作成
	 * @param sb SQL
	 * @param params パラメータ
	 * @param id ID
	 * @param kbn 区分
	 */
	private void createApplicationIdEachMail(StringBuilder sb, List<Object> params, int id, MAIL_KBN kbn) {

		sb.append(" SELECT *, ");
		sb.append(" (SELECT reading_datetime FROM t_mail WHERE id =  MAIL.receive_id) AS receive_reading_datetime ");
		sb.append(" FROM ");
		sb.append(" t_mail MAIL ");
		sb.append(" WHERE ");
		// 応募
		switch (kbn) {
		case APPLICATION_MAIL:
			sb.append(" MAIL.application_id = ? AND ");
			params.add(id);
			sb.append(" MAIL.application_id IS NOT NULL AND ");
			break;
		case PRE_APPLICATION_MAIL:
			sb.append(" MAIL.application_id = ? AND ");
			params.add(id);
			break;
		// スカウト
		case SCOUT_MAIL:
			sb.append(" MAIL.scout_mail_log_id = ? AND ");
			params.add(id);
			break;
		// 質問
		case OBSERVATE_APPLICATION:
			sb.append(" MAIL.observate_application_id = ? AND ");
			params.add(id);
			break;
		// グルメdeバイト
		case ARBEIT_APPLICATION:
			sb.append(" MAIL.arbeit_application_id = ? AND ");
			params.add(id);
			break;
		default:
			throw new FraudulentProcessException(" メール区分がNULLです。 ");
		}

		sb.append(" ((MAIL.send_kbn = ? AND MAIL.sender_kbn = ?) OR (MAIL.send_kbn = ? AND MAIL.sender_kbn in (?, ?))) ");
		params.add(MTypeConstants.SendKbn.SEND);
		params.add(MTypeConstants.SenderKbn.CUSTOMER);
		params.add(MTypeConstants.SendKbn.RECEIVE);
		params.add(MTypeConstants.SenderKbn.MEMBER);
		params.add(MTypeConstants.SenderKbn.NO_MEMBER);

		sb.append(" AND MAIL.delete_flg = ?");
		params.add(DeleteFlgKbn.NOT_DELETED);

		// 1年以内のメールのみを対象
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -1);
		sb.append(" AND send_datetime >= ? ");
		params.add(cal.getTime());
	}




	/**
	 * 返信メールを取得する
	 * @param applicationIdArray 応募ID配列
	 * @param kbn メール区分
	 * @return
	 * @throws NotExistMailException
	 */
	public List<Integer> getApplicationMail(int[] applicationIdArray, MAIL_KBN kbn) throws NotExistMailException {

		// 最終メールが送信メールか受信メールかチェックする
		StringBuilder sb;
		List<Object> params;

		List<Integer> mailIdList = new ArrayList<Integer>();

		/** 削除IDリスト */
		List<Integer> deleteIdList = new ArrayList<Integer>(0);
		for (int id : applicationIdArray) {

			sb = new StringBuilder(0);
			params = new ArrayList<Object>();

			createSqlRecive(sb, params, id, kbn);
			sb.append(" ORDER BY send_datetime DESC ").append(" OFFSET 0 ").append(" LIMIT 1 ");
			TMail tMail = jdbcManager.selectBySql(TMail.class, sb.toString(), params.toArray()).getSingleResult();

			if (tMail == null) {
				deleteIdList.add(id);
				continue;
			}

			// メールIDを取得
			mailIdList.add(tMail.id);
		}

		if (CollectionUtils.isNotEmpty(deleteIdList)) {
			throw new NotExistMailException(deleteIdList);
		}

		return mailIdList;
	}


	/**
	 * 応募検索プロパティ
	 * @author Takehiro Nakamori
	 *
	 */
	public static class ApplicationSearchProperty extends BaseProperty {

		/**
		 *
		 */
		private static final long serialVersionUID = 7669775466178438518L;

		/** WEB ID */
		public Integer webId;

		/** キーワード */
		public String keyword;

		/** メール区分 */
		public MAIL_KBN mailKbn;

	}
}

