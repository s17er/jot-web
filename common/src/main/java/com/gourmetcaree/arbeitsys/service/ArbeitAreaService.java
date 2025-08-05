package com.gourmetcaree.arbeitsys.service;

import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.arbeitsys.entity.MstArea;

public class ArbeitAreaService extends AbstractArbeitSystemService<MstArea> {

	public String convertIdToName(Integer id) {
		if (id == null) {
			return "";
		}


		MstArea entity = jdbcManager.from(MstArea.class)
							.where(new SimpleWhere()
							.eq(MstArea.ID, id))
							.getSingleResult();


		return entity == null ? "" : entity.name;
	}
}
