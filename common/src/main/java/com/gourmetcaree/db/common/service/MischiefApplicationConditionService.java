package com.gourmetcaree.db.common.service;

import java.util.List;

import javax.persistence.NoResultException;

import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.MMischiefApplicationCondition;
import com.gourmetcaree.db.common.exception.WNoResultException;

import jp.co.whizz_tech.commons.WztStringUtil;

/**
 * いたずら応募条件マスタのサービスクラスです。
 * @version 1.0
 */
public class MischiefApplicationConditionService extends AbstractGroumetCareeBasicService<MMischiefApplicationCondition> {

	private static final String[] NULLABLE_UPDATE_COLUMN  = new String[]{
			WztStringUtil.toCamelCase(MMischiefApplicationCondition.NAME),
			WztStringUtil.toCamelCase(MMischiefApplicationCondition.NAME_KANA),
			WztStringUtil.toCamelCase(MMischiefApplicationCondition.PREFECTURES_CD),
			WztStringUtil.toCamelCase(MMischiefApplicationCondition.MUNICIPALITY),
			WztStringUtil.toCamelCase(MMischiefApplicationCondition.ADDRESS),
			WztStringUtil.toCamelCase(MMischiefApplicationCondition.ADDRESS),
			WztStringUtil.toCamelCase(MMischiefApplicationCondition.PHONE_NO1),
			WztStringUtil.toCamelCase(MMischiefApplicationCondition.PHONE_NO2),
			WztStringUtil.toCamelCase(MMischiefApplicationCondition.PHONE_NO3),
			WztStringUtil.toCamelCase(MMischiefApplicationCondition.MAIL_ADDRESS),
			WztStringUtil.toCamelCase(MMischiefApplicationCondition.MEMBRE_FLG),
			WztStringUtil.toCamelCase(MMischiefApplicationCondition.TERMINAL_KBN),
			WztStringUtil.toCamelCase(MMischiefApplicationCondition.APPLICATION_SELF_PR),
		};


	/**
	 * 検索処理
	 * @param where
	 * @return
	 * @throws WNoResultException
	 */
	public List<MMischiefApplicationCondition> getMischiefApplicationConditionList(SimpleWhere where) throws WNoResultException{

		try {
			return jdbcManager
					.from(MMischiefApplicationCondition.class)
					.where(where)
					.orderBy("id DESC")
					.disallowNoResult()
					.getResultList();
		} catch(NoResultException e) {
			throw new WNoResultException("結果が0件です。");
		}
	}

	/**
	 * 更新処理
	 */
	public int update(MMischiefApplicationCondition entity) {
		return super.updateWithNull(entity, NULLABLE_UPDATE_COLUMN);
	}

}