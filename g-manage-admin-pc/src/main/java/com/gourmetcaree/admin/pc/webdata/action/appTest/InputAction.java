package com.gourmetcaree.admin.pc.webdata.action.appTest;

import static com.gourmetcaree.common.util.GourmetCareeUtil.*;
import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.MessageResourcesUtil;

import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.pc.sys.dto.mai.ApplicationTestMaiDto;
import com.gourmetcaree.admin.pc.sys.mai.GourmetcareeMai;
import com.gourmetcaree.admin.pc.webdata.form.appTest.InputForm;
import com.gourmetcaree.admin.service.logic.CustomerLogic;
import com.gourmetcaree.admin.service.logic.WebdataLogic;
import com.gourmetcaree.admin.service.property.CustomerProperty;
import com.gourmetcaree.admin.service.property.WebdataProperty;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.MCustomer;
import com.gourmetcaree.db.common.entity.MSales;
import com.gourmetcaree.db.common.entity.TApplicationTest;
import com.gourmetcaree.db.common.entity.TWeb;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.CustomerService;
import com.gourmetcaree.db.common.service.CustomerSubMailService;
import com.gourmetcaree.db.common.service.SalesService;
import com.gourmetcaree.db.common.service.WebService;

/**
 *
 * WEBデータの応募テストを行うクラス
 * @author Makoto Otani
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN,ManageAuthLevel.STAFF,ManageAuthLevel.AGENCY, ManageAuthLevel.OTHER, ManageAuthLevel.SALES})
public class InputAction extends PcAdminAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(InputAction.class);

	/** フォーム */
	@ActionForm
	@Resource
	protected InputForm inputForm;

	/** WEBデータのサービス */
	@Resource
	protected WebService webService;

	/** WEBデータのロジック */
	@Resource
	protected WebdataLogic webdataLogic;

	/** 営業担当者マスタのサービス */
	@Resource
	protected SalesService salesService;

	/** 顧客マスタのサービス */
	@Resource
	protected CustomerService customerService;

	/** 顧客マスタのロジック */
	@Resource
	protected CustomerLogic customerLogic;

	/** メール送信 */
	@Resource
	protected GourmetcareeMai gourmetCareeMai;

	@Resource
	private CustomerSubMailService customerSubMailService;

	/**
	 * 初期表示
	 * @return 入力画面
	 */
	@Execute(validator = false, urlPattern = "index/{id}", reset="resetForm", input=TransitionConstants.Webdata.JSP_APC02C01)
	@MethodAccess(accessCode="APPTEST_INPUT_INDEX")
	public String index() {

		// パラメータが空の場合はエラー
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, inputForm.id);

		// チェック用のIdに値を保持
		inputForm.hiddenId = inputForm.id;

		return show();
	}

	/**
	 * 戻る
	 * @return 詳細画面の初期表示
	 */
	@Execute(validator = false, reset="resetFormWithoutId")
	@MethodAccess(accessCode="APPTEST_INPUT_BACK")
	public String back() {

		// 確認画面の表示メソッドへリダイレクト
		return TransitionConstants.Webdata.ACTION_WEBDATA_DETAIL_INDEX + inputForm.id + TransitionConstants.REDIRECT_STR;
	}

	/**
	 * 確認
	 * @return 確認画面
	 */
	@Execute(validator = false, input = TransitionConstants.Webdata.JSP_APC02C01)
	@MethodAccess(accessCode="APPTEST_INPUT_CONF")
	public String conf() {

		// パラメータが空の場合はエラー
		checkArgsNull(NO_BLANK_FLG_NG, inputForm.id);

		// メールアドレスが空の場合（アドレス直打ちの対応）
		if (StringUtil.isEmpty(inputForm.salesMail)) {
			throw new FraudulentProcessException("不正な操作が行われました。" + inputForm);
		}

		// プロセスフラグを確認済みに設定
		inputForm.setProcessFlgOk();

		// 確認画面へ遷移
		return TransitionConstants.Webdata.JSP_APC02C02;
	}

	/**
	 * 訂正
	 * @return 入力画面
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode = "APPTEST_INPUT_CORRECT")
	public String correct() {

		// パラメータが空の場合はエラー
		checkArgsNull(NO_BLANK_FLG_NG, inputForm.id);

		// プロセスフラグを未確認に設定
		inputForm.setProcessFlgNg();

		// 登録画面へ遷移
		return TransitionConstants.Webdata.JSP_APC02C01;
	}

	/**
	 * 登録
	 * @return 完了へリダイレクト
	 */
	@Execute(validator = false, input = TransitionConstants.Webdata.JSP_APC02C01)
	@MethodAccess(accessCode="APPTEST_INPUT_SUBMIT")
	public String submit() {

		// プロセスフラグがfalseの場合、未確認としてエラー
		if (!inputForm.processFlg) {
			throw new FraudulentProcessException("不正な操作が行われました。" + inputForm);
		}

		// セッションのIdと、画面で保持するIdが違う場合エラー
		if (!inputForm.id.equals(inputForm.hiddenId)) {
			throw new FraudulentProcessException("不正な操作が行われました。" + inputForm);
		}

		// 登録処理
		WebdataProperty property = insert();
		// メール送信処理
		sendMail(property);

		// 完了メソッドへ遷移
		return TransitionConstants.Webdata.REDIRECT_APPTEST_INPUT_COMP;
	}

	/**
	 * 完了
	 * @return 完了画面
	 */
	@Execute(validator = false, removeActionForm = true)
	@MethodAccess(accessCode="APPTEST_INPUT_COMP")
	public String comp() {
		// 完了画面へ遷移
		return TransitionConstants.Webdata.JSP_APC02C03;
	}

	/**
	 * 初期表示遷移用
	 * @return 登録画面のパス
	 */
	private String show() {

		// IDが正しいかチェック
		checkId(inputForm, inputForm.id);

		TWeb tWeb;
		try {
			// データの取得
			tWeb = webService.findById(Integer.parseInt(inputForm.id));

		// データが無い場合はエラー
		} catch (SNoResultException e) {

			// 画面表示をしない
			inputForm.setExistDataFlgNg();

			// 「別ユーザによりデータ削除された可能性があります。」
			throw new ActionMessagesException("errors.app.possibleDelData");
		}

		WebdataProperty property = new WebdataProperty();
		property.tWeb = tWeb;
		// 応募テスト可能かチェック
		if (!webdataLogic.checkAppTest(property)) {
			throw new FraudulentProcessException("不正な操作が行われました。" + inputForm);
		}

		// フォームにコピー
		Beans.copy(tWeb, inputForm).execute();

		// 顧客データを取得して表示用にセットする
		CustomerProperty customerProperty = new CustomerProperty();
		customerProperty.mCustomer = new MCustomer();
		customerProperty.mCustomer.id = tWeb.customerId;
		try {
			// 値の取得
			customerLogic.getListRowData(customerProperty);
			// 顧客マスタの値を詰め替える
			inputForm.customerDto = customerLogic.convertSearchData(customerProperty);

		// 顧客が無い場合はエラー
		} catch (WNoResultException e) {
			// 「別ユーザによりデータ削除された可能性があります。」
			throw new ActionMessagesException("errors.app.possibleDelData");
		}

		// 連絡メール区分が入力値の場合、WEBデータの入力値を登録
		if (eqInt(MTypeConstants.CommunicationMailKbn.INPUT_MAIL, tWeb.communicationMailKbn)) {
			inputForm.mail = tWeb.mail;
		} else {
			// 連絡メール区分が顧客メールの場合、顧客マスタのメインメールをセット
			inputForm.mail = customerProperty.mCustomer.mainMail;

			// サブメール送信可の場合はサブメールをセット
			inputForm.subMailList = customerSubMailService.getReceptionSubMail(tWeb.customerId);
		}

		// 連絡メール区分を保持
		inputForm.communicationMailKbn = tWeb.communicationMailKbn;

		MSales mSales;
		try {
			// ログインユーザの情報を取得
			mSales = salesService.findById(Integer.parseInt(userDto.userId));
		} catch (SNoResultException e) {
			throw new FraudulentProcessException("不正な操作が行われました。" + inputForm);
		}

		// ログイン担当者の情報を保持
		inputForm.salesName = mSales.salesName;
		inputForm.salesMail = mSales.mainMail;

		// サブメール送信可の場合はサブメールをセット
		if (eqInt(MTypeConstants.SubmailReceptionFlg.RECEIVE, mSales.submailReceptionFlg)) {
			inputForm.salesSubMail = mSales.subMail;
		}

		// 登録画面へ遷移
		return TransitionConstants.Webdata.JSP_APC02C01;
	}

	/**
	 * 応募データを登録
	 */
	private WebdataProperty insert() {

		WebdataProperty property = new WebdataProperty();
		property.tWeb = new TWeb();
		//ID、verion、応募テストフラグをコピー
		Beans.copy(inputForm, property.tWeb).includes(toCamelCase(TWeb.ID),toCamelCase(TWeb.VERSION), toCamelCase(TWeb.APPLICATION_TEST_FLG)).execute();

		// 応募テストデータをプロパティにセット
		property.tApplicationTest = new TApplicationTest();
		// 顧客ID
		property.tApplicationTest.customerId = Integer.parseInt(inputForm.customerDto.id);
		// 営業担当者ID
		property.tApplicationTest.salesId = Integer.parseInt(userDto.userId);
		// 完了送信メインメールアドレス
		property.tApplicationTest.mainMail = inputForm.salesMail;
		// 完了送信サブメールアドレス
		if (!StringUtil.isEmpty(inputForm.salesSubMail)) {
			property.tApplicationTest.subMail = inputForm.salesSubMail;
		}
		// WEBデータID
		property.tApplicationTest.webId = Integer.parseInt(inputForm.id);
		// コメント
		property.tApplicationTest.comment = inputForm.comment;

		// 登録処理の呼び出し
		webdataLogic.insertAppTest(property);

		log.debug("応募テストを登録しました。" + inputForm);

		return property;
	}

	/**
	 * メール送信処理
	 */
	private void sendMail(WebdataProperty property) {

		Properties msgProperties = ResourceUtil.getProperties("sendMail.properties");

		// 応募管理メールアドレスを取得
		String mailAddress = msgProperties.getProperty("gc.mail.ouboAddress" + inputForm.areaCd);
		if (StringUtil.isEmpty(mailAddress)) {
			// メールアドレスが存在しないため未処理
			log.info("応募メールを送信できませんでした。応募管理アドレスが取得できません。");
			// 「{メール送信}の処理に失敗しました。再度処理を行うか、管理者へ連絡してください。」
			throw new ActionMessagesException("errors.processFailed", MessageResourcesUtil.getMessage("msg.app.sendMail"));
		}
		// 応募管理メールアドレス名を取得
		String mailName = msgProperties.getProperty("gc.mail.ouboName" + inputForm.areaCd);

		// 送信先を保持するリスト
		List<String> toAddressList = new ArrayList<String>();

		// 送信先に応募アドレスをセット
		toAddressList.add(mailAddress);

		// 連絡メール区分が入力値の場合、WEBデータの入力値を登録
		if (MTypeConstants.CommunicationMailKbn.INPUT_MAIL == inputForm.communicationMailKbn) {
			toAddressList.add(inputForm.mail);

		// 連絡メール区分が顧客メールの場合、顧客メールをセット
		} else {

			try {
				// 顧客情報を取得
				MCustomer mCustomer = customerService.findById(Integer.parseInt(inputForm.customerDto.id));
				// メインメールをセット
				toAddressList.add(mCustomer.mainMail);

				// サブメールをセット
				List<String> subMailList = customerSubMailService.getReceptionSubMail(mCustomer.id);
				if (CollectionUtils.isNotEmpty(subMailList)) {
					toAddressList.addAll(subMailList);
				}

			// 顧客が取得できない場合はエラー
			} catch (SNoResultException e) {

				// 「{顧客}が削除されている可能性があるため、{メール送信}することはできません。」
				throw new ActionMessagesException("errors.canNotProcessDel",
													MessageResourcesUtil.getMessage("msg.app.customer"),
													MessageResourcesUtil.getMessage("msg.app.sendMail"));
			}
		}

		// PC用パス作成
		String pcPath = GourmetCareeUtil.makePath(msgProperties.getProperty("gc.appTest.pcPath"), String.valueOf(property.tApplicationTest.id), property.tApplicationTest.accessCd);
		// 携帯用パス作成
		String mobilePath = GourmetCareeUtil.makePath(msgProperties.getProperty("gc.appTest.mobailePath"), String.valueOf(property.tApplicationTest.id), property.tApplicationTest.accessCd);

		// コメント作成
		String comment = "";
		if (!StringUtil.isEmpty(inputForm.comment)) {
			StringBuffer sb = new StringBuffer();
			sb.append(GourmetCareeConstants.RN_CD);
			sb.append(msgProperties.getProperty("gc.appTest.body")).append(GourmetCareeConstants.RN_CD);
			sb.append(inputForm.comment);
			sb.append(GourmetCareeConstants.RN_CD);
			comment = sb.toString();
		}

		// フッターお問い合わせメールアドレス
		String infoAddress = msgProperties.getProperty("gc.mail.infoAddress" + inputForm.areaCd);
		// フッターサイトアドレス
		String siteUrl = msgProperties.getProperty("gc.front.siteUrl" + inputForm.areaCd);

		// メール送信Dtoにセット
		ApplicationTestMaiDto mailDto = new ApplicationTestMaiDto();

		 // 送信元
		try {
			mailDto.setFrom(mailAddress, mailName);
		// アドレスがセットできない場合、初期値で送信
		} catch (UnsupportedEncodingException e) {
			// 送信元のセットに失敗した場合はエラーを送信する。（mailProperties.diconの値がセットされる）
			log.fatal("応募テスト送信時、送信元がセットできませんでした。" + e);
		}
		// 顧客名
		mailDto.setCustomerName(inputForm.customerDto.customerName);
		// PCアクションパス
		mailDto.setPcPath(pcPath);
		// 携帯アクションパス
		mailDto.setMobilePath(mobilePath);
		// コメント
		mailDto.setComment(comment);
		// お問い合わせメールアドレス
		mailDto.setInfoAddress(infoAddress);
		// フッターサイトアドレス
		mailDto.setSiteURL(siteUrl);

		// 送信先をセットして応募メールを送信
		for (String toAddress : toAddressList) {
			// 送信先をセット
			mailDto.resetTo();
			mailDto.addTo(toAddress);
			// メール送信処理実行
			gourmetCareeMai.sendApplicationTestMail(mailDto);
		}
	}

	/**
	 * 一覧へ戻る
	 * @return 一覧画面の検索メソッド
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="APPTEST_INPUT_BACKLIST")
	public String backList() {
		// 一覧画面へ遷移
		return TransitionConstants.Webdata.REDIRECT_WEBDATA_LIST_SEARCHAGAIN;
	}

}