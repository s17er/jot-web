package com.gourmetcaree.admin.service.property;

import java.io.Serializable;
import java.util.List;

import com.gourmetcaree.db.common.entity.MJuskillMember;
import com.gourmetcaree.db.common.entity.MJuskillMemberCareerHistory;

/**
 * ジャスキル会員のデータを受け渡しするクラス *
 */
public class JuskillMemberProperty extends BaseProperty implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 7613761038566368271L;


	/** 会員マスタエンティティ */
	public MJuskillMember juskillMember;

	/** 会員属性マスタエンティティリスト */
	public List<MJuskillMemberCareerHistory> mJuskillemberCareerHistoryList;

}
