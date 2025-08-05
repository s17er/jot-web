package com.gourmetcaree.db.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * WEBデータ路線図のエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="t_web_route")
public class TWebRoute extends AbstractCommonEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_web_route_id_gen")
	@SequenceGenerator(name="t_web_route_id_gen", sequenceName="t_web_route_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** WEBデータID */
	@Column(name="web_id")
	public Integer webId;

	/** 鉄道会社ID */
	@Column(name="railroad_id")
	public Integer railroadId;

	/** 路線ID */
	@Column(name="route_id")
	public Integer routeId;

	/** 駅ID */
	@Column(name="station_id")
	public Integer stationId;

	/** 対象外フラグ */
	@Column(name="not_target_flg")
	public Integer notTargetFlg;

	/** WEBデータ */
	@ManyToOne
	@JoinColumn(name="web_id")
	public TWeb tWeb;


	/** テーブル名 */
	public static final String TABLE_NAME = "t_web_route";

	/** ID */
	public static final String ID ="id";

	/** WEBデータID */
	public static final String WEB_ID ="web_id";

	/** 鉄道会社ID */
	public static final String RAILROAD_ID ="railroad_id";

	/** 路線ID */
	public static final String ROUTE_ID ="route_id";

	/** 駅ID */
	public static final String STATION_ID ="station_id";

	/** 対象外フラグ */
	public static final String NOT_TARGET_FLG = "not_target_flg";

	/** WEBデータ */
	public static final String T_WEB ="t_web";
}