package com.gourmetcaree.db.common.constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.math.NumberUtils;

/**
 * 区分マスタの定義ファイル
 * @author Makoto Otani
 * @version 1.0
 *
 */
public interface MTypeConstants {

	/**
	 * ログインフラグの定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class LoginFlg {
		/** ログインフラグ */
		public static final String TYPE_CD = "login_flg";

		/** ログイン可 */
		public static final int OK = 1;

		/** ログイン不可 */
		public static final int NG = 0;
	}

	/**
	 * 掲載フラグの定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class PublicationFlg {
		/** 掲載フラグ */
		public static final String TYPE_CD = "publication_flg";

		/** 掲載フラグ：掲載可 */
		public static final int OK = 1;

		/** 掲載フラグ：掲載不可 */
		public static final int NG = 0;
	}


	/**
	 * 掲載終了時の表示フラグの定義です。
	 * @author Takehiro Nakamori
	 *
	 */
	public static class PublicationEndDisplayFlg {
		/** 掲載終了時の表示フラグ */
		public static final String TYPE_CD = "publication_end_display_flg";

		/** 表示しない(404NotFound) */
		public static final int DISPLAY_NG = 0;

		/** 表示する */
		public static final int DISPLAY_OK = 1;
	}

	/**
	 * 求人の検索対象フラグの定義
	 */
	public static class SearchTargetFlg {
		/** 検索対象フラグ */
		public static final String TYPE_CD = "search_target_flg";

		/** 非対象 */
		public static final int NO_TARGET = 0;

		/** 対象 */
		public static final int TARGET = 1;
	}

	/**
	 * サブメール受信フラグの定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class SubmailReceptionFlg {
		/** サブメール受信フラグ */
		public static final String TYPE_CD = "submail_reception_flg";

		/** サブメール受信フラグ_受信否(0) */
		public static final int NOT_RECEIVE = 0;
		/** サブメール受信フラグ_受信可(1) */
		public static final int RECEIVE = 1;
	}

	/**
	 * スカウトメール使用フラグの定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class ScoutUseFlg {
		/** スカウトメール使用フラグ */
		public static final String TYPE_CD = "scout_use_flg";

		/** 使用可 */
		public static final int OK = 1;

		/** 使用不可 */
		public static final int NG = 0;
	}

	/**
	 * 会社区分の定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class CompanyKbn {
		/** 会社区分 */
		public static final String TYPE_CD = "company_kbn";
	}

	/**
	 * 代理店フラグの定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class AgencyFlg {
		/** 代理店フラグ */
		public static final String TYPE_CD = "agency_flg";

		/** 代理店フラグ_代理店以外(0) */
		public static final int NOT_AGENCY = 0;
		/** 代理店フラグ_代理店(1) */
		public static final int AGENCY = 1;
	}

	/**
	 * 有効フラグの定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class ValidFlg {
		/** 有効フラグ */
		public static final String TYPE_CD = "valid_flg";

		/** 無効 */
		public static final int INVALID = 0;

		/** 有効 */
		public static final int VALID = 1;
	}

	/**
	 * 配信先区分の定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class DeliveryKbn {
		/** 配信先区分 */
		public static final String TYPE_CD = "delivery_kbn";

		/** 配信先区分_顧客(1) */
		public static final int CUSTOMER = 1;
		/** 配信先区分_会員(2) */
		public static final int MEMBER = 2;
		/** 配信先区分_事前登録会員(3) */
		public static final int ADVANCED_REGISTRATION_DELIVERY_KBN = 3;
		/** 配信先区分_ジャスキル(4) */
		public static final int JUSKILL = 4;

	}

	/**
	 * 端末区分の定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class TerminalKbn {
		/** 端末区分 */
		public static final String TYPE_CD = "terminal_kbn";

		/** 端末区分_PC(1) */
		public static final int PC_VALUE = 1;
		/** 端末区分_MOBILE(2) */
		public static final int MOBILE_VALUE = 2;
		/** 端末区分_SMART(3) */
		public static final int SMART_VALUE = 3;
	}

	/**
	 * 配信フラグの定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class DeliveryFlg {
		/** 配信フラグ */
		public static final String TYPE_CD = "delivery_flg";

		/** 配信フラグ_未配信(0) */
		public static final int NON = 0;
		/** 配信フラグ_配信済(1) */
		public static final int FINISHED = 1;
	}

	/**
	 * 管理画面区分の定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class ManagementScreenKbn {
		/** 管理画面区分 */
		public static final String TYPE_CD = "management_screen_kbn";

		/** 管理画面区分_管理画面(1) */
		public static final int ADMIN_SCREEN = 1;
		/** 管理画面区分_店舗管理画面(2) */
		public static final int SHOP_SCREEN = 2;
		/** 管理画面区分_MyPage(3) */
		public static final int MY_PAGE_SCREEN = 3;
	}

	/**
	 * 転勤フラグの定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class TransferFlg {
		/** 転勤フラグ */
		public static final String TYPE_CD = "transfer_flg";

		/** 転勤不可(0) */
		public static final int NG = 0;
		/** 転勤可(1) */
		public static final int OK = 1;
	}

	/**
	 * 深夜勤フラグの定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class MidnightShiftFlg {
		/** 深夜勤フラグ */
		public static final String TYPE_CD = "midnight_shift_flg";

		/** 深夜勤務不可(0) */
		public static final int NG = 0;
		/** 深夜勤務可(1) */
		public static final int OK = 1;
	}

	/**
	 * 海外勤務経験フラグの定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class ForeignWorkFlg {
		/** 海外勤務経験フラグ */
		public static final String TYPE_CD = "foreign_work_flg";
	}

	/**
	 * 飲食業界経験年数区分の定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class FoodExpKbn {
		/** 飲食業界経験年数区分 */
		public static final String TYPE_CD = "food_exp_kbn";
	}

	/**
	 * 年収区分の定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class SalaryKbn {
		/** 年収区分 */
		public static final String TYPE_CD = "salary_kbn";
	}

	/**
	 * 削除理由区分の定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class DeleteReasonKbn {
		/** 削除理由区分 */
		public static final String TYPE_CD = "delete_reason_kbn";
	}

	/**
	 * 会員区分の定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class MemberKbn {
		/** 会員区分 */
		public static final String TYPE_CD = "member_kbn";

		/** 新会員 */
		public static final int NEW_MEMBER = 1;
		/** 未ログイン旧会員 */
		public static final int NON_LOGIN_OLD_MEMBER = 2;
		/** ログイン済旧会員 */
		public static final int LOGIN_OLD_MEMBER = 3;
		/** テスト会員 */
		public static final int TEST_MEMBER = 4;

		/** 事前登録会員 */
		public static final int ADVANCED_REGISTRATION_MEMBER = 5;
		/** 事前登録会員 + 新会員 */
		public static final int ADVANCED_REGISTRATION_NEW_MEMBER = 6;
		/** 事前登録会員 + 未ログイン旧会員 */
		public static final int ADVANCED_REGISTRATION_NON_LOGIN_OLD_MEMBER = 7;
		/** 事前登録会員 + ログイン済旧会員 */
		public static final int ADVANCED_REGISTRATION_LOGIN_OLD_MEMBER = 8;
		/** 事前登録会員 + テスト会員 */
		public static final int ADVANCED_REGISTRATION_TEST_MEMBER = 9;

		/** グルメ会員リスト */
		public static final List<Integer> GOURMETCAREE_MEMBER_LIST;
		/** 事前登録会員リスト */
		public static final List<Integer> ADVANCED_REGISTRATION_MEMBER_LIST;
		/** 事前登録済のグルメ会員リスト */
		public static final List<Integer> ADVANCED_REGISTRATION_GOURMETCAREE_MEMBER_LIST;
		/** グルメ会員のみのリスト */
		public static final List<Integer> ONLY_GOURMETCAREE_MEMBER_LIST;
		static {
			List<Integer> advancedList = new ArrayList<Integer>();
			advancedList.add(ADVANCED_REGISTRATION_MEMBER);
			advancedList.add(ADVANCED_REGISTRATION_NEW_MEMBER);
			advancedList.add(ADVANCED_REGISTRATION_NON_LOGIN_OLD_MEMBER);
			advancedList.add(ADVANCED_REGISTRATION_LOGIN_OLD_MEMBER);
			advancedList.add(ADVANCED_REGISTRATION_TEST_MEMBER);
			ADVANCED_REGISTRATION_MEMBER_LIST =
					Collections.unmodifiableList(advancedList);

			List<Integer> gourmetList = new ArrayList<Integer>();
			gourmetList.add(NEW_MEMBER);
			gourmetList.add(NON_LOGIN_OLD_MEMBER);
			gourmetList.add(LOGIN_OLD_MEMBER);
			gourmetList.add(TEST_MEMBER);
			gourmetList.add(ADVANCED_REGISTRATION_NEW_MEMBER);
			gourmetList.add(ADVANCED_REGISTRATION_NON_LOGIN_OLD_MEMBER);
			gourmetList.add(ADVANCED_REGISTRATION_LOGIN_OLD_MEMBER);
			gourmetList.add(ADVANCED_REGISTRATION_TEST_MEMBER);
			GOURMETCAREE_MEMBER_LIST = Collections.unmodifiableList(gourmetList);

			List<Integer> onlyGourmetList = new ArrayList<Integer>();
			onlyGourmetList.add(NEW_MEMBER);
			onlyGourmetList.add(NON_LOGIN_OLD_MEMBER);
			onlyGourmetList.add(LOGIN_OLD_MEMBER);
			onlyGourmetList.add(TEST_MEMBER);
			ONLY_GOURMETCAREE_MEMBER_LIST = Collections.unmodifiableList(onlyGourmetList);

			List<Integer> advancedGourmetList = new ArrayList<Integer>();
			advancedGourmetList.add(ADVANCED_REGISTRATION_NEW_MEMBER);
			advancedGourmetList.add(ADVANCED_REGISTRATION_NON_LOGIN_OLD_MEMBER);
			advancedGourmetList.add(ADVANCED_REGISTRATION_LOGIN_OLD_MEMBER);
			advancedGourmetList.add(ADVANCED_REGISTRATION_TEST_MEMBER);
			ADVANCED_REGISTRATION_GOURMETCAREE_MEMBER_LIST = Collections.unmodifiableList(advancedGourmetList);

		}
	}

	/**
	 * 退会理由区分の定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class WithdrawalReasonKbn {
		/** 退会理由区分 */
		public static final String TYPE_CD = "withdrawal_reason_kbn";
	}

	/**
	 * 性別の定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class Sex {
		/** 性別 */
		public static final String TYPE_CD = "sex_kbn";

		/** 男性 */
		public static final int MALE = 1;

		/** 女性 */
		public static final int FEMALE = 2;

		/** 回答なし */
		public static final int OTHER = 3;


		/**
		 * male / female の文字から値を取得
		 */
		public static Integer getTypeValueFromMaleFamaleLabel(String label) {
			if ("female".equals(label)) {
				return FEMALE;
			} else if ("male".equals(label)) {
				return MALE;
			}
			return OTHER;
		}
	}

	/**
	 * 資格区分の定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class QualificationKbn {
		/** 資格区分 */
		public static final String TYPE_CD = "qualification_kbn";
	}

	/**
	 * 入社可能時期の定数を保持するクラス
	 */
	public static final class PossibleEntryTermKbn {
		/** 入社可能区分 */
		public static final String TYPE_CD = "possible_entry_term_kbn";
	}

	/**
	 * 勤務可能時期の定数を保持するクラス
	 * @author Takehiro Nakamori
	 *
	 */
	public static class PossibleWorkTermKbn {
		/** 勤務可能時期 */
		public static final String TYPE_CD = "possible_work_term_kbn";
	}


	/**
	 * 転職希望時期
	 * 主に事前登録の登録画面で使う
	 * @author nakamori
	 *
	 */
	public static class HopeCareerChangeTerm {
		/** 転職希望時期 */
		public static final String TYPE_CD = "hope_career_change_term";

		/** すぐにでも */
		public static final int NOW = 1;

		/** 未定 */
		public static final int NO_PLAN = 2;
	}

	/**
	 * 状況区分の定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class GraduationKbn {
		/** 状況区分 */
		public static final String TYPE_CD = "graduation_kbn";
	}

	/**
	 * キャリア区分の定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class CarrierKbn {
		/** キャリア区分 */
		public static final String TYPE_CD = "carrier_kbn";
	}

	/**
	 * 携帯ドメイン区分の定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class MobileDomainKbn {
		/** 携帯ドメイン区分 */
		public static final String TYPE_CD = "mobile_domain_kbn";
	}

	/**
	 * メール区分の定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class MailKbn {
		/** メール区分 */
		public static final String TYPE_CD = "mail_kbn";

		/** メール区分 応募*/
		public static final int APPLICCATION = 1;

		/** メール区分 スカウト*/
		public static final int SCOUT = 2;

		/** メール区分 店舗見学 */
		public static final int OBSERVATE_APPLICATION = 3;

		/** メール区分 アルバイト応募 */
		public static final int ARBEIT_APPLICATION = 4;

		/** メール区分 プレ応募*/
		public static final int PRE_APPLICCATION = 5;
	}

	/**
	 * 送受信区分の定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class SendKbn {
		/** 送受信区分 */
		public static final String TYPE_CD = "send_kbn";

		/** 送信 */
		public static final int SEND = 1;

		/** 受信 */
		public static final int RECEIVE = 2;
	}

	/**
	 * 送信者区分の定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class SenderKbn {
		/** 送信者区分 */
		public static final String TYPE_CD = "sender_kbn";

		/** 顧客 */
		public static final int CUSTOMER = 1;
		/** 会員 */
		public static final int MEMBER = 2;
		/** 非会員 */
		public static final int NO_MEMBER = 3;
	}

	/**
	 * メールステータスの定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class MailStatus {
		/** メールステータス */
		public static final String TYPE_CD = "mail_status";

		/** 未開封 */
		public static final int UNOPENED = 1;

		/** 開封済 */
		public static final int OPENED = 2;

		/** 返信済 */
		public static final int REPLIED = 3;
	}

	/**
	 * サイズ区分の定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class SizeKbn {
		/** サイズ区分 */
		public static final String TYPE_CD = "size_kbn";

		// 運営側にて区分値からpropertiesを見ている箇所があるため、
		// 変更時はメンテナンスが必要
		/** サイズ区分 A */
		public static final int A = 1;
		/** サイズ区分 B */
		public static final int B = 2;
		/** サイズ区分 C */
		public static final int C = 3;
		/** サイズ区分 D */
		public static final int D = 4;
		/** サイズ区分 E */
		public static final int E = 5;
		/** サイズ区分 テキストWEB */
		public static final int TEXT_WEB = 6;
	}

	/**
	 * 応募フォームフラグの定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class ApplicationFormKbn {
		/** 応募フォームフラグ */
		public static final String TYPE_CD = "application_form_kbn";

		/** 応募フォームフラグ_無(0) */
		public static final int NON = 0;
		/** 応募フォームフラグ_有(1) */
		public static final int EXIST = 1;
	}

	/**
	 * 動画フラグの定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class MovieFlg {
		/** 動画フラグ */
		public static final String TYPE_CD = "movie_flg";

		/** 動画フラグ_無(0) */
		public static final int NON = 0;
		/** 動画フラグ_有(1) */
		public static final int EXIST = 1;

	}

	/**
	 * 業種区分の定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class IndustryKbn {
		/** 業種区分 */
		public static final String TYPE_CD = "industry_kbn";

		/** 和食 */
		public static final int JAPAN = 1;
		/** 日本料理・懐石 */
		public static final int JAPAN_KAISEKI = 2;
		/** 居酒屋 */
		public static final int IZAKAYA = 3;
		/** うどん・蕎麦 */
		public static final int UDON_SOBA = 5;
		/** 寿司 */
		public static final int SUSHI = 6;
		/** 洋食・西洋料理 */
		public static final int  WESTERN = 7;
		/** 中華料理 */
		public static final int CHINA = 8;
		/** フレンチ */
		public static final int FRENCH = 9;
		/** イタリアン */
		public static final int ITALIAN = 11;
		/** スペイン */
		public static final int SPAIN = 12;
		/** バル */
		public static final int BARL = 13;
		/** ラーメン */
		public static final int RAMEN = 17;
		/** アジアン・エスニック */
		public static final int ASIAN = 18;
		/** BAR */
		public static final int BAR = 19;
		/** 専門店(各国料理) */
		public static final int SPECIALTY = 20;
		/** パティスリー・製菓 */
		public static final int PATISSERIE_SWEETS = 21;
		/** ベーカリー・製パン */
		public static final int BAKERY_BREAD = 22;
		/** カフェ */
		public static final int CAFE = 23;
		/** ホテル・旅館 */
		public static final int HOTEL = 24;
		/** 焼肉・肉料理 */
		public static final int MEAT = 25;
		/** ステーキ・鉄板焼き */
		public static final int STEAK = 26;
		/** 焼鳥・鳥料理 */
		public static final int CHICKEN = 28;
		/** ウェディング */
		public static final int WEDDING = 35;
		/** 惣菜・ＤＥＬＩ・仕出・弁当 */
		public static final int SOUZAI_DELI = 39;
		/** ケータリング */
		public static final int CATERING = 45;
		/** 精肉・鮮魚 */
		public static final int FRESH = 49;
		/** 人材紹介・派遣 */
		public static final int DISPATCH = 50;
		/** その他 */
		public static final int OTHER = 99;

		/** B画面へ遷移するリスト */
		public static final List<Integer> B_LIST;
		static  {
			List<Integer> bList = new ArrayList<Integer>();
			B_LIST = Collections.unmodifiableList(bList);
		}
	}

	/**
	 * 企業特徴区分の定数を保持するクラス
	 * @author t_shiroumaru
	 *
	 */
	public static final class CompanyCharacteristicKbn{
		/** 企業特徴区分*/
		public static final String TYPE_CD = "company_characteristic_kbn";

		/** 業界未経験者歓迎 */
		public static final int WELCOME_BEGINNER = 1;

		/** 学歴・資格不問 */
		public static final int NO_ASK_CAREER = 2;

		/** ブランクOK */
		public static final int BLANK_OK = 3;

		/** ベテラン活躍中 */
		public static final int SUCCESS_EXPERIENCED = 4;

		/** 新卒歓迎 */
		public static final int WELCOME_NEW_GRAADUATES = 5;

		/** 10名以上の採用 */
		public static final int MENY_ADOPTION = 6;

		/** 応募者全員との面接 */
		public static final int ABSOLUTE_INTERVIEW = 7;

		/** 即管理職採用 */
		public static final int IMMEDIATELY_ADOPTION_MANAGEMENT = 8;

		/** 履歴書不要 */
		public static final int NO_NEED_RESUME = 9;

		/** 上場企業 */
		public static final int LISED_COMPANY = 10;

		/** 個人経営 */
		public static final int PRIVATE_MANAGEMENT = 11;

		/** 月残業40時間以下 */
		public static final int FEW_OVERTIME = 12;

		/** 中途入社率50%以上 */
		public static final int MENY_MIDCAREER_ENTRY = 13;

		/** 女性比率50％以上 */
		public static final int MENY_FEMALE_EMPLOYEE = 14;

		/** 女性管理職登用実績あり */
		public static final int APPOINTMENTED_FEMALE_MANAGEMENT = 15;
		
		/** オープニングスタッフ */
		public static final int OPENING_STAFF = 16;
		
		/** シニア活躍中 */
		public static final int SENIOR_ACTIVE = 17;
		
		/** 社員登用制度有り */
		public static final int EMPLOYEE_APPOINTMENT_SYSTEM = 18;

		/** 企業の特徴（重要）のリスト */
		public static final List<Integer> COMPANY_CHARACTERISTIC_LIST;
		static  {
			List<Integer> list = new ArrayList<>();

			list.add(LISED_COMPANY);
			list.add(PRIVATE_MANAGEMENT);
			list.add(FEW_OVERTIME);
			list.add(MENY_MIDCAREER_ENTRY);
			list.add(MENY_FEMALE_EMPLOYEE);
			list.add(APPOINTMENTED_FEMALE_MANAGEMENT);

			COMPANY_CHARACTERISTIC_LIST = Collections.unmodifiableList(list);
		}

		/** 採用条件のリスト */
		public static final List<Integer> HIRING_REQUIREMENT_LIST;
		static  {
			List<Integer> list = new ArrayList<>();

			list.add(WELCOME_BEGINNER);
			list.add(NO_ASK_CAREER);
			list.add(BLANK_OK);
			list.add(SUCCESS_EXPERIENCED);
			list.add(WELCOME_NEW_GRAADUATES);
			list.add(MENY_ADOPTION);
			list.add(ABSOLUTE_INTERVIEW);
			list.add(IMMEDIATELY_ADOPTION_MANAGEMENT);
			list.add(NO_NEED_RESUME);
			list.add(OPENING_STAFF);
			list.add(SENIOR_ACTIVE);
			list.add(EMPLOYEE_APPOINTMENT_SYSTEM);

			HIRING_REQUIREMENT_LIST = Collections.unmodifiableList(list);
		}
	}

	/**
	 * 募集特徴区分の定数を保持するクラス
	 * @author t_shiroumaru
	 *
	 */
	public static final class RecruitmentCharacteristicKbn{
		/** 募集特徴区分 */
		public static final String TYPE_CD = "recruitment_characteristic_kbn";

		/** オープニングスタッフ募集 */
		public static final int RECRUITMENT_OPENING_STAFF = 1;
	}

	/**
	 * 仕事特徴区分を保持するクラス
	 * @author t_shiroumaru
	 *
	 */
	public static final class WorkCharacteristicKbn{
		/** 仕事特徴区分 */
		public static final String TYPE_CD = "work_characteristic_kbn";

		/** お酒学べる(ワイン・日本酒・焼酎等) */
		public static final int LEARN_LIQUOR = 1;

		/** 魚が学べる */
		public static final int LEARN_FISH = 2;

		/** 肉が学べる */
		public static final int LEARN_MEET = 3;

		/** 菓子パン・製パンが学べる */
		public static final int LEARN_BREAD = 4;

		/** サービスマナーが学べる */
		public static final int LEARN_SERVICE_MANNER = 5;

		/** 着付けが学べる */
		public static final int LEARN_DRESSING_UP = 6;

		/** 英会話能力を活かせる */
		public static final int SUCCESS_ENGLISH_CONVERSATION_ABILITY = 7;

		/** 資格を取得できる */
		public static final int GET_QUALIFICATION = 8;

	}

	/**
	 * 職場会社特徴区分の定数を保持するクラス
	 * @author t_shiroumaru
	 *
	 */
	public static final class ShopCharacteristicKbn{
		/** 職場会社特徴区分 */
		public static final String TYPE_CD ="shop_characteristic_kbn";

		/** 日曜定休 */
		public static final int CLOSED_ON_SUNDAY = 1;

		/** スタッフの平均年齢20代 */
		public static final int STAFF_AGE_AVERAGE_TWENTIES = 2;

		/** ミシュラン・ビブグルマン掲載店 */
		public static final int ISSUED_MICHELIN_BIB_GULLMAN = 3;

		/** 午後出勤 */
		public static final int ATTENDANCE_AFTERNOON = 4;

		/** 20代料理長・店長在籍 */
		public static final int YOUNG_CHEF_OWNER_ENROLLED = 5;

		/** IUターン・リゾート・その他 */
		public static final int IU_RESORT_OTHER = 6;


	}

	/**
	 * 主要駅区分の定数を保持するクラス
	 * @author Takahiro Ando
	 */
	public static final class MainStationKbn {

		/** エリアコードを指定して主要駅区分の区分コードを取得 */
		public static String getTypeCd(String areaCd) {
			return getTypeCd(NumberUtils.toInt(areaCd, MAreaConstants.AreaCd.SHUTOKEN_AREA));
		}

		/** エリアコードを指定して主要駅区分の区分コードを取得 */
		public static String getTypeCd(int areaCd) {
			if (areaCd == MAreaConstants.AreaCd.SHUTOKEN_AREA) {
				return ShutokenMainStationKbn.TYPE_CD;
			} else {
				//デフォルト
				return ShutokenMainStationKbn.TYPE_CD;
			}
		}
	}

	/**
	 * 首都圏主要駅区分の定数を保持するクラス
	 * @author Takehiro Nakamori
	 */
	public static final class ShutokenMainStationKbn {
		/** 東海主要駅区分 */
		public static final String TYPE_CD = "shutoken_main_station_kbn";

		/** 新宿駅周辺 */
		public static final int SHINJUKU = 1;
		/** 渋谷駅周辺 */
		public static final int SHIBUYA = 2;
		/** 秋葉原駅周辺 */
		public static final int AKIHABARA = 3;
		/** 品川駅周辺 */
		public static final int SHINAGAWA = 4;
		/** 池袋駅周辺 */
		public static final int IKEBUKURO = 5;
		/** 東京駅周辺 */
		public static final int TOKYO = 6;
		/** 銀座駅周辺 */
		public static final int GINZA = 7;
		/** 飯田橋駅周辺 */
		public static final int IIDA = 8;
		/** 赤坂駅周辺 */
		public static final int AKASAKA = 9;
		/** 台場駅周辺 */
		public static final int DAIBA = 10;
		/** 町田駅周辺 */
		public static final int MACHIDA = 11;
		/** 下北沢駅周辺 */
		public static final int SHIMOKITAZAWA = 12;
		/** 千葉駅周辺 */
		public static final int CHIBA = 13;
		/** 横浜駅周辺 */
		public static final int YOKOHAMA = 14;
		/** 川崎駅周辺 */
		public static final int KAWASAKI = 15;
		/** 大宮駅周辺 */
		public static final int OOMIYA = 16;
	}

	/**
	 * 勤務地エリア(WEBエリアから名称変更)区分の定数を保持するクラス
	 * @author Takahiro Ando
	 */
	public static final class WebAreaKbn {

		/** エリアコードを指定して勤務地エリアの区分コードを取得 */
		public static String getTypeCd(String areaCd) {
			return getTypeCd(NumberUtils.toInt(areaCd, MAreaConstants.AreaCd.SHUTOKEN_AREA));
		}

		/** エリアコードを指定して勤務地エリアの区分コードを取得 */
		public static String getTypeCd(int areaCd) {
			if (areaCd == MAreaConstants.AreaCd.SHUTOKEN_AREA) {
				return ShutokenWebAreaKbn.TYPE_CD;

			} else if (areaCd == MAreaConstants.AreaCd.SENDAI_AREA) {
				return SendaiWebAreaKbn.TYPE_CD;
			} else {
				//デフォルト
				return ShutokenWebAreaKbn.TYPE_CD;
			}
		}

		/** エリアコードを指定して勤務地エリアの区分コードを取得 */
		public static List<Integer> getWebdataNoDispList(String areaCd) {
			return getWebdataNoDispList(NumberUtils.toInt(areaCd, MAreaConstants.AreaCd.SHUTOKEN_AREA));
		}

		/** 公開側で表示しない区分値を取得します。 */
		public static List<Integer> getWebdataNoDispList(int areaCd) {
			List<Integer> noDispList = new ArrayList<Integer>();

			if (areaCd == MAreaConstants.AreaCd.SHUTOKEN_AREA) {
				// 首都圏勤務地エリアから取得
				noDispList = ShutokenWebAreaKbn.getWebdataNoDispList();
			}
//			else {
//				//デフォルト（首都圏）
//				noDispList = ShutokenWebAreaKbn.getWebdataNoDispList();
//			}

			return noDispList;
		}

		/**
		 * 会員が表示する際、非表示にするコードを取得
		 * @param areaCd エリアコード
		 * @return 非表示のコードリスト
		 */
		public static List<Integer> getMemberNoDispList(String areaCd) {
			return getMemberNoDispList(NumberUtils.toInt(areaCd, MAreaConstants.AreaCd.SHUTOKEN_AREA));
		}

		/**
		 * 会員が表示する際、非表示にするコードを取得
		 * @param areaCd エリアコード
		 * @return 非表示のコードリスト
		 */
		public static List<Integer> getMemberNoDispList(int areaCd) {
			List<Integer> noDispList;
//			= new ArrayList<Integer>();

//			if (areaCd == MAreaConstants.AreaCd.SHUTOKEN_AREA) {
				// 首都圏勤務地エリアから取得
				noDispList = ShutokenWebAreaKbn.getMemberNoDispList();
//			}
//			else {
//				//デフォルト（首都圏）
//				noDispList = ShutokenWebAreaKbn.getMemberNoDispList();
//			}

			return noDispList;
		}

		/**
		 * 店舗が表示する際、非表示にするコードを取得
		 * @param areaCd エリアコード
		 * @return 非表示のコードリスト
		 */
		public static List<Integer> getShopNoDispList(String areaCd) {
			return getShopNoDispList(NumberUtils.toInt(areaCd, MAreaConstants.AreaCd.SHUTOKEN_AREA));
		}

		/**
		 * 店舗が表示する際、非表示にするコードを取得
		 * @param areaCd エリアコード
		 * @return 非表示のコードリスト
		 */
		public static List<Integer> getShopNoDispList(int areaCd) {
			List<Integer> noDispList;
//			= new ArrayList<Integer>();

//			if (areaCd == MAreaConstants.AreaCd.SHUTOKEN_AREA) {
				// 首都圏勤務地エリアから取得
				noDispList = ShutokenWebAreaKbn.getShopNoDispList();
//			}
//			else {
//				//デフォルト（首都圏）
//				noDispList = ShutokenWebAreaKbn.getShopNoDispList();
//			}

			return noDispList;
		}

		/**
		 * 各エリアの「こだわらない」の値を取得
		 * @param areaCd エリアコード
		 * @return 「こだわらない」の値
		 */
		public static int getNotDecideValue(String areaCd) {
			return getNotDecideValue(NumberUtils.toInt(areaCd, MAreaConstants.AreaCd.SHUTOKEN_AREA));
		}

		/**
		 * 各エリアの「こだわらない」の値を取得
		 * @param areaCd エリアコード
		 * @return 「こだわらない」の値
		 */
		public static int getNotDecideValue(int areaCd) {

//			if (areaCd == MAreaConstants.AreaCd.SHUTOKEN_AREA) {
				// 首都圏勤務地エリアから取得
				return ShutokenWebAreaKbn.NOT_DECIDE;
//			}
//			else {
//				//デフォルト（首都圏）
//				return ShutokenWebAreaKbn.NOT_DECIDE;
//			}
		}


		/**
		 * その他エリアのタイプコードリストを取得
		 * @param areaCd エリアコード
		 * @return その他エリアのタイプコードリスト
		 */
		public static List<Integer> getOtherAreaList(int areaCd) {
			if (MAreaConstants.AreaCd.isSendai(areaCd)) {
				return SendaiWebAreaKbn.getOtherAreaList();
			}

			return ShutokenWebAreaKbn.getOtherAreaList();
		}
	}

	/**
	 * エリア詳細区分
	 * 勤務地エリア(WEBエリアから名称変更)区分とは別物なので注意
	 * @author Takehiro Nakamori
	 *
	 */
	public static final class DetailAreaKbn {

		/** エリアコードを指定してエリアの区分コードを取得 */
		public static String getTypeCd(String areaCd) {
			return getTypeCd(NumberUtils.toInt(areaCd, MAreaConstants.AreaCd.SHUTOKEN_AREA));
		}

		/** エリアコードを指定してエリアの区分コードを取得 */
		public static String getTypeCd(int areaCd) {
			if (areaCd == MAreaConstants.AreaCd.SHUTOKEN_AREA) {
				return ShutokenDetailAreaKbn.TYPE_CD;
			} else if (areaCd == MAreaConstants.AreaCd.SENDAI_AREA) {
				return SendaiDetailAreaKbn.TYPE_CD;
			}

			return ShutokenDetailAreaKbn.TYPE_CD;
		}

		public static List<Integer> getOtherAreaList(int areaCd) {
			if (areaCd == MAreaConstants.AreaCd.SENDAI_AREA) {
				return SendaiDetailAreaKbn.getOtherAreaList();
			}

			return ShutokenDetailAreaKbn.getOtherAreaList();
		}

	}


	public static final class SendaiDetailAreaKbn {
		public static final String TYPE_CD = "sendai_detail_area_kbn";

		/** 東北・北海道以外 */
		public static final int OTHER_HOKURIKU = 200016;

		public static List<Integer> getOtherAreaList() {
			return Collections.singletonList(OTHER_HOKURIKU);
		}
	}

	/**
	 * 首都圏エリア詳細区分
	 * 勤務地エリア(WEBエリアから名称変更)区分とは別ものなので注意
	 * @author Takehiro Nakamori
	 *
	 */
	public static final class ShutokenDetailAreaKbn {
		/** 首都圏エリア区分 */
		public static final String TYPE_CD = "shutoken_detail_area_kbn";

		/** 丸の内・八重洲 */
		public static final int MARUNOUCHI_YAESU = 1;
		/** 渋谷・恵比寿 */
		public static final int SHIBUYA_EBISU = 2;
		/** 代官山・中目黒 */
		public static final int DAIKANYAMA_NAKAMEGURO = 3;
		/** 新宿・代々木 */
		public static final int SHINJUKU_YOYOGI = 4;
		/** 原宿・表参道 */
		public static final int HARAJUKU_OMOTESANDO = 5;
		/** 六本木・麻布十番・西麻布 */
		public static final int ROPPONGI_AZABUJUBAN_NISHIAZABU = 6;
		/** 広尾・白金・目黒 */
		public static final int HIROO_SHIROKANE_MEGURO = 7;
		/** 赤坂・永田町 */
		public static final int AKASAKA_NAGATACHO = 8;
		/** 銀座・築地・有楽町 */
		public static final int GINZA_TSUKIJI_YURAKUCHO = 9;
		/** 下北沢･明大前 */
		public static final int SHIROKITAZAWA_MEIDAIMAE = 10;
		/** 二子玉川・成城学園 */
		public static final int FUTAKOTAMAGAWA_SEIJOGAKUEN = 11;
		/** 新橋・浜松町・田町 */
		public static final int SHINBASHI_HAMAMATSUCHO_TAMACHI = 12;
		/** 品川・蒲田・大井町 */
		public static final int SHINAGAWA_KAMATA_OOIMACHI = 13;
		/** 自由が丘・武蔵小杉 */
		public static final int JIYUUGAOKA_MUSASHIKOSUGI = 14;
		/** 池袋・練馬・板橋 */
		public static final int IKEBUKURO_NERIMA_ITABASHI = 15;
		/** 四ツ谷・飯田橋 */
		public static final int YOTSUYA_IIDABASHI = 16;
		/** 神田・秋葉原 */
		public static final int KANDA_AKIHABARA = 17;
		/** 日本橋 */
		public static final int NIHONBASHI_AREA = 37;
		/** 上野・浅草・北千住 */
		public static final int UENO_ASAKUSA_KITASENJU = 18;
		/** お台場・豊洲・有明 */
		public static final int ODAIBA_TOYOSU_ARIAKE = 19;
		/** 吉祥寺・高円寺・中野 */
		public static final int KICHIJOUJI_KOUENJI_NAKANO = 20;
		/** 町田・相模原・大和 */
		public static final int MACHIDA_SAGAMIHARA_YAMATO = 21;
		/** 国分寺・立川・八王子 */
		public static final int KOKUBUNJI_TACHIKAWA_HACHIOUJI = 22;
		/** 府中・調布 */
		public static final int FUCHUU_CHOUFU = 23;
		/** 横浜（西区・中区・神奈川区） */
		public static final int YOKOHAMA = 24;
		/** 横浜市北部 */
		public static final int YOKOHAMA_HOKUBU = 25;
		/** 横浜市南部 */
		public static final int YOKOHAMA_NANBU = 26;
		/** 鎌倉･逗子・横須賀 */
		public static final int KAMAKURA_IZU_YOKOSUKA = 27;
		/** 藤沢・茅ヶ崎・湘南 */
		public static final int FUJISAWA_CHIGASAKI_SHOUNAN = 28;
		/** 川崎 */
		public static final int KAWASAKI = 29;
		/** 小田原・箱根 */
		public static final int ODAWARA_HAKONE = 30;
		/** 千葉・船橋 */
		public static final int CHIBA_FUNABASHI = 31;
		/** 浦安・舞浜 */
		public static final int URAYASU_MAIHAMA = 32;
		/** 柏・松戸 */
		public static final int KASHIWA_MATSUDO = 33;
		/** 越谷・草加・春日部 */
		public static final int KOSHIGAYA_KUSAKA_KUSAKABE = 34;
		/** 所沢・川越 */
		public static final int TOKOROZAWA_KAWAGOE = 35;
		/** 大宮・浦和・さいたま市 */
		public static final int OOMIYA_URAWA_SAITAMA = 36;

		public static List<Integer> getOtherAreaList() {
			return Collections.emptyList();
		}
	}

    /**
     * 詳細エリアグループ
     */
    class DetailAreaKbnGroup {
        public static String getTypeCd(String areaCd) {
            return getTypeCd(NumberUtils.toInt(areaCd));
        }
        public static String getTypeCd(int areaCd) {
            if (MAreaConstants.AreaCd.isSendai(areaCd)) {
                return SendaiDetailAreaKbnGroup.TYPE_CD;
            }

            return ShutokenDetailAreaKbnGroup.TYPE_CD;
        }

        public static List<Integer> getOtherAreaList(int areaCd) {
            if (MAreaConstants.AreaCd.isSendai(areaCd)) {
                return SendaiDetailAreaKbnGroup.getOtherAreaList();
            }

            return ShutokenDetailAreaKbnGroup.getOtherAreaList();
        }

    }

    /**
     * 首都圏用詳細エリアグループ
     */
    class ShutokenDetailAreaKbnGroup {
        public static final String TYPE_CD = "shutoken_detail_area_kbn_group";
        public static List<Integer> getOtherAreaList() {
            return Collections.emptyList();
        }
    }

    /**
     * 仙台用詳細エリアグループ
     */
    class SendaiDetailAreaKbnGroup {
        public static final String TYPE_CD = "sendai_detail_area_kbn_group";
        /** 東北・北海道以外 */
        public static final int OTHER_HOKURIKU = 200016;
        public static List<Integer> getOtherAreaList() {
            return Collections.singletonList(OTHER_HOKURIKU);
        }
    }


	/**
	 * 勤務地エリア(WEBエリアから名称変更)の説明区分
	 * @author Takehiro Nakamori
	 *
	 */
	public static final class WebAreaDescriptionKbn {

		/** エリアコードを指定してエリアの区分コードを取得 */
		public static String getTypeCd(String areaCd) {
			return getTypeCd(NumberUtils.toInt(areaCd, MAreaConstants.AreaCd.SHUTOKEN_AREA));
		}

		/** エリアコードを指定してエリアの区分コードを取得 */
		public static String getTypeCd(int areaCd) {
			if (areaCd == MAreaConstants.AreaCd.SHUTOKEN_AREA) {
				return ShutokenWebAreaDescriptionKbn.TYPE_CD;
			}

			return ShutokenWebAreaDescriptionKbn.TYPE_CD;
		}

	}

	/**
	 * 首都圏勤務地エリア(WEBエリアから名称変更)の説明区分を保持するクラス
	 * 対応する勤務地エリア(WEBエリアから名称変更)の番号と同じにすること。
	 * @author Takehiro Nakamori
	 *
	 */
	public static final class ShutokenWebAreaDescriptionKbn {
		/** 首都圏勤務地エリア説明区分 */
		public static final String TYPE_CD = "shutoken_web_area_description_kbn";

		/** 東京東部 */
		public static final int TOKYO_TOBU = 1;
		/** 東京西部 */
		public static final int TOKYO_SEIBU = 2;
		/** 東京南部 */
		public static final int TOKYO_NAMBU = 3;
		/** 東京北部 */
		public static final int TOKYO_HOKUBU = 4;
		/** 東京都下 */
		public static final int TOKYO_TOKA = 5;
		/** 神奈川県 */
		public static final int KANAGAWA = 6;
		/** 千葉県 */
		public static final int CHIBA = 7;
		/** 埼玉県 */
		public static final int SAITAMA = 8;
		/** IUターン・リゾート・その他 */
		public static final int IU_RESORT_OTHER = 9;
		/** 海外 */
		public static final int FOREIGN = 10;
	}


	/**
	 * 仙台ウェブエリア区分を保持するクラス
	 * 200000台と、210000台があるが、200000台は仙台系、210000台は仙台以外
	 * @author Makoto Otani
	 *
	 */
	public static final class SendaiWebAreaKbn {

		/** 仙台ウェブエリア区分 */
		public static final String TYPE_CD = "sendai_web_area_kbn";

		/** 県北エリア（多賀城市・塩竈市・その他県北エリア）のコード */
		public static final int NORTH_AREA = 200001;

		/** 仙台系最小値 */
		public static final int SENDAI_MIN_VALUE = 200001;

		/** 仙台系最大値 */
        public static final int SENDAI_MAX_VALUE = 209999;

		/** 北陸系最小値 */
		public static final int HOKURIKU_MIN_VALUE = 210001;

		/** 北陸系最大値 */
		public static final int HOKURIKU_MAX_VALUE = 219999;

		/** 東北・北海道以外 */
		public static final int OTHER_HOKURIKU = 200008;

		/**
		 * WEBdデータで表示する際の非表示項目を取得する
		 * @return 非表示項目のリスト
		 */
		public static List<Integer> getWebdataNoDispList() {
			return new ArrayList<Integer>(0);
		}

		/**
		 * 運営側、会員機能で表示する際の非表示項目を取得する
		 * @return 非表示項目のリスト
		 */
		public static List<Integer> getMemberNoDispList() {
			List<Integer> noDispList = new ArrayList<Integer>(0);

			return noDispList;
		}

		/**
		 * 顧客側で表示する際の非表示項目を取得する
		 * @return 非表示項目のリスト
		 */
		public static List<Integer> getShopNoDispList() {
			List<Integer> noDispList = new ArrayList<Integer>(0);

			return noDispList;
		}

		public static List<Integer> getOtherAreaList() {
			return Collections.singletonList(OTHER_HOKURIKU);
		}
	}

	/**
	 * 首都圏勤務地エリア(WEBエリアから名称変更)区分の定数を保持するクラス
	 * @author Takehiro Nakamori
	 */
	public static final class ShutokenWebAreaKbn {
		/** 首都圏勤務地エリア区分 */
		public static final String TYPE_CD = "shutoken_web_area_kbn";

		/** 東京東部 */
		public static final int TOKYO_TOBU = 1;
		/** 東京西部 */
		public static final int TOKYO_SEIBU = 2;
		/** 東京南部 */
		public static final int TOKYO_NAMBU = 3;
		/** 東京北部 */
		public static final int TOKYO_HOKUBU = 4;
		/** 東京都下 */
		public static final int TOKYO_TOKA = 5;
		/** 神奈川県 */
		public static final int KANAGAWA = 6;
		/** 千葉県 */
		public static final int CHIBA = 7;
		/** 埼玉県 */
		public static final int SAITAMA = 8;
		/** IUターン・リゾート・その他 */
		public static final int IU_RESORT_OTHER = 9;

		/** こだわらない */
		public static final int NOT_DECIDE = 11;

		/**
		 * WEBdデータで表示する際の非表示項目を取得する
		 * @return 非表示項目のリスト
		 */
		public static List<Integer> getWebdataNoDispList() {
			List<Integer> noDispList = new ArrayList<Integer>();
			// こだわらない
			noDispList.add(NOT_DECIDE);

			return noDispList;
		}

		/**
		 * 運営側、会員機能で表示する際の非表示項目を取得する
		 * @return 非表示項目のリスト
		 */
		public static List<Integer> getMemberNoDispList() {
			List<Integer> noDispList = new ArrayList<Integer>();
			// マルチエリア(複数勤務地)
//			noDispList.add(MULTI_AREA);

			return noDispList;
		}

		/**
		 * 顧客側で表示する際の非表示項目を取得する
		 * @return 非表示項目のリスト
		 */
		public static List<Integer> getShopNoDispList() {
			List<Integer> noDispList = new ArrayList<Integer>();
			// マルチエリア(複数勤務地)
//			noDispList.add(MULTI_AREA);
			// こだわらない
			noDispList.add(NOT_DECIDE);

			return noDispList;
		}

		public static List<Integer> getOtherAreaList() {
			return Collections.emptyList();
		}
	}

	/**
	 * 雇用形態区分の定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class EmployPtnKbn {
		/** 雇用形態区分 */
		public static final String TYPE_CD = "employ_ptn_kbn";

		/** 正社員 */
		public static final int SEISYAIN = 1;
		/** 契約社員 */
		public static final int KEIYAKU_SYAIN = 2;
		/** アルバイト・パート */
		public static final int ARUBAITO_PART = 3;
		/** 派遣・紹介 */
		public static final int HAKEN = 4;
		/** その他 */
		public static final int OTHER = 6;

		/** モバイルで使用する短縮されたラべル */
		public static final Map<Integer, String> employPtnSmallLabelMap;
		static {
			Map<Integer, String> tmpMap = new HashMap<Integer, String>();
			tmpMap.put(ARUBAITO_PART, "ア");
			tmpMap.put(HAKEN, "派");
			tmpMap.put(OTHER, "他");
			tmpMap.put(KEIYAKU_SYAIN, "契");
			tmpMap.put(SEISYAIN, "正");

			employPtnSmallLabelMap = tmpMap;
		}

		/** モバイルプレビューで使用する短縮されたラべル */
		public static final Map<String, String> previewEmployPtnSmallLabelMap;
		static {
			Map<String, String> tmpMap = new HashMap<String, String>();
			tmpMap.put(Integer.toString(ARUBAITO_PART), "ア");
			tmpMap.put(Integer.toString(HAKEN), "派");
			tmpMap.put(Integer.toString(OTHER), "他");
			tmpMap.put(Integer.toString(KEIYAKU_SYAIN), "契");
			tmpMap.put(Integer.toString(SEISYAIN), "正");

			previewEmployPtnSmallLabelMap = tmpMap;
		}
	}

	/**
	 * 職種区分の定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class JobKbn {

		/** 職種区分 */
		public static final String TYPE_CD = "job_kbn";


		/** 料理長候補 */
		public static final int OWNER_CANDIDATE = 1;
		/** 調理スタッフ */
		public static final int COOKING_STAFF = 2;
		/** 寿司職人 */
		public static final int SUSHI = 3;
		/** パティシエ・製菓製造 */
		public static final int PATISSIER = 4;
		/** パン職人 */
		public static final int BOULANGER = 5;
		/** 鉄板調理スタッフ */
		public static final int PLATE = 6;
		/** ピッツァイオーロ */
		public static final int PIZZAIOLO = 7;
		/** 蕎麦職人 */
		public static final int SOBA_UDON = 8;
		/** 店長・マネージャー候補 */
		public static final int SHOP_MANAGER_CANDIDATE = 10;
		/** ホールサービス */
		public static final int HALL_SERVICE = 11;
		/** ソムリエ */
		public static final int SOMMELIER = 12;
		/** 女将・和装接客スタッフ */
		public static final int PROPRIETRESS = 13;
		/** 販売スタッフ */
		public static final int SALES_STAFF = 14;
		/** バリスタ */
		public static final int BARISTA = 15;
		/** バーテンダー */
		public static final int BARTENDER = 16;
		/** レセプショニスト */
		public static final int RECEPTIONIST = 17;
		/** 栄養士・管理栄養士 */
		public static final int DIETITIAN = 19;
		/** スクール講師 */
		public static final int SCHOOL_LECTURER = 20;
		/** 本部スタッフ・SV */
		public static final int OFFICE_STAFF = 21;
		/** 委託店長・経営者 */
		public static final int MANAGER = 22;
		/** 調理見習い・調理補助 */
		public static final int COOKING_ASSISTANCE = 23;
		/** 焼鳥調理 */
		public static final int GRILLED_CHIKEN = 24;

		/** その他 */
		public static final int OTHER = 99;
	}

	/**
	 * 待遇区分の定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class TreatmentKbn {
		/** 待遇区分 */
		public static final String TYPE_CD = "treatment_kbn";

		/** 各種社会保険完備 */
		public static final int SOCIAL_INSURANCE = 1;

		/** 完全週休2日制または月8日以上休み */
		public static final int TWO_DAYS_OFF = 2;

		/** 独立制度有 */
		public static final int INDEPENDENCE_SUPPORT = 3;

		/** 寮・社宅あり */
		public static final int HOUSING_SUBSIDIES = 4;

		/** 海外研修・海外旅行有り */
		public static final int OUTSIDE_TRAINING = 5;

		/** 産休・育児休暇取得実績あり */
		public static final int MATERNITY_LEAVE_CHILDCARE_LEAVE = 6;

		/** 時短社員制度あり */
		public static final int TIME_SAVINGS_EMPLOYEE_SUPPORT = 7;

		/** 賞与有り */
		public static final int BONUS = 8;

		/** 退職金有り */
		public static final int SEVERANCE_PAY = 9;

		/** 海外勤務有り */
		public static final int WORKING_ABROAD = 10;

		/** 待遇用非表示リスト */
		public static final List<Integer> TREATMENT_NO_DISPLAY_LIST;

		/** 休日休暇用非表示リスト */
		public static final List<Integer> HOLIDAY_NO_DISPLAY_LIST;

		static {
			List<Integer> treatmentNoDispList = new ArrayList<Integer>();
			treatmentNoDispList.add(TWO_DAYS_OFF);
			treatmentNoDispList.add(MATERNITY_LEAVE_CHILDCARE_LEAVE);
			TREATMENT_NO_DISPLAY_LIST = Collections.unmodifiableList(treatmentNoDispList);


			List<Integer> holidayNoDispList = new ArrayList<Integer>();
			holidayNoDispList.add(SOCIAL_INSURANCE);
			holidayNoDispList.add(OUTSIDE_TRAINING);
			holidayNoDispList.add(INDEPENDENCE_SUPPORT);
			holidayNoDispList.add(TIME_SAVINGS_EMPLOYEE_SUPPORT);
			holidayNoDispList.add(BONUS);
			holidayNoDispList.add(SEVERANCE_PAY);
			holidayNoDispList.add(WORKING_ABROAD);
			HOLIDAY_NO_DISPLAY_LIST =
					Collections.unmodifiableList(holidayNoDispList);


		}


		/** モバイルで使用する待遇の短縮されたラベルを保持するマップ */
		public static final Map<Integer, String> treatmentKbnSmallLabelMap;
		static {
			Map<Integer, String> tmpMap = new HashMap<Integer, String>();
//			tmpMap.put(CAR_BIKE, "車");
			tmpMap.put(HOUSING_SUBSIDIES, "住");
			tmpMap.put(INDEPENDENCE_SUPPORT, "独");
			tmpMap.put(OUTSIDE_TRAINING, "海");
			tmpMap.put(SOCIAL_INSURANCE, "保");
			tmpMap.put(TWO_DAYS_OFF, "週2");

			treatmentKbnSmallLabelMap = tmpMap;
		}

		/** モバイルプレビューで使用する待遇の短縮されたラベルを保持するマップ */
		public static final Map<String, String> previewTreatmentKbnSmallLabelMap;
		static {
			Map<String, String> tmpMap = new HashMap<String, String>();
//			tmpMap.put(Integer.toString(CAR_BIKE), "車");
			tmpMap.put(Integer.toString(HOUSING_SUBSIDIES), "住");
			tmpMap.put(Integer.toString(INDEPENDENCE_SUPPORT), "独");
			tmpMap.put(Integer.toString(OUTSIDE_TRAINING), "海");
			tmpMap.put(Integer.toString(SOCIAL_INSURANCE), "保");
			tmpMap.put(Integer.toString(TWO_DAYS_OFF), "週2");

			previewTreatmentKbnSmallLabelMap = tmpMap;
		}
	}

	/**
	 * その他条件(表)の定数を保持するクラス
	 * OtherBackConditionKbn(other_back_condition_kbn)と連動。
	 * 値は同じものにしてください。
	 * @author Makoto Otani
	 */
	public static final class OtherConditionKbn {
		/** その他条件 */
		public static final String TYPE_CD = "other_condition_kbn";

		/** オープニングスタッフ募集 */
		public static final int OPENING_STAFF = 1;
		/** 業界未経験者歓迎 */
		public static final int AMATEUR = 2;
		/** 新卒歓迎 */
		public static final int NEW_GRADUATES = 3;

		/** 求める人物像・資格で表示しないリスト */
		public static final List<Integer> PERSON_HUNTING_NO_DISPLAY_LIST;

		/** オープン日で表示しないリスト */
		public static final List<Integer> OPENING_DAY_NO_DISPLAY_LIST;

		static {
			List<Integer> personNoDispList = new ArrayList<Integer>();
			personNoDispList.add(OPENING_STAFF);
			PERSON_HUNTING_NO_DISPLAY_LIST = Collections.unmodifiableList(personNoDispList);


			List<Integer> openingDayNoDispList = new ArrayList<Integer>();
			openingDayNoDispList.add(AMATEUR);
			openingDayNoDispList.add(NEW_GRADUATES);
			OPENING_DAY_NO_DISPLAY_LIST =
					Collections.unmodifiableList(openingDayNoDispList);
		}


	}

	/**
	 * その他条件(裏)の定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class OtherBackConditionKbn {
		/** その他条件 */
		public static final String TYPE_CD = "other_back_condition_kbn";

		/** オープニングスタッフ募集 */
		public static final int OPENING_STAFF = 1;
		/** シロウト */
		public static final int AMATEUR = 2;
		/** 新卒者歓迎 */
		public static final int NEW_GRADUATES = 3;

		/** 求める人物像・資格で表示しないリスト */
		public static final List<Integer> PERSON_HUNTING_NO_DISPLAY_LIST;

		/** オープン日で表示しないリスト */
		public static final List<Integer> OPENING_DAY_NO_DISPLAY_LIST;

		static {
			List<Integer> personNoDispList = new ArrayList<Integer>();
			personNoDispList.add(OPENING_STAFF);
			PERSON_HUNTING_NO_DISPLAY_LIST = Collections.unmodifiableList(personNoDispList);


			List<Integer> openingDayNoDispList = new ArrayList<Integer>();
			openingDayNoDispList.add(AMATEUR);
			openingDayNoDispList.add(NEW_GRADUATES);
			OPENING_DAY_NO_DISPLAY_LIST =
					Collections.unmodifiableList(openingDayNoDispList);
		}
	}

	/**
	 * 適正診断区分の定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class ReasonableKbn {
		/** 適正診断区分 */
		public static final String TYPE_CD = "reasonable_kbn";
	}

	/**
	 * 店舗数区分の定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class ShopsKbn {
		/** 店舗数区分 */
		public static final String TYPE_CD = "shops_kbn";
	}

	/**
	 * 素材区分の定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class MaterialKbn {
		/** 素材区分 */
		public static final String TYPE_CD = "material_kbn";

		// 元画像と、サムネイル画像の末尾が同じになるように値を設定
		// 運営側にて区分値からpropertiesを見ている箇所があるため、
		// 変更時はメンテナンスが必要
		// MaterialExistsDtoのメンテナンスが必要
		/** 素材区分_メイン1(1) */
		public static final String MAIN_1 = "1";
		/** 素材区分_メイン2(2) */
		public static final String MAIN_2 = "2";
		/** 素材区分_メイン3(3) */
		public static final String MAIN_3 = "3";
		/** 素材区分_ロゴ(4) */
		public static final String LOGO = "4";
		/** 素材区分_右画像(5) */
		public static final String RIGHT = "5";
		/** 素材区分_キャプション1(6) */
		public static final String PHOTO_A = "6";
		/** 素材区分_キャプション2(7) */
		public static final String PHOTO_B = "7";
		/** 素材区分_キャプション3(8) */
		public static final String PHOTO_C = "8";
		/** 素材区分_フリースペース(9) */
		public static final String FREE = "9";
		/** 素材区分_ピックアップ求人ロゴ(10) */
		public static final String ATTENTION_SHOP = "10";
		/** 素材区分_ここに注目(11) */
		public static final String ATTENTION_HERE = "11";


		/** 素材区分_メイン1サムネイル(21) */
		public static final String MAIN_1_THUMB = "21";
		/** 素材区分_メイン2サムネイル(22) */
		public static final String MAIN_2_THUMB = "22";
		/** 素材区分_メイン3サムネイル(23) */
		public static final String MAIN_3_THUMB = "23";
		/** 素材区分_ロゴサムネイル(24) */
		public static final String LOGO_THUMB = "24";
		/** 素材区分_右画像サムネイル(25) */
		public static final String RIGHT_THUMB = "25";
		/** 素材区分_キャプション1サムネイル(26) */
		public static final String PHOTO_A_THUMB = "26";
		/** 素材区分_キャプション2サムネイル(27) */
		public static final String PHOTO_B_THUMB = "27";
		/** 素材区分_キャプション3サムネイル(28) */
		public static final String PHOTO_C_THUMB = "28";
		/** 素材区分_フリーサムネイル(29) */
		public static final String FREE_THUMB = "29";
		/** 素材区分_ピックアップ求人ロゴサムネイル(30) */
		public static final String ATTENTION_SHOP_THUMB = "30";
		/** 素材区分_ここに注目サムネイル(31) */
		public static final String ATTENTION_HERE_THUMB = "31";

		/** 素材区分_動画(WM)(41) */
		public static final String MOVIE_WM = "41";
		/** 素材区分_動画(QT)(42) */
		public static final String MOVIE_QT = "42";

		/**
		 * 素材の対応したサムネイル区分を取得します。
		 * @return 素材区分
		 */
		public static String getThumbValue(String value) {

			if (MAIN_1.equals(value)) {
				return MAIN_1_THUMB;
			} else if (MAIN_2.equals(value)) {
				return MAIN_2_THUMB;
			} else if (MAIN_3.equals(value)) {
				return MAIN_3_THUMB;
			} else if (LOGO.equals(value)) {
				return LOGO_THUMB;
			}  else if (RIGHT.equals(value)) {
				return RIGHT_THUMB;
			} else if (PHOTO_A.equals(value)) {
				return PHOTO_A_THUMB;
			} else if (PHOTO_B.equals(value)) {
				return PHOTO_B_THUMB;
			} else if (PHOTO_C.equals(value)) {
				return PHOTO_C_THUMB;
			} else if (FREE.equals(value)) {
				return FREE_THUMB;
			} else if (ATTENTION_SHOP.equals(value)) {
				return ATTENTION_SHOP_THUMB;
			} else if (ATTENTION_HERE.equals(value)) {
				return ATTENTION_HERE_THUMB;
			}
			return "";
		}

		/**
		 * 区分がサムネイルの区分か判定します。
		 * @param value 区分値
		 * @return サムネイルの区分の場合true、そうでない場合false
		 */
		public static boolean isThumbKbn(String value) {

			if (MAIN_1_THUMB.equals(value)) {
				return true;
			} else if (MAIN_2_THUMB.equals(value)) {
				return true;
			} else if (MAIN_3_THUMB.equals(value)) {
				return true;
			} else if (LOGO_THUMB.equals(value)) {
				return true;
			} else if (RIGHT_THUMB.equals(value)) {
				return true;
			} else if (PHOTO_A_THUMB.equals(value)) {
				return true;
			} else if (PHOTO_B_THUMB.equals(value)) {
				return true;
			} else if (PHOTO_C_THUMB.equals(value)) {
				return true;
			} else if (FREE_THUMB.equals(value)) {
				return true;
			} else if (ATTENTION_SHOP_THUMB.equals(value)) {
				return true;
			} else if (ATTENTION_HERE_THUMB.equals(value)) {
				return true;
			}
			return false;
		}

		/**
		 * 素材区分が動画かどうかを取得します。
		 * @param value
		 * @return true：動画、false:動画以外
		 */
		public static boolean isMovie(String value) {
			return (MOVIE_WM.equals(value) || MOVIE_QT.equals(value));
		}
	}

	/**
	 * 店舗一覧素材区分
	 * @author Takehiro Nakamori
	 *
	 */
	public static final class ShopListMaterialKbn {
		/** 素材区分 */
		public static final String TYPE_CD = "shop_list_material_kbn";
		/** 素材区分_メイン1(1) */
		public static final String MAIN_1 = "1";
		/** 素材区分_ロゴ */
		public static final String LOGO = "2";
		/** 素材区分_メイン1サムネイル(21) */
		public static final String MAIN_1_THUMB = "21";
		/** 素材区分_ロゴサムネイル(22) */
		public static final String LOGO_THUMB = "22";

		/**
		 * サムネイルの値を返します。
		 * @param value
		 * @return
		 */
		public static String getThumbValue(String value) {
			if (MAIN_1.equals(value)) {
				return MAIN_1_THUMB;
			}

			if (LOGO.equals(value)) {
				return LOGO_THUMB;
			}
			return "";
		}

		/**
		 * サムネイルかどうか判断します。
		 * @param value
		 * @return
		 */
		public static boolean isThumbKbn(String value) {
			if (MAIN_1_THUMB.equals(value)) {
				return true;
			}
			if (LOGO_THUMB.equals(value)) {
				return true;
			}
			return false;
		}


		/**
		 * オリジナル画像区分リストを取得します。
		 * @return
		 */
		public static List<String> getOriginalMaterialKbnList() {
			List<String> kbnList= new ArrayList<String>();
			kbnList.add(MAIN_1);
			kbnList.add(LOGO);
			return kbnList;
		}

	}

	/**
	 * ジャスキル会員素材区分
	 */
	public static final class JuskillMemberMaterialKbn {

		/** 素材区分_履歴書データ(1) */
		public static final String RESUME = "1";

		/**
		 * 素材区分が履歴書データかどうかを取得します。
		 *
		 * @param value
		 * @return
		 */
		public static boolean isResume(String value) {
			return (RESUME.equals(value));
		}

	}

	/**
	 * 店舗一覧の緯度経度区分
	 * @author Takehiro Nakamori
	 *
	 */
	public static final class ShopListLatLngKbn {
		/** タイプコード */
		public static final String TYPE_CD = "shop_list_lat_lng_kbn";
		/** 通常(住所からの登録) */
		public static final int ADDRESS = 0;
		/** 緯度経度からの登録 */
		public static final int LAT_LNG = 1;
	}

	/**
	 * 応募テストフラグの定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class ApplicationTestFlg {
		/** 応募テストフラグ */
		public static final String TYPE_CD = "application_test_flg";

		/** 応募テストフラグ_未応募(0) */
		public static final int NON = 0;
		/** 応募テストフラグ_応募済(1) */
		public static final int FIN = 1;


	}

	/**
	 * 送信者表示フラグの定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class SendDisplayFlg {
		/** 送信者表示フラグ */
		public static final String TYPE_CD = "send_display_flg";
	}

	/**
	 * 受信者表示フラグの定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class ReadingDisplayFlg {
		/** 受信者表示フラグ */
		public static final String TYPE_CD = "reading_display_flg";
	}

	/**
	 * 客席数区分の定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class SeatKbn {
		/** 客席数区分 */
		public static final String TYPE_CD = "seat_kbn";
	}

	/**
	 * 月売上区分の定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class MonthSalesKbn {
		/** 月売上区分 */
		public static final String TYPE_CD = "month_sales_kbn";
	}

	/**
	 * ジャスキル登録フラグの定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class JuskillFlg {
		/** ジャスキル登録フラグ */
		public static final String TYPE_CD = "juskill_flg";

		/** 希望しない */
		public static final int NOT_HOPE = 0;
		/** 希望する */
		public static final int HOPE = 1;
	}

	/**
	 * ジャスキル連絡（転職相談窓口）フラグの定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class JuskillContactFlg {
		/** ジャスキル登録フラグ */
		public static final String TYPE_CD = "juskill_contact_flg";

		/** 希望しない */
		public static final int NOT_HOPE = 0;
		/** 希望する */
		public static final int HOPE = 1;
	}

	/**
	 * 選考対象フラグの定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class SelectionFlg {
		/** 選考対象フラグ */
		public static final String TYPE_CD = "selection_flg";

		/** ブランク-- */
		public static final int BLANK = -1;

		/** 対象外(外す) */
		public static final int OUT_TARGET = 0;

		/** 選考対象 (採用検討) */
		public static final int SELECTION_TARGET = 1;

		/** 書類選考中 */
		public static final int DOCUMENT_SELECTING = 2;
