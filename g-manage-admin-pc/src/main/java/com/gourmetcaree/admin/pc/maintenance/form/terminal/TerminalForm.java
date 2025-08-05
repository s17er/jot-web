package com.gourmetcaree.admin.pc.maintenance.form.terminal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts.action.ActionMessages;
import org.seasar.struts.annotation.Required;

import com.gourmetcaree.common.form.BaseForm;

/**
 *
 * 駅グループフォーム
 * @author yamanei
 * @version 1.0
 */
public abstract class TerminalForm extends BaseForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 3432975540690096948L;

	/** ターミナルID */
	public String id;

	/** タイトル */
	@Required
	public String terminalTitle;

	/** 都道府県コード */
	@Required
	public String prefecturesCd;

	/** 鉄道会社 */
	public String companyCd;

	/** 路線 */
	public String lineCd;

	/** 駅 */
	public String stationCd;

	/** JSON（駅） */
	public List<Map<String, String>> sendJson;

	/** 削除する駅 */
	public String deleteStationId;

	/**
	 *  独自チェックを行う
	 * @return ActionMessages
	 */
	public ActionMessages validate() {

		// エラー情報
		ActionMessages errors = new ActionMessages();

		return errors;
	}

	public void resetForm() {
		resetBaseForm();
		terminalTitle = null;
		prefecturesCd = null;
		companyCd = null;
		lineCd = null;
		stationCd = null;
		deleteStationId = null;
		sendJson = new ArrayList<>();
	}

	public void resetJson() {
		sendJson = new ArrayList<>();
	}
}