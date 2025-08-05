package com.gourmetcaree.common.logic;


import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.NoResultException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.extension.jdbc.SqlSelect;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.exception.SOptimisticLockException;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.util.Base64Util;
import org.seasar.framework.util.UUID;

import com.gourmetcaree.common.csv.ShopListArbeitCsv;
import com.gourmetcaree.common.csv.ShopListCsv;
import com.gourmetcaree.common.csv.ShopListJobCsv;
import com.gourmetcaree.common.csv.StationGroupCsv;
import com.gourmetcaree.common.dto.ShopListMaterialDto;
import com.gourmetcaree.common.exception.ImageWriteErrorException;
import com.gourmetcaree.common.property.ShopListProperty;
import com.gourmetcaree.common.property.ShopListSearchProperty;
import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.common.util.WebdataFileUtils;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.MRStation;
import com.gourmetcaree.db.common.entity.TCustomerImage;
import com.gourmetcaree.db.common.entity.TShopChangeJobCondition;
import com.gourmetcaree.db.common.entity.TShopList;
import com.gourmetcaree.db.common.entity.TShopListAttribute;
import com.gourmetcaree.db.common.entity.TShopListLine;
import com.gourmetcaree.db.common.entity.TShopListMaterial;
import com.gourmetcaree.db.common.entity.TShopListRoute;
import com.gourmetcaree.db.common.entity.VStationGroup;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.common.service.CustomerImageService;
import com.gourmetcaree.db.common.service.RStationService;
import com.gourmetcaree.db.common.service.ShopChangeJobConditionService;
import com.gourmetcaree.db.common.service.ShopListAttributeService;
import com.gourmetcaree.db.common.service.ShopListLineService;
import com.gourmetcaree.db.common.service.ShopListMaterialService;
import com.gourmetcaree.db.common.service.ShopListRouteService;
import com.gourmetcaree.db.common.service.ShopListService;
import com.gourmetcaree.db.common.service.ShopListTagMappingService;
import com.gourmetcaree.db.common.service.TypeService;

/**
 * 店舗一覧に関するロジック
 * 運営管理、店舗側にまたがるためcommonに配置
 * @author Takehiro Nakamori
 *
 */
public class ShopListLogic extends AbstractGourmetCareeLogic {

	/**
	 * 店舗一覧サービス
	 */
	@Resource
	private ShopListService shopListService;

	/** 店舗一覧画像サービス */
	@Resource
	private ShopListMaterialService shopListMaterialService;

	/** 店舗一覧属性サービス */
	@Resource
	private ShopListAttributeService shopListAttributeService;

	/** 店舗一覧路線サービス */
	@Resource
	private ShopListRouteService shopListRouteService;

	/** リニューアル後の路線サービス */
	@Resource
	private ShopListLineService shopListLineService;

	@Resource
	private ShopListTagMappingService shopListTagMappingService;

	@Resource
	private ShopChangeJobConditionService shopChangeJobConditionService;

	@Resource
	private TypeService typeService;

	@Resource
	private RStationService rStationService;

	@Resource
	private CustomerImageService customerImageService;

	/**
	 * 店舗一覧の検索
	 * @param property
	 * @return
	 * @throws WNoResultException
	 */
	public List<TShopList> searchShopList(ShopListSearchProperty property) throws WNoResultException {

		StringBuilder sb = new StringBuilder("");
		List<Object> params = new ArrayList<>();
		createShopListSearchSql(property, sb, params);

		int count = (int) jdbcManager.getCountBySql(sb.toString(), params.toArray());

		if (property.pageNavi == null) {
			property.pageNavi = new PageNavigateHelper(property.maxRow);
		}

		property.pageNavi.changeAllCount(count);
		if (count < 1) {
			throw new WNoResultException();
		}

		property.pageNavi.setPage(property.targetPage);

		addSortKey(sb);

		return jdbcManager.selectBySql(TShopList.class, sb.toString(), params.toArray())
							.limit(property.pageNavi.limit)
							.offset(property.pageNavi.offset)
							.getResultList();

	}

	/**
	 * 店舗一覧のインサート
	 * @param property
	 */
	public void insertShopList(ShopListProperty property) {
		checkEmptyShopListPropety(property);
		shopListService.setNextDispOrder(property.tShopList);
		property.tShopList.accessKey = UUID.create();
		shopListService.insert(property.tShopList);
		addMaterialValue(property);
	}

