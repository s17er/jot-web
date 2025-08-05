package com.gourmetcaree.shop.pc.application.action.arbeit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.NoResultException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.ResponseUtil;

import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.property.PagerProperty;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TArbeitApplication;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.common.service.ArbeitApplicationService;
import com.gourmetcaree.shop.logic.dto.ApplicationMailRetDto;
import com.gourmetcaree.shop.logic.dto.MailSelectDto;
import com.gourmetcaree.shop.logic.logic.ApplicationLogic.MAIL_KBN;
import com.gourmetcaree.shop.logic.logic.ArbeitApplicationLogic;
import com.gourmetcaree.shop.pc.application.dto.application.MailForApplicationIdDto;
import com.gourmetcaree.shop.pc.application.dto.arbeit.ArbeitApplicationListDto;
import com.gourmetcaree.shop.pc.application.form.arbeit.DetailMailListForm;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;

import jp.co.whizz_tech.common.sastruts.converter.ZenkakuKanaConverter;
import net.arnx.jsonic.JSON;


/**
 * アルバイト応募者毎のメールリストを表示するアクションクラスです。
 * @author Yamane
 * @version 1.0
 */
@ManageLoginRequired
public class DetailMailListAction extends ArbeitBaseAction {

	/** アルバイト応募者詳細フォーム */
	@ActionForm
	@Resource
	protected DetailMailListForm detailMailListForm;

	/** アルバイト応募ロジック */
	@Resource
	protected ArbeitApplicationLogic arbeitApplicationLogic;

	/** アルバイト応募サービス */
	@Resource
	protected ArbeitApplicationService arbeitApplicationService;

	/** ページ上部の応募者情報 */
	public ArbeitApplicationListDto arbeitApplicationListDto;

	/** メールを保持するリスト */
	public List<MailForApplicationIdDto> dataList;

