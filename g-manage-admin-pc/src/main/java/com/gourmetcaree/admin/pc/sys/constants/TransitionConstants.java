package com.gourmetcaree.admin.pc.sys.constants;

/**
 * 遷移先の定義
 * @author Takahiro Ando
 * @version 1.0
 */
public interface TransitionConstants {

	/** セッションタイムアウト */
	public static final String JSP_SESSION_TIMEOUT = "/error/sessionTimeout.jsp";

	/** アクセスエラー*/
	public static final String JSP_AUTHORIZATION_ERROR = "/error/authorizationError.jsp";

	/** データ不整合エラー */
	public static final String JSP_INCONSISTENT_DATA_ERROR = "/error/inconsistentDataError.jsp";

	/** 不正なプロセスエラー */
	public static final String JSP_FRAUDULEN_PROCESS_ERROR = "/error/fraudulentProcessError.jsp";

	/** メールが存在しないエラー */
	public static final String JSP_MAIL_NOT_FOUND_ERROR = "/error/mailNotFoundError.jsp";

	/** ページが存在しないエラー */
	public static final String JSP_PAGE_NOT_FOUND_ERROR = "/error/pageNotFoundError.jsp";

	/** プレビュー表示時の入力チェックなどの表示用エラー画面 */
	public static final String JSP_PREVIEW_ERROR = "/error/previewError.jsp";

	/** ログイン */
	public static final String FWD_LOGIN = "/login/login";

	/** リダイレクト文字列 */
	public static final String REDIRECT_STR = "?redirect=true";


	/**
	 * ログインの画面IDを保持するクラス
	 * @author Makoto Otani
	 */
	public static final class Login{
		/** ログイン画面へリダイレクト */
		public static final String REDIRECT_LOGIN_LOGIN = "/login/login/?redirect=true";
		/** ログイン */
		public static final String  JSP_APA01S01 = "/login/apA01S01.jsp";
	}

	/**
	トップメニューの画面IDを保持するクラス
	 * @author Makoto Otani
	 */
	public static final class Top{
		/** トップメニューへリダイレクト */
		public static final String REDIRECT_TOP_MENU = "/top/menu/?redirect=true";
		/** 管理メニュー */
		public static final String  JSP_APB01M01 = "/top/apB01M01.jsp";
	}

	/**
	 * webデータ管理の画面IDを保持するクラス
	 * @author Makoto Otani
	 */
	public static final class Webdata{

		/** WEBデータ登録 */
		public static final String  JSP_APC01C01 = "/webdata/apC01C01.jsp";
		/** WEBデータ登録確認 */
		public static final String  JSP_APC01C02 = "/webdata/apC01C02.jsp";
		/** WEBデータ登録完了 */
		public static final String  JSP_APC01C03 = "/webdata/apC01C03.jsp";
		/** コピーWEBデータ登録完了 */
		public static final String  JSP_APC01C04 = "/webdata/apC01C04.jsp";
		/** 登録完了メソッドへリダイレクト */
		public static final String  REDIRECT_WEBDATA_INPUT_COMP = "/webdata/input/comp/?redirect=true";
		/** コピーからの登録完了メソッドへリダイレクト */
		public static final String  REDIRECT_WEBDATA_INPUT_FROMCOPYCOMP = "/webdata/input/fromCopyComp/?redirect=true";
		/** WEBデータの路線図削除アクションパス */
		public static final String  ACTION_WEBDATA_INPUT_DELETEASSIGNED = "/webdata/input/deleteAssigned/";
		/** コピー完了メソッドへリダイレクト */
		public static final String  REDIRECT_WEBDATA_INPUT_COPYCOMP = "/webdata/input/copyComp/?redirect=true";

		/** WEBデータ一覧 */
		public static final String  JSP_APC01L01 = "/webdata/apC01L01.jsp";
		/** WEBデータ一覧の初期表示メソッドへリダイレクト */
		public static final String  REDIRECT_WEBDATA_LIST_INDEX = "/webdata/list/index/?redirect=true";
		/** WEBデータ一覧検索メソッドへリダイレクト */
		public static final String  REDIRECT_WEBDATA_LIST_SEARCHAGAIN = "/webdata/list/searchAgain/?redirect=true";
		/** WEBデータ一覧検索メソッドへリダイレクト */
		public static final String  REDIRECT_WEBDATA_LIST_BACKSEARCH = "/webdata/list/backSearch/?redirect=true";
		/** WEBデータ一覧検索メソッドへフォワード */
		public static final String  FWD_WEBDATA_LIST_BACKSEARCH = "/webdata/list/backSearch/";

		/** WEBデータ詳細 */
		public static final String  JSP_APC01R01 = "/webdata/apC01R01.jsp";
		/** WEBデータ詳細の初期表示アクションパス */
		public static final String  ACTION_WEBDATA_DETAIL_INDEX = "/webdata/detail/index/";
		/** WEBデータメーラ起動完了メソッドへリダイレクト */
		public static final String  REDIRECT_WEBDATA_DETAIL_STARTUPMAILERCOMP = "/webdata/detail/startUpMailerComp/?redirect=true";

		/** WEBデータ編集の初期表示アクションパス */
		public static final String  ACTION_WEBDATA_EDIT_INDEX = "/webdata/edit/index/";
		/** WEBデータ編集 */
		public static final String  JSP_APC01E01 = "/webdata/apC01E01.jsp";
		/** WEBデータ編集確認 */
		public static final String  JSP_APC01E02 = "/webdata/apC01E02.jsp";
		/** WEBデータ編集完了 */
		public static final String  JSP_APC01E03 = "/webdata/apC01E03.jsp";
		/** 編集完了メソッドへリダイレクト */
		public static final String  REDIRECT_WEBDATA_EDIT_COMP = "/webdata/edit/comp/?redirect=true";
		/** WEBデータの路線図削除アクションパス */
		public static final String  ACTION_WEBDATA_EDIT_DELETEASSIGNED = "/webdata/edit/deleteAssigned/";

		/** ステータス変更完了 */
		public static final String  JSP_APC01E04 = "/webdata/apC01E04.jsp";
		/** ステータス変更完了メソッドへリダイレクト */
		public static final String  REDIRECT_WEBDATA_EDIT_STATUSCOMP = "/webdata/edit/statusComp/?redirect=true";

		/** WEBデータ削除完了 */
		public static final String  JSP_APC01D03 = "/webdata/apC01D03.jsp";
		/** 削除完了メソッドへリダイレクト */
		public static final String  REDIRECT_WEBDATA_DELETE_COMP = "/webdata/delete/comp/?redirect=true";


		/**テストデータ登録の初期表示アクションパス */
		public static final String  ACTION_APPTEST_INPUT_INDEX = "/appTest/input/index/";
		/** 応募テストデータ登録 */
		public static final String  JSP_APC02C01 = "/webdata/apC02C01.jsp";
		/** 応募テストデータ登録確認 */
		public static final String  JSP_APC02C02 = "/webdata/apC02C02.jsp";
		/** 応募テストデータ登録完了 */
		public static final String  JSP_APC02C03 = "/webdata/apC02C03.jsp";
		/** 応募テスト完了メソッドへリダイレクト */
		public static final String  REDIRECT_APPTEST_INPUT_COMP = "/appTest/input/comp/?redirect=true";

