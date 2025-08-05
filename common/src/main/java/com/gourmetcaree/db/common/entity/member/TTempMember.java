package com.gourmetcaree.db.common.entity.member;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

/**
 * 仮会員エンティティ
 * @author nakamori
 *
 */
@Entity
@Table(name = TTempMember.TABLE_NAME)
public class TTempMember extends BaseMemberEntity {


	/**
	 *
	 */
	private static final long serialVersionUID = -68058844793189876L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_temp_member_id_gen")
	@SequenceGenerator(name="t_temp_member_id_gen", sequenceName="t_temp_member_id_seq", allocationSize=1)
	@Column(name = ID)
	public Integer id;

	/** 登録日時 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = REGISTRATION_DATETIME)
	public Date registrationDatetime;


	/** アクセスコード */
	@Column(name = ACCESS_CD)
	public String accessCd;

	/** 会員登録済みフラグ */
	@Column(name = MEMBER_REGISTERED_FLG)
	public Integer memberRegisteredFlg;

	/** リマインドメール送信ステータス */
	@Column(name = REMIND_MAIL_STATUS)
	public Integer remindMailStatus;

	@Column(name = NEXT_REMIND_MAIL_DATETIME)
	public Timestamp nextRemindMailDatetime;

	/** ジャスキルフラグ */
	@Column(name = JUSKILL_FLG)
	public Integer juskillFlg;

	/** 会員ID */
	@Column(name = MEMBER_ID)
	public Integer memberId;

	/** 応募ID */
	@Column(name = APPLICATION_ID)
	public Integer applicationId;

	/** テーブル名 */
	public static final String TABLE_NAME = "t_temp_member";

	/** ID */
	public static final String ID = "id";

	/** 登録日時 */
	public static final String REGISTRATION_DATETIME = "registration_datetime";

	/** アクセスコード */
	public static final String ACCESS_CD = "access_cd";

	/** 会員登録済みフラグ */
	public static final String MEMBER_REGISTERED_FLG = "member_registered_flg";

	/** リマインドメール送信ステータス */
	public static final String REMIND_MAIL_STATUS = "remind_mail_status";

	/** 次回メール送信日時 */
	public static final String NEXT_REMIND_MAIL_DATETIME = "next_remind_mail_datetime";

	/** ジャスキルフラグ */
	public static final String JUSKILL_FLG = "juskill_flg";

	/** 会員ID */
	public static final String MEMBER_ID = "member_id";

	/** 応募ID */
	public static final String APPLICATION_ID = "application_id";

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}


	/**
	 * リマインドメールステータスと、次回配信日時をインクリメント
	 */
	public void incrementRemindMailStatusAndDatetime() {
		this.remindMailStatus = RemindMailStatus.increment(this.remindMailStatus);
		final Date incrementedDate;

		// 登録日時からの経過時間を設定する。
		switch (remindMailStatus.intValue()) {
			case RemindMailStatus.ONE_DAY:
				incrementedDate = DateUtils.addDays(registrationDatetime, 1);
				break;
			case RemindMailStatus.THREE_DAY:
				incrementedDate = DateUtils.addDays(registrationDatetime, 3);
				break;
			case RemindMailStatus.ONE_WEEK:
				incrementedDate = DateUtils.addWeeks(registrationDatetime, 1);
				break;

			default:
				// 次回予約がないため設定しない。
				return;
		}
		nextRemindMailDatetime = new Timestamp(incrementedDate.getTime());
	}


	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}


	/**
	 * リマインドメール送信ステータス
	 *
	 */
	public static class RemindMailStatus {
		/** 24時間後 */
		public static final int ONE_DAY = 100;

		/** 72時間語 */
		public static final int THREE_DAY = 200;

		/** 一週間後 */
		public static final int ONE_WEEK = 300;

		/** 送信完了 */
		public static final int COMPLETE = 9000000;

		/**
		 * リマインドメールステータスのインクリメントを行います。
		 */
		public static int increment(final Integer remindMailStatus) {
			if (remindMailStatus == null) {
				return ONE_DAY;
			}

			switch (remindMailStatus.intValue()) {
				case ONE_DAY:
					return THREE_DAY;
				case THREE_DAY:
					return ONE_WEEK;
				case ONE_WEEK:
					// 完了済みの場合は更新しない。
				case COMPLETE:
					return COMPLETE;
				default:
					final String msg = String.format(
							"不正な値が引数に渡されました。ONE_DAYを返却します。値：[%d]",
							remindMailStatus);
					Logger.getLogger(RemindMailStatus.class)
						.fatal(msg, new IllegalArgumentException(msg));
					return ONE_DAY;
			}
		}
	}
}
