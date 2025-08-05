package com.gourmetcaree.shop.logic.logic;

import static com.gourmetcaree.common.util.GourmetCareeUtil.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.framework.util.UUID;

import com.gourmetcaree.common.exception.InternalGourmetCareeSystemErrorException;
import com.gourmetcaree.common.logic.ValueToNameConvertLogic;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MAreaConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants.CommunicationMailKbn;
import com.gourmetcaree.db.common.entity.MCustomer;
import com.gourmetcaree.db.common.entity.MMember;
import com.gourmetcaree.db.common.entity.TApplication;
import com.gourmetcaree.db.common.entity.TArbeitApplication;
import com.gourmetcaree.db.common.entity.TMail;
import com.gourmetcaree.db.common.entity.TObservateApplication;
import com.gourmetcaree.db.common.entity.TPreApplication;
import com.gourmetcaree.db.common.entity.TWeb;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.CustomerService;
import com.gourmetcaree.db.common.service.CustomerSubMailService;
import com.gourmetcaree.db.common.service.MailService;
import com.gourmetcaree.db.common.service.MemberService;
import com.gourmetcaree.db.common.service.WebService;
import com.gourmetcaree.shop.logic.dto.mai.AppTestConfMaiDto;
import com.gourmetcaree.shop.logic.dto.mai.ApplicationMailReturnToAdminMaiDto;
import com.gourmetcaree.shop.logic.dto.mai.ApplicationMailReturnToApplicantMaiDto;
import com.gourmetcaree.shop.logic.dto.mai.ApplicationMailReturnToCustomerMaiDto;
import com.gourmetcaree.shop.logic.dto.mai.ChangePasswordMaiDto;
import com.gourmetcaree.shop.logic.dto.mai.ContactMaiDto;
import com.gourmetcaree.shop.logic.dto.mai.ObservateApplicationMailReturnToAdminMaiDto;
import com.gourmetcaree.shop.logic.dto.mai.PasswordReissueMailCompMaiDto;
import com.gourmetcaree.shop.logic.dto.mai.PasswordReissueMailConfMaiDto;
import com.gourmetcaree.shop.logic.mai.GourmetcareeMai;
import com.gourmetcaree.shop.logic.property.ApplicationMailProperty;
import com.gourmetcaree.shop.logic.property.ContactMailProperty;
import com.gourmetcaree.shop.logic.property.SendMailProperty;
/**
 * メール送信に関するロジッククラスです。
 * @author Makoto Otani
 *
 */
@Component(instance=InstanceType.SINGLETON)
public class SendMailLogic extends AbstractShopLogic {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(SendMailLogic.class);

	/** 応募者取得SQLフォーマット */
	private static final String APPLICANT_NAME_SQL_FORMAT = "SELECT APP.name FROM %s APP INNER JOIN t_mail MAIL ON APP.id = MAIL.%s WHERE MAIL.id = ? AND APP.delete_flg = ? AND MAIL.delete_flg = ?";

	/** プレ応募者取得SQLフォーマット */
	private static final String PRE_APPLICANT_NAME_SQL_FORMAT = "SELECT APP.member_id FROM %s APP INNER JOIN t_mail MAIL ON APP.id = MAIL.%s WHERE MAIL.id = ? AND APP.delete_flg = ? AND MAIL.delete_flg = ?";

	/** 質問者取得SQLフォーマット */
	private static final String OBSERVATE_APPLICANT_NAME_SQL_FORMAT = "SELECT APP.name FROM %s APP INNER JOIN t_mail MAIL ON APP.id = MAIL.%s WHERE MAIL.id = ? AND APP.delete_flg = ? AND MAIL.delete_flg = ?";

	/** メール送信 */
	@Resource
	protected GourmetcareeMai gourmetcareeMai;

	/** 応募ロジック */
	@Resource
	protected ApplicationLogic applicationLogic;

	/** プレ応募ロジック */
	@Resource
	protected PreApplicationLogic preApplicationLogic;

	/** 顧客サービス */
	@Resource
	protected CustomerService customerService;

	/** 値から名前の文字列に変換するロジッククラス */
	@Resource
	protected ValueToNameConvertLogic valueToNameConvertLogic;

	/** メールID */
	@Resource
	protected MailService mailService;

	/** 会員サービス */
	@Resource
	protected MemberService memberService;

	/** Webデータサービス */
	@Resource
	private WebService webService;

	@Resource
	private CustomerSubMailService customerSubMailService;


	/** メールの送信方法パターン */
	public enum MailPattern {
		/** 通常のメールボックスからの返信 */
		NORMAL_REPLY,
		/** 応募詳細からの返信 */
		FIRST_MAIL_REPLY
	}

	/**
	 * 店舗管理システムのPCログインURLをプロパティから取得します。
	 * @return PCログインURL
	 */
	public String getPCLoginURL() {
		return getMailProerties().getProperty("gc.shop.pcLoginUrl");
	}

	/**
	 * 店舗管理システムのPCログインURLをプロパティから取得します。
	 * @return PCログインURL
	 */
	public String getMobileLoginURL() {
		return getMailProerties().getProperty("gc.shop.mobileLoginUrl");
	}

	/**
	 * MYページのPCログインURLをプロパティから取得します。
	 * @return MYページのPCログインURL
	 */
	public String getMemberPCLoginURL(int areaCd) {
		String loginURLKey = "gc.member.pcLoginUrl" + areaCd;
		return getMailProerties().getProperty(loginURLKey);
	}

	/**
	 * MYページの携帯ログインURLをプロパティから取得します。
	 * @return MYページの携帯ログインURL
	 */
	public String getMemberMobileLoginURL(int areaCd) {
		String loginURLKey = "gc.member.mobileLoginUrl" + areaCd;
		return getMailProerties().getProperty(loginURLKey);
	}

	/**
	 * 非会員のメール確認URL（PC）をプロパティから取得します。
	 * @return 非会員のメール確認URL（PC）
	 */
	public String getNonMemberPCMailURL(int areaCd) {
		String loginURLKey = "gc.non.member.pcMailUrl" + areaCd;
		return getMailProerties().getProperty(loginURLKey);
	}


	/**
	 * 非会員のアルバイトメール確認URLをプロパティから取得します。
	 * @param terminalKbn 端末区分
	 * @return 非会員のアルバイトメール確認URL
	 */
	private String getNonMemberArbeitMailUrl(int terminalKbn) {
		String key;
		if (terminalKbn == MTypeConstants.TerminalKbn.PC_VALUE) {
			key = "gc.non.member.pcArbeitMailUrl";
		} else {
			key = "gc.non_member.mobileArbeitMailUrl";
		}

		return getMailProerties().getProperty(key);
	}

	/**
	 * 非会員のメール確認URL(店舗見学)（PC）をプロパティから取得します。
	 * @return 非会員のメール確認URL（PC）
	 */
	public String getNonMemberPCObservateMailURL(int areaCd) {
		String loginURLKey = "gc.non.member.pcObservationMailUrl" + areaCd;
		return getMailProerties().getProperty(loginURLKey);
	}

	/**
	 * 非会員のメール確認URL（モバイル）をプロパティから取得します。
	 * @return 非会員のメール確認URL（モバイル）
	 */
	public String getNonMemberMobileMailURL(int areaCd) {
		String loginURLKey = "gc.non_member.mobileMailUrl" + areaCd;
		return getMailProerties().getProperty(loginURLKey);
	}

	/**
	 * 非会員のメール確認URL(店舗見学)（モバイル）をプロパティから取得します。
	 * @return 非会員のメール確認URL（モバイル）
	 */
	public String getNonMemberMobileObservateMailURL(int areaCd) {
		String loginURLKey = "gc.non_member.mobileObservationMailUrl" + areaCd;
		return getMailProerties().getProperty(loginURLKey);
	}

	/**
	 * お問い合わせのメールアドレスをプロパティから取得します。
	 * @param areaCd エリアコード
	 * @return お問い合わせメールアドレス
	 */
	public String getInfoAddress(int areaCd) {
		String infoAddressKey = "gc.mail.infoAddress" + areaCd;
		return getMailProerties().getProperty(infoAddressKey);
	}

	/**
	 * グルメキャリーのサイトURLを取得します。
	 * @param areaCd エリアコード
	 * @return グルメキャリーURL
	 */
	public String getSiteUrl(int areaCd) {
		String siteUrlKey = "gc.front.siteUrl" + areaCd;
		return getMailProerties().getProperty(siteUrlKey);
	}

	/**
	 * 応募テスト確認完了のメールを担当者へ送信します。
	 * @param property メール送信プロパティ
	 */
	public void sendAppTestConfMail(SendMailProperty property) {

		// 引数チェック
		checkEmptyProperty(property);
		if (property.tApplicationTest == null) {
			throw new IllegalArgumentException("引数がnullです。");
		}

		// 管理アドレスの取得
		String mailAddress = getTestOuboAddress();
		// 管理アドレス名の取得
		String mailName = getTestOuboName();


		// 応募管理メールアドレスを取得
		if (StringUtil.isEmpty(mailAddress)) {
			// メールアドレスが存在しないため未処理
			log.info("応募メール完了を送信できませんでした");
			return;
		}

		AppTestConfMaiDto mailDto = new AppTestConfMaiDto();

		// 送信先を保持するリスト
		List<String> toAddressList = new ArrayList<String>();

		// 送信先に応募アドレスをセット
		toAddressList.add(mailAddress);

		// 送信先に送信先アドレスをセット
		toAddressList.add(property.tApplicationTest.mainMail);

		// サブアドレスが登録されていれば送信先にセット
		if (!StringUtil.isEmpty(property.tApplicationTest.subMail)) {
			toAddressList.add(property.tApplicationTest.subMail);
		}

		// 送信元に応募アドレスをセット
		try {
			mailDto.setFrom(mailAddress, mailName);
		// アドレスがセットできない場合は、初期値で送信
		} catch (UnsupportedEncodingException e) {
			// 送信元のセットに失敗した場合はエラーを送信する。（mailProperties.diconの値がセットされる）
			log.fatal("応募テスト確認完了のメールを担当者へ送信時、送信元がセットできませんでした。" + e);
		}
		// 顧客ID
		mailDto.setCustomerId(String.valueOf(property.tApplicationTest.customerId));
		// 顧客名
		mailDto.setCustomerName(StringUtils.defaultIfEmpty(property.customerName, ""));

		// 送信先をセットして応募メールを送信
		for (String toAddress : toAddressList) {
			// 送信先をセット
			mailDto.resetTo();
			mailDto.addTo(toAddress);
			// メール送信処理実行
			gourmetcareeMai.sendAppTestConf(mailDto);
		}
	}

