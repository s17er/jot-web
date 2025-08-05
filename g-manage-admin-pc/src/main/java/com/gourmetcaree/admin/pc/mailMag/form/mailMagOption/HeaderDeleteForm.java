package com.gourmetcaree.admin.pc.mailMag.form.mailMagOption;

import java.io.Serializable;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;


/**
 * メルマガヘッダメッセージ 削除アクションフォーム
 * @author Takehiro Nakamori
 *
 */
@Component(instance=InstanceType.REQUEST)
public class HeaderDeleteForm extends BaseMailMagOptionForm implements Serializable{

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -1179870843378809375L;

	/** ID */
	public String id;

	/** メルマガID */
	public String mailMagazineId;

	/** バージョン */
	public String version;

	/**
	 * フォームのリセット
	 */
	@Override
	public void resetForm() {
		super.resetForm();
		id = null;
		mailMagazineId = null;
		version = null;
	}


}
