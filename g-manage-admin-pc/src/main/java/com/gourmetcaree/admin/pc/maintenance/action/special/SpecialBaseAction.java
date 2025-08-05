package com.gourmetcaree.admin.pc.maintenance.action.special;

import static com.gourmetcaree.common.util.SqlUtils.asc;
import static com.gourmetcaree.common.util.SqlUtils.dot;
import static org.seasar.framework.util.StringUtil.camelize;

import javax.annotation.Resource;

import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.maintenance.form.special.SpecialForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.db.common.entity.MSpecial;
import com.gourmetcaree.db.common.entity.MSpecialDisplay;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.SpecialService;
/**
 * 特集データのBaseクラス
 * @author Makoto Otani
 *
 */
public abstract class SpecialBaseAction extends PcAdminAction {

	/** 特集マスタのサービス */
	@Resource
	protected SpecialService specialService;

	/** プロパティから取得した特集のURLを返す */
	protected String getSpecialUrl () {
		return getCommonProperty("gc.special.url");
	}

	/**
	 * 特集マスタからデータを取得<br />
	 * データが存在しない場合は、エラーを返す
	 * @return 特集マスタエンティティ
	 */
	protected MSpecial getData(SpecialForm form) {

		// IDが正常かチェック
		checkId(form, form.id);

		try {
			// 編集データの取得
			return specialService.findByIdLeftJoin(camelize(MSpecial.M_SPECIAL_DISPLAY_LIST),
													Integer.parseInt(form.id),
													// エリアコード順にソート
													asc(dot(camelize(MSpecial.M_SPECIAL_DISPLAY_LIST), camelize(MSpecialDisplay.AREA_CD))));
		// データが存在しない場合エラー
		} catch (WNoResultException e) {

			// 画面表示をしない
			form.setExistDataFlgNg();
			//「該当するデータが見つかりませんでした。」
			throw new ActionMessagesException("errors.app.dataNotFound");
		}
	}

}