package com.gourmetcaree.admin.pc.shopList.action.shopList;

import static com.gourmetcaree.common.constants.GourmetCareeConstants.*;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.util.Base64Util;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.MessageResourcesUtil;
import org.seasar.struts.util.RequestUtil;
import org.seasar.struts.util.ResponseUtil;

import com.gourmetcaree.admin.pc.shopList.dto.shopList.ShopListSearchDto;
import com.gourmetcaree.admin.pc.shopList.dto.shopList.ShopListViewDto;
import com.gourmetcaree.admin.pc.shopList.form.shopList.ListForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.csv.ShopListArbeitCsv;
import com.gourmetcaree.common.csv.ShopListJobCsv;
import com.gourmetcaree.common.csv.StationGroupCsv;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.FileWriteErrorException;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.exception.ImageWriteErrorException;
import com.gourmetcaree.common.logic.ShopListCsvLogic;
import com.gourmetcaree.common.logic.ShopListLogic;
import com.gourmetcaree.common.parser.csv.ToCsvParser;
import com.gourmetcaree.common.property.ShopListCsvProperty;
import com.gourmetcaree.common.property.ShopListJobCsvProperty;
import com.gourmetcaree.common.property.ShopListSearchProperty;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.common.util.WebdataFileUtils;
import com.gourmetcaree.common.util.WebdataFileUtils.ContentType;
import com.gourmetcaree.db.common.constants.MAreaConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TShopChangeJobCondition;
import com.gourmetcaree.db.common.entity.TShopList;
import com.gourmetcaree.db.common.entity.TShopListMaterial;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.common.service.ShopListMaterialNoDataService;
import com.gourmetcaree.db.webdata.dto.webdata.IdSelectDto;

import net.arnx.jsonic.JSON;

