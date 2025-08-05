package com.gourmetcaree.admin.service.logic;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.gourmetcaree.admin.service.property.MemberProperty;
import com.gourmetcaree.db.common.entity.TCareerHistory;
import com.gourmetcaree.db.common.entity.TCareerHistoryAttribute;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.CareerHistoryAttributeService;
import com.gourmetcaree.db.common.service.CareerHistoryService;
import com.gourmetcaree.db.common.service.MemberAttributeService;
import com.gourmetcaree.db.common.service.MemberHopeCityService;
import com.gourmetcaree.db.common.service.MemberService;
import com.gourmetcaree.db.common.service.SchoolHistoryService;

/**
 * 会員に関するロジッククラスです。
 * @author Takahiro Kimura
 * @version 1.0
 *
 */
public class MemberLogic extends AbstractAdminLogic {

	/** 会員サービス */
	@Resource
	protected MemberService memberService;

	/** 会員属性サービス */
	@Resource
	protected MemberAttributeService memberAttributeService;

	/** 学歴サービス */
	@Resource
	protected SchoolHistoryService schoolHistoryService;

	/** 職歴サービス */
	@Resource
	protected CareerHistoryService careerHistoryService;

	/** 職歴属性サービス */
	@Resource
	protected CareerHistoryAttributeService careerHistoryAttributeService;

	/** 会員希望勤務地サービス */
	@Resource
	private MemberHopeCityService memberHopeCityService;


	/**
	 * 会員情報を更新
	 * @param property 会員プロパティ
	 */
	public void updateMemberData(MemberProperty property) {

		// 会員情報を更新
		updateMember(property);

		// 会員属性を更新
		updateMemberAttribute(property);

		// 学歴を更新
		updateSchoolHistory(property);

		// 職歴を更新
		updateCareerHistory(property);

		// 希望勤務地を更新
		deleteInsertHopeCity(property);

	}

	/**
	 * 会員マスタを更新
	 * @param property 会員プロパティ
	 */
	private void updateMember(MemberProperty property) {

		memberService.updateWithNull(property.member);
	}

	/**
	 * 会員属性テーブルを更新(Delete,Insert)
	 * @param property 会員プロパティ
	 */
	private void updateMemberAttribute(MemberProperty property) {

		// 会員属性を削除
		memberAttributeService.deleteMemberAttributeByMemberId(property.member.id);

		// 会員属性が空でない場合
		if (property.mMemberAttributeList != null && !property.mMemberAttributeList.isEmpty()) {
			// 会員属性を登録
			memberAttributeService.insertBatch(property.mMemberAttributeList);
		}
	}

	/**
	 * 学歴テーブルを更新
	 * @param property 会員プロパティ
	 */
	private void updateSchoolHistory(MemberProperty property) {

		// 学歴を削除
		schoolHistoryService.deleteSchoolHistoryByMemberId(property.member.id);

		// 学歴を登録
		if (property.tSchoolHistory != null && StringUtils.isNotEmpty(property.tSchoolHistory.schoolName)) {
			schoolHistoryService.insert(property.tSchoolHistory);
		}
	}

	/**
	 * 職歴・職歴属性を更新
	 * @param property 会員プロパティ
	 */
	private void updateCareerHistory(MemberProperty property) {

		try {
			// 会員IDをキーに職歴・職歴属性データを取得
			List<TCareerHistory> careerList = careerHistoryService.getCareerHistoryDataByMemberId(property.member.id);

			// 職歴を削除
			deleteCareerHistory(careerList);

			// 職歴を登録
			if (property.tCareerHistoryList != null && !property.tCareerHistoryList.isEmpty()) {
				insertCareerHistory(property);
			}

		} catch (WNoResultException e) {
			// 職歴が存在しない場合は、登録のみ実行
			// 職歴を登録
			if (property.tCareerHistoryList != null && !property.tCareerHistoryList.isEmpty()) {
				insertCareerHistory(property);
			}
		}
	}

	/**
	 * 職歴を削除
	 * @param careerList 職歴リスト
	 */
	private void deleteCareerHistory(List<TCareerHistory> careerList) {

		// 職歴を削除
		careerHistoryService.deleteBatch(careerList);

		// 職歴属性を削除
		deleteCareerHistoryAttr(careerList);
	}

	/**
	 * 職歴を登録
	 * @param careerHistoryList 職歴リスト
	 */
	private void insertCareerHistory(MemberProperty property) {

		// 職歴を登録
		careerHistoryService.insertBatch(property.tCareerHistoryList);

		// 職歴属性を登録
		insertCareerHistoryAttr(property.tCareerHistoryList);
	}

	/**
	 * 職歴属性削除データを削除
	 * @param careerList 職歴リスト
	 */
	private void deleteCareerHistoryAttr(List<TCareerHistory> careerList) {

		List<TCareerHistoryAttribute> attrEntityList = new ArrayList<TCareerHistoryAttribute>();

		for (TCareerHistory entity : careerList) {
			for (TCareerHistoryAttribute attrEntity : entity.tCareerHistoryAttributeList) {
				attrEntityList.add(attrEntity);
			}
		}

		if (attrEntityList.size() > 0) {
			careerHistoryAttributeService.deleteBatch(attrEntityList);
		}
	}

	/**
	 * 希望勤務地を削除登録
	 * @param property
	 */
	private void deleteInsertHopeCity(MemberProperty property) {
		memberHopeCityService.deleteByMemberId(property.member.id);
		if (CollectionUtils.isEmpty(property.mMemberHopeCityList)) {
			return;
		}
		memberHopeCityService.insertBatch(property.mMemberHopeCityList);
	}

	/**
	 * 職歴属性を登録
	 * @param careerHistoryList 職歴リスト
	 */
	private void insertCareerHistoryAttr(List<TCareerHistory> careerHistoryList) {

		List<TCareerHistoryAttribute> attrEntityList = new ArrayList<TCareerHistoryAttribute>();

		for (TCareerHistory entity : careerHistoryList) {
			for (TCareerHistoryAttribute attrEntity : entity.tCareerHistoryAttributeList) {
				attrEntity.careerHistoryId = entity.id;
				attrEntityList.add(attrEntity);
			}
		}

		if (attrEntityList.size() > 0) {
			careerHistoryAttributeService.insertBatch(attrEntityList);
		}
	}


}
