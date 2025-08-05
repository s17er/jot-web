package com.gourmetcaree.shop.logic.dto;

import java.io.Serializable;
import java.util.Date;

import com.gourmetcaree.common.dto.BaseDto;

/**
 * メールをセレクトする際に扱うDTO
 * @author Takehiro Nakamori
 *
 */
public class MailSelectDto extends BaseDto implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 7376092252462125685L;

	/** ID */
	public Integer id;

	/** メール区分 */
	public Integer mailKbn;

	/** 送受信区分 */
	public Integer sendKbn;

	/** 送信者区分 */
	public Integer senderKbn;

	/** 送信者ID */
	public Integer fromId;

	/** 送信者名 */
	public String fromName;

	/** 受信者ID */
	public Integer toId;

	/** 受信者名 */
	public String toName;

	/** 件名 */
	public String subject;

	/** 本文 */
	public String body;

	/** 応募ID */
	public Integer applicationId;

	/** 店舗応募ID */
	public Integer observateApplicationId;

	/** スカウト履歴ID */
	public Integer scoutHistoryId;

	/** アルバイト応募ID */
	public Integer arbeitApplicationId;

	/** メールステータス */
	public Integer mailStatus;

	/** 送信日時 */
	public Date sendDatetime;

	/** 閲覧日時 */
	public Date readingDatetime;

	/** 親メールID */
	public Integer parentMailId;

	/** アクセスコード */
	public String accessCd;

	/** 閲覧日時 */
	public Date receiveReadingDatetime;

	/** エリアコード */
	public Integer areaCd;

	/** 選考フラグ */
	public Integer selectionFlg;

	/** メモ */
	public String memo;

	/** 誕生日 */
	public Date birthday;

	/** 雇用形態区分 */
	public Integer employPtnKbn;

	/** 職種区分 */
	public Integer jobKbn;

	/** 年齢 */
	public Integer age;

	/** 飲食業界経験年数区分 */
	public Integer foodExpKbn;

	/** 応募先名 */
	public String applicationName;

	public String applicationJob;

	/** 会員ID */
	public Integer memberId;

	/** 店舗ID */
	public Integer shopListId;
}
