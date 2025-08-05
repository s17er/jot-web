package com.gourmetcaree.db.common.entity;

import java.io.Serializable;
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
@Table(name="t_interest")
public class TInterest extends AbstractCommonEntity implements Serializable {


	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_interest_id_gen")
	@SequenceGenerator(name="t_interest_id_gen", sequenceName="t_interest_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** 気になる日時 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="interest_datetime")
	public Date interestDatetime;

	/** 会員ID */
	@Column(name="member_id")
	public Integer memberId;

	/** WebID */
	@Column(name="web_id")
	public Integer webId;

	/** 応募ID */
	@Column(name="application_id")
	public Integer applicationId;

	/** 表示フラグ */
	@Column(name="display_flg")
	public Integer displayFlg;


	/** テーブル名 */
	public static final String TABLE_NAME = "t_interest";

	/** ID */
	public static final String ID = "id";

	/** 気になる日時 */
	public static final String INTEREST_DATETIME = "interest_datetime";

	/** 会員ID */
	public static final String MEMBER_ID = "member_id";

	/** WebID */
	public static final String WEB_ID = "web_id";

	/** 応募ID */
	public static final String APPLICATION_ID = "application_id";

	/** 表示フラグ */
	public static final String DISPLAY_FLG = "display_flg";
}
