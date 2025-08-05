package com.gourmetcaree.admin.pc.maintenance.action.terminal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.annotation.Resource;

import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.maintenance.form.terminal.ListForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.logic.TerminalLogic;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;

/**
 * 駅グループ一覧を表示するクラス
 * @author yamane
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN})
public class ListAction extends PcAdminAction {

	/** 登録フォーム */
	@ActionForm
	@Resource
	protected ListForm listForm;

	@Resource
	protected TerminalLogic terminalLogic;

	/**
	 * 初期表示
	 * @return 一覧画面
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Maintenance.JSP_APJ08L01)
	@MethodAccess(accessCode="TERMINAL_LIST_INDEX")
	public String index() {

		// 表示フラグを非表示
		listForm.setExistDataFlgNg();

		return show();
	}

	/**
	 * 初期表示遷移用
	 * @return 一覧画面のパス
	 */
	private String show() {

		listForm.terminalMap = terminalLogic.getTerminalMap();

		if (listForm.terminalMap == null) {

			// 表示フラグを非表示
			listForm.setExistDataFlgNg();
			return TransitionConstants.Maintenance.JSP_APJ08L01;
		}

		listForm.terminalIdList = new ArrayList<>();
		listForm.terminalDetailMap = new HashMap<>();
		listForm.terminalTitleMap = new HashMap<>();

		Set<String> keys = listForm.terminalMap.keySet();
		for (String s : keys) {
			listForm.terminalTitleMap.put(s, terminalLogic.getTitle(s));
			listForm.terminalIdList.add(s);
			listForm.terminalDetailMap.put(s, terminalLogic.getTerminalDto(s, listForm.terminalMap.get(s)));
		}

		if (listForm.terminalIdList.size() > 0) {

			// 表示フラグを表示
			listForm.setExistDataFlgOk();
		}

		// 登録画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ08L01;
	}

}