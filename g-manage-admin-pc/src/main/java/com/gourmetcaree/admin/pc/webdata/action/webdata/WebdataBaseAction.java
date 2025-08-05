package com.gourmetcaree.admin.pc.webdata.action.webdata;


import static com.gourmetcaree.common.constants.GourmetCareeConstants.*;
import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.util.Base64Util;
import org.seasar.framework.util.InputStreamUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.ActionMessagesUtil;
import org.seasar.struts.util.MessageResourcesUtil;
import org.seasar.struts.util.ResponseUtil;

import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.webdata.dto.webdata.ListDto;
import com.gourmetcaree.admin.pc.webdata.form.webdata.WebdataForm;
import com.gourmetcaree.admin.pc.webdata.form.webdata.WebdataForm.WebJobDto;
import com.gourmetcaree.admin.service.dto.RailroadDto;
import com.gourmetcaree.admin.service.logic.CustomerLogic;
import com.gourmetcaree.admin.service.logic.RailroadLogic;
import com.gourmetcaree.admin.service.logic.ShopListLogic;
import com.gourmetcaree.admin.service.logic.WebdataLogic;
import com.gourmetcaree.admin.service.logic.tempWebdata.TempWebdataLogic;
import com.gourmetcaree.admin.service.property.CustomerProperty;
import com.gourmetcaree.admin.service.property.WebdataProperty;
import com.gourmetcaree.admin.service.property.tempWebdata.TempMaterialProperty;
import com.gourmetcaree.admin.service.property.tempWebdata.TempWebJobPropterty;
import com.gourmetcaree.admin.service.property.tempWebdata.TempWebdataProperty;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.dto.MaterialDto;
import com.gourmetcaree.common.dto.TagListDto;
import com.gourmetcaree.common.dto.WebRouteDto;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.AuthorizationException;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.exception.ImageWriteErrorException;
import com.gourmetcaree.common.logic.TagListLogic;
import com.gourmetcaree.common.logic.ValueToNameConvertLogic;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.common.util.WebdataFileUtils;
import com.gourmetcaree.common.util.WebdataFileUtils.ContentType;
import com.gourmetcaree.db.common.constants.MAreaConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants.MaterialKbn;
import com.gourmetcaree.db.common.constants.MTypeConstants.SizeKbn;
import com.gourmetcaree.db.common.entity.MArea;
import com.gourmetcaree.db.common.entity.MCustomer;
import com.gourmetcaree.db.common.entity.MType;
import com.gourmetcaree.db.common.entity.MWebTag;
import com.gourmetcaree.db.common.entity.TMaterial;
import com.gourmetcaree.db.common.entity.TShopList;
import com.gourmetcaree.db.common.entity.TTempWeb;
import com.gourmetcaree.db.common.entity.TWeb;
import com.gourmetcaree.db.common.entity.TWebAttribute;
import com.gourmetcaree.db.common.entity.TWebIpPhone;
import com.gourmetcaree.db.common.entity.TWebJob;
import com.gourmetcaree.db.common.entity.TWebJobAttribute;
import com.gourmetcaree.db.common.entity.TWebJobShopList;
import com.gourmetcaree.db.common.entity.TWebRoute;
import com.gourmetcaree.db.common.entity.TWebShopList;
import com.gourmetcaree.db.common.entity.TWebSpecial;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.AreaService;
import com.gourmetcaree.db.common.service.MaterialService;
import com.gourmetcaree.db.common.service.ShopListService;
import com.gourmetcaree.db.common.service.TypeService;
import com.gourmetcaree.db.common.service.WebAttributeService;
import com.gourmetcaree.db.common.service.WebIpPhoneService;
import com.gourmetcaree.db.common.service.WebService;
import com.gourmetcaree.db.common.service.WebShopListService;
import com.gourmetcaree.db.common.service.WebTagMappingService;
import com.gourmetcaree.db.webdata.dto.webdata.IdSelectDto;

import jp.co.whizz_tech.common.image.Image;
import jp.co.whizz_tech.common.image.encoder.impl.JpegEncoder;
import jp.co.whizz_tech.common.image.filter.impl.ResizeFilter;
import net.arnx.jsonic.JSON;

/**
 *
 * WEBデータBaseクラス
 * @author Makoto Otani
 *
 */
