package com.gourmetcaree.admin.pc.member.form.tempMember;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.annotation.Arg;
import org.seasar.struts.annotation.DateType;
import org.seasar.struts.annotation.Msg;
import org.seasar.struts.util.MessageResourcesUtil;

import com.gourmetcaree.admin.service.accessor.tempMember.SearchAccessor;
import com.gourmetcaree.common.form.BaseForm;
import com.gourmetcaree.common.util.DateUtils;

import jp.co.whizz_tech.common.sastruts.annotation.IntegerHankakuType;

/**
 * 会員一覧のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class ListForm extends BaseForm implements SearchAccessor {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -6658404654447994706L;

	/** ページ遷移用に選択されたページ数 */
	public String pageNum;

	/** 最大表示件数 */
	public String maxRow;

	/** ID */
	@IntegerHankakuType(arg0 = @Arg(key="ID", resource = false))
	public String where_id = "";

	/** 氏名 */
	public String where_name = "";

	/** フリガナ */
	public String where_nameKana = "";

	/** エリア */
	public String where_areaCd = "";

	/** 都道府県 */
	public String where_prefecturesCd = "";

	/** 希望業種 */
	public String where_industryCd = "";

	/** 雇用形態 */
	public String where_empPtnKbn = "";

	/** 性別 */
	public String where_sexKbn = "";

	/** 下限年齢 */
	@IntegerHankakuType
	public String where_lowerAge = "";

	/** 上限年齢 */
	@IntegerHankakuType
	public String where_upperAge = "";

	/** 登録日(開始) */
	@DateType(datePattern="yyyy/MM/dd", msg=@Msg(key="errors.datetype"))
	public String where_fromRegistratedDate = "";

	/** 登録日(終了) */
	@DateType(datePattern="yyyy/MM/dd", msg=@Msg(key="errors.datetype"))
	public String where_toRegistratedDate = "";

	/** メールアドレス */
	public String where_mail = "";

	/** 会員登録フラグ */
	public String where_memberRegisteredFlg;


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

		if (StringUtils.isNotEmpty(where_fromRegistratedDate) && StringUtils.isNotEmpty(where_toRegistratedDate)) {
			try {
				// 開始日時より終了日時が先の場合エラー
				if (DateUtils.compareDateTime(where_fromRegistratedDate, where_toRegistratedDate) < 0) {
					errors.add("errors", new ActionMessage("errors.app.TermSet", MessageResourcesUtil.getMessage("labels.where_fromUpdateDate"), MessageResourcesUtil.getMessage("labels.where_toUpdateDate")));
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
		where_name = "";
		where_nameKana = "";
		where_areaCd = "";
		where_prefecturesCd = "";
		where_industryCd = "";
		where_empPtnKbn = "";
		where_sexKbn = "";
		where_lowerAge = "";
		where_upperAge = "";
		where_fromRegistratedDate = "";
		where_toRegistratedDate = "";
		where_mail = "";
		whereMap = new HashMap<String, String>();
		where_memberRegisteredFlg = "";

	}



	@Override
	public String getPageNum() {
		return pageNum;
	}

	@Override
	public String getMaxRow() {
		return maxRow;
	}

	@Override
	public String getWhere_id() {
		return where_id;
	}

	@Override
	public String getWhere_name() {
		return where_name;
	}

	@Override
	public String getWhere_nameKana() {
		return where_nameKana;
	}

	@Override
	public String getWhere_areaCd() {
		return where_areaCd;
	}

	@Override
	public String getWhere_prefecturesCd() {
		return where_prefecturesCd;
	}

	@Override
	public String getWhere_industryCd() {
		return where_industryCd;
	}

	@Override
	public String getWhere_empPtnKbn() {
		return where_empPtnKbn;
	}

	@Override
	public String getWhere_sexKbn() {
		return where_sexKbn;
	}

	@Override
	public String getWhere_lowerAge() {
		return where_lowerAge;
	}

	@Override
	public String getWhere_upperAge() {
		return where_upperAge;
	}

	@Override
	public String getWhere_fromRegistratedDate() {
		return where_fromRegistratedDate;
	}

	@Override
	public String getWhere_toRegistratedDate() {
		return where_toRegistratedDate;
	}


	@Override
	public String getWhere_mail() {
		return where_mail;
	}

	@Override
	public String getWhere_memberRegisteredFlg() {
		return where_memberRegisteredFlg;
	}

}