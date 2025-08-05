package com.gourmetcaree.shop.pc.preview.action.listPreview;

import static com.gourmetcaree.common.util.GourmetCareeUtil.*;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.xml.bind.ValidationEvent;

import com.google.common.collect.Lists;
import com.gourmetcaree.common.logic.TypeLogic;
import com.gourmetcaree.common.logic.TypeMappingLogic;
import com.gourmetcaree.db.common.entity.MType;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.common.dto.PreviewDto;
import com.gourmetcaree.common.dto.WebRouteDto;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.logic.ValueToNameConvertLogic;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MAreaConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants.SizeKbn;
import com.gourmetcaree.db.common.constants.MTypeConstants.TerminalKbn;
import com.gourmetcaree.db.common.entity.MCustomer;
import com.gourmetcaree.db.common.entity.MVolume;
import com.gourmetcaree.db.common.entity.TWeb;
import com.gourmetcaree.db.common.entity.TWebIpPhone;
import com.gourmetcaree.db.common.entity.TWebRoute;
import com.gourmetcaree.db.common.service.CustomerService;
import com.gourmetcaree.db.common.service.MaterialService;
import com.gourmetcaree.db.common.service.TypeService;
import com.gourmetcaree.db.common.service.VolumeService;
import com.gourmetcaree.db.common.service.WebAttributeService;
import com.gourmetcaree.db.common.service.WebIpPhoneService;
import com.gourmetcaree.db.common.service.WebRouteService;
import com.gourmetcaree.db.common.service.WebService;
import com.gourmetcaree.db.common.service.WebSpecialService;
import com.gourmetcaree.shop.logic.dto.RailroadDto;
import com.gourmetcaree.shop.logic.logic.RailroadLogic;
import com.gourmetcaree.shop.pc.preview.action.detailPreview.DraftDetailAction;
import com.gourmetcaree.shop.pc.preview.form.listPreview.ListForm;
import com.gourmetcaree.shop.pc.preview.form.listPreview.ListForm.PreviewMethodKbn;
import com.gourmetcaree.shop.pc.sys.action.PcShopAction;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;

/**
 * メールからのプレビューを表示するクラス
 * ログインしていない状態でも使用できる。
 * @author Takahiro Ando
 * @version 1.0
 */
public class DraftListAction extends PcShopAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(DraftDetailAction.class);

	/** ログオブジェクト（なりすまし） */
	private static final Logger sysMasqueradeLog = Logger.getLogger("sysMasqueradeLog");

	/** フォーム */
	@ActionForm
	@Resource
	protected ListForm listForm;

	/** Webdataサービス */
	@Resource
	private WebService webService;

	/** Webデータ属性サービス */
	@Resource
	private WebAttributeService webAttributeService;

	/** 素材サービス */
	@Resource
	private MaterialService materialService;

	/** 名称変換ロジック */
	@Resource
	private ValueToNameConvertLogic valueToNameConvertLogic;

	/** 号数サービス */
	@Resource
	private VolumeService volumeService;

	/** 顧客サービス */
	@Resource
	private CustomerService customerService;

	/** WEB路線サービス */
	@Resource
	private WebRouteService webRouteService;

	/** 鉄道ロジック */
	@Resource
	private RailroadLogic railroadLogic;

	@Resource
	private WebSpecialService webSpecialService;

	@Resource
	private TypeService typeService;

	/** IP電話サービス */
	@Resource
	private WebIpPhoneService webIpPhoneService;

	@Resource
    private TypeMappingLogic typeMappingLogic;

	@Resource
    private TypeLogic typeLogic;

