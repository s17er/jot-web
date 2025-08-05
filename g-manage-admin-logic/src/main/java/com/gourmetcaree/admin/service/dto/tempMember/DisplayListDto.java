package com.gourmetcaree.admin.service.dto.tempMember;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 検索結果一覧に表示させるDTO
 * @author nakamori
 *
 */
public class DisplayListDto implements Serializable {


	/**
	 *
	 */
	private static final long serialVersionUID = -4758467501310568252L;

	/** 会員ID */
	public int id;

	/** エリアコード */
	public List<Integer> memberAreaList;

	/** 氏名 */
	public String memberName;

	/** 氏名カナ */
	public String memberNameKana;

	/** 性別 */
	public String sexKbn;

	/** 年齢 */
	public String age;

	/** 都道府県 */
	public String prefecturesCd;

	/** 住所 */
	public String municipality;

	/** 雇用形態 */
	public String employPtnKbn;

	/** メールアドレス */
	public String loginId;

	/** 更新日 */
	public String lastUpdateDate;

	/** 希望業種リスト */
	public List<Integer> industryList;

	/** 職種リスト */
	public List<Integer> jobList;

	/** 詳細のパス */
	public String detailPath;

	/** メルマガ区分 */
	public String mailMagazineReceptionFlg;

	/** 端末区分 */
	public String terminalKbn;

	/** 更新日 */
	public String updateDatetime;

	/** 登録日時 */
	public Timestamp insertDatetime;

	/** 登録状況 */
	public int advancedStatus;

	/** 事前登録登録日時 */
	public String advancedRegistrationDatetime;
	
	/** 会員登録済みフラグ */
	public Integer memberRegisteredFlg;
	
	

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public static class AttrDto implements Serializable {

		/**
		 *
		 */
		private static final long serialVersionUID = 4966238249734030071L;

		/** 属性コード */
		public String attributeCd;

		/** 属性値 */
		public Integer attributeValue;


		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this);
		}
	}
}
