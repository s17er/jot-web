package com.gourmetcaree.shop.pc.application.action.arbeit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
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
import com.gourmetcaree.db.common.entity.TArbeitApplication;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.common.service.ArbeitApplicationService;
import com.gourmetcaree.shop.logic.dto.ArbeitApplicationRetDto;
import com.gourmetcaree.shop.logic.exception.NotExistMailException;
import com.gourmetcaree.shop.logic.logic.ApplicationLogic.ApplicationSearchProperty;
import com.gourmetcaree.shop.logic.logic.ApplicationLogic.MAIL_KBN;
import com.gourmetcaree.shop.logic.logic.ArbeitApplicationLogic;
import com.gourmetcaree.shop.pc.application.dto.arbeit.ArbeitApplicationListDto;
import com.gourmetcaree.shop.pc.application.form.applicationMail.ApplicationMailForm;
import com.gourmetcaree.shop.pc.application.form.arbeit.ListForm;
import com.gourmetcaree.shop.pc.application.form.arbeitMail.InputForm;
import com.gourmetcaree.shop.pc.sys.action.MailListAction;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;

import jp.co.whizz_tech.common.sastruts.converter.ZenkakuKanaConverter;
import net.arnx.jsonic.JSON;

/**
 * アルバイト応募者一覧
 * @author Yamane
 *
 */
@ManageLoginRequired
public class ListAction extends MailListAction {

	/** アクションフォーム */
	@ActionForm
	@Resource
	private ListForm listForm;

	/** グルメdeバイトのロジック */
	@Resource
	private ArbeitApplicationLogic arbeitApplicationLogic;

	/** グルメdeバイトのサービス */
	@Resource
	private ArbeitApplicationService arbeitApplicationService;

	/** グルメdeバイト応募者一覧 */
	public List<ArbeitApplicationListDto> dataList;

	/** ページナビゲータ */
	public PageNavigateHelper pageNavi;

