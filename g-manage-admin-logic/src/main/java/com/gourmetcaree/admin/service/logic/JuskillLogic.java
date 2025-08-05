package com.gourmetcaree.admin.service.logic;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.RandomStringUtils;

import com.gourmetcaree.db.common.entity.MJuskillMember;
import com.gourmetcaree.db.common.entity.MJuskillMemberCareerHistory;
import com.gourmetcaree.db.common.entity.TJuskillMemberMaterial;
import com.gourmetcaree.db.common.service.JuskillMemberCareerHistoryService;
import com.gourmetcaree.db.common.service.JuskillMemberMaterialService;
import com.gourmetcaree.db.common.service.JuskillMemberService;

/**
 * ジャスキル関連を操作するロジック
 * @author whizz
 *
 */
public class JuskillLogic extends AbstractAdminLogic {

	/** ジャスキル会員サービス */
	@Resource
	private JuskillMemberService juskillMemberService;

	/** ジャスキル会員職歴サービス */
	@Resource
	private JuskillMemberCareerHistoryService juskillMemberCareerHistoryService;

	/** ジャスキル会員素材サービス */
	@Resource
	private JuskillMemberMaterialService juskillMemberMaterialService;

	/**
	 * ジャスキル会員のデータをアップデートする
	 * @param mJuskillMember
	 * @param careerHistoryList
	 */
	public void updateJuskillMember(MJuskillMember mJuskillMember, List<MJuskillMemberCareerHistory> careerHistoryList, TJuskillMemberMaterial tJuskillMemberMaterial) {
		juskillMemberService.updateWithNull(mJuskillMember, MJuskillMember.getNullables());
		juskillMemberCareerHistoryService.deleteInsert(mJuskillMember.id, careerHistoryList);
		juskillMemberMaterialService.deleteInsert(mJuskillMember.id, tJuskillMemberMaterial);
	}

	/**
	 * ジャスキル会員の閲覧用パスワードを生成する
	 *
	 * @return ユニークなパスワード
	 */
	public String createJuskillPassword() {
		String password;
		do {
			password = RandomStringUtils.randomAlphanumeric(8);
		}while(!juskillMemberService.existJuskillPassword(password));

		return password;
	}
}
