package com.gourmetcaree.admin.service.property;

import java.io.Serializable;
import java.util.List;

import com.gourmetcaree.db.common.entity.MMember;
import com.gourmetcaree.db.common.entity.MMemberAttribute;
import com.gourmetcaree.db.common.entity.MMemberHopeCity;
import com.gourmetcaree.db.common.entity.TCareerHistory;
import com.gourmetcaree.db.common.entity.TCareerHistoryAttribute;
import com.gourmetcaree.db.common.entity.TSchoolHistory;

/**
 *
 * 会員のデータを受け渡しするクラス
 * @author Takahiro Kimura
 *
 */
public class MemberProperty extends BaseProperty implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 7613761038566368271L;


	/** 会員マスタエンティティ */
	public MMember member;

	/** 会員属性マスタエンティティリスト */
	public List<MMemberAttribute> mMemberAttributeList;

	/** 学歴エンティティ */
	public TSchoolHistory tSchoolHistory;

	/** 職歴エンティティ */
	public List<TCareerHistory> tCareerHistoryList;

	/** 職歴属性エンティティリスト */
	public List<TCareerHistoryAttribute> tCareerHistoryAttributeList;

	/** 希望勤務地エンティティリスト */
	public List<MMemberHopeCity> mMemberHopeCityList;

}
