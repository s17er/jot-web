package com.gourmetcaree.admin.pc.report.form.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gourmetcaree.admin.pc.report.dto.report.ListDetailDto;
import com.gourmetcaree.common.form.BaseForm;

/**
 * レポート詳細のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
public class DetailForm extends BaseForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 4586492742744564863L;

	/** エリアコード */
	public String areaCd;

	/** エリア */
	public String areaName;

	/** 号数ID */
	public String volumeId;

	/** 号数 */
	public String volume;

	/** 締切日時 */
	public Date deadlineDatetime;

	/** 確定締切日時 */
	public Date fixedDeadlineDatetime;

	/** 掲載開始日時 */
	public Date postStartDatetime;

	/** 掲載終了日時 */
	public Date postEndDatetime;

	/** 合計 */
	public int totalCount = 0;

	/** レポートのリスト */
	public List<ListDetailDto> reportList = new ArrayList<ListDetailDto>();

	/**
	 * リセットを行う
	 */
	public void resetForm() {
		super.resetBaseForm();
		areaCd = null;
		areaName = null;
		volumeId = null;
		volume = null;
		deadlineDatetime = null;
		fixedDeadlineDatetime = null;
		postStartDatetime = null;
		postEndDatetime = null;
		totalCount = 0;
		reportList = new ArrayList<ListDetailDto>();
	}
}