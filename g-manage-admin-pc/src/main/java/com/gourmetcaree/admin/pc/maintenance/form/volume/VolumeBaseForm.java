package com.gourmetcaree.admin.pc.maintenance.form.volume;

import java.io.Serializable;
import java.util.Date;

import com.gourmetcaree.common.form.BaseForm;

/**
 * 号数データBaseのフォーム
 * @author Makoto Otani
 * @version 1.0
 */
public abstract class VolumeBaseForm extends BaseForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -1224017475433200415L;

	/** 締切日時 */
	public Date deadlineDatetime;

	/** 確定締切日時 */
	public Date fixedDeadlineDatetime;

	/** 掲載開始日時 */
	public Date postStartDatetime;

	/** 掲載終了日時 */
	public Date postEndDatetime;

	/** 締切日時 */
	public String deadlineDatetimeStr;

	/** 確定締切日時 */
	public String fixedDeadlineDatetimeStr;

	/** 掲載開始日時 */
	public String postStartDatetimeStr;

	/** 掲載終了日時 */
	public String postEndDatetimeStr;

	protected void resetBaseVolumeForm() {
		super.resetBaseForm();
		deadlineDatetime = null;
		fixedDeadlineDatetime = null;
		postStartDatetime = null;
		postEndDatetime = null;
		deadlineDatetimeStr = null;
		fixedDeadlineDatetimeStr = null;
		postStartDatetimeStr = null;
		postEndDatetimeStr = null;
	}

}