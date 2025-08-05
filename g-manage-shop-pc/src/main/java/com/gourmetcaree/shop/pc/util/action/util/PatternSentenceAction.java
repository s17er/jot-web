package com.gourmetcaree.shop.pc.util.action.util;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ResponseUtil;

import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.db.common.entity.MSentence;
import com.gourmetcaree.db.common.service.MemberService;
import com.gourmetcaree.db.common.service.SentenceService;
import com.gourmetcaree.shop.pc.sys.action.PcShopAction;
import com.gourmetcaree.shop.pc.util.form.util.PatternSentenceForm;
import com.gourmetcaree.shop.pc.valueobject.MenuInfo;


/**
 * メール入力をするアクションクラスです。
 * @author Takahiro Kimura
 * @version 1.0
 */
@ManageLoginRequired
public class PatternSentenceAction extends PcShopAction {

	/** メール入力フォーム */
	@ActionForm
	@Resource
	protected PatternSentenceForm patternSentenceForm;

	/** 定型文サービス */
	@Resource
	protected SentenceService sentenceService;

	@Resource
	protected MemberService memberService;

	/**
	 * 定型文を反映
	 * @return
	 */
	@Execute(validator = false)
	public String addSentence() {

		if (StringUtils.isEmpty(patternSentenceForm.limitValue)) {
			ResponseUtil.write("errorNoSelect");
		}

		// 定型文を本文へ返す
		returnSentence();

		return null;
	}

	/**
	 * メール件名を反映
	 * @return
	 */
	@Execute(validator = false)
	public String addTitle() {

		if (StringUtils.isEmpty(patternSentenceForm.limitValue)) {
			ResponseUtil.write("errorNoSelect");
		}

		// メール件名を本文へ返す
		retuenTitle();

		return null;
	}

	/**
	 * 定型文を本文へ返す
	 */
	private void returnSentence() {

		try {
			MSentence mSentence = sentenceService.findById(NumberUtils
													.toInt(patternSentenceForm.limitValue));

			//ログインユーザと顧客IDが一致したもののみ
			if (mSentence.customerId != userDto.customerId) {
				ResponseUtil.write("errorSentence");
			}

			ResponseUtil.write(mSentence.body);

		} catch (SNoResultException e) {
			ResponseUtil.write("errorSentence");
		}
	}

	/**
	 * メール件名を返す
	 */
	private void retuenTitle() {

		try {
			MSentence mSentence = sentenceService.findById(NumberUtils
													.toInt(patternSentenceForm.limitValue));

			//ログインユーザと顧客IDが一致したもののみ
			if (mSentence.customerId != userDto.customerId) {
				ResponseUtil.write("errorSentence");
			}

			ResponseUtil.write(mSentence.sentenceTitle);

		} catch (SNoResultException e) {
			ResponseUtil.write("errorSentence");
		}
	}

	@Override
	public MenuInfo getMenuInfo() {
		return MenuInfo.patternInstance();
	}
}