public abstract class WebdataBaseAction extends PcAdminAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(WebdataBaseAction.class);

	/** Webデータのロジック */
	@Resource
	protected WebdataLogic webdataLogic;

	/** 区分マスタのサービス */
	@Resource
	protected TypeService typeService;

	/** 顧客のロジック */
	@Resource
	protected CustomerLogic customerLogic;

	/** 路線のロジック */
	@Resource
	protected RailroadLogic railroadLogic;

	@Resource
	private WebService webService;

	/** WEBデータ属性サービス */
	@Resource
	protected WebAttributeService webAttributeService;

	/** 素材サービス */
	@Resource
	private MaterialService materialService;

	/** ウェブデータとIP電話のサービス */
	@Resource
	protected WebIpPhoneService webIpPhoneService;

	/** 系列店舗ロジック */
	@Resource
	protected ShopListLogic shopListLogic;

	/** 系列店舗サービス */
	@Resource
	protected ShopListService shopListService;

	/** WEBデータ店舗サービス */
	@Resource
	protected WebShopListService webShopListService;

	/** タグのサービス（フリーワード用） */
	@Resource
	protected TagListLogic tagListLogic;

	@Resource
	protected WebTagMappingService webTagMappingService;

	@Resource
	protected TempWebdataLogic tempWebdataLogic;

	@Resource
	protected AreaService areaService;

	@Resource
	protected ValueToNameConvertLogic valueToNameConvertLogic;


	/** 応募用初期メッセージ */
	protected static final String DEFAULT_APPLICATION_MESSAGE = "応募フォームにご記入ください。折り返しご連絡致します。" + System.getProperty("line.separator") + "お電話での応募も受け付けております。" + System.getProperty("line.separator") + System.getProperty("line.separator") + "【採用の可能性があれば応募してみたい】という方は…「プレ応募に進む」へ" + System.getProperty("line.separator") + "経歴などを拝見しご返信させていただきます。" + System.getProperty("line.separator") + System.getProperty("line.separator") + "※お電話での際は「グルメキャリーを見た」とお伝えいただくとスムーズです。";

	/**
	 * ログインしている会社が選択可能なエリアかどうかチェックする
	 * @param form WEBデータフォーム
	 * @return 選択可能なエリアの場合はtrue、選択不可能なエリアの場合はエラーメッセージをセットしてfalse
	 */
	protected boolean canSelectArea(WebdataForm form) {

		// 代理店以外は全て選択可能なので処理を行わない
		if (!ManageAuthLevel.AGENCY.value().equals(userDto.authLevel)) {
			return true;
		}

		WebdataProperty property = new WebdataProperty();
		property.areaCd = Integer.parseInt(form.areaCd);

		// エラー情報
		ActionMessages errors = new ActionMessages();
		try {
			if (!webdataLogic.isCompanyArea(property)) {
				// 「入力した{エリア}は登録できません。」
				errors.add("errors", new ActionMessage("errors.notInsertData",
						MessageResourcesUtil.getMessage("labels.areaCd")));
			}

		// 会社エリアマスタが取得できなかった場合は不正なエラーとして処理
		} catch (WNoResultException e) {
			callFraudulentProcessError(form);
		}

		// 画面表示するため、エラーをセット
		ActionMessagesUtil.addErrors(request, errors);

		return errors.isEmpty();
	}

	/**
	 * 選択可能な所属（会社）かどうかチェックする
	 * @param form WEBデータフォーム
	 * @return 選択可能な会社の場合はtrue、選択不可能な会社の場合はエラーメッセージをセットしてfalse
	 */
	protected boolean canSelectCompany(WebdataForm form) {

		// プロパティに会社IDをセット
		WebdataProperty property = new WebdataProperty();
		property.companyId = Integer.parseInt(form.companyId);

		// エラー情報
		ActionMessages errors = new ActionMessages();
		try {
			if (!webdataLogic.canSelectCompany(property)) {
				// 「入力した{所属}は登録できません。」
				errors.add("errors", new ActionMessage("errors.notInsertData",
						MessageResourcesUtil.getMessage("labels.webCompany")));
			}

		// 会社が取得できなかった場合は不正なエラーとして処理
		} catch (WNoResultException e) {
			callFraudulentProcessError(form);
		}

		// 画面表示するため、エラーをセット
		ActionMessagesUtil.addErrors(request, errors);

		return errors.isEmpty();
	}

	/**
	 * 入力値と、顧客データの整合性チェックを行う
	 * @param form WEBデータフォーム
	 * @return 不整合の場合はエラーメッセージをセットしてfalse
	 */
	protected boolean isCustomerData(WebdataForm form) {

		// 顧客データが選択されていない場合は、処理を行わない
		if(StringUtil.isEmpty(form.customerDto.id)) {
			return true;
		}

		WebdataProperty property = new WebdataProperty();
		// エリアをセット
		property.areaCd = Integer.parseInt(form.areaCd);
		// 顧客IDをセット
		property.customerId = Integer.parseInt(form.customerDto.id);
		// 会社ID
		property.companyId = Integer.parseInt(form.companyId);

		try {

		// 顧客IDが不正な場合はエラー
		} catch (NumberFormatException e) {
			callFraudulentProcessError(form);
		}

		// エラー情報
		ActionMessages errors = new ActionMessages();

		try {
			// 顧客の会社と同じ会社が選択されているかチェック
			if (!webdataLogic.isCustomerCompany(property)) {
				// 「選択した{顧客}と同じ{会社}を選択してください。」
				errors.add("errors", new ActionMessage("errors.notSameData",
						MessageResourcesUtil.getMessage("msg.app.customer"),
						MessageResourcesUtil.getMessage("msg.app.company")));
			}

		// データが見つからない場合は、エラー
		} catch (WNoResultException e) {
			callFraudulentProcessError(form);
		}

		// 画面表示するため、エラーをセット
		ActionMessagesUtil.addErrors(request, errors);

		return errors.isEmpty();
	}

	/**
	 * 職種の内容をチェックする
	 * @param form WEBデータフォーム
	 * @return 募集職種があるが仕事内容項目が未入力の場合false 不正な店舗が含まれていればfalse
	 */
	protected boolean checkJobWorkContents(WebdataForm form) {

		// 募集職種が設定されていない場合は処理を行わない
		if(form.webJobDtoList.isEmpty()) {
			return true;
		}

		// エラー情報
		ActionMessages errors = new ActionMessages();

//		顧客IDから店舗を取得
		List<TShopList> shopList = new ArrayList<>(0);
		try {
//			顧客が選択されているときのみ
			if(form.customerDto.id != null && form.customerDto.id != "") {
				shopList = shopListService.getShopListByCustomerId(Integer.parseInt(form.customerDto.id));
			}
		} catch (WNoResultException e) {
//			何もしない
		}
//		店舗IDリストを作成
		List<String> shopIdList = new ArrayList<>(0);
		if(CollectionUtils.isNotEmpty(shopList)) {
			shopIdList = shopList.stream().map(TShopList -> TShopList.id.toString()).collect(Collectors.toList());
		}

		// 募集職種設定から職種単体情報を取り出す
		for (WebJobDto webJob : form.webJobDtoList) {
//			職種の名前を設定する
			String strJobName = "";
			// 雇用形態が正社員以外の場合、職種の前にショートラベルを付ける
			if(!webJob.employPtnKbn.equals(Integer.toString(MTypeConstants.EmployPtnKbn.SEISYAIN))) {
				strJobName = "【" + MTypeConstants.EmployPtnKbn.employPtnSmallLabelMap.get(Integer.valueOf(webJob.employPtnKbn)) +"】";
			}
			strJobName = strJobName + valueToNameConvertLogic.convertToJobName(new String[] {String.valueOf(webJob.jobKbn)});
			// 掲載フラグが可能かつ仕事内容項目が未入力の場合
			if (webJob.publicationFlg.equals(String.valueOf(MTypeConstants.PublicationFlg.OK)) && StringUtils.isEmpty(webJob.workContents)) {
				errors.add("errors", new ActionMessage("errors.noWorkContent",strJobName));
			}
//			不正な店舗が含まれている場合
			if(CollectionUtils.isNotEmpty(webJob.shopIdList) && !shopIdList.containsAll(webJob.shopIdList)) {
				errors.add("errors", new ActionMessage("errors.invalidJobShop",strJobName));
			}
			if(StringUtils.isNotEmpty(webJob.saleryStructureKbn) &&
					!webJob.saleryStructureKbn.equals(String.valueOf(MTypeConstants.SaleryStructureKbn.HOURLY)) &&
					!webJob.saleryStructureKbn.equals(String.valueOf(MTypeConstants.SaleryStructureKbn.DAILY)) &&
					StringUtils.isEmpty(webJob.salaryDetail) && MTypeConstants.PublicationFlg.OK == NumberUtils.toInt(webJob.publicationFlg)) {
				errors.add("errors", new ActionMessage("errors.noSalaryDetail",strJobName));
			}
			if((StringUtils.isNotEmpty(webJob.annualLowerSalaryPrice) ||  StringUtils.isNotEmpty(webJob.annualUpperSalaryPrice))
					&& StringUtils.isEmpty(webJob.annualSalaryDetail) && MTypeConstants.PublicationFlg.OK == NumberUtils.toInt(webJob.publicationFlg)) {
				errors.add("errors", new ActionMessage("errors.noAnnualSalaryDetail",strJobName));
			}
			if((StringUtils.isNotEmpty(webJob.monthlyLowerSalaryPrice) ||  StringUtils.isNotEmpty(webJob.monthlyUpperSalaryPrice))
					&& StringUtils.isEmpty(webJob.monthlySalaryDetail) && MTypeConstants.PublicationFlg.OK == NumberUtils.toInt(webJob.publicationFlg)) {
				errors.add("errors", new ActionMessage("errors.noMonthlySalaryDetail",strJobName));
			}
		}

		// 画面表示するため、エラーをセット
		ActionMessagesUtil.addErrors(request, errors);

		return errors.isEmpty();
	}

	/**
	 * 「該当するデータが見つかりませんでした。」のエラーメッセージを返す
	 */
	protected void dataNotFoundMessage() {

		// 「該当するデータが見つかりませんでした。」
		throw new ActionMessagesException("errors.app.dataNotFound");

	}

	/**
	 * 不正な操作のエラーを返す
	 */
	protected void callFraudulentProcessError(WebdataForm form) {

		throw new FraudulentProcessException("不正な操作が行われました。" + form);

	}
	/**
	 * アクセス権限エラーを返す
	 */
	protected void callAuthLevelError(WebdataForm form) {

		throw new AuthorizationException("アクセスする権限がありません" + form);

	}

	/**
	 * 追加された路線図を表示用に変更
	 */
	protected void convertRouteData(WebdataForm form) {

		WebRouteDto dto = new WebRouteDto();

		try  {
			// 名称の取得
			RailroadDto railNameDto = railroadLogic.getRailroadDto(Integer.parseInt(form.stationId));
			// 取得できない場合はエラー
			if (railNameDto == null) {
				// 「{路線・最寄駅}の取得に失敗しました。」
				throw new ActionMessagesException("errors.selectData",
						MessageResourcesUtil.getMessage("labels.routeStation"));
			}
			Beans.copy(railNameDto, dto).execute();
			dto.railroadId = Integer.parseInt(form.railroadId);
			dto.routeId = Integer.parseInt(form.routeId);
			dto.stationId = Integer.parseInt(form.stationId);

			form.webRouteList.add(dto);

			// 選択された路線をリセット
			form.railroadId = "";
			form.routeId = "";
			form.stationId = "";

			railNameDto = null;

		} catch (NumberFormatException e) {
			// 「{路線・最寄駅}の取得に失敗しました。」
			throw new ActionMessagesException("errors.selectData",
					MessageResourcesUtil.getMessage("labels.routeStation"));

		}

	}

	/**
	 * 選択された路線をリストから削除
	 * @param form
	 */
	protected void deleteRouteData(WebdataForm form) {

		for (int i = 0; i < form.webRouteList.size(); i++) {

			// フォームに登録された駅から削除対象を探し、リストを削除
			if (form.deleteStationId.equals(String.valueOf(form.webRouteList.get(i).stationId))) {
				form.webRouteList.remove(i);
			}
		}
	}

	/**
	 * 業種2を選択せず、業種3を選択した場合入れ替え処理を行う
	 * @param form WEBデータフォーム
	 */
	protected void sortIndustryKbn(WebdataForm form) {

		// 業種2が空で、業種3が入力されている場合
		if (StringUtil.isEmpty(form.industryKbn2) && !StringUtil.isEmpty(form.industryKbn3)) {
			// 3を2に移動
			form.industryKbn2 = form.industryKbn3;
			form.industryKbn3 = "";
		}
	}

	/**
	 * ホームページを順番に入力しなかった場合入れ替え処理を行う
	 * @param form WEBデータフォーム
	 */
	protected void sortHomepage(WebdataForm form) {

		// ホームページ1が空で、ホームページ2が入力されている場合
		if (StringUtil.isEmpty(form.homepage1) && !StringUtil.isEmpty(form.homepage2)) {
			// 2を1に移動
			form.homepage1 = form.homepage2;
			form.homepage2 = "";

			if (StringUtil.isNotBlank(form.homepageComment2)) {
				form.homepageComment1 = form.homepageComment2;
				form.homepageComment2 = "";
			}
		}

		// ホームページ2が空で、ホームページ3が入力されている場合
		if (StringUtil.isEmpty(form.homepage2) && !StringUtil.isEmpty(form.homepage3)) {
			// 3を2に移動
			form.homepage2 = form.homepage3;
			form.homepage3 = "";

			if (StringUtil.isNotBlank(form.homepageComment3)) {
				form.homepageComment2 = form.homepageComment3;
				form.homepageComment3 = "";
			}

		}

		// ホームページ1が空で、ホームページ3が入力されている場合
		if (StringUtil.isEmpty(form.homepage1) && !StringUtil.isEmpty(form.homepage3)) {
			// 3を1に移動
			form.homepage1 = form.homepage3;
			form.homepage3 = "";

			if (StringUtil.isNotBlank(form.homepageComment3)) {
				form.homepageComment1 = form.homepageComment3;
				form.homepageComment3 = "";
			}
		}
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
	 * サイズによって素材区分を取得する。
	 * @param sizeKbn
	 * @return
	 */
	private Set<String> getMaterialKbnListBySize(String sizeKbn) {
		Set<String> set = new HashSet<String>();
		int intSizeKbn = NumberUtils.toInt(sizeKbn, MTypeConstants.SizeKbn.TEXT_WEB);
		// ロゴは全サイズで扱うので、あらかじめ入れておく。
		set.add(MTypeConstants.MaterialKbn.LOGO);
		// サイズによって切り分け
		switch (intSizeKbn) {
			case MTypeConstants.SizeKbn.E:
			case MTypeConstants.SizeKbn.D:
				set.add(MTypeConstants.MaterialKbn.ATTENTION_HERE);
				set.add(MTypeConstants.MaterialKbn.MAIN_1);
				set.add(MTypeConstants.MaterialKbn.MAIN_2);
				set.add(MTypeConstants.MaterialKbn.MAIN_3);
				set.add(MTypeConstants.MaterialKbn.PHOTO_A);
				set.add(MTypeConstants.MaterialKbn.PHOTO_B);
				set.add(MTypeConstants.MaterialKbn.PHOTO_C);
				set.add(MTypeConstants.MaterialKbn.RIGHT);
				break;

			case MTypeConstants.SizeKbn.C:
				set.add(MTypeConstants.MaterialKbn.ATTENTION_HERE);
				set.add(MTypeConstants.MaterialKbn.MAIN_1);
				set.add(MTypeConstants.MaterialKbn.PHOTO_A);
				set.add(MTypeConstants.MaterialKbn.PHOTO_B);
				set.add(MTypeConstants.MaterialKbn.PHOTO_C);
				break;

			case MTypeConstants.SizeKbn.B:
				set.add(MTypeConstants.MaterialKbn.ATTENTION_HERE);
				set.add(MTypeConstants.MaterialKbn.MAIN_1);
				break;

			case MTypeConstants.SizeKbn.A:
				set.add(MTypeConstants.MaterialKbn.MAIN_1);
				break;

			default:
				break;
		}

		return set;
	}

	/**
	 * アップロードされていない画像を設定します。
	 * @param form
	 */
	protected String setUnUploadedImages(WebdataForm form) {
		Set<String> materialKbnList = getMaterialKbnListBySize(form.sizeKbn);

		Map<String, Map<String, String>> resMap = new HashMap<String, Map<String, String>>();

		String contextPath = request.getContextPath();
		String idForDirName = form.getIdForDirName();

		for (String materialKbn : materialKbnList) {

			String thumbKbn = MaterialKbn.getThumbValue(materialKbn);

			if (form.materialMap == null || form.materialMap.containsKey(thumbKbn)) {
				continue;
			}

			StringBuilder blackImageFileName = new StringBuilder("");
			blackImageFileName.append(getCommonProperty("gc.webdata.blackImg.dir"));
			blackImageFileName.append(materialKbn);
			blackImageFileName.append(".jpg");
			byte[] imageBytes;
			try {
				File file = new File(blackImageFileName.toString());
				InputStream inputStream = new FileInputStream(file);
				imageBytes = InputStreamUtil.getBytes(inputStream);

				form.materialMap.put(materialKbn, createMaterialDto(materialKbn, false, idForDirName, imageBytes));
				form.materialMap.put(thumbKbn, createMaterialDto(materialKbn, true, idForDirName, imageBytes));

				imageBytes = null;
				inputStream.close();
			} catch (FileNotFoundException e) {
				log.warn(e);
				imageBytes = null;
			} catch (ImageWriteErrorException e) {
				log.warn(e);
				imageBytes = null;
			} catch (IOException e) {
				log.warn(e);
				imageBytes = null;
			}

			// UP画像のパス
			String imgPath = String.format("%s/util/imageUtility/displayWebdataImage/%s/%s",
					contextPath,
					materialKbn,
					idForDirName);
			// サムネイル画像のパス
			String imgThumbPath = String.format("%s/util/imageUtility/displayWebdataImage/%s/%s",
					contextPath,
					thumbKbn,
					idForDirName);

			Map<String, String> pathMap = new HashMap<String, String>();
			pathMap.put("imgPath", imgPath);
			pathMap.put("imgThumbPath", imgThumbPath);
			resMap.put(materialKbn, pathMap);
		}

		ResponseUtil.write(JSON.encode(resMap), JSON_CONTENT_TYPE, ENCODING);
		return null;
	}


	private MaterialDto createMaterialDto(String materialKbn, boolean thumbFlg, String idForDirName, byte[] imageBytes) throws ImageWriteErrorException, IOException {
		String thumbName = null;

		if (thumbFlg) {
			thumbName = MTypeConstants.MaterialKbn.getThumbValue(materialKbn);
		}
		String dirPath = getCommonProperty("gc.webdata.imgUpload.dir.session");
		StringBuilder dirName = new StringBuilder("");
		dirName.append(idForDirName);
		dirName.append("_");
		dirName.append(session.getId());

		String fileName;



		MaterialDto dto = new MaterialDto();
		dto.contentType = GourmetCareeConstants.MEDIA_CONTENT_TYPE_JPEG;
		if (thumbFlg) {
			fileName = WebdataFileUtils.createFileName(thumbName, ContentType.JPEG);
			dto.fileName = thumbName;
			dto.materialKbn = thumbName;
		} else {
			fileName = WebdataFileUtils.createFileName(materialKbn, ContentType.JPEG);
			dto.fileName = materialKbn;
			dto.materialKbn = materialKbn;
		}




		if (thumbFlg) {
			WebdataFileUtils.createWebdataFile(dirPath, dirName.toString(), fileName, getSmallImage(imageBytes, dto.contentType, NumberUtils.toInt(materialKbn)));
		} else {
			WebdataFileUtils.createWebdataFile(dirPath, dirName.toString(), fileName, imageBytes);
		}

		return dto;
	}

	/**
	 * DB登録までに使用する画像フォルダを削除する。
	 */
	protected void initUploadMaterial(WebdataForm form) {
		String dirPath = getCommonProperty("gc.webdata.imgUpload.dir.session");
		StringBuilder dirName = new StringBuilder("");
		dirName.append(form.getIdForDirName());
		dirName.append("_");
		dirName.append(session.getId());

		WebdataFileUtils.deleteWebdataDir(dirPath, dirName.toString());
	}

	/**
	 * 画像のアップロード処理
	 * @param form
	 * @return
	 */
	protected String uploadImage(WebdataForm form) {

		Map<String, String> resMap = new HashMap<String, String>();

		// パラメータが空の場合はエラー
		if (StringUtil.isEmpty(form.hiddenMaterialKbn)) {
			resMap.put("error", "アップロードに失敗しました");
			ResponseUtil.write(JSON.encode(resMap), "application/json", "UTF-8");
			return null;
		}

		String materialKbn = form.hiddenMaterialKbn;
		// 画像のアップロード処理
		String result = doUploadMaterial(form);
		if(StringUtil.isNotEmpty(result)) {
			resMap.put("error", result);
			ResponseUtil.write(JSON.encode(resMap), JSON_CONTENT_TYPE, ENCODING);
			return null;
		}

		// UP画像のパス
		String imgPath = String.format("%s/util/imageUtility/displayWebdataImage/%s/%s",
				request.getContextPath(),
				materialKbn,
				form.getIdForDirName());
		// サムネイル画像のパス
		String imgThumbPath = String.format("%s/util/imageUtility/displayWebdataImage/%s/%s",
				request.getContextPath(),
				MaterialKbn.getThumbValue(materialKbn),
				form.getIdForDirName());
		resMap.put("imgPath", imgPath);
		resMap.put("imgThumbPath", imgThumbPath);
		ResponseUtil.write(JSON.encode(resMap), JSON_CONTENT_TYPE, ENCODING);
		return null;
	}

	/**
	 * 画像の削除処理
	 * @param form
	 * @return
	 */
	protected String deleteImage(WebdataForm form) {

		Map<String, String> resMap = new HashMap<>();

		// パラメータが空の場合はエラー
		if (StringUtil.isEmpty(form.hiddenMaterialKbn)) {
			resMap.put("error", "削除に失敗しました");
			ResponseUtil.write(JSON.encode(resMap), JSON_CONTENT_TYPE, ENCODING);
            return null;
		}

		// 画像の削除処理
		doDeleteMaterial(form);
		ResponseUtil.write(JSON.encode(resMap), JSON_CONTENT_TYPE, ENCODING);
		return null;
	}

	/**
	 * 素材アップロードのメイン処理
	 */
	protected String doUploadMaterial(WebdataForm form) {

		FormFile formFile = form.createFormFile(form.hiddenMaterialKbn);

		if (formFile == null || formFile.getFileSize() <= 0) {
			form.hiddenMaterialKbn = null;
			form.deleteImgFile();
			return MessageResourcesUtil.getMessage("errors.app.upload");
		}

		if (GourmetCareeConstants.MEDIA_CONTENT_TYPE_JPEG.equals(formFile.getContentType()) ||
				GourmetCareeConstants.MEDIA_CONTENT_TYPE_PJPEG.equals(formFile.getContentType())) {
			// 何もしない
		} else {
			// カーソルをリセット
			form.cursorPosition = "";
			form.deleteImgFile();
			return MessageResourcesUtil.getMessage("errors.app.imageNotJpeg");
		}

		String dirPath = getCommonProperty("gc.webdata.imgUpload.dir.session");
		StringBuilder dirName = new StringBuilder("");
		dirName.append(form.getIdForDirName());
		dirName.append("_");
		dirName.append(session.getId());

		try {

			MaterialDto dto = new MaterialDto();
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

			/*---------------------*/
			/* サムネイル画像の作成
			/*---------------------*/
			byte[] thumbImage = null;
			// イメージオブジェクトの生成
			Image image = Image.getInstance(formFile.getFileData());

			// 画像がjpgの時にサムネイル画像を作成する
			if (GourmetCareeConstants.MEDIA_CONTENT_TYPE_JPEG.equals(formFile.getContentType()) ||
					GourmetCareeConstants.MEDIA_CONTENT_TYPE_PJPEG.equals(formFile.getContentType())) {

				// 縮小比率をプロパティから取得
				float xScale = NumberUtils.toFloat(getCommonProperty("gc.marerial.materialKbn" + form.hiddenMaterialKbn), GourmetCareeConstants.DEFAULT_PERCENTAGE);
				float yScale = xScale;
				// 画質をプロパティから取得
				float quality = NumberUtils.toFloat(getCommonProperty("gc.marerial.quality"), 1.0F);

				// 画像の縮小
				thumbImage = image
						.addFilter(
								new ResizeFilter().setScale(xScale, yScale)
								.setInterpolation(ResizeFilter.INTERP_BILINEAR))
						.executeFilter()
						.encode(new JpegEncoder().setQuality(quality));
			}

			MaterialDto thumbDto = new MaterialDto();
			// サムネイルの区分を取得してセット
			String thumbMaterialKbn =  MTypeConstants.MaterialKbn.getThumbValue(form.hiddenMaterialKbn);
			thumbDto.materialKbn =thumbMaterialKbn;
			// ファイル名をセット
			thumbDto.fileName = formFile.getFileName();
			// コンテントタイプをセット
			// コンテントタイプがpjpegの場合は、jpegで登録
			if (GourmetCareeConstants.MEDIA_CONTENT_TYPE_PJPEG.equals(formFile.getContentType())) {
				thumbDto.contentType = GourmetCareeConstants.MEDIA_CONTENT_TYPE_JPEG;
			} else  {
				thumbDto.contentType = formFile.getContentType();
			}

			String fileName = WebdataFileUtils.createFileName(form.hiddenMaterialKbn, ContentType.toEnum(formFile.getContentType()));
			String thumbFileName = WebdataFileUtils.createFileName(MTypeConstants.MaterialKbn.getThumbValue(form.hiddenMaterialKbn), ContentType.toEnum(formFile.getContentType()));

			try {
				WebdataFileUtils.createWebdataFile(dirPath, dirName.toString(), fileName, formFile.getFileData());
				WebdataFileUtils.createWebdataFile(dirPath, dirName.toString(), thumbFileName, thumbImage);
			} catch (ImageWriteErrorException e) {
				log.warn(e);
			} catch (FileNotFoundException e) {
				log.warn(e);
			} catch (IOException e) {
				log.warn(e);
			}

			// Mapの初期処理を行う
			form.initMaterialMap();
			// 素材をマップにセット
			form.materialMap.put(thumbMaterialKbn, thumbDto);

			// セッションに値を保持(キーは区分コード＋区分値)
			String sessionKey = MTypeConstants.MaterialKbn.TYPE_CD + thumbMaterialKbn;
			session.setAttribute(sessionKey, thumbDto);

			// FormFileを削除
			form.deleteFormFile(form.hiddenMaterialKbn);

		} catch (FileNotFoundException e) {
			form.deleteImgFile();
			log.warn(e.getMessage(), e);
			return MessageResourcesUtil.getMessage("errors.app.upload");
		} catch (IOException e) {
			form.deleteImgFile();
			log.warn(e.getMessage(), e);
			return MessageResourcesUtil.getMessage("errors.app.upload");
		}

		//hiddenをリセット
		form.hiddenMaterialKbn = null;
		form.deleteImgFile();
		return null;
	}

	/**
	 * 画像削除のメインロジック
	 */
	protected void doDeleteMaterial(WebdataForm form) {

		// フォームをリセット
		form.deleteMaterial(form.hiddenMaterialKbn);

		//hiddenをリセット
		form.hiddenMaterialKbn = null;
	}

	/**
	 * 画像イメージのバイトのデータを出力ストリームにセットする。
	 */
	protected void displayMaterialData(String targeMaterialKey, Map<String, MaterialDto> map) {

		if (map != null && map.get(targeMaterialKey) != null) {
			//Mapから画像データを取得して返却
			getMaterialData(response, targeMaterialKey, map);
		}
	}

	/**
	 * 素材データを取得します
	 * @param res HttpServletResponse
	 * @param materialKey Mapから取得するキー
	 * @param dtoMap 素材Dto
	 */
	protected void getMaterialData(HttpServletResponse res, String materialKey, Map<String, MaterialDto> map) {

		MaterialDto dto =  map.get(materialKey);

		// 素材データの取得
		byte[] materialData = dto.materialData;
		// コンテントタイプの取得
		String contentType = dto.contentType;

		// 値が取得できれば、書き出し処理を呼び出す
		if (materialData != null && materialData.length > 0 && !StringUtil.isEmpty(contentType)){
			outputMaterialStream(res, materialData, dto.contentType);
		}
	}

	/**
	 * 素材データをresponseのアウトプットストリームに書き出します
	 * @param res HttpServletResponse
	 * @param binArray 素材データ
	 * @param contentType コンテントタイプ
	 */
	private void outputMaterialStream(HttpServletResponse res, byte[] binArray, String contentType) {

		BufferedOutputStream bos = null;

		try {
			//コンテントタイプを指定
			res.setContentType(contentType);

			bos = new BufferedOutputStream(res.getOutputStream());
			bos.write(binArray);

			bos.flush();

		} catch (FileNotFoundException e) {
			log.warn(e.getMessage());

		} catch (IOException e) {
			log.warn(e.getMessage());

		} finally {
			try {
				bos.close();
			} catch (Exception e) {
				log.warn(e.getMessage());
			}
		}
	}

	/**
	 * Mapにファイル（byte[]データ）が保持されているかどうかをチェックする。
	 * @param materialKey
	 * @param materialMap
	 * @return 存在すれば true : 存在しなければfalse
	 */
	protected boolean isMaterialBytesExists(String materialKey, Map<String, MaterialDto> materialMap) {

		if (materialMap == null || materialMap.isEmpty()) {
			return false;
		}

		return materialMap.containsKey(materialKey);
	}


	/**
	 * 値をセットしたWEBデータ属性を、プロパティにセットします。
	 * @param property WEBデータプロパティ
	 * @param form WEBデータフォーム
	 */
	protected void settWebAttributeProperty(WebdataProperty property, WebdataForm form) {

		if (property == null || form == null) {
			callFraudulentProcessError(form);
		}

		// リストがnullの場合は初期化
		if (property.tWebAttributeList == null || property.tWebAttributeList.isEmpty()) {
			property.tWebAttributeList = new ArrayList<TWebAttribute>();
		}

		// 企業の特徴区分をリストにセット
		if (form.companyCharacteristicKbnList.size() != 0) {
			addTWebAttributeList(property, form.companyCharacteristicKbnList, MTypeConstants.CompanyCharacteristicKbn.TYPE_CD);
		}
		// 連載区分をリストにセット
		if (StringUtils.isNotBlank(form.serialPublicationKbn)) {
			addTWebAttributeList(property, form.serialPublicationKbn, MTypeConstants.SERIAL_PUBLICATION_KBN.TYPE_CD);
		}
	}

	/**
	 * プロパティにWEBデータ属性を設定します。
	 * @param property WEBデータプロパティ
	 * @param valueList 値のリスト
	 * @param typeCd 区分コード
	 */
	private void addTWebAttributeList(WebdataProperty property, List<String> valueList, String typeCd) {

		// 区分の値をプロパティにセット
		for (String value : valueList) {

			TWebAttribute tWebAttribute = new TWebAttribute();
			// 区分コード
			tWebAttribute.attributeCd = typeCd;
			// 区分値
			tWebAttribute.attributeValue = Integer.parseInt(value);
			// リストにセット
			property.tWebAttributeList.add(tWebAttribute);
		}
	}

	/**
	 * プロパティにWEBデータ属性を設定します。
	 * @param property WEBデータプロパティ
	 * @param valueList 値のリスト
	 * @param typeCd 区分コード
	 */
	private void addTWebAttributeList(WebdataProperty property, String value, String typeCd) {
		TWebAttribute tWebAttribute = new TWebAttribute();
		// 区分コード
		tWebAttribute.attributeCd = typeCd;
		// 区分値
		tWebAttribute.attributeValue = Integer.parseInt(value);
		// リストにセット
		property.tWebAttributeList.add(tWebAttribute);
	}

	/**
	 * 職種リストをプロパティにセット
	 * @param property
	 * @param form
	 */
	protected void setWebJobProperty(WebdataProperty property, WebdataForm form) {

		if (property == null || form == null) {
			callFraudulentProcessError(form);
		}

		property.tWebJobList = new ArrayList<TWebJob>();

		for(WebJobDto jobDto : form.webJobDtoList) {
			TWebJob webJob = Beans.createAndCopy(TWebJob.class, jobDto).execute();
			webJob.tWebJobAttributeList = new ArrayList<TWebJobAttribute>();
			webJob.tWebJobShopList = new ArrayList<TWebJobShopList>();
			// 待遇区分
			if (CollectionUtils.isNotEmpty(jobDto.treatmentKbnList)) {
				addTWebJobAttributeList(webJob, jobDto.treatmentKbnList, MTypeConstants.TreatmentKbn.TYPE_CD);
			}
			// その他条件区分
			if (CollectionUtils.isNotEmpty(jobDto.otherConditionKbnList)) {
				addTWebJobAttributeList(webJob, jobDto.otherConditionKbnList, MTypeConstants.OtherConditionKbn.TYPE_CD);
			}
			// 職種に紐づく店舗
			if (CollectionUtils.isNotEmpty(jobDto.shopIdList)) {
				addTWebJobShopList(webJob, jobDto.shopIdList);
			}
			property.tWebJobList.add(webJob);
		}
	}

	/**
	 * TwebJobエンティティに属性を設定します。
	 * @param webJob
	 * @param valueList 値のリスト
	 * @param typeCd 区分コード
	 */
	private void addTWebJobAttributeList(TWebJob webJob, List<String> valueList, String typeCd) {
		// 区分の値をプロパティにセット
		for (String value : valueList) {
			TWebJobAttribute tWebJobAttribute = new TWebJobAttribute();
			// 区分コード
			tWebJobAttribute.attributeCd = typeCd;
			// 区分値
			tWebJobAttribute.attributeValue = Integer.parseInt(value);
			// リストにセット
			webJob.tWebJobAttributeList.add(tWebJobAttribute);
		}
	}

	/**
	 * WEBデータ系列店舗をプロパティにセット
	 * @param property
	 * @param form
	 */
	protected void setWebShopListProperty(WebdataProperty property, WebdataForm form) {
		property.tWebShopListList = new ArrayList<>();
		for (String shopListId : form.shopListIdList) {
			TWebShopList tWebShopList = new TWebShopList();
			if(StringUtil.isEmpty(shopListId)) {
				continue;
			}
			tWebShopList.shopListId = Integer.parseInt(shopListId);
			// インディード出力店舗に指定されている場合
			if (form.shopListIdListForIndeed.contains(shopListId)) {
				tWebShopList.indeedFeedFlg = 1;
			} else {
				tWebShopList.indeedFeedFlg = 0;
			}
			property.tWebShopListList.add(tWebShopList);
		}
	}

	/**
	 * TwebJobエンティティに店舗を設定します。
	 * @param webJob
	 * @param shopIdList 店舗IDのリスト
	 */
	private void addTWebJobShopList(TWebJob webJob, List<String> shopIdList) {
		for (String shopId : shopIdList) {
			TWebJobShopList tWebJobShopList = new TWebJobShopList();
//			店舗ID
			tWebJobShopList.shopListId = Integer.parseInt(shopId);
			// リストにセット
			webJob.tWebJobShopList.add(tWebJobShopList);
		}
	}

	/**
	 * 値をセットしたWEBデータ特集エンティティリストをプロパティにセットする。
	 * @param property WEBデータプロパティ
	 * @param form WEBデータフォーム
	 */
	protected void setTWebSpecialProperty(WebdataProperty property, WebdataForm form) {

		// リストがnullの場合は初期化
		if (property.tWebSpecialList == null || property.tWebSpecialList.isEmpty()) {
			property.tWebSpecialList = new ArrayList<TWebSpecial>();
		}

		// 特集の値をリストセット
		for (String value : form.specialIdList) {

			TWebSpecial tWebSpecial = new TWebSpecial();
			// 特集ID
			tWebSpecial.specialId = Integer.parseInt(value);
			// リストにセット
			property.tWebSpecialList.add(tWebSpecial);
		}
	}

	/**
	 * 値をセットした素材エンティティを、<br />
	 * プロパティにセットします。
	 * @param property セットするプロパティ
	 * @param form WEBデータフォーム
	 */
	protected void setTMaterialProperty(WebdataProperty property, WebdataForm form) {

		StringBuilder dirName = new StringBuilder("");
		dirName.append(form.getIdForDirName());
		dirName.append("_");
		dirName.append(session.getId());
		property.idForDir = dirName.toString();
		property.webdataSessionImgdirPath = getCommonProperty("gc.webdata.imgUpload.dir.session");

		// 素材区分の区分値を取得
		List<MType> mtypeList = new ArrayList<MType>();

		// リストがnullの場合は初期化
		if (property.tMaterialList == null) {
			property.tMaterialList = new ArrayList<TMaterial>();
		}

		try {
			mtypeList = typeService.getMTypeList(MTypeConstants.MaterialKbn.TYPE_CD);
		} catch (WNoResultException e) {
			// データが取得できなければエラー
			callFraudulentProcessError(form);
		}

		// 素材区分に該当する値が登録されていれば、リストにセット
		//画像データ本体はロジックでセットするのでここではエンティティにセットしない
		for (MType mType : mtypeList) {

			//動画の場合は後続処理でセットするのでcontinue
			if (MTypeConstants.MaterialKbn.isMovie(String.valueOf(mType.typeValue))) {
				continue;
			}

			if (isMaterialBytesExists(String.valueOf(mType.typeValue), form.materialMap)) {

				// MapからDtoを取得
				MaterialDto dto = form.materialMap.get(String.valueOf(mType.typeValue));

				// エンティティに詰め替え
				TMaterial tMaterial = new TMaterial();
				// 素材データ以外をコピー
				Beans.copy(dto, tMaterial).excludes(toCamelCase(TMaterial.MATERIAL_DATA)).execute();

				// プロパティにセット
				property.tMaterialList.add(tMaterial);
			}
		}

		// WM動画名が入力されていれば
		 if (!StringUtil.isEmpty(form.wmMovieName)) {

				// エンティティに詰め替え
				TMaterial tMaterial = new TMaterial();

				// 素材区分
				tMaterial.materialKbn = Integer.parseInt(MTypeConstants.MaterialKbn.MOVIE_WM);
				// ファイル名
				tMaterial.fileName = form.wmMovieName;
				// コンテントタイプ
				tMaterial.contentType = GourmetCareeConstants.MEDIA_CONTENT_TYPE_WMV;

				// プロパティにセット
				property.tMaterialList.add(tMaterial);
		 }

		// QT動画名が入力されていれば
		 if (!StringUtil.isEmpty(form.qtMovieName)) {

				// エンティティに詰め替え
				TMaterial tMaterial = new TMaterial();
				// 素材区分
				tMaterial.materialKbn = Integer.parseInt(MTypeConstants.MaterialKbn.MOVIE_QT);
				// ファイル名
				tMaterial.fileName = form.qtMovieName;
				// コンテントタイプ
				tMaterial.contentType = GourmetCareeConstants.MEDIA_CONTENT_TYPE_QUICKTIME;

				// プロパティにセット
				property.tMaterialList.add(tMaterial);
		 }
	}

	/**
	 * 値をセットしたWEBデータ路線図エンティティを、<br />
	 * プロパティにセットします。
	 * @param property セットするプロパティ
	 * @param form WEBデータフォーム
	 */
	protected void setTWebRouteProperty(WebdataProperty property, WebdataForm form) {

		if (property == null || form == null) {
			callFraudulentProcessError(form);
		}

		// リストがnullの場合は初期化
		if (property.tWebRouteList == null) {
			property.tWebRouteList = new ArrayList<TWebRoute>();
		}

		// 路線図が入力されていれば、プロパティにセット
		if (!StringUtil.isEmpty(form.stationId)) {

			TWebRoute tWebRoute = new TWebRoute();

			tWebRoute.railroadId = Integer.parseInt(form.railroadId);
			tWebRoute.routeId = Integer.parseInt(form.routeId);
			tWebRoute.stationId = Integer.parseInt(form.stationId);

			property.tWebRouteList.add(tWebRoute);
		}

		// 追加された路線図をプロパティにセット
		for (WebRouteDto dto : form.webRouteList) {

			TWebRoute tWebRoute = new TWebRoute();
			Beans.copy(dto, tWebRoute).excludesWhitespace().execute();
			property.tWebRouteList.add(tWebRoute);
		}
	}

	/**
	 * WEBデータからデータを取得する
	 * @return WEBデータプロパティ
	 * @throws WNoResultException
	 */
	protected WebdataProperty getData(WebdataForm form) throws WNoResultException {

		// IDが正常かチェック
		checkId(form, form.id);

		// ロジック受け渡し用プロパティに値をセット
		WebdataProperty property = new WebdataProperty();

		// idをエンティティにセット
		property.tWeb = new TWeb();
		property.tWeb.id = Integer.parseInt(form.id);

		// データの取得
		webdataLogic.getWebdataDetailExcludesMatelial(property);

		// リストが空の場合はエラーメッセージを返す
		if (property == null || property.tWeb == null) {

			// 画面表示をしない
			form.setExistDataFlgNg();

			//「該当するデータが見つかりませんでした。」
			dataNotFoundMessage();
		}

		// 取得したリストを返却
		return property;
	}

	/**
	 * 代理店の場合、データにアクセス可能かチェックする<br />
	 * アクセス権限が無い場合、権限エラーへ遷移
	 * @param entity WEBデータエンティティ
	 * @param form WEBデータフォーム
	 */
	protected void checkAgencyAccess(TWeb entity, WebdataForm form) {

		// 代理店の場合
		if (ManageAuthLevel.AGENCY.value().equals(userDto.authLevel) &&
				!String.valueOf(entity.companyId).equals(userDto.companyId)) {

			// 権限エラー
			callAuthLevelError(form);
		}
	}

	/**
	 * 自社スタッフの場合、データにアクセス可能かチェックする<br />
	 * アクセス権限が無い場合、権限エラーへ遷移
	 * @param entity WEBデータエンティティ
	 * @param form WEBデータフォーム
	 */
	protected void checkStaffAccess(TWeb entity, WebdataForm form) {

		if (entity == null || form == null) {
			callFraudulentProcessError(form);
		}

		// 自社スタッフの場合のみ処理を実行
		if (!ManageAuthLevel.STAFF.value().equals(userDto.authLevel)) {
			return;
		}

		// ロジック受け渡し用プロパティに値をセット
		WebdataProperty property = new WebdataProperty();
		// エンティティにセット
		property.tWeb = entity;

		try {
			// 代理店データの場合はエラー
			if (webdataLogic.isAgencyCompany(property)) {
				// 権限エラー
				callAuthLevelError(form);
			}

		} catch (WNoResultException e) {
			// 権限エラー
			callAuthLevelError(form);
		}
	}

	/**
	 * 検索結果をFormに移し返すロジック
	 * @param entity WEBデータエンティティ
	 * @param form WEBデータフォーム
	 */
	protected void createDisplayValue(WebdataProperty property, WebdataForm form) {

		// 引数チェック
		if (property == null || property.tWeb == null || form == null) {
			callFraudulentProcessError(form);
		}

		TWeb tWebEntity =  property.tWeb;
		// エンティティから情報を取得してFormにセット
		Beans.copy(tWebEntity, form).execute();

		// 複数選択可能な項目をセット
		setWebAttribute(tWebEntity, form);
		// 特集をセット
		setWebSpecial(tWebEntity, form);
		// 路線図をセット
		setWebRoute(tWebEntity, form);
		// 素材の値をセット
		setMaterial(tWebEntity, form);

		// 顧客が登録されていた場合
		if (tWebEntity.customerId != null && property.mCustomer != null) {
			// 顧客の値をセット
			setCustomer(property.mCustomer, form);
		}

		// 系列店舗をセット
		if (CollectionUtils.isNotEmpty(property.tWebShopListList)) {
			List<Integer> shopListIdList = property.tWebShopListList.stream().map(s -> s.shopListId).collect(Collectors.toList());
			setShopList(shopListIdList, form);
			form.shopListIdListForIndeed = property.tWebShopListList.stream().filter(s -> s.indeedFeedFlg == 1).map(s -> String.valueOf(s.shopListId)).collect(Collectors.toList());
		}else {
			if (StringUtil.isNotBlank(form.customerDto.id)) {
				int custmerId = Integer.parseInt(form.customerDto.id);
				form.allShopCount = shopListService.countByCustomerId(custmerId);
			};
		}

		// 職種をセット
		setWebJob(property.tWebJobList, form);

		if(tWebEntity.attentionShopStartDate != null && tWebEntity.attentionShopEndDate != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			form.attentionShopStartDate = sdf.format(tWebEntity.attentionShopStartDate);
			form.attentionShopEndDate = sdf.format(tWebEntity.attentionShopEndDate);
		}

		if(tWebEntity.searchPreferentialStartDate != null && tWebEntity.searchPreferentialEndDate != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			form.searchPreferentialStartDate = sdf.format(tWebEntity.searchPreferentialStartDate);
			form.searchPreferentialEndDate = sdf.format(tWebEntity.searchPreferentialEndDate);
		}

		// チェックステータス名作成
		form.checkedStatusName = typeService.getTypeName(MTypeConstants.WebdataCheckedStatus.TYPE_CD, tWebEntity.checkedStatus);

		tWebEntity = null;
	}

	/**
	 * 確認画面の系列店舗をセット
	 * @param form
	 */
	protected void createConfShopList(WebdataForm form) {
		if (CollectionUtils.isEmpty(form.shopListIdList)) {
			form.shopListDtoList.clear();
			return;
		}

		List<Integer> shopListIdList = form.shopListIdList.stream().map(Integer::parseInt).collect(Collectors.toList());
		setShopList(shopListIdList, form);
	}

	/**
	 * IP電話データを作成します。
	 */
	protected void createIpPhoneData(TWeb web, WebdataForm form) {
		List<TWebIpPhone> entityList = webIpPhoneService.findByWebData(web);
		if (CollectionUtils.isEmpty(entityList)) {
			log.debug("IP情報は登録されていません。");
			return;
		}

		for (TWebIpPhone entity : entityList) {
			entity.ipTel = GourmetCareeUtil.devideIpPhoneNumber(entity.ipTel);
			form.addIpPhoneNumber(entity);
		}

	}

	/**
	 * WEBデータ属性の値をFormにセットします
	 * @param entity WEBデータエンティティ
	 * @param form WEBデータフォーム
	 */
	protected void setWebAttribute(TWeb entity, WebdataForm form) {

		// WEBデータ区分
		String webAreaCd = "";
		// 海外エリア区分
		String foreignAreaCd = "";
		// 主要駅区分
		String mainStationCd = "";
		// 勤務地詳細エリア区分
		String detailAreaCd = "";


		// エリアを判別して、区分値をセット
		if (MAreaConstants.AreaCd.SHUTOKEN_AREA.equals(entity.areaCd)) {
			webAreaCd = MTypeConstants.ShutokenWebAreaKbn.TYPE_CD;
			foreignAreaCd = MTypeConstants.ShutokenForeignAreaKbn.TYPE_CD;
			mainStationCd = MTypeConstants.ShutokenMainStationKbn.TYPE_CD;
			detailAreaCd = MTypeConstants.ShutokenDetailAreaKbn.TYPE_CD;
		} else if (MAreaConstants.AreaCd.SENDAI_AREA.equals(entity.areaCd)) {
			webAreaCd = MTypeConstants.SendaiWebAreaKbn.TYPE_CD;
			foreignAreaCd = "";
			detailAreaCd = MTypeConstants.SendaiDetailAreaKbn.TYPE_CD;
		}

		if (entity.tWebAttributeList == null || entity.tWebAttributeList.isEmpty()) {
			return;
		}

		// WEBデータ属性の値を詰め替える
		for (TWebAttribute tWebAttribute : entity.tWebAttributeList) {

			String cd = tWebAttribute.attributeCd;
			String value = String.valueOf(tWebAttribute.attributeValue);

			// 勤務地エリア区分をセット
			if (webAreaCd.equals(cd)) form.webAreaKbnList.add(value);

			// 海外エリア区分をセット
			else if (foreignAreaCd.equals(cd)) form.foreignAreaKbnList.add(value);

			// 勤務地詳細エリア区分をセット
			else if (detailAreaCd.equals(cd)) form.detailAreaKbnList.add(value);

			// 主要駅区分をセット
			else if (mainStationCd.equals(cd)) form.mainStationKbnList.add(value);

			// 雇用形態区分をセット
			else if (MTypeConstants.EmployPtnKbn.TYPE_CD.equals(cd)) form.employPtnKbnList.add(value);

			//職種検索条件区分をセット
			else if (MTypeConstants.JobKbn.TYPE_CD.equals(cd)) form.jobKbnList.add(value);

			// 待遇検索条件区分
			else if (MTypeConstants.TreatmentKbn.TYPE_CD.equals(cd)) form.treatmentKbnList.add(value);

			// 2連載区分をセット
			else if (MTypeConstants.SERIAL_PUBLICATION_KBN.TYPE_CD.equals(cd)) form.serialPublicationKbn = value;

			// その他条件区分
			else if (MTypeConstants.OtherConditionKbn.TYPE_CD.equals(cd)) form.otherConditionKbnList.add(value);

			// 企業特徴
			else if (MTypeConstants.CompanyCharacteristicKbn.TYPE_CD.equals(cd)) form.companyCharacteristicKbnList.add(value);

		}
	}

	/**
	 * WEBデータ特集の値をFormにセットします
	 * @param entity WEBデータエンティティ
	 * @param form WEBデータフォーム
	 */
	protected void setWebSpecial(TWeb entity, WebdataForm form) {

		if (entity.tWebSpecialList == null || entity.tWebSpecialList.isEmpty()) {
			return;
		}

		// WEBデータ特集の値を詰め替える
		for (TWebSpecial tWebSpecial : entity.tWebSpecialList) {
			form.specialIdList.add(String.valueOf(tWebSpecial.specialId));
		}
	}

	/**
	 * 系列店舗のDTOをセット
	 * @param list
	 * @param form
	 */
	private void setShopList(List<Integer> shopListIdList, WebdataForm form) {

		// 総件数をセット
		if (StringUtil.isNotBlank(form.customerDto.id)) {
			int custmerId = Integer.parseInt(form.customerDto.id);
			form.allShopCount = shopListService.countByCustomerId(custmerId);
		};

		if (CollectionUtils.isEmpty(shopListIdList)) {
			return;
		}
		// 店舗をDTOに変換してセット
		form.shopListIdList = shopListIdList.stream().map(String::valueOf).collect(Collectors.toList());
		form.shopListDtoList = shopListLogic.getWebShopListByIds(shopListIdList);

		form.shopListIdList = shopListIdList.stream().map(String::valueOf).collect(Collectors.toList());
	}

	/**
	 * WEB職種をセット
	 * @param list
	 * @param form
	 */
	private void setWebJob(List<TWebJob> list, WebdataForm form) {
		if (CollectionUtils.isEmpty(list)) {
			return;
		}
//		表示中の店舗IDリスト
        List<String> displayShopIdList = new ArrayList<>(0);
        if(CollectionUtils.isNotEmpty(form.shopListDtoList)) {
        	displayShopIdList = form.shopListDtoList.stream().map(ShopListDto -> ShopListDto.id.toString()).collect(Collectors.toList());
        }
		for (TWebJob webJob : list) {
			if(form.employJobKbnList.indexOf(String.valueOf(webJob.employPtnKbn) + '-' + String.valueOf(webJob.jobKbn)) == -1) {
				WebJobDto dto = Beans.createAndCopy(WebJobDto.class, webJob).execute();
//				職種に紐づく店舗をセット
				setWebJobShopList(webJob, dto, displayShopIdList);
				setWebJobAttribute(webJob, dto);
				form.webJobDtoList.add(dto);
				form.employJobKbnList.add(String.valueOf(webJob.employPtnKbn) + '-' + String.valueOf(webJob.jobKbn));
			}
		}
	}

	/**
	 * WEB職種属性をDTOにセット
	 * @param entity
	 * @param dto
	 */
	private void setWebJobAttribute(TWebJob entity, WebJobDto dto) {

		if (CollectionUtils.isEmpty(entity.tWebJobAttributeList)) {
			return;
		}
		// 属性の値を詰め替える
		for (TWebJobAttribute attr : entity.tWebJobAttributeList) {
			String cd = attr.attributeCd;
			String value = String.valueOf(attr.attributeValue);
			// 待遇検索条件区分
			if (MTypeConstants.TreatmentKbn.TYPE_CD.equals(cd)) dto.treatmentKbnList.add(value);
			// その他条件区分
			else if (MTypeConstants.OtherConditionKbn.TYPE_CD.equals(cd)) dto.otherConditionKbnList.add(value);
		}
	}

	/**
	 * WEB職種店舗をDTOにセット
	 * @param entity
	 * @param dto
	 * @param displayShopIdList
	 */
	private void setWebJobShopList(TWebJob entity, WebJobDto dto, List<String> displayShopIdList) {

		if (CollectionUtils.isEmpty(entity.tWebJobShopList)) {
			return;
		}
		// 店舗の値を詰め替える
		for (TWebJobShopList attr : entity.tWebJobShopList) {
//			表示中の店舗IDに含まれていれば職種店舗IDを追加する
			if(displayShopIdList.contains(attr.shopListId.toString())) {
				dto.shopIdList.add(attr.shopListId.toString());
			}
		}
	}


	/**
	 * 素材の値をFormにセットします
	 * @param entity WEBデータエンティティ
	 * @param form WEBデータフォーム
	 */
	private void setMaterial(TWeb entity, WebdataForm form) {

		String dirPath = getCommonProperty("gc.webdata.imgUpload.dir.session");
		StringBuilder dirName = new StringBuilder("");
		dirName.append(form.getIdForDirName());
		dirName.append("_");
		dirName.append(session.getId());

		List<IdSelectDto> materialList = materialService.getIdList(entity.id);

		for (IdSelectDto idSelectDto : materialList) {
			try {
				TMaterial tMaterial = materialService.findById(idSelectDto.id);

				MaterialDto dto = new MaterialDto();
				// 素材データ以外をコピー
				Beans.copy(tMaterial, dto).excludes(toCamelCase(TMaterial.MATERIAL_DATA)).execute();

				if (!MaterialKbn.MOVIE_WM.equals(Integer.toString(tMaterial.materialKbn))
						&& !MaterialKbn.MOVIE_QT.equals(Integer.toString(tMaterial.materialKbn))) {

					// 素材データが登録されていればセット
					if (tMaterial.materialData != null) {

						String fileName = WebdataFileUtils.createFileName(Integer.toString(tMaterial.materialKbn), ContentType.toEnum(tMaterial.contentType));

						try {
							WebdataFileUtils.createWebdataFile(dirPath, dirName.toString(), fileName, Base64Util.decode(tMaterial.materialData.replaceAll("\\n", "")));
						} catch (ImageWriteErrorException e) {
							log.warn(e);
						}
					}

				}
				// フォームにセット
				form.materialMap.put(String.valueOf(tMaterial.materialKbn), dto);

				// サムネイル区分の場合はsessionに保持する
				if (MTypeConstants.MaterialKbn.isThumbKbn(dto.materialKbn)) {
					// セッションに値を保持(キーは区分コード＋区分値)
					String sessionKey = MTypeConstants.MaterialKbn.TYPE_CD + dto.materialKbn;
					session.setAttribute(sessionKey, dto);
				}

				// WM動画が登録されている場合
				 if (MTypeConstants.MaterialKbn.MOVIE_WM.equals(String.valueOf(tMaterial.materialKbn))) {
					// ファイル名を登録
					 form.wmMovieName = tMaterial.fileName;
				 }

				// WM動画が登録されている場合
				 if (MTypeConstants.MaterialKbn.MOVIE_QT.equals(String.valueOf(tMaterial.materialKbn))) {
					// ファイル名を登録
					 form.qtMovieName = tMaterial.fileName;
				 }



				tMaterial = null;
			} catch (SNoResultException e) {
				// 主キーのループ中のため0件は想定されない。未処理とする。
			}
		}
	}

	/**
	 * WEBデータ路線図の値をFormにセットします
	 * @param entity WEBデータエンティティ
	 * @param form WEBデータフォーム
	 */
	protected void setWebRoute(TWeb entity, WebdataForm form) {

		if (entity.tWebRouteList == null || entity.tWebRouteList.isEmpty()) {
			return;
		}

		form.webRouteList = new ArrayList<WebRouteDto>();

		// WEBデータ路線図の値をDto詰め替える
		for (TWebRoute tWebRoute : entity.tWebRouteList) {

			WebRouteDto dto = new WebRouteDto();

			// 名称の取得
			RailroadDto railNameDto = railroadLogic.getRailroadDto(tWebRoute.stationId);
			// 取得できない場合はエラー
			if (railNameDto == null) {
				// 「{路線・最寄駅}の取得に失敗しました。」
				throw new ActionMessagesException("errors.selectData",
						MessageResourcesUtil.getMessage("labels.routeStation"));
			}

			Beans.copy(railNameDto, dto).execute();

			// IDをセット
			dto.railroadId = tWebRoute.railroadId;
			dto.routeId = tWebRoute.routeId;
			dto.stationId = tWebRoute.stationId;


			if (isDuplicatedWebRoute(dto, form.webRouteList)) {
				continue;
			}
			form.webRouteList.add(dto);
		}
	}

	private boolean isDuplicatedWebRoute(WebRouteDto dto, List<WebRouteDto> webRouteList) {
		for (WebRouteDto listDto : webRouteList) {
			if (GourmetCareeUtil.eqInt(dto.railroadId, listDto.railroadId)
					&& GourmetCareeUtil.eqInt(dto.routeId, listDto.routeId)
					&& GourmetCareeUtil.eqInt(dto.stationId, listDto.stationId)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 顧客マスタの値をFormにセットします
	 * @param entity 顧客マスタエンティティ
	 * @param form WEBデータフォーム
	 */
	protected void setCustomer(MCustomer entity, WebdataForm form) {

		// 顧客ロジック用にプロパティを用意
		CustomerProperty property = new CustomerProperty();
		property.mCustomer = entity;

		// 顧客マスタの値を詰め替える
		form.customerDto = customerLogic.convertSearchData(property);

	}

	/**
	 * セッションに登録された素材データを削除する
	 */
	protected void resetSessionImage() {

		// 素材区分の区分値を取得
		List<MType> mtypeList = new ArrayList<MType>();

		try {
			mtypeList = typeService.getMTypeList(MTypeConstants.MaterialKbn.TYPE_CD);
		} catch (WNoResultException e) {
			// データが取得できなければエラー
			callFraudulentProcessError(null);
		}

		// 素材が登録されたセッションをリセットする
		for (MType mType : mtypeList) {
				session.removeAttribute(MTypeConstants.MaterialKbn.TYPE_CD + mType.typeValue);
		}
	}

	/**
	 * 一括コピーできるかどうかチェック
	 * @param webId WEBデータID
	 */
	protected void checkEnableCopy(String[] webId) {

		WebdataProperty property = new WebdataProperty();
		property.webId = webId;

		try {
			if (!webdataLogic.isEnableLumpCopy(property)) {

				if (CollectionUtils.isNotEmpty(property.deleteWebId)) {
					throw new ActionMessagesException("errors.app.enableDeleteWebData",
							GourmetCareeUtil.createKanmaSpaceStr(property.deleteWebId));
				}

				if (ManageAuthLevel.STAFF.value().equals(userDto.authLevel)) {
					throw new ActionMessagesException("errors.app.dinableLumpCopyStaff",
							GourmetCareeUtil.createKanmaSpaceStr(property.failWebId));
				} else if (ManageAuthLevel.AGENCY.value().equals(userDto.authLevel)) {
					throw new ActionMessagesException("errors.app.dinableLumpCopyAgency",
							GourmetCareeUtil.createKanmaSpaceStr(property.failWebId));
				}
			}

		} catch (NumberFormatException e) {
			throw new ActionMessagesException("errors.app.failGetWebId");
		}
	}

	/**
	 * 一括確定できるデータかどうかチェック
	 * @param webId WEBデータID
	 */
	protected boolean checkEnableDecide(String[] webId) {

		WebdataProperty property = new WebdataProperty();
		property.webId = webId;

		try {

			// エラー情報
			ActionMessages errors = new ActionMessages();

			webdataLogic.checkEnableLumpDecide(property);

			// 削除されているデータがある場合、エラーメッセージ追加
			if (CollectionUtils.isNotEmpty(property.deleteWebId)) {
				errors.add("errors", new ActionMessage("errors.app.dinableLumpDecideNoExistData",
						 GourmetCareeUtil.createKanmaSpaceStr(property.deleteWebId)));
			}

			// 担当会社が存在していないデータがある場合、エラーメッセージ追加
			if (CollectionUtils.isNotEmpty(property.noExistCompanyWebIdList)) {
				errors.add("errors", new ActionMessage("errors.app.dinableLumpDecideNoExistDataAttr",
						 GourmetCareeUtil.createKanmaSpaceStr(property.noExistCompanyWebIdList),
								 MessageResourcesUtil.getMessage("labels.assignedCompanyId")));
			}

			// 営業担当者が存在していないデータがある場合、エラーメッセージ追加
			if (CollectionUtils.isNotEmpty(property.noExistSalesWebIdList)) {
				errors.add("errors", new ActionMessage("errors.app.dinableLumpDecideNoExistDataAttr",
						 GourmetCareeUtil.createKanmaSpaceStr(property.noExistSalesWebIdList),
								 MessageResourcesUtil.getMessage("labels.assignedSalesId")));
			}

			// 顧客が存在していないデータがある場合、エラーメッセージ追加
			if (CollectionUtils.isNotEmpty(property.noExistCustomerWebIdList)) {
				errors.add("errors", new ActionMessage("errors.app.dinableLumpDecideNoExistDataAttr",
						 GourmetCareeUtil.createKanmaSpaceStr(property.noExistCustomerWebIdList),
								 MessageResourcesUtil.getMessage("msg.app.customer")));
			}

			// 号数が登録されていないデータがある場合、エラーメッセージ追加
			if (CollectionUtils.isNotEmpty(property.noRegistVolumeWebIdList)) {
				errors.add("errors", new ActionMessage("errors.app.dinableLumpDecideNotRegistData",
						 GourmetCareeUtil.createKanmaSpaceStr(property.noRegistVolumeWebIdList),
								 MessageResourcesUtil.getMessage("labels.volumeId")));
			}

			// 号数が存在していないデータがある場合、エラーメッセージ追加
			if (CollectionUtils.isNotEmpty(property.noExistVolumeWebIdList)) {
				errors.add("errors", new ActionMessage("errors.app.dinableLumpDecideNoExistDataAttr",
						GourmetCareeUtil.createKanmaSpaceStr(property.noExistVolumeWebIdList),
						MessageResourcesUtil.getMessage("labels.volumeId")));
			}

			// 管理者の場合
			if (ManageAuthLevel.ADMIN.value().equals(userDto.authLevel)) {
				// ステータスが不正なデータがある場合、エラーメッセージ追加
				if (CollectionUtils.isNotEmpty(property.failStatusWebIdList)) {
					errors.add("errors", new ActionMessage("errors.app.dinableLumpDecideFailStatusAdmin",
							 GourmetCareeUtil.createKanmaSpaceStr(property.failStatusWebIdList),
									 MessageResourcesUtil.getMessage("labels.draft"),
									 MessageResourcesUtil.getMessage("labels.reqApproval")));
				}
			} else if (ManageAuthLevel.STAFF.value().equals(userDto.authLevel)) {

				// 代理店データが存在する場合、エラーメッセージ追加
				if (CollectionUtils.isNotEmpty(property.failWebId)) {
					errors.add("errors", new ActionMessage("errors.app.dinableLumpDecideStaff",
							 GourmetCareeUtil.createKanmaSpaceStr(property.failWebId),
									 MessageResourcesUtil.getMessage("labels.draft")));
				}

				// ステータスが不正なデータがある場合、エラーメッセージ追加
				if (CollectionUtils.isNotEmpty(property.failStatusWebIdList)) {
					errors.add("errors", new ActionMessage("errors.app.dinableLumpDecideFailStatusStaff",
							 GourmetCareeUtil.createKanmaSpaceStr(property.failStatusWebIdList),
									 MessageResourcesUtil.getMessage("labels.draft")));
				}
				// 確定締切日時を超えているデータがある場合、エラーメッセージ追加
				if (CollectionUtils.isNotEmpty(property.failFixedDatetimeWebIdList)) {
					errors.add("errors", new ActionMessage("errors.app.dinableLumpDecideOverFixedDeadLine",
							 GourmetCareeUtil.createKanmaSpaceStr(property.failFixedDatetimeWebIdList)));
				}
			}

			ActionMessagesUtil.addErrors(request, errors);

			return errors.isEmpty() ? true : false;

		} catch (NumberFormatException e) {
			throw new ActionMessagesException("errors.app.failGetWebId");
		}
	}

	/**
	 * 一括削除できるデータかどうかチェック
	 * @param webId WEBデータID
	 * @return
	 */
	protected boolean checkEnableDelete(String[] webId) {

		WebdataProperty property = new WebdataProperty();
		property.webId = webId;

		try {
			// エラー情報
			ActionMessages errors = new ActionMessages();

			webdataLogic.checkEnableLumpDelete(property);

			// 削除されているデータがある場合、エラーメッセージ追加
			if (CollectionUtils.isNotEmpty(property.deleteWebId)) {
				errors.add("errors", new ActionMessage("errors.app.dinableLumpDeleteAlreadyDelete",
						 GourmetCareeUtil.createKanmaSpaceStr(property.deleteWebId)));
			}

			ActionMessagesUtil.addErrors(request, errors);

			return errors.isEmpty() ? true : false;
		} catch (NumberFormatException e) {
			throw new ActionMessagesException("errors.app.failGetWebId");
		}
	}



	/**
	 * 入力された文字をチェックします。
	 * @param baseForm
	 */
	protected boolean checkInputStr(WebdataForm baseForm) {
		int sizeKbn = NumberUtils.toInt(baseForm.sizeKbn);
		ActionMessages errors = new ActionMessages();

		// 職種の文字数チェック
		Map<Integer, String> employPtnMap = new HashMap<>();
		Map<Integer, String> jobKbnMap = new HashMap<>();
		try {
			employPtnMap = typeService.getMTypeValueMap(MTypeConstants.EmployPtnKbn.TYPE_CD);
			jobKbnMap = typeService.getMTypeValueMap(MTypeConstants.JobKbn.TYPE_CD);
		// 取得できるため入らない
		} catch (WNoResultException e) {}

		for (WebJobDto dto : baseForm.webJobDtoList) {
			if(StringUtil.isNotEmpty(dto.personHunting) && isOverStrLength(dto.personHunting, getCommonProperty("gc.webdata.personHunting.maxLength"))) {
				errors.add("errors", new ActionMessage("errors.overLimitStr",
						employPtnMap.get(Integer.parseInt(dto.employPtnKbn)) + "　" +
								jobKbnMap.get(Integer.parseInt(dto.jobKbn)) + " の " +
						MessageResourcesUtil.getMessage("labels.personHunting"),
						getCommonProperty("gc.webdata.personHunting.maxLength")));
			}
		}

		if (StringUtils.isNotBlank(baseForm.sentence1)) {
			if (isOverStrLength(baseForm.sentence1, getCommonProperty("gc.webdata.sentence1.maxLength" + sizeKbn))) {
				errors.add("errors", new ActionMessage("errors.overLimitStr",
						MessageResourcesUtil.getMessage("labels.sentence1"),
						getCommonProperty("gc.webdata.sentence1.maxLength" + sizeKbn)));
			}
		}

		if (StringUtils.isNotBlank(baseForm.sentence2)) {
			if (sizeKbn == SizeKbn.C || sizeKbn == SizeKbn.D || sizeKbn == SizeKbn.E) {

				if (isOverStrLength(baseForm.sentence2, getCommonProperty("gc.webdata.sentence2.maxLength" + sizeKbn))) {
					errors.add("errors", new ActionMessage("errors.overLimitStr",
							MessageResourcesUtil.getMessage("labels.sentence2"),
							getCommonProperty("gc.webdata.sentence2.maxLength" + sizeKbn)));
				}
			}
		}

		if (StringUtils.isNotBlank(baseForm.sentence3)) {
			if (sizeKbn == SizeKbn.D || sizeKbn == SizeKbn.E) {

				if (isOverStrLength(baseForm.sentence3, getCommonProperty("gc.webdata.sentence3.maxLength"))) {
					errors.add("errors", new ActionMessage("errors.overLimitStr",
							MessageResourcesUtil.getMessage("labels.sentence3"),
							getCommonProperty("gc.webdata.sentence3.maxLength")));
				}
			}
		}

		if (StringUtils.isNotBlank(baseForm.captionA)) {
			if (sizeKbn == SizeKbn.C || sizeKbn == SizeKbn.D || sizeKbn == SizeKbn.E) {

				if (isOverStrLength(baseForm.captionA, getCommonProperty("gc.webdata.caption.maxLength"))) {
					errors.add("errors", new ActionMessage("errors.overLimitStr",
							MessageResourcesUtil.getMessage("labels.captionA"),
							getCommonProperty("gc.webdata.caption.maxLength")));
				}
			}
		}

		if (StringUtils.isNotBlank(baseForm.captionB)) {
			if (sizeKbn == SizeKbn.C || sizeKbn == SizeKbn.D || sizeKbn == SizeKbn.E) {

				if (isOverStrLength(baseForm.captionB, getCommonProperty("gc.webdata.caption.maxLength"))) {
					errors.add("errors", new ActionMessage("errors.overLimitStr",
							MessageResourcesUtil.getMessage("labels.captionB"),
							getCommonProperty("gc.webdata.caption.maxLength")));
				}
			}
		}

		if (StringUtils.isNotBlank(baseForm.captionC)) {
			if (sizeKbn == SizeKbn.C || sizeKbn == SizeKbn.D || sizeKbn == SizeKbn.E) {

				if (isOverStrLength(baseForm.captionC, getCommonProperty("gc.webdata.caption.maxLength"))) {
					errors.add("errors", new ActionMessage("errors.overLimitStr",
							MessageResourcesUtil.getMessage("labels.captionC"),
							getCommonProperty("gc.webdata.caption.maxLength")));
				}
			}
		}

		if (StringUtils.isNotBlank(baseForm.attentionHereSentence)) {
			if (sizeKbn != SizeKbn.A && sizeKbn != SizeKbn.TEXT_WEB) {

				if (isOverStrLength(baseForm.attentionHereSentence, getCommonProperty("gc.webdata.attentionHere.maxLength"))) {
					errors.add("errors", new ActionMessage("errors.overLimitStr",
							MessageResourcesUtil.getMessage("labels.attentionHereSentence"),
							getCommonProperty("gc.webdata.attentionHere.maxLength")));
				}
			}
		}

		ActionMessagesUtil.addErrors(request, errors);

		return errors.isEmpty() ? true : false;
	}


	/**
	 * 一括確定用バリデート
	 * バリデート内容は、checkInputStr()と同様。
	 * @param entity
	 */
	protected boolean validateLumpDecide(TWeb entity, int sizeKbn) {
		ActionMessages errors = new ActionMessages();
		String webdataIdLabel = MessageResourcesUtil.getMessageResources().getMessage("msg.webdataId", entity.id);



		if (StringUtils.isNotBlank(entity.sentence1)) {
			if (isOverStrLength(entity.sentence1, getCommonProperty("gc.webdata.sentence1.maxLength" + sizeKbn))) {
				errors.add("errors", new ActionMessage("errors.overLimitStr",
						webdataIdLabel + MessageResourcesUtil.getMessage("labels.sentence1"),
						getCommonProperty("gc.webdata.sentence1.maxLength" + sizeKbn)));
			}
		}

		if (StringUtils.isNotBlank(entity.sentence2)) {
			if (sizeKbn == SizeKbn.C || sizeKbn == SizeKbn.D || sizeKbn == SizeKbn.E) {

				if (isOverStrLength(entity.sentence2, getCommonProperty("gc.webdata.sentence2.maxLength" + sizeKbn))) {
					errors.add("errors", new ActionMessage("errors.overLimitStr",
							webdataIdLabel + MessageResourcesUtil.getMessage("labels.sentence2"),
							getCommonProperty("gc.webdata.sentence2.maxLength" + sizeKbn)));
				}
			}
		}

		if (StringUtils.isNotBlank(entity.sentence3)) {
			if (sizeKbn == SizeKbn.D || sizeKbn == SizeKbn.E) {

				if (isOverStrLength(entity.sentence3, getCommonProperty("gc.webdata.sentence3.maxLength"))) {
					errors.add("errors", new ActionMessage("errors.overLimitStr",
							webdataIdLabel + MessageResourcesUtil.getMessage("labels.sentence3"),
							getCommonProperty("gc.webdata.sentence3.maxLength")));
				}
			}
		}

		if (StringUtils.isNotBlank(entity.captiona)) {
			if (sizeKbn == SizeKbn.C || sizeKbn == SizeKbn.D || sizeKbn == SizeKbn.E) {

				if (isOverStrLength(entity.captiona, getCommonProperty("gc.webdata.caption.maxLength"))) {
					errors.add("errors", new ActionMessage("errors.overLimitStr",
							webdataIdLabel + MessageResourcesUtil.getMessage("labels.captionA"),
							getCommonProperty("gc.webdata.caption.maxLength")));
				}
			}
		}

		if (StringUtils.isNotBlank(entity.captionb)) {
			if (sizeKbn == SizeKbn.C || sizeKbn == SizeKbn.D || sizeKbn == SizeKbn.E) {

				if (isOverStrLength(entity.captionb, getCommonProperty("gc.webdata.caption.maxLength"))) {
					errors.add("errors", new ActionMessage("errors.overLimitStr",
							webdataIdLabel + MessageResourcesUtil.getMessage("labels.captionB"),
							getCommonProperty("gc.webdata.caption.maxLength")));
				}
			}
		}

		if (StringUtils.isNotBlank(entity.captionc)) {
			if (sizeKbn == SizeKbn.C || sizeKbn == SizeKbn.D || sizeKbn == SizeKbn.E) {

				if (isOverStrLength(entity.captionc, getCommonProperty("gc.webdata.caption.maxLength"))) {
					errors.add("errors", new ActionMessage("errors.overLimitStr",
							webdataIdLabel + MessageResourcesUtil.getMessage("labels.captionC"),
							getCommonProperty("gc.webdata.caption.maxLength")));
				}
			}
		}

		if (StringUtils.isNotBlank(entity.attentionHereSentence)) {
			if (sizeKbn != SizeKbn.A && sizeKbn != SizeKbn.TEXT_WEB) {

				if (isOverStrLength(entity.attentionHereSentence, getCommonProperty("gc.webdata.attentionHere.maxLength"))) {
					errors.add("errors", new ActionMessage("errors.overLimitStr",
							webdataIdLabel + MessageResourcesUtil.getMessage("labels.attentionHereSentence"),
							getCommonProperty("gc.webdata.attentionHere.maxLength")));
				}
			}
		}

		ActionMessagesUtil.addErrors(request, errors);

		return errors.isEmpty() ? true : false;
	}


	/**
	 * 掲載確定用バリデート
	 * バリデート内容は、checkInputStr()と同様。
	 * @param entity
	 */
	protected boolean validateDecide(TWeb entity) {
		ActionMessages errors = new ActionMessages();



		if (StringUtils.isNotBlank(entity.sentence1)) {
			if (isOverStrLength(entity.sentence1, getCommonProperty("gc.webdata.sentence1.maxLength" + entity.sizeKbn))) {
				errors.add("errors", new ActionMessage("errors.overLimitStr",
						MessageResourcesUtil.getMessage("labels.sentence1"),
						getCommonProperty("gc.webdata.sentence1.maxLength" + entity.sizeKbn)));
			}
		}

		if (StringUtils.isNotBlank(entity.sentence2)) {
			if (entity.sizeKbn == SizeKbn.C || entity.sizeKbn == SizeKbn.D || entity.sizeKbn == SizeKbn.E) {

				if (isOverStrLength(entity.sentence2, getCommonProperty("gc.webdata.sentence2.maxLength" + entity.sizeKbn))) {
					errors.add("errors", new ActionMessage("errors.overLimitStr",
							MessageResourcesUtil.getMessage("labels.sentence2"),
							getCommonProperty("gc.webdata.sentence2.maxLength" + entity.sizeKbn)));
				}
			}
		}

		if (StringUtils.isNotBlank(entity.sentence3)) {
			if (entity.sizeKbn == SizeKbn.D || entity.sizeKbn == SizeKbn.E) {

				if (isOverStrLength(entity.sentence3, getCommonProperty("gc.webdata.sentence3.maxLength"))) {
					errors.add("errors", new ActionMessage("errors.overLimitStr",
							MessageResourcesUtil.getMessage("labels.sentence3"),
							getCommonProperty("gc.webdata.sentence3.maxLength")));
				}
			}
		}

		if (StringUtils.isNotBlank(entity.captiona)) {
			if (entity.sizeKbn == SizeKbn.C || entity.sizeKbn == SizeKbn.D || entity.sizeKbn == SizeKbn.E) {

				if (isOverStrLength(entity.captiona, getCommonProperty("gc.webdata.caption.maxLength"))) {
					errors.add("errors", new ActionMessage("errors.overLimitStr",
							MessageResourcesUtil.getMessage("labels.captionA"),
							getCommonProperty("gc.webdata.caption.maxLength")));
				}
			}
		}

		if (StringUtils.isNotBlank(entity.captionb)) {
			if (entity.sizeKbn == SizeKbn.C || entity.sizeKbn == SizeKbn.D || entity.sizeKbn == SizeKbn.E) {

				if (isOverStrLength(entity.captionb, getCommonProperty("gc.webdata.caption.maxLength"))) {
					errors.add("errors", new ActionMessage("errors.overLimitStr",
							MessageResourcesUtil.getMessage("labels.captionB"),
							getCommonProperty("gc.webdata.caption.maxLength")));
				}
			}
		}

		if (StringUtils.isNotBlank(entity.captionc)) {
			if (entity.sizeKbn == SizeKbn.C || entity.sizeKbn == SizeKbn.D || entity.sizeKbn == SizeKbn.E) {

				if (isOverStrLength(entity.captionc, getCommonProperty("gc.webdata.caption.maxLength"))) {
					errors.add("errors", new ActionMessage("errors.overLimitStr",
							MessageResourcesUtil.getMessage("labels.captionC"),
							getCommonProperty("gc.webdata.caption.maxLength")));
				}
			}
		}

		if (StringUtils.isNotBlank(entity.attentionHereSentence)) {
			if (entity.sizeKbn != SizeKbn.A && entity.sizeKbn != SizeKbn.TEXT_WEB) {

				if (isOverStrLength(entity.attentionHereSentence, getCommonProperty("gc.webdata.attentionHere.maxLength"))) {
					errors.add("errors", new ActionMessage("errors.overLimitStr",
							MessageResourcesUtil.getMessage("labels.attentionHereSentence"),
							getCommonProperty("gc.webdata.attentionHere.maxLength")));
				}
			}
		}

		ActionMessagesUtil.addErrors(request, errors);

		return errors.isEmpty() ? true : false;
	}

	/**
	 * 行ごとの文字数を超えているかをチェックします。
	 * @param targetStr
	 * @param maxCount
	 * @return
	 */
	private boolean isOverStrLength(String targetStr, String maxCount) {
		if (targetStr.length() > NumberUtils.toInt(maxCount)) {
			return true;
		}
		return false;
	}

	/**
	 * 一括確定ができるかデータをチェックします。
	 * @param dtoList
	 * @return
	 */
	protected boolean checkLumpDecideData(List<ListDto> dtoList) {
		boolean errorFlg = false;
		try {

			for (ListDto dto : dtoList) {
				TWeb entity = webService.findById(Integer.parseInt(dto.id));
				if (validateLumpDecide(entity, NumberUtils.toInt(dto.sizeKbn))) {
					errorFlg = true;
				}
			}

		} catch (NumberFormatException e) {
			throw new ActionMessagesException("errors.app.failGetWebId");
		} catch (SNoResultException e) {
			throw new ActionMessagesException("errors.app.dataNotFound");
		}

		return errorFlg;
	}


	/**
	 * 公開側のキャッシュイメージを作成します。
	 *
	 * @throws IOException
	 */
	protected void createFrontImage(List<TMaterial> tMaterialList) {
		// 画像がなければ何もしない。
		if (tMaterialList == null || tMaterialList.isEmpty()) {
			return;
		}

		for (TMaterial entity : tMaterialList) {

			// 素材区分がnull、サムネイル、動画の場合は処理しない。
			if (entity.materialKbn == null
					|| MTypeConstants.MaterialKbn.isThumbKbn(String
							.valueOf(entity.materialKbn))
					|| MTypeConstants.MaterialKbn.isMovie(String
							.valueOf(entity.materialKbn))) {
				continue;
			}

			try {
				createFrontImage(entity);
			} catch (IOException e) {
				log.info("公開側画像のキャッシュに失敗しました。 materialID:" + entity.id);
			}
		}
	}

	/**
	 * 公開側のキャッシュイメージを作成します。
	 *
	 * @throws IOException
	 */
	private void createFrontImage(TMaterial entity) throws IOException {
		byte[] materialData = Base64Util.decode(entity.materialData.replaceAll(
				"\\n", ""));
		String uniqueKey = GourmetCareeUtil
				.createUniqueKey(entity.insertDatetime);
		cachePcAndSmartImage(entity.webId, materialData, entity.materialKbn,
				uniqueKey);
		cacheMobileImage(entity.webId, materialData, entity.materialKbn,
				uniqueKey, entity.contentType);
	}

	/**
	 * 画像をキャッシュします。
	 *
	 * @param fileName
	 * @param materialData
	 */
	private void cacheImage(String fileName, byte[] materialData)
			throws IOException {
		BufferedOutputStream bos = null;
		try {
			File file = new File(fileName);
			try {
				file.createNewFile();
			} catch (IOException e) {
				log.warn("キャッシュ用画像ファイルのcreateNewFile()で例外が発生しました。"
						+ e.getMessage());
				throw e;
			}

			bos = new BufferedOutputStream(new FileOutputStream(file));
			bos.write(materialData);
			bos.flush();
		} catch (FileNotFoundException e) {
			log.warn("キャッシュ用画像ファイルの生成時に例外FileNotFoundExceptionが発生しました。"
					+ e.getMessage());
		} catch (IOException e) {
			log.warn("キャッシュ用画像ファイルの生成時に例外が発生しました。" + e.getMessage());
		} finally {
			try {
				if (bos != null) {
					bos.close();
				}
			} catch (IOException e) {
				log.warn("BufferedOutputStreamのclose()でに例外が発生しました。"
						+ e.getMessage());
			}
		}
	}

	/**
	 * 携帯のキャッシュイメージを作成します。
	 *
	 * @param webId
	 * @param materialData
	 * @param materialKbn
	 * @param uniqueKey
	 * @param contentType
	 * @throws IOException
	 */
	private void cacheMobileImage(int webId, byte[] materialData,
			int materialKbn, String uniqueKey, String contentType)
			throws IOException {
		cacheImage(createMobileCachePath(webId, materialKbn, uniqueKey),
				getSmallImage(materialData, contentType, materialKbn));
	}

	/**
	 * PC、スマホのキャッシュイメージを作成します。
	 *
	 * @param webId
	 * @param materialData
	 * @param materialKbn
	 * @param uniqueKey
	 * @throws IOException
	 */
	private void cachePcAndSmartImage(int webId, byte[] materialData,
			int materialKbn, String uniqueKey) throws IOException {
		cacheImage(createPcCachePath(webId, materialKbn, uniqueKey),
				materialData);
		cacheImage(createSmartCachePath(webId, materialKbn, uniqueKey),
				materialData);
	}

	/**
	 * 画像を縮小してbyte[]で返します。
	 *
	 * @param imageByte
	 * @param contentType
	 * @param materialKbn
	 * @return
	 * @throws IOException
	 */
	public byte[] getSmallImage(byte[] imageByte, String contentType,
			int materialKbn) throws IOException {

		byte[] thumbImage = null;
		// 縮小比率をプロパティから取得
		float xScale = NumberUtils.toFloat(getCommonProperty("gc.marerial.materialKbn" + materialKbn), GourmetCareeConstants.DEFAULT_PERCENTAGE);
		float yScale = xScale;

		// 圧縮しないサイズであれば処理しない。
		if (xScale == 1.0F) {
			return imageByte;
		}

		// イメージオブジェクトの生成
		Image image = Image.getInstance(imageByte);

		// 画像がjpgの時にサムネイル画像を作成する
		if (GourmetCareeConstants.MEDIA_CONTENT_TYPE_JPEG.equals(contentType)
				|| GourmetCareeConstants.MEDIA_CONTENT_TYPE_PJPEG
						.equals(contentType)) {

			// 画質をプロパティから取得
			float quality = NumberUtils.toFloat(getCommonProperty("gc.front.marerial.quality"), NumberUtils.FLOAT_ONE);

			// 画像の縮小
			thumbImage = image
					.addFilter(
							new ResizeFilter().setScale(xScale, yScale)
									.setInterpolation(
											ResizeFilter.INTERP_BILINEAR))
					.executeFilter()
					.encode(new JpegEncoder().setQuality(quality));
		}

		return thumbImage;
	}

	/**
	 * PCの公開イメージキャッシュディレクトリを作成します。
	 * @param webId
	 * @param materialKbn
	 * @param uniqueKey
	 * @return
	 */
	private String createPcCachePath(int webId, int materialKbn,
			String uniqueKey) {
		StringBuilder sb = new StringBuilder("");
		sb.append(getCommonProperty("gc.front.cache.image.pc.dir.path"));
		sb.append(webId);
		sb.append("_");
		sb.append(materialKbn);
		sb.append("_");
		sb.append(uniqueKey);
		sb.append(".jpg");

		return sb.toString();
	}

	/**
	 * 携帯の公開イメージキャッシュディレクトリを作成します。
	 * @param webId
	 * @param materialKbn
	 * @param uniqueKey
	 * @return
	 */
	private String createMobileCachePath(int webId, int materialKbn,
			String uniqueKey) {
		StringBuilder sb = new StringBuilder("");
		sb.append(getCommonProperty("gc.front.cache.image.mobile.dir.path"));
		sb.append(webId);
		sb.append("_");
		sb.append(materialKbn);
		sb.append("_");
		sb.append(uniqueKey);
		sb.append(".jpg");

		return sb.toString();
	}

	/**
	 * スマホの公開イメージキャッシュディレクトリを作成します。
	 * @param webId
	 * @param materialKbn
	 * @param uniqueKey
	 * @return
	 */
	private String createSmartCachePath(int webId, int materialKbn,
			String uniqueKey) {
		StringBuilder sb = new StringBuilder("");
		sb.append(getCommonProperty("gc.front.cache.image.smart.dir.path"));
		sb.append(webId);
		sb.append("_");
		sb.append(materialKbn);
		sb.append("_");
		sb.append(uniqueKey);
		sb.append(".jpg");

		return sb.toString();
	}

	/**
	 * WEBデータ用のよく使うタグを作成する
	 * @return
	 */
	protected List<TagListDto> createTagList() {
		return tagListLogic.getCommonlyUsedFindByWebTagOrderByCount();
	}

	/**
	 * WEBデータに登録されたタグの一覧を取得する
	 * @param webDataId
	 * @return
	 */
	protected void getWebDataTag(Integer webDataId, WebdataForm form) {

		List<MWebTag> webDataTags = tagListLogic.findByWebDataId(webDataId);
		if (CollectionUtils.isEmpty(webDataTags)) {
			form.tagList = null;
		}

		TagListDto dto = new TagListDto();
		webDataTags.stream().forEach((entity) -> {
			dto.tagNameList.add(entity.getWebTagName());
		});

		form.tagList = String.join(",", dto.tagNameList);
	}

	/**
	 * 一時プレビュー
	 * @param editForm
	 * @return
	 */
	protected String tempPreview(WebdataForm editForm) {
		TempWebdataProperty property = new TempWebdataProperty();
		Beans.copy(editForm, property).execute();

		// セッションIDを保存
		property.sessionId = session.getId();
		property.customerId = editForm.customerDto.id;

		// 職種のコピー
		property.webJobDtoList = new ArrayList<>();
		for (WebJobDto webJobDto : editForm.webJobDtoList) {

			// 掲載可のみにする
			if (MTypeConstants.PublicationFlg.OK != NumberUtils.toInt(webJobDto.publicationFlg)) {
				continue;
			}

			TempWebJobPropterty tempWebJobPropterty = new TempWebJobPropterty();
			if (StringUtils.isNotBlank(webJobDto.jobKbn)) tempWebJobPropterty.job_kbn = webJobDto.jobKbn;
			if (StringUtils.isNotBlank(webJobDto.publicationFlg)) tempWebJobPropterty.publication_flg = webJobDto.publicationFlg;
			if (StringUtils.isNotBlank(webJobDto.employPtnKbn)) tempWebJobPropterty.employ_ptn_kbn = webJobDto.employPtnKbn;
			if (StringUtils.isNotBlank(webJobDto.workContents)) tempWebJobPropterty.work_contents = webJobDto.workContents;
			if (StringUtils.isNotBlank(webJobDto.saleryStructureKbn)) tempWebJobPropterty.salery_structure_kbn = webJobDto.saleryStructureKbn;
			if (StringUtils.isNotBlank(webJobDto.lowerSalaryPrice)) tempWebJobPropterty.lower_salary_price = webJobDto.lowerSalaryPrice;
			if (StringUtils.isNotBlank(webJobDto.upperSalaryPrice)) tempWebJobPropterty.upper_salary_price = webJobDto.upperSalaryPrice;
			if (StringUtils.isNotBlank(webJobDto.salary)) tempWebJobPropterty.salary = webJobDto.salary;
			if (StringUtils.isNotBlank(webJobDto.salaryDetail)) tempWebJobPropterty.salary_detail = webJobDto.salaryDetail;
			if (StringUtils.isNotBlank(webJobDto.annualLowerSalaryPrice)) tempWebJobPropterty.annual_lower_salary_price = webJobDto.annualLowerSalaryPrice;
			if (StringUtils.isNotBlank(webJobDto.annualUpperSalaryPrice)) tempWebJobPropterty.annual_upper_salary_price = webJobDto.annualUpperSalaryPrice;
			if (StringUtils.isNotBlank(webJobDto.annualSalary)) tempWebJobPropterty.annual_salary = webJobDto.annualSalary;
			if (StringUtils.isNotBlank(webJobDto.annualSalaryDetail)) tempWebJobPropterty.annual_salary_detail = webJobDto.annualSalaryDetail;
			if (StringUtils.isNotBlank(webJobDto.monthlyLowerSalaryPrice)) tempWebJobPropterty.monthly_lower_salary_price = webJobDto.monthlyLowerSalaryPrice;
			if (StringUtils.isNotBlank(webJobDto.monthlyUpperSalaryPrice)) tempWebJobPropterty.monthly_upper_salary_price = webJobDto.monthlyUpperSalaryPrice;
			if (StringUtils.isNotBlank(webJobDto.monthlySalary)) tempWebJobPropterty.monthly_salary= webJobDto.monthlySalary;
			if (StringUtils.isNotBlank(webJobDto.monthlySalaryDetail)) tempWebJobPropterty.monthly_salary_detail = webJobDto.monthlySalaryDetail;
			if (CollectionUtils.isNotEmpty(webJobDto.otherConditionKbnList)) tempWebJobPropterty.other_condition_kbn_list = webJobDto.otherConditionKbnList;
			if (StringUtils.isNotBlank(webJobDto.personHunting)) tempWebJobPropterty.person_hunting = webJobDto.personHunting;
			if (StringUtils.isNotBlank(webJobDto.workingHours)) tempWebJobPropterty.working_hours = webJobDto.workingHours;
			if (CollectionUtils.isNotEmpty(webJobDto.treatmentKbnList)) tempWebJobPropterty.treatment_kbn_list = webJobDto.treatmentKbnList;
			if (StringUtils.isNotBlank(webJobDto.treatment)) tempWebJobPropterty.treatment = webJobDto.treatment;
			if (StringUtils.isNotBlank(webJobDto.holiday)) tempWebJobPropterty.holiday = webJobDto.holiday;
			if (StringUtils.isNotBlank(webJobDto.displayOrder)) tempWebJobPropterty.display_order = webJobDto.displayOrder;
			if (StringUtils.isNotBlank(webJobDto.editFlg)) tempWebJobPropterty.edit_flg = webJobDto.editFlg;
			if (CollectionUtils.isNotEmpty(webJobDto.shopIdList)) tempWebJobPropterty.shop_id_list = webJobDto.shopIdList;

			property.webJobDtoList.add(tempWebJobPropterty);
		}

		// 素材のコピー
		property.materialList = new ArrayList<>();
		for (Entry<String, MaterialDto> map : editForm.materialMap.entrySet()) {
			MaterialDto materialDto = map.getValue();
			TempMaterialProperty tempMaterialProperty = new TempMaterialProperty();
			tempMaterialProperty.id = materialDto.id;
			tempMaterialProperty.web_id = materialDto.webId;
			tempMaterialProperty.material_kbn = materialDto.materialKbn;
			tempMaterialProperty.file_name = materialDto.fileName;
			tempMaterialProperty.content_type = materialDto.contentType;
			property.materialList.add(tempMaterialProperty);
		}

		TTempWeb tTempWeb = tempWebdataLogic.register(property);

		List<MArea> areaList;
		try {
			areaList = areaService.getMAreaList(NumberUtils.toInt(editForm.areaCd));
		} catch (WNoResultException e1) {
			return null;
		}

		// プレビューURLのセット
		StringBuilder previewUrl = new StringBuilder();
		previewUrl.append(getCommonProperty("gc.sslDomain"));
		previewUrl.append(String.format(getCommonProperty("gc.preview.url.webdata.tempList"), areaList.get(0).linkName));
		previewUrl.append(String.format("?ids=%s&key=%s&info=1", tTempWeb.id, DigestUtils.md5Hex(tTempWeb.id + GourmetCareeConstants.SYSTEM_HASH_SOLT)));
		previewUrl.append("?redirect=true");

		log.info("リダイレクトします：" + previewUrl.toString());

		return previewUrl.toString();
	}

}