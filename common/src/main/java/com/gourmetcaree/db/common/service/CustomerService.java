package com.gourmetcaree.db.common.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.extension.jdbc.AutoSelect;
import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.entity.MCustomer;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;

import jp.co.whizz_tech.commons.WztStringUtil;

/**
 * 顧客マスタのサービスクラスです。
 * @version 1.0
 */
public class CustomerService extends AbstractGroumetCareeBasicService<MCustomer> {

	public static final String MAIL_ADDRESS_WHERE = "mailAddress";

	// 検索条件の受け渡し
	public static final String SHOP_LIST_PREFECTURES_CD_LIST = "where_shopListPrefecturesCdList";
	public static final String SHOP_LIST_SHUTOKEN_FOREIGN_AREA_KBN_LIST = "where_shopListShutokenForeignAreaKbnList";
	public static final String SHOP_LIST_INDUSTRY_KBN_LIST = "where_shopListIndustryKbnList";
	public static final String LOWER_SHOP_LIST_COUNT = "where_lowerShopListCount";
	public static final String UPPER_SHOP_LIST_COUNT = "where_upperShopListCount";
	public static final String MAIL_MAGAZINE_AREA_CD_LIST = "where_mailMagazineAreaCdList";

	/**
	 * 検索のメインロジック
	 * @param targetPage 表示するページ数
	 * @throws WNoResultException
	 */
	public List<MCustomer> doSearch(PageNavigateHelper pageNavi,Map<String, String> map, int targetPage, String companyId) throws WNoResultException {

		List<MCustomer> entityList = null;

		try {
			StringBuilder sqlStr = new StringBuilder();
			List<Object> params = new ArrayList<Object>();

			// カウント用SQL作成
			createSql(sqlStr, params, map);

			//ページナビゲータを使用
			int count = (int)getCountBySql(sqlStr.toString(), params.toArray());
			if (pageNavi.maxRow == 0) {
				pageNavi.setMaxRow(count);
			}
			pageNavi.changeAllCount(count);
			pageNavi.setPage(targetPage);

			// 検索結果のIDを取得するSQLを作成
			addSql(sqlStr, params, pageNavi);

			// 検索を行い該当する顧客のidのリストを取得
			List<Integer> idList = getCustomerIdList(sqlStr, params);

			entityList = getCustomerList(idList, pageNavi, companyId);
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}

		return entityList;
	}


	public int countSearchResult(Map<String, String> map) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();

		// カウント用SQL作成
		createSql(sql, params, map);

