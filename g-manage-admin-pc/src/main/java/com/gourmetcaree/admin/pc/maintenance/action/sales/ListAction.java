package com.gourmetcaree.admin.pc.maintenance.action.sales;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.maintenance.dto.sales.SalesDto;
import com.gourmetcaree.admin.pc.maintenance.form.sales.ListForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.entity.MSales;
import com.gourmetcaree.db.common.entity.VSalesCompany;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.common.service.SalesCompanyService;

/**
 * 営業担当者一覧アクションクラス
 * @author Takahiro Kimura
 * @version 1.0
 *
 */
@ManageLoginRequired(authLevel=ManageAuthLevel.ADMIN)
public class ListAction extends SalesBaseAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(ListAction.class);

	/** 一覧フォーム */
	@ActionForm
	@Resource
	protected ListForm listForm;

	/** 営業担当者・会社サービスクラス */
	@Resource
	protected SalesCompanyService salesCompanyService;

	/** デフォルトの表示ページ */
	private static final int DEFAULT_PAGE = 1;

	/** デフォルトの表示件数 */
	private static final int DEFAULT_MAX_ROW = 20;

	/** PageNavigatorにセットするデフォルトのソートキー */
	private static final String DEFAULT_SORT_KEY = "c_display_order DESC, company_id DESC, s_display_order DESC, id DESC";

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Maintenance.JSP_APJ04L01)
	@MethodAccess(accessCode="SALES_LIST_INDEX")
	public String index() {
		return show();
	}

	/**
	 * 初期表示遷移用
	 * @return 登録画面のパス
	 */
	private String show() {
		// 検索画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ04L01;
	}

	/**
	 * 営業担当者検索の検索ボタン押下時の処理
	 * @return 営業担当者検索JSPのパス
	 */
	@Execute(validator = false, input = TransitionConstants.Maintenance.JSP_APJ04L01)
	@MethodAccess(accessCode="SALES_LIST_SEARCH")
	public String search() {

		//ページナビゲータを初期化
		listForm.pageNavi = new PageNavigateHelper(DEFAULT_SORT_KEY, getMaxRowNum());
		listForm.maxRow = String.valueOf(getMaxRowNum());

		doSearch(DEFAULT_PAGE);

		return TransitionConstants.Maintenance.JSP_APJ04L01;
	}

	/**
	 * ページ遷移のボタン押下時の処理。
	 * 画面から指定されたページ数を数値化して検索を実行
	 * 不正なパラメータの場合は1をデフォルトページを表示する。
	 * @return 営業担当者検索JSPのパス
	 */
	@Execute(validator = false, urlPattern = "changePage/{pageNum}", input=TransitionConstants.Maintenance.JSP_APJ04L01)
	@MethodAccess(accessCode="SALES_LIST_CHANGEPAGE")
	public String changePage() {

		int targetPage = NumberUtils.toInt(listForm.pageNum, DEFAULT_PAGE);
		doSearch(targetPage);

		return TransitionConstants.Maintenance.JSP_APJ04L01;
	}

	/**
	 * 最大表示件数変更時の処理。
	 * 画面から指定された最大表示件数を数値化して検索を実行
	 * 不正なパラメータの場合は20をデフォル表示件数として表示する。
	 * @return 営業担当者検索JSPのパス
	 */
	@Execute(validator = false, input=TransitionConstants.Maintenance.JSP_APJ04L01)
	@MethodAccess(accessCode="SALES_LIST_CHANGEMAXROW")
	public String changeMaxRow() {

		int targetPage = DEFAULT_PAGE;

		if ("".equals(listForm.maxRow)) {

			if (listForm.pageNavi == null) {
				listForm.pageNavi = new PageNavigateHelper(DEFAULT_SORT_KEY, getMaxRowNum());
			}

			// 検索用パラメータを生成
			Map<String, String> map = new HashMap<String, String>();
			createParams(map);

			List<VSalesCompany> entityList;
			try {
				entityList = salesCompanyService.doAllSearch(listForm.pageNavi, map);

				// 表示用にデータを生成
				convertShowList(entityList);

				listForm.pageNavi = new PageNavigateHelper(DEFAULT_SORT_KEY, getMaxRowNum());
				listForm.pageNavi.allCount = entityList.size();

			} catch (WNoResultException e) {
				throw new ActionMessagesException("errors.app.dataNotFound");
			}

		} else {
			doSearch(targetPage);
		}

		return TransitionConstants.Maintenance.JSP_APJ04L01;
	}

	/**
	 * 別画面から再び検索に戻った場合に、遷移前の状態(=検索結果画面)を表示するために使用する
	 * 再検索メソッド
	 * @return 営業担当者検索JSPのパス
	 */
	@Execute(validator=false, input=TransitionConstants.Maintenance.JSP_APJ04L01)
	@MethodAccess(accessCode="SALES_LIST_SEARCHAGAIN")
	public String searchAgain() {

		int targetPage = DEFAULT_PAGE;

		if (listForm.pageNavi != null) {
			//セッションタイムアウトを考慮し、nullチェック後にページ取得
			targetPage = listForm.pageNavi.currentPage;
		}

		doSearch(targetPage);

		return TransitionConstants.Maintenance.JSP_APJ04L01;
	}

	/**
	 * 検索メインロジック
	 * @param targetPage
	 */
	private void doSearch(int targetPage) {

		// 検索用パラメータを生成
		Map<String, String> map = new HashMap<String, String>();
		createParams(map);

		if (listForm.pageNavi == null) {
			listForm.pageNavi = new PageNavigateHelper(DEFAULT_SORT_KEY, getMaxRowNum());
		}

		try {
			List<VSalesCompany> entityList;

			entityList = salesCompanyService.doSearch(listForm.pageNavi, map, targetPage);

			log.debug("検索結果取得");

			// 表示用にデータを生成
			convertShowList(entityList);

		} catch (WNoResultException e) {
			throw new ActionMessagesException("errors.app.dataNotFound");
		}
	}

	/**
	 * 検索条件をMapにセット
	 * @param map 検索条件Map
	 */
	private void createParams(Map<String, String> map) {

		map.put("salesName", StringUtils.defaultString(listForm.where_salesName, ""));
		map.put("salesNameKana", StringUtils.defaultString(listForm.where_salesNameKana, ""));
		map.put("companyId", StringUtils.defaultString(listForm.where_companyId, ""));
		map.put("authorityCd", StringUtils.defaultString(listForm.where_authorityCd, ""));
	}

	/**
	 * 表示用リストを生成
	 * @param entityList 営業担当者エンティティリスト
	 */
	private void convertShowList(List<VSalesCompany> entityList) {

		List<SalesDto> dtoList = new ArrayList<SalesDto>();

		for (VSalesCompany entity : entityList) {

			SalesDto dto = new SalesDto();

			Beans.copy(entity, dto).execute();
			dto.authorityCdName = MSales.AUTH_LEVEL_MAP.get(String.valueOf(entity.authorityCd));
			dto.detailPath = GourmetCareeUtil.makePath("/sales/detail/", "index", String.valueOf(entity.id));

			dtoList.add(dto);
		}

		listForm.salesList = dtoList;

	}

	/**
	 * 最大表示件数を取得する
	 * @return
	 */
	private int getMaxRowNum() {
		return NumberUtils.toInt(getCommonProperty("gc.sales.maxRow"), DEFAULT_MAX_ROW);
	}


}