		/** WEBデータ一括コピーINDEXメソッドへリダイレクト */
		public static final String REDIRECT_LUMPCOPY_INPUT_INDEX = "/lumpCopy/input/index/?redirect=true";
		/** WEBデータ一括コピー */
		public static final String  JSP_APC03C01 = "/webdata/apC03C01.jsp";
		/** WEBデータ一括コピー 再表示パス */
		public static final String PATH_LUMP_COPY_INDEX_AGAIN = "/lumpCopy/input/indexAgain";
		/** WEBデータ一括コピー確認 */
		public static final String  JSP_APC03C02 = "/webdata/apC03C02.jsp";
		/** WEBデータ一括コピー完了 */
		public static final String  JSP_APC03C03 = "/webdata/apC03C03.jsp";
		/** 一括コピー完了メソッドへリダイレクト */
		public static final String  REDIRECT_LUMPCOPY_INPUT_COMP = "/lumpCopy/input/comp/?redirect=true";

		/** WEBデータ一括確定INDEXメソッドへリダイレクト */
		public static final String REDIRECT_LUMPDECIDE_EDIT_INDEX = "/lumpDecide/edit/index/?redirect=true";
		/** WEBデータ一括確定確認 */
		public static final String  JSP_APC04E02 = "/webdata/apC04E02.jsp";
		/** WEBデータ一括確定完了 */
		public static final String  JSP_APC04E03 = "/webdata/apC04E03.jsp";
		/** 一括確定完了メソッドへリダイレクト */
		public static final String  REDIRECT_LUMPDECIDE_EDIT_COMP = "/lumpDecide/edit/comp/?redirect=true";

		/** WEBデータ一括削除INDEXメソッドへリダイレクト */
		public static final String REDIRECT_LUMPDELETE_DELETE_INDEX = "/lumpDelete/delete/index/?redirect=true";
		/** WEBデータ一括削除確認 */
		public static final String  JSP_APC05D02 = "/webdata/apC05D02.jsp";
		/** WEBデータ一括削除完了 */
		public static final String  JSP_APC05D03 = "/webdata/apC05D03.jsp";
		/** 一括削除完了メソッドへリダイレクト */
		public static final String  REDIRECT_LUMPDELETE_DELETE_COMP = "/lumpDelete/delete/comp/?redirect=true";

	}

	/**
	 * 顧客管理の画面IDを保持するクラス
	 * @author Makoto Otani
	 */
	public static final class Customer{

		/** 顧客データ登録 */
		public static final String  JSP_APD01C01 = "/customer/apD01C01.jsp";
		/** 顧客データ登録確認 */
		public static final String  JSP_APD01C02 = "/customer/apD01C02.jsp";
		/** 顧客データ登録完了 */
		public static final String  JSP_APD01C03 = "/customer/apD01C03.jsp";
		/** 登録完了メソッドへリダイレクト */
		public static final String  REDIRECT_CUSTOMER_INPUT_COMP = "/customer/input/comp/?redirect=true";

		/** 顧客データ一覧 */
		public static final String  JSP_APD01L01 = "/customer/apD01L01.jsp";
		/** 顧客データ一覧へリダイレクト */
		public static final String REDIRECT_CUSTOMER_SEARCH_AGAIN = "/customer/list/searchAgain/?redirect=true";
		/** メルマガ確認から顧客データ一覧へリダイレクト */
		public static final String REDIRECT_CUSTOMER_BACKMAILMAG = "/customer/list/backMailMag/?redirect=true";

		/** 顧客データ詳細 */
		public static final String  JSP_APD01R01 = "/customer/apD01R01.jsp";
		public static final String  REDIRECT_CUSTOMER_DETAIL_INDEX = "/customer/detail/index/";

		/** 顧客データ編集 */
		public static final String  JSP_APD01E01 = "/customer/apD01E01.jsp";
		/** 顧客データ編集確認 */
		public static final String  JSP_APD01E02 = "/customer/apD01E02.jsp";
		/** 顧客データ編集完了 */
		public static final String  JSP_APD01E03 = "/customer/apD01E03.jsp";
		/** 編集完了メソッドへリダイレクト */
		public static final String  REDIRECT_CUSTOMER_EDIT_COMP = "/customer/edit/comp/?redirect=true";
		/** 編集完了メソッド */
		public static final String  CUSTOMER_EDIT_COMP = "/customer/edit/comp/";
		/** 顧客データ一覧検索メソッドへリダイレクト */
		public static final String  REDIRECT_CUSTOMER_LIST_SEARCHAGAIN = "/customer/list/searchAgain/?redirect=true";

		/** 顧客データ削除完了 */
		public static final String  JSP_APD01D03 = "/customer/apD01D03.jsp";
		/** 削除完了メソッドへリダイレクト */
		public static final String  REDIRECT_CUSTOMER_DELETE_COMP = "/customer/delete/comp/?redirect=true";

		/** 顧客向けメルマガ配信先確認初期表示メソッドへリダイレクト */
		public static final String  CUSTOMERMAILMAG_LIST_INDEX = "/customerMailMag/list/";
		/** 顧客向けメルマガ配信先確認 */
		public static final String  JSP_APD02L02 = "/customer/apD02L02.jsp";
		/** メルマガ入力から顧客向けメルマガ配信先確認へリダイレクト */
		public static final String REDIRECT_CUSTOMERMAILMAG_BACKMAILMAG = "/customerMailMag/list/backMailMag/?redirect=true";

		/** 顧客向けメルマガ登録初期表示メソッドへリダイレクト */
		public static final String  CUSTOMERMAILMAG_INPUT_INDEX = "/customerMailMag/input/";
		/** 顧客向けメルマガ登録 */
		public static final String  JSP_APD02C01 = "/customer/apD02C01.jsp";
		/** 顧客向けメルマガ登録確認 */
		public static final String  JSP_APD02C02 = "/customer/apD02C02.jsp";
		/** 顧客向けメルマガ登録完了 */
		public static final String  JSP_APD02C03 = "/customer/apD02C03.jsp";
		/** メルマガ登録完了メソッドへリダイレクト */
		public static final String  REDIRECT_CUSTOMERMAILMAG_INUT_COMP = "/customerMailMag/input/comp/?redirect=true";

		public static final String  CUSTOMER_IMAGE_BACK_TO_NULL = "/customerImage/list/backToNull";
		/** 顧客画像一覧 */
		public static final String  JSP_APD03L01 = "/customer/apD03L01.jsp";
	}

	/**
	 * 店舗一覧の画面IDを保持するクラス
	 * @author Takehiro Nakamori
	 *
	 */
	public static final class ShopList {
		/** 店舗一覧インデックス */
		public static final String JSP_APQ01A01 = "/shopList/apQ01A01.jsp";
		/** 店舗一覧登録画面 */
		public static final String JSP_APQ01C01 = "/shopList/apQ01C01.jsp";
		/** 店舗一覧確認画面 */
		public static final String JSP_APQ01C02 = "/shopList/apQ01C02.jsp";
		/** 店舗一覧完了画面 */
		public static final String JSP_APQ01C03 = "/shopList/apQ01C03.jsp";
		/** 店舗一覧完了画面へリダイレクト */
		public static final String REDIRECT_SHOPLIST_INPUT_COMP = "/shopList/input/comp/?redirect=true";
		/** 店舗一覧インデックスのパス */
		public static final String ACTION_SHOPLIST_INDEX = "/shopList/index";
		/** 店舗一覧のWEBデータからのパス */
		public static final String ACTION_SHOPLIST_INDEX_INDEXWEBDATAIL = "/shopList/index/indexWebDatail/%s/%s";

		/** 店舗一覧詳細画面 */
		public static final String JSP_APQ03R01 = "/shopList/apQ03R01.jsp";
		/** 店舗一覧詳細 削除画面 */
		public static final String JSP_APQ03D01 = "/shopList/apQ03D01.jsp";
		/** 店舗一覧詳細 削除完了画面へのリダイレクト */
		public static final String REDIRECT_SHOPLIST_DETAIL_DELETE_COMP = "/shopList/detail/deleteComp/?redirect=true";
		/** 店舗一覧詳細画面のパス */
		public static final String ACTION_SHOPLIST_DETAIL = "/shopList/detail";

