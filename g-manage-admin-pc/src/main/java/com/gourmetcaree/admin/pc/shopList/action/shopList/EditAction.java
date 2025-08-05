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
import com.gourmetcaree.admin.pc.shopList.form.shopList.EditForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.exception.PageNotFoundException;
import com.gourmetcaree.common.property.ShopListProperty;
import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MAreaConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TShopList;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * 店舗一覧編集画面
 * @author Takehiro Nakamori
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN, ManageAuthLevel.STAFF, ManageAuthLevel.AGENCY, ManageAuthLevel.OTHER, ManageAuthLevel.SALES})
public class EditAction extends ShopListBaseAction {

	private static final Logger log = Logger.getLogger(EditAction.class);

	@ActionForm
	@Resource
	private EditForm editForm;

	public InputViewDto viewDto;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", urlPattern = "index/{customerId}/{id}", input = TransitionConstants.ShopList.JSP_APQ04E01)
	@MethodAccess(accessCode="SHOPLIST_EDIT_INDEX")
	public String index() {
		checkArgsNull(NO_BLANK_FLG_NG, editForm.customerId, editForm.id);
		return show();
	}

	/**
	 * 初期表示遷移
	 * @return
	 */
	private String show() {
		initUploadMaterial(editForm);
		createShopListData();

		viewDto = new InputViewDto();
		viewDto.imageList = createImageDtoList(NumberUtils.toInt(editForm.customerId));

		return TransitionConstants.ShopList.JSP_APQ04E01;
	}

	/**
	 * 確認画面
	 * @return
	 */
	@Execute(validator = true, validate="validate", reset="resetCheckBox", input = TransitionConstants.ShopList.ACTION_EDIT_BACK)
	@MethodAccess(accessCode="SHOPLIST_EDIT_CONF")
	public String conf() {
		checkArgsNull(NO_BLANK_FLG_NG, editForm.customerId, editForm.id);
		editForm.setProcessFlgOk();

		viewDto = new InputViewDto();
		viewDto.imageList = createImageDtoList(NumberUtils.toInt(editForm.customerId));

		return TransitionConstants.ShopList.JSP_APQ04E02;
	}

