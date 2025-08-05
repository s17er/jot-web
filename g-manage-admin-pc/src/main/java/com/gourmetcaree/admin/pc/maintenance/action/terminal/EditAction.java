package com.gourmetcaree.admin.pc.maintenance.action.terminal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.framework.util.StringUtil;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.maintenance.form.terminal.EditForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.logic.TerminalLogic;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.dto.TerminalDto;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.db.common.entity.MTerminal;
import com.gourmetcaree.db.common.entity.MTerminalStation;
import com.gourmetcaree.db.common.service.TerminalService;
import com.gourmetcaree.db.common.service.TerminalStationService;

/**
*
* 駅グループ編集を行うクラス
* @author yamane
*
*/
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN})
public class EditAction extends PcAdminAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(EditAction.class);

	/** フォーム */
	@ActionForm
	@Resource
	protected EditForm editForm;

	/** ターミナルサービス */
	@Resource
	protected TerminalService terminalService;

	/** ターミナル駅サービス */
	@Resource
	protected TerminalStationService terminalStationService;

	/** ターミナルロジック */
	@Resource
	protected TerminalLogic terminalLogic;

	/**
	 * 初期表示
	 * @return 入力画面
	 */
	@Execute(validator = false, urlPattern = "index/{id}", reset="resetForm", input=TransitionConstants.Maintenance.JSP_APJ08E01)
	@MethodAccess(accessCode="TERMINAL_EDIT_INDEX")
	public String index() {

		// パラメータが空の場合はエラー
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, editForm.id);

		// チェック用のIdに値を保持
		editForm.hiddenId = editForm.id;

		return show();
	}

	/**
	 * 確認
	 * @return 確認画面
	 */
	@Execute(validator = true, validate = "validate", reset="resetJson", input = TransitionConstants.Maintenance.JSP_APJ08E01)
	@MethodAccess(accessCode="TERMINAL_EDIT_CONF")
	public String conf() {

		// パラメータが空の場合はエラー
		checkArgsNull(NO_BLANK_FLG_NG, editForm.id);

		// プロセスフラグを確認済みに設定
		editForm.setProcessFlgOk();

		List<Map<String, String>> stationGourpList =  new ArrayList<>();
		for (Map<String, String> map : editForm.sendJson) {
			if (StringUtil.isEmpty(map.get("companyCd"))) {
				continue;
			}
			stationGourpList.add(map);
		}

		editForm.sendJson = stationGourpList;


		// 確認画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ08E02;
	}

	/**
	 * 戻る
	 * @return 詳細画面の初期表示
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="TERMINAL_EDIT_BACK")
	public String back() {

		// 一覧画面へ遷移
		return TransitionConstants.Maintenance.REDIRECT_TERMINAL_LIST;
	}

	/**
	 * 訂正
	 * @return 編集画面
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="TERMINAL_EDIT_CORRECT")
	public String correct() {

		// プロセスフラグを未確認に設定
		editForm.setProcessFlgNg();

		// 登録画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ08E01;
	}

	/**
	 * 登録
	 * @return 完了へリダイレクト
	 */
	@Execute(validator = false, input = TransitionConstants.Maintenance.JSP_APJ08E01)
	@MethodAccess(accessCode="TERMINAL_EDIT_SUBMIT")
	public String submit() {

		// プロセスフラグがfalseの場合、未確認としてエラー
		if (!editForm.processFlg) {
			throw new FraudulentProcessException("不正な操作が行われました。" + editForm);
		}

		// セッションのIdと、画面で保持するIdが違う場合エラー
		if (!editForm.id.equals(editForm.hiddenId)) {
			throw new FraudulentProcessException("不正な操作が行われました。" + editForm);
		}

		try {
			update();
		} catch (SNoResultException e) {
			throw new ActionMessagesException("errors.inconsistentDataError");
		}

		return TransitionConstants.Maintenance.REDIRECT_TERMINAL_EDIT_COMP;
	}

	/**
	 * 完了
	 * @return 完了画面
	 */
	@Execute(validator = false, removeActionForm = true)
	@MethodAccess(accessCode="TERMINAL_EDIT_COMP")
	public String comp() {

		// 完了画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ08E03;
	}


	/**
	 * 初期表示遷移用
	 * @return 登録画面のパス
	 */
	private String show() {

		// プロセスフラグを非表示に設定
		editForm.setProcessFlgNg();

		// パラメータが空の場合はエラー
		checkArgsNull(NO_BLANK_FLG_NG, editForm.id);

		editForm.terminalTitle = terminalLogic.getTitle(editForm.id);

		MTerminal mTerminal = terminalService.findById(Integer.parseInt(editForm.id));
		editForm.terminalTitle = mTerminal.terminalName;
		editForm.prefecturesCd = String.valueOf(mTerminal.prefecturesCd);

		List<Map<String, String>> terminalList = new ArrayList<>();

		List<TerminalDto> dtoList = terminalLogic.getTerminalDto(editForm.id, String.valueOf(mTerminal.prefecturesCd));

		for (TerminalDto terminalDto :  dtoList) {
			Map<String, String> map = new HashMap<>();
			map.put("companyCd", terminalDto.companyCd);
			map.put("lineCd", terminalDto.lineCd);
			map.put("stationCd", terminalDto.stationCd);
			terminalList.add(map);
		}
		editForm.sendJson = terminalList;


		// 登録画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ08E01;
	}

	/**
	 * 更新処理
	 */
	private void update() {

		List<MTerminalStation> mTerminalStationList = new ArrayList<>();

		int count = 0;
		for (Map<String, String> map : editForm.sendJson) {
			if (StringUtil.isEmpty(map.get("stationCd"))) {
				continue;
			}
			MTerminalStation mTerminalStation = new MTerminalStation();
			mTerminalStation.terminalId = Integer.parseInt(editForm.id);
			mTerminalStation.stationCd = Integer.parseInt(map.get("stationCd"));
			mTerminalStation.displayOrder = count++;
			mTerminalStationList.add(mTerminalStation);
		}

		terminalLogic.deleteTerminalData(editForm.id);
		MTerminal mTerminal = terminalService.findById(Integer.parseInt(editForm.id));
		mTerminal.id = Integer.parseInt(editForm.id);
		mTerminal.terminalName = editForm.terminalTitle;
		mTerminal.prefecturesCd = Integer.parseInt(editForm.prefecturesCd);
		terminalService.update(mTerminal);
		terminalLogic.insertTerminalStation(editForm.id, mTerminalStationList);
	}

}