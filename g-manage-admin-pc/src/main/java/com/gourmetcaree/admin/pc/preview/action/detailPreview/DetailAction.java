package com.gourmetcaree.admin.pc.preview.action.detailPreview;

import static com.gourmetcaree.common.util.GourmetCareeUtil.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.util.ArrayUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.preview.action.listPreview.PreviewBaseAction;
import com.gourmetcaree.admin.pc.preview.dto.detailPreview.RelationShopListDto;
import com.gourmetcaree.admin.pc.preview.form.detailPreview.DetailForm;
import com.gourmetcaree.admin.pc.preview.form.listPreview.ListForm;
import com.gourmetcaree.admin.pc.preview.form.listPreview.ListForm.ImgMethodKbn;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.arbeitsys.constants.MArbeitConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.dto.PreviewDto;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.AuthorizationException;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.exception.PageNotFoundException;
import com.gourmetcaree.common.logic.ValueToNameConvertLogic;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MAreaConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.MVolume;
import com.gourmetcaree.db.common.entity.TShopList;
import com.gourmetcaree.db.common.entity.VShopListMaterialNoData;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.ShopListMaterialNoDataService;
import com.gourmetcaree.db.common.service.ShopListRouteService;
import com.gourmetcaree.db.common.service.ShopListService;
import com.gourmetcaree.db.common.service.VolumeService;
import com.gourmetcaree.db.shopList.dto.shopList.RelationShopListRetDto;


