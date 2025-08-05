package com.gourmetcaree.admin.pc.shopList.action.shopList;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.exception.SNonUniqueResultException;
import org.seasar.extension.jdbc.exception.SOptimisticLockException;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.util.Base64Util;
import org.seasar.framework.util.UUID;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.ActionMessagesUtil;
import org.seasar.struts.util.MessageResourcesUtil;

import com.gourmetcaree.admin.pc.shopList.dto.shopList.InputViewDto;
import com.gourmetcaree.admin.pc.shopList.dto.shopList.ShopListSearchDto;
import com.gourmetcaree.admin.pc.shopList.form.shopList.InputCsvForm;
import com.gourmetcaree.admin.pc.shopList.form.shopList.ShopListBaseForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.arbeitsys.logic.ArbeitAddressLogic;
import com.gourmetcaree.arbeitsys.logic.ArbeitRailloadLogic;
import com.gourmetcaree.arbeitsys.service.ArbeitGyotaiService;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.dto.ShopListMaterialDto;
import com.gourmetcaree.common.dto.TagListDto;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.exception.ImageWriteErrorException;
import com.gourmetcaree.common.property.ShopListProperty;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.common.util.WebdataFileUtils;
import com.gourmetcaree.common.util.WebdataFileUtils.ContentType;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.MShopListTagMapping;
import com.gourmetcaree.db.common.entity.TShopList;
import com.gourmetcaree.db.common.entity.TShopListAttribute;
import com.gourmetcaree.db.common.entity.TShopListLine;
import com.gourmetcaree.db.common.entity.TShopListMaterial;
import com.gourmetcaree.db.common.entity.TShopListRoute;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.common.property.ShopListSearchInputCsvProperty;
import com.gourmetcaree.db.common.service.CityService;
import com.gourmetcaree.db.common.service.PrefecturesService;
import com.gourmetcaree.db.common.service.ShopListLineService;
import com.gourmetcaree.db.common.service.ShopListMaterialNoDataService;
import com.gourmetcaree.db.common.service.StationGroupService;

