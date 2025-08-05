package com.gourmetcaree.common.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import jp.co.whizz_tech.commons.WztDateUtil;

import org.apache.commons.lang.StringUtils;

import com.gourmetcaree.common.constants.GourmetCareeConstants;


/**
 * 日付、時刻を扱うユーティリティクラス
 * @author Takahiro Ando
 *
 */
public class DateUtils {

	/** 1日の秒数 */
	public static final long DATE_SECOND = 86400000;


	/**
	 * 指定した形式で当日の日時情報を取得する。
	 * @param pattern 例："yyyy'年'M'月'd'日'"
	 * @return
	 */
	public static String getTodayDateStr(String pattern) {
		String todayDateStr = "";

	    Date todayDate = new Date();
	    SimpleDateFormat sdf1 = new SimpleDateFormat(pattern);
	    todayDateStr = sdf1.format(todayDate);

	    return todayDateStr;
	}

	/**
	 * 日付に開始時間(00:00:00)を追加します。
	 * @param date 開始日
	 * @return 開始日時
	 * @throws ParseException
	 */
	public static Date getStartDatetime(Date date) throws ParseException {

		String dateStr = "";

		SimpleDateFormat sdf = new SimpleDateFormat(GourmetCareeConstants.DATE_FORMAT_SLASH);
		dateStr = sdf.format(date);
		sdf.applyPattern(GourmetCareeConstants.MAIL_DATE_TIME_FORMAT);
		return sdf.parse(dateStr + GourmetCareeConstants.TIME_STR);
	}

	/**
	 * 日付に終了時間(23:59:59)を追加します。
	 * @param date 終了日
	 * @return 終了日時
	 * @throws ParseException
	 */
	public static Date getEndDatetime(Date date) throws ParseException {

		String dateStr = "";

		SimpleDateFormat sdf = new SimpleDateFormat(GourmetCareeConstants.DATE_FORMAT_SLASH);
		dateStr = sdf.format(date);
		sdf.applyPattern(GourmetCareeConstants.MAIL_DATE_TIME_FORMAT);
		return sdf.parse(dateStr + GourmetCareeConstants.END_TIME_STR);
	}

	/**
	 * 指定した形式で日付を取得する。
	 * @param date
	 * @param pattern 例:'yyyy'年'M'月'd'日
	 * @return
	 */
	public static String getDateStr(Date date, String pattern) {
		String updateDateStr = "";

		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		updateDateStr = sdf.format(date);

		return updateDateStr;
	}

	/**
	 * 日付以下の情報がカットされた現在日付のDateを取得する。
	 * @return 日付以下がカットされた現在の日付
	 */
	public static Date getJustDate() {
		return getJustDate(new Date());
	}

	/**
	 * 日付以下の情報がカットされた日付のDateを取得する。
	 * @return 日付以下がカットされた日付
	 */
	public static Date getJustDate(Date date) {
		return org.apache.commons.lang.time.DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
	}

	/**
	 * 現在の日時を「分」よりしたをトリムして返却する。
	 * @return 現在の日付
	 */
	public static Date getJustDateTime() {
		return org.apache.commons.lang.time.DateUtils.truncate(new Date(), Calendar.MINUTE);
	}

	/**
	 * 現在日時のTimestampを取得する
	 * @return 現在日時のTimestamp
	 */
	public static Timestamp getJustTimestamp() {
		return new Timestamp(new Date().getTime());
	}

	/**
	 * 二つの日付の差を取得する。
	 * date2 - date1 をしているので注意。
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int getDateDifference(Date date1, Date date2) {
		Date ymd1 = org.apache.commons.lang.time.DateUtils.truncate(date1, Calendar.DAY_OF_MONTH);
		Date ymd2 = org.apache.commons.lang.time.DateUtils.truncate(date2, Calendar.DAY_OF_MONTH);
		return (int) ((ymd2.getTime() - ymd1.getTime()) / DATE_SECOND);
	}

	/**
	 * パターンを指定した日付文字列をjava.sql.Dateに変換します。
	 * @param dateStr 日付文字列
	 * @param pattern 日付パターン
	 * @return java.sql.Dateのオブジェクト
	 * @throws ParseException 日付文字列のパースエラー
	 */
	public static java.sql.Date convertStrToSqlDate(String dateStr, String pattern)
	throws ParseException {

		java.util.Date utilDt = org.apache.commons.lang.time.DateUtils
				.parseDate(dateStr, new String[] { pattern });

		return WztDateUtil.convertToSqlDate(utilDt);
	}

