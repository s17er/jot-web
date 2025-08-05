package com.gourmetcaree.db.common.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.gourmetcaree.accessor.web.WebDataAccessor;
import com.gourmetcaree.common.util.GourmetCareeUtil;

/**
 * 公開側で表示可能なWEBデータVIEWのエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="v_release_web")
public class VReleaseWeb extends AbstractBaseEntity implements WebDataAccessor {

    /** シリアルバージョンUID */
	private static final long serialVersionUID = 8828563293558572930L;

	/** ID */
    @Id
    @Column(name="id")
    public Integer id;

    /** ステータス */
    @Column(name="status")
    public Integer status;

    /** 掲載確定担当者ID */
    @Column(name="fixed_user_id")
    public Integer fixedUserId;

    /** 掲載確定日時 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="fixed_datetime")
    public Date fixedDatetime;

    /** 掲載依頼担当者ID */
    @Column(name="request_user_id")
    public Integer requestUserId;

    /** 掲載依頼日時 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="request_datetime")
    public Date requestDatetime;

    /** エリアコード */
    @Column(name="area_cd")
    public Integer areaCd;

    /** 号数ID */
    @Column(name="volume_id")
    public Integer volumeId;

    /** 顧客ID */
    @Column(name="customer_id")
    public Integer customerId;

    /** コピー元WEBID */
    @Column(name="source_web_id")
    public Integer sourceWebId;

    /** 原稿名 */
    @Column(name="manuscript_name")
    public String manuscriptName;

    /** サイズ区分 */
    @Column(name="size_kbn")
    public Integer sizeKbn;

    /** 応募フォームフラグ */
    @Column(name="application_form_kbn")
    public Integer applicationFormKbn;

    /** 店舗見学区分 */
	@Column(name="observation_kbn")
	public Integer observationKbn;

    /** 動画フラグ */
    @Column(name="movie_flg")
    public Integer movieFlg;

	/** 複数勤務地店舗フラグ */
    @Column(name="multi_working_place_flg")
	public Integer multiWorkingPlaceFlg;

	/** 所属会社ID */
    @Column(name="company_id")
    public Integer companyId;

    /** 営業担当者ID */
    @Column(name="sales_id")
    public Integer salesId;

    /** 業種区分1 */
    @Column(name="industry_kbn1")
    public Integer industryKbn1;

    /** 業種区分2 */
    @Column(name="industry_kbn2")
    public Integer industryKbn2;

    /** 業種区分3 */
    @Column(name="industry_kbn3")
    public Integer industryKbn3;

    /** 適職診断区分 */
    @Column(name="reasonable_kbn")
    public Integer reasonableKbn;

    /** 店舗数区分 */
    @Column(name="shops_kbn")
    public Integer shopsKbn;

    /** 募集職種 */
    @Column(name="recruitment_job")
    public String recruitmentJob;

    /** 仕事内容 */
    @Column(name="work_contents")
    public String workContents;

    /** 給与 */
    @Column(name="salary")
    public String salary;

    /** 求める人物像 */
    @Column(name="person_hunting")
    public String personHunting;

    /** 勤務時間 */
    @Column(name="working_hours")
    public String workingHours;

    /** 勤務地エリア・最寄駅 */
    @Column(name="working_place")
    public String workingPlace;

    /** 勤務地詳細 */
    @Column(name="working_place_detail")
    public String workingPlaceDetail;

    /** 待遇 */
    @Column(name="treatment")
    public String treatment;

    /** 休日休暇 */
    @Column(name="holiday")
    public String holiday;

    /** 客席数・坪数 */
    @Column(name="seating")
    public String seating;

    /** 客単価 */
    @Column(name="unit_price")
    public String unitPrice;

    /** 営業時間 */
    @Column(name="business_hours")
    public String businessHours;

    /** オープン日 */
    @Column(name="opening_day")
    public String openingDay;

    /** 会社情報 */
    @Column(name="shop_information")
    public String shopInformation;

    /** ホームページ１ */
    @Column(name="homepage1")
    public String homepage1;

    /** ホームページコメント1 */
	@Column(name="homepage_comment1")
	public String homepageComment1;

    /** ホームページ２ */
    @Column(name="homepage2")
    public String homepage2;

    /** ホームページコメント2 */
	@Column(name="homepage_comment2")
	public String homepageComment2;

    /** ホームページ３ */
    @Column(name="homepage3")
    public String homepage3;

    /** ホームページコメント3 */
	@Column(name="homepage_comment3")
	public String homepageComment3;

    /** 電話番号/受付時間 */
    @Column(name="phone_receptionist")
    public String phoneReceptionist;

    /** 連絡メール区分 */
    @Column(name="communication_mail_kbn")
    public Integer communicationMailKbn;

    /** 連絡メール */
    @Column(name="mail")
    public String mail;

    /** 応募方法 */
    @Column(name="application_method")
    public String applicationMethod;

    /** 面接地住所/交通 */
    @Column(name="address_traffic")
    public String addressTraffic;

    /** 地図タイトル */
    @Column(name="map_title")
    public String mapTitle;

    /** 地図用住所 */
    @Column(name="map_address")
    public String mapAddress;

    /** メッセージ */
    @Column(name="message")
    public String message;

    /** キャッチコピー1 */
    @Column(name="catch_copy1")
    public String catchCopy1;

    /** 文章1 */
    @Column(name="sentence1")
    public String sentence1;

    /** キャッチコピー2 */
    @Column(name="catch_copy2")
    public String catchCopy2;

    /** 文章2 */
    @Column(name="sentence2")
    public String sentence2;

    /** キャッチコピー3 */
    @Column(name="catch_copy3")
    public String catchCopy3;

    /** 文章3 */
    @Column(name="sentence3")
    public String sentence3;

    /** 文章4 */
    @Column(name="sentence4")
    public String sentence4;

    /** キャプションA */
    @Column(name="captionA")
    public String captiona;

    /** キャプションB */
    @Column(name="captionB")
    public String captionb;

    /** キャプションC */
    @Column(name="captionC")
    public String captionc;

    /** 人事担当者より */
    @Column(name="comment")
    public String comment;

	/** 注目店舗フラグ */
	@Column(name="attention_shop_flg")
	public Integer attentionShopFlg;

	/** 注目店舗文章 */
	@Column(name="attention_shop_sentence")
	public String attentionShopSentence;

	/** ここに注目タイトル */
	@Column(name="attention_here_title")
	public String attentionHereTitle;

	/** ここに注目文章 */
	@Column(name="attention_here_sentence")
	public String attentionHereSentence;

	/** 動画URL */
	@Column(name = MOVIE_URL)
	public String movieUrl;

	/** 動画コメント */
	@Column(name = MOVIE_COMMENT)
	public String movieComment;

    /** メモ */
    @Column(name="memo")
    public String memo;

    /** 誌面原稿番号 */
    @Column(name="magazine_manuscript_no")
    public String magazineManuscriptNo;

    /** 誌面号数 */
    @Column(name="magazine_volume")
    public String magazineVolume;

	/** 誌面データフラグ */
	@Column(name="magazine_flg")
	public Integer magazineFlg;

	/** トップインタビューURL */
	@Column(name="top_interview_url")
	public String topInterviewUrl;

	/** 合同説明会参加区分 */
	@Column(name="briefing_present_kbn")
	public Integer briefingPresentKbn;

	/** アクセスコード */
    @Column(name="access_cd")
    public String accessCd;

    /** 応募テストフラグ */
    @Column(name="application_test_flg")
    public Integer applicationTestFlg;

	/** 対象外フラグ */
	@Column(name="not_target_flg")
	public Integer notTargetFlg;

	/** 最終編集日時 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="last_edit_datetime")
	public Date lastEditDatetime;

	/** 掲載開始日時 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="post_start_datetime")
    public Date postStartDatetime;

    /** 店舗一覧表示区分 */
	@Column(name="shop_list_display_kbn")
	public Integer shopListDisplayKbn;

    /** 掲載終了日時 */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="post_end_datetime")
    public Date postEndDatetime;

	/** 顧客名 */
	@Column(name="customer_name")
	public String customerName;

    /** 求人識別番号 */
	@Column(name="web_no")
	public String webNo;


    /** テーブル名 */
    public static final String TABLE_NAME = "v_release_web";

    /** ID */
    public static final String ID ="id";

    /** ステータス */
    public static final String STATUS = "status";

    /** 掲載確定担当者ID */
    public static final String FIXED_USER_ID = "fixed_user_id";

    /** 掲載確定日時 */
    public static final String FIXED_DATETIME = "fixed_datetime";

    /** 掲載依頼担当者ID */
    public static final String REQUEST_USER_ID = "request_user_id";

    /** 掲載依頼日時 */
    public static final String REQUEST_DATETIME = "request_datetime";

    /** エリアコード */
    public static final String AREA_CD = "area_cd";

    /** 号数ID */
    public static final String VOLUME_ID = "volume_id";

    /** 顧客ID */
    public static final String CUSTOMER_ID = "customer_id";

    /** コピー元WEBID */
    public static final String SOURCE_WEB_ID = "source_web_id";

    /** 原稿名 */
    public static final String MANUSCRIPT_NAME = "manuscript_name";

    /** サイズ区分 */
    public static final String SIZE_KBN = "size_kbn";

    /** 応募フォームフラグ */
    public static final String APPLICATION_FORM_KBN = "application_form_kbn";

    /** 店舗見学区分 */
	public static final String OBSERVATION_KBN = "observation_kbn";

    /** 動画フラグ */
    public static final String MOVIE_FLG = "movie_flg";

    /** 複数勤務地店舗フラグ */
	public static final String MULTI_WORKING_PLACE_FLG = "multi_working_place_flg";

    /** 所属会社ID */
    public static final String COMPANY_ID = "company_id";

    /** 営業担当者ID */
    public static final String SALES_ID = "sales_id";

    /** 業種区分1 */
    public static final String INDUSTRY_KBN1 = "industry_kbn1";

    /** 業種区分2 */
    public static final String INDUSTRY_KBN2 = "industry_kbn2";

    /** 業種区分3 */
    public static final String INDUSTRY_KBN3 = "industry_kbn3";

    /** 適職診断区分 */
    public static final String REASONABLE_KBN = "reasonable_kbn";

    /** 店舗数区分 */
    public static final String SHOPS_KBN = "shops_kbn";

    /** 募集職種 */
    public static final String RECRUITMENT_JOB = "recruitment_job";

    /** 仕事内容 */
    public static final String WORK_CONTENTS = "work_contents";

    /** 給与 */
    public static final String SALARY = "salary";

    /** 求める人物像 */
    public static final String PERSON_HUNTING = "person_hunting";

    /** 勤務時間 */
    public static final String WORKING_HOURS = "working_hours";

    /** 勤務地エリア・最寄駅 */
    public static final String WORKING_PLACE = "working_place";

    /** 勤務地詳細 */
    public static final String WORKING_PLACE_DETAIL = "working_place_detail";

    /** 待遇 */
    public static final String TREATMENT = "treatment";

    /** 休日休暇 */
    public static final String HOLIDAY = "holiday";

    /** 客席数・坪数 */
    public static final String SEATING = "seating";

    /** 客単価 */
    public static final String UNIT_PRICE = "unit_price";

    /** 営業時間 */
    public static final String BUSINESS_HOURS = "business_hours";

    /** オープン日 */
    public static final String OPENING_DAY = "opening_day";

    /** 会社情報 */
    public static final String SHOP_INFORMATION = "shop_information";

    /** ホームページ１ */
    public static final String HOMEPAGE1 = "homepage1";

    /** ホームページコメント1 */
	public static final String HOMEPAGE_COMMENT1 = "homepage_comment1";

    /** ホームページ２ */
    public static final String HOMEPAGE2 = "homepage2";

    /** ホームページコメント2 */
	public static final String HOMEPAGE_COMMENT2 = "homepage_comment2";

    /** ホームページ３ */
    public static final String HOMEPAGE3 = "homepage3";

    /** ホームページコメント3 */
	public static final String HOMEPAGE_COMMENT3 = "homepage_comment3";

    /** 電話番号/受付時間 */
    public static final String PHONE_RECEPTIONIST = "phone_receptionist";

    /** 連絡メール区分 */
    public static final String COMMUNICATION_MAIL_KBN = "communication_mail_kbn";

    /** 連絡メール */
    public static final String MAIL = "mail";

    /** 応募方法 */
    public static final String APPLICATION_METHOD = "application_method";

    /** 面接地住所/交通 */
    public static final String ADDRESS_TRAFFIC = "address_traffic";

    /** 地図タイトル */
    public static final String MAP_TITLE = "map_title";

    /** 地図用住所 */
    public static final String MAP_ADDRESS = "map_address";

    /** メッセージ */
    public static final String MESSAGE = "message";

    /** キャッチコピー1 */
    public static final String CATCH_COPY1 = "catch_copy1";

    /** 文章1 */
    public static final String SENTENCE1 = "sentence1";

    /** キャッチコピー2 */
    public static final String CATCH_COPY2 = "catch_copy2";

    /** 文章2 */
    public static final String SENTENCE2 = "sentence2";

    /** キャッチコピー3 */
    public static final String CATCH_COPY3 = "catch_copy3";

    /** 文章3 */
    public static final String SENTENCE3 = "sentence3";

    /** 文章4 */
    public static final String SENTENCE4 = "sentence4";

    /** キャプションA */
    public static final String CAPTIONA = "captionA";

    /** キャプションB */
    public static final String CAPTIONB = "captionB";

    /** キャプションC */
    public static final String CAPTIONC = "captionC";

    /** 人事担当者より */
    public static final String COMMENT = "comment";

    /** メモ */
    public static final String MEMO = "memo";

    /** 誌面原稿番号 */
    public static final String MAGAZINE_MANUSCRIPT_NO = "magazine_manuscript_no";

    /** 誌面号数 */
    public static final String MAGAZINE_VOLUME = "magazine_volume";

	/** 誌面フラグ */
	public static final String MAGAZINE_FLG = "magazine_flg";

    /** アクセスコード */
    public static final String ACCESS_CD = "access_cd";

    /** 応募テストフラグ */
    public static final String APPLICATION_TEST_FLG = "application_test_flg";

    /** 注目店舗フラグ */
    public static final String ATTENTION_SHOP_FLG = "attention_shop_flg";

    /** ここに注目タイトル */
	public static final String ATTENTION_HERE_TITLE = "attention_here_title";

	/** ここに注目文章 */
	public static final String ATTENTION_HERE_SENTENCE = "attention_here_sentence";

	/** 動画URL */
	public static final String MOVIE_URL = "movie_url";

	/** 動画コメント */
	public static final String MOVIE_COMMENT = "movie_comment";

	/**トップインタビューURL */
	public static final String TOP_INTERVIEW_URL = "top_interview_url";

	/** 合同説明会参加区分 */
	public static final String BRIEFING_PRESENT_KBN = "briefing_present_kbn";

	/** 対象外フラグ */
	public static final String NOT_TARGET_FLG = "not_target_flg";

	/** 最終編集日時 */
	public static final String LAST_EDIT_DATETIME = "last_edit_datetime";

	/** 掲載開始日時 */
    public static final String POST_START_DATETIME = "post_start_datetime";

    /** 掲載終了日時 */
    public static final String POST_END_DATETIME = "post_end_datetime";

	/** 顧客名 */
    public static final String CUSTOMER_NAME = "customer_name";

    /** 求人識別番号 */
    public static final String WEB_NO = "web_no";

    /** 店舗一覧表示区分 */
	public static final String SHOP_LIST_DISPLAY_KBN = "shop_list_display_kbn";

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public List<Integer> getIndustryList() {
		return GourmetCareeUtil.joinObjects(Integer.class, industryKbn1, industryKbn2, industryKbn3);
	}

	@Override
	public String getManuscriptName() {
		return manuscriptName;
	}

	@Override
	public Integer getVolumeId() {
		return volumeId;
	}

	@Override
	public String getWorkingPlace() {
		return workingPlace;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}