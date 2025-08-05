package com.gourmetcaree.common.logic.tempMember;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.Beans;

import com.google.common.collect.Lists;
import com.gourmetcaree.accessor.tempMember.CareerHistoryDtoAccessor;
import com.gourmetcaree.common.logic.AbstractGourmetCareeLogic;
import com.gourmetcaree.common.property.tempMember.TempMemberProperty;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.member.TTempMemberArea;
import com.gourmetcaree.db.common.entity.member.TTempMemberAttribute;
import com.gourmetcaree.db.common.entity.member.TTempMemberCareerHistory;
import com.gourmetcaree.db.common.entity.member.TTempMemberCareerHistoryAttribute;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.service.member.TempMemberAreaService;
import com.gourmetcaree.db.common.service.member.TempMemberAttributeService;
import com.gourmetcaree.db.common.service.member.TempMemberCareerHistoryAttributeService;
import com.gourmetcaree.db.common.service.member.TempMemberCareerHistoryService;
import com.gourmetcaree.db.common.service.member.TempMemberSchoolHistoryService;
import com.gourmetcaree.db.common.service.member.TempMemberService;

import jp.co.whizz_tech.common.sastruts.converter.ZenkakuKanaConverter;


/**
 * 仮会員のINSERT/UPATEを行うロジック
 * @author nakamori
 *
 */
public class SaveLogic extends AbstractGourmetCareeLogic {

	private static final Logger log = Logger.getLogger(SaveLogic.class);

	@Resource
	private TempMemberAreaService tempMemberAreaService;

	@Resource
	private TempMemberAttributeService tempMemberAttributeService;

	@Resource
	private TempMemberService tempMemberService;

	@Resource(name = "member_tempMemberCareerHistoryAttributeService")
	private TempMemberCareerHistoryAttributeService careerHistoryAttrService;

	@Resource(name = "member_tempMemberCareerHistoryService")
	private TempMemberCareerHistoryService tempCareerHistoryService;

	@Resource(name = "member_tempMemberSchoolHistoryService")
	private TempMemberSchoolHistoryService tempSchoolHistoryService;




	public <E extends CareerHistoryDtoAccessor> void save(TempMemberProperty<E> property) {

		// 会員マスタを登録
		saveMember(property);

		// 会員エリアマスタを登録
		updateMemberArea(property);

		// 会員属性マスタを登録
		updateMemberAttribute(property);

		// WEB履歴書学歴を登録
		updateSchoolHistory(property);


		// WEB履歴書職歴を登録
		updateCareerHistory(property);

//		// ログイン履歴を登録
//		insertLoginHistory(property);
//
//		relateAdvancedRegistrationEntry(property.mMember);
	}


	public <E extends CareerHistoryDtoAccessor> void update(TempMemberProperty<E> property) {
		// 会員マスタを登録
		updateMember(property);

		// 会員エリアマスタを登録
		updateMemberArea(property);

		// 会員属性マスタを登録
		updateMemberAttribute(property);

		// WEB履歴書学歴を登録
		updateSchoolHistory(property);


		// WEB履歴書職歴を登録
		updateCareerHistory(property);
	}

	/**
	 * 会員データを登録する
	 * @param property 会員プロパティ
	 */
	private <E extends CareerHistoryDtoAccessor> void saveMember(TempMemberProperty<E> property) {

		// エンティティのチェック
		if (property.member == null) {
			throw new IllegalArgumentException("引数がnullです。");
		}


		// 会員データの登録
		tempMemberService.saveMemberData(property.member);

		log.info("仮会員を登録しました。" + property.member);
	}

	/**
	 * 会員データを登録する
	 * @param property 会員プロパティ
	 */
	private <E extends CareerHistoryDtoAccessor> void updateMember(TempMemberProperty<E> property) {
		// エンティティのチェック
		if (property.member == null) {
			throw new IllegalArgumentException("引数がnullです。");
		}

		tempMemberService.update(property.member);
		log.info("仮会員を更新しました。" + property.member);
	}


	/**
	 * 仮会員エリアマスタを登録します。
	 */
	private <E extends CareerHistoryDtoAccessor> void updateMemberArea(TempMemberProperty<E> property) {


		List<TTempMemberArea> entityList = Lists.newArrayList();
		for (String area : property.areaList) {
			TTempMemberArea entity = new TTempMemberArea();

			// 会員エリア
			entity.areaCd = Integer.parseInt(area);
			// 削除フラグ
			entity.deleteFlg = DeleteFlgKbn.NOT_DELETED;
			entityList.add(entity);
		}

		// 登録
		tempMemberAreaService.deleteInsert(property.member.id, entityList);

	}