	/**
	 * すべての店舗一覧(バイト側を含む)をインサートします。
	 * @param property
	 */
	public void insertAllShopList(ShopListProperty property) {
		checkEmptyShopListPropety(property);
		shopListService.setNextDispOrder(property.tShopList);
		property.tShopList.accessKey = UUID.create();
		shopListService.insert(property.tShopList);
		insertShopListAttribute(property);
		insertShopChangeJobCondition(property);
		deleteInsertShopListLine(property);
		addMaterialValue(property);
		updateKeywordSearch(property.tShopList.id);
	}

	/**
	 * すべての店舗一覧属性をインサートします。
	 * @param property
	 */
	private void insertShopListAttribute(ShopListProperty property) {
		if (CollectionUtils.isEmpty(property.tShopListAttributeList)) {
			return;
		}

		for (TShopListAttribute entity : property.tShopListAttributeList) {
			entity.shopListId = property.tShopList.id;
			shopListAttributeService.insert(entity);
		}
	}

	/**
	 * すべての表示条件をインサートします。
	 * @param property
	 */
	private void insertShopChangeJobCondition(ShopListProperty property) {
		if (CollectionUtils.isEmpty(property.tShopChangeJobConditionList)) {
			return;
		}
		for (TShopChangeJobCondition entity : property.tShopChangeJobConditionList) {
			entity.shopListId = property.tShopList.id;
			shopChangeJobConditionService.insert(entity);
		}
	}

	/**
	 * 店舗一覧路線をインサートします。
	 */
	@Deprecated
	private void insertShopListRoute(ShopListProperty property) {
		if (CollectionUtils.isEmpty(property.tShopListRouteList)) {
			return;
		}
		for (TShopListRoute entity : property.tShopListRouteList) {
			entity.shopListId = property.tShopList.id;
			shopListRouteService.insert(entity);
		}
	}

	/**
	 * リニューアル後の店舗一覧路線を削除登録する
	 * @param property
	 */
	private void deleteInsertShopListLine(ShopListProperty property) {
		shopListLineService.deleteInsert(property.tShopList.id, property.tShopListLineList);
	}

	private void deleteInsertShopChangeJobCondiotion(ShopListProperty property) {
		shopChangeJobConditionService.deleteInsert(property.tShopList.id, property.tShopChangeJobConditionList);
	}

	/**
	 * 店舗情報タグを削除登録する
	 * @param property
	 */
	private void deleteInsertShopListTag(ShopListProperty property) {
		shopListTagMappingService.deleteInsert(property.tShopList.id, property.mShopListTagMappingList);
	}

	/**
	 * 店舗一覧のアップデート（顧客管理側）
	 */
	public void updateShopList(ShopListProperty property) {
		checkEmptyShopListPropety(property);
		shopListService.update(property.tShopList);
		shopListMaterialService.deleteTShopListMaterialByShopListId(property.tShopList.id);
		deleteInsertShopListLine(property);
		deleteInsertShopListTag(property);
		addMaterialValue(property);
		shopListAttributeService.deleteByShopListId(property.tShopList.id);
		insertShopListAttribute(property);
	}

	/**
	 * 店舗一覧のアップデート（企業管理側）
	 */
	public void updateShopListForCompany(ShopListProperty property) {
		checkEmptyShopListPropety(property);
		shopListService.update(property.tShopList);
		shopListMaterialService.deleteTShopListMaterialByShopListId(property.tShopList.id);
		deleteInsertShopListLine(property);
		addMaterialValue(property);
		shopListAttributeService.deleteByShopListId(property.tShopList.id);
		insertShopListAttribute(property);
	}

	/**
	 * すべての店舗一覧をアップデートします。（顧客管理側）
	 * @param property 店舗一覧プロパティ
	 */
	public void updateAllShopList(ShopListProperty property) {
		checkEmptyShopListPropety(property);
		shopListService.updateWithNull(
				property.tShopList,
				"arbeitGyotaiId",
				"industryKbn2",
				"seatKbn",
				"salesPerCustomerKbn",
				"openDateYear",
				"openDateMonth",
				"address1",
				"arbeitTodouhukenId",
				"arbeitAreaId",
				"arbeitSubAreaId",
				"shutokenForeignAreaKbn",
				"openDateLimitDisplayDate",
				toCamelCase(TShopList.PREFECTURES_CD),
				toCamelCase(TShopList.LATITUDE),
				toCamelCase(TShopList.LONGITUDE));
		shopListMaterialService.deleteTShopListMaterialByShopListId(property.tShopList.id);
		addMaterialValue(property);

		deleteInsertShopListLine(property);
		deleteInsertShopChangeJobCondiotion(property);
		deleteInsertShopListTag(property);
		shopListAttributeService.deleteByShopListId(property.tShopList.id);
		insertShopListAttribute(property);
		updateKeywordSearch(property.tShopList.id);
	}

