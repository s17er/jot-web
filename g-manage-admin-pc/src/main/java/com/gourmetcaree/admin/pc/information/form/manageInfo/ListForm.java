package com.gourmetcaree.admin.pc.information.form.manageInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.gourmetcaree.admin.pc.information.dto.information.InformationListDto;
import com.gourmetcaree.common.form.BaseForm;

/**
 * お知らせ一覧のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
public class ListForm extends BaseForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -3970393345284463397L;

	/** 管理画面区分 */
	public String managementScreenKbn;

	/** お知らせのDtoを保持するリスト */
	public List<InformationListDto> informationList = new ArrayList<InformationListDto>();

	/**
	 * リセットする
	 */
	public void resetForm() {
		resetBaseForm();
		managementScreenKbn = null;
		informationList = new ArrayList<InformationListDto>();
	}
}