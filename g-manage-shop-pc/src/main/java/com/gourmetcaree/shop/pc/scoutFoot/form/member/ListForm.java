package com.gourmetcaree.shop.pc.scoutFoot.form.member;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.db.common.helper.PageNavigateHelper;

import jp.co.whizz_tech.common.sastruts.annotation.IntegerHankakuType;


/**
 * 求職者検索一覧フォームです。
 * @author Takahiro Kimura
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class ListForm extends MemberForm {


	/** シリアルバージョンUID */
	private static final long serialVersionUID = 144708695422939689L;

	/** ページナビゲーター */
	public PageNavigateHelper pageNavi;

	/** ページ遷移用に選択されたページ数 */
	public String pageNum;

	/** 希望業種 */
	public String[] where_industryCd;

	/** 雇用形態 */
	public String[] where_empPtnKbn;

	/** 職種 */
	public String[] where_job;

	/** 取得資格 */
	public String[] where_qualification;

	/** 希望勤務地 */
	public String[] where_workLocation;

	/** 性別 */
	public String where_sexKbn = "";

	/** 下限年齢 */
	@IntegerHankakuType
	public String where_lowerAge = "";

	/** 上限年齢 */
	@IntegerHankakuType
	public String where_upperAge = "";

	/** 転勤可 */
	public String where_transferFlg = "";

	/** 深夜勤務可 */
	public String where_midnightShiftFlg = "";

	/** キーワード */
	public String where_keyword;

	/** 希望都道府県 */
	public String[] where_prefList;

	/** 住所 */
	public String[] where_addressList;

	/** 希望地域 */
	public String[] where_areaList;

	public String where_scoutedFlg;

	public String where_favoriteFlg;

	public String where_subscFlg;

	public String where_refuseFlg;

	/** 会員ID(Ajax用) */
	public String limitValue;

	/** ターゲットID */
	public String targetId;

	/** 検索条件保存済フラグ */
	public boolean searchConditionSavedFlg;

	/** 最大表示件数 */
	public String maxRow;

	/**
	 *  独自チェックを行う
	 * @return ActionMessages
	 */
	public ActionMessages validate() {

		// エラー情報
		ActionMessages errors = new ActionMessages();

		if (StringUtils.isNotEmpty(where_lowerAge) && StringUtils.isNotEmpty(where_upperAge)) {
			try {
				// 上限年齢が下限年齢より小さい場合はエラー
				if (Integer.parseInt(where_lowerAge) > Integer.parseInt(where_upperAge)) {
					errors.add("errors", new ActionMessage("errors.fraudurentAge"));
				}
			} catch (NumberFormatException e1) {
				errors.add("errors", new ActionMessage("errors.ageFailed"));
			}
		}

		return errors;
	}

	public ActionMessages saveValidate() {

		ActionMessages errors = new ActionMessages();

		if(where_empPtnKbn != null
				||where_industryCd != null
				||where_workLocation != null
				||where_job != null
				||where_areaList != null
				||where_qualification != null
				||where_prefList != null
				||where_addressList != null
				||StringUtils.isNotEmpty(where_sexKbn)
				||StringUtils.isNotEmpty(where_lowerAge)
				||StringUtils.isNotEmpty(where_upperAge)
				||StringUtils.isNotEmpty(where_keyword)
				||StringUtils.isNotEmpty(where_transferFlg)
				||StringUtils.isNotEmpty(where_midnightShiftFlg)
				||StringUtils.isNotEmpty(where_scoutedFlg)
				||StringUtils.isNotEmpty(where_favoriteFlg)
				||StringUtils.isNotEmpty(where_subscFlg)
				||StringUtils.isNotEmpty(where_refuseFlg)) {
			return errors;
		}

		errors.add("errors", new ActionMessage("errors.saveConditions"));

		return errors;
	}

	/**
	 * リセットを行う
	 */
	public void resetForm() {
		super.resetForm();
		pageNavi = null;
		pageNum = null;
		where_industryCd = null;
		where_empPtnKbn = null;
		where_job = null;
		where_qualification = null;
		where_workLocation = null;
		where_sexKbn = "";
		where_lowerAge = "";
		where_upperAge = "";
		where_transferFlg = "";
		where_midnightShiftFlg = "";
		where_keyword = null;
		where_prefList = null;
		where_addressList= null;
		where_areaList = null;
		limitValue = null;
		targetId = null;
		where_scoutedFlg = null;
		where_favoriteFlg = null;
		where_subscFlg = null;
		where_refuseFlg = null;
		maxRow = null;
	}

	/**
	 * チェックBOXをリセット
	 */
	public void resetMultiBox() {

		where_transferFlg = "";
		where_midnightShiftFlg = "";
		where_scoutedFlg = "";
		where_favoriteFlg = "";
		where_subscFlg = "";
		where_refuseFlg = "";
		where_prefList = null;
		where_addressList = null;
		where_areaList = null;
		where_job = null;
		where_workLocation = null;
		where_industryCd = null;
		where_empPtnKbn = null;
		where_qualification = null;
		checkId = new String[0];
	}
}
