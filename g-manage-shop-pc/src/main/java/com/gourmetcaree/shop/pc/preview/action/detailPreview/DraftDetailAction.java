package com.gourmetcaree.shop.pc.preview.action.detailPreview;

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
import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.util.ArrayUtil;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.arbeitsys.constants.MArbeitConstants;
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
import com.gourmetcaree.shop.logic.logic.WebdataLogic;
import com.gourmetcaree.shop.pc.preview.dto.detailPreview.RelationShopListDto;
import com.gourmetcaree.shop.pc.preview.form.detailPreview.DetailForm;
import com.gourmetcaree.shop.pc.preview.form.listPreview.ListForm.PreviewMethodKbn;
import com.gourmetcaree.shop.pc.sys.action.PcShopAction;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;


/**
 * 求人情報詳細を表示するクラス
 * @author Takahiro Ando
 * @version 1.0
 */
public class DraftDetailAction extends PcShopAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(DraftDetailAction.class);

	/** ログオブジェクト（なりすまし） */
	private static final Logger sysMasqueradeLog = Logger.getLogger("sysMasqueradeLog");

	/** フォーム */
	@ActionForm
	@Resource
	protected DetailForm detailForm;

	/** 名称変換サービス */
	@Resource
	private ValueToNameConvertLogic valueToNameConvertLogic;

	/** Webデータサービス */
	@Resource
	private WebService webService;

	/** Webデータ属性サービス */
	@Resource
	private WebAttributeService webAttributeService;

	/** Webデータロジック */
	@Resource
	private WebdataLogic webdataLogic;

	/** データ本体のない素材Viewサービス */
	@Resource
	protected MaterialNoDataService materialNoDataService;

	/** 号数サービス */
	@Resource
	private VolumeService volumeService;

	@Resource
	private ShopListService shopListService;

	@Resource
	private ShopListMaterialNoDataService shopListMaterialNoDataService;

	@Resource
	private ShopListRouteService shopListRouteService;

