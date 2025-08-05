package com.gourmetcaree.shop.pc.sys.constants;

/**
 * 遷移先の定義です。
 * @author Takahiro Kimura
 * @version 1.0
 */
public interface TransitionConstants {

	/** リダイレクト文字列 */
	public static final String REDIRECT_STR = "/?redirect=true";

	/** リダイレクト文字列 */
	public static final String REDIRECT_STR_NO_SLASH = "?redirect=true";

	/**
	 * エラー時の遷移パス
	 * @author Takahiro Kimura
	 * @version 1.0
	 *
	 */
	public static final class ErrorTransition {

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

		/** 有効期限が切れたエラー */
		public static final String JSP_OUT_OF_DATE_ERROR = "/error/outOfDateError.jsp";

		/** プレビュー表示時の入力チェックなどの表示用エラー画面 */
		public static final String JSP_PREVIEW_ERROR = "/error/previewError.jsp";

		/** データ登録エラー */
		public static final String JSP_REGISTERD_DATA_ERROR = "/error/registeredDataError.jsp";
	}

	/**
	 * ログイン機能パス
	 * @author Takahiro Kimura
	 * @version 1.0
	 *
	 */
	public static final class Login {

		/** ログイン */
		public static final String FWD_LOGIN = "/login/login";

		/** ログイン画面JSP */
		public static final String JSP_SPA01S01 = "/login/spA01S01.jsp";

		/** ログイン */
		public static final String REDIRECT_LOGIN = "/login/login/?redirect=true";

	}

	/**
	 * なりすましログイン機能パス
	 * @author Keita Yamane
	 * @version 1.0
	 *
	 */
	public static final class MasqueradeLogin {

		/** ログイン */
		public static final String FWD_LOGIN = "/masquerade/login/login";

		/** ログイン画面JSP */
		public static final String JSP_SPB01S01 = "/masquerade/login/spB01S01.jsp";

		/** ログイン */
		public static final String REDIRECT_LOGIN = "/masquerade/login/login/?redirect=true";

	}


	/**
	 * プレビューの画面IDを保持するクラス
	 * @author Makoto Otani
	 */
	public static final class Preview{

		/** プレビュー画面（一覧）へのフォワード */
		public static final String FORWARD_APO02 = "/listPreview/list/showListPreview/";

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
		/** 詳細プレビュー（店舗一覧） */
		public static final String  JSP_APO01A05 = "/preview/apO01A05.jsp";
		/** 詳細プレビュー (店舗一覧処理画面から) */
		public static final String  JSP_APO03A01 = "/preview/apO03A01.jsp";

		/** モバイルプレビュー画面（一覧）へのフォワード */
		public static final String FORWARD_AMO02 = "/listPreview/list/showMobileListPreview/";

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


		/** スマホプレビュー一覧プレビュー */
		public static final String JSP_FSB01L01 = "/preview/fsB01L01.jsp";
		/** スマホプレビュー詳細ぺージ1  */
		public static final String JSP_FSB01R01 = "/preview/fsB01R01.jsp";
		/** スマホプレビュー詳細ぺージ2  */
		public static final String JSP_FSB01R02 = "/preview/fsB01R02.jsp";
		/** スマホプレビュー店舗詳細ページ  */
		public static final String JSP_FSB02R01 = "/preview/fsB02R01.jsp";

		/** スマホプレビュー一覧プレビュー */
		public static final String JSP_FSB01L02 = "/preview/fsB01L02.jsp";
	}

	/**
	 * メニュー機能パス
	 * @author Takahiro Kimura
	 * @version 1.0
	 *
	 */
	public static final class Menu {

		/** メニュー画面JSP */
		public static final String JSP_SPB01M01 = "/top/spB01M01.jsp";

		/** メニューメソッドへリダイレクト */
		public static final String REDIRECT_MENU = "/top/menu/?redirect=true";


	}

	/**
	 * 応募者一覧機能パス
	 * @author Takahiro Kimura
	 * @version 1.0
	 *
	 */
	public static final class Application {

		/** 応募者一覧画面JSP */
		public static final String JSP_SPC01L01 = "/application/spC01L01.jsp";