/**
 * CSV入力用アクション
 * @author Takehiro Nakamori
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN, ManageAuthLevel.STAFF, ManageAuthLevel.AGENCY, ManageAuthLevel.OTHER, ManageAuthLevel.SALES})
public class InputCsvAction extends ShopListBaseAction {

	/** ログオブジェクト */
	private static final Logger log = Logger.getLogger(InputCsvAction.class);

	/** 店舗情報最大文字数 */
	private static final int SHOP_INFORMATION_MAX_LENGTH = 300;

	/** 業態【HP表示用】最大文字数 */
	private static final int INDUSTRY_TEXT_MAX_LENGTH = 20;

	/** キャッチコピー最大文字数 */
	private static final int CATCHCOPY_MAX_LENGTH = 30;

	/** 店舗一覧同時登録最大数 */
	private static final int MAX_REGISTER_NUM = 100;

	/** オープン日公開期限に文字列を入れた場合の値 */
	private static final long ERROR_OPEN_DATE_LIMIT_DISPLAY_DATE = -32400000;

	/** 区分に文字列を入れた場合の値 */
	private static final int ERROR_INTEGER = -1;

	/** 一覧表示最大数の初期値 */
	private static final int DEFALT_MAX_ROW = 10;

	/** 最大表示件数維持キー */
	private static final String KEPP_MAX_REGISTER_KEY = "keep_max_register_key";

	/** アクションフォーム */
	@ActionForm
	@Resource
	private InputCsvForm inputCsvForm;

	/** 店舗一覧データ無し画像サービス */
	@Resource
	private ShopListMaterialNoDataService shopListMaterialNoDataService;

	/** バイト側ルートロジック */
	@Resource
	private ArbeitRailloadLogic arbeitRailloadLogic;

	/** バイト側住所ロジック */
	@Resource
	private ArbeitAddressLogic arbeitAddressLogic;

	/** バイト側業態サービス */
	@Resource
	private ArbeitGyotaiService arbeitGyotaiService;

	/** 都道府県サービス */
	@Resource
	private PrefecturesService prefecturesService;

	/** 市区町村サービス */
	@Resource
	private CityService cityService;

	/** 店舗駅サービス */
	@Resource
	private ShopListLineService shopListLineService;

	/** 駅情報サービス */
	@Resource
	private StationGroupService stationGroupService;

	/** 店舗一覧リスト */
	public List<ShopListSearchDto> shopList;

	/** ページナビ */
	public PageNavigateHelper pageNavi;

	public InputViewDto viewDto;

	/**
	 * 初期表示メソッド
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", urlPattern = "index/{customerId}", input = TransitionConstants.ShopList.JSP_APQ05L01)
	@MethodAccess(accessCode="SHOPLIST_INPUTCSV_INDEX")
	public String index() {
		checkArgsNull(NO_BLANK_FLG_NG, inputCsvForm.customerId);
		ActionMessages errors = new ActionMessages();
		return show(errors);
	}

	/**
	 * ページ変更
	 * @return
	 */
	@Execute(validator = false, reset = "resetErrorFlg", urlPattern = "changePage/{targetPage}", input = TransitionConstants.ShopList.JSP_APQ05L01)
	@MethodAccess(accessCode="SHOPLIST_INPUTCSV_INDEX")
	public String changePage() {
		checkArgsNull(NO_BLANK_FLG_NG, inputCsvForm.customerId);
		ActionMessages errors = new ActionMessages();
		return show(errors);
	}

	/**
	 * 住所取得時に呼び出されるメソッド
	 * @return
	 */
	@Execute(validator = false, reset = "resetErrorFlg", input = TransitionConstants.ShopList.JSP_APQ05L01)
	@MethodAccess(accessCode="SHOPLIST_INPUTCSV_FAILGEOLIST")
	public String failGeoCodingList() {
		checkArgsNull(NO_BLANK_FLG_NG, inputCsvForm.customerId, inputCsvForm.failGeoId);
		ActionMessages errors = new ActionMessages();
		for (String id : inputCsvForm.failGeoId.split(",")) {
			errors.add("errors", new ActionMessage("errors.app.failGeoCodingWithId", id));
		}
		inputCsvForm.geoErrorFlg = true;
		inputCsvForm.errorFlg = true;
		return show(errors);
	}

	/**
	 * 詳細編集時のジオコーディングに失敗したときに呼び出されるメソッド
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_APQ05E01)
	@MethodAccess(accessCode="SHOPLIST_INPUTCSV_FAILGEODETAIL")
	public String failGeoCodeingDetail() {
		throw new ActionMessagesException("errors.failGeoCoding");
	}


	/**
	 * 初期表示遷移メソッド
	 * @return
	 */
	private String show(ActionMessages errors) {
		int maxRow = 0;

		if(StringUtils.isNotEmpty((String)session.getAttribute(KEPP_MAX_REGISTER_KEY))) {
			maxRow = NumberUtils.toInt((String)session.getAttribute(KEPP_MAX_REGISTER_KEY));
			inputCsvForm.maxRow = (String)session.getAttribute(KEPP_MAX_REGISTER_KEY);
			session.removeAttribute(KEPP_MAX_REGISTER_KEY);
		}else {
			if(StringUtils.isEmpty(inputCsvForm.maxRow)) {
				maxRow = DEFALT_MAX_ROW;
			} else {
				maxRow = NumberUtils.toInt(inputCsvForm.maxRow);
			}
		}

		createTempList(errors, maxRow);

		viewDto = new InputViewDto();
		viewDto.imageList = createImageDtoList(NumberUtils.toInt(inputCsvForm.customerId));

		return TransitionConstants.ShopList.JSP_APQ05L01;
	}

	/**
	 * 編集
	 * @return
	 */
	@Execute(validator = false, reset = "resetDetail", urlPattern = "edit/{customerId}/{id}", input = TransitionConstants.ShopList.JSP_APQ05L01)
	@MethodAccess(accessCode="SHOPLIST_INPUTCSV_EDIT")
	public String edit() {

		Where where = new SimpleWhere()
						.eq("id", inputCsvForm.id)
						.eq("customerId", inputCsvForm.customerId)
						.eq("status", MTypeConstants.ShopListStatus.TEMP_SAVE)
						.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED);
		List<TShopList> entityList;
		try {
			entityList = shopListService.findByCondition(where);
			TShopList entity = entityList.get(0);
			Beans.copy(entity, inputCsvForm).execute();
			initUploadMaterial(inputCsvForm);
			for (String materialKbn : MTypeConstants.ShopListMaterialKbn.getOriginalMaterialKbnList()) {
				boolean materialExistsFlg = shopListMaterialNoDataService
						.isMaterialEntityExist(
								NumberUtils.toInt(inputCsvForm.id),
								Integer.parseInt(materialKbn));
				if (inputCsvForm.targetId == 0 || materialExistsFlg) {
					setCsvMaterial(inputCsvForm, materialKbn);
				} else {
					setCsvMaterial(inputCsvForm, inputCsvForm.targetId, materialKbn);
				}
			}

			if(entity.openDateLimitDisplayDate != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				inputCsvForm.openDateLimitDisplayDate = sdf.format(entity.openDateLimitDisplayDate);
			}

			createAttributeArrays(entity.id, inputCsvForm);
			createStationDtoList(entity.id, inputCsvForm);
			// タグをセット
			inputCsvForm.tagListDto = tagListLogic.shopTagFindAll();
			super.getShopListTag(entity.id, inputCsvForm);
		} catch (WNoResultException e) {
			throw new ActionMessagesException("errors.pageNotFoundError");
		}

		viewDto = new InputViewDto();
		viewDto.imageList = createImageDtoList(NumberUtils.toInt(inputCsvForm.customerId));

		inputCsvForm.setProcessFlgNg();
		return TransitionConstants.ShopList.JSP_APQ05E01;
	}

	/**
	 * 編集画面へ遷移
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_APQ05L01)
	@MethodAccess(accessCode="SHOPLIST_INPUTCSV_BACK")
	public String backToEdit() {
		viewDto = new InputViewDto();
		viewDto.imageList = createImageDtoList(NumberUtils.toInt(inputCsvForm.customerId));

		return TransitionConstants.ShopList.JSP_APQ05L01;
	}

	/**
	 * 修正画面から確認一覧へ戻る
	 * @return
	 */
	@Execute(validator = false, reset = "resetErrorFlg", input = TransitionConstants.ShopList.JSP_APQ05L01)
	@MethodAccess(accessCode="SHOPLIST_INPUTCSV_BACK")
	public String backToList() {
		checkArgsNull(NO_BLANK_FLG_NG, inputCsvForm.customerId);
		ActionMessages errors = new ActionMessages();
		return show(errors);
	}

	/**
	 * 確認画面詳細
	 * @return
	 */
	@Execute(validator = true, validate = "validate", reset="resetCheckBox", input = TransitionConstants.ShopList.ACTION_CSV_EDIT_BACK)
	@MethodAccess(accessCode="SHOPLIST_INPUTCSV_CONFDETAIL")
	public String confDetail() {
		inputCsvForm.setProcessFlgOk();

		viewDto = new InputViewDto();
		viewDto.imageList = createImageDtoList(NumberUtils.toInt(inputCsvForm.customerId));

		return TransitionConstants.ShopList.JSP_APQ05E02;
	}

	/**
	 * 入力訂正
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_APQ05E01)
	@MethodAccess(accessCode="SHOPLIST_INPUTCSV_CORRECT")
	public String correct() {
		inputCsvForm.setProcessFlgNg();

		viewDto = new InputViewDto();
		viewDto.imageList = createImageDtoList(NumberUtils.toInt(inputCsvForm.customerId));

		return TransitionConstants.ShopList.JSP_APQ05E01;
	}

	/**
	 * 詳細のサブミット
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_APQ05E01)
	@MethodAccess(accessCode="SHOPLIST_INPUTCSV_SUBMITDETAIL")
	public String submitDetail() {
		if (!inputCsvForm.processFlg) {
			throw new FraudulentProcessException();
		}

		ShopListProperty property = new ShopListProperty();
		TShopList entity = new TShopList();
		Beans.copy(inputCsvForm, entity).excludes("targetId","openDateLimitDisplayDate").execute();
		if(Integer.parseInt(inputCsvForm.domesticKbn) == MTypeConstants.DomesticKbn.DOMESTIC) {
			entity.address1 = valueToNameConvertLogic.convertToPrefecturesName(Integer.parseInt(inputCsvForm.prefecturesCd))
								+ valueToNameConvertLogic.convertToCityName(inputCsvForm.cityCd)
								+ inputCsvForm.address;
		}else{
			entity.address1 = inputCsvForm.foreignAddress;
		}
		property.tShopList = entity;
		property.tShopListAttributeList = createShopListAttributeListFromForm(inputCsvForm);
		if(StringUtils.isNotBlank(inputCsvForm.openDateLimitDisplayDate)) {
			property.tShopList.openDateLimitDisplayDate = Date.valueOf(inputCsvForm.openDateLimitDisplayDate);
		}

		property.tShopListLineList = createShopListLineList(inputCsvForm.stationDtoList);
		// タグが選択されている場合
		if(StringUtils.isNotBlank(inputCsvForm.tagList)) {
			property.mShopListTagMappingList = createShopListTagList(inputCsvForm.tagList, Integer.parseInt(inputCsvForm.id));
		}
		setMaterialProperty(property, inputCsvForm);
		shopListLogic.updateAllShopList(property);
		inputCsvForm.resetDetail();

		return TransitionConstants.ShopList.REDIRECT_SHOPLIST_CSV_BACK_TO_LIST;

	}

	/**
	 * 素材のアップロードを行う。
	 * @return 登録画面
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_APQ05E01)
	@MethodAccess(accessCode="SHOPLIST_EDIT_UPMATERIAL")
	public String upAjaxMaterial() {

		// パラメータが空の場合はエラー
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, inputCsvForm.hiddenMaterialKbn);

		// 画像のアップロード処理
		uploadTempImageJsonResponse(inputCsvForm);
		return null;
	}

	/**
	 * アップロードされた素材の削除処理
	 * @return 登録画面
	 */
	@Execute(validator=false, input = TransitionConstants.ShopList.JSP_APQ05E01)
	@MethodAccess(accessCode="SHOPLIST_EDIT_DELMATERIAL")
	public String delAjaxMaterial() {

		// パラメータが空の場合はエラー
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, inputCsvForm.hiddenMaterialKbn);

		// 画像の削除処理
		deleteTempImageJsonResponse(inputCsvForm);
		return null;
	}

	/**
	 * 一時保存リスト作成
	 */
	private void createTempList(ActionMessages errors, int maxRow) {
		try {
			ShopListSearchInputCsvProperty property = createSearchProperty(maxRow);
			shopListService.searchInputCsvList(property);

			pageNavi = property.pageNavi;
			List<Map<String, Object>> mapList = new ArrayList<Map<String,Object>>();
			for (TShopList entity : property.shopList) {
				Map<String, Object> map = new HashMap<String, Object>();

				List<Integer> industryKbnList = new ArrayList<Integer>();
				if (entity.industryKbn1 != null) {
					industryKbnList.add(entity.industryKbn1);
				}

				if (entity.industryKbn2 != null) {
					industryKbnList.add(entity.industryKbn2);
				}


				String detailPath = GourmetCareeUtil.makePath(TransitionConstants.ShopList.ACTION_SHOPLIST_INPUTCSV, "/edit", String.valueOf(entity.customerId), String.valueOf(entity.id));
//				map.put("shopList", dto);
				map.put("id", entity.id);
				map.put("targetId", entity.targetId);
				if (entity.targetId != null) {
					if (!shopListService.isOwnData(entity.targetId, NumberUtils.toInt(inputCsvForm.customerId))) {
						errors.add("errors", new ActionMessage("errors.app.notOwnShoplistData", entity.targetId));
					}
					map.put("dispId", entity.targetId);
				} else {
					map.put("dispId", entity.id);
				}
				map.put("checked", "");
				if (CollectionUtils.isNotEmpty(industryKbnList)) {
					map.put("industryName", valueToNameConvertLogic.convertToIndustryName(industryKbnList.toArray(new Integer[0])));
				} else {
					map.put("industryName", "");
				}

				map.put("areaCd", entity.areaCd);
				map.put("latLngKbn", entity.latLngKbn);
				map.put("shopName", entity.shopName);
				map.put("detailPath", detailPath);
				map.put("latitude", entity.latitude);
				map.put("longitude", entity.longitude);
				map.put("seatKbn", entity.seatKbn);
				map.put("salesPerCustomerKbn", entity.salesPerCustomerKbn);
				map.put("address", entity.address1);
				map.put("displayFlg", entity.displayFlg);

				mapList.add(map);
				checkInputCsvData(entity, errors);
			}
			inputCsvForm.csvMapList = mapList;

			if (mapList.size() > MAX_REGISTER_NUM) {
				errors.add("errors", new ActionMessage("errors.app.registerShopListOverLimit", MAX_REGISTER_NUM));
				inputCsvForm.geoErrorFlg = true;
			}

		} catch (WNoResultException e) {
			inputCsvForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.dataNotFound");
		}
		if (!errors.isEmpty()) {
			ActionMessagesUtil.addErrors(request, errors);
			inputCsvForm.errorFlg = true;
		}
	}

	/**
	 * 入力CSVデータのチェック
	 * @param entity
	 * @param errors
	 */
	private void checkInputCsvData(TShopList entity, ActionMessages errors) {

		int dispId;

		if (entity.targetId != null) {
			dispId = entity.targetId;
		} else {
			dispId = entity.id;
		}

		//エリア
		if (entity.areaCd == null) {
			errors.add("errors", new ActionMessage("errors.app.requiredWithId", dispId, MessageResourcesUtil.getMessage("labels.areaCd")));
		}

		//店舗名
		if (StringUtils.isBlank(entity.shopName)) {
			errors.add("errors", new ActionMessage("errors.app.requiredWithId", dispId, MessageResourcesUtil.getMessage("labels.shopName")));
		}

		//業態【HP表示用】
		checkIndustryText(entity, errors, dispId);

		// 業種
		checkIndustry(entity, errors, dispId);

		//キャッチコピー
		checkCatchCopy(entity, errors, dispId);

		// 必須なし変更後処理 300文字判定だけ行う。
		if (StringUtils.isNotBlank(entity.shopInformation)) {
			if (entity.shopInformation.length() > SHOP_INFORMATION_MAX_LENGTH) {
				errors.add("errors", new ActionMessage("errors.app.maxlengthWithId",
						dispId,
						MessageResourcesUtil.getMessage("labels.shopInformation"),
						SHOP_INFORMATION_MAX_LENGTH));
			}
		}

		// 住所
		checkAddress(entity, errors, dispId);

		// 電話番号
		checkPhoneNumber(entity, errors, dispId);

		//駅情報
		checkStationGroup(entity, errors, dispId);

		//席数区分
		checkSeatKbn(entity, errors, dispId);

		//客単価区分
		checkSalesPerCustomerKbn(entity, errors, dispId);

		//オープン日
		checkOpenDate(entity, errors, dispId);

		// インディードタグ
		checktag(entity, errors, dispId);
	}

	/**
	 * ルートをチェックします。
	 * @param shopListId 店舗一覧ID
	 * @param errors エラー
	 * @param dispId 表示ID
	 * @param pref アルバイト都道府県ID
	 */
	private void checkRoute(int shopListId, ActionMessages errors, int dispId, Object pref) {
		if (pref.equals(null)) {
			return;
		}
		List<TShopListRoute> routeList = shopListRouteService.findByShopListId(shopListId);
		for (TShopListRoute route : routeList) {
			if (route.railroadId != null
					|| route.routeId != null
					|| route.stationId != null) {

				if (!arbeitRailloadLogic.existRoute(route, (Integer) pref)) {

					errors.add("errors", new ActionMessage("errors.app.notExistRouteWithId",
							dispId));
					// 一回エラーを出せばいいので終了
					return;
				}
			}
		}
	}


	/**
	 * 都道府県のチェックを行います。
	 * @param entity 店舗一覧
	 * @param errors エラー
	 * @param dispId 表示ID
	 */
	private void checkPrefectures(TShopList entity, ActionMessages errors, int dispId) {

		if (entity.arbeitTodouhukenId == null){
			errors.add("errors", new ActionMessage("errors.app.notExistPrefectureMunicipalityWithId",
					dispId));
			return;
		}

		if (entity.arbeitAreaId != null && !arbeitAddressLogic.existPrefectureMunicipality(entity)) {
			errors.add("errors", new ActionMessage("errors.app.notExistPrefectureMunicipalityWithId",
								dispId));
		}

		if (entity.arbeitAreaId == null && entity.arbeitSubAreaId != null) {
			errors.add("errors", new ActionMessage("errors.app.cantChooseSubareaWithId", dispId));
			return;
		}

		if (entity.arbeitSubAreaId != null && !arbeitAddressLogic.existSubArea(entity)) {
			errors.add("errors", new ActionMessage("errors.app.notExistSubareaWithId",
					dispId));
		}
	}


	/**
	 * 属性が存在するかどうか。
	 * SELECTしてきた属性と、m_typeに存在する値の数が一致しなければfalseとする。
	 * @param shopListId 店舗一覧ID
	 * @param typeCd タイプコード
	 * @return 存在すればtrue
	 */
	private boolean isAttributesExists(int shopListId, String typeCd) {
		List<Integer> attributeList = shopListAttributeService.createAttributeValueList(shopListId, typeCd);
		if (CollectionUtils.isEmpty(attributeList)) {
			return true;
		}
		int count = typeService.countTypeValues(typeCd, attributeList);

		return attributeList.size() == count;
	}

	/**
	 * 電話番号のチェック
	 * @param entity
	 * @param errors
	 * @param message メッセージに使用するラベル
	 */
	private void checkPhoneNumber(TShopList entity, ActionMessages errors, int dispId) {
		// 電話番号が入力されていなければ終了
		if (StringUtils.isBlank(entity.csvPhoneNo)) {
			return;
		}

		//番号をまとめて入力する場合
		String[] tel = entity.csvPhoneNo.split("-",0);

		//ハイフンできちんと区切られているかチェック
		if(tel.length != 3) {
			errors.add("errors", new ActionMessage("errors.app.phoneNoFailed", dispId));
		}

		//数値で入力されているかチェック
		for(String number : tel){
			if(!StringUtils.isNumeric(number)) {
				errors.add("errors", new ActionMessage("errors.app.phoneNoFailed", dispId));
			}
		}
	}

	/**
	 * 駅情報のチェック
	 * @param entity
	 * @param errors
	 * @param dispId
	 */
	private void checkStationGroup(TShopList entity, ActionMessages errors, int dispId) {
		List<TShopListLine> list = shopListLineService.findByShopListId(entity.id);
		for(TShopListLine line : list) {
			if(line.stationCd == 0) {
				errors.add("errors", new ActionMessage("errors.app.requiredThreeSetStationInformationWithId", dispId));
				return;
			}
			if(!stationGroupService.checkStationCd(line.stationCd)) {
				errors.add("errors", new ActionMessage("errors.app.mistakenStationCdWithId", dispId));
			}
			if (!typeService.existType(MTypeConstants.TransportationKbn.TYPE_CD, line.transportationKbn)){
				errors.add("errors", new ActionMessage("errors.app.requiredInNumberWithId",
										dispId,
										MessageResourcesUtil.getMessage("labels.transportationKbn")));
			}
		}
	}

	/**
	 * 席数区分のチェック
	 * @param entity
	 * @param errors
	 * @param dispId
	 */
	private void checkSeatKbn(TShopList entity, ActionMessages errors, int dispId) {
		if (entity.seatKbn != null && !typeService.existType(MTypeConstants.SeatKbn.TYPE_CD, entity.seatKbn)){
			errors.add("errors", new ActionMessage("errors.app.requiredInNumberWithId",
									dispId,
									MessageResourcesUtil.getMessage("labels.seatKbn")));
		}
	}

	/**
	 * 客単価区分のチェック
	 * @param entity
	 * @param errors
	 * @param dispId
	 */
	private void checkSalesPerCustomerKbn(TShopList entity, ActionMessages errors, int dispId) {
		if (entity.salesPerCustomerKbn != null && !typeService.existType(MTypeConstants.SalesPerCustomerKbn.TYPE_CD, entity.salesPerCustomerKbn)){
			errors.add("errors", new ActionMessage("errors.app.requiredInNumberWithId",
									dispId,
									MessageResourcesUtil.getMessage("labels.salesPerCustomerKbn")));
		}
	}

	/**
	 * オープン日のチェック
	 * @param entity
	 * @param errors
	 * @param dispId
	 */
	private void checkOpenDate(TShopList entity, ActionMessages errors, int dispId) {
		Calendar now = Calendar.getInstance();
		if(entity.openDateYear != null) {
			if(entity.openDateYear == 0) {
				errors.add("errors", new ActionMessage("errors.app.notNumberWithId", dispId, "labels.openDateYear"));

			}else if(entity.openDateYear < now.get(Calendar.YEAR) || entity.openDateYear > now.get(Calendar.YEAR) + 1 ) {
				errors.add("errors", new ActionMessage("errors.app.outOfRangeOpenDateYearWithId", dispId));
			}
		}

		if(entity.openDateMonth != null) {
			if(entity.openDateMonth == 0) {
				errors.add("errors", new ActionMessage("errors.app.notNumberWithId", dispId, "labels.openDateMonth"));
			}
		}

		if(entity.openDateLimitDisplayDate != null) {
			if(entity.openDateLimitDisplayDate.getTime() == ERROR_OPEN_DATE_LIMIT_DISPLAY_DATE) {
				errors.add("errors", new ActionMessage("errors.app.notExistOpenDateLimitDisplayDateWithId", dispId));
			}
		}
	}


	/**
	 * Fax番号のチェック
	 * @param entity
	 * @param errors
	 * @param dispId
	 */
	private void checkFaxNo(TShopList entity, ActionMessages errors, int dispId) {
		// FAX番号が入力されていなければ終了
		if (StringUtils.isBlank(entity.csvFaxNo)) {
			return;
		}

		// FAX番号のチェック
		if (StringUtils.isBlank(entity.faxNo1)
				|| StringUtils.isBlank(entity.faxNo2)
				|| StringUtils.isBlank(entity.faxNo3)) {
			errors.add("errors", new ActionMessage("errors.app.phoneHypenWithId",
												dispId,
												MessageResourcesUtil.getMessage("labels.faxNo")));
		} else if (!StringUtils.isNumeric(entity.faxNo1)
					|| !StringUtils.isNumeric(entity.faxNo2)
					|| !StringUtils.isNumeric(entity.faxNo3)) {
			errors.add("errors", new ActionMessage("errors.app.notNumberWithId",
									dispId,
									MessageResourcesUtil.getMessage("labels.faxNo")));
		}
	}

	/**
	 * 住所のチェック
	 */
	private void checkAddress(TShopList entity, ActionMessages errors, int dispId) {

		if(entity.domesticKbn == null) {
			errors.add("errors", new ActionMessage("errors.app.requiredWithId",
								dispId,
								MessageResourcesUtil.getMessage("labels.domesticKbn")));
			return;
		}

		if(entity.domesticKbn == MTypeConstants.DomesticKbn.DOMESTIC) {

			if(entity.prefecturesCd == null || StringUtils.isBlank(entity.cityCd)) {
				errors.add("errors", new ActionMessage("errors.app.requiredSelectDomesticWithId", dispId));
				return;
			}

			if(!prefecturesService.checkFindByPrefectureCd(entity.prefecturesCd)) {
				errors.add("errors", new ActionMessage("errors.app.requiredInCodeWithId",
						dispId,
						MessageResourcesUtil.getMessage("labels.prefecturesCd")));
				return;
			}

			if(!cityService.checkFindByCityCd(entity.cityCd)) {
				errors.add("errors", new ActionMessage("errors.app.requiredInCodeWithId",
						dispId,
						MessageResourcesUtil.getMessage("labels.cityCd")));
				return;
			}

			if(entity.shutokenForeignAreaKbn != null
					|| !StringUtils.isBlank(entity.foreignAddress)) {
				errors.add("errors", new ActionMessage("errors.app.canNotSelectDomesticWithId",	dispId));
			}

			if(StringUtils.isNotBlank(entity.cityCd)) {
				Integer prefectureCd = cityService.getPrefectureCd(entity.cityCd);
				if(!entity.prefecturesCd.equals(prefectureCd)) {
					errors.add("errors", new ActionMessage("errors.app.selectDifferentCityWithId",	dispId));
				}
			}

		}

		if(entity.domesticKbn == MTypeConstants.DomesticKbn.overseas) {
			if(entity.shutokenForeignAreaKbn == null || StringUtils.isBlank(entity.foreignAddress)) {
				errors.add("errors", new ActionMessage("errors.app.requiredSelectOverseasWithId", dispId));
			}

			if(entity.prefecturesCd != null || StringUtils.isNotBlank(entity.cityCd) || StringUtils.isNotBlank(entity.address)) {
				errors.add("errors", new ActionMessage("errors.app.canNotSelectOverseasWithId", dispId));
			}
		}

	}

	/**
	 * 業種のチェック
	 * @param entity
	 * @param errors
	 */
	private void checkIndustry(TShopList entity, ActionMessages errors, int dispId) {
		if (entity.industryKbn1 == null && entity.industryKbn2 != null) {
			errors.add("errors", new ActionMessage("errors.app.wrongInputDataWithId",
													dispId,
													MessageResourcesUtil.getMessage("labels.industryKbn2"),
													MessageResourcesUtil.getMessage("labels.industryKbn1")));
			return;
		}

		if (entity.industryKbn1 != null) {
			if(entity.industryKbn1 == ERROR_INTEGER) {
				errors.add("errors", new ActionMessage("errors.app.integerTypeWithId",
						dispId,
						MessageResourcesUtil.getMessage("labels.industryKbn1")));

			}else if (!typeService.existType(MTypeConstants.IndustryKbn.TYPE_CD, entity.industryKbn1)){
				errors.add("errors", new ActionMessage("errors.app.requiredInNumberWithId",
										dispId,
										MessageResourcesUtil.getMessage("labels.industryKbn1")));
			}
		} else {
			errors.add("errors", new ActionMessage("errors.app.requiredWithId",
										dispId,
										MessageResourcesUtil.getMessage("labels.industryKbn1")));
		}

		if (entity.industryKbn2 != null) {
			if(entity.industryKbn2 == ERROR_INTEGER) {
				errors.add("errors", new ActionMessage("errors.app.integerTypeWithId",
						dispId,
						MessageResourcesUtil.getMessage("labels.industryKbn2")));

			}else if (!typeService.existType(MTypeConstants.IndustryKbn.TYPE_CD, entity.industryKbn2)){
				errors.add("errors", new ActionMessage("errors.app.requiredInNumberWithId",
									dispId,
									MessageResourcesUtil.getMessage("labels.industryKbn2")));
			}
		}
	}

	/**
	 * キャッチコピーのチェック
	 */
	private void checkCatchCopy(TShopList entity, ActionMessages errors, int dispId) {
		if(StringUtils.isBlank(entity.catchCopy)) {
			errors.add("errors", new ActionMessage("errors.app.requiredWithId",
					dispId,
					MessageResourcesUtil.getMessage("labels.catchCopy")));
		}else if(entity.catchCopy.length() > CATCHCOPY_MAX_LENGTH){
			errors.add("errors", new ActionMessage("errors.app.maxlengthWithId",
					dispId,
					MessageResourcesUtil.getMessage("labels.catchCopy"),
					CATCHCOPY_MAX_LENGTH + "文字"));
		}
	}

	/**
	 * 業態【HP表示用】のチェック
	 */
	private void checkIndustryText(TShopList entity, ActionMessages errors, int dispId) {
		if(StringUtils.isBlank(entity.industryText)) {
			errors.add("errors", new ActionMessage("errors.app.requiredWithId",
					dispId,
					MessageResourcesUtil.getMessage("labels.industryText")));
		}else if(entity.industryText.length() > INDUSTRY_TEXT_MAX_LENGTH) {
			errors.add("errors", new ActionMessage("errors.app.maxlengthWithId",
					dispId,
					MessageResourcesUtil.getMessage("labels.industryText"),
					INDUSTRY_TEXT_MAX_LENGTH + "文字"));
		}
	}


	/**
	 * 登録
	 * @return
	 */
	@Execute(validator=false, reset="resetCheckBox", validate = "validateSaveIdList", input = TransitionConstants.ShopList.JSP_APQ05L01)
	@MethodAccess(accessCode="SHOPLIST_INPUTCSV_SUBMIT")
	public String submit() {
		Set<Integer> updateIdSet = new HashSet<Integer>();
		for(Map<String, Object> map : inputCsvForm.csvMapList) {
			// 保存するIDリストに含まれていなければコンティニュー
			if (!ArrayUtils.contains(inputCsvForm.saveIdList, String.valueOf(map.get("id")))) {
				continue;
			}

			int id = NumberUtils.toInt(String.valueOf(map.get("id")));

			int originalId;

			boolean updateFlg;
			TShopList targetEntity = new TShopList();
			if (map.get("targetId") != null) {
				updateFlg = true;
			} else {
				updateFlg = false;
			}

			try {
				TShopList tempEntity = shopListService.getData(NumberUtils.toInt(inputCsvForm.customerId),
															id,
															MTypeConstants.ShopListStatus.TEMP_SAVE);


				if (updateFlg) {
					try {
					targetEntity.version = shopListService.getVersion(NumberUtils.toInt(inputCsvForm.customerId),
																		NumberUtils.toInt(String.valueOf(map.get("targetId"))),
																		MTypeConstants.ShopListStatus.REGISTERED);
					targetEntity.id = NumberUtils.toInt(String.valueOf(map.get("targetId")));
					shopListMaterialService.moveMaterial(id, NumberUtils.toInt(String.valueOf(map.get("targetId"))));
					Beans.copy(tempEntity, targetEntity).excludes("id", "targetId", "version").execute();
					} catch (WNoResultException e) {
						throw new FraudulentProcessException();
					}


					originalId = targetEntity.id;
					shopListAttributeService.deleteByShopListId(originalId);
					shopListLineService.deleteByShopListId(originalId);
					shopListTagMappingService.deleteByShopListId(originalId);
				} else {
					originalId = id;
				}


				// すでに登録しているものをアップデート
				if (updateFlg) {
					targetEntity.latitude = NumberUtils.toDouble(String.valueOf(map.get("latitude")));
					targetEntity.longitude = NumberUtils.toDouble(String.valueOf(map.get("longitude")));
					targetEntity.status = MTypeConstants.ShopListStatus.REGISTERED;
					shopListService.updateForImport(targetEntity);
					shopListService.delete(tempEntity);

					List<TShopListAttribute> attrList = shopListAttributeService.findByShopListId(id);
					if (CollectionUtils.isNotEmpty(attrList)) {
						for (TShopListAttribute entity : attrList) {
							TShopListAttribute newEntity = Beans.createAndCopy(TShopListAttribute.class, entity)
									.includes("attributeValue", "attributeCd")
									.execute();
							newEntity.shopListId = originalId;
							shopListAttributeService.insert(newEntity);

						}
					}


					List<TShopListLine> lineList = shopListLineService.findByShopListId(id);
					if (CollectionUtils.isNotEmpty(lineList)) {
						for (TShopListLine oldEntity : lineList) {
							TShopListLine newEntity = Beans.createAndCopy(TShopListLine.class, oldEntity)
														.includes("companyCd", "lineCd", "stationCd", "transportationKbn", "timeRequiredMinute", "displayOrder")
														.execute();
							newEntity.shopListId = originalId;
							shopListLineService.insert(newEntity);
						}
					}

					// 登録されているタグを取得
					List<MShopListTagMapping> tagList = shopListTagMappingService.findByShopListId(id);
					// 空でなければ各タブを登録
					if (CollectionUtils.isNotEmpty(tagList)) {
						for (MShopListTagMapping oldTagEntity : tagList) {
							MShopListTagMapping newTagEntity = Beans.createAndCopy(MShopListTagMapping.class, oldTagEntity)
														.includes("shopListTagId")
														.execute();
							newTagEntity.shopListId = originalId;
							shopListTagMappingService.insert(newTagEntity);
						}
					}
				// 新しく追加するものをアップデート
				} else {
					tempEntity.latitude = NumberUtils.toDouble(String.valueOf(map.get("latitude")));
					tempEntity.longitude = NumberUtils.toDouble(String.valueOf(map.get("longitude")));
					tempEntity.status = MTypeConstants.ShopListStatus.REGISTERED;
					tempEntity.address1 = (String) map.get("address1");
					if(map.get("seatKbn") != null) {
						tempEntity.seating = valueToNameConvertLogic.convertToTypeName(MTypeConstants.SeatKbn.TYPE_CD, (int)map.get("seatKbn"));
					}
					if(map.get("salesPerCustomerKbn") != null) {
						tempEntity.unitPrice  = valueToNameConvertLogic.convertToTypeName(MTypeConstants.SalesPerCustomerKbn.TYPE_CD, (int)map.get("salesPerCustomerKbn"));
					}
					synchronized (tempEntity) {
						tempEntity.accessKey = UUID.create();
					}
					shopListService.updateWithNewDispOrder(tempEntity);
					shopListLogic.updateKeywordSearch(originalId);
				}


				updateIdSet.add(id);
			} catch (WNoResultException e) {
				throw new FraudulentProcessException();
			} catch (SOptimisticLockException e) {
				throw new ActionMessagesException("errors.optimisticLockException");
			}
		}

		// 登録から削除
		removeUpdateId(updateIdSet);

		log.info("店舗一覧を一括登録しました。顧客ID=" + inputCsvForm.customerId + "：登録者：" + userDto);
		return returnToAfterChanges();
	}

	/**
	 * 変更後のJSPへ戻る
	 * @return
	 */
	private String returnToAfterChanges() {
		if (CollectionUtils.isEmpty(getShopListTempIdListFromSession())) {
			return TransitionConstants.ShopList.REDIRECT_SHOPLIST_CSV_COMPLETE;
		}

		return createRedirectToIndexStr();
	}

	/**
	 * 更新したIDを除去
	 * @param updateIdSet
	 */
	private void removeUpdateId(Set<Integer> updateIdSet) {
		List<Integer> idList = getShopListTempIdListFromSession();
		for (Integer id : updateIdSet) {
			if (idList.contains(id)) {
				idList.remove(id);
			}
		}
		inputCsvForm.idList = idList;
		session.setAttribute(SHOP_LIST_TEMP_ID_LIST_SESSION_KEY, idList);
	}

	/**
	 * 削除
	 * @return
	 */
	@Execute(validator = false, reset="resetCheckBox", validate = "validateSaveIdList", input=TransitionConstants.ShopList.JSP_APQ05L01)
	@MethodAccess(accessCode="SHOPLIST_INPUTCSV_DELETE")
	public String delete() {
		List<Integer> idList = getShopListTempIdListFromSession();
		for (String id : inputCsvForm.saveIdList) {
			int targetId = NumberUtils.toInt(id);
			deleteTempData(targetId);
			idList.remove((Integer) targetId);
		}
		inputCsvForm.idList = idList;

		log.info("店舗一覧を一括削除しました。顧客ID=" + inputCsvForm.customerId + "：削除者：" + userDto);
		return returnToAfterChanges();
	}

	/**
	 * 完了画面
	 * @return
	 */
	@Execute(validator = false, reset = "resetFormWithoutCustomerId", input = TransitionConstants.ShopList.JSP_APQ05C03)
	@MethodAccess(accessCode="SHOPLIST_INPUTCSV_COMP")
	public String comp() {
		session.removeAttribute(SHOP_LIST_TEMP_ID_LIST_SESSION_KEY);
		return TransitionConstants.ShopList.JSP_APQ05C03;
	}


	/**
	 * セッションから店舗リストの一時保存IDを取得
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private List<Integer> getShopListTempIdListFromSession() {
		if (CollectionUtils.isNotEmpty(inputCsvForm.idList)) {
			return inputCsvForm.idList;
		}

		Object obj = getObjectFromSession(SHOP_LIST_TEMP_ID_LIST_SESSION_KEY);
		if (obj == null) {
			throw new FraudulentProcessException("店舗一覧の一時保存IDリストがみつかりません。");
		}

		if (obj instanceof List) {
			List<Integer> retList = new ArrayList<Integer>();
			for (Object id : (List) obj) {
				if (id instanceof Integer) {
					retList.add((Integer) id);
					continue;
				}

				throw new FraudulentProcessException("店舗一覧の一時保存IDリストの型が不正です。");
			}
			inputCsvForm.idList = retList;
			return retList;
		}

		throw new FraudulentProcessException("店舗一覧の一時保存IDリストの型が不正です。");
	}

	/**
	 * 一時保存の削除
	 * @param shopListId
	 */
	private void deleteTempData(int shopListId) {
		try {
			TShopList entity = shopListService.getData(NumberUtils.toInt(inputCsvForm.customerId), shopListId, MTypeConstants.ShopListStatus.TEMP_SAVE);
			shopListService.delete(entity);
			shopListLineService.deleteByShopListId(shopListId);
			// 店舗情報タグの一時保存情報を削除
			shopListTagMappingService.deleteByShopListId(shopListId);
		} catch (WNoResultException e) {
			log.warn("一時保存店舗一覧の取得に失敗しました。 ID=" + shopListId);
		}
	}

	/**
	 * 店舗一覧へ戻る
	 * @return
	 */
	@Execute(validator=false, reset="resetFormWithoutCustomerId", input = TransitionConstants.ShopList.JSP_APQ05L01)
	@MethodAccess(accessCode="SHOPLIST_INPUTCSV_BACKINDEX")
	public String backToIndex() {
		String backPath = createReindexPath(inputCsvForm.customerId);
		inputCsvForm.customerId = null;
		session.removeAttribute(SHOP_LIST_TEMP_ID_LIST_SESSION_KEY);
		return backPath;
	}

	/**
	 * 表示変数変更
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_APQ05L01)
	@MethodAccess(accessCode="SHOPLIST_INPUTCSV_INDEX")
	public String changeMaxRow() {
		checkArgsNull(NO_BLANK_FLG_NG, inputCsvForm.customerId);
		ActionMessages errors = new ActionMessages();
		createTempList(errors, NumberUtils.toInt(inputCsvForm.maxRow));
		session.setAttribute(KEPP_MAX_REGISTER_KEY, inputCsvForm.maxRow);
		viewDto = new InputViewDto();
		viewDto.imageList = createImageDtoList(NumberUtils.toInt(inputCsvForm.customerId));
		return TransitionConstants.ShopList.JSP_APQ05L01;
	}

	/**
	 * インデックスへのリダイレクト
	 * @return
	 */
	private String createRedirectToIndexStr() {
		return GourmetCareeUtil.makePath(TransitionConstants.ShopList.ACTION_SHOPLIST_INPUTCSV,
				"/index",
				inputCsvForm.customerId,
				TransitionConstants.REDIRECT_STR);
	}

	/**
	 * 電話番号・ファックス番号を変換します。
	 * @param entity
	 */
	private void convertPhoneNumber(TShopList entity) {
		if (StringUtils.isNotBlank(entity.phoneNo1)
				&& StringUtils.isNotBlank(entity.phoneNo2)
				&& StringUtils.isNotBlank(entity.phoneNo3)) {
			StringBuilder tel = new StringBuilder("");
			tel.append(entity.phoneNo1);
			tel.append(GourmetCareeConstants.HYPHEN_MINUS_STR);
			tel.append(entity.phoneNo2);
			tel.append(GourmetCareeConstants.HYPHEN_MINUS_STR);
			tel.append(entity.phoneNo3);
			entity.csvPhoneNo = tel.toString();
		} else if (StringUtils.isBlank(entity.phoneNo1)
				      && StringUtils.isBlank(entity.phoneNo2)
				      && StringUtils.isBlank(entity.phoneNo3)) {
			entity.csvPhoneNo = "";
		}

		if (StringUtils.isNotBlank(entity.faxNo1)
				&& StringUtils.isNotBlank(entity.faxNo2)
				&& StringUtils.isNotBlank(entity.faxNo3)) {
			StringBuilder tel = new StringBuilder("");
			tel.append(entity.faxNo1);
			tel.append(GourmetCareeConstants.HYPHEN_MINUS_STR);
			tel.append(entity.faxNo2);
			tel.append(GourmetCareeConstants.HYPHEN_MINUS_STR);
			tel.append(entity.faxNo3);
			entity.csvFaxNo = tel.toString();
		} else if (StringUtils.isBlank(entity.faxNo1)
					&& StringUtils.isBlank(entity.faxNo2)
					&& StringUtils.isBlank(entity.faxNo3)) {
			entity.csvFaxNo = "";
		}
	}



	/**
	 * インポートした店舗一覧を検索するためのプロパティを作成
	 * @return
	 */
	private ShopListSearchInputCsvProperty createSearchProperty(int maxRow) {
		ShopListSearchInputCsvProperty property = new ShopListSearchInputCsvProperty();
		property.maxRow = maxRow;
		property.targetPage = NumberUtils.toInt(inputCsvForm.targetPage, GourmetCareeConstants.DEFAULT_PAGE);
		property.idList = getShopListTempIdListFromSession();

		if (CollectionUtils.isEmpty(property.idList)) {
			inputCsvForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.dataNotFound");
		}

		return property;

	}



	/**
	 * CSVの素材をセット
	 * @param form フォーム
	 * @param materialKbn 素材区分
	 */
	private void setCsvMaterial(ShopListBaseForm form, String materialKbn) {
		setCsvMaterial(form, NumberUtils.toInt(form.id), materialKbn);
	}

	/**
	 * CSVの素材をセット
	 * @param form フォーム
	 * @param targetId 対象ID
	 * @param materialKbn 素材区分
	 */
	private void setCsvMaterial(ShopListBaseForm form, int targetId, String materialKbn) {
		String dirPath = getCommonProperty("gc.shopList.imgUpload.dir.session");
		StringBuilder dirName = new StringBuilder("");
		dirName.append(form.getIdForDirName());
		dirName.append("_");
		dirName.append(session.getId());

		Integer materialId;
		try {
			materialId = shopListMaterialService.getId(targetId, NumberUtils.toInt(materialKbn));
		} catch (SNonUniqueResultException e) {
			StringBuilder sb = new StringBuilder(0);
			sb.append("店舗一覧画像が複数見つかりました。")
				.append("対象ID:")
				.append(targetId)
				.append(" 素材区分：")
				.append(materialKbn);
			log.error(sb.toString(), e);
			return;
		}

		if (materialId == null) {
			return;
		}

		try {
			TShopListMaterial entity = shopListMaterialService.findById(materialId);
			ShopListMaterialDto dto = new ShopListMaterialDto();
			Beans.copy(entity, dto).excludes("materialData").execute();
			if (entity.materialData == null) {
				return;
			}

			String fileName = WebdataFileUtils.createFileName(String.valueOf(entity.materialKbn),
					ContentType.toEnum(entity.contentType));
			try {
				WebdataFileUtils.createWebdataFile(dirPath,
						dirName.toString(),
						fileName,
						Base64Util
						.decode(entity.
								materialData.
								replaceAll("\\n", "")));
			} catch (ImageWriteErrorException e) {
				log.warn(e);
			}
			form.materialMap.put(String.valueOf(entity.materialKbn), dto);
		} catch (SNoResultException e) {
			// 主キー検索のため、0件は想定されない。
			log.error(e);
			return;
		}
	}

	/**
	 * インディードタグの形式チェック
	 * @param entity
	 * @param errors
	 * @param dispId
	 */
	private void checktag(TShopList entity, ActionMessages errors, int dispId) {
		// タグのマスターデータを取得
		List<TagListDto> tagList = tagListLogic.shopTagFindAll();
		// 一時保存からタグを取得
		List<String> tags = shopListLogic.getShopTagId(entity.id);

		// 各タグをチェック
		for(String tag : tags){
			// タグが空の場合
			if(StringUtils.isBlank(tag)) {
				errors.add("errors", new ActionMessage("errors.app.integerTypeWithId", dispId, MessageResourcesUtil.getMessage("labels.tag")));
			} else {
				// タグIDがDBに存在しない場合
				if (!tagList.stream().anyMatch(obj -> tag.equals(obj.id))) {
					errors.add("errors", new ActionMessage("errors.app.notExistTag", dispId, MessageResourcesUtil.getMessage("labels.tag")));
				}
			}
		}
	}

}
