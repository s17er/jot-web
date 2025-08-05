package com.gourmetcaree.shop.pc.scoutFoot.action.scoutMember;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.property.PagerProperty;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.common.service.ScoutMailLogService;
import com.gourmetcaree.shop.logic.dto.ApplicationMailRetDto;
import com.gourmetcaree.shop.logic.dto.MailSelectDto;
import com.gourmetcaree.shop.logic.logic.ApplicationLogic.MAIL_KBN;
import com.gourmetcaree.shop.logic.logic.ScoutMailLogic.ScoutMailTargetDto;
import com.gourmetcaree.shop.logic.logic.ScoutMailLogic.ScoutmailSearchProperty;
import com.gourmetcaree.shop.pc.application.dto.application.MailForApplicationIdDto;
import com.gourmetcaree.shop.pc.scoutFoot.form.scoutMember.DetailMailListForm;
import com.gourmetcaree.shop.pc.sys.action.MailListAction;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.shop.pc.valueobject.MenuInfo;


/**
 * 応募者毎のメールリストを表示するアクションクラスです。
 * スカウトメールのログIDをキーに、送受信したメールの一覧を表示する画面。
 * @author Yamane
 * @version 1.0
 */
@ManageLoginRequired
public class DetailMailListAction extends MailListAction {

	/** スカウト詳細フォーム */
	@ActionForm
	@Resource
	protected DetailMailListForm detailMailListForm;

	/** スカウトロジック */
	@Resource
	protected ScoutMailLogService scoutMailLogService;

	/** スカウト会員情報リスト */
	public ScoutMailTargetDto scoutMailTargetDto;

	/** メールを保持するリスト */
	public List<MailForApplicationIdDto> dataList;

