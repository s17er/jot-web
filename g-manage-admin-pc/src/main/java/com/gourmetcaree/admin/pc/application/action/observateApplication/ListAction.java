package com.gourmetcaree.admin.pc.application.action.observateApplication;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.beans.util.Copy;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.application.dto.observateApplication.ObservateApplicationDto;
import com.gourmetcaree.admin.pc.application.form.observateApplication.ListForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.logic.ApplicationLogic;
import com.gourmetcaree.admin.service.property.ApplicationCsvProperty;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.logic.ValueToNameConvertLogic;
import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants.RenewalFlg;
import com.gourmetcaree.db.common.entity.TObservateApplication;
import com.gourmetcaree.db.common.entity.TWeb;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.common.service.WebIndustryKbnService;

/**
 * 応募一覧アクションクラス
 * @author Takahiro Kimura
 * @version 1.0
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN,ManageAuthLevel.STAFF, ManageAuthLevel.OTHER, ManageAuthLevel.SALES})
public class ListAction extends ObservateApplicationBaseAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(ListAction.class);

	/** デフォルトの表示ページ */
	private static final int DEFAULT_PAGE = 1;

	/** デフォルトの表示件数 */
	private static final int DEFAULT_MAX_ROW = 20;

	/** PageNavigatorにセットするデフォルトのソートキー */
	private static final String DEFAULT_SORT_KEY = "TOA.application_datetime desc, TOA.id desc";

	/** リストフォーム */
	@ActionForm
	@Resource
	protected ListForm listForm;

	/** 名前変換ロジック */
	@Resource
	private ValueToNameConvertLogic valueToNameConvertLogic;

	/** 応募ロジック */
	@Resource
	private ApplicationLogic applicationLogic;

	/** ページナビゲーター */
	public PageNavigateHelper pageNavi;

	/** 応募リスト */
	public List<ObservateApplicationDto> applicationList;

	/** WEBデータに設定された店舗の業態サービス */
	@Resource
	private WebIndustryKbnService webIndustryKbnService;


	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input =TransitionConstants.Application.JSP_API02L01)
	public String index() {
		return show();
	}

	/**
	 * 検索
	 * @return
	 */
	@Execute(validator = true, validate = "validate", input =TransitionConstants.Application.JSP_API02L01)
	@MethodAccess(accessCode="APPLICATION_LIST_SEARCH")
	public String search() {

		//ページナビゲータを初期化
		listForm.maxRow = String.valueOf(getMaxRowNum());

		doSearch(DEFAULT_PAGE);

		return TransitionConstants.Application.JSP_API02L01;
	}

	/**
	 * ページの切り替え
	 * @return
	 */
	@Execute(validator = false, urlPattern = "changePage/{pageNum}", input = TransitionConstants.Application.JSP_API02L01)
	@MethodAccess(accessCode="APPLICATION_LIST_CHANGEPAGE")
	public String changePage() {


		doSearch(NumberUtils.toInt(listForm.pageNum, DEFAULT_PAGE));

		return TransitionConstants.Application.JSP_API02L01;
	}

	/**
	 * 最大表示件数変更時の処理。
	 * 画面から指定された最大表示件数を数値化して検索を実行
	 * 不正なパラメータの場合は20をデフォル表示件数として表示する。
	 * @return GCWコード検索JSPのパス
	 */
	@Execute(validator = false, input = TransitionConstants.Application.JSP_API02L01)
	@MethodAccess(accessCode="APPLICATION_LIST_CHANGEMAXROW")
	public String changeMaxRow() {


		doSearch(DEFAULT_PAGE);

		return TransitionConstants.Application.JSP_API02L01;
	}

	/**
	 * 別画面から再び検索に戻った場合に、遷移前の状態(=検索結果画面)を表示するために使用する
	 * 再検索メソッド
	 * @return GCWコード検索JSPのパス
	 */
	@Execute(validator=false, input = TransitionConstants.Application.JSP_API02L01)
	@MethodAccess(accessCode="APPLICATION_LIST_SEARCHAGAIN")
	public String searchAgain() {

		doSearch(NumberUtils.toInt(listForm.pageNum, DEFAULT_PAGE));

		return TransitionConstants.Application.JSP_API02L01;
	}

	/**
	 * 初期表示遷移用
	 * @return 登録画面のパス
	 */
	private String show() {

		listForm.setExistDataFlgNg();

		// 一覧画面へ遷移
		return TransitionConstants.Application.JSP_API02L01;
	}

	/**
	 * 検索メインロジック
	 * @param targetPage
	 */
	private void doSearch(int targetPage) {

		// 検索用パラメータを生成
		Map<String, String> map = new HashMap<String, String>();
		createParams(map);

		pageNavi = new PageNavigateHelper(DEFAULT_SORT_KEY, getMaxRowNum());

		try {
			List<TObservateApplication> entityList;

//			entityList = observateApplicationService.doSearch(listForm.pageNavi, map);
			entityList = applicationLogic.doSearchObservateApplication(pageNavi, targetPage, map);

			log.debug("顧客情報を取得");

			// 表示用にデータを生成
			convertShowList(entityList);

			listForm.setExistDataFlgOk();

		} catch (WNoResultException e) {
			listForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.dataNotFound");
		} catch (ParseException e) {
			listForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.invalidSearchParameter.");
		}
	}

	/**
	 * 表示データを生成
	 * @param entityList 応募エンティティリスト
	 */
	private void convertShowList(List<TObservateApplication> entityList) {

		List<ObservateApplicationDto> dtoList = new ArrayList<ObservateApplicationDto>();
		for (TObservateApplication entity : entityList) {
			ObservateApplicationDto dto = new ObservateApplicationDto();

			Copy copy = Beans.copy(entity, dto).dateConverter(GourmetCareeConstants.DATE_TIME_FORMAT, "applicationDateTime");
			copy.execute();

			dto.applicationDateTime = DateUtils.getDateStr(entity.applicationDatetime, GourmetCareeConstants.DATE_TIME_FORMAT);
			dto.detailPath = GourmetCareeUtil.makePath("/observateApplication/detail/", "index", String.valueOf(entity.id));

			if (entity.mCustomer != null) {
				dto.customerName = entity.mCustomer.customerName;
			}

			TWeb web = webService.findById(entity.webId);
			// リニューアル移行のデータの場合
			if (web.renewalFlg == RenewalFlg.TARGET) {
				dto.industryKbnList = webIndustryKbnService.getIndustryKbnList(web.id);
			}

			// リニューアル後は年齢
			if (entity.birthday != null) {
				dto.age = String.valueOf(GourmetCareeUtil.convertToAge(entity.birthday));
			}

			Beans.copy(web, dto).includes("manuscriptName", "industryKbn1", "industryKbn2", "industryKbn3").execute();
//			dto.manuscriptName = web.manuscriptName;
//			dto.industryKbn1 = web.industryKbn1;
//			dto.industryKbn2 = web.industryKbn2;
//			dto.industryKbn3 = web.industryKbn3;


			dtoList.add(dto);
		}

		applicationList = dtoList;
	}

	/**
	 * 検索条件をMapにセット
	 * @param map 検索条件Map
	 */
	private void createParams(Map<String, String> map) {

		map.put("areaCd", StringUtils.defaultString(listForm.where_areaCd, ""));
		map.put("industryKbn", StringUtils.defaultString(listForm.where_industryKbn, ""));
		map.put("lowerAge", StringUtils.defaultString(listForm.where_lowerAge, ""));
		map.put("upperAge", StringUtils.defaultString(listForm.where_upperAge, ""));
		map.put("sexKbn", StringUtils.defaultString(listForm.where_sexKbn, ""));
		map.put("id", StringUtils.defaultString(listForm.where_observateApplicationId, ""));
		map.put("memberFlg", StringUtils.defaultString(listForm.where_memberFlg, ""));
		map.put("terminalKbn", StringUtils.defaultString(listForm.where_terminalKbn, ""));
		map.put("customerName", StringUtils.defaultString(listForm.where_customerName, ""));
		map.put("name", StringUtils.defaultString(listForm.where_name, ""));
		map.put("nameKana", StringUtils.defaultString(listForm.where_nameKana, ""));
		map.put("pcMail", StringUtils.defaultString(listForm.where_pcMail, ""));
		map.put("mobileMail", StringUtils.defaultString(listForm.where_mobileMail, ""));
		map.put("applicationStartDate", StringUtils.defaultString(listForm.where_applicationStartDate, ""));
		map.put("applicationStartHour", StringUtils.defaultString(listForm.where_applicationStartHour, ""));
		map.put("applicationStartMinute", StringUtils.defaultString(listForm.where_applicationStartMinute, ""));
		map.put("applicationEndDate", StringUtils.defaultString(listForm.where_applicationEndDate, ""));
		map.put("applicationEndHour", StringUtils.defaultString(listForm.where_applicationEndHour, ""));
		map.put("applicationEndtMinute", StringUtils.defaultString(listForm.where_applicationEndMinute, ""));
		map.put("applicationName", StringUtils.defaultString(listForm.where_applicationName, ""));
		map.put("observationKbn", StringUtils.defaultString(listForm.where_observationKbn, ""));
		map.put("webId", StringUtils.defaultString(listForm.where_webId, ""));
	}

	/**
	 * CSV出力
	 * @return 一覧画面のパス
	 */
	@Execute(validator = false, input =TransitionConstants.Application.JSP_API02L01)
	@MethodAccess(accessCode="APPLICATION_LIST_OUTPUT")
	public String output() {

		ApplicationCsvProperty property = new ApplicationCsvProperty();
		// CSV出力に必要な値をセット
		setProperty(property);

		// CSV出力
		try {
			applicationCsvLogic.outPutObservateApplicationCsv(property);

//			// 出力件数を表示するメッセージをセット
//			ActionMessages messages = new ActionMessages();
//			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("message.csv.outputCount", property.count));
//			ActionMessagesUtil.addMessages(request, messages);

		} catch (WNoResultException e) {
			listForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.csvDataNotFound");

		} catch (ParseException e) {
			listForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.csvOutPutFailed");

		} catch (IOException e) {
			log.fatal("入出力エラーが発生しました。", e);
			listForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.csvOutPutFailed");

		}

		return null;
	}

	/**
	 * CSV出力処理に必要なプロパティをセット
	 * @param property
	 */
	private void setProperty(ApplicationCsvProperty property) {

		property.pass = getFilePass();
		property.faileName = getFileName();
		property.encode = getEncode();

		// 検索用パラメータを生成
		property.map = new HashMap<String, String>();
		createParams(property.map);
		property.sortKey = DEFAULT_SORT_KEY;
	}

	/**
	 * 最大表示件数を取得する
	 * @return
	 */
	private int getMaxRowNum() {
		try {
			return Integer.parseInt(listForm.maxRow);
		} catch (NumberFormatException e) {
			listForm.maxRow = getCommonProperty("gc.application.initMaxRow");
			return NumberUtils.toInt(listForm.maxRow, DEFAULT_MAX_ROW);
		}
	}

	/**
	 * CSV出力パスを取得する
	 * @return
	 */
	private String getFilePass() {
		return getCommonProperty("gc.csv.filepass");
	}

	/**
	 * CSVファイル名を取得する
	 * @return
	 */
	private String getFileName() {
		return getCommonProperty("gc.observateApplication.csv.filename");
	}

	/**
	 * CSV出力文字コードを取得する
	 * @return
	 */
	private String getEncode() {
		return getCommonProperty("gc.csv.encoding");
	}

}