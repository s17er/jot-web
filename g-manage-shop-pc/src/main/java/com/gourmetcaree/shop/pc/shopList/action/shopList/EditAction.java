package com.gourmetcaree.shop.pc.shopList.action.shopList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.RequestUtil;

import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.exception.PageNotFoundException;
import com.gourmetcaree.common.property.ShopListProperty;
import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MAreaConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TShopList;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.shop.pc.shopList.dto.shopList.InputViewDto;
import com.gourmetcaree.shop.pc.shopList.form.shopList.EditForm;
import com.gourmetcaree.shop.pc.shopList.form.shopList.ShopListBaseForm.DisplayConditionDto;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;


/**
 * 店舗一覧編集画面
 * @author Takehiro Nakamori
 *
 */
@ManageLoginRequired
public class EditAction extends ShopListBaseAction {

	private static final Logger log = Logger.getLogger(EditAction.class);

	/** ログオブジェクト（なりすまし） */
	private static final Logger sysMasqueradeLog = Logger.getLogger("sysMasqueradeLog");

	@ActionForm
	@Resource
	private EditForm editForm;

	public InputViewDto viewDto;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", urlPattern = "index/{id}", input = TransitionConstants.ShopList.JSP_SPJ05E01)
	public String index() {
		checkArgsNull(NO_BLANK_FLG_NG, editForm.id);
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
		checkUnReadMail();

		return TransitionConstants.ShopList.JSP_SPJ05E01;
	}

	/**
	 * 確認画面
	 * @return
	 */
	@Execute(validator = true, validate="validate" , reset = "resetMultiBox", input = TransitionConstants.ShopList.ACTION_EDIT_BACK)
	public String conf() {
        log.info("-----不具合調査-----");
     // ポストされた値をログ出力
        Map<String,String[]> params = RequestUtil.getRequest().getParameterMap();
        for(String key : params.keySet()) {
        	 String[] vals = params.get(key);
        	 for(String s : vals) {
             	log.info(key + "：" + s);
        	 }
        }

        for(DisplayConditionDto a : editForm.displayConditionDtoList) {
            log.info(a.employPtnKbn + "-" + a.jobKbn);
        }
        for(String b : editForm.employJobKbnList) {
            log.info(b);
        }

        log.info("--------------------");
		checkArgsNull(NO_BLANK_FLG_NG, editForm.id);
		editForm.setProcessFlgOk();

		viewDto = new InputViewDto();
		viewDto.imageList = createImageDtoList(NumberUtils.toInt(editForm.customerId));
		completionChangeCondition();
		checkUnReadMail();

		return TransitionConstants.ShopList.JSP_SPJ05E02;
	}

	/**
	 * インデックスへ遷移
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_SPJ05E01)
	public String backTo() {
		viewDto = new InputViewDto();
		viewDto.imageList = createImageDtoList(NumberUtils.toInt(editForm.customerId));
		checkUnReadMail();

		return TransitionConstants.ShopList.JSP_SPJ05E01;
	}


	/**
	 * 位置情報取得に失敗した場合に呼ばれるメソッド
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_SPJ05E01)
	public String failGeoCoding() {
		editForm.setProcessFlgNg();
		throw new ActionMessagesException("errors.failGeoCoding");
	}

	/**
	 * 修正
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_SPJ05E01)
	public String correct() {
		checkArgsNull(NO_BLANK_FLG_NG, editForm.id);
		editForm.setProcessFlgNg();

		viewDto = new InputViewDto();
		viewDto.imageList = createImageDtoList(NumberUtils.toInt(editForm.customerId));
		checkUnReadMail();

		return TransitionConstants.ShopList.JSP_SPJ05E01;
	}

	/**
	 * 更新処理
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_SPJ05E01)
	public String submit() {
		checkArgsNull(NO_BLANK_FLG_NG, editForm.id);
		update();
		return TransitionConstants.ShopList.REDIRECT_SHOPLIST_EDIT_COMP;
	}

	/**
	 * 更新を行う。
	 */
	private void update() {
		ShopListProperty property = new ShopListProperty();
		TShopList entity = new TShopList();
		editForm.areaCd = String.valueOf(MAreaConstants.AreaCd.SHUTOKEN_AREA);
		property.tShopList = entity;
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
		property.tShopListAttributeList = createShopListAttributeListFromForm(editForm);
        property.tShopChangeJobConditionList = createShopChangeJobConditionList(editForm.displayConditionDtoList);
		shopListLogic.updateAllShopListForCompany(property);

		log.info("店舗一覧をアップデートしました。 TShopList ID=" + property.tShopList.id);

		if (userDto.isMasqueradeFlg()) {
			sysMasqueradeLog.info(String.format("店舗一覧をアップデートしました。TShopList ID： %d, 営業ID：%s, 顧客ID：%s", property.tShopList.id, userDto.masqueradeUserId, userDto.customerId));
		}
	}

	/**
	 * 完了画面
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.ShopList.JSP_SPJ05E01)
	public String comp() {
		checkUnReadMail();
		return TransitionConstants.ShopList.JSP_SPJ05E03;
	}

	/**
	 * 詳細画面へ戻る
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_SPJ05E01)
	public String back() {
		checkArgsNull(NO_BLANK_FLG_NG, editForm.id);
		String backPath = GourmetCareeUtil.makePath(
							TransitionConstants.ShopList.ACTION_SHOPLIST_DETAIL,
							"/index",
							editForm.id,
							TransitionConstants.REDIRECT_STR_NO_SLASH);

		editForm.resetForm();
		return backPath;
	}

	/**
	 * 素材のアップロードを行う。
	 * @return 登録画面
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_SPJ05E01)
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
	@Execute(validator=false, input = TransitionConstants.ShopList.JSP_SPJ05E01)
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
			TShopList entity = shopListService.getData(userDto.getCustomerCd(),
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
		} catch (WNoResultException e) {
			throw new PageNotFoundException();
		}
	}

	/**
	 * JOT-963
	 * 本番環境で稀にdisplayConditionDtoListの雇用形態や職種区分が抜け落ちる(原因不明)
	 * その補完を行う
	 *
	 */
	private void completionChangeCondition() {
		int i = 0;
		for(DisplayConditionDto dto : editForm.displayConditionDtoList) {
			if(StringUtils.isEmpty(dto.employPtnKbn)) {
				editForm.displayConditionDtoList.get(i).employPtnKbn =  editForm.employJobKbnList.get(i).split("-")[0];
			} else if(StringUtils.isEmpty(dto.jobKbn)) {
				editForm.displayConditionDtoList.get(i).jobKbn =  editForm.employJobKbnList.get(i).split("-")[1];
			}
		}
	}

	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.ShopList.JSP_SPJ05E01)
	public String backToList() {
		String backPath = GourmetCareeUtil.makePath(TransitionConstants.ShopList.ACTION_SHOPLIST_LIST, "reShowList");
		return backPath;
	}
}
