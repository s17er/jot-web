package com.gourmetcaree.shop.pc.top.form.top;

import java.util.List;

import com.gourmetcaree.common.form.BaseForm;
import com.gourmetcaree.shop.pc.top.dto.top.MenuDto;

/**
 * メニューフォームです。
 * @author Takahiro Kimura
 * @version 1.0
 */
public class MenuForm extends BaseForm {

	/** シリアルバージョンUID  */
	private static final long serialVersionUID = -2391837134357453780L;

	/** 掲載中の求人一覧 */
	public List<MenuDto> list;

	/** 未読の応募メール数 */
	public int applicationMailCount;

	/** 未読のプレ応募メール数 */
	public int preApplicationMailCount;

	/** 未読のスカウトメール数 */
	public int scoutMailCount;

	/** 未読の質問メール数 */
	public int observateApplicationMailCount;

	/** 未読のバイトメール数 */
	public int arbeitMailCount;
}
