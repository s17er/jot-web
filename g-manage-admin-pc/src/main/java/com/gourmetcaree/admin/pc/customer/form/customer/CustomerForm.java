package com.gourmetcaree.admin.pc.customer.form.customer;

import static com.gourmetcaree.common.constants.GourmetCareeConstants.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.util.StringUtil;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.Mask;
import org.seasar.struts.annotation.Minlength;
import org.seasar.struts.annotation.Msg;
import org.seasar.struts.annotation.Required;
import org.seasar.struts.util.MessageResourcesUtil;

import com.gourmetcaree.admin.pc.customer.dto.customer.CompanySalesDto;
import com.gourmetcaree.admin.pc.customer.dto.customer.ScoutMailAddHistoryDto;
import com.gourmetcaree.admin.pc.customer.dto.customer.ScoutMailRemainDto;
import com.gourmetcaree.common.form.BaseForm;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;

import jp.co.whizz_tech.common.sastruts.annotation.ZipType;
import jp.co.whizz_tech.commons.WztValidateUtil;

/**
 * 顧客登録のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
public abstract class CustomerForm extends BaseForm implements Serializable {


	/** シリアルバージョンUID */
	private static final long serialVersionUID = -7013155514247686341L;

	/**
	 * WEBデータ詳細から遷移した際の情報を管理するためのセッションキークラス
	 * @author Makoto Otani
	 */
	public static final class SESSION_KEY {
		public static final String WEB_ID = "com.gourmetcaree.admin.pc.customer.form.customer.CustomerForm.WEB_ID";
	}

	/** 顧客ID */
	public String id;

	/** 登録日 */
	public String registrationDatetime;

	/** 顧客名 */
	@Required
	public String customerName;

	/** 顧客名(カナ) */
	@Required
	public String customerNameKana;

	/** エリアコード */
	@Required
	public String areaCd;

	/** 表示用会社名 */
	public String displayCompanyName;

	/** 担当者名 */
	@Required
	public String contactName;

	/** 担当者名(カナ) */
	@Required
	public String contactNameKana;

	/** 担当者部署 */
	public String contactPost;

	/** 郵便番号 */
//	@Required
	@ZipType
	public String zipCd;

	/** 都道府県 */
//	@Required
	public String prefecturesCd;

	/** 市区町村 */
//	@Required
	public String municipality;

	/** 町名番地・ビル名 */
