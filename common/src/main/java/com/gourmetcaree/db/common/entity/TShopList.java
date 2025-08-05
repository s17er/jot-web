package com.gourmetcaree.db.common.entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Entity
@Table(name="t_shop_list")
public class TShopList extends AbstractCommonMasqueradeEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 3452673001879272577L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_shop_list_id_gen")
	@SequenceGenerator(name="t_shop_list_id_gen", sequenceName="t_shop_list_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** 更新対象ID */
	@Column(name="target_id")
	public Integer targetId;

	/** エリアコード */
	@Column(name="area_cd")
	public Integer areaCd;

	/** 店舗名 */
	@Column(name="shop_name")
	public String shopName;

	/**
	 * ステータス
	 */
	@Column(name="status")
	public Integer status;

	/** 顧客ID */
	@Column(name="customer_id")
	public Integer customerId;

	/** 業態1 */
	@Column(name="industry_kbn1")
	public Integer industryKbn1;

	/** 業態2 */
	@Column(name="industry_kbn2")
	public Integer industryKbn2;

	/** 都道府県コード */
	@Column(name = PREFECTURES_CD)
	public Integer prefecturesCd;

	/** 市区町村コード */
	@Column(name = CITY_CD)
	public String cityCd;

	/** その他住所 */
	@Column(name="address")
	public String address;

	/** 海外住所 */
	@Column(name="foreign_address")
	public String foreignAddress;

	/** 緯度 */
	@Column(name="latitude")
	public Double latitude;

	/** 経度 */
	@Column(name="longitude")
	public Double longitude;

	/** 緯度経度区分 */
	@Column(name="lat_lng_kbn")
	public Integer latLngKbn;

	/** 電話番号1 */
	@Column(name="phone_no1")
	public String phoneNo1;

	/** 電話番号2 */
	@Column(name="phone_no2")
	public String phoneNo2;

	/** 電話番号3 */
	@Column(name="phone_no3")
	public String phoneNo3;

	/** csvから入力された電話番号 */
	@Column(name="csv_phone_no")
	public String csvPhoneNo;

	/** FAX番号1 */
	@Column(name="fax_no1")
	public String faxNo1;

	/** FAX番号2 */
	@Column(name="fax_no2")
	public String faxNo2;

	/** FAX番号3 */
	@Column(name="fax_no3")
	public String faxNo3;

	/** csvから入力されたFAX番号 */
	@Column(name="csv_fax_no")
	public String csvFaxNo;

	/** 交通 */
	@Column(name="transit")
	public String transit;

	/** 店舗情報 */
	@Column(name="shop_information")
	public String shopInformation;

	/** 定休日 */
	@Column(name="holiday")
	public String holiday;

	/** 営業時間 */
	@Column(name="business_hours")
	public String businessHours;

	/** 席数 */
	@Column(name="seating")
	public String seating;

	/** 顧客単価 */
	@Column(name="unit_price")
	public String unitPrice;

	/** スタッフ */
	@Column(name="staff")
	public String staff;

	/** URL1 */
	@Column(name="url1")
	public String url1;

	/** 表示番号 */
	@Column(name="disp_order")
	public Integer dispOrder;

	/** 表示フラグ */
	@Column(name="display_flg")
	public Integer displayFlg;

	/** 求人募集フラグ */
	@Column(name="job_offer_flg")
	public Integer jobOfferFlg;

	/** アクセスキー */
	@Column(name="access_key")
	public String accessKey;

	/** バイト側業態 */
	@Column(name="arbeit_gyotai_id")
	public Integer arbeitGyotaiId;

	/** バイト側職種 */
	@Column(name="arbeit_job")
	public String arbeitJob;

	/** バイト側時給(下限) */
	@Column(name="arbeit_hour_salary")
	public String arbeitHourSalary;

	/** バイト側時給(上限) */
	@Column(name="arbeit_hour_max_salary")
	public String arbeitHourMaxSalary;

	/** バイト側備考 */
	@Column(name="arbeit_remarks")
	public String arbeitRemarks;

	/** バイト側時間 */
	@Column(name="arbeit_working_hour")
	public String arbeitWorkingHour;

	/** バイト側待遇 */
	@Column(name="arbeit_treatment")
	public String arbeitTreatment;

	/** バイト側昇給タイミング */
	@Column(name="arbeit_rise_salary_timing")
	public String arbeitRiseSalaryTiming;

	/** バイト側給料日 */
	@Column(name="arbeit_pay_day")
	public String arbeitPayDay;

	/** 国内外区分 */
	@Column(name="domestic_kbn")
	public Integer domesticKbn;

	/** バイト側都道府県ID */
	@Column(name="arbeit_todouhuken_id")
	public Integer arbeitTodouhukenId;

	/** 海外エリア区分 */
	@Column(name="shutoken_foreign_area_kbn")
	public Integer shutokenForeignAreaKbn;

	/** バイト側エリアID */
	@Column(name="arbeit_area_id")
	public Integer arbeitAreaId;

	/** バイト側サブエリアID */
	@Column(name="arbeit_sub_area_id")
	public Integer arbeitSubAreaId;

	/** バイト側住所 */
	@Column(name="arbeit_address")
	public String arbeitAddress;

	/** バイト側電話番号 */
	@Column(name="arbeit_phone_no")
	public String arbeitPhoneNo;

	/** バイト側電話受付時間 */
	@Column(name="arbeit_phone_reception_time")
	public String arbeitPhoneReceptionTime;

	/** バイト側応募方法 */
	@Column(name="arbeit_application_method")
	public String arbeitApplicationMethod;

	/** バイト側応募資格 */
	@Column(name="arbeit_application_capacity")
	public String arbeitApplicationCapacity;

	/** バイト側お店を一言で */
	@Column(name="arbeit_shop_single_word")
	public String arbeitShopSingleWord;

	/** バイト側お店を一言で区分 */
	@Column(name="arbeit_shop_single_word_kbn")
	public Integer arbeitShopSingleWordKbn;

	/** バイト側フリーコメント */
	@Column(name="arbeit_free_comment")
	public String arbeitFreeComment;

	/** 業態テキスト */
	@Column(name=INDUSTRY_TEXT)
	public String industryText;

	/** キャッチコピー */
	@Column(name=CATCH_COPY)
	public String catchCopy;

	/** 座席数区分 */
	@Column(name=SEAT_KBN)
	public Integer seatKbn;

	/** 客単価区分 */
	@Column(name=SALES_PER_CUSTOMER_KBN)
	public Integer salesPerCustomerKbn;

	/** オープン日（年） */
	@Column(name=OPEN_DATE_YEAR)
	public Integer openDateYear;

	/** オープン日（月） */
	@Column(name=OPEN_DATE_MONTH)
	public Integer openDateMonth;

	/** オープン日備考 */
	@Column(name=OPEN_DATE_NOTE)
	public String openDateNote;

	/** オープン日表示期限 */
	@Column(name=OPEN_DATE_LIMIT_DISPLAY_DATE)
	public Date openDateLimitDisplayDate;

	/** キーワード検索 */
	@Column(name = KEYWORD_SEARCH)
	public String keywordSearch;

	/** 受動喫煙対策 */
	@Column(name = PREVENT_SMOKE)
	public String preventSmoke;


	/** 住所1(番-号まで)　旧レイアウト用のためリニューアル後は使用しない */
	@Column(name="address1")
	public String address1;

	/** 住所2(ビル名など)　旧レイアウト用のためリニューアル後は使用しない */
	@Column(name="address2")
	public String address2;


	/** 系列店舗ラベルグループエンティティリスト */
	@OneToMany(mappedBy="tShopList")
	public List<TShopListLabelGroup> tShopListLabelGroupList;


	/** テーブル名 */
	public static final String TABLE_NAME = "t_shop_list";

	/** ID */
	public static final String ID = "id";
	/** 更新対象ID */
	public static final String TARGET_ID = "target_id";
	/** エリアコード */
	public static final String AREA_CD = "area_cd";
	/** 店舗名 */
	public static final String SHOP_NAME = "shop_name";
	/** 顧客ID */
	public static final String CUSTOMER_ID = "customer_id";
	/** 業態1 */
	public static final String INDUSTRY_KBN1 = "industry_kbn1";
	/** 業態2 */
	public static final String INDUSTRY_KBN2 = "industry_kbn2";
	/** 都道府県コード */
	public static final String PREFECTURES_CD = "prefectures_cd";
	/** 市区町村コード */
	public static final String CITY_CD = "city_cd";
	/** その他住所 */
	public static final String ADDRESS = "address";
	/** 海外住所 */
	public static final String FOREIGN_ADDRESS = "foreign_address";
	/** 国内外区分 */
	public static final String DOMESTIC_KBN = "domestic_kbn";
	/** 緯度 */
	public static final String LATITUDE = "latitude";
	/** 経度 */
	public static final String LONGITUDE = "longitude";
	/** 緯度経度区分 */
	public static final String LAT_LNG_KBN = "lat_lng_kbn";
	/** 電話番号1 */
	public static final String PHONE_NO1 = "phone_no1";
	/** 電話番号2 */
	public static final String PHONE_NO2 = "phone_no2";
	/** 電話番号3 */
	public static final String PHONE_NO3 = "phone_no3";
	/** CSVから入力された電話番号 */
	public static final String CSV_PHONE_NO = "csv_phone_no";
	/** FAX番号1 */
	public static final String FAX_NO1 = "fax_no1";
	/** FAX番号2 */
	public static final String FAX_NO2 = "fax_no2";
	/** FAX番号3 */
	public static final String FAX_NO3 = "fax_no3";
	/** CSVから入力されたFAX番号 */
	public static final String CSV_FAX_NO = "csv_fax_no";
	/** 交通 */
	public static final String TRANSIT = "transit";
	/** 店舗情報 */
	public static final String SHOP_INFORMATION = "shop_information";
	/** 定休日 */
	public static final String HOLIDAY = "holiday";
	/** 営業時間 */
	public static final String BUSSINESS_HOURS = "business_hours";
	/** 席数 */
	public static final String SEATING = "seating";
	/** 顧客単価 */
	public static final String UNIT_PRICE = "unit_price";
	/** スタッフ */
	public static final String STAFF = "staff";
	/** URL1 */
	public static final String URL1 = "url1";
	/** 求人募集フラグ */
	public static final String JOB_OFFER_FLG = "job_offer_flg";
	/** ステータス */
	public static final String STATUS = "status";
	/** 表示番号 */
	public static final String DISP_ORDER = "disp_order";
	/** 表示フラグ */
	public static final String DISPLAY_FLG = "display_flg";
	/** アクセスキー */
	public static final String ACCESS_KEY = "access_key";




	/** バイト側業態 */
	public static final String ARBEIT_GYOTAI_ID = "arbeit_gyotai_id";
	/** バイト側職種 */
	public static final String ARBEIT_JOB = "arbeit_job";
	/** バイト側時給(下限) */
	public static final String ARBEIT_HOUR_SALARY = "arbeit_hour_salary";
	/** バイト側時給(上限) */
	public static final String ARBEIT_HOUR_MAX_SALARY = "arbeit_hour_max_salary";
	/** バイト側備考 */
	public static final String ARBEIT_REMARKS = "arbeit_remarks";
	/** バイト側時間 */
	public static final String ARBEIT_WORKING_HOUR = "arbeit_working_hour";
	/** バイト側待遇 */
	public static final String ARBEIT_TREATMENT = "arbeit_treatment";
	/** バイト側昇給タイミング */
	public static final String ARBEIT_RISE_SALARY_TIMING = "arbeit_rise_salary_timing";
	/** バイト側給料日 */
	public static final String ARBEIT_PAY_DAY = "arbeit_pay_day";
	/** バイト側都道府県ID */
	public static final String ARBEIT_TODOUHUKEN_ID = "arbeit_todouhuken_id";
	/** バイト側エリアID */
	public static final String ARBEIT_AREA_ID = "arbeit_area_id";
	/** バイト側サブエリアID */
	public static final String ARBEIT_SUB_AREA_ID = "arbeit_sub_area_id";
	/** バイト側住所 */
	public static final String ARBEIT_ADDRESS = "arbeit_address";
	/** バイト側電話番号 */
	public static final String ARBEIT_PHONE_NO = "arbeit_phone_no";
	/** バイト側電話受付時間 */
	public static final String ARBEIT_PHONE_RECEPTION_TIME = "arbeit_phone_reception_time";
	/** バイト側応募方法 */
	public static final String ARBEIT_APPLICATION_METHOD = "arbeit_application_method";
	/** バイト側応募資格 */
	public static final String ARBEIT_APPLICATION_CAPACITY = "arbeit_application_capacity";
	/** バイト側お店を一言で */
	public static final String ARBEIT_SHOP_SINGLE_WORD = "arbeit_shop_single_word";
	/** バイト側お店を一言で区分 */
	public static final String ARBEIT_SHOP_SINGLE_WORD_KBN = "arbeit_shop_single_word_kbn";
	/** バイト側フリーコメント */
	public static final String ARBEIT_FREE_COMMENT = "arbeit_free_comment";
	/** 業態テキスト */
	public static final String INDUSTRY_TEXT = "industry_text";
	/** キャッチコピー */
	public static final String CATCH_COPY = "catch_copy";
	/** 座席数区分 */
	public static final String SEAT_KBN = "seat_kbn";
	/** 客単価区分 */
	public static final String SALES_PER_CUSTOMER_KBN = "sales_per_customer_kbn";
	/** オープン日（年） */
	public static final String OPEN_DATE_YEAR = "open_date_year";
	/** オープン日（月） */
	public static final String OPEN_DATE_MONTH = "open_date_month";
	/** オープン日備考 */
	public static final String OPEN_DATE_NOTE = "open_date_note";
	/** オープン日表示期限 */
	public static final String OPEN_DATE_LIMIT_DISPLAY_DATE = "open_date_limit_display_date";
	/** キーワード検索 */
	public static final String KEYWORD_SEARCH = "keyword_search";

	public static final String PREVENT_SMOKE= "prevent_smoke";

// リニューアル後に不要
	/** 住所1(番-号まで) */
	public static final String ADDRESS1 = "address1";
	/** 住所2(ビル名など) */
	public static final String ADDRESS2 = "address2";


	/** バイト側フリーコメントの最大文字数 */
	public static final int ARBEIT_FREECOMMENT_MAX_LENGTH = 300;

	/** バイト側お店を一言での最大文字数 */
	public static final int ARBEIT_SHOP_SINGLE_WORD_MAX_LENGTH = 13;

	/** キャッチコピーの最大文字数 */
	public static final int CATCH_COPY_MAX_LENGTH = 30;


	/** 系列店舗ラベルグループリスト */
	public static final String T_SHOP_LIST_LABEL_GROUP_LIST = "tShopListLabelGroupList";

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
