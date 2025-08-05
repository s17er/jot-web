package com.gourmetcaree.db.common.service;

import static com.gourmetcaree.common.util.SqlUtils.*;
import static jp.co.whizz_tech.commons.WztStringUtil.*;
import static org.seasar.framework.util.StringUtil.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.record.formula.functions.T;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.util.StringUtil;

import com.gourmetcaree.arbeitsys.entity.MstTodouhuken;
import com.gourmetcaree.common.builder.sql.SqlBuilder.SqlCondition;
import com.gourmetcaree.common.builder.sql.web.AppliableWebSqlBuilder;
import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.constants.MStatusConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.MSpecial;
import com.gourmetcaree.db.common.entity.MVolume;
import com.gourmetcaree.db.common.entity.TShopListAttribute;
import com.gourmetcaree.db.common.entity.TWeb;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * WEBデータのサービスクラスです。
 * @version 1.0
 */
public class WebService extends AbstractGroumetCareeBasicService<TWeb> {

	/** UPDATE時にNULLへのUPDATEを許可するカラム名の保持配列 */
	private static final String[] NULL_ENABLED_COLUMN_LIST = new String[]{
																	toCamelCase(TWeb.VOLUME_ID),			// 号数ID
																	toCamelCase(TWeb.CUSTOMER_ID),			// 顧客ID
																	toCamelCase(TWeb.INDUSTRY_KBN1),		// 業種区分1
																	toCamelCase(TWeb.INDUSTRY_KBN2),		// 業種区分2
																	toCamelCase(TWeb.INDUSTRY_KBN3),		// 業種区分3
																	toCamelCase(TWeb.REASONABLE_KBN),		// 適職診断区分
																	toCamelCase(TWeb.SHOPS_KBN),			// 店舗数区分
																	toCamelCase(TWeb.COMMUNICATION_MAIL_KBN),// 連絡メール区分
																	toCamelCase(TWeb.LATITUDE),				// 緯度
																	toCamelCase(TWeb.LONGITUDE),			// 経度
																	toCamelCase(TWeb.MT_BLOG_ID),           // インタビュー
																	toCamelCase(TWeb.SERIAL_PUBLICATION), //連載
																};

	/* (非 Javadoc)
	 * @see com.gourmetcaree.db.common.service.AbstractGroumetCareeBasicService#setCommonInsertColmun(java.lang.Object)
	 */
	@Override
	protected void setCommonInsertColmun(TWeb entity) {
		super.setCommonInsertColmun(entity);
		// 登録時は必ず最終編集日時に登録日時が入るようにする
		entity.lastEditDatetime = entity.insertDatetime;
	}

	/**
	 * NULLを許可するカラムを含んだテーブルに対してUPDATEを行います。
	 * @param entity WEBデータエンティティ
	 * @return UPDATE件数
	 */
	public int updateWithNull(TWeb entity) {
		return updateWithNull(entity, NULL_ENABLED_COLUMN_LIST);
	}


	/**
	 * WEBデータのID配列からWEBデータのリストを取得
	 * @throws WNoResultException, NumberFormatException
	 */
	public List<TWeb> getWebDataByIdArray(String[] webId) throws WNoResultException, NumberFormatException {

		try {
			List<Integer> idList = new ArrayList<Integer>();

			for (String id : webId) {
				idList.add(Integer.parseInt(id));
			}

			String sortKey = createSortKey();

			List<TWeb> entityList = jdbcManager.from(TWeb.class)
										.leftOuterJoin("mCompany", "mCompany.deleteFlg = ?", DeleteFlgKbn.NOT_DELETED)
										.where(new SimpleWhere()
										.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED)
										.in("id", idList.toArray()))
										.orderBy(sortKey)
										.disallowNoResult()
										.getResultList();

			return entityList;
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}


//	/**
//	 * WEBデータのIDからWEBデータを取得
//	 * @throws WNoResultException, NumberFormatException
//	 */
//	public TWeb getWebDataById(Integer webId) throws WNoResultException {
//		try {
//			return jdbcManager.from(TWeb.class)
//					.leftOuterJoin("tWebAttributeList", "tWebAttributeList.deleteFlg = ?", DeleteFlgKbn.NOT_DELETED)
//					.leftOuterJoin("tWebRouteList", "tWebRouteList.deleteFlg = ?", DeleteFlgKbn.NOT_DELETED)
//					.leftOuterJoin("tWebSpecialList", "tWebSpecialList.deleteFlg = ?", DeleteFlgKbn.NOT_DELETED)
//					.where(new SimpleWhere()
//					.eq("id", webId)
//					.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
//					.disallowNoResult()
//					.limit(1)
//					.getSingleResult();
//		} catch (SNoResultException e) {
//			throw new WNoResultException();
//		} catch (SNonUniqueResultException e) {
//			throw new FraudulentProcessException("同一のwebIdが複数あります。");
//		}
//	}

