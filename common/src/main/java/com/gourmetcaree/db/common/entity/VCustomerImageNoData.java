package com.gourmetcaree.db.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="v_customer_image_no_data")
public class VCustomerImageNoData implements Serializable {

	private static final long serialVersionUID = 5656911591177722306L;

	/** ID */
	@Id
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

	/** テーブル名 */
	public static final String TABLE_NAME = "v_customer_image_no_data";

	/** ID */
	public static final String ID ="id";

	/** 顧客ID */
	public static final String CUSTOMER_ID ="customer_id";

	/** ファイル名 */
	public static final String FILE_NAME ="file_name";

	/** コンテントタイプ */
	public static final String CONTENT_TYPE ="content_type";

}
