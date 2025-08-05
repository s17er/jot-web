package com.gourmetcaree.shop.pc.application.form.arbeitMail;

import java.util.Date;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * アルバイトメール詳細アクションフォーム
 * @author Takehiro Nakamori
 *
 */
@Component(instance = InstanceType.REQUEST)
public class DetailForm extends ArbeitMailBaseForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -822457008946078428L;

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
    public Integer arbeitApplicationId;

    /** メールステータス */
    public Integer mailStatus;

    /** 送信日時（受信日時） */
    public Date sendDatetime;

    /** バージョン情報 */
    public long version;

    /** 削除するバージョン */
    public String deleteVersion;

    public Integer sentenceId;

	public String memo;

	public String errorMessage;

	public String fromPage;

	@Override
	public void resetForm() {
		super.resetBaseForm();
		mailKbn = null;
		sendKbn = null;
		senderKbn = null;
		fromId = null;
		fromName = null;
		toName = null;
		subject = null;
		mailBody = null;
		arbeitApplicationId = null;
		mailStatus = null;
		sendDatetime = null;
		version = 0L;
		deleteVersion = null;
	    sentenceId = null;
	    memo = null;
	    errorMessage = null;
	    fromPage = null;
	}
}
