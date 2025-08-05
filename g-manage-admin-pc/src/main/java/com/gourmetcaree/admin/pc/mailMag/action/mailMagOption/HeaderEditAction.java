package com.gourmetcaree.admin.pc.mailMag.action.mailMagOption;

import java.text.ParseException;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.ActionMessagesUtil;

import com.gourmetcaree.admin.pc.mailMag.form.mailMagOption.HeaderEditForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TMailMagazineOption;

/**
 * メルマガヘッダメッセージ編集用アクションです。
 * @author Takehiro Nakamori
 *
 */
@ManageLoginRequired(authLevel=ManageAuthLevel.ADMIN)
public class HeaderEditAction extends BaseMailMagOptionAction {

	/** ログオブジェクト */
	private static final Logger log = Logger.getLogger(HeaderEditAction.class);

	@ActionForm
	@Resource
	private HeaderEditForm headerEditForm;

	/**
	 * 初期表示メソッド
	 * @return
	 */
	@Execute(validator = false, urlPattern = "index/{id}", input = TransitionConstants.MailMag.JSP_APK03C01)
	@MethodAccess(accessCode="MAILMAGOPT_HEADER_EDIT_INDEX")
	public String index() {
		// IDがない場合はエラー
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, headerEditForm.id);
		return show();
	}

	/**
	 * 初期表示遷移
	 * @return
	 */
	private String show() {
		createDispValue();
		return TransitionConstants.MailMag.JSP_APK03C01;
	}

	/**
	 * メルマガヘッダメッセージへ遷移
	 * @return
	 */
	@Execute(validator = false, removeActionForm = true)
	@MethodAccess(accessCode="MAILMAGOPT_HEADER_DETAIL_INDEX")
	public String backToDetail() {
		return TransitionConstants.MailMag.ACTION_HEADER_DETAIL_INDEX + headerEditForm.id + TransitionConstants.REDIRECT_STR;
	}


	/**
	 * 確認画面
	 * @return
	 */
	@Execute(validator = true, validate = "validate", input = TransitionConstants.MailMag.JSP_APK03C01)
	@MethodAccess(accessCode="MAILMAGOPT_HEADER_EDIT_CONF")
	public String conf() {
		// すでに登録されていないかチェック
		if (isAlreadyResiteredDate()) {
			return TransitionConstants.MailMag.JSP_APK03C01;
		}

		headerEditForm.setProcessFlgOk();
		return TransitionConstants.MailMag.JSP_APK03C02;
	}

	/**
	 * 修正ボタン押下時に呼ばれるメソッド
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.MailMag.JSP_APK03C01)
	@MethodAccess(accessCode="MAILMAGOPT_HEADER_EDIT_INDEX")
	public String correct() {
		headerEditForm.setProcessFlgNg();
		return TransitionConstants.MailMag.JSP_APK03C01;
	}

	/**
	 * アップデート
	 */
	@Execute(validator = false, input = TransitionConstants.MailMag.JSP_APK03C01)
	@MethodAccess(accessCode="MAILMAGOPT_HEADER_EDIT_SUBMIT")
	public String submit() {
		// プロセスフラグがfalseの場合、未確認としてエラー
		if (!headerEditForm.processFlg) {
			callFraudulentProcessError(headerEditForm);
		}

		// すでに登録されていないかチェック
		if (isAlreadyResiteredDate()) {
			return TransitionConstants.MailMag.JSP_APK03C01;
		}
		update();

		return TransitionConstants.MailMag.REDIRECT_MAILMAG_HEADER_EDIT_COMP;
	}

	/**
	 * 完了画面
	 */
	@Execute(validator = false, removeActionForm = true)
	@MethodAccess(accessCode="MAILMAGOPT_HEADER_EDIT_COMP")
	public String comp() {
		return TransitionConstants.MailMag.JSP_APK03C03;
	}

	/**
	 * アップデート処理
	 */
	private void update() {
		TMailMagazineOption entity = new TMailMagazineOption();
		Beans.copy(headerEditForm, entity).excludes("areaCd", "optionValue").dateConverter(GourmetCareeConstants.DATE_FORMAT_SLASH, "deliveryScheduleDatetime").execute();
		entity.areaCd = Integer.parseInt(headerEditForm.areaCd.get(0));
		if(headerEditForm.deliveryType.equals(MTypeConstants.deliveryTypeKbn.HTML)) {
			entity.optionValue = headerEditForm.htmlBody;
		} else {
			entity.optionValue = headerEditForm.textBody;
		}
		mailMagazineOptionService.update(entity);

		log.debug("メルマガヘッダメッセージを更新しました。" + headerEditForm);
	}

	/**
	 * メールマガジンオプションの値作成
	 */
	private void createDispValue() {
		try {
			TMailMagazineOption entity = mailMagazineOptionService.findById(NumberUtils.toInt(headerEditForm.id));
			Beans.copy(entity, headerEditForm).excludes("deliveryScheduleDatetime", "areaCd").execute();
			headerEditForm.deliveryScheduleDatetime = DateUtils.getDateStr(entity.deliveryScheduleDatetime, GourmetCareeConstants.DATE_FORMAT_SLASH);
			headerEditForm.areaCd.add(String.valueOf(entity.areaCd));
			if(entity.deliveryType.equals(MTypeConstants.deliveryTypeKbn.HTML)) {
				headerEditForm.htmlBody = entity.optionValue;
			} else {
				headerEditForm.textBody = entity.optionValue;
			}
		} catch (SNoResultException e) {
			// 画面表示をしない
			headerEditForm.setExistDataFlgNg();

			//「該当するデータが見つかりませんでした。」
			throw new ActionMessagesException("errors.app.dataNotFound");
		}
	}


	/**
	 * すでに登録されているかどうか
	 * @return
	 */
	private boolean isAlreadyResiteredDate() {
		Date deliveryScheduleDatetime;
		try {
			deliveryScheduleDatetime = DateUtils.formatDate(headerEditForm.deliveryScheduleDatetime, GourmetCareeConstants.DATE_FORMAT_SLASH);
		if( mailMagazineOptionService.isAlreadyResisteredDate(headerEditForm.id, deliveryScheduleDatetime, headerEditForm.areaCd,
				MTypeConstants.MailmagazineKbn.NEW_INFORMATION, MTypeConstants.MailmagazineOptionKbn.HEADER_MESSAGE)) {

			ActionMessages errors = new ActionMessages();
			errors.add("errors", new ActionMessage("errors.app.SameDataExists"));
			ActionMessagesUtil.addErrors(request, errors);

			return true;
		}

		return false;

		} catch (ParseException e) {
			throw new FraudulentProcessException("日付の変更に失敗しました。");
		} catch (NumberFormatException e) {
			throw new FraudulentProcessException("不正な値が入力されました。");
		}
	}




}
