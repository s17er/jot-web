package com.gourmetcaree.common.logic;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.NoResultException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.exception.SNonUniqueResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.s2csv.csv.factory.S2CSVCtrlFactory;

import com.gourmetcaree.common.builder.sql.SqlBuilder.SqlCondition;
import com.gourmetcaree.common.builder.sql.TagList.ShopTagFindByNameSqlBuilder;
import com.gourmetcaree.common.builder.sql.TagList.ShopTagListCountSqlBuilder;
import com.gourmetcaree.common.builder.sql.TagList.ShopTagListIdSqlBuilder;
import com.gourmetcaree.common.builder.sql.TagList.ShopTagListSqlBuilder;
import com.gourmetcaree.common.builder.sql.TagList.WebTagFindByNameSqlBuilder;
import com.gourmetcaree.common.builder.sql.TagList.WebTagListCountSqlBuilder;
import com.gourmetcaree.common.builder.sql.TagList.WebTagListIdSqlBuilder;
import com.gourmetcaree.common.builder.sql.TagList.WebTagListSqlBuilder;
import com.gourmetcaree.common.dto.TagListDto;
import com.gourmetcaree.db.common.entity.MShopListTag;
import com.gourmetcaree.db.common.entity.MShopListTagMapping;
import com.gourmetcaree.db.common.entity.MWebTag;
import com.gourmetcaree.db.common.entity.MWebTagMapping;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.ShopListService;
import com.gourmetcaree.db.common.service.ShopListTagMappingService;
import com.gourmetcaree.db.common.service.ShopListTagService;
import com.gourmetcaree.db.common.service.WebListService;
import com.gourmetcaree.db.common.service.WebTagMappingService;
import com.gourmetcaree.db.common.service.WebTagService;

import jp.co.whizz_tech.commons.WztStringUtil;

/**
 * 系列店舗一覧タグ用ロジックです。
 * @author yamane
 *
 */
public class TagListLogic extends AbstractGourmetCareeLogic {

	/** ログオブジェクト */
	private final static Logger log = Logger.getLogger(TagListLogic.class);

	/** CSVコントローラファクトリ */
	@Resource
	private S2CSVCtrlFactory s2CSVCtrlFactory;

	@Resource
	private ShopListService shopListService;

	@Resource
	private ShopListTagService shopListTagService;

	@Resource
	private ShopListTagMappingService shopListTagMappingService;

	@Resource
	private WebTagService webTagService;

	@Resource
	private WebTagMappingService webTagMappingService;

	@Resource
	private WebListService webListService;

	/**
	 * 紐づいているタグの一覧を取得する
	 * @throws WNoResultException
	 */
	public List<TagListDto> getShopListTagListByMapping() throws WNoResultException {

		List<TagListDto> dtoList = new ArrayList<>();

		/* 一覧を取得 */
		SqlCondition conditionId = new ShopTagListIdSqlBuilder().build();
		List<MShopListTagMapping> shopListTagMappingList = jdbcManager.selectBySql(MShopListTagMapping.class, conditionId.getSql(), conditionId.getParamsArray())
				.disallowNoResult()
				.getResultList();

		/* 店舗IDと店舗名を取得 */
		if (!shopListTagMappingList.isEmpty()) {

			for (MShopListTagMapping mapping : shopListTagMappingList) {

				if (mapping.shopListId == null) {
					continue;
				}

				TagListDto dto = new TagListDto();

				dto.id = String.valueOf(mapping.shopListId);
				dto.tagName = shopListService.findById(mapping.shopListId).shopName;

				SqlCondition condition = new ShopTagListSqlBuilder(mapping.shopListId).build();

				List<MShopListTag> shopTagList = jdbcManager.selectBySql(
						MShopListTag.class,
						condition.getSql(),
						condition.getParamsArray()
						)
						.disallowNoResult()
						.getResultList();

				if (CollectionUtils.isEmpty(shopTagList)) {
					continue;
				}

				for (MShopListTag entity : shopTagList) {
					dto.tagNameList.add(entity.shopListTagName);
				}

				dtoList.add(dto);
			}
		} else {

			dtoList = new ArrayList<>();
		}

		return dtoList;
	}

