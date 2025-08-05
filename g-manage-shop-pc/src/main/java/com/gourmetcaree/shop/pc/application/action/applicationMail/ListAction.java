package com.gourmetcaree.shop.pc.application.action.applicationMail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.ResponseUtil;

import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.dto.LabelValueDto;
import com.gourmetcaree.common.logic.LabelValueListLogic;
import com.gourmetcaree.common.property.PagerProperty;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants.SendKbn;
import com.gourmetcaree.db.common.constants.MTypeConstants.SenderKbn;
import com.gourmetcaree.db.common.entity.TApplication;
import com.gourmetcaree.db.common.entity.TShopList;
import com.gourmetcaree.db.common.entity.TWebJob;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.common.service.ApplicationAttributeService;
import com.gourmetcaree.db.common.service.ApplicationService;
import com.gourmetcaree.db.common.service.MemberService;
import com.gourmetcaree.db.common.service.ShopListService;
import com.gourmetcaree.db.common.service.CustomerService;
import com.gourmetcaree.shop.logic.dto.ApplicationMailRetDto;
import com.gourmetcaree.shop.logic.dto.MailSelectDto;
import com.gourmetcaree.shop.logic.logic.ApplicationCsvLogic;
import com.gourmetcaree.shop.logic.logic.ApplicationLogic.MAIL_KBN;
import com.gourmetcaree.shop.logic.logic.WebdataLogic;
import com.gourmetcaree.shop.logic.property.ApplicationCsvProperty;
import com.gourmetcaree.shop.logic.property.MailSearchProperty;
import com.gourmetcaree.shop.pc.application.dto.applicationMail.ApplicationMailListDto;
import com.gourmetcaree.shop.pc.application.form.applicationMail.ListForm;
import com.gourmetcaree.shop.pc.sys.action.MailListAction;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;

import net.arnx.jsonic.JSON;


/**
 * 応募メール一覧を表示するアクションクラスです。
 * @author Takahiro Kimura
 * @version 1.0
 */
@ManageLoginRequired
public class ListAction extends MailListAction {

	/** 応募メール一覧フォーム */
	@ActionForm
	@Resource
	protected ListForm listForm;

	/** 応募サービス */
	@Resource
	protected ApplicationService applicationService;

	/** 求人ロジック */
	@Resource
	protected WebdataLogic webdataLogic;

	@Resource
	protected LabelValueListLogic labelValueListLogic;

	/** 応募CSVロジック */
	@Resource
	protected ApplicationCsvLogic applicationCsvLogic;

	@Resource
	private ApplicationAttributeService applicationAttributeService;

	@Resource
	private MemberService memberService;

	/** 店舗一覧サービス */
	@Resource
	private ShopListService shopListService;

	@Resource
	private CustomerService customerService;

	/** メールを保持するリスト */
	public List<ApplicationMailListDto> dataList;

	/** 今までに募集したことのある職種リスト */
	public List<LabelValueDto> recruitedJobKbnList;

	/** 今までに募集したことのある雇用形態リスト */
	public List<LabelValueDto> recruitedEmployPtnList;

	/** ページナビゲータ */
	public PageNavigateHelper pageNavi;

