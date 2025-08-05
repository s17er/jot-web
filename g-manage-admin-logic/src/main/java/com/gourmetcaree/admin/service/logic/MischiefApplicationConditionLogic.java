package com.gourmetcaree.admin.service.logic;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.util.StringUtil;

import com.gourmetcaree.admin.service.property.MischiefApplicationConditionProperty;
import com.gourmetcaree.db.common.entity.MMischiefApplicationCondition;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.MischiefApplicationConditionService;

/**
 * いたずら応募条件ロジック
 * @author Aquarius
 *
 */
public class MischiefApplicationConditionLogic  extends AbstractAdminLogic {

	/** サービスクラス */
	@Resource
	protected MischiefApplicationConditionService mischiefApplicationConditionService;

	/**
	 * いたずら応募条件登録
	 * @param proprty
	 */
	public void insertMischiefApplicationConditionData(MischiefApplicationConditionProperty proprty) {

		mischiefApplicationConditionService.insert(proprty.mMischiefApplicationCondition);
	}

	/**
	 * いたずら応募条件検索
	 * @param property
	 * @throws WNoResultException
	 */
	public void getMischiefApplicationConditionList(MischiefApplicationConditionProperty property) throws WNoResultException  {

		checkEmptyProperty(property);

		property.entityList = mischiefApplicationConditionService.getMischiefApplicationConditionList(
				createWhere(property.mMischiefApplicationCondition));
	}

	/**
	 * いたずら応募条件取得
	 * @param id
	 * @return
	 */
	public MMischiefApplicationCondition findById(int id) {
		return mischiefApplicationConditionService.findById(id);
	}

	/**
	 * いたずら応募条件更新
	 * @param property
	 */
	public void update(MischiefApplicationConditionProperty property) {

		mischiefApplicationConditionService.update(property.mMischiefApplicationCondition);
	}

	/**
	 * プロパティがnullの場合は、エラーを返す。
	 * @param property チェックするプロパティ
	 */
	private void checkEmptyProperty(MischiefApplicationConditionProperty property) {

		// プロパティがnullの場合はエラー
		if (property == null || property.mMischiefApplicationCondition == null) {
			throw new IllegalArgumentException("引数がnullです。");
		}
	}

	/**
	 * 検索条件を作成
	 * @param entity
	 * @return
	 */
	private SimpleWhere createWhere(MMischiefApplicationCondition entity) {

		SimpleWhere where = new SimpleWhere();

		where.eq(StringUtil.camelize(MMischiefApplicationCondition.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		if(StringUtils.isNotBlank(entity.name)) {
			where.like(StringUtil.camelize(MMischiefApplicationCondition.NAME), "%"+entity.name+"%");
		}

		if(StringUtils.isNotBlank(entity.nameKana)) {
			where.like(StringUtil.camelize(MMischiefApplicationCondition.NAME_KANA), "%"+entity.nameKana+"%");
		}

		if(!ObjectUtils.equals(entity.prefecturesCd, null)) {
			where.eq(StringUtil.camelize(MMischiefApplicationCondition.PREFECTURES_CD), entity.prefecturesCd);
		}

		if(StringUtils.isNotBlank(entity.municipality)) {
			where.like(StringUtil.camelize(MMischiefApplicationCondition.MUNICIPALITY), "%"+entity.municipality+"%");
		}

		if(StringUtils.isNotBlank(entity.address)) {
			where.like(StringUtil.camelize(MMischiefApplicationCondition.ADDRESS), "%"+entity.address+"%");
		}

		if(StringUtils.isNotBlank(entity.phoneNo1)) {
			where.like(StringUtil.camelize(MMischiefApplicationCondition.PHONE_NO1), "%"+entity.phoneNo1+"%");
		}

		if(StringUtils.isNotBlank(entity.phoneNo2)) {
			where.like(StringUtil.camelize(MMischiefApplicationCondition.PHONE_NO2), "%"+entity.phoneNo2+"%");
		}

		if(StringUtils.isNotBlank(entity.phoneNo3)) {
			where.like(StringUtil.camelize(MMischiefApplicationCondition.PHONE_NO3), "%"+entity.phoneNo3+"%");
		}

		if(StringUtils.isNotBlank(entity.mailAddress)) {
			where.like(StringUtil.camelize(MMischiefApplicationCondition.MAIL_ADDRESS), "%"+entity.mailAddress+"%");
		}

		if(!ObjectUtils.equals(entity.memberFlg, null)) {
			where.eq(StringUtil.camelize(MMischiefApplicationCondition.MEMBRE_FLG), entity.memberFlg);
		}

		if(!ObjectUtils.equals(entity.terminalKbn, null)) {
			where.eq(StringUtil.camelize(MMischiefApplicationCondition.TERMINAL_KBN), entity.terminalKbn);
		}

		if(StringUtils.isNotBlank(entity.applicationSelfPr)) {
			where.like(StringUtil.camelize(MMischiefApplicationCondition.APPLICATION_SELF_PR), "%"+entity.applicationSelfPr+"%");
		}

		return where;

	}
}
