package com.gourmetcaree.admin.pc.customer.action.customer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.customer.dto.customer.CompanySalesDto;
import com.gourmetcaree.admin.pc.customer.dto.customer.CustomerInfoDto;
import com.gourmetcaree.admin.pc.customer.form.customer.ListForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.logic.CustomerCsvLogic;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.AuthorizationException;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MAreaConstants;
import com.gourmetcaree.db.common.entity.MCustomer;
import com.gourmetcaree.db.common.entity.MCustomerCompany;
import com.gourmetcaree.db.common.entity.MCustomerSubMail;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.common.service.CustomerMailMagazineAreaService;
import com.gourmetcaree.db.common.service.CustomerService;
import com.gourmetcaree.db.common.service.SalesService;
import com.gourmetcaree.db.common.service.ShopListIndustryKbnService;
import com.gourmetcaree.db.common.service.ShopListPrefecturesService;
import com.gourmetcaree.db.common.service.ShopListShutokenForeignAreaKbnService;

/**
 * 顧客一覧アクションクラス
 * @author Takahiro Kimura
 * @version 1.0
 */
@ManageLoginRequired(authLevel = { ManageAuthLevel.ADMIN, ManageAuthLevel.STAFF, ManageAuthLevel.AGENCY, ManageAuthLevel.OTHER, ManageAuthLevel.SALES})
public class ListAction extends PcAdminAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(ListAction.class);

	/** デフォルトの表示ページ */
	private static final int DEFAULT_PAGE = 1;

	/** デフォルトの表示件数 */
	private static final int DEFAULT_MAX_ROW = 20;

	/** PageNavigatorにセットするデフォルトのソートキー */
	private static final String DEFAULT_SORT_KEY = "id desc, company_id, sales_id";


	/** 一覧フォーム */
	@ActionForm
	@Resource
	protected ListForm listForm;

	/** 顧客マスタサービス */
	@Resource
	protected CustomerService customerService;

	/** 顧客CSVロジック */
	@Resource
	private CustomerCsvLogic customerCsvLogic;

	/** 営業担当者サービス */
	@Resource
	protected SalesService salesService;

	/** ページナビゲーター */
	public PageNavigateHelper pageNavi;

	/** 顧客一覧 */
	public List<CustomerInfoDto> customerInfoDtoList;

	/** 系列店舗の業態サービス */
	@Resource
	private ShopListIndustryKbnService shopListIndustryKbnService;

	/** 系列店舗の都道府県サービス */
	@Resource
	private ShopListPrefecturesService shopListPrefecturesService;

	/** 系列店舗の海外サービス */
	@Resource
	private ShopListShutokenForeignAreaKbnService shopListShutokenForeignAreaKbnService;

	/** 顧客メルマガエリア */
	@Resource
	private CustomerMailMagazineAreaService customerMailMagazineAreaService;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Customer.JSP_APD01L01)
	@MethodAccess(accessCode = "CUSTOMER_LIST_INDEX")
	public String index() {
		return show();
	}

	/**
	 * 初期表示遷移用
	 * @return 登録画面のパス
	 */
	private String show() {

		try {
			// ログインユーザが代理店の場合
			// 担当会社にユーザの会社IDをセット
			if (ManageAuthLevel.AGENCY.value().equals(userDto.authLevel) || ManageAuthLevel.OTHER.value().equals(userDto.authLevel)) {

				listForm.assignedCompanyId = String.valueOf(salesService.getCompanyIdBySalesId(NumberUtils
						.toInt(userDto.userId)));
			}
		} catch (SNoResultException e) {
			throw new AuthorizationException("会社IDが取得できませんでした。userDto.userId：" + userDto.userId);
		}

		log.debug("一覧画面を表示");

		listForm.setExistDataFlgNg();

		// 一覧画面へ遷移
		return TransitionConstants.Customer.JSP_APD01L01;
	}

	/**
	 * 顧客検索の検索ボタン押下時の処理
	 * @return 顧客検索JSPのパス
	 */
	@Execute(validator = true, validate = "validate", reset = "resetSearch", input = TransitionConstants.Customer.JSP_APD01L01)
	@MethodAccess(accessCode = "CUSTOMER_LIST_SEARCH")
	public String search() {

		doSearch(DEFAULT_PAGE);

		log.debug("検索結果表示");

		return TransitionConstants.Customer.JSP_APD01L01;
	}


	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset="resetId", input = TransitionConstants.Customer.JSP_APD01C01)
	@MethodAccess(accessCode = "CUSTOMER_LIST_SEARCH")
	public String outputCsv() {

		Map<String, String> map = new HashMap<String, String>();
		createParams(map);

		String companyIdStr = "";

		// ユーザが代理店の場合、所属する会社のIDを取得
		if (ManageAuthLevel.AGENCY.value().equals(userDto.authLevel)) {
			companyIdStr = listForm.assignedCompanyId;
		}

		try {
			customerCsvLogic.outputCsv(map, companyIdStr, DEFAULT_SORT_KEY);
		} catch (WNoResultException e) {
			log.warn("顧客CSV出力時、データが見つかりませんでした。", e);
			throw new ActionMessagesException("errors.app.dataNotFound");
		} catch (IOException e) {
			log.warn("顧客CSV出力時、ファイルの操作に失敗しました。", e);
			throw new ActionMessagesException("errors.app.csvOutPutFailed");
		}
		return null;
	}

	/**
	 * ページ遷移のボタン押下時の処理。
	 * 画面から指定されたページ数を数値化して検索を実行
	 * 不正なパラメータの場合は1をデフォルトページを表示する。
	 * @return 顧客検索JSPのパス
	 */
	@Execute(validator = false, urlPattern = "changePage/{pageNum}", reset = "resetId", input = TransitionConstants.Customer.JSP_APD01L01)
	@MethodAccess(accessCode = "CUSTOMER_LIST_CHANGEPAGE")
	public String changePage() {

		int targetPage = NumberUtils.toInt(listForm.pageNum, DEFAULT_PAGE);
		doSearch(targetPage);

		return TransitionConstants.Customer.JSP_APD01L01;
	}

	/**
	 * 最大表示件数変更時の処理。
	 * 画面から指定された最大表示件数を数値化して検索を実行
	 * 不正なパラメータの場合は20をデフォル表示件数として表示する。
	 * @return 顧客検索JSPのパス
	 */
	@Execute(validator = false, reset = "resetId", input = TransitionConstants.Customer.JSP_APD01L01)
	@MethodAccess(accessCode = "CUSTOMER_LIST_CHANGEMAXROW")
	public String changeMaxRow() {

		int targetPage = DEFAULT_PAGE;

		doSearch(targetPage);

		return TransitionConstants.Customer.JSP_APD01L01;
	}

	/**
	 * 別画面から再び検索に戻った場合に、遷移前の状態(=検索結果画面)を表示するために使用する
	 * 再検索メソッド
	 * @return 顧客検索JSPのパス
	 */
	@Execute(validator = false, reset = "resetId", input = TransitionConstants.Customer.JSP_APD01L01)
	@MethodAccess(accessCode = "CUSTOMER_LIST_SEARCHAGAIN")
	public String searchAgain() {

		int targetPage = NumberUtils.toInt(listForm.pageNum, DEFAULT_PAGE);

		// TODO
//		// フォームがクリアされていればエラー
//		if (StringUtil.isEmpty(listForm.where_areaCd)) {
//			// 「検索条件を再度選択してください。」
//			throw new ActionMessagesException("errors.app.selectAgainSearchValue");
//		}

		doSearch(targetPage);

		return TransitionConstants.Customer.JSP_APD01L01;
	}

	/**
	 * メルマガ確認画面から再び検索に戻った場合に、遷移前の状態(=検索結果画面)を表示するために使用する
	 * 再検索メソッド
	 * @return 顧客一覧画面
	 */
	@Execute(validator = false, input = TransitionConstants.Customer.JSP_APD01L01)
	@MethodAccess(accessCode = "CUSTOMER_LIST_BACKMAILMAG")
	public String backMailMag() {

		int targetPage = DEFAULT_PAGE;

		// TODO
//		// フォームがクリアされていればエラー
//		if (StringUtil.isEmpty(listForm.where_areaCd)) {
//			// 「検索条件を再度選択してください。」
//			throw new ActionMessagesException("errors.app.selectAgainSearchValue");
//		}

		doSearch(targetPage);

		return TransitionConstants.Customer.JSP_APD01L01;
	}

	/**
	 * メルマガ送信
	 * @return 顧客向けメルマガ配信先確認初期表示メソッド
	 */
	@Execute(validator = true, reset = "resetId", input = "/customer/list/searchAgain/")
	@MethodAccess(accessCode = "CUSTOMER_LIST_MAILMAGAZINE")
	public String mailMagazine() {

		// 顧客向けメルマガ配信先確認初期表示メソッドへ遷移
		return TransitionConstants.Customer.CUSTOMERMAILMAG_LIST_INDEX;
	}

	/**
	 * 検索メインロジック
	 * @param targetPage
	 */
	private void doSearch(int targetPage) {

		// 検索用パラメータを生成
		Map<String, String> map = new HashMap<String, String>();
		createParams(map);

		String companyIdStr = "";

		// ユーザが代理店の場合、所属する会社のIDを取得
		if (ManageAuthLevel.AGENCY.value().equals(userDto.authLevel)) {
			companyIdStr = listForm.assignedCompanyId;
		}

		int maxRow = getMaxRowNum();
		pageNavi = new PageNavigateHelper(DEFAULT_SORT_KEY, maxRow);
		if (maxRow == 0) {
			pageNavi.maxRow = 0;
		}

		try {
			List<MCustomer> entityList;

			entityList = customerService.doSearch(pageNavi, map, targetPage, companyIdStr);

			log.debug("顧客情報を取得");

			// 表示用にデータを生成
			convertShowList(entityList);

			listForm.setExistDataFlgOk();

		} catch (WNoResultException e) {
			listForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.dataNotFound");
		} catch (SNoResultException e) {
			throw new AuthorizationException();
		}
	}

	/**
	 * 検索条件をMapにセット
	 * @param map 検索条件Map
	 */
	private void createParams(Map<String, String> map) {

		map.put("areaCd", String.valueOf(MAreaConstants.AreaCd.SHUTOKEN_AREA));
		map.put("id", StringUtils.defaultString(listForm.where_customerId, ""));
		map.put("companyId", StringUtils.defaultString(listForm.assignedCompanyId, ""));
		map.put("salesId", StringUtils.defaultString(listForm.assignedSalesId, ""));
		map.put("customerName", StringUtils.defaultString(listForm.where_customerName, ""));
		map.put("contactName", StringUtils.defaultString(listForm.where_contactName, ""));
		map.put(CustomerService.MAIL_MAGAZINE_AREA_CD_LIST, StringUtils.defaultString(StringUtils.join(listForm.where_mailMagazineAreaCdList, ","),""));
		map.put(CustomerService.MAIL_ADDRESS_WHERE, listForm.where_mailAddress);
		map.put(CustomerService.SHOP_LIST_PREFECTURES_CD_LIST, StringUtils.defaultString(StringUtils.join(listForm.where_shopListPrefecturesCdList, ","),""));
		map.put(CustomerService.SHOP_LIST_SHUTOKEN_FOREIGN_AREA_KBN_LIST, StringUtils.defaultString(StringUtils.join(listForm.where_shopListShutokenForeignAreaKbnList, ","),""));
		map.put(CustomerService.SHOP_LIST_INDUSTRY_KBN_LIST, StringUtils.defaultString(StringUtils.join(listForm.where_shopListIndustryKbnList, ","),""));
		map.put(CustomerService.LOWER_SHOP_LIST_COUNT, StringUtils.defaultString(listForm.where_lowerShopListCount, ""));
		map.put(CustomerService.UPPER_SHOP_LIST_COUNT, StringUtils.defaultString(listForm.where_upperShopListCount, ""));
	}

	/**
	 * 表示用リストを生成
	 * @param entityList 営業担当者エンティティリスト
	 */
	private void convertShowList(List<MCustomer> entityList) {

		List<CustomerInfoDto> dtoList = new ArrayList<CustomerInfoDto>();

		for (MCustomer entity : entityList) {

			CustomerInfoDto dto = new CustomerInfoDto();
			Beans.copy(entity, dto).execute();

			if (!StringUtils.isEmpty(entity.phoneNo1)) {
				dto.phoneNo = entity.phoneNo1 + "-" + entity.phoneNo2 + "-" + entity.phoneNo3;
			}

			dto.companySalesDtoList = new ArrayList<CompanySalesDto>();
			for (MCustomerCompany customerCompany : entity.mCustomerCompanyList) {
				CompanySalesDto companySalesDto = new CompanySalesDto();
				companySalesDto.companyId = String.valueOf(customerCompany.companyId);
				companySalesDto.salesId = String.valueOf(customerCompany.salesId);
				dto.companySalesDtoList.add(companySalesDto);
			}

			// 系列店舗の都道府県
			dto.shopListPrefecturesCdList = shopListPrefecturesService.findByCustomerId(entity.id);

			// 系列店舗の海外エリア
			dto.shopListShutokenForeignAreaKbnList = shopListShutokenForeignAreaKbnService.findByCustomerId(entity.id);

			// 系列店舗の業態
			dto.shopListIndustryKbnList = shopListIndustryKbnService.findByCustomerId(entity.id);

			// 店舗数
			dto.shopCount = entity.vCustomerShopCount != null ? entity.vCustomerShopCount.count : 0;

			// メルマガ受信エリア
			dto.mailMagazineAreaCdList = customerMailMagazineAreaService.getAreaList(entity.id);

			dto.detailPath = GourmetCareeUtil.makePath("/customer/detail/", "index", String.valueOf(entity.id));

			// サブメールの登録
			if (CollectionUtils.isNotEmpty(entity.mCustomerSubMailList)) {
				dto.subMailList = new ArrayList<>();
				for (MCustomerSubMail subMail : entity.mCustomerSubMailList) {
					if (StringUtils.isNotEmpty(subMail.subMail)) {
						dto.subMailList.add(subMail.subMail + (subMail.submailReceptionFlg == 1 ? "　[可]" : "　[否]"));
					}
				}
			}

			dtoList.add(dto);
		}

		customerInfoDtoList = dtoList;

	}

	/**
	 * 最大表示件数を取得する
	 * @return
	 */
	private int getMaxRowNum() {
		if ("".equals(listForm.maxRow)) {
			return 0;
		}

		try {
			return NumberUtils.toInt(listForm.maxRow);
		} catch (NumberFormatException e) {
			listForm.maxRow = getCommonProperty("gc.customer.initMaxRow");
			return NumberUtils.toInt(listForm.maxRow, DEFAULT_MAX_ROW);
		}
	}

}