package com.gourmetcaree.admin.pc.juskill.form.juskillMailMag;

import java.util.HashMap;
import java.util.Map;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.Required;

import com.gourmetcaree.common.form.BaseForm;

/**
 * ジャスキルメルマガ登録のフォーム
 * @author yamane
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class InputForm extends BaseForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -4073403745159251068L;

	/** PCタイトル */
	@Required
	public String pcMailMagazineTitle;

	/** PC本文 */
	@Required
	public String pcBody;

	/** 検索条件を保持するMap */
	public Map<String, String> whereMap = new HashMap<String, String>();

	/**
	 * リセットを行う
	 */
	public void resetForm() {
		super.resetBaseForm();
		pcMailMagazineTitle = null;
		pcBody = null;
		whereMap = new HashMap<String, String>();

	}
}