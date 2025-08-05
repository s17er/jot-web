package com.gourmetcaree.admin.pc.subWindow.form.customerSearch;

import java.io.Serializable;

import jp.co.whizz_tech.common.sastruts.annotation.IntegerHankakuType;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.common.form.BaseForm;

/**
 * サブウィンドウで顧客データ検索のフォーム
 * @author Katsutoshi Hasegawa
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class SelectForm extends BaseForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -285746500338657418L;

	/** 画面コード */
	public String dispCd;



	/** ページ遷移用に選択されたページ数 */
	public String pageNum;


	/** 最大表示件数 */
	public String maxRow;


	/** 顧客ID */
	@IntegerHankakuType
	public String where_customerId = "";

	/** 担当会社ID */
	public String assignedCompanyId = "";

	/** 営業担当者ID */
	public String assignedSalesId = "";

	/** 顧客名 */
	public String where_customerName = "";

	/** 担当者名 */
	public String where_contactName = "";



	/**
	 * リセットを行う
	 */
	public void resetForm() {
		super.resetBaseForm();
		dispCd = null;
		pageNum = null;
		maxRow = null;
		where_customerId = "";
		assignedCompanyId = "";
		assignedSalesId = "";
		where_customerName = "";
		where_contactName = "";
	}
}