	/** ページナビゲータ */
	public PageNavigateHelper pageNavi;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, urlPattern = "index/{id}", reset = "resetForm", input = TransitionConstants.Application.JSP_SPC05L03)
	public String index() {

		//必要なパラメータをチェック
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, detailMailListForm.id);

		return show();
	}

	/**
	 * メール一覧(送信者)のリンクから表示する画面。
	 * 通常の表記と同じだが、ヘッダのタブ表示を行わない画面とする。
	 * また、ページ下部の「戻る」ボタンもリンク元のものに合わせて切り替える。
	 * @return
	 */
	@Execute(validator = false, urlPattern = "list/{id}", reset = "resetForm", input = TransitionConstants.Application.JSP_SPC05L03)
	public String list() {
		final String jsp = index();
		detailMailListForm.hideHeaderFlg = true;
		return jsp;
	}


	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPC05L03)
	public String showList() {
		createList(NumberUtils.toInt(detailMailListForm.pageNum, GourmetCareeConstants.DEFAULT_PAGE));
		return TransitionConstants.Application.JSP_SPC05L03;
	}

	/**
	 * 初期表示遷移用
	 * @return
	 */
	private String show() {

		createList(GourmetCareeConstants.DEFAULT_PAGE);

		return TransitionConstants.Application.JSP_SPC05L03;
	}

	/**
	 * 一覧表示用のデータを作成します。
	 * @param targetPage
	 */
	private void createList(int targetPage) {

		PagerProperty property = new PagerProperty();
		property.targetPage = targetPage;
		property.maxRow = getInitMaxRow();

		// ページの設定
		detailMailListForm.pageNum = String.valueOf(targetPage);

		List<MailForApplicationIdDto> tmpList = new ArrayList<MailForApplicationIdDto>();

		try {

			// 応募IDをキーに応募者のデータを取得する
			TArbeitApplication tApplicationEntity = arbeitApplicationService.findById(Integer.parseInt(detailMailListForm.id));
			arbeitApplicationListDto =
					Beans.createAndCopy(ArbeitApplicationListDto.class, tApplicationEntity).excludesNull().excludesWhitespace().execute();

			Map<Integer, String> selectionMap = MTypeConstants.SelectionFlg.SELECTION_COLOR_MAP;
			if (arbeitApplicationListDto.selectionFlg == -1) {
				arbeitApplicationListDto.selectionFlgColor = MTypeConstants.SelectionFlg.SELECTION_FLG_DEFAULT_COLOR;
			} else {
				arbeitApplicationListDto.selectionFlgColor = selectionMap.get(arbeitApplicationListDto.selectionFlg);
			}

			//応募にたいして未読メールが存在するかどうかを取得。
			boolean unopendMailFlg = applicationLogic.isUnopenedApplicantMailExist(Integer.parseInt(detailMailListForm.id), MTypeConstants.MailKbn.ARBEIT_APPLICATION);
			if (!unopendMailFlg) {
				arbeitApplicationListDto.unopenedMailFlg = unopendMailFlg;
			}


			// 応募IDをキーに応募者のメール（送受信）を取得する
			ApplicationMailRetDto retDto = applicationLogic.getApplicationIdEachMail(property, Integer.parseInt(detailMailListForm.id), MAIL_KBN.ARBEIT_APPLICATION);

			for (MailSelectDto entity : retDto.retList) {

				MailForApplicationIdDto dto = new MailForApplicationIdDto();
				Beans.copy(entity, dto).execute();

				tmpList.add(dto);
			}

			detailMailListForm.setExistDataFlgOk();
			dataList = tmpList;
			pageNavi = retDto.pageNavi;

		} catch (NoResultException e) {

			dataList = tmpList;
			pageNavi = new PageNavigateHelper(GourmetCareeConstants.DEFAULT_MAX_ROW_INT);

			detailMailListForm.setExistDataFlgNg();

			throw new ActionMessagesException("errors.app.dataNotFound");
		}
	}


	/**
	 * 応募者のメモを編集する
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPC05L03)
	public String editMemo() {

		//フォームにはメモ以外のレコード情報が含まれるため、必要かラムのみセット
		TArbeitApplication entity = new TArbeitApplication();
		Beans.copy(detailMailListForm, entity).includes("id", "version", "memo").converter(new ZenkakuKanaConverter()).execute();

		entity.memoUpdateDatetime = new Date();

		arbeitApplicationService.update(entity);

		return String.format("/arbeit/detailMailList/changePage/%s/%s%s", detailMailListForm.id, StringUtils.defaultIfEmpty(detailMailListForm.pageNum, "1"), GourmetCareeConstants.REDIRECT_STR );


//				"/arbeit/detailMailList/index/" + detailMailListForm.id + GourmetCareeConstants.REDIRECT_STR;
	}


	/**
	 * 最大件数切替の初期値をプロパティから取得します。<br />
	 * 取得できない場合は、システム全体の初期値を設定
	 * @return 最大表示件数の初期値
	 */
	private int getInitMaxRow() {
		return  NumberUtils.toInt(getCommonProperty("gc.application.initMaxRow"), GourmetCareeConstants.DEFAULT_MAX_ROW_INT);
	}


	@Execute(validator = false, urlPattern = "changePage/{id}/{pageNum}", input = TransitionConstants.Application.JSP_SPC05L03)
	public String changePage() {
		//必要なパラメータをチェック
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, detailMailListForm.id);
		createList(NumberUtils.toInt(detailMailListForm.pageNum, GourmetCareeConstants.DEFAULT_PAGE));
		return TransitionConstants.Application.JSP_SPC05L03;
	}

	/**
	 * ステータスの変更
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Application.JSP_SPC05L03)
	public String ajaxSelectionFlg() {

		TArbeitApplication entity = new TArbeitApplication();
		Beans.copy(detailMailListForm, entity).execute();

		changeSelectionFlg(entity, detailMailListForm.selectionKbn);

		arbeitApplicationService.updateIncludesVersion(entity);

		String json = JSON.encode(MTypeConstants.SelectionFlg.SELECTION_COLOR_STRING_MAP);
		ResponseUtil.write(json, "application/json", "UTF-8");

		return null;
	}

	@Override
	public MAIL_KBN getMailKbn() {
		return MAIL_KBN.ARBEIT_APPLICATION;
	}
}
