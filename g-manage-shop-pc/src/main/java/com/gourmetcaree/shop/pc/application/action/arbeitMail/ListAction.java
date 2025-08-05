package com.gourmetcaree.shop.pc.application.action.arbeitMail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
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
import com.gourmetcaree.db.common.entity.TArbeitApplication;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.common.service.ArbeitApplicationService;
import com.gourmetcaree.db.common.service.MailService;
import com.gourmetcaree.shop.logic.dto.ApplicationMailRetDto;
import com.gourmetcaree.shop.logic.dto.MailSelectDto;
import com.gourmetcaree.shop.logic.logic.ApplicationLogic.MAIL_KBN;
import com.gourmetcaree.shop.logic.logic.ArbeitApplicationLogic;
import com.gourmetcaree.shop.logic.property.ArbeitApplicationCsvProperty;
import com.gourmetcaree.shop.logic.property.MailSearchProperty;
import com.gourmetcaree.shop.pc.application.dto.applicationMail.ApplicationMailListDto;
import com.gourmetcaree.shop.pc.application.form.arbeitMail.ListForm;
import com.gourmetcaree.shop.pc.sys.action.MailListAction;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.shop.pc.valueobject.MenuInfo;

import net.arnx.jsonic.JSON;

/**
 * アルバイトメール一覧アクション
 * @author Takehiro Nakamori
 *
 */
@ManageLoginRequired
public class ListAction extends MailListAction {

	/** リストフォーム */
	@ActionForm
	@Resource
	private ListForm listForm;

	@Resource
	private ArbeitApplicationLogic arbeitApplicationLogic;

	@Resource
	private ArbeitApplicationService arbeitApplicationService;

	/** メールサービス */
	@Resource
	private MailService mailService;

	/** メールを保持するリスト */
	private List<ApplicationMailListDto> dataList;

	/** ページナビゲータ */
	private PageNavigateHelper pageNavi;