	/**
	 * すべての店舗一覧をアップデートします。（企業管理側）
	 * @param property 店舗一覧プロパティ
	 */
	public void updateAllShopListForCompany(ShopListProperty property) {
		checkEmptyShopListPropety(property);
		shopListService.updateWithNull(
				property.tShopList,
				"arbeitGyotaiId",
				"industryKbn2",
				"seatKbn",
				"salesPerCustomerKbn",
				"openDateYear",
				"openDateMonth",
				"address1",
				"arbeitTodouhukenId",
				"arbeitAreaId",
				"arbeitSubAreaId",
				"shutokenForeignAreaKbn",
				"openDateLimitDisplayDate",
				toCamelCase(TShopList.PREFECTURES_CD),
				toCamelCase(TShopList.LATITUDE),
				toCamelCase(TShopList.LONGITUDE));
		shopListMaterialService.deleteTShopListMaterialByShopListId(property.tShopList.id);
		addMaterialValue(property);

		deleteInsertShopListLine(property);
		deleteInsertShopChangeJobCondiotion(property);
		shopListAttributeService.deleteByShopListId(property.tShopList.id);
		insertShopListAttribute(property);
		updateKeywordSearch(property.tShopList.id);
	}

	/**
	 * 顧客に設定されている画像を店舗画像を削除登録する
	 * @param customerId
	 * @param customerImageId 登録する顧客画像のID
	 * @param shopListId
	 * @param shopListMaterialKbn
	 * @return 登録成功の場合はtrue、失敗の場合はfalse
	 */
	public boolean deleteInsertShopListMaterial(int customerId, int customerImageId, int shopListId, int shopListMaterialKbn) {

    	try {
    		TCustomerImage customerImage = customerImageService.findById(customerImageId);

    		// 対象画像があれば削除（画像IDが見つからない場合は、不正な値が来ている可能性があるため画像を消さない）
    		shopListMaterialService.deleteMaterial(shopListId, shopListMaterialKbn);

    		TShopListMaterial entity = Beans.createAndCopy(TShopListMaterial.class, customerImage).execute();
			entity.shopListId  = shopListId;
			entity.materialKbn = shopListMaterialKbn;
			entity.customerImageId = customerImageId;

			// 登録
			shopListMaterialService.insert(entity);

    	} catch (NoResultException e) {
			return false;
		}
    	return true;
	}

	/**
	 * 店舗画像を削除する
	 * @param shopListId
	 * @param shopListMaterialKbn
	 * @return 登録成功の場合はtrue、失敗の場合はfalse
	 */
	public boolean deleteShopListMaterial(int shopListId, int shopListMaterialKbn) {

		try {
			List<TShopListMaterial> list = shopListMaterialService.findByCondition(
					new SimpleWhere()
						.eq(toCamelCase(TShopListMaterial.MATERIAL_KBN), shopListMaterialKbn)
						.eq(toCamelCase(TShopListMaterial.SHOP_LIST_ID), shopListId)
					);

			shopListMaterialService.deleteBatch(list);

		// 画像が無ければfalse
		} catch (WNoResultException e) {
			return false;
		}
		return true;
	}

	/**
	 * 素材データに登録する値を追加する。
	 * @param property
	 */
	private void addMaterialValue(ShopListProperty property) {
		if (CollectionUtils.isEmpty(property.tShopListMaterialList)) {
			return;
		}

		for (TShopListMaterial material : property.tShopListMaterialList) {
			material.shopListId = property.tShopList.id;
			material.notTargetFlg = MTypeConstants.NotTargetFlg.TARGET;
			material.deleteFlg = DeleteFlgKbn.NOT_DELETED;

			if (StringUtils.isBlank(material.materialData)) {
				try {
					ShopListMaterialDto dto = WebdataFileUtils.getShopListImageFile(property.shopListSessionImgdirPath,
							property.idForDir,
							Integer.toString(material.materialKbn));
					material.materialData = Base64Util.encode(dto.materialData);
				} catch (ImageWriteErrorException e) {
					throw new SOptimisticLockException("ファイルシステムから取り出した画像の登録時に画像、またはフォルダが存在しなかった可能性があります。" + e);
				}
			}

			shopListMaterialService.insert(material);
		}
	}

