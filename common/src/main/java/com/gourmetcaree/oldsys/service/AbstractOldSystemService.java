package com.gourmetcaree.oldsys.service;

import javax.annotation.Resource;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.extension.jdbc.service.S2AbstractService;

/**
 * 旧システム用サービスクラスです
 * @author Hiroyuki Sugimoto
 * @param <T>
 */
abstract public class AbstractOldSystemService<T> extends S2AbstractService<T> {

	@Resource(name = "oldSysJdbcManager")
	public void setJdbcManager(JdbcManager jdbcManager) {
		this.jdbcManager = jdbcManager;
	}

}
