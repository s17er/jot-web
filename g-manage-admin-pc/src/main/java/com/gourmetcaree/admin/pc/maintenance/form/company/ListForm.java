package com.gourmetcaree.admin.pc.maintenance.form.company;

import java.io.Serializable;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.Required;

import com.gourmetcaree.common.form.BaseForm;
import com.gourmetcaree.db.common.constants.MTypeConstants;

/**
 * 会社一覧のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class ListForm extends BaseForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1811316575932545089L;

	/** エリアコード */
	public String areaCd;

	/** 社名 */
	public String companyName;

	/** 代理店フラグ */
	@Required
	public String agencyFlg;

	/**
	 * フォームのリセットを行う
	 */
	public void resetForm() {

		super.resetBaseForm();
		areaCd = null;
		companyName = null;
		// 代理店フラグを、代理店にセット
		this.agencyFlg = String.valueOf(MTypeConstants.AgencyFlg.AGENCY);

	}

}