	/**
	 * プロパティが空かチェック
	 * @param property
	 */
	private void checkEmptyShopListPropety(ShopListProperty property) {
		if (property == null) {
			throw new IllegalArgumentException("引数がnullです。");
		}

		if (property.tShopList == null) {
			throw new IllegalArgumentException("引数がnullです。");
		}
	}

	/**
	 * CSV用店舗一覧検索
	 * @param property
	 * @param csvProperty
	 * @throws WNoResultException
	 */
	public List<ShopListArbeitCsv> searchShopListForCsv(ShopListSearchProperty property) throws WNoResultException {
		StringBuilder sb = new StringBuilder("");
		List<Object> params = new ArrayList<>();
		createShopListSearchSql(property, sb, params);
		addSortKey(sb);

		try {
			return jdbcManager.selectBySql(ShopListArbeitCsv.class, sb.toString(), params.toArray())
													.disallowNoResult()
													.getResultList();
		} catch (SNoResultException e) {
			return new ArrayList<>();
		}
	}


	/**
	 * CSV用店舗一覧のセレクトを作成します。
	 * @param property 店舗一覧検索プロパティ
	 * @return CSV用店舗一覧のセレクト
	 */
	public List<ShopListArbeitCsv> createShopListSelectForCsv(ShopListSearchProperty property) {
		StringBuilder sb = new StringBuilder(0);
		List<Object> params = new ArrayList<>();
		createShopListSearchSql(property, sb, params);


		addSortKey(sb);

		List<ShopListArbeitCsv> list = jdbcManager.selectBySql(ShopListArbeitCsv.class, sb.toString(), params.toArray())
				.getResultList();

		for(ShopListArbeitCsv shopListArbeitCsv : list) {
			addShopAttribute(shopListArbeitCsv);
			addStationGroup(shopListArbeitCsv);
			addShopTag(shopListArbeitCsv);
		}


		return list;
	}

	public List<ShopListJobCsv> createShopListJobselectForCsv(ShopListSearchProperty property) {
		StringBuilder sb = new StringBuilder(0);
		List<Object> params = new ArrayList<>();
		createShopListJobSearchSql(property, sb, params);

		sb.append(" ORDER BY ");
		sb.append(" JOB.shop_list_id ASC, JOB.id ASC ");

		List<ShopListJobCsv> list = jdbcManager.selectBySql(ShopListJobCsv.class, sb.toString(), params.toArray())
				.getResultList();

		return list;
	}

	/**
	 * CSV用店舗一覧のセレクトを作成します。
	 * @param property 店舗一覧検索プロパティ
	 * @return CSV用店舗一覧のセレクト
	 */
	public List<ShopListCsv> createShopListForCsv(ShopListSearchProperty property) {
		StringBuilder sb = new StringBuilder(0);
		List<Object> params = new ArrayList<>();
		createShopListSearchSql(property, sb, params);


		addSortKey(sb);

		List<ShopListCsv> list = jdbcManager.selectBySql(ShopListCsv.class, sb.toString(), params.toArray())
				.getResultList();

		for(ShopListCsv shopListArbeitCsv : list) {
			addShopAttribute(shopListArbeitCsv);
			addStationGroup(shopListArbeitCsv);
			addShopTag(shopListArbeitCsv);
		}


		return list;
	}

	public SqlSelect<TShopListAttribute> getShopAttribute(Integer shopId, String attributeCd){
		StringBuilder sb = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sb.append("SELECT ");
		sb.append("    SLA.* ");
		sb.append("FROM ");
		sb.append("    t_shop_list_attribute SLA ");
		sb.append("WHERE ");
		sb.append("    SLA.shop_list_id = ? ");

		sb.append("AND SLA.attribute_cd = ? ");

		params.add(shopId);
		params.add(attributeCd);

		return jdbcManager.selectBySql(TShopListAttribute.class, sb.toString(), params.toArray());
	}

	public SqlSelect<TShopListLine> getStationGroup(Integer shopId){
		StringBuilder sb = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sb.append("SELECT ");
		sb.append("    SLL.* ");
		sb.append("FROM ");
		sb.append("    t_shop_list_line SLL ");
		sb.append("WHERE ");
		sb.append("    SLL.shop_list_id = ? ");
		sb.append("    AND SLL.station_cd IS NOT NULL");

		params.add(shopId);

		return jdbcManager.selectBySql(TShopListLine.class, sb.toString(), params.toArray());
	}

