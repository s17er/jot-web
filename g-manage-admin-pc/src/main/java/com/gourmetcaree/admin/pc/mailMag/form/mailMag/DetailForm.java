package com.gourmetcaree.admin.pc.mailMag.form.mailMag;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gourmetcaree.common.form.BaseForm;
import com.gourmetcaree.db.common.entity.TMailMagazineDetail;

/**
 * メルマガ詳細のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
public class DetailForm extends BaseForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -5386685992241175633L;

	/** メルマガID */
	public String id;

	/** 配信日時 */
	public Date deliveryDatetime;

	/** 配信先区分 */
	public Integer deliveryKbn;

	/** 配信先フラグ  */
	public Integer deliveryFlg;

	/** メルマガ詳細リスト */
	public List<TMailMagazineDetail> detailList = new ArrayList<TMailMagazineDetail>();

	/**
	 * リセットを行う
	 */
	public void resetForm() {
		resetBaseForm();
		id = null;
		deliveryDatetime = null;
		deliveryKbn = null;
		deliveryFlg = null;
		detailList = new ArrayList<TMailMagazineDetail>();
	}
}