package com.gourmetcaree.shop.pc.shopList.action.shopList;

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
import org.seasar.extension.jdbc.exception.SOptimisticLockException;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.util.UUID;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.ActionMessagesUtil;
import org.seasar.struts.util.MessageResourcesUtil;

import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.property.ShopListProperty;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.common.dto.TagListDto;
import com.gourmetcaree.db.common.entity.MShopListTagMapping;
import com.gourmetcaree.db.common.entity.TShopList;
import com.gourmetcaree.db.common.entity.TShopListAttribute;
import com.gourmetcaree.db.common.entity.TShopListLine;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.property.ShopListSearchInputCsvProperty;
import com.gourmetcaree.db.common.service.CityService;
import com.gourmetcaree.db.common.service.PrefecturesService;
import com.gourmetcaree.db.common.service.ShopListLineService;
import com.gourmetcaree.db.common.service.ShopListMaterialNoDataService;
import com.gourmetcaree.db.common.service.ShopListTagMappingService;
import com.gourmetcaree.db.common.service.StationGroupService;
import com.gourmetcaree.shop.pc.shopList.form.shopList.InputCsvForm;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;

/**
 * CSV入力用アクション
 * @author Takehiro Nakamori
 *
 */
@ManageLoginRequired
public class InputCsvAction extends ShopListBaseAction {

	/** ログオブジェクト */
	private static final Logger log = Logger.getLogger(InputCsvAction.class);

	/** ログオブジェクト（なりすまし） */
	private static final Logger sysMasqueradeLog = Logger.getLogger("sysMasqueradeLog");

	/** 店舗情報最大文字数 */
	private static final int SHOP_INFORMATION_MAX_LENGTH = 300;

	/** 業態【HP表示用】最大文字数 */
	private static final int INDUSTRY_TEXT_MAX_LENGTH = 20;

	/** キャッチコピー最大文字数 */
	private static final int CATCHCOPY_MAX_LENGTH = 30;

	/** 店舗一覧同時登録最大数 */
	private static final int MAX_REGISTER_NUM = 100;

	/** 一覧表示最大数の初期値 */
	private static final int DEFALT_MAX_ROW = 10;

    /** オープン日公開期限に文字列を入れた場合の値 */
    private static final long ERROR_OPEN_DATE_LIMIT_DISPLAY_DATE = -32400000;

    /** 区分に文字列を入れた場合の値 */
    private static final int ERROR_INTEGER = -1;


	/** アクションフォーム */
	@ActionForm
	@Resource
	private InputCsvForm inputCsvForm;


	/** 店舗一覧データ無し画像サービス */
	@Resource
	private ShopListMaterialNoDataService shopListMaterialNoDataService;

	/** 都道府県サービス */
	@Resource
	private PrefecturesService prefecturesService;

	/** 市区町村サービス */
	@Resource
	private CityService cityService;

	/** 店舗駅サービス */
	@Resource
	private ShopListLineService shopListLineService;

	/** 店舗タグマッピングサービス */
	@Resource
	private ShopListTagMappingService shopListTagMappingService;

	/** 駅情報サービス */
	@Resource
	private StationGroupService stationGroupService;


	/**
	 * 初期表示メソッド
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.ShopList.JSP_SPJ06L01)
	public String index() {
		ActionMessages errors = new ActionMessages();
		return show(errors);
	}

	/**
	 * ページ変更
	 * @return
	 */
	@Execute(validator = false, reset = "resetErrorFlg", urlPattern = "changePage/{targetPage}", input = TransitionConstants.ShopList.JSP_SPJ06L01)
	public String changePage() {
		ActionMessages errors = new ActionMessages();
		return show(errors);
	}

	/**
	 * 住所取得時に呼び出されるメソッド
	 * @return
	 */
	@Execute(validator = false, reset = "resetErrorFlg", input = TransitionConstants.ShopList.JSP_SPJ06L01)
	public String failGeoCodingList() {
		checkArgsNull(NO_BLANK_FLG_NG, inputCsvForm.failGeoId);
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
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_SPJ06E01)
	public String failGeoCodeingDetail() {
		throw new ActionMessagesException("errors.failGeoCoding");
	}


	/**
	 * 初期表示遷移メソッド
	 * @return
	 */
	private String show(ActionMessages errors) {
		int maxRow = 0;
		if(StringUtils.isEmpty(inputCsvForm.maxRow)) {
			maxRow = DEFALT_MAX_ROW;
		} else {
			maxRow = NumberUtils.toInt(inputCsvForm.maxRow);
		}
		createTempList(errors, maxRow);
		return TransitionConstants.ShopList.JSP_SPJ06L01;
	}

