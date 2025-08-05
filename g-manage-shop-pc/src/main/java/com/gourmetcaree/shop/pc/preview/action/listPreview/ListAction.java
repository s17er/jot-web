package com.gourmetcaree.shop.pc.preview.action.listPreview;

import static com.gourmetcaree.common.util.GourmetCareeUtil.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.dto.PreviewDto;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MAreaConstants;
import com.gourmetcaree.db.common.constants.MStatusConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants.SizeKbn;
import com.gourmetcaree.db.common.entity.MVolume;
import com.gourmetcaree.db.common.entity.TWeb;
import com.gourmetcaree.db.common.service.MaterialService;
import com.gourmetcaree.db.common.service.VolumeService;
import com.gourmetcaree.db.common.service.WebAttributeService;
import com.gourmetcaree.db.common.service.WebService;
import com.gourmetcaree.shop.pc.preview.form.listPreview.ListForm;
import com.gourmetcaree.shop.pc.preview.form.listPreview.ListForm.PreviewMethodKbn;
import com.gourmetcaree.shop.pc.sys.action.PcShopAction;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;


/**
 * プレビューを表示するクラス
 * @author Takahiro Ando
 * @version 1.0
 */
@ManageLoginRequired
public class ListAction extends PcShopAction {

	/** フォーム */
	@ActionForm
	@Resource
	protected ListForm listForm;

	/** Webdataサービス */
	@Resource
	private WebService webService;

	/** 号数サービス */
	@Resource
	private VolumeService volumeService;

	/** Webデータ属性サービス */
	@Resource
	private WebAttributeService webAttributeService;

	/** 素材サービス */
	@Resource
	private MaterialService materialService;

	/** 原稿一覧からのプレビュー時の閲覧可能年数 */
	private static final int RELEASE_PREVIEW_MINUS_YEAR = -1;

	/**
	 * リストでのプレビュー（PC）を表示します。
	 * @return
	 */
	@Execute(validator = false, urlPattern = "showPcPreview/{webId}", input = TransitionConstants.ErrorTransition.JSP_PREVIEW_ERROR)
	public String showPcPreview() {

		checkArgsNull(NO_BLANK_FLG_NG, listForm.webId);
		listForm.previewMethodKbn = PreviewMethodKbn.RELEASE_REVIEW;

		createData();
		return TransitionConstants.Preview.JSP_APO02A01;
	}


	/**
	 * リストでのプレビュー（モバイル）を表示します。
	 * @return
	 */
	@Execute(validator = false, urlPattern = "showMobilePreview/{webId}", input = TransitionConstants.ErrorTransition.JSP_PREVIEW_ERROR)
	public String showMobilePreview() {

		checkArgsNull(NO_BLANK_FLG_NG, listForm.webId);
		listForm.previewMethodKbn = PreviewMethodKbn.RELEASE_REVIEW;

		createData();
		return TransitionConstants.Preview.JSP_FMB01L01;
	}

