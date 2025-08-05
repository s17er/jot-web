package com.gourmetcaree.admin.pc.advancedRegistration.action.advancedRegistrationMember;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.advancedRegistration.form.advancedRegistrationMember.ListForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.dto.AdvancedRegistrationSearchResultDto;
import com.gourmetcaree.admin.service.logic.AdvancedMemberCsvLogic;
import com.gourmetcaree.admin.service.logic.AdvancedRegistrationLogic.SearchProperty;
import com.gourmetcaree.admin.service.property.AdvancedMemberCsvProperty;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;

/**
 * 事前登録会員検索アクション
 * @author Takehiro Nakamori
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN, ManageAuthLevel.STAFF, ManageAuthLevel.SALES} )
public class ListAction extends AdvancedRegisterdMemberBaseAction {

	/** ログ */
	private static final Logger log = Logger.getLogger(ListAction.class);

	private static final String REDIRECT_PATH = "/advancedRegistrationMember/list/comp?redirect=true";

	/** CSVファイル名 */
	private static final String CSV_FILE_NAME = "advanced";

	/** アクションフォーム */
	@Resource
	@ActionForm
	private ListForm listForm;

	@Resource
	private AdvancedMemberCsvLogic advancedMemberCsvLogic;

	/** ページナビ */
	private PageNavigateHelper pageNavi;

	/** 会員情報リスト */
	private List<AdvancedRegistrationSearchResultDto> list;




	/**
	 * 初期表示
	 * @return リストJSP
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.AdvancedRegistration.JSP_APH01L01)
	public String index() {
		return show();
	}

	/**
	 * 最大表示件数の切り替え
	 * @return リストJSP
	 */
	@Execute(validator = true, validate = "validate", input = TransitionConstants.AdvancedRegistration.JSP_APH01L01)
	public String changeMaxRow() {
		doSearch(GourmetCareeConstants.DEFAULT_PAGE);
		return TransitionConstants.AdvancedRegistration.JSP_APH01L01;
	}


	/**
	 * ページ切り替え
	 * @return リストJSP
	 */
	@Execute(validator = true, validate = "validate", urlPattern = "changePage/{pageNum}", input = TransitionConstants.AdvancedRegistration.JSP_APH01L01)
	public String changePage() {
		doSearch(NumberUtils.toInt(listForm.pageNum, GourmetCareeConstants.DEFAULT_PAGE));
		return TransitionConstants.AdvancedRegistration.JSP_APH01L01;
	}

	/**
	 * 印刷ボタン用メソッド
	 * @return 印刷アクションへのリダイレクト
	 */
	@Execute(validator = true, validate = "validate", input = TransitionConstants.AdvancedRegistration.JSP_APH01L01)
	public String printOut() {
		return TransitionConstants.AdvancedRegistration.REDIRECT_ADVANCED_MEMBER_PRINT_OUT;
	}

	/**
	 * 初期表示
	 */
	private String show() {
		return TransitionConstants.AdvancedRegistration.JSP_APH01L01;
	}


	/**
	 * 印刷ボタン用メソッド
	 * @return 印刷アクションへのリダイレクト
	 */
	@Execute(validator = true, validate = "validate", input = TransitionConstants.AdvancedRegistration.JSP_APH01L01)
	public String searchAgain() {
		doSearch(createTargetPage());
		return TransitionConstants.AdvancedRegistration.JSP_APH01L01;
	}


	/**
	 * 検索メソッド
	 * @return 検索結果JSP
	 */
	@Execute(validator = true, validate = "validate", reset="resetCheckBox", input = TransitionConstants.AdvancedRegistration.JSP_APH01L01)
	public String search() {
		doSearch(GourmetCareeConstants.DEFAULT_PAGE);
		return TransitionConstants.AdvancedRegistration.JSP_APH01L01;
	}




	/**
	 * CSVを出力する
	 * @return null
	 */
	@Execute(validator = true, validate = "validate", input = TransitionConstants.AdvancedRegistration.JSP_APH01L01)
	public String output() {

		AdvancedMemberCsvProperty csvProperty = createCsvProperty();
		// ページ番号は必要無いので、値はなんでもいい
		SearchProperty property = createProperty(0, listForm);

		try {
			advancedRegistrationLogic.outputCsv(property, csvProperty);
		} catch (UnsupportedEncodingException e) {
			listForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.csvDataNotFound");
		} catch (WNoResultException e) {
			listForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.csvOutPutFailed");
		} catch (IOException e) {
			log.fatal("入出力エラーが発生しました。", e);
			listForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.csvOutPutFailed");
		}


		return null;
	}

	/**
	 * 検索の実行
	 * @param targetPage 表示ページ番号
	 */
	private void doSearch(int targetPage) {
		SearchProperty property = createProperty(targetPage, listForm);
		pageNavi = new PageNavigateHelper(listForm.createMaxRow(GourmetCareeConstants.DEFAULT_MAX_ROW_INT));
		try {
			list = advancedRegistrationLogic.doSearch(property, pageNavi);
		} catch (WNoResultException e) {
			throw new ActionMessagesException("errors.app.dataNotFound");
		}
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
	 * 会員情報リストの取得
	 * @return 会員情報リスト
	 */
	public List<AdvancedRegistrationSearchResultDto> getList() {
		if (list == null) {
			return new ArrayList<AdvancedRegistrationSearchResultDto>();
		}
		return list;
	}


	/**
	 * 表示ページを作成します。
	 * @return 表示ページ番号
	 */
	private int createTargetPage() {
		if (StringUtils.isBlank(listForm.pageNum)) {
			return GourmetCareeConstants.DEFAULT_PAGE;
		}

		return NumberUtils.toInt(listForm.pageNum, GourmetCareeConstants.DEFAULT_PAGE);
	}


	/**
	 * CSVプロパティを作成します。
	 * @return CSVプロパティ
	 */
	private AdvancedMemberCsvProperty createCsvProperty() {
		AdvancedMemberCsvProperty property = new AdvancedMemberCsvProperty();
		property.fileName = CSV_FILE_NAME;
		property.encode = getCommonProperty("gc.csv.encoding");
		property.pass = getCommonProperty("gc.csv.filepass");
		return property;
	}

	/**
	 * メールマガジン作成画面へ遷移します。
	 * @return メールマガジン作成画面パス
	 */
	@Execute(validator = false)
	public String mailMagazine() {
		return "/advancedRegistrationMemberMailMag/input/?redirect=true";
	}


	/**
	 * 事前登録の来場ステータスをトグルする。
	 * 終了時には、トグル結果を文字列にしてレスポンスに返す。
	 * POSTで、「id」を受け取る。
	 * idは、t_advanced_registration.idのこと。
	 */
	@Execute(validator = false)
	public String toggle() {
		if (!NumberUtils.isDigits(listForm.toggleId)) {
			throw new FraudulentProcessException("toggleするIDが不正");
		}

		logic.toggleAttendedStatus(Integer.parseInt(listForm.toggleId));

		return REDIRECT_PATH;
	}

	@Execute(validator = false, input = TransitionConstants.AdvancedRegistration.JSP_APH01L01)
	public String comp() {
		doSearch(NumberUtils.toInt(listForm.pageNum, 1));
		return TransitionConstants.AdvancedRegistration.JSP_APH01L01;
	}

	/**
	 * プロパティを作成します
	 * @return
	 */
	protected SearchProperty createProperty(int targetPage, ListForm listForm) {
		SearchProperty property = advancedRegistrationLogic.createSearchProperty();
		Beans.copy(listForm, property)
				.excludesWhitespace()
				.excludesNull()
				.execute();

		property.advancedRegistrationIdList = GourmetCareeUtil.convertStringArrayToIntegerList(listForm.advancedRegistrationIdArray);

		property.registrationStartTimestamp = listForm.createRegistrationStartTimestamp();
		property.registrationEndTimestamp = listForm.createRegistrationEndTimestamp();
		property.targetPage = targetPage;

		return property;
	}

}