//	/** メールからのプレビュー時の閲覧可能年数 */
//	private static final int DRAFT_PREVIEW_MINUS_MONTH = -1;

	/**
	 * 初期表示
	 * @return 求人情報詳細画面
	 */
	@Execute(validator = false, urlPattern = "index/{id}/{accessCd}/{areaCd}", reset="resetForm", input=TransitionConstants.Preview.JSP_APO01A01)
	public String index() {
		detailForm.siteAreaCd = detailForm.areaCd;
		createDisplayData();


		return TransitionConstants.Preview.JSP_APO01A01;
	}

	/**
	 * 表示データを作成します。
	 * 表示データが取得できない場合はPageNotFoundExceptionをスローします。
	 */
	private void createDisplayData() {

		checkArgsNull(NO_BLANK_FLG_NG, detailForm.id, detailForm.accessCd, detailForm.areaCd);
		detailForm.previewMethodKbn = PreviewMethodKbn.DRAFT_PREVIEW;

		try {
			TWeb entityTWeb = webService.findById(NumberUtils.toInt(detailForm.id));

			checkReleaseAccess(entityTWeb);

			Beans.copy(entityTWeb, detailForm).execute();
			detailForm.frontHttpUrl = getCommonProperty("gc.front.httpUrl.area" + detailForm.siteAreaCd);

			//周辺テーブルのデータを取得
			String[] treatmentKbnArray = GourmetCareeUtil.toIntToStringArray(webAttributeService.getWebAttributeValueArray(entityTWeb.id, MTypeConstants.TreatmentKbn.TYPE_CD));
			detailForm.treatmentKbnList =  Arrays.asList(treatmentKbnArray);

			String[] employPtnList = GourmetCareeUtil.toIntToStringArray(webAttributeService.getWebAttributeValueArray(entityTWeb.id, MTypeConstants.EmployPtnKbn.TYPE_CD));
			detailForm.employPtnList = Arrays.asList(employPtnList);

			String[] otherConditionKbnList = GourmetCareeUtil.toIntToStringArray(webAttributeService.getWebAttributeValueArray(entityTWeb.id, MTypeConstants.OtherConditionKbn.TYPE_CD));
			detailForm.otherConditionKbnList = Arrays.asList(otherConditionKbnList);

			detailForm.imageUniqueKeyMap = webdataLogic.getImageUniqueKeyMap(entityTWeb.id);

			createMaterialData(entityTWeb.id);

			if (entityTWeb.volumeId != null) {
				try {
					MVolume volumeEntity = volumeService.findById(entityTWeb.volumeId);
					detailForm.postStartDatetime = volumeEntity.postStartDatetime;
					detailForm.postEndDatetime = volumeEntity.postEndDatetime;
				} catch (SNoResultException e) {
					//未処理
				}
			}

			if (GourmetCareeUtil.eqInt(entityTWeb.areaCd, MAreaConstants.AreaCd.SENDAI_AREA)) {
				detailForm.cssLocation = detailForm.getSendaiPath(DetailForm.SYUTOKEN_CSS);
				detailForm.imgLocation = detailForm.getSendaiPath(DetailForm.SYUTOKEN_IMG);
				detailForm.imagesLocation = detailForm.getSendaiPath(DetailForm.SYUTOKEN_IMAGES);
				detailForm.incLocation = detailForm.getSendaiPath(DetailForm.SYUTOKEN_INC);
				detailForm.helpLocation = detailForm.getSendaiPath(DetailForm.SYUTOKEN_HELP);
			}

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
	 * 顧客側の原稿一覧からのプレビュー時の権限チェックを行います。
	 * @param entityTWeb
	 */
	private void checkReleaseAccess(TWeb entityTWeb) {

		/** アクセスコードをチェック */
		if (!detailForm.accessCd.equals(entityTWeb.accessCd)) {
			log.warn("メールからのプレビュー時にアクセスコードが一致しませんでした。エンティティのアクセスコード：" + entityTWeb.accessCd);
			if (userDto.isMasqueradeFlg()) {
				sysMasqueradeLog.warn(String.format("メールからのプレビュー時にアクセスコードが一致しませんでした。営業ID：%s, 顧客ID：%s, エンティティのアクセスコード：%s", userDto.masqueradeUserId, userDto.customerId, entityTWeb.accessCd));
			}
			detailForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.noPermissionToShowPreview");
		}

		//エリアはアクセスされたメソッドで見分けて判定
		if (!eqInt(NumberUtils.toInt(detailForm.areaCd), entityTWeb.areaCd)) {
			log.warn("メールからのプレビュー時にエリアが一致しませんでした。エンティティのエリアコード：" + entityTWeb.areaCd);
			if (userDto.isMasqueradeFlg()) {
				sysMasqueradeLog.warn(String.format("メールからのプレビュー時にエリアが一致しませんでした。営業ID：%s, 顧客ID：%s, エンティティのエリアコード：%s", userDto.masqueradeUserId, userDto.customerId, entityTWeb.areaCd));
			}
			detailForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.noPermissionToShowPreview");
		}

		// ドラフトのプレビュー期間制限はなし。
//		Date todayDate = new Date();

//		if (entityTWeb.updateDatetime != null) {
//			if (entityTWeb.updateDatetime.before(DateUtils.addMonths(todayDate, DRAFT_PREVIEW_MINUS_MONTH))) {
//				log.warn("メールからのプレビュー時に更新日時が範囲外でした。更新日時：" + entityTWeb.updateDatetime);
//				detailForm.setExistDataFlgNg();
//				throw new ActionMessagesException("errors.app.noPermissionToShowPreview");
//			}
//		} else if (entityTWeb.insertDatetime != null) {
//			if (entityTWeb.insertDatetime.before(DateUtils.addMonths(todayDate, DRAFT_PREVIEW_MINUS_MONTH))) {
//				log.warn("メールからのプレビュー時に登録日時が範囲外でした。登録日時：" + entityTWeb.insertDatetime);
//				detailForm.setExistDataFlgNg();
//				throw new ActionMessagesException("errors.app.noPermissionToShowPreview");
//			}
//		}
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
	@Execute(validator = false, urlPattern = "displayDetail/{id}/{accessCd}/{areaCd}", reset="resetForm", input=TransitionConstants.Preview.JSP_APO01A01)
	public String displayDetail() {
		detailForm.siteAreaCd = detailForm.areaCd;
		createDisplayData();

		return  TransitionConstants.Preview.JSP_APO01A01;
	}

	/**
	 * メッセージ表示
	 * @return メッセージ画面
	 */
	@Execute(validator = false, urlPattern = "displayMessage/{id}/{accessCd}/{areaCd}", reset="resetForm", input=TransitionConstants.Preview.JSP_APO01A01)
	public String displayMessage() {
		detailForm.siteAreaCd = detailForm.areaCd;
		createDisplayData();

		return  TransitionConstants.Preview.JSP_APO01A02;
	}

	/**
	 * 地図表示
	 * @return 地図画面
	 */
	@Execute(validator = false, urlPattern = "displayMap/{id}/{accessCd}/{areaCd}", reset="resetForm", input=TransitionConstants.Preview.JSP_APO01A01)
	public String displayMap() {
		detailForm.siteAreaCd = detailForm.areaCd;
		createDisplayData();
		return  TransitionConstants.Preview.JSP_APO01A03;
	}

	/**
	 * 動画表示
	 * @return 動画画面
	 */
	@Execute(validator = false, urlPattern = "displayMovie/{id}/{accessCd}/{areaCd}", reset="resetForm", input=TransitionConstants.Preview.JSP_APO01A01)
	public String displayMovie() {
		detailForm.siteAreaCd = detailForm.areaCd;
		createDisplayData();

		if (StringUtils.isBlank(detailForm.movieUrl)) {
			return TransitionConstants.Preview.JSP_APO01A01;
		}
		return  TransitionConstants.Preview.JSP_APO01A04;
	}

	/**
	 * 店舗一覧の表示
	 * @return
	 */
	@Execute(validator = false, urlPattern = "displayShopList/{accessCd}/{shopListId}/{id}/{areaCd}", reset="resetForm", input=TransitionConstants.Preview.JSP_APO01A05)
	public String displayShopList() {
		detailForm.siteAreaCd = detailForm.areaCd;
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
					dto.detailPath = GourmetCareeUtil.makePath("/detailPreview/draftDetail/", "displayShopList", detailForm.accessCd, String.valueOf(retDto.id), detailForm.id, detailForm.siteAreaCd);
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


			if (GourmetCareeUtil.eqInt(MTypeConstants.JobOfferFlg.ARI, entity.jobOfferFlg)) {
				detailForm.arbeitPreviewPath =  String.format(
						getCommonProperty("gc.arbeitPreview.url.format"),
						MArbeitConstants.ArbeitSite.getArbeitSiteConst(String.valueOf(entity.arbeitTodouhukenId)),
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