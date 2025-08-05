package com.gourmetcaree.shop.pc.application.form.observateApplication;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * 店舗見学・質問メール者の一覧のフォーム
 * @author Yamane
 *
 */
@Component(instance=InstanceType.SESSION)
public class ListForm extends ObservateApplicationBaseForm {

	/** serialVersionUID */
	private static final long serialVersionUID = 7584867207998999861L;

	/** ページ番号 */
	public String pageNum;

	/**
	 * リセット
	 */
	public void resetForm() {

		pageNum = null;
		super.resetForm();
	}
}
