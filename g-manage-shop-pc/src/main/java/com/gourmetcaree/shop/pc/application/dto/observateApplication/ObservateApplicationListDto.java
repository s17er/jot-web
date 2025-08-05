package com.gourmetcaree.shop.pc.application.dto.observateApplication;

import java.sql.Timestamp;

import com.gourmetcaree.common.dto.BaseDto;
import com.gourmetcaree.db.common.constants.MTypeConstants;

/**
 * 店舗見学・質問者一覧Dto
 * @author Yamane
 *
 */
public class ObservateApplicationListDto extends BaseDto {

	/** serialVersionUID */
	private static final long serialVersionUID = -1393008706962555734L;

	/** 未読メールがあるかどうかのフラグ */
	public boolean unopenedMailFlg = true;

	/** 質問者 */
	public String name;

	/** 性別 */
	public int sexKbn;

	/** 年齢 */
	public int age;

	/** 掲載店名 */
	public String applicationName;

	/** メモ */
	public String memo;

	/** 質問日時 */
	public Timestamp applicationDatetime;

	/** 質問者ID */
	public int id;

	/** 顧客ID */
	public int customerId;

	/** Version */
	public long version;

	/**
	 * 男性かどうかを取得する。
	 * @return true:男性、false:女性
	 */
	public boolean isApplicantMale() {
		return (sexKbn == MTypeConstants.Sex.MALE) ? true : false;
	}
}
