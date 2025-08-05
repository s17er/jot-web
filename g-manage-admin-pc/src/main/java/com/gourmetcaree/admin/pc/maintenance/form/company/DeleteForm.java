package com.gourmetcaree.admin.pc.maintenance.form.company;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 会社削除のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
public class DeleteForm extends CompanyForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -3239926078003609127L;

	/**
	 * リセットを行う
	 */
	@Override
	public void resetForm() {
		super.resetForm();
	}

	/**
	 * 削除項目以外のリセットを行う
	 */
	public void resetFormWithoutDelValue() {
		displayId = null;
	    existDataFlg = true;
	    hiddenId = null;
		companyName = null;
		companyNameKana = null;
		contactName = null;
		contactNameKana = null;
		agencyFlg = null;
		areaCd = new ArrayList<String>();
		zipCd = null;
		prefecturesCd = null;
		municipality = null;
		address = null;
		phoneNo1 = null;
		phoneNo2 = null;
		phoneNo3 = null;
		faxNo1 = null;
		faxNo2 = null;
		faxNo3 = null;
		mainMail = null;
		subMail = null;
		submailReceptionFlg = null;
		note = null;
		registrationDatetime = null;
	}
}