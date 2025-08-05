package com.gourmetcaree.admin.pc.maintenance.action.company;

import javax.annotation.Resource;

import org.seasar.framework.beans.util.BeanUtil;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.maintenance.form.company.CompanyForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.service.logic.CompanyLogic;
import com.gourmetcaree.admin.service.property.CompanyProperty;
import com.gourmetcaree.db.common.entity.MCompany;
import com.gourmetcaree.db.common.entity.MCompanyArea;
import com.gourmetcaree.db.common.exception.WNoResultException;
/**
*
* 会社のBaseクラス
* @author Makoto Otani
*
*/
public abstract class CompanyBaseAction extends PcAdminAction {

	/** 会社管理のロジック */
	@Resource
	protected CompanyLogic companyLogic;

	/**
	 * 会社マスタからデータを取得する
	 * @return 会社マスタエンティティ
	 */
	protected MCompany getData(CompanyForm form) throws WNoResultException {

		// IDが正常かチェック
		checkId(form, form.id);

		// ロジック受け渡し用プロパティに値をセット
		CompanyProperty property = new CompanyProperty();

		// idをエンティティにセット
		MCompany mCompany = new MCompany();
		mCompany.id = Integer.parseInt(form.id);
		property.mCompany = mCompany;

		// データの取得
		companyLogic.getCompanyDetail(property);

		// リストが空の場合はエラーメッセージを返す
		if (property == null || property.mCompany == null) {

			// 画面表示をしない
			form.setExistDataFlgNg();

			//「該当するデータが見つかりませんでした。」
			dataNotFoundMessage();
		}

		// 取得したリストを返却
		return property.mCompany;

	}

	/**
	 * 「該当するデータが見つかりませんでした。」のエラーメッセージを返す
	 */
	protected void dataNotFoundMessage() {

		// 「該当するデータが見つかりませんでした。」
		throw new ActionMessagesException("errors.app.dataNotFound");

	}

	/**
	 * 検索結果をFormに移し返すロジック
	 * @param entity 会社マスタエンティティ
	 */
	protected void createDisplayValue(MCompany entity, CompanyForm form) {

		// エンティティから情報を取得してFormにセット
		BeanUtil.copyProperties(entity, form);

		// エリアコードをFormの配列にセット
		for (MCompanyArea mCompanyArea : entity.mCompanyAreaList){
			form.areaCd.add(String.valueOf(mCompanyArea.areaCd));
		}
	}

}