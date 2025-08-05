package com.gourmetcaree.admin.service.accessor.tempMember;

import java.util.List;

public interface DetailAccessor<E extends CareerDtoAccessor> {

	String getId();

	void setId(String id);

	String getInsertDatatime();

	void setInsertDatatime(String insertDatatime);

	String getRegistrationDatetime();

	void setRegistrationDatetime(String registrationDatetime);

	String getLastLoginDatetime();

	void setLastLoginDatetime(String lastLoginDatetime);

	String getMemberName();

	void setMemberName(String memberName);

	String getMemberNameKana();

	void setMemberNameKana(String memberNameKana);

	String getAreaCd();

	void setAreaCd(String areaCd);

	String[] getAreaList();

	void setAreaList(String[] areaList);

	String getLoginId();

	void setLoginId(String loginId);

	String getSubMailAddress();

	void setSubMailAddress(String subMailAddress);

	String getBirthYear();

	void setBirthYear(String birthYear);

	String getBirthMonth();

	void setBirthMonth(String birthMonth);

	String getBirthDay();

	void setBirthDay(String birthDay);

	String getPassword();

	void setPassword(String password);

	String getRePassword();

	void setRePassword(String rePassword);

	String getDispPassword();

	void setDispPassword(String dispPassword);

	String getSexKbn();

	void setSexKbn(String sexKbn);

	String getPhoneNo1();

	void setPhoneNo1(String phoneNo1);

	String getPhoneNo2();

	void setPhoneNo2(String phoneNo2);

	String getPhoneNo3();

	void setPhoneNo3(String phoneNo3);

	String getPhoneNo();

	void setPhoneNo(String phoneNo);

	String getZipCd();

	void setZipCd(String zipCd);

	String getPrefecturesCd();

	void setPrefecturesCd(String prefecturesCd);

	String getMunicipality();

	void setMunicipality(String municipality);

	String getAddress();

	void setAddress(String address);

	String[] getQualification();

	void setQualification(String[] qualification);

	String getFoodExpKbn();

	void setFoodExpKbn(String foodExpKbn);

	String getForeignWorkFlg();

	void setForeignWorkFlg(String foreignWorkFlg);

	String getScoutSelfPr();

	void setScoutSelfPr(String scoutSelfPr);

	String getApplicationSelfPr();

	void setApplicationSelfPr(String applicationSelfPr);

	String getAdvancedRegistrationSelfPr();

	void setAdvancedRegistrationSelfPr(String advancedRegistrationSelfPr);

	String getJobInfoReceptionFlg();

	void setJobInfoReceptionFlg(String jobInfoReceptionFlg);

	String getMailMagazineReceptionFlg();

	void setMailMagazineReceptionFlg(String mailMagazineReceptionFlg);

	String getScoutMailReceptionFlg();

	void setScoutMailReceptionFlg(String scoutMailReceptionFlg);

	String[] getJob();

	void setJob(String[] job);

	String[] getIndustry();

	void setIndustry(String[] industry);

	String[] getWorkLocation();

	void setWorkLocation(String[] workLocation);

	String[] getEmployPtnKbns();

	void setEmployPtnKbns(String[] employPtnKbns);

	String getTransferFlg();

	void setTransferFlg(String transferFlg);

	String getMidnightShiftFlg();

	void setMidnightShiftFlg(String midnightShiftFlg);

	String getSalaryKbn();

	void setSalaryKbn(String salaryKbn);

	String getSalaryKbnName();

	void setSalaryKbnName(String salaryKbnName);

	String getSchoolName();

	void setSchoolName(String schoolName);

	String getDepartment();

	void setDepartment(String department);

	String getGraduationKbn();

	void setGraduationKbn(String graduationKbn);

	List<E> getCareerList();

	void setCareerList(List<E> careerList);

	String getJuskillFlg();

	void setJuskillFlg(String juskillFlg);

	String getPcMailStopFlg();

	void setPcMailStopFlg(String pcMailStopFlg);

	String getMobileMailStopFlg();

	void setMobileMailStopFlg(String mobileMailStopFlg);

	String getTerminalKbn();

	void setTerminalKbn(String terminalKbn);

	String getMemberKbn();

	void setMemberKbn(String memberKbn);

	Long getVersion();

	void setVersion(Long version);

	E newCareerDtoInstance();

	void setMemberRegisteredFlg(Integer memberRegisteredFlg);

	Integer getMemberRegisteredFlg();

	void setEditable(boolean editable);

	String registrationUrl();

	void setRegistrationUrl(String registrationUrl);
}
