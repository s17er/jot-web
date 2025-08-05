package com.gourmetcaree.admin.pc.maintenance.form.special;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.admin.pc.maintenance.dto.special.ListDto;
import com.gourmetcaree.common.form.BaseForm;

/**
 * 特集データ一覧のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class ListForm extends BaseForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 3372212347375059031L;

	/** エリアコード */
	public String areaCd;

	/** 掲載開始日 */
	public String postStartDate;

	/** 掲載終了日 */
	public String postEndDate;

	/** 特集一覧リスト */
	public List<ListDto> list;

	/** エリアのリンク名を保持するMAP */
	public Map<String, String> areaLinkNameMap = new HashMap<>();

	/**
	 * フォームのリセットを行う
	 */
	public void resetForm() {
		resetBaseForm();
		areaCd = null;
		postStartDate = null;
		postEndDate = null;
		list = null;
		areaLinkNameMap.clear();
	}
}