package com.gourmetcaree.accessor.tempMember;

import java.util.List;

/**
 * 各モジュールのCareerHistoryDtoにアクセスするインタフェース
 *
 * @author nakamori
 *
 */
public interface CareerHistoryDtoAccessor {

	String getCompanyName();

	void setCompanyName(String companyName);

	String getWorkTerm();

	void setWorkTerm(String workTerm);

	List<String> getJobKbnList();

	void setJobKbnList(List<String> jobKbnList);

	List<String> getIndustryKbnList();

	void setIndustryKbnList(List<String> industryKbnList);

	String getBusinessContent();

	void setBusinessContent(String businessContent);

	String getSeat();

	void setSeat(String seat);

	String getMonthSales();

	void setMonthSales(String monthSales);

}
