package com.gourmetcaree.admin.pc.member.form.tempMember;

import java.util.List;

import com.gourmetcaree.admin.pc.member.dto.member.CareerDto;
import com.gourmetcaree.admin.pc.member.form.member.MemberForm;
import com.gourmetcaree.admin.service.accessor.tempMember.DetailAccessor;

/**
 * 仮会員基底フォーム
 * データ内容は基本的に会員と同じなので、会員用フォームを継承する。
 * @author nakamori
 *
 */
public class DetailBaseForm extends MemberForm implements DetailAccessor<CareerDto> {

	/**
	 *
	 */
	private static final long serialVersionUID = -7643695608252822197L;

	private String advancedRegistrationSelfPr;

	/** 会員登録済みフラグ */
	public Integer memberRegisteredFlg;

	/** アクセスコード */
	public String accessCd;

	/** 本登録用URL */
	public String registrationUrl;

	public boolean isEditable;

	@Override
	public void setEditable(boolean editable) {
		this.isEditable = editable;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getInsertDatatime() {
		return insertDatatime;
	}

	@Override
	public void setInsertDatatime(String insertDatatime) {
		this.insertDatatime = insertDatatime;
	}

	@Override
	public String getRegistrationDatetime() {
		return registrationDatetime;
	}

	@Override
	public void setRegistrationDatetime(String registrationDatetime) {
		this.registrationDatetime = registrationDatetime;
	}

	@Override
	public String getLastLoginDatetime() {
		return lastLoginDatetime;
	}

	@Override
	public void setLastLoginDatetime(String lastLoginDatetime) {
		this.lastLoginDatetime = lastLoginDatetime;
	}

	@Override
	public String getMemberName() {
		return memberName;
	}

	@Override
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	@Override
	public String getMemberNameKana() {
		return memberNameKana;
	}

	@Override
	public void setMemberNameKana(String memberNameKana) {
		this.memberNameKana = memberNameKana;
	}

	@Override
	public String getAreaCd() {
		return areaCd;
	}

	@Override
	public void setAreaCd(String areaCd) {
		this.areaCd = areaCd;
	}

	@Override
	public String[] getAreaList() {
		return areaList;
	}

	@Override
	public void setAreaList(String[] areaList) {
		this.areaList = areaList;
	}

	@Override
	public String getLoginId() {
		return loginId;
	}

	@Override
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	@Override
	public String getSubMailAddress() {
		return subMailAddress;
	}

	@Override
	public void setSubMailAddress(String subMailAddress) {
		this.subMailAddress = subMailAddress;
	}

	@Override
	public String getBirthYear() {
		return birthYear;
	}

	@Override
	public void setBirthYear(String birthYear) {
		this.birthYear = birthYear;
	}

	@Override
	public String getBirthMonth() {
		return birthMonth;
	}

	@Override
	public void setBirthMonth(String birthMonth) {
		this.birthMonth = birthMonth;
	}

	@Override
	public String getBirthDay() {
		return birthDay;
	}

	@Override
	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getRePassword() {
		return rePassword;
	}

	@Override
	public void setRePassword(String rePassword) {
		this.rePassword = rePassword;
	}

	@Override
	public String getDispPassword() {
		return dispPassword;
	}

	@Override
	public void setDispPassword(String dispPassword) {
		this.dispPassword = dispPassword;
	}

	public String getAccessCd() {
		return accessCd;
	}

	public void setAccessCd(String accessCd) {
		this.accessCd = accessCd;
	}

	@Override
	public String getSexKbn() {
		return sexKbn;
	}

	@Override
	public void setSexKbn(String sexKbn) {
		this.sexKbn = sexKbn;
	}

	@Override
	public String getPhoneNo1() {
		return phoneNo1;
	}

	@Override
	public void setPhoneNo1(String phoneNo1) {
		this.phoneNo1 = phoneNo1;
	}

	@Override
	public String getPhoneNo2() {
		return phoneNo2;
	}

	@Override
	public void setPhoneNo2(String phoneNo2) {
		this.phoneNo2 = phoneNo2;
	}

	@Override
	public String getPhoneNo3() {
		return phoneNo3;
	}

	@Override
	public void setPhoneNo3(String phoneNo3) {
		this.phoneNo3 = phoneNo3;
	}

	@Override
	public String getPhoneNo() {
		return phoneNo;
	}

	@Override
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	@Override
	public String getZipCd() {
		return zipCd;
	}

	@Override
	public void setZipCd(String zipCd) {
		this.zipCd = zipCd;
	}

	@Override
	public String getPrefecturesCd() {
		return prefecturesCd;
	}

	@Override
	public void setPrefecturesCd(String prefecturesCd) {
		this.prefecturesCd = prefecturesCd;
	}

	@Override
	public String getMunicipality() {
		return municipality;
	}

	@Override
	public void setMunicipality(String municipality) {
		this.municipality = municipality;
	}

	@Override
	public String getAddress() {
		return address;
	}

	@Override
	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String[] getQualification() {
		return qualification;
	}

	@Override
	public void setQualification(String[] qualification) {
		this.qualification = qualification;
	}

	@Override
	public String getFoodExpKbn() {
		return foodExpKbn;
	}

	@Override
	public void setFoodExpKbn(String foodExpKbn) {
		this.foodExpKbn = foodExpKbn;
	}

	@Override
	public String getForeignWorkFlg() {
		return foreignWorkFlg;
	}

	@Override
	public void setForeignWorkFlg(String foreignWorkFlg) {
		this.foreignWorkFlg = foreignWorkFlg;
	}

	@Override
	public String getScoutSelfPr() {
		return scoutSelfPr;
	}

	@Override
	public void setScoutSelfPr(String scoutSelfPr) {
		this.scoutSelfPr = scoutSelfPr;
	}

	@Override
	public String getApplicationSelfPr() {
		return applicationSelfPr;
	}

	@Override
	public void setApplicationSelfPr(String applicationSelfPr) {
		this.applicationSelfPr = applicationSelfPr;
	}

	@Override
	public String getAdvancedRegistrationSelfPr() {
		return advancedRegistrationSelfPr;
	}

	@Override
	public void setAdvancedRegistrationSelfPr(String advancedRegistrationSelfPr) {
		this.advancedRegistrationSelfPr = advancedRegistrationSelfPr;
	}

	@Override
	public String getJobInfoReceptionFlg() {
		return jobInfoReceptionFlg;
	}

	@Override
	public void setJobInfoReceptionFlg(String jobInfoReceptionFlg) {
		this.jobInfoReceptionFlg = jobInfoReceptionFlg;
	}

	@Override
	public String getMailMagazineReceptionFlg() {
		return mailMagazineReceptionFlg;
	}

	@Override
	public void setMailMagazineReceptionFlg(String mailMagazineReceptionFlg) {
		this.mailMagazineReceptionFlg = mailMagazineReceptionFlg;
	}

	@Override
	public String getScoutMailReceptionFlg() {
		return scoutMailReceptionFlg;
	}

	@Override
	public void setScoutMailReceptionFlg(String scoutMailReceptionFlg) {
		this.scoutMailReceptionFlg = scoutMailReceptionFlg;
	}

	@Override
	public String[] getJob() {
		return job;
	}

	@Override
	public void setJob(String[] job) {
		this.job = job;
	}

	@Override
	public String[] getIndustry() {
		return industry;
	}

	@Override
	public void setIndustry(String[] industry) {
		this.industry = industry;
	}

	@Override
	public String[] getWorkLocation() {
		return workLocation;
	}

	@Override
	public void setWorkLocation(String[] workLocation) {
		this.workLocation = workLocation;
	}

	@Override
	public String[] getEmployPtnKbns() {
		return employPtnKbns;
	}

	@Override
	public void setEmployPtnKbns(String[] employPtnKbns) {
		this.employPtnKbns = employPtnKbns;
	}

	@Override
	public String getTransferFlg() {
		return transferFlg;
	}

	@Override
	public void setTransferFlg(String transferFlg) {
		this.transferFlg = transferFlg;
	}

	@Override
	public String getMidnightShiftFlg() {
		return midnightShiftFlg;
	}

	@Override
	public void setMidnightShiftFlg(String midnightShiftFlg) {
		this.midnightShiftFlg = midnightShiftFlg;
	}

	@Override
	public String getSalaryKbn() {
		return salaryKbn;
	}

	@Override
	public void setSalaryKbn(String salaryKbn) {
		this.salaryKbn = salaryKbn;
	}

	@Override
	public String getSalaryKbnName() {
		return salaryKbnName;
	}

	@Override
	public void setSalaryKbnName(String salaryKbnName) {
		this.salaryKbnName = salaryKbnName;
	}

	@Override
	public String getSchoolName() {
		return schoolName;
	}

	@Override
	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	@Override
	public String getDepartment() {
		return department;
	}

	@Override
	public void setDepartment(String department) {
		this.department = department;
	}

	@Override
	public String getGraduationKbn() {
		return graduationKbn;
	}

	@Override
	public void setGraduationKbn(String graduationKbn) {
		this.graduationKbn = graduationKbn;
	}

	@Override
	public List<CareerDto> getCareerList() {
		return careerList;
	}

	@Override
	public void setCareerList(List<CareerDto> careerList) {
		this.careerList = careerList;
	}

	@Override
	public String getJuskillFlg() {
		return juskillFlg;
	}

	@Override
	public void setJuskillFlg(String juskillFlg) {
		this.juskillFlg = juskillFlg;
	}

	@Override
	public String getPcMailStopFlg() {
		return pcMailStopFlg;
	}

	@Override
	public void setPcMailStopFlg(String pcMailStopFlg) {
		this.pcMailStopFlg = pcMailStopFlg;
	}

	@Override
	public String getMobileMailStopFlg() {
		return mobileMailStopFlg;
	}

	@Override
	public void setMobileMailStopFlg(String mobileMailStopFlg) {
		this.mobileMailStopFlg = mobileMailStopFlg;
	}

	@Override
	public String getTerminalKbn() {
		return terminalKbn;
	}

	@Override
	public void setTerminalKbn(String terminalKbn) {
		this.terminalKbn = terminalKbn;
	}

	@Override
	public String getMemberKbn() {
		return memberKbn;
	}

	@Override
	public void setMemberKbn(String memberKbn) {
		this.memberKbn = memberKbn;
	}

	@Override
	public Long getVersion() {
		return version;
	}

	@Override
	public void setVersion(Long version) {
		this.version = version;
	}

	@Override
	public Integer getMemberRegisteredFlg() {
		return memberRegisteredFlg;
	}

	@Override
	public void setMemberRegisteredFlg(Integer memberRegisteredFlg) {
		this.memberRegisteredFlg = memberRegisteredFlg;
	}

	@Override
	public CareerDto newCareerDtoInstance() {
		return new CareerDto();
	}

	@Override
	public String registrationUrl() {
		return registrationUrl;
	}

	@Override
	public void setRegistrationUrl(String registrationUrl) {
		this.registrationUrl = registrationUrl;
	}
}
