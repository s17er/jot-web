<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants" %>

<gt:areaList name="areaList" blankLineLabel="--"/>
<gt:prefecturesList name="prefecturesList" blankLineLabel="--" />
<gt:typeList name="employPtnList" typeCd="<%=MTypeConstants.EmployPtnKbn.TYPE_CD %>"  blankLineLabel="--"  />
<gt:typeList name="industryList" typeCd="<%=MTypeConstants.IndustryKbn.TYPE_CD %>"  blankLineLabel="--"  />
<gt:typeList name="jobList" typeCd="<%=MTypeConstants.JobKbn.TYPE_CD %>" />
<gt:typeList name="sexList" typeCd="<%=MTypeConstants.Sex.TYPE_CD %>"  blankLineLabel="--"  />
<gt:typeList name="juskillList" typeCd="<%=MTypeConstants.JuskillFlg.TYPE_CD %>"  blankLineLabel="--"  />
<gt:typeList name="mRegisterFlgList" typeCd="<%=MTypeConstants.MemberRegisteredFlg.TYPE_CD %>" blankLineLabel="--" />


<link rel="stylesheet" type="text/css" href="${ADMIN_CONTENS}/css/jquery/gourmet.datepicker.css" />
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/ui.datepicker.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/ui.datepicker-ja.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/changeMaxRow.js"></script>
<script type="text/javascript">
<!--

	// 「DatePicker」の搭載
	$(function(){
		$("#start_ymd").datepicker();
		$("#end_ymd").datepicker();
	});



// -->
</script>
<!-- #main# -->
<div id="main">

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />

	<h2 class="title member">${f:h(pageTitle1)}</h2>
	<hr />

	<p class="btm_margin">${f:h(defaultMsg)}</p>
	<hr />

	<html:errors/>

				<html:messages id="msg" message="true">
				<div class="message">
					<ul>
						<li>
			  				<bean:write name="msg" ignore="true"   />
			  			</li>
					</ul>
				</div>
				</html:messages>


	<!-- #wrap_serch# -->
	<div id="wrap_search">
		<s:form action="${f:h(actionPath)}">
			<table cellpadding="0" cellspacing="1" border="0" class="search_table">
				<tr>
					<th colspan="4" class="td_title">検索</th>
				</tr>
				<tr>
					<th>ID</th>
    				<td class="release"><html:text property="where_id" size="10" styleClass="disabled" /></td>
					<th></th>
					<td></td>
				</tr>
				<tr>
					<th>氏名</th>
					<td><html:text property="where_name" size="40" /></td>
					<th>フリガナ</th>
					<td><html:text property="where_nameKana" size="40" /></td>
				</tr>
				<tr>
					<th>エリア</th>
					<td>
						<html:select property="where_areaCd"  >
							<html:optionsCollection name="areaList"/>
						</html:select>
					</td>
					<th>都道府県</th>
					<td>
						<html:select property="where_prefecturesCd"  >
							<html:optionsCollection name="prefecturesList"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<th>希望業種</th>
					<td>
						<html:select property="where_industryCd"  >
							<html:optionsCollection name="industryList"/>
						</html:select>
					</td>
					<th>雇用形態</th>
					<td>
						<html:select property="where_empPtnKbn"  >
							<html:optionsCollection name="employPtnList"/>
						</html:select>
					</td>
				</tr>
				<tr>
					<th>性別</th>
					<td>
						<html:select property="where_sexKbn"  >
							<html:optionsCollection name="sexList"/>
						</html:select>
					</td>
					<th>年齢</th>
					<td class="release">
						<html:text property="where_lowerAge" size="3" maxlength="2" styleClass="disabled" />
						&nbsp;～&nbsp;
						<html:text property="where_upperAge" size="3" maxlength="2" styleClass="disabled" />
					</td>
				</tr>
				<tr>
					<th>登録日</th>
					<td class="release">
						<html:text property="where_fromRegistratedDate" size="12" styleId="start_ymd" maxlength="10" styleClass="disabled" />
						&nbsp;～&nbsp;
						<html:text property="where_toRegistratedDate" size="12" styleId="end_ymd" maxlength="10" styleClass="disabled" />
					</td>
					<th>登録状況</th>
					<td class="release">
						<html:select property="where_memberRegisteredFlg">
							<html:optionsCollection name="mRegisterFlgList" />
						</html:select>
					</td>
				</tr>
				<tr>
					<th>メールアドレス</th>
					<td class="release"  colspan="3"><html:text property="where_mail" size="45" styleClass="disabled" /></td>
				</tr>
			</table>
			<hr />
			<div class="wrap_btn">
				<html:submit property="search" value="検 索" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
			</div>
		</s:form>
	</div>
	<!-- #wrap_serch# -->
	<hr />

<c:if test="${fn:length(memberInfoDtoList) gt 0}">
	<!-- #wrap_result# -->

	<jsp:include page="/WEB-INF/view/member/include/apH03_list_include.jsp"></jsp:include>

	<!-- #wrap_result# -->
</c:if>
	<hr />
	<br />

	<!-- #navigation# -->
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
	</ul>
	<!-- #navigation# -->
	<hr />
</div>
<!-- #main# -->
