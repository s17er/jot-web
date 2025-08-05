package com.gourmetcaree.admin.pc.preview.action.detailPreview;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.util.StringUtil;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.preview.action.listPreview.PreviewBaseAction;
import com.gourmetcaree.admin.pc.preview.form.detailPreview.DetailForm;
import com.gourmetcaree.admin.pc.preview.form.listPreview.ListForm;
import com.gourmetcaree.admin.pc.preview.form.listPreview.ListForm.ImgMethodKbn;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.logic.PreviewLogic;
import com.gourmetcaree.common.dto.PreviewDto;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.AuthorizationException;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.logic.ValueToNameConvertLogic;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MAreaConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.ShopListService;


/**
 * 求人情報詳細のスマホプレビューを表示するクラス
 * @author Makoto Otani
 * @version 1.0
 */
public class SmartPhoneDbDetailAction extends PreviewBaseAction {

	/** フォーム */
	@ActionForm
	@Resource
	protected DetailForm detailForm;

	/** 名称変換サービス */
	@Resource
	protected ValueToNameConvertLogic valueToNameConvertLogic;

	/** プレビューロジック */
	@Resource
	private PreviewLogic previewLogic;

	/** 店舗一覧サービス */
	@Resource
	private ShopListService shopListService;

	/**
	 * 代理店の場合、データにアクセス可能かチェックする<br />
	 * アクセス権限が無い場合、権限エラーへ遷移
	 * @param companyId 会社ID
	 */
	private void checkAgencyAccess(int companyId) {

		// 代理店の場合は自分自身の会社のみ閲覧可とする。
		if (ManageAuthLevel.AGENCY.value().equals(userDto.authLevel) &&
				!Integer.toString(companyId).equals(userDto.companyId)) {

			throw new AuthorizationException("アクセスする権限がありません");
		}
	}

	/**
	 * 初期表示
	 * @return 求人情報詳細画面
	 */
	@Execute(validator = false, urlPattern = "index/{id}", reset="resetForm", input=TransitionConstants.Preview.JSP_FSO01R01)
	public String index() {
		return show();
	}

	/**
	 * 初期表示遷移用
	 * @return 詳細画面のパス
	 */
	private String show() {

		createDisplayData();

		// 詳細画面のパス
		return TransitionConstants.Preview.JSP_FSO01R01;
	}

	/**
	 * 詳細メッセージを表示
	 * @return 詳細画面メッセージのパス
	 */
	@Execute(validator = false, urlPattern = "displayDetailMsg/{id}", reset="resetForm", input=TransitionConstants.Preview.JSP_FSO01R01)
	public String displayDetailMsg() {
		createDisplayData();

		if (detailForm.isShopMessageExist()){
			return TransitionConstants.Preview.JSP_FSO01R02;
		}
		return TransitionConstants.Preview.JSP_FSO01R01;
	}

	/**
	 * 表示データを作成します。
	 * 表示データが取得できない場合はPageNotFoundExceptionをスローします。
	 */
	private void createDisplayData() {

		PreviewDto sessionPreviewDto;
		try {
			sessionPreviewDto = previewLogic.createPreviewDtoByWebId(NumberUtils.toInt(detailForm.id));
		} catch (WNoResultException e1) {
			throw new FraudulentProcessException("プレビュー表示用のデータのセッションからの取得に失敗しました。");
		}

		previewLogic.createImageCheckMap(sessionPreviewDto);

		//ログインユーザがアクセス可能かチェックする。
		checkAgencyAccess(sessionPreviewDto.companyId);

		detailForm.imgMethodKbn = ImgMethodKbn.IMG_FROM_DB;

		Beans.copy(sessionPreviewDto, detailForm).execute();
		detailForm.frontHttpUrl = getCommonProperty("gc.front.httpUrl.area" + sessionPreviewDto.areaCd);
		if (GourmetCareeUtil.eqInt(sessionPreviewDto.areaCd, MAreaConstants.AreaCd.SENDAI_AREA)) {
			detailForm.cssLocation = detailForm.getSendaiPath(ListForm.SYUTOKEN_CSS);
			detailForm.imgLocation = detailForm.getSendaiPath(ListForm.SYUTOKEN_IMG);
			detailForm.imagesLocation = detailForm.getSendaiPath(ListForm.SYUTOKEN_IMAGES);
			detailForm.incLocation = detailForm.getSendaiPath(ListForm.SYUTOKEN_INC);
			detailForm.helpLocation = detailForm.getSendaiPath(ListForm.SYUTOKEN_HELP);
		}
		detailForm.SITE_COLOR = getCommonProperty("gc.siteColor" + sessionPreviewDto.areaCd);
		detailForm.SITE_TEXT_COLOR = getCommonProperty("gc.siteTextColor" + sessionPreviewDto.areaCd);
		detailForm.SITE_EMOJI = getCommonProperty("gc.siteEmoji" + sessionPreviewDto.areaCd);
		detailForm.SITE_TEXT_LINK_COLOR = getCommonProperty("gc.siteTextLinkColor" + sessionPreviewDto.areaCd);

		if (StringUtils.isNotBlank(sessionPreviewDto.customerId)
				&& GourmetCareeUtil.eqInt(MTypeConstants.ShopListDisplayKbn.ARI, sessionPreviewDto.shopListDisplayKbn)) {
			detailForm.shopListDisplayFlg = shopListService.existsShopListByCustomerId(NumberUtils.toInt(sessionPreviewDto.customerId));
		} else {
			detailForm.shopListDisplayFlg = false;
		}
		// 地図を表示するためエンコーディング
		try {
			if (StringUtil.isNotBlank(detailForm.mapAddress)) {
				detailForm.mapAddress = URLEncoder.encode(detailForm.mapAddress, "UTF-8");
			} else {
				detailForm.mapAddress = "";
			}

		} catch (UnsupportedEncodingException e) {
			detailForm.mapAddress = "";
		}

	}


	/**
	 * JSP側で毎回生成せずにここでやる
	 * @return メッセージ詳細ページパス
	 */
	public String getMessageDetailPath() {
		return "/detailPreview/smartPhoneDbDetail/displayDetailMsg/" + detailForm.id;
	}

	/**
	 * JSP側で毎回生成せずにここでやる
	 * @return 求人詳細ページパス
	 */
	public String getJobDetailPath() {
		return "/detailPreview/smartPhoneDbDetail/index/" + detailForm.id;
	}
}