package com.gourmetcaree.admin.pc.customer.form.customer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.IntegerType;
import org.seasar.struts.annotation.Msg;
import org.seasar.struts.annotation.Required;

import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.form.BaseForm;

import jp.co.whizz_tech.common.sastruts.annotation.IntegerHankakuType;

/**
 * 顧客一覧のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class ListForm extends BaseForm implements Serializable {


	/** シリアルバージョンUID */
	private static final long serialVersionUID = -5729183450109883689L;



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

	/** メールアドレス */
	public String where_mailAddress;

	/** 系列店舗件数（下限） */
	@IntegerType
	public String where_lowerShopListCount;

	/** 系列店舗件数（上限） */
	@IntegerType
	public String where_upperShopListCount;

	/** 系列店舗の都道府県リスト */
	public List<String> where_shopListPrefecturesCdList;

	/** 系列店舗の海外 */
	public List<String> where_shopListShutokenForeignAreaKbnList;

	/** 系列店舗の業態リスト */
	public List<String> where_shopListIndustryKbnList;

	/** メルマガエリアコード */
	public List<String> where_mailMagazineAreaCdList;

	/** 顧客IDリスト */
	@Required(target = "mailMagazine", msg = @Msg(key = "errors.noCustomer"))
	public List<String> customerIdList;

	/**
	 *  入力チェック
	 * @return
	 */
	public ActionMessages validate() {

		ActionMessages errors = new ActionMessages();


		return errors;

	}

	/**
	 * リセットを行う
	 */
	public void resetForm() {
		super.resetBaseForm();
		pageNum = null;
		maxRow = GourmetCareeConstants.DEFAULT_MAX_ROW;
		where_customerId = "";
		assignedCompanyId = "";
		assignedSalesId = "";
		where_customerName = "";
		where_contactName = "";
		customerIdList = null;
		where_mailMagazineAreaCdList = new ArrayList<String>();
		where_mailAddress = null;
		where_lowerShopListCount = null;
		where_upperShopListCount = null;
		where_shopListPrefecturesCdList = new ArrayList<String>();
		where_shopListShutokenForeignAreaKbnList = new ArrayList<String>();
		where_shopListIndustryKbnList = new ArrayList<String>();
	}

	/**
	 * IDをリセットする
	 */
	public void resetId() {
		//メルマガ送信対象顧客IDをクリア
		customerIdList = null;
	}

	/**
	 * 検索リセットする
	 */
	public void resetSearch() {
		//メルマガ送信対象顧客IDをクリア
		customerIdList = null;
		where_mailMagazineAreaCdList = new ArrayList<String>();
		where_shopListPrefecturesCdList = new ArrayList<String>();
		where_shopListShutokenForeignAreaKbnList = new ArrayList<String>();
		where_shopListIndustryKbnList = new ArrayList<String>();
	}
}