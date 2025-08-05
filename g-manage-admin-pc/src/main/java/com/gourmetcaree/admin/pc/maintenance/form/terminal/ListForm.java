package com.gourmetcaree.admin.pc.maintenance.form.terminal;

import java.util.List;
import java.util.Map;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.common.dto.TerminalDto;
import com.gourmetcaree.common.form.BaseForm;

/**
 * 駅グループ一覧のフォーム
 * @author yamane
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class ListForm extends BaseForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1811316575932545089L;

	public List<String> terminalIdList;

	public Map<String, String> terminalTitleMap;

	public Map<String, String> terminalMap;

	public Map<String, List<TerminalDto>> terminalDetailMap;

	/**
	 * フォームのリセットを行う
	 */
	public void resetForm() {

		super.resetBaseForm();
		terminalIdList = null;
		terminalMap = null;
		terminalDetailMap = null;
	}
}