package com.gourmetcaree.shop.pc.application.action.observateApplication;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.property.PagerProperty;
import com.gourmetcaree.db.common.entity.TObservateApplication;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.common.service.ObservateApplicationService;
import com.gourmetcaree.shop.logic.dto.ApplicationMailRetDto;
import com.gourmetcaree.shop.logic.dto.MailSelectDto;
import com.gourmetcaree.shop.logic.logic.ApplicationLogic.MAIL_KBN;
import com.gourmetcaree.shop.logic.logic.ObservateApplicationLogic;
import com.gourmetcaree.shop.pc.application.dto.application.MailForApplicationIdDto;
import com.gourmetcaree.shop.pc.application.dto.applicationMail.ApplicationMailListDto;
import com.gourmetcaree.shop.pc.application.dto.observateApplication.ObservateApplicationListDto;
import com.gourmetcaree.shop.pc.application.form.observateApplication.DetailMailListForm;
import com.gourmetcaree.shop.pc.sys.action.MailListAction;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;

import jp.co.whizz_tech.common.sastruts.converter.ZenkakuKanaConverter;


/**
 * 応募者毎のメールリストを表示するアクションクラスです。
 * @author Yamane
 * @version 1.0
 */
@ManageLoginRequired
public class DetailMailListAction extends MailListAction {

	/** 応募者詳細フォーム */
	@ActionForm
	@Resource
	protected DetailMailListForm detailMailListForm;

	/** 店舗見学・質問者サービス */
	@Resource
	protected ObservateApplicationService observateApplicationService;

	/** 店舗見学・質問者ロジック */
	@Resource
	private ObservateApplicationLogic observateApplicationLogic;

	/** ページ上部の応募者情報 */
	public ObservateApplicationListDto observateApplicationListDto;

	/** メールを保持するリスト */
	public List<ApplicationMailListDto> dataList;

	/** ページナビゲータ */
	public PageNavigateHelper pageNavi;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, urlPattern = "index/{id}", reset = "resetForm", input = TransitionConstants.Application.JSP_SPC04L03)
	public String index() {

		//必要なパラメータをチェック
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, detailMailListForm.id);

		return show();
	}

	/**
	 * 初期表示遷移用
	 * @return
	 */
	private String show() {

		createList(GourmetCareeConstants.DEFAULT_PAGE);

		return TransitionConstants.Application.JSP_SPC04L03;
	}

	/**
	 * 一覧表示用のデータを作成します。
	 * @param targetPage
	 */
	private void createList(int targetPage) {

		PagerProperty property = new PagerProperty();
		property.targetPage = targetPage;
		property.maxRow = getInitMaxRow();

		List<ApplicationMailListDto> tmpList = new ArrayList<ApplicationMailListDto>();

		/* 質問IDをキーに質問者情報を取得する */
		TObservateApplication tObservateApplication;

		try {
			tObservateApplication = observateApplicationService.findById(Integer.parseInt(detailMailListForm.id));
		} catch (SNoResultException e) {
			throw new FraudulentProcessException("IDが不正です。", e);
		}

		observateApplicationListDto =
				Beans.createAndCopy(ObservateApplicationListDto.class, tObservateApplication).excludesNull().excludesWhitespace().execute();


		// 応募IDをキーに応募者のメール（送受信）を取得する
		ApplicationMailRetDto retDto =
				applicationLogic.getApplicationIdEachMail(property, Integer.parseInt(detailMailListForm.id), MAIL_KBN.OBSERVATE_APPLICATION);

		if (CollectionUtils.isEmpty(retDto.retList)) {
			detailMailListForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.dataNotFound");
		}
		if (retDto != null && retDto.retList != null && retDto.retList.size() > 0) {
			observateApplicationListDto.unopenedMailFlg = observateApplicationLogic.isUnopenedApplicantMailExist(retDto.retList.get(0).observateApplicationId);
		}


		for (MailSelectDto entity : retDto.retList) {

			MailForApplicationIdDto dto = new MailForApplicationIdDto();
			Beans.copy(entity, dto).execute();

			tmpList.add(dto);
		}

		dataList = tmpList;
		pageNavi = retDto.pageNavi;
	}


	/**
	 * 質問者のメモを編集する
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPC04L03)
	public String editMemo() {

		//フォームにはメモ以外のレコード情報が含まれるため、必要かラムのみセット
		TObservateApplication entity = new TObservateApplication();
		Beans.copy(detailMailListForm, entity).includes("id", "version", "memo").converter(new ZenkakuKanaConverter()).execute();

		entity.memoUpdateDatetime = new Date();

		observateApplicationService.update(entity);

		if (StringUtils.isBlank(detailMailListForm.pageNum)) {
			detailMailListForm.pageNum = "1";
		}

//		return "/observateApplication/detailMailList/" + detailMailListForm.id + "/changePage/" + detailMailListForm.pageNum + TransitionConstants.REDIRECT_STR;

		return String.format("/observateApplication/detailMailList/changePage/%s/%s%s", detailMailListForm.id, StringUtils.defaultIfEmpty(detailMailListForm.pageNum, "1"), TransitionConstants.REDIRECT_STR);
	}

	/**
	 * ページ遷移
	 * 受信メール一覧用のページ遷移。
	 * @return
	 */
	@Execute(urlPattern = "changePage/{id}/{pageNum}", input = TransitionConstants.Application.JSP_SPC04L03)
	public String changePage() {

		createList(NumberUtils.toInt(detailMailListForm.pageNum));
		return TransitionConstants.Application.JSP_SPC04L03;
	}

	/**
	 * 最大件数切替の初期値をプロパティから取得します。<br />
	 * 取得できない場合は、システム全体の初期値を設定
	 * @return 最大表示件数の初期値
	 */
	private int getInitMaxRow() {
		return  NumberUtils.toInt(getCommonProperty("gc.observateApplication.mail.initMaxRow"), GourmetCareeConstants.DEFAULT_MAX_ROW_INT);
	}

	/**
	 * 一覧に戻る
	 * @return
	 */
	@Execute(validator = false)
	public String backList() {

		return TransitionConstants.Application.REDIRECT_OBSERVATEAPPLICATION_LIST_INDEX;
	}

	public MAIL_KBN getMailKbn() {
		return MAIL_KBN.OBSERVATE_APPLICATION;
	}
}
