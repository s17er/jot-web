package com.gourmetcaree.arbeitsys.logic;

import org.seasar.extension.jdbc.JdbcManager;
import org.seasar.framework.container.annotation.tiger.Binding;

/**
 * 運営管理用アルバイトロジック
 * @author Takehiro Nakamori
 *
 */
public abstract class AbstractArbeitLogic  {

	/** JDBCマネージャ */
	@Binding("arbeitSysJdbcManager")
	protected JdbcManager jdbcManager;

}
