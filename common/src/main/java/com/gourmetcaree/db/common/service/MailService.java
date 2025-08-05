package com.gourmetcaree.db.common.service;

import static com.gourmetcaree.common.util.GourmetCareeUtil.*;
import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.exception.SNonUniqueResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.constants.DbFlgValue;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants.MailKbn;
import com.gourmetcaree.db.common.constants.MTypeConstants.SendKbn;
import com.gourmetcaree.db.common.constants.MTypeConstants.SenderKbn;
import com.gourmetcaree.db.common.entity.AbstractCommonEntity;
import com.gourmetcaree.db.common.entity.TApplication;
import com.gourmetcaree.db.common.entity.TMail;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.enums.MTypeEnum.MailStatusEnum;
import com.gourmetcaree.db.common.enums.MTypeEnum.SenderKbnEnum;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.mailBox.dto.mailBox.MailListDto;
import com.gourmetcaree.db.scoutFoot.dto.scoutMail.ScoutMailListDto;

import jp.co.whizz_tech.commons.WztStringUtil;

/**
 * メールのサービスクラスです。
 * @version 1.0
 */
public class MailService extends AbstractGroumetCareeBasicService<TMail> {

	/** 気になるメールフラグ */
	private final int  INTEREST_MAIL_FLG = 1;

	/**
	 * 指定されたメールIDが会員の操作可能なメールかどうかを取得します。
	 * @param id
	 * @param memberId
	 * @return
	 */
	public boolean canMemberHandleEntity(int id, int memberId) {

		Where sendWhere =  new SimpleWhere()
							.eq(WztStringUtil.toCamelCase(TMail.ID), id)
							.eq(WztStringUtil.toCamelCase(TMail.FROM_ID), memberId)
							.eq(WztStringUtil.toCamelCase(TMail.SEND_KBN), MTypeConstants.SendKbn.SEND)
							.in(WztStringUtil.toCamelCase(TMail.SENDER_KBN), SenderKbn.MEMBER)
							.eq(WztStringUtil.toCamelCase(AbstractCommonEntity.DELETE_FLG), DeleteFlgKbn.NOT_DELETED)
							;

		Where receiveWhere =  new SimpleWhere()
							.eq(WztStringUtil.toCamelCase(TMail.ID), id)
							.eq(WztStringUtil.toCamelCase(TMail.TO_ID), memberId)
							.eq(WztStringUtil.toCamelCase(TMail.SEND_KBN), MTypeConstants.SendKbn.RECEIVE)
							.in(WztStringUtil.toCamelCase(TMail.SENDER_KBN), SenderKbn.CUSTOMER)
							.eq(WztStringUtil.toCamelCase(AbstractCommonEntity.DELETE_FLG), DeleteFlgKbn.NOT_DELETED)
							;

		long sendCount = countRecords(sendWhere);
		long receiveCount = countRecords(receiveWhere);

		return (sendCount > 0 || receiveCount > 0);
	}

	/**
	 * 受信した応募メールが存在するかを返します。
	 * @param toId 受信者ID
	 * @param mailStatus メールステータス
	 * @return
	 */
	public boolean isReceivedApplicationMailExist(int toId, int mailStatus, SenderKbnEnum... senderKbnEnum) {
		Where where = getWhereReceivedMail(toId, MTypeConstants.MailKbn.APPLICCATION, mailStatus, senderKbnEnum);
		long count = countRecords(where);

		return (count != 0) ? true : false;
	}

	/**
	 * 受信した応募メールが存在するかを返します。
	 * @param toId 受信者ID
	 * @param mailStatus メールステータス
	 * @return
	 */
	public boolean isReceivedPreApplicationMailExist(int toId, int mailStatus, SenderKbnEnum... senderKbnEnum) {
		Where where = getWhereReceivedMail(toId, MTypeConstants.MailKbn.PRE_APPLICCATION, mailStatus, senderKbnEnum);
		long count = countRecords(where);

		return (count != 0) ? true : false;
	}

	/**
	 * 受信した応募メールの件数を取得します。
	 * @param toId 受信者ID
	 * @param mailStatus メールステータス
	 * @return
	 */
	public int countReceivedApplicationMail(int toId, MailStatusEnum mailStatus, SenderKbnEnum... senderKbnEnum) {
		Where where = getWhereReceivedMail(toId, MTypeConstants.MailKbn.APPLICCATION, mailStatus.getValue(), senderKbnEnum);
		long count = countRecords(where);

		return (int)count;
	}

	/**
	 * 受信したスカウトメールが存在するかを返します。
	 * @param toId 受信者ID
	 * @param mailStatus メールステータス
	 * @return
	 */
	public boolean isReceivedScoutMailExist(int toId, int mailStatus, SenderKbnEnum... senderKbnEnum) {
		Where where = getWhereReceivedMail(toId, MTypeConstants.MailKbn.SCOUT, mailStatus, senderKbnEnum);
		long count = countRecords(where);

		return (count != 0) ? true : false;
	}

	/**
	 * 受信したスカウトメールが存在するかを返します。
	 * @param toId 受信者ID
	 * @param mailStatus メールステータス
	 * @return
	 */
	public boolean isReceivedObservateMailExist(int toId, int mailStatus, SenderKbnEnum... senderKbnEnum) {
		Where where = getWhereReceivedMail(toId, MTypeConstants.MailKbn.OBSERVATE_APPLICATION, mailStatus, senderKbnEnum);
		long count = countRecords(where);

		return (count != 0) ? true : false;
	}


	/**
	 * 受信したスアルバイトメールが存在するかを返します。
	 * @param toId 受信者ID
	 * @param mailStatus メールステータス
	 * @return
	 */
	public boolean isReceivedArbeitMailExist(int toId, int mailStatus, SenderKbnEnum... senderKbnEnum) {
		Where where = getWhereReceivedMail(toId, MTypeConstants.MailKbn.ARBEIT_APPLICATION, mailStatus, senderKbnEnum);
		long count = countRecords(where);

		return (count != 0) ? true : false;
	}

	/**
	 * 受信したスカウトメールの件数を取得します。
	 * @param toId 受信者ID
	 * @param mailStatus メールステータス
	 * @return
	 */
	public int countReceivedScoutMail(int toId, MailStatusEnum mailStatus, SenderKbnEnum... senderKbnEnum) {
		Where where = getWhereReceivedMail(toId, MTypeConstants.MailKbn.SCOUT, mailStatus.getValue(), senderKbnEnum);
		long count = countRecords(where);

		return (int)count;
	}