	/**
	 * 紐づいているタグの一覧を取得する
	 * @throws WNoResultException
	 */
	public List<TagListDto> getWebTagListByMapping() throws WNoResultException {

		List<TagListDto> dtoList = new ArrayList<>();

		/* 一覧を取得 */
		SqlCondition conditionId = new WebTagListIdSqlBuilder().build();
		List<MWebTagMapping> webTagMappingList = jdbcManager.selectBySql(MWebTagMapping.class, conditionId.getSql(), conditionId.getParamsArray())
				.disallowNoResult()
				.getResultList();

		/* 店舗IDと店舗名を取得 */
		if (!webTagMappingList.isEmpty()) {

			for (MWebTagMapping mapping : webTagMappingList) {

				if (mapping.webId == null) {
					continue;
				}

				TagListDto dto = new TagListDto();

				dto.id = String.valueOf(mapping.webId);
				dto.tagName = webListService.getSingleVWebListById(mapping.webId).manuscriptName;

				SqlCondition condition = new WebTagListSqlBuilder(mapping.webId).build();

				List<MWebTag> webTagList = jdbcManager.selectBySql(
						MWebTag.class,
						condition.getSql(),
						condition.getParamsArray()
						)
						.disallowNoResult()
						.getResultList();

				if (CollectionUtils.isEmpty(webTagList)) {
					continue;
				}

				for (MWebTag entity : webTagList) {
					dto.tagNameList.add(entity.webTagName);
				}


				dtoList.add(dto);
			}
		} else {
			dtoList = new ArrayList<>();
		}


		return dtoList;
	}

	/**
	 * WEB用のタグで使用頻度の高いタグ名をリストで取得する
	 * @return
	 */
	public List<TagListDto> getCommonlyUsedFindByWebTagOrderByCount() {

		List<TagListDto> dtoList = new ArrayList<>();
		List<MWebTagMapping> webTagMappingList ;

		try {

			SqlCondition condition = new WebTagListCountSqlBuilder().build();
			webTagMappingList = jdbcManager.selectBySql(
					MWebTagMapping.class,
					condition.getSql(),
					condition.getParamsArray()
					)
					.disallowNoResult()
					.getResultList();
		} catch (NoResultException e) {

			return new ArrayList<>();
		}

		for (MWebTagMapping entity : webTagMappingList) {
			MWebTag tag = jdbcManager.from(MWebTag.class)
					.where(new SimpleWhere()
							.eq(MWebTag.ID, entity.webTagId)
							.eq(WztStringUtil.toCamelCase(MWebTag.DELETE_FLG), DeleteFlgKbn.NOT_DELETED)
							)
					.getSingleResult();

			if (null == tag) {
				continue;
			}

			TagListDto dto = new TagListDto();
			dto.id = String.valueOf(tag.id);
			dto.tagName = tag.getWebTagName();

			dtoList.add(dto);
		}

		if (CollectionUtils.isEmpty(dtoList)) {
			return new ArrayList<>();
		}

		return dtoList;
	}

	/**
	 * 店舗一覧用のタグで使用頻度の高いタグ名をリストで取得する
	 * @return
	 */
	public List<TagListDto> getCommonlyUsedFindByShopListTagOrderByCount() {

		List<TagListDto> dtoList = new ArrayList<>();
		List<MShopListTagMapping> shopListTagMappingList;
		try {

			SqlCondition condition = new ShopTagListCountSqlBuilder().build();
			shopListTagMappingList = jdbcManager.selectBySql(
					MShopListTagMapping.class,
					condition.getSql(),
					condition.getParamsArray()
					)
					.disallowNoResult()
					.getResultList();

		} catch (NoResultException e) {
			return new ArrayList<>();
		}

		for (MShopListTagMapping entity : shopListTagMappingList) {
			MShopListTag tag = jdbcManager.from(MShopListTag.class)
			.where(new SimpleWhere()
					.eq(MShopListTag.ID, entity.shopListTagId)
					.eq(WztStringUtil.toCamelCase(MShopListTag.DELETE_FLG), DeleteFlgKbn.NOT_DELETED)
					)
			.getSingleResult();

			if (null == tag) {
				continue;
			}

			TagListDto dto = new TagListDto();
			dto.id = String.valueOf(tag.id);
			dto.tagName = tag.getShopListTagName();

			dtoList.add(dto);
		}

		if (CollectionUtils.isEmpty(dtoList)) {
			return new ArrayList<>();
		}

		return dtoList;
	}

