package com.gourmetcaree.admin.pc.juskill.form.juskillMember;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.common.util.GourmetCareeUtil;

import jp.co.whizz_tech.commons.WztRefrelctionUtils;

/**
 * ジャスキル会員編集のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class EditForm extends JuskillMemberForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -6612204614447995706L;

	/** 職歴リスト */
	public List<String> careerList = new ArrayList<>();

	/**
	 *  独自チェックを行う
	 * @return ActionMessages
	 */
	public ActionMessages validate() {

		// エラー情報
		ActionMessages errors = new ActionMessages();

		// 生年月日チェック

		return errors;
	}



	/**
	 * 生年月日必須入力チェック
	 * @return 生年月日が入力されていない場合、falseを返す
	 */
	private void checkBirthDay(ActionMessages errors) {

		if (StringUtils.isNotEmpty(birthYear) || StringUtils.isNotEmpty(birthMonth) || StringUtils.isNotEmpty(birthDate)) {
			 if (!StringUtils.isNumeric(birthYear) || !StringUtils.isNumeric(birthMonth) || !StringUtils.isNumeric(birthDate)) {
				errors.add("errors", new ActionMessage("errors.birthDayFailed"));
			} else {
				if (!GourmetCareeUtil.checkDate(birthYear, birthMonth, birthDate)) {
					errors.add("errors", new ActionMessage("errors.birthDayFailed"));
				} else {
					try {
						DateUtils.convertStrDate(birthYear, birthMonth, birthDate);
					} catch (ParseException e) {
						errors.add("errors", new ActionMessage("errors.birthDayFailed"));
					}
				}
			}
		}
	}

	/**
	 * リセットを行う
	 */
	public void resetForm() {
		WztRefrelctionUtils.reset(this, null);

		// 履歴書データ
		if (pdfFile != null) {
			pdfFile.destroy();
			pdfFile = null;
		}

		hiddenMaterialKbn = null;

		pdfFileName = null;
	}

}