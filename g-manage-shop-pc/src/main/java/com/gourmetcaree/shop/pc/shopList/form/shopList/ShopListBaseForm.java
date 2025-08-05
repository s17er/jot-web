package com.gourmetcaree.shop.pc.shopList.form.shopList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.struts.annotation.FloatType;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Required;
import org.seasar.struts.util.MessageResourcesUtil;

import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.dto.ShopListMaterialDto;
import com.gourmetcaree.common.dto.TagListDto;
import com.gourmetcaree.common.form.BaseForm;
import com.gourmetcaree.common.logic.ValueToNameConvertLogic;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TShopList;
import com.gourmetcaree.shop.pc.shopList.dto.shopList.ShopListRailroadDto;

import jp.co.whizz_tech.commons.WztValidateUtil;

/**
 * 店舗一覧の入力用ベースアクションフォーム
 * @author Takehiro Nakamori
 *
 */
public abstract class ShopListBaseForm extends BaseForm {

	@Resource
	protected ValueToNameConvertLogic valueToNameConvertLogic;

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 170239105978416353L;

	/** 緯度整数部桁数 */
	public static final int LATITUDE_INTEGRAL_LENGTH = 2;

	/** 経度整数部桁数 */
	public static final int LONGITUDE_INTEGRAL_LENGTH = 3;

	/** 緯度・経度整数部桁数 */
	public static final int LATLNG_FRACTIONAL_LENGTH = 13;

	/** 店舗一覧ID */
	public String id;

	/** 顧客ID */
	public String customerId;

	/** エリア */
	public String areaCd;

	/** 店舗名 */
	@Required
	public String shopName;

	/** 業態【HP表示用】 */
	@Required
	public String industryText;

	/** 業態1 */
	@Required
	public String industryKbn1;

	/** 業態2 */
	public String industryKbn2;

	/** キャッチコピー */
	@Required
	public String catchCopy;

	/** 住所1(番-号まで) */
	public String address1;

	/** 住所2(ビル名など) */
	public String address2;

	/** 緯度 */
	@FloatType
	public String latitude;

	/** 経度 */
	@FloatType
	public String longitude;

	/** 緯度経度区分 */
	public String latLngKbn;

	/** 電話番号1 */
	public String phoneNo1;

	/** 電話番号2 */
	public String phoneNo2;

	/** 電話番号3 */
	public String phoneNo3;

	/** 電話番号（統合ver） */
	public String csvPhoneNo;

	/** FAX番号1 */
	public String faxNo1;

	/** FAX番号2 */
	public String faxNo2;

	/** FAX番号3 */
	public String faxNo3;

	/** 交通 */
	public String transit;

	/** 店舗情報 */
	@Maxlength(maxlength=300)
	public String shopInformation;

	/** 定休日 */
	public String holiday;

	/** 営業時間 */
	public String businessHours;

	/** オープン年 */
	public String openDateYear;

	/** オープン月 */
	public String openDateMonth;

	/** オープン日備考 */
	public String openDateNote;

	/** オープン日表示期限 */
	public String openDateLimitDisplayDate;

	/** 席数 */
	public String seatKbn;

	/** 顧客単価 */
	public String salesPerCustomerKbn;

	/** スタッフ */
	public String staff;

	/** URL1 */
	public String url1;
	/** 素材のアップロード操作用パラメータ */
	public String hiddenMaterialKbn;
	/** カーソル位置 */
	public String cursorPosition;

	/** 画像アップのためのフォーム */
	public FormFile imgFile;

	/** メイン画像の選択 */
	public String mainImgSelect;

	/** ロゴ画像の選択 */
	public String logoImgSelect;

	/** 素材を保持するマップ */
	@Binding(bindingType = BindingType.NONE)
	public Map<String, ShopListMaterialDto> materialMap = new HashMap<>();

	/** 求人募集フラグ */
	public String jobOfferFlg;


//	/** バイト側職種 */
//	public String arbeitJob;
//
//	/** バイト側業態 */
//	public String arbeitGyotaiId;
//
//	/** バイト側時給(下限) */
//	public String arbeitHourSalary;
//
//	/** バイト側時給(上限) */
//	public String arbeitHourMaxSalary;
//
//	/** バイト側備考 */
//	public String arbeitRemarks;
//
//
//	/** バイト側時間 */
//	public String arbeitWorkingHour;
//
//	/** バイト側待遇 */
//	public String arbeitTreatment;
//
//	/** バイト側昇給タイミング */
//	public String arbeitRiseSalaryTiming;
//
//	/** バイト側給料日 */
//	public String arbeitPayDay;

	/** 国内外区分 */
	public String domesticKbn;

	/** 都道府県コード */
	public String prefecturesCd;

	/** 市区町村コード */
	public String cityCd;

	/** その他住所 */
	public String address;

	/** 海外エリア区分 */
	public String shutokenForeignAreaKbn;