		/** 店舗一覧編集画面 インデックス */
		public static final String JSP_APQ04E01 = "/shopList/apQ04E01.jsp";
		/** 店舗一覧編集画面 確認 */
		public static final String JSP_APQ04E02 = "/shopList/apQ04E02.jsp";
		/** 店舗一覧編集画面 完了 */
		public static final String JSP_APQ04E03 = "/shopList/apQ04E03.jsp";
		/** 店舗一覧編集完了画面へリダイレクト */
		public static final String REDIRECT_SHOPLIST_EDIT_COMP = "/shopList/edit/comp/?redirect=true";

		public static final String  ACTION_INPUT_BACK = "/shopList/input/back";

		public static final String  ACTION_EDIT_BACK = "/shopList/edit/backTo";

		/** 店舗一覧検索画面 */
		public static final String JSP_APQ02L01 = "/shopList/apQ02L01.jsp";
		/** 店舗一覧検索画面リスト */
		public static final String ACTION_SHOPLIST_LIST = "/shopList/list/";

		public static final String FWD_SHOPLIST_LIST_RESHOW = "/shopList/list/reShowList";

		/** 店舗一覧CSV入力画面リスト */
		public static final String JSP_APQ05L01 = "/shopList/apQ05L01.jsp";
		/** 店舗一覧CSV編集画面 編集 */
		public static final String JSP_APQ05E01 = "/shopList/apQ05E01.jsp";
		/** 店舗一覧CSV編集画面 確認 */
		public static final String JSP_APQ05E02 = "/shopList/apQ05E02.jsp";
		/** 店舗一覧CSV完了画面 */
		public static final String JSP_APQ05C03 = "/shopList/apQ05C03.jsp";
		/** 店舗一覧CSV完了画面へリダイレクト */
		public static final String REDIRECT_SHOPLIST_CSV_COMPLETE = "/shopList/inputCsv/comp?redirect=true";
		/** 店舗一覧CSVリストへリダイレクト */
		public static final String REDIRECT_SHOPLIST_CSV_BACK_TO_LIST = "/shopList/inputCsv/backToList?redirect=true";

		public static final String  ACTION_CSV_EDIT_BACK = "/shopList/inputCsv/backToEdit";

		/** 店舗一覧CSV入力アクションのパス */
		public static final String ACTION_SHOPLIST_INPUTCSV = "/shopList/inputCsv";

		public static final String ACTION_SHOPLIST_INPUTJOBCSV = "/shopList/inputJobCsv";

		/** 系列店舗一覧用ラベルの登録画面 */
		public static final String JSP_APQ06C01 = "/shopList/apQ06C01.jsp";
		/** 系列店舗一覧用ラベルの登録確認画面 */
		public static final String JSP_APQ06C02 = "/shopList/apQ06C02.jsp";
		/** 系列店舗一覧用ラベルの登録完了画面 */
		public static final String JSP_APQ06C03 = "/shopList/apQ06C03.jsp";
		/** 系列店舗一覧用ラベルの一覧画面 */
		public static final String JSP_APQ06L01 = "/shopList/apQ06L01.jsp";
		/** 系列店舗一覧用ラベルの変更画面 */
		public static final String JSP_APQ06E01 = "/shopList/apQ06E01.jsp";
		/** 系列店舗一覧用ラベルの変更確認画面 */
		public static final String JSP_APQ06E02 = "/shopList/apQ06E02.jsp";
		/** 系列店舗一覧用ラベルの変更完了画面 */
		public static final String JSP_APQ06E03 = "/shopList/apQ06E03.jsp";
		/** 系列店舗一覧用ラベルの削除完了画面 */
		public static final String JSP_APQ06D03 = "/shopList/apQ06D03.jsp";
		/** 系列店舗一覧用ラベル登録完了画面へリダイレクト */
		public static final String REDIRECT_SHOPLIST_LABEL_INPUT_COMP = "/shopLabel/input/comp/?redirect=true";
		/** 系列店舗一覧用ラベル削除完了画面へリダイレクト */
		public static final String REDIRECT_SHOPLIST_LABEL_DELETE_COMP = "/shopLabel/list/deleteComp/?redirect=true";
		/** 系列店舗一覧用ラベル編集完了画面へリダイレクト */
		public static final String REDIRECT_SHOPLIST_LABEL_EDIT_COMP = "/shopLabel/edit/updateComp/?redirect=true";

		/** 店舗一覧職種CSV入力画面リスト */
		public static final String JSP_APQ07L01 = "/shopList/apQ07L01.jsp";
		/** 店舗一覧職種CSV完了画面 */
		public static final String JSP_APQ07C03 = "/shopList/apQ07C03.jsp";

		/** 店舗一覧職種CSV完了画面へリダイレクト */
		public static final String REDIRECT_SHOPLIST_JOB_CSV_COMPLETE = "/shopList/inputJobCsv/comp?redirect=true";
	}

	/**
	 * GCWコード管理の画面IDを保持するクラス
	 * @author Makoto Otani
	 */
	public static final class Gcw{

		/** GCWコード登録 */
		public static final String  JSP_APE01C01 = "/gcw/apE01C01.jsp";
		/** GCWコード登録確認 */
		public static final String  JSP_APE01C02 = "/gcw/apE01C02.jsp";
		/** GCWコード登録完了 */
		public static final String  JSP_APE01C03 = "/gcw/apE01C03.jsp";
		/** 登録完了メソッドへリダイレクト */
		public static final String  REDIRECT_GCW_INPUT_COMP = "/gcw/input/comp/?redirect=true";

		/** GCWコード一覧 */
		public static final String  JSP_APE01L01 = "/gcw/apE01L01.jsp";
		/** GCWコード一覧へリダイレクト */
		public static final String REDIRECT_GCW_SEARCH_AGAIN = "/gcw/list/searchAgain/?redirect=true";

		/** GCWコード編集 */
		public static final String  JSP_APE01E01 = "/gcw/apE01E01.jsp";
		/** GCWコード編集確認 */
		public static final String  JSP_APE01E02 = "/gcw/apE01E02.jsp";
		/** GCWコード編集完了 */
		public static final String  JSP_APE01E03 = "/gcw/apE01E03.jsp";
		/** 編集完了メソッドへリダイレクト */
		public static final String  REDIRECT_GCW_EDIT_COMP = "/gcw/edit/comp/?redirect=true";

		/** GCWコード削除確認 */
		public static final String  JSP_APE01D02 = "/gcw/apE01D02.jsp";
		/** GCWコード削除完了 */
		public static final String  JSP_APE01D03 = "/gcw/apE01D03.jsp";
		/** 削除完了メソッドへリダイレクト */
		public static final String  REDIRECT_GCW_DELETE_COMP = "/gcw/delete/comp/?redirect=true";

	}

//	/**
//	 * 誌面データ移行の画面IDを保持するクラス
//	 * @author Makoto Otani
//	 */
//	public static final class MagazineImport{
//
//		/** 誌面データ移行 */
//		public static final String  JSP_APF01A01 = "/magazineImport/apF01A01.jsp";
//		/** 誌面データ移行待ち */
//		public static final String  JSP_APF01A02 = "/magazineImport/apF01A02.jsp";
//		/** 誌面データ移行完了 */
//		public static final String  JSP_APF01A03 = "/magazineImport/apF01A03.jsp";
//		/** 移行完了メソッドへリダイレクト */
//		public static final String  REDIRECT_MAGAZINEIMPORT_IMPORT_COMP = "/magazineImport/import/comp/?redirect=true";
//
//	}

