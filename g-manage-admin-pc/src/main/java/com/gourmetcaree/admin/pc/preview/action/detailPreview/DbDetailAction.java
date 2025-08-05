package com.gourmetcaree.admin.pc.preview.action.detailPreview;

import static com.gourmetcaree.common.util.GourmetCareeUtil.*;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.preview.dto.detailPreview.RelationShopListDto;
import com.gourmetcaree.admin.pc.preview.form.detailPreview.DetailForm;
import com.gourmetcaree.admin.pc.preview.form.listPreview.ListForm.ImgMethodKbn;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.logic.PreviewLogic;
import com.gourmetcaree.arbeitsys.constants.MArbeitConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.AuthorizationException;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.exception.PageNotFoundException;
import com.gourmetcaree.common.logic.ValueToNameConvertLogic;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MAreaConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants.MaterialKbn;
import com.gourmetcaree.db.common.entity.MVolume;
import com.gourmetcaree.db.common.entity.TShopList;
import com.gourmetcaree.db.common.entity.TWeb;
import com.gourmetcaree.db.common.entity.VMaterialNoData;
import com.gourmetcaree.db.common.entity.VShopListMaterialNoData;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.MaterialNoDataService;
import com.gourmetcaree.db.common.service.ShopListMaterialNoDataService;
import com.gourmetcaree.db.common.service.ShopListRouteService;
import com.gourmetcaree.db.common.service.ShopListService;
import com.gourmetcaree.db.common.service.VolumeService;
import com.gourmetcaree.db.common.service.WebAttributeService;
import com.gourmetcaree.db.common.service.WebService;
import com.gourmetcaree.db.shopList.dto.shopList.RelationShopListRetDto;


