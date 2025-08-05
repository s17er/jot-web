package com.gourmetcaree.admin.pc.juskill.form.juskillMember;

import java.io.Serializable;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.DateType;
import org.seasar.struts.annotation.Msg;
import org.seasar.struts.util.MessageResourcesUtil;

import com.gourmetcaree.common.form.BaseForm;
import com.gourmetcaree.common.util.DateUtils;

import jp.co.whizz_tech.common.sastruts.annotation.IntegerHankakuType;

/**
 * ジャスキル会員一覧のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class ListForm extends BaseForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -6658404654447995706L;

	/** ページ遷移用に選択されたページ数 */
	public String pageNum;

	/** 最大表示件数 */
	public String maxRow;

	/** ID */
	public String where_id = "";

	/** ジャスキルNo */
	@IntegerHankakuType
	public String where_juskillMemberNo = "";

	/** 氏名 */
	public String where_juskillMemberName = "";

	/** 都道府県 */
	public String where_prefecturesCd = "";

	/** 登録日（開始） */
	@DateType(datePattern="yyyy/MM/dd", msg=@Msg(key="errors.datetype"))
	public String where_fromJuskillEntryDate = "";

	/** 登録日（終了） */
	@DateType(datePattern="yyyy/MM/dd", msg=@Msg(key="errors.datetype"))
	public String where_toJuskillEntryDate = "";

	/** 希望業態 */
	public String where_hopeIndustry = "";

	/** 希望職種 */
	public String where_hopeJob = "";

	/** 性別 */
	public String where_sexKbn = "";

	/** 下限年齢 */
	@IntegerHankakuType
	public String where_lowerAge = "";

	/** 上限年齢 */
	@IntegerHankakuType
	public String where_upperAge = "";

	/** メールアドレス */
	public String where_mail = "";

	/** フリーワード */
	public String free_word = "";

	/** 会員登録済みフラグ */
	public String where_memberRegisteredFlg = "";


	/** 検索条件を保持するMap */
	public Map<String, String> whereMap = new HashMap<String, String>();

	/**
	 *  独自チェックを行う
	 * @return ActionMessages
	 */
	public ActionMessages validate() {

		// エラー情報
		ActionMessages errors = new ActionMessages();

		if (StringUtils.isNotEmpty(where_lowerAge) && StringUtils.isNotEmpty(where_upperAge)) {
			try {
				// 上限年齢が下限年齢より小さい場合はエラー
				if (Integer.parseInt(where_lowerAge) > Integer.parseInt(where_upperAge)) {
					errors.add("errors", new ActionMessage("errors.fraudurentAge"));
				}
			} catch (NumberFormatException e1) {
				errors.add("errors", new ActionMessage("errors.ageFailed"));
			}
		}

		if (StringUtils.isNotEmpty(where_fromJuskillEntryDate) && StringUtils.isNotEmpty(where_toJuskillEntryDate)) {
			try {
				// 開始日より終了日が先の場合エラー
				if (DateUtils.compareDateTime(where_fromJuskillEntryDate, where_toJuskillEntryDate) < 0) {
					errors.add("errors", new ActionMessage("errors.app.TermSet", MessageResourcesUtil.getMessage("labels.where_fromJuskillEntryDate"), MessageResourcesUtil.getMessage("labels.where_toJuskillEntryDate")));
				}

			} catch (ParseException e) {
				errors.add("errors", new ActionMessage("errors.app.dateFailed"));
			}
		}

		return errors;
	}


	/**
	 * リセットを行う
	 */
	public void resetForm() {

		super.resetBaseForm();
		pageNum = null;
		maxRow = null;
		where_id = "";
		where_juskillMemberNo = "";
		where_juskillMemberName = "";
		where_prefecturesCd = "";
		where_fromJuskillEntryDate = "";
		where_toJuskillEntryDate = "";
		where_hopeJob = "";
		where_sexKbn = "";
		where_lowerAge = "";
		where_upperAge = "";
		where_mail = "";
		free_word = "";
		whereMap = new HashMap<String, String>();
		where_hopeIndustry = "";
		where_memberRegisteredFlg = "";
	}

}