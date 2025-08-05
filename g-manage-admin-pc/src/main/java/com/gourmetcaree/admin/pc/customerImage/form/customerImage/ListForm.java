package com.gourmetcaree.admin.pc.customerImage.form.customerImage;

import javax.persistence.Transient;

import org.apache.struts.upload.FormFile;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;

import com.gourmetcaree.common.form.BaseForm;

/**
 * 顧客詳細のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
public class ListForm extends BaseForm {

	private static final long serialVersionUID = -3605358584066553110L;

	public String customerId;

	public String customerImageId;

	/** アップロード画像 */
	@Transient
	@Binding(bindingType = BindingType.NONE)
	public FormFile formFile;

	/**
	 * リセットを行う
	 */
	public void resetForm() {
		customerId = null;
		customerImageId = null;
		formFile = null;
	}

}