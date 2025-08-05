package com.gourmetcaree.admin.pc.webdata.action.webdata;

import static com.gourmetcaree.common.constants.GourmetCareeConstants.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.pc.webdata.form.webdata.DetailForm;
import com.gourmetcaree.admin.pc.webdata.form.webdata.WebdataForm;
import com.gourmetcaree.admin.service.dto.WebdataControlDto;
import com.gourmetcaree.admin.service.property.WebdataProperty;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.entity.MArea;
import com.gourmetcaree.db.common.entity.TShopList;
import com.gourmetcaree.db.common.entity.TWeb;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.MaterialNoDataService;
import com.gourmetcaree.db.webdata.dto.webdata.MaterialExistsDto;
/**
*
* WEBデータ詳細を表示するクラス
* @author Makoto Otani
*
*/
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN,ManageAuthLevel.STAFF,ManageAuthLevel.AGENCY, ManageAuthLevel.OTHER, ManageAuthLevel.SALES})
public class DetailAction extends WebdataBaseAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(DetailAction.class);

	/** フォーム */
	@ActionForm
	@Resource
	protected DetailForm detailForm;

	/** 素材データなしサービス */
	@Resource
	MaterialNoDataService materialNoDataService;

	/** 素材有無Dto */
	public MaterialExistsDto materialExistsDto;

	/**
	 * 初期表示
	 * @return 確認画面
	 */
	@Execute(validator = false, urlPattern = "index/{id}", reset="resetForm", input=TransitionConstants.Webdata.JSP_APC01L01)
	@MethodAccess(accessCode="WEBDATA_DETAIL_INDEX")
	public String index() {

		checkId(detailForm, detailForm.id);

		return show();
	}

	/**
	 * 戻る
	 * @return 一覧画面の検索メソッド
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="WEBDATA_DETAIL_BACK")
	public String back() {
	HttpSession session = request.getSession(true);
	session.setAttribute("anchor", Integer.parseInt(detailForm.id));
		// 確認画面へ遷移
		return TransitionConstants.Webdata.REDIRECT_WEBDATA_LIST_SEARCHAGAIN;
	}

	/**
	 * 初期表示遷移用
	 * @return 登録画面のパス
	 */
	private String show() {

		try {
			// DBから取得したデータを表示用に変換
			createDisplayValue(getData());
			materialExistsDto = materialNoDataService.getExistsDto(Integer.parseInt(detailForm.id));

			log.debug("詳細画面データ取得");

		} catch (WNoResultException e) {

			// 画面表示をしない
			detailForm.setExistDataFlgNg();
			// データが取得できないエラーを表示
			dataNotFoundMessage();
		}

		// フラグを確認済み
		detailForm.setProcessFlgOk();
		createMailto();

		// 詳細画面のパス
		return TransitionConstants.Webdata.JSP_APC01R01;
	}

	/**
	 * WEBデータからデータを取得する
	 * @return WEBデータプロパティ
	 * @throws WNoResultException
	 */
	private WebdataProperty getData() throws WNoResultException {

		// ロジック受け渡し用プロパティに値をセット
		WebdataProperty property = new WebdataProperty();

		// idをエンティティにセット
		property.tWeb = new TWeb();
		property.tWeb.id = Integer.parseInt(detailForm.id);

		// データの取得
		webdataLogic.getWebdataDetailExcludesMatelial(property);

		// リストが空の場合はエラーメッセージを返す
		if (property == null || property.tWeb == null) {

			// 画面表示をしない
			detailForm.setExistDataFlgNg();

			//「該当するデータが見つかりませんでした。」
			dataNotFoundMessage();
		}

		// 取得したリストを返却
		return property;
	}

	/**
	 * 画面表示用に値をFormにセット<br />
	 * データが取得できない場合はエラー
	 * @param property WEBデータプロパティ
	 * @param form 詳細フォーム
	 */
	private void createDisplayValue(WebdataProperty property) {

		// 権限があるかチェック（なければエラー画面へ遷移）
		checkAgencyAccess(property.tWeb, detailForm);

		// データのセット
		createDisplayValue(property, detailForm);

		try {
			// 画面制御のためのDtoをセット
			WebdataControlDto controlDto = webdataLogic.setControlFlg(property);
			Beans.copy(controlDto, detailForm.controlDto).execute();

			getWebDataTag(Integer.parseInt(detailForm.id), detailForm);

			// 画面表示ステータスをセット
			detailForm.displayStatus = String.valueOf(controlDto.displayStatus);

			// 編集画面のパスをセット
			detailForm.editPath = GourmetCareeUtil.makePath(TransitionConstants.Webdata.ACTION_WEBDATA_EDIT_INDEX, String.valueOf(detailForm.id));

			// 応募テスト画面のパスをセット
			detailForm.appTestPath = GourmetCareeUtil.makePath(TransitionConstants.Webdata.ACTION_APPTEST_INPUT_INDEX, String.valueOf(detailForm.id));

			List<MArea> areaList;
			try {
				areaList = areaService.getMAreaList(NumberUtils.toInt(detailForm.areaCd));
			} catch (WNoResultException e1) {
				return;
			}

			// プレビューURLのセット
			StringBuilder listPreviewUrl = new StringBuilder();
			listPreviewUrl.append(getCommonProperty("gc.sslDomain"));
			listPreviewUrl.append(String.format(getCommonProperty("gc.preview.url.webdata.list"), areaList.get(0).linkName));
			listPreviewUrl.append(String.format("?ids=%s&key=%s&info=1", detailForm.id, DigestUtils.md5Hex(detailForm.id + GourmetCareeConstants.SYSTEM_HASH_SOLT)));
			detailForm.listPreviewUrl = listPreviewUrl.toString();

			property = null;

		} catch (WNoResultException e) {

			// 画面表示をしない
			detailForm.setExistDataFlgNg();

			// データが取得できないエラーを表示
			dataNotFoundMessage();
		}
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
	 * mailtoの文字列を作成します。
	 */
	private void createMailto() {

		// アクセスコードが未取得の場合はエラー
		if(StringUtil.isEmpty(detailForm.accessCd)) {
			callFraudulentProcessError(detailForm);
		}

		// メール用プロパティファイルの取得
		Properties sendMailText = ResourceUtil.getProperties("sendMail.properties");

		TShopList entity;
		try {
			entity = shopListService.selectArbeitPreviewEntity(
								Integer.parseInt(detailForm.customerDto.id),
								Integer.parseInt(detailForm.areaCd));

			detailForm.lightPreviewUrl = String.format(getCommonProperty("gc.arbeitPreviewList.url.format"), entity.customerId, entity.accessKey);
		} catch (WNoResultException | NumberFormatException e) {
			entity = null;
		}

		try {
			// 本文の生成
			StringBuffer body = new StringBuffer();

			// TODO
			body.append(encode(
			        String.format(
			                sendMailText.getProperty("gc.mailer.confMsg"),
                            StringUtils.replace(detailForm.manuscriptName, " ", "　")))) // 半角スペースを全角スペースに。
                    .append(MAILTO_RN_CD);			// タイトル

			// 一覧プレビュー
			body.append(encode(detailForm.listPreviewUrl)).append(MAILTO_RN_CD);	// PCプレビューパス

			body.append(MAILTO_RN_CD);
			body.append(encode(sendMailText.getProperty("gc.mailer.shopLoginMsg"))).append(MAILTO_RN_CD);		// ログインメッセージ
			body.append(encode(sendMailText.getProperty("gc.mailer.shopLoginPCPath"))).append(MAILTO_RN_CD);	// PCログインパス

			addShopListPreviewUrl(body, entity);

			// mailtoの件名
			detailForm.mailerSubject = encode(sendMailText.getProperty("gc.mailer.subject"));
			// mailtoの本文
			detailForm.mailerBody = body.toString();

			detailForm.previewMessage = previewMessage(entity);

		// エンコードに失敗した場合はエラー
		} catch (UnsupportedEncodingException e) {
			// 「メーラの起動に失敗しました。手動で行ってください。<br />(プレビュー用アドレス：{xxx})」
			throw new ActionMessagesException("errors.mailerFailed");
		}

	}


	/**
	 * 店舗一覧プレビューURLを追加します。
	 * @param body ボディ
	 * @param entity 店舗一覧
	 * @throws UnsupportedEncodingException エンコードに失敗した際にスロー
	 *
	 */
	private void addShopListPreviewUrl(final StringBuffer body, final TShopList entity) throws UnsupportedEncodingException {

		if(entity == null) {
			return;
		}

		body.append(MAILTO_RN_CD)
			.append(MAILTO_RN_CD);

		body.append(encode("【グルメdeバイト　ライト版求人詳細ページ】"))
			.append(MAILTO_RN_CD)
			.append(MAILTO_RN_CD);
		body.append(encode(String.format(getCommonProperty("gc.arbeitPreviewList.url.format"),
				entity.customerId,
				entity.accessKey)));

		body.append(MAILTO_RN_CD)
		.append(MAILTO_RN_CD);
	}

	/**
	 * X-SJISにエンコード
	 * @param value エンコードする値
	 * @return エンコードした値
	 * @throws UnsupportedEncodingException
	 */
	private String encode(String value) throws UnsupportedEncodingException {
		return URLEncoder.encode(value, getCommonProperty("gc.previewMail.encode"));
	}

	/**
	 * プレビューメッセージの取得
	 * @param entity 店舗一覧
	 * @return
	 */
	private String previewMessage(final TShopList entity) {
		// メール用プロパティファイルの取得
		Properties sendMailText = ResourceUtil.getProperties("sendMail.properties");

		StringBuffer body = new StringBuffer(0);
		body.append(String.format(sendMailText.getProperty("gc.mailer.confMsg"), StringUtils.replace(detailForm.manuscriptName, " ", "　")));
        body.append(RN_CD);

		// 一覧プレビュー
		body.append(detailForm.listPreviewUrl).append(RN_CD);

		body.append(RN_CD);
		body.append(sendMailText.getProperty("gc.mailer.shopLoginMsg")).append(RN_CD);		// ログインメッセージ
		body.append(sendMailText.getProperty("gc.mailer.shopLoginPCPath")).append(RN_CD);	// PCログインパス

		if(entity != null) {
			body.append(RN_CD);
			body.append("【グルメdeバイト　ライト版求人詳細ページ】").append(RN_CD);
			body.append(String.format(getCommonProperty("gc.arbeitPreviewList.url.format"), entity.customerId, entity.accessKey)).append(RN_CD);
		}

		return body.toString();
	}

	/**
	 * PCプレビュー(一覧画面用)を表示します。
	 * @return
	 */
	@Execute(validator = false)
	public String previewPc() {
		checkArgsNull(NO_BLANK_FLG_NG, detailForm.id);
//		storePreviewData(detailForm, WEBDATA_PREVIEW_DTO_KEY);

		String path =TransitionConstants.Preview.FORWARD_APO02.concat(detailForm.id);
		return path;
	}

	/**
	 * モバイルプレビュー(一覧画面用)を表示します。
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.JSP_PREVIEW_ERROR)
	public String previewMobile() {
		checkArgsNull(NO_BLANK_FLG_NG, detailForm.id);

		return TransitionConstants.Preview.FORWARD_AMO02.concat(detailForm.id);
	}

	/**
	 * スマホプレビュー(一覧画面用)を表示します。
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.JSP_PREVIEW_ERROR)
	public String previewSmartPhone() {
		checkArgsNull(NO_BLANK_FLG_NG, detailForm.id);

		return TransitionConstants.Preview.FORWARD_FSO01.concat(detailForm.id);
	}
}