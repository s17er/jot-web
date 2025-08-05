package com.gourmetcaree.shop.pc.preApplication.action.preApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.ResponseUtil;

import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.exception.PageNotFoundException;
import com.gourmetcaree.common.property.PagerProperty;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TPreApplication;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.common.service.PreApplicationService;
import com.gourmetcaree.shop.logic.dto.ApplicationMailRetDto;
import com.gourmetcaree.shop.logic.dto.MailSelectDto;
import com.gourmetcaree.shop.logic.logic.ApplicationLogic.MAIL_KBN;
import com.gourmetcaree.shop.pc.application.dto.application.ApplicationListDto;
import com.gourmetcaree.shop.pc.application.dto.application.MailForApplicationIdDto;
import com.gourmetcaree.shop.pc.application.form.application.DetailMailListForm;
import com.gourmetcaree.shop.pc.sys.action.MailListAction;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;

import jp.co.whizz_tech.common.sastruts.converter.ZenkakuKanaConverter;
import net.arnx.jsonic.JSON;


/**
 * プレ応募者毎のメールリストを表示するアクションクラスです。
 * メールのグループは、応募IDに紐づくメールが対象とし、
 * メールの送信日時が一年以内のものとする。
 * ※ 応募日時が一年以内ではないので注意
 */
@ManageLoginRequired
public class DetailMailListAction extends MailListAction {

	/** 応募者詳細フォーム */
	@ActionForm
	@Resource
	protected DetailMailListForm detailMailListForm;

	/** プレ応募サービス */
	@Resource
	protected PreApplicationService preApplicationService;


	/** ページ上部の応募者情報 */
	public ApplicationListDto applicationDto;

	/** メールを保持するリスト */
	public List<MailForApplicationIdDto> dataList;

