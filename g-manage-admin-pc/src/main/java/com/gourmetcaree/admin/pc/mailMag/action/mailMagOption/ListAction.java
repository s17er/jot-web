package com.gourmetcaree.admin.pc.mailMag.action.mailMagOption;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.MessageResourcesUtil;

import com.gourmetcaree.admin.pc.mailMag.dto.mailMagOption.ListDto;
import com.gourmetcaree.admin.pc.mailMag.form.mailMagOption.ListForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.entity.TMailMagazineOption;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * メルマガヘッダメッセージのリストアクション
 * @author Takehiro Nakamori
 *
 */
@ManageLoginRequired(authLevel=ManageAuthLevel.ADMIN)
public class ListAction extends BaseMailMagOptionAction {

	@Resource
	@ActionForm
	private ListForm listForm;

	/** メルマガオプションのリスト */
	public List<ListDto> dtoList;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator=false, reset = "resetForm", input = TransitionConstants.MailMag.JSP_APK02L01)
	@MethodAccess(accessCode="MAILMAGOPT_HEADER_LIST")
	public String index() {

		// 表示件数切替の初期値が取得できない場合は、システム全体の初期値をセット
		listForm.maxRowValue = GourmetCareeUtil.checkStringEmpty(getInitMaxRow(), GourmetCareeConstants.DEFAULT_MAX_ROW);

		// 表示フラグのリセット
		listForm.setExistDataFlgOk();

		return show();
	}

	/**
	 * 初期表示遷移メソッド
	 * @return
	 */
	public String show() {
		createResult(getData());
		return TransitionConstants.MailMag.JSP_APK02L01;
	}


	/**
	 * ヘッダメッセージデータの取得
	 * @return
	 */
	private List<TMailMagazineOption> getData() {
		List<TMailMagazineOption> entityList = new ArrayList<TMailMagazineOption>();

		try {

			entityList = mailMagazineOptionService.getHeaderMessageList(listForm.maxRowValue, listForm.areaCd);

		} catch (WNoResultException e) {
			// 件数が0件の場合はエラーメッセージを返す「{ヘッダメッセージ}は現在登録されていません。」
			listForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.noInputData", MessageResourcesUtil.getMessage("msg.app.mailMagHeader"));

		} catch (NumberFormatException e) {
			// intの変換ができなければエラー
			throw new FraudulentProcessException("不正なデータが入力されました。");
		}

		if (entityList == null || entityList.isEmpty()) {
			// 件数が0件の場合はエラーメッセージを返す「{ヘッダメッセージ}は現在登録されていません。」
			listForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.noInputData", MessageResourcesUtil.getMessage("msg.app.mailMagHeader"));
		}

		return entityList;
	}

	/**
	 * 表示する値の作成
	 */
	private void createResult(List<TMailMagazineOption> entityList) {
		List<ListDto> dtoList = new ArrayList<ListDto>();

		for (TMailMagazineOption entity : entityList) {
			ListDto dto = new ListDto();
			Beans.copy(entity, dto).execute();

			dto.detailPath = GourmetCareeUtil.makePath("/mailMagOption/headerDetail/", "index", String.valueOf(entity.id));
			if (entity.id != null) {
				dto.editPath = GourmetCareeUtil.makePath("/mailMagOption/headerEdit/", "index", String.valueOf(entity.id));
				dto.deletePath = GourmetCareeUtil.makePath("/mailMagOption/headerDelete/", "index", String.valueOf(entity.id));
			}
			dtoList.add(dto);
		}
		this.dtoList = dtoList;
	}

	@Execute(validator=false, reset = "resetFormShowList", input = TransitionConstants.MailMag.JSP_APK02L01)
	@MethodAccess(accessCode="MAILMAGOPT_HEADER_LIST")
	public String showList() {
		// 直接呼ばれるなどで、Formに値が登録されていない場合は、初期値をセット
		if (CollectionUtils.isEmpty(listForm.areaCd)) {
			// リストの初期化
			listForm.resetForm();
			// 表示件数切替の初期値が取得できない場合は、システム全体の初期値をセット
			listForm.maxRowValue =  GourmetCareeUtil.checkStringEmpty(getInitMaxRow(), GourmetCareeConstants.DEFAULT_MAX_ROW);
		}
		return show();
	}


	/**
	 * 最大件数切替の初期値をプロパティから取得します。
	 * @return 最大表示件数の初期値
	 */
	private String getInitMaxRow() {
		return getCommonProperty("gc.volume.initMaxRow");
	}


}