	/**
	 * 受信した気になるメールが存在するかを返します。
	 * @param toId 受信者ID
	 * @param mailStatus メールステータス
	 * @return
	 */
	public boolean isReceivedInterestMailExist(int toId, int mailStatus, SenderKbnEnum... senderKbnEnum) {
		Where where = getWhereReceivedInterestMail(toId, MTypeConstants.MailKbn.SCOUT, mailStatus, senderKbnEnum);
		long count = countRecords(where);

		return (count != 0) ? true : false;
	}

	/**
	 * 受信した気になるメールの件数を取得します。
	 * @param toId 受信者ID
	 * @param mailStatus メールステータス
	 * @return
	 */
	public int countReceivedInterestMail(int toId, MailStatusEnum mailStatus, SenderKbnEnum... senderKbnEnum) {
		Where where = getWhereReceivedInterestMail(toId, MTypeConstants.MailKbn.SCOUT, mailStatus.getValue(), senderKbnEnum);
		long count = countRecords(where);

		return (int)count;
	}

	/**
	 * 受信した見学メールの件数を取得します。
	 */
	public int countReceivedObservationMail(int toId, MailStatusEnum mailStatus, SenderKbnEnum... senderKbnEnum) {
		Where where = getWhereReceivedMail(toId, MTypeConstants.MailKbn.OBSERVATE_APPLICATION, mailStatus.getValue(), senderKbnEnum);
		long count = countRecords(where);

		return (int)count;
	}

	/**
	 * 受信メールについての検索条件を取得します。
	 * @param toId 受信者ID
	 * @param mailKbn メール区分
	 * @param mailStatus メールステータス
	 * @return
	 */
	private Where getWhereReceivedMail(int toId, int mailKbn, int mailStatus, SenderKbnEnum... senderKbnEnum) {

		List<Integer> list = new ArrayList<Integer>();

		if (senderKbnEnum != null) {
			for (SenderKbnEnum tmpSenderKbn : senderKbnEnum) {
				list.add(tmpSenderKbn.getValue());
			}
		}

		//検索実行日の1年前の日付を取得
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -1);

		Where where =  new SimpleWhere()
						.eq(WztStringUtil.toCamelCase(TMail.TO_ID), toId)
						.eq(WztStringUtil.toCamelCase(TMail.MAIL_KBN), mailKbn)
						.eq(WztStringUtil.toCamelCase(TMail.SEND_KBN), MTypeConstants.SendKbn.RECEIVE)
						.eq(WztStringUtil.toCamelCase(TMail.MAIL_STATUS), mailStatus)
						.ge(WztStringUtil.toCamelCase(TMail.SEND_DATETIME), cal.getTime())
						.in(WztStringUtil.toCamelCase(TMail.SENDER_KBN), list)
						.eq(WztStringUtil.toCamelCase(AbstractCommonEntity.DELETE_FLG), DeleteFlgKbn.NOT_DELETED)
						.ne(WztStringUtil.toCamelCase(TMail.INTEREST_FLG), INTEREST_MAIL_FLG)
						;

