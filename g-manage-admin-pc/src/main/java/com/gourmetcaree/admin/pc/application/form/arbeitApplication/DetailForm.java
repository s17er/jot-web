package com.gourmetcaree.admin.pc.application.form.arbeitApplication;

import java.sql.Timestamp;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.common.form.BaseForm;

/**
 * グルメdeバイト応募管理詳細アクションフォーム
 * @author Takehiro Nakamori
 *
 */
@Component(instance = InstanceType.REQUEST)
public class DetailForm extends BaseForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1194852674702592037L;

	/** ID */
	public String id;

	/** 応募日時 */
	public Timestamp applicationDatetime;

	/** 応募名 */
	public String applicationName;

	/** エリア */
	public String areaCd;

	/** 名前 */
	public String name;


	/** 名前(カナ) */
	public String nameKana;


	/** 性別区分 */
	public Integer sexKbn;


	/** 年齢 */
	public Integer age;


	/** 郵便番号 */
	public String zipCd;


	/** 都道府県コード */
	public Integer prefecturesCd;


	/** 市区 */
	public String municipality;


	/** 住所 */
	public String address;


	/** 電話番号 */
	public String phoneNo;


	/** メールアドレス */
	public String mailAddress;


	/** 現在の職業区分 */
	public Integer currentJobKbn;


	/** 勤務可能時期区分 */
	public Integer possibleEntryTermKbn;


	/** 飲食店勤務の経験区分 */
	public Integer foodExpKbn;


	/** 応募職種 */
	public String applicationJob;


	/** 希望連絡時間・連絡方法 */
	public String connectionTime;


	/** 自己ＰＲ・要望 */
	public String applicationSelfPr;


	/** 端末区分 */
	public Integer terminalKbn;

	/** 顧客名 */
	public String customerName;

}
