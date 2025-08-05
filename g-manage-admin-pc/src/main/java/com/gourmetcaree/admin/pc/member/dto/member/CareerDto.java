package com.gourmetcaree.admin.pc.member.dto.member;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.gourmetcaree.accessor.tempMember.CareerHistoryDtoAccessor;
import com.gourmetcaree.common.util.GcCollectionUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.gourmetcaree.admin.service.accessor.tempMember.CareerDtoAccessor;

/**
 * 職歴DTOクラス
 * @author Takahiro Kimura
 * @version 1.0
 */
public class CareerDto implements Serializable, CareerDtoAccessor, CareerHistoryDtoAccessor {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -8004808265134821496L;

	/** 会社名 */
	public String companyName;

	/** 勤務期間 */
	public String workTerm;

	/** 職種 */
	public String[] job;

	/** 業態 */
	public String[] industry;

	/** 業務内容 */
	public String businessContent;

	/** 客席数・坪数 */
	public String seat;

	/** 月売上 */
	public String monthSales;


	@Override
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Override
	public String getCompanyName() {
		return companyName;
	}

	@Override
	public void setWorkTerm(String workTerm) {
		this.workTerm = workTerm;
	}
	@Override
	public String getWorkTerm() {
		return workTerm;
	}

	@Override
	public void setJob(String[] job) {
		this.job = job;
	}

	@Override
	public String[] getJob() {
		return job;
	}

	@Override
	public void setIndustry(String[] industry) {
		this.industry = industry;
	}

	@Override
	public String[] getIndustry() {
		return industry;
	}

	@Override
	public void setBusinessContent(String businessContent) {
		this.businessContent = businessContent;
	}

	@Override
	public String getBusinessContent() {
		return businessContent;
	}

	@Override
	public void setSeat(String seat) {
		this.seat = seat;
	}

	@Override
	public String getSeat() {
		return seat;
	}

	@Override
	public void setMonthSales(String monthSales) {
		this.monthSales = monthSales;
	}

	@Override
	public String getMonthSales() {
		return monthSales;
	}

	@Override
	public List<String> getJobKbnList() {
		return GcCollectionUtils.toList(job);
	}

	@Override
	public void setJobKbnList(List<String> jobKbnList) {
		final String[] empty = new String[0];
		if (jobKbnList == null) {
			this.job = empty;
			return;
		}

		this.job = jobKbnList.toArray(empty);
	}

	@Override
	public List<String> getIndustryKbnList() {
		return GcCollectionUtils.toList(industry);
	}

	@Override
	public void setIndustryKbnList(List<String> industryKbnList) {
		final String[] empty = new String[0];
		if (industryKbnList == null) {
			this.industry = empty;
			return;
		}

		this.industry = industryKbnList.toArray(empty);

	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}