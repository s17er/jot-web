package com.gourmetcaree.admin.pc.mailMag.action.mailMagOption;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.MessageResourcesUtil;

import com.gourmetcaree.admin.pc.mailMag.form.mailMagOption.HeaderDeleteForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.db.common.entity.TMailMagazineOption;


/**
 * メルマガヘッダメッセージの削除アクション
 * @author Takehiro Nakamori
 *
 */
@ManageLoginRequired(authLevel=ManageAuthLevel.ADMIN)
public class HeaderDeleteAction extends BaseMailMagOptionAction {

	/** ログオブジェクト */
	private static final Logger log = Logger.getLogger(HeaderDeleteAction.class);

	/** アクションフォーム */
	@ActionForm
	@Resource
	private HeaderDeleteForm headerDeleteForm;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, urlPattern = "index/{id}", input = TransitionConstants.MailMag.JSP_APK05C02)
	@MethodAccess(accessCode="MAILMAGOPT_HEADER_DELETE_INDEX")
	public String index() {
		// IDがない場合はNotFound
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, headerDeleteForm.id);
		headerDeleteForm.setProcessFlgOk();
		return show();
	}

	/**
	 * 初期表示遷移
	 * @return
	 */
	private String show() {
		createDispValue();
		return TransitionConstants.MailMag.JSP_APK05C02;
	}

	/**
	 * 削除サブミット
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.MailMag.JSP_APK05C02)
	@MethodAccess(accessCode="MAILMAGOPT_HEADER_DELETE_SUBMIT")
	public String submit() {
		// 確認画面のダイアログから遷移していない場合はエラー
		if (!headerDeleteForm.processFlg) {
			callFraudulentProcessError(headerDeleteForm);
		}

		checkArgsNull(NO_BLANK_FLG_NG, headerDeleteForm.id, headerDeleteForm.version);

		delete();
		return TransitionConstants.MailMag.REDIRECT_MAILMAG_HEADER_DELETE_COMP;
	}

	/**
	 * メルマガヘッダメッセージへ遷移
	 * @return
	 */
	@Execute(validator = false, removeActionForm = true)
	@MethodAccess(accessCode="MAILMAGOPT_HEADER_DETAIL_INDEX")
	public String backToDetail() {
		return TransitionConstants.MailMag.ACTION_HEADER_DETAIL_INDEX + headerDeleteForm.id + TransitionConstants.REDIRECT_STR;
	}

	/**
	 * 削除完了
	 * @return
	 */
	@Execute(validator = false, removeActionForm = true)
	@MethodAccess(accessCode="MAILMAGOPT_HEADER_DELETE_COMP")
	public String comp() {
		return TransitionConstants.MailMag.JSP_APK05C03;
	}

	/**
	 * ヘッダメッセージの削除
	 */
	private void delete() {
		TMailMagazineOption entity = new TMailMagazineOption();
		entity.id = NumberUtils.toInt(headerDeleteForm.id);
		entity.version = NumberUtils.toLong(headerDeleteForm.version);

		// 削除できるか確認
		if (!mailMagazineOptionService.canEditable(entity)) {
			createDispValue();
			headerDeleteForm.setExistDataFlgNg();
			headerDeleteForm.setProcessFlgNg();

			throw new ActionMessagesException("errors.app.isNotEditable", MessageResourcesUtil.getMessage("labels.headerMessage"), MessageResourcesUtil.getMessage("msg.delete"));
		}
		mailMagazineOptionService.logicalDelete(entity);

		log.debug("メルマガヘッダメッセージを削除しました。" + headerDeleteForm);


	}

	/**
	 * メールマガジンオプションの値作成
	 */
	private void createDispValue() {
		try {
			TMailMagazineOption entity = mailMagazineOptionService.findById(NumberUtils.toInt(headerDeleteForm.id));

			// 削除できるか確認
			if (!mailMagazineOptionService.canEditable(entity)) {
				headerDeleteForm.setProcessFlgNg();
				headerDeleteForm.setExistDataFlgNg();
				throw new ActionMessagesException("errors.app.isNotEditable", MessageResourcesUtil.getMessage("labels.headerMessage"), MessageResourcesUtil.getMessage("msg.delete"));
			}
			Beans.copy(entity, headerDeleteForm).excludes("areaCd", "deliveryScheduleDatetime").execute();
			headerDeleteForm.deliveryScheduleDatetime = DateUtils.getDateStr(entity.deliveryScheduleDatetime, GourmetCareeConstants.DATE_FORMAT_SLASH);
			headerDeleteForm.areaCd.add(String.valueOf(entity.areaCd));

		} catch (SNoResultException e) {
			// 画面表示をしない
			headerDeleteForm.setExistDataFlgNg();

			//「該当するデータが見つかりませんでした。」
			throw new ActionMessagesException("errors.app.dataNotFound");
		}
	}
}