	/**
	 * レポート管理の画面IDを保持するクラス
	 * @author Makoto Otani
	 */
	public static final class Report{

		/** レポート一覧 */
		public static final String  JSP_APG01L01 = "/report/apG01L01.jsp";

		/** レポート詳細 */
		public static final String  JSP_APG01R01 = "/report/apG01R01.jsp";

		/** レポート一覧の再表示へのリダイレクト */
		public static final String  REDIRECT_REPORT_LIST_AGAIN = "/report/list/?showList=+&redirect=true";

	}

	/**
	 * 会員データ管理の画面IDを保持するクラス
	 * @author Makoto Otani
	 */
	public static final class Member{

		/** 会員データ一覧 */
		public static final String  JSP_APH01L01 = "/member/apH01L01.jsp";
		/** 会員データ一覧へリダイレクト */
		public static final String REDIRECT_MEMBER_SEARCH_AGAIN = "/member/list/searchAgain/?redirect=true";

		/** 会員データ詳細 */
		public static final String  JSP_APH01R01 = "/member/apH01R01.jsp";
		/** 会員データ詳細へリダイレクト */
		public static final String  REDIRECT_MEMBER_DETAIL_INDEX = "/member/detail/index/";


		/** 会員データ編集 */
		public static final String  JSP_APH01E01 = "/member/apH01E01.jsp";
		/** 会員データ編集確認 */
		public static final String  JSP_APH01E02 = "/member/apH01E02.jsp";
		/** 会員データ編集完了 */
		public static final String  JSP_APH01E03 = "/member/apH01E03.jsp";
		/** 編集完了メソッドへリダイレクト */
		public static final String  REDIRECT_MEMBER_EDIT_COMP = "/member/edit/comp/?redirect=true";

		/** 会員データ削除 */
		public static final String  JSP_APH01D01 = "/member/apH01D01.jsp";
		/** 会員データ削除再表示パス */
		public static final String MEMBER_DELETE_INDEX_AGAIN = "/member/delete/indexAgain";
		/** 会員データ削除完了 */
		public static final String  JSP_APH01D03 = "/member/apH01D03.jsp";
		/** 削除完了メソッドへリダイレクト */
		public static final String  REDIRECT_MEMBER_DELETE_COMP = "/member/delete/comp/?redirect=true";

		/** 会員向けメルマガ登録初期表示メソッド */
		public static final String  MEMBERMAILMAG_INPUT_INDEX = "/memberMailMag/input/";
		/** 会員向けメルマガ登録 */
		public static final String  JSP_APH02C01 = "/member/apH02C01.jsp";
		/** 会員向けメルマガ登録確認 */
		public static final String  JSP_APH02C02 = "/member/apH02C02.jsp";
		/** 会員向けメルマガ登録完了 */
		public static final String  JSP_APH02C03 = "/member/apH02C03.jsp";
		/** メルマガ登録完了メソッドへリダイレクト */
		public static final String  REDIRECT_MEMBERMAILMAG_INPUT_COMP = "/memberMailMag/input/comp/?redirect=true";





		/** 仮会員データ一覧 */
		public static final String  JSP_APH03L01 = "/member/apH03L01.jsp";

		/** 仮会員データ一覧へリダイレクト */
		public static final String REDIRECT_TEMP_MEMBER_SEARCH_AGAIN = "/tempMember/list/searchAgain/?redirect=true";

		/** 仮会員データ詳細 */
		public static final String  JSP_APH03R01 = "/member/apH03R01.jsp";


		/** 仮会員データ編集 */
		public static final String  JSP_APH03E01 = "/member/apH03E01.jsp";
		/** 仮会員データ編集確認 */
		public static final String  JSP_APH03E02 = "/member/apH03E02.jsp";
		/** 仮会員データ編集完了 */
		public static final String  JSP_APH03E03 = "/member/apH03E03.jsp";
		/** 仮会員編集完了メソッドへリダイレクト */
		public static final String  REDIRECT_TEMP_MEMBER_EDIT_COMP = "/tempMember/edit/comp/?redirect=true";

		/** 仮会員データ削除完了 */
		public static final String  JSP_APH03D03 = "/member/apH03D03.jsp";
		/** 仮会員編集完了メソッドへリダイレクト */
		public static final String  REDIRECT_TEMP_MEMBER_DELETE_COMP = "/tempMember/detail/compDelete/?redirect=true";

		/** 会員登録完了 */
		public static final String JSP_APH03C01 = "/member/apH03C01.jsp";

	}

	/**
	 * ジャスキルデータ管理の画面IDを保持するクラス
	 * @author Makoto Otani
	 *
	 */
	public static final class Juskill {

		/** ジャスキル会員データ一覧 */
		public static final String  JSP_APQ01L01 = "/juskill/apQ01L01.jsp";
		/** ジャスキル会員データ一覧へリダイレクト */
		public static final String REDIRECT_JUSKILL_SEARCH_AGAIN = "/juskillMember/list/searchAgain/?redirect=true";

		/** ジャスキル会員データ詳細 */
		public static final String  JSP_APQ01R01 = "/juskill/apQ01R01.jsp";
		/** ジャスキル会員データ詳細へリダイレクト */
		public static final String  REDIRECT_JUSKILL_DETAIL_INDEX = "/juskillMember/detail/index/";


		/** ジャスキル会員データ編集 */
		public static final String  JSP_APQ01E01 = "/juskill/apQ01E01.jsp";
		/** ジャスキル会員データ編集確認 */
		public static final String  JSP_APQ01E02 = "/juskill/apQ01E02.jsp";
		/** ジャスキル会員データ編集完了 */
		public static final String  JSP_APQ01E03 = "/juskill/apQ01E03.jsp";
		/** 編集完了メソッドへリダイレクト */
		public static final String  REDIRECT_JUSKILL_EDIT_COMP = "/juskillMember/edit/comp/?redirect=true";

		/** ジャスキル会員データ削除完了 */
		public static final String  JSP_APQ01D03 = "/juskill/apQ01D03.jsp";
		/** 削除完了メソッドへリダイレクト */
		public static final String  REDIRECT_JUSKILL_DELETE_COMP = "/juskillMember/delete/comp/?redirect=true";


		/** ジャスキル会員向けメルマガ登録初期表示メソッド */
		public static final String  JUSKILL_MEMBERMAILMAG_INPUT_INDEX = "/juskillMailMag/input/";
		/** ジャスキル会員向けメルマガ登録 */
		public static final String  JSP_APQ02C01 = "/juskill/apQ02C01.jsp";
		/** ジャスキル会員向けメルマガ登録確認 */
		public static final String  JSP_APQ02C02 = "/juskill/apQ02C02.jsp";
		/** ジャスキル会員向けメルマガ登録完了 */
		public static final String  JSP_APQ02C03 = "/juskill/apQ02C03.jsp";
		/** メルマガ登録完了メソッドへリダイレクト */
		public static final String  REDIRECT_JUSKILLMAILMAG_INPUT_COMP = "/juskillMailMag/input/comp/?redirect=true";

	}

	public static final class AdvancedRegistration {
		/** 事前登録会員データ一覧 */
		public static final String JSP_APH01L01 = "/advancedRegistration/apH01L01.jsp";

		/** 事前登録会員データ一覧へリダイレクト */
		public static final String REDIRECT_ADVANCED_MEMBER_SEARCH_AGAIN = "/advancedRegistrationMember/list/searchAgain/?redirect=true";

		/** 事前登録会員データ詳細 */
		public static final String JSP_APH01R01 = "/advancedRegistration/apH01R01.jsp";

