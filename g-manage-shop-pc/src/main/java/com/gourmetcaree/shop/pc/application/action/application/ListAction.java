package com.gourmetcaree.shop.pc.application.action.application;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.ActionMessagesUtil;
import org.seasar.struts.util.ResponseUtil;

import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.property.PagerProperty;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TApplication;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.common.service.ApplicationService;
import com.gourmetcaree.shop.logic.dto.ApplicationListRetDto;
import com.gourmetcaree.shop.logic.exception.NotExistMailException;
import com.gourmetcaree.shop.logic.logic.ApplicationCsvLogic;
import com.gourmetcaree.shop.logic.logic.ApplicationLogic.ApplicationSearchProperty;
import com.gourmetcaree.shop.logic.logic.ApplicationLogic.MAIL_KBN;
import com.gourmetcaree.shop.logic.property.ApplicationCsvProperty;
import com.gourmetcaree.shop.pc.application.dto.application.ApplicationListDto;
import com.gourmetcaree.shop.pc.application.form.application.ListForm;
import com.gourmetcaree.shop.pc.application.form.application.ListForm.ListDisplayKbn;
import com.gourmetcaree.shop.pc.application.form.applicationMail.ApplicationMailForm;
import com.gourmetcaree.shop.pc.application.form.applicationMail.InputForm;
import com.gourmetcaree.shop.pc.sys.action.MailListAction;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;

import jp.co.whizz_tech.common.sastruts.converter.ZenkakuKanaConverter;
import net.arnx.jsonic.JSON;


/**
 * 応募者一覧を表示するアクションクラスです。
 * @author Takahiro Kimura
 * @version 1.0
 */
@ManageLoginRequired
public class ListAction extends MailListAction {

	/** 応募者一覧フォーム */
	@ActionForm
	@Resource
	protected ListForm listForm;

	/** 応募サービス */
	@Resource
	protected ApplicationService applicationService;

	/** 応募CSVロジック */
	@Resource
	protected ApplicationCsvLogic applicationCsvLogic;

	/** 応募を保持するリスト */
	public List<ApplicationListDto> dataList;

	/** ページナビゲータ */
	public PageNavigateHelper pageNavi;

	@Resource
	public InputForm inputForm;

