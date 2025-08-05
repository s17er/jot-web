package com.gourmetcaree.admin.pc.preview.action.shopListPreview;

import static com.gourmetcaree.common.util.GourmetCareeUtil.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.preview.dto.detailPreview.RelationShopListDto;
import com.gourmetcaree.admin.pc.preview.form.shopListPreview.DetailForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.arbeitsys.constants.MArbeitConstants;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.exception.PageNotFoundException;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MAreaConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.MCustomer;
import com.gourmetcaree.db.common.entity.TShopList;
import com.gourmetcaree.db.common.entity.VShopListMaterialNoData;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.CustomerService;
import com.gourmetcaree.db.common.service.ShopListMaterialNoDataService;
import com.gourmetcaree.db.common.service.ShopListService;
import com.gourmetcaree.db.shopList.dto.shopList.RelationShopListRetDto;

public class DetailAction extends PcAdminAction {

	/** 店舗一覧プレビュー詳細アクションフォーム */
	@Resource
	@ActionForm
	private DetailForm detailForm;

	/** 店舗一覧サービス */
	@Resource
	private ShopListService shopListService;

	/** 店舗一覧画像サービス(画像データなし) */
	@Resource
	private ShopListMaterialNoDataService shopListMaterialNoDataService;

	@Resource
	private CustomerService customerService;


	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", urlPattern = "index/{customerId}/{areaCd}", input = TransitionConstants.Preview.JSP_APO03A01)
	public String index() {
		checkArgsNull(NO_BLANK_FLG_NG, detailForm.customerId);

		return show();
	}

	/**
	 * 店舗切り替え
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", urlPattern = "changeShop/{customerId}/{id}/{areaCd}", input = TransitionConstants.Preview.JSP_APO03A01)
	public String changeShop() {
		checkArgsNull(NO_BLANK_FLG_NG, detailForm.id, detailForm.customerId);
		return show();
	}

	/**
	 * ページ遷移用
	 * @return
	 */
	private String show() {
		try {
			createCustomerData();
			createShopListData();
		} catch (NumberFormatException e) {
			throw new FraudulentProcessException(e);
		}

		return TransitionConstants.Preview.JSP_APO03A01;
	}

	/**
	 * 顧客データを作成します。
	 */
	private void createCustomerData() {
		try {
			MCustomer entity = customerService.findById(Integer.parseInt(detailForm.customerId));
			detailForm.customerName = entity.customerName;

		} catch(NumberFormatException e) {
			throw new FraudulentProcessException(e);
		}
	}


