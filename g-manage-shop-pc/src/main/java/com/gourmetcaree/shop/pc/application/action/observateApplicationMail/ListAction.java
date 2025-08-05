package com.gourmetcaree.shop.pc.application.action.observateApplicationMail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

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
import com.gourmetcaree.common.property.PagerProperty;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants.SendKbn;
import com.gourmetcaree.db.common.entity.TObservateApplication;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.common.service.ApplicationAttributeService;
import com.gourmetcaree.db.common.service.MemberService;
import com.gourmetcaree.db.common.service.ObservateApplicationService;
import com.gourmetcaree.shop.logic.dto.ApplicationMailRetDto;
import com.gourmetcaree.shop.logic.dto.MailSelectDto;
import com.gourmetcaree.shop.logic.logic.ApplicationLogic.MAIL_KBN;
import com.gourmetcaree.shop.logic.property.MailSearchProperty;
import com.gourmetcaree.shop.pc.application.dto.applicationMail.ApplicationMailListDto;
import com.gourmetcaree.shop.pc.application.form.observateApplicationMail.ListForm;
import com.gourmetcaree.shop.pc.sys.action.MailListAction;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.shop.pc.valueobject.MenuInfo;

import net.arnx.jsonic.JSON;

/**
 * 店舗見学応募メール一覧を表示するアクションクラスです。
 * @author Takehiro Nakamori
 * @version 1.0
 */
@ManageLoginRequired
public class ListAction extends MailListAction {

	/** アクションフォーム */
	@ActionForm
	@Resource
	private ListForm listForm;

	@Resource
	ObservateApplicationService observateApplicationService;

	@Resource
	private ApplicationAttributeService applicationAttributeService;

	@Resource
	private MemberService memberService;

	/** メールを保持するリスト */
	public List<ApplicationMailListDto> dataList;

	/** ページナビゲータ */
	public PageNavigateHelper pageNavi;


