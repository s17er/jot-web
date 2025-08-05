package com.gourmetcaree.admin.service.logic;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import jp.co.whizz_tech.commons.WztStringUtil;

import org.apache.commons.lang.StringUtils;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.admin.service.dto.ScoutMailLogDto;
import com.gourmetcaree.admin.service.dto.ScoutMailRemainRetDto;
import com.gourmetcaree.admin.service.property.CustomerProperty;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TScoutMailLog;
import com.gourmetcaree.db.common.entity.TScoutMailManage;
import com.gourmetcaree.db.common.entity.TWeb;
import com.gourmetcaree.db.common.entity.VActiveScoutMail;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.ActiveScoutMailService;
import com.gourmetcaree.db.common.service.CustomerService;
import com.gourmetcaree.db.common.service.ScoutMailLogService;
import com.gourmetcaree.db.common.service.ScoutMailManageService;

/**
 * スカウトメールに関するロジッククラスです
 * @author Takehiro Nakamori
 *
 */
public class ScoutMailLogic extends AbstractAdminLogic {

	/** スカウトメール管理サービス */
	@Resource
	private ScoutMailManageService scoutMailManageService;

	/** スカウトメールログサービス */
	@Resource
	private ScoutMailLogService scoutMailLogService;

	/** 顧客サービス */
	@Resource
	private CustomerService customerService;

	/** 使用可能スカウトメールVIEWサービス */
	@Resource
	private ActiveScoutMailService activeScoutMailService;

	/**
	 * 原稿確定によるスカウトメールの付与
	 */
	public void addScoutMailByFixWeb(TWeb web, int addCount) {
		// スカウトメール追加ができなければリターン
		if (canNotAddScoutMailByFixWeb(web)) {
			return;
		}

		TScoutMailManage manageEntity = new TScoutMailManage();
		manageEntity.customerId = web.customerId;
		manageEntity.scoutRemainCount = addCount;
		manageEntity.scoutMailKbn = MTypeConstants.ScoutMailKbn.FIX_WEB;
		manageEntity.webId = web.id;

		scoutMailManageService.insert(manageEntity);

		insertAddLog(manageEntity);
	}

