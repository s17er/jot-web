package com.gourmetcaree.shop.pc.shopList.form.shopList;

import java.io.Serializable;

import javax.persistence.Transient;

import org.apache.commons.lang.ArrayUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.seasar.framework.container.annotation.tiger.Binding;
import org.seasar.framework.container.annotation.tiger.BindingType;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.util.MessageResourcesUtil;

import com.gourmetcaree.common.form.BaseForm;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;


/**
 * 店舗一覧検索アクションフォーム
 * @author Takehiro Nakamori
 *
 */
@Component(instance = InstanceType.SESSION)
public class ListForm extends BaseForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 5986962011009648795L;

	/** 検索条件 エリア */
	public String where_areaCd;

	/** 検索条件 店舗名 */
	public String where_shopName;

	/** 検索条件 業態区分 */
	public String where_industryKbn;

//	/** 検索条件 求人応募 */
//	public String where_jobOfferFlg;

	/** 検索条件 住所 */
	public String where_address;

	/** 検索条件 フリーワード */
	public String where_keyword;

	/** ページナビヘルパー */
	public PageNavigateHelper pageNavi;

	/** インポートするCSV */
	public String importCsvFileName;

	/** 最大表示件数 */
	public String maxRow;

	/** ページ番号 */
	public String pageNum;

	/** ディスプレイを変更するID */
	public String changeDisplayId;

	/** 変更先のディスプレイ番号 */
	public String toDisplayOrder;

	/** 変更するID配列 */
	public String[] changeIdArray;

	@Transient
	@Binding(bindingType = BindingType.NONE)
	public FormFile csvFormFile;
	/**
	 * フォームのリセット
	 */
	public void resetForm() {
		super.resetBaseForm();
		where_areaCd = null;
		where_shopName = null;
		where_industryKbn = null;
//		where_jobOfferFlg = null;
		where_address = null;
		where_keyword = null;
		pageNavi = null;
		importCsvFileName = null;
		maxRow = null;
		pageNum = null;
		csvFormFile = null;
		changeDisplayId = null;
		toDisplayOrder = null;
		changeIdArray = null;
	}


	public void resetFormFile() {
		if (csvFormFile == null) {
			return;
		}
		csvFormFile.destroy();
		csvFormFile = null;
	}

	/**
	 * マルチボックスのリセット
	 */
	public void resetMultiBox() {
		changeIdArray = null;
	}

	/**
	 * 表示番号のリセット
	 */
	public void resetDispOrder() {
		changeDisplayId = null;
		toDisplayOrder = null;
	}

	/**
	 * 一括処理用バリデート
	 */
	public ActionMessages validateForLump() {
		ActionMessages errors = new ActionMessages();
		if (ArrayUtils.isEmpty(changeIdArray)) {
			errors.add("errors", new ActionMessage("errors.required",
									MessageResourcesUtil.getMessage("labes.changeIdArray")));
		}
		return errors;
	}
}
