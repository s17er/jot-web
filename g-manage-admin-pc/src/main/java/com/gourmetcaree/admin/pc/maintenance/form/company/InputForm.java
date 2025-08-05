package com.gourmetcaree.admin.pc.maintenance.form.company;

import java.io.Serializable;

import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.db.common.constants.MTypeConstants;

/**
 *
 * 会社登録のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class InputForm extends CompanyForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 8407109514209666945L;

	/**
	 *  独自チェックを行う
	 * @return ActionMessages
	 */
	@Override
	public ActionMessages validate() {
		return super.validate();
	}

	/**
	 * リセットを行う
	 */
	public void resetForm() {
		super.resetForm();
		// 代理店フラグの初期値を代理店以外に設定
		this.agencyFlg = String.valueOf(MTypeConstants.AgencyFlg.AGENCY);
		// サブアドレス受信フラグの初期値を受信否に設定
		this.submailReceptionFlg = String.valueOf(MTypeConstants.SubmailReceptionFlg.NOT_RECEIVE);
	}
}