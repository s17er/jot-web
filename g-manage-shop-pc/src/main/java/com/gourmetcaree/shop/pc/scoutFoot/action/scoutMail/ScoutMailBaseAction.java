package com.gourmetcaree.shop.pc.scoutFoot.action.scoutMail;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;

import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.service.MailService;
import com.gourmetcaree.db.common.service.SentenceService;
import com.gourmetcaree.db.common.service.WebListService;
import com.gourmetcaree.shop.logic.logic.MemberLogic;
import com.gourmetcaree.shop.logic.property.ScoutMailProperty;
import com.gourmetcaree.shop.pc.scoutFoot.form.scoutMail.ScoutMailForm;
import com.gourmetcaree.shop.pc.sys.action.PcShopAction;
import com.gourmetcaree.shop.pc.valueobject.MenuInfo;

/**
 * スカウトメールBaseアクションクラス
 * @author Takahiro Kimura
 * @version 1.0
 *
 */
abstract public class ScoutMailBaseAction extends PcShopAction {

	/** 会員ロジック */
	@Resource
	protected MemberLogic memberLogic;

	/** メールサービス */
	@Resource
	protected MailService mailService;

	/** 定型文サービス */
	@Resource
	protected SentenceService sentenceService;

	/** WEBリストサービス */
	@Resource
	protected WebListService webListService;

    protected MenuInfo menuInfo;
	/**
	 * 送受信区分を置き換える
	 * @param sendKbn 送受信区分
	 * @return 送受信区分を返す
	 */
	protected int replaceSendKbn(String sendKbn) {

		if (String.valueOf(MTypeConstants.SendKbn.SEND).equals(sendKbn)) {
			return MTypeConstants.SendKbn.SEND;
		} else {
			return MTypeConstants.SendKbn.RECEIVE;
		}
	}

	/**
	 * 返信可能かどうかチェック
	 * @param form
	 */
	protected void isPossibleReturnMail(ScoutMailForm form) {

		// 返信可能かどうかチェック
		ScoutMailProperty property = new ScoutMailProperty();

		try {
			property.customerId = userDto.customerId;
			property.mailId = Integer.parseInt(form.id);
		} catch (NumberFormatException e) {
			throw new FraudulentProcessException("不正な操作が行われました。" + form);
		}
		scoutMailLogic.isPossibleReturnMail(property);
	}


	/**
	 * 返信可能かどうかチェック
	 * @param mailIdList
	 */
	protected void isPossibleReturnMail(List<Integer> mailIdList, List<Integer> scoutMailLogIdList, List<Integer> memberIdList) {
		if (CollectionUtils.isEmpty(mailIdList) || CollectionUtils.isEmpty(scoutMailLogIdList) || CollectionUtils.isEmpty(memberIdList)) {
			throw new FraudulentProcessException("対象メールがありません。");
		}

		int mailIdSize = mailIdList.size();
		if (mailIdSize != scoutMailLogIdList.size() || mailIdSize != memberIdList.size()) {
			throw new FraudulentProcessException("スカウトメールのIDとログIDの数が合いません。");
		}



		for (int index = 0; index < mailIdSize; index++) {
			int mailId = mailIdList.get(index);
			int scoutMailLogId = scoutMailLogIdList.get(index);
			int memberId = memberIdList.get(index);
			ScoutMailProperty property = new ScoutMailProperty();
			property.customerId = userDto.customerId;
			property.mailId = mailId;

			scoutMailLogic.isPossibleReturnMail(property);
			if (!scoutMailLogic.isScoutMailLogIdCorrect(mailId, scoutMailLogId, memberId)) {
				throw new FraudulentProcessException();
			}

		}


	}

    @Override
    public MenuInfo getMenuInfo() {
        return menuInfo == null ?
                MenuInfo.scoutInstance() :
                menuInfo;
    }


}