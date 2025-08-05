package com.gourmetcaree.db.common.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/**
 * 事前登録を管理するマスタテーブル
 * @author Takehiro Nakamori
 *
 */
@Entity
@Table(name = "m_advanced_registration")
public class MAdvancedRegistration extends AbstractCommonEntity{

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -1009088925508091593L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="m_advanced_registration_id_gen")
	@SequenceGenerator(name="m_advanced_registration_id_gen", sequenceName="m_advanced_registration_id_seq", allocationSize=1)
	@Column(name = ID)
	public Integer id;

	/** 事前登録名 */
	@Column(name = ADVANCED_REGISTRATION_NAME)
	public String advancedRegistrationName;

	/** 事前登録省略名 */
	@Column(name = ADVANCED_REGISTRATION_SHORT_NAME)
	public String advancedRegistrationShortName;

	/** 開始日時 */
	@Column(name = TERM_START_DATETIME)
	public Timestamp termStartDatetime;

	/** 終了日時 */
	@Column(name = TERM_END_DATETIME)
	public Timestamp termEndDatetime;

	/** 表示順 */
	@Column(name = DISP_ORDER)
	public Integer dispOrder;

	/** テストフラグ */
	@Column(name = TEST_FLG)
	public Integer testFlg;

	/** エリアコード */
	@Column(name = AREA_CD)
	public Integer areaCd;

	/** テーブル名 */
	public static final String TABLE_NAME = "m_advanced_registration";

	/** ID */
	public static final String ID = "id";

	/** 事前登録名 */
	public static final String ADVANCED_REGISTRATION_NAME = "advanced_registration_name";

	/** 事前登録省略名 */
	public static final String ADVANCED_REGISTRATION_SHORT_NAME = "advanced_registration_short_name";

	/** 開始日時 */
	public static final String TERM_START_DATETIME = "term_start_datetime";

	/** 終了日時 */
	public static final String TERM_END_DATETIME = "term_end_datetime";

	/** 表示順 */
	public static final String DISP_ORDER = "disp_order";

	/** テストフラグ */
	public static final String TEST_FLG = "test_flg";

	/** エリアコード */
	public static final String AREA_CD = "area_cd";


	/**
	 * 名前区分の定数定義
	 * @author Takehiro Nakamori
	 *
	 */
	public static final class NameKbn {
		/** 通常名 */
		public static final int NORMAL_NAME = 1;

		/** 短縮名 */
		public static final int SHORT_NAME = 2;
	}

	public static final class TestFlgValue {
		public static final int NORMAL = 0;
		public static final int TEST = 1;
	}
}
