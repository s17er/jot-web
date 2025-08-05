package com.gourmetcaree.admin.pc.shopList.action.shopList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.ActionMessagesUtil;

import com.gourmetcaree.admin.pc.shopList.dto.shopList.InputViewDto;
import com.gourmetcaree.admin.pc.shopList.dto.shopList.ShopListSearchDto;
import com.gourmetcaree.admin.pc.shopList.form.shopList.InputJobCsvForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TShopChangeJobCondition;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.common.service.ShopListMaterialNoDataService;


@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN, ManageAuthLevel.STAFF, ManageAuthLevel.AGENCY, ManageAuthLevel.OTHER, ManageAuthLevel.SALES})
public class InputJobCsvAction extends ShopListBaseAction {

	/** ログオブジェクト */
	private static final Logger log = Logger.getLogger(InputJobCsvAction.class);

	/** 一覧表示最大数の初期値 */
	private static final int DEFALT_MAX_ROW = 10001;

	/** 店舗一覧同時登録最大数 */
	private static final int MAX_REGISTER_NUM = 10001;

	/** 最大表示件数維持キー */
	private static final String KEPP_MAX_REGISTER_KEY = "keep_max_register_key";

	/** アクションフォーム */
	@ActionForm
	@Resource
	private InputJobCsvForm inputJobCsvForm;

	/** 店舗一覧データ無し画像サービス */
	@Resource
	private ShopListMaterialNoDataService shopListMaterialNoDataService;

	/** 店舗一覧リスト */
	public List<ShopListSearchDto> shopList;

	/** ページナビ */
	public PageNavigateHelper pageNavi;

	public InputViewDto viewDto;

	/**
	 * 初期表示メソッド
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", urlPattern = "index/{customerId}", input = TransitionConstants.ShopList.JSP_APQ05L01)
	@MethodAccess(accessCode="SHOPLIST_INPUTCSV_INDEX")
	public String index() {
		checkArgsNull(NO_BLANK_FLG_NG, inputJobCsvForm.customerId);
		ActionMessages errors = new ActionMessages();
		return show(errors);
	}

	/**
	 * 登録
	 * @return
	 */
	@Execute(validator=false, reset="resetCheckBox", input = TransitionConstants.ShopList.JSP_APQ07L01)
	@MethodAccess(accessCode="SHOPLIST_INPUTCSV_SUBMIT")
	public String submit() {

		List<TShopChangeJobCondition> insertList = new ArrayList<>();
		List<Integer> employList = new ArrayList<>();
		List<Integer> jobList = new ArrayList<>();

		for(Map<String, Object> map : inputJobCsvForm.csvMapList) {

			TShopChangeJobCondition entity = new TShopChangeJobCondition();

			entity.shopListId = NumberUtils.toInt(String.valueOf(map.get("shopListId")));
			entity.employPtnKbn = NumberUtils.toInt(String.valueOf(map.get("employPtnKbn")));
			entity.jobKbn = NumberUtils.toInt(String.valueOf(map.get("jobKbn")));
			entity.saleryStructureKbn = NumberUtils.toInt(String.valueOf(map.get("saleryStructureKbn")));
			entity.lowerSalaryPrice = String.valueOf(map.get("lowerSalaryPrice"));
			entity.upperSalaryPrice = String.valueOf(map.get("upperSalaryPrice"));
			entity.salary = String.valueOf(map.get("salary"));
			entity.salaryDetail = String.valueOf(map.get("salaryDetail"));
			entity.annualLowerSalaryPrice = String.valueOf(map.get("annualLowerSalaryPrice"));
			entity.annualUpperSalaryPrice = String.valueOf(map.get("annualUpperSalaryPrice"));
			entity.annualSalary = String.valueOf(map.get("annualSalary"));
			entity.annualSalaryDetail = String.valueOf(map.get("annualSalaryDetail"));
			entity.monthlyLowerSalaryPrice = String.valueOf(map.get("monthlyLowerSalaryPrice"));
			entity.monthlyUpperSalaryPrice = String.valueOf(map.get("monthlyUpperSalaryPrice"));
			entity.monthlySalary = String.valueOf(map.get("monthlySalary"));
			entity.monthlySalaryDetail = String.valueOf(map.get("monthlySalaryDetail"));

			insertList.add(entity);
			if(employList.indexOf(NumberUtils.toInt(String.valueOf(map.get("employPtnKbn")))) == -1) {
				employList.add(NumberUtils.toInt(String.valueOf(map.get("employPtnKbn"))));
			}

			if(jobList.indexOf(NumberUtils.toInt(String.valueOf(map.get("jobKbn")))) == -1) {
				jobList.add(NumberUtils.toInt(String.valueOf(map.get("jobKbn"))));
			}
		}

		try {
			shopChangeJobConditionService.allDeleteInsert(NumberUtils.toInt(inputJobCsvForm.customerId), sortInsertList(insertList, employList, jobList));
		} catch (WNoResultException e) {
			throw new ActionMessagesException("errors.app.dataNotFound");
		}

		log.info("店舗一覧職種を一括登録しました。顧客ID=" + inputJobCsvForm.customerId + "：登録者：" + userDto);
		return TransitionConstants.ShopList.REDIRECT_SHOPLIST_JOB_CSV_COMPLETE;
	}