	/**
	 * パスワード変更完了のメールを顧客へ送信します。
	 * @param property メール送信プロパティ
	 */
	public void sendChangePassword(SendMailProperty property) {

		// 引数チェック
		checkEmptyProperty(property);
		if (property.mCustomer == null) {
			throw new IllegalArgumentException("引数がnullです。");
		}

		// 管理アドレスの取得
		String mailAddress = getKanriAddress();
		// 管理アドレスの取得
		String mailName = getKanriName();

		// 応募管理メールアドレスを取得
		if (StringUtil.isEmpty(mailAddress)) {
			// メールアドレスが存在しないため未処理
			log.info("応募メール完了を送信できませんでした");
			return;
		}

		ChangePasswordMaiDto mailDto = new ChangePasswordMaiDto();

		// 送信先を保持するリスト
		List<String> toAddressList = new ArrayList<String>();

		// 送信先に応募アドレスをセット
		toAddressList.add(mailAddress);

		// 送信先に送信先アドレスをセット
		toAddressList.add(property.mCustomer.mainMail);

		// サブアドレスが登録されていれば送信先にセット
		List<String> subMailList = customerSubMailService.getReceptionSubMail(property.mCustomer.id);
		if (CollectionUtils.isNotEmpty(subMailList)) {
			toAddressList.addAll(subMailList);
		}

		// 送信元に管理アドレスをセット
		try {
			mailDto.setFrom(mailAddress, mailName);
		// アドレスがセットできない場合は、初期値で送信
		} catch (UnsupportedEncodingException e) {
			// 送信元のセットに失敗した場合はエラーを送信する。（mailProperties.diconの値がセットされる）
			log.fatal("パスワード変更完了のメールを顧客へ送信時、送信元がセットできませんでした。" + e);
		}
		// 顧客名
		mailDto.setCustomerName(property.mCustomer.customerName);
		// PC版ログインアドレス
		mailDto.setPcPath(getPCLoginURL());
		// 携帯版ログインアドレス
		mailDto.setMobilePath(getMobileLoginURL());

		// フッターをセット
		mailDto.setInfoAddress(getInfoAddress(getAreaCd()));
		mailDto.setSiteURL(getSiteUrl(getAreaCd()));

		// 送信先をセットして応募メールを送信
		for (String toAddress : toAddressList) {

			if (StringUtil.isEmpty(toAddress)) {
				continue;
			}
			// 送信先をセット
			mailDto.resetTo();
			mailDto.addTo(toAddress);
			// メール送信処理実行
			gourmetcareeMai.sendChangePassword(mailDto);
		}
	}

	/**
	 * メールプロパティを取得します。
	 * @return メールプロパティ
	 */
	public Properties getMailProerties() {
		return ResourceUtil.getProperties("sendMail.properties");
	}

	/**
	 * 応募の管理メールアドレスをプロパティから取得します。
	 * @return 応募用管理アドレス
	 */
	public String getOuboAddress() {
		String ouboAddressKey = "gc.mail.ouboAddress" + getAreaCd();
		return getMailProerties().getProperty(ouboAddressKey);
	}

	/**
	 * 応募の管理メール名をプロパティから取得します。
	 * @return 応募用管理メール名
	 */
	public String getOuboName() {
		String ouboNameKey = "gc.mail.ouboName" + getAreaCd();
		return getMailProerties().getProperty(ouboNameKey);
	}

	/**
	 * 応募テストの管理メールアドレスをプロパティから取得します。
	 * @return 応募テスト用管理アドレス
	 */
	public String getTestOuboAddress() {
		String testOuboAddressKey = "gc.mail.ouboTestAddress";
		return getMailProerties().getProperty(testOuboAddressKey);
	}

	/**
	 * 応募テストの管理メール名をプロパティから取得します。
	 * @return 応募テスト用管理メール名
	 */
	public String getTestOuboName() {
		String testOuboNameKey = "gc.mail.ouboTestName";
		return getMailProerties().getProperty(testOuboNameKey);
	}

	/**
	 * 管理メールアドレスをプロパティから取得します。
	 * @return 管理アドレス
	 */
	public String getKanriAddress() {
		String kanriAddressKey = "gc.mail.kanriAddress" + getAreaCd();
		return getMailProerties().getProperty(kanriAddressKey);
	}

	/**
	 * 管理メールアドレスをプロパティから取得します。
	 * @return 管理アドレス
	 */
	public String getKanriAddress(int areaCd) {
		String kanriAddressKey = "gc.mail.kanriAddress" + areaCd;
		return getMailProerties().getProperty(kanriAddressKey);
	}

	/**
	 * 管理メール名をプロパティから取得します。
	 * @return 管理メール名
	 */
	public String getKanriName() {
		String kanriNameKey = "gc.mail.kanriName" + getAreaCd();
		return getMailProerties().getProperty(kanriNameKey);
	}

	/**
	 * 管理メール名をプロパティから取得します。
	 * @return 管理メール名
	 */
	public String getKanriName(int areaCd) {
		String kanriNameKey = "gc.mail.kanriName" + areaCd;
		return getMailProerties().getProperty(kanriNameKey);
	}

	/**
	 * プロパティがnullの場合は、エラーを返す。
	 * @param property チェックするプロパティ
	 */
	private void checkEmptyProperty(SendMailProperty property) {

		// プロパティがnullの場合はエラー
		if (property == null) {
			throw new IllegalArgumentException("引数がnullです。");
		}
	}



	/**
	 * グルメdeバイトのFROM名を取得
	 * @return グルメdeバイトのFROM名
	 */
	private String getArbeitFromName() {
		return getMailProerties().getProperty("arbeit.mail.infoSenderName");
	}

	/**
	 * グルメdeバイトのinfoメールアドレスを取得
	 * @return グルメdeバイトのinfoメールアドレス
	 */
	private String getArbeitInfoAddress() {
		return getMailProerties().getProperty("arbeit.mail.infoAddress");
	}



	/**
	 * 応募メールへの返信を行います。
	 * @param property
	 * @throws WNoResultException
	 */
	public void doReplyObservateApplicationMail(ApplicationMailProperty property, MailPattern mailPattern) throws WNoResultException {

		TMail originalMailEntity = null;

		//返信元メールの情報を取得
		if (mailPattern == MailPattern.FIRST_MAIL_REPLY) {
			//初回メールは削除フラグの有無に関係なく取得する。
			originalMailEntity = mailService.getEntityIgnoreDeleteByUser(property.originalMailId, getCustomerId());
		} else {
			originalMailEntity = mailService.getEntityByUser(property.originalMailId, getCustomerId());
		}

		//メールステータスが返信済みでなければアップデート
		if (originalMailEntity.mailStatus != MTypeConstants.MailStatus.REPLIED) {
			originalMailEntity.mailStatus = MTypeConstants.MailStatus.REPLIED;
			mailService.update(originalMailEntity);
		}

		//送信日時
		Date sendDateTime = new Date();
		TObservateApplication applicatonEntity = applicationLogic.findByIdFromObservateApplication(originalMailEntity.observateApplicationId);
		String observationName = valueToNameConvertLogic.convertToTypeName(MTypeConstants.ObservationKbn.TYPE_CD, applicatonEntity.observationKbn);

		//応募者にメールを送信する
		TMail toApplicantEntity = new TMail();
		toApplicantEntity.mailKbn = MTypeConstants.MailKbn.OBSERVATE_APPLICATION;
		toApplicantEntity.sendKbn = MTypeConstants.SendKbn.RECEIVE;
		toApplicantEntity.senderKbn =MTypeConstants.SenderKbn.CUSTOMER;
		toApplicantEntity.fromId = getCustomerId();
		toApplicantEntity.fromName = applicatonEntity.applicationName;
		toApplicantEntity.toId = originalMailEntity.fromId;
		toApplicantEntity.toName = originalMailEntity.fromName;
		toApplicantEntity.subject = property.subject;
		toApplicantEntity.body = property.body;
		toApplicantEntity.mailStatus = MTypeConstants.MailStatus.UNOPENED;
		toApplicantEntity.sendDatetime = sendDateTime;
		toApplicantEntity.accessCd = UUID.create();
		toApplicantEntity.parentMailId = property.originalMailId;
		toApplicantEntity.observateApplicationId = originalMailEntity.observateApplicationId;
		mailService.insert(toApplicantEntity);

		//顧客に送信箱用メールを送信する
		TMail toCustomerEntity = new TMail();
		toCustomerEntity.mailKbn = MTypeConstants.MailKbn.OBSERVATE_APPLICATION;
		toCustomerEntity.sendKbn = MTypeConstants.SendKbn.SEND;
		toCustomerEntity.senderKbn =MTypeConstants.SenderKbn.CUSTOMER;
		toCustomerEntity.fromId = getCustomerId();
		toCustomerEntity.fromName = applicatonEntity.applicationName;
		toCustomerEntity.toId = originalMailEntity.fromId;
		toCustomerEntity.toName = originalMailEntity.fromName;
		toCustomerEntity.subject = property.subject;
		toCustomerEntity.body = property.body;
		toCustomerEntity.mailStatus = MTypeConstants.MailStatus.UNOPENED;
		toCustomerEntity.sendDatetime = sendDateTime;
		toCustomerEntity.accessCd = UUID.create();
		toCustomerEntity.parentMailId = property.originalMailId;
		toCustomerEntity.observateApplicationId = originalMailEntity.observateApplicationId;
		toCustomerEntity.receiveId = toApplicantEntity.id;
		mailService.insert(toCustomerEntity);

		//会員か非会員かで処理を分岐します。会員の場合は退会している場合には非会員と同じメールを送ります。
		if (originalMailEntity.senderKbn == MTypeConstants.SenderKbn.NO_MEMBER) {
			sendObservateApplicationRetToNoMember(applicatonEntity, toApplicantEntity);

		} else if (originalMailEntity.senderKbn == MTypeConstants.SenderKbn.MEMBER) {

			try {
				MMember memberEntity = memberService.findById(originalMailEntity.fromId);
				sendObservateApplicationRetToMember(memberEntity, applicatonEntity);
			} catch (SNoResultException e) {
				sendObservateApplicationRetToNoMember(applicatonEntity, toApplicantEntity);
			}

		} else {
			//送信者区分が不正の場合
			throw new InternalGourmetCareeSystemErrorException("送信者区分が不正です。");
		}

		try {
			//顧客にメールを送信。
			MCustomer customerEntity = customerService.findById(getCustomerId());
			sendObservateApplicationRetToCustomer(originalMailEntity, customerEntity, applicatonEntity);
		} catch (SNoResultException e) {
			throw new InternalGourmetCareeSystemErrorException("応募メール返信時に顧客データ取得に失敗しました。");
		}

		//管理者にメールを送信
		sendObservateApplicationRetToAdmin(originalMailEntity, applicatonEntity, toApplicantEntity, observationName);
	}