	/**
	 * 開始日時と終了日時を連結して比較する。
	 * @param fromDate 開始日
	 * @param fromHour 開始時
	 * @param fromMin  開始分
	 * @param toDate   終了日
	 * @param toHour   終了時
	 * @param toMin    終了分
	 * @return 開始日時が終了日時と等しい場合は値 0。<br />
	 *         開始日時が終了日時より後の場合は 0 より小さい値。<br />
	 *         開始日時が終了日時より前の場合は 0 より大きい値。
	 * @throws ParseException
	 */
	public static int compareDateTime(String fromDateStr, String fromHourStr, String fromMinStr,
			String toDateStr, String toHourStr, String toMinStr) throws ParseException {

		// 開始日付を生成
		Date fromDate = formatDate(fromDateStr, fromHourStr, fromMinStr);

		// 終了日付を生成
		Date toDate = formatDate(toDateStr, toHourStr, toMinStr);

		return toDate.compareTo(fromDate);

	}

	/**
	 * 開始日時と終了日時を比較する。
	 * @param fromDate 開始日時
	 * @param toDate   終了日時
	 * @return 開始日時が終了日時と等しい場合は値 0。<br />
	 *         開始日時が終了日時より後の場合は 0 より小さい値。<br />
	 *         開始日時が終了日時より前の場合は 0 より大きい値。
	 * @throws ParseException
	 */
	public static int compareDateTime(Date fromDate, Date toDate) throws ParseException {

		return toDate.compareTo(fromDate);

	}

	/**
	 * 開始日と終了日を比較する。
	 * @param fromDateStr 開始日
	 * @param toDateStr   終了日
	 * @return 開始日が終了日と等しい場合は値 0。<br />
	 *         開始日が終了日より後の場合は 0 より小さい値。<br />
	 *         開始日が終了日より前の場合は 0 より大きい値。
	 * @throws ParseException
	 */
	public static int compareDateTime(String fromDateStr, String toDateStr) throws ParseException {

		// 日付に変換
		SimpleDateFormat sdf = new SimpleDateFormat(GourmetCareeConstants.DATE_FORMAT_SLASH);
		Date fromDate = sdf.parse(fromDateStr);
		Date toDate = sdf.parse(toDateStr);

		return compareDateTime(fromDate, toDate);
	}

	/**
	 * 日付の文字列を日付型にフォーマットする
	 * @param dateStr 日付
	 * @return 日付
	 * @throws ParseException
	 */
	public static Date formatDate(String dateStr, String pattern) throws ParseException {

		return org.apache.commons.lang.time.DateUtils.parseDate(dateStr, new String[] {pattern});

	}

	/**
	 * 日、時、分を連結して日付型にフォーマットする
	 * @param date 日
	 * @param hour 時
	 * @param min  分
	 * @return 日付
	 * @throws ParseException
	 */
	public static Date formatDate(String date, String hour, String min) throws ParseException {

		Date result = null;
		// 日付を生成
		if(StringUtils.isNotEmpty(hour) && StringUtils.isNotEmpty(min)) {

			result = org.apache.commons.lang.time.DateUtils.parseDate(date + " " + hour + ":" + min, new String[] {GourmetCareeConstants.DATE_TIME_FORMAT});
		} else {
			result = org.apache.commons.lang.time.DateUtils.parseDate(date, new String[] {GourmetCareeConstants.DATE_FORMAT_SLASH});
		}

		return result;
	}


