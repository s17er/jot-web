package com.gourmetcaree.admin.pc.maintenance.dto.sales;

import java.io.Serializable;

/**
 * 営業担当者DTOクラス
 * @author Takahiro Kimura
 * @version 1.0
 */
public class SalesDto implements Serializable {


	/** シリアルバージョンUID */
	private static final long serialVersionUID = 30547988126147043L;

	/** 営業担当者ID */
	public int id;

	/** 氏名 */
	public String salesName;

	/** 氏名カナ */
	public String salesNameKana;

	/** 権限名 */
	public String authorityCdName;

	/** 会社名 */
	public String companyName;

	/** メインメールアドレス */
	public String mainMail;

	/** 詳細のパス */
	public String detailPath;

}