package com.gourmetcaree.admin.pc.customer.action.customerMailMag;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.MessageResourcesUtil;

import com.gourmetcaree.admin.pc.customer.dto.customer.CompanySalesDto;
import com.gourmetcaree.admin.pc.customer.dto.customer.CustomerInfoDto;
import com.gourmetcaree.admin.pc.customer.form.customerMailMag.ListForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.logic.CustomerLogic;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.MCustomer;
import com.gourmetcaree.db.common.entity.MCustomerCompany;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.CustomerMailMagazineAreaService;
import com.gourmetcaree.db.common.service.CustomerSubMailService;

/**
 * 顧客向けメルマガ配信先一覧確認アクションクラス
 * @author Makoto Otani
 * @version 1.0
 *
 */
@ManageLoginRequired(authLevel=ManageAuthLevel.ADMIN)
public class ListAction extends PcAdminAction {

	/** 登録フォーム */
	@ActionForm
	@Resource
	protected ListForm listForm;

	/** 顧客ロジック */
	@Resource
	protected CustomerLogic customerLogic;

	/** 顧客一覧 */
	public List<CustomerInfoDto> list;

	@Resource
	private CustomerMailMagazineAreaService customerMailMagazineAreaService;

	@Resource
	private CustomerSubMailService customerSubMailService;

	/**
	 * 初期表示
	 * @return 一覧確認画面
	 */
	@Execute(validator = false, reset = "resetForm", input=TransitionConstants.Customer.JSP_APD02L02)
	@MethodAccess(accessCode = "CUSTOMERMAILMAG_LIST_INDEX")
	public String index() {
		return show();
	}

	/**
	 * 確認
	 * @return メルマガ入力画面
	 */
	@Execute(validator = false, input=TransitionConstants.Customer.JSP_APD02L02)
	@MethodAccess(accessCode = "CUSTOMERMAILMAG_LIST_CONF")
	public String conf() {
		// メルマガ入力画面へ遷移
		return TransitionConstants.Customer.CUSTOMERMAILMAG_INPUT_INDEX;
	}

	/**
	 * 戻る
	 * @return 顧客一覧再検索メソッド
	 */
	@Execute(validator = false, removeActionForm = true, input=TransitionConstants.Customer.JSP_APD02L02)
	@MethodAccess(accessCode = "CUSTOMERMAILMAG_LIST_BACK")
	public String back() {
		// リスト画面へ遷移
		return TransitionConstants.Customer.REDIRECT_CUSTOMER_BACKMAILMAG;
	}

	/**
	 * メルマガ入力画面から再び戻った場合に、遷移前の状態(=確認結果画面)を表示するために使用する再表示メソッド
	 * @return 顧客一覧確認画面
	 */
	@Execute(validator=false, input=TransitionConstants.Customer.JSP_APD02L02)
	@MethodAccess(accessCode="CUSTOMERMAILMAG_LIST_BACKMAILMAG")
	public String backMailMag() {

		// 非表示
		listForm.setExistDataFlgNg();

		// 再表示処理
		return show();
	}

	/**
	 * 初期表示遷移用
	 * @return 登録画面のパス
	 */
	private String show() {

		// 遷移チェック
		if (listForm.customerIdList == null || listForm.customerIdList.isEmpty()) {
			listForm.setExistDataFlgNg();
			// 「該当するデータが見つかりませんでした。」
			throw new ActionMessagesException("errors.app.dataNotFound");
		}

		// エラーId
		List<String> errorIdList = new ArrayList<String>();

		// リストの初期化
		list = new ArrayList<CustomerInfoDto>();

		// 選択した顧客が削除されていないかチェックする
		for (Iterator<String> it = listForm.customerIdList.iterator(); it.hasNext();) {
			String id = it.next();
			try {
				// データの検索
				MCustomer entity = customerLogic.getCustomerListById(Integer.parseInt(id));

				// メルマガを受信しない顧客はリストから削除し、continue
				if (GourmetCareeUtil.eqInt(MTypeConstants.CustomerMailMagazineReceptionFlg.NOT_RECEIVE,
						entity.mailMagazineReceptionFlg)) {
					it.remove();
					continue;
				}
				// 画面表示用に変換する
				convertShowList(entity);

			// データが存在しない場合は存在しないIDを保持
			} catch (WNoResultException e) {
				errorIdList.add(id);
			}
		}

		// IDが削除されている場合は、エラーメッセージを表示
		if (errorIdList != null && !errorIdList.isEmpty()) {
			// 画面非表示
			listForm.setExistDataFlgNg();
			// IDをカンマ区切りで表示
			String errorId = GourmetCareeUtil.createKanmaSpaceStr(errorIdList);
			// 「{顧客iD：id, ...}が削除されている可能性があるため、{メルマガ配信}することはできません。」
			throw new ActionMessagesException("errors.canNotProcessDel",
					MessageResourcesUtil.getMessage("labels.customerId") + "：" + errorId,
					MessageResourcesUtil.getMessage("labels.mailStopFlg"));
		}

		if (CollectionUtils.isEmpty(listForm.customerIdList)) {
			// 「該当するデータが見つかりませんでした。」
			throw new ActionMessagesException("errors.app.dataNotFound");
		}

		// 画面表示
		listForm.setExistDataFlgOk();

		// 確認画面へ遷移
		return TransitionConstants.Customer.JSP_APD02L02;
	}

	/**
	 * 表示用に加工して、リストにセットする
	 * @param entity 顧客エンティティ
	 */
	private void convertShowList(MCustomer entity) {

		CustomerInfoDto dto = new CustomerInfoDto();
		Beans.copy(entity, dto).execute();

		// 電話番号をセット
		if (!StringUtils.isEmpty(entity.phoneNo1)) {
			dto.phoneNo = entity.phoneNo1 + "-" + entity.phoneNo2 + "-" + entity.phoneNo3;
		}

		// 会社、営業担当者をセット
		dto.companySalesDtoList = new ArrayList<CompanySalesDto>();
		for (MCustomerCompany customerCompany : entity.mCustomerCompanyList) {
			CompanySalesDto companySalesDto = new CompanySalesDto();
			companySalesDto.companyId = String.valueOf(customerCompany.companyId);
			companySalesDto.salesId = String.valueOf(customerCompany.salesId);
			dto.companySalesDtoList.add(companySalesDto);
		}

		// 送信対象のサブメールを取得
		dto.subMailList = customerSubMailService.getReceptionSubMail(entity.id);

		// メルマガエリア
		dto.mailMagazineAreaCdList = customerMailMagazineAreaService.getAreaList(entity.id);

		list.add(dto);
	}
}