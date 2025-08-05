package com.gourmetcaree.admin.service.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.persistence.NoResultException;

import org.apache.commons.collections.CollectionUtils;
import org.seasar.framework.beans.util.Beans;

import com.gourmetcaree.admin.service.dto.ShopListDto;
import com.gourmetcaree.admin.service.dto.ShopListDto.LabelDto;
import com.gourmetcaree.common.logic.ValueToNameConvertLogic;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TShopList;
import com.gourmetcaree.db.common.entity.TShopListLabel;
import com.gourmetcaree.db.common.entity.TShopListLabelGroup;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.CityService;
import com.gourmetcaree.db.common.service.PrefecturesService;
import com.gourmetcaree.db.common.service.ShopListLabelGroupService;
import com.gourmetcaree.db.common.service.ShopListLabelService;
import com.gourmetcaree.db.common.service.ShopListService;
import com.gourmetcaree.db.common.service.TypeService;

/**
 * 系列店舗のロジック
 * @author whizz
 *
 */
public class ShopListLogic extends AbstractAdminLogic {

	@Resource
	private ShopListService shopListService;

	@Resource
	private TypeService typeService;

	@Resource
	private PrefecturesService prefecturesService;

	@Resource
	private CityService cityService;

	@Resource
	private ShopListLabelService shopListLabelService;

	@Resource
	private ShopListLabelGroupService shopListLabelGroupService;

	@Resource
	protected ValueToNameConvertLogic valueToNameConvertLogic;


	/**
	 * 系列店舗一覧のDTOを顧客IDで取得する。
	 * 系列店舗のラベルも一緒に取得。
	 * 主にWEBデータ関連での項目をDTOにセット
	 * @param customerId
	 * @return 系列店舗の一覧。取得できない場合は空のリストを返す
	 */
	public List<ShopListDto> getWebShopListByCustomerId(int customerId) {
		return convertShopListDto(shopListService.findByCustomerIdWithLabelGroups(customerId));

	}

	/**
	 * 系列店舗一覧のDTOをIDで取得する。
	 * 系列店舗のラベルも一緒に取得。
	 * 主にWEBデータ関連での項目をDTOにセット
	 * @param shopListIdList
	 * @return 系列店舗の一覧。取得できない場合は空のリストを返す
	 */
	public List<ShopListDto> getWebShopListByIds(List<Integer> shopListIdList) {
		return convertShopListDto(shopListService.findByIdsWithLabelGroups(shopListIdList));
	}

	/**
	 * 系列店舗のリストを業態、住所の名前をセットしたDTOの一覧に変換する
	 * @param list
	 * @return 系列店舗一覧のDTOリスト。引数リストが空の場合は空のリストを返す
	 */
	public List<ShopListDto> convertShopListDto(List<TShopList> list) {

		if (CollectionUtils.isEmpty(list)) {
			return new ArrayList<>(0);
		}

		// 業態取得
		Map<Integer, String> industryMap = typeService.getMTypeValueMapWithoutThrow(MTypeConstants.IndustryKbn.TYPE_CD);

		// 住所
		Map<Integer, String> todouhukenMap = prefecturesService.findAll().stream().collect(Collectors.toMap(s -> s.prefecturesCd, s -> s.prefecturesName));
		Map<String, String> tempCityMap = new  HashMap<>();

		// ラベル
		Map<Integer, String> labelMap = new HashMap<>();


		List<ShopListDto> dtoList = new ArrayList<>();
		for (TShopList entity : list) {
			ShopListDto dto = Beans.createAndCopy(ShopListDto.class, entity).execute();

			// 業態のラベルを設定
			dto.industryKbn1Label = (dto.industryKbn1 != null || industryMap.containsKey(dto.industryKbn1) ? industryMap.get(dto.industryKbn1) : "");
			dto.industryKbn2Label = (dto.industryKbn2 != null || industryMap.containsKey(dto.industryKbn2) ? industryMap.get(dto.industryKbn2) : "");

			// 住所
			if(entity.domesticKbn == MTypeConstants.DomesticKbn.DOMESTIC) {
				dto.prefecturesName = (dto.prefecturesCd != null || todouhukenMap.containsKey(dto.prefecturesCd) ? todouhukenMap.get(dto.prefecturesCd) : "");
				dto.cityName = "";
				if (dto.cityCd != null) {
					if (tempCityMap.containsKey(dto.cityCd)) {
						dto.cityName = tempCityMap.get(dto.cityCd);
					} else {
						dto.cityName = cityService.getName(dto.cityCd);
						tempCityMap.put(dto.cityCd, dto.cityName);
					}
				}
			}else {
				dto.prefecturesName = "海外";
				dto.cityName = valueToNameConvertLogic.convertToForeginAreaName(String.valueOf(entity.shutokenForeignAreaKbn));
			}


			// ラベルをセット
			for (TShopListLabelGroup label : entity.tShopListLabelGroupList) {
				if (!labelMap.containsKey(label.shopListLabelId)) {
					try {
						TShopListLabel shopListLabel = shopListLabelService.findById(label.shopListLabelId);
						labelMap.put(label.shopListLabelId, shopListLabel.labelName);
					} catch (NoResultException e) {
						// ラベルがなければ処理しない
						continue;
					}
				}
				LabelDto labelDto = new LabelDto();
				labelDto.id = label.shopListLabelId;
				labelDto.labelName = labelMap.get(label.shopListLabelId);
				dto.labelDtoList.add(labelDto);
			}
			dtoList.add(dto);
		}

		return dtoList;
	}


	public void updateShopListLabel(int id, int customerId, String labelName, List<Integer> shopListLabelId) throws WNoResultException {

		// ラベル名の更新をする
		TShopListLabel shopListLabel = shopListLabelService.findById(id);
		shopListLabel.labelName = labelName;

		shopListLabelService.updateIncludesVersion(shopListLabel);

		// ラベル名に紐づく系列店舗を削除する
		shopListLabelGroupService.deleteByShopListLabelId(id);

		// ラベル名に紐づく系列店舗を更新する
		shopListLabelId.stream().forEach( n -> {
			TShopListLabelGroup shopListLabelGroup = new TShopListLabelGroup();
			shopListLabelGroup.shopListLabelId = id;
			shopListLabelGroup.shopListId = n;
			shopListLabelGroup.deleteFlg = DeleteFlgKbn.NOT_DELETED;
			shopListLabelGroupService.insert(shopListLabelGroup);
			shopListLabelGroup.displayOrder = shopListLabelGroup.id;
			shopListLabelGroupService.updateIncludesVersion(shopListLabelGroup);
		});

	}

	/**
	 * 系列店舗ラベルIDに紐づく系列店舗のラベルや店舗を削除する
	 * @param shopListLabelId
	 */
	public void deleteShopListLabel(List<Integer> shopListLabelId) {

		shopListLabelId.stream().forEach(id -> {
			TShopListLabel shopListLabel = new TShopListLabel();
			shopListLabel.id = id;
			TShopListLabelGroup tShopListLabelGroup = new TShopListLabelGroup();
			tShopListLabelGroup.shopListLabelId = id;

			shopListLabelService.deleteIgnoreVersion(shopListLabel);
			try {
				shopListLabelGroupService.deleteByShopListLabelId(id);
			} catch (WNoResultException e) {
			}
		});
	}

}
