package com.gourmetcaree.admin.service.property;

import org.apache.commons.lang.math.NumberUtils;

import com.gourmetcaree.db.common.entity.AbstractCommonEntity;

/**
 *
 * レポート一覧用の条件を受け渡しするクラス
 * @author Takahiro Ando
 *
 */
public class ReportListSqlProperty extends BaseProperty {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 4378706412161039673L;

	/** 検索条件マップ */
	public int areaCd;

	/** 最大件数 */
	public String maxRow;

	/**
	 * 最大件数が空かどうかのゲッター
	 * @return
	 */
	public boolean isNoLimitFlg() {
		if (NumberUtils.toInt(maxRow) == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 最大件数をintで取得
	 * @return
	 */
	public int getLimitNum() {
		return NumberUtils.toInt(maxRow);
	}

	/**
	 * 削除フラグ(未削除)を取得します。
	 * ※SQLファイルで使用するためのgetter
	 * @return 削除フラグ(未削除)
	 */
	public int getDeleteFlg() {
	    return AbstractCommonEntity.DeleteFlgValue.NOT_DELETED;
	}
}
