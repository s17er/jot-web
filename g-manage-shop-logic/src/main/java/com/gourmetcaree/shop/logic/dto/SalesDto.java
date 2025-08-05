package com.gourmetcaree.shop.logic.dto;

import java.io.Serializable;

/**
 * 営業担当者の情報を保持するクラスです。
 * @author Hiroyuki Sugimoto
 */
public class SalesDto implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -4595943521026061576L;

	/** ID */
	public Integer id;

	/** ログインID */
	public String loginId;

	/** 営業担当者名 */
	public String salesName;

	/** 会社ID */
	public String companyId;

	/** 会社名 */
	public String companyName;

	/** 権限コード */
	public Integer authorityCd;

}