//	/** メールからのプレビュー時の閲覧可能年数 */
//	private static final int DRAFT_PREVIEW_MINUS_MONTH = -1;

	/**
	 * リストでのPCプレビューを表示します。（エリア=首都圏）
	 * @return
	 */
	@Execute(validator = false, urlPattern = "showPcKPreview/{webId}/{accessCd}/{areaCd}", input = TransitionConstants.ErrorTransition.JSP_PREVIEW_ERROR)
	public String showPcKPreview() {

		checkArgsNull(NO_BLANK_FLG_NG, listForm.webId, listForm.accessCd);
		listForm.previewMethodKbn = PreviewMethodKbn.DRAFT_PREVIEW;

		createData(TerminalKbn.PC_VALUE);

		return TransitionConstants.Preview.JSP_APO02A01;
	}


	/**
	 * リストでのモバイルプレビューを表示します。（首都圏）
	 * @return
	 */
	@Execute(validator = false, urlPattern = "showMobileKPreview/{webId}/{accessCd}/{areaCd}", input = TransitionConstants.ErrorTransition.JSP_PREVIEW_ERROR)
	public String showMobileKPreview() {

		checkArgsNull(NO_BLANK_FLG_NG, listForm.webId, listForm.accessCd);
		listForm.previewMethodKbn = PreviewMethodKbn.DRAFT_PREVIEW;
		listForm.SITE_COLOR = getCommonProperty("gc.siteColor" + listForm.areaCd);
		listForm.SITE_TEXT_COLOR = getCommonProperty("gc.siteTextColor" + listForm.areaCd);
		listForm.SITE_EMOJI = getCommonProperty("gc.siteEmoji" + listForm.areaCd);
		listForm.SITE_TEXT_LINK_COLOR = getCommonProperty("gc.siteTextLinkColor" + listForm.areaCd);

		createData(TerminalKbn.MOBILE_VALUE);

		return TransitionConstants.Preview.JSP_FMB01L01;
	}

	/**
	 * リストでのモバイルプレビューを表示します。（首都圏）
	 * @return
	 */
	@Execute(validator = false, urlPattern = "showSmartKPreview/{webId}/{accessCd}/{areaCd}", input = TransitionConstants.ErrorTransition.JSP_PREVIEW_ERROR)
	public String showSmartKPreview() {
		checkArgsNull(NO_BLANK_FLG_NG, listForm.webId, listForm.accessCd);
		listForm.previewMethodKbn = PreviewMethodKbn.DRAFT_PREVIEW;
		listForm.SITE_COLOR = getCommonProperty("gc.siteColor" + listForm.areaCd);
		listForm.SITE_TEXT_COLOR = getCommonProperty("gc.siteTextColor" + listForm.areaCd);
		listForm.SITE_EMOJI = getCommonProperty("gc.siteEmoji" + listForm.areaCd);
		listForm.SITE_TEXT_LINK_COLOR = getCommonProperty("gc.siteTextLinkColor" + listForm.areaCd);

		createData(TerminalKbn.SMART_VALUE);

		return TransitionConstants.Preview.JSP_FSB01L01;
	}


	/**
	 * 検索のAction側メインロジック
	 */
	private void createData(int terminalKbn) {

		try {
			TWeb entityTWeb = webService.findById(Integer.parseInt(listForm.webId));

			checkDraftAccess(entityTWeb);

			PreviewDto dispDto = new PreviewDto();
			Beans.copy(entityTWeb, dispDto).execute();

			createWebAttribute(entityTWeb, dispDto);
			createIpPhoneData(entityTWeb, dispDto);
			dispDto.specialIdList = webSpecialService.getSpecialIdListByWebId(entityTWeb.id);


			try {
				if (entityTWeb.volumeId != null) {
					MVolume volume = volumeService.findById(entityTWeb.volumeId);
					dispDto.postStartDatetime = volume.postStartDatetime;
					dispDto.postEndDatetime = volume.postEndDatetime;
				}
			} catch (SNoResultException e) {
				// 何もしない
			}

			if (entityTWeb.customerId != null
					&& GourmetCareeUtil.eqInt(MTypeConstants.CommunicationMailKbn.CUSTOMER_MAIL, entityTWeb.communicationMailKbn)) {
				try {
					MCustomer customer = customerService.findById(entityTWeb.customerId);
					dispDto.mail = customer.mainMail;
					if (GourmetCareeUtil.eqInt(MTypeConstants.SubmailReceptionFlg.RECEIVE,
							customer.submailReceptionFlg)) {
						dispDto.customerSubMailAddress = customer.subMail;
					}
				} catch (SNoResultException e) {
					// 何もしない
				}
			}

			if (SizeKbn.D == entityTWeb.sizeKbn || SizeKbn.E == entityTWeb.sizeKbn) {
				String materialKbn;
				if (terminalKbn == TerminalKbn.SMART_VALUE) {
					materialKbn = MTypeConstants.MaterialKbn.FREE;
				} else {
					materialKbn = MTypeConstants.MaterialKbn.FREE;
				}
				dispDto.materialSearchListExistFlg = materialService.isMaterialEntityExist(entityTWeb.id, NumberUtils.toInt(materialKbn));
			} else if (SizeKbn.A == entityTWeb.sizeKbn || SizeKbn.B == entityTWeb.sizeKbn || SizeKbn.C == entityTWeb.sizeKbn) {
				dispDto.materialSearchListExistFlg = materialService.isMaterialEntityExist(entityTWeb.id, NumberUtils.toInt(MTypeConstants.MaterialKbn.MAIN_1));
			} else {
				dispDto.materialSearchListExistFlg = false;
			}

			convertRouteData(dispDto);


			if (GourmetCareeUtil.eqInt(MTypeConstants.SizeKbn.C, dispDto.sizeKbn)) {
				//サイズDのみ複数画像を表示するので存在をチェック
				dispDto.materialCaptionAExistFlg = materialService.isMaterialEntityExist(entityTWeb.id, NumberUtils.toInt(MTypeConstants.MaterialKbn.PHOTO_A));
				dispDto.materialCaptionBExistFlg = materialService.isMaterialEntityExist(entityTWeb.id, NumberUtils.toInt(MTypeConstants.MaterialKbn.PHOTO_B));
			}

			dispDto.recruitmentIndustry = valueToNameConvertLogic.convertToIndustryName(" / ",GourmetCareeUtil.toIntToStringArray(dispDto.getIndustryKbnList()));

			List<PreviewDto> dataList = new ArrayList<PreviewDto>();
			dataList.add(dispDto);
			listForm.dataList = dataList;
			listForm.frontHttpUrl = getCommonProperty("gc.front.httpUrl.area" + listForm.areaCd);

			if (GourmetCareeUtil.eqInt(listForm.areaCd, MAreaConstants.AreaCd.SENDAI_AREA)) {
				listForm.cssLocation = listForm.getSendaiPath(ListForm.SYUTOKEN_CSS);
				listForm.imgLocation = listForm.getSendaiPath(ListForm.SYUTOKEN_IMG);
				listForm.imagesLocation = listForm.getSendaiPath(ListForm.SYUTOKEN_IMAGES);
				listForm.incLocation = listForm.getSendaiPath(ListForm.SYUTOKEN_INC);
				listForm.helpLocation = listForm.getSendaiPath(ListForm.SYUTOKEN_HELP);
			}

		} catch (SNoResultException e) {
			throw new ActionMessagesException("errors.app.dataNotFound");
		} catch (NumberFormatException e1) {
			throw new FraudulentProcessException();
		}
	}


	/**
	 * IP電話データの作成
	 */
	private void createIpPhoneData(TWeb entityTWeb, final PreviewDto dispDto) {

		dispDto.ipPhoneList = new ArrayList<String>();
		dispDto.telList = new ArrayList<String>();

		webIpPhoneService.selectDataByWebIdAndCustomerId(entityTWeb.id, entityTWeb.customerId, new IterationCallback<TWebIpPhone, Void>() {
			public Void iterate(TWebIpPhone entity, IterationContext context) {
				if (entity == null || StringUtils.isBlank(entity.ipTel)) {
					return null;
				}


				dispDto.ipPhoneList.add(GourmetCareeUtil.devideIpPhoneNumber(entity.ipTel));
				dispDto.telList.add(entity.customerTel);

				return null;
			}

		});
	}



	private void createWebAttribute(TWeb entityTWeb, PreviewDto dispDto) {
		//周辺テーブルのデータを取得
		dispDto.treatmentKbnList =
				createSortedWebAttribute(entityTWeb.id, MTypeConstants.TreatmentKbn.TYPE_CD);

		dispDto.employPtnList =
				createSortedWebAttribute(entityTWeb.id, MTypeConstants.EmployPtnKbn.TYPE_CD);
		dispDto.otherConditionKbnList = createSortedWebAttribute(entityTWeb.id, MTypeConstants.OtherConditionKbn.TYPE_CD);

		dispDto.webAreaKbnList = createSortedWebAttributeWithoutChildValues(entityTWeb.id, MTypeConstants.WebAreaKbn.getTypeCd(entityTWeb.areaCd));
		dispDto.detailAreaKbnList = createDetailAreaList(entityTWeb.id, entityTWeb.areaCd);
		dispDto.jobKbnList = createSortedWebAttribute(entityTWeb.id, MTypeConstants.JobKbn.TYPE_CD);
		dispDto.foreignAreaKbnList = createSortedWebAttribute(entityTWeb.id, MTypeConstants.ForeignAreaKbn.getTypeCd(entityTWeb.areaCd));
	}

	private List<String> createSortedWebAttribute(Integer id, String typeCd) {
		return typeService.getSortedTypeList(typeCd, webAttributeService.getWebAttributeValueIntegerList(id, typeCd));
	}


    /**
     * ソートされた属性値リスト(String)を生成
     * m_type_mappingに登録されている子の値は抜く。
     * @param webId WebId
     * @param typeCd タイプコード
     * @return  子の値を抜いた属性値リスト(String)
     */
    private List<String> createSortedWebAttributeWithoutChildValues(Integer webId, String typeCd) {
        List<Integer> values = webAttributeService.getWebAttributeValueIntegerList(webId, typeCd);
        typeMappingLogic.removeChildValues(typeCd, values);
        return typeService.getSortedTypeList(typeCd, values);
    }

    /**
     * 詳細エリアリストの生成
     * @param webId webID
     * @param areaCd エリアコード
     * @return 詳細エリアのタイプ値リスト
     */
    private List<String> createDetailAreaList(Integer webId, int areaCd) {
        List<MType> detailList = typeLogic.selectDetailAreaList(webId, areaCd);
        List<String> values = Lists.newArrayList();
        for (MType type : detailList) {
            values.add(String.valueOf(type.typeValue));
        }
        return values;
    }

	/**
	 * 路線データをコンバートします。
	 * @param previewDto
	 */
	private void convertRouteData(PreviewDto previewDto) {
		List<TWebRoute> entityList = webRouteService.findByWebId(previewDto.id);

		previewDto.webRouteList = new ArrayList<WebRouteDto>();
		for (TWebRoute entity : entityList) {
			WebRouteDto dto = new WebRouteDto();

			RailroadDto railNameDto;
			try {
				// 名称の取得
				railNameDto = railroadLogic.getRailroadDto(entity.stationId);
			} catch (SNoResultException e) {
				continue;
			}

			Beans.copy(railNameDto, dto).execute();

			// IDをセット
			dto.railroadId = entity.railroadId;
			dto.routeId = entity.routeId;
			dto.stationId = entity.stationId;

			previewDto.webRouteList.add(dto);
		}
	}


	/**
	 * メールからのプレビュー時のアクセス権限のチェック
	 * @param entityTWeb
	 */
	private void checkDraftAccess(TWeb entityTWeb) {

		//アクセスコードチェック
		if (!listForm.accessCd.equals(entityTWeb.accessCd)) {
			log.warn("メールからのプレビュー時にアクセスコードが一致しませんでした。 id : " + entityTWeb.id + " アクセスコード：" + entityTWeb.accessCd);
			if (userDto.isMasqueradeFlg()) {
				sysMasqueradeLog.warn(String.format("メールからのプレビュー時にアクセスコードが一致しませんでした。営業ID：%s, 顧客ID：%s", userDto.masqueradeUserId, userDto.customerId));
				sysMasqueradeLog.warn("メールからのプレビュー時にアクセスコードが一致しませんでした。 id : " + entityTWeb.id + " アクセスコード：" + entityTWeb.accessCd + " 営業ID：" + userDto.masqueradeUserId);
			}
			listForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.noPermissionToShowPreview");
		}

		//エリアはアクセスされたメソッドで見分けて判定
		if (!eqInt(listForm.areaCd, entityTWeb.areaCd)) {
			log.warn("メールからのプレビュー時にエリアが一致しませんでした。 id : " + entityTWeb.id + " エリアコード：" + entityTWeb.areaCd);
			if (userDto.isMasqueradeFlg()) {
				sysMasqueradeLog.warn(String.format("メールからのプレビュー時にアクセスコードが一致しませんでした。営業ID：%s, 顧客ID：%s", userDto.masqueradeUserId, userDto.customerId));
				sysMasqueradeLog.warn("メールからのプレビュー時にアクセスコードが一致しませんでした。 id : " + entityTWeb.id + " エリアコード：" + entityTWeb.areaCd + " 営業ID：" + " 営業ID：" + userDto.masqueradeUserId);
			}
			listForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.noPermissionToShowPreview");
		}


	}

}