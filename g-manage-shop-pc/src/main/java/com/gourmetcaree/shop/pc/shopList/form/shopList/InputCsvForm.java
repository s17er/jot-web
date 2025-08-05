package com.gourmetcaree.shop.pc.shopList.form.shopList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.util.MessageResourcesUtil;

import com.gourmetcaree.common.dto.ShopListMaterialDto;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;

@Component(instance=InstanceType.SESSION)
public class InputCsvForm extends ShopListBaseForm implements Serializable  {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -9141456865764811945L;


	/** チェックしたID */
	public String[] saveIdList = new String[0];

	/** インポートした数 */
	public int importNum;

	/** ターゲットID */
	public int targetId;

	/** バージョン */
	public Long version;

	/** CSVの情報を記載したMapリスト */
	public List<Map<String, Object>> csvMapList;

	/** 仮登録されたIDのリスト */
	public List<Integer> idList;

	/** ジオコーディングに失敗したID文字列 */
	public String failGeoId;

	/** エラーがあるかどうか */
	public boolean errorFlg;

	/** ジオコーディングエラーフラグ */
	public boolean geoErrorFlg;

	/** ターゲットページ */
	public String targetPage;

	/** ページナビ */
	public PageNavigateHelper pageNavi;

	/** 最大表示件数 */
	public String maxRow;

	/**
	 * フォームのリセット
	 */
	@Override
	public void resetForm() {
		super.resetForm();
		id = null;
		saveIdList = new String[0];
		importNum = 0;
		version = null;
		csvMapList = null;
		idList = null;
		failGeoId = null;
		errorFlg = false;
		geoErrorFlg = false;
		pageNavi = null;
		targetPage = null;
		maxRow = null;
	}

	/**
	 * チェックボックスのリセット
	 */
	public void resetCheckBox() {
		saveIdList = new String[0];
	}

	/**
	 * 詳細用リセット
	 */
	public void resetDetail() {
		areaCd = null;
		shopName = null;
		industryKbn1 = null;
		industryKbn2 = null;
		address1 = null;
		address2 = null;
		latitude = null;
		longitude = null;
		phoneNo1 = null;
		phoneNo2 = null;
		phoneNo3 = null;
		faxNo1 = null;
		faxNo2 = null;
		faxNo3 = null;
		transit = null;
		shopInformation = null;
		holiday = null;
		businessHours = null;
		seatKbn = null;
		salesPerCustomerKbn = null;
		stationDtoList = new ArrayList<>();
		staff = null;
		url1 = null;
//		jobOfferFlg = null;
		version = null;
		id = null;
		materialMap = new HashMap<String, ShopListMaterialDto>();
	}

	/**
	 * エラーフラグのリセット
	 */
	public void resetErrorFlg() {
		errorFlg = false;
		geoErrorFlg = false;
		failGeoId = null;
	}


	/**
	 * 一時保存IDのバリデート
	 * @return
	 */
	public ActionMessages validateSaveIdList () {
		ActionMessages errors = new ActionMessages();
		if (ArrayUtils.isEmpty(saveIdList)) {
			errors.add("errors", new ActionMessage("errors.required", MessageResourcesUtil.getMessage("labels.id")));
		}
		return errors;
	}

	/**
	 * 独自チェックを行う
	 * @return
	 */
	@Override
	public ActionMessages validate() {
		ActionMessages errors = new ActionMessages();
		sortStationDto();
		checkPhoneNo(errors);
		checkFaxNo(errors);
		checkIndustryKbn(errors);
		checkUrl(errors);
		checkLatLngKbn(errors);
//		checkSalaryNum(errors);
//		checkArbeitHourSalaryHighAndLow(errors);
		checkRailload(errors);
//		checkArbeitFeatureKbnLength(errors);
//		checkArbeitStringLength(errors);
		checkCatchCopyLength(errors);
		checkTel(errors);

		return errors;
	}
}
