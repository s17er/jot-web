package com.gourmetcaree.db.common.service;

import static com.gourmetcaree.common.util.SqlUtils.*;
import static org.seasar.framework.util.StringUtil.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.extension.jdbc.AutoSelect;
import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.extension.jdbc.SqlSelect;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.exception.SOptimisticLockException;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.beans.util.Beans;

import com.gourmetcaree.arbeitsys.constants.MArbeitConstants;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.property.PagerProperty;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.AbstractCommonEntity;
import com.gourmetcaree.db.common.entity.TShopList;
import com.gourmetcaree.db.common.entity.TShopListLabelGroup;
import com.gourmetcaree.db.common.entity.VShopListMaterialNoData;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.common.property.ShopListSearchInputCsvProperty;
import com.gourmetcaree.db.shopList.dto.shopList.RelationShopListRetDto;

import jp.co.whizz_tech.commons.WztStringUtil;

/**
 * 店舗一覧サービス
 * @author Takehiro Nakamori
 *
 */
public class ShopListService extends AbstractGroumetCareeBasicService<TShopList> {

	/** アプリケーションオブジェクト */
	@Resource
	protected ServletContext application;

	@Resource
	private ShopListMaterialNoDataService shopListMaterialNoDataService;
	@Resource
	private WebJobShopListService webJobShopListService;

	/** NULLでアップデートを許可するカラム */
	private static final String[] NULLABLE_UPDATE_COLUMN  = new String[]{
																	WztStringUtil.toCamelCase(TShopList.PHONE_NO1),
																	WztStringUtil.toCamelCase(TShopList.PHONE_NO2),
																	WztStringUtil.toCamelCase(TShopList.PHONE_NO3),
																	WztStringUtil.toCamelCase(TShopList.FAX_NO1),
																	WztStringUtil.toCamelCase(TShopList.FAX_NO2),
																	WztStringUtil.toCamelCase(TShopList.FAX_NO3),
																	WztStringUtil.toCamelCase(TShopList.LATITUDE),
																	WztStringUtil.toCamelCase(TShopList.LONGITUDE),

																	WztStringUtil.toCamelCase(TShopList.TRANSIT),
																	WztStringUtil.toCamelCase(TShopList.CSV_PHONE_NO),
																	WztStringUtil.toCamelCase(TShopList.SEAT_KBN),
																	WztStringUtil.toCamelCase(TShopList.STAFF),
																	WztStringUtil.toCamelCase(TShopList.SALES_PER_CUSTOMER_KBN),
																	WztStringUtil.toCamelCase(TShopList.HOLIDAY),
																	WztStringUtil.toCamelCase(TShopList.BUSSINESS_HOURS),
																	WztStringUtil.toCamelCase(TShopList.OPEN_DATE_YEAR),
																	WztStringUtil.toCamelCase(TShopList.OPEN_DATE_MONTH),
																	WztStringUtil.toCamelCase(TShopList.OPEN_DATE_NOTE),
																	WztStringUtil.toCamelCase(TShopList.OPEN_DATE_LIMIT_DISPLAY_DATE),
																	WztStringUtil.toCamelCase(TShopList.URL1),

																	WztStringUtil.toCamelCase(TShopList.ARBEIT_GYOTAI_ID),
																};

	/**
	 * IDを指定して店舗一覧を検索
	 */
	public List<TShopList> findByIds(Collection<Integer> ids, Integer limit, Integer offset, String orderBy) {
		if (CollectionUtils.isEmpty(ids)) {
			return new ArrayList<TShopList>(0);
		}

		SimpleWhere where = new SimpleWhere();
		where.in(TShopList.ID, ids);

		AutoSelect<TShopList> select = jdbcManager.from(entityClass)
																	.where(where);

		if (limit != null) {
			select.limit(limit);
		}

		if (offset != null) {
			select.offset(offset);
		}

		if (StringUtils.isNotBlank(orderBy)) {
			select.orderBy(orderBy);
		}

		return select.getResultList();
	}