	/**
	 * 会員属性マスタを登録します。
	 * @param property 会員プロパティ
	 */
	private <E extends CareerHistoryDtoAccessor> void updateMemberAttribute(TempMemberProperty<E> property) {


		// TODO
		// 登録する値がマップにセットされている場合
		if (GourmetCareeUtil.isNotEmptyMap(property.attrMap)) {
			// 登録用Mapデータを作成する
			property.memberAttrList = convertInsertMemberAttrList(property);

		// エンティティにセットされていた場合
		} else if (CollectionUtils.isNotEmpty(property.memberAttrList)) {

			// 属性エンティティに会員IDをセットする
			for (TTempMemberAttribute attrEntity : property.memberAttrList) {
				// 削除フラグ
				attrEntity.deleteFlg = DeleteFlgKbn.NOT_DELETED;
			}
		}

		// 会員属性を登録
		tempMemberAttributeService.deleteInsert(property.member.id, property.memberAttrList);
	}


	/**
	 * プロパティの会員属性マスタの値がセットされたMapを<br />
	 * 登録する会員属性マスタのリストに変換して返します。
	 * @param property 登録するMapを保持した会員プロパティ
	 * @param memberId セットする会員ID
	 * @return 登録用の会員属性マスタリスト
	 */
	private <E extends CareerHistoryDtoAccessor> List<TTempMemberAttribute> convertInsertMemberAttrList(TempMemberProperty<E> property) {


		Map<String, List<String>> attrMap = property.attrMap;
		int memberId = property.member.id;

		// 登録する会員属性を保持するリスト
		List<TTempMemberAttribute> insertEntityList = Lists.newArrayList();

		/* ---- 会員属性のリストに値をセットする ----*/
		//資格区分
		setMemberAttributeList(attrMap.get(MTypeConstants.QualificationKbn.TYPE_CD), memberId, MTypeConstants.QualificationKbn.TYPE_CD, insertEntityList);
		// 希望職種区分
		setMemberAttributeList(attrMap.get(MTypeConstants.JobKbn.TYPE_CD), memberId, MTypeConstants.JobKbn.TYPE_CD, insertEntityList);
		// 希望業種区分
		setMemberAttributeList(attrMap.get(MTypeConstants.IndustryKbn.TYPE_CD), memberId, MTypeConstants.IndustryKbn.TYPE_CD, insertEntityList);
		// 希望勤務地区分
		setMemberAttributeList(attrMap.get(MTypeConstants.WebAreaKbn.getTypeCd(property.areaCd)), memberId, MTypeConstants.ShutokenWebAreaKbn.TYPE_CD, insertEntityList);

		// リストを返却
		return insertEntityList;
	}

	/**
	 * 会員属性マスタに登録する値をセットします。
	 * @param attrList セットする値を保持したリスト
	 * @param memberId 会員ID
	 * @param typeCd 属性コード
	 * @param setList データをセットして返すリスト
	 *
	 * TODO エリアまわりの部分を修正。 エンティティを生成した地点でインサート
	 */
	private void setMemberAttributeList(List<String> attrList, int memberId, String typeCd, List<TTempMemberAttribute> returnList) {

		// 値がnullの場合は処理しない
		if (attrList == null || attrList.isEmpty()) return;

		// 値のリストをセット
		for (String value : attrList) {

			TTempMemberAttribute entity = new TTempMemberAttribute();
			// 会員ID
			entity.tempMemberId = memberId;

			// 属性コード
			int attrVal = Integer.parseInt(value);

			if (typeCd.equals(MTypeConstants.ShutokenWebAreaKbn.TYPE_CD)) {
			// 属性コード[100001 ~ 200000] は海外
				if (100001 <= attrVal &&  attrVal <= 200000){
					entity.attributeCd = MTypeConstants.ShutokenForeignAreaKbn.TYPE_CD;
				// 属性コード[200001 ~ ] は仙台
				} else if (200001 <= attrVal) {
					entity.attributeCd = MTypeConstants.SendaiWebAreaKbn.TYPE_CD;
				// 属性コード[ ~ 100000] は首都圏
				} else if (attrVal <= 100000) {
					entity.attributeCd = MTypeConstants.ShutokenWebAreaKbn.TYPE_CD;
				// ここには入ってこない想定
				} else {
					entity.attributeCd = MTypeConstants.ShutokenWebAreaKbn.TYPE_CD;
				}
			} else {
				entity.attributeCd = typeCd;
			}

			// 属性値
			entity.attributeValue = attrVal;
			// 削除フラグ
			entity.deleteFlg = DeleteFlgKbn.NOT_DELETED;

			// リストにセット
			returnList.add(entity);
		}
	}


	/**
	 * WEB履歴書学歴を登録します。
	 * @param property 会員プロパティ
	 */
	private <E extends CareerHistoryDtoAccessor> void updateSchoolHistory(TempMemberProperty<E> property) {

		// エンティティのチェック(空の場合は処理しない)
		if (property.schoolHistory == null) {
			tempSchoolHistoryService.deleteByTempMemberId(property.member.id);
			return;
		}


		// WEB履歴書学歴を登録
		tempSchoolHistoryService.deleteInsert(property.member.id, property.schoolHistory);
	}