	/**
	 * 店舗一覧検索SQL
	 * @param property
	 * @param sb
	 * @param params
	 */
	private void createShopListSearchSql(ShopListSearchProperty property, StringBuilder sb, List<Object> params) {

		sb.append("SELECT ");
		sb.append("    SL.* ");
		sb.append("FROM ");
		sb.append("    t_shop_list SL ");
		sb.append("WHERE ");
		sb.append("    SL.customer_id = ? ");

		params.add(property.customerId);

		// エリア
		if(property.where_areaCd != null){
			sb.append("    AND SL.area_cd = ? ");
			params.add(property.where_areaCd);
		}

		// 店舗名
		if (StringUtils.isNotBlank(property.where_shopName)) {

			sb.append(" AND ( ");
			sb.append(SqlUtils.createLikeSearch(
					property.where_shopName,
					params,
					" SL.shop_name LIKE ? "));
			sb.append(" ) ");
		}

		// 住所
		if (StringUtils.isNotBlank(property.where_address)) {

			sb.append("AND ( ");
			sb.append(SqlUtils.createLikeSearch(
					property.where_address,
					params,
					" SL.address1 LIKE ? ",
					" OR SL.address2 LIKE ? "));
			sb.append(") ");
		}


		// 業種
		if (StringUtils.isNotEmpty(property.where_industryKbn)) {
			sb.append("AND ( ");
			sb.append("    SL.industry_kbn1 = ? OR SL.industry_kbn2 = ? ");
            params.add(NumberUtils.toInt(property.where_industryKbn));
            params.add(NumberUtils.toInt(property.where_industryKbn));

			sb.append(") ");
		}

		// オープン日
		if (StringUtils.isNotBlank(property.where_searchOpenDateFlg)) {
			sb.append("AND ( ");
			if (property.where_searchOpenDateFlg.equals(String.valueOf(MTypeConstants.searchOpenDateFlg.INCLUDED))) {
				sb.append("SL.open_date_year IS NOT NULL ");
				sb.append("AND SL.open_date_month IS NOT NULL ");
			} else {
				sb.append("SL.open_date_year IS NULL ");
				sb.append("OR SL.open_date_month IS NULL ");
			}
			sb.append(") ");
		}

		// キーワード
		if (StringUtils.isNotBlank(property.where_keyword)) {

			sb.append("AND ( ");
			sb.append(SqlUtils.createLikeSearch(
					property.where_keyword,
					params,
					"    SL.shop_name LIKE ? ",
					"    OR SL.address1 LIKE ? ",
					"    OR SL.address2 LIKE ? ",
					"    OR SL.transit LIKE ? ",
					"    OR SL.shop_information LIKE ? ",
					"    OR SL.holiday LIKE ? ",
					"    OR SL.business_hours LIKE ? ",
					"    OR SL.seating LIKE ? ",
					"    OR SL.unit_price LIKE ? ",
					"    OR SL.staff LIKE ? ",
					"    OR SL.url1 LIKE ? ",
					"    OR SL.keyword_search LIKE ? ",
					"    OR SL.catch_copy LIKE ? ",
					"    OR SL.industry_text LIKE ? "
					));

			// 業種1
			sb.append(" OR EXISTS ( ");
			sb.append("     SELECT ");
			sb.append("           * ");
			sb.append("     FROM ");
			sb.append("           m_type MT");
			sb.append("     WHERE ");
			sb.append("           MT.type_cd = ? ");
			params.add(MTypeConstants.IndustryKbn.TYPE_CD);
			sb.append("           AND MT.type_value = SL.industry_kbn1");
			sb.append("  AND ( ");
			sb.append(SqlUtils.createLikeSearch(
					property.where_keyword,
					params,
					" MT.type_name LIKE ? "));
			sb.append("  ) ");
			sb.append(")");

			// 業種2
			sb.append(" OR EXISTS ( ");
			sb.append("     SELECT ");
			sb.append("           * ");
			sb.append("     FROM ");
			sb.append("           m_type MT");
			sb.append("     WHERE ");
			sb.append("           MT.type_cd = ? ");
			params.add(MTypeConstants.IndustryKbn.TYPE_CD);
			sb.append("           AND MT.type_value = SL.industry_kbn2");
			sb.append(" AND ( ");
			sb.append(SqlUtils.createLikeSearch(
					property.where_keyword,
					params,
					"   MT.type_name LIKE ?  "));
			sb.append("   ) ");
			sb.append(" ) ");

			sb.append(")");
		}

		sb.append(" AND SL.status = ? ");
		sb.append(" AND SL.delete_flg = ? ");
		params.add(MTypeConstants.ShopListStatus.REGISTERED);
		params.add(DeleteFlgKbn.NOT_DELETED);
	}