	/**
	 * 応募メールへの返信を行います。
	 * @param property
	 * @throws WNoResultException
	 */
	public void doReplyApplicationMail(ApplicationMailProperty property, MailPattern mailPattern) throws WNoResultException {

		TMail originalMailEntity = null;

		//返信元メールの情報を取得
		if (mailPattern == MailPattern.FIRST_MAIL_REPLY) {
			//初回メールは削除フラグの有無に関係なく取得する。
			originalMailEntity = mailService.getEntityIgnoreDeleteByUser(property.originalMailId, getCustomerId());
		} else {
			originalMailEntity = mailService.getEntityByUser(property.originalMailId, getCustomerId());
		}

		//メールステータスが返信済みでなければアップデート
		if (originalMailEntity.mailStatus != MTypeConstants.MailStatus.REPLIED) {
			originalMailEntity.mailStatus = MTypeConstants.MailStatus.REPLIED;
			mailService.update(originalMailEntity);
		}

		//送信日時
		Date sendDateTime = new Date();
		TApplication applicatonEntity = applicationLogic.findByIdFromApplication(originalMailEntity.applicationId);

		//応募者にメールを送信する
		TMail toApplicantEntity = new TMail();
		toApplicantEntity.mailKbn = MTypeConstants.MailKbn.APPLICCATION;
		toApplicantEntity.sendKbn = MTypeConstants.SendKbn.RECEIVE;
		toApplicantEntity.senderKbn =MTypeConstants.SenderKbn.CUSTOMER;
		toApplicantEntity.fromId = getCustomerId();
		toApplicantEntity.fromName = applicatonEntity.applicationName;
		toApplicantEntity.toId = originalMailEntity.fromId;
		toApplicantEntity.toName = originalMailEntity.fromName;
		toApplicantEntity.subject = property.subject;
		toApplicantEntity.body = property.body;
		toApplicantEntity.mailStatus = MTypeConstants.MailStatus.UNOPENED;
		toApplicantEntity.sendDatetime = sendDateTime;
		toApplicantEntity.accessCd = UUID.create();
		toApplicantEntity.parentMailId = property.originalMailId;
		toApplicantEntity.applicationId = originalMailEntity.applicationId;
		mailService.insert(toApplicantEntity);

		//顧客に送信箱用メールを送信する
		TMail toCustomerEntity = new TMail();
		toCustomerEntity.mailKbn = MTypeConstants.MailKbn.APPLICCATION;
		toCustomerEntity.sendKbn = MTypeConstants.SendKbn.SEND;
		toCustomerEntity.senderKbn =MTypeConstants.SenderKbn.CUSTOMER;
		toCustomerEntity.fromId = getCustomerId();
		toCustomerEntity.fromName = applicatonEntity.applicationName;
		toCustomerEntity.toId = originalMailEntity.fromId;
		toCustomerEntity.toName = originalMailEntity.fromName;
		toCustomerEntity.subject = property.subject;
		toCustomerEntity.body = property.body;
		toCustomerEntity.mailStatus = MTypeConstants.MailStatus.UNOPENED;
		toCustomerEntity.sendDatetime = sendDateTime;
		toCustomerEntity.accessCd = UUID.create();
		toCustomerEntity.parentMailId = property.originalMailId;
		toCustomerEntity.applicationId = originalMailEntity.applicationId;
		toCustomerEntity.receiveId = toApplicantEntity.id;
		mailService.insert(toCustomerEntity);

		//会員か非会員かで処理を分岐します。会員の場合は退会している場合には非会員と同じメールを送ります。
		if (originalMailEntity.senderKbn == MTypeConstants.SenderKbn.NO_MEMBER) {
			sendApplicationRetToNoMember(applicatonEntity, toApplicantEntity);

		} else if (originalMailEntity.senderKbn == MTypeConstants.SenderKbn.MEMBER) {

			try {
				MMember memberEntity = memberService.findById(originalMailEntity.fromId);
				sendApplicationRetToMember(memberEntity, applicatonEntity);
			} catch (SNoResultException e) {
				sendApplicationRetToNoMember(applicatonEntity, toApplicantEntity);
			}

		} else {
			//送信者区分が不正の場合
			throw new InternalGourmetCareeSystemErrorException("送信者区分が不正です。");
		}

		try {
			//顧客にメールを送信。
			MCustomer customerEntity = customerService.findById(getCustomerId());
			sendApplicationRetToCustomer(originalMailEntity, customerEntity, applicatonEntity);
		} catch (SNoResultException e) {
			throw new InternalGourmetCareeSystemErrorException("応募メール返信時に顧客データ取得に失敗しました。");
		}

		//管理者にメールを送信
		sendApplicationRetToAdmin(originalMailEntity, applicatonEntity, toApplicantEntity);
	}


	/**
	 * プレ応募メールへの返信を行います。
	 * @param property
	 * @throws WNoResultException
	 */
	public void doReplyPreApplicationMail(ApplicationMailProperty property, MailPattern mailPattern) throws WNoResultException {

		TMail originalMailEntity = null;

		//返信元メールの情報を取得
		if (mailPattern == MailPattern.FIRST_MAIL_REPLY) {
			//初回メールは削除フラグの有無に関係なく取得する。
			originalMailEntity = mailService.getEntityIgnoreDeleteByUser(property.originalMailId, getCustomerId());
		} else {
			originalMailEntity = mailService.getEntityByUser(property.originalMailId, getCustomerId());
		}

		//メールステータスが返信済みでなければアップデート
		if (originalMailEntity.mailStatus != MTypeConstants.MailStatus.REPLIED) {
			originalMailEntity.mailStatus = MTypeConstants.MailStatus.REPLIED;
			mailService.update(originalMailEntity);
		}

		//送信日時
		Date sendDateTime = new Date();
		TPreApplication applicatonEntity = preApplicationLogic.findByIdFromPreApplication(originalMailEntity.applicationId);
		TWeb tWeb = webService.findById(applicatonEntity.webId);

		//応募者にメールを送信する
		TMail toApplicantEntity = new TMail();
		toApplicantEntity.mailKbn = MTypeConstants.MailKbn.PRE_APPLICCATION;
		toApplicantEntity.sendKbn = MTypeConstants.SendKbn.RECEIVE;
		toApplicantEntity.senderKbn =MTypeConstants.SenderKbn.CUSTOMER;
		toApplicantEntity.fromId = getCustomerId();
		toApplicantEntity.fromName = tWeb.manuscriptName;
		toApplicantEntity.toId = originalMailEntity.fromId;
		toApplicantEntity.toName = originalMailEntity.fromName;
		toApplicantEntity.subject = property.subject;
		toApplicantEntity.body = property.body;
		toApplicantEntity.mailStatus = MTypeConstants.MailStatus.UNOPENED;
		toApplicantEntity.sendDatetime = sendDateTime;
		toApplicantEntity.accessCd = UUID.create();
		toApplicantEntity.parentMailId = property.originalMailId;
		toApplicantEntity.applicationId = originalMailEntity.applicationId;
		mailService.insert(toApplicantEntity);

		//顧客に送信箱用メールを送信する
		TMail toCustomerEntity = new TMail();
		toCustomerEntity.mailKbn = MTypeConstants.MailKbn.PRE_APPLICCATION;
		toCustomerEntity.sendKbn = MTypeConstants.SendKbn.SEND;
		toCustomerEntity.senderKbn =MTypeConstants.SenderKbn.CUSTOMER;
		toCustomerEntity.fromId = getCustomerId();
		toCustomerEntity.fromName = tWeb.manuscriptName;
		toCustomerEntity.toId = originalMailEntity.fromId;
		toCustomerEntity.toName = originalMailEntity.fromName;
		toCustomerEntity.subject = property.subject;
		toCustomerEntity.body = property.body;
		toCustomerEntity.mailStatus = MTypeConstants.MailStatus.UNOPENED;
		toCustomerEntity.sendDatetime = sendDateTime;
		toCustomerEntity.accessCd = UUID.create();
		toCustomerEntity.parentMailId = property.originalMailId;
		toCustomerEntity.applicationId = originalMailEntity.applicationId;
		toCustomerEntity.receiveId = toApplicantEntity.id;
		mailService.insert(toCustomerEntity);

		MMember memberEntity = memberService.findById(originalMailEntity.fromId);
		sendPreApplicationRetToMember(memberEntity, tWeb.manuscriptName, tWeb.areaCd);


		try {
			//顧客にメールを送信。
			MCustomer customerEntity = customerService.findById(getCustomerId());
			sendPreApplicationRetToCustomer(originalMailEntity, customerEntity, applicatonEntity);
		} catch (SNoResultException e) {
			throw new InternalGourmetCareeSystemErrorException("応募メール返信時に顧客データ取得に失敗しました。");
		}

		//管理者にメールを送信
		sendPreApplicationRetToAdmin(originalMailEntity, applicatonEntity, toApplicantEntity);
	}



	/**
	 * 応募メールへの返信を行います。
	 * @param property
	 * @throws WNoResultException
	 */
	public void doReplyArbeitMail(ApplicationMailProperty property, MailPattern mailPattern) throws WNoResultException {

		TMail originalMailEntity = null;

		//返信元メールの情報を取得
		if (mailPattern == MailPattern.FIRST_MAIL_REPLY) {
			//初回メールは削除フラグの有無に関係なく取得する。
			originalMailEntity = mailService.getEntityIgnoreDeleteByUser(property.originalMailId, getCustomerId());
		} else {
			originalMailEntity = mailService.getEntityByUser(property.originalMailId, getCustomerId());
		}

		//メールステータスが返信済みでなければアップデート
		if (originalMailEntity.mailStatus != MTypeConstants.MailStatus.REPLIED) {
			originalMailEntity.mailStatus = MTypeConstants.MailStatus.REPLIED;
			mailService.update(originalMailEntity);
		}

		//送信日時
		Date sendDateTime = new Date();
		TArbeitApplication applicatonEntity = applicationLogic.findByIdFromArbeitApplication(originalMailEntity.arbeitApplicationId);

		//応募者にメールを送信する
		TMail toApplicantEntity = new TMail();
		toApplicantEntity.mailKbn = MTypeConstants.MailKbn.ARBEIT_APPLICATION;
		toApplicantEntity.sendKbn = MTypeConstants.SendKbn.RECEIVE;
		toApplicantEntity.senderKbn =MTypeConstants.SenderKbn.CUSTOMER;
		toApplicantEntity.fromId = getCustomerId();
		toApplicantEntity.fromName = applicatonEntity.applicationName;
		toApplicantEntity.toId = originalMailEntity.fromId;
		toApplicantEntity.toName = originalMailEntity.fromName;
		toApplicantEntity.subject = property.subject;
		toApplicantEntity.body = property.body;
		toApplicantEntity.mailStatus = MTypeConstants.MailStatus.UNOPENED;
		toApplicantEntity.sendDatetime = sendDateTime;
		toApplicantEntity.accessCd = UUID.create();
		toApplicantEntity.parentMailId = property.originalMailId;
		toApplicantEntity.arbeitApplicationId = originalMailEntity.arbeitApplicationId;
		mailService.insert(toApplicantEntity);

		//顧客に送信箱用メールを送信する
		TMail toCustomerEntity = new TMail();
		toCustomerEntity.mailKbn = MTypeConstants.MailKbn.ARBEIT_APPLICATION;
		toCustomerEntity.sendKbn = MTypeConstants.SendKbn.SEND;
		toCustomerEntity.senderKbn =MTypeConstants.SenderKbn.CUSTOMER;
		toCustomerEntity.fromId = getCustomerId();
		toCustomerEntity.fromName = applicatonEntity.applicationName;
		toCustomerEntity.toId = originalMailEntity.fromId;
		toCustomerEntity.toName = originalMailEntity.fromName;
		toCustomerEntity.subject = property.subject;
		toCustomerEntity.body = property.body;
		toCustomerEntity.mailStatus = MTypeConstants.MailStatus.UNOPENED;
		toCustomerEntity.sendDatetime = sendDateTime;
		toCustomerEntity.accessCd = UUID.create();
		toCustomerEntity.parentMailId = property.originalMailId;
		toCustomerEntity.arbeitApplicationId = originalMailEntity.arbeitApplicationId;
		toCustomerEntity.receiveId = toApplicantEntity.id;
		mailService.insert(toCustomerEntity);

		// バイト用メールでは、全て非会員扱いとする。
		if (originalMailEntity.senderKbn != null) {
			sendArbeitApplicationRetToNoMember(applicatonEntity, toApplicantEntity);

		} else {
			//送信者区分が不正の場合
			throw new InternalGourmetCareeSystemErrorException("送信者区分が不正です。");
		}

		try {
			//顧客にメールを送信。
			MCustomer customerEntity = customerService.findById(getCustomerId());
			sendArbeitApplicationRetToCustomer(originalMailEntity, customerEntity, applicatonEntity);
		} catch (SNoResultException e) {
			throw new InternalGourmetCareeSystemErrorException("応募メール返信時に顧客データ取得に失敗しました。");
		}

	}

