package com.gourmetcaree.shop.pc.application.dto.application;

import com.gourmetcaree.shop.pc.application.dto.applicationMail.ApplicationMailListDto;

/**
 * 応募者のメール一覧
 * @author Yamane
 *
 */
public class MailForApplicationIdDto extends ApplicationMailListDto {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -3924692341227567556L;

	/** 選考対象フラグ */
	public int selectionFlg;

	/** 選考対象フラグカラー */
	public String selectionFlgColor;

}
