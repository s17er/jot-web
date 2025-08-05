package com.gourmetcaree.admin.pc.maintenance.form.company;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.db.common.entity.MCompanyArea;

/**
 * 会社編集のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class EditForm extends CompanyForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -5067019744088278033L;

	/** 削除する会社エリアマスタを保持するリスト */
	public List<MCompanyArea> delMCompanyAreaList;

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
		delMCompanyAreaList = null;
	}

	/**
	 * ID以外のリセットを行う
	 */
	public void resetFormWithoutId() {
		resetBaseForm();
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
		version = null;
	}
}