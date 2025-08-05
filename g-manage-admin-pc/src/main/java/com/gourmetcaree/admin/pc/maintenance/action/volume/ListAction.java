package com.gourmetcaree.admin.pc.maintenance.action.volume;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.seasar.framework.beans.util.BeanUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.MessageResourcesUtil;

import com.gourmetcaree.admin.pc.maintenance.dto.volume.ListDto;
import com.gourmetcaree.admin.pc.maintenance.form.volume.ListForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.entity.MVolume;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.VolumeService;

/**
 *
 * 号数一覧を表示するクラス
 * @author Makoto Otani
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN})
public class ListAction extends PcAdminAction {

	/** 号数マスタのサービス */
	@Resource
	protected VolumeService volumeService;

	/** フォーム */
	@ActionForm
	@Resource
	protected ListForm listForm;

	/** 号数一覧リスト */
	public List<ListDto> list;

	/**
	 * 初期表示
	 * @return 一覧画面
	 */
	@Execute(validator = false, reset = "resetForm", input = TransitionConstants.Maintenance.JSP_APJ02L01)
	@MethodAccess(accessCode="VOLUME_LIST_INDEX")
	public String index() {

		// 表示件数切替の初期値が取得できない場合は、システム全体の初期値をセット
		listForm.maxRowValue =  GourmetCareeUtil.checkStringEmpty(getInitMaxRow(), GourmetCareeConstants.DEFAULT_MAX_ROW);

		// 表示フラグのリセット
		listForm.setExistDataFlgOk();

		return show();
	}

	/**
	 * エリア、表示件数を選択した時の一覧表示
	 * @return 一覧画面
	 */
	@Execute(validator = false, reset = "resetFormShowList",input = TransitionConstants.Maintenance.JSP_APJ02L01)
	@MethodAccess(accessCode="VOLUME_LIST_SHOWLIST")
	public String showList() {

		// 直接呼ばれるなどで、Formに値が登録されていない場合は、初期値をセット
		if (StringUtil.isEmpty(listForm.areaCd)) {
			// リストの初期化
			listForm.resetForm();
			// 表示件数切替の初期値が取得できない場合は、システム全体の初期値をセット
			listForm.maxRowValue =  GourmetCareeUtil.checkStringEmpty(getInitMaxRow(), GourmetCareeConstants.DEFAULT_MAX_ROW);
		}

		// 指定の条件で検索して、号数一覧のパスを返す
		return show();

	}

	/**
	 * 初期表示遷移用
	 * @return 登録画面のパス
	 */
	private String show() {

		// DBからセレクトされたDTOを画面表示用のDTOに移し変えます。
		list = createResult(getData());

		// 一覧画面へ遷移
		return TransitionConstants.Maintenance.JSP_APJ02L01;
	}

	/**
	 * 号数マスタからデータを取得する
	 */
	private List<MVolume> getData() {

		List<MVolume> entityList = null;

		try{
			// データの取得
			entityList = volumeService.getVolumeList(listForm.areaCd, listForm.maxRowValue);

		} catch (WNoResultException e) {

			// 画面表示をしない
			listForm.setExistDataFlgNg();

			// 件数が0件の場合はエラーメッセージを返す「{号数データ}は現在登録されていません。」
			throw new ActionMessagesException("errors.app.noInputData", MessageResourcesUtil.getMessage("msg.app.volume"));
		}
		if (entityList == null || entityList.isEmpty()) {
			// リストが空の場合はエラーメッセージを返す「{号数データ}は現在登録されていません。」
			throw new ActionMessagesException("errors.app.noInputData", MessageResourcesUtil.getMessage("msg.app.volume"));
		}

		// 取得したリストを返却
		return entityList;

	}

	/**
	 * 検索結果をDtoのリストに移し返すロジック
	 * @param entityList MVolumeエンティティのリスト
	 * @return Dtoのリスト
	 */
	private List<ListDto> createResult(List<MVolume> entityList) {

		List<ListDto> retList = new ArrayList<ListDto>();

		Iterator<MVolume> it = entityList.iterator();

		//m_volumeから情報を取得してdtoを作成
		while(it.hasNext()) {

			MVolume entity = it.next();

			// 取得した値をdtoにセット
			ListDto dto = new ListDto();
			BeanUtil.copyProperties(entity, dto);

			// 編集画面のパスをセット
			dto.editPath = GourmetCareeUtil.makePath(TransitionConstants.Maintenance.ACTION_VOLUME_EDIT_INDEX, String.valueOf(dto.id));
			// 削除画面のパスをセット
			dto.deletePath = GourmetCareeUtil.makePath(TransitionConstants.Maintenance.ACTION_VOLUME_DELETE_INDEX, String.valueOf(dto.id));

			retList.add(dto);
		}

		return retList;
	}

	/**
	 * 最大件数切替の初期値をプロパティから取得します。
	 * @return 最大表示件数の初期値
	 */
	private String getInitMaxRow() {
		return getCommonProperty("gc.volume.initMaxRow");
	}
}