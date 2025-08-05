package com.gourmetcaree.admin.service.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.google.common.collect.Lists;
import com.gourmetcaree.common.logic.TypeLogic;
import com.gourmetcaree.common.logic.TypeMappingLogic;
import com.gourmetcaree.common.util.GcCollectionUtils;
import com.gourmetcaree.db.common.entity.MType;
import jp.co.whizz_tech.commons.WztStringUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.extension.jdbc.AutoSelect;
import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.beans.util.Beans;

import com.gourmetcaree.admin.service.accessor.preview.CheckStatusChangeAccessor;
import com.gourmetcaree.admin.service.dto.RailroadDto;
import com.gourmetcaree.common.dto.PreviewDto;
import com.gourmetcaree.common.dto.WebRouteDto;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants.SizeKbn;
import com.gourmetcaree.db.common.entity.MCustomer;
import com.gourmetcaree.db.common.entity.MVolume;
import com.gourmetcaree.db.common.entity.TWeb;
import com.gourmetcaree.db.common.entity.TWebIpPhone;
import com.gourmetcaree.db.common.entity.TWebRoute;
import com.gourmetcaree.db.common.entity.TWebSpecial;
import com.gourmetcaree.db.common.entity.VWebList;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.common.service.MaterialNoDataService;
import com.gourmetcaree.db.common.service.TypeService;
import com.gourmetcaree.db.common.service.WebAttributeService;
import com.gourmetcaree.db.common.service.WebIpPhoneService;
import com.gourmetcaree.db.common.service.WebListService;
import com.gourmetcaree.db.common.service.WebRouteService;
import com.gourmetcaree.db.common.service.WebService;
import com.gourmetcaree.db.webdata.dto.webdata.VWebListDto;

/**
 * プレビュー用ロジックです。
 * @author Takehiro Nakamori
 *
 */
public class PreviewLogic extends AbstractAdminLogic {

	@Resource
	private WebListService webListService;

	@Resource
	private WebAttributeService webAttributeService;

	@Resource
	private MaterialNoDataService materialNoDataService;

	@Resource
	private RailroadLogic railroadLogic;

	@Resource
	private WebRouteService webRouteService;

	@Resource
	private TypeService typeService;

	@Resource
	private WebIpPhoneService webIpPhoneService;

	@Resource
	private WebService webService;

	@Resource
    private WebdataLogic webdataLogic;



	/**
	 * webデータ検索画面からのプレビューDTOリストを作成します。
	 * @return
	 * @throws WNoResultException
	 */
	public List<PreviewDto> createWebDataSearchPreviewDtoList(VWebListDto dto, PageNavigateHelper pageNavi, int targetPage) throws WNoResultException {
		if (dto == null) {
			throw new IllegalArgumentException("PrevewWebDataSearchPropertyがnullです。");
		}

		dto.webIpPhoneIdList = webIpPhoneService.selectIdListFromIpPhoneNumber(dto.ipPhone);
		AutoSelect<VWebList> select =
				webListService.getWebdataListSelect(dto, pageNavi, targetPage);

		List<PreviewDto> dtoList = select.iterate(createWebDataSearchPreviewIterate());

		if (CollectionUtils.isEmpty(dtoList)) {
			throw new WNoResultException();
		}

		return dtoList;


	}


	/**
	 * web検索からのプレビューデータ作成イテレータを作成します。
	 * @return
	 */
	private IterationCallback<VWebList, List<PreviewDto>> createWebDataSearchPreviewIterate() {
		IterationCallback<VWebList, List<PreviewDto>> iterate = new IterationCallback<VWebList, List<PreviewDto>>() {
			private List<PreviewDto> retList = new ArrayList<PreviewDto>();
			@Override
			public List<PreviewDto> iterate(VWebList entity, IterationContext context) {
				if (entity == null) {
					return retList;
				}

				PreviewDto dto = new PreviewDto();
				Beans.copy(entity, dto).execute();
				dto.fromInputFlg = false;

				createWebDataAttribute(dto);
				createWebSpecialList(dto);

				createImageExistData(dto);



				createCustomerData(dto);

				createRouteData(dto);

				createIpPhoneData(dto);

				retList.add(dto);

				return retList;
			}
		};

		return iterate;
	}