	/**
	 * 応募メール返信時の確認メールを管理者に送信します。
	 * @param originalMailEntity
	 * @param applicatonEntity
	 * @param toApplicantEntity
	 * @throws WNoResultException
	 * @throws NumberFormatException
	 */
	private void sendApplicationRetToAdmin(TMail originalMailEntity, TApplication applicatonEntity,
			TMail toApplicantEntity) throws NumberFormatException, WNoResultException {

		String customerName = valueToNameConvertLogic.convertToCustomerName(new String[]{Integer.toString(getCustomerId())});

		ApplicationMailReturnToAdminMaiDto adminDto = new ApplicationMailReturnToAdminMaiDto();
		adminDto.addTo(getOuboAddress());
		try {
			adminDto.setFrom(getOuboAddress(), getOuboName());
		// アドレスがセットできない場合は、初期値で送信
		} catch (UnsupportedEncodingException e) {
			// 送信元のセットに失敗した場合はエラーを送信する。（mailProperties.diconの値がセットされる）
			log.fatal("応募メール返信時の確認メールを管理者に送信時、送信元がセットできませんでした。" + e);
		}
		adminDto.setApplicationId(Integer.toString(applicatonEntity.id));
		adminDto.setApplicantName(originalMailEntity.fromName);
		adminDto.setCustomerId(Integer.toString(getCustomerId()));
		adminDto.setCustomerName(customerName);
		adminDto.setMailId(Integer.toString(toApplicantEntity.id));

		gourmetcareeMai.sendApplicationInfoToAdmin(adminDto);
	}

	/**
	 * プレ応募メール返信時の確認メールを管理者に送信します。
	 * @param originalMailEntity
	 * @param applicatonEntity
	 * @param toApplicantEntity
	 * @throws WNoResultException
	 * @throws NumberFormatException
	 */
	private void sendPreApplicationRetToAdmin(TMail originalMailEntity, TPreApplication applicatonEntity,
			TMail toApplicantEntity) throws NumberFormatException, WNoResultException {

		String customerName = valueToNameConvertLogic.convertToCustomerName(new String[]{Integer.toString(getCustomerId())});

		ApplicationMailReturnToAdminMaiDto adminDto = new ApplicationMailReturnToAdminMaiDto();
		adminDto.addTo(getOuboAddress());
		try {
			adminDto.setFrom(getOuboAddress(), getOuboName());
		// アドレスがセットできない場合は、初期値で送信
		} catch (UnsupportedEncodingException e) {
			// 送信元のセットに失敗した場合はエラーを送信する。（mailProperties.diconの値がセットされる）
			log.fatal("応募メール返信時の確認メールを管理者に送信時、送信元がセットできませんでした。" + e);
		}
		adminDto.setApplicationId(Integer.toString(applicatonEntity.id));
		adminDto.setApplicantName(originalMailEntity.fromName);
		adminDto.setCustomerId(Integer.toString(getCustomerId()));
		adminDto.setCustomerName(customerName);
		adminDto.setMailId(Integer.toString(toApplicantEntity.id));

		gourmetcareeMai.sendApplicationInfoToAdmin(adminDto);
	}

	/**
	 * 店舗見学応募メール返信時の確認メールを管理者に送信します。
	 * @param originalMailEntity
	 * @param applicatonEntity
	 * @param toApplicantEntity
	 * @throws WNoResultException
	 * @throws NumberFormatException
	 */
	private void sendObservateApplicationRetToAdmin(TMail originalMailEntity, TObservateApplication applicatonEntity,
			TMail toApplicantEntity, String observationName) throws NumberFormatException, WNoResultException {

		String customerName = valueToNameConvertLogic.convertToCustomerName(new String[]{Integer.toString(getCustomerId())});

		ObservateApplicationMailReturnToAdminMaiDto adminDto = new ObservateApplicationMailReturnToAdminMaiDto();
		adminDto.addTo(getOuboAddress());
		try {
			adminDto.setFrom(getOuboAddress(), getOuboName());
		// アドレスがセットできない場合は、初期値で送信
		} catch (UnsupportedEncodingException e) {
			// 送信元のセットに失敗した場合はエラーを送信する。（mailProperties.diconの値がセットされる）
			log.fatal("応募メール返信時の確認メールを管理者に送信時、送信元がセットできませんでした。" + e);
		}
		adminDto.setApplicationId(Integer.toString(applicatonEntity.id));
		adminDto.setApplicantName(originalMailEntity.fromName);
		adminDto.setCustomerId(Integer.toString(getCustomerId()));
		adminDto.setCustomerName(customerName);
		adminDto.setMailId(Integer.toString(toApplicantEntity.id));
		adminDto.setObservationName(observationName);

		gourmetcareeMai.sendObservateApplicationInfoToAdmin(adminDto);
	}



	/**
	 * 応募メール返信時に顧客の送信箱にメールを送信します。
	 * @param originalMailEntity
	 * @param customerEntity
	 * @param applicatonEntity
	 */
	private void sendApplicationRetToCustomer(TMail originalMailEntity, MCustomer customerEntity, TApplication applicatonEntity) {

		//GCW以外の場合で、連絡先区分がWebの場合はWebデータのメールアドレスを使用して送信。
			try {
				TWeb webEntity = webService.findById(applicatonEntity.webId);

				//Webdataの入力メールがあればそちらに応募確認メールを送信。
				if (eqInt(CommunicationMailKbn.INPUT_MAIL, webEntity.communicationMailKbn)
						&& StringUtils.isNotBlank(webEntity.mail)) {

					ApplicationMailReturnToCustomerMaiDto dto = new ApplicationMailReturnToCustomerMaiDto();
					dto.addTo(webEntity.mail);
					try {
						dto.setFrom(getOuboAddress(), getOuboName());
					// アドレスがセットできない場合は、初期値で送信
					} catch (UnsupportedEncodingException e) {
						// 送信元のセットに失敗した場合はエラーを送信する。（mailProperties.diconの値がセットされる）
						log.fatal("応募メール返信時にWEBデータ登録アドレスに送信時、送信元がセットできませんでした。" + e);
					}
					dto.setInfoAddress(getInfoAddress(applicatonEntity.areaCd));
					dto.setApplicantName(originalMailEntity.fromName);
					dto.setAreaName(getAreaNameByAreaCd(applicatonEntity.areaCd));
					if(MAreaConstants.AreaCd.EAST_AREA_CD_LIST.contains(applicatonEntity.areaCd)) {
						dto.setManagement("株式会社ジェイオフィス東京(グルメキャリー)");
					} else {
						dto.setManagement("株式会社ジェイオフィス");
					}

					gourmetcareeMai.sendApplicationReturnInfoToCustomer(dto);

					//Webデータのメールアドレスを使用して返信した場合はここで顧客へのメール処理を終了。
					return;
				}
			} catch (SNoResultException e) {
				//Webデータが存在しない場合は顧客の連絡先でやりとりを可能とする。
			}

		//顧客のメインメールへ返信する
		if (!StringUtils.isEmpty(customerEntity.mainMail)) {
			ApplicationMailReturnToCustomerMaiDto dto = new ApplicationMailReturnToCustomerMaiDto();
			dto.addTo(customerEntity.mainMail);
			try {
				dto.setFrom(getOuboAddress(), getOuboName());
			// アドレスがセットできない場合は、初期値で送信
			} catch (UnsupportedEncodingException e) {
				// 送信元のセットに失敗した場合はエラーを送信する。（mailProperties.diconの値がセットされる）
				log.fatal("応募メール返信時に顧客のメインメールに送信時、送信元がセットできませんでした。" + e);
			}
			dto.setApplicantName(originalMailEntity.fromName);
			dto.setInfoAddress(getInfoAddress(getAreaCd()));
			dto.setAreaName(getAreaNameByAreaCd(applicatonEntity.areaCd));
			if(MAreaConstants.AreaCd.EAST_AREA_CD_LIST.contains(applicatonEntity.areaCd)) {
				dto.setManagement("株式会社ジェイオフィス東京(グルメキャリー)");
			} else {
				dto.setManagement("株式会社ジェイオフィス");
			}

			gourmetcareeMai.sendApplicationReturnInfoToCustomer(dto);
		}

		// サブメールに送信
		List<String> subMailList = customerSubMailService.getReceptionSubMail(customerEntity.id);
		for (String subMail : subMailList) {
			ApplicationMailReturnToCustomerMaiDto dto = new ApplicationMailReturnToCustomerMaiDto();
			dto.addTo(subMail);
			try {
				dto.setFrom(getOuboAddress(), getOuboName());
				// アドレスがセットできない場合は、初期値で送信
			} catch (UnsupportedEncodingException e) {
				// 送信元のセットに失敗した場合はエラーを送信する。（mailProperties.diconの値がセットされる）
				log.fatal("応募メール返信時に顧客のサブメールに送信時、送信元がセットできませんでした。" + e);
			}
			dto.setApplicantName(originalMailEntity.fromName);
			dto.setInfoAddress(getInfoAddress(getAreaCd()));
			dto.setAreaName(getAreaNameByAreaCd(applicatonEntity.areaCd));
			if(MAreaConstants.AreaCd.EAST_AREA_CD_LIST.contains(applicatonEntity.areaCd)) {
				dto.setManagement("株式会社ジェイオフィス東京(グルメキャリー)");
			} else {
				dto.setManagement("株式会社ジェイオフィス");
			}

			gourmetcareeMai.sendApplicationReturnInfoToCustomer(dto);
		}
	}