//		CONSULTATION
		/** 面接日程調整中 */
		public static final int PLANNING_CONSULTATION_DAY = 3;

		/** 面接待ち */
		public static final int WAITING_FIRST_CONSULTATION = 4;

		/** 一次面接後　不採用 */
		public static final int REJECT_AFTER_FIRST_CONSULTATION = 5;

		/** 二次面接検討 */
		public static final int CONSIDERATION_FOR_SECOND_CONSULTATION = 6;

		/**　二次面接待ち */
		public static final int WAITING_SECOND_CONSULTATION = 7;

		/** 面接後選考中 */
		public static final int SELECTING_AFTER_CONSULTATION = 8;

		/** 面接後不採用予定 */
		public static final int REJECT_AFTER_CONSULTATION = 9;

		/** 不採用 */
		public static final int REJECT = 10;

		/** 採用 */
		public static final int EMPLOY = 11;

		/** 辞退 */
		public static final int DECLINE = 12;

		/** 選考フラグデフォルトカラー */
		public static final String SELECTION_FLG_DEFAULT_COLOR = "#FFFFFF";

		/** 選考色マップ */
		public static final Map<Integer, String> SELECTION_COLOR_MAP;


		public static final Map<String, String> SELECTION_COLOR_STRING_MAP;
		static {

			Map<Integer, String> map = new LinkedHashMap<Integer, String>(10);
			map.put(BLANK, "#FFFFFF");
			map.put(DOCUMENT_SELECTING, "#C3E58C");
			map.put(PLANNING_CONSULTATION_DAY, "#C3E58C");
			map.put(WAITING_FIRST_CONSULTATION, "#BCAEF4");
			map.put(REJECT_AFTER_FIRST_CONSULTATION, "#CCCCCC");
			map.put(CONSIDERATION_FOR_SECOND_CONSULTATION, "#F79494");
			map.put(WAITING_SECOND_CONSULTATION, "#F9A870");
			map.put(SELECTING_AFTER_CONSULTATION, "#E5C59E");
			map.put(REJECT_AFTER_CONSULTATION, "#C6C297");
			map.put(REJECT, "#CCCCCC");
			map.put(SELECTION_TARGET, "#A2D6EA");
			map.put(EMPLOY, "#91B1F9");
			map.put(DECLINE, "#CCCCCC");
			map.put(OUT_TARGET, "#CCCCCC");
			SELECTION_COLOR_MAP = Collections.unmodifiableMap(map);

			Map<String, String> stringMap = new LinkedHashMap<String, String>(SELECTION_COLOR_MAP.size());
			for (Iterator<Entry<Integer, String>> it = SELECTION_COLOR_MAP.entrySet().iterator(); it.hasNext();) {
				Entry<Integer, String> entry = it.next();
				stringMap.put(String.valueOf(entry.getKey()), entry.getValue());
			}

			SELECTION_COLOR_STRING_MAP = Collections.unmodifiableMap(stringMap);
		}

	}

	/**
	 * 仮登録区分の定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class TemporaryRegistrationKbn {
		/** 仮登録区分 */
		public static final String TYPE_CD = "temporary_registration_kbn";

		/** 仮会員登録(1) */
		public static final int TEMP_SIGNIN = 1;
		/** パスワード再登録(2) */
		public static final int PASSWORD_REISSUE = 2;
		/** ログインID変更(3) */
		public static final int CHANGE_LOGIN_MAIL = 3;
		/** 顧客用パスワード再登録(4) */
		public static final int CUSTOMER_PASSWORD_REISSUE = 4;
		/** 事前登録会員登録 */
		public static final int ADVANCED_REGISTRATION = 5;
	}

	/**
	 * 連絡メール区分の定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class CommunicationMailKbn {
		/** 連絡メール区分 */
		public static final String TYPE_CD = "communication_mail_kbn";

		/** 連絡メール区分_入力メール(1) */
		public static final int INPUT_MAIL = 1;
		/** 連絡メール区分_顧客メール(2) */
		public static final int CUSTOMER_MAIL = 2;
	}

	/**
	 * アクセスフラグの定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class AccessFlg {
		/** アクセスフラグ */
		public static final String TYPE_CD = "access_flg";

		/** アクセスフラグ_未アクセス(0) */
		public static final int NEVER = 0;
		/** アクセスフラグ_アクセス済(1) */
		public static final int ALREADY = 1;
	}

	/**
	 * 会員フラグの定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class MemberFlg {
		/** 会員フラグ */
		public static final String TYPE_CD = "member_flg";

		/** 会員フラグ_非会員(0) */
		public static final int NOT_MEMBER = 0;

		/** 会員フラグ_会員(1) */
		public static final int MEMBER = 1;
	}

	/**
	 * 複数勤務地店舗フラグの定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class MultiWorkingPlaceFlg {
		/** 複数勤務地店舗フラグ */
		public static final String TYPE_CD = "multi_working_place_flg";

		/** 複数勤務地店舗フラグ_非対象(0) */
		public static final int NOT_TARGET = 0;
		/** 複数勤務地店舗フラグ_対象(1) */
		public static final int TARGET = 1;
	}

	/**
	 * 注目店舗フラグの定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class AttentionShopFlg {
		/** 注目店舗フラグ */
		public static final String TYPE_CD = "attention_shop_flg";

		/** 注目店舗フラグ_非対象(0) */
		public static final int NOT_TARGET = 0;
		/** 注目店舗フラグ_対象(1) */
		public static final int TARGET = 1;

	}

	/**
	 * 新着情報受信フラグの定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class JobInfoReceptionFlg {
		/** 新着情報受信フラグ */
		public static final String TYPE_CD = "job_info_reception_flg";

		/** 新着情報受信フラグ_受け取らない(0) */
		public static final int NOT_RECEPTION = 0;
		/** 新着情報受信フラグ_受け取る(1) */
		public static final int RECEPTION = 1;
	}

	/**
	 * メルマガ受信フラグの定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class MailMagazineReceptionFlg {
		/** メルマガ受信フラグ */
		public static final String TYPE_CD = "mail_magazine_reception_flg";

		/** メルマガ受信フラグ_受け取らない(0) */
		public static final int NOT_RECEPTION = 0;
		/** メルマガ受信フラグ_受け取る(1) */
		public static final int RECEPTION = 1;
	}

	/**
	 * スカウトメール受信フラグの定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class ScoutMailReceptionFlg {

		/** スカウトメール受信フラグ */
		public static final String TYPE_CD = "scout_mail_reception_flg";

		/** スカウトメール受信フラグ_初期値(受け取らない) */
		public static final int NOT_RECEPTION = 0;
		/** スカウトメール受信フラグ_初期値(受け取る) */
		public static final int RECEPTION = 1;

	}

	/**
	 * PCメール配信停止フラグの定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class PcMailStopFlg {

		/** メール配信停止フラグ */
		public static final String TYPE_CD = "pc_mail_stop_flg";

		/** メール配信停止フラグ_配信(0) */
		public static final int DELIVERY = 0;
		/** メール配信停止フラグ_停止(1) */
		public static final int STOP = 1;
	}

	/**
	 * ステータス区分の定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class StatusKbn {

		/** ステータス区分 */
		public static final String TYPE_CD = "status_kbn";

		/** ステータス区分_データ用ステータス(1) */
		public static final int DATA_STATUS_VALUE= 1;
		/** ステータス区分_表示用ステータス(2) */
		public static final int DIPLAY_STATUS_VALUE = 2;

	}

	/**
	 * ステータス変更区分の定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class ChangeStatusKbn {

		/** ステータス変更区分 */
		public static final String TYPE_CD = "change_status_kbn";

		/** ステータス変更区分_掲載確定(1) */
		public static final int FIXED_VALUE= 1;
		/** ステータス変更区分_確定取消(2) */
		public static final int CANCEL_VALUE= 2;
		/** ステータス変更区分_掲載依頼(3) */
		public static final int POST_REQUEST_VALUE= 3;
		/** ステータス変更区分_掲載終了(4) */
		public static final int POST_END_VALUE= 4;
		/** ステータス変更区分_WEB非表示(5) */
		public static final int WEB_HIDDEN_VALUE= 5;

	}

	/**
	 * 誌面フラグの定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class MagazineFlg {

		/** 誌面フラグ */
		public static final String TYPE_CD = "magazine_flg";

		/** 誌面フラグ_非対象(0) */
		public static final int NOT_TARGET = 0;
		/** 誌面フラグ_対象(1) */
		public static final int TARGET = 1;
	}

	/**
	 * 対象外フラグの定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class NotTargetFlg {

		/** 対象外フラグ */
		public static final String TYPE_CD = "not_target_flg";

		/** 対象外フラグ_対象(0) */
		public static final int TARGET = 0;
		/** 対象外フラグ_対象外(1) */
		public static final int NOT_TARGET = 1;
	}

	/**
	 * ユーザ区分の定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class UserKbn {

		/** ユーザ区分 */
		public static final String TYPE_CD = "user_kbn";

		/** ユーザ区分_顧客(1) */
		public static final int CUSTOMER = 1;
		/** ユーザ区分_会員(2) */
		public static final int MEMBER = 2;
		/** ユーザ区分_事前登録会員(3) */
		public static final int ADVANCED_REGISTRATION = 3;
		/** ユーザ区分_ジャスキル(4) */
		public static final int JUSKILL = 4;
	}

	/**
	 * エラーフラグの定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class ErrorFlg {

		/** エラーフラグ */
		public static final String TYPE_CD = "error_flg";

		/** エラーフラグ_エラー無(0) */
		public static final int NON = 0;
		/** エラーフラグ_エラー有(1) */
		public static final int EXIST = 1;
	}

	/**
	 * モバイルメール配信停止フラグの定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class MobileMailStopFlg {

		/** メール配信停止フラグ */
		public static final String TYPE_CD = "mobile_mail_stop_flg";

		/** メール配信停止フラグ_配信(0) */
		public static final int DELIVERY = 0;
		/** メール配信停止フラグ_停止(1) */
		public static final int STOP = 1;
	}

	/**
	 * 閲覧フラグの定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class ReadFlg {

		/** 閲覧フラグ */
		public static final String TYPE_CD = "read_flg";

		/** 閲覧フラグ_未閲覧(0) */
		public static final int NEVER = 0;
		/** 閲覧フラグ_閲覧済(1) */
		public static final int ALREADY = 1;
	}

	/**
	 * メルマガ区分の定数を保持するクラス
	 * @author Takahiro Ando
	 */
	public static final class MailmagazineKbn {

		/** メルマガ区分 */
		public static final String TYPE_CD = "mailmagazine_kbn";

		/** メルマガ区分_顧客・会員(1) */
		public static final int CUSTOMER_MEMEBER = 1;
		/** メルマガ区分_新着情報(2) */
		public static final int NEW_INFORMATION = 2;
	}

	/**
	 * メルマガオプション区分を保持するクラス
	 * @author Takehiro Nakamori
	 *
	 */
	public static final class MailmagazineOptionKbn {
		/** メルマガオプション区分 */
		public static final String TYPE_CD = "mailmagazine_option_kbn";

		/** ヘッダメッセージ */
		public static final int HEADER_MESSAGE = 1;
	}

	/**
	 * いたずらフラグの定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class MischiefFlg {

		/** いたずらフラグ */
		public static final String TYPE_CD = "mischief_flg";

		/** いたずらフラグ_通常(0) */
		public static final int NORMAL = 0;
		/** いたずらフラグ(1) */
		public static final int MISCHIEF = 1;
	}

	/**
	 * 店舗見学区分の定数を保持するクラス
	 * @author Takehiro Nakamori
	 *
	 */
	public static final class ObservationKbn {
		/** 店舗見学区分 */
		public static final String TYPE_CD = "observation_kbn";

		/** 質問のみ */
		public static final int ADMIN_QUESTION = 0;
		/** 質問・店舗見学 */
		public static final int ADMIN_QUESTION_OBSERVATION = 1;
		/** 無 */
		public static final int ADMIN_NONE = 2;
		/** ご質問 */
		public static final int FRONT_QUESTION = 3;
		/** 店舗見学 */
		public static final int FRONT_OBSERVATION = 4;

		/**
		 * 運営管理での非表示リストの取得
		 * @return
		 */
		public static List<Integer> getAdminNoDispList() {
			List<Integer> list = new ArrayList<Integer>();
			list.add(FRONT_QUESTION);
			list.add(FRONT_OBSERVATION);
			return list;
		}

		/**
		 * 公開側での非表示リストの取得
		 * @return
		 */
		public static List<Integer> getFrontNoDispList() {
			List<Integer> list = new ArrayList<Integer>();
			list.add(ADMIN_QUESTION);
			list.add(ADMIN_QUESTION_OBSERVATION);
			list.add(ADMIN_NONE);
			return list;
		}

	}

	/**
	 * 海外エリア区分の定数を保持するクラス
	 * @author Takehiro Nakamori
	 *
	 */
	public static class ForeignAreaKbn {
		/** エリアコードを指定して海外エリアの区分コードを取得 */
		public static String getTypeCd(String areaCd) {
			return getTypeCd(NumberUtils.toInt(areaCd, MAreaConstants.AreaCd.SHUTOKEN_AREA));
		}

		/** エリアコードを指定して海外エリアの区分コードを取得 */
		public static String getTypeCd(int areaCd) {
			if (areaCd == MAreaConstants.AreaCd.SHUTOKEN_AREA) {
				return ShutokenForeignAreaKbn.TYPE_CD;
			}
			// デフォルト
			return ShutokenForeignAreaKbn.TYPE_CD;
		}

		/**
		 * 勤務地エリア(WEBエリアから名称変更)とわけるため、エリアコードから海外エリアかどうかを判断する。
		 * @param kbnValue
		 * @param areaCd
		 * @return
		 */
		public static boolean isForeignArea(String kbnValue, String areaCd) {
			return isForeignArea(NumberUtils.toInt(kbnValue),
						NumberUtils.toInt(areaCd, MAreaConstants.AreaCd.SHUTOKEN_AREA));
		}

		/**
		 * 勤務地エリア(WEBエリアから名称変更)とわけるため、エリアコードから海外エリアかどうかを判断する。
		 * @param kbnValue
		 * @param areaCd
		 * @return
		 */
		public static boolean isForeignArea(int kbnValue, int areaCd) {
			if (areaCd == MAreaConstants.AreaCd.SHUTOKEN_AREA) {
				return ShutokenForeignAreaKbn.isForeignArea(kbnValue);
			}

			// デフォルト
			return ShutokenForeignAreaKbn.isForeignArea(kbnValue);
		}
	}

	/**
	 * 首都圏海外エリア区分の値を保持するクラス
	 *
	 * 勤務地エリア(WEBエリアから名称変更)のセレクトボックスと混合して扱うため、値を大きくしています。
	 * @author Takehiro Nakamori
	 *
	 */
	public static class ShutokenForeignAreaKbn {
		/** 首都圏海外エリア */
		public static final String TYPE_CD = "shutoken_foreign_area_kbn";
		/** 北米（アメリカ・カナダ その他北米） */
		public static final int NORTH_AMERICA = 100001;
		/** ヨーロッパ（ロシア・ドイツ・イギリス・フランス その他ヨーロッパ） */
		public static final int EUROPE = 100002;
		/** アジア・中東（中国・香港・台湾・タイ その他アジア・中東） */
		public static final int ASIA = 100003;
		/** オセアニア（オーストラリア その他オセアニア） */
		public static final int OCEANIA = 100004;
		/** 中南米（メキシコ・ブラジル その他中南米） */
		public static final int MIDDLE_SOUTH_AMERICA = 100005;
		/** アフリカ（モロッコ・エチオピア・南アフリカ　他） */
		public static final int AFRICA = 100006;

		/** 最小値 */
		public static final int MIN_VALUE = 100001;
		/** 最大値 */
		public static final int MAX_VALUE = 199999;

		/**
		 * 勤務地エリア(WEBエリアから名称変更)とわけるため、海外エリアかどうかを判断する。
		 * @param value
		 * @return
		 */
		public static final boolean isForeignArea(int value) {
			return value >= MIN_VALUE && MAX_VALUE >= value;
		}
	}

	/**
	 * 求人募集フラグの値を保持するクラス
	 * @author Takehiro Nakamori
	 *
	 */
	public static class JobOfferFlg {
		/** タイプコード */
		public static final String TYPE_CD = "job_offer_flg";
		/** なし */
		public static final int NASHI = 0;
		/** あり */
		public static final int ARI = 1;
	}

	/**
	 * 店舗一覧のステータスの値を保持するクラス
	 * @author Takehiro Nakamori
	 *
	 */
	public static class ShopListStatus {
		/** タイプコード */
		public static final String TYPE_CD = "shop_list_status";
		/** 一時保存 */
		public static final int TEMP_SAVE = 0;
		/** 登録済 */
		public static final int REGISTERED = 1;
	}

	/**
	 * 店舗一覧表示区分（WEBデータ用）
	 * @author Takehiro Nakamori
	 *
	 */
	public static class ShopListDisplayKbn {
		/** タイプコード */
		public static final String TYPE_CD = "shop_list_display_kbn";
		/** 無 */
		public static final int NASHI = 1;
		/** 有 */
		public static final int ARI = 2;
	}

	/**
	 * 連載区分
	 * @author Takehiro Nakamori
	 *
	 */
	public static class SERIAL_PUBLICATION_KBN {
		/** タイプコード */
		public static final String TYPE_CD = "serial_publication_kbn";
		/** 2連載 */
		public static final int DOUBLE = 1;
		/** 2連載OK */
		public static final int DOUBLE_OK = 2;
		/** 3連載 */
		public static final int TRIPLE = 3;
		/** 3連載OK */
		public static final int TRIPLE_OK = 4;
		/**定額制*/
		public static final int FLAT_RATE_SYSTEM = 5;
	}

	/**
	 * 店舗一覧表示フラグ（店舗一覧用）
	 * @author Takehiro Nakamori
	 *
	 */
	public static class ShopListDisplayFlg {
		/** タイプコード */
		public static final String TYPE_CD = "shop_list_display_flg";
		/** 表示 */
		public static final int DISPLAY = 1;
		/** 表示しない */
		public static final int NOT_DISPLAY = 0;
	}


	/**
	 * スカウトメール区分
	 */
	public static class ScoutMailKbn {
		/** タイプコード */
		public static final String TYPE_CD = "scout_mail_kbn";
		/** 原稿確定 */
		public static final int FIX_WEB = 1;
		/** 購入 */
		public static final int BOUGHT = 2;
		/** 制限なし */
		public static final int UNLIMITED = 3;
	}

	/**
	 * スカウトメールログ区分
	 * @author Takehiro Nakamori
	 *
	 */
	public static class ScoutMailLogKbn {
		/** タイプコード */
		public static final String TYPE_CD = "scout_mail_log_kbn";
		/** 追加 */
		public static final int ADD = 1;
		/** 追加(手動) */
		public static final int ADD_MANUAL = 2;
		/** 使用 */
		public static final int USE = 3;
	}

	/**
	 * 顧客テスト区分
	 */
	public static class CustomerTestFlg {
		/** タイプコード */
		public static final String TYPE_CD = "customer_test_flg";
		/** 通常顧客 */
		public static final int NORMAL = 0;
		/** テスト顧客 */
		public static final int TEST = 1;
	}

	/**
	 * 職務経歴表示フラグ
	 */
	public static class JobHistoryDisplayFlg {
		/** タイプコード */
		public static final String TYPE_CD = "job_history_display_flg";

		/** 公開 */
		public static final int DISPLAY = 1;

		/** 非公開 */
		public static final int NO_DISPLAY = 0;

	}

	/**
	 * 顧客のメルマガ受信フラグ
	 * @author Takehiro Nakamori
	 *
	 */
	public static class CustomerMailMagazineReceptionFlg {
		/** タイプコード */
		public static final String TYPE_CD = "customer_mail_magazine_reception_flg";

		/** 受信する */
		public static final int RECEIVE = 1;

		/** 受信しない */
		public static final int NOT_RECEIVE = 0;
	}

	/**
	 * 事前登録から会員に鳴るかどうかの区分
	 * @author Takehiro Nakamori
	 *
	 */
	public static class AdvancedRegistrationBecomeMemberKbn {
		/** タイプコード */
		public static final String TYPE_CD = "advanced_registration_become_member_kbn";
		/** 会員にならない */
		public static final int NOT_BECOME = 1;
		/** 会員になる */
		public static final int BECOME = 2;

	}

	/**
	 * 事前登録会員の登録状況
	 * @author Takehiro Nakamori
	 *
	 */
	public static class AdvancedRegistrationStatusKbn {
		/** タイプコード */
		public static final String TYPE_CD = "advanced_registration_status_kbn";
		/** 会員／登録無 */
		public static final int NO_ADVANCED_MEMBER = 1;
		/** 会員／登録有 */
		public static final int ADVANCED_MEMBER = 2;
		/** 非会員 */
		public static final int NO_MEMBER = 3;
	}

	/**
	 * 事前登録メルマガ受信フラグ
	 * @author Takehiro Nakamori
	 *
	 */
	public static class AdvancedMailMagReceptionFlg {
		/** タイプコード */
		public static final String TYPE_CD = "advanced_mail_magazine_reception_flg";
		/** メルマガ受信フラグ_受け取らない(0) */
		public static final int NOT_RECEPTION = 0;
		/** メルマガ受信フラグ_受け取る(1) */
		public static final int RECEPTION = 1;
	}

	/**
	 * 事前登録区分
	 * @author Takehiro Nakamori
	 *
	 */
	public static class BriefingPresentKbn {
		/** タイプコード */
		public static final String TYPE_CD = "briefing_present_kbn";
		/** 参加する */
		public static final int JOIN = 1;
		/** 参加しない */
		public static final int NOT_JOIN = 2;
	}


	/**
	 * バイト用職種タイプ
	 * @author Takehiro Nakamori
	 *
	 */
	public static class ArbeitJobType {
		/** タイプコード */
		public static final String TYPE_CD = "arbeit_job_type";
	}


	/**
	 * バイト用稼げる指数
	 * @author Takehiro Nakamori
	 *
	 */
	public static class ArbeitMoneyLevel {
		/** タイプコード */
		public static final String TYPE_CD = "arbeit_money_level";
	}

	/**
	 * バイト用シフト(曜日)
	 * @author Takehiro Nakamori
	 *
	 */
	public static class ArbeitShiftWeek {
		/** タイプコード */
		public static final String TYPE_CD = "arbeit_shift_week";
	}


	/**
	 * バイト用シフト時間
	 * @author Takehiro Nakamori
	 *
	 */
	public static class ArbeitShiftTime {
		/** タイプコード */
		public static final String TYPE_CD = "arbeit_shift_time";
	}

	/**
	 * バイト用特徴区分
	 * @author Takehiro Nakamori
	 *
	 */
	public static class ArbeitFeatureKbn {
		/** タイプコード */
		public static final String TYPE_CD = "arbeit_feature_kbn";

		/** 最大登録数 */
		public static final int MAX_REGISTER_NUM = 6;

		/** 3ヶ月勤務ＯＫ */
		public static final int WORKING_PERIOD_3_MONTHS = 16;

		/** ランチのみＯＫ */
		public static final int LUNCH_ONLY = 17;

		/** 土日のみＯＫ */
		public static final int ONLY_ON_WEEKENDS = 18;

		/** 週1からOK */
		public static final int WEEK_1 = 20;

		/** 社保完備のお店 */
		public static final int COMPANY_INSURANCE = 21;

	}

	/**
	 * バイト側お店を一言で区分
	 * @author Takehiro Nakamori
	 *
	 */
	public static class ArbeitShopSingleWordKbn {
		/** タイプコード */
		public static final String TYPE_CD = "arbeit_shop_single_word_kbn";
	}



	/**
	 * 現在の職種
	 * @author Takehiro Nakamori
	 *
	 */
	public static class CurrentJob {
		/** タイプコード */
		public static final String TYPE_CD = "current_job";

		/** 高校生 */
		public static final int HIGH_SCHOOL_STUDENT = 1;
		/** 大学生 */
		public static final int COLLEGE_STUDENT = 2;
		/** 大学院生 */
		public static final int POSTGRADUATE_STUDENT = 3;
		/** 短大生 */
		public static final int JUNIOR_COLLEGE_STUDENT = 4;
		/** 専門学校生 */
		public static final int SPECIAL_SCHOOL_STUDENT = 5;
		/** フリーター */
		public static final int FREETER = 6;
		/** 派遣社員 */
		public static final int TEMPORARY_EMPLOYEE = 7;
		/** 会社員 */
		public static final int COMPANY_EMPLOYEE = 8;
		/** 自営業 */
		public static final int INDEPENDENT_BUSINESS = 9;
		/** 主婦・主夫 */
		public static final int HOUSE_MAN = 10;
		/** 無職 */
		public static final int JOBLESS = 11;
		/** その他 */
		public static final int OTHER = 12;
	}

	/**
	 * 「在職中」「離職中」の定数定義
	 * @author Yamane
	 *
	 */
	public static class CurrentEmployedSituationKbn {

		/** タイプコード */
		public static final String TYPE_CD = "current_employed_situation_kbn";

		/** 在籍中 */
		public static final int EMPLOYED = 1;

		/** 離職中 */
		public static final int NOT_EMPLOYED = 0;
	}


	/**
	 * 有り/無しの定数定義
	 * @author Takehiro Nakamori
	 *
	 */
	public static class AriNashiKbn {
		/** タイプコード */
		public static final String TYPE_CD = "ari_nashi_kbn";
		/** あり */
		public static final int ARI = 1;
		/** なし */
		public static final int NASHI = 0;
	}

	/**
	 * 現在の状況の定数定義
	 * @author Makoto Otani
	 *
	 */
	public static class CurrentSituationKbn {
		/** タイプコード */
		public static final String TYPE_CD = "current_situation_kbn";

		/** 中途転職者 */
		public static final int JOB_CHANGER = 1;

		/** 新卒 */
		public static final int NEW_GRADUATER = 2;
	}


	/**
	 * WEBデータのチェック済みステータスの定数定義(t_web.checked_status)
	 * @author nakamori
	 *
	 */
	public static class WebdataCheckedStatus {
		/** タイプコード */
		public static final String TYPE_CD = "webdata_checked_status";

		/** 未チェック(デフォルト) */
		public static final int UNCHECKED = 0;

		/** チェック済み */
		public static final int CHECKED = 1;

	}


	/**
	 * 仮会員が会員登録済みかどうかを表すフラグ。
	 * XXX 後から追加したため、実ソースではこのクラスではなく {@link DbFlgValue} が使われている。
	 * @author tnaka
	 *
	 */
	public static class MemberRegisteredFlg {
		/** タイプコード */
		public static final String TYPE_CD = "member_registered_flg";

		/** 未登録 */
		public static final int NOT_REGISTERED = DbFlgValue.FALSE;

		/** 登録済み */
		public static final int REGISTERED = DbFlgValue.TRUE;
	}

	/**
	 * 事前登録の来場ステータス
	 */
	public static final class AdvancedRegistrationAttendedStatus {
		/** タイプコード */
		public static final String TYPE_CD = "advanced_registration_attended_status";

		/** 未来場 */
		public static final int NOT_ATTENDED = 0;
		/** 来場済み */
		public static final int ATTENDED = 1;
	}

	/**
	 * 給与体系区分
	 */
	public static final class SaleryStructureKbn {

		public static final String TYPE_CD = "salery_structure_kbn";

		/** 時給 */
		public static final int HOURLY = 1;
		/** 日給 */
		public static final int DAILY = 2;
		/** 月給 */
		public static final int MONTHLY = 3;
		/** 年収 */
		public static final int YEARLY = 4;
		/** 年俸 */
		public static final int ANNUAL = 5;
	}

	/**
	 * 新給与体系区分
	 */
	public static final class NewSaleryStructureKbn {

		public static final String TYPE_CD = "new_salery_structure_kbn";

		/** 時給 */
		public static final int HOURLY = 1;
		/** 日給 */
		public static final int DAILY = 2;
		/** 月給 */
		public static final int MONTHLY = 3;
		/** 年俸 */
		public static final int ANNUAL = 5;
	}

	/**
	 * 客単価区分
	 */
	public static final class SalesPerCustomerKbn {

		public static final String TYPE_CD = "sales_per_customer_kbn";

		/** 1000円未満 */
		public static final int less_than_1000 = 1;
		/** 1000～3000円 */
		public static final int TO_3000 = 2;
		/** 3000～5000円 */
		public static final int TO_5000 = 3;
		/** 5000～8000円 */
		public static final int TO_8000 = 4;
		/** 8000～10000円 */
		public static final int TO_10000 = 4;
		/** 10000円以上 */
		public static final int OR_MORE_10000 = 4;
	}

	/**
	 * ジャスキル移行会員フラグ
	 */
	public static final class JuskillMigrationFlg {

		public static final String TYPE_CD = "juskill_migration_flg";

		/** 新規会員 */
		public static final int OFF = 0;
		/** 移行会員 */
		public static final int ON = 1;
	}

	/**
	 * 求人データ区分
	 */
	public static final class OfferDataKbn {

		public static final String TYPE_CD = "offer_data_kbn";

		/** WEBデータ */
		public static final int WEB_DATA = 1;
		/** 店舗データ */
		public static final int SHOP_LIST = 2;
	}

	/**
	 * 応募区分の定数を保持するクラス
	 * @author t_shiroumaru
	 *
	 */
	public static final class ApplicationKbn{
		/** 応募区分 */
		public static final String TYPE_CD ="application_kbn";

		/** 応募区分_通常 */
		public static final int NORMAL = 1;

		/** 応募区分_気になる */
		public static final int ATTENTION = 2;
	}

	/**
	 * 求人一覧動画表示フラグ
	 */
	public static final class MovieListDisplayFlg {

		public static final String TYPE_CD = "movie_list_display_flg";

		/** 表示 */
		public static final int DISPLAY = 1;
		/** 表示しない */
		public static final int NOT_DISPLAY = 0;
	}

	/**
	 * 求人詳細動画表示フラグ
	 */
	public static final class MovieDetailDisplayFlg {

		public static final String TYPE_CD = "movie_detail_display_flg";

		/** 表示 */
		public static final int DISPLAY = 1;
		/** 表示しない */
		public static final int NOT_DISPLAY = 0;
	}

	/**
	 * リニューアルフラグ
	 */
	public static final class RenewalFlg {

		public static final String TYPE_CD = "renewal_flg";

		/** リニューアルフラグ_非対象(0) */
		public static final int NOT_TARGET = 0;
		/** リニューアルフラグ_対象(1) */
		public static final int TARGET = 1;

	}

	/**
	 * リニューアル2フラグ
	 */
	public static final class Renewal2Flg {
		
		public static final String TYPE_CD = "renewal2_flg";

		/** リニューアル2フラグ_非対象(0) */
		public static final int NOT_TARGET = 0;
		/** リニューアル2フラグ_対象(1) */
		public static final int TARGET = 1;
	}

	/**
	 * 国内外区分
	 */
	public static final class DomesticKbn {

		public static final String TYPE_CD = "domestic_kbn";

		/** 国内外区分_国内 */
		public static final int DOMESTIC = 1;
		/** 国内外区分_海外 */
		public static final int overseas = 2;
	}


	/**
	 * 移動手段区分
	 */
	public static final class TransportationKbn {

		public static final String TYPE_CD = "transportation_kbn";

		/** 徒歩 */
		public static final int WALK = 1;
		/** バス */
		public static final int BUS = 2;
		/** 車 */
		public static final int CAR = 3;
	}

	/**
	 * 経験役職区分の定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class ExpManagerKbn {
		/** 経験役職区分 */
		public static final String TYPE_CD = "exp_manager_kbn";

		/** バイトリーダー */
		public static final int BEIT_LEADER = 1;
		/** ホール主任 */
		public static final int HOLE_LEADER = 2;
		/** ホールマネージャー */
		public static final int HOLE_MANAGER = 3;
		/** 店長 */
		public static final int STORE_MANAGER = 4;
		/** 店長補佐・店長代理 */
		public static final int ASSISTANT_AGENT_MANAGER = 5;
		/** マネージャー */
		public static final int MANAGER = 6;
		/** キッチン主任 */
		public static final int KITCHEN_LEADER = 7;
		/** スーシェフ */
		public static final int SUE_CHEF = 8;
		/** 料理長 */
		public static final int COOKING_LEADER = 9;
		/** シェフ */
		public static final int CHEF = 10;
		/** スーパーバイザー */
		public static final int SUPERVISOR = 11;
		/** エリアマネージャー */
		public static final int AREA_MANAGER = 12;
		/** 事業部長 */
		public static final int DIVISION_MANAGER = 13;
		/** 経営幹部 */
		public static final int EXECUTIVES = 14;
		/** オーナー・自営業 */
		public static final int OWNER_SELF_EMPLOYED = 15;
		/** その他管理職 */
		public static final int OTHER = 99;

	}

	/**
	 * 経験役職年数区分の定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class ExpManagerYearKbn {
		/** 経験役職年数区分 */
		public static final String TYPE_CD = "exp_manager_year_kbn";

		/** 1年未満 */
		public static final int LESS_THAN_ONE_YEAH = 1;
		/** 1年以上 */
		public static final int OVER_ONE_YEAH = 2;
		/** 2年以上 */
		public static final int OVER_TWO_YEAH = 3;
		/** 3年以上 */
		public static final int OVER_THREE_YEAH = 4;
		/** 4年以上 */
		public static final int OVER_FOUR_YEAH = 5;
		/** 5年以上 */
		public static final int OVER_FIVE_YEAH = 6;
		/** 6年以上 */
		public static final int OVER_SIX_YEAH = 7;
		/** 7年以上 */
		public static final int OVER_SEVEN_YEAH = 8;
		/** 8年以上 */
		public static final int OVER_EIGHT_YEAH = 9;
		/** 9年以上 */
		public static final int OVER_NINE_YEAH = 10;
		/** 10年以上 */
		public static final int OVER_TEN_YEAH = 11;
	}

	/**
	 * 経験管理人数区分の定数を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class ExpManagerPersonsKbn {
		/** 経験管理人数区分 */
		public static final String TYPE_CD = "exp_manager_persons_kbn";

		/** 5名以下 */
		public static final int FIVE_OR_LESS = 1;
		/** 5～10名 */
		public static final int FIVE_TO_TEN_PEOPLE = 2;
		/** 11～20名 */
		public static final int ELEVEN_TO_TWENTY_PEOPLE = 3;
		/** 21~30名 */
		public static final int TWENTY_ONE_TO_THIRTY_PEOPLE = 4;
		/** 31~50名 */
		public static final int THIRTY_ONE_TO_FIFTY_PEOPLE = 5;
		/** 50名以上 */
		public static final int FIFTY_OR_MORE = 6;
	}

	/**
	 * スカウト受け取り区分
	 * @author whizz
	 */
	public static final class ScoutReceiveKbn {
		public static final String TYPE_CD = "scout_receive_kbn";
		/** 未選択 */
		public static final int UNSELECTED = 1;
		/** 受け取る */
		public static final int RECEIVE = 2;
		/** 断る */
		public static final int REFUSAL = 3;

	}

	/**
	 * 健康状態区分
	 * @author whizz
	 */
	public static final class HealthKbn {

		public static final String TYPE_CD = "health_kbn";
	}

	/**
	 * 刑事罰訴訟区分
	 * @author whizz
	 */
	public static final class SinKbn {

		public static final String TYPE_CD = "sin_kbn";
	}

	/**
	 * 暴力団区分
	 * @author whizz
	 */
	public static final class GangstersKbn {

		public static final String TYPE_CD = "gangsters_kbn";
	}

	/**
	 * 履歴修正フラグ
	 * @author whizz
	 */
	public static final class HistoryModificationFlg {

		public static final String TYPE_CD = "history_modification_flg";
	}

	/**
	 * 入退社修正フラグ
	 * @author whizz
	 */
	public static final class OnLeavingModificationFlg {

		public static final String TYPE_CD = "on_leaving_modification_flg";
	}

	/**
	 * 取得資格修正フラグ
	 * @author whizz
	 */
	public static final class QualificationModificationFlg {

		public static final String TYPE_CD = "qualification_modification_flg";
	}

	/**
	 * MT記事ID
	 */
	public static final class MtBlogId {

		public static final String TYPE_CD = "mt_blog_id";

		public static final int FEATURING_COMPANY = 10;

		public static final int CHUBOU = 7;

		public static final int MISERU = 11;

		public static final int STEP_UP = 16;

		public static final int SENDAI_KITCHEN = 12;

		public static final int SENDAI_HALL = 14;
	}

	/**
	 * 掲載ターム
	 */
	public static final class PostingTermKbn {

		public static final String TYPE_CD = "posting_term_kbn";

		public static final int TWO_WEEKS = 1;

		public static final int THREE_WEEKS = 2;

		public static final int FOUR_WEEKS = 3;

		public static final int FIVE_WEEKS = 4;
	}

	/**
	 * 配信形式
	 */
	public static final class deliveryTypeKbn {
		public static final String TYPE_CD = "delivery_type_kbn";

		public static final int HTML = 1;

		public static final int TEXT = 2;
	}

	/**
	 * 新規登録プレゼントキャンペーン希望
	 * @author whizz
	 */
	public static final class gourmetMagazineReceptionFlg {

		public static final String TYPE_CD = "gourmet_magazine_reception_flg";

		public static final int NOT_DESIRE = 0;

		public static final int DESIRE = 1;
	}

	/**
	 * グルメキャリー本誌の発送状況
	 * @author whizz
	 */
	public static final class deliveryStatus {

		public static final String TYPE_CD = "delivery_status";

		public static final int NOT_SEND = 0;

		public static final int SEND = 1;
	}

	/**
	 * オープン日検索フラグ
	 * @author whizz
	 */
	public static final class searchOpenDateFlg {

		public static final String TYPE_CD = "search_open_date_flg";

		public static final int NOT_INCLUDED = 0;

		public static final int INCLUDED = 1;
	}

	public static final class SearchPreferentialFlg {

		public static final String TYPE_CD = "search_preferential_flg";

		/** 非対象 */
		public static final int NO_TARGET = 0;

		/** 対象 */
		public static final int TARGET = 1;
	}
}
