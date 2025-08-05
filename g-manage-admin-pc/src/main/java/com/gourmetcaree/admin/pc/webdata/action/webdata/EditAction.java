package com.gourmetcaree.admin.pc.webdata.action.webdata;

import static com.gourmetcaree.common.constants.GourmetCareeConstants.*;
import static com.gourmetcaree.common.util.GourmetCareeUtil.*;
import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.exception.SOptimisticLockException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.MessageResourcesUtil;
import org.seasar.struts.util.RequestUtil;
import org.seasar.struts.util.ResponseUtil;

import com.gourmetcaree.admin.pc.sys.annotation.WebdataAccessUser;
import com.gourmetcaree.admin.pc.sys.annotation.WebdataAccessUser.AccessType;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.pc.sys.dto.WebDataEditorDto;
import com.gourmetcaree.admin.pc.sys.dto.WebDataEditorDto.WebDataAccessDto;
import com.gourmetcaree.admin.pc.sys.dto.mai.FixedMaiDto;
import com.gourmetcaree.admin.pc.sys.dto.mai.PostRequestMaiDto;
import com.gourmetcaree.admin.pc.sys.mai.GourmetcareeMai;
import com.gourmetcaree.admin.pc.webdata.form.webdata.EditForm;
import com.gourmetcaree.admin.pc.webdata.form.webdata.WebdataForm;
import com.gourmetcaree.admin.service.connection.IntecCreateConnection.ClientData;
import com.gourmetcaree.admin.service.dto.WebdataControlDto;
import com.gourmetcaree.admin.service.logic.IPPhoneLogic;
import com.gourmetcaree.admin.service.logic.ScoutMailLogic;
import com.gourmetcaree.admin.service.property.WebdataProperty;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MStatusConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TWeb;
import com.gourmetcaree.db.common.entity.TWebIpPhone;
import com.gourmetcaree.db.common.exception.NoExistCompanyDataException;
import com.gourmetcaree.db.common.exception.NoExistSalesDataException;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.CustomerService;
import com.gourmetcaree.db.common.service.SalesService;
import com.gourmetcaree.db.common.service.WebService;

import net.arnx.jsonic.JSON;