		/** 応募者一覧の求人原稿から遷移するアクションパス */
		public static final String ACTION_APPLICATION_MAIL_LIST_FOCUSWEB = "/applicationMail/list/focusWeb/";

		/** 応募者一覧リストへ戻る */
		public static final String RETURN_TO_APPLICATION_LIST = "/application/list/returnToList";

		/** 応募者詳細画面JSP  */
		public static final String JSP_SPC01R01 = "/application/spC01R01.jsp";

		/** 応募者詳細画面（応募者のメール一覧）JSP  */
		public static final String JSP_SPC01L02 = "/application/spC01L02.jsp";

		/** 応募者詳細画面ポップアップJSP */
		public static final String JSP_SPC02R01_SUB = "/application/spC02R02_sub.jsp";

		/** 応募メール一覧画面JSP */
		public static final String JSP_SPC02L01 = "/application/spC02L01.jsp";

		/** 応募メール詳細画面JSP */
		public static final String JSP_SPC02R01 = "/application/spC02R01.jsp";

		/** 返信メール入力画面JSP */
		public static final String JSP_SPC02C01 = "/application/spC02C01.jsp";

		/** 返信メール入力確認画面JSP */
		public static final String JSP_SPC02C02 = "/application/spC02C02.jsp";

		/** 返信メール入力完了画面JSP */
		public static final String JSP_SPC02C03 = "/application/spC02C03.jsp";

		/** 返信メール削除完了画面JSP */
		public static final String JSP_SPC02D03 = "/application/spC02D03.jsp";

		/** 返信メソッドへリダイレクト */
		public static final String REDIRECT_APPLICATIONMAIL_INPUT = "/applicationMail/input/lumpSend/?redirect=true";

		/** 返信完了メソッドへリダイレクト */
		public static final String REDIRECT_APPLICATIONMAIL_INPUT_COMP = "/applicationMail/input/comp/?redirect=true";

		/** メール削除完了メソッドへリダイレクト */
		public static final String REDIRECT_APPLICATIONMAIL_DELETE_COMP = "/applicationMail/detail/comp/?redirect=true";

		/** 応募者一覧へのリダイレクト */
		public static final String REDIRECT_APPLICATION_LIST_INDEX = "/application/list/?redirect=true";

		/** 応募テスト確認完了 */
		public static final String JSP_SPC03R01 = "/application/spC03R01.jsp";

		/** 完了メソッドへリダイレクト */
		public static final String REDIRECT_APPTEST_INDEX_COMP = "/appTest/index/comp/?redirect=true";

		/** 店舗見学メール一覧画面JSP */
		public static final String JSP_SPC04L01 = "/application/spC04L01.jsp";

		/** 店舗見学メール一覧画面JSP */
		public static final String JSP_SPC04L02 = "/application/spC04L02.jsp";

		/** 店舗見学メール一覧画面JSP(メール送信者とメール一覧) */
		public static final String JSP_SPC04L03 = "/application/spC04L03.jsp";

		/** 店舗見学メール詳細画面JSP */
		public static final String JSP_SPC04R01 = "/application/spC04R01.jsp";

		/** 店舗見学メール詳細画面JSP */
		public static final String JSP_SPC04R01_SUB = "/application/spC04R01_sub.jsp";

		/** 返信メール入力画面JSP */
		public static final String JSP_SPC04C01 = "/application/spC04C01.jsp";

		/** 返信メール入力確認画面JSP */
		public static final String JSP_SPC04C02 = "/application/spC04C02.jsp";

		/** 返信メール入力完了画面JSP */
		public static final String JSP_SPC04C03 = "/application/spC04C03.jsp";

		/** 返信メール削除完了画面JSP */
		public static final String JSP_SPC04D03 = "/application/spC04D03.jsp";

		/** 質問者返信完了メソッドへリダイレクト */
		public static final String REDIRECT_OBSERVATEAPPLICATIONMAIL_INPUT_COMP = "/observateApplicationMail/input/comp/?redirect=true";

		/** 質問者一覧へのリダイレクト */
		public static final String REDIRECT_OBSERVATEAPPLICATION_LIST_INDEX = "/observateApplication/list/?redirect=true";


		/** アルバイトメール一覧画面JSP */
		public static final String JSP_SPC05L01 = "/application/spC05L01.jsp";

