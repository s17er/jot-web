package com.gourmetcaree.db.common.entity;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "t_arbeit_application")
public class TArbeitApplication extends AbstractCommonMasqueradeEntity {


	private static final long serialVersionUID = -5876732335163114664L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_arbeit_application_id_gen")
	@SequenceGenerator(name="t_arbeit_application_id_gen", sequenceName="t_arbeit_application_id_seq", allocationSize=1)
	@Column(name = "id")
	public Integer id;


	/** 応募日時 */
	@Column(name = "application_datetime")
	public Timestamp applicationDatetime;


	/** エリアコード */
	@Column(name = "area_cd")
	public Integer areaCd;


	/** メールID */
	@Column(name = "mail_id")
	public Integer mailId;

	/** 店舗一覧ID */
	@Column(name = "shop_list_id")
	public Integer shopListId;


	/** 顧客ID */
	@Column(name = "customer_id")
	public Integer customerId;


	/** 会員ID */
	@Column(name = "member_id")
	public Integer memberId;


	/** アルバイト会員ID */
	@Column(name = "arbeit_member_id")
	public Integer arbeitMemberId;

	/** 応募名 */
	@Column(name = "application_name")
	public String applicationName;


	/** 名前 */
	@Column(name = "name")
	public String name;


	/** 名前(カナ) */
	@Column(name = "name_kana")
	public String nameKana;


	/** 性別区分 */
	@Column(name = "sex_kbn")
	public Integer sexKbn;


	/** 年齢 */
	@Column(name = "age")
	public Integer age;


	/** 郵便番号 */
	@Column(name = "zip_cd")
	public String zipCd;


	/** 都道府県コード */
	@Column(name = "prefectures_cd")
	public Integer prefecturesCd;


	/** 市区 */
	@Column(name = "municipality")
	public String municipality;


	/** 住所 */
	@Column(name = "address")
	public String address;


	/** 電話番号 */
	@Column(name = "phone_no")
	public String phoneNo;


	/** メールアドレス */
	@Column(name = "mail_address")
	public String mailAddress;


	/** 現在の職業区分 */
	@Column(name = "current_job_kbn")
	public Integer currentJobKbn;


	/** 勤務可能時期区分 */
	@Column(name = "possible_entry_term_kbn")
	public Integer possibleEntryTermKbn;


	/** 飲食店勤務の経験区分 */
	@Column(name = "food_exp_kbn")
	public Integer foodExpKbn;


	/** 応募職種 */
	@Column(name = "application_job")
	public String applicationJob;


	/** 希望連絡時間・連絡方法 */
	@Column(name = "connection_time")
	public String connectionTime;


	/** 自己ＰＲ・要望 */
	@Column(name = "application_self_pr")
	public String applicationSelfPr;


	/** 端末区分 */
	@Column(name = "terminal_kbn")
	public Integer terminalKbn;

	/** 会員フラグ */
	@Column(name="member_flg")
	public Integer memberFlg;

	/** グルメdeバイト会員フラグ */
	@Column(name="arbeit_member_flg")
	public Integer arbeitMemberFlg;


	/** メモ */
	@Column(name="memo")
	public String memo;

	/** メモの更新日時 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="memo_update_datetime")
	public Date memoUpdateDatetime;

	/** 選考対象フラグ */
	@Column(name="selection_flg")
	public Integer selectionFlg;

	/** テーブル名 */
	public static final String TABLE_NAME = "t_arbeit_application";

	/** ID */
	public static final String ID = "id";


	/** 応募日時 */
	public static final String APPLICATION_DATETIME = "application_datetime";


	/** エリアコード */
	public static final String AREA_CD = "area_cd";


	/** メールID */
	public static final String MAIL_ID = "mail_id";

	/** 店舗一覧ID */
	public static final String SHOP_LIST_ID = "shop_list_id";

	/** 顧客ID */
	public static final String CUSTOMER_ID = "customer_id";


	/** 会員ID */
	public static final String MEMBER_ID = "member_id";


	/** アルバイト会員ID */
	public static final String ARBEIT_MEMBER_ID = "arbeit_member_id";

	/** 応募名 */
	public static final String APPLICATION_NAME = "application_name";


	/** 名前 */
	public static final String NAME = "name";


	/** 名前(カナ) */
	public static final String NAME_KANA = "name_kana";


	/** 性別区分 */
	public static final String SEX_KBN = "sex_kbn";


	/** 生年月日 */
	public static final String AGE = "age";


	/** 郵便番号 */
	public static final String ZIP_CD = "zip_cd";


	/** 都道府県コード */
	public static final String PREFECTURES_CD = "prefectures_cd";


	/** 市区 */
	public static final String MUNICIPALITY = "municipality";


	/** 住所 */
	public static final String ADDRESS = "address";


	/** 電話番号 */
	public static final String PHONE_NO = "phone_no";


	/** メールアドレス */
	public static final String MAIL_ADDRESS = "mail_address";


	/** 現在の職業区分 */
	public static final String CURRENT_JOB_KBN = "current_job_kbn";


	/** 勤務可能時期区分 */
	public static final String POSSIBLE_ENTRY_TERM_KBN = "possible_entry_term_kbn";


	/** 飲食店勤務の経験区分 */
	public static final String FOOD_EXP_KBN = "food_exp_kbn";


	/** 応募職種 */
	public static final String APPLICATION_JOB = "application_job";


	/** 希望連絡時間・連絡方法 */
	public static final String CONNECTION_TIME = "connection_time";


	/** 自己ＰＲ・要望 */
	public static final String APPLICATION_SELF_PR = "application_self_pr";


	/** 端末区分 */
	public static final String TERMINAL_KBN = "terminal_kbn";


	/** 会員フラグ */
	public static final String MEMBER_FLG = "member_flg";
	/** グルメdeバイト会員フラグ */
	public static final String ARBEIT_MEMBER_FLG = "arbeit_member_flg";


	/** メモ */
	public static final String MEMO = "memo";

	/** メモの更新日時 */
	public static final String MEMO_UPDATE_DATETIME = "memo_update_datetime";


	/** 選考対象フラグ */
	public static final String SELECTION_FLG = "selection_flg";
}