	/**
	 * プレ応募メール返信時に顧客の送信箱にメールを送信します。
	 * @param originalMailEntity
	 * @param customerEntity
	 * @param applicatonEntity
	 */
	private void sendPreApplicationRetToCustomer(TMail originalMailEntity, MCustomer customerEntity, TPreApplication applicatonEntity) {

			try {
				TWeb webEntity = webService.findById(applicatonEntity.webId);

				//Webdataの入力メールがあればそちらに応募確認メールを送信。
				if (eqInt(CommunicationMailKbn.INPUT_MAIL, webEntity.communicationMailKbn)
						&& StringUtils.isNotBlank(webEntity.mail)) {

					ApplicationMailReturnToCustomerMaiDto dto = new ApplicationMailReturnToCustomerMaiDto();
					dto.addTo(webEntity.mail);
					try {
						dto.setFrom(getOuboAddress(), getOuboName());
					// アドレスがセットできない場合は、初期値で送信
					} catch (UnsupportedEncodingException e) {
						// 送信元のセットに失敗した場合はエラーを送信する。（mailProperties.diconの値がセットされる）
						log.fatal("応募メール返信時にWEBデータ登録アドレスに送信時、送信元がセットできませんでした。" + e);
					}
					dto.setApplicantName(String.valueOf(applicatonEntity.memberId));
					dto.setInfoAddress(getInfoAddress(getAreaCd()));
					dto.setAreaName(getAreaNameByAreaCd(applicatonEntity.areaCd));
					if(MAreaConstants.AreaCd.EAST_AREA_CD_LIST.contains(applicatonEntity.areaCd)) {
						dto.setManagement("株式会社ジェイオフィス東京(グルメキャリー)");
					} else {
						dto.setManagement("株式会社ジェイオフィス");
					}

					gourmetcareeMai.sendApplicationReturnInfoToCustomer(dto);

					//Webデータのメールアドレスを使用して返信した場合はここで顧客へのメール処理を終了。
					return;
				}
			} catch (SNoResultException e) {
				//Webデータが存在しない場合は顧客の連絡先でやりとりを可能とする。
			}

		//顧客のメインメールへ返信する
		if (!StringUtils.isEmpty(customerEntity.mainMail)) {
			ApplicationMailReturnToCustomerMaiDto dto = new ApplicationMailReturnToCustomerMaiDto();
			dto.addTo(customerEntity.mainMail);
			try {
				dto.setFrom(getOuboAddress(), getOuboName());
			// アドレスがセットできない場合は、初期値で送信
			} catch (UnsupportedEncodingException e) {
				// 送信元のセットに失敗した場合はエラーを送信する。（mailProperties.diconの値がセットされる）
				log.fatal("応募メール返信時に顧客のメインメールに送信時、送信元がセットできませんでした。" + e);
			}
			dto.setApplicantName(String.valueOf(applicatonEntity.memberId));
			dto.setInfoAddress(getInfoAddress(getAreaCd()));
			dto.setAreaName(getAreaNameByAreaCd(applicatonEntity.areaCd));
			if(MAreaConstants.AreaCd.EAST_AREA_CD_LIST.contains(applicatonEntity.areaCd)) {
				dto.setManagement("株式会社ジェイオフィス東京(グルメキャリー)");
			} else {
				dto.setManagement("株式会社ジェイオフィス");
			}

			gourmetcareeMai.sendApplicationReturnInfoToCustomer(dto);
		}

		// サブメールに送信
		List<String> subMailList = customerSubMailService.getReceptionSubMail(customerEntity.id);
		for (String subMail : subMailList) {
			ApplicationMailReturnToCustomerMaiDto dto = new ApplicationMailReturnToCustomerMaiDto();
			dto.addTo(subMail);
			try {
				dto.setFrom(getOuboAddress(), getOuboName());
				// アドレスがセットできない場合は、初期値で送信
			} catch (UnsupportedEncodingException e) {
				// 送信元のセットに失敗した場合はエラーを送信する。（mailProperties.diconの値がセットされる）
				log.fatal("応募メール返信時に顧客のサブメールに送信時、送信元がセットできませんでした。" + e);
			}
			dto.setApplicantName(String.valueOf(applicatonEntity.memberId));
			dto.setInfoAddress(getInfoAddress(getAreaCd()));
			dto.setAreaName(getAreaNameByAreaCd(applicatonEntity.areaCd));
			if(MAreaConstants.AreaCd.EAST_AREA_CD_LIST.contains(applicatonEntity.areaCd)) {
				dto.setManagement("株式会社ジェイオフィス東京(グルメキャリー)");
			} else {
				dto.setManagement("株式会社ジェイオフィス");
			}
			gourmetcareeMai.sendApplicationReturnInfoToCustomer(dto);
		}
	}

	/**
	 * 店舗見学応募メール返信時に顧客の送信箱にメールを送信します。
	 * @param originalMailEntity
	 * @param customerEntity
	 * @param applicatonEntity
	 */
	private void sendObservateApplicationRetToCustomer(TMail originalMailEntity, MCustomer customerEntity, TObservateApplication applicatonEntity) {

		//GCW以外の場合で、連絡先区分がWebの場合はWebデータのメールアドレスを使用して送信。
			try {
				TWeb webEntity = webService.findById(applicatonEntity.webId);

				//Webdataの入力メールがあればそちらに応募確認メールを送信。
				if (eqInt(CommunicationMailKbn.INPUT_MAIL, webEntity.communicationMailKbn)
						&& StringUtils.isNotBlank(webEntity.mail)) {

					ApplicationMailReturnToCustomerMaiDto dto = new ApplicationMailReturnToCustomerMaiDto();
					dto.addTo(webEntity.mail);
					try {
						dto.setFrom(getOuboAddress(), getOuboName());
					// アドレスがセットできない場合は、初期値で送信
					} catch (UnsupportedEncodingException e) {
						// 送信元のセットに失敗した場合はエラーを送信する。（mailProperties.diconの値がセットされる）
						log.fatal("応募メール返信時にWEBデータ登録アドレスに送信時、送信元がセットできませんでした。" + e);
					}
					dto.setApplicantName(originalMailEntity.fromName);
					dto.setInfoAddress(getInfoAddress(getAreaCd()));
					dto.setSiteURL(getSiteUrl(getAreaCd()));
//					dto.setObservationName(observationName);
					dto.setAreaName(getAreaNameByAreaCd(applicatonEntity.areaCd));
					if(MAreaConstants.AreaCd.EAST_AREA_CD_LIST.contains(applicatonEntity.areaCd)) {
						dto.setManagement("株式会社ジェイオフィス東京(グルメキャリー)");
					} else {
						dto.setManagement("株式会社ジェイオフィス");
					}

					gourmetcareeMai.sendApplicationReturnInfoToCustomer(dto);

					//Webデータのメールアドレスを使用して返信した場合はここで顧客へのメール処理を終了。
					return;
				}
			} catch (SNoResultException e) {
				//Webデータが存在しない場合は顧客の連絡先でやりとりを可能とする。
			}

		//顧客のメインメールへ返信する
		if (!StringUtils.isEmpty(customerEntity.mainMail)) {
			ApplicationMailReturnToCustomerMaiDto dto = new ApplicationMailReturnToCustomerMaiDto();
			dto.addTo(customerEntity.mainMail);
			try {
				dto.setFrom(getOuboAddress(), getOuboName());
			// アドレスがセットできない場合は、初期値で送信
			} catch (UnsupportedEncodingException e) {
				// 送信元のセットに失敗した場合はエラーを送信する。（mailProperties.diconの値がセットされる）
				log.fatal("応募メール返信時に顧客のメインメールに送信時、送信元がセットできませんでした。" + e);
			}
			dto.setApplicantName(originalMailEntity.fromName);
			dto.setInfoAddress(getInfoAddress(getAreaCd()));
			dto.setSiteURL(getSiteUrl(getAreaCd()));
//			dto.setObservationName(observationName);
			dto.setAreaName(getAreaNameByAreaCd(applicatonEntity.areaCd));
			if(MAreaConstants.AreaCd.EAST_AREA_CD_LIST.contains(applicatonEntity.areaCd)) {
				dto.setManagement("株式会社ジェイオフィス東京(グルメキャリー)");
			} else {
				dto.setManagement("株式会社ジェイオフィス");
			}

			gourmetcareeMai.sendApplicationReturnInfoToCustomer(dto);		}

		//顧客のサブメールが受信可能であればサブメールへ返信する
		List<String> subMailList = customerSubMailService.getReceptionSubMail(customerEntity.id);
		for (String subMail : subMailList) {
			ApplicationMailReturnToCustomerMaiDto dto = new ApplicationMailReturnToCustomerMaiDto();
			dto.addTo(subMail);
			try {
				dto.setFrom(getOuboAddress(), getOuboName());
			// アドレスがセットできない場合は、初期値で送信
			} catch (UnsupportedEncodingException e) {
				// 送信元のセットに失敗した場合はエラーを送信する。（mailProperties.diconの値がセットされる）
				log.fatal("応募メール返信時に顧客のサブメールに送信時、送信元がセットできませんでした。" + e);
			}
			dto.setApplicantName(originalMailEntity.fromName);
			dto.setInfoAddress(getInfoAddress(getAreaCd()));
			dto.setSiteURL(getSiteUrl(getAreaCd()));
			dto.setAreaName(getAreaNameByAreaCd(applicatonEntity.areaCd));
			if(MAreaConstants.AreaCd.EAST_AREA_CD_LIST.contains(applicatonEntity.areaCd)) {
				dto.setManagement("株式会社ジェイオフィス東京(グルメキャリー)");
			} else {
				dto.setManagement("株式会社ジェイオフィス");
			}

			gourmetcareeMai.sendApplicationReturnInfoToCustomer(dto);
		}
	}


