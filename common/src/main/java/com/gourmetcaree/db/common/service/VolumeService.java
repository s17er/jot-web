package com.gourmetcaree.db.common.service;

import static com.gourmetcaree.common.util.SqlUtils.asc;
import static com.gourmetcaree.common.util.SqlUtils.desc;
import static jp.co.whizz_tech.commons.WztStringUtil.toCamelCase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.AbstractCommonEntity;
import com.gourmetcaree.db.common.entity.MVolume;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.ExistDataException;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * 号数マスタのサービスクラスです。
 * @version 1.0
 */
public class VolumeService extends AbstractGroumetCareeBasicService<MVolume> {

	/** デフォルトの号数 */
	private static final int DEFAULT_DISPLAY_NUM = 20;

	/** デフォルトのエリアコード */
	private static final int DEFAULT_AREA_CD = 1;

	/**
	 * エリアコードをキーに次の号数を取得する。
	 * @param areaCd
	 * @return 次の号数
	 * @throws SNoResultException
	 */
	public int getVolumeByAreaCd(int areaCd) {


		try {
			Integer maxVolume;

			StringBuilder sb = new StringBuilder();
			List<Object> list = new ArrayList<Object>();

			sb.append("SELECT max(volume) FROM m_volume ");
			sb.append("WHERE area_cd = ? AND delete_flg = ? ");

			list.add(areaCd);
			list.add(AbstractCommonEntity.DeleteFlgValue.NOT_DELETED);

			maxVolume = jdbcManager.selectBySql(Integer.class, sb.toString(), list.toArray())
								.disallowNoResult()
								.getSingleResult();

			return maxVolume + 1;

			// 号数がない場合は"1"を返す
		} catch (SNoResultException e) {
			return 1;
		}

	}

	/**
	 * エリアコード、最大表示件数から号数一覧を取得
	 * @param areaCd エリアコード
	 * @param sortKey ソートキー
	 * @param maxRow 最大表示件数
	 * @return entityList 号数エンティティリスト
	 * @throws WNoResultException
	 */
	public List<MVolume> getVolumeList(String areaCd, String maxRow) throws WNoResultException {

		try {
			StringBuilder sb = new StringBuilder();
			List<Object> list = new ArrayList<Object>();

			sb.append("SELECT * FROM m_volume ");
			sb.append("WHERE area_cd = ? AND delete_flg = ? ");
			sb.append("ORDER BY volume DESC ");

			list.add(NumberUtils.toInt(areaCd, DEFAULT_AREA_CD));
			list.add(DeleteFlgKbn.NOT_DELETED);

			if (StringUtils.isNotBlank(maxRow)) {
				sb.append("LIMIT  ? ");
				list.add(NumberUtils.toInt(maxRow, DEFAULT_DISPLAY_NUM));
			}

			List<MVolume> entityList = jdbcManager.selectBySql(MVolume.class, sb.toString(), list.toArray())
										.disallowNoResult()
										.getResultList();

			return entityList;

		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}

	/**
	 * エリアコード、号数をキーに号数データを検索
	 * @param areaCd エリアコード
	 * @param volume 号数
	 * @return 号数が存在していない場合、trueを返す
	 */
	public boolean existVolumeDataByAreaVolume(int areaCd, int volume) {

		MVolume entity = jdbcManager.from(MVolume.class)
						.where(new SimpleWhere()
						.eq("areaCd", areaCd)
						.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED)
						.eq("volume", volume))
						.getSingleResult();

		return entity == null ? true : false;
	}

	/**
	 * 同じエリアに同一の号数が存在していない場合に、号数を登録
	 * @param entity 号数エンティティ
	 * @throws ExistDataException
	 */
	public void inputMVolume(MVolume entity) throws ExistDataException {

		// 号数チェック
		if (!existVolumeDataByAreaVolume(entity.areaCd, entity.volume)) {
			throw new ExistDataException("号数マスタにエリアと号数を元に検索した結果が存在しませんでした。");
		}

		// 号数登録
		insert(entity);
	}

	/**
	 * 現在の日付を範囲に持つエンティティを取得します。
	 * レコードが複数存在する場合は、掲載開始日が直近のものを取得します。
	 * @param areaCd
	 * @param date
	 * @return
	 * @throws WNoResultException
	 */
	public MVolume getCurrentEntity(int areaCd, Date date) throws WNoResultException {

		SimpleWhere where = new SimpleWhere()
							.eq(toCamelCase(MVolume.AREA_CD), areaCd)
							.le(toCamelCase(MVolume.POST_START_DATETIME), date)
							.ge(toCamelCase(MVolume.POST_END_DATETIME), date)
							.eq(toCamelCase(MVolume.DELETE_FLG), DeleteFlgKbn.NOT_DELETED)
							;

		List<MVolume> retList = findByCondition(where, desc(MVolume.POST_START_DATETIME) + ", " + desc(MVolume.ID));

		//未取得の場合はWNoResultExceptionが発生するのでチェックせず1件目を取得。
		return retList.get(0);
	}

	/**
	 * 現在の日付より未来の直近のエンティティを取得します。
	 * レコードが複数存在する場合は、掲載開始日が直近のものを取得します。
	 * @param areaCd
	 * @param date
	 * @return
	 * @throws WNoResultException
	 */
	public MVolume getNextCurrentEntity(int areaCd, Date date) throws WNoResultException {

		SimpleWhere where = new SimpleWhere()
							.eq(toCamelCase(MVolume.AREA_CD), areaCd)
							.gt(toCamelCase(MVolume.POST_START_DATETIME), date)
							.eq(toCamelCase(MVolume.DELETE_FLG), DeleteFlgKbn.NOT_DELETED)
							;

		List<MVolume> retList = findByCondition(where, asc(MVolume.POST_START_DATETIME) + ", " + desc(MVolume.ID));

		//未取得の場合はWNoResultExceptionが発生するのでチェックせず1件目を取得。
		return retList.get(0);
	}
}