	/**
	 * 表示用データ作成メインロジック
	 * @param targetPage
	 */
	private void createData() {

		try {
			TWeb entityTWeb = webService.findById(NumberUtils.toInt(listForm.webId));

			checkReleaseAccess(entityTWeb);

			PreviewDto dispDto = new PreviewDto();
			Beans.copy(entityTWeb, dispDto).execute();

			//周辺テーブルのデータを取得
			String[] treatmentKbnArray = GourmetCareeUtil.toIntToStringArray(webAttributeService.getWebAttributeValueArray(entityTWeb.id, MTypeConstants.TreatmentKbn.TYPE_CD));
			dispDto.treatmentKbnList =  Arrays.asList(treatmentKbnArray);

			String[] employPtnList = GourmetCareeUtil.toIntToStringArray(webAttributeService.getWebAttributeValueArray(entityTWeb.id, MTypeConstants.EmployPtnKbn.TYPE_CD));
			dispDto.employPtnList = Arrays.asList(employPtnList);

			if (SizeKbn.D == entityTWeb.sizeKbn || SizeKbn.E == entityTWeb.sizeKbn) {
				dispDto.materialSearchListExistFlg = materialService.isMaterialEntityExist(entityTWeb.id, NumberUtils.toInt(MTypeConstants.MaterialKbn.FREE));
			} else if (SizeKbn.A == entityTWeb.sizeKbn || SizeKbn.B == entityTWeb.sizeKbn || SizeKbn.C == entityTWeb.sizeKbn) {
				dispDto.materialSearchListExistFlg = materialService.isMaterialEntityExist(entityTWeb.id, NumberUtils.toInt(MTypeConstants.MaterialKbn.MAIN_1));
			} else {
				dispDto.materialSearchListExistFlg = false;
			}

			String[] otherConditionKbnArray = GourmetCareeUtil.toIntToStringArray(webAttributeService.getWebAttributeValueArray(entityTWeb.id, MTypeConstants.OtherConditionKbn.TYPE_CD));
			dispDto.otherConditionKbnList = Arrays.asList(otherConditionKbnArray);


			if (GourmetCareeUtil.eqInt(MTypeConstants.SizeKbn.C, dispDto.sizeKbn)) {
				//サイズDのみ複数画像を表示するので存在をチェック
				dispDto.materialCaptionAExistFlg = materialService.isMaterialEntityExist(entityTWeb.id, NumberUtils.toInt(MTypeConstants.MaterialKbn.PHOTO_A));
				dispDto.materialCaptionBExistFlg = materialService.isMaterialEntityExist(entityTWeb.id, NumberUtils.toInt(MTypeConstants.MaterialKbn.PHOTO_B));
			}

			List<PreviewDto> dataList = new ArrayList<PreviewDto>();
			dataList.add(dispDto);
			listForm.dataList = dataList;
			listForm.areaCd = entityTWeb.areaCd;
			listForm.frontHttpUrl = getCommonProperty("gc.front.httpUrl.area" + userDto.areaCd);

			if (GourmetCareeUtil.eqInt(listForm.areaCd, MAreaConstants.AreaCd.SENDAI_AREA)) {
				listForm.cssLocation = listForm.getSendaiPath(ListForm.SYUTOKEN_CSS);
				listForm.imgLocation = listForm.getSendaiPath(ListForm.SYUTOKEN_IMG);
				listForm.imagesLocation = listForm.getSendaiPath(ListForm.SYUTOKEN_IMAGES);
				listForm.incLocation = listForm.getSendaiPath(ListForm.SYUTOKEN_INC);
				listForm.helpLocation = listForm.getSendaiPath(ListForm.SYUTOKEN_HELP);
			}
			listForm.SITE_COLOR = getCommonProperty("gc.siteColor" + listForm.areaCd);
			listForm.SITE_TEXT_COLOR = getCommonProperty("gc.siteTextColor" + listForm.areaCd);
			listForm.SITE_EMOJI = getCommonProperty("gc.siteEmoji" + listForm.areaCd);
			listForm.SITE_TEXT_LINK_COLOR = getCommonProperty("gc.siteTextLinkColor" + listForm.areaCd);

		} catch (SNoResultException e) {
			throw new ActionMessagesException("errors.app.dataNotFound");
		}
	}

	/**
	 * 顧客側の原稿一覧からのプレビュー時の権限チェックを行います。
	 * @param entityTWeb
	 */
	private void checkReleaseAccess(TWeb entityTWeb) {

		if (!eqInt(userDto.customerId, entityTWeb.customerId)) {
			throw new ActionMessagesException("errors.app.noPermissionToShowPreview");
		} else if (!(eqInt(MStatusConstants.DBStatusCd.POST_FIXED, entityTWeb.status)
					|| eqInt(MStatusConstants.DBStatusCd.POST_END, entityTWeb.status))) {
			throw new ActionMessagesException("errors.app.noPermissionToShowPreview");
		}

		try {
			MVolume volumeEntity = volumeService.findById(entityTWeb.volumeId);
			Date todayDate = new Date();

			//掲載期間内か、掲載終了日から一年以内のデータ以外は閲覧不可
			if (volumeEntity.postEndDatetime.before(DateUtils.addYears(todayDate, RELEASE_PREVIEW_MINUS_YEAR))) {
				throw new ActionMessagesException("errors.app.noPermissionToShowPreview");
			} else if (volumeEntity.postStartDatetime.after(todayDate)) {
				throw new ActionMessagesException("errors.app.noPermissionToShowPreview");
			}

		} catch (SNoResultException e) {
			throw new ActionMessagesException("errors.app.dataNotFound");
		}
	}

}