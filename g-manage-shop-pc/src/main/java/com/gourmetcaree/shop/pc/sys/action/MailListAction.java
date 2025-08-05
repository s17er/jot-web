package com.gourmetcaree.shop.pc.sys.action;

import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.shop.logic.logic.ApplicationLogic.MAIL_KBN;
import com.gourmetcaree.shop.pc.valueobject.MenuInfo;


/**
 * メール一覧用基底アクション
 * @author Yamane
 *
 */
public abstract class MailListAction extends PcShopAction {


	/** ステータスのデフォルト値 */
	public static final int STATUS_DEFAULT_VALUE = -1;

	/** ステータスのデフォルトカラー */
	public static final String STATUS_DEFAULT_COLOER = MTypeConstants.SelectionFlg.SELECTION_FLG_DEFAULT_COLOR;

	/**
	 * メール区分を取得。
	 * このメール区分により、応募/スカウトなど、どのメールかを判断する。
	 */
	public abstract MAIL_KBN getMailKbn();


	@Override
	public MenuInfo getMenuInfo() {
		return MenuInfo.mailInstance();
	}
}
