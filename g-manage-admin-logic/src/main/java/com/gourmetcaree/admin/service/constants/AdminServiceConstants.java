package com.gourmetcaree.admin.service.constants;

/**
 * Adminサービス共通の定数定義です。
 * @author Takahiro Ando
 * @version 1.0
 */
public interface AdminServiceConstants {

	/**
	 * SQLファイルの名称を保持するクラス
	 * @author ando
	 *
	 */
	public static class SqlFileName {
		/** レポート管理一覧用SQL */
		public static final String REPORT_VOLUME_LIST = "com/gourmetcaree/admin/service/sql/reportList.sql";
		/** レポート管理詳細用SQL（営業担当者あり） */
		public static final String REPORT_DETAIL_LIST = "com/gourmetcaree/admin/service/sql/reportDetailList.sql";
		/** レポート管理詳細用SQL（営業担当者なし） */
		public static final String REPORT_DETAIL_NOSALES_LIST = "com/gourmetcaree/admin/service/sql/reportDetailNoSalesList.sql";

	}
}
