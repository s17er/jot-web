package com.gourmetcaree.arbeitsys.service;

import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.arbeitsys.entity.MstSubArea;

public class ArbeitSubArbeitAreaService extends AbstractArbeitSystemService<MstSubArea> {

	public String convertIdToName(Integer id) {
		if (id == null) {
			return "";
		}


		MstSubArea entity = jdbcManager.from(MstSubArea.class)
							.where(new SimpleWhere()
							.eq(MstSubArea.ID, id))
							.getSingleResult();


		return entity == null ? "" : entity.name;
	}
}
