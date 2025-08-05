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
 * WEBデータ系列店舗のエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="t_web_shop_list")
public class TWebShopList extends AbstractCommonEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 295935770751562792L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_web_shop_list_id_gen")
	@SequenceGenerator(name="t_web_shop_list_id_gen", sequenceName="t_web_shop_list_id_seq", allocationSize=1)
	@Column(name=ID)
	public Integer id;

	/** WEBデータID */
	@Column(name=WEB_ID)
	public Integer webId;

	/** 店舗ID */
	@Column(name=SHOP_LIST_ID)
	public Integer shopListId;

	/** indeed出力フラグ */
	@Column(name=INDEED_FEED_FLG)
	public Integer indeedFeedFlg;

	/** テーブル名 */
	public static final String TABLE_NAME = "t_web_shop_list";

	/** ID */
	public static final String ID ="id";
	/** WEBデータID */
	public static final String WEB_ID = "web_id";
	/** 店舗ID */
	public static final String SHOP_LIST_ID = "shop_list_id";
	/** indeed出力フラグ */
	public static final String INDEED_FEED_FLG = "indeed_feed_flg";
	/** インディード出力対象 */
	public static final int INDEED_FEED = 1;

}