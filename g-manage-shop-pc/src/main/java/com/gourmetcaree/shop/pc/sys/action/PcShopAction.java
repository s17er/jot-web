package com.gourmetcaree.shop.pc.sys.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.ResponseUtil;

import com.gourmetcaree.common.action.BaseAction;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.exception.PageNotFoundException;
import com.gourmetcaree.common.form.BaseForm;
import com.gourmetcaree.shop.logic.logic.ApplicationLogic;
import com.gourmetcaree.shop.logic.logic.PreApplicationLogic;
import com.gourmetcaree.shop.logic.logic.ScoutMailLogic;
import com.gourmetcaree.shop.pc.sys.dto.UserDto;
import com.gourmetcaree.shop.pc.valueobject.MenuInfo;

import net.arnx.jsonic.JSON;

/**
 * PC版管理画面の共通アクションです。<br>
 * このクラスを継承して下さい。
 * @author Takahiro Ando
 * @version 1.0
 */
public abstract class PcShopAction extends BaseAction {

	/** ユーザDTO */
	@Resource
	public UserDto userDto;

	/** 応募ロジック */
	@Resource
	protected ApplicationLogic applicationLogic;

	/** プレ応募ロジック */
	@Resource
	protected PreApplicationLogic preApplicationLogic;

	/** スカウトメールロジック */
	@Resource
	protected ScoutMailLogic scoutMailLogic;

	/** 未読応募メールフラグ */
	public boolean unReadApplicationMailFlg;

	/** 未読プレ応募メールフラグ */
	public boolean unReadPreApplicationMailFlg;

	/** 未読スカウトメールフラグ */
	public boolean unReadScoutMailFlg;

	/** 未読質問メールフラグ */
	public boolean unReadObservateApplicationMailFlg;

	/** 未読アルバイトメールフラグ */
	public boolean unReadArbeitMailFlg;

	/** 引数チェックでブランクを許容するかどうかのフラグ - 許容しない */
	protected final boolean NO_BLANK_FLG_NG = true;

	/** 引数チェックでブランクを許容するかどうかのフラグ - 許容する */
	protected final boolean NO_BLANK_FLG_ADMIT = false;

	/** プレビュー画面セッション用キー */
	public static final String WEBDATA_PREVIEW_DTO_KEY = "WEBDATA_PREVIEW_DTO";

	/**
	 * 渡された引数がひとつでもnullかどうかをチェックする。<br />
	 * また、IDが不正で無いかチェックする。
	 * ひとつでもnull、不正なIDがあればページが見つからない旨のエラーをthrow
	 * @param noBlankFlg trueであればブランク("")もエラーとする。
	 * @param args
	 */
	protected void checkArgsNullPageNotFound(boolean noBlankFlg, String... args) {

		if (args == null) {
			throw new PageNotFoundException();
		}

		for (String str : args) {
			if (str == null) {
				throw new PageNotFoundException();
			} else if (noBlankFlg) {

				if (StringUtils.isEmpty(str)) {
					throw new PageNotFoundException();
				}
			}
		}
	}

	/**
	 * 渡された引数がひとつでもnullかどうかをチェックする。
	 * ひとつでもnullがあれば不正な操作である旨のエラーをthrow
	 * @param noBlankFlg trueであればブランク("")もエラーとする。
	 * @param args
	 */
	protected void checkArgsNull(boolean noBlankFlg, String... args) {

		if (args == null) {
			throw new FraudulentProcessException();
		}

		for (String str : args) {
			if (str == null) {
				throw new FraudulentProcessException();
			} else if (noBlankFlg) {

				if (StringUtils.isEmpty(str)) {
					throw new FraudulentProcessException();
				}
			}
		}
	}

	/**
	 * IDに不正が無いかチェックする。<br />
	 * 不正な場合は、データが見つからないエラーメッセージを返却する。
	 * @param form 画面表示フラグの設定用フォーム
	 * @param id チェックするID
	 */
	protected void checkId(BaseForm form, String id) {

		try {
			// IDが不正かどうかチェック
			Integer.parseInt(id);

		} catch (NumberFormatException e) {

			// 画面を非表示に設定
			form.setExistDataFlgNg();

			// idが数値でない場合、データなしと同じエラーを返す。
			throw new ActionMessagesException("errors.app.dataNotFound");
		}
	}

	/**
	 * 数値チェック
	 * @param str
	 */
	protected void checkNumber(String str) {
		try {
			Integer.parseInt(str);
		} catch (NumberFormatException e) {
			throw new FraudulentProcessException();
		}
	}

    /**
     * メニュー情報の作成。
     * 各画面上部のメニューバーでどのメニューを使用中かをJSPに渡すために定義している。
     */
	public MenuInfo getMenuInfo() {
	    return MenuInfo.emptyInstance();
    }

	/**
	 * JSONを書き出します。
	 * @param json JSON
	 * @author Takehiro Nakamori
	 */
	protected void writeJson(Object obj) {
		ResponseUtil.write(JSON.encode(obj), GourmetCareeConstants.JSON_CONTENT_TYPE, GourmetCareeConstants.ENCODING);
		writeJson(obj, HttpServletResponse.SC_OK);
	}

	/**
	 * JSONを書き出します。
	 * @param obj
	 * @param statusCode
	 */
	protected void writeJson(Object obj, int statusCode) {
		response.setStatus(statusCode);
		ResponseUtil.write(JSON.encode(obj), GourmetCareeConstants.JSON_CONTENT_TYPE, GourmetCareeConstants.ENCODING);
	}


	/**
	 * 未読の各種メールがあるか判定する
	 * (正直この方法は無理矢理実装しているので要改善)
	 */
	protected void checkUnReadMail() {
		unReadApplicationMailFlg = applicationLogic.checkUnReadApplicationMail();
		unReadPreApplicationMailFlg = preApplicationLogic.checkUnReadPreApplicationMail();
		unReadScoutMailFlg = scoutMailLogic.checkUnReadScoutMail();
		unReadObservateApplicationMailFlg = applicationLogic.checkUnReadObservateApplicationMail();
		unReadArbeitMailFlg = applicationLogic.checkUnReadArbeitMail();
	}
}
