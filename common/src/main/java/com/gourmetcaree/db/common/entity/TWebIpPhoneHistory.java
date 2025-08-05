package com.gourmetcaree.db.common.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * IP通話応募履歴テーブル
 * @author nakamori
 *
 */
@Entity
@Table(name = TWebIpPhoneHistory.TABLE_NAME)
public class TWebIpPhoneHistory extends AbstractCommonEntity {


	/**
	 *
	 */
	private static final long serialVersionUID = 7672041730059368687L;

	/** ID */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_web_ip_phone_history_id_gen")
	@SequenceGenerator(name = "t_web_ip_phone_history_id_gen", sequenceName = "t_web_ip_phone_history_id_seq", allocationSize = 1)
	@Column(name = ID)
	public Integer id;

	/** WEBデータID */
	@Column(name = WEB_ID)
	public Integer webId;

	/** 顧客ID */
	@Column(name = CUSTOMER_ID)
	public Integer customerId;

	/** 枝番 */
	@Column(name = EDA_NO)
	public Integer edaNo;

	/** 求職者電話番号 */
	@Column(name = MEMBER_TEL)
	public String memberTel;

	/** 求職者電話番号のMD5 */
	@Column(name = MEMBER_TEL_MD5)
	public String memberTelMd5;

	/** APIで登録した時に生成される電話番号 */
	@Column(name = IP_PHONE_TEL)
	public String ipPhoneTel;

	/** アタッチコード */
	@Column(name = ATTACH_CD)
	public String attachCd;

	/** B-Leg発信元電話番号 */
	@Column(name = CUSTOMER_DISPLAY_TEL)
	public String customerDisplayTel;

	/** 顧客電話番号 */
	@Column(name = CUSTOMER_TEL)
	public String customerTel;

	/** 求職者に電話がつながった日時 */
	@Column(name = CONNECT_TO_MEMBER_DATETIME)
	public Timestamp connectToMemberDatetime;

	/** 顧客が応答した時間 */
	@Column(name = RESPONSE_CUSTOMER_DATETIME)
	public Timestamp responseCustomerDatetime;

	/** 求職者かシステムが電話を切った日時 */
	@Column(name = MEMBER_HANG_UP_DATETIME)
	public Timestamp memberHangUpDatetime;

	/** 顧客かシステムが電話を切った日時 */
	@Column(name = CUSTOMER_HANG_UP_DATETIME)
	public Timestamp customerHangUpDatetime;

	/** 求職者の電話種別 */
	@Column(name = MEMBER_TEL_TYPE)
	public String memberTelType;

	/** 顧客の電話種別 */
	@Column(name = CUSTOMER_TEL_TYPE)
	public String customerTelType;

	/** 求職者の通話時間(秒) */
	@Column(name = MEMBER_TEL_SECOND)
	public Integer memberTelSecond;

	/** 顧客の通話時間(秒) */
	@Column(name = CUSTOMER_TEL_SECOND)
	public Integer customerTelSecond;

	/** 通話ステータス */
	@Column(name = TEL_STATUS_NAME)
	public String telStatusName;

	/** 通話切断理由 */
	@Column(name = HANG_UP_REASON)
	public String hangUpReason;

	/** 通話ステータス詳細 */
	@Column(name = TEL_STATUS_DETAIL)
	public String telStatusDetail;

	/** クライアントＣＤ */
	@Column(name = CLIENT_CD)
	public String clientCd;

	/** 顧客名 */
	@Column(name = CUSTOMER_NAME)
	public String customerName;

	/** 企業コード */
	@Column(name = COMPANY_CD)
	public String companyCd;

	/** サービスコード */
	@Column(name = SERVICE_CD)
	public String serviceCd;

	/** 課金電話番号体系 */
	@Column(name = PURCHACE_MODEL)
	public String purchaceModel;

	/** 求職者の電話をコールノートがキャッチした日時 */
	@Column(name = CALL_NOTE_CAUGHT_MEMBER_TEL_DATETIME)
	public Timestamp callNoteCaughtMemberTelDatetime;

	/** コールノートが顧客に発信を開始した日時 */
	@Column(name = CALL_NOTE_BEGIN_CALL_TO_CUSTOMER)
	public Timestamp callNoteBeginCallToCustomer;


	/** テーブル名 */
	public static final String TABLE_NAME = "t_web_ip_phone_history";

	/** ID */
	public static final String ID = "id";

	/** WEBデータID */
	public static final String WEB_ID = "web_id";

	/** 顧客ID */
	public static final String CUSTOMER_ID = "customer_id";

	/** 枝番 */
	public static final String EDA_NO = "eda_no";

	/** 求職者電話番号 */
	public static final String MEMBER_TEL = "member_tel";

	/** 求職者電話番号のMD5 */
	public static final String MEMBER_TEL_MD5 = "member_tel_md5";

	/** APIで登録した時に生成される電話番号 */
	public static final String IP_PHONE_TEL = "ip_phone_tel";

	/** アタッチコード */
	public static final String ATTACH_CD = "attach_cd";

	/** B-Leg発信元電話番号 */
	public static final String CUSTOMER_DISPLAY_TEL = "customer_display_tel";

	/** 顧客電話番号 */
	public static final String CUSTOMER_TEL = "customer_tel";

	/** 求職者に電話がつながった日時 */
	public static final String CONNECT_TO_MEMBER_DATETIME = "connect_to_member_datetime";

	/** 顧客が応答した時間 */
	public static final String RESPONSE_CUSTOMER_DATETIME = "response_customer_datetime";

	/** 求職者かシステムが電話を切った日時 */
	public static final String MEMBER_HANG_UP_DATETIME = "member_hang_up_datetime";

	/** 顧客かシステムが電話を切った日時 */
	public static final String CUSTOMER_HANG_UP_DATETIME = "customer_hang_up_datetime";

	/** 求職者の電話種別 */
	public static final String MEMBER_TEL_TYPE = "member_tel_type";

	/** 顧客の電話種別 */
	public static final String CUSTOMER_TEL_TYPE = "customer_tel_type";

	/** 求職者の通話時間(秒) */
	public static final String MEMBER_TEL_SECOND = "member_tel_second";

	/** 顧客の通話時間(秒) */
	public static final String CUSTOMER_TEL_SECOND = "customer_tel_second";

	/** 通話ステータス */
	public static final String TEL_STATUS_NAME = "tel_status_name";

	/** 通話切断理由 */
	public static final String HANG_UP_REASON = "hang_up_reason";

	/** 通話ステータス詳細 */
	public static final String TEL_STATUS_DETAIL = "tel_status_detail";

	/** クライアントＣＤ */
	public static final String CLIENT_CD = "client_cd";

	/** 顧客名 */
	public static final String CUSTOMER_NAME = "customer_name";

	/** 企業コード */
	public static final String COMPANY_CD = "company_cd";

	/** サービスコード */
	public static final String SERVICE_CD = "service_cd";

	/** 課金電話番号体系 */
	public static final String PURCHACE_MODEL = "purchace_model";

	/** 求職者の電話をコールノートがキャッチした日時 */
	public static final String CALL_NOTE_CAUGHT_MEMBER_TEL_DATETIME = "call_note_caught_member_tel_datetime";

	/** コールノートが顧客に発信を開始した日時 */
	public static final String CALL_NOTE_BEGIN_CALL_TO_CUSTOMER = "call_note_begin_call_to_customer";



	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

}