		return where;
	}

	/**
	 * 気になるメールについての検索条件を取得します。
	 * @param toId 受信者ID
	 * @param mailKbn メール区分
	 * @param mailStatus メールステータス
	 * @return
	 */
	private Where getWhereReceivedInterestMail(int toId, int mailKbn, int mailStatus, SenderKbnEnum... senderKbnEnum) {

		List<Integer> list = new ArrayList<Integer>();

		if (senderKbnEnum != null) {
			for (SenderKbnEnum tmpSenderKbn : senderKbnEnum) {
				list.add(tmpSenderKbn.getValue());
			}
		}

		//検索実行日の1年前の日付を取得
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -1);

		Where where =  new SimpleWhere()
						.eq(WztStringUtil.toCamelCase(TMail.TO_ID), toId)
						.eq(WztStringUtil.toCamelCase(TMail.MAIL_KBN), mailKbn)
						.eq(WztStringUtil.toCamelCase(TMail.SEND_KBN), MTypeConstants.SendKbn.RECEIVE)
						.eq(WztStringUtil.toCamelCase(TMail.MAIL_STATUS), mailStatus)
						.ge(WztStringUtil.toCamelCase(TMail.SEND_DATETIME), cal.getTime())
						.in(WztStringUtil.toCamelCase(TMail.SENDER_KBN), list)
						.eq(WztStringUtil.toCamelCase(AbstractCommonEntity.DELETE_FLG), DeleteFlgKbn.NOT_DELETED)
						.eq(WztStringUtil.toCamelCase(TMail.INTEREST_FLG), INTEREST_MAIL_FLG)
						;

		return where;
	}

	/**
	 * スカウトメールの一覧を取得
	 * @param sendKbn
	 * @param customerId
	 * @return
	 * @throws WNoResultException
	 */
	public List<ScoutMailListDto> getScoutMailList(int sendKbn, int customerId, PageNavigateHelper pageNavi, int targetPage, Map<String,Object> searchRequestsMap) throws WNoResultException {

		try {
			// 一覧取得用SQLを生成
			StringBuilder sqlStr = new StringBuilder();
			List<Object> params = new ArrayList<Object>();
			createSql(sendKbn, customerId, sqlStr, params, searchRequestsMap);

			// メール件数を取得
			int count = (int)getCountBySql(sqlStr.toString(), params.toArray());
			pageNavi.changeAllCount(count);
			pageNavi.setPage(targetPage);
			// ページングSQLを追加
			addPageSql(pageNavi, sqlStr, params);

			List<ScoutMailListDto> list = getScoutMailList(sqlStr, params);

			return list;
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}

	}

	/**
	 * スカウトメール一覧取得用SQLを生成
	 * @param sendKbn 送受信区分
	 * @param customerId 顧客ID
	 */
	private void createSql(int sendKbn, int customerId, StringBuilder sqlStr, List<Object> params, Map<String,Object> searchRequestsMap) {

		if (sendKbn == MTypeConstants.SendKbn.RECEIVE) {
			sqlStr.append("SELECT * FROM(");
			sqlStr.append("SELECT");
			sqlStr.append("    DISTINCT ON (scout_mail_log_id) ");
			sqlStr.append("     TM.id, ");
			sqlStr.append("     TM.from_id, ");
			sqlStr.append("     TM.to_id, ");
			sqlStr.append("     TM.subject, ");
			sqlStr.append("     TM.body, ");
			sqlStr.append("     TM.mail_status, ");
			sqlStr.append("     TM.send_datetime, ");
			sqlStr.append("     TM.scout_mail_log_id, ");
			sqlStr.append("     M.id AS member_id, ");
			sqlStr.append("     TMR.reading_datetime AS receive_reading_datetime, ");
			sqlStr.append("     M.birthday, ");
			sqlStr.append("     M.area_cd, ");
			sqlStr.append("     M.prefectures_cd, ");
			sqlStr.append("     M.municipality, ");
			sqlStr.append("     M.food_exp_kbn, ");
			sqlStr.append("     TSML.selection_flg, ");
			sqlStr.append("     TSML.memo ");
			sqlStr.append("FROM");
			sqlStr.append("    t_mail TM ");
			sqlStr.append("    LEFT OUTER JOIN t_mail TMR ");
			sqlStr.append("        ON TM.receive_id = TMR.id ");
			sqlStr.append("    LEFT OUTER JOIN t_scout_mail_log TSML ");
			sqlStr.append("    ON TM.scout_mail_log_id = TSML.id ");
			sqlStr.append("    LEFT OUTER JOIN m_member M ");
			sqlStr.append("    ON TM.from_id = M.id AND M.delete_flg = ? ");
			sqlStr.append("    WHERE TM.to_id = ? ");
			sqlStr.append("    AND TM.sender_kbn = ? ");
			sqlStr.append("    AND TM.display_flg = ? ");

			params.add(DeleteFlgKbn.NOT_DELETED);
			params.add(customerId);
			params.add(MTypeConstants.SenderKbn.MEMBER);
			params.add(DbFlgValue.TRUE);


			//検索実行日の1年前の日付を取得
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.YEAR, -1);

			sqlStr.append("AND TM.send_datetime >= ? ");
			sqlStr.append("AND TM.send_kbn = ? ");
			sqlStr.append("AND TM.mail_kbn = ? ");
			sqlStr.append("AND TM.delete_flg = ? ");

			params.add(cal.getTime());
			params.add(MTypeConstants.SendKbn.RECEIVE);
			params.add(MTypeConstants.MailKbn.SCOUT);
			params.add(DeleteFlgKbn.NOT_DELETED);

			if(searchRequestsMap.containsKey("where_areaCd")) {
				List<Integer> areaCds = GourmetCareeUtil.convertStringArrayToIntegerList(
						GourmetCareeUtil.removeBlankElement((String[])searchRequestsMap.get("where_areaCd")));
				sqlStr.append(SqlUtils.andInNoCamelize("M.area_cd", areaCds.size()));
				params.addAll(areaCds);
			}

			if(searchRequestsMap.containsKey("where_selectionKbn")) {
				sqlStr.append("    AND TSML.selection_flg  = ?");
				params.add(Integer.parseInt((String)searchRequestsMap.get("where_selectionKbn")));
			}

			sqlStr.append(" ORDER BY scout_mail_log_id, TM.send_datetime DESC");
			sqlStr.append(" ) SUB ");

			if(searchRequestsMap.containsKey("where_mailStatus") || searchRequestsMap.containsKey("where_keyword")) {
				sqlStr.append(" WHERE ");
			}

			if(searchRequestsMap.containsKey("where_mailStatus")) {
				List<Integer> mailStatus = GourmetCareeUtil.convertStringArrayToIntegerList(
						GourmetCareeUtil.removeBlankElement((String[])searchRequestsMap.get("where_mailStatus")));
				sqlStr.append(SqlUtils.inNoCamelize("mail_status", mailStatus.size()));
				params.addAll(mailStatus);
			}

			if(searchRequestsMap.containsKey("where_keyword")) {
				String keyword = (String)searchRequestsMap.get("where_keyword");
				Pattern pattern = Pattern.compile(GourmetCareeConstants.ZENBUN_BAD_WORD);
				Matcher matcher = pattern.matcher(keyword);
				keyword = WztStringUtil.trim(
									matcher.replaceAll(keyword)
											.replaceAll("　", " "));

				if(searchRequestsMap.containsKey("where_mailStatus")) {
					sqlStr.append(" AND (");
				} else {
					sqlStr.append(" (");
				}

				sqlStr.append(SqlUtils.createLikeSearch(
						keyword,
						params,
						" subject LIKE ? "));

				sqlStr.append("  OR ");
				sqlStr.append(SqlUtils.createLikeSearch(
						keyword,
						params,
						" body LIKE ? "));

				sqlStr.append(" ) ");
			}
		}else {
			sqlStr.append("SELECT");
			sqlStr.append("     TM.id, ");
			sqlStr.append("     TM.from_id, ");
			sqlStr.append("     TM.to_id, ");
			sqlStr.append("     TM.subject, ");
			sqlStr.append("     TM.mail_status, ");
			sqlStr.append("     TM.send_datetime, ");
			sqlStr.append("     TM.scout_mail_log_id, ");
			sqlStr.append("     M.id AS member_id, ");
			sqlStr.append("     TMR.reading_datetime AS receive_reading_datetime ");
			sqlStr.append("FROM");
			sqlStr.append("    t_mail TM ");
			sqlStr.append("    LEFT OUTER JOIN t_mail TMR ");
			sqlStr.append("        ON TM.receive_id = TMR.id ");
			sqlStr.append("    LEFT OUTER JOIN m_member M ");

			sqlStr.append("ON TM.to_id = M.id AND M.delete_flg = ? ");
			sqlStr.append("WHERE TM.from_id = ? ");
			sqlStr.append("AND TM.sender_kbn = ? ");
			sqlStr.append("AND TM.display_flg = ? ");
			params.add(DeleteFlgKbn.NOT_DELETED);
			params.add(customerId);
			params.add(MTypeConstants.SenderKbn.CUSTOMER);
			params.add(DbFlgValue.TRUE);

				//検索実行日の1年前の日付を取得
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.YEAR, -1);

			sqlStr.append("AND TM.send_datetime >= ? ");
			sqlStr.append("AND TM.send_kbn = ? ");
			sqlStr.append("AND TM.mail_kbn = ? ");
			sqlStr.append("AND TM.delete_flg = ? ");


			params.add(cal.getTime());
			params.add(sendKbn);
			params.add(MTypeConstants.MailKbn.SCOUT);
			params.add(DeleteFlgKbn.NOT_DELETED);
		}

	}

	/**
	 * 未読のスカウトメールを取得する
	 * @param customerId
	 * @param sqlStr
	 * @param params
	 */
	private void createUnReadMailSql(int customerId, StringBuilder sqlStr, List<Object> params) {

		sqlStr.append("SELECT * FROM(");
		sqlStr.append("SELECT");
		sqlStr.append("    DISTINCT ON (scout_mail_log_id) ");
		sqlStr.append("     TM.scout_mail_log_id ");
		sqlStr.append("FROM");
		sqlStr.append("    t_mail TM ");
		sqlStr.append("    INNER JOIN m_member M ");
		sqlStr.append("    ON TM.from_id = M.id ");
		sqlStr.append("    WHERE TM.to_id = ? ");
		sqlStr.append("    AND TM.sender_kbn = ? ");

		params.add(customerId);
		params.add(MTypeConstants.SenderKbn.MEMBER);


		//検索実行日の1年前の日付を取得
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -1);

		sqlStr.append("AND TM.send_datetime >= ? ");
		sqlStr.append("AND TM.send_kbn = ? ");
		sqlStr.append("AND TM.mail_kbn = ? ");
		sqlStr.append("AND TM.mail_status = ? ");
		sqlStr.append("AND TM.delete_flg = ? ");

		params.add(cal.getTime());
		params.add(MTypeConstants.SendKbn.RECEIVE);
		params.add(MTypeConstants.MailKbn.SCOUT);
		params.add(MTypeConstants.MailStatus.UNOPENED);
		params.add(DeleteFlgKbn.NOT_DELETED);

		sqlStr.append(" ORDER BY scout_mail_log_id, TM.send_datetime DESC");
		sqlStr.append(" ) SUB ");

	}

	/**
	 * ページングSQLを追加
	 * @param pageNavi ページナビヘルパー
	 * @param sqlStr SQL文字列
	 * @param params パラメータ
	 */
	private void addPageSql(PageNavigateHelper pageNavi, StringBuilder sqlStr, List<Object> params) {

		sqlStr.append("ORDER BY send_datetime desc, id desc ");
		sqlStr.append("LIMIT ? ");
		sqlStr.append("OFFSET ?");

		params.add(pageNavi.limit);
		params.add(pageNavi.offset);
	}

	/**
	 * メールの一覧を取得
	 * @param sendKbn
	 * @param memberId
	 * @return
	 * @throws WNoResultException
	 */
	public List<MailListDto> getMailList(int sendKbn, int memberId, PageNavigateHelper pageNavi, int targetPage) throws WNoResultException {

		try {
			// 一覧取得用SQLを生成
			StringBuilder sqlStr = new StringBuilder();
			List<Object> params = new ArrayList<Object>();
			createMailBoxSql(sendKbn, memberId, sqlStr, params);

			// メール件数を取得
			int count = (int)getCountBySql(sqlStr.toString(), params.toArray());
			pageNavi.changeAllCount(count);
			pageNavi.setPage(targetPage);

			// ページングSQLを追加
			addMailBoxPageSql(pageNavi, sqlStr, params);

			List<MailListDto> list = getMailList(sqlStr, params);

			return list;
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}

	}

	/**
	 * メール一覧取得用SQLを生成
	 * @param sendKbn 送受信区分
	 * @param memberId 会員ID
	 */
	private void createMailBoxSql(int sendKbn, int memberId, StringBuilder sqlStr, List<Object> params) {

		sqlStr.append("SELECT TM.id, TM.mail_kbn, TM.from_id, TM.from_name, TM.to_id, TM.to_name, TM.subject, TM.mail_status, TM.send_datetime, MC.id AS customer_id ");
		sqlStr.append("FROM t_mail TM LEFT OUTER JOIN m_customer MC ");
		if (sendKbn == MTypeConstants.SendKbn.SEND) {
			sqlStr.append("ON TM.to_id = MC.id AND MC.delete_flg = ? ");
			sqlStr.append("WHERE TM.from_id = ? ");
			sqlStr.append("AND TM.sender_kbn = ? ");
			params.add(DeleteFlgKbn.NOT_DELETED);
			params.add(memberId);
			params.add(MTypeConstants.SenderKbn.MEMBER);
		} else if (sendKbn == MTypeConstants.SendKbn.RECEIVE) {
			sqlStr.append("ON TM.from_id = MC.id AND MC.delete_flg = ? ");
			sqlStr.append("WHERE TM.to_id = ? ");
			sqlStr.append("AND TM.sender_kbn = ? ");
			params.add(DeleteFlgKbn.NOT_DELETED);
			params.add(memberId);
			params.add(MTypeConstants.SenderKbn.CUSTOMER);
		}

		//検索実行日の1年前の日付を取得
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -1);

		sqlStr.append(" AND TM.send_datetime >= ? ");
		sqlStr.append(" AND TM.mail_kbn != ? ");
		sqlStr.append("AND TM.send_kbn = ? ");
		sqlStr.append("AND TM.delete_flg = ? ");

		params.add(cal.getTime());
		params.add(MTypeConstants.MailKbn.ARBEIT_APPLICATION);
		params.add(sendKbn);
		params.add(DeleteFlgKbn.NOT_DELETED);

	}

	/**
	 * ページングSQLを追加
	 * @param pageNavi ページナビヘルパー
	 * @param sqlStr SQL文字列
	 * @param params パラメータ
	 */
	private void addMailBoxPageSql(PageNavigateHelper pageNavi, StringBuilder sqlStr, List<Object> params) {

		sqlStr.append("ORDER BY TM.send_datetime desc, TM.id desc ");
		sqlStr.append("LIMIT ? ");
		sqlStr.append("OFFSET ?");

		params.add(pageNavi.limit);
		params.add(pageNavi.offset);
	}

	/**
	 * スカウトメールの一覧を取得
	 * @param sqlStr SQL文字列
	 * @param params パラメータ
	 * @return スカウトメールリスト
	 */
	private List<ScoutMailListDto> getScoutMailList(StringBuilder sqlStr, List<Object> params) {

		List<ScoutMailListDto> resultList = jdbcManager.selectBySql
											(ScoutMailListDto.class, sqlStr.toString(), params.toArray())
											.disallowNoResult()
											.getResultList();

		return resultList;
	}

	/**
	 * メールの一覧を取得
	 * @param sqlStr SQL文字列
	 * @param params パラメータ
	 * @return スカウトメールリスト
	 */
	private List<MailListDto> getMailList(StringBuilder sqlStr, List<Object> params) {

		List<MailListDto> resultList = jdbcManager.selectBySql
											(MailListDto.class, sqlStr.toString(), params.toArray())
											.disallowNoResult()
											.getResultList();

		return resultList;
	}

	/**
	 * 顧客のメールが存在しているかチェック
	 * @param id メールID
	 * @param customerId 顧客ID
	 * @param sendKbn 送受信区分
	 * @param mailKbn メール区分
	 * @return 存在している場合、trueを返す
	 */
	public boolean isCustomerMailExist(int id, int customerId, int sendKbn, int mailKbn) {

		if (MTypeConstants.SendKbn.SEND == sendKbn) {
			return isMailExistFromCustomer(id, customerId, mailKbn);
		} else {
			return isMailExistToCustomer(id, customerId, mailKbn);
		}

	}

	/**
	 * 顧客が受信したメールが存在しているかチェック
	 * @param id メールID
	 * @param customerId 顧客ID
	 * @param mailKbn メール区分
	 * @return 存在している場合、trueを返す
	 */
	public boolean isMailExistToCustomer(int id, int customerId, int mailKbn) {

		Long count = jdbcManager.from(TMail.class)
						.where(new SimpleWhere()
						.eq("id", id)
						.eq("mailKbn", mailKbn)
						.eq(toCamelCase(TMail.SEND_KBN), SendKbn.RECEIVE)
						.in(toCamelCase(TMail.SENDER_KBN), SenderKbn.MEMBER, SenderKbn.NO_MEMBER)
						.eq("toId", customerId))
						.getCount();

		return count > 0 ? true : false;
	}

	/**
	 * 顧客が送信したメールが存在しているかチェック
	 * @param id メールID
	 * @param customerId 顧客ID
	 * @param mailKbn メール区分
	 * @return 存在している場合、trueを返す
	 */
	public boolean isMailExistFromCustomer(int id, int customerId, int mailKbn) {

		Long count = jdbcManager.from(TMail.class)
						.where(new SimpleWhere()
						.eq("id", id)
						.eq("mailKbn", mailKbn)
						.eq(toCamelCase(TMail.SEND_KBN), SendKbn.SEND)
						.eq(toCamelCase(TMail.SENDER_KBN), SenderKbn.CUSTOMER)
						.eq("fromId", customerId))
						.getCount();

		return count > 0 ? true : false;
	}

	/**
	 * 顧客のメールデータを返す
	 * @param id
	 * @param customerId
	 * @param mailKbn
	 * @param sendKbn
	 * @return
	 * @throws WNoResultException
	 */
	public TMail getMailDataCustomer(int id, int customerId, int mailKbn, int sendKbn) throws WNoResultException {

		if (MTypeConstants.SendKbn.SEND == sendKbn) {
			return getMailDataFromCustomer(id, customerId, mailKbn);
		} else {
			return getMailDataToCustomer(id, customerId, mailKbn);
		}
	}

	/**
	 * 会員のメールデータを返す
	 * @param id
	 * @param memberId
	 * @param sendKbn
	 * @return
	 * @throws WNoResultException
	 */
	public TMail getMailDataMember(int id, int memberId, int sendKbn) throws WNoResultException {

		if (MTypeConstants.SendKbn.SEND == sendKbn) {
			return getMailDataFromMember(id, memberId, sendKbn);
		} else {
			return getMailDataToMember(id, memberId, sendKbn);
		}
	}


	/**
	 * 顧客の受信メールデータを返す
	 * @param id メールID
	 * @param customerId 顧客ID
	 * @param mailKbn メール区分
	 * @return メールエンティティ
	 * @throws WNoResultException
	 */
	public TMail getMailDataToCustomer(int id, int customerId, int mailKbn) throws WNoResultException {

		try {
			TMail result = jdbcManager.from(TMail.class)
							.where(new SimpleWhere()
							.eq("id", id)
							.eq("mailKbn", mailKbn)
							.eq("toId", customerId)
							.eq(toCamelCase(TMail.SEND_KBN), SendKbn.RECEIVE)
							.in(toCamelCase(TMail.SENDER_KBN), SenderKbn.MEMBER, SenderKbn.NO_MEMBER)
							.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
							.disallowNoResult()
							.getSingleResult();

			return result;
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}

	/**
	 * 顧客の送信メールデータを返す
	 * @param id メールID
	 * @param customerId 顧客ID
	 * @param mailKbn メール区分
	 * @return メールエンティティ
	 * @throws WNoResultException
	 */
	public TMail getMailDataFromCustomer(int id, int customerId, int mailKbn) throws WNoResultException {

		try {
			TMail result = jdbcManager.from(TMail.class)
							.where(new SimpleWhere()
							.eq("id", id)
							.eq("mailKbn", mailKbn)
							.eq("fromId", customerId)
							.eq(toCamelCase(TMail.SEND_KBN), SendKbn.SEND)
							.eq(toCamelCase(TMail.SENDER_KBN), SenderKbn.CUSTOMER)
							.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
							.disallowNoResult()
							.getSingleResult();

			return result;
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}

	/**
	 * 会員の受信メールデータを返す
	 * @param id メールID
	 * @param memberId 会員ID
	 * @param sendKbn 送受信区分
	 * @return メールエンティティ
	 * @throws WNoResultException
	 */
	public TMail getMailDataToMember(int id, int memberId, int sendKbn) throws WNoResultException {

		try {
			TMail result = jdbcManager.from(TMail.class)
							.where(new SimpleWhere()
							.eq("id", id)
							.eq("toId", memberId)
							.eq("sendKbn", sendKbn)
							.eq(toCamelCase(TMail.SENDER_KBN), SenderKbn.CUSTOMER)
							.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
							.disallowNoResult()
							.getSingleResult();

			return result;
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}

	/**
	 * 会員の送信メールデータを返す
	 * @param id メールID
	 * @param memberId 会員ID
	 * @param sendKbn 送受信区分
	 * @return メールエンティティ
	 * @throws WNoResultException
	 */
	public TMail getMailDataFromMember(int id, int memberId, int sendKbn) throws WNoResultException {

		try {
			TMail result = jdbcManager.from(TMail.class)
							.where(new SimpleWhere()
							.eq("id", id)
							.eq("fromId", memberId)
							.eq("sendKbn", sendKbn)
							.eq(toCamelCase(TMail.SENDER_KBN), SenderKbn.MEMBER)
							.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
							.disallowNoResult()
							.getSingleResult();

			return result;
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}

	/**
	 * 送信者を特定し、受信した応募メールが存在するかを返します。
	 * @param applicationId
	 * @param toId
	 * @param mailStatus
	 * @return
	 */
	public boolean isReceivedApplicationMailFromExist(int applicationId, int toId, int mailStatus) {
		Where where = getWhereReceivedMailByFrom(applicationId, toId, MTypeConstants.MailKbn.APPLICCATION, mailStatus);
		long count = countRecords(where);

		return (count != 0) ? true : false;
	}

	/**
	 * 応募IDを特定して顧客が操作可能な受信メールの検索条件を作成します。
	 * @param applicationId
	 * @param toId
	 * @param mailKbn
	 * @param mailStatus
	 * @return
	 */
	private Where getWhereReceivedMailByFrom(int applicationId, int toId, int mailKbn, int mailStatus) {

		Where where =  new SimpleWhere()
							.eq(WztStringUtil.toCamelCase(TMail.TO_ID), toId)
							.eq(WztStringUtil.toCamelCase(TMail.APPLICATION_ID), applicationId)
							.eq(WztStringUtil.toCamelCase(TMail.MAIL_KBN), mailKbn)
							.eq(WztStringUtil.toCamelCase(TMail.SEND_KBN), MTypeConstants.SendKbn.RECEIVE)
							.eq(WztStringUtil.toCamelCase(TMail.MAIL_STATUS), mailStatus)
							.in(toCamelCase(TMail.SENDER_KBN), SenderKbn.MEMBER, SenderKbn.NO_MEMBER)
							.eq(WztStringUtil.toCamelCase(AbstractCommonEntity.DELETE_FLG), DeleteFlgKbn.NOT_DELETED)
							;

		return where;
	}

	/**
	 * 送信者を特定し、受信したメールが存在するかを返します。
	 * @param toId 送信元
	 * @param mailStatus メールステータス
	 * @param applicationId 各種メールID
	 * @return
	 */
	public boolean isReceivedApplicationMailFromExist(int applicationId, int toId, int mailKbn, int mailStatus) {

		Where where = getWhereReceivedApplicationMailByFrom(applicationId, toId, mailKbn, mailStatus);
		long count = countRecords(where);

		return (count != 0) ? true : false;
	}
	private Where getWhereReceivedApplicationMailByFrom(int applicationId, int toId, int mailKbn, int mailStatus) {


		SimpleWhere where = new SimpleWhere();

		where.eq(WztStringUtil.toCamelCase(TMail.TO_ID), toId);
		switch (mailKbn) {
		case MTypeConstants.MailKbn.APPLICCATION:
			where.eq(WztStringUtil.toCamelCase(TMail.MAIL_KBN), MTypeConstants.MailKbn.APPLICCATION)
				.eq(WztStringUtil.toCamelCase(TMail.APPLICATION_ID), applicationId);
			break;
		case MTypeConstants.MailKbn.SCOUT:
			where.eq(WztStringUtil.toCamelCase(TMail.MAIL_KBN), MTypeConstants.MailKbn.SCOUT)
				.eq(WztStringUtil.toCamelCase(TMail.SCOUT_MAIL_LOG_ID), applicationId);
			break;
		case MTypeConstants.MailKbn.OBSERVATE_APPLICATION:
			where.eq(WztStringUtil.toCamelCase(TMail.MAIL_KBN), MTypeConstants.MailKbn.OBSERVATE_APPLICATION)
				.eq(WztStringUtil.toCamelCase(TMail.OBSERVATE_APPLICATION_ID), applicationId);
			break;
		case MTypeConstants.MailKbn.ARBEIT_APPLICATION:
			where.eq(WztStringUtil.toCamelCase(TMail.MAIL_KBN), MTypeConstants.MailKbn.ARBEIT_APPLICATION)
				.eq(WztStringUtil.toCamelCase(TMail.ARBEIT_APPLICATION_ID), applicationId);
			break;
		default:
			throw new FraudulentProcessException("不正な操作が行われました。" + mailKbn);
		}

		where.eq(WztStringUtil.toCamelCase(TMail.SEND_KBN), MTypeConstants.SendKbn.RECEIVE)
		.eq(WztStringUtil.toCamelCase(TMail.MAIL_STATUS), mailStatus)
		.in(toCamelCase(TMail.SENDER_KBN), SenderKbn.MEMBER, SenderKbn.NO_MEMBER)
		.eq(WztStringUtil.toCamelCase(AbstractCommonEntity.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		return where;
	}

	/**
	 * 未読のメールを既読にする処理。
	 * メールステータスが未読以外の場合は処理を行わない。
	 * @param mailId
	 * @param toId
	 * @param mailKbn
	 * @throws WNoResultException
	 */
	public void changeMailToOpened(int mailId, int toId, int mailKbn) throws WNoResultException  {

		try {
			Where where = new SimpleWhere()
								.eq(toCamelCase(TMail.ID), mailId)
								.eq(toCamelCase(TMail.TO_ID), toId)
								.eq(toCamelCase(TMail.MAIL_KBN), mailKbn)
								.eq(toCamelCase(TMail.MAIL_STATUS),  MTypeConstants.MailStatus.UNOPENED)
								.eq(toCamelCase(TApplication.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

			long count = countRecords(where);

			if (count != 0) {
				TMail updateEntity = new TMail();
				updateEntity.id = mailId;
				updateEntity.mailStatus = MTypeConstants.MailStatus.OPENED;
				updateEntity.readingDatetime = new Timestamp(new Date().getTime());
				updateIncludesVersion(updateEntity);
			}

		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}

	/**
	 * 既読のメールを未読にする処理。
	 * メールステータスが既読以外の場合は処理を行わない。
	 * @param mailId
	 * @param toId
	 * @param mailKbn
	 * @throws WNoResultException
	 */
	public void changeMailToUnOpened(int mailId, int toId, int mailKbn) throws WNoResultException  {

		try {
			Where where = new SimpleWhere()
								.eq(toCamelCase(TMail.ID), mailId)
								.eq(toCamelCase(TMail.TO_ID), toId)
								.eq(toCamelCase(TMail.MAIL_KBN), mailKbn)
								.eq(toCamelCase(TMail.MAIL_STATUS),  MTypeConstants.MailStatus.OPENED)
								.eq(toCamelCase(TApplication.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

			long count = countRecords(where);

			if (count != 0) {
				TMail updateEntity = new TMail();
				updateEntity.id = mailId;
				updateEntity.mailStatus = MTypeConstants.MailStatus.UNOPENED;
				updateEntity.readingDatetime = new Timestamp(new Date().getTime());
				updateIncludesVersion(updateEntity);
			}

		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}

	/**
	 * IDで指定したスカウトメールのやりとりを全て既読にして非表示に変更する
	 * @param mailIds
	 * @param customerId
	 */
	public void changeScoutMailToUnDisplay(List<Integer> mailIds, int customerId) {

		StringBuilder sb = new StringBuilder();
		List<Object> params = new ArrayList<>();

		sb.append("SELECT * FROM t_mail");
		sb.append(" WHERE scout_mail_log_id in (");
		sb.append(" SELECT scout_mail_log_id FROM t_mail");
		sb.append(" WHERE ");
		sb.append(SqlUtils.inNoCamelize("id", mailIds.size()));
		params.addAll(mailIds);
		sb.append(" )");
		sb.append(" AND delete_flg = ?");
		sb.append(" AND to_id = ?");
		sb.append(" AND mail_kbn = ?");
		sb.append(" AND send_kbn = ?");
		sb.append(" AND sender_kbn = ?");

		params.add(DeleteFlgKbn.NOT_DELETED);
		params.add(customerId);
		params.add(MailKbn.SCOUT);
		params.add(SendKbn.RECEIVE);
		params.add(SenderKbn.MEMBER);

		List<ScoutMailListDto> resultList = jdbcManager.selectBySql
				(ScoutMailListDto.class, sb.toString(), params.toArray())
				.disallowNoResult()
				.getResultList();

		if(resultList != null && resultList.size() > 0) {
			for(ScoutMailListDto dto : resultList) {
				TMail updateEntity = new TMail();
				updateEntity.id = dto.id;
				updateEntity.readingDatetime = new Timestamp(new Date().getTime());
				updateEntity.mailStatus = MTypeConstants.MailStatus.OPENED;
				updateEntity.displayFlg = DbFlgValue.FALSE;
				updateIncludesVersion(updateEntity);
			}
		}
	}

	/**
	 * IDで指定した応募メールのやりとりを全て既読にして非表示に変更する
	 * @param mailIds
	 * @param customerId
	 */
	public void changeApplicationMailToUnDisplay(List<Integer> mailIds, int customerId) {

		StringBuilder sb = new StringBuilder();
		List<Object> params = new ArrayList<>();

		sb.append("SELECT * FROM t_mail");
		sb.append(" WHERE application_id in (");
		sb.append(" SELECT application_id FROM t_mail");
		sb.append(" WHERE ");
		sb.append(SqlUtils.inNoCamelize("id", mailIds.size()));
		params.addAll(mailIds);
		sb.append(" )");
		sb.append(" AND delete_flg = ?");
		sb.append(" AND to_id = ?");
		sb.append(" AND mail_kbn = ?");
		sb.append(" AND send_kbn = ?");

		params.add(DeleteFlgKbn.NOT_DELETED);
		params.add(customerId);
		params.add(MailKbn.APPLICCATION);
		params.add(SendKbn.RECEIVE);

		List<MailListDto> resultList = jdbcManager.selectBySql
				(MailListDto.class, sb.toString(), params.toArray())
				.disallowNoResult()
				.getResultList();

		if(resultList != null && resultList.size() > 0) {
			for(MailListDto dto : resultList) {
				TMail updateEntity = new TMail();
				updateEntity.id = dto.id;
				updateEntity.readingDatetime = new Timestamp(new Date().getTime());
				updateEntity.mailStatus = MTypeConstants.MailStatus.OPENED;
				updateEntity.displayFlg = DbFlgValue.FALSE;
				updateIncludesVersion(updateEntity);
			}
		}
	}

	/**
	 * IDで指定した質問メールのやりとりを全て既読にして非表示に変更する
	 * @param mailIds
	 * @param customerId
	 */
	public void changeObservateApplicationMailToUnDisplay(List<Integer> mailIds, int customerId) {

		StringBuilder sb = new StringBuilder();
		List<Object> params = new ArrayList<>();

		sb.append("SELECT * FROM t_mail");
		sb.append(" WHERE observate_application_id in (");
		sb.append(" SELECT observate_application_id FROM t_mail");
		sb.append(" WHERE ");
		sb.append(SqlUtils.inNoCamelize("id", mailIds.size()));
		params.addAll(mailIds);
		sb.append(" )");
		sb.append(" AND delete_flg = ?");
		sb.append(" AND to_id = ?");
		sb.append(" AND mail_kbn = ?");
		sb.append(" AND send_kbn = ?");

		params.add(DeleteFlgKbn.NOT_DELETED);
		params.add(customerId);
		params.add(MailKbn.OBSERVATE_APPLICATION);
		params.add(SendKbn.RECEIVE);

		List<MailListDto> resultList = jdbcManager.selectBySql
				(MailListDto.class, sb.toString(), params.toArray())
				.disallowNoResult()
				.getResultList();

		if(resultList != null && resultList.size() > 0) {
			for(MailListDto dto : resultList) {
				TMail updateEntity = new TMail();
				updateEntity.id = dto.id;
				updateEntity.readingDatetime = new Timestamp(new Date().getTime());
				updateEntity.mailStatus = MTypeConstants.MailStatus.OPENED;
				updateEntity.displayFlg = DbFlgValue.FALSE;
				updateIncludesVersion(updateEntity);
			}
		}
	}

	/**
	 * IDで指定したプレ応募メールのやりとりを全て既読にして非表示に変更する
	 * @param mailIds
	 * @param customerId
	 */
	public void changePreApplicationMailToUnDisplay(List<Integer> mailIds, int customerId) {

		StringBuilder sb = new StringBuilder();
		List<Object> params = new ArrayList<>();

		sb.append("SELECT * FROM t_mail");
		sb.append(" WHERE application_id in (");
		sb.append(" SELECT application_id FROM t_mail");
		sb.append(" WHERE ");
		sb.append(SqlUtils.inNoCamelize("id", mailIds.size()));
		params.addAll(mailIds);
		sb.append(" )");
		sb.append(" AND delete_flg = ?");
		sb.append(" AND to_id = ?");
		sb.append(" AND mail_kbn = ?");
		sb.append(" AND send_kbn = ?");
		sb.append(" AND sender_kbn = ?");

		params.add(DeleteFlgKbn.NOT_DELETED);
		params.add(customerId);
		params.add(MailKbn.PRE_APPLICCATION);
		params.add(SendKbn.RECEIVE);
		params.add(SenderKbn.MEMBER);

		List<MailListDto> resultList = jdbcManager.selectBySql
				(MailListDto.class, sb.toString(), params.toArray())
				.disallowNoResult()
				.getResultList();

		if(resultList != null && resultList.size() > 0) {
			for(MailListDto dto : resultList) {
				TMail updateEntity = new TMail();
				updateEntity.id = dto.id;
				updateEntity.readingDatetime = new Timestamp(new Date().getTime());
				updateEntity.mailStatus = MTypeConstants.MailStatus.OPENED;
				updateEntity.displayFlg = DbFlgValue.FALSE;
				updateIncludesVersion(updateEntity);
			}
		}
	}

	/**
	 * 顧客が操作可能な応募に紐付いているメールで、最初に送信されたメールを取得する。
	 * 親IDがNULLのものが最初の応募時に送信されたものである。
	 * @param applicationId メールID
	 * @param customerId 顧客ID
	 * @param mailKbn メール区分
	 * @return メールエンティティ
	 * @throws WNoResultException
	 */
	public TMail getFirstApplicationMailToCustomer(int applicationId, int customerId, int mailKbn) throws WNoResultException {

		try {
			TMail result = jdbcManager.from(TMail.class)
							.where(new SimpleWhere()
							.eq(WztStringUtil.toCamelCase(TMail.APPLICATION_ID), applicationId)
							.eq(WztStringUtil.toCamelCase(TMail.MAIL_KBN), mailKbn)
							.eq(WztStringUtil.toCamelCase(TMail.SEND_KBN), MTypeConstants.SendKbn.RECEIVE)
							.in(toCamelCase(TMail.SENDER_KBN), SenderKbn.MEMBER, SenderKbn.NO_MEMBER)
							.eq(toCamelCase(TMail.TO_ID), customerId)
							.isNull(toCamelCase(TMail.PARENT_MAIL_ID), true)
							.eq(WztStringUtil.toCamelCase(AbstractCommonEntity.DELETE_FLG), DeleteFlgKbn.NOT_DELETED))
							.disallowNoResult()
							.getSingleResult();

			return result;
		} catch (SNoResultException e) {
			throw new WNoResultException();
		} catch (SNonUniqueResultException e) {
			throw new WNoResultException();
		}
	}

	/**
	 * 顧客が操作可能なメールテーブルのデータを取得します。
	 * 指定したIDが操作可能なデータのみを取得します。
	 * @param id
	 * @param targetId
	 * @return
	 * @throws WNoResultException
	 */
	public TMail getEntityByUser(int id, int targetId) throws WNoResultException {

		try {
			TMail entity = findById(id);

			if (eqInt(MTypeConstants.SendKbn.RECEIVE, entity.sendKbn)
					&& (eqInt(SenderKbn.MEMBER, entity.senderKbn) || eqInt(SenderKbn.NO_MEMBER, entity.senderKbn))
					&& eqInt(targetId, entity.toId)) {
				return entity;
			} else if (eqInt(MTypeConstants.SendKbn.SEND, entity.sendKbn)
					&& eqInt(SenderKbn.CUSTOMER, entity.senderKbn)
					&& eqInt(targetId, entity.fromId)) {
				return entity;
			} else {
				throw new WNoResultException();
			}
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}

	/**
	 * 指定したIDで削除フラグを考慮せず取得します。
	 * @param id
	 * @return エンティティのリスト
	 */
	public TMail findByIdIgnoreDelete(Integer id) {
		return jdbcManager.from(entityClass)
						.where(new SimpleWhere()
						.eq("id", id))
						.disallowNoResult()
						.getSingleResult();
	}

	/**
	 * 顧客が操作可能なメールテーブルのデータを取得します。
	 * 削除フラグは考慮しません。
	 * 指定したIDが操作可能なデータのみを取得します。
	 * @param id
	 * @param targetId
	 * @return
	 * @throws WNoResultException
	 */
	public TMail getEntityIgnoreDeleteByUser(int id, int targetId) throws WNoResultException {

		try {
			TMail entity = findByIdIgnoreDelete(id);

			if (eqInt(MTypeConstants.SendKbn.RECEIVE, entity.sendKbn)
					&& (eqInt(SenderKbn.MEMBER, entity.senderKbn) || eqInt(SenderKbn.NO_MEMBER, entity.senderKbn))
					&& eqInt(targetId, entity.toId)) {
				return entity;
			} else if (eqInt(MTypeConstants.SendKbn.SEND, entity.sendKbn)
					&& eqInt(SenderKbn.CUSTOMER, entity.senderKbn)
					&& eqInt(targetId, entity.fromId)) {
				return entity;
			} else {
				throw new WNoResultException();
			}
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}

	/**
	 * 未読のスカウトメール数を取得する
	 * @param customerId
	 * @return
	 */
	public long getUnReadScoutMailCount(int customerId) {
		StringBuilder sb = new StringBuilder("");
		List<Object> params = new ArrayList<Object>();
		createUnReadMailSql(customerId, sb, params);
		return jdbcManager.getCountBySql(sb.toString(), params.toArray());
	}

	/**
	 * 未読のスカウトメールがあるか判定する
	 * @param customerId
	 * @return
	 */
	public boolean checkUnReadScoutMail(int customerId) {
		return getUnReadScoutMailCount(customerId) > 0;
	}

}