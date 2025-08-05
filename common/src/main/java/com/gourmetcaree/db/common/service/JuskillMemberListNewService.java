package com.gourmetcaree.db.common.service;

import org.apache.log4j.Logger;

import com.gourmetcaree.db.common.entity.MJuskillMember;
import com.gourmetcaree.db.common.entity.MJuskillMemberCareerHistory;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * ジャスキル一覧関連のサービスクラス
 * @author whizz
 *
 */
public class JuskillMemberListNewService extends AbstractGroumetCareeBasicService<MJuskillMember> {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(JuskillMemberListNewService.class);

	/**
	 * ジャスキル会員の詳細を取得する
	 * @param id
	 * @return 履歴がセットされたジャスキル会員エンティティ
	 * @throws WNoResultException
	 */
	public MJuskillMember getDetail(Integer id) throws WNoResultException {
		String joinProp = MJuskillMember.M_JUSKILL_MEMBER_CAREER_HISTORY_LIST;
		return findByIdLeftJoin(joinProp, id, joinProp + "." + MJuskillMemberCareerHistory.ID);
	}
}