		/** アルバイト者一覧画面JSP */
		public static final String JSP_SPC05L02 = "/application/spC05L02.jsp";

		/** アルバイト応募者一覧へもどる */
		public static final String RETURN_TO_ARBEIT_APPLICATION_LIST = "/arbeit/list/returnToList";

		/** アルバイト者一覧のメール一覧画面JSP */
		public static final String JSP_SPC05L03 = "/application/spC05L03.jsp";

		/** アルバイトメール詳細画面JSP */
		public static final String JSP_SPC05R01 = "/application/spC05R01.jsp";

		/** アルバイト応募者詳細画面JSP */
		public static final String JSP_SPC05R02 = "/application/spC05R02.jsp";

		/** アルバイト応募者詳細画面JSP */
		public static final String JSP_SPC05R02_SUB = "/application/spC05R02_sub.jsp";

		/** アルバイト一括送信メソッドへリダイレクト */
		public static final String REDIRECT_ARBEIT_MAIL_INPUT = "/arbeitMail/input/lumpSend/?redirect=true";

		/** アルバイトメール削除完了メソッドへリダイレクト */
		public static final String REDIRECT_ARBEIT_AIL_DELETE_COMP = "/arbeitMail/detail/comp/?redirect=true";

		/** 返信完了メソッドへリダイレクト */
		public static final String REDIRECT_ARBEIT_MAIL_INPUT_COMP = "/applicationMail/input/comp/?redirect=true";


		/** アルバイト返信メール入力画面JSP */
		public static final String JSP_SPC05C01 = "/application/spC05C01.jsp";

		/** アルバイト返信メール入力確認画面JSP */
		public static final String JSP_SPC05C02 = "/application/spC05C02.jsp";

		/** アルバイト返信メール入力完了画面JSP */
		public static final String JSP_SPC05C03 = "/application/spC05C03.jsp";

		/** アルバイト返信メール入力完了画面JSP */
		public static final String JSP_SPC05C04 = "/application/spC05C04.jsp";

		/** アルバイトメール削除完了画面JSP */
		public static final String JSP_SPC05D04 = "/application/spC05D04.jsp";

		/** スカウト会員情報一覧JSP  */
		public static final String JSP_SPC06L02 = "/application/spC06L02.jsp";

		/** 気になる会員一覧JSP */
		public static final String JSP_SPC07L01 = "/application/spC07L01.jsp";

		/** スカウト会員の再検索へフォワード */
		public static final String FWD_SCOUT_SEARCH_AGAIN = "/scoutMember/list/searchAgain";

		/** スカウト会員情報とメール送受信一覧JSP  */
		public static final String JSP_SPC06L03 = "/application/spC06L03.jsp";

		/** スカウト詳細リストにリダイレクト */
		public static final String REDIRECT_SCOUT_DETAIL_LIST_SHOW_LIST = "/scoutMember/detailMailList/showList".concat(REDIRECT_STR_NO_SLASH);

		/** スカウトメール一括送信にリダイレクト */
		public static final String REDIRECT_SCOUT_MAIL_RETURN_LUMP_SEND = "/scoutMail/input/returnLumpSend?redirect=true";

		/** スカウトメール一括送信にリダイレクト(遷移元：メールBOX)  */
		public static final String REDIRECT_SCOUT_MAIL_RETURN_LUMP_SEND_FROM_MAILBOX = "/scoutMail/input/returnLumpSendFromMail?redirect=true";

		/** スカウト送信者一覧へリダイレクト */
		public static final String SCOUT_MEMBER_CHANGE_PAGE = "/scoutMember/list/changePage/";

		/** プレ応募メール一覧画面JSP */
		public static final String JSP_SPP02L01 = "/preApplication/spP02L01.jsp";

		/** プレ応募メール詳細画面JSP */
		public static final String JSP_SPP02R01 = "/preApplication/spP02R01.jsp";

		/** プレ応募メール入力画面JSP */
		public static final String JSP_SPP02C01 = "/preApplication/spP02C01.jsp";

		public static final String JSP_SPP02C02 = "/preApplication/spP02C02.jsp";

		/** 返信完了メソッドへリダイレクト */
		public static final String REDIRECT_PRE_APPLICATIONMAIL_INPUT_COMP = "/preApplicationMail/input/comp/?redirect=true";

