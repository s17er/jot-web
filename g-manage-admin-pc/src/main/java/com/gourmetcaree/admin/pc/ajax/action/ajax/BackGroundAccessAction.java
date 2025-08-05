package com.gourmetcaree.admin.pc.ajax.action.ajax;

import org.apache.log4j.Logger;
import org.seasar.struts.annotation.Execute;

/**
 * バックグラウンド用アクション
 * @author nakamori
 *
 */
public class BackGroundAccessAction {

	private final Logger log = Logger.getLogger(this.getClass());

	@Execute(validator = false)
	public String index() {
		log.debug("バックグラウンドアクセスを受け付けました。");
		return null;
	}

}
