package com.gourmetcaree.admin.pc.shopList.action.shopList;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.util.Base64Util;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.MessageResourcesUtil;
import org.seasar.struts.util.ResponseUtil;

import com.gourmetcaree.admin.pc.shopList.dto.shopList.ShopListRailroadDto;
import com.gourmetcaree.admin.pc.shopList.form.shopList.ShopListBaseForm;
import com.gourmetcaree.admin.pc.shopList.form.shopList.ShopListBaseForm.DisplayConditionDto;
import com.gourmetcaree.admin.pc.shopList.form.shopList.ShopListBaseForm.RefelerKbn;
import com.gourmetcaree.admin.pc.shopList.form.shopList.ShopListBaseForm.StationDto;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.action.util.CustomerImageAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.dto.ShopListMaterialDto;
import com.gourmetcaree.common.dto.TagListDto;
import com.gourmetcaree.common.dto.customerImage.ImageDto;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.exception.ImageWriteErrorException;
import com.gourmetcaree.common.logic.ShopListLogic;
import com.gourmetcaree.common.logic.TagListLogic;
import com.gourmetcaree.common.logic.ValueToNameConvertLogic;
import com.gourmetcaree.common.property.ShopListProperty;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.common.util.WebdataFileUtils;
import com.gourmetcaree.common.util.WebdataFileUtils.ContentType;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants.ShopListMaterialKbn;
import com.gourmetcaree.db.common.entity.MShopListTag;
import com.gourmetcaree.db.common.entity.MShopListTagMapping;
import com.gourmetcaree.db.common.entity.MType;
import com.gourmetcaree.db.common.entity.TCustomerImage;
import com.gourmetcaree.db.common.entity.TShopChangeJobCondition;
import com.gourmetcaree.db.common.entity.TShopListAttribute;
import com.gourmetcaree.db.common.entity.TShopListLine;
import com.gourmetcaree.db.common.entity.TShopListMaterial;
import com.gourmetcaree.db.common.entity.TShopListRoute;
import com.gourmetcaree.db.common.entity.VCustomerImageNoData;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.CustomerImageNoDataService;
import com.gourmetcaree.db.common.service.CustomerImageService;
import com.gourmetcaree.db.common.service.ShopChangeJobConditionService;
import com.gourmetcaree.db.common.service.ShopListAttributeService;
import com.gourmetcaree.db.common.service.ShopListLineService;
import com.gourmetcaree.db.common.service.ShopListMaterialService;
import com.gourmetcaree.db.common.service.ShopListRouteService;
import com.gourmetcaree.db.common.service.ShopListService;
import com.gourmetcaree.db.common.service.ShopListTagMappingService;
import com.gourmetcaree.db.common.service.TypeService;
import com.gourmetcaree.db.webdata.dto.webdata.IdSelectDto;

import net.arnx.jsonic.JSON;

/**
 * 店舗一覧用ベースアクション
 * @author Takehiro Nakamori
 *
 */
public abstract class ShopListBaseAction extends PcAdminAction {

	/** ログ */
	private static final Logger shopListBaseLog = Logger.getLogger(ShopListBaseAction.class);

	/** 画像のターゲットアンカー */
	public static final String TARGET_ANCHOR_IMAGE = "ANCHOR_IMAGE";

	/** ロゴのターゲットアンカー */
	public static final String TARGET_ANCHOR_LOGO = "ANCHOR_LOGO";

	/** 画像のデフォルト最大サイズ(2MB) */
	private static final int DEFAULT_IMAGE_MAX_SIZE = 1024 * 1024 * 2;

	/** タイプサービス */
	@Resource
	protected TypeService typeService;

	/** 店舗一覧サービス */
	@Resource
	protected ShopListService shopListService;

	/** IDリストセッションキー */
	protected static final String SHOP_LIST_TEMP_ID_LIST_SESSION_KEY = "ADMIN_SHOPLIST_TEMP_ID_LIST";

	protected static final String SHOP_LIST_JOB_TEMP_LIST_SESSION_KEY = "ADMIN_SHOPLIS_JOB_TEMP_LIST";

	/** 名称変換ロジック */
	@Resource
	protected ValueToNameConvertLogic valueToNameConvertLogic;

