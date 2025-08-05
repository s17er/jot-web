<%@page import="com.gourmetcaree.shop.pc.application.form.applicationMail.ApplicationMailForm"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.db.common.constants.MAreaConstants"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="ja" xml:lang="ja">
<head>
	<tiles:insert page="../common/commonTracking.jsp" />
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<meta http-equiv="content-script-type" content="text/javascript" />
	<meta http-equiv="content-style-type" content="text/css" />
	<meta http-equiv="Pragma" content="no-cache" />
	<meta http-equiv="Cache-Control" content="no-cache" />
	<meta http-equiv="Expires" content="0" />
	<meta name="robots" content="noindex" />
	<meta name="Description" content="グルメキャリーweb 店舗側管理システム" />
	<meta name="Author" content="Joffice Inc." />

	<%	/* CSSファイルを設定 */	 %>
	<link rel="stylesheet" type="text/css" href="${SHOP_CONTENS}/css/shared.css" />

	<c:choose>
		<c:when test="${userDto.areaCd eq AREA_KANSAI}">
		<link rel="stylesheet" type="text/css" href="${SHOP_CONTENS}/css/kansai.css" />
		</c:when>
		<%-- ログイン前などエリアを特定できない場合 --%>
		<c:otherwise>
		<link rel="stylesheet" type="text/css" href="${SHOP_CONTENS}/css/kansai.css" />
		</c:otherwise>
	</c:choose>
</head>
<body>
<!-- Google Tag Manager (noscript) --> <noscript><iframe src="https://www.googletagmanager.com/ns.html?id=GTM-NCVX9X2" height="0" width="0" style="display:none;visibility:hidden"></iframe></noscript> <!-- End Google Tag Manager (noscript) -->
	<gt:typeList name="sexList" typeCd="<%=MTypeConstants.Sex.TYPE_CD %>"/>

	<html:errors />

	<c:if test="${existDataFlg}">

		<div id="entry_profile" class="wrap_detail">
		<h3 title="応募者プロフィール" id="profile">応募者プロフィール</h3>
				<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
					<tr>
						<th>応募日時</th>
						<td><fmt:formatDate value="${applicationDatetime}" pattern="yyyy/MM/dd HH:mm" /></td>
					</tr>
					<tr>
						<th width="150">応募ID</th>
						<td>${f:h(id)} &nbsp;</td>
					</tr>
					<tr>
						<th>氏名(漢字）</th>
						<td>${f:h(name)} &nbsp;</td>
					</tr>
					<tr>
						<th>氏名(カナ）</th>
						<td>${f:h(nameKana)} &nbsp;</td>
					</tr>
						<tr>
						<th>性別</th>
						<td>${f:label(sexKbn, sexList, 'value', 'label')} &nbsp;</td>
					</tr>
						<tr>
						<th>年齢</th>
						<td>${f:h(age)}才 &nbsp;</td>
					</tr>
					<tr>
						<th>電話番号</th>
						<td>${f:h(phoneNo)} &nbsp;</td>
					</tr>
					<tr>
						<th>PCメールアドレス</th>
						<td>${f:h(pcMail)} &nbsp;</td>
					</tr>
					<tr>
						<th class="bdrs_bottom">携帯メールアドレス</th>
						<td class="bdrs_bottom">${f:h(mobileMail)} &nbsp;</td>
					</tr>
				</table>
		</div>
</c:if>

</body>
</html>

