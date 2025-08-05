package com.gourmetcaree.admin.pc.webdata.form.webdata;

import java.io.Serializable;

import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.db.common.constants.MTypeConstants;

/**
 * WEBデータ登録のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class InputForm extends WebdataForm implements Serializable  {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1669366921609798645L;


	/**
	 *  独自チェックを行う
	 * @return ActionMessages
	 */
	@Override
	public ActionMessages validate() {
		return super.validate();
	}

	/**
	 * リセットを行う
	 */
	@Override
	public void resetForm() {

		super.resetBaseForm();
		super.resetForm();

		// 応募フォームフラグの初期値を有"1"にセット
		this.applicationFormKbn = String.valueOf(MTypeConstants.ApplicationFormKbn.EXIST);

		// 動画フラグの初期値を無"0"にセット
		this.movieFlg = String.valueOf(MTypeConstants.MovieFlg.NON);

		// 複数勤務地店舗フラグの初期値を非対象"0"にセット
		this.multiWorkingPlaceFlg = String.valueOf(MTypeConstants.MultiWorkingPlaceFlg.NOT_TARGET);

		// 注目店舗フラグの初期値を非対象"0"にセット
		this.attentionShopFlg = MTypeConstants.AttentionShopFlg.NOT_TARGET;

		// 掲載終了表示フラグの初期値を表示するにセット
		this.publicationEndDisplayFlg = MTypeConstants.PublicationEndDisplayFlg.DISPLAY_OK;

		// 検索対象フラグの初期値を対象にセット
		this.searchTargetFlg = MTypeConstants.SearchTargetFlg.TARGET;
	}
}
