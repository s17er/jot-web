package com.gourmetcaree.shop.pc.scoutFoot.form.scoutMail;

import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.Maxlength;
import org.seasar.struts.annotation.Msg;
import org.seasar.struts.annotation.Required;

import com.gourmetcaree.common.form.BaseForm;
import com.gourmetcaree.shop.logic.dto.ScoutMailRemainDto;


/**
 * スカウトメールフォームです。
 * @author Takahiro Kimura
 * @version 1.0
 */
public abstract class ScoutMailForm extends BaseForm {


	/** シリアルバージョンUID */
	private static final long serialVersionUID = -6572227602868859555L;

	/** メールID */
	public String id;

	/** 送受信区分 */
	public String sendKbn;

	/** 会員ID */
	public String memberId;

    /** 件名 */
	@Required
	@Maxlength(maxlength = 256, msg = @Msg(key = "errors.passMaxLimit"), arg0 = @Arg(key = "件名", resource = false) ,arg1 = @Arg(key = "256", resource = false))
    public String subject;

    /** 本文 */
	@Required(arg0 = @Arg(key="本文", resource = false))
    public String body;

	/** メールステータス */
	public String mailStatus;

    /** 送信日時 */
    public String sendDatetime;

    /** バージョン番号 */
    public String version;

    /** スカウトメール残数DTO */
    public ScoutMailRemainDto scoutMailRemainDto;

    /** 気になるメールフラグ */
    public Boolean interestFlg;

    public void resetForm() {
    	super.resetBaseForm();
    	id = null;
    	sendKbn = null;
    	memberId = null;
        subject = null;
        body = null;
    	mailStatus = null;
        sendDatetime = null;
        version = null;
        scoutMailRemainDto = null;
        interestFlg = null;
    }




}
