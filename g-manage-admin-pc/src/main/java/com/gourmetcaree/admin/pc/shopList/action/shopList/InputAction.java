package com.gourmetcaree.admin.pc.shopList.action.shopList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.shopList.dto.shopList.InputViewDto;
import com.gourmetcaree.admin.pc.shopList.form.shopList.InputForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.exception.PageNotFoundException;
import com.gourmetcaree.common.property.ShopListProperty;
import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.db.common.constants.MAreaConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TShopList;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * 店舗一覧登録アクション
 * @author Takehiro Nakamori
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN, ManageAuthLevel.STAFF, ManageAuthLevel.AGENCY, ManageAuthLevel.OTHER, ManageAuthLevel.SALES})
public class InputAction extends ShopListBaseAction {

	/** ログオブジェクト */
	private static final Logger log = Logger.getLogger(InputAction.class);

	/** アクションフォーム */
	@Resource
	@ActionForm
	private InputForm inputForm;

	public InputViewDto viewDto;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", urlPattern = "index/{customerId}", input = TransitionConstants.ShopList.JSP_APQ01C01)
	@MethodAccess(accessCode="SHOPLIST_INPUT_INDEX")
	public String index() {
		checkArgsNull(NO_BLANK_FLG_NG, inputForm.customerId);
		return show();
	}

	/**
	 * 初期表示遷移用
	 * @return
	 */
	private String show() {
		inputForm.areaCd = String.valueOf(MAreaConstants.AreaCd.SHUTOKEN_AREA);
		inputForm.latLngKbn = String.valueOf(MTypeConstants.ShopListLatLngKbn.ADDRESS);
		// タグをセット
		inputForm.tagListDto = tagListLogic.shopTagFindAll();
		inputForm.preventSmoke = "屋内原則禁煙";

		viewDto = new InputViewDto();
		viewDto.imageList = createImageDtoList(NumberUtils.toInt(inputForm.customerId));

		return TransitionConstants.ShopList.JSP_APQ01C01;
	}

	/**
	 * 確認画面
	 * @return
	 */
	@Execute(validator = true, validate = "validate", reset="resetCheckBox"
			, input = TransitionConstants.ShopList.ACTION_INPUT_BACK)
	@MethodAccess(accessCode="SHOPLIST_INPUT_CONF")
	public String conf() {
		inputForm.setProcessFlgOk();
		checkArgsNull(NO_BLANK_FLG_NG, inputForm.customerId);

		viewDto = new InputViewDto();
		viewDto.imageList = createImageDtoList(NumberUtils.toInt(inputForm.customerId));

		return TransitionConstants.ShopList.JSP_APQ01C02;
	}

	/**
	 * インデックスへ遷移
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_APQ01C01)
	@MethodAccess(accessCode="SHOPLIST_INPUT_BACKINDEX")
	public String back() {
		viewDto = new InputViewDto();
		viewDto.imageList = createImageDtoList(NumberUtils.toInt(inputForm.customerId));

		return TransitionConstants.ShopList.JSP_APQ01C01;
	}


	/**
	 * 登録処理
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_APQ01C01)
	@MethodAccess(accessCode="SHOPLIST_INPUT_SUBMIT")
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
	@Execute(validator = false, reset="resetFormWithoutCustomerId", input = TransitionConstants.ShopList.JSP_APQ01C01)
	@MethodAccess(accessCode="SHOPLIST_INPUT_COMP")
	public String comp() {
		return TransitionConstants.ShopList.JSP_APQ01C03;
	}

	/**
	 * 訂正して入力画面へ戻る。
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_APQ01C01)
	@MethodAccess(accessCode="SHOPLIST_INPUT_CORRECT")
	public String correct() {
		checkArgsNull(NO_BLANK_FLG_NG, inputForm.customerId);
		inputForm.setProcessFlgNg();

		viewDto = new InputViewDto();
		viewDto.imageList = createImageDtoList(NumberUtils.toInt(inputForm.customerId));

		return TransitionConstants.ShopList.JSP_APQ01C01;
	}

	/**
	 * 位置情報取得に失敗した場合に呼ばれるメソッド
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_APQ01C01)
	@MethodAccess(accessCode="SHOPLIST_INPUT_FAILGEO")
	public String failGeoCoding() {
		checkArgsNull(NO_BLANK_FLG_NG, inputForm.customerId);
		inputForm.setProcessFlgNg();
		throw new ActionMessagesException("errors.failGeoCoding");
	}

	/**
	 * セッションを破棄してインデックスへ遷移
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_APQ01C01)
	@MethodAccess(accessCode="SHOPLIST_INPUT_BACKINDEX")
	public String backToIndex() {
		checkArgsNull(NO_BLANK_FLG_NG, inputForm.customerId);
		String indexPath = createReindexPath(inputForm.customerId);
		inputForm.resetForm();
		return indexPath;
	}

	/**
	 * 素材の一時アップロードを行う。
	 * @return 登録画面
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_APQ01C01)
	@MethodAccess(accessCode="SHOPLIST_INPUT_UPMATERIAL")
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
	@Execute(validator=false, input = TransitionConstants.ShopList.JSP_APQ01C01)
	@MethodAccess(accessCode="SHOPLIST_INPUT_DELMATERIAL")
	public String delAjaxMaterial() {

		// パラメータが空の場合はエラー
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, inputForm.hiddenMaterialKbn);

		// 画像の削除処理
		deleteTempImageJsonResponse(inputForm);
		return null;
	}


	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.ShopList.JSP_APQ01C01)
	@MethodAccess(accessCode="SHOPLIST_INPUT_INDEX")
	public String copy() {

		if(!inputForm.processFlg) {
			throw new FraudulentProcessException();
		}

		initUploadMaterial(inputForm);
		createShopListData();

		viewDto = new InputViewDto();
		viewDto.imageList = createImageDtoList(NumberUtils.toInt(inputForm.customerId));

		return TransitionConstants.ShopList.JSP_APQ01C01;
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
		// タグの選択肢を取得
		inputForm.tagListDto = tagListLogic.shopTagFindAll();
		super.getShopListTag(entity.id, inputForm);
		} catch (WNoResultException e) {
			throw new PageNotFoundException();
		}
	}


	/**
	 * インサート
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
		entity.status = MTypeConstants.ShopListStatus.REGISTERED;
		property.tShopList = entity;
		property.tShopListAttributeList = createShopListAttributeListFromForm(inputForm);
		property.tShopListLineList = createShopListLineList(inputForm.stationDtoList);
		property.tShopChangeJobConditionList = createShopChangeJobConditionList(inputForm.displayConditionDtoList);

		setMaterialProperty(property, inputForm);
//		shopListLogic.insertShopList(property);


		shopListLogic.insertAllShopList(property);

		List<String> tagList = new ArrayList<>();
		if (StringUtils.isNotBlank(inputForm.tagList)) {
			tagList = Arrays.asList(inputForm.tagList.split(","));
		}
		tagListLogic.insertShopList(tagList, property.tShopList.id);

		StringBuilder logSb = new StringBuilder("");
		logSb.append("店舗一覧を登録しました。");
		logSb.append("顧客ID : ");
		logSb.append(inputForm.customerId);
		logSb.append(" 店舗一覧ID: ");
		logSb.append(property.tShopList.id);
		logSb.append("  登録者：");
		logSb.append(userDto);

		log.info(logSb.toString());

	}
}