/**
 * 求人情報詳細をDBから表示するクラス
 * @author Takahiro Ando
 * @version 1.0
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN,ManageAuthLevel.STAFF,ManageAuthLevel.AGENCY, ManageAuthLevel.OTHER, ManageAuthLevel.SALES})
public class DbDetailAction extends PcAdminAction {

	/** フォーム */
	@ActionForm
	@Resource
	protected DetailForm detailForm;

	/** プレビューロジック */
	@Resource
	private PreviewLogic previewLogic;

	/** 号数サービス */
	@Resource
	private VolumeService volumeService;

	/** Webデータサービス */
	@Resource
	private WebService webService;

	/** Webデータ属性サービス */
	@Resource
	private WebAttributeService webAttributeService;

	/** 本体データのない素材サービス */
	@Resource
	private MaterialNoDataService materialNoDataService;

	/** 名称変換サービス */
	@Resource
	private ValueToNameConvertLogic valueToNameConvertLogic;

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
	@Execute(validator = false, urlPattern = "index/{id}",  reset="resetForm")
	public String index() {

		createDisplayData();

		// サイズFの場合はメッセージが初期表示
//		if (detailForm.sizeKbn == SizeKbn.F) {
//			return TransitionConstants.Preview.JSP_APO01A02;
//		}

		return TransitionConstants.Preview.JSP_APO01A01;
	}


	/**
	 * WEBデータのチェックステータスを変更します。
	 */
	@Execute(validator = false)
	public String changeCheckStatus() {
		previewLogic.changeCheckStatus(detailForm);
		return String.format("/detailPreview/dbDetail/index/%s?redirect=true", detailForm.id);
	}

	/**
	 * 表示データを作成します。
	 * 表示データが取得できない場合はPageNotFoundExceptionをスローします。
	 */
	private void createDisplayData() {

		checkArgsNull(NO_BLANK_FLG_NG, detailForm.id);

		detailForm.imgMethodKbn = ImgMethodKbn.IMG_FROM_DB;

		try {
			TWeb entityTWeb = webService.findById(NumberUtils.toInt(detailForm.id));

			checkAgencyAccess(entityTWeb.companyId);

			Beans.copy(entityTWeb, detailForm).execute();
			previewLogic.convertWebdataCheckStatusChangeable(detailForm);
			detailForm.frontHttpUrl = getCommonProperty("gc.front.httpUrl.area" + detailForm.areaCd);
			if (GourmetCareeUtil.eqInt(entityTWeb.areaCd, MAreaConstants.AreaCd.SENDAI_AREA )) {
				detailForm.cssLocation = detailForm.getSendaiPath(DetailForm.SYUTOKEN_CSS);
				detailForm.imgLocation = detailForm.getSendaiPath(DetailForm.SYUTOKEN_IMG);
				detailForm.imagesLocation = detailForm.getSendaiPath(DetailForm.SYUTOKEN_IMAGES);
				detailForm.incLocation = detailForm.getSendaiPath(DetailForm.SYUTOKEN_INC);
				detailForm.helpLocation = detailForm.getSendaiPath(DetailForm.SYUTOKEN_HELP);
			}
			//周辺テーブルのデータを取得
			String[] treatmentKbnArray = GourmetCareeUtil.toIntToStringArray(webAttributeService.getWebAttributeValueArray(entityTWeb.id, MTypeConstants.TreatmentKbn.TYPE_CD));
			detailForm.treatmentKbnList =  Arrays.asList(treatmentKbnArray);

			String[] employPtnList = GourmetCareeUtil.toIntToStringArray(webAttributeService.getWebAttributeValueArray(entityTWeb.id, MTypeConstants.EmployPtnKbn.TYPE_CD));
			String[] otherConditionKbnList = GourmetCareeUtil.toIntToStringArray(webAttributeService.getWebAttributeValueArray(entityTWeb.id, MTypeConstants.OtherConditionKbn.TYPE_CD));
			detailForm.employPtnList = Arrays.asList(employPtnList);
			detailForm.otherConditionKbnList = Arrays.asList(otherConditionKbnList);

			detailForm.imageUniqueKeyMap = materialNoDataService.getImageUniqueKeyMap(entityTWeb.id);

			createMaterialData(entityTWeb.id);

			//号数情報を取得
			if (entityTWeb.volumeId != null) {
				try {
					MVolume volumeEntity = volumeService.findById(entityTWeb.volumeId);
						detailForm.postStartDatetime = volumeEntity.postStartDatetime;
						detailForm.postEndDatetime = volumeEntity.postEndDatetime;
				} catch (SNoResultException e) {
					//volumeIdが未セットの場合は取得しない。
				}
			}

			if (StringUtils.isNotBlank(detailForm.id)) {
				try {
					if (entityTWeb.customerId != null
							&& GourmetCareeUtil.eqInt(MTypeConstants.ShopListDisplayKbn.ARI, entityTWeb.shopListDisplayKbn)) {
						detailForm.firstShopListId = shopListService.getShopListIdByCustomerId(entityTWeb.customerId);
						detailForm.shopListDisplayFlg = true;
					} else {
						detailForm.shopListDisplayFlg = false;
					}
				} catch (WNoResultException e) {
					detailForm.shopListDisplayFlg = false;
				}
			} else {
				detailForm.shopListDisplayFlg = false;
			}

			//SEO用の情報を作成
			createSeoCondition();

		} catch (NumberFormatException e) {
			throw new PageNotFoundException();
		} catch (SNoResultException e) {
			throw new PageNotFoundException();
		}
	}

	/**
	 * 画像データが存在するかどうかをMapに保持します。
	 */
	private void createMaterialData(int webId) {

		Map<String, String> imageUniqueKeyMap = new HashMap<String, String>();
		Map<String, Boolean> imageCheckMap = new HashMap<String, Boolean>();

		try {
			List<VMaterialNoData> retList = materialNoDataService.getMaterialList(webId);

			for (VMaterialNoData entity : retList) {
				//動画かサムネイル以外の画像の場合に情報を保持する
				if (MaterialKbn.MOVIE_WM.equals(Integer.toString(entity.materialKbn))) {
					detailForm.wmMovieName = entity.fileName;
				} else if (MaterialKbn.MOVIE_QT.equals(Integer.toString(entity.materialKbn))) {
					detailForm.qtMovieName = entity.fileName;
				} else if (!MaterialKbn.isThumbKbn(Integer.toString(entity.materialKbn))) {
					imageCheckMap.put(Integer.toString(entity.materialKbn), true);
					imageUniqueKeyMap.put(Integer.toString(entity.materialKbn), createUniqueKey(entity.insertDatetime));
				}
			}

		} catch (WNoResultException e) {
			//素材データがない場合は未処理とする。
		}

		detailForm.imageUniqueKeyMap = imageUniqueKeyMap;
		detailForm.imageCheckMap = imageCheckMap;
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
	@Execute(validator = false, urlPattern = "displayDetail/{id}", reset="resetForm", input=TransitionConstants.Preview.JSP_APO01A01)
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
	@Execute(validator = false, urlPattern = "displayMap/{id}", reset="resetForm", input=TransitionConstants.Preview.JSP_APO01A01)
	public String displayMap() {
		createDisplayData();
		return  TransitionConstants.Preview.JSP_APO01A03;
	}

	/**
	 * 動画表示
	 * @return 動画画面
	 */
	@Execute(validator = false, urlPattern = "displayMovie/{id}", reset="resetForm", input=TransitionConstants.Preview.JSP_APO01A01)
	public String displayMovie() {
		createDisplayData();
		return  TransitionConstants.Preview.JSP_APO01A04;
	}

	/**
	 * 店舗一覧の表示
	 * @return
	 */
	@Execute(validator = false, urlPattern = "displayShopList/{shopListId}/{id}", reset="resetForm", input=TransitionConstants.Preview.JSP_APO01A05)
	public String displayShopList() {
		createDisplayData();
		createShopListData();

		return TransitionConstants.Preview.JSP_APO01A05;
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
					dto.detailPath = GourmetCareeUtil.makePath("/detailPreview/dbDetail/", "displayShopList", String.valueOf(retDto.id), detailForm.id);
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