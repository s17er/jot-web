package com.gourmetcaree.shop.pc.scoutFoot.action.member;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.shop.logic.property.MemberProperty;
import com.gourmetcaree.shop.pc.scoutFoot.form.member.SubMemberDetailForm;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;

/**
 * 会員詳細情報を表示するアクションクラスです。
 * @author motoaki hara
 * @version 1.0
 */
public class SubMemberDetailAction extends MemberBaseAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(DetailAction.class);

	/** ログオブジェクト（なりすまし） */
	private static final Logger sysMasqueradeLog = Logger.getLogger("sysMasqueradeLog");

	/** 会員詳細情報フォーム */
	@ActionForm
	@Resource
	public SubMemberDetailForm subMemberDetailForm;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", urlPattern = "index/{id}")
	public String index() {

		return show();
	}

	/**
	 * 初期表示遷移用
	 * @return
	 */
	private String show() {

		// idがnullかチェック
		checkArgsNull(NO_BLANK_FLG_NG, subMemberDetailForm.id);

		// 表示データを生成
		convertDispData();
		log.debug("詳細データ取得");

		if (userDto.isMasqueradeFlg()) {
			sysMasqueradeLog.debug(String.format("会員詳細情報を取得しました。営業ID：%s, 顧客ID：%s", userDto.masqueradeUserId, userDto.customerId));
		}


		return TransitionConstants.ScoutFoot.JSP_SPE01R02_SUB;
	}

	/**
	 * 表示データをフォームにセットする
	 * @param form 会員フォーム
	 */
	private void convertDispData() {

		try {
			MemberProperty property = new MemberProperty();
			property.memberId = Integer.parseInt(subMemberDetailForm.id);
			property.customerId = userDto.customerId;
			property.areaCd = userDto.areaCd;

			// 会員情報・会員属性情報をフォームにセット
			subMemberDetailForm.dto = memberLogic.convertDispData(property);

		} catch (NumberFormatException e) {
			subMemberDetailForm.setExistDataFlgNg();
			// idが数値でない場合、データなしと同じエラーを返す。
			throw new ActionMessagesException("errors.app.dataNotFound");
		} catch (WNoResultException e) {
			subMemberDetailForm.setExistDataFlgNg();
			// データなしのエラーを返す。
			throw new ActionMessagesException("errors.app.dataNotFound");
		}
	}
}
