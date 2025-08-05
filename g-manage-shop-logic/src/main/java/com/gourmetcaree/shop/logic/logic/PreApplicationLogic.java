package com.gourmetcaree.shop.logic.logic;

import static com.gourmetcaree.common.util.SqlUtils.*;
import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.property.PagerProperty;
import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.constants.DbFlgValue;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants.MailKbn;
import com.gourmetcaree.db.common.constants.MTypeConstants.MailStatus;
import com.gourmetcaree.db.common.constants.MTypeConstants.SendKbn;
import com.gourmetcaree.db.common.constants.MTypeConstants.SenderKbn;
import com.gourmetcaree.db.common.entity.TApplication;
import com.gourmetcaree.db.common.entity.TMail;
import com.gourmetcaree.db.common.entity.TPreApplication;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.common.service.PreApplicationService;
import com.gourmetcaree.shop.logic.dto.MailSelectDto;
import com.gourmetcaree.shop.logic.dto.PreApplicationListRetDto;
import com.gourmetcaree.shop.logic.dto.PreApplicationMailRetDto;
import com.gourmetcaree.shop.logic.property.MailSearchProperty;

import jp.co.whizz_tech.commons.WztStringUtil;

/**
 * プレ応募に関するロジッククラスです。
 *
 */
@Component(instance=InstanceType.SINGLETON)
public class PreApplicationLogic extends ApplicationLogic {

	/**
	 * プレ応募サービス
	 */
	@Resource
	PreApplicationService preApplicationService;