		public static final String REDIRECT_ADVANCED_MEMBER_PRINT_OUT = "/advancedRegistrationMember/print/index";


		/** 事前登録会員印刷一覧 */
		public static final String JSP_APH02L01 ="/advancedRegistration/apH02L01.jsp";

		/** 事前登録会員向けメルマガ登録 */
		public static final String  JSP_APH03C01 = "/advancedRegistration/apH03C01.jsp";
		/** 事前登録会員向けメルマガ登録確認 */
		public static final String  JSP_APH03C02 = "/advancedRegistration/apH03C02.jsp";
		/** 事前登録会員向けメルマガ登録完了 */
		public static final String  JSP_APH03C03 = "/advancedRegistration/apH03C03.jsp";

		/** メルマガ登録完了メソッドへリダイレクト */
		public static final String  REDIRECT_ADVANCEDMEMBERMAILMAG_INPUT_COMP = "/advancedRegistrationMemberMailMag/input/comp/?redirect=true";
	}

	/**
	 * 応募データ管理の画面IDを保持するクラス
	 * @author Makoto Otani
	 */
	public static final class Application{

		/** 応募データ一覧 */
		public static final String  JSP_API01L01 = "/application/apI01L01.jsp";
		/** 応募データ一覧へリダイレクト */
		public static final String REDIRECT_APPLICATION_SEARCH_AGAIN = "/application/list/searchAgain/?redirect=true";

		/** 応募データ詳細 */
		public static final String  JSP_API01R01 = "/application/apI01R01.jsp";

		/** 応募データ一覧 */
		public static final String  JSP_API02L01 = "/application/apI02L01.jsp";
		/** 応募データ一覧へリダイレクト */
		public static final String REDIRECT_OBSERVATE_APPLICATION_SEARCH_AGAIN = "/observateApplication/list/searchAgain/?redirect=true";

		/** 応募データ詳細 */
		public static final String  JSP_API02R01 = "/application/apI02R01.jsp";


		/** グルメdeバイト応募一覧 */
		public static final String JSP_API03L01 = "/application/apI03L01.jsp";

		/** グルメdeバイト詳細 */
		public static final String JSP_API03R01 = "/application/apI03R01.jsp";

		/** 応募データ一覧へリダイレクト */
		public static final String REDIRECT_ARBEIT_APPLICATION_SEARCH_AGAIN = "/arbeitApplication/list/searchAgain/?redirect=true";

	}

	/**
	 * マスタメンテナンスの画面IDを保持するクラス
	 * @author Makoto Otani
	 */
	public static final class Maintenance{

		/** マスタメンテナンスメニュー */
		public static final String  JSP_APJ01M01 = "/maintenance/apJ01M01.jsp";
		/** マスタメンテナンスメニューへリダイレクト */
		public static final String REDIRECT_MAINTENANCE_MENU = "/maintenance/menu/?redirect=true";

		/** 号数データ登録 */
		public static final String  JSP_APJ02C01 = "/maintenance/apJ02C01.jsp";
		/** 号数データ登録確認 */
		public static final String  JSP_APJ02C02 = "/maintenance/apJ02C02.jsp";
		/** 号数データ登録完了 */
		public static final String  JSP_APJ02C03 = "/maintenance/apJ02C03.jsp";
		/** 号数登録完了メソッドへリダイレクト */
		public static final String  REDIRECT_VOLUME_INPUT_COMP = "/volume/input/comp/?redirect=true";

		/** 号数データ一覧 */
		public static final String  JSP_APJ02L01 = "/maintenance/apJ02L01.jsp";
		/** 号数一覧初期表示メソッドへリダイレクト */
		public static final String  REDIRECT_VOLUME_LIST_SHOWLIST = "/volume/list/showList/?redirect=true";

		/** 号数編集の初期表示アクションパス */
		public static final String  ACTION_VOLUME_EDIT_INDEX = "/volume/edit/index/";
		/** 号数データ編集 */
		public static final String  JSP_APJ02E01 = "/maintenance/apJ02E01.jsp";
		/** 号数データ編集確認 */
		public static final String  JSP_APJ02E02 = "/maintenance/apJ02E02.jsp";
		/** 号数データ編集完了 */
		public static final String  JSP_APJ02E03 = "/maintenance/apJ02E03.jsp";
		/** 号数編集完了メソッドへリダイレクト */
		public static final String  REDIRECT_VOLUME_EDIT_COMP = "/volume/edit/comp/?redirect=true";

		/** 号数削除の初期表示アクションパス */
		public static final String  ACTION_VOLUME_DELETE_INDEX = "/volume/delete/index/";
		/** 号数データ削除確認 */
		public static final String  JSP_APJ02D02 = "/maintenance/apJ02D02.jsp";
		/** 号数データ削除完了 */
		public static final String  JSP_APJ02D03 = "/maintenance/apJ02D03.jsp";
		/** 号数削除完了メソッドへリダイレクト */
		public static final String  REDIRECT_VOLUME_DELETE_COMP = "/volume/delete/comp/?redirect=true";

		/** 特集データ登録 */
		public static final String  JSP_APJ03C01 = "/maintenance/apJ03C01.jsp";
		/** 特集データ登録確認 */
		public static final String  JSP_APJ03C02 = "/maintenance/apJ03C02.jsp";
		/** 特集データ登録完了 */
		public static final String  JSP_APJ03C03 = "/maintenance/apJ03C03.jsp";
		/** 特集登録完了メソッドへリダイレクト */
		public static final String  REDIRECT_SPECIAL_INPUT_COMP = "/special/input/comp/?redirect=true";

		/** 特集データ一覧 */
		public static final String  JSP_APJ03L01 = "/maintenance/apJ03L01.jsp";
		/** 特集一覧初期表示メソッドへリダイレクト */
		public static final String  REDIRECT_SPECIAL_LIST_INDEX = "/special/list/index/?redirect=true";
		/** 特集一覧検索メソッドへリダイレクト */
		public static final String  REDIRECT_SPECIAL_LIST_SEARCH = "/special/list/search/?redirect=true";

		/** 特集編集の初期表示アクションパス */
		public static final String  ACTION_SPECIAL_EDIT_INDEX = "/special/edit/index/";
		/** 特集データ編集 */
		public static final String  JSP_APJ03E01 = "/maintenance/apJ03E01.jsp";
		/** 特集データ編集確認 */
		public static final String  JSP_APJ03E02 = "/maintenance/apJ03E02.jsp";
		/** 特集データ編集完了 */
		public static final String  JSP_APJ03E03 = "/maintenance/apJ03E03.jsp";
		/** 特集編集完了メソッドへリダイレクト */
		public static final String  REDIRECT_SPECIAL_EDIT_COMP = "/special/edit/comp/?redirect=true";

		/** 特集編集の初期表示アクションパス */
		public static final String  ACTION_SPECIAL_DELETE_INDEX = "/special/delete/index/";
		/** 特集データ削除確認 */
		public static final String  JSP_APJ03D02 = "/maintenance/apJ03D02.jsp";
		/** 特集データ削除完了 */
		public static final String  JSP_APJ03D03 = "/maintenance/apJ03D03.jsp";
		/** 特集削除完了メソッドへリダイレクト */
		public static final String  REDIRECT_SPECIAL_DELETE_COMP = "/special/delete/comp/?redirect=true";

		/** 営業担当者登録 */
		public static final String  JSP_APJ04C01 = "/maintenance/apJ04C01.jsp";
		/** 営業担当者登録確認 */
		public static final String  JSP_APJ04C02 = "/maintenance/apJ04C02.jsp";
		/** 営業担当者登録完了 */
		public static final String  JSP_APJ04C03 = "/maintenance/apJ04C03.jsp";
		/** 営業担当者登録完了メソッドへリダイレクト */
		public static final String  REDIRECT_SALES_INPUT_COMP = "/sales/input/comp/?redirect=true";