	/** ページナビゲータ */
	public PageNavigateHelper pageNavi;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, urlPattern = "index/{id}", reset = "resetForm", input = TransitionConstants.Application.JSP_SPC01L02)
	public String index() {

		//必要なパラメータをチェック
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, detailMailListForm.id);

		detailMailListForm.pageNum = String.valueOf(GourmetCareeConstants.DEFAULT_PAGE);
		return show(GourmetCareeConstants.DEFAULT_PAGE);
	}



	/**
	 * メール一覧(送信者)のリンクから表示する画面。
	 * 通常の表記と同じだが、ヘッダのタブ表示を行わない画面とする。
	 * また、ページ下部の「戻る」ボタンもリンク元のものに合わせて切り替える。
	 * @return
	 */
	@Execute(validator = false, urlPattern = "list/{id}", reset = "resetForm", input = TransitionConstants.Application.JSP_SPC01L02)
	public String list() {
		final String jsp = index();
		detailMailListForm.hideHeaderFlg = true;
		return jsp;
	}

	/**
	 * 初期表示遷移用
	 * @return
	 */
	private String show(int targetPage) {

		createList(targetPage);

		return TransitionConstants.Application.JSP_SPP01L02;
	}

	/**
	 * 一覧表示用のデータを作成します。
	 * @param targetPage
	 */
	private void createList(int targetPage) {

		PagerProperty property = new PagerProperty();
		property.targetPage = targetPage;
		property.maxRow = getInitMaxRow();

		List<MailForApplicationIdDto> tmpList = new ArrayList<MailForApplicationIdDto>();

		// 応募IDをキーに応募者のデータを取得する
		TPreApplication tApplicationEntity;



		try {
			tApplicationEntity = preApplicationService.findById(Integer.parseInt(detailMailListForm.id));
		} catch (NumberFormatException e) {
			detailMailListForm.setExistDataFlgNg();
			throw new FraudulentProcessException();
		} catch (SNoResultException e) {
			detailMailListForm.setExistDataFlgNg();
			throw new PageNotFoundException();
		}
		applicationDto =
				Beans.createAndCopy(ApplicationListDto.class, tApplicationEntity).excludesNull().excludesWhitespace().execute();

		Map<Integer, String> selectionMap = MTypeConstants.SelectionFlg.SELECTION_COLOR_MAP;
		if (applicationDto.selectionFlg == -1) {
			applicationDto.selectionFlgColor = MTypeConstants.SelectionFlg.SELECTION_FLG_DEFAULT_COLOR;
		} else {
			applicationDto.selectionFlgColor = selectionMap.get(applicationDto.selectionFlg);
		}

		applicationDto.name = String.valueOf(applicationDto.id);

		//応募にたいして未読メールが存在するかどうかを取得。
		applicationDto.unopenedMailFlg = preApplicationLogic.isUnopenedApplicantMailExist(Integer.parseInt(detailMailListForm.id));

		// 応募IDをキーに応募者のメール（送受信）を取得する
		ApplicationMailRetDto retDto = preApplicationLogic.getApplicationIdEachMail(property, Integer.parseInt(detailMailListForm.id), MAIL_KBN.APPLICATION_MAIL);

		if (CollectionUtils.isEmpty(retDto.retList)) {
			detailMailListForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.dataNotFound");
		}

		for (MailSelectDto entity : retDto.retList) {
			MailForApplicationIdDto dto = new MailForApplicationIdDto();
			Beans.copy(entity, dto).execute();

			tmpList.add(dto);
		}

		dataList = tmpList;
		pageNavi = retDto.pageNavi;

		detailMailListForm.setExistDataFlgOk();
	}


	/**
	 * 応募者のメモを編集する
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPP01L02)
	public String editMemo() {

		//フォームにはメモ以外のレコード情報が含まれるため、必要かラムのみセット
		TPreApplication entity = new TPreApplication();
		Beans.copy(detailMailListForm, entity).includes("id", "version", "memo").converter(new ZenkakuKanaConverter()).execute();

		preApplicationService.update(entity);

		return String.format("/preApplication/detailMailList/changePage/%s/%s%s", detailMailListForm.id, StringUtils.defaultIfEmpty(detailMailListForm.pageNum, "1"), GourmetCareeConstants.REDIRECT_STR);
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
	 * ステータスの変更
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Application.JSP_SPP01L02)
	public String ajaxSelectionFlg() {

		TPreApplication entity = new TPreApplication();
		Beans.copy(detailMailListForm, entity).execute();

		//不正な値を防ぐため値をチェック。
		Map<String, String> selectionMap = MTypeConstants.SelectionFlg.SELECTION_COLOR_STRING_MAP;
		if (StringUtils.isNotBlank(selectionMap.get(detailMailListForm.selectionKbn))) {
			entity.selectionFlg = Integer.parseInt(detailMailListForm.selectionKbn);
		} else {
			entity.selectionFlg = STATUS_DEFAULT_VALUE;
		}

		preApplicationService.updateIncludesVersion(entity);

		String json = JSON.encode(MTypeConstants.SelectionFlg.SELECTION_COLOR_STRING_MAP);
		ResponseUtil.write(json, "application/json", "UTF-8");

		return null;
	}

	/**
	 * 再表示<br>
	 * セッションをリセットせずに検索結果を一覧に表示します。<br>
	 * ※URL欄に再表示であることを明示しないように、メソッド名はあえて抽象的にしています。
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPP01L02)
	public String showList() {


		// 表示フラグを非表示
		detailMailListForm.setExistDataFlgNg();

		createList(NumberUtils.toInt(detailMailListForm.pageNum));

		return TransitionConstants.Application.JSP_SPP01L02;
	}

	/**
	 * ページ遷移
	 * @return
	 */
	@Execute(validator = false, urlPattern = "changePage/{id}/{pageNum}", input = TransitionConstants.Application.JSP_SPP01L02)
	public String changePage() {

		createList(NumberUtils.toInt(detailMailListForm.pageNum));

		return TransitionConstants.Application.JSP_SPP01L02;
	}

	public MAIL_KBN getMailKbn() {
		return MAIL_KBN.PRE_APPLICATION_MAIL;
	}
}