	/** 海外住所 */
	public String foreignAddress;

//	/** バイト側電話番号 */
//	public String arbeitPhoneNo;
//
//	/** バイト側電話受付時間 */
//	public String arbeitPhoneReceptionTime;
//
//	/** バイト側応募方法 */
//	public String arbeitApplicationMethod;
//
//	/** バイト側応募資格 */
//	public String arbeitApplicationCapacity;
//
//	/** バイト側お店を一言で */
//	public String arbeitShopSingleWord;
//
//	/** バイト側お店を一言で区分 */
//	public String arbeitShopSingleWordKbn;
//
//	/** バイト側フリーコメント */
//	public String arbeitFreeComment;
//
//	/** 職種タイプ */
//	public String[] arbeitJobTypeArray;
//
//	/** 稼げる指数 */
//	public String[] arbeitMoneyLevelArray;
//
//	/** シフト時間 */
//	public String[] arbeitShiftTimeArray;
//
//	/** シフト週 */
//	public String[] arbeitShiftWeekArray;
//
//
//	/** 特徴 */
//	public String[] arbeitFeatureKbnArray;
//
	/** 仕事の特徴 */
	public String[] workCharacteristicKbnArray;

	/** 職場 */
	public String[] shopCharacteristicKbnArray;

	/** 駅関連DTO */
	public List<StationDto> stationDtoList = new ArrayList<>();

	/** よく使うタグ */
	public List<TagListDto> tagListDto = new ArrayList<>();

	/** 受動喫煙対策 */
    public String preventSmoke;

    /** 職種の給与情報の中身 */
    public List<DisplayConditionDto> displayConditionDtoList = new ArrayList<>();

    /** 職種のチェックボックスを保持する（画面制御用でDB登録しない） */
    public List<String> employJobKbnList = new ArrayList<>();



	// リニューアルで不要になる項目
//	/** バイト側都道府県ID */
//	public String arbeitTodouhukenId;
//	/** バイト側エリアID */
//	public String arbeitAreaId;
//	/** バイト側サブエリアID */
//	public String arbeitSubAreaId;
//	/** バイト側住所 */
//	public String arbeitAddress;
	/** 路線1 */
	public ShopListRailroadDto railroadDto1 = new ShopListRailroadDto();
	/** 路線2 */
	public ShopListRailroadDto railroadDto2 = new ShopListRailroadDto();
	/** 路線3 */
	public ShopListRailroadDto railroadDto3 = new ShopListRailroadDto();


	/** タグ */
	public String[] tags;

	public String tagList;
	/**
	 * フォームのリセット
	 */
	public void resetForm() {
		resetBaseForm();
		shopName = null;
		id = null;
		industryText = null;
		industryKbn1 = null;
		industryKbn2 = null;
		catchCopy = null;
		address1 = null;
		address2 = null;
		latitude = null;
		longitude = null;
		phoneNo1 = null;
		phoneNo2 = null;
		phoneNo3 = null;
		csvPhoneNo = null;
		faxNo1 = null;
		faxNo2 = null;
		faxNo3 = null;
		transit = null;
		shopInformation = null;
		holiday = null;
		businessHours = null;
		openDateYear = null;
		openDateMonth = null;
		openDateNote = null;
		openDateLimitDisplayDate = null;
		seatKbn = null;
		salesPerCustomerKbn = null;
		staff = null;
		url1 = null;
		jobOfferFlg = null;

		if (imgFile != null) {
			imgFile.destroy();
		}
		imgFile = null;

		materialMap.clear();
		hiddenMaterialKbn = null;
		cursorPosition = null;
		latLngKbn = null;
//		arbeitGyotaiId = null;
//		arbeitJob = null;
//		arbeitHourSalary = null;
//		arbeitWorkingHour = null;
//		arbeitTreatment = null;
//		arbeitRiseSalaryTiming = null;
//		arbeitPayDay = null;
		domesticKbn = null;
		prefecturesCd = null;
		cityCd = null;
		address = null;
		shutokenForeignAreaKbn = null;
		foreignAddress = null;
//		arbeitPhoneNo = null;
//		arbeitPhoneReceptionTime = null;
//		arbeitApplicationMethod = null;
//		arbeitApplicationCapacity = null;
//		arbeitShopSingleWord = null;
//		arbeitShopSingleWordKbn = null;
//		arbeitHourMaxSalary = null;
//		arbeitRemarks = null;
//		arbeitFreeComment = null;
//		arbeitJobTypeArray = null;
//		arbeitMoneyLevelArray = null;
//		arbeitShiftTimeArray = null;
//		arbeitShiftWeekArray = null;
//		arbeitFeatureKbnArray = null;
//		workCharacteristicKbnArray = null;
//		shopCharacteristicKbnArray = null;
		stationDtoList = new ArrayList<>();

		// よく使うタグ
		tagListDto = new ArrayList<>();
		// タグ
		tags = null;
		tagList = null;

		preventSmoke = null;

//		arbeitTodouhukenId = null;
//		arbeitAreaId = null;
//		arbeitSubAreaId = null;
//		arbeitAddress = null;
		railroadDto1 = new ShopListRailroadDto();
		railroadDto2 = new ShopListRailroadDto();
		railroadDto3 = new ShopListRailroadDto();

		// 職種のリセット
        displayConditionDtoList = new ArrayList<>();
        employJobKbnList = new ArrayList<>();
	}