	/** 素材サービス */
	@Resource
	protected ShopListMaterialService shopListMaterialService;

	/** 店舗一覧ロジック */
	@Resource
	protected ShopListLogic shopListLogic;

	/** 店舗一覧属性サービス */
	@Resource
	protected ShopListAttributeService shopListAttributeService;

	/** 店舗一覧路線サービス */
	@Deprecated
	@Resource
	protected ShopListRouteService shopListRouteService;

	/** リニューアル後の店舗路線サービス */
	@Resource
	protected ShopListLineService shopListLineService;

	/** ターゲットアンカー */
	private String targetAnchor;

	/** タグのサービス（フリーワード用） */
	@Resource
	protected TagListLogic tagListLogic;

	/** 店舗一覧とタグサービスのマッピング */
	@Resource
	protected ShopListTagMappingService shopListTagMappingService;

	@Resource
	protected CustomerImageNoDataService customerImageNoDataService;

	@Resource
	protected CustomerImageService customerImageService;

	@Resource
	protected ShopChangeJobConditionService shopChangeJobConditionService;


	/**
	 * インデックスへのパスを作成
	 * @param customerId
	 * @return
	 */
	protected String createReindexPath(String customerId) {
		return GourmetCareeUtil.makePath("/shopList", "/reindex", customerId, TransitionConstants.REDIRECT_STR);
	}

	/**
	 * インデックスへのパスを作成
	 * @param customerId
	 * @return
	 */
	protected String createListIndexPath(String customerId) {
		return GourmetCareeUtil.makePath("/shopList/list", "/index", customerId, TransitionConstants.REDIRECT_STR);
	}

	/**
	 * セッションからオブジェクトを取得
	 * @param sessionKey
	 * @return
	 */
	protected Object getObjectFromSession(String sessionKey) {
		return session.getAttribute(sessionKey);
	}

	/**
	 * セッションにオブジェクトをセット
	 * @param sessionKey
	 * @param obj
	 */
	protected void setObjectToSession(String sessionKey, Object obj) {
		session.setAttribute(sessionKey, obj);
	}

	/**
	 * セッションからオブジェクトを除去
	 * @param sessionKey
	 */
	protected void removeObjectFromSession(String sessionKey) {
		session.removeAttribute(sessionKey);
	}

	/**
	 * 電話番号フォーマットの作成
	 * @param phoneNo1
	 * @param phoneNo2
	 * @param phoneNo3
	 * @return
	 */
	protected String createPhoneFormat(String phoneNo1, String phoneNo2, String phoneNo3) {
		if (StringUtils.isNotBlank(phoneNo1)
				&& StringUtils.isNotBlank(phoneNo2)
				&& StringUtils.isNotBlank(phoneNo3)) {
			return new StringBuilder("")
						.append(phoneNo1)
						.append(GourmetCareeConstants.HYPHEN_MINUS_STR)
						.append(phoneNo2)
						.append(GourmetCareeConstants.HYPHEN_MINUS_STR)
						.append(phoneNo3)
						.toString();
		}

		return "";
	}

	/**
	 * FormFileが空かどうか判定します。
	 */
	protected boolean isEmptyFormFile(FormFile formFile) {
		if (formFile == null || formFile.getFileSize() <= 0) {
			return true;
		}

		return false;
	}

	/**
	 * ファイルサイズをチェックします。
	 * @param formFile ファイルサイズ
	 */
	private void checkFileSize(FormFile formFile) {
		if (isEmptyFormFile(formFile)) {
			return;
		}
		int maxSize = NumberUtils.toInt(getCommonProperty("gc.shopList.image.maxSize"), DEFAULT_IMAGE_MAX_SIZE);

		if (formFile.getFileSize() > maxSize) {
			throw new ActionMessagesException("errors.upload.size",
					MessageResourcesUtil.getMessage("msg.size.2MB"));
		}
	}

	/**
	 * DB登録までに使用する画像フォルダを削除する。
	 */
	protected void initUploadMaterial(ShopListBaseForm form) {
		String dirPath = getCommonProperty("gc.shopList.imgUpload.dir.session");
		StringBuilder dirName = new StringBuilder("");
		dirName.append(form.getIdForDirName());
		dirName.append("_");
		dirName.append(session.getId());

		WebdataFileUtils.deleteWebdataDir(dirPath, dirName.toString());
	}

