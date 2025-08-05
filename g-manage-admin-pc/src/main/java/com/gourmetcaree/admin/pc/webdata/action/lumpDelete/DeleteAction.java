package com.gourmetcaree.admin.pc.webdata.action.lumpDelete;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.pc.webdata.action.webdata.WebdataBaseAction;
import com.gourmetcaree.admin.pc.webdata.dto.webdata.ListDto;
import com.gourmetcaree.admin.pc.webdata.form.lumpDelete.DeleteForm;
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

/**
 * WEBデータ一括削除アクションクラス
 * @author Takahiro Kimura
 * @version 1.0
 *
 */
@ManageLoginRequired(authLevel=ManageAuthLevel.ADMIN)
public class DeleteAction extends WebdataBaseAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(DeleteAction.class);

	/** 登録フォーム */
	@ActionForm
	@Resource
	protected DeleteForm deleteForm;

	/** WEBリストサービス */
	@Resource
	protected WebListService webListService;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input=TransitionConstants.Webdata.JSP_APC05D02)
	@MethodAccess(accessCode="LUMPDELETE_DELETE_INDEX")
	public String index() {
		return show();
	}

	/**
	 * 戻る
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="LUMPDELETE_DELETE_BACK")
	public String back() {
		removeSessionData();
		// 一覧画面へ遷移
		return TransitionConstants.Webdata.REDIRECT_WEBDATA_LIST_BACKSEARCH;
	}

	/**
	 * 登録
	 * @return
	 */
	@Execute(validator = false, removeActionForm = true, input = TransitionConstants.Webdata.JSP_APC05D02)
	@MethodAccess(accessCode="LUMPDELETE_DELETE_SUBMIT")
	public String submit() {

		convertSessionData();
		if (ArrayUtils.isEmpty(deleteForm.webId)) {
			throw new FraudulentProcessException("不正な操作が行われました。" + deleteForm);
		}

		// 一括削除可能データチェック
		if (!checkEnableDelete(deleteForm.webId)) {
			return TransitionConstants.Webdata.JSP_APC05D02;
		}

		try {
			createDtoList();
		} catch (NumberFormatException e) {
			throw new FraudulentProcessException("不正な操作が行われました。" + deleteForm);
		} catch (WNoResultException e) {
			throw new FraudulentProcessException("不正な操作が行われました。" + deleteForm);
		}

		// 一括削除処理
		doLumpDelete();

		log.debug("一括削除しました。");

		// セッションを破棄
		removeSessionData();

		return TransitionConstants.Webdata.REDIRECT_LUMPDELETE_DELETE_COMP;
	}

	/**
	 * 一覧へ戻る
	 * @return 一覧画面の検索メソッド
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="LUMPDELETE_DELETE_BACKLIST")
	public String backList() {
		removeSessionData();
		// 一覧画面へ遷移
		return TransitionConstants.Webdata.REDIRECT_WEBDATA_LIST_SEARCHAGAIN;
	}

	/**
	 * 一括削除処理
	 */
	private void doLumpDelete() {
		WebdataProperty property = new WebdataProperty();
		property.webId = deleteForm.webId;

		// 一括削除用データ生成
		convertLumpDeleteData(property);

		// 一括削除処理
		webdataLogic.doLumpDelete(property);

		// 楽観的排他制御が発生していればエラーを返す
		if (property.failWebId != null && property.failWebId.size() > 0) {
			throw new ActionMessagesException("errors.app.dinableLumpOptimisticLockException",
					 GourmetCareeUtil.createDelimiterStr(property.failWebId));
		}
	}

	/**
	 * 一括削除用データ生成
	 * @param property WEBデータプロパティ
	 */
	private void convertLumpDeleteData(WebdataProperty property) {

		List<TWeb> entityList = new ArrayList<TWeb>();
		for (ListDto dto : deleteForm.dtoList) {
			TWeb entity = new TWeb();
			entity.id = NumberUtils.toInt(dto.id);
			entity.version = dto.version;
			entityList.add(entity);
		}
		property.entityList = entityList;
	}

	/**
	 * 完了
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="LUMPDELETE_DELETE_COMP")
	public String comp() {
		// 完了画面へ遷移
		return TransitionConstants.Webdata.JSP_APC05D03;
	}

	/**
	 * 一括削除初期表示
	 * @return
	 */
	private String show() {
		// セッションからデータを取得
		convertSessionData();

		if (ArrayUtils.isEmpty(deleteForm.webId)) {
			deleteForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.dataNotFound");
		}

		// 初期表示データを取得
		try {
			// TODO
			createDtoList();

		} catch (NumberFormatException e) {
			deleteForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.fraudulentProcessError");
		} catch (WNoResultException e) {
			deleteForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.dataNotFound");
		}

		return TransitionConstants.Webdata.JSP_APC05D02;
	}

	/**
	 * 表示用リストを生成
	 * @param entityList WEBデータ一覧リスト
	 * @return WEBデータ一覧Dtoをセットしたリスト
	 */
	private void convertShowList(List<VWebList> entityList, List<Integer> canNotDeleteIdList) {

		List<ListDto> dtoList = new ArrayList<ListDto>();
		// Dtoにコピー
		for (VWebList entity : entityList) {
			if (entity.fixedDatetime != null || entity.fixedUserId != null) {
				canNotDeleteIdList.add(entity.id);
				continue;
			}
			ListDto dto = new ListDto();
			Beans.copy(entity, dto).execute();
			int[] serialArray = webAttributeService.getWebAttributeValueArray(entity.id, MTypeConstants.SERIAL_PUBLICATION_KBN.TYPE_CD);
			if (!ArrayUtils.isEmpty(serialArray)) {
				dto.serialPublicationKbn = String.valueOf(serialArray[0]);
			}
			dtoList.add(dto);
		}

		deleteForm.dtoList = dtoList;
	}

	/**
	 * 削除できるかどうか。
	 * できない場合は、ActionMessageをスロー
	 * @param canNotDeleteIdList
	 */
	private void canDelete(List<Integer> canNotDeleteIdList) {
		if (CollectionUtils.isEmpty(canNotDeleteIdList)) {
			return;
		}
		StringBuilder sb = new StringBuilder("");
		sb.append(canNotDeleteIdList.get(0));
		for (int i = 1; i < canNotDeleteIdList.size(); i++) {
			sb.append(", ");
			sb.append(canNotDeleteIdList.get(i));
		}
		deleteForm.setExistDataFlgNg();
		throw new ActionMessagesException("errors.app.cannotDeleteCuzFixed", sb.toString());
	}


	/**
	 * セッションに保持しているデータの変換
	 */
	private void convertSessionData() {
		deleteForm.webId = (String[]) session.getAttribute("webId");
	}

	/**
	 * セッションに保持しているデータの破棄
	 */
	private void removeSessionData() {
		session.removeAttribute("webId");
	}


	private void createDtoList() throws NumberFormatException, WNoResultException {
		List<VWebList> entityList = webListService.getVWebListById(deleteForm.webId);
		List<Integer> canNotDeleteIdList = new ArrayList<Integer>();
		convertShowList(entityList, canNotDeleteIdList);
		canDelete(canNotDeleteIdList);
	}

}