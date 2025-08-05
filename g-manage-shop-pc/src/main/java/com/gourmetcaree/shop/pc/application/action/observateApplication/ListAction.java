package com.gourmetcaree.shop.pc.application.action.observateApplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import jp.co.whizz_tech.common.sastruts.converter.ZenkakuKanaConverter;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.property.PagerProperty;
import com.gourmetcaree.db.common.entity.TObservateApplication;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.common.service.ObservateApplicationService;
import com.gourmetcaree.shop.logic.dto.ObservateApplicationRetDto;
import com.gourmetcaree.shop.logic.logic.ApplicationLogic.MAIL_KBN;
import com.gourmetcaree.shop.logic.logic.ObservateApplicationLogic;
import com.gourmetcaree.shop.pc.application.dto.observateApplication.ObservateApplicationListDto;
import com.gourmetcaree.shop.pc.application.form.observateApplication.ListForm;
import com.gourmetcaree.shop.pc.sys.action.MailListAction;
import com.gourmetcaree.shop.pc.sys.constants.TransitionConstants;

/**
 * 店舗見学・質問者一覧を表示するアクションクラスです。
 * @author Takehiro Nakamori
 * @version 1.0
 */
@ManageLoginRequired
public class ListAction extends MailListAction {

	private static final Logger log = Logger.getLogger(ListAction.class);

	/** ログオブジェクト（なりすまし） */
	private static final Logger sysMasqueradeLog = Logger.getLogger("sysMasqueradeLog");

	/** アクションフォーム */
	@ActionForm
	@Resource
	private ListForm listForm;

	/** 店舗見学・質問者サービス */
	@Resource
	private ObservateApplicationService observateApplicationService;

	/** 店舗見学・質問者ロジック */
	@Resource
	private ObservateApplicationLogic observateApplicationLogic;

	/** 店舗見学・質問者Dto */
	public List<ObservateApplicationListDto> dataList;

	/** ページナビゲータ */
	public PageNavigateHelper pageNavi;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Application.JSP_SPC04L02)
	public String index() {

		return show();
	}

	/**
	 * 初期表示遷移用
	 * @return
	 */
	private String show() {

		createList(GourmetCareeConstants.DEFAULT_PAGE);

		return TransitionConstants.Application.JSP_SPC04L02;
	}

	/**
	 * 送信箱の初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Application.JSP_SPC04L02)
	public String sendBox() {

		createList(GourmetCareeConstants.DEFAULT_PAGE);

		return TransitionConstants.Application.JSP_SPC04L02;
	}


	/**
	 * CSV出力
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPC04L02)
	public String outputCsv() {

		try {
			observateApplicationLogic.outputCsv(response, "observateApplication");
		} catch (WNoResultException e) {
			throw new ActionMessagesException("errors.app.mailDataNotFound");
		} catch (IOException e) {
			throw new ActionMessagesException("errors.app.failedOutputCsv");
		}
		return null;
	}

	/**
	 * 一覧表示用のデータを作成します。
	 * @param targetPage
	 */
	private void createList(int targetPage) {

		PagerProperty property = new PagerProperty();
		property.targetPage = targetPage;
		property.maxRow = getInitMaxRow();

		List<ObservateApplicationListDto> tmpList = new ArrayList<ObservateApplicationListDto>();

		try {

			ObservateApplicationRetDto retDto = observateApplicationLogic.getObservateApplication(property);

			for (TObservateApplication entity : retDto.retList) {

				ObservateApplicationListDto dto = new ObservateApplicationListDto();
				Beans.copy(entity, dto).execute();

				dto.unopenedMailFlg = observateApplicationLogic.isUnopenedApplicantMailExist(entity.id);
				log.info(dto.unopenedMailFlg);
				if (userDto.isMasqueradeFlg()) {
					sysMasqueradeLog.debug(String.format("求人原稿一覧を取得しました。営業ID：%s, 顧客ID：%s, 未読メールフラグ：", userDto.masqueradeUserId, userDto.customerId, dto.unopenedMailFlg));
				}

				tmpList.add(dto);
			}

			dataList = tmpList;
			pageNavi = retDto.pageNavi;

			listForm.setExistDataFlgOk();

		} catch (WNoResultException e) {
			dataList = tmpList;
			pageNavi = new PageNavigateHelper(GourmetCareeConstants.DEFAULT_MAX_ROW_INT);

			listForm.setExistDataFlgNg();

			throw new ActionMessagesException("errors.app.mailDataNotFound");
		}
	}

	/**
	 * 応募者のメモを編集する
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPC04L02)
	public String editMemo() {

		//フォームにはメモ以外のレコード情報が含まれるため、必要かラムのみセット
		TObservateApplication entity = new TObservateApplication();
		Beans.copy(listForm, entity).includes("id", "version", "memo").converter(new ZenkakuKanaConverter()).execute();

		entity.memoUpdateDatetime = new Date();

		observateApplicationService.update(entity);

		if (StringUtils.isBlank(listForm.pageNum)) {
			listForm.pageNum = "1";
		}

		return "/observateApplication/list/changePage/" + listForm.pageNum + TransitionConstants.REDIRECT_STR;
	}

	/**
	 * ページ遷移
	 * 受信メール一覧用のページ遷移。
	 * @return
	 */
	@Execute(urlPattern = "changePage/{pageNum}", input = TransitionConstants.Application.JSP_SPC04L02)
	public String changePage() {

		createList(NumberUtils.toInt(listForm.pageNum));
		return TransitionConstants.Application.JSP_SPC04L02;
	}

	/**
	 * 再表示<br>
	 * セッションをリセットせずに検索結果を一覧に表示します。<br>
	 * ※URL欄に再表示であることを明示しないように、メソッド名はあえて抽象的にしています。
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Application.JSP_SPC04L02)
	public String showList() {

		createList(NumberUtils.toInt(listForm.pageNum));

		return TransitionConstants.Application.JSP_SPC04L02;
	}


	/**
	 * 最大件数切替の初期値をプロパティから取得します。<br />
	 * 取得できない場合は、システム全体の初期値を設定
	 * @return 最大表示件数の初期値
	 */
	private int getInitMaxRow() {
		return  NumberUtils.toInt(getCommonProperty("gc.observateApplication.mail.initMaxRow"), GourmetCareeConstants.DEFAULT_MAX_ROW_INT);
	}

	@Override
	public MAIL_KBN getMailKbn() {
		return MAIL_KBN.OBSERVATE_APPLICATION;
	}
}