	/**
	 * 最大件数切替の初期値をプロパティから取得します。<br />
	 * 取得できない場合は、システム全体の初期値を設定
	 * @return 最大表示件数の初期値
	 */
	private int getInitMaxRow() {
		return  NumberUtils.toInt(getCommonProperty("gc.application.initMaxRow"), GourmetCareeConstants.DEFAULT_MAX_ROW_INT);
	}

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Application.JSP_SPC01L01)
	public String index() {

		return show();
	}

	/**
	 * 初期表示遷移用
	 * @return
	 */
	private String show() {

		listForm.listDisplayKbn = ListDisplayKbn.ALL;

		createList(GourmetCareeConstants.DEFAULT_PAGE);

		return TransitionConstants.Application.JSP_SPC01L01;
	}

	/**
	 * 一覧表示用のデータを作成します。
	 * @param targetPage
	 */
	private void createList(int targetPage) {

		PagerProperty property = new PagerProperty();
		property.targetPage = targetPage;
		property.maxRow = getInitMaxRow();


		// ページの設定
		listForm.pageNum = String.valueOf(targetPage);

		List<ApplicationListDto> tmpList = new ArrayList<ApplicationListDto>();

		try {
			ApplicationListRetDto retDto;

			//WebIdで絞り込むかどうかで処理を分岐。セッション保持していた条件が消えた場合は全件表示とする。
//			if (listForm.listDisplayKbn != ListDisplayKbn.WEB_DATA) {
//				retDto = applicationLogic.getApplicationListData(property, new ApplicationSearchProperty());
//			} else {
				ApplicationSearchProperty searchProperty = new ApplicationSearchProperty();

				if (listForm.listDisplayKbn == ListDisplayKbn.WEB_DATA) {
					searchProperty.webId = NumberUtils.toInt(listForm.webId);
				} else {
					searchProperty.keyword = listForm.search;
				}

				retDto = applicationLogic.getApplicationListData(property, searchProperty);
//			}

			addApplicationListDto(retDto, tmpList);

			dataList = tmpList;
			pageNavi = retDto.pageNavi;

			listForm.setExistDataFlgOk();

		} catch (WNoResultException e) {
			dataList = tmpList;
			pageNavi = new PageNavigateHelper(GourmetCareeConstants.DEFAULT_MAX_ROW_INT);

			listForm.setExistDataFlgNg();

			throw new ActionMessagesException("errors.app.dataNotFound");
		}
	}

	/**
	 * ページ遷移
	 * @return
	 */
	@Execute(validator = false, urlPattern = "changePage/{pageNum}", input = TransitionConstants.Application.JSP_SPC01L01)
	public String changePage() {

		createList(NumberUtils.toInt(listForm.pageNum));

		return TransitionConstants.Application.JSP_SPC01L01;
	}

	/**
	 * WEBデータで絞った状態で一覧を表示します
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", urlPattern = "focusWebdata/{webId}", input = TransitionConstants.Application.JSP_SPC01L01)
	public String focusWebdata() {

		listForm.listDisplayKbn = ListDisplayKbn.WEB_DATA;
		createList(GourmetCareeConstants.DEFAULT_PAGE);

		return TransitionConstants.Application.JSP_SPC01L01;
	}

	/**
	 * CSV出力を行います。
	 * @return なし
	 */
	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPC01L01)
	public String outputCsv() {
		Integer webId = null;
		if (listForm.listDisplayKbn == ListDisplayKbn.WEB_DATA) {
			webId = NumberUtils.toInt(listForm.webId);
		}
		try {
			applicationCsvLogic.outputApplicationCsv(webId, createApplicationCsvProperty());
		} catch (WNoResultException e) {
			listForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.csvDataNotFound");
		} catch (UnsupportedEncodingException e) {
			listForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.csvOutPutFailed");
		} catch (IOException e) {
			listForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.csvOutPutFailed");
		}

		return null;
	}

	/**
	 * 応募CSVプロパティを作成します。
	 * @return 応募CSVプロパティ
	 */
	private ApplicationCsvProperty createApplicationCsvProperty() {
		ApplicationCsvProperty property = new ApplicationCsvProperty();
		property.pass = getCommonProperty("gc.csv.filepass");
		property.faileName = getCommonProperty("gc.application.csv.filename");
		property.encode = getCommonProperty("gc.csv.encoding");

		return property;
	}

	/**
	 * 再表示<br>
	 * セッションをリセットせずに検索結果を一覧に表示します。<br>
	 * ※URL欄に再表示であることを明示しないように、メソッド名はあえて抽象的にしています。
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPC01L01)
	public String showList() {

		// 表示フラグを非表示
		listForm.setExistDataFlgNg();

		createList(NumberUtils.toInt(listForm.pageNum));

		return TransitionConstants.Application.JSP_SPC01L01;
	}

	/**
	 * 選考対象フラグを変更する<br>
	 * Ajaxを利用してアクセスされます。
	 * バージョン番号は無視し、選択対象フラグのみのUPDATEを行います。
	 * @return
	 */
	@Execute(validator = false)
	public String check() {

		TApplication entity = new TApplication();
		Beans.copy(listForm, entity).execute();

		//不正な値を防ぐため値をチェック。
		if (NumberUtils.toInt(listForm.checkKbn) == MTypeConstants.SelectionFlg.SELECTION_TARGET) {
			entity.selectionFlg = MTypeConstants.SelectionFlg.SELECTION_TARGET;
		} else {
			entity.selectionFlg = MTypeConstants.SelectionFlg.OUT_TARGET;
		}

		applicationService.updateIncludesVersion(entity);

		return null;
	}

	/**
	 * 応募者のメモを編集する。
	 * 一覧から通常のサブミットでアクセスされます。
	 * 処理後はリダイレクトで再表示
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPC01L01)
	public String editMemo() {

		//フォームにはメモ以外のレコード情報が含まれるため、必要かラムのみセット
		TApplication entity = new TApplication();
		Beans.copy(listForm, entity).includes("id", "version", "memo").converter(new ZenkakuKanaConverter()).execute();

		entity.memoUpdateDatetime = new Date();

		applicationService.update(entity);

		return String.format("/application/list/changePage/%s%s", StringUtils.defaultIfEmpty(listForm.pageNum, "1"), GourmetCareeConstants.REDIRECT_STR );
	}

	/**
	 * 応募者の検索を行う。
	 * 一覧から通常のサブミットでアクセスされます。
	 * 処理後はリダイレクトで再表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Application.JSP_SPC01L01)
	public String doSearch() {



		createList(NumberUtils.toInt(listForm.pageNum, GourmetCareeConstants.DEFAULT_PAGE));


		return TransitionConstants.Application.JSP_SPC01L01;
	}


	/**
	 * ステータスの変更
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Application.JSP_SPC01R01)
	public String ajaxSelectionFlg() {

		TApplication entity = new TApplication();
		Beans.copy(listForm, entity).execute();

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

	/**
	 * 一括送信
	 * @return
	 */
	@Execute(validator = true, reset = "resetForLumpSend", validate="validateLumpSend", input = TransitionConstants.Application.RETURN_TO_APPLICATION_LIST)
	public String lumpSend() {



		String[] lumpSendIdArray = listForm.lumpSendIds.split(",");
		List<Integer> mailIdList;
		try {
			mailIdList = applicationLogic.getApplicationMail(GourmetCareeUtil.convertStringArrayToIntArray(lumpSendIdArray), MAIL_KBN.APPLICATION_MAIL);
		} catch (NotExistMailException e) {
			String failApplicantName = applicationService.createJoinedApplicantName(e.getNotExistIdList());
			createList(NumberUtils.toInt(listForm.pageNum, GourmetCareeConstants.DEFAULT_PAGE));
			throw new ActionMessagesException("errors.app.cannotReplyApplicantMail", failApplicantName);
		}


		inputForm.resetForm();
		inputForm.applicationId = lumpSendIdArray;
		inputForm.fromPageKbn = ApplicationMailForm.FROM_APPLICATION_LIST;

		List<String> originalMailIdList = new ArrayList<String>(inputForm.applicationId.length);
		for (Integer mailId : mailIdList) {
			originalMailIdList.add(String.valueOf(mailId));
		}

		inputForm.originalMailId = originalMailIdList.toArray(new String[0]);

		return TransitionConstants.Application.REDIRECT_APPLICATIONMAIL_INPUT;
	}



	/**
	 * 応募者一覧のDTO
	 * @param retDto
	 * @param list
	 */
	private void addApplicationListDto(ApplicationListRetDto retDto, List<ApplicationListDto> list) {

		List<String> lumpSendIdList;
		if (StringUtils.isBlank(listForm.lumpSendIds)) {
			lumpSendIdList = new ArrayList<String>(0);
		} else {
			lumpSendIdList = Arrays.asList(listForm.lumpSendIds.split(GourmetCareeConstants.KANMA_STR));
		}
		//不正な値を防ぐため値をチェック。
		Map<Integer, String> selectionMap = MTypeConstants.SelectionFlg.SELECTION_COLOR_MAP;
		for (TApplication entity : retDto.retList) {

			ApplicationListDto dto = new ApplicationListDto();
			Beans.copy(entity, dto).execute();

			//応募にたいして未読メールが存在するかどうかを取得。
			dto.unopenedMailFlg = applicationLogic.isUnopenedApplicantMailExist(entity.id);

			if (StringUtils.isNotBlank(selectionMap.get(dto.selectionFlg))) {
				dto.selectionFlg = entity.selectionFlg;
				dto.selectionFlgColor = selectionMap.get(dto.selectionFlg);
			} else {
				dto.selectionFlg = STATUS_DEFAULT_VALUE;
				dto.selectionFlgColor = MTypeConstants.SelectionFlg.SELECTION_FLG_DEFAULT_COLOR;
			}
			// リニューアル移行は誕生日で管理
			if (entity.birthday != null) {
				dto.age = GourmetCareeUtil.convertToAge(entity.birthday);
			}
			dto.containsCheckedFlg = lumpSendIdList.contains(String.valueOf(entity.id));


			list.add(dto);
		}

	}

	public MAIL_KBN getMailKbn() {
		return MAIL_KBN.APPLICATION_MAIL;
	}



	/**
	 * リストへ戻る
	 * @return
	 */
	@Execute(validator = false)
	public String returnToList() {
		try {
			createList(NumberUtils.toInt(listForm.pageNum, GourmetCareeConstants.DEFAULT_PAGE));
		} catch (ActionMessagesException e) {
			ActionMessagesUtil.addErrors(request, e.getMessages());
		}
		return TransitionConstants.Application.JSP_SPC01L01;
	}
}
