package com.gourmetcaree.db.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 店舗一覧路線エンティティ
 * @author Takehiro Nakamori
 *
 */
@Entity
@Table(name="t_shop_list_route")
public class TShopListRoute extends AbstractCommonMasqueradeEntity {

	/**
	 *
	 */
	private static final long serialVersionUID = -5989182324670033871L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_shop_list_route_id_gen")
	@SequenceGenerator(name="t_shop_list_route_id_gen", sequenceName="t_shop_list_route_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;


	/** 店舗一覧ID */
	@Column(name="shop_list_id")
	public Integer shopListId;

	/** 鉄道会社ID */
	@Column(name="railroad_id")
	public Integer railroadId;

	/** 路線ID */
	@Column(name="route_id")
	public Integer routeId;

	/** 駅ID */
	@Column(name="station_id")
	public Integer stationId;

	@Column(name="comment")
	public String comment;

	/** 表示順 */
	@Column(name="disp_order")
	public Integer dispOrder;

	/** バイトフラグ */
	@Column(name="arbeit_flg")
	public Integer arbeitFlg;

	/** テーブル名 */
	public static final String TABLE_NAME = "t_shop_list_route";

	/** ID */
	public static final String ID = "id";

	/** 店舗一覧ID */
	public static final String SHOP_LIST_ID = "shop_list_id";

	/** 鉄道会社ID */
	public static final String RAILROAD_ID = "railroad_id";

	/** 路線ID */
	public static final String ROUTE_ID = "route_id";

	/** 駅ID */
	public static final String STATION_ID = "station_id";

	/** コメント */
	public static final String COMMENT = "comment";

	/** 表示順 */
	public static final String DISP_ORDER = "disp_order";

	/** バイトフラグ */
	@Column(name="arbeit_flg")
	public static final String ARBEIT_FLG = "arbeit_flg";

	/**
	 * バイトフラグの値定数
	 * @author Takehiro Nakamori
	 *
	 */
	public static final class ArbeitFlgValue {
		/** グルメキャリー */
		public static final int GOURMETCAREE = 0;
		/** アルバイト */
		public static final int ARBEIT = 1;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
