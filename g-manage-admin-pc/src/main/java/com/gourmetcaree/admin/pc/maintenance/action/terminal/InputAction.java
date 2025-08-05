package com.gourmetcaree.admin.pc.maintenance.action.terminal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.framework.util.StringUtil;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.maintenance.form.terminal.InputForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.logic.TerminalLogic;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.db.common.entity.MTerminal;
import com.gourmetcaree.db.common.entity.MTerminalStation;

/**
 *
 * 駅グループ登録を行うクラス
 * @author yamane
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN})
public class InputAction extends PcAdminAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(InputAction.class);

	/** フォーム */
	@ActionForm
	@Resource
	protected InputForm inputForm;

	/** ターミナルロジック */
	@Resource
	protected TerminalLogic terminalLogic;

	/**
	 * 初期表示
	 *
	 * @return 入力画面
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Maintenance.JSP_APJ08C01)
	@MethodAccess(accessCode = "TERMINAL_INPUT_INDEX")
	public String index() {

		return show();

	}

	/**
	 * 初期表示遷移用
	 *
	 * @return 入力画面のパス
	 */
	private String show() {

		// 登録画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ08C01;
	}

	/**
	 * 確認
	 *
	 * @return 確認画面
	 */
	@Execute(validator = true, validate = "validate", reset = "resetJson", input = TransitionConstants.Maintenance.JSP_APJ08C01)
	@MethodAccess(accessCode = "TERMINAL_INPUT_CONF")
	public String conf() {

		// プロセスフラグを確認済みに設定
		inputForm.setProcessFlgOk();

		List<Map<String, String>> stationGourpList =  new ArrayList<>();
		for (Map<String, String> map : inputForm.sendJson) {
			if (StringUtil.isEmpty(map.get("companyCd"))) {
				continue;
			}
			stationGourpList.add(map);
		}

		inputForm.sendJson = stationGourpList;

		// 確認画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ08C02;
	}

	/**
	 * 訂正
	 *
	 * @return 入力画面
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode = "TERMINAL_INPUT_CORRECT")
	public String correct() {

		// プロセスフラグを未確認に設定
		inputForm.setProcessFlgNg();

		// 登録画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ08C01;
	}

	/**
	 * 登録
	 *
	 * @return 完了へリダイレクト
	 */
	@Execute(validator = false, input = TransitionConstants.Maintenance.JSP_APJ08C01)
	@MethodAccess(accessCode = "TERMINAL_INPUT_SUBMIT")
	public String submit() {

		// プロセスフラグがfalseの場合、未確認としてエラー
		if (!inputForm.processFlg) {
			throw new FraudulentProcessException("不正な操作が行われました。" + inputForm);
		}

		// 登録処理の呼び出し
		insert();

		// 完了メソッドへリダイレクト
		return TransitionConstants.Maintenance.REDIRECT_TERMINAL_INPUT_COMP;
	}

	/**
	 * 完了
	 *
	 * @return 完了画面
	 */
	@Execute(validator = false, removeActionForm = true)
	@MethodAccess(accessCode = "TERMINAL_INPUT_COMP")
	public String comp() {

		// 完了画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ08C03;
	}

	/**
	 * 登録処理
	 */
	private void insert() {

		MTerminal mTerminal = new MTerminal();
		mTerminal.prefecturesCd = Integer.parseInt(inputForm.prefecturesCd);
		mTerminal.terminalName = inputForm.terminalTitle;
		List<MTerminalStation> mTerminalStationList = new ArrayList<>();
		int count = 0;
		for (Map<String, String> map : inputForm.sendJson) {
			if (StringUtil.isEmpty(map.get("stationCd"))) {
				continue;
			}
			MTerminalStation mTerminalStation = new MTerminalStation();
			mTerminalStation.stationCd = Integer.parseInt(map.get("stationCd"));
			mTerminalStation.displayOrder = count;
			mTerminalStationList.add(mTerminalStation);
		}
		terminalLogic.insert(mTerminal, mTerminalStationList);


		log.debug("ターミナルマスタを登録しました。" + inputForm);
	}


}