	/**
	 * 店舗一覧の情報を作成します。
	 */
	private void createShopListData() {
		try {

			TShopList entity;

			if (detailForm.areaCd == null) {
				detailForm.areaCd  = MAreaConstants.AreaCd.SHUTOKEN_AREA;
			}
			detailForm.frontHttpUrl = getCommonProperty("gc.front.httpUrl.area" + detailForm.areaCd);
			if (GourmetCareeUtil.eqInt(detailForm.areaCd, MAreaConstants.AreaCd.SENDAI_AREA )) {
				detailForm.cssLocation = detailForm.getSendaiPath(DetailForm.SYUTOKEN_CSS);
				detailForm.imgLocation = detailForm.getSendaiPath(DetailForm.SYUTOKEN_IMG);
				detailForm.imagesLocation = detailForm.getSendaiPath(DetailForm.SYUTOKEN_IMAGES);
				detailForm.incLocation = detailForm.getSendaiPath(DetailForm.SYUTOKEN_INC);
				detailForm.helpLocation = detailForm.getSendaiPath(DetailForm.SYUTOKEN_HELP);
			}


			// フォームに店舗IDがない場合は初回なので、最初の店舗IDを入れる
			if (StringUtils.isBlank(detailForm.id)) {
				// 最初に並ぶ店舗エンティティ
				TShopList firstEntity = shopListService.getFirstShopListByCustomerId(Integer.parseInt(detailForm.customerId));
				entity = shopListService.getResisteredData(firstEntity.id);
				detailForm.id = String.valueOf(firstEntity.id);
			} else {
				entity = shopListService.getResisteredData(Integer.parseInt(detailForm.id));
			}

			Beans.copy(entity, detailForm).excludes("id", "industryKbn1", "industryKbn2", "holiday").execute();
			detailForm.shopHoliday = entity.holiday;
			detailForm.industryKbn1 = entity.industryKbn1;
			detailForm.shopListIndustryKbn1 = entity.industryKbn1;
			if (entity.industryKbn2 != null) {
				detailForm.industryKbn2 = entity.industryKbn2;
			}
			List<RelationShopListDto> dtoList = new ArrayList<RelationShopListDto>();
			try {
				List<RelationShopListRetDto> retDtoList = shopListService.getRelationShopListByCustomerId(entity.customerId);
				for (RelationShopListRetDto retDto : retDtoList) {
					RelationShopListDto dto = new RelationShopListDto();
					Beans.copy(retDto, dto).execute();
					dto.detailPath = GourmetCareeUtil.makePath("/shopListPreview/detail/", "changeShop", detailForm.customerId, String.valueOf(retDto.id), String.valueOf(entity.areaCd));
					dto.shopListImagePath = createShopListImagePath(retDto.id);
					dtoList.add(dto);
				}
				createShopListMaterialData(Integer.parseInt(detailForm.id));
			} catch (WNoResultException e) {
				// なにもしない
			}
			detailForm.relationShopList = dtoList;

			if (GourmetCareeUtil.eqInt(MTypeConstants.JobOfferFlg.ARI, entity.jobOfferFlg)) {
				detailForm.arbeitPreviewPath = String.format(
						getCommonProperty("gc.arbeitPreview.url.format"),
						MArbeitConstants.ArbeitSite.getArbeitSiteConst(entity.arbeitTodouhukenId),
						entity.id,
						entity.accessKey);

			}


		} catch (WNoResultException e) {
			throw new PageNotFoundException();
		}
	}


	/**
	 * 店舗一覧イメージパスの作成
	 * @param id
	 * @return
	 */
	private String createShopListImagePath(int id) {
		try {
			List<VShopListMaterialNoData> entityList = shopListMaterialNoDataService.getMaterialList(id);
			for (VShopListMaterialNoData entity : entityList) {
				if (GourmetCareeUtil.eqInt(Integer.parseInt(MTypeConstants.ShopListMaterialKbn.MAIN_1), entity.materialKbn)) {
					return GourmetCareeUtil.makePath("/util/imageUtility/",
														"displayShopListImageCache",
														String.valueOf(id),
														MTypeConstants.ShopListMaterialKbn.MAIN_1,
														GourmetCareeUtil.createUniqueKey(entity.insertDatetime));
				}
			}
		} catch (WNoResultException e) {
			return "";
		}
		return "";
	}

	/**
	 * 店舗一覧の画像データが存在するかどうかをMapに保持します。
	 */
	private void createShopListMaterialData(int shopListId) {
		Map<String, String> imageUniqueKeyMap = new HashMap<String, String>();
		Map<String, Boolean> imageCheckMap = new HashMap<String, Boolean>();
		try {
			List<VShopListMaterialNoData> retList = shopListMaterialNoDataService.getMaterialList(shopListId);

			for (VShopListMaterialNoData entity : retList) {
					imageCheckMap.put(Integer.toString(entity.materialKbn), true);
					imageUniqueKeyMap.put(Integer.toString(entity.materialKbn), createUniqueKey(entity.insertDatetime));
			}

		} catch (WNoResultException e) {
			//素材データがない場合は未処理とする。
		}

		detailForm.imageCheckMap = imageCheckMap;
		detailForm.uniqueKeyMap = imageUniqueKeyMap;
	}
}