	/**
	 *
	 *　日時文字列をパースします。
	 *
	 * @param date yyyy/MM/dd
	 * @param hour 時間
	 * @param minute 分
	 * @return Dateに変換した日付
	 * @throws ParseException 日付変換に失敗した時にスロー
	 * @throws IllegalArgumentException dateがブランクの場合にスロー
	 */
	public static Date parseDate(String date, String hour, String minute) throws ParseException {
		if (StringUtils.isBlank(date)) {
			throw new IllegalArgumentException("日付がありません。");
		}

		StringBuilder pattern = new StringBuilder(GourmetCareeConstants.DATE_FORMAT_SLASH);
		StringBuilder dateStr = new StringBuilder(date);

		if (StringUtils.isNotBlank(hour)) {
			pattern.append(" HH");
			dateStr.append(" ")
					.append(hour);
		}

		if (StringUtils.isNotBlank(minute)) {
			pattern.append(":mm");
			dateStr.append(":")
					.append(minute);
		}

		SimpleDateFormat sdf = new SimpleDateFormat(pattern.toString());
		sdf.setLenient(false);
		return sdf.parse(dateStr.toString());
	}

	/**
	 *
	 *　日時文字列をパースします。
	 *
	 * @param date yyyy/MM/dd
	 * @param hour 時間
	 * @param minute 分
	 * @param second 秒
	 * @return Dateに変換した日付
	 * @throws ParseException 日付変換に失敗した時にスロー
	 * @throws IllegalArgumentException dateがブランクの場合にスロー
	 */
	public static Date parseDate(String date, String hour, String minute, String second) throws ParseException {
		if (StringUtils.isBlank(date)) {
			throw new IllegalArgumentException("日付がありません。");
		}

		StringBuilder pattern = new StringBuilder(GourmetCareeConstants.DATE_FORMAT_SLASH);
		StringBuilder dateStr = new StringBuilder(date);

		if (StringUtils.isNotBlank(hour)) {
			pattern.append(" HH");
			dateStr.append(" ")
					.append(hour);
		}

		if (StringUtils.isNotBlank(minute)) {
			pattern.append(":mm");
			dateStr.append(":")
					.append(minute);
		}

		if (StringUtils.isNotBlank(second)) {
			pattern.append(":ss");
			dateStr.append(":")
					.append(second);
		}

		SimpleDateFormat sdf = new SimpleDateFormat(pattern.toString());
		sdf.setLenient(false);
		return sdf.parse(dateStr.toString());
	}

	/**
	 * 入力された文字列の年、月、日を"yyyy/MM/dd"形式でDateへ変換する
	 * @return Date型の日付
	 * @throws ParseException 変換に失敗した場合のエラー
	 */
	public static Date convertStrDate(String year, String month, String day) throws ParseException {

		StringBuffer dateStr = new StringBuffer();
		dateStr.append(year).append("/").append(month).append("/").append(day);

		return  formatDate(dateStr.toString(), GourmetCareeConstants.DATE_FORMAT_SLASH);
	}

	/**
	 * java.util.Date型の日付から、java.sql.Date型の日付を返す
	 * @param d java.util.Date型の日付
	 * @return java.sql.Date型の日付
	 */
	public static java.sql.Date convertSqlDate(java.util.Date d) {
		if (d == null) {
			return null;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return new java.sql.Date(cal.getTimeInMillis());
	}

	/**
	 * java.sql.Dateの現在日付を返す
	 * @return java.sql.Date型の日付
	 */
	public static java.sql.Date getJustSqlDate() {
		return convertSqlDate(new java.util.Date());
	}


	/**
	 * 現在の日時が指定された期間内かどうかを返す
	 * @param startTimestamp 開始年月日時分秒
	 * @param endTimestamp 終了年月日時分秒
	 * @return 期間内の場合はtrue、期間外の場合はfalse
	 */
	public static boolean isPeriodJustDateTime(Timestamp startTimestamp, Timestamp endTimestamp) {
		Calendar nowCal = Calendar.getInstance();
		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startTimestamp);
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endTimestamp);
		return nowCal.after(startCal) && nowCal.before(endCal);
	}

	/**
	 * 日付文字列から一日足したTimestampへ変換して返す
	 * @param dateStr 日付文字列
	 * @return 日付(Timestamp)
	 * @throws ParseException
	 */
	public static Timestamp convertDateStrToTimestampAddDate(String dateStr, int addDate) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat(GourmetCareeConstants.DATE_FORMAT_SLASH);
		Date date = sdf.parse(dateStr);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, addDate);

		return new Timestamp(cal.getTimeInMillis());

	}
}
