<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="html" uri="http://struts.codelibs.org/tags-html"%>
<%@taglib prefix="bean" uri="http://struts.apache.org/tags-bean"%>
<%@taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>
<%@taglib prefix="tiles" uri="http://jakarta.apache.org/struts/tags-tiles"%>
<%@taglib prefix="s" uri="http://sastruts.seasar.org"%>
<%@taglib prefix="f" uri="http://sastruts.seasar.org/functions"%>
<%@taglib prefix="gt" uri="http://www.gourmetcaree.com/gourmetcaree-tag"%>
<%@taglib prefix="gf" uri="http://www.gourmetcaree.com/gourmetcaree-func"%>
<%@taglib prefix="ar" uri="http://www.g-de-b.com/arbeit-tag"%>
<%@ page trimDirectiveWhitespaces="true" %>

<c:set var="SITE_NAME" value="${common['gc.siteName']}" />
<%//定義jspのpaegKbnセットする定義  %>
<c:set var="PAGE_INPUT" value="input" />
<c:set var="PAGE_EDIT" value="edit" />
<c:set var="PAGE_DELETE" value="delete" />
<c:set var="PAGE_DETAIL" value="detail" />
<c:set var="PAGE_LIST" value="list" />
<c:set var="PAGE_MENU" value="menu" />
<c:set var="PAGE_OTHER" value="other" />
<c:set var="APACHE_HOME" value="${common['gourmetcaree.apacheHomePath']}" scope="request" />
<c:set var="AUTH_LEVEL_ADMIN" value="1" />
<c:set var="AUTH_LEVEL_STAFF" value="2" />
<c:set var="AUTH_LEVEL_AGENCY" value="3" />
<c:set var="AUTH_LEVEL_OTHER" value="4" />
<c:set var="AUTH_LEVEL_SALES" value="5" />
<c:set var="SHUTOKEN_AREA_CD" value="3" />
<c:set var="AUTH_LEVEL_NONE" value="99" />
<c:set var="REQUEST_DOMAIN" value="<%= request.getServerName() %>" />
<c:set var="REQUEST_HTTP" value="<%= request.getScheme() %>" />
<c:set var="ADMIN_CONTENS" value="${REQUEST_HTTP}://${REQUEST_DOMAIN}/adminContent" />
