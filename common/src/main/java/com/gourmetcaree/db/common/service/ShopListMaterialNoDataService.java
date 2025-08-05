package com.gourmetcaree.db.common.service;

import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.whizz_tech.commons.WztStringUtil;

import org.apache.commons.collections.CollectionUtils;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants.MaterialKbn;
import com.gourmetcaree.db.common.constants.MTypeConstants.ShopListMaterialKbn;
import com.gourmetcaree.db.common.entity.VShopListMaterialNoData;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.shopList.dto.shopList.ShopListMaterialExistsDto;


/**
 * 素材データからデータ内容を除いたデータビューのサービスクラスです。
 * @author Makoto Otani
 * @version 1.0
 */
public class ShopListMaterialNoDataService extends AbstractGroumetCareeReferenceService<VShopListMaterialNoData> {

	/**
	 * shopListIdと素材区分を指定してエンティティを取得します。
	 * @param shopListId
	 * @param materialKbn
	 * @return
	 * @throws WNoResultException
	 */
	public VShopListMaterialNoData getMaterialEntity(int shopListId, int materialKbn)
	throws WNoResultException {

		SimpleWhere where = getWhereByShopListId(shopListId, materialKbn);
		return findByCondition(where).get(0);
	}

	/**
	 * 店舗一覧データに紐付く素材情報を取得します。
	 * @param shopListId
	 * @return
	 * @throws WNoResultException
	 */
	public List<VShopListMaterialNoData> getMaterialList(int shopListId)
	throws WNoResultException {

		SimpleWhere where = new SimpleWhere()
								.eq(toCamelCase(VShopListMaterialNoData.SHOP_LIST_ID), shopListId);
		return findByCondition(where);
	}

	/**
	 * 素材データが存在しているかを取得します。
	 * @param shopListId
	 * @param materialKbn
	 * @return true:存在する、false:存在しない
	 */
	public boolean isMaterialEntityExist(int shopListId, int materialKbn) {

		SimpleWhere where = getWhereByShopListId(shopListId, materialKbn);
		long count = countRecords(where);

		return (count > 0) ? true : false;
	}

	/**
	 * 店舗一覧IDと素材区分を特定した検索条件です。
	 * @param shopListId
	 * @param materialKbn
	 * @return
	 */
	private SimpleWhere getWhereByShopListId(int shopListId, int materialKbn) {
		SimpleWhere where = new SimpleWhere()
		.eq(toCamelCase(VShopListMaterialNoData.SHOP_LIST_ID), shopListId)
		.eq(toCamelCase(VShopListMaterialNoData.MATERIAL_KBN), materialKbn)
		;
		return where;
	}

	/**
	 * レコードのユニークキーを取得します。
	 * データが存在しない場合はブランクを返します。
	 * @param shopListId
	 * @param materialKbn
	 * @return
	 */
	public String getMaterialUniqueCd(int shopListId, int materialKbn) {
		try {
			VShopListMaterialNoData entity = getMaterialEntity(shopListId, materialKbn);
			return GourmetCareeUtil.createUniqueKey(entity.insertDatetime);
		} catch (WNoResultException e) {
			return "";
		}
	}

	/**
	 * 画像のユニークキーを保持するMapを取得します。
	 * @param shopListId
	 * @return
	 */
	public Map<String, String> getImageUniqueKeyMap(int shopListId) {
		Map<String, String> imageCheckMap = new HashMap<String, String>();

		try {
			List<VShopListMaterialNoData> retList = getMaterialList(shopListId);

			for (VShopListMaterialNoData entity : retList) {
				imageCheckMap.put(Integer.toString(entity.materialKbn), GourmetCareeUtil.createUniqueKey(entity.insertDatetime));
			}

			return imageCheckMap;
		} catch (WNoResultException e) {
			return  new HashMap<String, String>();
		}
	}

	/**
	 * 素材が存在するか保持するDtoを取得します。
	 * @param shopListId WEBデータID
	 * @return 素材が存在するかどうかを保持するDto
	 */
	public ShopListMaterialExistsDto getExistsDto(int shopListId) {

		SimpleWhere where = new SimpleWhere()
			.eq(toCamelCase(VShopListMaterialNoData.SHOP_LIST_ID), shopListId);

		try {
			List<VShopListMaterialNoData> list = findByCondition(where);

			ShopListMaterialExistsDto existsDto = new ShopListMaterialExistsDto();

			for (VShopListMaterialNoData entity : list) {

				String materialKbn = String.valueOf(entity.materialKbn);

				if (MaterialKbn.MAIN_1.equals(materialKbn)) {
					existsDto.isMain1ExistFlg = true;

				} else if (MaterialKbn.MAIN_1_THUMB.equals(materialKbn)) {
					existsDto.isMain1ThumbExistFlg = true;

				} else if(ShopListMaterialKbn.LOGO.equals(materialKbn)) {
					existsDto.isLogoExistFlg = true;
				} else if (ShopListMaterialKbn.LOGO_THUMB.equals(materialKbn)) {
					existsDto.isLogoThumbExistFlg = true;
				}
			}
			return existsDto;

		} catch (WNoResultException e) {
			return new ShopListMaterialExistsDto();
		}
	}

	/**
	 * 画像があるかどうか
	 * @param shopListId
	 * @param materialKbn
	 * @return
	 */
	public boolean isMaterialExists(int shopListId, int materialKbn) {
		List<VShopListMaterialNoData> entityList = jdbcManager.from(VShopListMaterialNoData.class)
															.where(new SimpleWhere()
															.eq(WztStringUtil.toCamelCase(VShopListMaterialNoData.SHOP_LIST_ID), shopListId)
															.eq(WztStringUtil.toCamelCase(VShopListMaterialNoData.MATERIAL_KBN), materialKbn)
															)
															.getResultList();

		return CollectionUtils.isNotEmpty(entityList);
	}
}