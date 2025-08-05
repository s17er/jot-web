package com.gourmetcaree.admin.service.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import com.gourmetcaree.common.dto.BaseDto;

/**
 * スカウトメールログDTO
 * @author Takehiro Nakamori
 *
 */
public class ScoutMailLogDto extends BaseDto implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 3414087794031469399L;

	/** ログID */
	public Integer id;

	/** スカウト管理ID */
	public Integer scoutManageId;

	/** 顧客ID */
	public Integer customerId;

	/** スカウトメール区分 */
	public Integer scoutMailKbn;

	 /** 会員ID */
    public Integer memberId;

    /** 送信日時 */
    public Timestamp sendDatetime;

    /** 追加数 */
    public Integer addScoutCount;

    /** 追加日時 */
    public Timestamp addDatetime;

    /** スカウトメールログ区分 */
    public Integer scoutMailLogKbn;

}