	/**
	 * 素材アップロードのメイン処理
	 */
	protected String doUploadMaterial(ShopListBaseForm form) {

		FormFile formFile = form.imgFile;

		//JPEG以外の場合はエラー
		if ((formFile != null && formFile.getFileSize() > 0) &&
			!GourmetCareeConstants.MEDIA_CONTENT_TYPE_JPEG.equals(formFile.getContentType()) &&
			!GourmetCareeConstants.MEDIA_CONTENT_TYPE_PJPEG.equals(formFile.getContentType())) {

			// カーソルをリセット
			form.cursorPosition = "";
			throw new ActionMessagesException("errors.app.imageNotJpeg");
		}

		checkFileSize(formFile);

		String dirPath = getCommonProperty("gc.shopList.imgUpload.dir.session");
		StringBuilder dirName = new StringBuilder("");
		dirName.append(form.getIdForDirName());
		dirName.append("_");
		dirName.append(session.getId());

		if (formFile != null && formFile.getFileSize() > 0) {

			ShopListMaterialDto dto = new ShopListMaterialDto();
			// 素材区分をセット
			dto.materialKbn = form.hiddenMaterialKbn;
			// ファイル名をセット
			dto.fileName = formFile.getFileName();
			// コンテントタイプをセット
			// コンテントタイプがpjpegの場合は、jpegで登録
			if (GourmetCareeConstants.MEDIA_CONTENT_TYPE_PJPEG.equals(formFile.getContentType())) {
				dto.contentType = GourmetCareeConstants.MEDIA_CONTENT_TYPE_JPEG;
			} else  {
				dto.contentType = formFile.getContentType();
			}

			// Mapの初期処理を行う
			form.initMaterialMap();
			// 素材をマップにセット
			form.materialMap.put(form.hiddenMaterialKbn, dto);

			String fileName = WebdataFileUtils.createFileName(form.hiddenMaterialKbn, ContentType.toEnum(formFile.getContentType()));

			try {
				WebdataFileUtils.createWebdataFile(dirPath, dirName.toString(), fileName, formFile.getFileData());
			} catch (ImageWriteErrorException e) {
				shopListBaseLog.warn(e);
				return MessageResourcesUtil.getMessage("errors.app.upload");
			} catch (FileNotFoundException e) {
				shopListBaseLog.warn(e);
				return MessageResourcesUtil.getMessage("errors.app.upload");
			} catch (IOException e) {
				shopListBaseLog.warn(e);
				return MessageResourcesUtil.getMessage("errors.app.upload");
			}
		}

		setTargetAnchor(form.hiddenMaterialKbn);

		// FormFileを削除
		form.deleteFormFile(form.hiddenMaterialKbn);

		//hiddenをリセット
		form.hiddenMaterialKbn = null;

		shopListBaseLog.info("一時画像をアップしました。");
		return null;
	}

	/**
	 * 画像削除のメインロジック
	 */
	protected void doDeleteMaterial(ShopListBaseForm form) {

		// フォームをリセット
		form.deleteMaterial(form.hiddenMaterialKbn);
		setTargetAnchor(form.hiddenMaterialKbn);

		//hiddenをリセット
		form.hiddenMaterialKbn = null;
		shopListBaseLog.info("一時画像を削除しました。");
	}