	/**
	 * WEBデータのIDリストから代理店担当(承認依頼)のWEBデータのリストを取得
	 * @throws WNoResultException, NumberFormatException
	 */
	public List<TWeb> getAgencyWebData(String[] webId) throws WNoResultException, NumberFormatException {

		try {
			List<Integer> idList = new ArrayList<Integer>();

			for (String id : webId) {
				idList.add(Integer.parseInt(id));
			}

			String sortKey = createSortKey();

			List<TWeb> entityList = jdbcManager.from(TWeb.class)
										.innerJoin("mCompany", "mCompany.deleteFlg = ? and mCompany.agencyFlg = ?"
												, DeleteFlgKbn.NOT_DELETED, MTypeConstants.AgencyFlg.AGENCY)
										.innerJoin("mCustomer", "mCustomer.deleteFlg = ?", DeleteFlgKbn.NOT_DELETED)
										.innerJoin("mSales", "mSales.deleteFlg = ?", DeleteFlgKbn.NOT_DELETED)
										.where(new SimpleWhere()
										.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED)
										.eq("status", MStatusConstants.DBStatusCd.REQ_APPROVAL)
										.in("id", idList.toArray()))
										.orderBy(sortKey)
										.disallowNoResult()
										.getResultList();

			return entityList;
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}

	/**
	 * WEBデータのID配列からWEBデータのリストを取得
	 * @throws WNoResultException, NumberFormatException
	 */
	public List<TWeb> getWebDataListByIdArray(String[] webId) throws WNoResultException, NumberFormatException {

		try {
			List<Integer> idList = new ArrayList<Integer>();

			for (String id : webId) {
				idList.add(Integer.parseInt(id));
			}

			String sortKey = createSortKey();

			List<TWeb> entityList = jdbcManager.from(TWeb.class)
										.leftOuterJoin("mCompany", "mCompany.deleteFlg = ?", DeleteFlgKbn.NOT_DELETED)
										.leftOuterJoin("mSales", "mSales.deleteFlg = ?", DeleteFlgKbn.NOT_DELETED)
										.leftOuterJoin("mCustomer", "mCustomer.deleteFlg = ?", DeleteFlgKbn.NOT_DELETED)
										.leftOuterJoin("mVolume", "mVolume.deleteFlg = ?", DeleteFlgKbn.NOT_DELETED)
										.where(new SimpleWhere()
										.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED)
										.in("id", idList.toArray()))
										.orderBy(sortKey)
										.disallowNoResult()
										.getResultList();

			return entityList;
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}

//	/**
//	 * WEBデータのID配列からWEBデータのリストを取得
//	 * @throws WNoResultException, NumberFormatException
//	 */
//	public List<TWeb> getWebDataById(String[] webId) throws WNoResultException, NumberFormatException {
//
//		try {
//			List<Integer> idList = new ArrayList<Integer>();
//
//			for (String id : webId) {
//				idList.add(Integer.parseInt(id));
//			}
//
//			List<TWeb> entityList = jdbcManager.from(TWeb.class)
//										.leftOuterJoin("tWebAttributeList", "tWebAttributeList.deleteFlg = ?", DeleteFlgKbn.NOT_DELETED)
//										.leftOuterJoin("tWebRouteList", "tWebRouteList.deleteFlg = ?", DeleteFlgKbn.NOT_DELETED)
//										.leftOuterJoin("tMaterialList", "tMaterialList.deleteFlg = ?", DeleteFlgKbn.NOT_DELETED)
//										.leftOuterJoin("tWebSpecialList", "tWebSpecialList.deleteFlg = ?", DeleteFlgKbn.NOT_DELETED)
//										.where(new SimpleWhere()
//										.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED)
//										.in("id", idList.toArray()))
//										.disallowNoResult()
//										.getResultList();
//
//			return entityList;
//		} catch (SNoResultException e) {
//			throw new WNoResultException();
//		}
//	}

	/**
	 * ソート順を返す
	 * @return ソート順文字列
	 */
	private String createSortKey() {

		String[] sortKey = new String[] {
				// ソート順を設定
				asc( camelize(TWeb.STATUS)),	// 表示順
				desc(camelize(TWeb.VOLUME_ID)),		// 号数IDの降順
				desc(camelize(TWeb.COMPANY_ID)),	// 会社IDの降順
				desc(camelize(TWeb.SALES_ID)),		// 営業担当者の降順
				desc(camelize(TWeb.ID))				// WEBIDの降順
			};
			// カンマ区切りにして返す
			return SqlUtils.createCommaStr(sortKey);
	}