/**
 * 店舗一覧検索アクション
 * @author Takehiro Nakamori
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN, ManageAuthLevel.STAFF, ManageAuthLevel.AGENCY, ManageAuthLevel.OTHER, ManageAuthLevel.SALES})
public class ListAction extends ShopListBaseAction {

	/** ログオブジェクト */
	private static final Logger log = Logger.getLogger(ListAction.class);

	/** 店舗一覧検索アクションフォーム */
	@ActionForm
	@Resource
	private ListForm listForm;

	@Resource
	private ShopListCsvLogic shopListCsvLogic;

	/** デフォルトの表示ページ */
	private static final int DEFAULT_PAGE = 1;

	/** ページナビヘルパー */
	public PageNavigateHelper pageNavi;

	/** 検索結果DTO */
	public List<ShopListSearchDto> shopList;

	public ShopListViewDto viewDto;

	@Resource
	private ShopListMaterialNoDataService shopListMaterialNoDataService;

	@Resource
	private ShopListLogic shopListLogic;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm" , urlPattern = "index/{customerId}", input = TransitionConstants.ShopList.JSP_APQ02L01)
	@MethodAccess(accessCode="SHOPLIST_LIST_INDEX")
	public String index() {
		checkArgsNull(NO_BLANK_FLG_NG, listForm.customerId);
		listForm.setExistDataFlgNg();
		return show();
	}

	/**
	 * 初期表示遷移
	 * @return
	 */
	private String show() {

		viewDto = new ShopListViewDto();

		// プレビューURLのセット
		StringBuilder previewUrl = new StringBuilder();
		previewUrl.append(getCommonProperty("gc.sslDomain"));
		previewUrl.append(String.format(getCommonProperty("gc.preview.url.shopList.detail"), "kanto"));
		previewUrl.append(String.format("?customerId=%s&key=%s", listForm.customerId, DigestUtils.md5Hex(listForm.customerId + GourmetCareeConstants.SYSTEM_HASH_SOLT)));
		viewDto.previewUrl = previewUrl.toString();
		return TransitionConstants.ShopList.JSP_APQ02L01;
	}

	/**
	 * 検索を実行する画面
	 * @return
	 */
	@Execute(validator = true, reset = "resetFormWithoutCustomerId", input = TransitionConstants.ShopList.JSP_APQ02L01)
	@MethodAccess(accessCode="SHOPLIST_LIST_SEARCH")
	public String search() {
		checkArgsNull(NO_BLANK_FLG_NG, listForm.customerId);
		doSearch(GourmetCareeConstants.DEFAULT_PAGE);
		return TransitionConstants.ShopList.JSP_APQ02L01;
	}

	/**
	 * 表示変数変更
	 * @return
	 */
	@Execute(validator = true, input = TransitionConstants.ShopList.JSP_APQ02L01)
	@MethodAccess(accessCode="SHOPLIST_LIST_CHANGEMAXROW")
	public String changeMaxRow() {
		checkArgsNull(NO_BLANK_FLG_NG, listForm.customerId);
		int targetPage = DEFAULT_PAGE;
//		if (listForm.pageNavi != null) {
//			listForm.pageNavi.maxRow = NumberUtils.toInt(listForm.maxRow, getMaxRowNum());
//		}
		doSearch(targetPage);
		return TransitionConstants.ShopList.JSP_APQ02L01;
	}

	/**
	 * ページ変更
	 * @return
	 */
	@Execute(validator = true, urlPattern="changePage/{pageNum}", input = TransitionConstants.ShopList.JSP_APQ02L01)
	@MethodAccess(accessCode="SHOPLIST_LIST_CHANGEPAGE")
	public String changePage() {
		checkArgsNull(NO_BLANK_FLG_NG, listForm.customerId);
		int targetPage = NumberUtils.toInt(listForm.pageNum, DEFAULT_PAGE);
		doSearch(targetPage);

		return TransitionConstants.ShopList.JSP_APQ02L01;
	}

	/**
	 * インデックスへの遷移
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_APQ02L01)
	@MethodAccess(accessCode="SHOPLIST_LIST_BACKINDEX")
	public String backToIndex() {
		checkArgsNull(NO_BLANK_FLG_NG, listForm.customerId);
		String backPath = createReindexPath(listForm.customerId);
		listForm.resetForm();
		return backPath;
	}



	/**
	 * CSVのインポート
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_APQ02L01)
	@MethodAccess(accessCode="SHOPLIST_LIST_IMPORTCSV")
	public String importCsv() {
		checkArgsNull(NO_BLANK_FLG_NG, listForm.customerId);
		if (listForm.csvFormFile == null || listForm.csvFormFile.getFileSize() <= 0) {
			throw new ActionMessagesException("errors.app.noExistFile");
		}

		String fileName = listForm.csvFormFile.getFileName();
		if (StringUtils.isNotBlank(fileName)
				&& !fileName.toLowerCase().endsWith(GourmetCareeConstants.MEDIA_EXTENTION_CSV)) {
			throw new ActionMessagesException("errors.app.notCsvFile");
		}

		String dirPath = getCommonProperty("gc.csv.shopList");
		StringBuilder dirName = new StringBuilder("")
									.append(listForm.customerId)
									.append("_")
									.append(session.getId());

		String fileAbsPath = "";

		try {
			fileAbsPath = WebdataFileUtils.createFile(dirPath, dirName.toString(), listForm.csvFormFile.getFileName(), listForm.csvFormFile.getFileData());
		} catch (FileNotFoundException e) {
			log.warn(e);
		} catch (FileWriteErrorException e) {
			log.warn(e);
		} catch (IOException e) {
			log.warn(e);
		}

		listForm.resetFormFile();

		List<Integer> registerIdList = new ArrayList<Integer>();

		try {
			registerIdList = shopListCsvLogic.importArbeitCsv(fileAbsPath, NumberUtils.toInt(listForm.customerId), MAreaConstants.AreaCd.SHUTOKEN_AREA, getCommonProperty("gc.csv.encoding"));
		} catch (FileNotFoundException e) {
			throw new ActionMessagesException("errors.app.noExistFile");
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new ActionMessagesException("errors.app.incorrectCsvFile");
		} catch (IOException e) {
			log.warn("CSVファイルの読み込み中にエラーが発生しました" + e);
		}

		setObjectToSession(SHOP_LIST_TEMP_ID_LIST_SESSION_KEY, registerIdList);

		return  GourmetCareeUtil.makePath(TransitionConstants.ShopList.ACTION_SHOPLIST_INPUTCSV,
											"/index",
											listForm.customerId,
											TransitionConstants.REDIRECT_STR);
	}

	/**
	 * CSVアウトプット
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_APQ02L01)
	@MethodAccess(accessCode="SHOPLIST_LIST_EXPORT")
	public String export() {
		checkArgsNull(NO_BLANK_FLG_NG, listForm.customerId);
		outPut();
		return null;
	}

	/**
	 * CSVのインポート
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_APQ02L01)
	@MethodAccess(accessCode="SHOPLIST_LIST_IMPORTCSV")
	public String importJobCsv() {
		checkArgsNull(NO_BLANK_FLG_NG, listForm.customerId);
		if (listForm.jobCsvFormFile == null || listForm.jobCsvFormFile.getFileSize() <= 0) {
			throw new ActionMessagesException("errors.app.noExistFile");
		}

		String fileName = listForm.jobCsvFormFile.getFileName();
		if (StringUtils.isNotBlank(fileName)
				&& !fileName.toLowerCase().endsWith(GourmetCareeConstants.MEDIA_EXTENTION_CSV)) {
			throw new ActionMessagesException("errors.app.notCsvFile");
		}

		String dirPath = getCommonProperty("gc.csv.shopListJob");
		StringBuilder dirName = new StringBuilder("")
									.append(listForm.customerId)
									.append("_")
									.append(session.getId());

		String fileAbsPath = "";

		try {
			fileAbsPath = WebdataFileUtils.createFile(dirPath, dirName.toString(), listForm.jobCsvFormFile.getFileName(), listForm.jobCsvFormFile.getFileData());
		} catch (FileNotFoundException e) {
			log.warn(e);
		} catch (FileWriteErrorException e) {
			log.warn(e);
		} catch (IOException e) {
			log.warn(e);
		}

		listForm.resetFormFile();

		List<TShopChangeJobCondition> tempList = new ArrayList<TShopChangeJobCondition>();

		try {
			tempList = shopListCsvLogic.importJobCsv(fileAbsPath, NumberUtils.toInt(listForm.customerId), MAreaConstants.AreaCd.SHUTOKEN_AREA, getCommonProperty("gc.csv.encoding"));
		} catch (FileNotFoundException e) {
			throw new ActionMessagesException("errors.app.noExistFile");
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new ActionMessagesException("errors.app.incorrectCsvFile");
		} catch (IOException e) {
			log.warn("CSVファイルの読み込み中にエラーが発生しました" + e);
		}

		setObjectToSession(SHOP_LIST_JOB_TEMP_LIST_SESSION_KEY, tempList);

		return  GourmetCareeUtil.makePath(TransitionConstants.ShopList.ACTION_SHOPLIST_INPUTJOBCSV,
											"/index",
											listForm.customerId,
											TransitionConstants.REDIRECT_STR);
	}

	/**
	 * CSVアウトプット
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_APQ02L01)
	@MethodAccess(accessCode="SHOPLIST_LIST_EXPORT")
	public String exportJob() {
		checkArgsNull(NO_BLANK_FLG_NG, listForm.customerId);
		outPutJob();
		return null;
	}

	/**
	 * 表示順を変更するメソッド
	 * @return
	 */
	@Execute(validator = false, reset = "resetDispOrder", input = TransitionConstants.ShopList.JSP_APQ02L01)
	public String changeDispOrder() {
		checkArgsNull(NO_BLANK_FLG_NG, listForm.changeDisplayId, listForm.customerId);
		checkDispOrder();
		try {
			shopListService.changeDispOrder(Integer.parseInt(listForm.changeDisplayId), Integer.parseInt(listForm.customerId), Integer.parseInt(listForm.toDisplayOrder));
		} catch (NumberFormatException e) {
			throw new FraudulentProcessException(e);
		}
		listForm.resetDispOrder();
		return "/shopList/list/reShowList?redirect=true";
	}

	/**
	 * 表示番号のチェック
	 */
	private void checkDispOrder() {
		if (StringUtils.isBlank(listForm.toDisplayOrder)
				|| !NumberUtils.isNumber(listForm.toDisplayOrder)) {
			throw new ActionMessagesException("errors.integerhankaku", MessageResourcesUtil.getMessage("labels.toDispOrder"));
		}

		int dispOrder = NumberUtils.toInt(listForm.toDisplayOrder);
		if (dispOrder <= 0 || shopListService.isOrverMaxDispOrder(NumberUtils.toInt(listForm.customerId), dispOrder)) {
			throw new ActionMessagesException("errors.app.outOfShoplistOrder", dispOrder);
		}
	}

	/**
	 * 表示フラグをONに変更する
	 * @return
	 */
	@Execute(validator = false, validate = "validateForLump", reset = "resetMultiBox", input = TransitionConstants.ShopList.FWD_SHOPLIST_LIST_RESHOW)
	public String doLumpChangeDisplayOn() {
		return doLumpChangeDisplay(MTypeConstants.ShopListDisplayFlg.DISPLAY);
	}

	/**
	 * 表示フラグをOFFに変更する
	 * @return
	 */
	@Execute(validator = false, validate = "validateForLump", reset = "resetMultiBox", input = TransitionConstants.ShopList.FWD_SHOPLIST_LIST_RESHOW)
	public String doLumpChangeDisplayOff() {
		return doLumpChangeDisplay(MTypeConstants.ShopListDisplayFlg.NOT_DISPLAY);
	}

	/**
	 * 表示フラグの切り替えを行う
	 * @param displayFlg
	 * @return
	 */
	private String doLumpChangeDisplay(int displayFlg) {
		checkArgsNull(NO_BLANK_FLG_NG, listForm.customerId);
		try {
			shopListService.updateDisplayFlg(displayFlg, Integer.parseInt(listForm.customerId), listForm.changeIdArray);
		} catch (NumberFormatException e) {
			throw new FraudulentProcessException();
		}
		listForm.resetMultiBox();
		return "/shopList/list/reShowList?redirect=true";
	}

	@Execute(validator = false, validate = "validateForLump", reset = "resetMultiBox", input = TransitionConstants.ShopList.FWD_SHOPLIST_LIST_RESHOW)
	public String doLumpDelete() {
		checkArgsNull(NO_BLANK_FLG_NG, listForm.customerId);
		try {
			shopListService.lumpDelete(Integer.parseInt(listForm.customerId), listForm.changeIdArray);
			listForm.resetMultiBox();
			return "/shopList/list/reShowList?redirect=true";
		} catch (NumberFormatException e) {
			throw new FraudulentProcessException();
		}
	}




	@Execute(validator = false, input = TransitionConstants.ShopList.JSP_APQ02L01)
	public String reShowList() {
		checkArgsNull(NO_BLANK_FLG_NG, listForm.customerId);
		doSearch(NumberUtils.toInt(listForm.pageNum, GourmetCareeConstants.DEFAULT_PAGE));
		return TransitionConstants.ShopList.JSP_APQ02L01;
	}


    /**
     * 画像を登録する
     * @return
     */
    @Execute(validator = false)
    public String ajaxInsertShopImage() {
        final String customerImageId = RequestUtil.getRequest().getParameter("customerImageId");
        final String shopListId = RequestUtil.getRequest().getParameter("shopListId");
        final String shopListMaterialKbn = RequestUtil.getRequest().getParameter("shopListMaterialKbn");

		Map<String, String> resMap = new HashMap<>();

        if (StringUtils.isEmpty(customerImageId) || StringUtils.isEmpty(shopListId) || StringUtils.isEmpty(shopListMaterialKbn)) {
        	resMap.put("error", "登録に失敗しました");
    		ResponseUtil.write(JSON.encode(resMap), JSON_CONTENT_TYPE, ENCODING);
        	return null;
        }

        boolean isSuccess = shopListLogic.deleteInsertShopListMaterial(
    		Integer.parseInt(listForm.customerId),
    		Integer.parseInt(customerImageId),
			Integer.parseInt(shopListId),
			Integer.parseInt(shopListMaterialKbn)
		);

        if (!isSuccess) {
        	resMap.put("error", "登録に失敗しました");
        }

		ResponseUtil.write(JSON.encode(resMap), JSON_CONTENT_TYPE, ENCODING);
    	return null;
    }


    /**
     * 画像を削除する
     * @return
     */
    @Execute(validator = false)
    public String ajaxDeleteShopImage() {
        final String shopListId = RequestUtil.getRequest().getParameter("shopListId");
        final String shopListMaterialKbn = RequestUtil.getRequest().getParameter("shopListMaterialKbn");

		Map<String, String> resMap = new HashMap<>();

        if (StringUtils.isEmpty(shopListId) || StringUtils.isEmpty(shopListMaterialKbn)) {
        	resMap.put("error", "削除に失敗しました");
    		ResponseUtil.write(JSON.encode(resMap), JSON_CONTENT_TYPE, ENCODING);
        	return null;
        }

        boolean isSuccess = shopListLogic.deleteShopListMaterial(
			Integer.parseInt(shopListId),
			Integer.parseInt(shopListMaterialKbn)
		);

        if (!isSuccess) {
        	resMap.put("error", "削除に失敗しました");
        }

		ResponseUtil.write(JSON.encode(resMap), JSON_CONTENT_TYPE, ENCODING);
    	return null;
    }


	/**
	 * CSVのアウトプットを行う
	 */
	private void outPut() {
		ShopListCsvProperty csvProperty = new ShopListCsvProperty();
		final int maxSize;
		if (listForm.existDataFlg) {
				ShopListSearchProperty property = new ShopListSearchProperty();
				Beans.copy(listForm, property).execute();
				csvProperty.shopListList = shopListLogic.createShopListSelectForCsv(property);
				maxSize = csvProperty.shopListList.stream().max((csv1, csv2) -> Integer.compare(csv1.stationGroupList.size(), csv2.stationGroupList.size()))
						.map(csv -> csv.stationGroupList.size())
						.orElseGet(() -> 0);
		} else {
			maxSize = 0;
			csvProperty.shopListList = null;
		}
		convertCsvProperty(csvProperty);
		try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(ResponseUtil.getResponse().getOutputStream(), "shift-jis"))){
			final CSVPrinter printer = CSVFormat
					.EXCEL
					.print(bw);

			response.setContentType(
					"application/octet-stream;charset=Windows-31J");
			response.setHeader(
					"Content-Disposition",
					"attachment; filename=" + createOutputFilePath(csvProperty));

			ToCsvParser<ShopListArbeitCsv> parser = ToCsvParser.newInstance(csvProperty.shopListList);
			parser.parse((int index, ShopListArbeitCsv data, List<String> row) -> {
					if(index == 0) {
						for(int i = 0; i < maxSize; i++) {
							row.add("駅" + (i + 1));
							row.add("移動手段区分"+ (i + 1));
							row.add("所要時間(分)"+ (i + 1));
						}

					}else if(data.stationGroupList != null) {
						for(StationGroupCsv stationInfo : data.stationGroupList) {
							row.add(stationInfo.stationCd);
							row.add(stationInfo.transportationKbn);
							row.add(stationInfo.timeRequiredMinute);
						}
				}

				// CSVﾚｺｰﾄﾞ作成
				try {
					printer.printRecord(row);
				} catch (IOException e) {
					log.warn("CSVの出力に失敗しました", e);
					throw new ActionMessagesException("errors.app.csvOutPutFailed");
				}
			});
		} catch (IOException e) {
			log.warn("CSVの出力に失敗しました", e);
			throw new ActionMessagesException("errors.app.csvOutPutFailed");
		} catch (Throwable t) {
			log.warn("CSVの出力に失敗しました", t);
			throw new ActionMessagesException("errors.app.csvOutPutFailed");
		}

	}

	/**
	 * CSVのアウトプットを行う
	 */
	private void outPutJob() {
		ShopListJobCsvProperty csvProperty = new ShopListJobCsvProperty();

		ShopListSearchProperty property = new ShopListSearchProperty();
		Beans.copy(listForm, property).execute();
		csvProperty.shopListJob = shopListLogic.createShopListJobselectForCsv(property);

		convertJobCsvProperty(csvProperty);
		try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(ResponseUtil.getResponse().getOutputStream(), "shift-jis"))){
			final CSVPrinter printer = CSVFormat
					.EXCEL
					.print(bw);

			response.setContentType(
					"application/octet-stream;charset=Windows-31J");
			response.setHeader(
					"Content-Disposition",
					"attachment; filename=" + createOutputFilePath(csvProperty));

			ToCsvParser<ShopListJobCsv> parser = ToCsvParser.newInstance(csvProperty.shopListJob);

			parser.parse((int index, ShopListJobCsv data, List<String> row) -> {
				// CSVﾚｺｰﾄﾞ作成
				try {
					printer.printRecord(row);
				} catch (IOException e) {
					log.warn("CSVの出力に失敗しました", e);
					throw new ActionMessagesException("errors.app.csvOutPutFailed");
				}
			});

		} catch (IOException e) {
			log.warn("CSVの出力に失敗しました", e);
			throw new ActionMessagesException("errors.app.csvOutPutFailed");
		} catch (Throwable t) {
			log.warn("CSVの出力に失敗しました", t);
			throw new ActionMessagesException("errors.app.csvOutPutFailed");
		}

	}

	/**
	 * 検索を実行
	 */
	private void doSearch(int targetPage) {
		ShopListSearchProperty property = new ShopListSearchProperty();
		Beans.copy(listForm, property).execute();
		property.targetPage = targetPage;
		property.maxRow = getMaxRowNum();
		try {
			List<TShopList> entityList = shopListLogic.searchShopList(property);
			listForm.setExistDataFlgOk();
			pageNavi = property.pageNavi;

			List<ShopListSearchDto> dtoList = new ArrayList<ShopListSearchDto>();
			for (TShopList entity : entityList) {
				ShopListSearchDto dto = new ShopListSearchDto();
				Beans.copy(entity, dto).execute();

				List<Integer> industryKbnList = new ArrayList<>();
				if (entity.industryKbn1 != null) {
					industryKbnList.add(entity.industryKbn1);
				}

				dto.industryNameList.add(industryKbnList.get(0));

				if (entity.industryKbn2 != null) {
					industryKbnList.add(entity.industryKbn2);
					dto.industryNameList.add(industryKbnList.get(1));
				}

				dto.detailPath = GourmetCareeUtil.makePath(TransitionConstants.ShopList.ACTION_SHOPLIST_DETAIL, "/index", String.valueOf(entity.customerId), String.valueOf(entity.id));

				// 表示・非表示フラグ
				dto.dispFlg = entity.displayFlg;

				// 画像をセット
				dto.materialExistsDto = shopListMaterialNoDataService.getExistsDto(entity.id);

				String dirPath = getCommonProperty("gc.shopList.imgUpload.dir.session");
				StringBuilder dirName = new StringBuilder("");
				dirName.append(entity.id);
				dirName.append("_");
				dirName.append(session.getId());

				List<IdSelectDto> materialList = shopListMaterialService.getIdList(entity.id);
				for (IdSelectDto idSelectDto : materialList) {
					try {
						TShopListMaterial material = shopListMaterialService.findById(idSelectDto.id);
						if (material.materialData == null) {
							continue;
						}

						String fileName = WebdataFileUtils.createFileName(String.valueOf(material.materialKbn),
																	ContentType.toEnum(material.contentType));
						try {
							WebdataFileUtils.createWebdataFile(dirPath,
															dirName.toString(),
															fileName,
															Base64Util
																.decode(material.materialData.replaceAll("\\n", "")));
						} catch (ImageWriteErrorException e) {
							log.warn(e);
						}
					} catch (SNoResultException e) {
						// 主キーのループ中のため0件は想定されない。未処理とする。
					}
				}


				dtoList.add(dto);
			}

			shopList = dtoList;
		} catch (WNoResultException e) {
			listForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.dataNotFound");
		}

		viewDto = new ShopListViewDto();

		// プレビューURLのセット
		StringBuilder previewUrl = new StringBuilder();
		previewUrl.append(getCommonProperty("gc.sslDomain"));
		previewUrl.append(String.format(getCommonProperty("gc.preview.url.shopList.detail"), "kanto"));
		previewUrl.append(String.format("?customerId=%s&key=%s", listForm.customerId, DigestUtils.md5Hex(listForm.customerId + GourmetCareeConstants.SYSTEM_HASH_SOLT)));
		viewDto.previewUrl = previewUrl.toString();
		viewDto.imageList = createImageDtoList(NumberUtils.toInt(listForm.customerId));
	}

	/**
	 * 表示件数の取得
	 * @return
	 */
	private int getMaxRowNum() {
		if (StringUtils.isNotBlank(listForm.maxRow)) {
			return NumberUtils.toInt(listForm.maxRow, GourmetCareeConstants.DEFAULT_MAX_ROW_INT);
		}
		return NumberUtils.toInt(getCommonProperty("gc.shopList.initMaxRow"), GourmetCareeConstants.DEFAULT_MAX_ROW_INT);
	}

	/**
	 * CSVプロパティの作成
	 * @param property
	 */
	private void convertCsvProperty(ShopListCsvProperty property) {
		property.pass = getCommonProperty("gc.csv.filepass");
		property.faileName = getCommonProperty("gc.shopList.csv.filename");
		property.encode = getCommonProperty("gc.csv.encoding");
	}

	/**
	 * CSVプロパティの作成
	 * @param property
	 */
	private void convertJobCsvProperty(ShopListJobCsvProperty property) {
		property.pass = getCommonProperty("gc.csv.filepass");
		property.faileName = getCommonProperty("gc.shopListJob.csv.filename");
		property.encode = getCommonProperty("gc.csv.encoding");
	}

	/**
	 * ファイル名を生成する
	 * @return ファイル名
	 */
	private static String createOutputFilePath(ShopListCsvProperty property) {
		String filename = null;
		String dateStr = null;
		filename = property.faileName;

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

		dateStr = sdf.format(date);

		String outputFileName = new StringBuilder()
		.append(filename)
		.append("_")
		.append(dateStr)
		.append(".csv")
		.toString();

		return outputFileName;
	}

	/**
	 * ファイル名を生成する
	 * @return ファイル名
	 */
	private static String createOutputFilePath(ShopListJobCsvProperty property) {
		String filename = null;
		String dateStr = null;
		filename = property.faileName;

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

		dateStr = sdf.format(date);

		String outputFileName = new StringBuilder()
		.append(filename)
		.append("_")
		.append(dateStr)
		.append(".csv")
		.toString();

		return outputFileName;
	}
}
