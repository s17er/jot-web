package com.gourmetcaree.shop.logic.mai;

import com.gourmetcaree.shop.logic.dto.mai.AppTestConfMaiDto;
import com.gourmetcaree.shop.logic.dto.mai.ApplicationMailReturnToAdminMaiDto;
import com.gourmetcaree.shop.logic.dto.mai.ApplicationMailReturnToApplicantMaiDto;
import com.gourmetcaree.shop.logic.dto.mai.ApplicationMailReturnToCustomerMaiDto;
import com.gourmetcaree.shop.logic.dto.mai.ChangePasswordMaiDto;
import com.gourmetcaree.shop.logic.dto.mai.ContactMaiDto;
import com.gourmetcaree.shop.logic.dto.mai.ObservateApplicationMailReturnToAdminMaiDto;
import com.gourmetcaree.shop.logic.dto.mai.ObservateApplicationMailReturnToApplicantMaiDto;
import com.gourmetcaree.shop.logic.dto.mai.ObservateApplicationMailReturnToCustomerMaiDto;
import com.gourmetcaree.shop.logic.dto.mai.PasswordReissueMailCompMaiDto;
import com.gourmetcaree.shop.logic.dto.mai.PasswordReissueMailConfMaiDto;
import com.gourmetcaree.shop.logic.dto.mai.PreApplicationMailReturnToCustomerMaiDto;
import com.gourmetcaree.shop.logic.dto.mai.ScoutMailReceiveInfoToMemberMaiDto;
import com.gourmetcaree.shop.logic.dto.mai.ScoutMailSendInfoToAdminMaiDto;
import com.gourmetcaree.shop.logic.dto.mai.ScoutMailSendInfoToCustomerMaiDto;


/**
 * メール送信用のインタフェースです。(for S2Mai)
 * @author Takahiro Ando
 * @version 1.0
 */
public interface GourmetcareeMai {

	/**
	 * 返信メール(スカウト)受信のお知らせを求職者へ送信します。
	 * @param scoutMailMaiDto メール情報
	 */
	public void sendScoutReturnInfoToMember(ScoutMailReceiveInfoToMemberMaiDto scoutMailReceiveInfoToMemberMaiDto);

	/**
	 * 返信メール(スカウト)送信のお知らせを顧客へ送信します。
	 * @param scoutMailMaiDto メール情報
	 */
	public void sendScoutReturnInfoToCustomer(ScoutMailSendInfoToCustomerMaiDto scoutMailSendInfoToCustomerMaiDto);

	/**
	 * スカウトメール送信のお知らせを管理者へ送信します。
	 * @param scoutMailSendInfoToAdminMaiDto
	 */
	public void sendScoutInfoToAdmin(ScoutMailSendInfoToAdminMaiDto scoutMailSendInfoToAdminMaiDto);

	/**
	 * スカウトメール送信のお知らせを顧客へ送信します。
	 * @param scoutMailSendInfoToCustomerMaiDto
	 */
	public void sendScoutInfoToCustomer(ScoutMailSendInfoToCustomerMaiDto scoutMailSendInfoToCustomerMaiDto);

	/**
	 * スカウトメール受信のお知らせを求職者へ送信します。
	 * @param scoutMailReceiveInfoToMemberMaiDto
	 */
	public void sendScoutInfoToMember(ScoutMailReceiveInfoToMemberMaiDto scoutMailReceiveInfoToMemberMaiDto);

	/**
	 * 応募テスト確認が完了のお知らせを担当者へ送信します。
	 * @param appTestConfMaiDto
	 */
	public void sendAppTestConf(AppTestConfMaiDto appTestConfMaiDto);

	/**
	 * パスワード変更完了のお知らせを顧客へ送信します。
	 * @param appTestConfMaiDto
	 */
	public void sendChangePassword(ChangePasswordMaiDto changePasswordMaiDto);


	/**
	 * 応募メールの返信メールを送信します。（応募者へのお知らせメール）
	 * @param applicationMailReturnToApplicantMaiDto
	 */
	public void sendApplicationReturnInfoToApplicant(ApplicationMailReturnToApplicantMaiDto applicationMailReturnToApplicantMaiDto);

	/**
	 * 応募メールの返信メールを送信します。（応募者へのお知らせメール）
	 * @param applicationMailReturnToApplicantMaiDto
	 */
	public void sendArbeitApplicationReturnInfoToApplicant(ApplicationMailReturnToApplicantMaiDto applicationMailReturnToApplicantMaiDto);

	/**
	 * 応募メールの返信メールを送信します。（顧客への送信確認メール）
	 * @param applicationMailReturnToCustomerMaiDto
	 */
	public void sendApplicationReturnInfoToCustomer(ApplicationMailReturnToCustomerMaiDto applicationMailReturnToCustomerMaiDto);

	/**
	 * プレ応募メールの返信メールを送信します。（顧客への送信確認メール）
	 * @param applicationMailReturnToCustomerMaiDto
	 */
	public void sendPreApplicationReturnInfoToCustomer(PreApplicationMailReturnToCustomerMaiDto preApplicationMailReturnToCustomerMaiDto);

	/**
	 * 応募メールの管理者確認メールを送信します。（管理者への送信確認メール）
	 * @param applicationMailReturnToAdminMaiDto
	 */
	public void sendApplicationInfoToAdmin(ApplicationMailReturnToAdminMaiDto applicationMailReturnToAdminMaiDto);


//	/**
//	 * アルバイト応募メールの管理者確認メールを送信します。（管理者への送信確認メール）
//	 * @param applicationMailReturnToAdminMaiDto
//	 */
//	public void sendArbeitApplicationInfoToAdmin(ApplicationMailReturnToAdminMaiDto applicationMailReturnToAdminMaiDto);

	/***************************************/
	/**
	 * 店舗見学応募メールの返信メールを送信します。（応募者へのお知らせメール）
	 * @param applicationMailReturnToApplicantMaiDto
	 */
	public void sendObservateApplicationReturnInfoToApplicant(ObservateApplicationMailReturnToApplicantMaiDto observateApplicationMailReturnToApplicantMaiDto);

	/**
	 * 店舗見学応募メールの返信メールを送信します。（顧客への送信確認メール）
	 * @param applicationMailReturnToCustomerMaiDto
	 */
	public void sendObservateApplicationReturnInfoToCustomer(ObservateApplicationMailReturnToCustomerMaiDto observateApplicationMailReturnToCustomerMaiDto);

	/**
	 * 店舗見学応募メールの管理者確認メールを送信します。（管理者への送信確認メール）
	 * @param applicationMailReturnToAdminMaiDto
	 */
	public void sendObservateApplicationInfoToAdmin(ObservateApplicationMailReturnToAdminMaiDto observateApplicationMailReturnToAdminMaiDto);

	/**
	 * パスワード変更確認メールを送信します。
	 * @param passwordReissueMailConfMaiDto
	 */
	public void sendPasswordReissueMailConf(PasswordReissueMailConfMaiDto passwordReissueMailConfMaiDto);

	public void sendPasswordReissueMailComp(PasswordReissueMailCompMaiDto passwordReissueMailCompMaiDto);





	/**
	 * アルバイト応募メールの返信メールを送信します。(顧客向け
	 */
	public void sendArbeitApplicationReturnInfoToCustomer(ApplicationMailReturnToCustomerMaiDto applicationMailReturnToCustomerMaiDto);

	/**
	 * 顧客からの問い合わせメールを送信します。
	 */
	public void sendContactToAdmin(ContactMaiDto contactMaiDto);
}