	public void deleteFreeScoutMail(TWeb web) {
		try {
			TScoutMailManage entity = jdbcManager.from(TScoutMailManage.class)
											.where(new SimpleWhere()
											.eq(WztStringUtil.toCamelCase(TScoutMailManage.WEB_ID), web.id)
											.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
											.disallowNoResult()
											.getSingleResult();

			scoutMailManageService.logicalDelete(entity);
		} catch (SNoResultException e) {
			return;
		}
	}


	/**
	 * スカウトメールを追加します。
	 * @param customerId
	 * @param addCount
	 * @param useEndDatetime
	 */
	public void addScoutMail(int customerId, int addCount, Date useEndDatetime) {
		TScoutMailManage manageEntity = new TScoutMailManage();
		// 顧客が存在しない場合は追加できない。
		if (!customerService.isCustomerExist(customerId)) {
			throw new FraudulentProcessException("顧客が存在しないためスカウトメールを追加できません。");
		}
		manageEntity.customerId = customerId;
		manageEntity.scoutRemainCount = addCount;
		manageEntity.scoutMailKbn = MTypeConstants.ScoutMailKbn.BOUGHT;
		scoutMailManageService.insert(manageEntity);
		insertAddLog(manageEntity);
	}


	/**
	 * スカウトメール追加ログをインサート
	 * @param manageEntity
	 */
	private void insertAddLog(TScoutMailManage manageEntity) {
		TScoutMailLog entity = new TScoutMailLog();
		entity.addDatetime = new Timestamp(new Date().getTime());
		entity.addScoutCount = manageEntity.scoutRemainCount;
		entity.scoutManageId = manageEntity.id;
		entity.scoutMailLogKbn = MTypeConstants.ScoutMailLogKbn.ADD;
		scoutMailLogService.insert(entity);
	}


	/**
	 * 原稿確定によってスカウトメールを追加できないかどうか
	 * @param web
	 * @return
	 */
	private boolean canNotAddScoutMailByFixWeb(TWeb web) {
		// 顧客登録がない場合、追加できない
		if (web.customerId == null) {
			return true;
		}

		// 指定IDの顧客が存在しない場合、追加できない
		if (!customerService.isCustomerExist(web.customerId)) {
			throw new FraudulentProcessException("顧客が存在しないためスカウトメールを追加できません。");
		}
		try {
			// すでに登録されている場合、追加できない。
			scoutMailManageService.findByCustomerIdAndWebId(web.customerId, web.id);
			return true;

		// 登録がなければ追加できる。
		} catch (WNoResultException e) {
			return false;
		}
	}

	/**
	 * スカウトメール残数を作成
	 * @param property
	 * @return
	 */
	public ScoutMailRemainRetDto createScoutMailRemain(CustomerProperty property) {
		ScoutMailRemainRetDto dto = new ScoutMailRemainRetDto();


		dto.freeScoutMailCount = activeScoutMailService.getScoutRemainCount(
												property.mCustomer.id,
												MTypeConstants.ScoutMailKbn.FIX_WEB);

		dto.unlimitedScoutMailCount = activeScoutMailService.getScoutRemainCount(
												property.mCustomer.id,
												MTypeConstants.ScoutMailKbn.UNLIMITED);

		try {
			dto.boughtScoutMailManageList = activeScoutMailService.findByCondition(
												new SimpleWhere()
													.eq(WztStringUtil.toCamelCase(
															VActiveScoutMail.CUSTOMER_ID),
															property.mCustomer.id)
													.eq(WztStringUtil.toCamelCase(
															VActiveScoutMail.SCOUT_MAIL_KBN),
															MTypeConstants.ScoutMailKbn.BOUGHT),
													SqlUtils.desc(VActiveScoutMail.USE_END_DATETIME));
		} catch (WNoResultException e) {
			dto.boughtScoutMailManageList = new ArrayList<VActiveScoutMail>();
		}

		return dto;
	}


	/**
	 * スカウトメールのログリストを取得します。
	 * @param customerId
	 * @param scoutMailLogKbn
	 * @param scoutMailKbn
	 * @return
	 * @throws WNoResultException
	 */
	public List<ScoutMailLogDto> getScoutMailLogList(int customerId, int[] scoutMailLogKbnArray, int[] scoutMailKbnArray, String sortKey ) throws WNoResultException {
		StringBuilder sb = new StringBuilder("");
		List<Object> params = new ArrayList<Object>();

		createGetScoutMailLogSql(sb, params, customerId, scoutMailLogKbnArray, scoutMailKbnArray, sortKey);

		try {
			return jdbcManager.selectBySql(ScoutMailLogDto.class, sb.toString(), params.toArray())
							.disallowNoResult()
							.getResultList();
		} catch (SNoResultException e) {
			throw new WNoResultException(e);
		}
	}

	/**
	 * スカウトメール追加のログリストを取得します。
	 * @param customerId
	 * @return
	 * @throws WNoResultException
	 */
	public List<ScoutMailLogDto> getScoutMailAddLogList(int customerId) throws WNoResultException {
		return getScoutMailLogList(customerId,
									new int[] {MTypeConstants.ScoutMailLogKbn.ADD,
												MTypeConstants.ScoutMailLogKbn.ADD_MANUAL},
									new int[] {
												MTypeConstants.ScoutMailKbn.BOUGHT,
												MTypeConstants.ScoutMailKbn.UNLIMITED},
									"MANAGE.scout_mail_kbn ASC, LOGS.add_datetime DESC"
		);
	}



	/**
	 * スカウトメールログリスト取得のSQLを作成します。
	 * @param sb
	 * @param params
	 * @param customerId
	 * @param scoutMailLogKbnArray
	 * @param scoutMailKbnArray
	 */
	private void createGetScoutMailLogSql(StringBuilder sb, List<Object> params,
											int customerId, int[] scoutMailLogKbnArray,
											int[] scoutMailKbnArray, String sortKey) {
		sb.append(" SELECT ");
		sb.append("    LOGS.id, ");
		sb.append("    LOGS.scout_manage_id, ");
		sb.append("    MANAGE.customer_id, ");
		sb.append("    MANAGE.scout_mail_kbn, ");
		sb.append("    LOGS.member_id, ");
		sb.append("    LOGS.send_datetime, ");
		sb.append("    LOGS.add_scout_count, ");
		sb.append("    LOGS.add_datetime, ");
		sb.append("    LOGS.scout_mail_log_kbn ");
		sb.append(" FROM ");
		sb.append("    t_scout_mail_log LOGS INNER JOIN t_scout_mail_manage MANAGE ");
		sb.append("    ON LOGS.scout_manage_id = MANAGE.id ");
		sb.append(" WHERE ");
		sb.append("    MANAGE.customer_id = ? ");
		sb.append("    AND MANAGE.scout_mail_kbn IN ( ");
		sb.append(			SqlUtils.getQMarks(scoutMailKbnArray.length));
		sb.append("    ) ");
		sb.append("    AND LOGS.scout_mail_log_kbn IN ( ");
		sb.append(			SqlUtils.getQMarks(scoutMailLogKbnArray.length));
		sb.append("    ) ");
		sb.append("    AND MANAGE.delete_flg = ? ");
		sb.append("    AND LOGS.delete_flg = ? ");
		sb.append(" ORDER BY ");

		sb.append(" MANAGE.scout_mail_kbn ASC, ");

		if (StringUtils.isNotBlank(sortKey)) {
			sb.append(sortKey);
		}

		params.add(customerId);
		for (int scoutMailKbn : scoutMailKbnArray) {
			params.add(scoutMailKbn);
		}

		for (int scoutMailLogKbn : scoutMailLogKbnArray) {
			params.add(scoutMailLogKbn);
		}
		params.add(DeleteFlgKbn.NOT_DELETED);
		params.add(DeleteFlgKbn.NOT_DELETED);
	}

}
