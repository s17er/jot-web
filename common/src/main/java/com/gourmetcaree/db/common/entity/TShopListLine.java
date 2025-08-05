package com.gourmetcaree.db.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.seasar.extension.jdbc.annotation.ReferentialConstraint;

@Entity
@Table(name="t_shop_list_line")
public class TShopListLine extends AbstractCommonMasqueradeEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 6087734344099374660L;

	/** id */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_shop_list_line_id_gen")
	@SequenceGenerator(name = "t_shop_list_line_id_gen", sequenceName = "t_shop_list_line_id_seq", allocationSize = 1)
	@Column(name = ID)
	public Integer id;

	/** 系列店舗ID */
	@Column(name = SHOP_LIST_ID)
	public Integer shopListId;

	/** 事業者コード */
	@Column(name = COMPANY_CD)
	public Integer companyCd;

	/** 路線コード */
	@Column(name = LINE_CD)
	public Integer lineCd;

	/** 駅コード */
	@Column(name = STATION_CD)
	public Integer stationCd;

	/** 移動手段区分 */
	@Column(name = TRANSPORTATION_KBN)
	public Integer transportationKbn;

	/** 所要時間（分） */
	@Column(name = TIME_REQUIRED_MINUTE)
	public Integer timeRequiredMinute;

	/** コメント */
	@Column(name = COMMENT)
	public String comment;

	/** 表示順 */
	@Column(name = DISPLAY_ORDER)
	public Integer displayOrder;

	/** 駅グループ */
	@ReferentialConstraint(enable = false)
	@OneToOne
	@JoinColumn(name = STATION_CD, referencedColumnName = STATION_CD)
	public VStationGroup vStationGroup;


	/** テーブル名 */
	public static final String TABLE_NAME = "t_shop_list_line";

	/** id */
	public static final String ID = "id";

	/** 系列店舗ID */
	public static final String SHOP_LIST_ID = "shop_list_id";

	/** 事業者コード */
	public static final String COMPANY_CD = "company_cd";

	/** 路線コード */
	public static final String LINE_CD = "line_cd";

	/** 駅コード */
	public static final String STATION_CD = "station_cd";

	/** 移動手段区分 */
	public static final String TRANSPORTATION_KBN = "transportation_kbn";

	/** 所要時間（分） */
	public static final String TIME_REQUIRED_MINUTE = "time_required_minute";

	/** コメント */
	public static final String COMMENT = "comment";

	/** 表示順 */
	public static final String DISPLAY_ORDER = "display_order";


}
