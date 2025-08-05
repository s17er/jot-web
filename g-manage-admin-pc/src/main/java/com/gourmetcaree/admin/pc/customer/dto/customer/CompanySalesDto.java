package com.gourmetcaree.admin.pc.customer.dto.customer;

import java.io.Serializable;

/**
 * 担当会社・営業担当者DTOクラス
 * @author Takahiro Kimura
 * @version 1.0
 */
public class CompanySalesDto implements Serializable {


	/** シリアルバージョンUID */
	private static final long serialVersionUID = 2715163225189161317L;

	/** ID */
	public int id;

	/** 担当会社ID */
	public String companyId;

	/** 営業担当者ID */
	public String salesId;

	/** 削除のパス */
	public String deletePath;

	/** バージョン番号 */
	public Long version;

}