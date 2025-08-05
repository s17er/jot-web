package com.gourmetcaree.db.common.service;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.TJuskillPreviewAccess;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;

/**
 * ジャスキル会員プレビューアクセス関連のサービス
 *
 */
public class JuskillPreviewAccessService extends AbstractGroumetCareeBasicService<TJuskillPreviewAccess> {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(JuskillPreviewAccessService.class);

	public String findPreviewAccessCd(int juskillMemberId) {

		TJuskillPreviewAccess tJuskillPreviewAccess = jdbcManager.from(TJuskillPreviewAccess.class)
				.where(new SimpleWhere()
				.eq("juskillMemberId", juskillMemberId)
				.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
				.getSingleResult();


		if(tJuskillPreviewAccess != null) {
			return tJuskillPreviewAccess.accessCd;
		}else {
			return createPreviewAccessData(juskillMemberId);
		}
	}

	private String createPreviewAccessData(int juskillMemberId) {

		TJuskillPreviewAccess tJuskillPreviewAccess = new TJuskillPreviewAccess();
		tJuskillPreviewAccess.juskillMemberId = juskillMemberId;
		tJuskillPreviewAccess.accessCd = RandomStringUtils.randomAlphabetic(20);

		insert(tJuskillPreviewAccess);

		return tJuskillPreviewAccess.accessCd;
	}
}