	/**
	 * 素材をプロパティにセット
	 * @param property
	 * @param form
	 */
	protected void setMaterialProperty(ShopListProperty property, ShopListBaseForm form) {
		StringBuilder dirName = new StringBuilder("");
		dirName.append(form.getIdForDirName());
		dirName.append("_");
		dirName.append(session.getId());
		property.idForDir = dirName.toString();
		property.shopListSessionImgdirPath = getCommonProperty("gc.shopList.imgUpload.dir.session");

		List<MType> mtypeList = new ArrayList<>();
		if (CollectionUtils.isEmpty(property.tShopListMaterialList)) {
			property.tShopListMaterialList = new ArrayList<>();
		}

		try {
			mtypeList = typeService.getMTypeList(MTypeConstants.ShopListMaterialKbn.TYPE_CD);
		} catch (WNoResultException e) {
			callFraudulentProcessError(form);
		}

		// 素材区分に該当する値が登録されていれば、リストにセット
		// 画像データ本体はロジックでセットするのでここではエンティティにセットしない
		for (MType mType : mtypeList) {

			TShopListMaterial entity = new TShopListMaterial();

			if (isMaterialBytesExists(String.valueOf(mType.typeValue), form.materialMap)) {
				ShopListMaterialDto dto = form.materialMap.get(String.valueOf(mType.typeValue));
				Beans.copy(dto, entity).execute();
				property.tShopListMaterialList.add(entity);

			} else if (MTypeConstants.ShopListMaterialKbn.MAIN_1.equals(String.valueOf(mType.typeValue))
					&& StringUtils.isNotBlank(form.mainImgSelect)) {

				TCustomerImage customerImage = customerImageService.findById(NumberUtils.toInt(form.mainImgSelect));
				if (customerImage == null) {
					continue;
				}

				entity.shopListId  = property.tShopList.id;
				entity.materialKbn = mType.typeValue;
				entity.fileName    = customerImage.fileName;
				entity.contentType = customerImage.contentType;
				entity.materialData = customerImage.materialData;
				entity.customerImageId = customerImage.id;
				property.tShopListMaterialList.add(entity);

			} else if (MTypeConstants.ShopListMaterialKbn.LOGO.equals(String.valueOf(mType.typeValue))
					&& StringUtils.isNotBlank(form.logoImgSelect)) {

				TCustomerImage customerImage = customerImageService.findById(NumberUtils.toInt(form.logoImgSelect));
				if (customerImage == null) {
					continue;
				}

				entity.shopListId  = property.tShopList.id;
				entity.materialKbn = mType.typeValue;
				entity.fileName    = customerImage.fileName;
				entity.contentType = customerImage.contentType;
				entity.materialData = customerImage.materialData;
				entity.customerImageId = customerImage.id;
				property.tShopListMaterialList.add(entity);

			}
		}
	}

	/**
	 * 素材をセット
	 * @param form
	 */
	protected void setMaterial(ShopListBaseForm form) {
		setMaterial(form, NumberUtils.toInt(form.id));
	}

	/**
	 * 素材をセット
	 * @param form
	 * @param targetId
	 */
	protected void setMaterial(ShopListBaseForm form, int targetId) {
		String dirPath = getCommonProperty("gc.shopList.imgUpload.dir.session");
		StringBuilder dirName = new StringBuilder("");
		dirName.append(form.getIdForDirName());
		dirName.append("_");
		dirName.append(session.getId());

		List<IdSelectDto> materialList = shopListMaterialService.getIdList(targetId);
		for (IdSelectDto idSelectDto : materialList) {
			try {
				TShopListMaterial entity = shopListMaterialService.findById(idSelectDto.id);
				ShopListMaterialDto dto = new ShopListMaterialDto();
				Beans.copy(entity, dto).excludes("materialData").execute();
				if (entity.materialData == null) {
					continue;
				}

				String fileName = WebdataFileUtils.createFileName(String.valueOf(entity.materialKbn),
															ContentType.toEnum(entity.contentType));
				try {
					WebdataFileUtils.createWebdataFile(dirPath,
													dirName.toString(),
													fileName,
													Base64Util
														.decode(entity.
																materialData.
																replaceAll("\\n", "")));
				} catch (ImageWriteErrorException e) {
					shopListBaseLog.warn(e);
				}
				form.materialMap.put(String.valueOf(entity.materialKbn), dto);
			} catch (SNoResultException e) {
				// 主キーのループ中のため0件は想定されない。未処理とする。
			}
		}
	}

	/**
	 * 不正な操作のエラーを返す
	 */
	protected void callFraudulentProcessError(ShopListBaseForm form) {

		throw new FraudulentProcessException("不正な操作が行われました。" + form);

	}

	/**
	 * Mapにファイル（byte[]データ）が保持されているかどうかをチェックする。
	 * @param materialKey
	 * @param materialMap
	 * @return 存在すれば true : 存在しなければfalse
	 */
	protected boolean isMaterialBytesExists(String materialKey, Map<String, ShopListMaterialDto> materialMap) {

		if (materialMap == null || materialMap.isEmpty()) {
			return false;
		}

		return materialMap.containsKey(materialKey);
	}