	/**
	 * 店舗一覧検索SQL
	 * @param property
	 * @param sb
	 * @param params
	 */
	private void createShopListJobSearchSql(ShopListSearchProperty property, StringBuilder sb, List<Object> params) {
		sb.append("SELECT JOB.* FROM t_shop_change_job_condition JOB WHERE JOB.shop_list_id IN (");
		sb.append("SELECT ");
		sb.append("    SL.id ");
		sb.append("FROM ");
		sb.append("    t_shop_list SL ");
		sb.append("WHERE ");
		sb.append("    SL.customer_id = ? ");

		params.add(property.customerId);

		sb.append(" AND SL.status = ? ");
		sb.append(" AND SL.delete_flg = ? ");
		params.add(MTypeConstants.ShopListStatus.REGISTERED);
		params.add(DeleteFlgKbn.NOT_DELETED);
		sb.append(" ) ");
	}

	/**
	 * ソートキーの追加
	 * @param sb
	 */
	private void addSortKey(StringBuilder sb) {
		sb.append(" ORDER BY ");
		sb.append(" SL.disp_order ASC ");
	}

	/**
	 * 店舗の各属性を追加します（運営）
	 * @param data 店舗CSVデータ
	 */
	private void addShopAttribute(ShopListArbeitCsv data) {
		addWorkCharacteristicKbn(data);
		addShopCharacteristicKbn(data);
		addShopListMaterial(data);
	}

	/**
	 * 店舗の各属性を追加します（店舗）
	 * @param data 店舗CSVデータ
	 */
	private void addShopAttribute(ShopListCsv data) {
		addWorkCharacteristicKbn(data);
		addShopCharacteristicKbn(data);
		addShopListMaterial(data);
	}
	/**
	 * 仕事の特徴を追加します
	 * @param data 店舗CSVデータ
	 */
	private void addWorkCharacteristicKbn(ShopListArbeitCsv data) {
		List<TShopListAttribute> selectWorkCharacteristicKbnList = getShopAttribute(Integer.parseInt(data.id), "work_characteristic_kbn")
				.getResultList();
		if(!selectWorkCharacteristicKbnList.isEmpty()) {
			List<String> valueList = new ArrayList<>();

			for(TShopListAttribute attribute : selectWorkCharacteristicKbnList) {
				valueList.add(String.valueOf(attribute.attributeValue));
			}

			data.workCharacteristicKbnArray = StringUtils.join(valueList, ",");
		}
	}
	/**
	 * 仕事の特徴を追加します
	 * @param data 店舗CSVデータ
	 */
	private void addWorkCharacteristicKbn(ShopListCsv data) {
		List<TShopListAttribute> selectWorkCharacteristicKbnList = getShopAttribute(Integer.parseInt(data.id), "work_characteristic_kbn")
				.getResultList();
		if(!selectWorkCharacteristicKbnList.isEmpty()) {
			List<String> valueList = new ArrayList<>();

			for(TShopListAttribute attribute : selectWorkCharacteristicKbnList) {
				valueList.add(String.valueOf(attribute.attributeValue));
			}

			data.workCharacteristicKbnArray = StringUtils.join(valueList, ",");
		}
	}
	/**
	 * 職場・会社の特徴を追加します
	 * @param data 店舗CSVデータ
	 */
	private void addShopCharacteristicKbn(ShopListArbeitCsv data) {
		List<TShopListAttribute> selectShopCharacteristicKbnList = getShopAttribute(Integer.parseInt(data.id), "shop_characteristic_kbn")
				.getResultList();

		if(!selectShopCharacteristicKbnList.isEmpty()) {
			List<String> valueList = new ArrayList<>();

			for(TShopListAttribute attribute : selectShopCharacteristicKbnList) {
				valueList.add(String.valueOf(attribute.attributeValue));
			}

			data.shopCharacteristicKbnArray = StringUtils.join(valueList, ",");
		}
	}

