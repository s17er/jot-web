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
 * 素材のエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="t_shop_list_material")
public class TShopListMaterial extends AbstractCommonMasqueradeEntity implements Serializable {


	/** シリアルバージョンUID */
	private static final long serialVersionUID = 3450534199236084205L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_shop_list_material_id_gen")
	@SequenceGenerator(name="t_shop_list_material_id_gen", sequenceName="t_shop_list_material_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** 店舗一覧ID */
	@Column(name="shop_list_id")
	public Integer shopListId;

	/** 素材区分 */
	@Column(name="material_kbn")
	public Integer materialKbn;

	/** ファイル名 */
	@Column(name="file_name")
	public String fileName;

	/** コンテントタイプ */
	@Column(name="content_type")
	public String contentType;

	/** 素材データ */
	@Column(name="material_data")
	public String materialData;

	/** 対象外フラグ */
	@Column(name="not_target_flg")
	public Integer notTargetFlg;

	/** 顧客画像ID */
	@Column(name="customer_image_id")
	public Integer customerImageId;

	/** 店舗一覧データ */
	@ManyToOne
	@JoinColumn(name="shop_list_id")
	public TShopList tShopList;


	/** テーブル名 */
	public static final String TABLE_NAME = "t_shop_list_material";

	/** ID */
	public static final String ID ="id";

	/** 店舗一覧ID */
	public static final String SHOP_LIST_ID ="shop_list_id";

	/** 素材区分 */
	public static final String MATERIAL_KBN ="material_kbn";

	/** ファイル名 */
	public static final String FILE_NAME ="file_name";

	/** コンテントタイプ */
	public static final String CONTENT_TYPE ="content_type";

	/** 素材データ */
	public static final String MATERIAL_DATA ="material_data";

	/** 対象外フラグ */
	public static final String NOT_TARGET_FLG = "not_target_flg";

	/** 顧客画像ID */
	public static final String CUSTOMER_IMAGE_ID = "customer_image_id";

	/** WEBデータ */
	public static final String T_WEB ="t_web";
}