package com.gourmetcaree.shop.pc.application.form.observateApplicationMail;

import java.util.Date;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.shop.pc.application.form.applicationMail.ApplicationMailForm;

/**
 * 応募メール詳細フォームです。
 * @author Takahiro Kimura
 * @version 1.0
 */
@Component(instance=InstanceType.REQUEST)
public class DetailForm extends ApplicationMailForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -4763933061921375932L;

    /** メール区分 */
    public Integer mailKbn;

    /** 送受信区分 */
    public Integer sendKbn;

    /** 送信者区分 */
    public Integer senderKbn;

    /** 送信者ID */
    public Integer fromId;

    /** 送信者名 */
    public String fromName;

    /** 受信者名 */
    public String toName;

    /** 件名 */
    public String subject;

    /** 本文 */
    public String mailBody;

    /** 応募ID */
    public Integer observateApplicationId;

    /** メールステータス */
    public Integer mailStatus;

    /** 送信日時（受信日時） */
    public Date sendDatetime;

    /** バージョン情報 */
    public long version;

    /** 削除するバージョン */
    public String deleteVersion;

    /** 元の画面に戻るためのID（質問メールID） */
    public String dispObservateApplicationId;

	/** 質問日時 */
    public Date applicationDatetime;

	/** 氏名（漢字） */
    public String name;

	/** 氏名（カナ） */
    public String nameKana;

	/** 性別 */
    public String sexKbn;

	/** 年齢 */
    public String age;

	/** 電話番号 */
	public String phoneNo;

	/** PCメールアドレス */
	public String pcMail;

	/** 携帯メールアドレス */
	public String mobileMail;

    public Integer originaMailId;

    public Integer sentenceId;

	public String memo;

	public String errorMessage;

	public String fromPage;

	public boolean unsubscribeFlg = false;

	/**
	 * リセットを行う
	 */
	public void resetForm() {
		super.resetApplicationMailBaseForm();
		mailKbn = null;
		sendKbn = null;
		senderKbn = null;
		fromId = null;
		fromName = null;
		toName = null;
		subject = null;
		mailBody = null;
		observateApplicationId = null;
		mailStatus = null;
		sendDatetime = null;
		version = 0L;
		deleteVersion = null;
		applicationDatetime = null;
		name = null;
		nameKana = null;
		sexKbn = null;
		age = null;
		phoneNo = null;
		pcMail = null;
		mobileMail = null;
	    originaMailId = null;
	    sentenceId = null;
	    memo = null;
	    errorMessage = null;
	    fromPage = null;
	    unsubscribeFlg = false;
	}
}
