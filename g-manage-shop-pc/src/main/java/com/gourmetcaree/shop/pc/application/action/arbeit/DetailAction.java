package com.gourmetcaree.shop.pc.application.action.arbeit;

import java.util.Date;

import javax.annotation.Resource;

import jp.co.whizz_tech.common.sastruts.converter.ZenkakuKanaConverter;

import org.apache.commons.lang.math.NumberUtils;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.logic.ValueToNameConvertLogic;
import com.gourmetcaree.db.common.entity.TArbeitApplication;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.ArbeitApplicationService;
import com.gourmetcaree.shop.logic.logic.ApplicationLogic.MAIL_KBN;
import com.gourmetcaree.shop.logic.logic.ArbeitApplicationLogic;
import com.gourmetcaree.shop.pc.application.form.arbeit.DetailForm;
import com.gourmetcaree.shop.pc.sys.action.MailListAction;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;


/**
 * グルメdeバイト応募者詳細を表示するアクションクラスです。
 * @author Takahiro Kimura
 * @version 1.0
 */
@ManageLoginRequired
public class DetailAction extends MailListAction {

	/** 応募者詳細フォーム */
	@ActionForm
	@Resource
	protected DetailForm detailForm;

	/** アルバイト応募ロジック */
	@Resource
	protected ArbeitApplicationLogic arbeitApplicationLogic;

	/** アルバイト応募ロジック */
	@Resource
	protected ArbeitApplicationService arbeitApplicationService;

	@Resource
	private ValueToNameConvertLogic valueToNameConvertLogic;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, urlPattern = "index/{id}", reset = "resetForm", input = TransitionConstants.Application.JSP_SPC05R02)
	public String index() {

		//必要なパラメータをチェック
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, detailForm.id);

		return show();
	}


	/**
	 * 応募者情報のダイアログ
	 * @return
	 */
	@Execute(validator = false, urlPattern = "subApplicationDetail/{id}", reset = "resetForm", input = TransitionConstants.Application.JSP_SPC05R02_SUB)
	public String subApplicationDetail() {

		//必要なパラメータをチェック
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, detailForm.id);

		show();

		return TransitionConstants.Application.JSP_SPC05R02_SUB;
	}


	/**
	 * 初期表示遷移用
	 * @return
	 */
	private String show() {

		try {
			TArbeitApplication entity = arbeitApplicationLogic.findByIdFromApplication(NumberUtils.toInt(detailForm.id));
			Beans.copy(entity, detailForm).execute();
			//初回メールを保持
			detailForm.firstMailId = entity.mailId;

			detailForm.setExistDataFlgOk();

		} catch (WNoResultException e) {
			detailForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.dataNotFound");
		}

		return TransitionConstants.Application.JSP_SPC05R02;
	}

	/**
	 * 応募者のメモを編集する
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPC05R02)
	public String editMemo() {

		//フォームにはメモ以外のレコード情報が含まれるため、必要かラムのみセット
		TArbeitApplication entity = new TArbeitApplication();
		Beans.copy(detailForm, entity).includes("id", "version", "memo").converter(new ZenkakuKanaConverter()).execute();

		entity.memoUpdateDatetime = new Date();

		arbeitApplicationService.update(entity);

		return "/arbeit/detail/index/" + detailForm.id + GourmetCareeConstants.REDIRECT_STR;
	}

	public MAIL_KBN getMailKbn() {
		return MAIL_KBN.ARBEIT_APPLICATION;
	}
}
