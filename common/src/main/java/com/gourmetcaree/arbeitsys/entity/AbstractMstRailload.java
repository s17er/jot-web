package com.gourmetcaree.arbeitsys.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

import com.gourmetcaree.arbeitsys.constants.MArbeitConstants.ArbeitSite;


/**
 * @author Takehiro Nakamori
 *
 */
@MappedSuperclass
public class AbstractMstRailload implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -746808722433018198L;

	/** ID */
	@Id
	@Column(name="id")
	public Integer id;


	/** 鉄道会社名 */
	@Column(name="name")
	public String name;


	/** ID */
	public static final String ID = "id";

	/** 鉄道会社名 */
	public static final String NAME = "name";



	/**
	 * 鉄道会社エンティティクラスを取得
	 * @param todouhukenId 都道府県ID
	 * @return
	 */
	public static Class<? extends AbstractMstRailload> getRailloadClass(int todouhukenId) {
		switch (ArbeitSite.getArbeitSiteEnum(todouhukenId)) {

		case KANSAI_SITE_CONST:
			return MstRailloadKansai.class;

		case SENDAI_SITE_CONST:
			return MstRailloadSendai.class;

		case SHUTOKEN_SITE_CONST:
		default:
			return MstRailloadShutoken.class;
		}
	}

	/**
	 * 鉄道会社テーブルを取得
	 * @param todouhukenId
	 * @return
	 */
	public static String getRailloadTable(int todouhukenId) {
		switch (ArbeitSite.getArbeitSiteEnum(todouhukenId)) {

		case KANSAI_SITE_CONST:
			return MstRailloadKansai.class.getAnnotation(Table.class).name();

		case SENDAI_SITE_CONST:
			return MstRailloadSendai.class.getAnnotation(Table.class).name();

		case SHUTOKEN_SITE_CONST:
		default:
			return MstRailloadShutoken.class.getAnnotation(Table.class).name();
		}
	}
}
