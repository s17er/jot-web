package com.gourmetcaree.admin.pc.shopList.action.shopList;

import java.io.IOException;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.service.logic.ShopListReportCsvLogic;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.db.common.exception.WNoResultException;

@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN})
public class ShopListCsvReportAction extends PcAdminAction {

	/** ログ */
	private static final Logger log = Logger.getLogger(ShopListCsvReportAction.class);

	/** CSVロジック */
	@Resource
	private ShopListReportCsvLogic shopListReportCsvLogic;

	/**
	 * ダウンロード
	 * @return なし
	 */
	@Execute(validator = false)
	public String index() {
		try {
			shopListReportCsvLogic.outputReportCsv();
		} catch (WNoResultException e) {
			log.warn("検索結果が見つかりませんでした。", e);
		} catch (IOException e) {
			log.warn("ファイルの操作時にエラーが発生しました。", e);
		}
		return null;
	}

}