	public static final String APPLICATION_ID_MAP = "APPLICATION_ID_MAP";

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Application.JSP_SPC02L01)
	public String index() {

		return show();
	}

	/**
	 * 送信箱の初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Application.JSP_SPC02L01)
	public String sendBox() {

		listForm.sendKbn = SendKbn.SEND;
		createList(GourmetCareeConstants.DEFAULT_PAGE);
		checkUnReadMail();

		return TransitionConstants.Application.JSP_SPC02L01;
	}

	/**
	 * 検索
	 * @return
	 */
	@Execute(urlPattern = "search", reset = "resetForm", input = TransitionConstants.Application.JSP_SPC02L01)
	public String search() {

		setRecruitedList();
		createList(NumberUtils.toInt(listForm.pageNum));
		checkUnReadMail();

		return TransitionConstants.Application.JSP_SPC02L01;
	}

	/**
	 * 対象の求人のみの応募メール一覧
	 * @return
	 */
	@Execute(urlPattern = "focusWeb/{where_webId}", reset = "resetForm",  input = TransitionConstants.Application.JSP_SPC02L01)
	public String focusWeb() {

		setRecruitedList();
		createList(NumberUtils.toInt(listForm.pageNum));
		checkUnReadMail();

		return TransitionConstants.Application.JSP_SPC02L01;
	}


	/**
	 * ページ遷移
	 * 受信メール一覧用のページ遷移。
	 * @return
	 */
	@Execute(urlPattern = "changePage/{pageNum}", input = TransitionConstants.Application.JSP_SPC02L01)
	public String changePage() {
		setRecruitedList();
		createList(NumberUtils.toInt(listForm.pageNum));
		checkUnReadMail();
		return TransitionConstants.Application.JSP_SPC02L01;
	}

	/**
	 * 再表示<br>
	 * セッションをリセットせずに検索結果を一覧に表示します。<br>
	 * ※URL欄に再表示であることを明示しないように、メソッド名はあえて抽象的にしています。
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPC02L01)
	public String showList() {

		setRecruitedList();
		createList(NumberUtils.toInt(listForm.pageNum));
		checkUnReadMail();
		return TransitionConstants.Application.JSP_SPC02L01;
	}

	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPC02L01)
	public String doLumpChangeRead() {

		if(listForm.changeIdArray != null) {
			for(String applicationMailId : listForm.changeIdArray) {
				try {
					applicationLogic.changeMailToOpened(NumberUtils.toInt(applicationMailId));
				} catch (WNoResultException e) {
					throw new ActionMessagesException("errors.app.dataNotFound");
				}
			}
		} else {
			listForm.errorMessage = "応募者を選択してください。";
		}


		return "/applicationMail/list/showList?redirect=true";
	}

	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPC02L01)
	public String doLumpChangeUnRead() {

		if(listForm.changeIdArray != null) {
			for(String applicationMailId : listForm.changeIdArray) {
				try {
					applicationLogic.changeMailToUnOpened(NumberUtils.toInt(applicationMailId));
				} catch (WNoResultException e) {
					throw new ActionMessagesException("errors.app.dataNotFound");
				}
			}
		} else {
			listForm.errorMessage = "応募者を選択してください。";
		}

		return "/applicationMail/list/showList?redirect=true";
	}

	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPC02L01)
	public String doLumpChangeUnDisplay() {

		if(listForm.changeIdArray != null) {
			List<Integer> mailIds = Arrays.asList(listForm.changeIdArray).stream()
					.map(s -> Integer.parseInt(s))
					.collect(Collectors.toList());

			applicationLogic.changeMailToUnDisplay(mailIds);
		} else {
			listForm.errorMessage = "応募者を選択してください。";
		}

		return "/applicationMail/list/showList?redirect=true";
	}

	/**
	 * ステータスの変更
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPC02L01)
	public String ajaxSelectionFlg() {

		TApplication entity = new TApplication();
		entity.id = listForm.applicationId;

		//不正な値を防ぐため値をチェック。
		Map<String, String> selectionMap = MTypeConstants.SelectionFlg.SELECTION_COLOR_STRING_MAP;
		if (StringUtils.isNotBlank(selectionMap.get(listForm.selectionKbn))) {
			entity.selectionFlg = Integer.parseInt(listForm.selectionKbn);
		} else {
			entity.selectionFlg = STATUS_DEFAULT_VALUE;
		}

		applicationService.updateIncludesVersion(entity);

		String json = JSON.encode(MTypeConstants.SelectionFlg.SELECTION_COLOR_STRING_MAP);
		ResponseUtil.write(json, "application/json", "UTF-8");

		return null;
	}


	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPC02L01)
	public String editMemo() {

		TApplication entity = new TApplication();
		entity.id = listForm.applicationId;
		entity.memo = listForm.memo;
		entity.memoUpdateDatetime = new Date();

		applicationService.updateIncludesVersion(entity);

		return null;
	}

	/**
	 * CSV出力
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPC02L01)
	public String outputCsv() {

		if(listForm.changeIdArray == null) {
			listForm.errorMessage = "応募者を選択してください。";
			return "/applicationMail/list/showList?redirect=true";
		}

		Map<Integer,Integer> applicationIdMap = (Map<Integer,Integer>)session.getAttribute(APPLICATION_ID_MAP);
		List<Integer> applicationIds = new ArrayList<>();

		for(String id : listForm.changeIdArray) {
			applicationIds.add(applicationIdMap.get(Integer.valueOf(id)));
		}

		ApplicationCsvProperty property = new ApplicationCsvProperty();
		property.pass = getCommonProperty("gc.csv.filepass");
		property.faileName = getCommonProperty("gc.application.csv.filename");
		property.encode = getCommonProperty("gc.csv.encoding");

		try {
			applicationCsvLogic.outputApplicationMailUsersCsv(applicationIds, property);
		} catch (IOException e) {
			listForm.errorMessage = "CSV出力中にエラーが発生しました。";
			return "/applicationMail/list/showList?redirect=true";
		}

		return null;
	}

	/**
	 * 初期表示遷移用
	 * @return
	 */
	private String show() {

		listForm.sendKbn = SendKbn.RECEIVE;
		listForm.where_displayCount = "20";
		setRecruitedList();
		createList(GourmetCareeConstants.DEFAULT_PAGE);
		checkUnReadMail();
		return TransitionConstants.Application.JSP_SPC02L01;
	}



	/**
	 * 一覧表示用のデータを作成します。
	 * @param targetPage
	 */
	private void createList(int targetPage) {

		PagerProperty property = new PagerProperty();
		property.targetPage = targetPage;
		if(StringUtils.isNotBlank(listForm.where_displayCount)) {
			property.maxRow = Integer.parseInt(listForm.where_displayCount);
		} else {
			property.maxRow = getInitMaxRow();
		}

		List<ApplicationMailListDto> tmpList = new ArrayList<ApplicationMailListDto>();

		try {

			//セッション上の値が不正の場合は受信箱を表示する
			if (listForm.sendKbn != SendKbn.SEND
					&& listForm.sendKbn != SendKbn.RECEIVE) {
				listForm.sendKbn = SendKbn.RECEIVE;
			}

			MailSearchProperty searchProperty = new MailSearchProperty();
			if(listForm.sendKbn == SendKbn.RECEIVE) {
				Beans.copy(listForm, searchProperty).excludes("where_mailStatus", "where_areaCd").execute();
				searchProperty.where_mailStatus = GourmetCareeUtil.convertStringArrayToIntegerList(GourmetCareeUtil.removeBlankElement(listForm.where_mailStatus));
				searchProperty.where_areaCd = GourmetCareeUtil.convertStringArrayToIntegerList(GourmetCareeUtil.removeBlankElement(listForm.where_areaCd));
			}

			ApplicationMailRetDto retDto = applicationLogic.getApplicationMailListData(property, listForm.sendKbn, searchProperty);
			Map<Integer, Integer> applicationIdMap = new HashedMap();
			for (MailSelectDto entity : retDto.retList) {

				ApplicationMailListDto dto = new ApplicationMailListDto();
				Beans.copy(entity, dto).execute();

				switch(entity.areaCd) {
					case 1:
						dto.areaName = "首都圏";
						break;
					case 2:
						dto.areaName = "東北";
						break;
					case 3:
						dto.areaName = "関西";
						break;
					case 4:
						dto.areaName = "東海";
						break;
					case 5:
						dto.areaName = "九州・沖縄";
						break;
				}

				if(listForm.sendKbn == SendKbn.RECEIVE) {
					dto.age = GourmetCareeUtil.convertToAge(entity.birthday);

					int[] arr = applicationAttributeService.getApplicationAttrValue(entity.applicationId, "food_exp_kbn");

					if(arr.length > 0) {
						dto.foodExpKbn = arr[0];
					}

					dto.sendDatetime = GourmetCareeUtil.convertSendDateTimeForMailList(entity.sendDatetime);

					applicationIdMap.put(entity.id, entity.applicationId);

					dto.unsubscribeFlg = false;
					if(entity.memberId != null) {
						try {
							memberService.findById(entity.memberId);
						} catch(SNoResultException e) {
							dto.unsubscribeFlg = true;
						}
					}
				}

				// 応募店舗IDが登録されている場合、店舗名を取得
				if (entity.shopListId != null) {
					TShopList tShopList = shopListService.findByIdAllowNoResult(entity.shopListId);
					if (tShopList != null){
						dto.fromApplicationName = tShopList.shopName;
					} else {
						// 未削除から店舗情報が見つからない場合は削除済み含め取得
						dto.fromApplicationName = shopListService.findByIdIncludeDelete(entity.shopListId).shopName + "（店舗データ無し）";
					}
				} else {
					if (listForm.sendKbn == SenderKbn.CUSTOMER) {
						dto.fromApplicationName = customerService.findById(entity.fromId).customerName;
					} else {
						dto.fromApplicationName = customerService.findById(entity.toId).customerName;
					}
				}

				tmpList.add(dto);
			}

			dataList = tmpList;
			pageNavi = retDto.pageNavi;

			listForm.setExistDataFlgOk();

			session.setAttribute(APPLICATION_ID_MAP, applicationIdMap);

		} catch (WNoResultException e) {
			dataList = tmpList;
			pageNavi = new PageNavigateHelper(GourmetCareeConstants.DEFAULT_MAX_ROW_INT);

			listForm.setExistDataFlgNg();

			throw new ActionMessagesException("errors.app.mailDataNotFound");
		}
	}

	/**
	 * 過去に募集したことのある職種、雇用形態のリストを設定
	 */
	private void setRecruitedList() {
		List<TWebJob> jobList = webdataLogic.getRecruitedList();

		if(jobList.size() > 0) {
			recruitedJobKbnList = new ArrayList<>();
			recruitedEmployPtnList = new ArrayList<>();
			List<LabelValueDto> jobKbnList = labelValueListLogic.getTypeList("job_kbn", "すべて", "", new ArrayList<>());
			List<LabelValueDto> employPtnKbnList =labelValueListLogic.getTypeList("employ_ptn_kbn", "すべて", "", new ArrayList<>());


			for(TWebJob j : jobList) {
				if(j.jobKbn != null) {
					for(LabelValueDto job : jobKbnList) {
						if((job.getLabel().equals("すべて") || Integer.valueOf(job.getValue()).equals(j.jobKbn)) && !recruitedJobKbnList.contains(job)) {
							recruitedJobKbnList.add(job);
						}
					}
				}
				if(j.employPtnKbn != null) {
					for(LabelValueDto employ : employPtnKbnList) {
						if((employ.getLabel().equals("すべて") || Integer.valueOf(employ.getValue()).equals(j.employPtnKbn)) && !recruitedEmployPtnList.contains(employ)) {
							recruitedEmployPtnList.add(employ);
						}
					}
				}
			}
		} else {
			recruitedJobKbnList = labelValueListLogic.getTypeList("job_kbn", "すべて", "", new ArrayList<>());
			recruitedEmployPtnList = labelValueListLogic.getTypeList("employ_ptn_kbn", "すべて", "", new ArrayList<>());
		}
	}


	/**
	 * 最大件数切替の初期値をプロパティから取得します。<br />
	 * 取得できない場合は、システム全体の初期値を設定
	 * @return 最大表示件数の初期値
	 */
	private int getInitMaxRow() {
		return  NumberUtils.toInt(getCommonProperty("gc.application.mail.initMaxRow"),
				GourmetCareeConstants.DEFAULT_MAX_ROW_INT);
	}


	public MAIL_KBN getMailKbn() {
		return MAIL_KBN.APPLICATION_MAIL;
	}

}