		/** 返信メール入力完了画面JSP */
		public static final String JSP_SPP02C03 = "/preApplication/spP02C03.jsp";

		/** プレ応募者一覧の求人原稿から遷移するアクションパス */
		public static final String ACTION_PRE_APPLICATION_MAIL_LIST_FOCUSWEB = "/preApplicationMail/list/focusWeb/";

		/** 応募者一覧画面JSP */
		public static final String JSP_SPP01L01 = "/preApplication/spP01L01.jsp";

		/** 返信メール削除完了画面JSP */
		public static final String JSP_SPP02D03 = "/preApplication/spP02D03.jsp";

		/** 応募者詳細画面（応募者のメール一覧）JSP  */
		public static final String JSP_SPP01L02 = "/preApplication/spP01L02.jsp";

	}

	/**
	 * 求人原稿一覧機能パス
	 * @author Takahiro Kimura
	 * @version 1.0
	 */
	public static final class Webdata {
		/** 求人原稿一覧画面JSP */
		public static final String JSP_SPD01L01 = "/webdata/spD01L01.jsp";

		/** 電話応募一覧画面JSP */
		public static final String JSP_SPD02L01 = "/webdata/spD02L01.jsp";

		/** 電話応募一覧へのパス */
		public static final String FOCUS_PHONE_APPLICATION_LIST = "/webdata/list/focusPhoneApplication/";
	}

	/**
	 * スカウト・足あと機能パス
	 * @author Takahiro Kimura
	 * @version 1.0
	 */
	public static final class ScoutFoot {

		/** 求職者一覧画面JSP */
		public static final String JSP_SPE01L01 = "/scoutFoot/spE01L01.jsp";

		/** 求職者詳細画面JSP */
		public static final String JSP_SPE01R01 = "/scoutFoot/spE01R01.jsp";

		/** 求職者一覧へリダイレクト */
		public static final String REDIRECT_MEMBER_LIST_SEARCH_AGAIN = "/member/list/searchAgain/?redirect=true";

		/** 求職者詳細へリダイレクト */
		public static final String REDIRECT_MEMBER_DETAIL = "/member/detail/index/";

		/** 求職者詳細へリダイレクト */
		public static final String REDIRECT_MEMBER_DETAIL_FROM_KEEP_BOX = "/member/detail/indexFromKeepBox/";

		/** スカウトメール一覧画面JSP */
		public static final String JSP_SPE02L01 = "/scoutFoot/spE02L01.jsp";

		/** スカウトメール詳細画面JSP */
		public static final String JSP_SPE02R01 = "/scoutFoot/spE02R01.jsp";

		/** スカウトメール詳細画面へリダイレクト */
		public static final String REDIRECT_SCOUTMAIL_DETAIL = "/scoutMail/detail/index/";

		/** スカウトメール入力画面JSP */
		public static final String JSP_SPE02C01 = "/scoutFoot/spE02C01.jsp";

		/** スカウトメール入力画面へリダイレクト */
		public static final String REDIRECT_SCOUTMAIL_INPUT = "/scoutMail/input/index/?redirect=true";

		/** スカウトメール入力画面へリダイレクト */
		public static final String REDIRECT_SCOUTMAIL_INPUT_FROM_KEEP_BOX = "/scoutMail/input/indexFromKeepBox/?redirect=true";

		/** スカウトメール入力確認画面JSP */
		public static final String JSP_SPE02C02 = "/scoutFoot/spE02C02.jsp";

		/** スカウトメール入力完了画面JSP */
		public static final String JSP_SPE02C03 = "/scoutFoot/spE02C03.jsp";

		/** スカウトメール入力完了画面JSP（返信） */
		public static final String JSP_SPE02C04 = "/scoutFoot/spE02C04.jsp";

		/** スカウトメール削除完了画面JSP */
		public static final String JSP_SPE02D03 = "/scoutFoot/spE02D03.jsp";

		/** スカウトメール削除完了画面JSP */
		public static final String JSP_SPE02D04 = "/scoutFoot/spE02D04.jsp";

		/** スカウトメール一覧へリダイレクト */
		public static final String REDIRECT_SCOUTMAIL_LIST_SEARCH_AGAIN = "/scoutMail/list/searchAgain/?redirect=true";