	/** ページナビゲータ */
	public PageNavigateHelper pageNavi;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, urlPattern = "index/{mailLogId}", reset = "resetForm", input = TransitionConstants.Application.JSP_SPC06L03)
	public String index() {

		//必要なパラメータをチェック
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, detailMailListForm.mailLogId);
		detailMailListForm.fromMenu = DetailMailListForm.FromMenuKbn.SCOUT_MAIL;
		detailMailListForm.pageNum = String.valueOf(GourmetCareeConstants.DEFAULT_PAGE);


		return show(GourmetCareeConstants.DEFAULT_PAGE);
	}

	/**
	 * スカウト側メール一覧(送信者)のリンクから表示する画面。
	 * 通常の表記と同じだが、メールの切り替えプルダウンの表示を行わない画面とする。
	 * また、ページ下部の「戻る」ボタンもリンク元のものに合わせて切り替える。
	 * @return
	 */
	@Execute(validator = false, urlPattern = "list/{mailLogId}", reset = "resetForm", input = TransitionConstants.Application.JSP_SPC06L03)
	public String list() {
		final String jsp = index();
		detailMailListForm.hideHeaderFlg = true;
		return jsp;
	}


	/**
	 * メールボックス側メール一覧(送信者)のリンクから表示する画面。
	 * 通常の表記と同じだが、メールの切り替えプルダウンの表示を行わない画面とする。
	 * また、ページ下部の「戻る」ボタンもリンク元のものに合わせて切り替える。
	 * @return
	 */
	@Execute(validator = false, urlPattern = "listFromMail/{mailLogId}", reset = "resetForm", input = TransitionConstants.Application.JSP_SPC06L03)
	public String listFromMail() {
		final String jsp = indexFromMail();
		detailMailListForm.hideHeaderFlg = true;
		return jsp;
	}

	/**
	 * 初期表示(メールBOXからの遷移)
	 * @return
	 */
	@Execute(validator = false, urlPattern = "indexFromMail/{mailLogId}", reset = "resetForm", input = TransitionConstants.Application.JSP_SPC06L03)
	public String indexFromMail() {

		//必要なパラメータをチェック
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, detailMailListForm.mailLogId);
		detailMailListForm.fromMenu = DetailMailListForm.FromMenuKbn.MAIL_BOX;
		detailMailListForm.pageNum = String.valueOf(GourmetCareeConstants.DEFAULT_PAGE);


		return show(GourmetCareeConstants.DEFAULT_PAGE);
	}

	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPC06L03)
	public String showList() {
		return show(NumberUtils.toInt(detailMailListForm.pageNum, GourmetCareeConstants.DEFAULT_PAGE));
	}

	/**
	 * 初期表示遷移用
	 * @return
	 */
	private String show(int targetPage) {
		createList(targetPage);

		return TransitionConstants.Application.JSP_SPC06L03;
	}

	/**
	 * 一覧表示用のデータを作成します。
	 * @param targetPage
	 */
	private void createList(int targetPage) {


		// スカウト者情報を取得する
		ScoutmailSearchProperty prop = new ScoutmailSearchProperty();
		prop.maxRow = getInitMaxRow();
		prop.scountId = Integer.parseInt(detailMailListForm.mailLogId);

		List<ScoutMailTargetDto> dtoList = scoutMailLogic.selectScoutTargetList(prop);

		scoutMailTargetDto = Beans.createAndCopy(ScoutMailTargetDto.class, dtoList.get(0)).execute();

		// スカウトメールを取得する
		PagerProperty property = new PagerProperty();
		property.targetPage = targetPage;
		property.maxRow = getInitMaxRow();

		// 応募IDをキーに応募者のメール（送受信）を取得する
		List<MailForApplicationIdDto> tmpList = new ArrayList<MailForApplicationIdDto>();

		ApplicationMailRetDto retDto = applicationLogic.getApplicationIdEachMail(property, scoutMailTargetDto.id, MAIL_KBN.SCOUT_MAIL);

		for (MailSelectDto entity : retDto.retList) {

			MailForApplicationIdDto mMailForApplicationIdDto = new MailForApplicationIdDto();
			Beans.copy(entity, mMailForApplicationIdDto).execute();

			tmpList.add(mMailForApplicationIdDto);
		}

		dataList = tmpList;
		pageNavi = retDto.pageNavi;
	}

	/**
	 * ページの切り替え
	 * @return
	 */
	@Execute(validator = false, urlPattern = "changePage/{id}/{pageNum}", input =TransitionConstants.Application.JSP_SPC06L03)
	public String changePage() {
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, detailMailListForm.mailLogId);

		// 表示リストを取得
		createList(NumberUtils.toInt(detailMailListForm.pageNum, GourmetCareeConstants.DEFAULT_PAGE));

		return TransitionConstants.Application.JSP_SPC06L03;
	}


	/**
	 * メモ追加用Ajaxメソッド
	 */
	@Execute(validator = false, reset = "resetForMemo")
	public String addMemo() {
		scoutMailLogService.changeMemo(NumberUtils.toInt(detailMailListForm.mailLogId), detailMailListForm.memo);
		return null;
	}


	/**
	 * 最大件数切替の初期値をプロパティから取得します。<br />
	 * 取得できない場合は、システム全体の初期値を設定
	 * @return 最大表示件数の初期値
	 */
	private int getInitMaxRow() {
		return  NumberUtils.toInt(getCommonProperty("gc.application.initMaxRow"), GourmetCareeConstants.DEFAULT_MAX_ROW_INT);
	}


	/**
	 * 選考ステータスを変更するAjaxメソッド
	 * @return
	 */
	@Execute(validator = false, reset = "resetForSelection")
	public String changeSelectionFlg() {

		Integer selectionFlg;
		if (StringUtils.isBlank(detailMailListForm.selectionFlg)) {
			selectionFlg = null;
		} else {
			selectionFlg = NumberUtils.toInt(detailMailListForm.selectionFlg);
		}
		scoutMailLogService.changeSelectionFlg(NumberUtils.toInt(detailMailListForm.mailLogId), selectionFlg);
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see com.gourmetcaree.shop.pc.sys.action.MailListAction#getMailKbn()
	 */
	@Override
	public MAIL_KBN getMailKbn() {
		return MAIL_KBN.SCOUT_MAIL;
	}

	@Override
    public MenuInfo getMenuInfo() {
        if (detailMailListForm.fromMenu == DetailMailListForm.FromMenuKbn.SCOUT_MAIL) {
            return MenuInfo.scoutInstance();
        }
        return MenuInfo.mailInstance();
    }
}
