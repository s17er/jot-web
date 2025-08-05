package com.gourmetcaree.common.constants;

import java.util.regex.Pattern;

/**
 * グルメキャリー共通の定数定義です。
 * @author Takahiro Ando
 * @version 1.0
 */
public interface GourmetCareeConstants {

	/** エラーIDの基数 */
	public static final int ERROR_ID_RADIX = 36;

	/** メールアクセスコードの長さ */
	public static final int MAIL_ACCESS_CODE_LEN = 16;

	/** メール返信の件名プレフィックス */
	public static final String MAIL_REPLY_SUBJECT_PREFIX = "Re: ";

	/** メール返信の引用符 */
	public static final String MAIL_REPLY_QUOTATION_MARK = "> ";

	/** スラッシュ区切りの日付文字列パターン(yyyy/MM/dd) */
	public static final String DATE_FORMAT_SLASH = "yyyy/MM/dd";

	/** スラッシュ無しの日付文字列パターン(yyyyMMdd) */
	public static final String DATE_FORMAT_NONSLASH = "yyyyMMdd";

	/** スラッシュ無しの日時文字列パターン(yyyyMMddHHmmss) */
	public static final String DATETIME_FORMAT_NONSLASH = "yyyyMMddHHmmss";

	/** 年のフォーマット */
	public static final String YEAR_FORMAT = "yyyy";

	/** 月のフォーマット */
	public static final String MONTH_FORMAT = "MM";

	/** 月のフォーマット */
	public static final String SINGLE_MONTH_FORMAT = "M";

	/** 日のフォーマット */
	public static final String DAY_FORMAT = "dd";

	/** 日のフォーマット */
	public static final String SINGLE_DAY_FORMAT = "d";

	/** 時フォーマット(HH) */
	public static final String HOUR_FORMAT = "HH";

	/** 分フォーマット(mm) */
	public static final String MINUTE_FORMAT = "mm";

	/** メールの日時フォーマット(yyyy/MM/dd HH:mm:ss) */
	public static final String MAIL_DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss";

	/** 日時フォーマット(yyyy/MM/dd HH:mm) */
	public static final String DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm";

	/** 日付フォーマット(yyyy年MM月dd日 HH:mm */
	public static final String DATE_TIME_LETTER_FORMAT = "yyyy年MM月dd日 HH:mm";

	/** 時間フォーマット(HH:mm) */
	public static final String TIME_FORMAT = "HH:mm";

	/** 時間のデフォルト( 00:00:00) */
	public static final String TIME_STR = " 00:00:00";

	/** 終了時間のデフォルト( 23:59:59) */
	public static final String END_TIME_STR = " 23:59:59";

	/** 半年の日数 */
	public static final int HALF_YEAR_DAYS = 180;

	/** 無料スカウトメール数 */
	public static final int FREE_SCOUT_MAIL_COUNT = 10;


	/** 正規表現：メールアドレス */
	public static final String MASK_MAIL_ADDRESS = "[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+";

	/** 正規表現：0文字以上の任意の文字 */
	public static final String MASK_FREE_CHAR = "*";

	/** デフォルトのフォームメール保存時間 */
	public static final int DEFAULT_FORM_MAIL_RESERVE_TIME = 48;

	/** リダイレクト用 */
	public static final String REDIRECT_STR = "?redirect=true";

	/** カンマ */
	public static final String KANMA_STR = ",";

	/** スラッシュ */
	public static final String SLASH_STR = "/";

	/** 全角チルダ */
	public static final String ZENKAKU_TILDE_STR = "～";

	/** ハイフンマイナス */
	public static final String HYPHEN_MINUS_STR = "-";

	/** コロン */
	public static final String COLON_STR = ":";

	/** htmlの改行 */
	public static final String BR_TAG = "<br />";

	/** 検索条件を特定するsuffix */
	public static final String CONDITION_SUFFIX = "where_";

	/** 正規表現：半角英数のみ */
	public static final String MASK_SINGLE_ALPHANUM = "^[0-9a-zA-Z]+$";

	/** 正規表現：半角英数のみ */
	public static final String MASK_SINGLE_ALPHANUMSYMBOL = "^[0-9a-zA-Z@\\-\\_\\.]+$";

	/** 正規表現 : 全角かな */
	public static final String MASK_HIRAGANA = "^[ぁ-んー、。・／]+$";

	/** カタカナで始まる正規表現フォーマット */
	public static final String MASK_KATAKANA_FORMAT = "^[ァ-%s]";

	/** チェックボックス：チェック状態 */
	public static final String CHECKBOX_ON = "on";

	/** チェックボックス：未チェック状態 */
	public static final String CHECKBOX_OFF = "off";

	/** content-type：JPEG */
	public static final String MEDIA_CONTENT_TYPE_JPEG = "image/jpeg";

	/** content-type：JPEG(アップロード時) */
	public static final String MEDIA_CONTENT_TYPE_PJPEG = "image/pjpeg";

	/** content-type：PNG */
	public static final String MEDIA_CONTENT_TYPE_PNG = "image/png";

	/** content-type：GIF */
	public static final String MEDIA_CONTENT_TYPE_GIF = "image/gif";

	/** content-type：BMP */
	public static final String MEDIA_CONTENT_TYPE_BMP = "image/bmp";

	/** content-type：WMV */
	public static final String MEDIA_CONTENT_TYPE_WMV = "video/x-ms-wmv";

