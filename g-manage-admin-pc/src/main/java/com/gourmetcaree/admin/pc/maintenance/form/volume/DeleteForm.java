package com.gourmetcaree.admin.pc.maintenance.form.volume;

import java.io.Serializable;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * 号数データ削除のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
@Component(instance = InstanceType.REQUEST)
public class DeleteForm extends VolumeBaseForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -2982838919576395105L;

	/** 号数ID */
	public String id;

	/** エリア */
	public String areaCd;

	/** エリア名 */
	public String areaName;

	/** 号数 */
	public String volume;

	/** 締切日時(年月日) */
	public String deadlineDate;

	/** 締切日時(時) */
	public String deadlineHour;

	/** 締切日時(分) */
	public String deadlineMinute;

	/** 確定締切日時(年月日) */
	public String fixedDeadlineDate;

	/** 確定締切日時(時) */
	public String fixedDeadlineHour;

	/** 確定締切日時(分) */
	public String fixedDeadlineMinute;

	/** 掲載開始日時(年月日) */
	public String postStartDate;

	/** 掲載開始日時(時) */
	public String postStartHour;

	/** 掲載開始日時(分) */
	public String postStartMinute;

	/** 掲載終了日時(年月日) */
	public String postEndDate;

	/** 掲載終了日時(時) */
	public String postEndHour;

	/** 掲載終了日時(分) */
	public String postEndMinute;

	/** バージョン番号 */
	public String version;

	/**
	 * フォームのリセットを行う
	 */
	public void resetForm() {
		resetBaseVolumeForm();
		id = null;
		areaCd = null;
		areaName = null;
		volume = null;
		deadlineDate = null;
		deadlineHour = null;
		deadlineMinute = null;
		fixedDeadlineDate = null;
		fixedDeadlineHour = null;
		fixedDeadlineMinute = null;
		postStartDate = null;
		postStartHour = null;
		postStartMinute = null;
		postEndDate = null;
		postEndHour = null;
		postEndMinute = null;
		version = null;
	}
}