package com.gourmetcaree.db.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * ジャスキル会員マスタのエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="t_temp_juskill")
public class TTempJuskill extends AbstractCommonEntity implements Serializable{

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 513836841156014670L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_temp_juskill_id_gen")
	@SequenceGenerator(name="t_temp_juskill_id_gen", sequenceName="t_temp_juskill_id_gen", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** 仮会員ID */
	@Column(name = TEMP_MEMBER_ID)
	public Integer tempMemberId;

	/** 健康状態区分 */
	@Column(name = HEALTH_KBN)
	public Integer healthKbn;

	/** 刑事罰訴訟区分 */
	@Column(name = SIN_KBN)
	public Integer sinKbn;

	/** 暴力団区分 */
	@Column(name = GANGSTERS_KBN)
	public Integer gangstersKbn;

	/** 履歴修正フラグ */
	@Column(name = HISTORY_MODIFICATION_FLG)
	public Integer historyModificationFlg;

	/** 入退社修正フラグ */
	@Column(name = ON_LEAVING_MODIFICATION_FLG)
	public Integer onLeavingModificationFlg;

	/** 取得資格修正フラグ */
	@Column(name = QUALIFICATION_MODIFICATION_FLG)
	public Integer qualificationModificationFlg;

	/** テーブル名 */
	public static final String TABLE_NAME = "t_temp_juskill";

	/** ID */
	public static final String ID = "id";

	/** 仮会員ID */
	public static final String TEMP_MEMBER_ID = "temp_member_id";

	/** 健康状態区分 */
	public static final String HEALTH_KBN = "health_kbn";

	/** 刑事罰訴訟区分 */
	public static final String SIN_KBN = "sin_kbn";

	/** 暴力団区分 */
	public static final String GANGSTERS_KBN = "gangsters_kbn";

	/** 履歴修正フラグ */
	public static final String HISTORY_MODIFICATION_FLG = "history_modification_flg";

	/** 入退社修正フラグ */
	public static final String ON_LEAVING_MODIFICATION_FLG = "on_leaving_modification_flg";

	/** 取得資格修正フラグ */
	public static final String QUALIFICATION_MODIFICATION_FLG = "qualification_modification_flg";


}