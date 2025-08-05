package com.gourmetcaree.shop.pc.pattern.action.pattern;

import javax.annotation.Resource;

import com.gourmetcaree.shop.pc.valueobject.MenuInfo;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.db.common.entity.MSentence;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.SentenceService;
import com.gourmetcaree.shop.logic.logic.PatternLogic;
import com.gourmetcaree.shop.pc.pattern.form.pattern.PatternForm;
import com.gourmetcaree.shop.pc.sys.action.PcShopAction;

/**
 * 定型文のBaseアクションクラスです。
 * @author Makoto Otani
 * @version 1.0
 */
public abstract class PatternBaseAction extends PcShopAction {

	/** 定型文ロジック */
	@Resource
	protected PatternLogic patternLogic;

	/** 定型文サービス */
	@Resource
	protected SentenceService sentenceService;

	/**
	 * 定型文マスタからデータを取得する
	 * @return 定型文マスタエンティティ
	 *
	 */
	protected MSentence getData(PatternForm form) {

		// IDが正常かチェック
		checkId(form, form.id);

		try {
			// データの取得
			MSentence mSentence = patternLogic.getDetailSentence(Integer.parseInt(form.id));

			// 値が空の場合はエラーメッセージを返す
			if (mSentence == null) {

				// 画面表示をしない
				form.setExistDataFlgNg();
				// 「該当するデータが見つかりませんでした。」
				throw new ActionMessagesException("errors.app.dataNotFound");
			}

			// 取得したエンティティを返却
			return mSentence;

		// データが取得できない場合はエラー
		} catch (WNoResultException e) {

			// 画面表示をしない
			form.setExistDataFlgNg();
			// 「該当するデータが見つかりませんでした。」
			throw new ActionMessagesException("errors.app.dataNotFound");
		}
	}

	@Override
	public MenuInfo getMenuInfo() {
		return MenuInfo.patternInstance();
	}
}