		/** 営業担当者一覧 */
		public static final String  JSP_APJ04L01 = "/maintenance/apJ04L01.jsp";
		/** 営業担当者一覧へリダイレクト */
		public static final String REDIRECT_SALES_SEARCH_AGAIN = "/sales/list/searchAgain/?redirect=true";

		/** 営業担当者詳細 */
		public static final String  JSP_APJ04R01 = "/maintenance/apJ04R01.jsp";
		/** 営業担当者詳細へリダイレクト */
		public static final String  REDIRECT_SALES_DETAIL_INDEX = "/sales/detail/index/";

		/** 営業担当者編集 */
		public static final String  JSP_APJ04E01 = "/maintenance/apJ04E01.jsp";
		/** 営業担当者編集確認 */
		public static final String  JSP_APJ04E02 = "/maintenance/apJ04E02.jsp";
		/** 営業担当者編集完了 */
		public static final String  JSP_APJ04E03 = "/maintenance/apJ04E03.jsp";
		/** 営業担当者編集完了メソッドへリダイレクト */
		public static final String  REDIRECT_SALES_EDIT_COMP = "/sales/edit/comp/?redirect=true";

		/** 営業担当者削除完了 */
		public static final String  JSP_APJ04D03 = "/maintenance/apJ04D03.jsp";
		/** 営業担当者削除完了メソッドへリダイレクト */
		public static final String  REDIRECT_SALES_DELETE_COMP = "/sales/delete/comp/?redirect=true";

		/** 会社登録 */
		public static final String  JSP_APJ05C01 = "/maintenance/apJ05C01.jsp";
		/** 会社登録確認 */
		public static final String  JSP_APJ05C02 = "/maintenance/apJ05C02.jsp";
		/** 会社登録完了 */
		public static final String  JSP_APJ05C03 = "/maintenance/apJ05C03.jsp";
		/** 会社登録完了メソッドへリダイレクト */
		public static final String  REDIRECT_COMPANY_INPUT_COMP = "/company/input/comp/?redirect=true";

		/** 会社一覧 */
		public static final String  JSP_APJ05L01 = "/maintenance/apJ05L01.jsp";
		/** 会社一覧検索メソッドへリダイレクト */
		public static final String  REDIRECT_COMPANY_LIST_SEARCH = "/company/list/search/?redirect=true";

		/** 会社詳細の初期表示アクションパス */
		public static final String  ACTION_COMPANY_DETAIL_INDEX = "/company/detail/index/";
		/** 会社詳細 */
		public static final String  JSP_APJ05R01 = "/maintenance/apJ05R01.jsp";

		/** 会社編集の初期表示アクションパス */
		public static final String  ACTION_COMPANY_EDIT_INDEX = "/company/edit/index/";
		/** 会社編集 */
		public static final String  JSP_APJ05E01 = "/maintenance/apJ05E01.jsp";
		/** 会社編集確認 */
		public static final String  JSP_APJ05E02 = "/maintenance/apJ05E02.jsp";
		/** 会社編集完了 */
		public static final String  JSP_APJ05E03 = "/maintenance/apJ05E03.jsp";
		/** 会社編集完了メソッドへリダイレクト */
		public static final String  REDIRECT_COMPANY_EDIT_COMP = "/company/edit/comp/?redirect=true";

		/** 会社削除の初期表示アクションパス */
		public static final String  ACTION_COMPANY_DELETE_INDEX = "/volume/delete/submit/?redirect=true";
		/** 会社削除完了 */
		public static final String  JSP_APJ05D03 = "/maintenance/apJ05D03.jsp";
		/** 会社削除完了メソッドへリダイレクト */
		public static final String  REDIRECT_COMPANY_DELETE_COMP = "/company/delete/comp/?redirect=true";


		/** 事前登録開催日の登録画面 */
		public static final String JSP_APJ06C01 = "/maintenance/apJ06C01.jsp";
		/** 事前登録開催日の登録確認画面 */
		public static final String JSP_APJ06C02 = "/maintenance/apJ06C02.jsp";
		/** 事前登録開催日の登録完了画面 */
		public static final String JSP_APJ06C03 = "/maintenance/apJ06C03.jsp";
		/** 事前登録開催日登録完了画面へのリダイレクト */
		public static final String REDIRECT_ADVANCED_REGISTRATION_INPUT_COMP = "/advancedRegistration/input/comp".concat(REDIRECT_STR);


		/** 事前登録開催日の削除画面 */
		public static final String JSP_APJ06D01 = "/maintenance/apJ06D01.jsp";

		/** 事前登録開催日の削除完了画面 */
		public static final String JSP_APJ06D03 = "/maintenance/apJ06D03.jsp";

		/** 事前登録開催日削除完了画面へのリダイレクト */
		public static final String REDIRECT_ADVANCED_REGISTRATION_DELETE_COMP = "/advancedRegistration/delete/comp".concat(REDIRECT_STR);


		/** 事前登録開催日の一覧画面 */
		public static final String JSP_APJ06L01 = "/maintenance/apJ06L01.jsp";

		/** 事前登録開催日一覧画面へのリダイレクト */
		public static final String REDIRECT_ADVANCED_REGISTRATION_LIST = "/advancedRegistration/list".concat(REDIRECT_STR);


		/** 事前登録開催日の編集画面 */
		public static final String JSP_APJ07C01 = "/maintenance/apJ07C01.jsp";
		/** 事前登録開催日の編集確認画面 */
		public static final String JSP_APJ07C02 = "/maintenance/apJ07C02.jsp";
		/** 事前登録開催日の編集完了画面 */
		public static final String JSP_APJ07C03 = "/maintenance/apJ07C03.jsp";
		/** 事前登録開催日変更完了画面へのリダイレクト */
		public static final String REDIRECT_ADVANCED_REGISTRATION_EDIT_COMP = "/advancedRegistration/edit/comp".concat(REDIRECT_STR);

		/** 駅グループ登録 */
		public static final String  JSP_APJ08C01 = "/maintenance/apJ08C01.jsp";
		/** 駅グループ登録確認 */
		public static final String  JSP_APJ08C02 = "/maintenance/apJ08C02.jsp";
		/** 駅グループ登録完了 */
		public static final String  JSP_APJ08C03 = "/maintenance/apJ08C03.jsp";
		/** 駅グループ登録完了メソッドへリダイレクト */
		public static final String  REDIRECT_TERMINAL_INPUT_COMP = "/terminal/input/comp/?redirect=true";

		/** 駅グループ一覧 */
		public static final String  JSP_APJ08L01 = "/maintenance/apJ08L01.jsp";
		/** 駅グループ一覧へリダイレクト */
		public static final String REDIRECT_TERMINAL_LIST = "/terminal/list/?redirect=true";

		/** 駅グループ編集 */
		public static final String  JSP_APJ08E01 = "/maintenance/apJ08E01.jsp";
		/** 駅グループ編集確認 */
		public static final String  JSP_APJ08E02 = "/maintenance/apJ08E02.jsp";
		/** 駅グループ編集完了 */
		public static final String  JSP_APJ08E03 = "/maintenance/apJ08E03.jsp";
		/** 駅グループ編集完了メソッドへリダイレクト */
		public static final String  REDIRECT_TERMINAL_EDIT_COMP = "/terminal/edit/comp/?redirect=true";

