package com.gourmetcaree.arbeitsys.service;

import javax.annotation.Resource;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.service.S2AbstractService;

/**
 * グルメdeバイト用サービスクラスです。
 * @author Takehiro Nakamori
 *
 * @param <T>
 */
public class AbstractArbeitSystemService<T> extends S2AbstractService<T> {

	@Resource(name = "arbeitSysJdbcManager")
	public void setJdbcManager(JdbcManager jdbcManager) {
		this.jdbcManager = jdbcManager;
	}
}
