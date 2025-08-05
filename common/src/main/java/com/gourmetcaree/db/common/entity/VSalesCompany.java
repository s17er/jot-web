package com.gourmetcaree.db.common.entity;

/* Code Generator Information.
 * generator Version 1.0.0 release 2007/10/10
 * generated Date Tue Aug 26 21:21:01 JST 2008
 */
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 営業担当者・会社情報ビュー用エンティティ
 * @author Takahiro Kimura
 * @version 1.0
 *
 */
@Entity
@Table(name="v_sales_company")
public class VSalesCompany extends AbstractBaseEntity implements Serializable {


	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1719233655290722870L;

	/** 営業担当者ID */
	public String id;

	/** 営業担当者名 */
	@Column(name="sales_name")
	public String salesName;

	/** 営業担当者名(カナ) */
	@Column(name="sales_name_kana")
	public String salesNameKana;

	/** 権限コード */
	@Column(name="authority_cd")
	public String authorityCd;

	/** メインメールアドレス */
	@Column(name="main_mail")
	public String mainMail;

	/** 会社ID */
	@Column(name="company_id")
	public String companyId;

	/** 会社名 */
	@Column(name="company_name")
	public String companyName;

	/** 削除フラグ */
	@Column(name="delete_flg")
	public int deleteFlg;

}
