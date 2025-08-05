package com.gourmetcaree.arbeitsys.service;

import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.arbeitsys.entity.MstGyotai;
import com.gourmetcaree.common.util.GourmetCareeUtil;

/**
 * バイト側業態サービス
 * @author Takehiro Nakamori
 *
 */
public class ArbeitGyotaiService extends AbstractArbeitSystemService<MstGyotai> {


	/**
	 * 業態が存在するかどうか
	 * @param id 業態ID
	 * @return 存在すればtrue
	 */
	public boolean existGyotai(String id) {
		return existGyotai(GourmetCareeUtil.convertStringToInteger(id));
	}

	/**
	 * 業態が存在するかどうか
	 * @param id 業態ID
	 * @return 存在すればtrue
	 */
	public boolean existGyotai(Integer id) {
		if (id == null) {
			return false;
		}
		long count = jdbcManager.from(MstGyotai.class)
								.where(new SimpleWhere()
								.eq("id", id))
								.getCount();

		return count > 0l;
	}


	/**
	 * IDを名前に変換します。
	 * @param id ID
	 * @return 名前
	 */
	public String convertIdToName(int id) {
		SimpleWhere where = new SimpleWhere();
		where.eq("id", id);
		MstGyotai entity = jdbcManager.from(MstGyotai.class)
							.where(where)
							.getSingleResult();

		if (entity == null) {
			return "";
		}

		return entity.name;
	}
}