	/**
	 * WEBデータの属性を作成します。
	 * @param dto
	 */
	private void createWebDataAttribute(PreviewDto dto) {
		// 待遇リスト
		dto.treatmentKbnList =  getWebDataList(
						dto.id,
						MTypeConstants.TreatmentKbn.TYPE_CD);

		// 雇用形態リスト
		dto.employPtnList = getWebDataList(
						dto.id,
						MTypeConstants.EmployPtnKbn.TYPE_CD);

		// その他条件リスト
		dto.otherConditionKbnList =
				getWebDataList(
						dto.id,
						MTypeConstants.OtherConditionKbn.TYPE_CD);

		// 勤務地エリア(WEBエリアから名称変更)リスト
		dto.webAreaKbnList =
                createWebAreaList(dto.id,
						dto.areaCd);

		// 海外エリアリスト
		dto.foreignAreaKbnList =
				getWebDataList(
						dto.id,
						MTypeConstants.ForeignAreaKbn.getTypeCd(dto.areaCd));

		// 勤務地詳細エリアリスト
		dto.detailAreaKbnList =
                getWebDataList(
                        dto.id,
                        MTypeConstants.DetailAreaKbn.getTypeCd(dto.areaCd));

		// 職種リスト
		dto.jobKbnList =
				getWebDataList(dto.id,
						MTypeConstants.JobKbn.TYPE_CD);

	}


	/**
	 * IP電話のデータを生成します。
	 */
	public void createIpPhoneData(PreviewDto dto) {
		final List<String> ipPhoneList = new ArrayList<String>();
		final List<String> customerTelList = new ArrayList<String>();
		webIpPhoneService.selectDataByWebIdAndCustomerId(
				dto.id,
				NumberUtils.toInt(dto.customerId),
				new IterationCallback<TWebIpPhone, Void>() {

					@Override
					public Void iterate(TWebIpPhone entity,
							IterationContext context) {

						if (entity != null && StringUtils.isNotEmpty(entity.ipTel)) {
							ipPhoneList.add(GourmetCareeUtil.devideIpPhoneNumber(entity.ipTel));
							customerTelList.add(entity.customerTel);
						}
						return null;
					}
		});

		dto.ipPhoneList = ipPhoneList;
		dto.customerTelList = customerTelList;
		dto.telList = GourmetCareeUtil.extractPhoneNumber(dto.phoneReceptionist);
	}


	private List<String> getWebDataList(int webId, String typeCd) {
		return typeService.getSortedTypeList(typeCd, webAttributeService.getWebAttributeValueIntegerList(webId, typeCd));
	}


	private List<String> createWebAreaList(Integer webId, int areaCd) {
        List<MType> areaList = webdataLogic.selectChildList(webId, MTypeConstants.WebAreaKbn.getTypeCd(areaCd));
        return GcCollectionUtils.typeToStringValueList(areaList);
    }



