package com.gourmetcaree.admin.pc.maintenance.form.sales;

import static com.gourmetcaree.common.constants.GourmetCareeConstants.*;

import java.io.Serializable;

import jp.co.whizz_tech.common.sastruts.annotation.EmailType;

import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.IntegerType;
import org.seasar.struts.annotation.Mask;
import org.seasar.struts.annotation.Minlength;
import org.seasar.struts.annotation.Msg;
import org.seasar.struts.annotation.Required;

import com.gourmetcaree.common.form.BaseForm;

/**
 * 営業担当者登録のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
public abstract class SalesForm extends BaseForm implements Serializable {


	/** シリアルバージョンUID */
	private static final long serialVersionUID = 5289480963015957901L;

	/** 営業担当者ID */
	public String id;

	/** 会社ID */
	@Required
	@IntegerType
	public String companyId;

	/** 会社名 */
	public String companyName;

	/** 氏名 */
	@Required
	public String salesName;

	/** 氏名(カナ) */
	@Required
	public String salesNameKana;

	/** 携帯電話1 */
	public String mobileNo1;

	/** 携帯電話2 */
	public String mobileNo2;

	/** 携帯電話3 */
	public String mobileNo3;

	/** 権限コード */
	@Required
	public String authorityCd;

	/** 権限名 */
	public String authorityName;

	/** メインアドレス */
	@Required
	@EmailType
	public String mainMail;

	/** サブアドレス */
	@EmailType
	public String subMail;

	/** サブアドレス受信フラグ */
	public String submailReceptionFlg;

	/** サブアドレス受信フラグ名 */
	public String submailReceptionFlgName;

	/** 所属部署 */
	public String department;

	/** ログインID */
	@Required
	@Mask(mask=MASK_SINGLE_ALPHANUMSYMBOL, msg = @Msg(key = "errors.singleAlphanumSymbol"))
	@Minlength(minlength = 6,  msg=@Msg(key="errors.loginIdMinLimit"), arg0 = @Arg(key = "6", resource = false))
	public String loginId;

	/** パスワード表示用 */
	public String dispPassword;

	/** 備考 */
	public String note;

	/** 旧システム会員ID */
	public String oldSystemSalesId;

	/** 登録日 */
	public String registrationDatetime;

	/** バージョン番号 */
	public String version;

	/**
	 * フォームのリセット
	 */
	public void resetForm() {
		id = null;
		companyId = null;
		companyName = null;
		salesName = null;
		salesNameKana = null;
		mobileNo1 = null;
		mobileNo2 = null;
		mobileNo3 = null;
		authorityCd = null;
		authorityName = null;
		mainMail = null;
		subMail = null;
		submailReceptionFlg = null;
		submailReceptionFlgName = null;
		department = null;
		loginId = null;
		dispPassword = null;
		note = null;
		oldSystemSalesId = null;
		registrationDatetime = null;
		version = null;
	}

}