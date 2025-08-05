package com.gourmetcaree.shop.pc.shopList.action.shopList;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.exception.PageNotFoundException;
import com.gourmetcaree.common.property.ShopListProperty;
import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.db.common.constants.MAreaConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants.JobOfferFlg;
import com.gourmetcaree.db.common.entity.TShopList;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.shop.pc.shopList.dto.shopList.InputViewDto;
import com.gourmetcaree.shop.pc.shopList.form.shopList.InputForm;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;

/**
 * 店舗一覧登録アクション
 * @author Takehiro Nakamori
 *
 */
@ManageLoginRequired
public class InputAction extends ShopListBaseAction {

	/** ログ */
	private static final Logger log = Logger.getLogger(InputAction.class);

	/** ログオブジェクト（なりすまし） */
	private static final Logger sysMasqueradeLog = Logger.getLogger("sysMasqueradeLog");

	/** アクションフォーム */
	@Resource
	@ActionForm
	private InputForm inputForm;

	public InputViewDto viewDto;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.ShopList.JSP_SPJ02C01)
	public String index() {
		return show();
	}

	/**
	 * 初期表示遷移用
	 * @return
	 */
	private String show() {
		inputForm.areaCd = String.valueOf(MAreaConstants.AreaCd.SHUTOKEN_AREA);
		inputForm.latLngKbn = String.valueOf(MTypeConstants.ShopListLatLngKbn.ADDRESS);
		inputForm.jobOfferFlg = String.valueOf(JobOfferFlg.NASHI);
		inputForm.preventSmoke = "屋内原則禁煙";

		viewDto = new InputViewDto();
		viewDto.imageList = createImageDtoList(userDto.customerId);
		checkUnReadMail();

		return TransitionConstants.ShopList.JSP_SPJ02C01;
	}

	/**
	 * 確認画面
	 */
	@Execute(validator = true, validate = "validate", reset = "resetMultiBox", input = TransitionConstants.ShopList.ACTION_INPUT_BACK)
	public String conf() {
		inputForm.setProcessFlgOk();

		viewDto = new InputViewDto();
		viewDto.imageList = createImageDtoList(userDto.customerId);
		checkUnReadMail();

		return TransitionConstants.ShopList.JSP_SPJ02C02;
	}

	/**
	 * インデックスへ遷移
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_SPJ02C01)
	public String back() {
		viewDto = new InputViewDto();
		viewDto.imageList = createImageDtoList(userDto.customerId);
		checkUnReadMail();

		return TransitionConstants.ShopList.JSP_SPJ02C01;
	}

	/**
	 * 訂正して入力画面へ戻る。
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_SPJ02C01)
	public String correct() {
		inputForm.setProcessFlgNg();

		viewDto = new InputViewDto();
		viewDto.imageList = createImageDtoList(userDto.customerId);
		checkUnReadMail();

		return TransitionConstants.ShopList.JSP_SPJ02C01;
	}

	/**
	 * 位置情報取得に失敗した場合に呼ばれるメソッド
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_SPJ02C01)
	public String failGeoCoding() {
		inputForm.setProcessFlgNg();
		throw new ActionMessagesException("errors.failGeoCoding");
	}

	/**
	 * 登録処理
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_SPJ02C01)
	public String submit() {
		if (!inputForm.processFlg) {
			throw new FraudulentProcessException();
		}
		insert();
		return TransitionConstants.ShopList.REDIRECT_SHOPLIST_INPUT_COMP;
	}


	/**
	 * 登録完了画面
	 * @return
	 */
	@Execute(validator = false, removeActionForm = true, input = TransitionConstants.ShopList.JSP_SPJ02C01)
	public String comp() {
		checkUnReadMail();
		return TransitionConstants.ShopList.JSP_SPJ02C03;
	}

	/**
	 * 素材のアップロードを行う。
	 * @return 登録画面
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_SPJ02C01)
	public String upAjaxMaterial() {

		// パラメータが空の場合はエラー
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, inputForm.hiddenMaterialKbn);

		// 画像のアップロード処理
		uploadTempImageJsonResponse(inputForm);
		return null;
	}

	/**
	 * アップロードされた素材の削除処理
	 * @return 登録画面
	 */
	@Execute(validator=false, input = TransitionConstants.ShopList.JSP_SPJ02C01)
	public String delAjaxMaterial() {

		// パラメータが空の場合はエラー
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, inputForm.hiddenMaterialKbn);

		// 画像の削除処理
		deleteTempImageJsonResponse(inputForm);
		return null;
	}

	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.ShopList.JSP_SPJ02C01)
	public String copy() {

		if(!inputForm.processFlg) {
			throw new FraudulentProcessException();
		}

		initUploadMaterial(inputForm);
		createShopListData();

		viewDto = new InputViewDto();
		viewDto.imageList = createImageDtoList(NumberUtils.toInt(inputForm.customerId));
		checkUnReadMail();

		return TransitionConstants.ShopList.JSP_SPJ02C01;
	}

	/**
	 * 店舗一覧データの作成
	 */
	private void createShopListData() {
		try {
		TShopList entity = shopListService.getData(NumberUtils.toInt(inputForm.customerId),
				NumberUtils.toInt(inputForm.id),
				MTypeConstants.ShopListStatus.REGISTERED);
		Beans.copy(entity, inputForm).excludes("openDateLimitDisplayDate").execute();
		if(entity.openDateLimitDisplayDate != null) {
			inputForm.openDateLimitDisplayDate = new SimpleDateFormat("yyyy-MM-dd").format(entity.openDateLimitDisplayDate);
		}
		setMaterial(inputForm);
		createAttributeArrays(entity.id, inputForm);
		createStationDtoList(entity.id, inputForm);
        createConditionDtoList(entity.id, inputForm);
		} catch (WNoResultException e) {
			throw new PageNotFoundException();
		}
	}

	/**
	 * インサート処理
	 */
	private void insert() {
		ShopListProperty property = new ShopListProperty();
		TShopList entity = new TShopList();
		Beans.copy(inputForm, entity).excludes("arbeitJobType", "openDateLimitDisplayDate", "seating", "unitPrice").execute();
		try {
			if(!StringUtils.isBlank(inputForm.openDateLimitDisplayDate)) {
				entity.openDateLimitDisplayDate = DateUtils.convertStrToSqlDate(inputForm.openDateLimitDisplayDate, "yyyy-MM-dd");
			}
			entity.seating = valueToNameConvertLogic.convertToTypeName(MTypeConstants.SeatKbn.TYPE_CD, inputForm.seatKbn);
			entity.unitPrice  = valueToNameConvertLogic.convertToTypeName(MTypeConstants.SalesPerCustomerKbn.TYPE_CD, inputForm.salesPerCustomerKbn);
			if(Integer.parseInt(inputForm.domesticKbn) == MTypeConstants.DomesticKbn.DOMESTIC) {
				entity.address1 = valueToNameConvertLogic.convertToPrefecturesName(Integer.parseInt(inputForm.prefecturesCd))
									+ valueToNameConvertLogic.convertToCityName(inputForm.cityCd)
									+ inputForm.address;
			}else{
				entity.address1 = inputForm.foreignAddress;
			}
		} catch (ParseException e) {
			throw new FraudulentProcessException("公開期限の値が不正です", e);
		}
		entity.jobOfferFlg = MTypeConstants.JobOfferFlg.NASHI;
		entity.customerId = userDto.getCustomerCd();
		entity.status = MTypeConstants.ShopListStatus.REGISTERED;
		entity.areaCd = MAreaConstants.AreaCd.SHUTOKEN_AREA;
		property.tShopListAttributeList = createShopListAttributeListFromForm(inputForm);
		property.tShopListLineList = createShopListLineList(inputForm.stationDtoList);
		property.tShopList = entity;
        property.tShopChangeJobConditionList = createShopChangeJobConditionList(inputForm.displayConditionDtoList);
		setMaterialProperty(property, inputForm);
		shopListLogic.insertAllShopList(property);

		log.info("店舗一覧を登録しました。 ID = " + property.tShopList.id);

		if (userDto.isMasqueradeFlg()) {
			sysMasqueradeLog.info(String.format("店舗一覧を登録しました。ID：%d, 営業ID：%s, 顧客ID：%s", property.tShopList.id, userDto.masqueradeUserId, userDto.customerId));
		}
	}
}
