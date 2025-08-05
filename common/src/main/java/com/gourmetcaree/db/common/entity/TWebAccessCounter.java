package com.gourmetcaree.db.common.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * WEBデータアクセスカウンターのエンティティクラスです。
 * @author Takehiro Nakamori
 *
 */
@Entity
@Table(name="t_web_access_counter")
public class TWebAccessCounter extends AbstractBaseEntity implements Serializable{

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 3292358478954746791L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_web_access_counter_id_gen")
	@SequenceGenerator(name="t_web_access_counter_id_gen", sequenceName="t_web_access_counter_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** WEBデータID */
	@Column(name="web_id")
	public Integer webId;

	/** エリアCD */
	@Column(name="area_cd")
	public Integer areaCd;

	/** 端末区分 */
	@Column(name="terminal_kbn")
	public String terminalKbn;

	/** アクセス数 */
	@Column(name="access_count")
	public Integer accessCount;

	/** アクセス年 */
	@Column(name="year")
	public String year;

	/** アクセス月 */
	@Column(name="month")
	public String month;

	/** アクセス日 */
	@Column(name="day")
	public String day;

	/** 最終アクセス日時 */
	@Column(name="last_access_datetime")
	public Timestamp lastAccessDatetime;

	/** テーブル名 */
	public static final String TABLE_NAME = "t_web_access_counter";

	/** ID */
	public static final String ID = "id";

	/** WEBデータID */
	public static final String WEB_ID = "web_id";

	/** エリアCD */
	public static final String AREA_CD = "area_cd";

	/** 端末区分 */
	public static final String TERMINAL_KBN = "terminal_kbn";

	/** アクセス数 */
	public static final String ACCESS_COUNT = "access_count";

	/** 年 */
	public static final String YEAR = "year";

	/** 月 */
	public static final String MONTH = "month";

	/** 日 */
	public static final String DAY = "day";

	/** 最終アクセス日時 */
	public static final String LAST_ACCESS_DATETIME = "last_access_datetime";


}