	/**
	 * セッションをチェックします。
	 */
	protected void checkSession() {
		Object refelerKbn = session.getAttribute(ShopListBaseForm.SESSION_KEY.REFERER);
		if (refelerKbn == null
				|| !(refelerKbn instanceof RefelerKbn)) {
			throw new FraudulentProcessException();
		}

		if ((RefelerKbn) refelerKbn == RefelerKbn.WEB_DETAIL) {
			Object webIdObj = session.getAttribute(ShopListBaseForm.SESSION_KEY.WEB_ID);
			if (webIdObj == null) {
				throw new FraudulentProcessException();
			}

			if (StringUtils.isBlank(String.valueOf(webIdObj))) {
				throw new FraudulentProcessException();
			}
		}
	}



	/**
	 * 店舗一覧属性エンティティリストを作成します。
	 * @param form 店舗一覧フォーム
	 * @return 店舗一覧属性エンティティリスト
	 */
	protected static List<TShopListAttribute> createShopListAttributeListFromForm(ShopListBaseForm form) {
		List<TShopListAttribute> entityList = new ArrayList<TShopListAttribute>();
		if (!ArrayUtils.isEmpty(form.arbeitJobTypeArray)) {
			for (String value : form.arbeitJobTypeArray) {
				entityList.add(createAttributeEntity(MTypeConstants.ArbeitJobType.TYPE_CD, value));
			}
		}

		if (!ArrayUtils.isEmpty(form.arbeitMoneyLevelArray)) {
			for (String value : form.arbeitMoneyLevelArray){
				entityList.add(createAttributeEntity(MTypeConstants.ArbeitMoneyLevel.TYPE_CD, value));
			}
		}

		if (!ArrayUtils.isEmpty(form.arbeitShiftTimeArray)) {
			for (String value : form.arbeitShiftTimeArray) {
				entityList.add(createAttributeEntity(MTypeConstants.ArbeitShiftTime.TYPE_CD, value));
			}
		}

		if (!ArrayUtils.isEmpty(form.arbeitShiftWeekArray)) {
			for (String value : form.arbeitShiftWeekArray) {
				entityList.add(createAttributeEntity(MTypeConstants.ArbeitShiftWeek.TYPE_CD, value));
			}
		}

		if (!ArrayUtils.isEmpty(form.arbeitFeatureKbnArray)) {
			for (String value : form.arbeitFeatureKbnArray) {
				entityList.add(createAttributeEntity(MTypeConstants.ArbeitFeatureKbn.TYPE_CD, value));
			}
		}

		if (!ArrayUtils.isEmpty(form.workCharacteristicKbnArray)) {
			for (String value : form.workCharacteristicKbnArray) {
				entityList.add(createAttributeEntity(MTypeConstants.WorkCharacteristicKbn.TYPE_CD, value));
			}
		}

		if (!ArrayUtils.isEmpty(form.shopCharacteristicKbnArray)) {
			for (String value : form.shopCharacteristicKbnArray) {
				entityList.add(createAttributeEntity(MTypeConstants.ShopCharacteristicKbn.TYPE_CD, value));
			}
		}

		return entityList;

	}

	/**
	 * StationDtoをセット
	 * @param shopListId
	 * @param form
	 */
	protected void createStationDtoList(int shopListId, ShopListBaseForm form) {
		List<TShopListLine> list = shopListLogic.getShopListLineWithStationGroup(shopListId);
		if (CollectionUtils.isEmpty(list)) {
			return;
		}
		list.stream()
			.forEach(s -> {
				StationDto dto = Beans.createAndCopy(StationDto.class, s).execute();
				Beans.copy(s.vStationGroup, dto).execute();
				form.stationDtoList.add(dto);
			});
	}

	protected void createConditionDtoList(int shopListId, ShopListBaseForm form) {
		List<TShopChangeJobCondition> list = shopListLogic.getShopChangeJobConditionList(shopListId);
		if (CollectionUtils.isEmpty(list)) {
			return;
		}
		list.stream()
		.forEach(s -> {
			DisplayConditionDto dto = Beans.createAndCopy(DisplayConditionDto.class, s).execute();
			Beans.copy(s, dto).execute();
			form.displayConditionDtoList.add(dto);
			form.employJobKbnList.add(String.valueOf(s.employPtnKbn) + '-' + String.valueOf(s.jobKbn));
		});
	}


