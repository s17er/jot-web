package com.gourmetcaree.admin.service.property;

import com.gourmetcaree.db.common.entity.AbstractCommonEntity;

/**
 *
 * レポート一覧用の条件を受け渡しするクラス
 * @author Takahiro Ando
 *
 */
public class ReportListDetailSqlProperty extends BaseProperty {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 4378706412161039673L;

	/** 号数ID */
	public int volumeId;

	/**
	 * 削除フラグ(未削除)を取得します。
	 * ※SQLファイルで使用するためのgetter
	 * @return 削除フラグ(未削除)
	 */
	public int getDeleteFlg() {
	    return AbstractCommonEntity.DeleteFlgValue.NOT_DELETED;
	}
}