	public static final String ARBEIT_APPLICATION_ID_MAP = "ARBEIT_APPLICATION_ID_MAP";


	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Application.JSP_SPC05L01)
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

		return TransitionConstants.Application.JSP_SPC05L01;
	}

	/**
	 * 送信箱の初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Application.JSP_SPC05L01)
	public String sendBox() {

		listForm.sendKbn = SendKbn.SEND;
		createList(GourmetCareeConstants.DEFAULT_PAGE);
		checkUnReadMail();
		return TransitionConstants.Application.JSP_SPC05L01;
	}

	/**
	 * 検索
	 * @return
	 */
	@Execute(urlPattern = "search", reset = "resetForm", input = TransitionConstants.Application.JSP_SPC05L01)
	public String search() {

		createList(NumberUtils.toInt(listForm.pageNum));
		checkUnReadMail();

		return TransitionConstants.Application.JSP_SPC05L01;
	}

	/**
	 * ページ遷移
	 * 受信メール一覧用のページ遷移。
	 * @return
	 */
	@Execute(urlPattern = "changePage/{pageNum}", input = TransitionConstants.Application.JSP_SPC05L01)
	public String changePage() {

		createList(NumberUtils.toInt(listForm.pageNum, GourmetCareeConstants.DEFAULT_PAGE));
		checkUnReadMail();

		return TransitionConstants.Application.JSP_SPC05L01;
	}

	/**
	 * 再表示<br>
	 * セッションをリセットせずに検索結果を一覧に表示します。<br>
	 * ※URL欄に再表示であることを明示しないように、メソッド名はあえて抽象的にしています。
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPC05L01)
	public String showList() {

		createList(NumberUtils.toInt(listForm.pageNum));
		checkUnReadMail();

		return TransitionConstants.Application.JSP_SPC05L01;
	}

	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPC05L01)
	public String doLumpChangeRead() {

		if(listForm.changeIdArray != null) {
			for(String arbeitApplicationMailId : listForm.changeIdArray) {
				try {
					applicationLogic.changeArbeitMailToOpened(NumberUtils.toInt(arbeitApplicationMailId));
				} catch (WNoResultException e) {
					throw new ActionMessagesException("errors.app.dataNotFound");
				}
			}
		} else {
			listForm.errorMessage = "応募者を選択してください。";
		}


		return "/arbeitMail/list/showList?redirect=true";
	}

	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPC05L01)
	public String doLumpChangeUnRead() {

		if(listForm.changeIdArray != null) {
			for(String arbeitApplicationMailId : listForm.changeIdArray) {
				try {
					applicationLogic.changeArbeitMailToUnOpened(NumberUtils.toInt(arbeitApplicationMailId));
				} catch (WNoResultException e) {
					throw new ActionMessagesException("errors.app.dataNotFound");
				}
			}
		} else {
			listForm.errorMessage = "応募者を選択してください。";
		}

		return "/arbeitMail/list/showList?redirect=true";
	}

	/**
	 * ステータスの変更
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPC05L01)
	public String ajaxSelectionFlg() {

		TArbeitApplication entity = new TArbeitApplication();
		entity.id = listForm.arbeitApplicationId;

		//不正な値を防ぐため値をチェック。
		Map<String, String> selectionMap = MTypeConstants.SelectionFlg.SELECTION_COLOR_STRING_MAP;
		if (StringUtils.isNotBlank(selectionMap.get(listForm.selectionKbn))) {
			entity.selectionFlg = Integer.parseInt(listForm.selectionKbn);
		} else {
			entity.selectionFlg = STATUS_DEFAULT_VALUE;
		}

		arbeitApplicationService.updateIncludesVersion(entity);

		String json = JSON.encode(MTypeConstants.SelectionFlg.SELECTION_COLOR_STRING_MAP);
		ResponseUtil.write(json, "application/json", "UTF-8");

		return null;
	}


	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPC05L01)
	public String editMemo() {

		TArbeitApplication entity = new TArbeitApplication();
		entity.id = listForm.arbeitApplicationId;
		entity.memo = listForm.memo;
		entity.memoUpdateDatetime = new Date();

		arbeitApplicationService.updateIncludesVersion(entity);

		return null;
	}

	/**
	 * CSV出力
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPC05L01)
	public String outputCsv() {

		if(listForm.changeIdArray == null) {
			listForm.errorMessage = "応募者を選択してください。";
			return "/arbeitMail/list/showList?redirect=true";
		}

		Map<Integer,Integer> arbeitApplicationIdMap = (Map<Integer,Integer>)session.getAttribute(ARBEIT_APPLICATION_ID_MAP);
		List<Integer> arbeitApplicationIds = new ArrayList<>();

		for(String id : listForm.changeIdArray) {
			arbeitApplicationIds.add(arbeitApplicationIdMap.get(Integer.valueOf(id)));
		}

		ArbeitApplicationCsvProperty property = new ArbeitApplicationCsvProperty();
		property.pass = getCommonProperty("gc.csv.filepass");
		property.faileName = getCommonProperty("gc.arbeitApplication.csv.filename");
		property.encode = getCommonProperty("gc.csv.encoding");

		try {
			arbeitApplicationLogic.outputArbeitApplicationMailUsersCsv(arbeitApplicationIds, property);
		} catch (IOException e) {
			listForm.errorMessage = "CSV出力中にエラーが発生しました。";
			return "/arbeitMail/list/showList?redirect=true";
		}


		return null;
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


	/**
	 * メール一覧データを作成
	 * @param targetPage 表示する対象ページ
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

		ApplicationMailRetDto retDto = applicationLogic.getApplicationMailSelect(property,listForm.sendKbn,MTypeConstants.MailKbn.ARBEIT_APPLICATION, searchProperty);
		Map<Integer, Integer> arbeitApplicationIdMap = new HashedMap();
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
				dto.sendDatetime = GourmetCareeUtil.convertSendDateTimeForMailList(entity.sendDatetime);

				arbeitApplicationIdMap.put(entity.id, entity.arbeitApplicationId);
			}
			tmpList.add(dto);
		}

		dataList = tmpList;
		pageNavi = retDto.pageNavi;
		listForm.setExistDataFlgOk();

		session.setAttribute(ARBEIT_APPLICATION_ID_MAP, arbeitApplicationIdMap);
	}


	/**
	 * メールが見つからないエラーをスローします。
	 */
	private void throwMailNotFoundError() {
		dataList = new ArrayList<ApplicationMailListDto>();
		pageNavi = new PageNavigateHelper(GourmetCareeConstants.DEFAULT_MAX_ROW_INT);
		listForm.setExistDataFlgNg();
		throw new ActionMessagesException("errors.app.mailDataNotFound");
	}

	/**
	 * メールリストの取得
	 * @return
	 */
	public List<ApplicationMailListDto> getDataList() {
		if (dataList == null) {
			return new ArrayList<ApplicationMailListDto>();
		}
		return dataList;
	}


	public PageNavigateHelper getPageNavi() {
		return pageNavi;
	}

	public MAIL_KBN getMailKbn() {
		return MAIL_KBN.ARBEIT_APPLICATION;
	}


    @Override
    public MenuInfo getMenuInfo() {
        return MenuInfo.mailInstance();
    }
}
