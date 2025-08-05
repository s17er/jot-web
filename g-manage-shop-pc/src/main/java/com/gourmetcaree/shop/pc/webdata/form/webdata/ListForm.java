package com.gourmetcaree.shop.pc.webdata.form.webdata;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.math.NumberUtils;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.db.common.service.WebIpPhoneHistoryService.SearchProperty;
import com.gourmetcaree.shop.pc.webdata.dto.webdata.ListDto;

/**
 * 求人原稿一覧フォームです。
 * @author Takahiro Kimura
 * @version 1.0
 */
@Component(instance=InstanceType.SESSION)
public class ListForm extends WebdataForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 3285488131598023615L;

	/** ページ遷移用に選択されたページ数 */
	public String pageNum;

	/** 最大表示件数 */
	public String maxRow;

	/** 求人原稿一覧のリスト */
	public List<ListDto> list;

	public Date postStartDatetime;

	public Date postEndDatetime;

	/** webID */
	public String webId;

	public String where_displayCount;

	public String[] where_webStatus;

	public String[] where_areaCd;

	public String[] where_sizeKbn;

	public String where_displayOrder;

	/**
	 * リセットを行う
	 */
	public void resetForm() {
		super.resetForm();
		pageNum = null;
		maxRow = null;
		list = null;
		webId = null;
		postStartDatetime = null;
		postEndDatetime = null;
		where_displayCount = null;
		where_webStatus = null;
		where_areaCd = null;
		where_sizeKbn = null;
		where_displayOrder = null;
	}

	public void resetWebdata() {
		webId = null;
		postStartDatetime = null;
		postEndDatetime = null;
	}

	/**
	 * 最大表示件数のint値を作成
	 */
	private int createMaxRow() {
		return NumberUtils.toInt(maxRow, GourmetCareeConstants.DEFAULT_MAX_ROW_INT);
	}

	/**
	 * ページ番号のint値を作成
	 */
	private int createPageNum() {
		return NumberUtils.toInt(pageNum, GourmetCareeConstants.DEFAULT_PAGE);
	}

	/**
	 * 検索プロパティを作成
	 */
	public SearchProperty createSearchProperty(int webId) {
		SearchProperty property = new SearchProperty();
		property.webId = webId;
		property.maxRow = createMaxRow();
		property.targetPage = createPageNum();
		property.sort = where_displayOrder;
		return property;
	}

}