	/**
	 * 編集
	 * @return
	 */
	@Execute(validator = false, reset = "resetDetail", urlPattern = "edit/{id}", input = TransitionConstants.ShopList.JSP_SPJ06L01)
	public String edit() {

		boolean materialExistsFlg = shopListMaterialNoDataService.isMaterialEntityExist(NumberUtils.toInt(inputCsvForm.id), Integer.parseInt(MTypeConstants.ShopListMaterialKbn.MAIN_1));
		Where where = new SimpleWhere()
						.eq("id", inputCsvForm.id)
						.eq("customerId", userDto.getCustomerCd())
						.eq("status", MTypeConstants.ShopListStatus.TEMP_SAVE)
						.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED);
		List<TShopList> entity;
		try {
			entity = shopListService.findByCondition(where);
			Beans.copy(entity.get(0), inputCsvForm).execute();
			initUploadMaterial(inputCsvForm);
			if (inputCsvForm.targetId == 0 || materialExistsFlg) {
				setMaterial(inputCsvForm);
			} else {
				setMaterial(inputCsvForm, inputCsvForm.targetId);
			}

			if(entity.get(0).openDateLimitDisplayDate != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				inputCsvForm.openDateLimitDisplayDate = sdf.format(entity.get(0).openDateLimitDisplayDate);
			}
			createAttributeArrays(entity.get(0).id, inputCsvForm);
			createStationDtoList(entity.get(0).id, inputCsvForm);

		} catch (WNoResultException e) {
			throw new ActionMessagesException("errors.pageNotFoundError");
		}

