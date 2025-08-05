package com.gourmetcaree.admin.pc.sys.mai;

import com.gourmetcaree.admin.pc.sys.dto.mai.ApplicationTestMaiDto;
import com.gourmetcaree.admin.pc.sys.dto.mai.FixedMaiDto;
import com.gourmetcaree.admin.pc.sys.dto.mai.MailMagazineTriggerMaiDto;
import com.gourmetcaree.admin.pc.sys.dto.mai.PostRequestMaiDto;
import com.gourmetcaree.admin.pc.sys.dto.mai.ScoutMailAddMaiDto;


/**
 * メール送信用のインタフェースです。(for S2Mai)
 * @author Takahiro Ando
 * @version 1.0
 */
public interface GourmetcareeMai {

	/**
	 * 掲載依頼のメールを管理者へ送信します。
	 * @param postRequestMaiDto メール情報
	 */
	public void sendPostRequestMail(PostRequestMaiDto postRequestMaiDto);

	/**
	 * 掲載確定のメールを代理店と担当者へ送信します。
	 * @param fixedMaiDto メール情報
	 */
	public void sendFixedMail(FixedMaiDto fixedMaiDto);

	/**
	 * スカウトメール追加のメールを顧客へ送信します。
	 * @param scountMailAddMaiDto メール情報
	 */
	public void sendScoutMailAddMail(ScoutMailAddMaiDto scoutMailAddMaiDto);

	/**
	 * 応募テストメールを顧客へ送信します。
	 * @param applicationTestMaiDto メール情報
	 */
	public void sendApplicationTestMail(ApplicationTestMaiDto applicationTestMaiDto);

	/**
	 * Jamesに向けてメールマガジンのトリガー用メールを送信します。
	 * @param MailMagazineDto メール情報
	 */
	public void sendMailMagazineTriggerMail(MailMagazineTriggerMaiDto mailMagazineTriggerMaiDto);

}
