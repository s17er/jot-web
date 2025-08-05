package com.gourmetcaree.shop.pc.scoutFoot.action.scoutMember;

import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.shop.logic.property.ScoutMailProperty;
import com.gourmetcaree.shop.pc.scoutFoot.form.scoutMail.ScoutMailForm;
import com.gourmetcaree.shop.pc.sys.action.PcShopAction;
import com.gourmetcaree.shop.pc.valueobject.MenuInfo;

/**
 * スカウトメールBaseアクションクラス
 * @author Motoaki Hara
 * @version 1.0
 *
 */
abstract public class ScoutBaseAction extends PcShopAction {

    protected MenuInfo menuInfo;

	/**
	 * 送受信区分を置き換える
	 * @param sendKbn 送受信区分
	 * @return 送受信区分を返す
	 */
	protected int replaceSendKbn(String sendKbn) {

		if (String.valueOf(MTypeConstants.SendKbn.SEND).equals(sendKbn)) {
			return MTypeConstants.SendKbn.SEND;
		}
		return MTypeConstants.SendKbn.RECEIVE;
	}

	/**
	 * 返信可能かどうかチェック
	 * @param form
	 */
	protected void isPossibleReturnMail(ScoutMailForm form) {

		// 返信可能かどうかチェック
		ScoutMailProperty property = new ScoutMailProperty();

		try {
			property.customerId = userDto.customerId;
			property.mailId = Integer.parseInt(form.id);
		} catch (NumberFormatException e) {
			throw new FraudulentProcessException("不正な操作が行われました。" + form);
		}
		scoutMailLogic.isPossibleReturnMail(property);
	}

    @Override
    public MenuInfo getMenuInfo() {
        return menuInfo == null ?
                MenuInfo.scoutInstance() :
                menuInfo;
    }
}