		/** スカウトメール返信メソッドへリダイレクト(メールBOX) */
		public static final String REDIRECT_SCOUTMAIL_INPUT_RETURN_MAIL_FROM_MAIL = "/scoutMail/input/returnMailFromMail/?redirect=true";

		/** スカウトメール返信メソッドへリダイレクト(スカウト・足あと) */
		public static final String REDIRECT_SCOUTMAIL_INPUT_RETURN_MAIL = "/scoutMail/input/returnMail/?redirect=true";

		/** スカウトメール一覧（メールBOX） */
		public static final String REDIRECT_SCOUTMAIL_LIST_MAILBOX = "/scoutMail/list/mailBox/";

		/** スカウトメール一覧（スカウト・足あと） */
		public static final String REDIRECT_SCOUTMAIL_LIST = "/scoutMail/list/";

		/** 気になる応募者一覧 */
		public static final String FOCUS_INTEREST_LIST = "/scoutMember/list/focusInterest/";

		/** 求職者詳細画面JSP */
		public static final String JSP_SPE01R02_SUB = "/scoutFoot/spE01R02_sub.jsp";

		/** キープボックス画面JSP */
		public static final String JSP_SPE03L01 = "/scoutFoot/spE03L01.jsp";

		/** キープボックス一覧へリターン */
		public static final String RETURN_TO_MEMBER_KEEPBOX_LIST = "/member/keepBox/searchAgain";

		/** キープBOX一覧へリダイレクト */
		public static final String REDIRECT_MEMBER_KEEPBOX_SEARCH_AGAIN = "/member/keepBox/searchAgain/?redirect=true";

		/** 返信完了メソッドへリダイレクト */
		public static final String REDIRECT_SCOUTMAIL_INPUT_COMP = "/scoutMail/input/comp/?redirect=true";

		/** メール削除INDEXメソッドへリダイレクト */
		public static final String REDIRECT_SCOUTMAIL_DELETE_INDEX = "/scoutMail/delete/index/?redirect=true";

		/** メール削除INDEXメソッドへリダイレクト */
		public static final String REDIRECT_SCOUTMAIL_DELETE_INDEX_FROM_MAIL = "/scoutMail/delete/indexFromMail/?redirect=true";

		/** メール削除完了メソッドへリダイレクト */
		public static final String REDIRECT_SCOUTMAIL_DELETE_COMP = "/scoutMail/delete/comp/?redirect=true";

		/** メール削除完了メソッドへリダイレクト */
		public static final String REDIRECT_SCOUTMAIL_DELETE_COMP_FROM_MAIL = "/scoutMail/delete/compFromMail/?redirect=true";

	}

	/**
	 * 定型文機能パス
	 * @author Takahiro Kimura
	 * @version 1.0
	 */
	public static final class Pattern {

		/** 定型文一覧の初期表示アクションパス */
		public static final String  ACTION_PATTERN_LIST_INDEX = "/pattern/list/index/";
		/** 定型文一覧画面JSP */
		public static final String JSP_SPF01L01 = "/pattern/spF01L01.jsp";

		/** 定型文一覧再表示パス */
		public static final String PATTERN_LIST_RE_SHOW_LIST_PATH = "/pattern/list/reShowList";

		/** 定型文詳細の初期表示アクションパス */
		public static final String  ACTION_PATTERN_DETAIL_INDEX = "/pattern/detail/index/";
		/** 定型文詳細画面JSP */
		public static final String JSP_SPF01R01 = "/pattern/spF01R01.jsp";

		/** 定型文登録画面JSP */
		public static final String JSP_SPF01C01 = "/pattern/spF01C01.jsp";
		/** 定型文登録確認画面JSP */
		public static final String JSP_SPF01C02 = "/pattern/spF01C02.jsp";
		/** 定型文登録完了画面JSP */
		public static final String JSP_SPF01C03 = "/pattern/spF01C03.jsp";
		/** 登録完了メソッドへリダイレクト */
		public static final String REDIRECT_PATTERN_INPUT_COMP = "/pattern/input/comp/?redirect=true";