	/**
	 * 路線DTOを作成します。
	 * @param shopListId 店舗一覧ID
	 * @param form 店舗一覧フォーム
	 */
	@Deprecated
	protected void createRailroadDtos(int shopListId, ShopListBaseForm form) {
		List<TShopListRoute> entityList = shopListRouteService.findByShopListId(shopListId);
		if (CollectionUtils.isEmpty(entityList)) {
			return;
		}

		for (TShopListRoute entity : entityList) {
			ShopListRailroadDto dto = Beans.createAndCopy(ShopListRailroadDto.class,
										entity)
										.execute();

			if (GourmetCareeUtil.eqInt(1, entity.dispOrder)) {
				form.railroadDto1 = dto;
				continue;
			}

			if (GourmetCareeUtil.eqInt(2, entity.dispOrder)) {
				form.railroadDto2 = dto;
				continue;
			}

			if (GourmetCareeUtil.eqInt(3, entity.dispOrder)) {
				form.railroadDto3 = dto;
				continue;
			}
		}
	}


	/**
	 *
	 * 属性配列を作成します。
	 * @param shopListId
	 * @param form
	 */
	protected void createAttributeArrays(int shopListId, ShopListBaseForm form) {
		List<String> arbeitJobList = new ArrayList<String>();
		List<String> arbeitMoneyLevelList = new ArrayList<String>();
		List<String> arbeitShiftTimeList = new ArrayList<String>();
		List<String> arbeitShiftWeekList = new ArrayList<String>();
		List<String> arbeitFeatureKbnList = new ArrayList<String>();
		List<String> workCharacteristicKbnList = new ArrayList<String>();
		List<String> shopCharacteristicKbnList = new ArrayList<String>();

		List<TShopListAttribute> entityList = shopListAttributeService.findByShopListId(shopListId);

		if (CollectionUtils.isNotEmpty(entityList)) {
			for (TShopListAttribute entity : entityList) {
				if (MTypeConstants.ArbeitJobType.TYPE_CD.equals(entity.attributeCd)) {
					arbeitJobList.add(String.valueOf(entity.attributeValue));
					continue;
				}
				if (MTypeConstants.ArbeitMoneyLevel.TYPE_CD.equals(entity.attributeCd)) {
					arbeitMoneyLevelList.add(String.valueOf(entity.attributeValue));
					continue;
				}

				if (MTypeConstants.ArbeitShiftTime.TYPE_CD.equals(entity.attributeCd)) {
					arbeitShiftTimeList.add(String.valueOf(entity.attributeValue));
					continue;
				}

				if (MTypeConstants.ArbeitShiftWeek.TYPE_CD.equals(entity.attributeCd)) {
					arbeitShiftWeekList.add(String.valueOf(entity.attributeValue));
					continue;
				}

				if (MTypeConstants.ArbeitFeatureKbn.TYPE_CD.equals(entity.attributeCd)) {
					arbeitFeatureKbnList.add(String.valueOf(entity.attributeValue));
					continue;
				}

				if (MTypeConstants.WorkCharacteristicKbn.TYPE_CD.equals(entity.attributeCd)) {
					workCharacteristicKbnList.add(String.valueOf(entity.attributeValue));
					continue;
				}

				if (MTypeConstants.ShopCharacteristicKbn.TYPE_CD.equals(entity.attributeCd)) {
					shopCharacteristicKbnList.add(String.valueOf(entity.attributeValue));
					continue;
				}
			}
		}

		form.arbeitJobTypeArray = arbeitJobList.toArray(new String[0]);
		form.arbeitMoneyLevelArray = arbeitMoneyLevelList.toArray(new String[0]);
		form.arbeitShiftTimeArray = arbeitShiftTimeList.toArray(new String[0]);
		form.arbeitShiftWeekArray = arbeitShiftWeekList.toArray(new String[0]);
		form.arbeitFeatureKbnArray = arbeitFeatureKbnList.toArray(new String[0]);
		form.workCharacteristicKbnArray = workCharacteristicKbnList.toArray(new String[0]);
		form.shopCharacteristicKbnArray = shopCharacteristicKbnList.toArray(new String[0]);
	}