		inputCsvForm.setProcessFlgNg();
		return TransitionConstants.ShopList.JSP_SPJ06E01;
	}

	/**
	 * 修正画面から確認一覧へ戻る
	 * @return
	 */
	@Execute(validator = false, reset = "resetErrorFlg", input = TransitionConstants.ShopList.JSP_SPJ06L01)
	public String backToList() {
		ActionMessages errors = new ActionMessages();
		return show(errors);
	}

	/**
	 * 確認画面詳細
	 * @return
	 */
	@Execute(validator = true, validate = "validate", reset = "resetMultiBox", input = TransitionConstants.ShopList.JSP_SPJ06E01)
	public String confDetail() {
		inputCsvForm.setProcessFlgOk();
		return TransitionConstants.ShopList.JSP_SPJ06E02;
	}

	/**
	 * 入力訂正
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_SPJ06E01)
	public String correct() {
		inputCsvForm.setProcessFlgNg();
		return TransitionConstants.ShopList.JSP_SPJ06E01;
	}

	/**
	 * 詳細のサブミット
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_SPJ06E01)
	public String submitDetail() {
		if (!inputCsvForm.processFlg) {
			throw new FraudulentProcessException();
		}

		ShopListProperty property = new ShopListProperty();
		TShopList entity = new TShopList();
		Beans.copy(inputCsvForm, entity).excludes("targetId","openDateLimitDisplayDate").execute();
		property.tShopList = entity;
		if(StringUtils.isNotBlank(inputCsvForm.openDateLimitDisplayDate)) {
			property.tShopList.openDateLimitDisplayDate = Date.valueOf(inputCsvForm.openDateLimitDisplayDate);
		}
		setMaterialProperty(property, inputCsvForm);
		property.tShopListLineList = createShopListLineList(inputCsvForm.stationDtoList);
		property.tShopListAttributeList = createShopListAttributeListFromForm(inputCsvForm);
		shopListLogic.updateShopListForCompany(property);
		inputCsvForm.resetDetail();

		log.info("店舗一覧(CSVインポート)の一件を更新しました。");

		if (userDto.isMasqueradeFlg()) {
			sysMasqueradeLog.info(String.format("店舗一覧(CSVインポート)の一件を更新しました。営業ID：%s, 顧客ID：%s", userDto.masqueradeUserId, userDto.customerId));
		}

		return TransitionConstants.ShopList.REDIRECT_SHOPLIST_CSV_BACK_TO_LIST;

	}


	/**
	 * 素材のアップロードを行う。
	 * @return 登録画面
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_SPJ06E01)
	public String upMaterial() {

		// パラメータが空の場合はエラー
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, inputCsvForm.hiddenMaterialKbn);

		// 画像のアップロード処理
		doUploadMaterial(inputCsvForm);

		return TransitionConstants.ShopList.JSP_SPJ06E01;
	}

	/**
	 * アップロードされた素材の削除処理
	 * @return 登録画面
	 */
	@Execute(validator=false, input = TransitionConstants.ShopList.JSP_SPJ06E01)
	public String deleteMaterial() {

		// パラメータが空の場合はエラー
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, inputCsvForm.hiddenMaterialKbn);

		// 画像の削除処理
		doDeleteMaterial(inputCsvForm);

		return TransitionConstants.ShopList.JSP_SPJ06E01;
	}

	/**
	 * 表示変数変更
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_SPJ06L01)
	public String changeMaxRow() {
		ActionMessages errors = new ActionMessages();
		createTempList(errors, NumberUtils.toInt(inputCsvForm.maxRow));
		return TransitionConstants.ShopList.JSP_SPJ06L01;
	}

	/**
	 * 一時保存リスト作成
	 */
	private void createTempList(ActionMessages errors, int maxRow) {
		List<Integer> idList = getShopListTempIdListFromSession();

		if (CollectionUtils.isEmpty(idList)) {
			inputCsvForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.dataNotFound");
		}

		try {
			ShopListSearchInputCsvProperty property = createSearchProperty(maxRow);
			shopListService.searchInputCsvList(property);

			inputCsvForm.pageNavi = property.pageNavi;

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

				String detailPath = GourmetCareeUtil.makePath(TransitionConstants.ShopList.ACTION_SHOPLIST_INPUTCSV, "/edit", String.valueOf(entity.id));
//				map.put("shopList", dto);
				map.put("id", entity.id);
				map.put("targetId", entity.targetId);
				if (entity.targetId != null) {
					if (!shopListService.isOwnData(entity.targetId, userDto.getCustomerCd())) {
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
				if (entity.jobOfferFlg != null) {
					map.put("jobOfferFlgName", valueToNameConvertLogic.convertToTypeName(MTypeConstants.JobOfferFlg.TYPE_CD, entity.jobOfferFlg));
				} else {
					map.put("jobOfferFlgName", "");
				}

				map.put("areaCd", entity.areaCd);
				map.put("latLngKbn", entity.latLngKbn);
				map.put("shopName", entity.shopName);
				map.put("detailPath", detailPath);
				map.put("latitude", entity.latitude);
				map.put("longitude", entity.longitude);
				map.put("address", entity.address);
				map.put("address1", entity.address1);
				map.put("prefecturesCd", entity.prefecturesCd);
				map.put("cityCd", entity.cityCd);
				map.put("foreignAddress", entity.foreignAddress);
				map.put("shutokenForeignAreaKbn", entity.shutokenForeignAreaKbn);
				map.put("domesticKbn", entity.domesticKbn);
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

		// 電話・FAX番号
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
	 * 入力された都道府県などから住所を取得する
	 * @param entity
	 * @param map
	 */
	public void getAddress1(TShopList entity, Map<String, Object> map) {
		if(entity.domesticKbn == MTypeConstants.DomesticKbn.DOMESTIC) {
			if(entity.prefecturesCd != null && entity.cityCd != null) {
				String prefName = prefecturesService.getName(entity.prefecturesCd);
				String cityName = cityService.getName(entity.cityCd);

				String address1 = prefName + cityName + "" + entity.address;
				entity.address1 = address1;

				map.put("address1", address1);
			}
		}
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
					CATCHCOPY_MAX_LENGTH));
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
					INDUSTRY_TEXT_MAX_LENGTH));
		}
	}

	/**
	 * 登録
	 * @return
	 */
	@Execute(validator=false, reset="resetCheckBox", validate = "validateSaveIdList", input = TransitionConstants.ShopList.JSP_SPJ06L01)
	public String submit() {
		Set<Integer> updateIdSet = new HashSet<Integer>();
		for(Map<String, Object> map : inputCsvForm.csvMapList) {
			// 保存するIDリストに含まれていなければコンティニュー
			if (!ArrayUtils.contains(inputCsvForm.saveIdList, String.valueOf(map.get("id")))) {
				continue;
			}

			int id = NumberUtils.toInt(String.valueOf(map.get("id")));
			boolean updateFlg;
			TShopList targetEntity = new TShopList();

			if (map.get("targetId") != null) {
				updateFlg = true;
			} else {
				updateFlg = false;
			}

			try {
				TShopList tempEntity = shopListService.getData(userDto.getCustomerCd(),
																id,
																MTypeConstants.ShopListStatus.TEMP_SAVE);

				List<TShopListAttribute> tempAttributeList = shopListAttributeService.findByShopListId(id);

				List<TShopListLine> tempLineList = shopListLineService.findByShopListId(id);

				List<MShopListTagMapping> tempTagList = shopListTagMappingService.findByShopListId(id);

				if (updateFlg) {
					try {
						int targetId = NumberUtils.toInt(String.valueOf(map.get("targetId")));
						targetEntity = shopListService.getData(userDto.getCustomerCd(),
								targetId,
								MTypeConstants.ShopListStatus.REGISTERED);
						shopListMaterialService.moveMaterial(id, NumberUtils.toInt(String.valueOf(map.get("targetId"))));
						Beans.copy(tempEntity, targetEntity).excludes("id", "targetId", "version").execute();
					} catch (WNoResultException e) {
						throw new FraudulentProcessException();
					}
				}
				if (updateFlg) {
					targetEntity.latitude = NumberUtils.toDouble(String.valueOf(map.get("latitude")));
					targetEntity.longitude = NumberUtils.toDouble(String.valueOf(map.get("longitude")));
					targetEntity.status = MTypeConstants.ShopListStatus.REGISTERED;
					shopListService.updateForImport(targetEntity);
					shopListService.delete(tempEntity);

					int targetId = NumberUtils.toInt(String.valueOf(map.get("targetId")));

					shopListAttributeService.deleteByShopListId(targetId);
					for(TShopListAttribute tempAttribute : tempAttributeList) {
						tempAttribute.shopListId = targetId;
						shopListAttributeService.update(tempAttribute);
					}
					shopListLineService.deleteByShopListId(targetId);
					for(TShopListLine tempLine : tempLineList) {
						tempLine.shopListId = targetId;
						shopListLineService.update(tempLine);
					}

					// インディードタグの削除・更新
					shopListTagMappingService.deleteByShopListId(targetId);
					if (!CollectionUtils.isEmpty(tempTagList)) {
						for(MShopListTagMapping tempTag : tempTagList) {
							tempTag.shopListId = targetId;
							shopListTagMappingService.update(tempTag);
						}
					}
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
					shopListLogic.updateKeywordSearch(id);
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

		log.info("店舗一覧を一括登録しました。" + userDto);

		if (userDto.isMasqueradeFlg()) {
			sysMasqueradeLog.info(String.format("店舗一覧を一括登録しました。営業ID：%s, 顧客ID：%s", userDto.masqueradeUserId, userDto.customerId));
		}

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
	@Execute(validator = false, reset="resetCheckBox", validate = "validateSaveIdList", input=TransitionConstants.ShopList.JSP_SPJ06L01)
	public String delete() {
		List<Integer> idList = getShopListTempIdListFromSession();
		for (String id : inputCsvForm.saveIdList) {
			int targetId = NumberUtils.toInt(id);
			deleteTempData(targetId);
			idList.remove((Integer) targetId);

		}
		inputCsvForm.idList = idList;
		return createRedirectToIndexStr();
	}

	/**
	 * 完了画面
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.ShopList.JSP_SPJ06C03)
	public String comp() {
		session.removeAttribute(SHOP_LIST_TEMP_ID_LIST_SESSION_KEY);
		return TransitionConstants.ShopList.JSP_SPJ06C03;
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
			TShopList entity = shopListService.getData(userDto.getCustomerCd(), shopListId, MTypeConstants.ShopListStatus.TEMP_SAVE);
			shopListService.delete(entity);
			shopListLineService.deleteByShopListId(shopListId);
			// 店舗情報タグの一時保存情報を削除
			shopListTagMappingService.deleteByShopListId(shopListId);
		} catch (WNoResultException e) {
			log.warn("一時保存店舗一覧の取得に失敗しました。 ID=" + shopListId);

			if (userDto.isMasqueradeFlg()) {
				sysMasqueradeLog.warn(String.format("一時保存店舗一覧の取得に失敗しました。ID：%d, 営業ID：%s, 顧客ID：%s", shopListId, userDto.masqueradeUserId, userDto.customerId));
			}
		}
	}

	/**
	 * インデックスへのリダイレクト
	 * @return
	 */
	private String createRedirectToIndexStr() {
		return GourmetCareeUtil.makePath(TransitionConstants.ShopList.ACTION_SHOPLIST_INPUTCSV,
				"/index",
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
