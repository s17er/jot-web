<%@page import="com.gourmetcaree.common.constants.LabelConstants"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants"%>
<%@page pageEncoding="UTF-8"%>
<c:set var="SSL_DOMAIN" value="${common['gc.sslDomain']}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>事前登録印刷画面</title>
<link rel="stylesheet" type="text/css" href="${ADMIN_CONTENS}/css/reset.css" />
<link rel="stylesheet" type="text/css" href="${ADMIN_CONTENS}/css/fairEntryForm.css" />
<style type="">
@media print {
	div.print {
		height: auto !IMPORTANT;
		width: auto !important;
	}
	div.pageBreak {
		page-break-before: always;
	}
	h1 {
		margin: 10px 0 10px;
	}
}

h1 {
	margin: 5px 0 10px;
}
</style>
</head>

<gt:prefecturesList name="prefecturesList" />
<c:set var="TERMINAL_KBN_PC" value="<%=MTypeConstants.TerminalKbn.PC_VALUE%>" scope="page" />
<gt:typeList name="qualificationList" typeCd="<%=MTypeConstants.QualificationKbn.TYPE_CD%>" />
<gt:typeList name="graduationKbnList" typeCd="<%=MTypeConstants.GraduationKbn.TYPE_CD%>" />
<gt:typeList name="jobList" typeCd="<%=MTypeConstants.JobKbn.TYPE_CD%>" />
<gt:typeList name="sexList" typeCd="<%=MTypeConstants.Sex.TYPE_CD %>" />
<gt:typeList name="industryList" typeCd="<%=MTypeConstants.IndustryKbn.TYPE_CD %>" />
<gt:typeList name="possibleTermList" typeCd="<%=MTypeConstants.PossibleEntryTermKbn.TYPE_CD %>" />
<gt:typeList name="currentSituationList" typeCd="<%=MTypeConstants.CurrentSituationKbn.TYPE_CD %>" />
<gt:typeList name="qualificationList" typeCd="<%=MTypeConstants.QualificationKbn.TYPE_CD %>" />