	/**
	 * Web用のタグ登録ロジック
	 * @param mWebTagList タグ名のリスト
	 * @param webId　WEBID
	 * @return
	 */
	public int insertWebTag(List<String> mWebTagList, Integer webId) {

		MWebTagMapping webTagMapping = new MWebTagMapping();
		webTagMapping.webId = webId;

		for (String name : mWebTagList) {

			try {

				// 既に登録されているタグを取得する
				// 同じものが登録されていた場合、あたら行く登録はしない
				MWebTag mWebTag = findByWebTagName(name);

				webTagMapping.webTagId = mWebTag.id;

				webTagMappingService.insert(webTagMapping);

			} catch (NoResultException e) {

				MWebTag entity = new MWebTag();
				entity.webTagName = name;

				webTagService.insert(entity);
				webTagMapping.webTagId = entity.id;

				webTagMappingService.insert(webTagMapping);
			}

		}

		return 0;
	}

	/**
	 * タグ名で検索を行う
	 * @param name タグ名
	 * @return
	 */
	public MWebTag findByWebTagName(String name) {
		SqlCondition conditionWebTag = new WebTagFindByNameSqlBuilder(name).build();
		MWebTag mWebTag = jdbcManager.selectBySql(
				MWebTag.class,
				conditionWebTag.getSql(),
				conditionWebTag.getParamsArray()
				)
				.disallowNoResult()
				.getSingleResult();
		return mWebTag;
	}

	/**
	 * Shop用のタグ登録ロジック
	 * @param mShopListTagList
	 * @param shopId
	 * @return
	 */
	public int insertShopList(List<String> mShopListTagList, Integer shopId) {

		MShopListTagMapping shopListTagMapping = new MShopListTagMapping();
		shopListTagMapping.shopListId = shopId;

		for (String name : mShopListTagList) {

			try {

				// 既に登録されているタグを取得する
				// 同じものが登録されていた場合、あたらしく登録はしない
				MShopListTag shopListTag = findByShopListTagName(name);

				shopListTagMapping.shopListTagId = shopListTag.id;

				shopListTagMappingService.insert(shopListTagMapping);

			} catch (NoResultException e) {

				MShopListTag entity = new MShopListTag();
				entity.shopListTagName = name;

				shopListTagService.insert(entity);
				shopListTagMapping.shopListTagId = entity.id;

				shopListTagMappingService.insert(shopListTagMapping);
			}

		}

		return 0;
	}

	/**
	 * タグ名で検索を行う
	 * @param name タグ名
	 * @return
	 */
	public MShopListTag findByShopListTagName(String name) {
		SqlCondition conditionWebTag = new ShopTagFindByNameSqlBuilder(name).build();
		MShopListTag shopListTag = jdbcManager.selectBySql(
				MShopListTag.class,
				conditionWebTag.getSql(),
				conditionWebTag.getParamsArray()
				)
				.disallowNoResult()
				.getSingleResult();
		return shopListTag;
	}

	/**
	 * 店舗一覧IDに登録されているタグを取得する
	 * @param shopListId
	 * @return
	 */
	public List<MShopListTag> findByShopListId(Integer shopListId) {
		SqlCondition condition = new ShopTagListSqlBuilder(shopListId).build();

		try {
			List<MShopListTag> shopListTag = jdbcManager.selectBySql(
					MShopListTag.class,
					condition.getSql(),
					condition.getParamsArray()
					)
					.disallowNoResult()
					.getResultList();

			return shopListTag;

		} catch (NoResultException e) {
			return new ArrayList<>();
		}
	}

	/**
	 * WebデータIDに登録されているタグを取得する
	 * @param webdataId
	 * @return
	 */
	public List<MWebTag> findByWebDataId(Integer webdataId) {
		SqlCondition condition = new WebTagListSqlBuilder(webdataId).build();

		try {
			List<MWebTag> webDataTag = jdbcManager.selectBySql(
					MWebTag.class,
					condition.getSql(),
					condition.getParamsArray()
					)
					.disallowNoResult()
					.getResultList();

			return webDataTag;

		} catch (NoResultException e) {
			return new ArrayList<>();
		}
	}

