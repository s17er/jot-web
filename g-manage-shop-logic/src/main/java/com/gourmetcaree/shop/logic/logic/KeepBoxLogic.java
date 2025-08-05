package com.gourmetcaree.shop.logic.logic;

import java.util.List;

import javax.annotation.Resource;

import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.db.common.entity.MMember;
import com.gourmetcaree.db.common.entity.TScoutConsideration;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.ScoutConsiderationService;
import com.gourmetcaree.shop.logic.dto.MemberInfoDto;
import com.gourmetcaree.shop.logic.property.MemberProperty;

import jp.co.whizz_tech.commons.WztStringUtil;

/**
 * キープBOXに関するロジッククラスです。
 * @author Takahiro Kimura
 * @version 1.0
 *
 */
@Component(instance=InstanceType.SINGLETON)
public class KeepBoxLogic  extends AbstractShopLogic {


	/** スカウト検討中サービス */
	@Resource
	protected ScoutConsiderationService scoutConsiderationService;

	/** 会員ロジック */
	@Resource
	protected MemberLogic memberLogic;


	/**
	 * キープBOXに追加する
	 * @param customerId 顧客ID
	 * @param memberId 会員ID
	 */
	public void doAddkeepBox(int customerId, int memberId) {

		// 既にキープされていないかチェック
		// 既にキープされている場合追加しない
		if (!scoutConsiderationService.isAlredyKeep(customerId, memberId)) {
			// スカウト検討中テーブルに登録
			TScoutConsideration entity = new TScoutConsideration();
			entity.customerId = customerId;
			entity.memberId = memberId;

			scoutConsiderationService.insert(entity);
		}
	}

	/**
	 * キープBOXから削除する
	 * @param customerId 顧客ID
	 * @param memberId 会員ID
	 */
	public void doDeletekeepBox(int customerId, int memberId) {

		// 既にキープされているかチェック
		// 既にキープされていない場合削除しない
		if (scoutConsiderationService.isAlredyKeep(customerId, memberId)) {
			try {
				List<TScoutConsideration> list = scoutConsiderationService.findByCondition(new SimpleWhere()
						.eq(WztStringUtil.toCamelCase(TScoutConsideration.CUSTOMER_ID), customerId)
						.eq(WztStringUtil.toCamelCase(TScoutConsideration.MEMBRE_ID), memberId));
				for(TScoutConsideration entity : list) {
					scoutConsiderationService.deleteIgnoreVersion(entity);
				}
			} catch (WNoResultException e) {
				return;
			}
		}
	}

	/**
	 * キープBOXの会員リストを返す
	 * @param property キープBOXプロパティ
	 * @return 会員エンティティリスト
	 * @throws WNoResultException
	 */
	public List<MMember> doSelectKeepList(MemberProperty property) throws WNoResultException {

		return scoutConsiderationService.getKeepList(property.pageNavi, property.customerId, property.sortKey, property.targetPage);
	}

	/**
	 * キープBOX一覧表示データを生成
	 * @param memberEntityList 会員エンティティリスト
	 * @param customerId 顧客ID
	 * @return
	 */
	public List<MemberInfoDto> convertShowList(MemberProperty property) {

		return memberLogic.convertShowList(property);
	}
}
