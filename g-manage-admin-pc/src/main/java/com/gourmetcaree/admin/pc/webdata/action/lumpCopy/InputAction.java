package com.gourmetcaree.admin.pc.webdata.action.lumpCopy;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.pc.webdata.action.webdata.WebdataBaseAction;
import com.gourmetcaree.admin.pc.webdata.dto.webdata.ListDto;
import com.gourmetcaree.admin.pc.webdata.form.lumpCopy.InputForm;
import com.gourmetcaree.admin.service.property.WebdataProperty;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TWeb;
import com.gourmetcaree.db.common.entity.VWebList;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.WebListService;
import com.gourmetcaree.db.common.service.WebService;

/**
 * WEBデータ一括コピーアクションクラス
 * @author Takahiro Kimura
 * @version 1.0
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN,ManageAuthLevel.STAFF,ManageAuthLevel.AGENCY, ManageAuthLevel.OTHER, ManageAuthLevel.SALES})
public class InputAction extends WebdataBaseAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(InputAction.class);


	/** 登録フォーム */
	@ActionForm
	@Resource
	protected InputForm inputForm;

	/** WEBリストサービス */
	@Resource
	protected WebListService webListService;

	@Resource
	private WebService webService;
	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input=TransitionConstants.Webdata.JSP_APC03C01)
	@MethodAccess(accessCode="LUMPCOPY_INPUT_INDEX")
	public String index() {

		return show();
	}

	/**
	 * 確認
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Webdata.JSP_APC03C01)
	@MethodAccess(accessCode="LUMPCOPY_INPUT_CONF")
	public String conf() {

		if (inputForm.webId == null || inputForm.webId.length == 0 || inputForm.dtoList == null || inputForm.dtoList.size() == 0) {
			throw new FraudulentProcessException("不正な操作が行われました。" + inputForm);
		}

		// プロセスフラグを確認済みに設定
		inputForm.setProcessFlgOk();

		// 確認画面へ遷移
		return TransitionConstants.Webdata.JSP_APC03C02;
	}

	/**
	 * 戻る
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="LUMPCOPY_INPUT_BACK")
	public String back() {
		// 確認画面へ遷移
		return TransitionConstants.Webdata.REDIRECT_WEBDATA_LIST_BACKSEARCH;
	}

	/**
	 * 訂正
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="LUMPCOPY_INPUT_CORRECT")
	public String correct() {

		if (inputForm.webId == null || inputForm.webId.length == 0 || inputForm.dtoList == null || inputForm.dtoList.size() == 0) {
			throw new FraudulentProcessException("不正な操作が行われました。" + inputForm);
		}

		// プロセスフラグを未確認に設定
		inputForm.setProcessFlgNg();

		// 登録画面へ遷移
		return TransitionConstants.Webdata.JSP_APC03C01;
	}

	/**
	 * 登録
	 * @return
	 */
	@Execute(validator = false, removeActionForm = true, input = TransitionConstants.Webdata.JSP_APC03C01)
	@MethodAccess(accessCode="LUMPCOPY_INPUT_SUBMIT")
	public String submit() {

		// プロセスフラグがfalseの場合、未確認としてエラー
		if (!inputForm.processFlg) {
			throw new FraudulentProcessException("不正な操作が行われました。" + inputForm);
		}

		// 一括コピーできるデータかどうかチェック
		checkEnableCopy(inputForm.webId);

		// 一括コピー
		doLumpCopy();

		log.debug("WEBデータを一括コピーしました。");

		// セッションを破棄
		session.removeAttribute("webId");
		session.removeAttribute("areaCd");

		return TransitionConstants.Webdata.REDIRECT_LUMPCOPY_INPUT_COMP;
	}

	/**
	 * 完了
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="LUMPCOPY_INPUT_COMP")
	public String comp() {
		// 完了画面へ遷移
		return TransitionConstants.Webdata.JSP_APC03C03;
	}

	/**
	 * 一覧へ戻る
	 * @return 一覧画面の検索メソッド
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="LUMPCOPY_INPUT_BACKLIST")
	public String backList() {
		// 一覧画面へ遷移
		return TransitionConstants.Webdata.REDIRECT_WEBDATA_LIST_SEARCHAGAIN;
	}

	/**
	 * 初期表示遷移用
	 * @return 登録画面のパス
	 */
	private String show() {

		// セッションからデータを取得
		String[] webId = (String[]) session.getAttribute("webId");
		String areaCd = (String) session.getAttribute("areaCd");

		if (webId == null || webId.length == 0) {
			inputForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.dataNotFound");
		}

		if (StringUtils.isEmpty(areaCd)) {
			inputForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.required", "エリア");
		}

		// 初期表示データを取得
		try {
			List<VWebList> entityList = webListService.getVWebListById(webId);
			convertShowList(entityList);
			inputForm.webId = webId;
			inputForm.areaCd = areaCd;

		} catch (NumberFormatException e) {
			inputForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.fraudulentProcessError");
		} catch (WNoResultException e) {
			inputForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.dataNotFound");
		}

		// 登録画面へ遷移
		return TransitionConstants.Webdata.JSP_APC03C01;
	}

	/**
	 * 一括コピー処理
	 */
	private void doLumpCopy() {

		WebdataProperty property = new WebdataProperty();
		property.webId = inputForm.webId;

		// データ生成
		createLumpCopyData(property);

		// 一括コピー
		try {
			webdataLogic.doLumpCopy(property);
		} catch (NumberFormatException e) {
			throw new ActionMessagesException("errors.app.failGetWebId");
		} catch (WNoResultException e) {
			throw new ActionMessagesException("errors.app.dataNotFound");
		}

	}

	/**
	 * 一括コピー用データの生成
	 * @param property WEBデータプロパティ
	 */
	private void createLumpCopyData(WebdataProperty property) {

		List<TWeb> entityList = new ArrayList<TWeb>();
		for (ListDto dto : inputForm.dtoList) {
			TWeb entity = new TWeb();
			entity.sizeKbn = NumberUtils.toInt(dto.sizeKbn);
			entity.volumeId = dto.volumeId;
			entity.id = NumberUtils.toInt(dto.id);
			entity.serialPublication = dto.serialPublication;

			try {
				List<TWeb> twebList = webService.getWebDataByIdArray(new String[]{String.valueOf(entity.id)});

				if (CollectionUtils.isNotEmpty(twebList) && 0 < twebList.size()) {
					entity.customerId = twebList.get(0).customerId;
				}
			} catch (NumberFormatException e) {

			} catch (WNoResultException e) {

			}


			entityList.add(entity);
		}

		property.entityList = entityList;
	}

	/**
	 * 表示用リストを生成
	 * @param entityList WEBデータ一覧リスト
	 * @return WEBデータ一覧Dtoをセットしたリスト
	 */
	private void convertShowList(List<VWebList> entityList) {

		List<ListDto> dtoList = new ArrayList<ListDto>();

		// Dtoにコピー
		for (VWebList entity : entityList) {

			ListDto dto = new ListDto();
			Beans.copy(entity, dto).execute();
			int[] serialArray = webAttributeService.getWebAttributeValueArray(entity.id, MTypeConstants.SERIAL_PUBLICATION_KBN.TYPE_CD);
			if (!ArrayUtils.isEmpty(serialArray)) {
				dto.serialPublicationKbn = String.valueOf(serialArray[0]);

				Integer serialPublicationKbn = serialArray[0];
				// 2連載の場合は２連載OK
				if (GourmetCareeUtil.eqInt(MTypeConstants.SERIAL_PUBLICATION_KBN.DOUBLE, serialPublicationKbn)) {
					dto.copySerialPublicationKbn = String.valueOf(MTypeConstants.SERIAL_PUBLICATION_KBN.DOUBLE_OK);

				// 3連載の場合は２連載
				} else if (GourmetCareeUtil.eqInt(MTypeConstants.SERIAL_PUBLICATION_KBN.TRIPLE, serialPublicationKbn)) {
					dto.copySerialPublicationKbn = String.valueOf(MTypeConstants.SERIAL_PUBLICATION_KBN.DOUBLE);

				// 定額制の場合は定額制
				} else if (GourmetCareeUtil.eqInt(MTypeConstants.SERIAL_PUBLICATION_KBN.FLAT_RATE_SYSTEM, serialPublicationKbn)) {
					dto.copySerialPublicationKbn = String.valueOf(MTypeConstants.SERIAL_PUBLICATION_KBN.FLAT_RATE_SYSTEM);
				}

			}
			dtoList.add(dto);
		}

		inputForm.dtoList = dtoList;
	}
}
