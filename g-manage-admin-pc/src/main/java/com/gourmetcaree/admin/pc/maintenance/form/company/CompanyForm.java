package com.gourmetcaree.admin.pc.maintenance.form.company;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.whizz_tech.common.sastruts.annotation.EmailType;
import jp.co.whizz_tech.common.sastruts.annotation.IntegerHankakuType;
import jp.co.whizz_tech.common.sastruts.annotation.ZipType;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.annotation.Required;
import org.seasar.struts.util.MessageResourcesUtil;

import com.gourmetcaree.common.form.BaseForm;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;

/**
 *
 * 会社の入力用フォーム
 * @author Makoto Otani
 * @version 1.0
 */
public abstract class CompanyForm extends BaseForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 3748073317637284546L;

	/** 会社ID */
	public String id;

	/** 社名 */
	@Required
	public String companyName;

	/** 社名(カナ) */
	@Required
	public String companyNameKana;

	/** 担当者名 */
	@Required
	public String contactName;

	/** 担当者(カナ) */
	@Required
	public String contactNameKana;

	/** 代理店フラグ */
	@Required
	public String agencyFlg;

	/** エリアコード */
	@Required
	public List<String> areaCd = new ArrayList<String>();

	/** 郵便番号 */
	@Required
	@ZipType
	public String zipCd;

	/** 都道府県 */
	@Required
	public String prefecturesCd;

	/** 市区町村 */
	@Required
	public String municipality;

	/** 住所 */
	@Required
	public String address;

	/** 電話番号1 */
	@Required
	@IntegerHankakuType
	public String phoneNo1;

	/** 電話番号2 */
	@Required
	@IntegerHankakuType
	public String phoneNo2;

	/** 電話番号3 */
	@Required
	@IntegerHankakuType
	public String phoneNo3;

	/** FAX番号1 */
	@IntegerHankakuType
	public String faxNo1;

	/** FAX番号2 */
	@IntegerHankakuType
	public String faxNo2;

	/** FAX番号3 */
	@IntegerHankakuType
	public String faxNo3;

	/** メインアドレス */
	@Required
	@EmailType
	public String mainMail;

	/** サブアドレス */
	@EmailType
	public String subMail;

	/** サブアドレス受信フラグ */
	public String submailReceptionFlg;

	/** 備考 */
	public String note;

	/** 登録日 */
	public Date registrationDatetime;

	/** バージョン番号 */
	public Long version;

	/**
	 *  独自チェックを行う
	 * @return ActionMessages
	 */
	public ActionMessages validate() {

		// エラー情報
		ActionMessages errors = new ActionMessages();

		// FAX電話チェック
		if (!GourmetCareeUtil.checkPhoneNo(faxNo1, faxNo2, faxNo3)) {
			// 「{FAX番号}を正しく入力してください。」
			errors.add("errors", new ActionMessage("errors.inputValue", MessageResourcesUtil.getMessage("labels.faxNo")));
		}

		// サブアドレス受信フラグが受信可の場合は、サブアドレスが入力必須
		if (StringUtils.isBlank(subMail) &&
				String.valueOf(MTypeConstants.SubmailReceptionFlg.RECEIVE).equals(submailReceptionFlg)) {
			// 「サブアドレスを入力の上、受信可を選択してください。」
			errors.add("errors", new ActionMessage("errors.subMailReceptionFlg"));
		}

		return errors;
	}

	/**
	 * multiboxのリセットを行う
	 */
	public void resetMultibox() {
		this.areaCd = new ArrayList<String>();
	}


	public void resetForm() {
		resetBaseForm();
		id = null;
		companyName = null;
		companyNameKana = null;
		contactName = null;
		contactNameKana = null;
		agencyFlg = null;
		areaCd = new ArrayList<String>();
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
		note = null;
		registrationDatetime = null;
		version = null;
	}
}