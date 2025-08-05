package com.gourmetcaree.admin.pc.maintenance.action.special;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.util.StringUtil;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.MessageResourcesUtil;

import com.gourmetcaree.admin.pc.maintenance.dto.special.ListDto;
import com.gourmetcaree.admin.pc.maintenance.form.special.ListForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.logic.SpecialLogic;
import com.gourmetcaree.admin.service.property.SpecialProperty;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.entity.MSpecial;
import com.gourmetcaree.db.common.entity.MSpecialDisplay;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.AreaService;

/**
 * 特集一覧を表示するクラス
 * @author Makoto Otani
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN})
public class ListAction extends SpecialBaseAction {

	/** フォーム */
	@ActionForm
	@Resource
	protected ListForm listForm;

	@Resource
	private AreaService areaService;

	@Resource
	private SpecialLogic specialLogic;

	/**
	 * 初期表示
	 * @return 一覧画面
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Maintenance.JSP_APJ03L01)
	@MethodAccess(accessCode="SPECIAL_LIST_INDEX")
	public String index() {

		// 表示フラグのリセット
		listForm.setExistDataFlgNg();

		return show();
	}

	/**
	 * 初期表示遷移用
	 * @return 一覧画面のパス
	 */
	private String show() {

		// エリアリンク名を取得(型をそろえておく)
		areaService.getAllLinkNameMap().forEach((cd, val) -> listForm.areaLinkNameMap.put(String.valueOf(cd), val));

		// 一覧画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ03L01;
	}

	/**
	 * 検索
	 * @return 一覧画面
	 */
	@Execute(validator = false, input = TransitionConstants.Maintenance.JSP_APJ03L01)
	public String search() {

		// DBから取得したデータを表示用に変換
		listForm.list = createDisplayValue(getData());

		// 表示フラグを表示
		listForm.setExistDataFlgOk();

		return show();
	}

	/**
	 * 特集マスタからデータを取得する
	 */
	private List<MSpecial> getData() {

		try {
			MSpecial special = new MSpecial();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			if(StringUtil.isNotBlank(listForm.postStartDate)) {
				special.postStartDatetime = sdf.parse(listForm.postStartDate);
			}
			if(StringUtil.isNotBlank(listForm.postEndDate)) {
				special.postEndDatetime = sdf.parse(listForm.postEndDate);
			}
			SpecialProperty property = new SpecialProperty();
			property.mSpecial = special;
			if(!StringUtil.isEmpty(listForm.areaCd)) {
				property.areaCd = new ArrayList<Integer>(Arrays.asList(Integer.valueOf(listForm.areaCd)));
			}

			// データの取得
			return specialLogic.searchSpecial(property);

		// データが取得できない場合
		} catch (WNoResultException e) {

			// 画面表示をしない
			listForm.setExistDataFlgNg();

			// 件数が0件の場合はエラーメッセージを返す「{特集データ}は現在登録されていません。」
			throw new ActionMessagesException("errors.app.noInputData", MessageResourcesUtil.getMessage("msg.app.special"));

		} catch (ParseException e) {
			// 画面表示をしない
			listForm.setExistDataFlgNg();

			// 日付の形式が正しく無い場合はエラーメッセージを返す「日付を正しく入力してください。」
			throw new ActionMessagesException("errors.app.dateFailed");
		}

	}

	/**
	 * 検索結果を画面表示のDtoリストに設定する
	 * @param entityList MSpecialエンティティのリスト
	 * @return Dtoのリスト
	 */
	private List<ListDto> createDisplayValue(List<MSpecial> entityList) {

		List<ListDto> retList = new ArrayList<ListDto>();

		// 取得した値をdtoにセット
		for (MSpecial entity : entityList) {

			ListDto dto = new ListDto();
			Beans.copy(entity, dto).execute();

			// アドレスの作成
			dto.url = getSpecialUrl() + String.valueOf(dto.id);

			// 編集画面のパスをセット
			dto.editPath = GourmetCareeUtil.makePath(TransitionConstants.Maintenance.ACTION_SPECIAL_EDIT_INDEX, String.valueOf(dto.id));
			// 削除画面のパスをセット
			dto.deletePath = GourmetCareeUtil.makePath(TransitionConstants.Maintenance.ACTION_SPECIAL_DELETE_INDEX, String.valueOf(dto.id));

			// エリアをセット
			if (entity.mSpecialDisplayList != null && !entity.mSpecialDisplayList.isEmpty()) {
				for (MSpecialDisplay mSpecialDisplay : entity.mSpecialDisplayList) {
					dto.areaCd.add(String.valueOf(mSpecialDisplay.areaCd));
				}
			}

			retList.add(dto);
		}

		return retList;
	}
}