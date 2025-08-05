<%@page pageEncoding="UTF-8"%>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/checkConfFlg.js"></script>

<c:set var="HTML" value="<%=MTypeConstants.deliveryTypeKbn.HTML%>" />
<c:set var="TEXT" value="<%=MTypeConstants.deliveryTypeKbn.TEXT%>" />
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants"%>
<!-- #main# -->
<div id="main">
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<c:if test="${not empty navigationPath2}">
			<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
		</c:if>
	</ul>

	<hr />
	<html:errors/>

	<h2 class="title application">${f:h(pageTitle1)}</h2>

	<c:if test="${existDataFlg eq true }">
		<s:form action="${f:h(actionPath)}" enctype="multipart/form-data">
		<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table" id="webdata_table">
			<c:if test="${pageKbn eq PAGE_DETAIL}">
				<tr>
					<th width="150">メルマガID</th>
					<td>${f:h(mailMagazineId)}
				</tr>
			</c:if>
			<tr>
				<th>
					配信予定日
				</th>
				<td>
					${f:h(deliveryScheduleDatetime)}
				</td>
			</tr>
			<tr>
				<th width="150">
					配信エリア
				</th>
				<td>
					<gt:areaList name="areaList" />
	                <c:forEach items="${areaCd}" var="t" varStatus="loop">
						<c:if test="${!loop.first}">
						、
						</c:if>
						${f:h(f:label(t, areaList, 'value', 'label'))}
	                </c:forEach>
				</td>
			</tr>
			<tr>
				<th>配信形式</th>
				<td>
					<gt:typeList name="deliveryTypeKbnList" typeCd="<%=MTypeConstants.deliveryTypeKbn.TYPE_CD %>"/>
					${f:h(f:label(deliveryType, deliveryTypeKbnList, 'value', 'label'))}
				</td>
			</tr>
			<tr>
				<th class="bdrs_bottom">
					ヘッダメッセージ
				</th>
				<td class="bdrs_bottom">
			<c:choose>
				<c:when test="${pageKbn eq PAGE_INPUT or pageKbn eq PAGE_EDIT}">
					<c:choose>
						<c:when test="${deliveryType eq HTML }">
							${htmlBody}
						</c:when>
						<c:when test="${deliveryType eq TEXT }">
							${f:br(f:h(textBody))}
						</c:when>
					</c:choose>
				</c:when>
				<c:when test="${pageKbn eq PAGE_DETAIL or pageKbn eq PAGE_DELETE}">
					<c:choose>
						<c:when test="${deliveryType eq HTML }">
							${optionValue}
						</c:when>
						<c:when test="${deliveryType eq TEXT }">
							${f:br(f:h(optionValue))}
						</c:when>
					</c:choose>
				</c:when>
			</c:choose>
				</td>
			</tr>
		</table>


		<div class="wrap_btn">
			<c:choose>
				<c:when test="${pageKbn eq PAGE_INPUT}">
					<html:submit property="submit" value="登録"/>
					<html:submit property="correct" value="戻る" />
				</c:when>
				<c:when test="${pageKbn eq PAGE_DETAIL}">
					<c:if test="${editFlg eq true}">
						<html:button property="submit" value="編集" onclick="location.href='${f:url(gf:makePathConcat1Arg('/mailMagOption/headerEdit/index', id))}'"/>
						<html:button property="submit" value="削除" onclick="location.href='${f:url(gf:makePathConcat1Arg('/mailMagOption/headerDelete/index', id))}'"/>
					</c:if>
					<html:submit property="backToList"  value="戻る"/>
					<c:if test="${editFlg eq false}">
						<html:submit property="outPut" value="CSV出力" onclick="if(!confirm('CSVを出力しますか?')) {return false;};" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
						<html:hidden property="id" />
						<html:hidden property="mailMagazineId" />
					</c:if>
				</c:when>
				<c:when test="${pageKbn eq PAGE_EDIT}">
					<html:submit property="submit" value="更新"/>
					<html:submit property="correct" value="戻る" />
				</c:when>
				<c:when test="${pageKbn eq PAGE_DELETE}">
					<html:hidden property="id"/>
					<input type="button" value="削 除" onclick="deleteConf('deleteProcessFlg', 'deleteForm');" onmousedown="downBtn(this);" onmouseup="upBtn(this);" onmouseout="outBtn(this);"  />
					<html:submit property="backToDetail" value="詳細へ戻る"/>
				</c:when>
			</c:choose>

		</div>
		</s:form>
		<c:if test="${pageKbn eq PAGE_DELETE}">
			<s:form action="${f:h(deleteActionPath)}" styleId="deleteForm">
				<html:hidden property="processFlg" styleId="deleteProcessFlg"/>
				<html:hidden property="id"/>
				<html:hidden property="version"/>
			</s:form>
		</c:if>
	</c:if>
	<ul class="navigation clear">
		<li><a href="${f:url(navigationPath1)}">${f:h(navigationText1)}</a></li>
		<c:if test="${not empty navigationPath2}">
			<li><a href="${f:url(navigationPath2)}">${f:h(navigationText2)}</a></li>
		</c:if>
	</ul>
</div>
<!-- #main# -->