	/**
	 * アルバイト応募メール返信時に顧客の送信箱にメールを送信します。
	 * @param originalMailEntity 元のメールエンティティ
	 * @param customerEntity 顧客エンティティ
	 * @param applicatonEntity アルバイト応募エンティティ
	 */
	private void sendArbeitApplicationRetToCustomer(TMail originalMailEntity, MCustomer customerEntity, TArbeitApplication applicatonEntity) {

		//顧客のメインメールへ返信する
		if (!StringUtils.isEmpty(customerEntity.mainMail)) {
			ApplicationMailReturnToCustomerMaiDto dto = new ApplicationMailReturnToCustomerMaiDto();
			dto.addTo(customerEntity.mainMail);
			try {
				dto.setFrom(getOuboAddress(), getOuboName());
			// アドレスがセットできない場合は、初期値で送信
			} catch (UnsupportedEncodingException e) {
				// 送信元のセットに失敗した場合はエラーを送信する。（mailProperties.diconの値がセットされる）
				log.fatal("応募メール返信時に顧客のメインメールに送信時、送信元がセットできませんでした。" + e);
			}
			dto.setApplicantName(originalMailEntity.fromName);
			dto.setInfoAddress(getInfoAddress(getAreaCd()));
			dto.setSiteURL(getSiteUrl(getAreaCd()));

			gourmetcareeMai.sendArbeitApplicationReturnInfoToCustomer(dto);
		}

		//顧客のサブメールが受信可能であればサブメールへ返信する
		List<String> subMailList = customerSubMailService.getReceptionSubMail(customerEntity.id);
		for (String subMail : subMailList) {
			ApplicationMailReturnToCustomerMaiDto dto = new ApplicationMailReturnToCustomerMaiDto();
			dto.addTo(subMail);
			try {
				dto.setFrom(getOuboAddress(), getOuboName());
			// アドレスがセットできない場合は、初期値で送信
			} catch (UnsupportedEncodingException e) {
				// 送信元のセットに失敗した場合はエラーを送信する。（mailProperties.diconの値がセットされる）
				log.fatal("応募メール返信時に顧客のサブメールに送信時、送信元がセットできませんでした。" + e);
			}
			dto.setApplicantName(originalMailEntity.fromName);
			dto.setInfoAddress(getInfoAddress(getAreaCd()));
			dto.setSiteURL(getSiteUrl(getAreaCd()));

			gourmetcareeMai.sendArbeitApplicationReturnInfoToCustomer(dto);
		}
	}

	/**
	 * 応募メールを応募者(会員)に返信します。
	 * @param customerName
	 * @param memberEntity
	 */
	private void sendApplicationRetToMember(MMember memberEntity, TApplication applicatonEntity) {

		if (memberEntity == null) {
			throw new InternalGourmetCareeSystemErrorException("");
		}

		//応募者（会員）のログインIDへ返信する
		if (!StringUtils.isEmpty(memberEntity.loginId)) {

			//PCのメールの詳細画面
			ApplicationMailReturnToApplicantMaiDto dto = new ApplicationMailReturnToApplicantMaiDto();
			dto.addTo(memberEntity.loginId);
			try {
				dto.setFrom(getOuboAddress(), getOuboName());
			// アドレスがセットできない場合は、初期値で送信
			} catch (UnsupportedEncodingException e) {
				// 送信元のセットに失敗した場合はエラーを送信する。（mailProperties.diconの値がセットされる）
				log.fatal("応募メールを応募者(会員)のPCメールアドレスに送信時、送信元がセットできませんでした。" + e);
			}
			dto.setCustomerName(applicatonEntity.applicationName);
			dto.setLoginUrl(getMemberPCLoginURL(applicatonEntity.areaCd));
			dto.setInfoAddress(getInfoAddress(getAreaCd()));
			dto.setSiteURL(getSiteUrl(getAreaCd()));
			dto.setMobileMailFlg(false);
			dto.setAreaName(getAreaNameByAreaCd(applicatonEntity.areaCd));
			dto.setTopUrl(getCommonProperty("gc.sslDomain"));
			dto.setMemberId(String.valueOf(memberEntity.id));
			if(MAreaConstants.AreaCd.EAST_AREA_CD_LIST.contains(applicatonEntity.areaCd)) {
				dto.setManagement("株式会社ジェイオフィス東京(グルメキャリー)");
			} else {
				dto.setManagement("株式会社ジェイオフィス");
			}


			gourmetcareeMai.sendApplicationReturnInfoToApplicant(dto);
		}

		//応募者（会員）のサブメールへ返信する
		if (!StringUtils.isEmpty(memberEntity.subMailAddress)) {

			//メールの詳細画面
			ApplicationMailReturnToApplicantMaiDto dto = new ApplicationMailReturnToApplicantMaiDto();
			dto.addTo(memberEntity.subMailAddress);
			try {
				dto.setFrom(getOuboAddress(), getOuboName());
			// アドレスがセットできない場合は、初期値で送信
			} catch (UnsupportedEncodingException e) {
				// 送信元のセットに失敗した場合はエラーを送信する。（mailProperties.diconの値がセットされる）
				log.fatal("応募メールを応募者(会員)のモバイルメールアドレスに送信時、送信元がセットできませんでした。" + e);
			}
			dto.setCustomerName(applicatonEntity.applicationName);
			dto.setLoginUrl(getMemberMobileLoginURL(applicatonEntity.areaCd));
			dto.setLoginUrlForSmart(getMemberPCLoginURL(applicatonEntity.areaCd));
			dto.setNavigation("\"Myページ\"へログインの上");
			dto.setInfoAddress(getInfoAddress(getAreaCd()));
			dto.setSiteURL(getSiteUrl(getAreaCd()));
			dto.setMobileMailFlg(false);
			dto.setAreaName(getAreaNameByAreaCd(applicatonEntity.areaCd));
			dto.setTopUrl(getCommonProperty("gc.sslDomain"));
			dto.setMemberId(String.valueOf(memberEntity.id));
			if(MAreaConstants.AreaCd.EAST_AREA_CD_LIST.contains(applicatonEntity.areaCd)) {
				dto.setManagement("株式会社ジェイオフィス東京(グルメキャリー)");
			} else {
				dto.setManagement("株式会社ジェイオフィス");
			}

			gourmetcareeMai.sendApplicationReturnInfoToApplicant(dto);
		}
	}

	/**
	 * プレ応募メールを応募者(会員)に返信します。
	 * @param customerName
	 * @param memberEntity
	 */
	private void sendPreApplicationRetToMember(MMember memberEntity, String applicationName, Integer areaCd) {

		if (memberEntity == null) {
			throw new InternalGourmetCareeSystemErrorException("");
		}

		//応募者（会員）のログインIDへ返信する
		if (!StringUtils.isEmpty(memberEntity.loginId)) {

			//PCのメールの詳細画面
			ApplicationMailReturnToApplicantMaiDto dto = new ApplicationMailReturnToApplicantMaiDto();
			dto.addTo(memberEntity.loginId);
			try {
				dto.setFrom(getOuboAddress(), getOuboName());
			// アドレスがセットできない場合は、初期値で送信
			} catch (UnsupportedEncodingException e) {
				// 送信元のセットに失敗した場合はエラーを送信する。（mailProperties.diconの値がセットされる）
				log.fatal("応募メールを応募者(会員)のPCメールアドレスに送信時、送信元がセットできませんでした。" + e);
			}
			dto.setAreaName(getAreaNameByAreaCd(areaCd));
			dto.setCustomerName(applicationName);
			dto.setLoginUrl(getMemberPCLoginURL(areaCd));
			if(MAreaConstants.AreaCd.EAST_AREA_CD_LIST.contains(areaCd)) {
				dto.setManagement("株式会社ジェイオフィス東京(グルメキャリー)");
			} else {
				dto.setManagement("株式会社ジェイオフィス");
			}
			dto.setTopUrl(getCommonProperty("gc.sslDomain"));
			dto.setInfoAddress(getInfoAddress(getAreaCd()));
			dto.setMobileMailFlg(false);

			gourmetcareeMai.sendApplicationReturnInfoToApplicant(dto);
		}

		//応募者（会員）のサブメールへ返信する
		if (!StringUtils.isEmpty(memberEntity.subMailAddress)) {

			//メールの詳細画面
			ApplicationMailReturnToApplicantMaiDto dto = new ApplicationMailReturnToApplicantMaiDto();
			dto.addTo(memberEntity.subMailAddress);
			try {
				dto.setFrom(getOuboAddress(), getOuboName());
			// アドレスがセットできない場合は、初期値で送信
			} catch (UnsupportedEncodingException e) {
				// 送信元のセットに失敗した場合はエラーを送信する。（mailProperties.diconの値がセットされる）
				log.fatal("応募メールを応募者(会員)のモバイルメールアドレスに送信時、送信元がセットできませんでした。" + e);
			}
			dto.setAreaName(getAreaNameByAreaCd(areaCd));
			dto.setCustomerName(applicationName);
			dto.setLoginUrl(getMemberPCLoginURL(areaCd));
			if(MAreaConstants.AreaCd.EAST_AREA_CD_LIST.contains(areaCd)) {
				dto.setManagement("株式会社ジェイオフィス東京(グルメキャリー)");
			} else {
				dto.setManagement("株式会社ジェイオフィス");
			}
			dto.setTopUrl(getCommonProperty("gc.sslDomain"));
			dto.setInfoAddress(getInfoAddress(getAreaCd()));
			dto.setMobileMailFlg(false);

			gourmetcareeMai.sendApplicationReturnInfoToApplicant(dto);
		}
	}