	/** content-type：MPEG */
	public static final String MEDIA_CONTENT_TYPE_MPEG = "video/mpeg";

	/** content-type：QUICKTIME */
	public static final String MEDIA_CONTENT_TYPE_QUICKTIME = "video/quicktime";

	/** content-type : EXCEL */
	public static final String MEDIA_CONTENT_TYPE_EXCEL = "application/vnd.ms-excel";

	/** content-type : CSV */
	public static final String MEDIA_CONTENT_TYPE_CSV = "text/csv";

	/** content-type : PDF */
	public static final String MEDIA_CONTENT_TYPE_PDF = "application/pdf";

	/** 拡張子：CSV*/
	public static final String MEDIA_EXTENTION_CSV = ".csv";

	/** CSVエンコーディング */
	public static final String CSV_ENCODING = "MS932";

	/** CSVの出力コンテントタイプ */
	public static final String CSV_OUTPUT_CONTENT_TYPE = "application/octet-stream;charset=Windows-31J";

	/** 画像縮小の比率 */
	public static final float DEFAULT_PERCENTAGE = 0.25F;

	/** 省略文字：三点リーダー */
	public static final String SANTEN_LEADER = "…";

	/** 正規表現：半角英数のみ */
	public static final String MASK_ALPHANUM = "[0-9a-zA-Z]";

	/** 表示順のデフォルト番号 */
	public static final int DEFAULT_DISP_NO = 0;

	/** Mobile版最大表示件数切り替えの初期値 */
	public static final String MOBILE_DEFAULT_MAX_ROW = "10";

	/** 最大表示件数切り替えの初期値 */
	public static final String DEFAULT_MAX_ROW = "20";

	/** 最大表示件数切り替えの初期値(int) */
	public static final int DEFAULT_MAX_ROW_INT = 20;

	/** 表示期間(月)の初期値(int) */
	public static final String DEFAULT_LIMIT_MONTH = "3";

	/** 表示期間(月)の初期値*/
	public static final int DEFAULT_LIMIT_MONTH_INT = 3;

	/** ページ切り替えの初期値 */
	public static final int DEFAULT_PAGE = 1;

	/** 路線選択上限の初期値 */
	public static final int DEFAULT_ROUTE_LIMIT = 15;

	/** maito:URI mailto */
	public static final String MAILTO = "mailto:";

	/** maito:URI subject */
	public static final String MAILTO_SUBJECT = "subject=";

	/** maito:URI body */
	public static final String MAILTO_BODY = "body=";

	/** maito:改行コード */
	public static final String MAILTO_RN_CD = "%0d%0a";

	/** 改行コード */
	public static final String RN_CD = "\r\n";

	/** 送信区分(送信) */
	public static final String SEND_KBN_SEND = "0";

	/** 送信区分(返信) */
	public static final String SEND_KBN_RETURN = "1";

	/** UserDto判別定数(運営側) */
	public static final int ADMIN_DTO = 1;

	/** UserDto判別定数(店舗側) */
	public static final int SHOP_DTO = 2;

	/** UserDto判別定数(公開側) */
	public static final int FRONT_DTO = 3;

	/** 全文検索で使用してはいけない記号 */
	public static final String ZENBUN_BAD_WORD = "[\\!\\&\\(\\)\\|\\\\\\'\\-\\:]";

	/** 空文字 */
	public static final String BLANK_STR = "";

	/** 画像アップロードでWebIdが未決定（登録/COPY）の場合用のID */
	public static final String IMG_FILEKEY_INPUT = "INPUT";

	/** 画像アップロードでShopListIdが未決定（登録）の場合用のID */
	public static final String IMG_SHOP_LIST_FILEKEY_INPUT = "SHOPLISTINPUTIMAGE";

	/** メールの文字コード */
	public static final String MAIL_CHARSET = "iso-2022-jp";

	/** 改行コード */
	public static final String CRLF = System.getProperty("line.separator");



	/** CSVヘッダのパラメータ1 */
	public static final String CSV_HEADER_PARAM1 = "Content-Disposition";

	/** CSVヘッダのファイル名プレフィックス */
	public static final String CSV_HEADER_FILENAME_PREFIX = "attachment; filename=";

	/** JSON用コンテントタイプ */
	public static final String JSON_CONTENT_TYPE = "application/json";

	/** エンコーディング */
	public static final String ENCODING = "UTF-8";


	public static final String ENCODING_SJIS = "SHIFT_JIS";

	/** JOT本社の緯度 */
	public static final String JOT_LATITUDE = "35.674572";
	/** JOT本社の経度 */
	public static final String JOT_LONGITUDE = "139.768889";

	/**
	 * jsessionidのパターン
	 */
	Pattern JSESSION_PATTERN = Pattern.compile(";jsessionid=(\\w{32})$");

	/** システムハッシュのソルトキー */
	public static final String SYSTEM_HASH_SOLT = "ginza";

	/** メルマガを送信するためのlaravel側へのアクセストークンのキー */
	public static final String MAILMAGAZINE_ACCESS_TOKEN_KEY = "GOURMETCAREE-TOKEN";

	/** メルマガを送信するためのlaravel側へのアクセストークン */
	public static final String MAILMAGAZINE_ACCESS_TOKEN = "e2qO5XFM33UvH3PBtdW5OZXVJ5H9gtgM";


}
