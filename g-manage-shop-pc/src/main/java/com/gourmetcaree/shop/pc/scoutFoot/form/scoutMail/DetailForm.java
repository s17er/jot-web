package com.gourmetcaree.shop.pc.scoutFoot.form.scoutMail;

import java.util.List;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.shop.pc.application.dto.application.CareerHistoryDto;



/**
 * スカウトメール詳細フォームです。
 * @author Takahiro Kimura
 * @version 1.0
 */
@Component(instance = InstanceType.REQUEST)
public class DetailForm extends ScoutMailForm {


	/** シリアルバージョンUID */
	private static final long serialVersionUID = -968240448117674546L;

	public enum FromMenuKbn {
		/** スカウト・足跡 */
		SCOUT_MAIL,
		/** メールボックス */
		MAIL_BOX,;
	}


    /** メール返信フラグ */
    public boolean returnMailFlg;

    /** メール送信可能フラグ */
    public boolean isSendMailFlg;

    /** 返信パス */
    public String returnPath;

    /** 削除パス */
    public String deletePath;

    /** 遷移元 */
    public FromMenuKbn fromMenu;

    /** スカウトメールログID */
    public String scoutMailLogId;

    public Integer sentenceId;

    /** 件名 */
    public String subject;

    /** 本文 */
    public String mailBody;

    public List<CareerHistoryDto> careerHistoryList;

    public String errorMessage;

    public String fromPage;


	/**
	 * リセットを行う
	 */
	public void resetForm() {

		super.resetForm();
		returnMailFlg = false;
		isSendMailFlg = false;
		returnPath = null;
		deletePath = null;
		fromMenu = null;
		scoutMailLogId = null;
		sentenceId = null;
		subject = null;
		mailBody = null;
		errorMessage = null;
		fromPage = null;
	}

}