		/** 駅グループ削除の初期表示アクションパス */
		public static final String  ACTION_TERMINAL_DELETE_INDEX = "/terminal/delete/submit/?redirect=true";
		/** 駅グループ削除完了 */
		public static final String  JSP_APJ08D03 = "/maintenance/apJ08D03.jsp";
		/** 駅グループ削除完了メソッドへリダイレクト */
		public static final String  REDIRECT_TERMINAL_DELETE_COMP = "/terminal/delete/comp/?redirect=true";

		/** タグ管理一覧 */
		public static final String  JSP_APJ09L01 = "/maintenance/apJ09L01.jsp";

		/** タグ管理編集 */
		public static final String  JSP_APJ09E01 = "/maintenance/apJ09E01.jsp";

		/** いたずら応募条件登録 */
		public static final String JSP_APJ10C01 = "/maintenance/apJ10C01.jsp";
		/** いたずら応募条件確認 */
		public static final String JSP_APJ10C02 = "/maintenance/apJ10C02.jsp";
		/** いたずら応募条件完了 */
		public static final String JSP_APJ10C03 = "/maintenance/apJ10C03.jsp";

		public static final String  REDIRECT_MISCHIEF_INPUT_COMP = "/mischief/input/comp/?redirect=true";
		/** いたずら応募条件一覧 */
		public static final String  JSP_APJ10L01 = "/maintenance/apJ10L01.jsp";

		public static final String  ACTION_MISCHIEF_DETAIL_INDEX = "/mischief/detail/index/";
		/** いたずら応募条件詳細 */
		public static final String  JSP_APJ10R01 = "/maintenance/apJ10R01.jsp";

		public static final String  REDIRECT_MISCHIEF_LIST_SEARCH = "/mischief/list/search/?redirect=true";

		public static final String  ACTION_MISCHIEF_EDIT_INDEX = "/mischief/edit/index/";
		/** いたずら応募条件編集 */
		public static final String JSP_APJ10E01 = "/maintenance/apJ10E01.jsp";
		/** いたずら応募条件編確認 */
		public static final String JSP_APJ10E02 = "/maintenance/apJ10E02.jsp";
		/** いたずら応募条件編完了 */
		public static final String JSP_APJ10E03 = "/maintenance/apJ10E03.jsp";

		public static final String  REDIRECT_MISCHIEF_EDIT_COMP = "/mischief/edit/comp/?redirect=true";

		public static final String  REDIRECT_MISCHIEF_DELETE_COMP = "/mischief/delete/comp/?redirect=true";
		/** いたずら応募条削除完了 */
		public static final String  JSP_APJ10D03 = "/maintenance/apJ10D03.jsp";

	}

	/**
	 * メルマガ管理の画面IDを保持するクラス
	 * @author Makoto Otani
	 */
	public static final class MailMag{

		/** メルマガ初期表示 */
		public static final String JSP_APK01M01 = "/mailMag/apK01M01.jsp";

		/** メルマガ一覧 */
		public static final String  JSP_APK01L01 = "/mailMag/apK01L01.jsp";
		/** メルマガ一覧の再検索メソッドへリダイレクト */
		public static final String REDIRECT_MAILMAG_LIST_SEARCH_AGAIN = "/mailMag/list/searchAgain/?redirect=true";


		/** メルマガ詳細 */
		public static final String  JSP_APK01R01 = "/mailMag/apK01R01.jsp";

		/** メルマガヘッダメッセージ登録入力画面 */
		public static final String JSP_APK02C01 = "/mailMag/apK02C01.jsp";
		/** メルマガヘッダメッセージ登録確認画面 */
		public static final String JSP_APK02C02 = "/mailMag/apK02C02.jsp";
		/** メルマガヘッダメッセージ登録完了画面 */
		public static final String JSP_APK02C03 = "/mailMag/apK02C03.jsp";
		/** メルマガヘッダメッセージ登録完了画面へのリダイレクト */
		public static final String REDIRECT_MAILMAG_HEADER_INPUT_COMP = "/mailMagOption/headerInput/comp/?redirect=true";
		/** メルマガヘッダメッセージ一覧 */
		public static final String JSP_APK02L01 = "/mailMag/apK02L01.jsp";
		/** メルマガヘッダメッセージ一覧へフォワード */
		public static final String FORWORD_HEADER_LIST = "/mailMagOption/list";

		/** メルマガヘッダメッセージ編集画面 */
		public static final String JSP_APK03C01 = "/mailMag/apK03C01.jsp";
		/** メルマガヘッダメッセージ編集確認画面 */
		public static final String JSP_APK03C02 = "/mailMag/apK03C02.jsp";
		/** メルマガヘッダメッセージ編集完了画面 */
		public static final String JSP_APK03C03 = "/mailMag/apK03C03.jsp";
		/** メルマガヘッダメッセージ編集完了画面へのリダイレクト */
		public static final String REDIRECT_MAILMAG_HEADER_EDIT_COMP = "/mailMagOption/headerEdit/comp/?redirect=true";

		/** メルマガヘッダメッセージ詳細画面 */
		public static final String JSP_APK04C02 = "/mailMag/apK04C02.jsp";
		/** メルマガヘッダメッセージ詳細画面へフォワード */
		public static final String ACTION_HEADER_DETAIL_INDEX = "/mailMagOption/headerDetail/index/";

		/** メルマガヘッダメッセージ削除画面 */
		public static final String JSP_APK05C02 = "/mailMag/apK05C02.jsp";
		/** メルマガヘッダメッセージ削除完了画面 */
		public static final String JSP_APK05C03 = "/mailMag/apK05C03.jsp";
		/** メルマガヘッダメッセージ登録完了画面へのリダイレクト */
		public static final String REDIRECT_MAILMAG_HEADER_DELETE_COMP = "/mailMagOption/headerDelete/comp/?redirect=true";
	}


	/**
	 * お知らせ管理の画面IDを保持するクラス
	 * @author Makoto Otani
	 */
	public static final class Information{

		/** お知らせ管理メニュー */
		public static final String  JSP_APL01M01 = "/information/apL01M01.jsp";

		/** お知らせ一覧 */
		public static final String  JSP_APL02L01 = "/information/apL02L01.jsp";

		/** お知らせ編集 */
		public static final String  JSP_APL02E01 = "/information/apL02E01.jsp";
		/** お知らせ編集確認 */
		public static final String  JSP_APL02E02 = "/information/apL02E02.jsp";
		/** お知らせ編集完了 */
		public static final String  JSP_APL02E03 = "/information/apL02E03.jsp";
		/** お知らせ編集完了メソッドへリダイレクト */
		public static final String  REDIRECT_ADMININFO_EDIT_COMP = "/manageInfo/edit/comp/?redirect=true";


	}

	/**
	 * パスワード変更の画面IDを保持するクラス
	 * @author Makoto Otani
	 */
	public static final class ChangePassword{

		/** パスワード編集 */
		public static final String  JSP_APM01E01 = "/changePassword/apM01E01.jsp";
		/** パスワード編集確認 */
		public static final String  JSP_APM01E02 = "/changePassword/apM01E02.jsp";
		/** パスワード編集完了 */
		public static final String  JSP_APM01E03 = "/changePassword/apM01E03.jsp";
		/** 編集完了メソッドへリダイレクト */
		public static final String  REDIRECT_CHANGEPASSWORD_EDIT_COMP = "/changePassword/edit/comp/?redirect=true";

	}
	/**
	 * 検索サブ画面の画面IDを保持するクラス
	 * @author Makoto Otani
	 */
	public static final class SubWindow{

		/** 顧客データ検索 */
		public static final String  JSP_APN01L01 = "/subWindow/apN01L01.jsp";

		/** 駅検索 */
		public static final String  JSP_APN02C01 = "/subWindow/apN02C01.jsp";

