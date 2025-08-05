package com.gourmetcaree.shop.pc.scoutFoot.action.member;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.db.common.service.ScoutConsiderationService;
import com.gourmetcaree.shop.logic.dto.MemberInfoDto;
import com.gourmetcaree.shop.logic.dto.ScoutMailRemainDto;
import com.gourmetcaree.shop.logic.logic.CustomerLogic;
import com.gourmetcaree.shop.logic.logic.KeepBoxLogic;
import com.gourmetcaree.shop.logic.logic.MemberLogic;
import com.gourmetcaree.shop.logic.logic.SaveConditionLogic;
import com.gourmetcaree.shop.logic.property.ScoutMailProperty;
import com.gourmetcaree.shop.pc.scoutFoot.form.member.MemberForm;
import com.gourmetcaree.shop.pc.sys.action.PcShopAction;
import com.gourmetcaree.shop.pc.valueobject.MenuInfo;

/**
 * スカウト足あと(会員)Baseアクションクラス
 * @author Takahiro Kimura
 * @version 1.0
 *
 */
abstract public class MemberBaseAction extends PcShopAction {

	/** 会員ロジック */
	@Resource
	protected MemberLogic memberLogic;

	/** キープボックスロジック */
	@Resource
	protected KeepBoxLogic keepBoxLogic;

	/** 顧客ロジック */
	@Resource
	protected CustomerLogic customerLogic;

	@Resource
	protected SaveConditionLogic saveConditionLogic;

	/** スカウト検討中サービス */
	@Resource
	protected ScoutConsiderationService scoutConsiderationService;

	/** 会員情報DTOリスト */
	public List<MemberInfoDto> memberDtoList;

	/** スカウトメール残数DTO */
    public ScoutMailRemainDto scoutMailRemainDto;

    /** スカウトメール使用フラグ */
	public boolean scoutUseFlg;

    protected MenuInfo menuInfo;

	/**
	 * スカウトメール状況をフォームにセット
	 * @param form 会員フォーム
	 */
	protected void setScoutStatus() {

		scoutMailRemainDto = memberLogic.getRemainScoutMail(userDto.customerId);
		// TODO
		// スカウトメール使用可否フラグをセット
		if (memberLogic.isScoutUse(userDto.customerId)
				&& scoutMailRemainDto.isExistRemainScoutMail()
				&& checkScoutReceptionFlg()) {
			scoutUseFlg = true;
		} else {
			scoutUseFlg = false;
		}

	}

	/**
	 * 一覧表示する会員のスカウトメール受信フラグをチェック
	 * @param form
	 * @return 一人でもスカウトメール受信可能であればtrueを返す
	 */
	private boolean checkScoutReceptionFlg() {
		if(memberDtoList != null) {
			for (MemberInfoDto dto : memberDtoList) {
				if (dto.scoutMailOkFlg) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * スカウトメールが送信可能かどうかチェック
	 */
	protected boolean isPossibleScout(MemberForm form) {

		try {
			ScoutMailProperty property = new ScoutMailProperty();
			property.customerId = userDto.customerId;
			property.memberIdList = new ArrayList<Integer>();
			for (String memberId : form.checkId) {
				property.memberIdList.add(Integer.parseInt(memberId));
			}

			return scoutMailLogic.isPossibleScout(property);

		} catch (NumberFormatException e) {
			form.setExistDataFlgNg();
			throw new FraudulentProcessException("不正な操作が行われました。");
		}
	}

    @Override
    public MenuInfo getMenuInfo() {
        return menuInfo == null ?
                MenuInfo.scoutInstance() :
                MenuInfo.mailInstance();
    }
}