	/**
	 * マルチボックスのリセット
	 */
	public void resetMultiBox() {
		latLngKbn = null;
//		arbeitJobTypeArray = null;
//		arbeitMoneyLevelArray = null;
//		arbeitShiftTimeArray = null;
//		arbeitShiftWeekArray = null;
//		arbeitFeatureKbnArray = null;
//		workCharacteristicKbnArray = null;
//		shopCharacteristicKbnArray = null;
		stationDtoList = new ArrayList<>();

		// 職種のリセット
        displayConditionDtoList = new ArrayList<>();
        employJobKbnList = new ArrayList<>();
	}

	/**
	 * 独自チェックを行う
	 * @return
	 */
	public ActionMessages validate() {
		ActionMessages errors = new ActionMessages();
		sortStationDto();
		checkPhoneNo(errors);
		checkFaxNo(errors);
		checkIndustryKbn(errors);
		checkAddress(errors);
		checkUrl(errors);
//		checkSalaryNum(errors);
//		checkArbeitHourSalaryHighAndLow(errors);
//		checkRailload(errors);
//		checkArbeitFeatureKbnLength(errors);
//		checkArbeitStringLength(errors);
		checkCatchCopyLength(errors);
		checkTel(errors);
		// 職種給与のチェック
		checkCondition(errors);

//		if (isCheckArbeit()) {
//			checkArbeitInput(errors);
//		}
		return errors;
	}

	/**
	 * 緯度経度のチェックを行う
	 */
	protected void checkLatLngKbn(ActionMessages errors) {
		if (StringUtils.isBlank(latLngKbn)) {
			return;
		}

		if (GourmetCareeUtil.eqInt(MTypeConstants.ShopListLatLngKbn.LAT_LNG, latLngKbn)
				&& (StringUtils.isBlank(latitude)
						|| StringUtils.isBlank(longitude))) {
			errors.add("errors", new ActionMessage("errors.app.noInputLatLng"));
			return;
		}

		// 緯度チェック
		float lat = new Float(0);
		int lat_int = new Integer(0);
		try{
			lat = Float.valueOf(latitude);
			lat_int = (int) lat;
		}catch(NumberFormatException e) {
			// 入ることはない想定
			errors.add("errors", new ActionMessage("errors.float",MessageResourcesUtil.getMessage("labels.latitude")));
		}
		// 整数部桁数チェック
		int lat_intgral_len = String.valueOf(lat_int).length();
		if (lat_intgral_len > LATITUDE_INTEGRAL_LENGTH) {
			errors.add("errors", new ActionMessage("errors.app.noInputLatLngIntegralLengh"
					,MessageResourcesUtil.getMessage("labels.latitude")
					,LATITUDE_INTEGRAL_LENGTH
					));
		}
		int lat_len = latitude.length();
		if ((lat_len - lat_intgral_len -1) > LATLNG_FRACTIONAL_LENGTH) {
			errors.add("errors", new ActionMessage("errors.invalid"
					,MessageResourcesUtil.getMessage("labels.latitude")
					));
		}

		// 経度チェック
		float longi = new Float(0);
		int longi_int = new Integer(0);
		try{
			longi = Float.valueOf(longitude);
			longi_int = (int) longi;
		}catch(NumberFormatException e) {
			// 入ることはない想定
			errors.add("errors", new ActionMessage("errors.float",MessageResourcesUtil.getMessage("labels.longitude")));
		}
		// 整数部桁数チェック
		int longi_intgral_len = String.valueOf(longi_int).length();
		if (longi_intgral_len > LONGITUDE_INTEGRAL_LENGTH) {
			errors.add("errors", new ActionMessage("errors.app.noInputLatLngIntegralLengh"
					,MessageResourcesUtil.getMessage("labels.longitude")
					,LONGITUDE_INTEGRAL_LENGTH
					));
		}
		int longi_len = longitude.length();
		if ((longi_len - longi_intgral_len -1) > LATLNG_FRACTIONAL_LENGTH) {
			errors.add("errors", new ActionMessage("errors.invalid"
					,MessageResourcesUtil.getMessage("labels.longitude")
					));
		}
	}

	/**
	 * 電話番号のチェックを行う。
	 * @param errors
	 */
	protected void checkPhoneNo(ActionMessages errors) {
		// 全て空の場合はなにもしない。
		if (StringUtils.isBlank(phoneNo1)
				&& StringUtils.isBlank(phoneNo2)
				&& StringUtils.isBlank(phoneNo3)) {
			return;
		}

		// 3つすべてに値が入力されているかチェック
		if (StringUtils.isBlank(phoneNo1) || StringUtils.isBlank(phoneNo2) || StringUtils.isBlank(phoneNo3)) {
			errors.add("errors", new ActionMessage("errors.phoneNoFailed"));
			return;
		}

		phoneNo1 = GourmetCareeUtil.removeAllSpace(phoneNo1);
		phoneNo2 = GourmetCareeUtil.removeAllSpace(phoneNo2);
		phoneNo3 = GourmetCareeUtil.removeAllSpace(phoneNo3);

		// 3つすべて数値かどうかチェック
		if (!StringUtils.isNumeric(phoneNo1) || !StringUtils.isNumeric(phoneNo2) || !StringUtils.isNumeric(phoneNo3)) {
			errors.add("errors", new ActionMessage("errors.phoneNoFailed"));
			return;
		}
	}

