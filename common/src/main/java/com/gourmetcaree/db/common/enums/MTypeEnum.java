package com.gourmetcaree.db.common.enums;

import com.gourmetcaree.db.common.constants.MTypeConstants;

/**
 * 区分マスタのEnum定義です。
 * @author Makoto Otani
 * @version 1.0
 */
public class MTypeEnum {


	/**
	 * 端末区分のEnum
	 * @author Makoto Otani
	 *
	 */
	public enum TerminalKbnEnum {

		/** 端末区分_PC(1) */
		PC_VALUE(MTypeConstants.TerminalKbn.PC_VALUE),

		/** 端末区分_MOBILE(2) */
		MOBILE_VALUE(MTypeConstants.TerminalKbn.MOBILE_VALUE);

		/** 区分値 */
		private int value;

		/** コンストラクタ */
		private TerminalKbnEnum(int kbnValue) {
			this.value = kbnValue;
		};

		public int getValue() {
			return value;
		}

		/**
		 * 値からEnumを取得
		 * @param kbnValue
		 * @return Enumの値
		 */
		public static TerminalKbnEnum getEnum(int kbnValue) {
			if (kbnValue == MTypeConstants.TerminalKbn.PC_VALUE) {
				return TerminalKbnEnum.PC_VALUE;
			} else if (kbnValue == MTypeConstants.TerminalKbn.MOBILE_VALUE) {
				return TerminalKbnEnum.MOBILE_VALUE;
			} else {
				return TerminalKbnEnum.PC_VALUE;
			}
		}
	}

	/**
	 * メールステータスのEnum
	 * @author Takahiro Ando
	 */
	public enum MailStatusEnum {
		/** 返信済 */
		REPLIED(MTypeConstants.MailStatus.REPLIED),
		/** 開封済 */
		OPENED(MTypeConstants.MailStatus.OPENED),
		/** 未開封 */
		UNOPENED(MTypeConstants.MailStatus.UNOPENED);

		/** 区分値 */
		private int value;

		/** コンストラクタ */
		private MailStatusEnum(int kbnValue) {
			this.value = kbnValue;
		};

		/** 区分値を取得します */
		public int getValue() {
			return value;
		}
	}

	/**
	 * 送受信者区分のEnum
	 * @author Takahiro Ando
	 */
	public enum SenderKbnEnum {
		/** 顧客 */
		CUSTOMER(MTypeConstants.SenderKbn.CUSTOMER),
		/** 会員 */
		MEMBER(MTypeConstants.SenderKbn.MEMBER),
		/** 非会員 */
		NO_MEMBER(MTypeConstants.SenderKbn.NO_MEMBER);

		/** 区分値 */
		private int value;

		/** コンストラクタ */
		private SenderKbnEnum(int kbnValue) {
			this.value = kbnValue;
		};

		/** 区分値を取得します */
		public int getValue() {
			return value;
		}
	}

	/**
	 * ユーザー区分のEnum
	 * @author Takahiro Ando
	 *
	 */
	public enum UserKbnEnum {
		/** 顧客 */
		CUSTOMER(MTypeConstants.UserKbn.CUSTOMER),
		/** 会員 */
		MEMBER(MTypeConstants.UserKbn.MEMBER),
		/** 事前登録エントリ */
		ADVANCED_REGISTRATION(MTypeConstants.UserKbn.ADVANCED_REGISTRATION)
		;


		/** 区分値 */
		private int value;

		/** コンストラクタ */
		private UserKbnEnum(int kbnValue) {
			this.value = kbnValue;
		};

		/** 区分値を取得します */
		public int getValue() {
			return value;
		}
	}
}
