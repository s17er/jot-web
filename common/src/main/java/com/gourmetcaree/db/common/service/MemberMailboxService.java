package com.gourmetcaree.db.common.service;

import static com.gourmetcaree.common.util.SqlUtils.*;
import static jp.co.whizz_tech.commons.WztStringUtil.*;

import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.constants.MTypeConstants.MailKbn;
import com.gourmetcaree.db.common.constants.MTypeConstants.ScoutReceiveKbn;
import com.gourmetcaree.db.common.entity.VMemberMailbox;

/**
 * 送受信を整理した会員のメールボックスviewのサービスクラスです。
 * @version 1.0
 */
public class MemberMailboxService extends AbstractGroumetCareeBasicService<VMemberMailbox> {


	/**
	 * 最新のスカウト受信区分を取得
	 * @param customerId
	 * @param memberId
	 * @return スカウト受信区分　取得できなければnull
	 */
	public Integer getLastScoutReceiveKbn(int customerId, int memberId) {
		VMemberMailbox entity = jdbcManager.from(VMemberMailbox.class)
		.where(new SimpleWhere()
			.eq(toCamelCase(VMemberMailbox.CUSTOMER_ID), customerId)
			.eq(toCamelCase(VMemberMailbox.MEMBER_ID), memberId)
			.in(toCamelCase(VMemberMailbox.SCOUT_RECEIVE_KBN), ScoutReceiveKbn.RECEIVE, ScoutReceiveKbn.REFUSAL)
		).orderBy(desc(toCamelCase(VMemberMailbox.ID)))
		.limit(1)
		.getSingleResult();
		return entity == null ? null : entity.scoutReceiveKbn;
	}

	/**
	 * 最新のスカウト（メール）を取得
	 * @param customerId
	 * @param memberId
	 * @return スカウト　取得できなければnull
	 */
	public VMemberMailbox getLastScout(int customerId, int memberId) {
		return jdbcManager.from(VMemberMailbox.class)
		.where(new SimpleWhere()
			.eq(toCamelCase(VMemberMailbox.CUSTOMER_ID), customerId)
			.eq(toCamelCase(VMemberMailbox.MEMBER_ID), memberId)
			.eq(toCamelCase(VMemberMailbox.MAIL_KBN), MailKbn.SCOUT)
		).orderBy(desc(toCamelCase(VMemberMailbox.ID)))
		.limit(1)
		.getSingleResult();
	}

	/**
	 * 最新の応募（メール）を取得
	 * @param customerId
	 * @param memberId
	 * @return 応募　取得できなければnull
	 */
	public VMemberMailbox getLastApplication(int customerId, int memberId) {
		return jdbcManager.from(VMemberMailbox.class)
		.where(new SimpleWhere()
			.eq(toCamelCase(VMemberMailbox.CUSTOMER_ID), customerId)
			.eq(toCamelCase(VMemberMailbox.MEMBER_ID), memberId)
			.eq(toCamelCase(VMemberMailbox.MAIL_KBN), MailKbn.APPLICCATION)
		).orderBy(desc(toCamelCase(VMemberMailbox.ID)))
		.limit(1)
		.getSingleResult();
	}
}