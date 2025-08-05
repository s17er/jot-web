package com.gourmetcaree.admin.pc.maintenance.action.mischief;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.seasar.framework.beans.util.BeanUtil;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.maintenance.dto.mischief.ListDto;
import com.gourmetcaree.admin.pc.maintenance.form.mischief.ListForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.logic.MischiefApplicationConditionLogic;
import com.gourmetcaree.admin.service.property.MischiefApplicationConditionProperty;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.entity.MMischiefApplicationCondition;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * いたずら応募条件一覧クラス
 * @author Aquarius
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN})
public class ListAction extends PcAdminAction {

	/** 一覧フォーム */
	@ActionForm
	@Resource
	protected ListForm listForm;

	/** いたずら応募条件ロジック */
	@Resource
	protected MischiefApplicationConditionLogic mischiefApplicationConditionLogic;

	/** いたずら応募条件一覧リスト */
	public List<ListDto> list;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Maintenance.JSP_APJ10L01)
	@MethodAccess(accessCode = "MISCHIEF_LIST_INDEX")
	public String index() {

		listForm.setExistDataFlgNg();

		return show();
	}

	/**
	 * 検索
	 * @return 一覧画面
	 */
	@Execute(validator = false, input = TransitionConstants.Maintenance.JSP_APJ10L01)
	@MethodAccess(accessCode="MISCHIEF_LIST_INDEX")
	public String search() {

		// DBからセレクトされたDTOを画面表示用のDTOに移し変えます。
		list = createResult(getData());

		// 表示フラグを表示
		listForm.setExistDataFlgOk();

		return show();
	}

	/**
	 * 初期表示遷移用
	 *
	 * @return 入力画面のパス
	 */
	private String show() {

		// 一覧画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ10L01;
	}

	/**
	 * データ取得
	 * @return
	 */
	private List<MMischiefApplicationCondition> getData() {

		MMischiefApplicationCondition mMischiefApplicationCondition = new MMischiefApplicationCondition();
		BeanUtil.copyProperties(listForm, mMischiefApplicationCondition);

		MischiefApplicationConditionProperty property = new MischiefApplicationConditionProperty();
		property.mMischiefApplicationCondition = mMischiefApplicationCondition;

		try {

			mischiefApplicationConditionLogic.getMischiefApplicationConditionList(property);

		} catch (WNoResultException e) {
			// 画面表示をしない
			listForm.setExistDataFlgNg();

			// 件数が0件の場合はエラーメッセージを返す「該当するデータが見つかりませんでした。」
			throw new ActionMessagesException("errors.app.dataNotFound");
		}


		return property.entityList;
	}

	/**
	 * 検索結果表示用のリストを作成
	 * @param entityList
	 * @return
	 */
	private List<ListDto> createResult(List<MMischiefApplicationCondition> entityList) {

		List<ListDto> retList = new ArrayList<ListDto>();

		for(MMischiefApplicationCondition entity : entityList) {
			ListDto dto = new ListDto();
			Beans.copy(entity, dto).execute();
			if(StringUtils.isNotBlank(entity.municipality) || StringUtils.isNotBlank(entity.address)) {
				dto.address = entity.municipality + " " + entity.address;
			} else {
				dto.address = null;
			}

			dto.detailPath = GourmetCareeUtil.makePath(TransitionConstants.Maintenance.ACTION_MISCHIEF_DETAIL_INDEX, String.valueOf(dto.id));

			retList.add(dto);
		}

		return retList;
	}


}