	/**
	 * URLのチェックを行う。
	 * @param errors
	 */
	protected void checkUrl(ActionMessages errors) {
		if (StringUtils.isBlank(url1)) {
			return;
		}

		if (!WztValidateUtil.isHttpUrl(url1)) {
			errors.add("errors", new ActionMessage("errors.url",
					MessageResourcesUtil.getMessage("labels.shopListUrl1")));
		}
	}

	/**
	 * FAXのチェックを行う。
	 * @param errors
	 */
	protected void checkFaxNo(ActionMessages errors) {
		// 全て空の場合はなにもしない。
		if (StringUtils.isBlank(faxNo1)
				&& StringUtils.isBlank(faxNo2)
				&& StringUtils.isBlank(faxNo3)) {
			return;
		}


		if (StringUtils.isBlank(faxNo1)
				|| StringUtils.isBlank(faxNo2)
				|| StringUtils.isBlank(faxNo3)) {
			errors.add("errors", new ActionMessage("errors.faxnoNotNumeric"));
			return;
		}

		faxNo1 = GourmetCareeUtil.removeAllSpace(faxNo1);
		faxNo2 = GourmetCareeUtil.removeAllSpace(faxNo2);
		faxNo3 = GourmetCareeUtil.removeAllSpace(faxNo3);

		if (!StringUtils.isNumeric(faxNo1)
				|| !StringUtils.isNumeric(faxNo2)
				|| !StringUtils.isNumeric(faxNo3)) {
			errors.add("errors", new ActionMessage("errors.faxnoNotNumeric"));
			return;
		}
	}

	/**
	 * 業態のチェックを行う
	 * @param errors
	 */
	protected void checkIndustryKbn(ActionMessages errors) {
		if (StringUtils.isNotBlank(industryKbn1) && StringUtils.isNotBlank(industryKbn2) && StringUtils.equals(industryKbn1, industryKbn2)) {
			errors.add("errors", new ActionMessage("errors.unique",
					MessageResourcesUtil.getMessage("labels.industryKbn2")));
		}
	}

	/**
	 * 職種給与のチェック
	 * @param errors
	 */
	protected void checkCondition(ActionMessages errors) {
		if(!displayConditionDtoList.isEmpty()) {
			for(DisplayConditionDto displayConditionDto : displayConditionDtoList){
				String employJobName = "";
				employJobName = valueToNameConvertLogic.convertToEmployName(new String[] {String.valueOf(displayConditionDto.employPtnKbn)})  + "：" + valueToNameConvertLogic.convertToJobName(new String[] {String.valueOf(displayConditionDto.jobKbn)});
				// 給与区分をチェック
				if(!GourmetCareeUtil.eqInt(MTypeConstants.SaleryStructureKbn.DAILY, displayConditionDto.saleryStructureKbn)
						&& !GourmetCareeUtil.eqInt(MTypeConstants.SaleryStructureKbn.HOURLY, displayConditionDto.saleryStructureKbn)
						&& !GourmetCareeUtil.eqInt(MTypeConstants.SaleryStructureKbn.MONTHLY, displayConditionDto.saleryStructureKbn)
						&& !GourmetCareeUtil.eqInt(MTypeConstants.SaleryStructureKbn.YEARLY, displayConditionDto.saleryStructureKbn)
						&& !GourmetCareeUtil.eqInt(MTypeConstants.SaleryStructureKbn.ANNUAL, displayConditionDto.saleryStructureKbn)) {
					errors.add("errors", new ActionMessage("errors.selectSaleryStructureKbn", employJobName));
				}
				// 金額をチェック どちらか一つは必須
				if(StringUtils.isBlank(displayConditionDto.lowerSalaryPrice) && StringUtils.isBlank(displayConditionDto.upperSalaryPrice)) {
					errors.add("errors", new ActionMessage("errors.requiredSalaryPrice", employJobName));
				}
			}
		}
	}

	protected void checkAddress(ActionMessages errors) {
		// 都道府県
		if (StringUtils.isBlank(prefecturesCd) && MTypeConstants.DomesticKbn.DOMESTIC == Integer.parseInt(domesticKbn)) {
			errors.add("errors", new ActionMessage("errors.required",
					MessageResourcesUtil.getMessage("labels.prefecturesCd")));
		}

		// 市区町村
		if (StringUtils.isBlank(cityCd) && MTypeConstants.DomesticKbn.DOMESTIC == Integer.parseInt(domesticKbn)) {
			errors.add("errors", new ActionMessage("errors.required",
					MessageResourcesUtil.getMessage("labels.municipality")));
		}

		if (StringUtils.isBlank(shutokenForeignAreaKbn) && MTypeConstants.DomesticKbn.overseas == Integer.parseInt(domesticKbn)) {
			errors.add("errors", new ActionMessage("errors.required",
					MessageResourcesUtil.getMessage("labels.shutokenForeignAreaKbn")));
		}
	}

