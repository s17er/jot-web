package com.gourmetcaree.shop.pc.passwordReissue.form.passwordReissue;

import java.io.Serializable;

public class InputForm extends PasswordReissueBaseForm implements Serializable{

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1387344556478094657L;

	@Override
	public void resetForm() {
		resetPasswordReissueBaseForm();
	}

}
