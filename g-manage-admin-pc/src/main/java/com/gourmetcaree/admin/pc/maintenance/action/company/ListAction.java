package com.gourmetcaree.admin.pc.maintenance.action.company;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.seasar.framework.beans.util.BeanUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.maintenance.dto.company.ListDto;
import com.gourmetcaree.admin.pc.maintenance.form.company.ListForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.logic.CompanyLogic;
import com.gourmetcaree.admin.service.property.CompanyProperty;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.entity.MCompany;
import com.gourmetcaree.db.common.entity.MCompanyArea;
import com.gourmetcaree.db.common.exception.WNoResultException;
/**
*
* 会社一覧を表示するクラス
* @author Makoto Otani
*
*/
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN})
public class ListAction extends PcAdminAction {

	/** 登録フォーム */
	@ActionForm
	@Resource
	protected ListForm listForm;

	/** 会社管理のロジック */
	@Resource
	protected CompanyLogic companyLogic;

	/** 会社一覧リスト */
	public List<ListDto> list;


	/**
	 * 初期表示
	 * @return 一覧画面
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Maintenance.JSP_APJ05L01)
	@MethodAccess(accessCode="COMPANY_LIST_INDEX")
	public String index() {

		// 表示フラグを非表示
		listForm.setExistDataFlgNg();

		return show();
	}

	/**
	 * 検索
	 * @return 一覧画面
	 */
	@Execute(validator = false, input = TransitionConstants.Maintenance.JSP_APJ05L01)
	@MethodAccess(accessCode="COMPANY_LIST_SEARCH")
	public String search() {

		// DBからセレクトされたDTOを画面表示用のDTOに移し変えます。
		list = createResult(getData());

		// 表示フラグを表示
		listForm.setExistDataFlgOk();

		return show();
	}

	/**
	 * 初期表示遷移用
	 * @return 一覧画面のパス
	 */
	private String show() {

		// 登録画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ05L01;
	}


	/**
	 * 会社マスタからデータを取得する
	 */
	private List<MCompany> getData() {

		// 検索結果をエンティティにセット
		MCompany mCompany = new MCompany();
		BeanUtil.copyProperties(listForm, mCompany);

		// ロジック受け渡し用プロパティに値をセット
		CompanyProperty property = new CompanyProperty();
		property.mCompany = mCompany;

		// エリアコードが選択されていればセット
		if(!StringUtil.isEmpty(listForm.areaCd)) {
			property.areaCd.add(listForm.areaCd);
		}

		try{
			// データの取得
			companyLogic.getCompanyList(property);

		} catch (WNoResultException e) {

			// 画面表示をしない
			listForm.setExistDataFlgNg();

			// 件数が0件の場合はエラーメッセージを返す「該当するデータが見つかりませんでした。」
			throw new ActionMessagesException("errors.app.dataNotFound");
		}

		// リストが空の場合はエラーメッセージを返す
		if (property == null || property.entityList == null || property.entityList.isEmpty()) {

			// 画面表示をしない
			listForm.setExistDataFlgNg();

			//「該当するデータが見つかりませんでした。」
			throw new ActionMessagesException("errors.app.dataNotFound");
		}

		// 取得したリストを返却
		return property.entityList;

	}

	/**
	 * 検索結果をDtoのリストに移し返すロジック
	 * @param entityList MCompanyエンティティのリスト
	 * @return Dtoのリスト
	 */
	private List<ListDto> createResult(List<MCompany> entityList) {

		List<ListDto> retList = new ArrayList<ListDto>();

		Iterator<MCompany> it = entityList.iterator();

		//m_companyから情報を取得してdtoを作成
		while(it.hasNext()) {

			MCompany entity = it.next();

			// 取得した値をdtoにセット
			ListDto dto = new ListDto();
			BeanUtil.copyProperties(entity, dto);
			for(MCompanyArea obj :entity.mCompanyAreaList) {
				dto.areaCd.add(String.valueOf(obj.areaCd));
			}
			// 詳細画面のパスをセット
			dto.detailPath = GourmetCareeUtil.makePath(TransitionConstants.Maintenance.ACTION_COMPANY_DETAIL_INDEX, String.valueOf(dto.id));

			retList.add(dto);
		}

		return retList;
	}


}