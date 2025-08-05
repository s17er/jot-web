package com.gourmetcaree.admin.pc.maintenance.form.sales;

import java.io.Serializable;
import java.util.List;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.admin.pc.maintenance.dto.sales.SalesDto;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;

/**
 * 営業担当者一覧のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class ListForm extends SalesForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -8229364788687386023L;

	/** ページナビゲーター */
	public PageNavigateHelper pageNavi;

	/** ページ遷移用に選択されたページ数 */
	public String pageNum;

	/** 最大表示件数 */
	public String maxRow;

	/** 氏名 */
	public String where_salesName = "";

	/** 氏名カナ */
	public String where_salesNameKana = "";

	/** 会社 */
	public String where_companyId = "";

	/** 権限 */
	public String where_authorityCd = "";

	/** 営業担当者一覧 */
	public List<SalesDto> salesList;


	/**
	 * リセットを行う
	 */
	public void resetForm() {
		super.resetForm();
		pageNavi = null;
		pageNum = null;
		maxRow = null;
		where_salesName = "";
		where_salesNameKana = "";
		where_companyId = "";
		where_authorityCd = "";
		salesList = null;
	}

}