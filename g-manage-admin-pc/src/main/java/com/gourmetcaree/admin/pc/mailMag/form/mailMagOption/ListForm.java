package com.gourmetcaree.admin.pc.mailMag.form.mailMagOption;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * メルマガヘッダメッセージの一覧アクションです。
 * @author Takehiro Nakamori
 *
 */
@Component(instance=InstanceType.SESSION)
public class ListForm extends BaseMailMagOptionForm{

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 4827396868859718322L;

	/** ページ遷移用に選択されたページ数 */
	public String maxRowValue;



	/**
	 * フォームのリセット
	 */
	@Override
	public void resetForm() {
		super.resetForm();
		maxRowValue = null;
	}

	/**
	 * エリア、表示件数以外のリセットを行う
	 */
	public void resetFormShowList() {
		super.resetBaseForm();

	}

}