	/**
	 * IDを指定して店舗一覧を検索
	 * IDの数が長すぎる場合、クエスチョンマークでの置換記述ができないため、
	 * IDを直接指定してセレクトする。
	 */
	public List<TShopList> findByIdsWithSql(Collection<Integer> ids, Integer limit, Integer offset, String orderBy) {
		if (CollectionUtils.isEmpty(ids)) {
			return new ArrayList<TShopList>(0);
		}

		StringBuilder sql = new StringBuilder(String.format("SELECT * FROM %s WHERE %s IN (%s)", TShopList.TABLE_NAME, TShopList.ID, StringUtils.join(ids, ",")));

		if (StringUtils.isNotBlank(orderBy)) {
			sql.append(" ORDER BY ")
				.append(orderBy);
		}

		SqlSelect<TShopList> select = jdbcManager.selectBySql(TShopList.class, sql.toString());

		if (limit != null) {
			select.limit(limit);
		}

		if (offset != null) {
			select.offset(offset);
		}

		return select.getResultList();

	}
	/**
	 * 顧客ID、店舗一覧IDからデータを取得する。
	 * @param customerId
	 * @param shopListId
	 * @return
	 * @throws WNoResultException
	 */
	public TShopList getData(int customerId, int shopListId, int status) throws WNoResultException {
		try {
			return jdbcManager.from(TShopList.class)
								.where(new SimpleWhere()
								.eq(WztStringUtil.toCamelCase(TShopList.ID) ,shopListId)
								.eq(WztStringUtil.toCamelCase(TShopList.CUSTOMER_ID), customerId)
								.eq(TShopList.STATUS, status)
								.eq(WztStringUtil.toCamelCase(TShopList.DELETE_FLG), DeleteFlgKbn.NOT_DELETED))
								.disallowNoResult()
								.getSingleResult();
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}


	/**
	 * バイト用プレビューセレクトを作成します。
	 * @param customerId 顧客ID
	 * @param areaCd エリアコード
	 * @return バイト用プレビューセレクト
	 * @throws WNoResultException プレビューする
	 */
	public TShopList selectArbeitPreviewEntity(int customerId, int areaCd) throws WNoResultException {
		SimpleWhere where = new SimpleWhere();
		where.eq(WztStringUtil.toCamelCase(TShopList.CUSTOMER_ID), customerId);
		where.eq(WztStringUtil.toCamelCase(TShopList.DISPLAY_FLG), MTypeConstants.ShopListDisplayFlg.DISPLAY);
		where.eq(WztStringUtil.toCamelCase(TShopList.STATUS), MTypeConstants.ShopListStatus.REGISTERED);
		where.eq(WztStringUtil.toCamelCase(TShopList.JOB_OFFER_FLG), MTypeConstants.JobOfferFlg.ARI);
		where.eq(WztStringUtil.toCamelCase(TShopList.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);
//		where.eq(WztStringUtil.toCamelCase(TShopList.AREA_CD), areaCd);

		try {
			return jdbcManager.from(TShopList.class)
							.where(where)
							.orderBy(TShopList.DISP_ORDER)
							.limit(1)
							.disallowNoResult()
							.getSingleResult();
		} catch (SNoResultException e) {
			throw new WNoResultException(e);
		}

	}

	/**
	 * バージョン番号の取得
	 * @param customerId
	 * @param shopListId
	 * @param status
	 * @return
	 * @throws WNoResultException
	 */
	public Long getVersion(int customerId, int shopListId, int status) throws WNoResultException {
		TShopList entity = getData(customerId, shopListId, status);
		return entity.version;
	}

	/**
	 * 店舗一覧IDから登録されたデータを取得する。
	 * @param customerId
	 * @param shopListId
	 * @return
	 * @throws WNoResultException
	 */
	public TShopList getResisteredData(int shopListId) throws WNoResultException {
		try {
			return jdbcManager.from(TShopList.class)
								.where(new SimpleWhere()
								.eq(WztStringUtil.toCamelCase(TShopList.ID) ,shopListId)
								.eq(TShopList.STATUS, MTypeConstants.ShopListStatus.REGISTERED)
								.eq(WztStringUtil.toCamelCase(TShopList.DISPLAY_FLG), MTypeConstants.ShopListDisplayFlg.DISPLAY)
								.eq(WztStringUtil.toCamelCase(TShopList.DELETE_FLG), DeleteFlgKbn.NOT_DELETED))
								.disallowNoResult()
								.getSingleResult();
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}

	/**
	 * 店舗一覧ID、顧客IDから、該当顧客のデータかどうか判断する。
	 * @param shopListId
	 * @param customerId
	 * @return
	 */
	public boolean isOwnData(int shopListId, int customerId) {
		try {
			getData(customerId, shopListId, MTypeConstants.ShopListStatus.REGISTERED);
			return true;
		} catch (WNoResultException e) {
			return false;
		}
	}


	/**
	 * CSV詳細からのアップデート
	 * @param entity
	 * @return
	 */
	public int updateCsvDetail(TShopList entity) {
		if (StringUtils.isNotBlank(entity.phoneNo1)
				&& StringUtils.isNotBlank(entity.phoneNo2)
				&& StringUtils.isNotBlank(entity.phoneNo3)) {
			StringBuilder tel = new StringBuilder("");
			tel.append(entity.phoneNo1);
			tel.append(GourmetCareeConstants.HYPHEN_MINUS_STR);
			tel.append(entity.phoneNo2);
			tel.append(GourmetCareeConstants.HYPHEN_MINUS_STR);
			tel.append(entity.phoneNo3);
			entity.csvPhoneNo = tel.toString();
		}

		if (StringUtils.isNotBlank(entity.faxNo1)
				&& StringUtils.isNotBlank(entity.faxNo2)
				&& StringUtils.isNotBlank(entity.faxNo3)) {
			StringBuilder tel = new StringBuilder("");
			tel.append(entity.faxNo1);
			tel.append(GourmetCareeConstants.HYPHEN_MINUS_STR);
			tel.append(entity.faxNo2);
			tel.append(GourmetCareeConstants.HYPHEN_MINUS_STR);
			tel.append(entity.faxNo3);
			entity.csvFaxNo = tel.toString();
		}

		return update(entity);
	}

	/**
	 * 顧客IDから店舗リストのIDを取得します。
	 * @param customerId
	 * @return 店舗リストのIDで並び代えて、最初に来る店舗のID
	 * @throws WNoResultException
	 */
	public int getShopListIdByCustomerId (int customerId) throws WNoResultException {
		TShopList entity = getFirstShopListByCustomerId(customerId);
		return entity.id;
	}

	/**
	 * 顧客IDから最初に並ぶ店舗一覧を取得します。
	 * @param customerId
	 * @return
	 * @throws WNoResultException
	 */
	public TShopList getFirstShopListByCustomerId(int customerId) throws WNoResultException {
		try {
			return jdbcManager.from(TShopList.class)
					.where(new SimpleWhere()
					.eq(WztStringUtil.toCamelCase(TShopList.CUSTOMER_ID), customerId)
					.eq(WztStringUtil.toCamelCase(TShopList.STATUS), MTypeConstants.ShopListStatus.REGISTERED)
					.eq(WztStringUtil.toCamelCase(TShopList.DISPLAY_FLG), MTypeConstants.ShopListDisplayFlg.DISPLAY)
					.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
					.orderBy(SqlUtils.asc(TShopList.DISP_ORDER))
					.limit(1)
					.disallowNoResult()
					.getSingleResult();
		} catch (SNoResultException e) {
			throw new WNoResultException(e);
		}
	}

	/**
	 * 顧客IDから並ぶ店舗一覧を取得します。
	 * @param customerId
	 * @return
	 * @throws WNoResultException
	 */
	public List<TShopList> getShopListByCustomerId(int customerId) throws WNoResultException {
		try {
			return jdbcManager.from(TShopList.class)
					.where(new SimpleWhere()
					.eq(WztStringUtil.toCamelCase(TShopList.CUSTOMER_ID), customerId)
					.eq(WztStringUtil.toCamelCase(TShopList.STATUS), MTypeConstants.ShopListStatus.REGISTERED)
					.eq(WztStringUtil.toCamelCase(TShopList.DISPLAY_FLG), MTypeConstants.ShopListDisplayFlg.DISPLAY)
					.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
					.orderBy(SqlUtils.asc(TShopList.DISP_ORDER))
					.disallowNoResult()
					.getResultList();
		} catch (SNoResultException e) {
			throw new WNoResultException(e);
		}
	}

	/**
	 * 顧客IDに紐づく店舗一覧が存在するか
	 * @param customerid 顧客ID
	 * @return 存在すればtrue
	 */
	public boolean existsShopListByCustomerId(int customerId) {
		long count = jdbcManager.from(TShopList.class)
			.where(new SimpleWhere()
			.eq(WztStringUtil.toCamelCase(TShopList.CUSTOMER_ID), customerId)
			.eq(WztStringUtil.toCamelCase(TShopList.STATUS), MTypeConstants.ShopListStatus.REGISTERED)
			.eq(WztStringUtil.toCamelCase(TShopList.DISPLAY_FLG), MTypeConstants.ShopListDisplayFlg.DISPLAY)
			.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
			.getCount();

		return count > 0l;

	}

	/**
	 * 顧客IDをキーに関連する店舗一覧のリストを取得します。
	 * 非表示の店舗は除外します。
	 * @param customerId
	 * @throws WNoResultException
	 */
	public List<RelationShopListRetDto> getRelationShopListByCustomerId(int customerId) throws WNoResultException {
		StringBuffer sb = new StringBuffer("");
		List<Object> params = new ArrayList<Object>();
		createRelationShopListSql(sb, params, customerId);

		sb.append("ORDER BY SL.disp_order ");

		try {
			return jdbcManager.selectBySql(RelationShopListRetDto.class, sb.toString(), params.toArray())
								.disallowNoResult()
								.getResultList();
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}

	/**
	 * 顧客IDをキーに関連する店舗一覧のリストを取得します。
	 * 非表示の店舗は除外します。
	 * @param customerId
	 * @throws WNoResultException
	 */
	public List<RelationShopListRetDto> getRelationShopListByCustomerIdIterate(int customerId) throws WNoResultException {
		StringBuffer sb = new StringBuffer("");
		List<Object> params = new ArrayList<Object>();
		sb.append(" id IN ( ");
		createRelationShopListSqlForIterate(sb, params, customerId);
		sb.append("ORDER BY SL.disp_order ) ");

		AutoSelect<TShopList> retList =   jdbcManager.from(entityClass).where(sb.toString(), params.toArray()).orderBy("T1_.disp_order ");
		return  retList.iterate(createRelationShopListBeitIterate());
	}

	/**
	 * 顧客IDに関連する店舗一覧のSQLセレクトを返します。
	 * @param property 検索プロパティ
	 * @return 顧客IDに関連する店舗一覧のSQLセレクト
	 */
	public List<RelationShopListRetDto> createRelationShopListSelectByCustomerIdIterate(ShopListSearchProperty property) {
		StringBuffer sb = new StringBuffer("");
		List<Object> params = new ArrayList<Object>();
		createRelationShopListSql(sb, params, property.customerId);

		long count = jdbcManager.getCountBySql(sb.toString(), params.toArray());

		if (count == 0l) {
			return null;
		}

		StringBuffer sb2 = new StringBuffer("");
		sb2.append(" id IN ( ");
		params.clear();
		createRelationShopListSqlForIterate(sb2, params, property.customerId);

		PageNavigateHelper pageNavi = new PageNavigateHelper(property.maxRow);
		pageNavi.changeAllCount((int) count);
		pageNavi.setPage(property.targetPage);
		sb2.append("ORDER BY SL.disp_order ) ");

		property.pageNavi = pageNavi;

		AutoSelect<TShopList> retList =   jdbcManager.from(entityClass).where(sb2.toString(), params.toArray()).limit(pageNavi.limit).offset(pageNavi.offset).orderBy("T1_.disp_order ");

		return  retList.iterate(createRelationShopListPreviewIterate(property.smartImageCreator));
	}

	/**
	 * 顧客IDに関連する店舗一覧のSQLセレクトを返します。
	 * @param property 検索プロパティ
	 * @return 顧客IDに関連する店舗一覧のSQLセレクト
	 */
	public List<RelationShopListRetDto> createRelationShopListSelectByCustomerIdIterateForMobile(ShopListSearchProperty property) {
		StringBuffer sb = new StringBuffer("");
		List<Object> params = new ArrayList<Object>();
		createRelationShopListSql(sb, params, property.customerId);

		long count = jdbcManager.getCountBySql(sb.toString(), params.toArray());

		if (count == 0l) {
			return null;
		}

		StringBuffer sb2 = new StringBuffer("");
		sb2.append(" id IN ( ");
		params.clear();
		createRelationShopListSqlForIterate(sb2, params, property.customerId);

		PageNavigateHelper pageNavi = new PageNavigateHelper(property.maxRow);
		pageNavi.changeAllCount((int) count);
		pageNavi.setPage(property.targetPage);
		sb2.append("ORDER BY SL.disp_order ) ");

		property.pageNavi = pageNavi;

		AutoSelect<TShopList> retList =   jdbcManager.from(entityClass).where(sb2.toString(), params.toArray()).limit(pageNavi.limit).offset(pageNavi.offset).orderBy("T1_.disp_order ");

		return  retList.iterate(createRelationShopListBeitIterateForMobile());
	}


	/**
	 * 顧客IDに関連する店舗一覧のSQLセレクトを返します。
	 * @param property 検索プロパティ
	 * @return 顧客IDに関連する店舗一覧のSQLセレクト
	 */
	public List<RelationShopListRetDto> createRelationShopListSelectByCustomerId(ShopListSearchProperty property) {
		StringBuffer sb = new StringBuffer("");
		List<Object> params = new ArrayList<Object>();
		createRelationShopListSql(sb, params, property.customerId);

		long count = jdbcManager.getCountBySql(sb.toString(), params.toArray());

		if (count == 0l) {
			return null;
		}

		sb.append("ORDER BY SL.disp_order ");

		PageNavigateHelper pageNavi = new PageNavigateHelper(property.maxRow);
		pageNavi.changeAllCount((int) count);
		pageNavi.setPage(property.targetPage);

		property.pageNavi = pageNavi;

		return jdbcManager.selectBySql(RelationShopListRetDto.class, sb.toString(), params.toArray())
							.limit(pageNavi.limit)
							.offset(pageNavi.offset)
							.getResultList();
	}

	/**
	 * 関連する店舗一覧をSELECTするSQL文を作成
	 * @param sb
	 * @param params
	 * @param companyIdList
	 */
	private void createRelationShopListSql(StringBuffer sb, List<Object> params, int customerId) {
		sb.append("SELECT ");
		sb.append("    SL.id, ");
		sb.append("    SL.shop_name, ");
		sb.append("    SL.address1, ");
		sb.append("    SL.address2, ");
		sb.append("    SL.transit, ");
		sb.append("    SL.job_offer_flg, ");
		sb.append("    SL.arbeit_todouhuken_id, ");
		sb.append("    SL.access_key ");
		sb.append("FROM ");
		sb.append("    t_shop_list SL ");
		sb.append("    INNER JOIN m_customer CUS ON SL.customer_id = CUS.id ");
		sb.append("WHERE ");
		sb.append("    CUS.id = ?");
		sb.append("    AND SL.display_flg = ? ");
		sb.append("    AND SL.status = ? ");
		sb.append("    AND SL.delete_flg = ? ");


		params.add(customerId);
		params.add(MTypeConstants.ShopListDisplayFlg.DISPLAY);
		params.add(MTypeConstants.ShopListStatus.REGISTERED);
		params.add(DeleteFlgKbn.NOT_DELETED);
	}

	/**
	 * 関連する店舗一覧をSELECTするSQL文を作成
	 * @param sb
	 * @param params
	 * @param companyIdList
	 */
	private void createRelationShopListSqlForIterate(StringBuffer sb, List<Object> params, int customerId) {
		sb.append("SELECT ");
		sb.append("    SL.id ");
		sb.append("FROM ");
		sb.append("    t_shop_list SL ");
		sb.append("    INNER JOIN m_customer CUS ON SL.customer_id = CUS.id ");
		sb.append("WHERE ");
		sb.append("    CUS.id = ?");
		sb.append("    AND SL.display_flg = ? ");
		sb.append("    AND SL.status = ? ");
		sb.append("    AND SL.delete_flg = ? ");


		params.add(customerId);
		params.add(MTypeConstants.ShopListDisplayFlg.DISPLAY);
		params.add(MTypeConstants.ShopListStatus.REGISTERED);
		params.add(DeleteFlgKbn.NOT_DELETED);
	}


	/**
	 * インポートしたCSVを検索します。
	 */
	public void searchInputCsvList(ShopListSearchInputCsvProperty property) throws WNoResultException {
		PageNavigateHelper pageNavi = new PageNavigateHelper("targetId ASC, id ASC", property.maxRow);
		SimpleWhere where = new SimpleWhere();
		where.in("id", property.idList);
		where.eq("status", MTypeConstants.ShopListStatus.TEMP_SAVE);
		where.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED);

		pageNavi.changeAllCount((int) countRecords(where));
		pageNavi.setPage(property.targetPage);

		try {
			property.shopList = findByCondition(where, pageNavi);
			property.pageNavi = pageNavi;
		} catch (SNoResultException e) {
			throw new WNoResultException(e);
		}

	}

	/**
	 * インポート用アップデート
	 * @param entity
	 * @return
	 */
	public int updateForImport(TShopList entity) {
		return super.updateWithNull(entity, NULLABLE_UPDATE_COLUMN);
	}


	/**
	 * 表示番号の変更
	 * @param id
	 * @param customerId
	 * @param targetDispOrder
	 */
	public void changeDispOrder(int id, int customerId, int targetDispOrder) {
		TShopList oldEntity = null;
		int oldDispOrder = 0;
		List<TShopList> changeEntityList;

		/*
		 * インクリメントフラグ
		 * 変更する順番がもとより小さくなった場合にtrue
		 */
		boolean incrementFlg;
		try {
			oldEntity = findByIdAndCustomerId(id, customerId);
			oldDispOrder = oldEntity.dispOrder;
		} catch (WNoResultException e) {
		}

		// 変更がない場合は終了
		if (oldDispOrder == targetDispOrder) {
			return;
		}


		// 変更する順番が大きくなった場合
		if (oldDispOrder < targetDispOrder) {
			incrementFlg = false;
			changeEntityList = getDispOrderChangeList(customerId, oldDispOrder, targetDispOrder, targetDispOrder);
		} else {
			incrementFlg = true;
			changeEntityList = getDispOrderChangeList(customerId, targetDispOrder, oldDispOrder, targetDispOrder);
		}

		for (TShopList entity : changeEntityList) {
			if (incrementFlg) {
				entity.dispOrder++;
			} else {
				entity.dispOrder--;
			}
			update(entity);
		}

		oldEntity.dispOrder = targetDispOrder;
		update(oldEntity);

	}

	/**
	 * 次にインサートするための順番を取得します。
	 * @param customerId
	 * @return
	 */
	public void setNextDispOrder(TShopList entity) {
		try {
			TShopList maxDispOrderEntity = jdbcManager.from(TShopList.class)
													.where(new SimpleWhere()
														.eq(WztStringUtil.toCamelCase(TShopList.CUSTOMER_ID), entity.customerId)
														.eq(WztStringUtil.toCamelCase(TShopList.STATUS), MTypeConstants.ShopListStatus.REGISTERED)
														.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
													.orderBy(SqlUtils.desc(WztStringUtil.toCamelCase(TShopList.DISP_ORDER)))
													.limit(1)
													.disallowNoResult()
													.getSingleResult();

			entity.dispOrder = maxDispOrderEntity.dispOrder + 1;
		} catch (SNoResultException e) {
			entity.dispOrder = 1;
		}
	}


	/**
	 * 表示番号を変更するリストの取得
	 * @param customerId
	 * @param lowerDispOrder
	 * @param upperDispOrder
	 * @param targetDispOrder
	 * @return
	 */
	private List<TShopList> getDispOrderChangeList(int customerId,  int lowerDispOrder, int upperDispOrder, int targetDispOrder) {
		SimpleWhere where = new SimpleWhere();
		where.eq(WztStringUtil.toCamelCase(TShopList.CUSTOMER_ID), customerId);
		if (lowerDispOrder != targetDispOrder) {
			where.gt(WztStringUtil.toCamelCase(TShopList.DISP_ORDER), lowerDispOrder);
			where.le(WztStringUtil.toCamelCase(TShopList.DISP_ORDER), upperDispOrder);
		} else {
			where.ge(WztStringUtil.toCamelCase(TShopList.DISP_ORDER), lowerDispOrder);
			where.lt(WztStringUtil.toCamelCase(TShopList.DISP_ORDER), upperDispOrder);
		}
		where.eq(WztStringUtil.toCamelCase(TShopList.STATUS), MTypeConstants.ShopListStatus.REGISTERED);

		return jdbcManager.from(TShopList.class)
							.where(where)
							.orderBy(SqlUtils.asc(TShopList.DISP_ORDER))
							.getResultList();

	}


	/**
	 * 表示番号が最大値を超えているかどうか
	 * @param customerId
	 * @param dispOrder
	 * @return
	 */
	public boolean isOrverMaxDispOrder(int customerId, int dispOrder) {
		TShopList entity = jdbcManager.from(TShopList.class)
										.where(new SimpleWhere()
											.eq(WztStringUtil.toCamelCase(TShopList.CUSTOMER_ID), customerId)
											.eq(WztStringUtil.toCamelCase(TShopList.STATUS), MTypeConstants.ShopListStatus.REGISTERED)
											.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
										.limit(1)
										.orderBy(SqlUtils.desc(TShopList.DISP_ORDER))
										.getSingleResult();

		if (entity == null) {
			return true;
		}

		return dispOrder > entity.dispOrder;

	}


	/**
	 * IDと顧客IDで検索を行う
	 * @param id
	 * @param customerId
	 * @return
	 * @throws WNoResultException
	 */
	public TShopList findByIdAndCustomerId(int id, int customerId) throws WNoResultException {
		try {
			return jdbcManager.from(TShopList.class)
							.where(new SimpleWhere()
									.eq("id", id)
									.eq(WztStringUtil.toCamelCase(TShopList.CUSTOMER_ID), customerId)
									.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
									.disallowNoResult()
									.limit(1)
									.getSingleResult();
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}

	@Override
	public int insert(TShopList entity) {
		if (entity.displayFlg == null) {
			entity.displayFlg = MTypeConstants.ShopListDisplayFlg.DISPLAY;
		}
		return super.insert(entity);
	}

	/**
	 * 新しい表示番号でアップデートします。
	 * @param entity
	 * @return
	 */
	public int updateWithNewDispOrder(TShopList entity) {
		setNextDispOrder(entity);
		return super.update(entity);
	}


	/**
	 * 表示フラグを更新します。
	 * 別顧客のものを更新しないように、顧客IDも含めた検索を行います。
	 * @param displayFlg
	 * @param customerId
	 * @param targetIds
	 */
	public void updateDisplayFlg(int displayFlg, int customerId, String... targetIds) {
		try {
			Integer[] ids = GourmetCareeUtil.toIntegerArray(targetIds);
			List<TShopList> entityList = findByCondition(new SimpleWhere()
															.in("id", Arrays.asList(ids))
															.eq(WztStringUtil.toCamelCase(TShopList.CUSTOMER_ID), customerId)
															.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED));

			for (TShopList entity : entityList) {
				entity.displayFlg = displayFlg;
				update(entity);
			}
		} catch (WNoResultException e) {
			// 見つからなければ何もしない。
		}
	}

	/**
	 * 一括削除を行います。
	 * 他の顧客のものを消さないように、顧客IDを含めた検索を行います。
	 * @param customerId
	 * @param targetIds
	 */
	public void lumpDelete(int customerId, String... targetIds) {
		try {
			List<TShopList> entityList = findByCondition(new SimpleWhere()
																.in("id", Arrays.asList(GourmetCareeUtil.toIntegerArray(targetIds)))
																.eq(WztStringUtil.toCamelCase(TShopList.CUSTOMER_ID), customerId)
																.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED));

//			削除する複数の店舗で職種店舗を削除
			webJobShopListService.deleteByShopList(entityList);
			logicalDeleteBatch(entityList);
		} catch (WNoResultException e) {
			// 見つからなければ何もしない。
		}

		regulateDispOrder(customerId);
	}

	/**
	 * 表示番号の整理
	 * @param customerId
	 */
	private void regulateDispOrder(int customerId) {
		List<TShopList> entityList = jdbcManager.from(TShopList.class)
												.where(new SimpleWhere()
												.eq(WztStringUtil.toCamelCase(TShopList.CUSTOMER_ID), customerId)
												.eq(WztStringUtil.toCamelCase(TShopList.STATUS), MTypeConstants.ShopListStatus.REGISTERED)
												.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
												.orderBy(SqlUtils.asc(WztStringUtil.toCamelCase(TShopList.DISP_ORDER)))
												.getResultList();
		if (CollectionUtils.isEmpty(entityList)) {
			return;
		}

		int dispOrder = 1;
		for (TShopList entity : entityList) {
			entity.dispOrder = dispOrder;
			update(entity);
			dispOrder++;
		}
	}

	/**
	 * 整理して論理削除を行う
	 * @param entity
	 * @return
	 */
	public int regulationDelete(int id , int customerId, long version) {
		TShopList entity = new TShopList();
		try {
			try {
				entity = jdbcManager.from(TShopList.class)
								.where(new SimpleWhere()
										.eq("id", id)
										.eq(WztStringUtil.toCamelCase(TShopList.CUSTOMER_ID), customerId)
										.eq(TShopList.STATUS, MTypeConstants.ShopListStatus.REGISTERED)
										.eq(TShopList.VERSION, version)
										.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
								.limit(1)
								.disallowNoResult()
								.getSingleResult();
			} catch (SNoResultException e) {
				throw new SOptimisticLockException();
			}

			List<TShopList> regulateEntityList = findByCondition(new SimpleWhere()
																			.eq(WztStringUtil.toCamelCase(TShopList.CUSTOMER_ID), entity.customerId)
																			.eq(TShopList.STATUS, MTypeConstants.ShopListStatus.REGISTERED)
																			.gt(WztStringUtil.toCamelCase(TShopList.DISP_ORDER), entity.dispOrder)
																			.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED),
																	SqlUtils.asc(TShopList.DISP_ORDER));

			for (TShopList regulateEntity : regulateEntityList) {
				regulateEntity.dispOrder--;
				update(regulateEntity);
			}
		} catch (WNoResultException e) {
			// 見つからなければ何もしない
		}

//		削除する店舗のIDで職種店舗のレコードを物理削除
		webJobShopListService.deleteByShopListId(entity.id);
		return logicalDelete(entity);
	}

	/**
	 * 公開中のショップ数を取得する
	 * @param customerId 顧客ID
	 * @return ショップ数
	 * @author Yamane
	 */
	public int countByCustomerId(Integer customerId) {

		if (customerId == null) {
			return 0;
		}

		SimpleWhere sw = new SimpleWhere();

		sw.eq(WztStringUtil.toCamelCase(TShopList.CUSTOMER_ID), customerId);
		sw.eq(WztStringUtil.toCamelCase(TShopList.STATUS), MTypeConstants.ShopListStatus.REGISTERED);
		sw.eq(WztStringUtil.toCamelCase(TShopList.DISPLAY_FLG), MTypeConstants.ShopListDisplayFlg.DISPLAY);
		sw.eq(WztStringUtil.toCamelCase(AbstractCommonEntity.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		return (int) countRecords(sw);
	}

	/**
	 * 公開非公開に関わらずショップ数を取得する
	 * @param customerId 顧客ID
	 * @return ショップ数
	 * @author otani
	 */
	public int countByCustomerIdIgnoreDisplay(Integer customerId) {

		if (customerId == null) {
			return 0;
		}

		SimpleWhere sw = new SimpleWhere();

		sw.eq(WztStringUtil.toCamelCase(TShopList.CUSTOMER_ID), customerId);
		sw.eq(WztStringUtil.toCamelCase(TShopList.STATUS), MTypeConstants.ShopListStatus.REGISTERED);
		sw.eq(WztStringUtil.toCamelCase(AbstractCommonEntity.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		return (int) countRecords(sw);
	}

	/**
	 * 公開中のライト版求人募集数を取得する
	 * @param customerId 顧客ID
	 * @return ライト版求人募集数
	 * @author Yamane
	 */
	public int countByCustomerIdAndJobOffer(Integer customerId) {

		if (customerId == null) {
			return 0;
		}

		SimpleWhere sw = new SimpleWhere();

		sw.eq(WztStringUtil.toCamelCase(TShopList.CUSTOMER_ID), customerId);
		sw.eq(WztStringUtil.toCamelCase(TShopList.STATUS), MTypeConstants.ShopListStatus.REGISTERED);
		sw.eq(WztStringUtil.toCamelCase(TShopList.DISPLAY_FLG), MTypeConstants.ShopListDisplayFlg.DISPLAY);
		sw.eq(WztStringUtil.toCamelCase(TShopList.JOB_OFFER_FLG), MTypeConstants.JobOfferFlg.ARI);
		sw.eq(WztStringUtil.toCamelCase(AbstractCommonEntity.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		return (int) countRecords(sw);
	}


	/**
	 * 店舗一覧検索プロパティ
	 * @author Takehiro Nakamori
	 *
	 */
	public static class ShopListSearchProperty extends PagerProperty {

		/**
		 *
		 */
		private static final long serialVersionUID = -615815606755536021L;

		/** ページナビ */
		public PageNavigateHelper pageNavi;

		/** 顧客ID */
		public int customerId;

		public SmartPhoneImageCreator smartImageCreator;
	}



	/**
	 * プレビューデータ作成イテレータを作成します。
	 * @return
	 */
	private IterationCallback<TShopList, List<RelationShopListRetDto>> createRelationShopListPreviewIterate(final SmartPhoneImageCreator smartImageCreator) {
		IterationCallback<TShopList, List<RelationShopListRetDto>> iterate = new IterationCallback<TShopList, List<RelationShopListRetDto>>() {
			private List<RelationShopListRetDto> retList = new ArrayList<RelationShopListRetDto>();
			@Override
			public List<RelationShopListRetDto> iterate(TShopList entity, IterationContext context) {
				if (entity == null) {
					return retList;
				}

				RelationShopListRetDto dto = new RelationShopListRetDto();
				Beans.copy(entity, dto).execute();
				String arbeitTodouhukenId = String.valueOf(entity.arbeitTodouhukenId);
				if (!arbeitTodouhukenId.equals(null) && arbeitTodouhukenId != null && !arbeitTodouhukenId.equals("null")) {
					dto.arbeitPreviewPath =String.format((String) ((Map<?, ?>) application.getAttribute("common")).get("gc.arbeitPreview.smartPhone.url.format"),
							MArbeitConstants.ArbeitSite.getArbeitSiteConst(entity.arbeitTodouhukenId),
							dto.id,
							dto.accessKey);
				}


				createImagePath(dto, entity, smartImageCreator);
				retList.add(dto);
				return retList;
			}
		};

		return iterate;
	}

	/**
	 * 画像パスの作成
	 * @param dto
	 * @param shop
	 * @param smartImageCreator
	 */
	private void createImagePath(RelationShopListRetDto dto, TShopList shop, SmartPhoneImageCreator smartImageCreator) {
		if (smartImageCreator == null) {
			return;
		}

		try {
			VShopListMaterialNoData materialData = shopListMaterialNoDataService.getMaterialEntity(dto.id, NumberUtils.toInt(MTypeConstants.ShopListMaterialKbn.MAIN_1));
			dto.existImageFlg = true;
			dto.mobileImageUrl = smartImageCreator.createImagePath(shop, materialData);
		} catch (WNoResultException e1) {
			dto.existImageFlg = false;
		}

	}

	/**
	 * アルバイト表示あり、店舗作成イテレータを作成します。
	 * @return
	 */
	private IterationCallback<TShopList, List<RelationShopListRetDto>> createRelationShopListBeitIterate() {
		IterationCallback<TShopList, List<RelationShopListRetDto>> iterate = new IterationCallback<TShopList, List<RelationShopListRetDto>>() {
			private List<RelationShopListRetDto> retList = new ArrayList<RelationShopListRetDto>();
			@Override
			public List<RelationShopListRetDto> iterate(TShopList entity, IterationContext context) {
				if (entity == null) {
					return retList;
				}

				RelationShopListRetDto dto = new RelationShopListRetDto();
				Beans.copy(entity, dto).execute();
				String arbeitTodouhukenId = String.valueOf(entity.arbeitTodouhukenId);
				if (!arbeitTodouhukenId.equals(null) && arbeitTodouhukenId != null  && !arbeitTodouhukenId.equals("null")) {
					dto.arbeitPreviewPath =String.format((String) ((Map<?, ?>) application.getAttribute("common")).get("gc.arbeit.shopDetail.urlFormat"),
						MArbeitConstants.ArbeitSite.getArbeitSiteConst(entity.arbeitTodouhukenId),
						dto.id);
				}
				retList.add(dto);
				return retList;
			}
		};

		return iterate;
	}

	/**
	 * アルバイト表示あり、店舗作成イテレータを作成します。
	 * @return
	 */
	private IterationCallback<TShopList, List<RelationShopListRetDto>> createRelationShopListBeitIterateForMobile() {
		IterationCallback<TShopList, List<RelationShopListRetDto>> iterate = new IterationCallback<TShopList, List<RelationShopListRetDto>>() {
			private List<RelationShopListRetDto> retList = new ArrayList<RelationShopListRetDto>();
			@Override
			public List<RelationShopListRetDto> iterate(TShopList entity, IterationContext context) {
				if (entity == null) {
					return retList;
				}

				RelationShopListRetDto dto = new RelationShopListRetDto();
				Beans.copy(entity, dto).execute();
				String arbeitTodouhukenId = String.valueOf(entity.arbeitTodouhukenId);
				if (!arbeitTodouhukenId.equals(null) && arbeitTodouhukenId != null  && !arbeitTodouhukenId.equals("null")) {
					dto.arbeitPreviewPath =String.format((String) ((Map<?, ?>) application.getAttribute("common")).get("arbeit.shoplist.detailUrl"),
						MArbeitConstants.ArbeitSite.getArbeitSiteConst(entity.arbeitTodouhukenId),
						dto.id);
				}
				retList.add(dto);
				return retList;
			}
		};

		return iterate;
	}


	public static interface SmartPhoneImageCreator {
		String createImagePath(TShopList shop, VShopListMaterialNoData materialData);
	}

	/**
	 * 店舗一覧をラベルと一緒に取得
	 * @param customerId 顧客ID
	 * @return ラベルグループがセットされた店舗一覧。店舗が無ければ空のリストを返す
	 */
	public List<TShopList> findByCustomerIdWithLabelGroups(int customerId) {

		SimpleWhere sw = new SimpleWhere();

		sw.eq(WztStringUtil.toCamelCase(TShopList.CUSTOMER_ID), customerId);
		sw.eq(WztStringUtil.toCamelCase(TShopList.STATUS), MTypeConstants.ShopListStatus.REGISTERED);
		sw.eq(WztStringUtil.toCamelCase(TShopList.DISPLAY_FLG), MTypeConstants.ShopListDisplayFlg.DISPLAY);
		sw.eq(WztStringUtil.toCamelCase(AbstractCommonEntity.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		String[] sortKey = new String[]{
				desc(camelize(TShopList.ID)),
				asc(dot(TShopList.T_SHOP_LIST_LABEL_GROUP_LIST, camelize(TShopListLabelGroup.DISPLAY_ORDER)))
		};
		try {
			return findByConditionLeftJoin(TShopList.T_SHOP_LIST_LABEL_GROUP_LIST, sw, SqlUtils.createCommaStr(sortKey));
		} catch (WNoResultException e) {
			return new ArrayList<>(0);
		}
	}
	/**
	 * 店舗一覧をラベルと一緒に取得
	 * @param shopListIdList 店舗ID
	 * @return ラベルグループがセットされた店舗一覧。店舗が無ければ空のリストを返す
	 */
	public List<TShopList> findByIdsWithLabelGroups(List<Integer> shopListIdList) {

		SimpleWhere sw = new SimpleWhere();

		sw.eq(WztStringUtil.toCamelCase(TShopList.STATUS), MTypeConstants.ShopListStatus.REGISTERED);
		sw.eq(WztStringUtil.toCamelCase(TShopList.DISPLAY_FLG), MTypeConstants.ShopListDisplayFlg.DISPLAY);
		sw.eq(WztStringUtil.toCamelCase(AbstractCommonEntity.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);
		sw.in(WztStringUtil.toCamelCase(TShopList.ID), shopListIdList);

		String[] sortKey = new String[]{
				desc(camelize(TShopList.ID)),
				asc(dot(TShopList.T_SHOP_LIST_LABEL_GROUP_LIST, camelize(TShopListLabelGroup.DISPLAY_ORDER)))
		};
		try {
			return findByConditionLeftJoin(TShopList.T_SHOP_LIST_LABEL_GROUP_LIST, sw, SqlUtils.createCommaStr(sortKey));
		} catch (WNoResultException e) {
			return new ArrayList<>(0);
		}
	}
}