	/**
	 * webSpecialのリストを作成します
	 * @param dto
	 */
	private void createWebSpecialList(PreviewDto dto) {
		List<String> retList =
				jdbcManager.from(TWebSpecial.class)
					.where(new SimpleWhere()
					.eq(WztStringUtil.toCamelCase(TWebSpecial.WEB_ID), dto.id)
					.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
					.iterate(new IterationCallback<TWebSpecial, List<String>>() {
						private List<String> specialIdList = new ArrayList<String>();
						@Override
						public List<String> iterate(TWebSpecial entity, IterationContext context) {
							if (entity != null) {
								specialIdList.add(String.valueOf(entity.specialId));
							}
							return specialIdList;
						}
					});
		if (retList == null) {
			dto.specialIdList = new ArrayList<String>();
		}
		dto.specialIdList = retList;
	}

	/**
	 * 顧客データを作成します。
	 * @param dto
	 */
	public void createCustomerData(PreviewDto dto) {
		if (StringUtils.isBlank(dto.customerId)) {
			return;
		}

		if (GourmetCareeUtil.eqInt(
				MTypeConstants.CommunicationMailKbn.INPUT_MAIL, dto.communicationMailKbn)) {
			return;
		}
		MCustomer customer = jdbcManager.from(MCustomer.class)
									.where(new SimpleWhere()
									.eq(MCustomer.ID, NumberUtils.toInt(dto.customerId))
									.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
									.getSingleResult();

		if (customer != null) {
			dto.mail = customer.mainMail;
			if (GourmetCareeUtil.eqInt(
					MTypeConstants.SubmailReceptionFlg.RECEIVE, customer.submailReceptionFlg)) {
				dto.customerSubMailAddress = customer.subMail;
			}
		}
	}

	/**
	 * ルートデータを作成します。
	 * @param previewDto
	 */
	private void createRouteData(PreviewDto previewDto) {
		if (previewDto.fromInputFlg) {
			List<WebRouteDto> dtoList;


			if (CollectionUtils.isEmpty(previewDto.webRouteList)) {
				dtoList = new ArrayList<WebRouteDto>();
			} else {
				dtoList = previewDto.webRouteList;
			}


			if (StringUtils.isNotBlank(previewDto.stationId)) {
				WebRouteDto dto = new WebRouteDto();
				RailroadDto railNameDto;
				int stationId = NumberUtils.toInt(previewDto.stationId);
				try {
					railNameDto = railroadLogic.getRailroadDto(stationId);
					Beans.copy(railNameDto, dto).execute();
					dto.stationId = stationId;
					dto.railroadId = NumberUtils.toInt(previewDto.railroadId);
					dto.routeId = NumberUtils.toInt(previewDto.routeId);
					dtoList.add(dto);
				} catch (SNoResultException e) {
					// 何もしない。
				}
			}

			previewDto.webRouteList = dtoList;


			return;
		}
		AutoSelect<TWebRoute> select = webRouteService.findSelectByWebId(previewDto.id);

		List<WebRouteDto> retList =
		select.iterate(new IterationCallback<TWebRoute, List<WebRouteDto>>() {

			private List<WebRouteDto> dtoList = new ArrayList<WebRouteDto>();
			@Override
			public List<WebRouteDto> iterate(TWebRoute entity, IterationContext context) {
				if (entity == null) {
					return dtoList;
				}

				WebRouteDto dto = new WebRouteDto();
				// 名称の取得
				RailroadDto railNameDto;
				try {
					railNameDto= railroadLogic.getRailroadDto(entity.stationId);
				} catch (SNoResultException e) {
					return dtoList;
				}


				Beans.copy(railNameDto, dto).execute();

				// IDをセット
				dto.railroadId = entity.railroadId;
				dto.routeId = entity.routeId;
				dto.stationId = entity.stationId;

				dtoList.add(dto);
				return dtoList;
			}
		});


		if (CollectionUtils.isEmpty(retList)) {
			previewDto.webRouteList = new ArrayList<WebRouteDto>();
		} else {
			previewDto.webRouteList = retList;
		}
	}

	/**
	 * ボリュームデータの作成
	 * @param dto
	 */
	public void createVolumeData(PreviewDto dto) {
		if (StringUtils.isBlank(dto.volumeId)) {
			return;
		}

		MVolume volume = jdbcManager.from(MVolume.class)
									.where(new SimpleWhere()
									.eq(MVolume.ID, NumberUtils.toInt(dto.volumeId))
									.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
									.getSingleResult();

		if (volume == null) {
			return;
		}
		dto.postStartDatetime = volume.postStartDatetime;
		dto.postEndDatetime = volume.postEndDatetime;
	}


	/**
	 * WEBIDからプレビューデータを作成します。
	 * @param webId
	 * @return
	 * @throws WNoResultException
	 */
	public PreviewDto createPreviewDtoByWebId(int webId) throws WNoResultException {
		PreviewDto dto = new PreviewDto();
		TWeb web = createPreviewWeb(webId);
		Beans.copy(web, dto).execute();
		dto.fromInputFlg = false;
		createCustomerData(dto);
		createVolumeData(dto);
		createWebDataAttribute(dto);
		createWebSpecialList(dto);
		createRouteData(dto);
		createImageExistData(dto);
		createIpPhoneData(dto);
		return dto;
	}


	/**
	 * プレビュー用WEBデータ取得
	 * @param webId
	 * @return
	 * @throws WNoResultException
	 */
	private TWeb createPreviewWeb(int webId) throws WNoResultException {
		TWeb entity = jdbcManager.from(TWeb.class)
						.where(new SimpleWhere()
						.eq(TWeb.ID, webId)
						.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
						.getSingleResult();

		if (entity == null) {
			throw new WNoResultException();
		}
		return entity;
	}


	/**
	 * 画像の存在データを作成します。
	 * @param dto
	 */
	private void createImageExistData(PreviewDto dto) {
		if (dto.sizeKbn == SizeKbn.D || dto.sizeKbn == SizeKbn.E) {
			dto.materialSearchListExistFlg = materialNoDataService.isMaterialEntityExist(dto.id, NumberUtils.toInt(MTypeConstants.MaterialKbn.FREE));
		} else if (dto.sizeKbn == SizeKbn.A || dto.sizeKbn == SizeKbn.B || dto.sizeKbn == SizeKbn.C) {
			dto.materialSearchListExistFlg = materialNoDataService.isMaterialEntityExist(dto.id, NumberUtils.toInt(MTypeConstants.MaterialKbn.MAIN_1));
		} else {
			dto.materialSearchListExistFlg = false;
		}

		if (GourmetCareeUtil.eqInt(MTypeConstants.SizeKbn.C, dto.sizeKbn)) {
			//サイズDのみ複数画像を表示するので存在をチェック
			dto.materialCaptionAExistFlg = materialNoDataService.isMaterialEntityExist(dto.id, NumberUtils.toInt(MTypeConstants.MaterialKbn.PHOTO_A));
			dto.materialCaptionBExistFlg = materialNoDataService.isMaterialEntityExist(dto.id, NumberUtils.toInt(MTypeConstants.MaterialKbn.PHOTO_B));
		}
	}

	/**
	 * 画像存在マップの作成
	 * @param dto
	 */
	public void createImageCheckMap(PreviewDto dto) {
		Map<String, Boolean> imageCheckMap = new HashMap<String, Boolean>();
		putImageExistFlgToMap(dto.id, MTypeConstants.MaterialKbn.MAIN_1, imageCheckMap);
		putImageExistFlgToMap(dto.id, MTypeConstants.MaterialKbn.MAIN_2, imageCheckMap);
		putImageExistFlgToMap(dto.id, MTypeConstants.MaterialKbn.MAIN_3, imageCheckMap);
		putImageExistFlgToMap(dto.id, MTypeConstants.MaterialKbn.ATTENTION_HERE, imageCheckMap);
		putImageExistFlgToMap(dto.id, MTypeConstants.MaterialKbn.LOGO, imageCheckMap);
		putImageExistFlgToMap(dto.id, MTypeConstants.MaterialKbn.PHOTO_A, imageCheckMap);
		putImageExistFlgToMap(dto.id, MTypeConstants.MaterialKbn.PHOTO_B, imageCheckMap);
		putImageExistFlgToMap(dto.id, MTypeConstants.MaterialKbn.PHOTO_C, imageCheckMap);
		putImageExistFlgToMap(dto.id, MTypeConstants.MaterialKbn.RIGHT, imageCheckMap);

		dto.imageCheckMap = imageCheckMap;
	}

	/**
	 * 画像存在データをマップにプッとします
	 * @param webId
	 * @param materialKbn
	 * @param imageCheckMap
	 */
	private void putImageExistFlgToMap(int webId, String materialKbn, Map<String, Boolean> imageCheckMap) {
		imageCheckMap.put(
				materialKbn,
				materialNoDataService
				.isMaterialEntityExist(
						webId, NumberUtils.toInt(materialKbn)));
	}



	/**
	 * WEBデータのチェックステータスが変更可能かどうか
	 * @param id　WEBID
	 */
	public boolean isWebdataCheckStatusChangeable(String id) {
		if (NumberUtils.isDigits(id)) {
			return isWebdataCheckStatusChangeable(Integer.parseInt(id));
		}
		return false;
	}

	/**
	 * WEBデータのチェックステータスが変更可能かどうか
	 * @param id　WEBID
	 */
	public boolean isWebdataCheckStatusChangeable(Integer id) {
		if (!ManageAuthLevel.isAdmin(getAuthLevel())
				|| id == null) {
			return false;
		}
		return webListService.isCheckStatusChangeable(id);
	}
	/**
	 * WEBデータのチェック状態の変更ができるかどうか
	 */
	public void convertWebdataCheckStatusChangeable(CheckStatusChangeAccessor accessor) {
		accessor.setChangeableCheckStatus(isWebdataCheckStatusChangeable(accessor.getId()));
	}



	/**
	 * WEBデータのチェック状態を変更します。
	 */
	public void changeCheckStatus(CheckStatusChangeAccessor accessor) {
		if (!isWebdataCheckStatusChangeable(accessor.getId())) {
			throw new FraudulentProcessException("チェックステータスの変更ができない権限/WEBデータです。");
		}

		TWeb web = webService.findById(NumberUtils.toInt(accessor.getId()));
		TWeb updateEntity = Beans.createAndCopy(TWeb.class, web)
								.includes("id", "version")
								.execute();

		updateEntity.checkedStatus = MTypeConstants.WebdataCheckedStatus.CHECKED;
		webService.updateIncludesVersion(updateEntity);

		logicLog.info(String.format("WEBデータをチェック済みに変更しました。 ID:[%d]", web.id));

	}


}
