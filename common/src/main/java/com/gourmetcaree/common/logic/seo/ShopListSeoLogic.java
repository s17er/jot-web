package com.gourmetcaree.common.logic.seo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

import com.gourmetcaree.accessor.web.WebDataAccessor;
import com.gourmetcaree.arbeitsys.service.ArbeitSubArbeitAreaService;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TShopList;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.TypeService;
import com.gourmetcaree.db.common.service.VolumeService;
import com.gourmetcaree.db.common.service.WebAttributeService;

/**
 * 店舗一覧用SEOロジック
 * @author nakamori
 *
 */
public class ShopListSeoLogic {

	private static final Logger log = Logger.getLogger(ShopListSeoLogic.class);

	@Resource
	private TypeService typeService;

	@Resource
	private WebAttributeService webAttributeService;

	@Resource
	private VolumeService volumeService;

	@Resource(name = "arbeitSubArbeitAreaService")
	private ArbeitSubArbeitAreaService subAreaService;

	/**
	 * 店舗一覧のSEOの作成。
	 * 現段階では、DESCRIPTIONのみを生成して返している。
	 */
	public SeoResult createSeo(TShopList shop, WebDataAccessor web) {

		SeoResult result = new SeoResult();

		// サブエリア
		appendSubArea(shop, result);



		// 店舗名
		result.description.append(shop.shopName)
					.append("を経営する企業の募集です。");

		result.title.append(shop.shopName);
		result.keywords.append(shop.shopName);


		// 原稿名
		result.title.append(web.getManuscriptName());
		result.keywords.append(web.getManuscriptName());




		// 業態系
		appendIndustries(web, result);

		// 職種
		appendJobs(web, result);

		// タイトルだけ勤務地をつける。
		appendWorkingPlace(web, result);

		// descriptionに店舗の交通を追加
		appendShopTransit(shop, result);


		return result;
	}


	/**
	 * Descriptionにバイト用サブエリアを設定
	 */
	private void appendSubArea(TShopList shop, SeoResult result) {
		if (shop.arbeitSubAreaId == null) {
			return;
		}
		final String areaName = subAreaService.convertIdToName(shop.arbeitSubAreaId);
		if (StringUtils.isNotBlank(areaName)) {
			result.description.append(areaName)
								.append("の");
		}
	}

	/**
	 * 業種の追加
	 */
	private void appendIndustries(WebDataAccessor web, SeoResult seoResult) {
		try {
			Map<Integer, String> industryMap = typeService.getMTypeValueMap(MTypeConstants.IndustryKbn.TYPE_CD);
			List<String> industryNameList = new ArrayList<String>();
			for (Integer kbn : web.getIndustryList()) {
				if (industryMap.containsKey(kbn)) {
					industryNameList.add(industryMap.get(kbn));
				}
			}

			final String industryStr = StringUtils.join(industryNameList, ", ");
			seoResult.description.append("業種は");
			seoResult.description.append(industryStr);
			seoResult.description.append("を展開中。");

			seoResult.keywords.append(industryStr);
			seoResult.title.append(" 求人業種（")
						.append(industryStr)
						.append("）");

		} catch (WNoResultException e) {
			log.warn("業種のSEO生成に失敗", e);
		}
	}

	/**
	 * 職種の追加
	 */
	private void appendJobs(WebDataAccessor web, SeoResult seoResult) {
		final String typeCd = MTypeConstants.JobKbn.TYPE_CD;
		List<Integer> jobList = webAttributeService.getWebAttributeValueIntegerList(web.getId(), typeCd);
		List<String> nameList = typeService.getTypeNameList(jobList, typeCd);

		if (CollectionUtils.isEmpty(nameList)) {
			return;
		}

		final String jobStr = StringUtils.join(nameList, ", ");
		seoResult.description.append("今回募集している職種は");
		seoResult.description.append(jobStr);
		seoResult.description.append("です。");

		seoResult.keywords.append(", ").append(jobStr);

		seoResult.title.append("職種（")
						.append(jobStr)
						.append("）");
	}

	/**
	 * 勤務地をアペンド
	 * @param web
	 * @param result
	 */
	private void appendWorkingPlace(WebDataAccessor web, SeoResult result) {
		String workingPlace = web.getWorkingPlace();
		if (StringUtils.isBlank(workingPlace)) {
			return;
		}

		result.title.append("勤務地（")
					.append(GourmetCareeUtil.replaceCrlfToSpace(workingPlace))
					.append("）");

	}

	/**
	 * 店舗の交通をアペンド(非WEBデータ)
	 */
	private void appendShopTransit(TShopList shop, SeoResult result) {
		if (StringUtils.isBlank(shop.transit)) {
			return;
		}

		result.description.append(" 交通：")
							.append(GourmetCareeUtil.replaceCrlfToSpace(shop.transit));
	}



	/**
	 * 作成したSEOのリザルト
	 *
	 */
	public static class SeoResult implements Serializable {

		/**
		 *
		 */
		private static final long serialVersionUID = 1399800737797761554L;

		private final StringBuilder keywords;

		private final StringBuilder description;

		private final StringBuilder title;



		public SeoResult() {
			this.keywords = new StringBuilder();
			this.description = new StringBuilder();
			this.title = new StringBuilder();
		}

		public String getKeywords() {
			return keywords.toString();
		}

		public String getDescription() {
			return description.toString();
		}

		public String getTitle() {
			return title.toString();
		}


		@Override
		public String toString() {
			return ToStringBuilder.reflectionToString(this);
		}
	}
}
