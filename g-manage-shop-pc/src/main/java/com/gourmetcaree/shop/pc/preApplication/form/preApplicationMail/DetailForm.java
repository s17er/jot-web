package com.gourmetcaree.shop.pc.preApplication.form.preApplicationMail;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.db.common.entity.TPreApplicationSchoolHistory;
import com.gourmetcaree.shop.pc.application.dto.application.CareerHistoryDto;

/**
 * プレ応募メール詳細フォームです。
 */
@Component(instance=InstanceType.REQUEST)
public class DetailForm extends PreApplicationMailForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -4763933061921375932L;

	/** メールの返信時の遷移元ページ区分（応募者の詳細から） */
	public static final String FROM_APPLICANT_DETAIL = "0";

	/** メールの返信時の遷移元ページ区分（メールの詳細から） */
	public static final String FROM_MAIL_DETAIL = "1";

	/** 応募者一覧からの一括送信 */
	public static final String FROM_APPLICATION_LIST = "2";

	public static final String APPLICATION_DETAIL_MAIL_LIST_ID = "applicationDetailMailListId";

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
    public Integer applicationId;

    /** メールステータス */
    public Integer mailStatus;

    /** 送信日時（受信日時） */
    public Date sendDatetime;

    /** バージョン情報 */
    public long version;

    /** 削除するバージョン */
    public String deleteVersion;

    public Integer originaMailId;

    public Integer sentenceId;

	/** 取得資格 */
	public String[] qualificationKbnList;

	/** Web履歴書 */
	public TPreApplicationSchoolHistory applicationSchoolHistory;

	/** 経歴書リスト */
	public List<CareerHistoryDto> applicationCareerHistoryList;

	public String errorMessage;

	public String fromPage;

    public boolean unsubscribeFlg = false;


	/**
	 * 経歴書リストがあるかどうか
	 * @return
	 */
	public boolean isApplicationCareerHistoryListExist() {
		return CollectionUtils.isNotEmpty(applicationCareerHistoryList);
	}

	/**
	 * Web履歴書があるかどうか
	 */
	public boolean isApplicationSchoolHistoryExist() {
		return applicationSchoolHistory != null;
	}

	/**
	 * リセットを行う
	 */
	public void resetForm() {
		super.resetPreApplicationMailBaseForm();
		mailKbn = null;
	    sendKbn = null;
	    senderKbn = null;
	    fromId = null;
	    fromName = null;
	    toName = null;
	    subject = null;
	    mailBody = null;
	    applicationId = null;
	    mailStatus = null;
	    sendDatetime = null;
	    version = 0L;
	    deleteVersion = null;
	    originaMailId = null;
	    sentenceId = null;
	    qualificationKbnList = null;
	    applicationSchoolHistory = null;
	    applicationCareerHistoryList = null;
	    fromPage = null;
	    unsubscribeFlg = false;
	}
}
