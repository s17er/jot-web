package com.gourmetcaree.arbeitsys.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

import com.gourmetcaree.arbeitsys.constants.MArbeitConstants.ArbeitSite;

/**
 * グルメdeバイト用mst_routeエンティティです。<br />
 * 路線用
 * @author Takehiro Nakamori
 *
 */
@MappedSuperclass
public class AbstractMstRoute implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1800965435252683675L;

	/** ID */
	@Id
	@Column(name="id")
	public Integer id;

	/** 鉄道会社ID */
	@Column(name="railload_id")
	public Integer railloadId;


	/** 路線名 */
	@Column(name="name")
	public String name;


	/** ID */
	public static final String ID = "id";

	/** 鉄道会社ID */
	public static final String RAILLOAD_ID = "railload_id";

	/** 路線名 */
	public static final String NAME = "name";


	/**
	 * 路線エンティティクラスを取得
	 * @param todouhukenId 都道府県ID
	 * @return
	 */
	public static Class<? extends AbstractMstRoute> getRailloadClass(int todouhukenId) {
		switch (ArbeitSite.getArbeitSiteEnum(todouhukenId)) {

		case KANSAI_SITE_CONST:
			return MstRouteKansai.class;

		case SENDAI_SITE_CONST:
			return MstRouteSendai.class;

		case SHUTOKEN_SITE_CONST:
		default:
			return MstRouteShutoken.class;
		}
	}

	/**
	 * 路線テーブルを取得
	 * @param todouhukenId 都道府県ID
	 * @return
	 */
	public static String getRailloadTable(int todouhukenId) {
		switch (ArbeitSite.getArbeitSiteEnum(todouhukenId)) {

		case KANSAI_SITE_CONST:
			return MstRouteKansai.class.getAnnotation(Table.class).name();

		case SENDAI_SITE_CONST:
			return MstRouteSendai.class.getAnnotation(Table.class).name();

		case SHUTOKEN_SITE_CONST:
		default:
			return MstRouteShutoken.class.getAnnotation(Table.class).name();
		}
	}

}