	/**
	 * 店舗一覧エンティティを作成します。
	 * @param attributeCd 属性コード
	 * @param attributeValue 属性値
	 * @return 店舗一覧エンティティ
	 */
	private static TShopListAttribute createAttributeEntity(String attributeCd, String attributeValue) {
		TShopListAttribute entity = new TShopListAttribute();
		entity.attributeCd = attributeCd;
		try {
			entity.attributeValue = Integer.parseInt(attributeValue);
		} catch (NumberFormatException e) {
			throw new FraudulentProcessException(e);
		}
		return entity;
	}


	/**
	 *
	 * 店舗一覧路線DTOを店舗一覧路線エンティティリストに変換します。
	 * @param form 店舗一覧ベースフォーム
	 * @return 店舗一覧路線エンティティリスト
	 */
	@Deprecated
	protected static List<TShopListRoute> convertRouteDtosToRouteEntityList(ShopListBaseForm form) {
		List<TShopListRoute> entityList = new ArrayList<TShopListRoute>();
		entityList.add(convertRouteDtoToEntity(form.railroadDto1, 1));
		entityList.add(convertRouteDtoToEntity(form.railroadDto2, 2));
		entityList.add(convertRouteDtoToEntity(form.railroadDto3, 3));
		return entityList;
	}

	/**
	 * TShopListLineのリストに変換
	 * @param stationDtoList
	 * @return 系列店舗の路線エンティティリスト
	 */
	protected static List<TShopListLine> createShopListLineList(List<StationDto> stationDtoList) {
		if (CollectionUtils.isEmpty(stationDtoList)) {
			return new ArrayList<>(0);
		}
		return stationDtoList.stream().map(s -> Beans.createAndCopy(TShopListLine.class, s).execute()).collect(Collectors.toList());
	}

	/**
	 * インディードタグの入力をinsert用に整形
	 * @param tagList
	 * @param shopId
	 * @return
	 */
	protected List<MShopListTagMapping> createShopListTagList(String tagList, Integer shopId) {
		List<MShopListTagMapping> tagMappingList = new ArrayList<MShopListTagMapping>();
		List<String> tagNameList = Arrays.asList(tagList.split(","));
		for (String name : tagNameList) {
			MShopListTag tag = tagListLogic.findByShopListTagName(name);
			// タグが空でない場合
			if (tag != null) {
				MShopListTagMapping tagMapping = new MShopListTagMapping();
				tagMapping.shopListTagId = tag.id;
				tagMapping.shopListId = shopId;
				tagMappingList.add(tagMapping);
			}
		}

		return tagMappingList;
	}

	protected static List<TShopChangeJobCondition> createShopChangeJobConditionList(List<DisplayConditionDto> conditionList) {
		if (CollectionUtils.isEmpty(conditionList)) {
			return new ArrayList<>(0);
		}
		return conditionList.stream().map(s -> Beans.createAndCopy(TShopChangeJobCondition.class, s).execute()).collect(Collectors.toList());
	}

	/**
	 * 店舗一覧路線DTOを店舗一覧路線エンティティに変換します。
	 * @param dto 店舗一覧路線DTO
	 * @param dispOrder 表示順
	 * @return 店舗一覧路線エンティティ
	 */
	@Deprecated
	private static TShopListRoute convertRouteDtoToEntity(ShopListRailroadDto dto, int dispOrder) {
		TShopListRoute entity = Beans.createAndCopy(TShopListRoute.class, dto)
									.excludesWhitespace()
									.excludesNull()
									.execute();
		entity.dispOrder = dispOrder;
		entity.arbeitFlg = TShopListRoute.ArbeitFlgValue.ARBEIT;
		return entity;
	}

	/**
	 * ターゲットアンカーを取得します。
	 * @return ターゲットアンカー
	 */
	public String getTargetAnchor() {
		if (targetAnchor == null) {
			return "";
		}
		return targetAnchor;
	}

