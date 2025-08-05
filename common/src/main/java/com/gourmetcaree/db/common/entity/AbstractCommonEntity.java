package com.gourmetcaree.db.common.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import com.gourmetcaree.db.common.enums.DeleteFlgKbn;


/**
 * 共通管理項目を持つテーブルのエンティティクラスです。
 * @author Takahiro Ando
 * @version 1.0
 */
@MappedSuperclass
public abstract class AbstractCommonEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -8164556049539946155L;

	/** 削除フラグ */
	@Column(name="delete_flg")
	@Enumerated
	public DeleteFlgKbn deleteFlg;

	/** 登録ユーザID */
	@Column(name="insert_user_id")
	public String insertUserId;

	/** 登録日時 */
	@Column(name="insert_datetime")
	public Timestamp insertDatetime;

	/** 更新ユーザID */
	@Column(name="update_user_id")
	public String updateUserId;

	/** 更新日時 */
	@Column(name="update_datetime")
	public Timestamp updateDatetime;

	/** 削除ユーザID */
	@Column(name="delete_user_id")
	public String deleteUserId;

	/** 削除日時 */
	@Column(name="delete_datetime")
	public Timestamp deleteDatetime;

	/** バージョン番号 */
	@Version
	public Long version;



	/**
	 * 削除フラグ値を定義します。
	 * @author Takahiro Ando
	 * @version 1.0
	 */
	public static class DeleteFlgValue {

		/** 未削除 */
		public static final int NOT_DELETED = 0;

		/** 削除 */
		public static final int DELETED = 1;
	}

//	public static enum DeleteFlg {
//		NOT_DELETED,
//		DELETED
//		;
//
//	}

	/** 削除フラグ */
	public static final String DELETE_FLG = "delete_flg";

	/** 登録ユーザID */
	public static final String INSERT_USER_ID = "insert_user_id";

	/** 登録日時 */
	public static final String INSERT_DATETIME = "insert_datetime";

	/** 更新ユーザID */
	public static final String UPDATE_USER_ID = "update_user_id";

	/** 更新日時*/
	public static final String UPDATE_DATETIME = "update_datetime";

	/** 削除ユーザID*/
	public static final String DELETE_USER_ID = "delete_user_id";

	/** 削除日時*/
	public static final String DELETE_DATETIME = "delete_datetime";

	/** バージョン番号*/
	public static final String VERSION = "version";

}