	/**
	 * 画像を追加します
	 * @param data 店舗CSVデータ
	 */
	private void addShopListMaterial(ShopListArbeitCsv data) {
		TShopListMaterial materialMain;
		try {
			materialMain = shopListMaterialService.getMaterialEntity(Integer.parseInt(data.id)
					, Integer.parseInt(MTypeConstants.ShopListMaterialKbn.MAIN_1));
			if (materialMain != null) {
				data.mainImg = materialMain.fileName;
			}
		} catch (NumberFormatException e) {
			//
		} catch (WNoResultException e) {
			//
		}

		try {
			TShopListMaterial materialLogo = shopListMaterialService.getMaterialEntity(Integer.parseInt(data.id)
					, Integer.parseInt(MTypeConstants.ShopListMaterialKbn.LOGO));
			if (materialLogo != null) {
				data.logoImg = materialLogo.fileName;
			}
		} catch (NumberFormatException e) {
			//
		} catch (WNoResultException e) {
			//
		}
	}

	/**
	 * 画像を追加します
	 * @param data 店舗CSVデータ
	 */
	private void addShopListMaterial(ShopListCsv data) {
		TShopListMaterial materialMain;
		try {
			materialMain = shopListMaterialService.getMaterialEntity(Integer.parseInt(data.id)
					, Integer.parseInt(MTypeConstants.ShopListMaterialKbn.MAIN_1));
			if (materialMain != null) {
				data.mainImg = materialMain.fileName;
			}
		} catch (NumberFormatException e) {
			//
		} catch (WNoResultException e) {
			//
		}
	}

	/**
	 * 職場・会社の特徴を追加します
	 * @param data 店舗CSVデータ
	 */
	private void addShopCharacteristicKbn(ShopListCsv data) {
		List<TShopListAttribute> selectShopCharacteristicKbnList = getShopAttribute(Integer.parseInt(data.id), "shop_characteristic_kbn")
				.getResultList();

		if(!selectShopCharacteristicKbnList.isEmpty()) {
			List<String> valueList = new ArrayList<>();

			for(TShopListAttribute attribute : selectShopCharacteristicKbnList) {
				valueList.add(String.valueOf(attribute.attributeValue));
			}

			data.shopCharacteristicKbnArray = StringUtils.join(valueList, ",");
		}
	}

	/**
	 * 駅情報を追加します
	 * @param data 店舗CSVデータ
	 */
	private void addStationGroup(ShopListArbeitCsv data) {
		List<TShopListLine> selectStationGroupList = getStationGroup(Integer.parseInt(data.id)).getResultList();
		List<StationGroupCsv> list =new ArrayList<>();

		if(!selectStationGroupList.isEmpty()) {
			for(TShopListLine tShopListLine : selectStationGroupList) {
				StationGroupCsv stationGroupCsv = new StationGroupCsv();
				stationGroupCsv.stationCd = String.valueOf(tShopListLine.stationCd);
				stationGroupCsv.transportationKbn = String.valueOf(tShopListLine.transportationKbn);
				stationGroupCsv.timeRequiredMinute = String.valueOf(tShopListLine.timeRequiredMinute);

				list.add(stationGroupCsv);
			}
		}
		data.stationGroupList = list;
	}
	/**
	 * 駅情報を追加します
	 * @param data 店舗CSVデータ
	 */
	private void addStationGroup(ShopListCsv data) {
		List<TShopListLine> selectStationGroupList = getStationGroup(Integer.parseInt(data.id)).getResultList();
		List<StationGroupCsv> list =new ArrayList<>();

		if(!selectStationGroupList.isEmpty()) {
			for(TShopListLine tShopListLine : selectStationGroupList) {
				StationGroupCsv stationGroupCsv = new StationGroupCsv();
				stationGroupCsv.stationCd = String.valueOf(tShopListLine.stationCd);
				stationGroupCsv.transportationKbn = String.valueOf(tShopListLine.transportationKbn);
				stationGroupCsv.timeRequiredMinute = String.valueOf(tShopListLine.timeRequiredMinute);

				list.add(stationGroupCsv);
			}
		}
		data.stationGroupList = list;
	}

	/**
	 * 駅グループの最大サイズを取得します
	 * @param shopListList
	 * @return
	 */
	public int getMaxStationGroupSize(List<ShopListArbeitCsv> shopListList) {
		int maxSize = 0;
		for(ShopListArbeitCsv shopListArbeitCsv : shopListList) {
			if(!shopListArbeitCsv.stationGroupList.isEmpty()) {
				if(maxSize < shopListArbeitCsv.stationGroupList.size()) {
					maxSize = shopListArbeitCsv.stationGroupList.size();
				}
			}
		}
		return maxSize;
	}