		/** 会社検索 */
		public static final String  JSP_APN03C01 = "/subWindow/apN03C01.jsp";

	}

	/**
	 * IP電話系の画面IDを保持するクラス
	 * @author nakamori
	 *
	 */
	public static final class IpPhone {
		// TODO
		public static final String JSP_APR01L01 = "/ipPhone/apR01L01.jsp";
	}

	/**
	 * プレビューの画面IDを保持するクラス
	 * @author Makoto Otani
	 */
	public static final class Preview{

		/** プレビュー画面（一覧）へのフォワード */
		public static final String FORWARD_APO02 = "/listPreview/list/showListPreview/";

		/** プレビュー画面（一覧）へのフォワード */
		public static final String FORWARD_INPUT_PREVIEW_FORMAT = "/listPreview/list/showInputPreview/%s";

		/** 検索プレビューへのリダイレクト */
		public static final String REDIRECT_SEARCH_PREVIEW = "/listPreview/list/previewSearch?redirect=true";

		/** 一覧プレビュー */
		public static final String  JSP_APO02A01 = "/preview/apO02A01.jsp";
		/** 詳細プレビュー（詳細タブ） */
		public static final String  JSP_APO01A01 = "/preview/apO01A01.jsp";
		/** 詳細プレビュー（メッセージタブ） */
		public static final String  JSP_APO01A02 = "/preview/apO01A02.jsp";
		/** 詳細プレビュー（MAPタブ） */
		public static final String  JSP_APO01A03 = "/preview/apO01A03.jsp";
		/** 詳細プレビュー（動画タブ） */
		public static final String  JSP_APO01A04 = "/preview/apO01A04.jsp";
		/** 詳細プレビュー (店舗一覧タブ) */
		public static final String  JSP_APO01A05 = "/preview/apO01A05.jsp";
		/** 詳細プレビュー (店舗一覧処理画面から) */
		public static final String  JSP_APO03A01 = "/preview/apO03A01.jsp";

		/** モバイルプレビュー画面（一覧）へのフォワード */
		public static final String FORWARD_AMO02 = "/listPreview/list/showMobileListPreview/";

		/** モバイルプレビュー画面（一覧）へのフォワード */
		public static final String FORWARD_MOBILE_INPUT_PREVIEW_FORMAT = "/listPreview/list/showMobileInputPreview/%s";

		/** モバイルプレビュー一覧プレビュー */
		public static final String  JSP_FMB01L01 = "/preview/fmB01L01.jsp";

		/** モバイルプレビュー詳細ぺージ１  */
		public static final String JSP_FMB01R01 = "/preview/fmB01R01.jsp";
		/** モバイルプレビュー詳細ぺージ 画像１  */
		public static final String JSP_FMB01R02 = "/preview/fmB01R02.jsp";
		/** モバイルプレビュー詳細ぺージ 画像２  */
		public static final String JSP_FMB01R03 = "/preview/fmB01R03.jsp";
		/** モバイルプレビュー詳細ぺージ 画像３  */
		public static final String JSP_FMB01R04 = "/preview/fmB01R04.jsp";
		/** モバイルプレビュー詳細ぺージ　会社情報  */
		public static final String JSP_FMB01R05 = "/preview/fmB01R05.jsp";
		/** モバイルプレビュー詳細ページ　地図  */
		public static final String JSP_FMB01R06 = "/preview/fmB01R06.jsp";

		/** スマホプレビュー画面（一覧）へのフォワード */
		public static final String FORWARD_FSO01 = "/listPreview/list/showSmartPhoneListPreview/";

		/** スマホプレビュー画面（一覧）へのフォワード */
		public static final String FORWARD_SMARTPHONE_INPUT_PREVIEW_FORMAT = "/listPreview/list/showSmartPhoneInputPreview/%s";

		/** スマホプレビュー一覧ぺージ  */
		public static final String JSP_FSO01L01 = "/preview/fsO01L01.jsp";
		/** スマホプレビュー詳細ぺージ１  */
		public static final String JSP_FSO01R01 = "/preview/fsO01R01.jsp";
		/** スマホプレビュー詳細ぺージ２ */
		public static final String JSP_FSO01R02 = "/preview/fsO01R02.jsp";


		/** スマホ店舗一覧リストプレビュー  */
		public static final String  JSP_FSO01L02 = "/preview/fsO01LA02.jsp";
	}

	/**
	 * Ajax用部品画面の画面IDを保持するクラス
	 * @author Katsutoshi Hasegawa
	 */
	public static final class Ajax{

		/** 号数プルダウン用部品 */
		public static final String  JSP_APP01AV1 = "/ajax/apP01AV1.jsp";
		/** 特集セレクト用部品 */
		public static final String  JSP_APP01AP1 = "/ajax/apP01AP1.jsp";
		/** 特集セレクト用部品(List用) */
		public static final String  JSP_APP01AP2 = "/ajax/apP01AP2.jsp";
		/** 鉄道会社プルダウン用部品 */
		public static final String  JSP_APP01AR1 = "/ajax/apP01AR1.jsp";
		/** 路線プルダウン用部品 */
		public static final String  JSP_APP01AL1 = "/ajax/apP01AL1.jsp";
		/** 駅プルダウン用部品 */
		public static final String  JSP_APP01AS1 = "/ajax/apP01AS1.jsp";
		/** 勤務地エリア(WEBエリアから名称変更)チェックボックス用部品 */
		public static final String  JSP_APP01AW2 = "/ajax/apP01AW2.jsp";
		/** 勤務地エリア(WEBエリアから名称変更)セレクトボックス用部品 */
		public static final String  JSP_APP01AW3 = "/ajax/apP01AW3.jsp";
		/** 勤務地詳細エリアチェックボックス部品 */
		public static final String  JSP_APP01AD2 = "/ajax/apP01AD2.jsp";
		/** 勤務地詳細エリアセレクトボックス部品 */
		public static final String  JSP_APP01AD3 = "/ajax/apP01AD3.jsp";
		/** 主要駅チェックボックス用部品 */
		public static final String  JSP_APP01AM2 = "/ajax/apP01AM2.jsp";
		/** 営業担当者プルダウン用部品 */
		public static final String  JSP_APP01AU1 = "/ajax/apP01AU1.jsp";

		/** 会社プルダウン用部品(検索用) */
		public static final String  JSP_APP02C01 = "/ajax/apP02C01.jsp";
		/** 営業担当者プルダウン用部品(検索用) */
		public static final String  JSP_APP02S01 = "/ajax/apP02S01.jsp";

	}

	/**
	 * 店舗詳細
	 * @author Yamane
	 *
	 */
	public static final class ShopListForSmart {
		public static final String JSP_FSD01R01 = "/preview/fsD01R01.jsp";
	}

	/**
	 * 店舗管理
	 * @author kyamane
	 */
	public static final class ShopMmt {

		public static final String JSP_APQ01L01 = "/shopMmt/apQ01L01.jsp";
	}

	/**
	 * 気になる通知一覧
	 * @author t_shiroumaru
	 *
	 */
	public static final class Interest {

		public static final String JSP_APS01L01 = "/interest/apS01L01.jsp";

		public static final String REDIRECT_INTEREST_SEARCH_AGAIN = "/interest/list/searchAgain/?redirect=true";
	}

	/**
	 * プレ応募
	 * @author t_shiroumaru
	 *
	 */
	public static final class PreApplication {

		public static final String JSP_APT01L01 = "/preApplication/apT01L01.jsp";

		public static final String JSP_APT01R01 = "/preApplication/apT01R01.jsp";

		public static final String REDIRECT_PRE_APPLICATION_SEARCH_AGAIN = "/preApplication/list/searchAgain/?redirect=true";
	}
}

