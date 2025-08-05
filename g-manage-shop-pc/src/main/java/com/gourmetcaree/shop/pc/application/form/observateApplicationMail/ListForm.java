package com.gourmetcaree.shop.pc.application.form.observateApplicationMail;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.shop.pc.application.form.applicationMail.ApplicationMailForm;

/**
 * 応募メール一覧フォームです。
 * @author Takahiro Kimura
 * @version 1.0
 */
@Component(instance=InstanceType.SESSION)
public class ListForm extends ApplicationMailForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 8321223119325952143L;

	/** ページ番号 */
	public String pageNum;

	public int observateApplicationId;

	/** 送受信区分 */
	public int sendKbn;

	public String where_displayCount;

	public String[] where_mailStatus;

	public String[] where_areaCd;

	public String where_keyword;

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
		super.resetApplicationMailBaseForm();
		pageNum = null;
		sendKbn = 0;
		where_displayCount = null;
		where_mailStatus = null;
		where_areaCd = null;
		where_keyword = null;
		selectionKbn = null;
		changeIdArray = null;
		errorMessage = null;
		memo = null;
	}
}
