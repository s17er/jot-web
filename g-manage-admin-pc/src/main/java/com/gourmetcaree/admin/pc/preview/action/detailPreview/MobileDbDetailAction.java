package com.gourmetcaree.admin.pc.preview.action.detailPreview;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.util.ArrayUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.preview.action.listPreview.PreviewBaseAction;
import com.gourmetcaree.admin.pc.preview.form.detailPreview.DetailForm;
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
import com.gourmetcaree.db.common.exception.WNoResultException;


/**
 * 求人情報詳細を表示するクラス
 * @author Takahiro Ando
 * @version 1.0
 */
public class MobileDbDetailAction extends PreviewBaseAction {

	/** フォーム */
	@ActionForm
	@Resource
	protected DetailForm detailForm;

	/** 名称変換サービス */
	@Resource
	protected ValueToNameConvertLogic valueToNameConvertLogic;

	@Resource
	private PreviewLogic previewLogic;

	/**
	 * 代理店の場合、データにアクセス可能かチェックする<br />
	 * アクセス権限が無い場合、権限エラーへ遷移
	 * @param String companyId
	 * @param form WEBデータフォーム
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
	@Execute(validator = false, urlPattern = "index/{id}", reset="resetForm", input=TransitionConstants.Preview.JSP_FMB01R01)
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
		return TransitionConstants.Preview.JSP_FMB01R01;
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
		if (GourmetCareeUtil.eqInt(sessionPreviewDto.areaCd, MAreaConstants.AreaCd.SENDAI_AREA )) {
			detailForm.imagesLocation = detailForm.getSendaiPath(DetailForm.SYUTOKEN_IMAGES);
		}
		detailForm.SITE_COLOR = getCommonProperty("gc.siteColor" + sessionPreviewDto.areaCd);
		detailForm.SITE_TEXT_COLOR = getCommonProperty("gc.siteTextColor" + sessionPreviewDto.areaCd);
		detailForm.SITE_EMOJI = getCommonProperty("gc.siteEmoji" + sessionPreviewDto.areaCd);
		detailForm.SITE_TEXT_LINK_COLOR = getCommonProperty("gc.siteTextLinkColor" + sessionPreviewDto.areaCd);

//		//号数情報を取得
//		if (StringUtil.isNotEmpty(sessionPreviewDto.volumeId)) {
//			MVolume entity = volumeService.findById(NumberUtils.toInt(sessionPreviewDto.volumeId));
//
//			try {
//				detailForm.postStartDatetime = entity.postStartDatetime;
//				detailForm.postEndDatetime = entity.postEndDatetime;
//			} catch (SNoResultException e) {
//				//volumeIdが未セットの場合は取得しない。
//			}
//		}

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

		//SEO用の情報を作成
		createSeoCondition();

	}

	/**
	 * SEO用の文言をラベルに変換。
	 */
	private void createSeoCondition() {

		Integer[] integerIndustryList = ArrayUtils.toObject(detailForm.getIndustryKbnList());

		List<String> list = new ArrayList<String>();
		list.add(detailForm.manuscriptName);

		if (!ArrayUtil.isEmpty(integerIndustryList)) {
			list.add(valueToNameConvertLogic.convertToIndustryName(ValueToNameConvertLogic.DELIMITER, integerIndustryList));
		}

		StringBuilder sb = new StringBuilder("");

		if (!list.isEmpty()) {
			for (int i=0; i<list.size(); i++) {
				if (i != 0) {
					sb.append(", ");
				}
				sb.append(list.get(i));
			}
		}

		detailForm.seoH1 = sb.toString().replaceAll(", ", " ");
		detailForm.seoDescription = sb.toString().replaceAll(", ", " ");
		detailForm.seoTitle = sb.toString().replaceAll(", ", " ");
		detailForm.seoKeywords = "," + sb.toString().replaceAll(", ", ",");
	}

	/**
	 * 応募ページの表示
	 * @return メッセージ画面
	 */
	@Execute(validator = false, urlPattern = "displayRecruit/{id}", reset="resetForm", input=TransitionConstants.Preview.JSP_FMB01R01)
	public String displayRecruit() {
		createDisplayData();

		return TransitionConstants.Preview.JSP_FMB01R01;
	}


	/**
	 * 詳細ページ２の表示
	 * @return メッセージ画面
	 */
	@Execute(validator = false, urlPattern = "displayDetail2/{id}", reset="resetForm", input=TransitionConstants.Preview.JSP_FMB01R01)
	public String displayDetail2() {
		createDisplayData();
		return TransitionConstants.Preview.JSP_FMB01R02;
	}

	/**
	 * 詳細ページ3の表示
	 * @return メッセージ画面
	 */
	@Execute(validator = false, urlPattern = "displayDetail3/{id}", reset="resetForm", input=TransitionConstants.Preview.JSP_FMB01R01)
	public String displayDetail3() {
		createDisplayData();
		return TransitionConstants.Preview.JSP_FMB01R03;
	}

	/**
	 * 詳細ページ4の表示
	 * @return メッセージ画面
	 */
	@Execute(validator = false, urlPattern = "displayDetail4/{id}", reset="resetForm", input=TransitionConstants.Preview.JSP_FMB01R01)
	public String displayDetail4() {
		createDisplayData();
		return TransitionConstants.Preview.JSP_FMB01R04;
	}

	/**
	 * 会社データの表示
	 * @return メッセージ画面
	 */
	@Execute(validator = false, urlPattern = "displayCompany/{id}", reset="resetForm", input=TransitionConstants.Preview.JSP_FMB01R01)
	public String displayCompany() {
		createDisplayData();
		return TransitionConstants.Preview.JSP_FMB01R05;
	}


	/**
	 * 地図表示
	 * @return 地図画面
	 */
	@Execute(validator = false, urlPattern = "displayMap/{id}", reset="resetForm", input=TransitionConstants.Preview.JSP_FMB01R01)
	public String displayMap() {
		createDisplayData();
		return TransitionConstants.Preview.JSP_FMB01R06;
	}

	/**
	 * 動画表示
	 * @return 動画画面
	 */
	@Execute(validator = false, urlPattern = "displayMovie/{id}", reset="resetForm", input=TransitionConstants.Preview.JSP_FMB01R01)
	public String displayMovie() {
		createDisplayData();
		return TransitionConstants.Preview.JSP_FMB01R04;
	}

	/**
	 * 地図の拡大表示
	 * @return 地図画面
	 */
	@Execute(validator = false, urlPattern = "zoomInMap/{id}/{zoom}/{inputFormKbn}", reset="resetForm", input=TransitionConstants.Preview.JSP_FMB01R01)
	public String zoomInMap() {

		// 入力値のチェック
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, detailForm.id, String.valueOf(detailForm.zoom));

		detailForm.zoomMap(1);
		createDisplayData();
		return TransitionConstants.Preview.JSP_FMB01R06;
	}

	/**
	 * 地図の縮小表示
	 * @return 地図画面
	 */
	@Execute(validator = false, urlPattern = "zoomOutMap/{id}/{zoom}/{inputFormKbn}", reset="resetForm", input=TransitionConstants.Preview.JSP_FMB01R01)
	public String zoomOutMap() {

		// 入力値のチェック
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, detailForm.id, detailForm.zoom);

		detailForm.zoomMap(-1);
		createDisplayData();
		return TransitionConstants.Preview.JSP_FMB01R06;
	}
}