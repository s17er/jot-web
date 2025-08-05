package com.gourmetcaree.admin.pc.mailMag.action.mailMagOption;

import java.io.IOException;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.ResponseUtil;

import com.gourmetcaree.admin.pc.mailMag.form.mailMagOption.HeaderDetailForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.logic.MailMagazineLogic;
import com.gourmetcaree.admin.service.property.MailMagazineProperty;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.db.common.entity.TMailMagazineOption;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * ヘッダメッセージ詳細用アクションです。
 * @author Takehiro Nakamori
 *
 */
@ManageLoginRequired(authLevel=ManageAuthLevel.ADMIN)
public class HeaderDetailAction extends BaseMailMagOptionAction {

	/** アクションフォーム */
	@ActionForm
	@Resource
	private HeaderDetailForm headerDetailForm;

	/** メルマガロジック */
	@Resource
	protected MailMagazineLogic mailMagazineLogic;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, urlPattern = "index/{id}", reset="resetForm", input = TransitionConstants.MailMag.JSP_APK04C02)
	@MethodAccess(accessCode="MAILMAGOPT_HEADER_DETAIL_INDEX")
	public String index() {
		// IDがない場合はエラー
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, headerDetailForm.id);

		return show();
	}

	/**
	 * CSV出力
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="MAILMAGOPT_HEADER_DETAIL_INDEX")
	public String outPut() {

		// IDが不正かどうかチェック
		try {
			Integer.parseInt(headerDetailForm.mailMagazineId);
		} catch (NumberFormatException e) {
			// 画面を非表示に設定
			headerDetailForm.setExistDataFlgNg();
			// 「CSV出力に失敗しました。」
			throw new ActionMessagesException("errors.app.csvDataNotFound");
		}

		try {
			// 出力条件をセット
			MailMagazineProperty property = new MailMagazineProperty();
			property.mailMagazineId = Integer.parseInt(headerDetailForm.mailMagazineId);
			property.areaCd = mailMagazineOptionService.findById(NumberUtils.toInt(headerDetailForm.id)).areaCd;

			// CSV出力
			int count = mailMagazineLogic.outPutHeaderCsv(property);

			ResponseUtil.write(String.valueOf(count));

		// データが取得できない場合のエラー
		} catch (WNoResultException e) {
			headerDetailForm.setExistDataFlgNg();
			// 「対象のデータが見つかりませんでした。」
			throw new ActionMessagesException("errors.app.csvDataNotFound");

		// CSV出力時のエラー
		} catch (IOException e) {
			headerDetailForm.setExistDataFlgNg();
			// 「CSV出力に失敗しました。」
			throw new ActionMessagesException("errors.app.csvOutPutFailed");

		}
		return null;
	}

	/**
	 * 初期表示遷移
	 */
	private String show() {
		createDispValue();
		return TransitionConstants.MailMag.JSP_APK04C02;
	}

	/**
	 * メルマガヘッダメッセージへ遷移
	 * @return
	 */
	@Execute(validator = false, removeActionForm = true)
	@MethodAccess(accessCode="MAILMAGOPT_HEADER_LIST")
	public String backToList() {
		return TransitionConstants.MailMag.FORWORD_HEADER_LIST;
	}

	/**
	 * メールマガジンオプションの値作成
	 */
	private void createDispValue() {
		try {
			TMailMagazineOption entity = mailMagazineOptionService.findById(NumberUtils.toInt(headerDetailForm.id));
			Beans.copy(entity, headerDetailForm).excludes("deliveryScheduleDatetime", "areaCd").execute();
			headerDetailForm.deliveryScheduleDatetime = DateUtils.getDateStr(entity.deliveryScheduleDatetime, GourmetCareeConstants.DATE_FORMAT_SLASH);
			headerDetailForm.setExistDataFlgOk();
			headerDetailForm.areaCd.add(String.valueOf(entity.areaCd));

			if (entity.mailMagazineId == null) {
				headerDetailForm.editFlg = true;
			}

		} catch (SNoResultException e) {
			// 画面表示をしない
			headerDetailForm.setExistDataFlgNg();

			//「該当するデータが見つかりませんでした。」
			throw new ActionMessagesException("errors.app.dataNotFound");
		}
	}
}