	/**
	 * WEB履歴書職歴と、それに紐付くWEB履歴書職歴属性を登録します。
	 * @param property 会員プロパティ
	 */
	private <E extends CareerHistoryDtoAccessor> void updateCareerHistory(TempMemberProperty<E> property) {

		// 職歴Dtoにセットされている場合
		// ただし、値が空の場合は処理しない
		if ((property.careerHistoryDtoList != null && !property.careerHistoryDtoList.isEmpty())) {

			// Dtoリストの値をエンティティのリストに変換する
			convertInsertCareerHistoryList(property, property.member.id);

		// 値がエンティティにセットされている場合
		} else if (property.careerHistoryList != null && !property.careerHistoryList.isEmpty()) {

			// エンティティに値をセット
			for (TTempMemberCareerHistory entity : property.careerHistoryList) {
				// 会員IDをセット
				entity.tempMemberId = property.member.id;
				// 削除フラグをセット
				entity.deleteFlg = DeleteFlgKbn.NOT_DELETED;
			}
		}


		// WEB履歴書職歴を登録
		List<Integer> deletedHistoryIdList = tempCareerHistoryService.deleteInsert(property.member.id, property.careerHistoryList);
		// WEB履歴書職歴属性の登録
		updateTCareerHistoryAttribute(property, deletedHistoryIdList);
	}

	/**
	 * WEB履歴書職歴属性を登録
	 * @param property 会員プロパティ
	 */
	private <E extends CareerHistoryDtoAccessor> void updateTCareerHistoryAttribute(TempMemberProperty<E> property, List<Integer> deletedHistoryIdList) {

		// WEB履歴書職歴エンティティリストのチェック(空の場合は処理しない)
		if (property.careerHistoryList == null || property.careerHistoryList.isEmpty()) return;

		// WEB履歴書職歴属性をリセット
		property.careerHistoryAttributeList = Lists.newArrayList();

		// WEB履歴書職歴の値をWEB履歴書職歴属性にセットする
		for (TTempMemberCareerHistory entity : property.careerHistoryList) {

			// WEB履歴書属性をセットする
			for (TTempMemberCareerHistoryAttribute attrEntity : entity.tCareerHistoryAttributeList) {
				// WEB履歴書職歴IDをセット
				attrEntity.tempMemberCareerHistoryId = entity.id;
				// 削除フラグをセット
				attrEntity.deleteFlg = DeleteFlgKbn.NOT_DELETED;

				// 登録のためWEB履歴書職歴属性にセットする
				property.careerHistoryAttributeList.add(attrEntity);
			}
		}


		careerHistoryAttrService.deleteByTempMemberCareerHistoryIds(deletedHistoryIdList);
		// データが登録されている場合は処理をする
		if (property.careerHistoryAttributeList != null && !property.careerHistoryAttributeList.isEmpty()) {
			// WEB履歴書職歴属性を登録
			careerHistoryAttrService.insertBatch(property.careerHistoryAttributeList);
		}
	}


	/**
	 * WEB履歴書職歴に登録するプロパティにセットするDtoリストの値を<br />
	 * エンティティのリストに変換して、プロパティのリストにセットします。
	 * @param property 会員プロパティ
	 * @param memberId 会員ID
	 */
	private <E extends CareerHistoryDtoAccessor> void convertInsertCareerHistoryList(TempMemberProperty<E> property, int memberId) {

		// 登録用データを保持するためリセット
		property.careerHistoryList = Lists.newArrayList();

		// Dtoの値をエンティティに詰め替える
		for (CareerHistoryDtoAccessor dto : property.careerHistoryDtoList) {

			// エンティティの生成
			TTempMemberCareerHistory entity = new TTempMemberCareerHistory();
			entity.tCareerHistoryAttributeList = Lists.newArrayList();

			// Dtoの値をエンティティにコピー
			Beans.copy(dto, entity).converter(new ZenkakuKanaConverter()).execute();
			// 会員IDをセット
			entity.tempMemberId = memberId;
			// 削除フラグをセット
			entity.deleteFlg = DeleteFlgKbn.NOT_DELETED;

			// 職種
			if (dto.getJobKbnList() != null) {
				// WEB履歴書職歴属性の値を詰め替える
				for (String value : dto.getJobKbnList()) {

					TTempMemberCareerHistoryAttribute attrEntity = new TTempMemberCareerHistoryAttribute();
					// 属性コードに職種区分をセット
					attrEntity.attributeCd = MTypeConstants.JobKbn.TYPE_CD;
					// 属性値をセット
					attrEntity.attributeValue = Integer.parseInt(value);

					// プロパティのリストに保持
					entity.tCareerHistoryAttributeList.add(attrEntity);
				}
			}

			// 業態
			if (dto.getIndustryKbnList() != null) {
				// WEB履歴書職歴属性の値を詰め替える
				for (String value : dto.getIndustryKbnList()) {

					TTempMemberCareerHistoryAttribute attrEntity = new TTempMemberCareerHistoryAttribute();
					// 属性コードに業態区分をセット
					attrEntity.attributeCd = MTypeConstants.IndustryKbn.TYPE_CD;
					// 属性値をセット
					attrEntity.attributeValue = Integer.parseInt(value);

					// プロパティのリストに保持
					entity.tCareerHistoryAttributeList.add(attrEntity);
				}
			}

			// プロパティのリストにセット
			property.careerHistoryList.add(entity);
		}
	}
}