/**
*
* WEBデータ編集を表示するクラス
* @author Makoto Otani
*
*/
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN,ManageAuthLevel.STAFF,ManageAuthLevel.AGENCY, ManageAuthLevel.OTHER, ManageAuthLevel.SALES})
public class EditAction extends WebdataBaseAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(EditAction.class);

	/** WEBデータログ */
	private static Logger webDataLog = Logger.getLogger("webdataLog." + EditAction.class);


	/** フォーム */
	@ActionForm
	@Resource
	protected EditForm editForm;

	/** WEBデータサービス */
	@Resource
	WebService webService;

	/** 営業担当者サービス */
	@Resource
	SalesService salesService;

	/** メール送信 */
	@Resource
	protected GourmetcareeMai gourmetCareeMai;

	/** スカウトメールロジック */
	@Resource
	private ScoutMailLogic scoutMailLogic;

	/** 他の人がアクセスしているフラグ */
	private boolean alertOtherAccessFlg;

	/** WEBデータ編集の排他判定DTO */
	@Resource
	public WebDataEditorDto webDataEditorDto;

	@Resource
	private IPPhoneLogic iPPhoneLogic;

	@Resource
	private CustomerService customerService;

	/**
	 * 初期表示
	 * @return 入力画面
	 */
	@Execute(validator = false, urlPattern = "index/{id}", reset="resetForm", input=TransitionConstants.Webdata.JSP_APC01E01)
	@MethodAccess(accessCode="WEBDATA_EDIT_INDEX")
	@WebdataAccessUser(accessType = AccessType.CHECK_ACCESS)
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
	@Execute(validator = true, validate = "validate", reset = "resetMultibox", input = TransitionConstants.Webdata.JSP_APC01E01)
	@MethodAccess(accessCode="WEBDATA_EDIT_CONF")
	@WebdataAccessUser(accessType = AccessType.UPDATE_TIME)
	public String conf() {

		// パラメータが空の場合はエラー
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, editForm.id);

		// idが数値かどうかチェック
		if (!StringUtils.isNumeric(editForm.id)) {
			throw new FraudulentProcessException("不正な操作が行われました。");
		}

		// 編集データを取得できているかチェック
		if (!editForm.existDataFlg) {
			throw new FraudulentProcessException("不正な操作が行われました。");
		}

		// エラーがある場合は、入力画面へ遷移
		if (!canSelectCompany(editForm) || !isCustomerData(editForm) || !checkInputStr(editForm) || !checkJobWorkContents(editForm)) {

			// プロセスフラグを未確認に設定
			editForm.setProcessFlgNg();

			// 入力画面へ遷移
			return TransitionConstants.Webdata.JSP_APC01E01;
		}

		// テキストエリアの文字列を整形
		editForm.convertTextAreaData();

		// 確認画面の系列店舗をセット
		createConfShopList(editForm);

		// プロセスフラグを確認済みに設定
		editForm.setProcessFlgOk();

		// 確認画面へ遷移
		return TransitionConstants.Webdata.JSP_APC01E02;
	}

	/**
	 * 素材のアップロードを行う。
	 * @return 登録画面
	 */
	@Execute(validator = false, input = TransitionConstants.Webdata.JSP_APC01E01)
	 @MethodAccess(accessCode="WEBDATA_EDIT_UPMATERIAL")
	public String upAjaxMaterial() {
		return uploadImage(editForm);

	}

	/**
	 * 素材のアップロードを行う。
	 * @return 登録画面
	 */
	@Execute(validator = false, reset = "resetMultibox", input = TransitionConstants.Webdata.JSP_APC01E01)
	@MethodAccess(accessCode="WEBDATA_EDIT_UPMATERIAL")
	@WebdataAccessUser(accessType = AccessType.UPDATE_TIME)
	@Deprecated
	public String upMaterial() {

		// パラメータが空の場合はエラー
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, editForm.hiddenMaterialKbn);

		// 画像のアップロード処理
		doUploadMaterial(editForm);

		return TransitionConstants.Webdata.JSP_APC01E01;
	}


	/**
	 * サイズ変更の際に、未登録素材に黒画像を設定する。
	 */
	@Execute(validator = false, input = TransitionConstants.Webdata.JSP_APC01C01)
	public String setUnUploadMaterial() {
        final String sizeKbn = RequestUtil.getRequest().getParameter("sizeKbn");

        if (StringUtils.isEmpty(sizeKbn)) {
    		Map<String, String> resMap = new HashMap<>();
        	resMap.put("error", "画像表示に失敗しました");
    		ResponseUtil.write(JSON.encode(resMap), JSON_CONTENT_TYPE, ENCODING);
        	return null;
        }
        editForm.sizeKbn = sizeKbn;
		return setUnUploadedImages(editForm);
	}

	/**
	 * アップロードされた素材の削除処理
	 * @return 登録画面
	 */
	@Execute(validator=false, reset = "resetMultibox", input = TransitionConstants.Webdata.JSP_APC01E01)
	@MethodAccess(accessCode="WEBDATA_EDIT_DELETEMATERIAL")
	@WebdataAccessUser(accessType = AccessType.UPDATE_TIME)
	@Deprecated
	public String deleteMaterial() {

		// パラメータが空の場合はエラー
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, editForm.hiddenMaterialKbn);

		// 画像の削除処理
		doDeleteMaterial(editForm);

		return TransitionConstants.Webdata.JSP_APC01E01;
	}

	/**
	 * アップロードされた素材の削除処理
	 * @return 登録画面
	 */
	@Execute(validator=false, input = TransitionConstants.Webdata.JSP_APC01C01)
	@MethodAccess(accessCode="WEBDATA_EDIT_DELETEMATERIAL")
	public String deleteAjaxMaterial() {
		return deleteImage(editForm);
	}

	/**
	 * 画像からアクセスされる素材の表示処理
	 * 画像イメージのバイトのデータを出力ストリームにセットする。
	 * @return なし
	 */
	@Execute(validator = false, urlPattern = "displayMaterial/{targetMaterialKey}")
	@MethodAccess(accessCode="WEBDATA_EDIT_DISPLAYMATERIAL")
	public String displayMaterial() {
		displayMaterialData(editForm.targetMaterialKey, editForm.materialMap);
		return null;
	}

	/**
	 * 戻る
	 * @return 詳細画面の初期表示
	 */
	@Execute(validator = false, reset="resetFormWithoutId")
	@MethodAccess(accessCode="WEBDATA_EDIT_BACK")
	@WebdataAccessUser(accessType = AccessType.REMOVE)
	public String back() {

		// 確認画面の表示メソッドへリダイレクト
		return TransitionConstants.Webdata.ACTION_WEBDATA_DETAIL_INDEX + editForm.id + TransitionConstants.REDIRECT_STR;
	}

	/**
	 * 訂正
	 * @return 入力画面
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode = "WEBDATA_EDIT_CORRECT")
	@WebdataAccessUser(accessType = AccessType.UPDATE_TIME)
	public String correct() {

		// パラメータが空の場合はエラー
		checkArgsNull(NO_BLANK_FLG_NG, editForm.id);

		// プロセスフラグを未確認に設定
		editForm.setProcessFlgNg();

		// 登録画面へ遷移
		return TransitionConstants.Webdata.JSP_APC01E01;
	}

	/**
	 * ステータス変更
	 * @return ステータス完了
	 */
	@Execute(validator = false, reset="resetFormWithoutStatusValue", input = TransitionConstants.Webdata.JSP_APC01R01)
	@MethodAccess(accessCode="WEBDATA_EDIT_CHANGESTATUS")
	public String changeStatus() {

		// 確認画面のダイアログから遷移していない場合はエラー
		if (!editForm.processFlg) {
			callFraudulentProcessError(editForm);
		}

		// 掲載確定の場合はバリデートを行う。
		if (GourmetCareeUtil.eqInt(MTypeConstants.ChangeStatusKbn.FIXED_VALUE, editForm.changeStatusKbn)) {
			try {
				TWeb entity = webService.findById(Integer.parseInt(editForm.id));
				if (!validateDecide(entity)) {
					// 掲載確定時、サイズによる文字数が基準値を超えている場合、詳細画面へ戻す。
					String returnUrl = TransitionConstants.Webdata.ACTION_WEBDATA_DETAIL_INDEX + editForm.id;
					editForm.resetForm();
					return returnUrl;
				}


				// 原稿確定付与
				scoutMailLogic.addScoutMailByFixWeb(entity, NumberUtils.toInt(
												getCommonProperty("gc.scoutMail.webFix.add"),
												GourmetCareeConstants.FREE_SCOUT_MAIL_COUNT));


			} catch (SNoResultException e) {
				throw new ActionMessagesException("errors.app.dataNotFound");
			} catch (NumberFormatException e) {
				throw new ActionMessagesException("errors.app.dataNotFound");

			}
		}

		// ステータスの更新
		updateStatus();

		// セッションをクリア
		super.resetSessionImage();

		// 完了メソッドへリダイレクト
		return TransitionConstants.Webdata.REDIRECT_WEBDATA_EDIT_STATUSCOMP;
	}

	/**
	 * WEB表示フラグの変更
	 * @return 変更完了
	 */
	@Execute(validator = false, reset="resetFormWithoutStatusValue", input = TransitionConstants.Webdata.JSP_APC01R01)
	@MethodAccess(accessCode="WEBDATA_EDIT_CHANGESTATUS")
	public String changePublicationEndDisplayFlg() {

		// 確認画面のダイアログから遷移していない場合はエラー
		if (!editForm.processFlg) {
			callFraudulentProcessError(editForm);
		}

		updatePublicationEndDisplayFlg();

		// セッションをクリア
		super.resetSessionImage();

		// 完了メソッドへリダイレクト
		return TransitionConstants.Webdata.REDIRECT_WEBDATA_EDIT_STATUSCOMP;
	}

	/**
	 * 登録
	 * @return 完了へリダイレクト
	 */
	@Execute(validator = false, input = TransitionConstants.Webdata.JSP_APC01E01)
	@MethodAccess(accessCode="WEBDATA_EDIT_SUBMIT")
	public String submit() {

		// プロセスフラグがfalseの場合、未確認としてエラー
		if (!editForm.processFlg) {
			callFraudulentProcessError(editForm);
		}

		// セッションのIdと、画面で保持するIdが違う場合エラー
		if (!editForm.id.equals(editForm.hiddenId)) {
			callFraudulentProcessError(editForm);
		}

		// 編集処理
		edit();

		// セッションをクリア
		super.resetSessionImage();
		initUploadMaterial(editForm);

		// 完了メソッドへリダイレクト
		return TransitionConstants.Webdata.REDIRECT_WEBDATA_EDIT_COMP;
	}

	/**
	 * 完了
	 * @return 完了画面
	 */
	@Execute(validator = false, removeActionForm = true)
	@MethodAccess(accessCode="WEBDATA_EDIT_COMP")
	@WebdataAccessUser(accessType = AccessType.REMOVE)
	public String comp() {

		// 完了画面へ遷移
		return TransitionConstants.Webdata.JSP_APC01E03;
	}

	/**
	 * ステータス変更完了
	 * @return ステータス変更完了画面
	 */
	@Execute(validator = false, removeActionForm = true)
	@MethodAccess(accessCode="WEBDATA_EDIT_STATUSCOMP")
	public String statusComp() {

		// 完了画面へ遷移
		return TransitionConstants.Webdata.JSP_APC01E04;
	}

	/**
	 * 一覧へ戻る
	 * @return 一覧画面の検索メソッド
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="WEBDATA_EDIT_BACKLIST")
	public String backList() {
		// 一覧画面へ遷移
		return TransitionConstants.Webdata.REDIRECT_WEBDATA_LIST_SEARCHAGAIN;
	}

	/**
	 * 画面の上下からボタン一覧へ戻る
	 * @return 一覧画面
	 * @author Makoto Otani
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="WEBDATA_EDIT_RESET_BACKLIST")
	@WebdataAccessUser(accessType = AccessType.REMOVE)
	public String backResetList() {
		// 一覧画面へ遷移
		return TransitionConstants.Webdata.REDIRECT_WEBDATA_LIST_INDEX;
	}

	/**
	 * 画面の上下ボタンからTOPページへ戻る
	 * @return TOPページ
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="WEBDATA_EDIT_BACKTOP")
	@WebdataAccessUser(accessType = AccessType.REMOVE)
	public String backTop() {
		// 一覧画面へ遷移
		return TransitionConstants.Top.REDIRECT_TOP_MENU;
	}


	/**
	 * 確認画面の店舗一覧へ遷移
	 * @return 店舗一覧の表示
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="WEBDATA_EDIT_INDEXWEBDETAIL")
	@WebdataAccessUser(accessType = AccessType.REMOVE)
	public String indexWebDetail() {

		checkArgsNull(NO_BLANK_FLG_NG ,editForm.id);

		// 店舗一覧画面へ遷移
		return  String.format(TransitionConstants.ShopList.ACTION_SHOPLIST_INDEX_INDEXWEBDATAIL, editForm.customerDto.id, editForm.id);
	}


	/**
	 * 初期表示遷移用
	 * @return 登録画面のパス
	 */
	private String show() {

		initUploadMaterial(editForm);

		// DBから取得したデータを表示用に変換
		try {
			// DBから取得したデータを表示用に変換
			createDisplayValue(getData(editForm));

			// よく使われているタグをセット
			editForm.tagListDto = super.createTagList();
			super.getWebDataTag(Integer.parseInt(editForm.id), editForm);


		} catch (WNoResultException e) {

			// 画面表示をしない
			editForm.setExistDataFlgNg();
			// データが取得できないエラーを表示
			dataNotFoundMessage();
		}

		// カーソル位置をリセット
		editForm.cursorPosition = "";

		// 登録画面へ遷移
		return TransitionConstants.Webdata.JSP_APC01E01;
	}

	/**
	 * 検索結果を画面表示にFormに移し返すロジック<br />
	 * 更新用に会社エリアマスタの値を保持
	 * @param entity MCompanyエンティティ
	 */
	private void createDisplayValue(WebdataProperty property) {

		try {

			// 編集可能なデータかチェック
			if (!webdataLogic.checkEdit(property)) {
				// 権限エラー
				callAuthLevelError(editForm);
			}

		} catch (WNoResultException e) {
			// 不正な処理エラー
			callFraudulentProcessError(editForm);
		}

		// 画面表示用に値を作成
		createDisplayValue(property, editForm);

		// 画面表示ステータスをセット
		try {
			editForm.displayStatus = String.valueOf(webdataLogic.getDisplayStatus(property));

		// データが取得できない場合はエラー
		} catch (WNoResultException e) {
			dataNotFoundMessage();
		}

		// 号数が登録されている場合
		if (!StringUtil.isEmpty(editForm.volumeId) && property.mVolume != null) {

			// 下書きの場合
			if (String.valueOf(MStatusConstants.DisplayStatusCd.DRAFT).equals(editForm.displayStatus)) {

				// 管理者以外の場合
				if (!ManageAuthLevel.ADMIN.value().equals(userDto.authLevel) && !ManageAuthLevel.SALES.value().equals(userDto.authLevel)) {

					// 締切日を過ぎていれば編集不可
					Date today = new Date();
					if (!StringUtil.isEmpty(editForm.volumeId) && property.mVolume != null && today.after(property.mVolume.deadlineDatetime)) {
						editForm.isEditVolumeFlg = false;
					}
				}

			// 下書き以外の場合は編集不可
			} else {
				editForm.isEditVolumeFlg = false;
			}
		}

		if(property.tWeb.attentionShopStartDate != null && property.tWeb.attentionShopEndDate != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			editForm.attentionShopStartDate = sdf.format(property.tWeb.attentionShopStartDate);
			editForm.attentionShopEndDate = sdf.format(property.tWeb.attentionShopEndDate);
		}

		if(property.tWeb.searchPreferentialStartDate != null && property.tWeb.searchPreferentialEndDate != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			editForm.searchPreferentialStartDate = sdf.format(property.tWeb.searchPreferentialStartDate);
			editForm.searchPreferentialEndDate = sdf.format(property.tWeb.searchPreferentialEndDate);
		}

		property = null;
	}



	@Override
	protected void createDisplayValue(WebdataProperty property, WebdataForm form) {
		final TWeb web = property.tWeb;
		super.createDisplayValue(property, form);
		if (web == null) {
			return;
		}

		super.createIpPhoneData(property.tWeb, form);
	}

	/**
	 * ステータスを更新
	 */
	private void updateStatus() {

		WebdataProperty property = new WebdataProperty();
		TWeb tWeb = new TWeb();

		// エンティティにID、versionをコピー
		Beans.copy(editForm, tWeb).includes(toCamelCase(TWeb.ID), toCamelCase(TWeb.VERSION)).execute();
		// 最終編集日時をセット
		tWeb.lastEditDatetime = new Date();

		// エンティティをセット
		property.tWeb = tWeb;
		WebdataControlDto controlDto = new WebdataControlDto();

		try {
			// ステータス更新(メールの制御のためDtoを保持)
			controlDto = webdataLogic.updateStatus(property, editForm.changeStatusKbn);
			log.debug("ステータスを更新しました。" + editForm);

		// 会社が取得できない場合はエラー
		} catch (NoExistCompanyDataException e) {

			// 画面表示をしない
			editForm.setExistDataFlgNg();

			// 「{会社}が削除されている可能性があるため、{ステータス変更}することはできません。」
			throw new ActionMessagesException("errors.canNotProcessDel",
												MessageResourcesUtil.getMessage("msg.app.company"),
												MessageResourcesUtil.getMessage("msg.app.changeStatus"));

		// 営業担当者が取得できない場合はエラー
		} catch (NoExistSalesDataException e) {

			// 画面表示をしない
			editForm.setExistDataFlgNg();

			// 「{営業担当}が削除されている可能性があるため、{ステータス変更}することはできません。」
			throw new ActionMessagesException("errors.canNotProcessDel",
												MessageResourcesUtil.getMessage("labels.salesId"),
												MessageResourcesUtil.getMessage("msg.app.changeStatus"));

		} catch (WNoResultException e) {

			// 画面表示をしない
			editForm.setExistDataFlgNg();
			// データが取得できないエラーを表示
			dataNotFoundMessage();

		// 楽観的排他制御でエラーとなった場合は、画面非表示にしてエラーを投げる(戻るボタンも該当)
		}catch (SOptimisticLockException e) {
			editForm.resetForm();
			editForm.setExistDataFlgNg();
			throw new SOptimisticLockException("WEBデータのステータス更新時に楽観的排他制御エラーが発生しました。");
		}

		/* メール送信処理 */

		// 掲載確定で処理可能、掲載依頼の場合は完了メールを送信する
		if (String.valueOf(MTypeConstants.ChangeStatusKbn.FIXED_VALUE).equals(editForm.changeStatusKbn) &&
				controlDto.fixedFlg &&
					String.valueOf(MStatusConstants.DisplayStatusCd.REQ_APPROVAL).equals(editForm.displayStatus)) {

			// メール送信
			sendFixedMail(property);

			log.debug("確定メールを送信しました。" + editForm);

		// 掲載依頼の場合は依頼メールを送信する
		} else if (String.valueOf(MTypeConstants.ChangeStatusKbn.POST_REQUEST_VALUE).equals(editForm.changeStatusKbn) &&
				controlDto.postRequestFlg) {

			// メールの送信
			sendPostRequestMail(property);

			log.debug("依頼メールを送信しました。" + editForm);
		}
	}

	/**
	 * WEB表示フラグを更新
	 */
	private void updatePublicationEndDisplayFlg() {

		WebdataProperty property = new WebdataProperty();
		TWeb tWeb = new TWeb();

		// エンティティにID、versionをコピー
		Beans.copy(editForm, tWeb).includes(toCamelCase(TWeb.ID), toCamelCase(TWeb.VERSION)).execute();
		// 最終編集日時をセット
		tWeb.lastEditDatetime = new Date();

		// エンティティをセット
		property.tWeb = tWeb;
		WebdataControlDto controlDto = new WebdataControlDto();

		try {
			// ステータス更新(メールの制御のためDtoを保持)
			controlDto = webdataLogic.updatePublicationEndDisplayFlg(property, editForm.publicationEndDisplayFlg);
			log.debug("ステータスを更新しました。" + editForm);

		// 会社が取得できない場合はエラー
		} catch (NoExistCompanyDataException e) {

			// 画面表示をしない
			editForm.setExistDataFlgNg();

			// 「{会社}が削除されている可能性があるため、{ステータス変更}することはできません。」
			throw new ActionMessagesException("errors.canNotProcessDel",
												MessageResourcesUtil.getMessage("msg.app.company"),
												MessageResourcesUtil.getMessage("msg.app.changeStatus"));

		// 営業担当者が取得できない場合はエラー
		} catch (NoExistSalesDataException e) {

			// 画面表示をしない
			editForm.setExistDataFlgNg();

			// 「{営業担当}が削除されている可能性があるため、{ステータス変更}することはできません。」
			throw new ActionMessagesException("errors.canNotProcessDel",
												MessageResourcesUtil.getMessage("labels.salesId"),
												MessageResourcesUtil.getMessage("msg.app.changeStatus"));

		} catch (WNoResultException e) {

			// 画面表示をしない
			editForm.setExistDataFlgNg();
			// データが取得できないエラーを表示
			dataNotFoundMessage();

		// 楽観的排他制御でエラーとなった場合は、画面非表示にしてエラーを投げる(戻るボタンも該当)
		} catch (SOptimisticLockException e) {
			editForm.resetForm();
			editForm.setExistDataFlgNg();
			throw new SOptimisticLockException("WEBデータのステータス更新時に楽観的排他制御エラーが発生しました。");
		}
	}

	/**
	 * プロパティから管理用メールアドレスを取得する
	 * @return 管理メールアドレス
	 */
	private String getAdminMailAddress() {
		// 送受信の管理メールアドレスを取得
		return ResourceUtil.getProperties("sendMail.properties").getProperty("gc.mail.kanriAddress");
	}

	/**
	 * プロパティから管理用メール名を取得する
	 * @return 管理メール名
	 */
	private String getAdminMailName() {
		// 送受信の管理メール名を取得
		return ResourceUtil.getProperties("sendMail.properties").getProperty("gc.mail.kanriName");
	}

	/**
	 * 掲載確定完了のメールを送信する
	 * @param property WEBデータプロパティ
	 */
	private void sendFixedMail(WebdataProperty property) {

		// 管理用メールアドレスの取得
		String mailAddress = getAdminMailAddress();
		if (StringUtil.isEmpty(mailAddress)) {
			// メールアドレスが存在しないため未処理
			log.info("掲載確定完了のメールを送信できませんでした");
			return;
		}
		// 管理用メール名の取得
		String mailName = getAdminMailName();

		// 宛先を保持するリスト
		List<String> toAddressList = new ArrayList<String>();

		// 管理アドレスをセット
		toAddressList.add(mailAddress);

		// 送信先に代理店のメインメールをセット
		toAddressList.add(property.mCompany.mainMail);
		// サブアドレスが送信可の場合、送信先にセット
		if (eqInt(MTypeConstants.SubmailReceptionFlg.RECEIVE, property.mCompany.submailReceptionFlg)) {
			toAddressList.add(property.mCompany.subMail);
		}

		// WEBデータ担当者のメインアドレスをセット
		toAddressList.add(property.mSales.mainMail);

		// サブアドレスが送信可の場合、送信先にセット
		if (eqInt(MTypeConstants.SubmailReceptionFlg.RECEIVE, property.mSales.submailReceptionFlg)) {
			toAddressList.add(property.mSales.subMail);
		}

		FixedMaiDto fixedMaiDto = new FixedMaiDto();
		// 送信元
		try {
			fixedMaiDto.setFrom(mailAddress, mailName);
		// アドレスがセットできない場合、初期値で送信
		} catch (UnsupportedEncodingException e) {
			// 送信元のセットに失敗した場合はエラーを送信する。（mailProperties.diconの値がセットされる）
			log.fatal("掲載確定完了のメールを送信時、送信元がセットできませんでした。" + e);
		}
		// 原稿番号
		fixedMaiDto.setId(editForm.id);
		// 会社名（代理店名）
		fixedMaiDto.setCompanyName(property.mCompany.companyName);

		// 顧客が未登録の場合
		if (property.mCustomer == null) {
			// 顧客名(「顧客未登録」)
			fixedMaiDto.setCustomerName(ResourceUtil.getProperties("sendMail.properties").getProperty("gc.msg.noCustomer"));
			// 顧客ID
			fixedMaiDto.setCustomerId("");

		// 顧客が登録されている場合
		} else {
			// 顧客名
			fixedMaiDto.setCustomerName(property.mCustomer.customerName);
			// 顧客ID
			fixedMaiDto.setCustomerId(String.valueOf(property.mCustomer.id));
		}

		// 送信先をセットして確定完了メールを送信
		for (String toAddress : toAddressList) {
			// 送信先をセット
			fixedMaiDto.resetTo();
			fixedMaiDto.addTo(toAddress);
			// メール送信処理実行
			gourmetCareeMai.sendFixedMail(fixedMaiDto);
		}
	}

	/**
	 * 掲載依頼完了のメールを送信する
	 * @param property WEBデータプロパティ
	 */
	private void sendPostRequestMail(WebdataProperty property) {

		// 管理用メールアドレスの取得
		String mailAddress = getAdminMailAddress();
		if (StringUtil.isEmpty(mailAddress)) {
			// メールアドレスが存在しないため未処理
			log.info("掲載依頼完了のメールを送信できませんでした");
			return;
		}
		// 管理用メール名の取得
		String mailName = getAdminMailName();

		// メール送信Dtoにセット
		PostRequestMaiDto postRequestMaiDto = new PostRequestMaiDto();

		// 送信先
		postRequestMaiDto.addTo(mailAddress);
		// 送信元
		try {
			postRequestMaiDto.setFrom(mailAddress, mailName);
		// アドレスがセットできない場合、初期値で送信
		} catch (UnsupportedEncodingException e) {
			// 送信元のセットに失敗した場合はエラーを送信する。（mailProperties.diconの値がセットされる）
			log.fatal("掲載依頼完了のメールを送信時、送信元がセットできませんでした。" + e);
		}
		// 原稿番号
		postRequestMaiDto.setId(editForm.id);
		// 会社名（代理店名）
		postRequestMaiDto.setCompanyName(property.mCompany.companyName);
		// 会社ID（代理店ID）
		postRequestMaiDto.setCompanyId(String.valueOf(property.mCompany.id));

		// 顧客が未登録の場合
		if (property.mCustomer == null) {
			// 顧客名(「顧客未登録」)
			postRequestMaiDto.setCustomerName(ResourceUtil.getProperties("sendMail.properties").getProperty("gc.msg.noCustomer"));
			// 顧客ID
			postRequestMaiDto.setCustomerId("");

		// 顧客が登録されている場合
		} else {
			// 顧客名
			postRequestMaiDto.setCustomerName(property.mCustomer.customerName);
			// 顧客ID
			postRequestMaiDto.setCustomerId(String.valueOf(property.mCustomer.id));
		}

		// メール送信処理実行
		gourmetCareeMai.sendPostRequestMail(postRequestMaiDto);
	}

	/**
	 * WEBデータを更新
	 */
	private void edit() {

		// 業種を並び替え
		sortIndustryKbn(editForm);
		// ホームページを並び替え
		sortHomepage(editForm);

		// 受け渡し用プロパティ
		WebdataProperty property = new WebdataProperty();

		// WEBデータエンティティ
		property.tWeb = new TWeb();
		// WEBデータエンティティにフォームをコピー
		Beans.copy(editForm, property.tWeb).excludes("customerId", "attentionShopStartDate","attentionShopEndDate", "searchPreferentialStartDate", "searchPreferentialEndDate", "serialPublication").execute();

		if(!StringUtil.isEmpty(editForm.attentionShopStartDate) && !StringUtil.isEmpty(editForm.attentionShopEndDate)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
			try {
				property.tWeb.attentionShopStartDate = sdf.parse(editForm.attentionShopStartDate + " 10:30:00");
				property.tWeb.attentionShopEndDate = sdf.parse(editForm.attentionShopEndDate + " 10:29:00");
			} catch (ParseException e) {
				throw new ActionMessagesException("errors.app.dateFailed");
			}
		}

		if(!StringUtil.isEmpty(editForm.searchPreferentialStartDate) && !StringUtil.isEmpty(editForm.searchPreferentialEndDate)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
			try {
				property.tWeb.searchPreferentialStartDate = sdf.parse(editForm.searchPreferentialStartDate + " 10:30:00");
				property.tWeb.searchPreferentialEndDate = sdf.parse(editForm.searchPreferentialEndDate + " 10:29:00");
			} catch (ParseException e) {
				throw new ActionMessagesException("errors.app.dateFailed");
			}
		}

		if(StringUtils.isNotBlank(editForm.serialPublication)) {
			property.tWeb.serialPublication = editForm.serialPublication;
		}

		// 顧客IDをセット
		if (!StringUtil.isEmpty(editForm.customerDto.id)) {
			property.tWeb.customerId = Integer.parseInt(editForm.customerDto.id);
		}
		// 最終編集日時をセット
		property.tWeb.lastEditDatetime = new Date();

		// WEBデータ属性
		settWebAttributeProperty(property, editForm);

		// WEBデータ特集
		setTWebSpecialProperty(property, editForm);

		// 路線図の値をプロパティにセット
		setTWebRouteProperty(property, editForm);

		// 素材の値をプロパティにセット
		setTMaterialProperty(property, editForm);

		// 系列店舗をセット
		setWebShopListProperty(property, editForm);

		// 職種をセット
		setWebJobProperty(property, editForm);

		property.addScoutMailCount = NumberUtils.toInt(getCommonProperty("gc.scoutMail.webFix.add"),
											GourmetCareeConstants.FREE_SCOUT_MAIL_COUNT);

		// 求人識別番号が未入力の場合
		if (StringUtil.isBlank(editForm.webNo)) {
			// 求人IDを求人識別番号にセット
			property.tWeb.webNo = property.tWeb.id;
		}

		// 更新
		webdataLogic.updateWebData(property);

		// 公開側キャッシュ画像の作成
		createFrontImage(property.tMaterialList);

		List<String> tagList = new ArrayList<>();
		if (StringUtils.isNotBlank(editForm.tagList)) {
			tagList = Arrays.asList(editForm.tagList.split(","));
		}
		tagListLogic.updateWebData(tagList, Integer.parseInt(editForm.id));


		webDataLog.info("WEBデータを更新しました。" + editForm);
		webDataLog.info("更新者：" + userDto);
	}

	/**
	 * プレビューを表示します。
	 * @return
	 */
	@Execute(validator = true, validate = "validate", reset = "resetMultibox", input = TransitionConstants.JSP_PREVIEW_ERROR)
	@WebdataAccessUser(accessType = AccessType.UPDATE_TIME)
	public String previewPc() {
		return tempPreview(editForm);
	}

	/**
	 * プレビューを表示します。
	 * @return
	 */
	@Execute(validator = true, validate = "validate", reset = "resetMultibox", input = TransitionConstants.JSP_PREVIEW_ERROR)
	@WebdataAccessUser(accessType = AccessType.UPDATE_TIME)
	public String previewSmartPhone() {
		return tempPreview(editForm);
	}

	public boolean isAlertOtherAccessFlg() {
		return alertOtherAccessFlg;
	}

	public void setAlertOtherAccessFlg(boolean alertOtherAccessFlg) {
		this.alertOtherAccessFlg = alertOtherAccessFlg;
	}

	/**
	 * WEBデータ・アクセス用DTOを取得します。
	 * @return WEBデータ・アクセス用DTO
	 * @author Makoto Otani
	 */
	public WebDataAccessDto getWebDataAccessDto() {

		return webDataEditorDto.getWebDataAccessDto(NumberUtils.toInt(editForm.id));
	}

	/**
	 * IP電話番号を外部APIより取得します。
	 *
	 * @return
	 *
	 * @author hara
	 */
	@Execute(validator=false)
	@WebdataAccessUser(accessType = AccessType.UPDATE_TIME)
	public String ipPhone() {

		// 登録データリスト
		List<ClientData> dataList = new ArrayList<ClientData>();

		final int customerId = NumberUtils.toInt(editForm.customerId);

		if (customerId == 0) {
			return null;
		}

		dataList.add(createClientData(customerId, 1, editForm.phoneNo1, editForm.ipPhoneNo1));
		dataList.add(createClientData(customerId, 2, editForm.phoneNo2, editForm.ipPhoneNo2));
		dataList.add(createClientData(customerId, 3, editForm.phoneNo3, editForm.ipPhoneNo3));

		if (CollectionUtils.isEmpty(dataList)) {
			return null;
		}

		List<TWebIpPhone> list = null;
		// IP電話番号を取得します
		list = iPPhoneLogic.createIpPhoneNumber(dataList);

		if (CollectionUtils.isNotEmpty(list)) {
			for (TWebIpPhone ipPhone : list) {
				ipPhone.ipTel = GourmetCareeUtil.devideIpPhoneNumber(ipPhone.ipTel);
			}
			writeJson(list);
		}


		return null;
	}

	/**
	 * IP電話の登録用データを作成します。
	 *
	 * @author hara
	 */
	private ClientData createClientData(Integer customerId, Integer edaNo, String phoneNo, String ipPhoneNo) {

		ClientData data = new ClientData();
		data.webId = Integer.parseInt(editForm.id);
		data.customerId = customerId;
		data.customerName =  customerService.getCustomerName(customerId);

		data.edaNo = edaNo;
		if(GourmetCareeUtil.checkPhoneNo(phoneNo)){
			data.customerTel = phoneNo.replaceAll("-", "");
		}
		data.ipTel = ipPhoneNo;

		data.createClientCd();

		return data;
	}

	public class IPPhoneDto {

		public List<TWebIpPhone> list;

	}
}