	/**
	 * プレ応募メールの一覧用データを取得します。
	 * 取得するデータは内部でログインユーザの顧客IDを検索条件のTO_IDにセットしています。
	 * @param property
	 * @param sendKbn
	 * @return
	 * @throws WNoResultException
	 */
	public PreApplicationMailRetDto getPreApplicationMailListData(PagerProperty property, int sendKbn, MailSearchProperty searchProperty) throws WNoResultException {

		PreApplicationMailRetDto retDto = new PreApplicationMailRetDto();

		try {

			StringBuilder sb = new StringBuilder("");
			List<Object> params = new ArrayList<Object>();
			createSql(sb, params, sendKbn, MailKbn.PRE_APPLICCATION, searchProperty);

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
	 * プレ応募テーブルのデータを取得します。
	 * ログインユーザが操作可能なデータのみを取得します。
	 * 取得条件
	 * ・顧客IDにログインユーザが含まれていること
	 * ・応募日時が当日の１年前以上であること
	 * @param id
	 * @return
	 * @throws WNoResultException
	 */
	public TPreApplication findByIdFromPreApplication(int id) throws WNoResultException {

		try {
			//検索実行日の1年前の日付を取得
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.YEAR, -1);

			SimpleWhere where = new SimpleWhere()
								.eq(toCamelCase(TPreApplication.CUSTOMER_ID), getCustomerId())
								.ge(toCamelCase(TPreApplication.APPLICATION_DATETIME), cal.getTime());

			return preApplicationService.findById(id, where);
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}

	/**
	 * 未読のプレ応募メールを既読に変更する処理
	 * 取得するデータは内部でログインユーザの顧客IDを検索条件のTO_IDにセットしています。
	 * @param mailId
	 * @throws WNoResultException
	 */
	public void changeMailToOpened(int mailId) throws WNoResultException  {
		mailService.changeMailToOpened(mailId, getCustomerId(), MTypeConstants.MailKbn.PRE_APPLICCATION);
	}
	/**
	 * 既読の応募メールを未読に変更する処理
	 * 取得するデータは内部でログインユーザの顧客IDを検索条件のTO_IDにセットしています。
	 * @param mailId
	 * @throws WNoResultException
	 */
	public void changeMailToUnOpened(int mailId) throws WNoResultException  {
		mailService.changeMailToUnOpened(mailId, getCustomerId(), MTypeConstants.MailKbn.PRE_APPLICCATION);
	}

	/**
	 * プレ応募メールを一覧から見えなくする処理
	 * @param mailId
	 * @throws WNoResultException
	 */
	public void changeMailToUnDisplay(List<Integer> mailIds) {
		mailService.changePreApplicationMailToUnDisplay(mailIds, getCustomerId());
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
			sb.append("    DISTINCT ON (MAIL.application_id) ");
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
			sb.append("    APP.selection_flg, ");
			sb.append("    APP.memo, ");
			sb.append("    APP.age, ");
			sb.append("    APP.employ_ptn_kbn, ");
			sb.append("    APP.job_kbn, ");
			sb.append("    APP.food_exp_kbn, ");
			sb.append("    APP.member_id ");
			sb.append("FROM ");
			sb.append(" t_mail MAIL ");
			sb.append("    LEFT OUTER JOIN t_mail MAIL_R ");
			sb.append("        ON MAIL.receive_id = MAIL_R.id ");
			sb.append("    LEFT OUTER JOIN t_pre_application APP ");
			sb.append("        ON MAIL.application_id = APP.id ");
			sb.append("WHERE ");
			sb.append("    MAIL.to_id = ? ");
			params.add(getCustomerId());

			sb.append("    AND MAIL.send_kbn = ?");
			sb.append("    AND MAIL.mail_kbn = ?");
			sb.append("    AND MAIL.sender_kbn = ?");
			sb.append("    AND MAIL.delete_flg = ?");
			sb.append("    AND MAIL.display_flg = ?");

			params.add(SendKbn.RECEIVE);
			params.add(mailKbn);
			params.add(SenderKbn.MEMBER);
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

			sb.append(" ORDER BY MAIL.application_id, MAIL.send_datetime DESC");
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
			sb.append("    APP.area_cd, ");
			sb.append("    APP.member_id ");
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
	 * 未読のプレ応募メールを取得する
	 * @param sb
	 * @param params
	 */
	public void createUnReadMailSql(StringBuilder sb, List<Object> params) {
		sb.append("SELECT * FROM(");
		sb.append("SELECT ");
		sb.append("    DISTINCT ON (MAIL.application_id) ");
		sb.append("    MAIL.application_id ");
		sb.append("FROM ");
		sb.append(" t_mail MAIL ");
		sb.append("WHERE ");
		sb.append("    MAIL.to_id = ? ");

		params.add(getCustomerId());

		sb.append("    AND MAIL.send_kbn = ?");
		sb.append("    AND MAIL.mail_kbn = ?");
		sb.append("    AND MAIL.sender_kbn = ?");
		sb.append("    AND MAIL.mail_status = ?");
		sb.append("    AND MAIL.delete_flg = ?");

		params.add(SendKbn.RECEIVE);
		params.add(MailKbn.PRE_APPLICCATION);
		params.add(SenderKbn.MEMBER);
		params.add(MailStatus.UNOPENED);
		params.add(DeleteFlgKbn.NOT_DELETED);

		// 1年以内のメールのみを対象
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -1);
		sb.append(" AND MAIL.send_datetime >= ? ");
		params.add(cal.getTime());

		sb.append(" ORDER BY MAIL.application_id, MAIL.send_datetime DESC");
		sb.append(" ) SUB");
	}

	/**
	 * 応募者一覧用のデータを取得します。
	 * @param property ページャプロパティ
	 * @param searchProperty 検索プロパティ
	 * @return
	 * @throws WNoResultException
	 */
	public PreApplicationListRetDto getPreApplicationListData(PagerProperty property, ApplicationSearchProperty searchProperty) throws WNoResultException {

		PreApplicationListRetDto retDto = new PreApplicationListRetDto();

		StringBuffer sql = new StringBuffer();
		List<Object> params = new ArrayList<Object>(0);

		createPreApplicationSearchSql(sql, params, searchProperty);

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
			retDto.retList = jdbcManager.selectBySql(TPreApplication.class, sql.toString(), params.toArray())
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
	 * 未読のプレ応募メール数を取得する
	 * @return
	 */
	public long getUnReadPreApplicationMailCount() {
		StringBuilder sb = new StringBuilder("");
		List<Object> params = new ArrayList<Object>();
		createUnReadMailSql(sb, params);
		return jdbcManager.getCountBySql(sb.toString(), params.toArray());
	}

	/**
	 * 未読のプレ応募メールがあるか判定する
	 * @return
	 */
	public boolean checkUnReadPreApplicationMail() {
		return getUnReadPreApplicationMailCount() > 0;
	}

	/**
	 * プレ応募メール検索SQLを作成します。
	 * @param sql SQL
	 * @param params パラメータ
	 * @param property 検索プロパティ
	 */
	private void createPreApplicationSearchSql(StringBuffer sql, List<Object> params, ApplicationSearchProperty property) {
		//検索実行日の1年前の日付を取得
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -1);

		sql.append(" SELECT ");
		sql.append("   APP.* ");
		sql.append(" FROM ");

		sql.append("   t_pre_application APP  ");

		sql.append(" WHERE ");
		sql.append(" APP.customer_id = ? ");
		sql.append("   AND APP.application_datetime > ? ");
		params.add(getCustomerId());
			params.add(cal.getTime());

		if (property.webId != null) {
			sql.append("   AND APP.web_id = ? ");
			params.add(property.webId);
		}

		addMailSearchSql(sql, params, property, "APP.id = MAIL.application_id");

		sql.append("   AND APP.delete_flg = ? ");

		params.add(DeleteFlgKbn.NOT_DELETED);

	}
}