	/**
	 * 完了画面
	 * @return
	 */
	@Execute(validator = false, reset = "resetFormWithoutCustomerId", input = TransitionConstants.ShopList.JSP_APQ05C03)
	@MethodAccess(accessCode="SHOPLIST_INPUTCSV_COMP")
	public String comp() {
		session.removeAttribute(SHOP_LIST_JOB_TEMP_LIST_SESSION_KEY);
		return TransitionConstants.ShopList.JSP_APQ07C03;
	}

	/**
	 * 店舗一覧へ戻る
	 * @return
	 */
	@Execute(validator=false, reset="resetFormWithoutCustomerId", input = TransitionConstants.ShopList.JSP_APQ07L01)
	@MethodAccess(accessCode="SHOPLIST_INPUTCSV_BACKINDEX")
	public String backToIndex() {
		String backPath = createReindexPath(inputJobCsvForm.customerId);
		inputJobCsvForm.customerId = null;
		session.removeAttribute(SHOP_LIST_JOB_TEMP_LIST_SESSION_KEY);
		return backPath;
	}

	/**
	 * 店舗一覧へ戻る
	 * @return
	 */
	@Execute(validator=false, reset="resetFormWithoutCustomerId", input = TransitionConstants.ShopList.JSP_APQ07L01)
	@MethodAccess(accessCode="SHOPLIST_INPUTCSV_BACKINDEX")
	public String backToShopList() {
		String backPath = createListIndexPath(inputJobCsvForm.customerId);
		inputJobCsvForm.customerId = null;
		session.removeAttribute(SHOP_LIST_JOB_TEMP_LIST_SESSION_KEY);
		return backPath;
	}

	/**
	 * 初期表示遷移メソッド
	 * @return
	 */
	private String show(ActionMessages errors) {
		int maxRow = 0;

		if(StringUtils.isNotEmpty((String)session.getAttribute(KEPP_MAX_REGISTER_KEY))) {
			maxRow = NumberUtils.toInt((String)session.getAttribute(KEPP_MAX_REGISTER_KEY));
			inputJobCsvForm.maxRow = (String)session.getAttribute(KEPP_MAX_REGISTER_KEY);
			session.removeAttribute(KEPP_MAX_REGISTER_KEY);
		}else {
			if(StringUtils.isEmpty(inputJobCsvForm.maxRow)) {
				maxRow = DEFALT_MAX_ROW;
			} else {
				maxRow = NumberUtils.toInt(inputJobCsvForm.maxRow);
			}
		}

		createTempList(errors, maxRow);

		return TransitionConstants.ShopList.JSP_APQ07L01;
	}

	/**
	 * 一時保存リスト作成
	 */
	@SuppressWarnings("rawtypes")
	private void createTempList(ActionMessages errors, int maxRow) {

		Object obj = getObjectFromSession(SHOP_LIST_JOB_TEMP_LIST_SESSION_KEY);
		if (obj == null) {
			throw new FraudulentProcessException("店舗一覧の一時保存給与変更リストがみつかりません。");
		}

		List<Map<String, Object>> mapList = new ArrayList<Map<String,Object>>();
		List<String> duplicationCheckList = new ArrayList<>();

		int index = 1;
		for (Object data : (List)obj) {
			Map<String, Object> map = new HashMap<String, Object>();
			if(data instanceof TShopChangeJobCondition) {
				TShopChangeJobCondition entity = (TShopChangeJobCondition)data;
				map.put("shopListId", entity.shopListId);
				if(entity.employPtnKbn != null) {
					map.put("employPtnKbnName", valueToNameConvertLogic.convertToTypeName(MTypeConstants.EmployPtnKbn.TYPE_CD, entity.employPtnKbn));
				} else {
					map.put("employPtnKbnName", "");
				}
				map.put("employPtnKbn", entity.employPtnKbn);
				if(entity.jobKbn != null) {
					map.put("jobKbnName", valueToNameConvertLogic.convertToTypeName(MTypeConstants.JobKbn.TYPE_CD, entity.jobKbn));
				} else {
					map.put("jobKbnName", "");
				}
				map.put("jobKbn", entity.jobKbn);
				if(entity.saleryStructureKbn != null) {
					map.put("saleryStructureKbnName", valueToNameConvertLogic.convertToTypeName(MTypeConstants.SaleryStructureKbn.TYPE_CD, entity.saleryStructureKbn));
				} else {
					map.put("saleryStructureKbnName", "");
				}
				map.put("saleryStructureKbn", entity.saleryStructureKbn);
				map.put("lowerSalaryPrice", entity.lowerSalaryPrice);
				map.put("upperSalaryPrice", entity.upperSalaryPrice);
				map.put("salary", entity.salary);
				map.put("salaryDetail", entity.salaryDetail);
				map.put("annualLowerSalaryPrice", entity.annualLowerSalaryPrice);
				map.put("annualUpperSalaryPrice", entity.annualUpperSalaryPrice);
				map.put("annualSalary", entity.annualSalary);
				map.put("annualSalaryDetail", entity.annualSalaryDetail);
				map.put("monthlyLowerSalaryPrice", entity.monthlyLowerSalaryPrice);
				map.put("monthlyUpperSalaryPrice", entity.monthlyUpperSalaryPrice);
				map.put("monthlySalary", entity.monthlySalary);
				map.put("monthlySalaryDetail", entity.monthlySalaryDetail);

				mapList.add(map);
				checkInputCsvData(entity, duplicationCheckList, errors, index);
				if(entity.shopListId != null && entity.employPtnKbn != null && entity.jobKbn != null
						&& duplicationCheckList.indexOf(entity.shopListId + "-" + entity.employPtnKbn + "-" + entity.jobKbn) == -1) {
					duplicationCheckList.add(entity.shopListId + "-" + entity.employPtnKbn + "-" + entity.jobKbn);
				}
				index++;
			}
		}

		inputJobCsvForm.csvMapList = mapList;

		if (mapList.size() > MAX_REGISTER_NUM) {
			errors.add("errors", new ActionMessage("errors.app.registerShopListOverLimit", MAX_REGISTER_NUM));
		}
		if (!errors.isEmpty()) {
			ActionMessagesUtil.addErrors(request, errors);
			inputJobCsvForm.errorFlg = true;
		}

	}

