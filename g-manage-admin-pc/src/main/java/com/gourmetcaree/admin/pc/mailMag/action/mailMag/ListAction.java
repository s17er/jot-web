package com.gourmetcaree.admin.pc.mailMag.action.mailMag;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.mailMag.dto.mailMag.ListDto;
import com.gourmetcaree.admin.pc.mailMag.form.mailMag.ListForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.logic.MailMagazineLogic;
import com.gourmetcaree.admin.service.property.MailMagazineProperty;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TMailMagazine;
import com.gourmetcaree.db.common.entity.TMailMagazineDetail;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.common.service.MailMagazineDetailService;

/**
 * メルマガ一覧アクションクラス
 * @author Makoto Otani
 * @version 1.0
 *
 */
@ManageLoginRequired(authLevel=ManageAuthLevel.ADMIN)
public class ListAction extends PcAdminAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(ListAction.class);

	/** 登録フォーム */
	@ActionForm
	@Resource
	protected ListForm listForm;

	/** メルマガロジック */
	@Resource
	protected MailMagazineLogic mailMagazineLogic;

	/** メルマガ詳細サービス */
	@Resource
	protected MailMagazineDetailService mailMagazineDetailService;


	/** メルマガ一覧 */
	public List<ListDto> list = new ArrayList<ListDto>();

	/** ページナビゲーター */
	public PageNavigateHelper pageNavi;

	/**
	 * 初期表示
	 * @return 一覧画面
	 */
	@Execute(validator = false, reset="resetForm", input = TransitionConstants.MailMag.JSP_APK01L01)
	@MethodAccess(accessCode="MAILMAG_LIST_INDEX")
	public String index() {

		// 表示フラグを非表示
		listForm.setExistDataFlgNg();

		return show();
	}

	/**
	 * 検索
	 * @return 一覧画面
	 */
	@Execute(validator = true, validate = "validate", input = TransitionConstants.MailMag.JSP_APK01L01)
	@MethodAccess(accessCode="MAILMAG_LIST_SEARCH")
	public String search() {

		// 表示フラグを非表示
		listForm.setExistDataFlgNg();

		// DBからセレクトされたリストを画面表示用のDTOに移し変える
		list = createDisplayValue(getData(GourmetCareeConstants.DEFAULT_PAGE));
		log.debug("メルマガデータを取得しました。");

		// 表示フラグを表示
		listForm.setExistDataFlgOk();

		// 一覧画面のパス
		return TransitionConstants.MailMag.JSP_APK01L01;

	}

	/**
	 * ページ遷移のボタン押下時の処理。
	 * 画面から指定されたページ数を数値化して検索を実行
	 * 不正なパラメータの場合は1をデフォルトページを表示する。
	 * @return メルマガ一覧のパス
	 */
	@Execute(validator = false, urlPattern = "changePage/{pageNum}", input = TransitionConstants.MailMag.JSP_APK01L01)
	@MethodAccess(accessCode="MAILMAG_LIST_CHANGEPAGE")
	public String changePage() {

		// 表示フラグを非表示
		listForm.setExistDataFlgNg();

		// ページ番号を指定。ページ番号が取得できない場合は初期値で検索
		int targetPage = NumberUtils.toInt(listForm.pageNum, GourmetCareeConstants.DEFAULT_PAGE);

		// DBからセレクトされたリストを画面表示用のDTOに移し変える
		list = createDisplayValue(getData(targetPage));
		log.debug("メルマガデータを取得しました。");

		// 表示フラグを表示
		listForm.setExistDataFlgOk();

		// 一覧画面のパス
		return TransitionConstants.MailMag.JSP_APK01L01;
	}

	/**
	 * 最大表示件数変更時の処理。
	 * 画面から指定された最大表示件数を数値化して検索を実行
	 * 不正なパラメータの場合は20をデフォル表示件数として表示する。
	 * @return メルマガ一覧のパス
	 */
	@Execute(validator = false, input = TransitionConstants.MailMag.JSP_APK01L01)
	@MethodAccess(accessCode="MAILMAG_LIST_CHANGEMAXROW")
	public String changeMaxRow() {

		// 表示フラグを非表示
		listForm.setExistDataFlgNg();

		// DBからセレクトされたリストを画面表示用のDTOに移し変える
		list = createDisplayValue(getData(GourmetCareeConstants.DEFAULT_PAGE));
		log.debug("メルマガデータを取得しました。");

		// 表示フラグを表示
		listForm.setExistDataFlgOk();

		// 一覧画面のパス
		return TransitionConstants.MailMag.JSP_APK01L01;
	}

	/**
	 * 別画面から再び検索に戻った場合に、遷移前の状態(=検索結果画面)を表示するために使用する
	 * 再検索メソッド
	 * @return メルマガ一覧のパス
	 */
	@Execute(validator = false, input = TransitionConstants.MailMag.JSP_APK01L01)
	@MethodAccess(accessCode="MAILMAG_LIST_SEARCHAGAIN")
	public String searchAgain() {

		// 表示フラグを非表示
		listForm.setExistDataFlgNg();

		int targetPage = GourmetCareeConstants.DEFAULT_PAGE;


		// DBからセレクトされたリストを画面表示用のDTOに移し変える
		list = createDisplayValue(getData(targetPage));
		log.debug("メルマガデータを取得しました。");

		// 表示フラグを表示
		listForm.setExistDataFlgOk();

		// 一覧画面のパス
		return TransitionConstants.MailMag.JSP_APK01L01;
	}

	/**
	 * 初期表示遷移用
	 * @return 一覧画面のパス
	 */
	private String show() {

		// 一覧画面へ遷移
		return TransitionConstants.MailMag.JSP_APK01L01;
	}

	/**
	 * メルマガテーブルからデータを取得する
	 * @param targetPage 表示対象ページ
	 * @return メルマガ一覧リスト
	 */
	private List<TMailMagazine> getData(int targetPage) {


		// エンティティのコピー（空白以外、日付のフォーマット）
		MailMagazineProperty property = Beans.createAndCopy(MailMagazineProperty.class, listForm)
			.excludesWhitespace()
			.dateConverter(GourmetCareeConstants.DATE_FORMAT_SLASH, "deliveryStartDate", "deliveryEndDate")
			.execute();
		property.pageNavi = new PageNavigateHelper(getMaxRow());

		property.targetPage = targetPage;

		try {
			// データの取得
			List<TMailMagazine> mailMagazineList = mailMagazineLogic.getMailMagazineList(property);
			// ページナビをセット
			pageNavi = property.pageNavi;

			return mailMagazineList;

		// データがない場合はエラー
		} catch (WNoResultException e) {
			// 画面非表示
			listForm.setExistDataFlgNg();
			pageNavi = new PageNavigateHelper(getMaxRow());
			// 「該当するデータが見つかりませんでした。」
			throw new ActionMessagesException("errors.app.dataNotFound");

		// 日付のフォーマットエラー
		} catch (ParseException e) {
			pageNavi = new PageNavigateHelper(getMaxRow());
			// 「日付を正しく入力してください。」
			throw new ActionMessagesException("errors.app.dateFailed");
		}
	}

	/**
	 * 表示用リストを生成
	 * @param entityList メルマガ一覧リスト
	 * @return メルマガ一覧Dtoをセットしたリスト
	 */
	private List<ListDto> createDisplayValue(List<TMailMagazine> entityList) {

		List<ListDto> dtolist = new ArrayList<ListDto>();

		// Dtoにコピー
		for (TMailMagazine entity : entityList) {

			ListDto dto = new ListDto();
			// メルマガテーブルの情報をセット
			Beans.copy(entity, dto).execute();

			// 詳細データの取得
			List<TMailMagazineDetail> detailEntityList;
			try {
				detailEntityList = mailMagazineDetailService.getListByMailMagazineId(entity.id);

			// データがない場合は表示しない
			} catch (WNoResultException e) {
				// 画面非表示
				listForm.setExistDataFlgNg();
				// 「該当するデータが見つかりませんでした。」
				throw new ActionMessagesException("errors.app.dataNotFound");
			}

			// 詳細データをセット
			for (TMailMagazineDetail detailEntity : detailEntityList) {

				// 端末区分をセット
				dto.terminalKbn = detailEntity.terminalKbn;

				// PC版の場合、PCタイトル、PC本文をセット
				if (GourmetCareeUtil.eqInt(MTypeConstants.TerminalKbn.PC_VALUE, detailEntity.terminalKbn)) {
					dto.pcMailMagazineTitle = detailEntity.mailMagazineTitle;
					dto.pcBody = detailEntity.body;
				// モバイル版の場合、モバイルタイトル、モバイル本文をセット
				} else {
					dto.mbMailMagazineTitle = detailEntity.mailMagazineTitle;
					dto.mbBody = detailEntity.body;
				}
			}
			dtolist.add(dto);
		}

		// Dtoリストを返却
		return dtolist;
	}

	/**
	 * 最大表示数の取得
	 * @return
	 */
	private int getMaxRow() {
		try {
			return Integer.parseInt(listForm.maxRow);
		} catch (NumberFormatException e) {
			listForm.maxRow = getCommonProperty("gc.mailMag.initMaxRow");
			return NumberUtils.toInt(listForm.maxRow, GourmetCareeConstants.DEFAULT_MAX_ROW_INT);
		}
	}


}