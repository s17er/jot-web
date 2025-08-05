package com.gourmetcaree.admin.pc.maintenance.form.mischief;

import java.io.Serializable;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * いたずら応募条件削除のフォーム
 * @author Aquarius
 *
 */
@Component(instance = InstanceType.SESSION)
public class DeleteForm extends MischiefApplicationForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;


	public void resetFormWithoutId() {
		name = null;
		nameKana = null;
		prefecturesCd = null;
		municipality = null;
		address = null;
		phoneNo1 = null;
		phoneNo2 = null;
		phoneNo3 = null;
		mailAddress = null;
		memberFlg = null;
		terminalKbn = null;
		applicationSelfPr = null;
	}

}