	/**
	 * インデックスへ遷移
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_APQ04E01)
	@MethodAccess(accessCode="SHOPLIST_INPUT_BACKINDEX")
	public String backTo() {
		viewDto = new InputViewDto();
		viewDto.imageList = createImageDtoList(NumberUtils.toInt(editForm.customerId));

		return TransitionConstants.ShopList.JSP_APQ04E01;
	}

	/**
	 * 位置情報取得に失敗した場合に呼ばれるメソッド
	 * @return
	 */
	@MethodAccess(accessCode="SHOPLIST_EDIT_FAILGEO")
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_APQ04E01)
	public String failGeoCoding() {
		checkArgsNull(NO_BLANK_FLG_NG, editForm.customerId);
		editForm.setProcessFlgNg();
		throw new ActionMessagesException("errors.failGeoCoding");
	}

	/**
	 * 修正
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_APQ04E01)
	@MethodAccess(accessCode="SHOPLIST_EDIT_CORRECT")
	public String correct() {
		checkArgsNull(NO_BLANK_FLG_NG, editForm.customerId, editForm.id);
		editForm.setProcessFlgNg();

		viewDto = new InputViewDto();
		viewDto.imageList = createImageDtoList(NumberUtils.toInt(editForm.customerId));

		return TransitionConstants.ShopList.JSP_APQ04E01;
	}

	/**
	 * 更新処理
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_APQ04E01)
	@MethodAccess(accessCode="SHOPLIST_EDIT_SUBMIT")
	public String submit() {
		checkArgsNull(NO_BLANK_FLG_NG, editForm.customerId, editForm.id);
		update();
		return TransitionConstants.ShopList.REDIRECT_SHOPLIST_EDIT_COMP;
	}

	/**
	 * 更新を行う。
	 */
	private void update() {
		ShopListProperty property = new ShopListProperty();
		TShopList entity = new TShopList();
		property.tShopList = entity;
		editForm.areaCd = String.valueOf(MAreaConstants.AreaCd.SHUTOKEN_AREA);
		Beans.copy(editForm, entity).excludes("openDateLimitDisplayDate", "seating", "unitPrice").execute();
		try {
			if(!StringUtils.isBlank(editForm.openDateLimitDisplayDate)) {
				entity.openDateLimitDisplayDate = DateUtils.convertStrToSqlDate(editForm.openDateLimitDisplayDate, "yyyy-MM-dd");
			}
			entity.seating = valueToNameConvertLogic.convertToTypeName(MTypeConstants.SeatKbn.TYPE_CD, editForm.seatKbn);
			entity.unitPrice  = valueToNameConvertLogic.convertToTypeName(MTypeConstants.SalesPerCustomerKbn.TYPE_CD, editForm.salesPerCustomerKbn);

			if(Integer.parseInt(editForm.domesticKbn) == MTypeConstants.DomesticKbn.DOMESTIC) {
				entity.address1 = valueToNameConvertLogic.convertToPrefecturesName(Integer.parseInt(editForm.prefecturesCd))
									+ valueToNameConvertLogic.convertToCityName(editForm.cityCd)
									+ editForm.address;
			}else{
				entity.address1 = editForm.foreignAddress;
			}
		} catch (ParseException e) {
			throw new FraudulentProcessException("公開期限の値が不正です", e);
		}
		setMaterialProperty(property, editForm);
		property.tShopListLineList = createShopListLineList(editForm.stationDtoList);
		property.tShopChangeJobConditionList = createShopChangeJobConditionList(editForm.displayConditionDtoList);
		property.tShopListAttributeList = createShopListAttributeListFromForm(editForm);
		shopListLogic.updateAllShopList(property);

		List<String> tagList = new ArrayList<>();
		if (StringUtils.isNotBlank(editForm.tagList)) {
			tagList = Arrays.asList(editForm.tagList.split(","));
		}
		tagListLogic.update(tagList, Integer.parseInt(editForm.id));

		StringBuilder logSb = new StringBuilder("");
		logSb.append("店舗一覧を変更しました。");
		logSb.append("顧客ID : ");
		logSb.append(editForm.customerId);
		logSb.append(" 店舗一覧ID: ");
		logSb.append(property.tShopList.id);
		logSb.append("  登録者：");
		logSb.append(userDto);

		log.info(logSb.toString());
	}

	/**
	 * 完了画面
	 * @return
	 */
	@Execute(validator = false, reset = "resetFormWithoutCustomerId", input = TransitionConstants.ShopList.JSP_APQ04E01)
	@MethodAccess(accessCode="SHOPLIST_EDIT_COMP")
	public String comp() {
		return TransitionConstants.ShopList.JSP_APQ04E03;
	}

	/**
	 * 詳細画面へ戻る
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_APQ04E01)
	@MethodAccess(accessCode="SHOPLIST_EDIT_BACK")
	public String back() {
		checkArgsNull(NO_BLANK_FLG_NG, editForm.customerId, editForm.id);
		String backPath = GourmetCareeUtil.makePath(
							TransitionConstants.ShopList.ACTION_SHOPLIST_DETAIL,
							"/index",
							editForm.customerId,
							editForm.id,
							TransitionConstants.REDIRECT_STR);

		editForm.resetForm();
		return backPath;
	}

	/**
	 * 素材のアップロードを行う。
	 * @return 登録画面
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_APQ04E01)
	@MethodAccess(accessCode="SHOPLIST_EDIT_UPMATERIAL")
	public String upAjaxMaterial() {

		// パラメータが空の場合はエラー
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, editForm.hiddenMaterialKbn);

		// 画像のアップロード処理
		uploadTempImageJsonResponse(editForm);
		return null;
	}

	/**
	 * アップロードされた素材の削除処理
	 * @return 登録画面
	 */
	@Execute(validator=false, input = TransitionConstants.ShopList.JSP_APQ04E01)
	@MethodAccess(accessCode="SHOPLIST_EDIT_DELMATERIAL")
	public String delAjaxMaterial() {

		// パラメータが空の場合はエラー
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, editForm.hiddenMaterialKbn);

		// 画像の削除処理
		deleteTempImageJsonResponse(editForm);
		return null;
	}


	/**
	 * 店舗一覧データの作成
	 */
	private void createShopListData() {
		try {
			TShopList entity = shopListService.getData(NumberUtils.toInt(editForm.customerId),
													NumberUtils.toInt(editForm.id),
													MTypeConstants.ShopListStatus.REGISTERED);
			Beans.copy(entity, editForm).excludes("openDateLimitDisplayDate").execute();
			if(entity.openDateLimitDisplayDate != null) {
				editForm.openDateLimitDisplayDate = new SimpleDateFormat("yyyy-MM-dd").format(entity.openDateLimitDisplayDate);
			}
			setMaterial(editForm);
			createAttributeArrays(entity.id, editForm);
			createStationDtoList(entity.id, editForm);
			createConditionDtoList(entity.id, editForm);

			// タグをセット
			editForm.tagListDto = tagListLogic.shopTagFindAll();
			super.getShopListTag(entity.id, editForm);
		} catch (WNoResultException e) {
			throw new PageNotFoundException();
		}
	}

	/**
	 * インデックスへ遷移
	 * @return
	 */
	@Execute(validator = false, reset = "resetFormWithoutCustomerId", input = TransitionConstants.ShopList.JSP_APQ03R01)
	@MethodAccess(accessCode="SHOPLIST_EDIT_BACKINDEX")
	public String backToIndex() {
		checkArgsNull(NO_BLANK_FLG_NG, editForm.customerId);
		String backPath = createReindexPath(editForm.customerId);
		editForm.customerId = null;
		return backPath;
	}

	@Execute(validator = false, reset = "resetFormWithoutCustomerId", input = TransitionConstants.ShopList.JSP_APQ03R01)
	public String backToList() {
		checkArgsNull(NO_BLANK_FLG_NG, editForm.customerId);
		String backPath = GourmetCareeUtil.makePath(TransitionConstants.ShopList.ACTION_SHOPLIST_LIST, "reShowList", "");
		editForm.customerId = null;
		return backPath;
	}
}
