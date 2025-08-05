package com.gourmetcaree.admin.pc.mailMag.action.mailMagOption;

import javax.annotation.Resource;

import com.gourmetcaree.admin.pc.mailMag.form.mailMagOption.BaseMailMagOptionForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.db.common.service.MailMagazineOptionService;

/**
 * メルマガヘッダ用アクションのベースクラスです。
 * @author Takehiro Nakamori
 *
 */
public abstract class BaseMailMagOptionAction extends PcAdminAction {

	/** メルマガオプションサービス */
	@Resource
	protected MailMagazineOptionService mailMagazineOptionService;

	/**
	 * 不正な操作のエラーを返す
	 */
	protected void callFraudulentProcessError(BaseMailMagOptionForm form) {

		throw new FraudulentProcessException("不正な操作が行われました。" + form);

	}
}
