package com.gourmetcaree.admin.pc.report.action.report;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.report.dto.report.ListDto;
import com.gourmetcaree.admin.pc.report.form.report.ListForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.dto.ReportListDto;
import com.gourmetcaree.admin.service.logic.ReportLogic;
import com.gourmetcaree.admin.service.property.ReportListSqlProperty;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * レポートの一覧を表示するActionです。
 * @author Takahiro Ando
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN})
public class ListAction extends PcAdminAction {

	/** 一覧フォーム */
	@ActionForm
	@Resource
	protected ListForm listForm;

	/** レポート機能のロジック */
	@Resource
	protected ReportLogic reportLogic;

	/** レポートのリスト */
	public List<ListDto> reportList;

	/**
	 * 最大件数切替の初期値をプロパティから取得します。<br />
	 * 取得できない場合は、システム全体の初期値を設定
	 * @return 最大表示件数の初期値
	 */
	private String getInitMaxRow() {
		return  StringUtils.defaultString(getCommonProperty("gc.report.initMaxRow"), GourmetCareeConstants.DEFAULT_MAX_ROW);
	}

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Report.JSP_APG01L01)
	@MethodAccess(accessCode = "REPORT_LIST_INDEX")
	public String index() {

		// 初期値をセット
		listForm.setExistDataFlgNg();
		listForm.maxRow = getInitMaxRow();

		return show();
	}

	/**
	 * 再表示<br>
	 * セッションをリセットせずに検索結果を一覧に表示します。<br>
	 * ※URL欄に再表示であることを明示しないように、メソッド名はあえて抽象的にしています。
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.Report.JSP_APG01L01)
	@MethodAccess(accessCode = "REPORT_LIST_SHOWLIST")
	public String showList() {

		// 表示フラグを非表示
		listForm.setExistDataFlgNg();

		return show();
	}

	/**
	 * 初期表示遷移用
	 * @return 登録画面のパス
	 */
	private String show() {

		createList();

		// 一覧画面へ遷移
		return TransitionConstants.Report.JSP_APG01L01;
	}

	/**
	 * 一覧表示用のデータを作成します。
	 */
	private void createList() {

		//検索条件
		ReportListSqlProperty property = new ReportListSqlProperty();
		property.areaCd = NumberUtils.toInt(listForm.areaCd);
		property.maxRow = listForm.maxRow;

		try {
			List<ReportListDto> retList = reportLogic.getReportList(property);

			List<ListDto> reportList = new ArrayList<ListDto>();

			//表示用Dtoに詰め込みなおし、Listに保持
			for (ReportListDto resultDto : retList) {

				ListDto dto = new ListDto();
				Beans.copy(resultDto, dto).execute();
				dto.detailPath = GourmetCareeUtil.makePath("/report/detail/", "index", Integer.toString(resultDto.volumeId));

				reportList.add(dto);
			}

			this.reportList = reportList;

		} catch (WNoResultException e) {
			listForm.setExistDataFlgNg();
			this.reportList = new ArrayList<ListDto>();

			throw new ActionMessagesException("errors.app.dataNotFound");
		}
	}


	/**
	 * 最大表示件数、エリア変更時の処理。
	 * 画面から指定された最大表示件数を数値化して検索を実行
	 * 不正なパラメータの場合は20をデフォル表示件数として表示する。
	 * @return 営業担当者検索JSPのパス
	 */
	@Execute(validator = false, input=TransitionConstants.Report.JSP_APG01L01)
	@MethodAccess(accessCode = "REPORT_LIST_CHANGEMAXROW")
	public String changeMaxRow() {

		// ブランク(全件)以外の場合は切り替えた最大表示件数をセット
		if (StringUtils.isNotEmpty(listForm.maxRow)) {
			listForm.maxRow = Integer.toString(NumberUtils.toInt(
								listForm.maxRow,
								(Integer.parseInt(getInitMaxRow()))));
		}

		createList();

		// 一覧画面へ遷移
		return TransitionConstants.Report.JSP_APG01L01;
	}
}