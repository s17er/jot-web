package com.gourmetcaree.admin.pc.webdata.form.appTest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.admin.service.dto.CustomerSearchDto;
import com.gourmetcaree.common.form.BaseForm;

/**
 * WEBデータ応募テストのフォーム
 * @author Makoto Otani
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class InputForm extends BaseForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1783647404060118596L;

	/** 顧客検索用DTO */
	public CustomerSearchDto customerDto = new CustomerSearchDto();

	/** 原稿番号 */
	public String id;

	/** エリアコード */
	public String areaCd;

	/** 号数ID */
	public String volumeId;

	/** 原稿名 */
	public String manuscriptName;

	/** メールアドレス */
	public String mail;

	/** サブメールアドレス */
	public List<String> subMailList;

	/** 応募テストフラグ */
	public String applicationTestFlg;

	/** 営業担当者名 */
	public String salesName;

	/** 連絡先アドレス */
	public String salesMail;

	/** 連絡先サブアドレス */
	public String salesSubMail;

	/** コメント */
	public String comment;

	/** 連絡メール区分 */
	public int communicationMailKbn;

	/** バージョン番号 */
	public Long version;

	/**
	 * フォームのリセットを行う
	 */
	public void resetForm() {
		super.resetBaseForm();
		customerDto = new CustomerSearchDto();
		id = null;
		areaCd = null;
		volumeId = null;
		manuscriptName = null;
		mail = null;
		subMailList = new ArrayList<>();
		applicationTestFlg = null;
		salesName = null;
		salesMail = null;
		salesSubMail = null;
		comment = null;
		communicationMailKbn = 0;
		version = null;

	}
	/**
	 * ID以外のリセットを行う
	 */
	public void resetFormWithoutId() {
		super.resetBaseForm();
		customerDto = new CustomerSearchDto();
		areaCd = null;
		volumeId = null;
		manuscriptName = null;
		mail = null;
		subMailList = new ArrayList<>();
		applicationTestFlg = null;
		salesName = null;
		salesMail = null;
		salesSubMail = null;
		comment = null;
		communicationMailKbn = 0;
		version = null;
	}
}
