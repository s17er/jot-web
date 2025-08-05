package com.gourmetcaree.admin.pc.shopList.form.shopList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.util.MessageResourcesUtil;

import com.gourmetcaree.common.dto.ShopListMaterialDto;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;

@Component(instance=InstanceType.SESSION)
public class InputCsvForm extends ShopListBaseForm implements Serializable  {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 3203733393460224658L;



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

	/** 最大表示件数 */
	public String maxRow;


	/**
	 * フォームのリセット
	 */
	@Override
	public void resetForm() {
		super.resetForm();
		saveIdList = new String[0];
		importNum = 0;
		version = null;
		csvMapList = null;
		idList = null;
		errorFlg = false;
		geoErrorFlg = false;
		failGeoId = null;
		targetPage = null;
		maxRow = null;
	}

	/**
	 * チェックボックスのリセット
	 */
	public void resetCheckBox() {
		super.resetCheckBox();
		saveIdList = new String[0];
	}

	/**
	 * 顧客ID以外をリセット
	 */
	@Override
	public void resetFormWithoutCustomerId() {
		super.resetFormWithoutCustomerId();
		saveIdList = new String[0];
		importNum = 0;
		version = null;
		csvMapList = null;
		idList = null;
		errorFlg = false;
		geoErrorFlg = false;
		failGeoId = null;
		targetId = 0;
		targetPage = null;
		maxRow = null;
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
		staff = null;
		url1 = null;
		version = null;
		id = null;
		materialMap = new HashMap<String, ShopListMaterialDto>();
		targetId = 0;
		stationDtoList = new ArrayList<>();
		mainImgSelect = null;
		logoImgSelect = null;
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
//		checkCsvStationDto(errors);
		checkPhoneNo(errors);
		checkIndustryKbn(errors);
		checkUrl(errors);
		checkSalaryNum(errors);
		checkArbeitHourSalaryHighAndLow(errors);
		checkArbeitFeatureKbnLength(errors);
		checkArbeitStringLength(errors);
		checkCatchCopyLength(errors);
		checkTel(errors);

		return errors;
	}

	/**
	 * 駅グループのチェック(CSV用)
	 * @param errors
	 */
	private void checkCsvStationDto(ActionMessages errors) {
		if (CollectionUtils.isEmpty(stationDtoList)
				&& GourmetCareeUtil.eqInt(MTypeConstants.DomesticKbn.DOMESTIC ,domesticKbn)) {
			errors.add("errors", new ActionMessage("errors.requiredWithId",
					id,
					MessageResourcesUtil.getMessage("labels.stationDtoList")));
		}
	}
}