		/** 定型文編集の初期表示アクションパス */
		public static final String  ACTION_PATTERN_EDIT_INDEX = "/pattern/edit/index/";
		/** 定型文編集画面JSP */
		public static final String JSP_SPF01E01 = "/pattern/spF01E01.jsp";
		/** 定型文編集確認画面JSP */
		public static final String JSP_SPF01E02 = "/pattern/spF01E02.jsp";
		/** 定型文編集完了画面JSP */
		public static final String JSP_SPF01E03 = "/pattern/spF01E03.jsp";
		/** 編集完了メソッドへリダイレクト */
		public static final String REDIRECT_PATTERN_EDIT_COMP = "/pattern/edit/comp/?redirect=true";

		/** 定型文削除完了画面JSP */
		public static final String JSP_SPF01D03 = "/pattern/spF01D03.jsp";
		/** 削除完了メソッドへリダイレクト */
		public static final String REDIRECT_PATTERN_DELETE_COMP = "/pattern/delete/comp/?redirect=true";

	}

	/**
	 * 登録情報機能パス
	 * @author Takahiro Kimura
	 * @version 1.0
	 */
	public static final class Shop {

		/** 登録情報詳細初期表示メソッドへリダイレクト */
		public static final String REDIRECT_SHOP_INDEX_INDEX = "/shop/index/index/?redirect=true";

		/** 登録情報詳細画面JSP */
		public static final String JSP_SPG01R01 = "/shop/spG01R01.jsp";

		/** 登録情報編集画面JSP */
		public static final String JSP_SPG01E01 = "/shop/spG01E01.jsp";

		/** 登録情報編集画面JSP */
		public static final String JSP_SPG01E02 = "/shop/spG01E02.jsp";

		/** 登録情報編集完了画面JSP */
		public static final String JSP_SPG01E03 = "/shop/spG01E03.jsp";

		/** 編集完了メソッドへリダイレクト */
		public static final String REDIRECT_SHOP_EDIT_COMP = "/shop/edit/comp/?redirect=true";

	}

	/**
	 * パスワードリマインド機能パス
	 */
	public static final class PasswordReissue {
		/** パスワード再登録メール送信 */
		public static final String JSP_SPH01C01 = "/passwordReissue/spH01C01.jsp";

		public static final String  REDIRECT_PASSWORDREISSUE_INPUT_COMP = "/passwordReissue/input/comp/?redirect=true";

		/** パスワード再登録メール送信完了 */
		public static final String JSP_SPH01C03 = "/passwordReissue/spH01C03.jsp";

		/** メールアドレス認証画面表示メソッドへリダイレクト */
		public static final String  REDIRECT_PASSWORDREISSUE_EDIT_SSLINDEX = "/passwordReissue/edit/sslIndex/?redirect=true";
		/** メールアドレス認証 */
		public static final String JSP_SPI01C01_1 = "/passwordReissue/spI01C01_1.jsp";
		/** パスワード再登録 */
		public static final String JSP_SPI01C01_2 = "/passwordReissue/spI01C01_2.jsp";
		/** パスワード再登録完了メソッドへリダイレクト */
		public static final String  REDIRECT_PASSWORDREISSUE_EDIT_COMP = "/passwordReissue/edit/comp/?redirect=true";
		/** パスワード再発行完了 */
		public static final String JSP_SPI01C03 = "/passwordReissue/spI01C03.jsp";
	}

	/**
	 * 店舗一覧の画面IDを保持するクラス
	 * @author Takehiro Nakamori
	 *
	 */
	public static final class ShopList {
		/** インデックス */
		public static final String JSP_SPJ01A01 = "/shopList/spJ01A01.jsp";
		/** 登録画面 */
		public static final String JSP_SPJ02C01 = "/shopList/spJ02C01.jsp";
		/** 登録確認画面 */
		public static final String JSP_SPJ02C02 = "/shopList/spJ02C02.jsp";
		/** 登録完了画面 */
		public static final String JSP_SPJ02C03 = "/shopList/spJ02C03.jsp";
		/** 登録完了画面へリダイレクト */
		public static final String REDIRECT_SHOPLIST_INPUT_COMP = "/shopList/input/comp" + REDIRECT_STR;

		public static final String  ACTION_INPUT_BACK = "/shopList/input/back";

		public static final String  ACTION_EDIT_BACK = "/shopList/edit/backTo";

