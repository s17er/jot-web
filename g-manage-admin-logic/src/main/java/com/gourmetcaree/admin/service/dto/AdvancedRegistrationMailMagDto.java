package com.gourmetcaree.admin.service.dto;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.gourmetcaree.common.dto.BaseDto;

/**
 * 事前登録メールマガジン送信用DTO
 * @author Takehiro Nakamori
 *
 */
public class AdvancedRegistrationMailMagDto extends BaseDto {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -8404032816774330583L;

	/** 事前登録エントリID */
	public Integer advancedRegistrationEntryId;

	/** 会員名 */
	public String memberName;

	/** ログインID */
	public String loginId;

	/** エリアコード */
	public Integer areaCd;

	/** PCメールアドレス */
	public String pcMail;

	/** 携帯メールアドレス */
	public String mobileMail;

	/** PCメール配信停止フラグ */
	public Integer pcMailStopFlg;

	/** モバイルメール配信停止フラグ */
	public Integer mobileMailStopFlg;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}
}
