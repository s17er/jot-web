package com.gourmetcaree.db.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = TWebIpPhone.TABLE_NAME)
public class TWebIpPhone extends AbstractCommonEntity {

	/**
	 *
	 */
	private static final long serialVersionUID = 6829393350619306850L;

	/** ID */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_web_ip_phone_id_gen")
	@SequenceGenerator(name = "t_web_ip_phone_id_gen", sequenceName = "t_web_ip_phone_id_seq", allocationSize = 1)
	@Column(name = ID)
	public Integer id;

	/** WEBデータID */
	@Column(name = WEB_ID)
	public Integer webId;

	/** 顧客ID */
	@Column(name = CUSTOMER_ID)
	public Integer customerId;

	/** 顧客電話番号 */
	@Column(name = CUSTOMER_TEL)
	public String customerTel;

	/** IP電話番号 */
	@Column(name = IP_TEL)
	public String ipTel;

	/** クライアントコード */
	@Column(name = CLIENT_CD)
	public String clientCd;

	/** 枝番 */
	@Column(name = EDA_NO)
	public Integer edaNo;


	/** テーブル名 */
	public static final String TABLE_NAME = "t_web_ip_phone";

	/** ID */
	public static final String ID = "id";

	/** WEBデータID */
	public static final String WEB_ID = "web_id";

	/** 顧客ID */
	public static final String CUSTOMER_ID = "customer_id";

	/** 顧客電話番号 */
	public static final String CUSTOMER_TEL = "customer_tel";

	/** IP電話番号 */
	public static final String IP_TEL = "ip_tel";

	/** クライアントコード */
	public static final String CLIENT_CD = "client_cd";

	/** 枝番 */
	public static final String EDA_NO = "eda_no";



}
