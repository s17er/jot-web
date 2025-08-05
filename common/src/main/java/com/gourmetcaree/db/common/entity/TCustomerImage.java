package com.gourmetcaree.db.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 顧客の画像のエンティティクラスです。
 * @author hara
 *
 */
@Entity
@Table(name="t_customer_image")
public class TCustomerImage extends AbstractCommonMasqueradeEntity {

	private static final long serialVersionUID = -8146932007976121678L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_customer_image_id_gen")
	@SequenceGenerator(name="t_customer_image_id_gen", sequenceName="t_customer_image_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** 顧客ID */
	@Column(name="customer_id")
	public Integer customerId;

	/** ファイル名 */
	@Column(name="file_name")
	public String fileName;

	/** コンテントタイプ */
	@Column(name="content_type")
	public String contentType;

	/** 素材データ */
	@Column(name="material_data")
	public String materialData;

	/** テーブル名 */
	public static final String TABLE_NAME = "t_customer_image";

	/** ID */
	public static final String ID ="id";

	/** 顧客ID */
	public static final String CUSTOMER_ID ="customer_id";

	/** ファイル名 */
	public static final String FILE_NAME ="file_name";

	/** コンテントタイプ */
	public static final String CONTENT_TYPE ="content_type";

	/** 素材データ */
	public static final String MATERIAL_DATA ="material_data";

}