	/**
	 * タグを更新する
	 * @param updateTag
	 * @param shopListId
	 */
	public void update(List<String> updateTag, Integer shopListId) {

		shopListTagMappingService.deleteByShopListId(shopListId);

		insertShopList(updateTag, shopListId);
	}

	/**
	 * タグを更新する
	 * @param updateTag
	 * @param webDataId
	 */
	public void updateWebData(List<String> updateTag, Integer webDataId) {

		webTagMappingService.deleteByWebDataId(webDataId);

		insertWebTag(updateTag, webDataId);
	}

	/**
	 * タグ名の登録を行う
	 * <P>
	 * 同じ名称は登録不可
	 * </P>
	 * @param tag
	 */
	public MShopListTag updateTagNameByUnique(MShopListTag tag) {

		try {

			// 既に登録されているタグを取得する
			// 同じものが登録されていた場合、あたらしく登録はしない
			MShopListTag entity = findByShopListTagName(tag.getShopListTagName());
			if (entity.id.equals(tag.id)) {
				shopListTagService.updateIncludesVersion(tag);
			} else {
				return new MShopListTag();
			}

		/** 存在しない場合、登録（更新）する */
		} catch (NoResultException e) {

			shopListTagService.updateIncludesVersion(tag);

		/* 2件以上存在した場合、登録（更新）しない */
		} catch (SNonUniqueResultException e) {
			return new MShopListTag();
		}

		return null;
	}

	/**
	 * タグ名の登録を行う
	 * <P>
	 * 同じ名称は登録不可
	 * </P>
	 * @param tag
	 */
	public MWebTag updateTagNameByUnique(MWebTag tag) {

		try {

			// 既に登録されているタグを取得する
			// 同じものが登録されていた場合、あたらしく登録はしない
			MWebTag entity = findByWebTagName(tag.getWebTagName());
			if (entity.id.equals(tag.id)) {
				webTagService.updateIncludesVersion(tag);
			} else {
				return new MWebTag();
			}

		/** 存在しない場合、登録（更新）する */
		} catch (NoResultException e) {

			webTagService.updateIncludesVersion(tag);

		/* 2件以上存在した場合、登録（更新）しない */
		} catch (SNonUniqueResultException e) {
			return new MWebTag();
		}

		return null;
	}

	/**
	 * Webデータ用のマッピングデータを取得する
	 * @param webDataId
	 * @return
	 */
	public List<MWebTagMapping> webDataMappingfindByWebDataId(Integer webDataId) {

		try {
			return jdbcManager.from(MWebTagMapping.class)
						.where(new SimpleWhere()
								.eq(WztStringUtil.toCamelCase(MWebTagMapping.WEB_ID), webDataId)
								.eq(WztStringUtil.toCamelCase(MWebTagMapping.DELETE_FLG), DeleteFlgKbn.NOT_DELETED)
								)
						.orderBy(WztStringUtil.toCamelCase(MWebTagMapping.ID))
						.disallowNoResult()
						.getResultList();
		} catch (NoResultException e) {
			return new ArrayList<>();
		}

	}

	/**
	 * タグを全て取得
	 * @return タグ一覧
	 */
	public List<TagListDto> shopTagFindAll() {
		List<TagListDto> dtoList = new ArrayList<>();

		/* タグ一覧を取得 */
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT id, shop_list_tag_name ");
		sql.append(" FROM m_shop_list_tag ");
		sql.append(" WHERE delete_flg = ? ");
		sql.append(" ORDER BY id ");

		SqlCondition conditionId = new SqlCondition(sql,DeleteFlgKbn.NOT_DELETED);

		List<MShopListTag> shopListTagList = jdbcManager.selectBySql(MShopListTag.class, conditionId.getSql(), conditionId.getParamsArray())
				.disallowNoResult()
				.getResultList();		

		// タグID、タグ名を取得
		if (!shopListTagList.isEmpty()) {
			for (MShopListTag tag : shopListTagList) {
				TagListDto dto = new TagListDto();
				dto.id = String.valueOf(tag.id);
				dto.tagName = tag.shopListTagName;

				dtoList.add(dto);
			}
		} else {
			dtoList = new ArrayList<>();
		}

		return dtoList;
	}
}
