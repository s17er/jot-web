package com.gourmetcaree.admin.pc.information.form.manageInfo;

import java.io.Serializable;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.common.form.BaseForm;

/**
 * お知らせ編集のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class EditForm extends BaseForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 5874028530762053217L;

	/** エリアコード */
	public String areaCd;

	/** 本文 */
	public String body;

	/** 主キー */
    public Integer id;

    /** 管理画面区分 */
    public String managementScreenKbn;

	/** バージョン番号 */
	public Long version;

	/**
	 * リセットする
	 */
	public void resetForm() {
		resetBaseForm();
		areaCd = null;
		body = null;
	    id = null;
	    managementScreenKbn = null;
		version = null;
	}
}