	/**
	 * FAX番号があるかどうか
	 * @return
	 */
	public boolean isFaxNoExist() {
		return (StringUtils.isNotBlank(faxNo1) && StringUtils.isNotBlank(faxNo2) && StringUtils.isNotBlank(faxNo3));
	}

	/**
	 * 電話番号があるかどうか
	 * @return
	 */
	public boolean isPhoneNoExist() {
		return (StringUtils.isNotBlank(phoneNo1) && StringUtils.isNotBlank(phoneNo2) && StringUtils.isNotBlank(phoneNo3));
	}

	/**
	 * 素材保持用Mapの使用前処理
	 */
	public void initMaterialMap() {

		if (this.materialMap == null) {
			this.materialMap = new HashMap<>();
		}
	}

	/**
	 * アップロードされた素材の削除処理
	 * @param materialKbn 素材区分
	 */
	public void deleteMaterial(String materialKbn) {

		// Mapの初期処理を行う
		initMaterialMap();

		// 選択された素材のMapを消す
		materialMap.remove(materialKbn);
		// 選択されたサムネイル素材のMapを消す
		materialMap.remove(MTypeConstants.MaterialKbn.getThumbValue(materialKbn));

		// FormFileを削除する
		deleteFormFile(materialKbn);
	}

	/**
	 * 素材区分を元に、登録されているFormFileを削除する
	 * @param materialKbn 素材区分
	 */
	public void deleteFormFile(String materialKbn) {
		if (imgFile != null) {
			imgFile.destroy();
			imgFile = null;
		}
	}

	/**
	 * ファイル出力用のキーを取得します。
	 * コピー画面以外でwebIdが存在すればwebId、それ以外の場合は文字列「INPUT」を取得します。
	 * @return
	 */
	public String getIdForDirName() {
		if (StringUtils.isBlank(id)) {
			return GourmetCareeConstants.IMG_SHOP_LIST_FILEKEY_INPUT;
		}
		return id;
	}