	/**
	 * ターゲットアンカーをセットします。
	 * @param targetAnchor ターゲットアンカー
	 */
	private void setTargetAnchor(String materialKbn) {
		if (ShopListMaterialKbn.MAIN_1.equals(materialKbn)) {
			targetAnchor = TARGET_ANCHOR_IMAGE;
			return;
		}

		if (ShopListMaterialKbn.LOGO.equals(materialKbn)) {
			targetAnchor = TARGET_ANCHOR_LOGO;
		}
	}


	/**
	 * SHOPList用のよく使うタグを作成する
	 * @return
	 */
	protected List<TagListDto> createTagList() {
		return tagListLogic.getCommonlyUsedFindByShopListTagOrderByCount();
	}

	/**
	 * ショップリストに登録されたタグの一覧を取得する
	 * @param shopListId
	 * @return
	 */
	protected void getShopListTag(Integer shopListId, ShopListBaseForm form) {

		List<MShopListTag> shopListTags = tagListLogic.findByShopListId(shopListId);
		if (CollectionUtils.isEmpty(shopListTags)) {
			form.tagList = null;
		}

		TagListDto dto = new TagListDto();
		for (MShopListTag entity : shopListTags) {
			dto.tagNameList.add(entity.getShopListTagName());
		}

		form.tagList = String.join(",", dto.tagNameList);
	}

	/**
	 * 顧客の登録している画像一覧を取得する
	 *
	 * @param customerId
	 * @return
	 */
	protected List<ImageDto> createImageDtoList(int customerId) {
		List<ImageDto> imageDtoList = new ArrayList<>();
		List<VCustomerImageNoData> imageEntityList;
		try {
			imageEntityList = customerImageNoDataService.findListByCustomerId(customerId);
		} catch (WNoResultException e) {
			return imageDtoList;
		}

		for (VCustomerImageNoData imageEntity : imageEntityList) {
			ImageDto imageDto = new ImageDto();
			Beans.copy(imageEntity, imageDto).execute();
			imageDto.filePath = CustomerImageAction.createImagePath(imageEntity.id);

			imageDtoList.add(imageDto);
		}

		return imageDtoList;
	}

	/**
	 * 画像の一時アップロード処理（DBには保存しない）
	 *
	 * @param form
	 * @return
	 */
	protected String uploadTempImageJsonResponse(ShopListBaseForm form) {

		Map<String, String> resMap = new HashMap<>();

		String materialKbn = form.hiddenMaterialKbn;

		// パラメータが空の場合はエラー
		if (StringUtils.isEmpty(materialKbn)) {
			resMap.put("error", "アップロードに失敗しました");
			ResponseUtil.write(JSON.encode(resMap), "application/json", "UTF-8");
			return null;
		}

		// 画像のアップロード処理
		String result = doUploadMaterial(form);

		if(StringUtils.isNotEmpty(result)) {
			resMap.put("error", result);
			ResponseUtil.write(JSON.encode(resMap)
					, GourmetCareeConstants.JSON_CONTENT_TYPE
					, GourmetCareeConstants.ENCODING);
			return null;
		}

		// UP画像のパス
		String imgPath = String.format("%s/util/imageUtility/displayShopListImage/%s/%s",
				request.getContextPath(),
				materialKbn,
				form.getIdForDirName());

		resMap.put("imgPath", imgPath);
		ResponseUtil.write(JSON.encode(resMap)
				, GourmetCareeConstants.JSON_CONTENT_TYPE
				, GourmetCareeConstants.ENCODING);
		return null;
	}

	/**
	 * 一時画像の削除処理
	 * @param form
	 * @return
	 */
	protected String deleteTempImageJsonResponse(ShopListBaseForm form) {
		Map<String, String> resMap = new HashMap<>();

		// パラメータが空の場合はエラー
		if (StringUtils.isEmpty(form.hiddenMaterialKbn)) {
			resMap.put("error", "削除に失敗しました");
			ResponseUtil.write(JSON.encode(resMap)
					, GourmetCareeConstants.JSON_CONTENT_TYPE
					, GourmetCareeConstants.ENCODING);
            return null;
		}

		// 画像の削除処理
		doDeleteMaterial(form);
		ResponseUtil.write(JSON.encode(resMap)
				, GourmetCareeConstants.JSON_CONTENT_TYPE
				, GourmetCareeConstants.ENCODING);
		return null;
	}

}
