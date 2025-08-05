package com.gourmetcaree.admin.pc.mailMag.form.mailMagOption;

import java.io.Serializable;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * メルマガヘッダメッセージ詳細用アクションフォームです。
 * @author Takehiro Nakamori
 *
 */
@Component(instance=InstanceType.REQUEST)
public class HeaderDetailForm extends BaseMailMagOptionForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -5371044886213530511L;

	/** ID */
	public String id;

	/** メルマガID */
	public String mailMagazineId;

	/** 編集できるかどうかのフラグ */
	public boolean editFlg;

}
