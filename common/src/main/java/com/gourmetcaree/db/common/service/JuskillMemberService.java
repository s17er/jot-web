package com.gourmetcaree.db.common.service;

import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.MJuskillMember;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;

/**
 * ジャスキル会員関連のサービスクラス
 * @author whizz
 *
 */
public class JuskillMemberService extends AbstractGroumetCareeBasicService<MJuskillMember> {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(JuskillMemberService.class);

	public boolean existJuskillPassword(String password) {
		long entity = jdbcManager.from(MJuskillMember.class)
							.where(new SimpleWhere()
							.eq("password", password)
							.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
							.getCount();

		return entity == 0;
	}
}
