package com.gourmetcaree.admin.pc.application.action.arbeitApplication;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.application.form.arbeitApplication.ListForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.logic.ArbeitApplicationLogic;
import com.gourmetcaree.admin.service.logic.ArbeitApplicationLogic.SearchListDto;
import com.gourmetcaree.admin.service.logic.ArbeitApplicationLogic.SearchProperty;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;


/**
 * グルメdeバイト応募管理リストアクション
 * @author Takehiro Nakamori
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN, ManageAuthLevel.STAFF, ManageAuthLevel.OTHER, ManageAuthLevel.SALES})
public class ListAction extends ArbeitApplicationBaseAction {

	@Resource
	@ActionForm
	private ListForm listForm;

	@Resource
	private ArbeitApplicationLogic arbeitApplicationLogic;

	/** ページナビ */
	private PageNavigateHelper pageNavi;

	/** 検索リスト */
	private List<SearchListDto> searchList;


	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Application.JSP_API03L01)
	public String index() {
		return show();
	}


	/**
	 * 初期画面
	 * @return JSP
	 */
	private String show() {
		return TransitionConstants.Application.JSP_API03L01;
	}



	/**
	 * 検索を行います
	 * @return JSP
	 */
	@Execute(validator = true, validate = "validate", input = TransitionConstants.Application.JSP_API03L01)
	public String search() {

		doSearch(GourmetCareeConstants.DEFAULT_PAGE);
		return TransitionConstants.Application.JSP_API03L01;
	}

	/**
	 * 最大表示件数を変更します。
	 * @return JSP
	 */
	@Execute(validator = true, validate = "validate", input = TransitionConstants.Application.JSP_API03L01)
	public String changeMaxRow() {
		doSearch(GourmetCareeConstants.DEFAULT_PAGE);
		return TransitionConstants.Application.JSP_API03L01;
	}

	/**
	 * ページの切り替えを行います。
	 * @return JSP
	 */
	@Execute(validator = true, validate = "validate", urlPattern = "changePage/{pageNum}", input = TransitionConstants.Application.JSP_API03L01)
	public String changePage() {
		doSearch(NumberUtils.toInt(listForm.pageNum, GourmetCareeConstants.DEFAULT_PAGE));
		return TransitionConstants.Application.JSP_API03L01;
	}

	@Execute(validator = true, validate = "validate", input = TransitionConstants.Application.JSP_API03L01)
	public String searchAgain() {
		doSearch(NumberUtils.toInt(listForm.pageNum, GourmetCareeConstants.DEFAULT_PAGE));
		return TransitionConstants.Application.JSP_API03L01;
	}


	/**
	 * CSVを出力します。
	 * @return null
	 */
	@Execute(validator = true, validate = "validate", input = TransitionConstants.Application.JSP_API03L01)
	public String output() {

		try {
			// CSVの出力を行う。
			// LIMIT、OFFSETは設定しないため、引数はなんでもいい
			arbeitApplicationLogic.outputCsv(createSearchProperty(0));

		} catch (WNoResultException e) {
			throw new ActionMessagesException("errors.app.dataNotFound");
		} catch (UnsupportedEncodingException e) {
			errorLog.error("バイト応募CSV作成時に、UnsupportedEncodingExceptionが発生しました。", e);
			throw new ActionMessagesException("errors.app.csvOutPutFailed");
		} catch (IOException e) {
			errorLog.error("バイト応募CSV作成時に、IOExceptionが発生しました。", e);
			throw new ActionMessagesException("errors.app.csvOutPutFailed");
		}
		return null;
	}

	/**
	 * 検索を行います。
	 * @param targetPage 対象ページ
	 */
	private void doSearch(int targetPage) {
		SearchProperty property = createSearchProperty(targetPage);
		try {
			arbeitApplicationLogic.doSearch(property);
			searchList = property.searchList;
			pageNavi = property.pageNavi;
		} catch (WNoResultException e) {
			throw new ActionMessagesException("errors.app.dataNotFound");
		}
	}


	/**
	 * 検索プロパティを作成します。
	 * @param targetPage 対象ページ
	 * @return 検索プロパティ
	 */
	private SearchProperty createSearchProperty(int targetPage) {
		SearchProperty property = Beans.createAndCopy(SearchProperty.class, listForm)
										.excludesNull()
										.execute();

		property.maxRow = NumberUtils.toInt(listForm.maxRow, GourmetCareeConstants.DEFAULT_MAX_ROW_INT);
		property.targetPage = targetPage;

		try {
			property.applicationStartDatetime = listForm.createApplicationStartDatetimeAsTimestamp();
			property.applicationEndDatetime = listForm.createApplicationEndDatetimeAsTimestamp();
		} catch (ParseException e) {
			throw new ActionMessagesException("errors.app.dateTimeFailed");
		}
		return property;
	}











	/**
	 * ページナビの取得
	 * @return ページナビ
	 */
	public PageNavigateHelper getPageNavi() {
		if (pageNavi == null) {
			return new PageNavigateHelper(0);
		}
		return pageNavi;
	}


	/**
	 * 検索結果リストの取得
	 * @return 検索結果リスト
	 */
	public List<SearchListDto> getSearchList() {
		if (searchList == null) {
			return new ArrayList<SearchListDto>(0);
		}
		return searchList;
	}


	/**
	 * データが存在するかどうか
	 * @return 存在すればtrue
	 */
	public boolean isExistDataFlg() {
		return CollectionUtils.isNotEmpty(searchList);
	}
}
