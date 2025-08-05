package com.gourmetcaree.shop.pc.application.action.arbeit;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TArbeitApplication;
import com.gourmetcaree.shop.logic.logic.ApplicationLogic.MAIL_KBN;
import com.gourmetcaree.shop.pc.sys.action.MailListAction;

abstract class ArbeitBaseAction extends MailListAction {


	/**
	 * ステータスの変更
	 * @param entity グルメdeバイトのエンティティ
	 */
	protected void changeSelectionFlg(TArbeitApplication entity, String selectionKbn) {

		//不正な値を防ぐため値をチェック。
		Map<String, String> selectionMap = MTypeConstants.SelectionFlg.SELECTION_COLOR_STRING_MAP;
		if (StringUtils.isNotBlank(selectionMap.get(selectionKbn))) {
			entity.selectionFlg = Integer.parseInt(selectionKbn);
		} else {
			entity.selectionFlg = STATUS_DEFAULT_VALUE;
		}
	}


	@Override
	public MAIL_KBN getMailKbn() {
		return MAIL_KBN.ARBEIT_APPLICATION;
	}
}