/**
 * 求人情報詳細を表示するクラス
 * @author Takahiro Ando
 * @version 1.0
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN,ManageAuthLevel.STAFF,ManageAuthLevel.AGENCY, ManageAuthLevel.OTHER, ManageAuthLevel.SALES})
public class DetailAction extends PreviewBaseAction {

	/** フォーム */
	@ActionForm
	@Resource
	protected DetailForm detailForm;

	/** 号数サービス */
	@Resource
	private VolumeService volumeService;

	/** 名称変換サービス */
	@Resource
	private ValueToNameConvertLogic valueToNameConvertLogic;

	/** 店舗一覧サービス */
	@Resource
	private ShopListService shopListService;

	@Resource
	private ShopListMaterialNoDataService shopListMaterialNoDataService;

	@Resource
	private ShopListRouteService shopListRouteService;

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
	@Execute(validator = false, urlPattern = "index/{id}/{inputFormKbn}",  reset="resetForm")
	public String index() {

		createDisplayData();

		// サイズFの場合はメッセージが初期表示
//		if (detailForm.sizeKbn == SizeKbn.F) {
//			return TransitionConstants.Preview.JSP_APO01A02;
//		}

		return TransitionConstants.Preview.JSP_APO01A01;
	}

	/**
	 * 表示データを作成します。
	 * 表示データが取得できない場合はPageNotFoundExceptionをスローします。
	 */
	private void createDisplayData() {

		PreviewDto sessionPreviewDto = createPreviewDtoFromInputForm(detailForm.inputFormKbn);

		if (sessionPreviewDto == null) {
			throw new FraudulentProcessException("プレビュー表示用のデータのセッションからの取得に失敗しました。");
		}

		//ログインユーザがアクセス可能かチェックする。
		checkAgencyAccess(sessionPreviewDto.companyId);

		detailForm.imgMethodKbn = ImgMethodKbn.IMG_FROM_SESSION;

		detailForm.areaCd = sessionPreviewDto.areaCd;

		Beans.copy(sessionPreviewDto, detailForm).execute();
		detailForm.frontHttpUrl = getCommonProperty("gc.front.httpUrl.area" + sessionPreviewDto.areaCd);
		if (GourmetCareeUtil.eqInt(sessionPreviewDto.areaCd, MAreaConstants.AreaCd.SENDAI_AREA)) {
			detailForm.cssLocation = detailForm.getSendaiPath(ListForm.SYUTOKEN_CSS);
			detailForm.imgLocation = detailForm.getSendaiPath(ListForm.SYUTOKEN_IMG);
			detailForm.imagesLocation = detailForm.getSendaiPath(ListForm.SYUTOKEN_IMAGES);
			detailForm.incLocation = detailForm.getSendaiPath(ListForm.SYUTOKEN_INC);
			detailForm.helpLocation = detailForm.getSendaiPath(ListForm.SYUTOKEN_HELP);
		}

		//号数情報を取得
		if (StringUtil.isNotEmpty(sessionPreviewDto.volumeId)) {
			MVolume entity = volumeService.findById(NumberUtils.toInt(sessionPreviewDto.volumeId));

			try {
				detailForm.postStartDatetime = entity.postStartDatetime;
				detailForm.postEndDatetime = entity.postEndDatetime;
			} catch (SNoResultException e) {
				//volumeIdが未セットの場合は取得しない。
			}
		}

		if (StringUtils.isNotBlank(sessionPreviewDto.customerId)
				&& String.valueOf(MTypeConstants.ShopListDisplayKbn.ARI).equals(sessionPreviewDto.shopListDisplayKbn)) {
			try {
				detailForm.firstShopListId = shopListService.getShopListIdByCustomerId(NumberUtils.toInt(sessionPreviewDto.customerId));
				detailForm.shopListDisplayFlg = true;
			} catch (WNoResultException e) {
				detailForm.shopListDisplayFlg = false;
			}
		} else {
			detailForm.shopListDisplayFlg = false;
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
	 * 求人情報詳細の表示
	 * @return メッセージ画面
	 */
	@Execute(validator = false, urlPattern = "displayDetail/{id}/{inputFormKbn}", reset="resetForm", input=TransitionConstants.Preview.JSP_APO01A01)
	public String displayDetail() {
		createDisplayData();

		return  TransitionConstants.Preview.JSP_APO01A01;
	}

	/**
	 * メッセージ表示
	 * @return メッセージ画面
	 */
	@Execute(validator = false, urlPattern = "displayMessage/{id}", reset="resetForm", input=TransitionConstants.Preview.JSP_APO01A01)
	public String displayMessage() {

		createDisplayData();

		//サイズF以外では表示不可
//		if (detailForm.sizeKbn != SizeKbn.F) {
//			throw new PageNotFoundException();
//		}

		return  TransitionConstants.Preview.JSP_APO01A02;
	}

	/**
	 * 地図表示
	 * @return 地図画面
	 */
	@Execute(validator = false, urlPattern = "displayMap/{id}/{inputFormKbn}", reset="resetForm", input=TransitionConstants.Preview.JSP_APO01A01)
	public String displayMap() {
		createDisplayData();
		return  TransitionConstants.Preview.JSP_APO01A03;
	}

	/**
	 * 店舗一覧の表示
	 * @return
	 */
	@Execute(validator = false, urlPattern = "displayShopList/{shopListId}/{id}/{inputFormKbn}", reset="resetForm", input=TransitionConstants.Preview.JSP_APO01A05)
	public String displayShopList() {
		createDisplayData();
		createShopListData();

		return TransitionConstants.Preview.JSP_APO01A05;
	}

	/**
	 * 動画表示
	 * @return 動画画面
	 */
	@Execute(validator = false, urlPattern = "displayMovie/{id}/{inputFormKbn}", reset="resetForm", input=TransitionConstants.Preview.JSP_APO01A01)
	public String displayMovie() {
		createDisplayData();

		if (StringUtils.isBlank(detailForm.movieUrl)) {
			return  TransitionConstants.Preview.JSP_APO01A01;
		}
		return  TransitionConstants.Preview.JSP_APO01A04;
	}

	/**
	 * 店舗一覧の情報を作成します。
	 */
	private void createShopListData() {
		try {
			TShopList entity = shopListService.getResisteredData(Integer.parseInt(detailForm.shopListId));

			Beans.copy(entity, detailForm).excludes("id", "industryKbn1", "industryKbn2", "holiday").execute();
			detailForm.shopHoliday = entity.holiday;
			detailForm.shopListIndustryKbn1 = entity.industryKbn1;
			if (entity.industryKbn2 != null) {
				detailForm.shopListIndustryKbn2 = entity.industryKbn2;
			}
			List<RelationShopListDto> dtoList = new ArrayList<RelationShopListDto>();
			try {
				List<RelationShopListRetDto> retDtoList = shopListService.getRelationShopListByCustomerId(entity.customerId);
				for (RelationShopListRetDto retDto : retDtoList) {
					RelationShopListDto dto = new RelationShopListDto();
					Beans.copy(retDto, dto).execute();
					dto.detailPath = GourmetCareeUtil.makePath("/detailPreview/detail/", "displayShopList", String.valueOf(retDto.id), detailForm.id, detailForm.inputFormKbn);
					dto.shopListImagePath = createShopListImagePath(retDto.id);
					dtoList.add(dto);
				}
				createShopListMaterialData(NumberUtils.toInt(detailForm.shopListId));
				// 路線取得
				detailForm.routeList = shopListRouteService.findByShopListId(Integer.parseInt(detailForm.shopListId));
			} catch (WNoResultException e) {
				// なにもしない
			}
			detailForm.relationShopList = dtoList;

			// アルバイト募集
			if (GourmetCareeUtil.eqInt(MTypeConstants.JobOfferFlg.ARI, entity.jobOfferFlg)) {
				detailForm.arbeitPreviewPath =  String.format(
						getCommonProperty("gc.arbeitPreview.url.format"),
						MArbeitConstants.ArbeitSite.getArbeitSiteConst(entity.arbeitTodouhukenId),
						entity.id,
						entity.accessKey);
			}

		} catch (NumberFormatException e) {
			throw new FraudulentProcessException();
		} catch (WNoResultException e) {
			throw new PageNotFoundException();
		}
	}

	/**
	 * 店舗一覧イメージパスの作成
	 * @param id
	 * @return
	 */
	private String createShopListImagePath(int id) {
		try {
			List<VShopListMaterialNoData> entityList = shopListMaterialNoDataService.getMaterialList(id);
			for (VShopListMaterialNoData entity : entityList) {
				if (GourmetCareeUtil.eqInt(Integer.parseInt(MTypeConstants.ShopListMaterialKbn.MAIN_1), entity.materialKbn)) {
					return GourmetCareeUtil.makePath("/util/imageUtility/",
														"displayShopListImageCache",
														String.valueOf(id),
														MTypeConstants.ShopListMaterialKbn.MAIN_1,
														GourmetCareeUtil.createUniqueKey(entity.insertDatetime));
				}
			}
		} catch (WNoResultException e) {
			return "";
		}
		return "";
	}

	/**
	 * 店舗一覧の画像データが存在するかどうかをMapに保持します。
	 */
	private void createShopListMaterialData(int shopListId) {
		Map<String, String> imageUniqueKeyMap = new HashMap<String, String>();
		Map<String, Boolean> imageCheckMap = new HashMap<String, Boolean>();
		try {
			List<VShopListMaterialNoData> retList = shopListMaterialNoDataService.getMaterialList(shopListId);

			for (VShopListMaterialNoData entity : retList) {
					imageCheckMap.put(Integer.toString(entity.materialKbn), true);
					imageUniqueKeyMap.put(Integer.toString(entity.materialKbn), createUniqueKey(entity.insertDatetime));
			}

		} catch (WNoResultException e) {
			//素材データがない場合は未処理とする。
		}

		detailForm.shopListImageCheckMap = imageCheckMap;
		detailForm.shopListUniqueKeyMap = imageUniqueKeyMap;
	}


}