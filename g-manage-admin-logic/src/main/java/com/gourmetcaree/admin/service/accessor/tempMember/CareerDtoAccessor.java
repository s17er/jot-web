package com.gourmetcaree.admin.service.accessor.tempMember;

public interface CareerDtoAccessor {


	void setCompanyName(String companyName);


	String getCompanyName();



	void setWorkTerm(String workTerm);

	String getWorkTerm();

	void setJob(String[] job);

	String[] getJob();

	void setIndustry(String[] industry);

	String[] getIndustry();

	void setBusinessContent(String businessContent);

	String getBusinessContent();

	void setSeat(String seat);

	String getSeat();

	void setMonthSales(String monthSales);

	String getMonthSales();
}
