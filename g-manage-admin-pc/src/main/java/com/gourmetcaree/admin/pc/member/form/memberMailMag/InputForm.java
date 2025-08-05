package com.gourmetcaree.admin.pc.member.form.memberMailMag;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.Required;

import com.gourmetcaree.common.form.BaseForm;
import com.gourmetcaree.common.util.ActionMessageUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;

/**
 * 会員メルマガ登録のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class InputForm extends BaseForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -4034350527625287023L;

	/** タイトル */
	@Required
	public String mailMagazineTitle;

	/** 配信形式 */
	@Required
	public Integer deliveryType;

	/** 本文(HTML形式用) */
	public String htmlBody;

	/** 本文(テキスト形式用) */
	public String textBody;

	/** アップロード受渡 */
	@Transient
	@Binding(bindingType = BindingType.NONE)
	public FormFile imageFile;

	/** 検索条件を保持するMap */
	public Map<String, String> whereMap = new HashMap<String, String>();

	/**
	 * 独自チェックを行う
	 */
	public ActionMessages validate() {

		ActionMessages errors = new ActionMessages();

		if(this.deliveryType.equals(MTypeConstants.deliveryTypeKbn.HTML) && StringUtils.isEmpty(this.htmlBody)) {
			ActionMessageUtil.addActionMessage(errors, "errors.bodyFailed");
		}

		if(this.deliveryType.equals(MTypeConstants.deliveryTypeKbn.TEXT) && StringUtils.isEmpty(this.textBody)) {
			ActionMessageUtil.addActionMessage(errors, "errors.bodyFailed");
		}

		return errors;
	}

	/**
	 * リセットを行う
	 */
	public void resetForm() {
		super.resetBaseForm();
		mailMagazineTitle = null;
		deliveryType = null;
		htmlBody = null;
		textBody = null;
		imageFile = null;
		whereMap = new HashMap<String, String>();

	}
}