	/**
	 * WEBデータをIDとエリアコードで検索します。
	 * @param id WEBID
	 * @param areaCd エリアコード
	 * @return WEBデータエンティティ
	 * @throws WNoResultException データが存在しない場合はエラー
	 */
	public TWeb findByIdAndAreaCd(Integer id, Integer areaCd) throws WNoResultException {

		// 引数が不正な場合は処理しない
		if (id == null || areaCd == null) {
			throw new WNoResultException();
		}

		SimpleWhere where = new SimpleWhere()
			.eq(camelize(TWeb.AREA_CD), areaCd)	// エリアコード
			;

		// データの検索
		return findById(id, where);



	}


    /**
     * 応募可能なWEBデータを取得
     * 掲載開始日時が現在より前で、ステータスが3or5(掲載確定or掲載終了)のものを対象としたWEBデータを取得
     *
     * @param id
     *            WEBデータID
     * @param areaCd
     *            エリアコード
     * @return 応募可能原稿
     * @throws WNoResultException
     *             見つからなかった場合にスロー
     */
    public TWeb findAppliableDataById(Integer id, Integer areaCd) throws WNoResultException {
        if (id == null || areaCd == null) {
            throw new WNoResultException("idかareaCdがNULLです。");
        }

        SqlCondition condition = new AppliableWebSqlBuilder(id, areaCd).build();

        try {
            return jdbcManager.selectBySql(TWeb.class, condition.getSql(), condition.getParamsArray())
                    .disallowNoResult()
                    .getSingleResult();
        } catch (NoResultException e) {
            throw new WNoResultException(String.format("応募可能データが存在しません。webId:%d エリアコード:%d", id, areaCd), e);
        }
    }

	/**
	 * 求人識別番号に求人IDと同じ値を更新
	 * @param entity
	 * @return
	 */
	public int updateWebNoById(TWeb entity) {
		entity.webNo = entity.id;
		return jdbcManager.update(entity).excludesNull().execute();
	}

	/**
	 * 顧客IDと求人識別番号で求人を検索
	 * 
	 * @param customerId
	 * @param webNo
	 * @return 同じ求人識別番号を持つ求人ID
	 */
	public List<TWeb> checkByCustomerIdAndWebNo(int customerId, int webNo) {
		StringBuilder sb = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sb.append("SELECT id FROM t_web WHERE web_no = ? AND customer_id = ? AND delete_flg = 0");

		params.add(webNo);
		params.add(customerId);

		return jdbcManager.selectBySql(TWeb.class, sb.toString(), params.toArray()).getResultList();
    }

	/**
	 * 同じ求人識別番号で掲載期間が重複している求人を検索
	 * 掲載終了は対象外
	 * 
	 * @param volumeId 号数ID
	 * @param customerId 顧客ID
	 * @param webNo 求人識別番号
	 * @param id 求人ID
	 * @return 指定した求人識別番号と掲載期間が重複している求人ID
	 */
	public List<TWeb> duplicationPostPeriodInWebNo(int volumeId, int customerId, int webNo, int id) {
		//vomumeIdから開始、終了日時を取得
		StringBuilder sqlStr = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlStr.append("SELECT id, post_start_datetime, post_end_datetime FROM m_volume WHERE id = ? AND delete_flg = ?;");
		params.add(volumeId);
		params.add(DeleteFlgKbn.NOT_DELETED);
		MVolume vomume = jdbcManager.selectBySql(MVolume.class, sqlStr.toString(), params.toArray()).getSingleResult();

		sqlStr = new StringBuilder();
		params = new ArrayList<Object>();

		params.add(webNo);
		params.add(customerId);
		params.add(MStatusConstants.DBStatusCd.POST_END);
		// 登録・コピー時
		if (id == 0) {
			sqlStr.append("SELECT web.id FROM (SELECT id, volume_id FROM t_web WHERE web_no = ? AND customer_id = ? AND status != ? AND delete_flg = ?) as web ");
		// 編集時は編集対象の求人は対象外
		} else {
			sqlStr.append("SELECT web.id FROM (SELECT id, volume_id FROM t_web WHERE web_no = ? AND customer_id = ? AND status != ? AND id != ? AND delete_flg = ?) as web ");
			params.add(id);
		}
		sqlStr.append("INNER JOIN m_volume as volume ON volume.id = web.volume_id AND volume.delete_flg = ? ");
		sqlStr.append("WHERE (volume.post_start_datetime < ? AND volume.post_end_datetime > ?) ");
		sqlStr.append("OR (volume.post_start_datetime = ? AND volume.post_end_datetime = ?);");
		params.add(DeleteFlgKbn.NOT_DELETED);
		params.add(DeleteFlgKbn.NOT_DELETED);
		params.add(vomume.postEndDatetime);
		params.add(vomume.postStartDatetime);
		params.add(vomume.postStartDatetime);
		params.add(vomume.postEndDatetime);

		return jdbcManager.selectBySql(TWeb.class, sqlStr.toString(), params.toArray()).getResultList();
	}
}