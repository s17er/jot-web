package com.gourmetcaree.common.property.tempMember;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gourmetcaree.accessor.tempMember.CareerHistoryDtoAccessor;
import com.gourmetcaree.common.property.BaseProperty;
import com.gourmetcaree.db.common.entity.member.TTempMember;
import com.gourmetcaree.db.common.entity.member.TTempMemberArea;
import com.gourmetcaree.db.common.entity.member.TTempMemberAttribute;
import com.gourmetcaree.db.common.entity.member.TTempMemberCareerHistory;
import com.gourmetcaree.db.common.entity.member.TTempMemberCareerHistoryAttribute;
import com.gourmetcaree.db.common.entity.member.TTempMemberSchoolHistory;

/**
 * 仮会員登録用プロパティ
 * @author nakamori
 *
 */
public class TempMemberProperty<E extends CareerHistoryDtoAccessor> extends BaseProperty {

	/**
	 *
	 */
	private static final long serialVersionUID = -6131034967679837855L;


	/** 会員エンティティ */
	public TTempMember member;

	/** 会員属性エンティティ */
	public List<TTempMemberAttribute> memberAttrList;

	/** 会員エリアエンティティ */
	public List<TTempMemberArea> memberAreaList;

	/** WEB履歴書職歴エンティティ */
	public List<TTempMemberCareerHistory> careerHistoryList;

	/** WEB履歴書職歴属性エンティティ */
	public List<TTempMemberCareerHistoryAttribute> careerHistoryAttributeList;

	/** WEB履歴書学歴エンティティ */
	public TTempMemberSchoolHistory schoolHistory;

	/** WEB履歴書職歴Dtoエンティティ */
	public List<E> careerHistoryDtoList;

	/** 登録する会員属性のマップ */
	public Map<String, List<String>> attrMap = new HashMap<String, List<String>>();

	/** 登録する会員エリアリスト */
	public List<String> areaList = new ArrayList<String>();

	/** 会員になるかどうかのフラグ(事前登録用) */
	public boolean becomeMemberFlg;

	/** 事前登録会員フラグ */
	public boolean advancedMemberFlg;

	/** 職歴IDリスト */
	public List<Integer> careerHistoryIdList;

	/** 登録するエリアコード */
	public String areaCd;

}