	/**
	 * 店舗一覧路線DTOリストを取得します。
	 * @return 店舗一覧路線DTOリスト
	 */
	public List<ShopListRailroadDto> getShopListRailroadDtoList() {
		List<ShopListRailroadDto> dtoList = new ArrayList<>();
		dtoList.add(railroadDto1);
		dtoList.add(railroadDto2);
		dtoList.add(railroadDto3);
		return dtoList;
	}



//	/**
//	 * バイトの入力をチェック。
//	 * @param errors
//	 */
//	protected void checkArbeitInput(ActionMessages errors) {
//
//		// 業態
//		if (StringUtils.isBlank(arbeitGyotaiId)) {
//			errors.add("errors", new ActionMessage("errors.required",
//					MessageResourcesUtil.getMessage("labels.arbeitGyotaiId")));
//		}
//
//		// 職種
//		if (StringUtils.isBlank(arbeitJob)) {
//			errors.add("errors", new ActionMessage("errors.required",
//					MessageResourcesUtil.getMessage("labels.arbeitJob")));
//		}
//
//		// 職種タイプ
//		if (ArrayUtils.isEmpty(arbeitJobTypeArray)) {
//			errors.add("errors", new ActionMessage("errors.required",
//					MessageResourcesUtil.getMessage("labels.arbeitJobTypeArray")));
//		}
//
//		// 時給
//		if (StringUtils.isBlank(arbeitHourSalary)) {
//			errors.add("errors", new ActionMessage("errors.required",
//					MessageResourcesUtil.getMessage("labels.arbeitHourSalary")));
//		}
//
//		// 時給上限
////		if (StringUtils.isBlank(arbeitHourMaxSalary)) {
////			errors.add("errors", new ActionMessage("errors.required",
////					MessageResourcesUtil.getMessage("labels.arbeitHourMaxSalary")));
////		}
//
//		// 備考
////		if (StringUtils.isBlank(arbeitRemarks)) {
////			errors.add("errors", new ActionMessage("errors.required",
////					MessageResourcesUtil.getMessage("labels.arbeitRemarks")));
////		}
//
//
//		// 時間
//		if (StringUtils.isBlank(arbeitWorkingHour)) {
//			errors.add("errors", new ActionMessage("errors.required",
//					MessageResourcesUtil.getMessage("labels.arbeitWorkingHour")));
//		}
//
//		// 待遇
//		if (StringUtils.isBlank(arbeitTreatment)) {
//			errors.add("errors", new ActionMessage("errors.required",
//					MessageResourcesUtil.getMessage("labels.arbeitTreatment")));
//		}
//
//		// 時給アップのタイミング
//		if (StringUtils.isBlank(arbeitRiseSalaryTiming)) {
//			errors.add("errors", new ActionMessage("errors.required",
//					MessageResourcesUtil.getMessage("labels.arbeitRiseSalaryTiming")));
//		}
//
//		// 給料日
//		if (StringUtils.isBlank(arbeitPayDay)) {
//			errors.add("errors", new ActionMessage("errors.required",
//					MessageResourcesUtil.getMessage("labels.arbeitPayDay")));
//		}
//
//		// 電話番号
//		if (StringUtils.isBlank(arbeitPhoneNo)) {
//			errors.add("errors", new ActionMessage("errors.required",
//					MessageResourcesUtil.getMessage("labels.arbeitPhoneNo")));
//		}
//
//		// 電話受付時間
//		if (StringUtils.isBlank(arbeitPhoneReceptionTime)) {
//			errors.add("errors", new ActionMessage("errors.required",
//					MessageResourcesUtil.getMessage("labels.arbeitPhoneReceptionTime")));
//		}
//
//		// 応募方法
//		if (StringUtils.isBlank(arbeitApplicationMethod)) {
//			errors.add("errors", new ActionMessage("errors.required",
//					MessageResourcesUtil.getMessage("labels.arbeitApplicationMethod")));
//		}
//
//		// 応募資格
//		if (StringUtils.isBlank(arbeitApplicationCapacity)) {
//			errors.add("errors", new ActionMessage("errors.required",
//					MessageResourcesUtil.getMessage("labels.arbeitApplicationCapacity")));
//		}
//
//		// お店を一言で
//		if (StringUtils.isBlank(arbeitShopSingleWord)) {
//			errors.add("errors", new ActionMessage("errors.required",
//					MessageResourcesUtil.getMessage("labels.arbeitShopSingleWord")));
//		}
//
//		// ロゴ画像
////		if (materialMap == null ||
////				!materialMap.containsKey(
////				MTypeConstants.ShopListMaterialKbn.LOGO)) {
////
////			errors.add("errors", new ActionMessage("errors.required",
////					MessageResourcesUtil.getMessage("labels.logo")));
////		}
//
//		// フリーコメント
//		if (StringUtils.isBlank(arbeitFreeComment)) {
//			errors.add("errors", new ActionMessage("errors.required",
//					MessageResourcesUtil.getMessage("labels.arbeitFreeComment")));
//		}
//
//		// 稼げる指数
//		if (ArrayUtils.isEmpty(arbeitMoneyLevelArray)) {
//			errors.add("errors", new ActionMessage("errors.required",
//					MessageResourcesUtil.getMessage("labels.arbeitMoneyLevelArray")));
//		}
//
//		// シフト 曜日
//		if (ArrayUtils.isEmpty(arbeitShiftWeekArray)) {
//			errors.add("errors", new ActionMessage("errors.required",
//					MessageResourcesUtil.getMessage("labels.arbeitShiftWeekArray")));
//		}
//
//		// シフト 時間
//		if (ArrayUtils.isEmpty(arbeitShiftTimeArray)) {
//			errors.add("errors", new ActionMessage("errors.required",
//					MessageResourcesUtil.getMessage("labels.arbeitShiftTimeArray")));
//		}
//
//		// 特徴
//		if (ArrayUtils.isEmpty(arbeitFeatureKbnArray)) {
//			errors.add("errors", new ActionMessage("errors.required",
//					MessageResourcesUtil.getMessage("labels.arbeitFeatureKbnArray")));
//		}
//	}

	protected void checkRailloadIsEmpty(ActionMessages errors) {
		if (railroadDto1.isNotExistInput()
				&& railroadDto2.isNotExistInput()
				&& railroadDto3.isNotExistInput()) {
			errors.add("errors", new ActionMessage("errors.app.noInputRailroad"));
		}
	}


