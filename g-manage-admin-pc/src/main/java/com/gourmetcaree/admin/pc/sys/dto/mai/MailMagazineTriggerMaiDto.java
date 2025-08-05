package com.gourmetcaree.admin.pc.sys.dto.mai;

import com.gourmetcaree.common.dto.BaseSysMaiDto;

/**
 * メールマガジンをキックするDtoです。
 * @author Takahiro Ando
 * @version 1.0
 */
public class MailMagazineTriggerMaiDto extends BaseSysMaiDto {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** 件名 */
	private String mailMagazineSuject;

	/**
	 * 件名を取得します。
	 * @return 件名
	 */
	public String getMailMagazineSuject() {
	    return mailMagazineSuject;
	}

	/**
	 * 件名を設定します。
	 * @param mailMagazineSuject 件名
	 */
	public void setMailMagazineSuject(String mailMagazineSuject) {
	    this.mailMagazineSuject = mailMagazineSuject;
	}
}