<body>
	<div id="wrapper">
		<c:forEach items="${printMemberDtoList}" var="dto" varStatus="dtoStatus">
			<c:choose>
				<c:when test="${dtoStatus.first}">
					<c:set var="className" value="" scope="page" />
				</c:when>
				<c:otherwise>
					<c:set var="className" value="pageBreak" scope="page" />
				</c:otherwise>
			</c:choose>

			<div class="print ${f:h(className)}">
				<h1>${dto.advancedRegistrationName}&nbsp;エントリーカード</h1>
				<p>エントリーカードのタイミングは各ブースの案内に従ってください。</p>
                <p style="text-align:right;">登録ID:<span>${f:h(dto.id)}</span>&nbsp;&nbsp;&nbsp;${f:label(dto.currentSituationKbn, currentSituationList, 'value', 'label')}</p>

				<table class="entryCardPrint" cellpadding="0" cellspacing="1">

					<tr>
						<th class="w150">フリガナ</th>
				        <td class="w255">${f:h(dto.memberNameKana)}</td>
						<th rowspan="2" class="w75">年令</th>
				        <td rowspan="2" class="w75">${f:h(dto.age)}才</td>
						<th rowspan="2" class="w75">性別</th>
				        <td rowspan="2" class="w180">
				        	&nbsp;&nbsp;
							<c:forEach items="${sexList}" var="t" varStatus="status">
								<c:choose>
									<c:when test="${dto.sexKbn eq t.value}">
										<c:set var="sexCheck" value="checked=\"checked\"" scope="page" />
									</c:when>
									<c:otherwise>
										<c:set var="sexCheck" value="" scope="page" />
									</c:otherwise>
								</c:choose>
								<input type="checkbox"   ${f:h(sexCheck)} />&nbsp;${f:h(t.label)}
								<c:if test="${status.last eq false}">
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
								</c:if>
							</c:forEach>
				        </td>
				    </tr>



					<tr>
						<th class="w150">名前</th>
				        <td class="w255">${f:h(dto.memberName)}</td>
				    </tr>

				    <tr>
				    	<th class="w150">住所</th>
				        <td colspan="5" class="w660"><!--
				        	<c:if test="${not empty dto.zipCd}">
								 -->（〒${f:h(dto.zipCd)}）&nbsp;<!--
							</c:if>
							 -->${f:label(dto.prefecturesCd, prefecturesList, 'value', 'label') } ${f:h(dto.municipality)}${f:h(dto.address)}
				        </td>
				    </tr>

				    <tr>
				        <th class="w150">電話番号</th>
				        <td colspan="5" class="w660">${f:h(dto.phoneNo)}</td>
				    </tr>

				    <tr>
				    	<th class="w150">メールアドレス</th>
				        <td colspan="5" class="w660">${f:h(dto.loginId)}</td>
				    </tr>

				    <tr>
				    	<th class="w150">最終学歴</th>
				        <td class="w255" colspan="2">学校名：${f:h(dto.schoolName)}&nbsp;${f:h(dto.department)}</td>
				        <td colspan="3" class="w405">${f:label(dto.graduationKbn, graduationKbnList, 'value', 'label')}</td>
				    </tr>
				</table>
				<br />

				<table class="entryCardPrint" cellpadding="0" cellspacing="1">
					<tr>
				    	<th class="w150">希望する業態</th>
				        <td class="w255">
				        	<c:forEach items="${dto.industryKbnList}" var="t" varStatus="status">

								${f:label(t, industryList, 'value', 'label')}
								<c:if test="${status.last eq false}">
									,&nbsp;
								</c:if>
							</c:forEach>
				        </td>
				    	<th class="w150">希望する職種・ポジション</th>
				        <td class="w255">
				        	<c:forEach items="${dto.jobKbnList}" var="t" varStatus="status">

								${f:label(t, jobList, 'value', 'label')}
								<c:if test="${status.last eq false}">
									,&nbsp;
								</c:if>
							</c:forEach>
				        </td>
				    </tr>
					<tr>
				    	<th class="w150">転職希望時期</th>
				        <td colspan="3" class="w660">
				        	${f:h(dto.hopeCareerChangeStr)}
				        	<c:if test="${not empty dto.hopeCareerChangeTermOther}">
				        	 ：${f:h(dto.hopeCareerChangeTermOther)}
				        	</c:if>
				        </td>
				    </tr>
					<tr>
				    	<th class="w150">経歴や資格など、PRがあればご自由にご記入ください。</th>
				        <td class="w255">${f:br(f:h(dto.advancedRegistrationSelfPr))}</td>
				        <th class="w150">転職先に望むことがあればご自由にご記入ください。</th>
				        <td class="w255">${f:br(f:h(dto.hopeCareerChangeText))}</td>
				    </tr>
					<tr>
				    	<th class="w150">取得資格</th>
				        <td colspan="3" class="w660">
				        	<c:forEach items="${dto.qualificationKbnList}" var="t" varStatus="status">
								${f:label(t, qualificationList, 'value', 'label')}
								<c:if test="${status.last eq false}">
									,&nbsp;
								</c:if>
							</c:forEach>
				        	<c:if test="${not empty dto.qualificationOther}">
				        	 ：${f:h(dto.qualificationOther)}
				        	</c:if>
				        </td>
				    </tr>
				</table>

				<h2>■職務経歴</h2>
				<c:forEach items="${dto.careerDtoList}" var="careerDto" varStatus="careerStatus">
					<p>＜職歴${f:h(careerStatus.count)}＞</p>
					<table class="entryCardPrint" cellpadding="0" cellspacing="1">
						<tr>
					    	<th class="w150">会社名（店名）</th>
					        <td class="w255">${f:h(careerDto.companyName)}</td>
					    	<th class="w150">在籍期間</th>
					        <td class="w255">${f:h(careerDto.workTerm)}</td>
					    </tr>

					    <tr>
					    	<th class="w150">職種・ポジション</th>
					        <td  class="w255">
					        	<c:forEach items="${careerDto.job}" var="t" varStatus="jobStatus">
									${f:label(t, jobList, 'value', 'label')}
									<c:if test="${jobStatus.last eq false}">
										,&nbsp;
									</c:if>
								</c:forEach>
					        </td>
					        <th class="w150">業態</th>
					        <td  class="w255">
					        	<c:forEach items="${careerDto.industry}" var="t" varStatus="indStatus">
									${f:label(t, industryList, 'value', 'label')}
									<c:if test="${indStatus.last eq false}">
										,&nbsp;
									</c:if>
								</c:forEach>
					        </td>
					    </tr>

					    <tr>
					    	<th class="w150">業務内容</th>
					        <td colspan="3" class="w660">${f:br(f:h(careerDto.businessContent))}</td>
					    </tr>

					    <tr>
					    	<th class="w150">客席数・坪数</th>
					        <td class="w255">${f:h(careerDto.seat)}</td>
					    	<th class="w150"><c:out value="<%=LabelConstants.Member.EARNINGS %>" /></th>
					        <td class="w255">${f:h(careerDto.monthSales)}</td>
					    </tr>
					</table>
				</c:forEach>


				<br />
				<table class="entryCardPrint"   cellpadding="0" cellspacing="1">
					<tr>
						<th class="w150">現在（前職）の年収</th>
        				<td class="w660">${f:h(dto.workSalary)}</td>
					</tr>
				</table>
				<p class="footer">◆カードがなくなりましたら受付にて新しいカードをお受け取りください。<br />◆訪問した企業にこのカードを1枚ずつ渡してください。お渡し頂いた個人情報は各社規定の取扱方針に準拠します。<br />◆ご記入頂いた個人情報は厳重に管理し、次回のご案内をさせて頂く以外の目的には一切使用致しません。<br />&nbsp;&nbsp;&nbsp;個人情報の訂正・抹消、サービスの提供停止をご希望の場合は、下記窓口までご連絡ください。</p>
				<p class="footer">＜お問合せ先＞<br />株式会社ジェイオフィスグループ<br />【首都圏】 TEL:03-5524-2239 【関西、東海・北陸、九州・沖縄】 TEL:06-6476-8363<br />※開催地域によりお問い合わせ先が異なります。</p>
			</div>
		</c:forEach>
	</div>
</body>
</html>
