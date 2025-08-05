package com.gourmetcaree.shop.pc.application.form.application;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;


/**
 * 応募者毎のメール一覧
 * @author Yamane
 *
 */
@Component(instance=InstanceType.SESSION)
public class DetailMailListForm extends ApplicationForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1901308175916204102L;

	/** メモ */
	public String memo;

	/** ページ番号 */
	public String pageNum;

	/**
	 * ヘッダを隠すフラグ
	 * 応募者ごとの一覧と、日付での一覧からの遷移があるが、
	 * 応募者ごとからの遷移はfalse、
	 * 日付での遷移はtrueとなる。
	 */
	public boolean hideHeaderFlg;


	/**
	 * リセットを行う
	 */
	public void resetForm() {
		super.resetBaseForm();
		memo = null;
		pageNum = null;
		hideHeaderFlg = false;
	}
}