	/**
	 * 店舗見学応募メールを応募者(会員)に返信します。
	 * @param customerName
	 * @param memberEntity
	 */
	private void sendObservateApplicationRetToMember(MMember memberEntity, TObservateApplication applicatonEntity) {

		if (memberEntity == null) {
			throw new InternalGourmetCareeSystemErrorException("");
		}

		//応募者（会員）のPCへ返信する
		if (!StringUtils.isEmpty(memberEntity.loginId)) {

			//ログインメールの詳細画面
			ApplicationMailReturnToApplicantMaiDto dto = new ApplicationMailReturnToApplicantMaiDto();
			dto.addTo(memberEntity.loginId);
			try {
				dto.setFrom(getOuboAddress(), getOuboName());
			// アドレスがセットできない場合は、初期値で送信
			} catch (UnsupportedEncodingException e) {
				// 送信元のセットに失敗した場合はエラーを送信する。（mailProperties.diconの値がセットされる）
				log.fatal("応募メールを応募者(会員)のログインメールアドレスに送信時、送信元がセットできませんでした。" + e);
			}
			dto.setCustomerName(applicatonEntity.applicationName);
			dto.setLoginUrl(getMemberPCLoginURL(applicatonEntity.areaCd));
			dto.setNavigation("\"Myページ\"へログインの上");
			dto.setInfoAddress(getInfoAddress(getAreaCd()));
			dto.setSiteURL(getSiteUrl(getAreaCd()));
			dto.setMobileMailFlg(false);
			dto.setAreaName(getAreaNameByAreaCd(applicatonEntity.areaCd));
			dto.setTopUrl(getCommonProperty("gc.sslDomain"));
			if(MAreaConstants.AreaCd.EAST_AREA_CD_LIST.contains(applicatonEntity.areaCd)) {
				dto.setManagement("株式会社ジェイオフィス東京(グルメキャリー)");
			} else {
				dto.setManagement("株式会社ジェイオフィス");
			}

			gourmetcareeMai.sendApplicationReturnInfoToApplicant(dto);
		}

		//応募者（会員）のサブメールへ返信する
		if (!StringUtils.isEmpty(memberEntity.subMailAddress)) {

			//PCのメールの詳細画面
			ApplicationMailReturnToApplicantMaiDto dto = new ApplicationMailReturnToApplicantMaiDto();
			dto.addTo(memberEntity.subMailAddress);
			try {
				dto.setFrom(getOuboAddress(), getOuboName());
			// アドレスがセットできない場合は、初期値で送信
			} catch (UnsupportedEncodingException e) {
				// 送信元のセットに失敗した場合はエラーを送信する。（mailProperties.diconの値がセットされる）
				log.fatal("応募メールを応募者(会員)のモバイルメールアドレスに送信時、送信元がセットできませんでした。" + e);
			}
			dto.setCustomerName(applicatonEntity.applicationName);
			dto.setLoginUrl(getMemberMobileLoginURL(applicatonEntity.areaCd));
			dto.setLoginUrlForSmart(getMemberPCLoginURL(applicatonEntity.areaCd));
			dto.setNavigation("\"Myページ\"へログインの上");
			dto.setInfoAddress(getInfoAddress(getAreaCd()));
			dto.setSiteURL(getSiteUrl(getAreaCd()));
			dto.setMobileMailFlg(false);
			dto.setAreaName(getAreaNameByAreaCd(applicatonEntity.areaCd));
			dto.setTopUrl(getCommonProperty("gc.sslDomain"));
			if(MAreaConstants.AreaCd.EAST_AREA_CD_LIST.contains(applicatonEntity.areaCd)) {
				dto.setManagement("株式会社ジェイオフィス東京(グルメキャリー)");
			} else {
				dto.setManagement("株式会社ジェイオフィス");
			}

			gourmetcareeMai.sendApplicationReturnInfoToApplicant(dto);
		}
	}

	/**
	 * 応募メールの返信を応募者（非会員）に送信します。
	 * @param applicatonEntity
	 * @param toApplicantEntity
	 */
	private void sendApplicationRetToNoMember(TApplication applicatonEntity, TMail toApplicantEntity) {

		//応募者（非会員）のPCへ返信する
		if (!StringUtils.isEmpty(applicatonEntity.pcMail)) {
			String mailPagePath = "";

			if(!StringUtils.isEmpty(applicatonEntity.mailToken)) {
				mailPagePath = GourmetCareeUtil.makePath(
						getNonMemberPCMailURL(applicatonEntity.areaCd),
						"list",
						applicatonEntity.mailToken);
			} else {
				mailPagePath = GourmetCareeUtil.makePath(
						getNonMemberPCMailURL(applicatonEntity.areaCd),
						Integer.toString(toApplicantEntity.id),
						toApplicantEntity.accessCd);
			}

			//PCのメールの詳細画面
			ApplicationMailReturnToApplicantMaiDto dto = new ApplicationMailReturnToApplicantMaiDto();
			dto.addTo(applicatonEntity.pcMail);
			try {
				dto.setFrom(getOuboAddress(), getOuboName());
			// アドレスがセットできない場合は、初期値で送信
			} catch (UnsupportedEncodingException e) {
				// 送信元のセットに失敗した場合はエラーを送信する。（mailProperties.diconの値がセットされる）
				log.fatal("応募メールの返信を応募者(非会員)のPCメールアドレスに送信時、送信元がセットできませんでした。" + e);
			}
			dto.setCustomerName(applicatonEntity.applicationName);
			dto.setLoginUrl(mailPagePath);
			dto.setInfoAddress(getInfoAddress(getAreaCd()));
			dto.setSiteURL(getSiteUrl(getAreaCd()));
			dto.setMobileMailFlg(false);
			dto.setAreaName(getAreaNameByAreaCd(applicatonEntity.areaCd));
			dto.setTopUrl(getCommonProperty("gc.sslDomain"));
			dto.setMemberId(null);
			if(MAreaConstants.AreaCd.EAST_AREA_CD_LIST.contains(applicatonEntity.areaCd)) {
				dto.setManagement("株式会社ジェイオフィス東京(グルメキャリー)");
			} else {
				dto.setManagement("株式会社ジェイオフィス");
			}

			gourmetcareeMai.sendApplicationReturnInfoToApplicant(dto);
		}

		//応募者（非会員）のモバイルへ返信する
		if (!StringUtils.isEmpty(applicatonEntity.mobileMail)) {
			String mailPagePath = GourmetCareeUtil.makePath(
					getNonMemberMobileMailURL(applicatonEntity.areaCd),
					Integer.toString(toApplicantEntity.id),
					toApplicantEntity.accessCd);
			String mailPagePathForSmart = GourmetCareeUtil.makePath(
					getNonMemberPCMailURL(applicatonEntity.areaCd),
					Integer.toString(toApplicantEntity.id),
					toApplicantEntity.accessCd);

			//PCのメールの詳細画面
			ApplicationMailReturnToApplicantMaiDto dto = new ApplicationMailReturnToApplicantMaiDto();
			dto.addTo(applicatonEntity.mobileMail);
			try {
				dto.setFrom(getOuboAddress(), getOuboName());
			// アドレスがセットできない場合は、初期値で送信
			} catch (UnsupportedEncodingException e) {
				// 送信元のセットに失敗した場合はエラーを送信する。（mailProperties.diconの値がセットされる）
				log.fatal("応募メールの返信を応募者(非会員)のモバイルメールアドレスに送信時、送信元がセットできませんでした。" + e);
			}
			dto.setCustomerName(applicatonEntity.applicationName);
			dto.setLoginUrl(mailPagePath);
			dto.setLoginUrlForSmart(mailPagePathForSmart);
			dto.setNavigation("下記URLをクリックの上");
			dto.setInfoAddress(getInfoAddress(getAreaCd()));
			dto.setSiteURL(getSiteUrl(getAreaCd()));
			dto.setMobileMailFlg(true);
			dto.setMemberId(null);
			gourmetcareeMai.sendApplicationReturnInfoToApplicant(dto);
		}
	}

	/**
	 * 店舗見学応募メールの返信を応募者（非会員）に送信します。
	 * @param applicatonEntity
	 * @param toApplicantEntity
	 */
	private void sendObservateApplicationRetToNoMember(TObservateApplication applicatonEntity, TMail toApplicantEntity) {

		//応募者（非会員）のPCへ返信する
		if (!StringUtils.isEmpty(applicatonEntity.pcMail)) {
			String mailPagePath = "";
			if(!StringUtils.isEmpty(applicatonEntity.mailToken)) {
				mailPagePath = GourmetCareeUtil.makePath(
						getNonMemberPCObservateMailURL(applicatonEntity.areaCd),
						"list",
						applicatonEntity.mailToken);
			} else {
				mailPagePath = GourmetCareeUtil.makePath(
						getNonMemberPCObservateMailURL(applicatonEntity.areaCd),
						Integer.toString(toApplicantEntity.id),
						toApplicantEntity.accessCd);
			}


			//PCのメールの詳細画面
			ApplicationMailReturnToApplicantMaiDto dto = new ApplicationMailReturnToApplicantMaiDto();
			dto.addTo(applicatonEntity.pcMail);
			try {
				dto.setFrom(getOuboAddress(), getOuboName());
			// アドレスがセットできない場合は、初期値で送信
			} catch (UnsupportedEncodingException e) {
				// 送信元のセットに失敗した場合はエラーを送信する。（mailProperties.diconの値がセットされる）
				log.fatal("応募メールの返信を応募者(非会員)のPCメールアドレスに送信時、送信元がセットできませんでした。" + e);
			}
			dto.setCustomerName(applicatonEntity.applicationName);
			dto.setLoginUrl(mailPagePath);
			dto.setNavigation("下記URLをクリックの上");
			dto.setInfoAddress(getInfoAddress(getAreaCd()));
			dto.setSiteURL(getSiteUrl(getAreaCd()));
			dto.setMobileMailFlg(false);
//			dto.setObservationName(observationName);
			dto.setAreaName(getAreaNameByAreaCd(applicatonEntity.areaCd));
			dto.setTopUrl(getCommonProperty("gc.sslDomain"));
			if(MAreaConstants.AreaCd.EAST_AREA_CD_LIST.contains(applicatonEntity.areaCd)) {
				dto.setManagement("株式会社ジェイオフィス東京(グルメキャリー)");
			} else {
				dto.setManagement("株式会社ジェイオフィス");
			}


//			gourmetcareeMai.sendObservateApplicationReturnInfoToApplicant(dto);
			gourmetcareeMai.sendApplicationReturnInfoToApplicant(dto);
		}

		//応募者（非会員）のモバイルへ返信する
		if (!StringUtils.isEmpty(applicatonEntity.mobileMail)) {
			String mailPagePath = GourmetCareeUtil.makePath(
					getNonMemberMobileObservateMailURL(applicatonEntity.areaCd),
					Integer.toString(toApplicantEntity.id),
					toApplicantEntity.accessCd);
			String mailPagePathForSmart = GourmetCareeUtil.makePath(
					getNonMemberPCMailURL(applicatonEntity.areaCd),
					Integer.toString(toApplicantEntity.id),
					toApplicantEntity.accessCd);

			//PCのメールの詳細画面
			ApplicationMailReturnToApplicantMaiDto dto = new ApplicationMailReturnToApplicantMaiDto();
			dto.addTo(applicatonEntity.mobileMail);
			try {
				dto.setFrom(getOuboAddress(), getOuboName());
			// アドレスがセットできない場合は、初期値で送信
			} catch (UnsupportedEncodingException e) {
				// 送信元のセットに失敗した場合はエラーを送信する。（mailProperties.diconの値がセットされる）
				log.fatal("応募メールの返信を応募者(非会員)のモバイルメールアドレスに送信時、送信元がセットできませんでした。" + e);
			}
			dto.setCustomerName(applicatonEntity.applicationName);
			dto.setLoginUrl(mailPagePath);
			dto.setLoginUrlForSmart(mailPagePathForSmart);
			dto.setNavigation("下記URLをクリックの上");
			dto.setInfoAddress(getInfoAddress(getAreaCd()));
			dto.setSiteURL(getSiteUrl(getAreaCd()));
			dto.setMobileMailFlg(true);
//			dto.setObservationName(observationName);
			dto.setAreaName(getAreaNameByAreaCd(applicatonEntity.areaCd));
			dto.setTopUrl(getCommonProperty("gc.sslDomain"));
			if(MAreaConstants.AreaCd.EAST_AREA_CD_LIST.contains(applicatonEntity.areaCd)) {
				dto.setManagement("株式会社ジェイオフィス東京(グルメキャリー)");
			} else {
				dto.setManagement("株式会社ジェイオフィス");
			}

			gourmetcareeMai.sendApplicationReturnInfoToApplicant(dto);
		}
	}



