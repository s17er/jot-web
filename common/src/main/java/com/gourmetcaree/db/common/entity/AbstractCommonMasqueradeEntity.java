package com.gourmetcaree.db.common.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;


/**
 * なりすまし管理項目を持つテーブルのエンティティクラスです。
 * @author Keita Yamane
 * @version 1.0
 */
@MappedSuperclass
public abstract class AbstractCommonMasqueradeEntity extends AbstractCommonEntity {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -4241305743402146461L;

	/** なりすまし登録ユーザID */
	@Column(name="insert_masquerade_id")
	public String insertMasqueradeId;

	/** なりすまし登録日時 */
	@Column(name="insert_masquerade_datetime")
	public Timestamp insertMasqueradeDatetime;

	/** なりすまし更新ユーザID */
	@Column(name="update_masquerade_id")
	public String updateMasqueradeId;

	/** なりすまし更新日時 */
	@Column(name="update_masquerade_datetime")
	public Timestamp updateMasqueradeDatetime;

	/** なりすまし削除ユーザID */
	@Column(name="delete_masquerade_id")
	public String deleteMasqueradeId;

	/** なりすまし削除日時 */
	@Column(name="delete_masquerade_datetime")
	public Timestamp deleteMasqueradeDatetime;


	/** なりすまし登録ユーザID */
	public static final String INSERT_MASQUERADE_ID = "insert_masquerade_id";

	/** なりすまし登録日時 */
	public static final String INSERT_MASQUERADE_DATETIME = "insert_masquerade_datetime";

	/** なりすまし更新ユーザID */
	public static final String UPDATE_MASQUERADE_ID = "update_masquerade_id";

	/** なりすまし更新日時 */
	public static final String UPDATE_MASQUERADE_DATETIME = "update_masquerade_datetime";

	/** なりすまし削除ユーザID */
	public static final String DELETE_MASQUERADE_ID = "delete_masquerade_id";

	/** なりすまし削除日時 */
	public static final String DELETE_MASQUERADE_DATETIME = "delete_masquerade_datetime";

}