	/**
	 * 路線をチェックします。
	 * @param errors アクションメッセージ
	 */
	protected void checkRailload(ActionMessages errors) {


		if (railroadDto1.isNotCompleteInput()) {
			errors.add("errors", new ActionMessage("errors.app.noCompletedInputRailroad",
					"1"));
		}

		if (railroadDto2.isNotCompleteInput()) {
			errors.add("errors", new ActionMessage("errors.app.noCompletedInputRailroad",
					"2"));
		}

		if (railroadDto3.isNotCompleteInput()) {
			errors.add("errors", new ActionMessage("errors.app.noCompletedInputRailroad",
					"3"));
		}
	}

//	/**
//	 * バイトの特徴をチェックします。
//	 * @param errors アクションメッセージ
//	 */
//	protected void checkArbeitFeatureKbnLength(ActionMessages errors) {
//		if (!ArrayUtils.isEmpty(arbeitFeatureKbnArray)
//				&& arbeitFeatureKbnArray.length > 6) {
//			errors.add("errors", new ActionMessage("errors.overSelectNumber",
//					MessageResourcesUtil.getMessage("labels.arbeitFeatureKbnArray"),
//					"6"));
//		}
//	}
//
//
//	/**
//	 * 時給の数字文字をチェック
//	 * @param errors アクションメッセージ
//	 */
//	protected void checkSalaryNum(ActionMessages errors) {
//		if (StringUtils.isNotBlank(arbeitHourSalary)) {
//			arbeitHourSalary = arbeitHourSalary.replaceAll(",", "");
//
//			if (!GourmetCareeUtil.isHankakuNumber(arbeitHourSalary)) {
//				errors.add("errors", new ActionMessage("errors.integerhankaku",
//						MessageResourcesUtil.getMessage("labels.arbeitHourSalary")));
//			}
//		}
//
//
//		if (StringUtils.isNotBlank(arbeitHourMaxSalary)) {
//			arbeitHourMaxSalary = arbeitHourMaxSalary.replaceAll(",", "");
//			if (!GourmetCareeUtil.isHankakuNumber(arbeitHourMaxSalary)) {
//				errors.add("errors", new ActionMessage("errors.integerhankaku",
//						MessageResourcesUtil.getMessage("labels.arbeitHourMaxSalary")));
//			}
//		}
//	}
//
//
//	/**
//	 * バイト時給の上下をチェック
//	 * @param errors エラー
//	 */
//	protected void checkArbeitHourSalaryHighAndLow(ActionMessages errors) {
//		if (StringUtils.isEmpty(arbeitHourSalary)
//				|| StringUtils.isEmpty(arbeitHourMaxSalary)) {
//			return;
//		}
//
//		int low = NumberUtils.toInt(arbeitHourSalary);
//		int high = NumberUtils.toInt(arbeitHourMaxSalary);
//
//		if (low > high) {
//			errors.add("errors", new ActionMessage("errors.app.oppositeHighAndLow",
//					MessageResourcesUtil.getMessage("labels.arbeitHourSalary"),
//					MessageResourcesUtil.getMessage("labels.arbeitHourMaxSalary")));
//		}
//	}
//
//
//	/**
//	 * バイト入力の文字数をチェックします。
//	 * @param errors エラー
//	 */
//	protected void checkArbeitStringLength(ActionMessages errors) {
//		 if (StringUtils.isNotBlank(arbeitFreeComment)
//				 && arbeitFreeComment.length() > TShopList.ARBEIT_FREECOMMENT_MAX_LENGTH) {
//			errors.add("errors", new ActionMessage("errors.maxlength",
//					MessageResourcesUtil.getMessage("labels.arbeitFreeComment"),
//					String.valueOf(TShopList.ARBEIT_FREECOMMENT_MAX_LENGTH)));
//		}
//
//
//		 if (StringUtils.isNotBlank(arbeitShopSingleWord)
//				 && arbeitShopSingleWord.length() > TShopList.ARBEIT_SHOP_SINGLE_WORD_MAX_LENGTH){
//			errors.add("errors", new ActionMessage("errors.maxlength",
//					MessageResourcesUtil.getMessage("labels.arbeitShopSingleWord"),
//					String.valueOf(TShopList.ARBEIT_SHOP_SINGLE_WORD_MAX_LENGTH)));
//		}
//	}

	/***
	 * キャッチコピーの文字数をチェックします
	 * @param errors エラー
	 */
	protected void checkCatchCopyLength(ActionMessages errors) {
		if(catchCopy.length() > 30) {
			errors.add("errors", new ActionMessage("errors.maxlength",
					MessageResourcesUtil.getMessage("labels.catchCopy"),
					String.valueOf(TShopList.CATCH_COPY_MAX_LENGTH)));
		}
	}

	/***
	 * 電話番号の形式をチェックします
	 * @param errors　エラー
	 */
	protected void checkTel(ActionMessages errors) {
		//空白の場合はチェックしない
		if(StringUtils.isBlank(csvPhoneNo)) {
			return ;
		}

		String[] tel = csvPhoneNo.split("-",0);

		//ハイフンできちんと区切られているかチェック
		if(tel.length != 3) {
			errors.add("errors", new ActionMessage("errors.phoneNoFailed"));
			return;
		}

		//数値で入力されているかチェック
		for(String number : tel){
			if(!StringUtils.isNumeric(number)) {
				errors.add("errors", new ActionMessage("errors.phoneNoFailed"));
				return;
			}
		}
	}

	/**
	 * 駅を並び変える
	 */
	public void sortStationDto() {
		stationDtoList = stationDtoList.stream()
				.filter(o -> o.displayOrder != null)
				.sorted(Comparator.comparing(i -> Integer.parseInt(i.getDisplayOrder())))
				.collect(Collectors.toList());
	}

	/**
	 * 駅関連のDTO
	 */
	public static class StationDto implements Serializable {

		/** シリアルバージョンUID */
		private static final long serialVersionUID = 435933128733011346L;

		/** 鉄道事業者コード */
		public String companyCd;
		/** 鉄道会社名 */
		public String companyName;
		/** 路線コード */
		public String lineCd;
		/** 路線名 */
		public String lineName;
		/** 駅コード */
		public String stationCd;
		/** 駅名 */
		public String stationName;
		/** 移動手段区分 */
		public String transportationKbn;
		/** 所要時間 */
		public String timeRequiredMinute;
		/** 表示順 */
		public String displayOrder;

