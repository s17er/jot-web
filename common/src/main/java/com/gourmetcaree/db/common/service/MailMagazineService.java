package com.gourmetcaree.db.common.service;

import static jp.co.whizz_tech.commons.WztStringUtil.toCamelCase;

import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TMailMagazine;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * メルマガのサービスクラスです。
 * @version 1.0
 */
public class MailMagazineService extends AbstractGroumetCareeBasicService<TMailMagazine> {

	/**
	 * 未配送のメールマガジンを取得します。
	 * @param id
	 * @param mailmagazineKbn
	 * @return
	 * @throws WNoResultException
	 */
	public TMailMagazine getNotDeliveryMailMagazineById(int id, int mailmagazineKbn) throws WNoResultException {

		Where where = new SimpleWhere()
						.eq(toCamelCase(TMailMagazine.ID), id)
						.eq(toCamelCase(TMailMagazine.MAILMAGAZINE_KBN), mailmagazineKbn)
						.eq(toCamelCase(TMailMagazine.DELIVERY_FLG), MTypeConstants.DeliveryFlg.NON)
						.eq(toCamelCase(TMailMagazine.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		//主キーのため1件であることは保証されている
		return findByCondition(where).get(0);
	}
}