		/** 店舗一覧検索画面 */
		public static final String JSP_SPJ03L01 = "/shopList/spJ03L01.jsp";
		/** 店舗一覧検索画面のパス */
		public static final String ACTION_SHOPLIST_LIST = "/shopList/list/";

		/** 店舗一覧再表示パス */
		public static final String SHOP_LIST_RE_SHOW_LIST_PATH = "/shopList/list/reShowList";

		/** 店舗一覧詳細画面 */
		public static final String JSP_SPJ04R01 = "/shopList/spJ04R01.jsp";
		/** 店舗一覧詳細 削除画面 */
		public static final String JSP_SPJ04D01 = "/shopList/spJ04D01.jsp";
		/** 店舗一覧詳細 削除完了画面へのリダイレクト */
		public static final String REDIRECT_SHOPLIST_DETAIL_DELETE_COMP = "/shopList/detail/deleteComp/?redirect=true";
		/** 店舗一覧詳細画面のパス */
		public static final String ACTION_SHOPLIST_DETAIL = "/shopList/detail";

		/** 店舗一覧編集画面 インデックス */
		public static final String JSP_SPJ05E01 = "/shopList/spJ05E01.jsp";
		/** 店舗一覧編集画面 確認 */
		public static final String JSP_SPJ05E02 = "/shopList/spJ05E02.jsp";
		/** 店舗一覧編集画面 完了 */
		public static final String JSP_SPJ05E03 = "/shopList/spJ05E03.jsp";
		/** 店舗一覧編集完了画面へリダイレクト */
		public static final String REDIRECT_SHOPLIST_EDIT_COMP = "/shopList/edit/comp/?redirect=true";


		/** 店舗一覧CSV入力画面リスト */
		public static final String JSP_SPJ06L01 = "/shopList/spJ06L01.jsp";
		/** 店舗一覧CSV編集画面 編集 */
		public static final String JSP_SPJ06E01 = "/shopList/spJ06E01.jsp";
		/** 店舗一覧CSV編集画面 確認 */
		public static final String JSP_SPJ06E02 = "/shopList/spJ06E02.jsp";
		/** 店舗一覧CSV完了画面 */
		public static final String JSP_SPJ06C03 = "/shopList/spJ06C03.jsp";
		/** 店舗一覧CSV完了画面へリダイレクト */
		public static final String REDIRECT_SHOPLIST_CSV_COMPLETE = "/shopList/inputCsv/comp?redirect=true";
		/** 店舗一覧CSVリストへリダイレクト */
		public static final String REDIRECT_SHOPLIST_CSV_BACK_TO_LIST = "/shopList/inputCsv/backToList?redirect=true";

		/** 店舗一覧CSV入力アクションのパス */
		public static final String ACTION_SHOPLIST_INPUTCSV = "/shopList/inputCsv";

		/** 店舗画像管理 */
		public static final String JSP_SPJ07L01 = "/shopList/spJ07L01.jsp";

		public static final String  CUSTOMER_IMAGE_BACK_TO_NULL = "/customerImage/list/backToNull";

	}

	/**
	 * 料金表パス
	 * @author Daiki Uchida
	 */
	public static final class Price {

		/** 料金表初期表示アクションパス */
		public static final String  ACTION_PRICE_INDEX = "/price/";
		/** 料金表画面JSP */
		public static final String JSP_SPK01A01 = "/price/spK01A01.jsp";

	}

	public static final class Contact {

		/** 問い合わせ入力画面 */
		public static final String JSP_SPL01A01 = "/contact/spL01A01.jsp";

		/** 問い合わせ内容確認画面 */
		public static final String JSP_SPL01C01 = "/contact/spL01C01.jsp";

		/** 問い合わせ完了画面 */
		public static final String JSP_SPL01C02 = "/contact/spL01C02.jsp";

		public static final String REDIRECT_CONTACT_COMP = "/contact/comp/?redirect=true";
	}

	/**
	 * Ajax用部品画面の画面IDを保持するクラス
	 * @author Katsutoshi Hasegawa
	 */
	public static final class Ajax{

		/** キープ済み用部品 */
		public static final String  JSP_APE01AS1 = "/ajax/apE01AS1.jsp";

	}
}