	/**
	 * アルバイト応募メールの返信を応募者（非会員）に送信します。
	 * @param applicatonEntity 応募エンティティ
	 * @param toApplicantEntity 応募者宛返信メールエンティティ
	 */
	private void sendArbeitApplicationRetToNoMember(TArbeitApplication applicatonEntity, TMail toApplicantEntity) {

		//応募者へ返信する

		if (StringUtils.isEmpty(applicatonEntity.mailAddress)) {
			return;
		}


		String mailPagePath = GourmetCareeUtil.makePath(
				getNonMemberArbeitMailUrl(MTypeConstants.TerminalKbn.PC_VALUE),
				Integer.toString(toApplicantEntity.id),
				toApplicantEntity.accessCd);

		String mailMobilePagePath = GourmetCareeUtil.makePath(
				getNonMemberArbeitMailUrl(MTypeConstants.TerminalKbn.MOBILE_VALUE),
				Integer.toString(toApplicantEntity.id),
				toApplicantEntity.accessCd);

		//PCのメールの詳細画面
		ApplicationMailReturnToApplicantMaiDto dto = new ApplicationMailReturnToApplicantMaiDto();
		dto.addTo(applicatonEntity.mailAddress);
		try {
			dto.setFrom(getArbeitInfoAddress(), getArbeitFromName());
		// アドレスがセットできない場合は、初期値で送信
		} catch (UnsupportedEncodingException e) {
			// 送信元のセットに失敗した場合はエラーを送信する。（mailProperties.diconの値がセットされる）
			log.fatal("応募メールの返信を応募者(非会員)のPCメールアドレスに送信時、送信元がセットできませんでした。" + e);
		}
		dto.setCustomerName(applicatonEntity.applicationName);
		dto.setLoginUrl(mailPagePath);
		dto.setLoginUrlForSmart(mailMobilePagePath);
		dto.setNavigation("下記URLをクリックの上");
		dto.setInfoAddress(getInfoAddress(getAreaCd()));
		dto.setSiteURL(getSiteUrl(getAreaCd()));
		dto.setMobileMailFlg(false);
		dto.setShopListName(applicatonEntity.applicationName);

		gourmetcareeMai.sendArbeitApplicationReturnInfoToApplicant(dto);

	}


	/**
	 * パスワード変更確認のメールを送信します。<br />
	 * プロパティの仮登録エンティティにセットして呼び出します。
	 */
	public void sendPasswordReissueMailConf(SendMailProperty property) {
		// プロパティがnullの場合はエラー
		checkEmptyProperty(property);
		// エンティティがnullの場合はエラー
		if (property.tTemporaryRegistration == null) {
			throw new IllegalArgumentException("引数がnullです。");
		}

		String kanriAddress = getKanriAddress(property.tTemporaryRegistration.areaCd);
		PasswordReissueMailConfMaiDto mailDto = new PasswordReissueMailConfMaiDto();

		//管理アドレスを送信元に設定
		try {
			mailDto.setFrom(kanriAddress, getKanriName(property.tTemporaryRegistration.areaCd));
		} catch (UnsupportedEncodingException e) {
			log.fatal("パスワード変更確認のメールを送信時、送信元がセットできませんでした。" + e);
		}

		String passwordReissuePagePath = "";
		// PCの場合
		if (MTypeConstants.TerminalKbn.PC_VALUE == property.tTemporaryRegistration.terminalKbn) {
			passwordReissuePagePath = GourmetCareeUtil.makePath(getMailProerties().getProperty("gc.customer.pcPasswordReissueUrl"),
					String.valueOf(property.tTemporaryRegistration.id), property.tTemporaryRegistration.accessCd);
			mailDto.setMobileMailFlg(false);

		// 携帯の場合
		} else {
			passwordReissuePagePath = GourmetCareeUtil.makePath(getMailProerties().getProperty("gc.customer.mobilePasswordReissueUrl"),
					String.valueOf(property.tTemporaryRegistration.id), property.tTemporaryRegistration.accessCd);
			mailDto.setMobileMailFlg(true);
		}

		mailDto.setPasswordReissuePagePath(passwordReissuePagePath);

		mailDto.addTo(property.tTemporaryRegistration.mail);

		mailDto.setInfoAddress(getMailProerties().getProperty("gc.mail.kanriAddress" + property.tTemporaryRegistration.areaCd));
		mailDto.setSiteURL(getMailProerties().getProperty("gc.front.siteUrl" + property.tTemporaryRegistration.areaCd));

		gourmetcareeMai.sendPasswordReissueMailConf(mailDto);

	}


	/**
	 * パスワード変更完了通知メールを送信します。
	 * @param property
	 */
	public void sendPasswordReissueMailComp(SendMailProperty property) {
		String kanriAddress = getKanriAddress(property.tTemporaryRegistration.areaCd);
		PasswordReissueMailCompMaiDto mailDto = new PasswordReissueMailCompMaiDto();
		mailDto.addTo(property.tTemporaryRegistration.mail);
		//管理アドレスを送信元に設定
		try {
			mailDto.setFrom(kanriAddress, getKanriName(property.tTemporaryRegistration.areaCd));
		} catch (UnsupportedEncodingException e) {
			log.fatal("パスワード変更確認のメールを送信時、送信元がセットできませんでした。" + e);
		}

		mailDto.setInfoAddress(getMailProerties().getProperty("gc.mail.kanriAddress" + property.tTemporaryRegistration.areaCd));
		mailDto.setSiteURL(getMailProerties().getProperty("gc.front.siteUrl" + property.tTemporaryRegistration.areaCd));

		gourmetcareeMai.sendPasswordReissueMailComp(mailDto);
	}

	/**
	 * 顧客からの問い合わせメールを送信します。
	 * @param property
	 */
	public void sendContactMail(ContactMailProperty property) {

		ContactMaiDto dto = new ContactMaiDto();
		dto.addTo(getInfoAddress(getAreaCd()));
		try {
			dto.setFrom(getInfoAddress(getAreaCd()), getKanriName());
		} catch (UnsupportedEncodingException e) {
			// 送信元のセットに失敗した場合はエラーを送信する。（mailProperties.diconの値がセットされる）
			log.fatal("顧客からの問い合わせメール送信時、送信元がセットできませんでした。" + e);
		}
		dto.setCustomerId(String.valueOf(getCustomerId()));
		dto.setCustomerName(property.customerName);
		dto.setContactName(property.contactName);
		dto.setPhoneNo(property.phoneNo);
		dto.setSender(property.sender);
		dto.setContents(property.body);

		gourmetcareeMai.sendContactToAdmin(dto);
	}



	/**
	 * メールIDから応募者名を取得
	 * @param mailId メールID
	 * @return
	 * @throws WNoResultException
	 */
	public String getApplicantNameByMailId(int mailId) throws WNoResultException {
		return getApplicantNameByMailId(mailId, TApplication.TABLE_NAME, TMail.APPLICATION_ID);
	}

	/**
	 * メールIDからプレ応募者名を取得
	 * @param mailId メールID
	 * @return
	 * @throws WNoResultException
	 */
	public String getPreApplicantNameByMailId(int mailId) throws WNoResultException {
		return getPreApplicantNameByMailId(mailId, TPreApplication.TABLE_NAME, TMail.APPLICATION_ID);
	}

	/**
	 * メールIDからバイト応募者名を取得
	 * @param mailId メールID
	 * @return
	 * @throws WNoResultException
	 */
	public String getArbeitApplicantNameByMailId(int mailId) throws WNoResultException {
		return getApplicantNameByMailId(mailId, TArbeitApplication.TABLE_NAME, TMail.ARBEIT_APPLICATION_ID);
	}

	public String getObservateApplicantNameByMailId(int mailId) throws WNoResultException {
		return getObservateApplicantNameByMailId(mailId, TObservateApplication.TABLE_NAME, TMail.OBSERVATE_APPLICATION_ID);
	}

	/**
	 * メールIDから応募者名を取得
	 * @param mailId メールID
	 * @param tableName テーブル名
	 * @param applicationIdColumn メールテーブルの応募IDとしてJOINするカラム名
	 * @return
	 * @throws WNoResultException
	 */
	private String getApplicantNameByMailId(int mailId, String tableName, String applicationIdColumn) throws WNoResultException {
		String sql = String.format(APPLICANT_NAME_SQL_FORMAT, tableName, applicationIdColumn);

		try {
			return jdbcManager.selectBySql(String.class, sql, mailId, DeleteFlgKbn.NOT_DELETED, DeleteFlgKbn.NOT_DELETED)
					.disallowNoResult()
					.getSingleResult();
		} catch (SNoResultException e) {
			throw new WNoResultException(e);
		}
	}

	/**
	 * メールIDから応募者名を取得
	 * @param mailId メールID
	 * @param tableName テーブル名
	 * @param applicationIdColumn メールテーブルの応募IDとしてJOINするカラム名
	 * @return
	 * @throws WNoResultException
	 */
	private String getObservateApplicantNameByMailId(int mailId, String tableName, String applicationIdColumn) throws WNoResultException {
		String sql = String.format(OBSERVATE_APPLICANT_NAME_SQL_FORMAT, tableName, applicationIdColumn);

		try {
			return jdbcManager.selectBySql(String.class, sql, mailId, DeleteFlgKbn.NOT_DELETED, DeleteFlgKbn.NOT_DELETED)
					.disallowNoResult()
					.getSingleResult();
		} catch (SNoResultException e) {
			throw new WNoResultException(e);
		}
	}

	/**
	 * メールIDから応募者名を取得
	 * @param mailId メールID
	 * @param tableName テーブル名
	 * @param applicationIdColumn メールテーブルの応募IDとしてJOINするカラム名
	 * @return
	 * @throws WNoResultException
	 */
	private String getPreApplicantNameByMailId(int mailId, String tableName, String applicationIdColumn) throws WNoResultException {
		String sql = String.format(PRE_APPLICANT_NAME_SQL_FORMAT, tableName, applicationIdColumn);

		try {
			return jdbcManager.selectBySql(String.class, sql, mailId, DeleteFlgKbn.NOT_DELETED, DeleteFlgKbn.NOT_DELETED)
					.disallowNoResult()
					.getSingleResult();
		} catch (SNoResultException e) {
			throw new WNoResultException(e);
		}
	}

	/**
	 * エリアコードからエリア名を取得します。
	 * @param areaCd
	 * @return
	 */
	private String getAreaNameByAreaCd(Integer areaCd) {
		String areaName = "";
		switch(areaCd) {
			case 1:
				areaName = "首都圏";
				break;
			case 2:
				areaName = "東北";
				break;
			case 3:
				areaName = "関西";
				break;
			case 4:
				areaName = "東海";
				break;
			case 5:
				areaName = "九州";
				break;
		}
		return areaName;
	}
}


