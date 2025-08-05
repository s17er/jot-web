package com.gourmetcaree.admin.pc.webdata.action.webdata;


import static com.gourmetcaree.common.constants.GourmetCareeConstants.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.util.StringUtil;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.RequestUtil;
import org.seasar.struts.util.ResponseUtil;

import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.pc.webdata.form.webdata.InputForm;
import com.gourmetcaree.admin.service.dto.IPPhoneDataCopyDto;
import com.gourmetcaree.admin.service.logic.IPPhoneLogic;
import com.gourmetcaree.admin.service.property.WebdataProperty;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TWeb;
import com.gourmetcaree.db.common.exception.WNoResultException;

import net.arnx.jsonic.JSON;

/**
 *
 * WEBデータ登録を行うクラス
 * @author Makoto Otani
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN,ManageAuthLevel.STAFF,ManageAuthLevel.AGENCY, ManageAuthLevel.OTHER, ManageAuthLevel.SALES})
public class InputAction extends WebdataBaseAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(InputAction.class);

	/** WEBデータログ */
	private static Logger webDataLog = Logger.getLogger("webdataLog." + InputAction.class);

	/** フォーム */
	@ActionForm
	@Resource
	protected InputForm inputForm;

	@Resource
	private IPPhoneLogic iPPhoneLogic;

	/**
	 * 初期表示
	 * @return 入力画面
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Webdata.JSP_APC01C01)
	@MethodAccess(accessCode = "WEBDATA_INPUT_INDEX")
	public String index() {

		return show();

	}

	/**
	 * 素材のアップロードを行う。
	 * @return 登録画面
	 */
	@Execute(validator = false, reset = "resetMultibox", input = TransitionConstants.Webdata.JSP_APC01C01)
	@MethodAccess(accessCode="WEBDATA_INPUT_UPMATERIAL")
	@Deprecated
	public String upMaterial() {

		// パラメータが空の場合はエラー
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, inputForm.hiddenMaterialKbn);

		// 画像のアップロード処理
		doUploadMaterial(inputForm);

		return TransitionConstants.Webdata.JSP_APC01C01;
	}

	/**
	 * 素材のアップロードを行う。
	 * @return 登録画面
	 */
	@Execute(validator = false, input = TransitionConstants.Webdata.JSP_APC01E01)
	 @MethodAccess(accessCode="WEBDATA_INPUT_UPMATERIAL")
	public String upAjaxMaterial() {
		return uploadImage(inputForm);
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
        inputForm.sizeKbn = sizeKbn;
		return setUnUploadedImages(inputForm);
	}

	/**
	 * アップロードされた素材の削除処理
	 * @return 登録画面
	 */
	@Execute(validator=false, reset = "resetMultibox", input = TransitionConstants.Webdata.JSP_APC01C01)
	@MethodAccess(accessCode="WEBDATA_INPUT_DELETEMATERIAL")
	@Deprecated
	public String deleteMaterial() {

		// パラメータが空の場合はエラー
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, inputForm.hiddenMaterialKbn);

		// 画像の削除処理
		doDeleteMaterial(inputForm);

		return TransitionConstants.Webdata.JSP_APC01C01;
	}

	/**
	 * アップロードされた素材の削除処理
	 * @return 登録画面
	 */
	@Execute(validator=false, input = TransitionConstants.Webdata.JSP_APC01C01)
	@MethodAccess(accessCode="WEBDATA_INPUT_DELETEMATERIAL")
	public String deleteAjaxMaterial() {
		return deleteImage(inputForm);
	}

	/**
	 * 画像からアクセスされる素材の表示処理
	 * 画像イメージのバイトのデータを出力ストリームにセットする。
	 * @return なし
	 */
	@Execute(validator = false, urlPattern = "displayMaterial/{targetMaterialKey}")
	@MethodAccess(accessCode="WEBDATA_INPUT_DISPLAYMATERIAL")
	public String displayMaterial() {
		displayMaterialData(inputForm.targetMaterialKey, inputForm.materialMap);
		return null;
	}

	/**
	 * 確認
	 * @return 確認画面
	 */
	@Execute(validator = true, validate = "@, validate", reset = "resetMultibox", stopOnValidationError = false, input = TransitionConstants.Webdata.JSP_APC01C01)
	@MethodAccess(accessCode = "WEBDATA_INPUT_CONF")
	public String conf() {

		// エラーがある場合は、入力画面へ遷移
		if (!canSelectCompany(inputForm) || !isCustomerData(inputForm) || !checkInputStr(inputForm) || !checkJobWorkContents(inputForm)) {

			// プロセスフラグを未確認に設定
			inputForm.setProcessFlgNg();

			// 入力画面へ遷移
			return TransitionConstants.Webdata.JSP_APC01C01;
		}

		// テキストエリアの文字列を整形
		inputForm.convertTextAreaData();

		// 確認画面の系列店舗をセット
		createConfShopList(inputForm);

		// プロセスフラグを確認済みに設定
		inputForm.setProcessFlgOk();

		// 確認画面へ遷移
		return TransitionConstants.Webdata.JSP_APC01C02;
	}

	/**
	 * 訂正
	 * @return 入力画面
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode = "WEBDATA_INPUT_CORRECT")
	public String correct() {

		// プロセスフラグを未確認に設定
		inputForm.setProcessFlgNg();

		// 登録画面へ遷移
		return TransitionConstants.Webdata.JSP_APC01C01;
	}

	/**
	 * 登録
	 * @return 完了へリダイレクト
	 */
	@Execute(validator = false, input = TransitionConstants.Webdata.JSP_APC01C01)
	@MethodAccess(accessCode = "WEBDATA_INPUT_SUBMIT")
	public String submit() {

		// プロセスフラグがfalseの場合、未確認としてエラー
		if (!inputForm.processFlg) {
			callFraudulentProcessError(inputForm);
		}

		// 登録前に再チェック（エラーがある場合は、入力画面へ遷移）
		if (!canSelectArea(inputForm) || !canSelectCompany(inputForm) || !isCustomerData(inputForm)) {
			// プロセスフラグを未確認に設定
			inputForm.setProcessFlgNg();
			// 入力画面へ遷移
			return TransitionConstants.Webdata.JSP_APC01C01;
		}

		// 登録処理の呼び出し
		insert();

		// セッションをクリア
		super.resetSessionImage();
		initUploadMaterial(inputForm);

		// コピーから遷移した際は、一覧へ戻るボタンのある画面へ遷移
		if(StringUtil.isNotEmpty(inputForm.sourceWebId)){
			return TransitionConstants.Webdata.REDIRECT_WEBDATA_INPUT_FROMCOPYCOMP;
		}

		// 完了メソッドへリダイレクト
		return TransitionConstants.Webdata.REDIRECT_WEBDATA_INPUT_COMP;
	}

	/**
	 * 完了
	 * @return 完了画面
	 */
	@Execute(validator = false, removeActionForm = true)
	@MethodAccess(accessCode = "WEBDATA_INPUT_COMP")
	public String comp() {

		// 完了画面へ遷移
		return TransitionConstants.Webdata.JSP_APC01C03;
	}

	/**
	 * コピーからの完了
	 * @return 完了画面
	 */
	@Execute(validator = false, removeActionForm = true)
	@MethodAccess(accessCode = "WEBDATA_INPUT_COMP")
	public String fromCopyComp() {

		// 完了画面へ遷移
		return TransitionConstants.Webdata.JSP_APC01C04;
	}

	/**
	 * コピー
	 * @return コピー完了画面へリダイレクト
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Webdata.JSP_APC01C01)
	@MethodAccess(accessCode = "WEBDATA_INPUT_COPY")
	public String copy() {

		// プロセスフラグがfalseの場合、未確認としてエラー
		if (!inputForm.processFlg) {
			callFraudulentProcessError(inputForm);
		}

		initUploadMaterial(inputForm);
		inputForm.copyFlg = true;

		// 登録画面へ遷移
		try {

			/* WebIdを退避 */
			String tempWebId = inputForm.id;

			// DBから取得したデータを表示用に変換
			createDisplayValue(getData(inputForm), inputForm);

			getWebDataTag(Integer.parseInt(tempWebId), inputForm);

			// よく使われているタグをセット
			inputForm.tagListDto = super.createTagList();

			log.debug("コピーデータを取得");

		} catch (WNoResultException e) {

			// 画面表示をしない
			inputForm.setExistDataFlgNg();
			// データが取得できないエラーを表示
			dataNotFoundMessage();
		}

		// コピー完了画面へリダイレクト
		return TransitionConstants.Webdata.JSP_APC01C01;
	}

	/**
	 * 完了
	 * @return 完了画面
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode = "WEBDATA_INPUT_COPYCOMP")
	public String copyComp() {

		// 完了画面へ遷移
		return TransitionConstants.Webdata.JSP_APC01C01;
	}

	/**
	 * 初期表示遷移用
	 * @return 入力画面のパス
	 */
	private String show() {

		//初期
		initUploadMaterial(inputForm);

		// 自身の会社IDをセット
		if(StringUtil.isEmpty(inputForm.companyId)) {
			inputForm.companyId = userDto.companyId;
		}
		// 自身の営業担当者IDをセット
		if(StringUtil.isEmpty(inputForm.salesId)) {
			inputForm.salesId = userDto.userId;
		}

		// 電話番号の初期値をセット
		inputForm.phoneReceptionist = "担当/";
		// 応募方法の初期メッセージをセット
		inputForm.applicationMethod = DEFAULT_APPLICATION_MESSAGE;

		// 事前登録の初期値をセット
		inputForm.briefingPresentKbn = String.valueOf(MTypeConstants.BriefingPresentKbn.NOT_JOIN);

		// よく使われているタグをセット
		inputForm.tagListDto = super.createTagList();

		inputForm.searchPreferentialFlg = MTypeConstants.SearchPreferentialFlg.NO_TARGET;

		// 登録画面へ遷移
		return TransitionConstants.Webdata.JSP_APC01C01;
	}
	/**
	 * 登録処理
	 */
	private void insert() {

		// リニューアルで項目削除
//		// 業種を並び替え
//		sortIndustryKbn(inputForm);
//		// ホームページを並び替え
//		sortHomepage(inputForm);

		// 受け渡し用プロパティ
		WebdataProperty property = new WebdataProperty();

		// WEBデータエンティティ
		property.tWeb = new TWeb();
		// WEBデータエンティティにフォームをコピー
		Beans.copy(inputForm, property.tWeb).excludes("customerId", "attentionShopStartDate","attentionShopEndDate", "searchPreferentialStartDate", "searchPreferentialEndDate", "serialPublication").execute();

		if(!StringUtil.isEmpty(inputForm.attentionShopStartDate) && !StringUtil.isEmpty(inputForm.attentionShopEndDate)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
			try {
				property.tWeb.attentionShopStartDate = sdf.parse(inputForm.attentionShopStartDate + " 10:30:00");
				property.tWeb.attentionShopEndDate = sdf.parse(inputForm.attentionShopEndDate + " 10:29:00");
			} catch (ParseException e) {
				throw new ActionMessagesException("errors.app.dateFailed");
			}
		}

		if(!StringUtil.isEmpty(inputForm.searchPreferentialStartDate) && !StringUtil.isEmpty(inputForm.searchPreferentialEndDate)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
			try {
				property.tWeb.searchPreferentialStartDate = sdf.parse(inputForm.searchPreferentialStartDate + " 10:30:00");
				property.tWeb.searchPreferentialEndDate = sdf.parse(inputForm.searchPreferentialEndDate + " 10:29:00");
			} catch (ParseException e) {
				throw new ActionMessagesException("errors.app.dateFailed");
			}
		}

		if(StringUtils.isNotBlank(inputForm.serialPublication)) {
			property.tWeb.serialPublication = inputForm.serialPublication;
		}

		// 顧客IDをセット
		if (!StringUtil.isEmpty(inputForm.customerDto.id)) {
			property.tWeb.customerId = Integer.parseInt(inputForm.customerDto.id);
		}

		// WEBデータ属性
		settWebAttributeProperty(property, inputForm);

		// 職種
		setWebJobProperty(property, inputForm);

		// 系列店舗
		setWebShopListProperty(property, inputForm);

		// WEBデータ特集
		setTWebSpecialProperty(property, inputForm);

		// 素材の値をプロパティにセット
		setTMaterialProperty(property, inputForm);

		// WEBデータを登録
		webdataLogic.insertWebData(property);

		// 公開画像のキャッシュ作成
		createFrontImage(property.tMaterialList);


		webDataLog.info("WEBデータを登録しました。webId=" + property.tWeb.id + inputForm);

		webDataLog.info("登録者：" + userDto);
		// TODO WEBデータログ
//		log.debug("WEBデータを登録しました。" + inputForm);

		List<String> tagList = new ArrayList<>();
		if (StringUtils.isNotBlank(inputForm.tagList)) {
			tagList = Arrays.asList(inputForm.tagList.split(","));
		}
		tagListLogic.insertWebTag(tagList, property.tWeb.id);


		TWeb web = property.tWeb;

		// IP電話のコピー
		copyIpPhone(web);
	}

	/**
	 * 検索結果をFormに移し返すロジック
	 * @param entity WEBデータエンティティ
	 * @param form WEBデータフォーム
	 * @throws WNoResultException
	 */
	protected void createDisplayValue(WebdataProperty property, InputForm form) throws WNoResultException {

		// 代理店がアクセス可能かチェック
		checkAgencyAccess(property.tWeb, form);
		// 自社スタッフがアクセス可能かチェック
		checkStaffAccess(property.tWeb, form);

		// 画面表示共通処理
		super.createDisplayValue(property, form);

		// コピー元のIDを過去原版にセット
		form.sourceWebId = form.id;
		// コピー元のIDをクリア
		form.id = null;
		// コピー元の号数をクリア
		form.volumeId = null;
		// メモをクリア
//		form.memo = null;
		/*
		 * <依頼メモ>　※一括コピーも対応
		 * １．原稿入稿画面の2連載、3連載のステイタスについて
		 * 2連載、もしくは3連載目を忘れず入稿するためのメモ的な機能ですが、
		 * これを、初入稿時、2連載と選択して入稿し、
		 * 2週間後、2連載入稿する為にコピーすると、ステイタスが自動で2連載OKになっている、
		 * ようになるととても助かります。
		 * 3連載入稿した場合には、2週間後に3連載中の2回目を入稿する場合、
		 * 自動的にステイタスのプルダウンメニューが「2連載」を選択してあるのが望ましいです。
		 */
		if (StringUtil.isNotEmpty(form.serialPublicationKbn)) {
			Integer serialPublicationKbn = Integer.parseInt(form.serialPublicationKbn);
			// 2連載の場合は２連載OK
			if (GourmetCareeUtil.eqInt(MTypeConstants.SERIAL_PUBLICATION_KBN.DOUBLE, serialPublicationKbn)) {
				form.serialPublicationKbn = String.valueOf(MTypeConstants.SERIAL_PUBLICATION_KBN.DOUBLE_OK);

			// 3連載の場合は２連載
			} else if (GourmetCareeUtil.eqInt(MTypeConstants.SERIAL_PUBLICATION_KBN.TRIPLE, serialPublicationKbn)) {
				form.serialPublicationKbn = String.valueOf(MTypeConstants.SERIAL_PUBLICATION_KBN.DOUBLE);

			// 定額制の場合は定額制
			} else if (GourmetCareeUtil.eqInt(MTypeConstants.SERIAL_PUBLICATION_KBN.FLAT_RATE_SYSTEM, serialPublicationKbn)) {
				form.serialPublicationKbn = String.valueOf(MTypeConstants.SERIAL_PUBLICATION_KBN.FLAT_RATE_SYSTEM);

			} else {
				form.serialPublicationKbn = null;
			}
		}


		// 注目店舗を非対称に設定
		form.attentionShopFlg = MTypeConstants.AttentionShopFlg.NOT_TARGET;

		// 優先検索を非対象に設定
		form.searchPreferentialFlg = MTypeConstants.SearchPreferentialFlg.NO_TARGET;
	}

	/**
	 * 一覧へ戻る
	 * @return 一覧画面の検索メソッド
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="WEBDATA_INPUT_BACKLIST")
	public String backList() {
		// 一覧画面へ遷移
		return TransitionConstants.Webdata.REDIRECT_WEBDATA_LIST_SEARCHAGAIN;
	}

	/**
	 * PCプレビュー(一覧画面用)を表示します。
	 * @return
	 */
	@Execute(validator = true, validate = "validate", reset = "resetMultibox", input = TransitionConstants.JSP_PREVIEW_ERROR)
	public String previewPc() {
		return tempPreview(inputForm);
	}


	/**
	 * スマホプレビュー(一覧画面用)を表示します。
	 * @return スマホプレビュー表示メソッド
	 */
	@Execute(validator = true, validate = "validate", reset = "resetMultibox", input = TransitionConstants.JSP_PREVIEW_ERROR)
	public String previewSmartPhone() {
		return tempPreview(inputForm);
	}


	/**
	 * コピーから登録した際の処理。
	 * コピー元の原稿にIP電話が登録されていた場合、
	 * 電話番号のみコピー先に登録します。
	 *
	 * @return
	 *
	 * @author hara
	 */
	private void copyIpPhone(TWeb web) {

		// コピーからの遷移でなければ登録しない。
		if (!inputForm.copyFlg || StringUtils.isBlank(inputForm.sourceWebId)) {
			return;
		}

		IPPhoneDataCopyDto dto = new IPPhoneDataCopyDto();
		dto.webId = web.id;
		dto.sourceWebId = Integer.parseInt(inputForm.sourceWebId);
		dto.customerId = web.customerId;

		iPPhoneLogic.copyIpPhoneNumber(dto);

	}

}