package com.gourmetcaree.db.webdata.dto.webdata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.arnx.jsonic.JSONHint;

public class WebSearchDto implements Serializable {

	private static final long serialVersionUID = 8847138276882904540L;

	public WebSearchDto(Integer webId) {
		this.webId = webId;
	}

	@JSONHint(name="web_id")
	public Integer webId;

	/** 職種 */
	@JSONHint(name="job_kbn")
	public List<Integer> jobKbnList = new ArrayList<>();

	/** 職種 */
	@JSONHint(name="employ_ptn_kbn")
	public List<Integer> employPtnKbnList = new ArrayList<>();

	/** 待遇 */
	@JSONHint(name="treatment_kbn")
	public List<Integer> treatmentKbnList = new ArrayList<>();

	/** 企業特徴 */
	@JSONHint(name="company_characteristic_kbn")
	public List<Integer> companyCharacteristicKbnList = new ArrayList<>();

	/** 特集 */
	@JSONHint(name="special_id")
	public List<Integer> specialIdList = new ArrayList<>();

}
