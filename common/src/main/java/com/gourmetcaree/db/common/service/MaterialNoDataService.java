package com.gourmetcaree.db.common.service;

import static jp.co.whizz_tech.commons.WztStringUtil.toCamelCase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants.MaterialKbn;
import com.gourmetcaree.db.common.entity.VMaterialNoData;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.webdata.dto.webdata.MaterialExistsDto;

/**
 * 素材データからデータ内容を除いたデータビューのサービスクラスです。
 * @author Makoto Otani
 * @version 1.0
 */
public class MaterialNoDataService extends AbstractGroumetCareeReferenceService<VMaterialNoData> {

	/**
	 * webIdと素材区分を指定してエンティティを取得します。
	 * @param webId
	 * @param materialKbn
	 * @return
	 * @throws WNoResultException
	 */
	public VMaterialNoData getMaterialEntity(int webId, int materialKbn)
	throws WNoResultException {

		SimpleWhere where = getWhereByWebId(webId, materialKbn);
		return findByCondition(where).get(0);
	}

	/**
	 * webデータに紐付く素材情報を取得します。
	 * @param webId
	 * @return
	 * @throws WNoResultException
	 */
	public List<VMaterialNoData> getMaterialList(int webId)
	throws WNoResultException {

		SimpleWhere where = new SimpleWhere()
								.eq(toCamelCase(VMaterialNoData.WEB_ID), webId);
		return findByCondition(where);
	}

	/**
	 * 素材データが存在しているかを取得します。
	 * @param webId
	 * @param materialKbn
	 * @return true:存在する、false:存在しない
	 */
	public boolean isMaterialEntityExist(int webId, int materialKbn) {

		SimpleWhere where = getWhereByWebId(webId, materialKbn);
		long count = countRecords(where);

		return (count > 0) ? true : false;
	}

	/**
	 * WebdataIdと素材区分を特定した検索条件です。
	 * @param webId
	 * @param materialKbn
	 * @return
	 */
	private SimpleWhere getWhereByWebId(int webId, int materialKbn) {
		SimpleWhere where = new SimpleWhere()
		.eq(toCamelCase(VMaterialNoData.WEB_ID), webId)
		.eq(toCamelCase(VMaterialNoData.MATERIAL_KBN), materialKbn)
		;
		return where;
	}

	/**
	 * レコードのユニークキーを取得します。
	 * データが存在しない場合はブランクを返します。
	 * @param webId
	 * @param materialKbn
	 * @return
	 */
	public String getMaterialUniqueCd(int webId, int materialKbn) {
		try {
			VMaterialNoData entity = getMaterialEntity(webId, materialKbn);
			return GourmetCareeUtil.createUniqueKey(entity.insertDatetime);
		} catch (WNoResultException e) {
			return "";
		}
	}

	/**
	 * 画像のユニークキーを保持するMapを取得します。
	 * @param webId
	 * @return
	 */
	public Map<String, String> getImageUniqueKeyMap(int webId) {
		Map<String, String> imageCheckMap = new HashMap<String, String>();

		try {
			List<VMaterialNoData> retList = getMaterialList(webId);

			for (VMaterialNoData entity : retList) {
				imageCheckMap.put(Integer.toString(entity.materialKbn), GourmetCareeUtil.createUniqueKey(entity.insertDatetime));
			}

			return imageCheckMap;
		} catch (WNoResultException e) {
			return  new HashMap<String, String>();
		}
	}

	/**
	 * 素材が存在するか保持するDtoを取得します。
	 * @param webId WEBデータID
	 * @return 素材が存在するかどうかを保持するDto
	 */
	public MaterialExistsDto getExistsDto(int webId) {

		SimpleWhere where = new SimpleWhere()
			.eq(toCamelCase(VMaterialNoData.WEB_ID), webId);

		try {
			List<VMaterialNoData> list = findByCondition(where);

			MaterialExistsDto existsDto = new MaterialExistsDto();

			for (VMaterialNoData entity : list) {

				String materialKbn = String.valueOf(entity.materialKbn);

				if (MaterialKbn.MAIN_1.equals(materialKbn)) {
					existsDto.isMain1ExistFlg = true;

				} else if (MaterialKbn.MAIN_1_THUMB.equals(materialKbn)) {
					existsDto.isMain1ThumbExistFlg = true;

				} if (MaterialKbn.MAIN_2.equals(materialKbn)) {
					existsDto.isMain2ExistFlg = true;

				} else if (MaterialKbn.MAIN_2_THUMB.equals(materialKbn)) {
					existsDto.isMain2ThumbExistFlg = true;

				} else if (MaterialKbn.MAIN_3.equals(materialKbn)) {
					existsDto.isMain3ExistFlg = true;

				} else if (MaterialKbn.MAIN_3_THUMB.equals(materialKbn)) {
					existsDto.isMain3ThumbExistFlg = true;

				} else if (MaterialKbn.LOGO.equals(materialKbn)) {
					existsDto.isLogoExistFlg = true;

				} else if (MaterialKbn.LOGO_THUMB.equals(materialKbn)) {
					existsDto.isLogoThumbExistFlg = true;

				} else if (MaterialKbn.RIGHT.equals(materialKbn)) {
					existsDto.isRightExistFlg = true;

				} else if (MaterialKbn.RIGHT_THUMB.equals(materialKbn)) {
					existsDto.isRightThumbExistFlg = true;

				} else if (MaterialKbn.PHOTO_A.equals(materialKbn)) {
					existsDto.isPhotoAExistFlg = true;

				} else if (MaterialKbn.PHOTO_A_THUMB.equals(materialKbn)) {
					existsDto.isPhotoAThumbExistFlg = true;

				} else if (MaterialKbn.PHOTO_B.equals(materialKbn)) {
					existsDto.isPhotoBExistFlg = true;

				} else if (MaterialKbn.PHOTO_B_THUMB.equals(materialKbn)) {
					existsDto.isPhotoBThumbExistFlg = true;

				} else if (MaterialKbn.PHOTO_C.equals(materialKbn)) {
					existsDto.isPhotoCExistFlg = true;

				} else if (MaterialKbn.PHOTO_C_THUMB.equals(materialKbn)) {
					existsDto.isPhotoCThumbExistFlg = true;

				} else if (MaterialKbn.FREE.equals(materialKbn)) {
					existsDto.isFreeExistFlg = true;

				} else if (MaterialKbn.FREE_THUMB.equals(materialKbn)) {
					existsDto.isFreeThumbExistFlg = true;

				} else if (MaterialKbn.ATTENTION_SHOP.equals(materialKbn)) {
					existsDto.isAttentionShopExistFlg = true;

				} else if (MaterialKbn.ATTENTION_SHOP_THUMB.equals(materialKbn)) {
					existsDto.isAttentionShopThumbExistFlg = true;

				} else if (MaterialKbn.ATTENTION_HERE.equals(materialKbn)) {
					existsDto.isAttentionHereExistFlg = true;

				} else if (MaterialKbn.ATTENTION_HERE_THUMB.equals(materialKbn)) {
					existsDto.isAttentionHereThumbExistFlg = true;

				} else if (MaterialKbn.MOVIE_WM.equals(materialKbn)) {
					existsDto.isMovieWmExistFlg = true;

				} else if (MaterialKbn.MOVIE_QT.equals(materialKbn)) {
					existsDto.isMovieQtExistFlg = true;
				}
			}
			return existsDto;

		} catch (WNoResultException e) {
			return new MaterialExistsDto();
		}


	}
}