	@Resource
	public InputForm inputForm;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Application.JSP_SPC05L02)
	public String index() {

		return show();
	}

	/**
	 * 初期表示遷移用
	 * @return
	 */
	private String show() {

		createList(GourmetCareeConstants.DEFAULT_PAGE);

		return TransitionConstants.Application.JSP_SPC05L02;
	}

	/**
	 * 送信箱の初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Application.JSP_SPC05L02)
	public String sendBox() {

		createList(GourmetCareeConstants.DEFAULT_PAGE);

		return TransitionConstants.Application.JSP_SPC05L02;
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


		try {
			ApplicationSearchProperty searchProperty = new ApplicationSearchProperty();
			if (StringUtils.isNotBlank(listForm.keyword)) {
				searchProperty.keyword = listForm.keyword;
			}
			ArbeitApplicationRetDto retDto = arbeitApplicationLogic.getArbeitApplication(property, searchProperty);
			if (retDto.applicationSelect == null) {
				throw new WNoResultException();
			}


			final Map<Integer, String> colorMap = MTypeConstants.SelectionFlg.SELECTION_COLOR_MAP;
			final List<String> lumpSendIdList;
			if (StringUtils.isBlank(listForm.lumpSendIds)) {
				lumpSendIdList = new ArrayList<String>(0);
			} else {
				lumpSendIdList = Arrays.asList(listForm.lumpSendIds.split(GourmetCareeConstants.KANMA_STR));
			}

			dataList = new ArrayList<ArbeitApplicationListDto>();

			retDto.applicationSelect.iterate(new IterationCallback<TArbeitApplication, Void>() {
				@Override
				public Void iterate(TArbeitApplication entity, IterationContext context) {
					if (entity == null) {
						return null;
					}

					ArbeitApplicationListDto dto = new ArbeitApplicationListDto();
					Beans.copy(entity, dto).execute();

					dto.unopenedMailFlg = arbeitApplicationLogic.isUnopenedApplicantMailExist(entity.id);
					if (colorMap.containsKey(dto.selectionFlg)) {
						dto.selectionFlgColor = colorMap.get(dto.selectionFlg);
					} else {
						dto.selectionFlgColor = MTypeConstants.SelectionFlg.SELECTION_FLG_DEFAULT_COLOR;
					}

					dto.containsCheckedIdFlg = lumpSendIdList.contains(String.valueOf(entity.id));
					dataList.add(dto);

					return null;
				}
			});
			pageNavi = retDto.pageNavi;

			listForm.setExistDataFlgOk();

		} catch (WNoResultException e) {
			dataList = new ArrayList<ArbeitApplicationListDto>(0);
			pageNavi = new PageNavigateHelper(GourmetCareeConstants.DEFAULT_MAX_ROW_INT);

			listForm.setExistDataFlgNg();

			throw new ActionMessagesException("errors.app.mailDataNotFound");
		}
	}


	/**
	 * キーワード検索を行います。
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Application.JSP_SPC05L02)
	public String doSearch() {
		createList(GourmetCareeConstants.DEFAULT_PAGE);
		return TransitionConstants.Application.JSP_SPC05L02;
	}

	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPC05L02)
	public String outputCsv() {
		ApplicationSearchProperty searchProperty = new ApplicationSearchProperty();
		if (StringUtils.isNotBlank(listForm.keyword)) {
			searchProperty.keyword = listForm.keyword;
		}

		try {
			arbeitApplicationLogic.outputCsv(searchProperty, response, "arbeit");
		} catch (WNoResultException e) {
			throw new ActionMessagesException("errors.app.mailDataNotFound");
		} catch (IOException e) {
			throw new ActionMessagesException("errors.app.failedOutputCsv");
		}
		return null;
	}
	/**
	 * 応募者のメモを編集する
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPC05L02)
	public String editMemo() {

		//フォームにはメモ以外のレコード情報が含まれるため、必要かラムのみセット
		TArbeitApplication entity = new TArbeitApplication();
		Beans.copy(listForm, entity).includes("id", "version", "memo").converter(new ZenkakuKanaConverter()).execute();

		entity.memoUpdateDatetime = new Date();

		arbeitApplicationService.update(entity);

		if (StringUtils.isBlank(listForm.pageNum)) {
			listForm.pageNum = "1";
		}

		return "/arbeit/list/changePage/" + listForm.pageNum + TransitionConstants.REDIRECT_STR;
	}

	/**
	 * ページ遷移
	 * 受信メール一覧用のページ遷移。
	 * @return
	 */
	@Execute(urlPattern = "changePage/{pageNum}", input = TransitionConstants.Application.JSP_SPC05L02)
	public String changePage() {

		createList(NumberUtils.toInt(listForm.pageNum));
		return TransitionConstants.Application.JSP_SPC05L02;
	}

	/**
	 * 再表示<br>
	 * セッションをリセットせずに検索結果を一覧に表示します。<br>
	 * ※URL欄に再表示であることを明示しないように、メソッド名はあえて抽象的にしています。
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPC05L02)
	public String showList() {

		createList(NumberUtils.toInt(listForm.pageNum));

		return TransitionConstants.Application.JSP_SPC05L02;
	}

	/**
	 * ステータスの変更
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Application.JSP_SPC05L02)
	public String ajaxSelectionFlg() {

		TArbeitApplication entity = new TArbeitApplication();
		Beans.copy(listForm, entity).execute();

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

	/**
	 * 一括送信
	 * @return
	 */
	@Execute(validator = true, reset = "resetForm", validate="validateLumpSend", input = TransitionConstants.Application.RETURN_TO_ARBEIT_APPLICATION_LIST)
	public String lumpSend() {



		String[] lumpSendIdArray = listForm.lumpSendIds.split(GourmetCareeConstants.KANMA_STR);
		List<Integer> mailIdList;
		try {
			mailIdList = applicationLogic.getApplicationMail(GourmetCareeUtil.convertStringArrayToIntArray(lumpSendIdArray), MAIL_KBN.ARBEIT_APPLICATION);
		} catch (NotExistMailException e) {
			String failApplicantName = arbeitApplicationService.createJoinedApplicantName(e.getNotExistIdList());
			createList(NumberUtils.toInt(listForm.pageNum, GourmetCareeConstants.DEFAULT_PAGE));
			throw new ActionMessagesException("errors.app.cannotReplyApplicantMail", failApplicantName);
		}



		inputForm.resetForm();
		inputForm.arbeitApplicationId = lumpSendIdArray;
		inputForm.fromPageKbn = ApplicationMailForm.FROM_APPLICATION_LIST;
		List<String> originalMailIdList = new ArrayList<String>(inputForm.arbeitApplicationId.length);
		for (Integer mailId : mailIdList) {
			originalMailIdList.add(String.valueOf(mailId));
		}

		inputForm.originalMailId = originalMailIdList.toArray(new String[0]);

		return TransitionConstants.Application.REDIRECT_ARBEIT_MAIL_INPUT;
	}


	/**
	 * 最大件数切替の初期値をプロパティから取得します。<br />
	 * 取得できない場合は、システム全体の初期値を設定
	 * @return 最大表示件数の初期値
	 */
	private int getInitMaxRow() {
		return  NumberUtils.toInt(getCommonProperty("gc.arbeitApplication.mail.initMaxRow"), GourmetCareeConstants.DEFAULT_MAX_ROW_INT);
	}

	@Override
	public MAIL_KBN getMailKbn() {
		return MAIL_KBN.ARBEIT_APPLICATION;
	}

	/**
	 * 応募者一覧へもどる
	 * @return
	 */
	@Execute(validator = false)
	public String returnToList() {
		try {
			createList(NumberUtils.toInt(listForm.pageNum, GourmetCareeConstants.DEFAULT_PAGE));
		} catch (ActionMessagesException e) {
			ActionMessagesUtil.addErrors(request, e.getMessages());
		}
		return TransitionConstants.Application.JSP_SPC05L02;
	}
}