//	@Required
	public String address;

	/** 電話番号1 */
	public String phoneNo1;

	/** 電話番号2 */
	public String phoneNo2;

	/** 電話番号3 */
	public String phoneNo3;

	/** FAX番号1 */
	public String faxNo1;

	/** FAX番号2 */
	public String faxNo2;

	/** FAX番号3 */
	public String faxNo3;

	/** メインアドレス */
	@Required
	public String mainMail;

	/** サブメールのリスト */
	public List<SubMailDto> subMailDtoList;

	/** サブアドレス */
	public String subMail;

	/** サブアドレス受信フラグ */
	public String submailReceptionFlg;

	/** 担当会社ID */
	@Required
	public String assignedCompanyId;

	/** 営業担当者ID */
	@Required
	public String assignedSalesId;

	/** メルマガエリアコード */
	@Required
	public List<String> mailMagazineAreaCdList;

	/** 削除営業担当者ID */
	public String deleteSalesId;

	/** 担当会社・営業担当者DTOリスト */
	public List<CompanySalesDto> companySalesList = new ArrayList<CompanySalesDto>();

	/** ログインID */
	@Required
	@Mask(mask=MASK_SINGLE_ALPHANUMSYMBOL, msg = @Msg(key = "errors.singleAlphanumSymbol"))
	@Minlength(minlength = 6,  msg=@Msg(key="errors.loginIdMinLimit"), arg0 = @Arg(key = "6", resource = false))
	public String loginId;

	/** ログインIDリスト */
	public List<String> loginIdList;

	/** パスワード表示用 */
	public String dispPassword;

	/** 備考 */
	public String note;

	/** ログイン可否(フラグ) */
	@Required
	public String loginFlg;

	/** 掲載可否(フラグ) */
	@Required
	public String publicationFlg;

	/** スカウトメール使用可否(フラグ) */
	@Required
	public String scoutUseFlg;

	/** 追加スカウトメール */
	public String addScout;

	/** スカウトメール追加履歴Dtoリスト */
	public List<ScoutMailAddHistoryDto> scoutMailAddHistoryList;

	/** 無料スカウトメール残数 */
	public long freeRemainScoutMail;

	/** 購入スカウトメール残数 */
	public List<ScoutMailRemainDto> boughtScoutMailRemainDtoList;

	/** 無期限スカウトメール残数 */
	public long unlimitedRemainScoutMail;

	/** バージョン番号(顧客マスタ) */
	public Long customerVersion;

	/** 顧客アカウントマスタID */
	public int customerAccountId;

	/** バージョン番号(顧客アカウントマスタ) */
	public Long customerAccountVersion;

	/** スカウトメール数テーブルID */
	public int scoutMailCountId;

	/** バージョン番号(スカウトメール数) */
	public Long scoutMailCountVersion;

	/** 未読応募メール数 */
	public int unopenedAppricationCnt;

	/** 未読スカウトメール数 */
	public int unopenedScoutCnt;

	/** 最終ログイン日時 */
	public String loginDatetime;

	/** テストフラグ */
	@Required
	public String testFlg;

	/** メルマガ受信フラグ */
	@Required
	public String mailMagazineReceptionFlg;

	/** 掲載終了時の区分 */
	@Required
	public String publicationEndDisplayFlg;

	/** 系列店舗の件数 */
	public int shopListCount;

	/** 系列店舗の都道府県一覧 */
	public List<Integer> shopListPrefecturesCdList;

	/** 系列店舗の海外エリア一覧 */
	public List<Integer> shopListShutokenForeignAreaKbnList;

	/** 系列店舗の業態区分一覧 */
	public List<Integer> shopListIndustryKbnList;

	/** 設立 */
	public String establishment;

	/** 資本金 */
	public String capital;

	/** 代表者 */
	public String representative;

	/** 従業員 */
	public String employee;

	/** 事業内容 */
	public String businessContent;

	/** ホームページ */
	public List<HomepageDto> homepageDtoList;

	/**
	 *  担当会社・営業担当者入力チェック
	 * @return
	 */
	public ActionMessages companySalesValidate() {

		ActionMessages errors = new ActionMessages();

		assigneCompanySalesCheck(errors);

		return errors;

	}

	/**
	 * 担当者・営業担当者入力チェック
	 * @param errors
	 */
	protected void assigneCompanySalesCheck(ActionMessages errors) {

		// 担当会社・営業担当者が両方入力されているかチェック
		if (StringUtils.isBlank(assignedCompanyId) && StringUtils.isBlank(assignedSalesId)) {
			errors.add("errors", new ActionMessage("errors.nonAssingnedCompanySales"));
		} else if (StringUtils.isBlank(assignedCompanyId)) {
			errors.add("errors", new ActionMessage("errors.nonAssignedCompany"));
		} else if (StringUtils.isBlank(assignedSalesId)) {
			errors.add("errors", new ActionMessage("errors.nonAssignedSales"));
		}

		// 同一の営業担当者が入力されていないかチェック
		for (CompanySalesDto dto : companySalesList) {
			if (dto.salesId.equals(assignedSalesId)) {
				errors.add("errors", new ActionMessage("errors.sameAssignedSalesSelect"));
				break;
			}
		}
	}

	/**
	 * 残りのスカウトメールがあるかどうか
	 * @return
	 */
	public boolean isExistRemainScoutMail() {
		if (freeRemainScoutMail > 0
				|| unlimitedRemainScoutMail > 0
				|| isExistRemainBoughtScoutMail()) {
			return true;
		}
		return false;
	}

	/**
	 * 購入スカウトメールの残りがあるかどうか
	 * @return
	 */
	public boolean isExistRemainBoughtScoutMail() {
		return !CollectionUtils.isEmpty(boughtScoutMailRemainDtoList);
	}

	/**
	 * カタカナ入力のチェック
	 */
	protected void checkKatakanaInput(ActionMessages errors) {
		if (StringUtils.isBlank(customerNameKana)) {
			errors.add("errors", new ActionMessage("errors.required",
									MessageResourcesUtil.getMessage("labels.customerNameKana")));
		} else if (!GourmetCareeUtil.isKatakanaStr(customerNameKana)) {
			errors.add("errors", new ActionMessage("errors.katakana",
									MessageResourcesUtil.getMessage("labels.customerNameKana")));
		}

		if (StringUtils.isBlank(contactNameKana)) {
			errors.add("errors", new ActionMessage("errors.required",
									MessageResourcesUtil.getMessage("labels.contactNameKana")));
		} else  if (!GourmetCareeUtil.isKatakanaStr(contactNameKana)) {
			errors.add("errors", new ActionMessage("errors.katakana",
									MessageResourcesUtil.getMessage("labels.contactNameKana")));
		}
	}
	
	/**
	 * メインメールのチェックをする
	 * @param errors
	 */
	protected void checkMainMail(ActionMessages errors) {
		if (!StringUtils.isBlank(mainMail)) {
			// アドレスチェック
			if(!Pattern.matches("[\\w._\\-+]+@([\\w_\\-]+\\.)+[\\w_\\-]+", mainMail)) {
				errors.add("errors", new ActionMessage("errors.email", MessageResourcesUtil.getMessage("labels.mainMail")));
			}
		}
	}

	/**
	 * サブメールのチェックをする
	 * @param errors
	 */
	protected void checkSubMail(ActionMessages errors) {
		int i = 1;

		List<SubMailDto> newList = new ArrayList<>();

		for (SubMailDto dto : subMailDtoList) {

			if (StringUtils.isEmpty(dto.subMail)) {
				continue;
			}

			String no = String.valueOf(i);
			if (StringUtils.isEmpty(dto.submailReceptionFlg)) {
				errors.add("errors", new ActionMessage("errors.notSelectData",
						MessageResourcesUtil.getMessage("labels.subMail") + no,
						MessageResourcesUtil.getMessage("labels.submailReceptionFlg") + no)
				);
			}

			// アドレスチェック
			if(!Pattern.matches("[\\w._\\-+]+@([\\w_\\-]+\\.)+[\\w_\\-]+", dto.subMail)) {
				errors.add("errors", new ActionMessage("errors.email", MessageResourcesUtil.getMessage("labels.subMail") + no));
			}

			// 間を詰める
			newList.add(dto);
			i++;
		}
		subMailDtoList = newList;
		setSubMailEntryForm();
	}

	protected void checkURL(ActionMessages errors) {
		int i = 1;

		List<HomepageDto> newList = new ArrayList<>();

		for (HomepageDto dto : homepageDtoList) {

			if (StringUtils.isEmpty(dto.url) && StringUtils.isEmpty(dto.comment)) {
				continue;
			}

			String no = String.valueOf(i);
			// ホームページの入力チェック
			if (StringUtil.isNotEmpty(dto.url)) {
				if(!WztValidateUtil.isHttpUrl(dto.url.trim())) {
					// 「{ホームページn}はURLとして不正です。」
					errors.add("errors", new ActionMessage("errors.url",
							MessageResourcesUtil.getMessage("labels.homepage") + no));
				}
				dto.url = dto.url.trim();
			}

			// ホームページコメントのチェック
			if (StringUtil.isNotBlank(dto.comment) && StringUtil.isBlank(dto.url) ) {
				errors.add("errors", new ActionMessage("errors.noRelatedInputOneVsOne",
						MessageResourcesUtil.getMessage("labels.homepageComment" + no),
						MessageResourcesUtil.getMessage("labels.homepage" + no)));
			}
			// 間を詰める
			newList.add(dto);
			i++;
		}
		homepageDtoList = newList;
		setHopepageEntryForm();
	}

	/**
	 * フォームのリセット
	 */
	public void resetForm() {
		id = null;
		registrationDatetime = null;
		customerName = null;
		customerNameKana = null;
		areaCd = null;
		displayCompanyName = null;
		contactName = null;
		contactNameKana = null;
		contactPost = null;
		zipCd = null;
		prefecturesCd = null;
		municipality = null;
		address = null;
		phoneNo1 = null;
		phoneNo2 = null;
		phoneNo3 = null;
		faxNo1 = null;
		faxNo2 = null;
		faxNo3 = null;
		mainMail = null;
		subMail = null;
		submailReceptionFlg = null;
		assignedCompanyId = null;
		assignedSalesId = null;
		deleteSalesId = null;
		companySalesList = new ArrayList<CompanySalesDto>();
		loginId = null;
		loginIdList = null;
		dispPassword = null;
		note = null;
		loginFlg = null;
		publicationFlg = null;
		scoutUseFlg = null;
		addScout = null;
		scoutMailAddHistoryList = null;
		freeRemainScoutMail = 0L;
		boughtScoutMailRemainDtoList = null;
		unlimitedRemainScoutMail = 0L;
		customerVersion = null;
		customerAccountId = 0;
		customerAccountVersion = null;
		scoutMailCountId = 0;
		scoutMailCountVersion = null;
		unopenedAppricationCnt = 0;
		unopenedScoutCnt = 0;
		loginDatetime = null;
		testFlg = null;
		mailMagazineReceptionFlg = null;
		publicationEndDisplayFlg = null;
		mailMagazineAreaCdList = new ArrayList<String>();
		shopListCount = 0;
		shopListIndustryKbnList = new ArrayList<Integer>();
		subMailDtoList = new ArrayList<>();
		establishment = null;
		capital = null;
		representative = null;
		employee = null;
		businessContent = null;
		homepageDtoList = new ArrayList<>();
		mailMagazineAreaCdList = new ArrayList<>();

	}
	/**
	 * multiboxのリセットを行う
	 */
	public void resetMultibox() {
		mailMagazineAreaCdList = new ArrayList<String>();
	}

	/**
	 * サブメールを登録用にセットする
	 * @param form
	 */
	public void setSubMailEntryForm() {
		// 3つセットする
		if (subMailDtoList.size() >= 3) {
			return;
		}
		int count = 3 - subMailDtoList.size();
		for (int i = 0; i < count; i++) {
			subMailDtoList.add(new SubMailDto());
		}
	}

	/**
	 * サブメールDTOリストの空を詰める
	 */
	public void packSubMailDtoList()
	{
		subMailDtoList = subMailDtoList.stream().filter(s -> StringUtils.isNotEmpty(s.subMail)).collect(Collectors.toList());
	}

	/**
	 * ホームページを登録用にセットする
	 * @param form
	 */
	public void setHopepageEntryForm() {
		// 3つセットする
		if (homepageDtoList.size() >= 3) {
			return;
		}
		int count = 3 - homepageDtoList.size();
		for (int i = 0; i < count; i++) {
			homepageDtoList.add(new HomepageDto());
		}
	}

	/**
	 * サブメールの値を保持するDTO
	 * @author whizz
	 *
	 */
	public static class SubMailDto implements Serializable {
		private static final long serialVersionUID = -535576959773981750L;
		public String subMail;
		public String submailReceptionFlg = String.valueOf(MTypeConstants.SubmailReceptionFlg.RECEIVE);
		public String getSubMail() {
			return subMail;
		}
		public String getSubmailReceptionFlg() {
			return submailReceptionFlg;
		}
	}

	/**
	 * ホームページの値を保持するDTO
	 * @author whizz
	 *
	 */
	public static class HomepageDto implements Serializable {
		private static final long  serialVersionUID = -535546559773981750L;
		public String url;
		public String comment;
		public String getUrl() {
			return url;
		}
		public String getComment() {
			return comment;
		}
	}

}