		return (int) getCountBySql(sql.toString(), params.toArray());
	}

	public <RESULT> RESULT executeSearch(Map<String, String> map, String companyId, String sortKey, IterationCallback<MCustomer, RESULT> callback) {
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();

		createSql(sql, params, map);

		final List<Integer> idList = new ArrayList<Integer>();

		jdbcManager.selectBySql(MCustomer.class, sql.toString(), params.toArray())
				.iterate(new IterationCallback<MCustomer, Void>() {
					@Override
					public Void iterate(MCustomer entity, IterationContext context) {
						if (entity != null) {
							idList.add(entity.id);
						}
						return null;
					}

				});


		return getCustomerSelect(idList, companyId, sortKey)
					.iterate(callback);





	}

	/**
	 * 検索用SQLを生成
	 * @param sqlStr SQL文字列
	 * @param params パラメータ
	 */
	private void createSql(StringBuilder sqlStr, List<Object> params, Map<String, String> map ) {

		sqlStr.append("SELECT mc.id  FROM m_customer mc INNER JOIN m_customer_company mcc ON mc.id = mcc.customer_id ");
		sqlStr.append("WHERE mc.delete_flg = ? AND mcc.delete_flg = ? ");
		params.add(DeleteFlgKbn.NOT_DELETED);
		params.add(DeleteFlgKbn.NOT_DELETED);

		// IDが選択されている場合
		if (!"".equals(map.get("id"))) {
			sqlStr.append("AND mc.id = ? ");
			params.add(NumberUtils.toInt(map.get("id")));
		}

		if (!"".equals(map.get("companyId"))) {
			sqlStr.append("AND mcc.company_id = ? ");
			params.add(NumberUtils.toInt(map.get("companyId")));
		}

		if (!"".equals(map.get("salesId"))) {
			sqlStr.append("AND mcc.sales_id = ? ");
			params.add(NumberUtils.toInt(map.get("salesId")));
		}

		if (!"".equals(map.get("customerName"))) {
			sqlStr.append("AND mc.customer_name like ? ");
			params.add("%" + map.get("customerName") + "%");
		}

		if (!"".equals(map.get("contactName"))) {
			sqlStr.append("AND mc.contact_name like ? ");
			params.add("%" + map.get("contactName") + "%");
		}

		// メールマガジンエリア
		if (StringUtils.isNotBlank(map.get(MAIL_MAGAZINE_AREA_CD_LIST))) {
			List<Integer> mailMagazineAreaList = Arrays.asList(map.get(MAIL_MAGAZINE_AREA_CD_LIST).split(","))
					.stream().map(Integer::valueOf).collect(Collectors.toList());
			sqlStr.append(" AND EXISTS ( ");
			sqlStr.append(" SELECT 1 FROM m_customer_mail_magazine_area cmma WHERE cmma.customer_id = mc.id");
			sqlStr.append(SqlUtils.andInNoCamelize("cmma.area_cd", mailMagazineAreaList.size()));
			sqlStr.append(" ) ");
			params.addAll(mailMagazineAreaList);
		}

		if (StringUtils.isNotBlank(map.get(MAIL_ADDRESS_WHERE))) {
			sqlStr.append(" AND ( ");
			sqlStr.append(SqlUtils.createLikeSearch(
					map.get(MAIL_ADDRESS_WHERE),
					params,
					" mc.main_mail like ? ",
					" OR mc.sub_mail like ? "
					));
			sqlStr.append(" OR EXISTS ( ");
			sqlStr.append(" SELECT 1 FROM m_customer_sub_mail submail WHERE submail.customer_id = mc.id AND");
			sqlStr.append(SqlUtils.createLikeSearch(
					map.get(MAIL_ADDRESS_WHERE),
					params,
					" submail.sub_mail like ? "
					));
			sqlStr.append("   ) ");
			sqlStr.append(" ) ");
		}

		// 店舗数
		if (StringUtils.isNotBlank(map.get(LOWER_SHOP_LIST_COUNT)) || StringUtils.isNotBlank(map.get(UPPER_SHOP_LIST_COUNT))) {
			sqlStr.append(" AND EXISTS ( ");
			sqlStr.append(" SELECT 1 FROM v_customer_shop_count vc WHERE vc.customer_id = mc.id");
			if (StringUtils.isNotBlank(map.get(LOWER_SHOP_LIST_COUNT))) {
				sqlStr.append(" AND count >= ? ");
				params.add(Integer.parseInt(map.get(LOWER_SHOP_LIST_COUNT)));
			}
			if (StringUtils.isNotBlank(map.get(UPPER_SHOP_LIST_COUNT))) {
				sqlStr.append(" AND count <= ? ");
				params.add(Integer.parseInt(map.get(UPPER_SHOP_LIST_COUNT)));
			}
			sqlStr.append(" ) ");
		}

		// 業態
		if (StringUtils.isNotBlank(map.get(SHOP_LIST_INDUSTRY_KBN_LIST))) {
			List<Integer> industryKbnList = Arrays.asList(map.get(SHOP_LIST_INDUSTRY_KBN_LIST).split(","))
					.stream().map(Integer::valueOf).collect(Collectors.toList());
			sqlStr.append(" AND EXISTS ( ");
			sqlStr.append(" SELECT 1 FROM v_shop_list_industry_kbn vi WHERE vi.customer_id = mc.id");
			sqlStr.append(SqlUtils.andInNoCamelize("vi.industry_kbn", industryKbnList.size()));
			sqlStr.append(" ) ");
			params.addAll(industryKbnList);
		}

		// 国内エリア
		if (StringUtils.isNotBlank(map.get(SHOP_LIST_PREFECTURES_CD_LIST))) {
			List<Integer> todouhukenList = Arrays.asList(map.get(SHOP_LIST_PREFECTURES_CD_LIST).split(","))
					.stream().map(Integer::valueOf).collect(Collectors.toList());
			sqlStr.append(" AND EXISTS ( ");
			sqlStr.append(" SELECT 1 FROM v_shop_list_prefectures vp WHERE vp.customer_id = mc.id");
			sqlStr.append(SqlUtils.andInNoCamelize("vp.prefectures_cd", todouhukenList.size()));
			sqlStr.append(" ) ");
			params.addAll(todouhukenList);
		}

		// 海外エリア
		if (StringUtils.isNotBlank(map.get(SHOP_LIST_SHUTOKEN_FOREIGN_AREA_KBN_LIST))) {
			List<Integer> shutokenForeignAreaKbnList = Arrays.asList(map.get(SHOP_LIST_SHUTOKEN_FOREIGN_AREA_KBN_LIST).split(","))
					.stream().map(Integer::valueOf).collect(Collectors.toList());
			sqlStr.append(" AND EXISTS ( ");
			sqlStr.append(" SELECT 1 FROM v_shop_list_shutoken_foreign_area_kbn vs WHERE vs.customer_id = mc.id");
			sqlStr.append(SqlUtils.andInNoCamelize("vs.shutoken_foreign_area_kbn", shutokenForeignAreaKbnList.size()));
			sqlStr.append(" ) ");
			params.addAll(shutokenForeignAreaKbnList);
		}
		sqlStr.append("GROUP BY mc.id, mcc.customer_id ");

	}

	/**
	 *
	 * @param sqlStr
	 * @param params
	 * @param pageNavi
	 */
	private void addSql(StringBuilder sqlStr, List<Object> params, PageNavigateHelper pageNavi) {

		sqlStr.append("ORDER BY mc.id DESC LIMIT ? OFFSET ?");
		params.add(pageNavi.limit);
		params.add(pageNavi.offset);

	}

	/**
	 * 検索結果の顧客IDリストを返す
	 * @param where 検索条件
	 * @param pageNavi ﾍﾟｰｼﾞナビ
	 * @return 顧客IDリスト
	 */
	private List<Integer> getCustomerIdList(StringBuilder sqlStr, List<Object> params) {

		List<MCustomer> entityList = jdbcManager.selectBySql(MCustomer.class, sqlStr.toString(), params.toArray()).disallowNoResult().getResultList();


		List<Integer> idList = new ArrayList<Integer>();

		for (MCustomer entity : entityList) {
			idList.add(entity.id);
		}

		return idList;
	}

	/**
	 * 検索結果の顧客リストを返す
	 * @param where 検索条件
	 * @param pageNavi ﾍﾟｰｼﾞナビ
	 * @return 顧客リスト
	 */
	private List<MCustomer> getCustomerList(List<Integer> idList, PageNavigateHelper pageNavi, String companyId) {

		String companyIdStr = null;
		if (!"".equals(companyId)) {
			companyIdStr = companyId;
		}

		List<MCustomer> entityList = jdbcManager.from(MCustomer.class)
										.innerJoin("mCustomerCompanyList")
										.leftOuterJoin("vCustomerShopCount")
										.leftOuterJoin("mCustomerSubMailList")
										.where(new SimpleWhere()
										.eq("mCustomerCompanyList.deleteFlg", DeleteFlgKbn.NOT_DELETED)
										.eq("mCustomerCompanyList.companyId", companyIdStr)
										.in("id", idList.toArray()))
										.orderBy(pageNavi.sortKey + ", mCustomerSubMailList.id ASC")
										.disallowNoResult()
										.getResultList();

		return entityList;
	}

	/**
	 * 検索結果の顧客リストを返す
	 * @param where 検索条件
	 * @return 顧客リスト
	 */
	private AutoSelect<MCustomer> getCustomerSelect(List<Integer> idList, String companyId, String sortKey) {

		String companyIdStr = null;
		if (!"".equals(companyId)) {
			companyIdStr = companyId;
		}

		return jdbcManager.from(MCustomer.class)
					.innerJoin("mCustomerCompanyList")
					.where(new SimpleWhere()
					.eq("mCustomerCompanyList.deleteFlg", DeleteFlgKbn.NOT_DELETED)
					.eq("mCustomerCompanyList.companyId", companyIdStr)
					.in("id", idList.toArray()))
					.orderBy(sortKey);


	}

	/**
	 * 顧客存在チェック
	 * @param customerId 顧客ID
	 * @return 顧客が存在している場合、trueを返す
	 */
	public boolean isCustomerExist(int customerId) {

		MCustomer entity = jdbcManager.from(MCustomer.class)
						.where(new SimpleWhere()
						.eq("id", customerId)
						.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
						.getSingleResult();

		return entity != null ? true : false;
	}

	/**
	 * スカウトメールを使用できるかどうかを返す
	 * @param customerId 顧客ID
	 * @return スカウトメール使用可能の場合、trueを返す
	 */
	public boolean isScoutUse(int customerId) {

		MCustomer entity = jdbcManager.from(MCustomer.class)
							.where(new SimpleWhere()
							.eq("id", customerId)
							.eq("loginFlg", MCustomer.LoginFlg.LOGIN_OK)
							.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
							.getSingleResult();

		if (entity == null) {
			return false;
		} else {
			return entity.scoutUseFlg.equals(Integer.valueOf(MCustomer.ScoutUseFlg.SCOUT_USE_OK)) ? true : false;
		}
	}

	/**
	 * 管理画面を使用できるかどうかを返す
	 * @param customerId 顧客ID
	 * @return 管理画面使用可能の場合、trueを返す
	 */
	public boolean isShopManageUse(int customerId) {

		Long count = jdbcManager.from(MCustomer.class)
							.where(new SimpleWhere()
							.eq("id", customerId)
							.eq("loginFlg", MCustomer.LoginFlg.LOGIN_OK)
							.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
							.getCount();

		return (count != 0)? true : false;
	}

	/**
	 * 顧客名を取得します。
	 */
	public String getCustomerName(int customerId) {
		try {
			MCustomer entity = jdbcManager.from(MCustomer.class)
								.where(new SimpleWhere()
								.eq("id", customerId)
								.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
								.disallowNoResult()
								.getSingleResult();
			return entity.customerName;
		} catch (SNoResultException e) {
			return "";
		}
	}


	/**
	 * 顧客エンティティに表示番号を作成します。
	 * @param entity
	 */
	public void createDispOrder(MCustomer entity) {
		checkEmptyEntity(entity);
		if (StringUtils.isBlank(entity.customerNameKana)) {
			throw new IllegalArgumentException("顧客名(カナ)に値がありません。");
		}

		List<Object> params = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder("");
		sb.append("SELECT * FROM m_customer WHERE customer_name_kana ~ ? ");
		params.add(getKanaRegex(entity.customerNameKana));
		if (entity.id != null) {
			sb.append(" AND id != ?");
			params.add(entity.id);
		}
		try {
			List<MCustomer> entityList = jdbcManager.selectBySql(MCustomer.class,
									sb.toString(),
									params.toArray())
									.disallowNoResult()
									.getResultList();

			entityList.add(entity);

			Collections.sort(entityList, new Comparator<MCustomer>() {
				@Override
				public int compare(MCustomer o1, MCustomer o2) {
					return o1.customerNameKana.compareTo(o2.customerNameKana);
				}
			});

			int index = entityList.indexOf(entity);

			if (index > 0) {
				entity.dispOrder = entityList.get(index - 1).dispOrder + 1;
			} else {
				// リストの先頭に来た場合は、最初の番号
				entity.dispOrder = 1;
			}
			// 見つからない場合は最初の番号。
		} catch (SNoResultException e) {
			entity.dispOrder = 1;
		}
	}

	/**
	 * 新規のdispOrderを指定して、他のdispOrderと被らないように
	 * 別エンティティをアップデートする。
	 *
	 * customerIdがnullでない場合、指定のIDを除いた顧客のdispOrderをアップデートする
	 *
	 * @param dispOrder 表示番号
	 * @param customerId 顧客ID
	 */
	public void updateDispOrderBatch(int dispOrder, Integer customerId) {
		SimpleWhere where = new SimpleWhere();
		where.ge(WztStringUtil.toCamelCase(MCustomer.DISP_ORDER), dispOrder);
		if (customerId != null) {
			where.ne("id", customerId);
		}
		List<MCustomer> entityList = jdbcManager.from(MCustomer.class)
										.where(where)
										.orderBy(SqlUtils.asc(MCustomer.DISP_ORDER))
										.getResultList();

		if (CollectionUtils.isEmpty(entityList)) {
			return;
		}

		int updDispOrder = dispOrder;
		for (MCustomer entity : entityList) {
			entity.dispOrder = ++updDispOrder;
			update(entity);

		}
	}

	private static String getKanaRegex(String kana) {
		return String.format(GourmetCareeConstants.MASK_KATAKANA_FORMAT, kana.substring(0, 1));
	}
}