	/**
	 * 駅グループと一緒に店舗路線を取得
	 * @param shopListId 店舗ID
	 * @return 駅グループをセットした店舗路線のリスト。取得できない場合は空を返す
	 */
	public List<TShopListLine> getShopListLineWithStationGroup(int shopListId) {
		try {
			return shopListLineService.findByConditionInnerJoin(
					toCamelCase(VStationGroup.TABLE_NAME),
					new SimpleWhere()
						.eq(toCamelCase(TShopListLine.SHOP_LIST_ID), shopListId),
						SqlUtils.createCommaStr(
							new String[]{
								toCamelCase(TShopListLine.DISPLAY_ORDER),
								toCamelCase(TShopListLine.ID)
							}));
		} catch (WNoResultException e) {
			return new ArrayList<>(0);
		}
	}

	public List<TShopChangeJobCondition> getShopChangeJobConditionList(int shopId) {
		try {
			return shopChangeJobConditionService.findByCondition(
					new SimpleWhere().eq(toCamelCase(TShopChangeJobCondition.SHOP_LIST_ID), shopId));
		} catch (WNoResultException e) {
			return new ArrayList<>(0);
		}

	}

	public TShopChangeJobCondition getShopChangeJobCondition(int shopId, int employPtnKbn, int jobKbn) {
		try {
			return shopChangeJobConditionService.findByCondition(
					new SimpleWhere()
					.eq(toCamelCase(TShopChangeJobCondition.SHOP_LIST_ID), shopId)
					.eq(toCamelCase(TShopChangeJobCondition.EMPLOY_PTN_KBN), employPtnKbn)
					.eq(toCamelCase(TShopChangeJobCondition.JOB_KBN), jobKbn)
					).get(0);
		} catch (WNoResultException e) {
			return null;
		}

	}

	/**
	 * 系列店舗のキーワードを設定する
	 * @param shopListId
	 */
	public void updateKeywordSearch(int shopListId) {
		TShopList entity = shopListService.findById(shopListId);

		// キーワード検索項目
		// 店舗名、駅名、業態名
		List<String> keywordList = new ArrayList<>();
		keywordList.add(entity.shopName);
		keywordList.add(entity.industryText);
		keywordList.add(entity.transit);
		try {
			// 業態1，2
			Map<Integer, String> industryMap = typeService.getMTypeValueMap(MTypeConstants.IndustryKbn.TYPE_CD);
			if (industryMap.containsKey(entity.industryKbn1)) {
				keywordList.add(industryMap.get(entity.industryKbn1));
			}
			if (industryMap.containsKey(entity.industryKbn2)) {
				keywordList.add(industryMap.get(entity.industryKbn2));
			}
		// マスタが取れなければ処理しない
		} catch (WNoResultException e) {}

		// 駅
		List<TShopListLine> lineList = shopListLineService.findByShopListId(shopListId);
		for (TShopListLine line : lineList) {
			MRStation station = rStationService.findByStationCd(line.stationCd);
			if (station != null) {
				// 「駅」付きで検索された場合の対処として駅を付けておく
				keywordList.add(station.stationName + "駅");
			}
		}
		entity.keywordSearch = StringUtils.join(keywordList, " ");
		shopListService.update(entity);
	}

	/**
	 * インディードタグを追加します
	 * @param data 店舗CSVデータ
	 */
	private void addShopTag(ShopListArbeitCsv data) {
		List<String> tagList = getShopTagId(Integer.parseInt(data.id));
		data.tag = String.join("・", tagList);
	}

	/**
	 * インディードタグを追加します
	 * @param data 店舗CSVデータ
	 */
	private void addShopTag(ShopListCsv data) {
		List<String> tagList = getShopTagId(Integer.parseInt(data.id));
		data.tag = String.join("・", tagList);
	}

	/**
	 * 店舗に紐づく店舗タグIDを取得
	 * @param shopId
	 * @return
	 */
	public List<String> getShopTagId(Integer shopId){
		StringBuilder sb = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sb.append("SELECT ");
		sb.append("    MAPPING.shop_list_tag_id ");
		sb.append("FROM ");
		sb.append("    m_shop_list_tag_mapping MAPPING ");
		sb.append("WHERE ");
		sb.append("    MAPPING.shop_list_id = ? ");
		sb.append("    AND MAPPING.delete_flg = ? ");

		params.add(shopId);
		params.add(DeleteFlgKbn.NOT_DELETED);

		return jdbcManager.selectBySql(String.class, sb.toString(), params.toArray()).getResultList();
	}

}
