package com.gourmetcaree.admin.pc.report.action.report;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.report.dto.report.ListDetailDto;
import com.gourmetcaree.admin.pc.report.form.report.DetailForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.dto.ReportListDetailDto;
import com.gourmetcaree.admin.service.logic.ReportLogic;
import com.gourmetcaree.admin.service.property.ReportListDetailSqlProperty;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.db.common.entity.MVolume;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.VolumeService;
import com.gourmetcaree.db.common.service.WebListService;

/**
 * レポート管理で号数別の詳細を表示する
 * @author ando
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN})
public class DetailAction extends PcAdminAction {

	/** 詳細フォーム */
	@ActionForm
	@Resource
	protected DetailForm detailForm;

	/** 号数サービス */
	@Resource
	protected VolumeService volumeService;

	/** Webデータリストサービス */
	@Resource
	protected WebListService webListService;

	/** レポートのロジック */
	@Resource
	protected ReportLogic reportLogic;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", urlPattern = "index/{volumeId}", input = TransitionConstants.Report.JSP_APG01R01)
	@MethodAccess(accessCode = "REPORT_DETAIL_INDEX")
	public String index() {

		checkId(detailForm, detailForm.volumeId);
		return show();
	}

	/**
	 * 戻る
	 * @return
	 */
	@Execute(validator = false, removeActionForm = true)
	@MethodAccess(accessCode = "REPORT_DETAIL_BACK")
	public String back() {
		// 一覧画面の再表示用メソッドにリダイレクト
		return TransitionConstants.Report.REDIRECT_REPORT_LIST_AGAIN;
	}

	/**
	 * 初期表示遷移用
	 * @return 登録画面のパス
	 */
	private String show() {

		createList();

		// 登録画面へ遷移
		return TransitionConstants.Report.JSP_APG01R01;
	}

	/**
	 * 一覧表示用のデータを作成します。
	 */
	private void createList() {
		try {
			MVolume volumeEntity = volumeService.findById(NumberUtils.toInt(detailForm.volumeId));
			Beans.copy(volumeEntity, detailForm).execute();

			detailForm.totalCount = webListService.getWebListCountByVolume(NumberUtils.toInt(detailForm.volumeId));

			//検索条件
			ReportListDetailSqlProperty property = new ReportListDetailSqlProperty();
			property.volumeId = NumberUtils.toInt(detailForm.volumeId);

			List<ReportListDetailDto> retList = reportLogic.getReportDetailList(property);
			List<ListDetailDto> reportList = new ArrayList<ListDetailDto>();

			//表示用Dtoに詰め込みなおし、Listに保持
			for (ReportListDetailDto resultDto : retList) {

				ListDetailDto dto = new ListDetailDto();
				Beans.copy(resultDto, dto).execute();

				reportList.add(dto);
			}

			detailForm.reportList = reportList;

		} catch (SNoResultException e) {
			detailForm.setExistDataFlgNg();
			detailForm.reportList = new ArrayList<ListDetailDto>();
			throw new ActionMessagesException("errors.app.dataNotFound");
		} catch (WNoResultException e) {
			detailForm.setExistDataFlgNg();
			detailForm.reportList = new ArrayList<ListDetailDto>();
			throw new ActionMessagesException("errors.app.dataNotFound");
		}
	}

}