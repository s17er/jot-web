package com.gourmetcaree.shop.pc.contact.action.contact;

import javax.annotation.Resource;

import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.shop.logic.logic.SendMailLogic;
import com.gourmetcaree.shop.logic.property.ContactMailProperty;
import com.gourmetcaree.shop.pc.contact.form.contact.InputForm;
import com.gourmetcaree.shop.pc.sys.action.PcShopAction;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;


@ManageLoginRequired
public class IndexAction extends PcShopAction {

	/** 入力フォーム */
	@ActionForm
	@Resource
	protected InputForm inputForm;

	/** メール送信ロジック */
	@Resource
	protected SendMailLogic sendMailLogic;

	/**
	 * 初期表示メソッド
	 * @return
	 */
	@Execute(validator = false, reset="resetForm", input = TransitionConstants.Contact.JSP_SPL01A01)
	public String index() {
		return show();
	}

	/**
	 * 入力画面へ戻る
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Contact.JSP_SPL01A01)
	public String back() {
		inputForm.setProcessFlgNg();
		checkUnReadMail();
		return TransitionConstants.Contact.JSP_SPL01A01;
	}

	/**
	 * 確認画面を表示
	 * @return
	 */
	@Execute(validator = true, validate = "validate", input = TransitionConstants.Contact.JSP_SPL01A01)
	public String conf() {
		inputForm.setProcessFlgOk();
		checkUnReadMail();
		return TransitionConstants.Contact.JSP_SPL01C01;
	}

	/**
	 * メール送信
	 * @return
	 */
	@Execute(validator = true, input=TransitionConstants.Contact.JSP_SPL01A01)
	public String submit() {

		// プロセスフラグがfalseの場合、未確認としてエラー
		if (inputForm.processFlg == false) {
			throw new FraudulentProcessException("不正な操作が行われました。" + inputForm);
		}

		sendMail();

		return TransitionConstants.Contact.REDIRECT_CONTACT_COMP;
	}

	/**
	 * 完了画面表示
	 * @return 完了画面
	 */
	@Execute(validator = false, removeActionForm = true)
	public String comp() {
		checkUnReadMail();
		// 完了画面へ遷移
		return TransitionConstants.Contact.JSP_SPL01C02;
	}

	/**
	 * 初期表示遷移用
	 * @return
	 */
	private String show() {
		checkUnReadMail();
		return TransitionConstants.Contact.JSP_SPL01A01;
	}

	private void sendMail() {

		ContactMailProperty property = new ContactMailProperty();
		property.subject = "【顧客からの問い合わせ通知】";
		property.customerName = userDto.customerName;
		property.contactName = inputForm.contactName;
		property.sender = inputForm.sender;
		property.phoneNo = inputForm.phoneNo1 + "-" + inputForm.phoneNo2 + "-" + inputForm.phoneNo3;
		property.body =inputForm.contents;

		sendMailLogic.sendContactMail(property);
	}
}