		public String getCompanyCd() {
			return companyCd;
		}
		public String getCompanyName() {
			return companyName;
		}
		public String getLineCd() {
			return lineCd;
		}
		public String getLineName() {
			return lineName;
		}
		public String getStationCd() {
			return stationCd;
		}
		public String getStationName() {
			return stationName;
		}
		public String getTransportationKbn() {
			return transportationKbn;
		}
		public String getTimeRequiredMinute() {
			return timeRequiredMinute;
		}
		public String getDisplayOrder() {
			return displayOrder;
		}
	}

//	protected boolean isCheckArbeit() {
//
//		if(StringUtils.isNotBlank(arbeitGyotaiId)
//				|| StringUtils.isNotBlank(arbeitJob)
//				|| arbeitJobTypeArray != null
//				|| StringUtils.isNotBlank(arbeitHourSalary)
//				|| StringUtils.isNotBlank(arbeitHourMaxSalary)
//				|| StringUtils.isNotBlank(arbeitRemarks)
//				|| StringUtils.isNotBlank(arbeitWorkingHour)
//				|| StringUtils.isNotBlank(arbeitTreatment)
//				|| StringUtils.isNotBlank(arbeitRiseSalaryTiming)
//				|| StringUtils.isNotBlank(arbeitPayDay)
//				|| StringUtils.isNotBlank(arbeitPhoneNo)
//				|| StringUtils.isNotBlank(arbeitPhoneReceptionTime)
//				|| StringUtils.isNotBlank(arbeitApplicationMethod)
//				|| StringUtils.isNotBlank(arbeitApplicationCapacity)
//				|| StringUtils.isNotBlank(arbeitShopSingleWord)
//				|| StringUtils.isNotBlank(arbeitFreeComment)
//				|| arbeitMoneyLevelArray != null
//				|| arbeitShiftTimeArray != null
//				|| arbeitShiftWeekArray != null
//				) {
//			return true;
//		}
//
//		return false;
//	}

	/**
	 * 表示条件のDTO
	 * @author whizz
	 *
	 */
	public static class DisplayConditionDto implements Serializable {

		/** シリアルバージョンUID */
		private static final long serialVersionUID = 435933628733011346L;

		/** 職種 */
		public String jobKbn;
		/** 雇用形態 */
		public String employPtnKbn;
		/** 仕事内容 */
		public String workContents;
		/** 給与体系区分 */
		public String saleryStructureKbn;
		/** 給与金額（下限） */
		public String lowerSalaryPrice;
		/** 給与金額（上限） */
		public String upperSalaryPrice;
		/** 給与 */
		public String salary;
		/** 給与詳細 */
		public String salaryDetail;
		/** 想定初年度年収給与下限 */
		public String annualLowerSalaryPrice;
		/** 想定初年度年収給与上限 */
		public String annualUpperSalaryPrice;
		/** 想定初年度年収フリー欄 */
		public String annualSalary;
		/** 想定初年度年収給与詳細 */
		public String annualSalaryDetail;
		/** 想定初年度月収給与下限 */
		public String monthlyLowerSalaryPrice;
		/** 想定初年度月収給与上限 */
		public String monthlyUpperSalaryPrice;
		/** 想定初年度月収フリー欄 */
		public String monthlySalary;
		/** 想定初年度月収給与詳細 */
		public String monthlySalaryDetail;
		/** 登録済みかどうか */
		public String editFlg;

		public String getJobKbn() {
			return jobKbn;
		}
		public String getEmployPtnKbn() {
			return employPtnKbn;
		}
		public String getWorkContents() {
			return workContents;
		}
		public String getSaleryStructureKbn() {
			return saleryStructureKbn;
		}
		public String getLowerSalaryPrice() {
			return lowerSalaryPrice;
		}
		public String getUpperSalaryPrice() {
			return upperSalaryPrice;
		}
		public String getSalary() {
			return salary;
		}

		public static long getSerialversionuid() {
			return serialVersionUID;
		}
		public String getSalaryDetail() {
			return salaryDetail;
		}
		public String getAnnualLowerSalaryPrice() {
			return annualLowerSalaryPrice;
		}
		public String getAnnualUpperSalaryPrice() {
			return annualUpperSalaryPrice;
		}
		public String getAnnualSalary() {
			return annualSalary;
		}
		public String getAnnualSalaryDetail() {
			return annualSalaryDetail;
		}
		public String getMonthlyLowerSalaryPrice() {
			return monthlyLowerSalaryPrice;
		}
		public String getMonthlyUpperSalaryPrice() {
			return monthlyUpperSalaryPrice;
		}
		public String getMonthlySalary() {
			return monthlySalary;
		}
		public String getMonthlySalaryDetail() {
			return monthlySalaryDetail;
		}
		public String getEditFlg() {
			return editFlg;
		}
	}
}
