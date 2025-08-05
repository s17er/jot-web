package com.gourmetcaree.admin.pc.information.form.information;

import java.io.Serializable;

import com.gourmetcaree.common.form.BaseForm;
import com.gourmetcaree.db.common.constants.MTypeConstants;

/**
 * お知らせ管理メニューのフォーム
 * 一覧画面用のパスもここでセットしています。
 * @author Makoto Otani
 * @version 1.0
 */
public class MenuForm extends BaseForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 6057169811488300016L;

	/** お知らせ管理の一覧画面のindexメソッド */
	private static final String MANAGE_INFO_LIST_INDEX = "/manageInfo/list/index/";

	/** 運営側管理のお知らせ管理用パス */
	public String adminInfoPath = MANAGE_INFO_LIST_INDEX + MTypeConstants.ManagementScreenKbn.ADMIN_SCREEN;

	/** 店舗管理のお知らせ管理用パス */
	public String shopInfoPath = MANAGE_INFO_LIST_INDEX + MTypeConstants.ManagementScreenKbn.SHOP_SCREEN;

	/** Mypage側管理のお知らせ管理用パス */
	public String mypageInfoPath = MANAGE_INFO_LIST_INDEX + MTypeConstants.ManagementScreenKbn.MY_PAGE_SCREEN;

}