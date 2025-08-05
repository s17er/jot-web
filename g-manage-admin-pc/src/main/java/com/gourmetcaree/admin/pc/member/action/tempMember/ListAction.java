package com.gourmetcaree.admin.pc.member.action.tempMember;

import java.text.ParseException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.google.common.collect.Lists;
import com.gourmetcaree.admin.pc.member.form.tempMember.ListForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.dto.tempMember.DisplayListDto;
import com.gourmetcaree.admin.service.logic.tempMember.SearchLogic;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;


/**
 * 仮会員一覧アクション
 * @author nakamori
 *
 */
@ManageLoginRequired(authLevel = {ManageAuthLevel.ADMIN, ManageAuthLevel.STAFF, ManageAuthLevel.SALES})
public class ListAction extends PcAdminAction {


	@Resource(name = "tempMember_listForm")
	@ActionForm
	private ListForm form;


	@Resource
	private SearchLogic searchLogic;

	public PageNavigateHelper pageNavi;

	public List<DisplayListDto> memberInfoDtoList;


	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm")
	@MethodAccess(accessCode="TEMPMEMBER_LIST")
	public String index() {
		return TransitionConstants.Member.JSP_APH03L01;
	}



	/**
	 * 検索
	 * @return
	 */
	@Execute(validator = true, validate = "validate", input =TransitionConstants.Member.JSP_APH03L01)
	@MethodAccess(accessCode="TEMPMEMBER_LIST")
	public String search() {
		//ページナビゲータを初期化
		form.maxRow = String.valueOf(GourmetCareeConstants.DEFAULT_MAX_ROW_INT);

		doSearch(GourmetCareeConstants.DEFAULT_DISP_NO);

		return TransitionConstants.Member.JSP_APH03L01;
	}

	/**
	 * 別画面から再び検索に戻った場合に、遷移前の状態(=検索結果画面)を表示するために使用する
	 * 再検索メソッド
	 * @return GCWコード検索JSPのパス
	 */
	@Execute(validator=false, input = TransitionConstants.Member.JSP_APH01L01)
	@MethodAccess(accessCode="TEMPMEMBER_LIST")
	public String searchAgain() {

		doSearch(NumberUtils.toInt(form.pageNum, GourmetCareeConstants.DEFAULT_PAGE));

		return TransitionConstants.Member.JSP_APH03L01;
	}

	/**
	 * 表示件数の変更
	 */
	@Execute(validator = false, urlPattern = "changePage/{pageNum}", input =TransitionConstants.Member.JSP_APH03L01)
	@MethodAccess(accessCode="TEMPMEMBER_LIST")
	public String changePage() {
		doSearch(NumberUtils.toInt(form.pageNum, GourmetCareeConstants.DEFAULT_PAGE));

		return TransitionConstants.Member.JSP_APH03L01;
	}

	/**
	 * 表示件数の変更
	 */
	@Execute(validator = true, validate = "validate", input =TransitionConstants.Member.JSP_APH03L01)
	@MethodAccess(accessCode="TEMPMEMBER_LIST")
	public String changeMaxRow() {
		doSearch(GourmetCareeConstants.DEFAULT_PAGE);

		return TransitionConstants.Member.JSP_APH03L01;
	}

	/**
	 * 検索メインロジック
	 * @param targetPage
	 */
	private void doSearch(int targetPage) {

		pageNavi = new PageNavigateHelper("", NumberUtils.toInt(form.maxRow, GourmetCareeConstants.DEFAULT_MAX_ROW_INT));

		try {
			memberInfoDtoList = searchLogic.search(form, pageNavi, targetPage);
			if (CollectionUtils.isEmpty(memberInfoDtoList)) {
				throw new ActionMessagesException("errors.app.dataNotFound");
			}
		} catch (ParseException e) {
			memberInfoDtoList = Lists.newArrayList();
			throw new ActionMessagesException("errors.app.invalidSearchParameter");
		}
	}
}
