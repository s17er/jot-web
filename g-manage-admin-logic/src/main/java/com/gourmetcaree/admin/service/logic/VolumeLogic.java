package com.gourmetcaree.admin.service.logic;

import java.util.ArrayList;

import org.seasar.extension.jdbc.exception.SNoResultException;

import com.gourmetcaree.db.common.entity.AbstractCommonEntity;
import com.gourmetcaree.db.common.entity.MArea;
import com.gourmetcaree.db.common.entity.MVolume;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.maintenance.dto.volume.DetailDto;

/**
 * 号数データロジッククラス
 * @author Makoto Otani
 * @version 1.0
 *
 */
public class VolumeLogic extends AbstractAdminLogic {

	/**
	 * 号数データの詳細を取得する
	 * @param id 号数Id
	 * @return 号数データの詳細
	 * @throws WNoResultException 検索結果が無い場合はエラー
	 */
	public DetailDto getVolumeDetail(int id) throws WNoResultException {

		// SQL文
		StringBuilder sqlStr = new StringBuilder();
		// SQLのパラメータ
		ArrayList<Object> params = new ArrayList<Object>();

		// SQL文の作成
		createSearchSql(sqlStr, params, id);
		DetailDto dto = null;

		try {
			// データの取得
			dto = jdbcManager.
							selectBySql(DetailDto.class, sqlStr.toString(), params.toArray()).
							disallowNoResult().
							getSingleResult();

		} catch (SNoResultException e) {
			throw new WNoResultException();
		}

		return dto;

	}

	/**
	 * 号数データ詳細を検索用のSQLを作成する
	 * @param sb SQLを保持するオブジェクト
	 * @param params パラメータ保持用オブジェクト
	 * @param id 号数Id
	 */
	private void createSearchSql(StringBuilder sqlStr, ArrayList<Object> params, int id) {

		// SQLの作成
		sqlStr.append(" SELECT ");
			sqlStr.append(" T1").append(".* ").append(", ");
			sqlStr.append(" T2").append(".").append(MArea.AREA_NAME);
		sqlStr.append(" FROM ");
			sqlStr.append(MVolume.TABLE_NAME).append(" T1 ");
		sqlStr.append(" LEFT OUTER JOIN ");
			sqlStr.append(MArea.TABLE_NAME).append(" T2 ");
		sqlStr.append( "ON ");
			sqlStr.append("T1").append(".").append(MVolume.AREA_CD);
			sqlStr.append(" = ");
			sqlStr.append("T2").append(".").append(MArea.AREA_CD);
		sqlStr.append(" WHERE ");
			sqlStr.append("T1").append(".").append(MVolume.ID);
			sqlStr.append(" = ");
			sqlStr.append("? ");
		sqlStr.append(" AND ");
			sqlStr.append("T1").append(".").append(MVolume.DELETE_FLG);
			sqlStr.append(" = ");
			sqlStr.append("? ");

		// パラメータの設定
		// Id
		params.add(id);
		// 削除フラグ = 0
		params.add(AbstractCommonEntity.DeleteFlgValue.NOT_DELETED);

	}
}