	private void checkInputCsvData(TShopChangeJobCondition entity, List<String> duplicationCheckList, ActionMessages errors, int index) {

		if (entity.shopListId == null) {
			errors.add("errors", new ActionMessage("errors.app.requiredShopListId", index));
		} else {
			try {
				shopListService.findByIdAndCustomerId(entity.shopListId, NumberUtils.toInt(inputJobCsvForm.customerId));
			} catch (WNoResultException e) {
				errors.add("errors", new ActionMessage("errors.app.requiredShopListId", index));
			}
		}

		if (entity.employPtnKbn == null) {
			errors.add("errors", new ActionMessage("errors.app.requiredEmployPtnKbn", index));
		} else {
			if(!typeService.existType(MTypeConstants.EmployPtnKbn.TYPE_CD, entity.employPtnKbn)) {
				errors.add("errors", new ActionMessage("errors.app.noExitEmployPtnKbn", index));
			}
		}

		if (entity.jobKbn == null) {
			errors.add("errors", new ActionMessage("errors.app.requiredJobKbn", index));
		} else {
			if(!typeService.existType(MTypeConstants.JobKbn.TYPE_CD, entity.jobKbn)) {
				errors.add("errors", new ActionMessage("errors.app.noExitJobKbn", index));
			}
		}

		if(duplicationCheckList.indexOf(entity.shopListId + "-" + entity.employPtnKbn + "-" + entity.jobKbn) != -1) {
			errors.add("errors", new ActionMessage("errors.app.duplication", index));
		}

		if(!GourmetCareeUtil.eqInt(MTypeConstants.SaleryStructureKbn.DAILY, entity.saleryStructureKbn)
				&& !GourmetCareeUtil.eqInt(MTypeConstants.SaleryStructureKbn.HOURLY, entity.saleryStructureKbn)
				&& !GourmetCareeUtil.eqInt(MTypeConstants.SaleryStructureKbn.MONTHLY, entity.saleryStructureKbn)
				&& !GourmetCareeUtil.eqInt(MTypeConstants.SaleryStructureKbn.ANNUAL, entity.saleryStructureKbn)) {
			errors.add("errors", new ActionMessage("errors.app.selectSaleryStructureKbn", index));
		}

		if(StringUtils.isBlank(entity.lowerSalaryPrice) && StringUtils.isBlank(entity.upperSalaryPrice)) {
			errors.add("errors", new ActionMessage("errors.app.requiredSalaryPrice", index));
		}
	}


	private List<TShopChangeJobCondition> sortInsertList(List<TShopChangeJobCondition> insertList,
			List<Integer> employList, List<Integer> jobList) throws WNoResultException {

		List<TShopChangeJobCondition> result = new ArrayList<>();

		Map<Integer, List<TShopChangeJobCondition>> employMap = new HashMap<>();

		List<Integer> sortedEmployValueList = typeService.getSortedTypeValueList(MTypeConstants.EmployPtnKbn.TYPE_CD, employList);
		List<Integer> sortedJobValueList = typeService.getSortedTypeValueList(MTypeConstants.JobKbn.TYPE_CD, jobList);


		for(Integer employ : sortedEmployValueList) {
			List<TShopChangeJobCondition> list = new ArrayList<>();
			for(TShopChangeJobCondition entity : insertList) {
				if(employ.equals(entity.employPtnKbn)) {
					list.add(entity);
				}
			}
			employMap.put(employ, list);
		}

		int index = 0;
		for(Integer employ : sortedEmployValueList) {
			for(Integer job : sortedJobValueList) {
				for(TShopChangeJobCondition entity : (List<TShopChangeJobCondition>)employMap.get(employ)) {
					if(job.equals(entity.jobKbn)) {
						result.add(index, entity);
						index++;
					}
				}
			}
		}

		return result;
	}
}
