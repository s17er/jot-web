package com.gourmetcaree.shop.pc.preApplication.form.preApplicationMail;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * プレ応募メール一覧フォームです。
 */
@Component(instance=InstanceType.SESSION)
public class ListForm extends PreApplicationMailForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -5250373599469369157L;

	/** ページ番号 */
	public String pageNum;

	/** 送受信区分 */
	public int sendKbn;

	public int applicationId;

	public String where_displayCount;

	public String[] where_mailStatus;

	public String[] where_areaCd;

	public String where_jobKbn;

	public String where_employPtnKbn;

	public String where_selectionKbn;

	public String where_keyword;

	public String where_webId;

	/** ステータス区分 */
	public String selectionKbn;

	/** 変更するID配列 */
	public String[] changeIdArray;

	public String errorMessage;

	public String memo;

	/**
	 * リセットを行う
	 */
	public void resetForm() {
		super.resetPreApplicationMailBaseForm();
		pageNum = null;
		sendKbn = 0;
		where_displayCount = null;
		where_mailStatus = null;
		where_areaCd = null;
		where_jobKbn = null;
		where_employPtnKbn = null;
		where_selectionKbn = null;
		where_keyword = null;
		where_webId = null;
		selectionKbn = null;
		changeIdArray = null;
		errorMessage = null;
		memo = null;
	}
}
