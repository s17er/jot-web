package com.gourmetcaree.shop.pc.pattern.action.pattern;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.db.common.entity.MSentence;
import com.gourmetcaree.db.common.service.SentenceService;
import com.gourmetcaree.shop.pc.pattern.form.pattern.InputForm;
import com.gourmetcaree.shop.pc.sys.action.PcShopAction;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.shop.pc.valueobject.MenuInfo;

import jp.co.whizz_tech.common.sastruts.converter.ZenkakuKanaConverter;

/**
 * 定型文の入力をするアクションクラスです。
 * @author Takahiro Kimura
 * @version 1.0
 */
@ManageLoginRequired()
public class InputAction extends PcShopAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(InputAction.class);

	/** ログオブジェクト（なりすまし） */
	private static final Logger sysMasqueradeLog = Logger.getLogger("sysMasqueradeLog");

	/** 定型文登録フォーム */
	@ActionForm
	@Resource
	protected InputForm inputForm;

	/** 定型文サービス */
	@Resource
	protected SentenceService sentenceService;

	/**
	 * 初期表示
	 * @return 入力画面
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Pattern.JSP_SPF01C01)
	public String index() {
		// プロセスフラグを確認済みに設定
		inputForm.setProcessFlgOk();
		return show();
	}

//	2022年4月5日　管理画面リニューアルで確認画面未使用になったためコメントアウト
//	/**
//	 * 確認画面表示
//	 * @return 確認画面
//	 */
//	@Execute(validator = true, input = TransitionConstants.Pattern.JSP_SPF01C01)
//	public String conf() {
//
//		// プロセスフラグを確認済みに設定
//		inputForm.setProcessFlgOk();
//
//		// 確認画面へ遷移
//		return TransitionConstants.Pattern.JSP_SPF01C02;
//	}

	/**
	 * 戻る
	 * @return 一覧画面の検索メソッド
	 */
	@Execute(validator = false)
	public String back() {

		// 一覧画面の検索メソッドへ遷移
		return TransitionConstants.Pattern.ACTION_PATTERN_LIST_INDEX;
	}

//	2022年4月5日　管理画面リニューアルで確認画面未使用になったためコメントアウト
//	/**
//	 * 訂正
//	 *
//	 * @return 入力画面
//	 */
//	@Execute(validator = false)
//	public String correct() {
//
//		// プロセスフラグを未確認に設定
//		inputForm.setProcessFlgNg();
//
//		// 登録画面へ遷移
//		return TransitionConstants.Pattern.JSP_SPF01C01;
//	}

	/**
	 * 登録完了
	 * @return 完了へリダイレクト
	 */
	@Execute(validator = true, input = TransitionConstants.Pattern.JSP_SPF01C01)
	public String submit() {

		// プロセスフラグがfalseの場合、未確認としてエラー
		if (!inputForm.processFlg) {
			throw new FraudulentProcessException("不正な操作が行われました。" + inputForm);
		}

		// 登録処理の呼び出し
		insert();

		// 完了メソッドへリダイレクト
		return TransitionConstants.Pattern.REDIRECT_PATTERN_INPUT_COMP;
	}

	/**
	 * 完了画面表示
	 */
	@Execute(validator = false, removeActionForm = true)
	public String comp() {

		checkUnReadMail();
		// 完了画面へ遷移
		return TransitionConstants.Pattern.JSP_SPF01C03;
	}

	/**
	 * 初期表示遷移用
	 * @return 入力画面のパス
	 */
	private String show() {

		checkUnReadMail();
		// 登録画面へ遷移
		return TransitionConstants.Pattern.JSP_SPF01C01;
	}

	/**
	 * 登録処理
	 */
	private void insert() {

		// 定型文マスタエンティティ
		MSentence entity = new MSentence();

		// 定型文マスタエンティティにフォームをコピー
		Beans.copy(inputForm, entity).converter(new ZenkakuKanaConverter()).execute();

		// 顧客IDをセット
		entity.customerId = userDto.customerId;

		// 定型文データを登録
		sentenceService.insert(entity);

		log.debug("定型文マスタを登録しました。" + inputForm);

		if (userDto.isMasqueradeFlg()) {
			sysMasqueradeLog.debug(String.format("定型文マスタを登録しました。営業ID：%s, 顧客ID：%s", userDto.masqueradeUserId, userDto.customerId));
			sysMasqueradeLog.debug(String.format("定型文マスタを登録しました。営業ID： " + userDto.masqueradeUserId + " " + inputForm));
		}
	}

	@Override
	public MenuInfo getMenuInfo() {
		return MenuInfo.patternInstance();
	}
}
