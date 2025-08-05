package com.gourmetcaree.admin.pc.subWindow.action.customerSearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.subWindow.dto.customerSearch.CustomerInfoDto;
import com.gourmetcaree.admin.pc.subWindow.form.customerSearch.SelectForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.AuthorizationException;
import com.gourmetcaree.common.logic.ValueToNameConvertLogic;
import com.gourmetcaree.db.common.entity.MCustomer;
import com.gourmetcaree.db.common.entity.MCustomerCompany;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.common.service.CustomerService;
import com.gourmetcaree.db.common.service.CustomerSubMailService;
import com.gourmetcaree.db.common.service.SalesService;

/**
 * サブウィンドウで顧客データ検索のアクション
 * @author Kstsutoshi Hasegawa
 * @version 1.0
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN, ManageAuthLevel.STAFF, ManageAuthLevel.AGENCY, ManageAuthLevel.OTHER, ManageAuthLevel.SALES})
public class SelectAction extends PcAdminAction {

//	/** ログオブジェクト */
//	private static Logger log = Logger.getLogger(SelectAction.class);

	/** デフォルトの表示ページ */
	private static final int DEFAULT_PAGE = 1;

	/** デフォルトの表示件数 */
	private static final int DEFAULT_MAX_ROW = 20;

	/** PageNavigatorにセットするデフォルトのソートキー */
	private static final String DEFAULT_SORT_KEY = "id desc, company_id, sales_id";

	/** 顧客データ検索フォーム */
	@ActionForm
	@Resource
	protected SelectForm selectForm;

	/** 顧客マスタサービス */
	@Resource
	protected CustomerService customerService;

	/** 営業担当者サービス */
	@Resource
	protected SalesService salesService;

	/** 値から名前の文字列に変換するロジッククラス */
	@Resource
	protected ValueToNameConvertLogic valueToNameConvertLogic;

	/** ページナビゲーター */
	public PageNavigateHelper pageNavi;

	/** 顧客データ検索結果DTO */
	public List<CustomerInfoDto> customerInfoList;

	@Resource
	private CustomerSubMailService customerSubMailService;


	/**
	 * 初期表示時の処理
	 * @return
	 */
	@Execute(validator = false, reset="resetForm", urlPattern = "index/{dispCd}", input=TransitionConstants.SubWindow.JSP_APN01L01)
	@MethodAccess(accessCode="CUSTOMERSEARCH_SELECT_INDEX")
	public String index() {
		return show();
	}

	/**
	 * 初期表示遷移用
	 * @return 顧客データ検索画面のパス
	 */
	private String show() {

		try {
			// ログインユーザが代理店の場合
			// 担当会社にユーザの会社IDをセット
			if (ManageAuthLevel.AGENCY.value().equals(userDto.authLevel)) {

				selectForm.assignedCompanyId = String.valueOf(salesService.getCompanyIdBySalesId(NumberUtils.toInt(userDto.userId)));
			}
		} catch (SNoResultException e) {
			throw new AuthorizationException("代理店ユーザの会社ID取得に失敗しました。");
		}

		selectForm.setExistDataFlgNg();

		// 顧客データ検索画面へ遷移
		return TransitionConstants.SubWindow.JSP_APN01L01;
	}

	/**
	 * 検索ボタン押下時の処理
	 * @return 顧客データ検索画面のパス
	 */
	@Execute(validator = true, input=TransitionConstants.SubWindow.JSP_APN01L01)
	@MethodAccess(accessCode="CUSTOMERSEARCH_SELECT_SEARCH")
	public String search() {

		//ページナビゲータを初期化
		selectForm.maxRow = String.valueOf(getMaxRowNum());

		doSearch(DEFAULT_PAGE);
		// 顧客データ検索画面へ遷移
		return TransitionConstants.SubWindow.JSP_APN01L01;
	}

	/**
	 * ページ遷移のボタン押下時の処理。
	 * 画面から指定されたページ数を数値化して検索を実行
	 * 不正なパラメータの場合は1をデフォルトページを表示する。
	 * @return 顧客検索JSPのパス
	 */
	@Execute(validator = false, urlPattern = "changePage/{pageNum}", input=TransitionConstants.SubWindow.JSP_APN01L01)
	@MethodAccess(accessCode="CUSTOMERSEARCH_SELECT_CHANGEPAGE")
	public String changePage() {

		int targetPage = NumberUtils.toInt(selectForm.pageNum, DEFAULT_PAGE);
		doSearch(targetPage);

		return TransitionConstants.SubWindow.JSP_APN01L01;
	}

	/**
	 * 最大表示件数変更時の処理。
	 * 画面から指定された最大表示件数を数値化して検索を実行
	 * 不正なパラメータの場合は20をデフォル表示件数として表示する。
	 * @return 顧客検索JSPのパス
	 */
	@Execute(validator = false, input=TransitionConstants.SubWindow.JSP_APN01L01)
	@MethodAccess(accessCode="CUSTOMERSEARCH_SELECT_CHANGEMAXROW")
	public String changeMaxRow() {

		int targetPage = DEFAULT_PAGE;

		doSearch(targetPage);

		return TransitionConstants.SubWindow.JSP_APN01L01;
	}

	/**
	 * 検索メインロジック
	 */
	private void doSearch(int targetPage) {

		// 検索用パラメータを生成
		Map<String, String> map = new HashMap<String, String>();
		createParams(map);

		String companyIdStr = "";

		// ユーザが代理店の場合、所属する会社のIDを取得
		if (ManageAuthLevel.AGENCY.value().equals(userDto.authLevel)) {
			companyIdStr = selectForm.assignedCompanyId;
		}

		pageNavi = new PageNavigateHelper(DEFAULT_SORT_KEY, getMaxRowNum());

		try {
			List<MCustomer> entityList;

			entityList = customerService.doSearch(pageNavi, map, targetPage, companyIdStr);

			// 表示用にデータを生成
			convertShowList(entityList);

			selectForm.setExistDataFlgOk();

		} catch (WNoResultException e) {
			selectForm.setExistDataFlgNg();
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

		map.put("id", StringUtils.defaultString(selectForm.where_customerId, ""));
		map.put("companyId", StringUtils.defaultString(selectForm.assignedCompanyId, ""));
		map.put("salesId", StringUtils.defaultString(selectForm.assignedSalesId, ""));
		map.put("customerName", StringUtils.defaultString(selectForm.where_customerName, ""));
		map.put("contactName", StringUtils.defaultString(selectForm.where_contactName, ""));
	}

	/**
	 * 表示用リストを生成
	 * @param entityList 営業担当者エンティティリスト
	 */
	private void convertShowList(List<MCustomer> entityList) {

		List<CustomerInfoDto> dtoList = new ArrayList<CustomerInfoDto>();

		for (MCustomer entity : entityList) {

			CustomerInfoDto dto = new CustomerInfoDto();
			// サブメールは受信可否で判断
			Beans.copy(entity, dto).excludes("subMail").execute();
			dto.areaName = valueToNameConvertLogic.convertToAreaName(new String[] {String.valueOf(entity.areaCd)});

			if (!StringUtils.isEmpty(entity.phoneNo1)) {
				dto.phoneNo = entity.phoneNo1 + "-" + entity.phoneNo2 + "-" + entity.phoneNo3;
			}

			// サブメールの取得
			dto.subMailList = customerSubMailService.getReceptionSubMail(entity.id);

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < entity.mCustomerCompanyList.size(); i++) {
				MCustomerCompany customerCompany = entity.mCustomerCompanyList.get(i);
				if (i != 0) {
					sb.append("<br />");
				}
				sb.append(valueToNameConvertLogic.convertToCompanyName(new String[]{String.valueOf(customerCompany.companyId)}));
				sb.append("：");
				sb.append(valueToNameConvertLogic.convertToSalesName(new String[]{String.valueOf(customerCompany.salesId)}));
			}

			dto.companySalesName = sb.toString();

			dtoList.add(dto);
		}

		customerInfoList = dtoList;

	}

	/**
	 * 最大表示件数を取得する
	 * @return
	 */
	private int getMaxRowNum() {
		try {
			return Integer.parseInt(selectForm.maxRow);
		} catch (NumberFormatException e) {
			return NumberUtils.toInt(getCommonProperty("gc.customer.initMaxRow"), DEFAULT_MAX_ROW);
		}
	}
}