	/**
	 * 最大件数切替の初期値をプロパティから取得します。<br />
	 * 取得できない場合は、システム全体の初期値を設定
	 * @return 最大表示件数の初期値
	 */
	private int getInitMaxRow() {
		return  NumberUtils.toInt(getCommonProperty("gc.observateApplication.mail.initMaxRow"), GourmetCareeConstants.DEFAULT_MAX_ROW_INT);
	}

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Application.JSP_SPC04L01)
	public String index() {

		return show();
	}

	/**
	 * 初期表示遷移用
	 * @return
	 */
	private String show() {

		listForm.sendKbn = SendKbn.RECEIVE;
		listForm.where_displayCount = "20";
		createList(GourmetCareeConstants.DEFAULT_PAGE);
		checkUnReadMail();

		return TransitionConstants.Application.JSP_SPC04L01;
	}

	/**
	 * 送信箱の初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Application.JSP_SPC04L01)
	public String sendBox() {

		listForm.sendKbn = SendKbn.SEND;
		createList(GourmetCareeConstants.DEFAULT_PAGE);
		checkUnReadMail();

		return TransitionConstants.Application.JSP_SPC04L01;
	}

	/**
	 * 検索
	 * @return
	 */
	@Execute(urlPattern = "search", reset = "resetForm", input = TransitionConstants.Application.JSP_SPC04L01)
	public String search() {

		createList(NumberUtils.toInt(listForm.pageNum));
		checkUnReadMail();

		return TransitionConstants.Application.JSP_SPC04L01;
	}

	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPC04L01)
	public String doLumpChangeRead() {

		if(listForm.changeIdArray != null) {
			for(String observateApplicationMailId : listForm.changeIdArray) {
				try {
					applicationLogic.changeObservationMailToOpened(NumberUtils.toInt(observateApplicationMailId));
				} catch (WNoResultException e) {
					throw new ActionMessagesException("errors.app.dataNotFound");
				}
			}
		} else {
			listForm.errorMessage = "質問者を選択してください。";
		}


		return "/observateApplicationMail/list/showList?redirect=true";
	}

	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPC04L01)
	public String doLumpChangeUnRead() {

		if(listForm.changeIdArray != null) {
			for(String observateApplicationMailId : listForm.changeIdArray) {
				try {
					applicationLogic.changeObservationMailToUnOpened(NumberUtils.toInt(observateApplicationMailId));
				} catch (WNoResultException e) {
					throw new ActionMessagesException("errors.app.dataNotFound");
				}
			}
		} else {
			listForm.errorMessage = "質問者を選択してください。";
		}

		return "/observateApplicationMail/list/showList?redirect=true";
	}

	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPC02L01)
	public String doLumpChangeUnDisplay() {

		if(listForm.changeIdArray != null) {
			List<Integer> mailIds = Arrays.asList(listForm.changeIdArray).stream()
					.map(s -> Integer.parseInt(s))
					.collect(Collectors.toList());

			applicationLogic.changeObservationMailToUnDisplay(mailIds);
		} else {
			listForm.errorMessage = "質問者を選択してください。";
		}

		return "/observateApplicationMail/list/showList?redirect=true";
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

			ApplicationMailRetDto retDto = applicationLogic.getObservateApplicationMailListData(property, listForm.sendKbn, searchProperty);
			for (MailSelectDto entity : retDto.retList) {

				ApplicationMailListDto dto = new ApplicationMailListDto();
				Beans.copy(entity, dto).execute();
				if(listForm.sendKbn == SendKbn.RECEIVE) {
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

					if(StringUtils.isBlank(entity.fromName)) {
						dto.fromName = "匿名";
					}


					dto.age = GourmetCareeUtil.convertToAge(entity.birthday);

					dto.sendDatetime = GourmetCareeUtil.convertSendDateTimeForMailList(entity.sendDatetime);

					dto.unsubscribeFlg = false;
					if(entity.memberId != null) {
						try {
							memberService.findById(entity.memberId);
						} catch(SNoResultException e) {
							dto.unsubscribeFlg = true;
						}
					}
				}
				tmpList.add(dto);
			}

			dataList = tmpList;
			pageNavi = retDto.pageNavi;

			listForm.setExistDataFlgOk();

		} catch (WNoResultException e) {
			dataList = tmpList;
			pageNavi = new PageNavigateHelper(GourmetCareeConstants.DEFAULT_MAX_ROW_INT);

			listForm.setExistDataFlgNg();

			throw new ActionMessagesException("errors.app.mailDataNotFound");
		}
	}

	/**
	 * ページ遷移
	 * 受信メール一覧用のページ遷移。
	 * @return
	 */
	@Execute(urlPattern = "changePage/{pageNum}", input = TransitionConstants.Application.JSP_SPC04L01)
	public String changePage() {

		createList(NumberUtils.toInt(listForm.pageNum));
		checkUnReadMail();

		return TransitionConstants.Application.JSP_SPC04L01;
	}

	/**
	 * 再表示<br>
	 * セッションをリセットせずに検索結果を一覧に表示します。<br>
	 * ※URL欄に再表示であることを明示しないように、メソッド名はあえて抽象的にしています。
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPC04L01)
	public String showList() {

		createList(NumberUtils.toInt(listForm.pageNum));
		checkUnReadMail();

		return TransitionConstants.Application.JSP_SPC04L01;
	}

	/**
	 * ステータスの変更
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPC04L01)
	public String ajaxSelectionFlg() {

		TObservateApplication entity = new TObservateApplication();
		entity.id = listForm.observateApplicationId;

		//不正な値を防ぐため値をチェック。
		Map<String, String> selectionMap = MTypeConstants.SelectionFlg.SELECTION_COLOR_STRING_MAP;
		if (StringUtils.isNotBlank(selectionMap.get(listForm.selectionKbn))) {
			entity.selectionFlg = Integer.parseInt(listForm.selectionKbn);
		} else {
			entity.selectionFlg = STATUS_DEFAULT_VALUE;
		}

		observateApplicationService.updateIncludesVersion(entity);

		String json = JSON.encode(MTypeConstants.SelectionFlg.SELECTION_COLOR_STRING_MAP);
		ResponseUtil.write(json, "application/json", "UTF-8");

		return null;
	}

	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPC04L01)
	public String editMemo() {

		TObservateApplication entity = new TObservateApplication();
		entity.id = listForm.observateApplicationId;
		entity.memo = listForm.memo;
		entity.memoUpdateDatetime = new Date();

		observateApplicationService.updateIncludesVersion(entity);

		return null;
	}

	public MAIL_KBN getMailKbn() {
		return MAIL_KBN.OBSERVATE_APPLICATION;
	}

	@Override
	public MenuInfo getMenuInfo() {
		return MenuInfo.mailInstance();
	}
}
