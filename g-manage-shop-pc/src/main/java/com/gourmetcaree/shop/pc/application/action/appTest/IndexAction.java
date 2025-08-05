package com.gourmetcaree.shop.pc.application.action.appTest;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.common.exception.PageNotFoundException;
import com.gourmetcaree.db.common.entity.TApplicationTest;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.ApplicationTestService;
import com.gourmetcaree.shop.logic.property.ApplicationProperty;
import com.gourmetcaree.shop.pc.application.form.appTest.IndexForm;
import com.gourmetcaree.shop.pc.sys.action.PcShopAction;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;

/**
 *
 * 応募テスト確認を行うクラス
 * @author Makoto Otani
 *
 */
public class IndexAction extends PcShopAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(IndexAction.class);

	/** ログオブジェクト（なりすまし） */
	private static final Logger sysMasqueradeLog = Logger.getLogger("sysMasqueradeLog");

	/** フォーム */
	@ActionForm
	@Resource
	protected IndexForm indexForm;

	/** 応募テストサービス */
	@Resource
	protected ApplicationTestService applicationTestService;

	/**
	 * 初期表示
	 * @return 入力画面
	 */
	@Execute(validator = false, urlPattern = "index/{id}/{accessCd}", reset="resetForm", input = TransitionConstants.Application.JSP_SPC03R01)
	public String index() {

		// パラメータが空の場合はエラー
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, indexForm.id, indexForm.accessCd);

		try {
			// IDが不正かどうかチェック
			Integer.parseInt(indexForm.id);
		} catch (NumberFormatException e) {
			// idが数値でない場合、データなしと同じエラーを返す。
			throw new PageNotFoundException();
		}

		return show();
	}

	/**
	 * 完了
	 * @return 完了画面
	 */
	@Execute(validator = false, removeActionForm = true)
	public String comp() {
		// 完了画面へ遷移
		return TransitionConstants.Application.JSP_SPC03R01;
	}

	/**
	 * 初期表示遷移用
	 * @return 登録画面のパス
	 */
	private String show() {

		// フォームをエンティティにコピー
		TApplicationTest entity = new TApplicationTest();
		Beans.copy(indexForm, entity).execute();

		// データが存在しない場合はページがみつからない画面へ遷移
		if (!applicationTestService.isAppTestConfExists(entity)) {
			throw new PageNotFoundException();
		}

		try {
			// すでにアクセスされている場合は処理せずに完了メソッドへリダイレクト
			if (applicationTestService.isAlreadyAccess(entity)) {
				return TransitionConstants.Application.REDIRECT_APPTEST_INDEX_COMP;
			}

		// データが存在しない場合はページがみつからない画面へ遷移
		} catch (WNoResultException e) {
			throw new PageNotFoundException();
		}

		// 更新処理
		applicationTestService.updateApptestConf(entity);
		log.debug("データを更新しました。");
		if (userDto.isMasqueradeFlg()) {
			sysMasqueradeLog.debug(String.format("応募テスト確認のデータを更新しました。営業ID：%s, 顧客ID：%s", userDto.masqueradeUserId, userDto.customerId));
		}

		// メール送信
		sendMail(entity);
		log.debug("メールを送信しました。");
		if (userDto.isMasqueradeFlg()) {
			sysMasqueradeLog.debug(String.format("応募テスト確認のメールを送信しました。営業ID：%s, 顧客ID：%s", userDto.masqueradeUserId, userDto.customerId));
		}

		// 完了メソッドへリダイレクト
		return TransitionConstants.Application.REDIRECT_APPTEST_INDEX_COMP;
	}

	/**
	 * 応募確認のメールを送信する
	 * @param entity 応募テストエンティティ
	 */
	private void sendMail(TApplicationTest entity) {

		// プロパティにセット
		ApplicationProperty property = new ApplicationProperty();
		property.tApplicationTest = entity;
		// メール送信処理
		applicationLogic.sendAppTestConfMail(property);

	}
}