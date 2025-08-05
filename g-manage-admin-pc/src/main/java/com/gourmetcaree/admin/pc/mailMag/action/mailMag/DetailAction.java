package com.gourmetcaree.admin.pc.mailMag.action.mailMag;

import java.io.IOException;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.ResponseUtil;

import com.gourmetcaree.admin.pc.mailMag.form.mailMag.DetailForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.logic.MailMagazineLogic;
import com.gourmetcaree.admin.service.property.MailMagazineProperty;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.db.common.entity.TMailMagazine;
import com.gourmetcaree.db.common.entity.TMailMagazineDetail;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * メルマガ詳細アクションクラス
 * @author Makoto Otani
 * @version 1.0
 *
 */
@ManageLoginRequired(authLevel=ManageAuthLevel.ADMIN)
public class DetailAction extends PcAdminAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(DetailAction.class);

	/** 登録フォーム */
	@ActionForm
	@Resource
	protected DetailForm detailForm;

	/** メルマガロジック */
	@Resource
	protected MailMagazineLogic mailMagazineLogic;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, urlPattern = "index/{id}", reset = "resetForm", input=TransitionConstants.MailMag.JSP_APK01R01)
	@MethodAccess(accessCode="MAILMAG_DETAIL_INDEX")
	public String index() {

		// IDが正しいかチェック
		checkId(detailForm, detailForm.id);

		return show();
	}

	/**
	 * 戻る
	 * @return メルマガ一覧
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="MAILMAG_DETAIL_BACK")
	public String back() {
		// メルマガ一覧再検索メソッドへリダイレクト
		return TransitionConstants.MailMag.REDIRECT_MAILMAG_LIST_SEARCH_AGAIN;
	}

	/**
	 * CSV出力
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="MAILMAG_DETAIL_OUTPUT")
	public String outPut() {

		// IDが不正かどうかチェック
		try {
			Integer.parseInt(detailForm.id);
		} catch (NumberFormatException e) {
			// 画面を非表示に設定
			detailForm.setExistDataFlgNg();
			// 「CSV出力に失敗しました。」
			throw new ActionMessagesException("errors.app.csvDataNotFound");
		}

		try {
			// 出力条件をセット
			MailMagazineProperty property = new MailMagazineProperty();
			property.mailMagazineId = Integer.parseInt(detailForm.id);

			// CSV出力
			int count = mailMagazineLogic.outPutCsv(property);

			ResponseUtil.write(String.valueOf(count));

		// データが取得できない場合のエラー
		} catch (WNoResultException e) {
			detailForm.setExistDataFlgNg();
			// 「対象のデータが見つかりませんでした。」
			throw new ActionMessagesException("errors.app.csvDataNotFound");

		// CSV出力時のエラー
		} catch (IOException e) {
			log.fatal("メルマガCSV入出力エラーが発生しました。", e);
			detailForm.setExistDataFlgNg();
			// 「CSV出力に失敗しました。」
			throw new ActionMessagesException("errors.app.csvOutPutFailed");

		}
		return null;
	}

	/**
	 * 初期表示遷移用
	 * @return 登録画面のパス
	 */
	private String show() {

		// データを取得して画面表示用に変換
		createDisplayValue(getData());
		log.debug("メルマガ詳細の取得をしました。");

		// 登録画面へ遷移
		return TransitionConstants.MailMag.JSP_APK01R01;
	}

	/**
	 * データの取得
	 * @return メルマガエンティティ
	 */
	private TMailMagazine getData() {

		// 検索条件のセット
		MailMagazineProperty property = new MailMagazineProperty();
		property.mailMagazineId = Integer.parseInt(detailForm.id);

			try {
				// データの取得
				return mailMagazineLogic.getMailMagazineDetail(property, false);

			// データが取得できない場合はエラー
			} catch (WNoResultException e) {

				// 画面非表示
				detailForm.setExistDataFlgNg();
				// 「該当するデータが見つかりませんでした。」
				throw new ActionMessagesException("errors.app.dataNotFound");
			}
	}

	/**
	 * 画面表示用のデータを作成する
	 * @param entity メルマガエンティティ
	 */
	private void createDisplayValue(TMailMagazine entity) {

		// データをコピー
		Beans.copy(entity, detailForm).execute();

		// メルマガ詳細をセット
		for (TMailMagazineDetail detailEntity : entity.tMailMagazineDetailList) {
			detailForm.detailList.add(detailEntity);
		}
	}
}