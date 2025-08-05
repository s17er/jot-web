package com.gourmetcaree.shop.pc.application.form.arbeitMail;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * アルバイトメール一覧アクションフォーム
 * @author Takehiro Nakamori
 *
 */
@Component(instance = InstanceType.SESSION)
public class ListForm extends ArbeitMailBaseForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -51734610756314877L;

	/** ページ番号 */
	public String pageNum;

	public int arbeitApplicationId;

	/** 送受信区分 */
	public int sendKbn;

	public String where_displayCount;

	public String[] where_mailStatus;

	public String[] where_areaCd;

	public String where_selectionKbn;

	public String where_keyword;

	/** ステータス区分 */
	public String selectionKbn;

	/** 変更するID配列 */
	public String[] changeIdArray;

	public String errorMessage;

	public String memo;

	@Override
	public void resetForm() {
		super.resetBaseForm();
		pageNum = null;
		sendKbn = 0;
		where_displayCount = null;
		where_mailStatus = null;
		where_areaCd = null;
		where_selectionKbn = null;
		where_keyword = null;
		selectionKbn = null;
		changeIdArray = null;
		errorMessage = null;
		memo = null;
	}



}
