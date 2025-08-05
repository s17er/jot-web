<%@page pageEncoding="UTF-8"%>
<%@page import="com.gourmetcaree.common.constants.GourmetCareeConstants"%>
<%@page import="com.gourmetcaree.common.constants.GeneralPropertiesKey"%>
<c:set var="GOOGLE_MAP_PROP_KEY" value="<%=GeneralPropertiesKey.GOOGLE_MAP_API_KEY %>" scope="request" />
<c:set var="JOT_LATITUDE" value="<%= GourmetCareeConstants.JOT_LATITUDE %>" />
<c:set var="JOT_LONGITUDE" value="<%= GourmetCareeConstants.JOT_LONGITUDE %>" />

<link rel="stylesheet" type="text/css" href="${SHOP_CONTENS}/css/jquery/jquery-ui.css" />
<link rel="stylesheet" type="text/css" href="${SHOP_CONTENS}/css/jquery/jquery.tagit.css" />
<link rel="stylesheet" type="text/css" href="${SHOP_CONTENS}/css/webdata.css" />
<script type="text/javascript" src="${SHOP_CONTENS}/js/jquery/jquery-ui.js"></script>
<script type="text/javascript" src="${SHOP_CONTENS}/js/jquery/ui.core.js"></script>
<script type="text/javascript" src="${SHOP_CONTENS}/js/jquery/tag-it.min.js"></script>

<link rel="stylesheet" type="text/css" href="${SHOP_CONTENS}/js/lightbox/css/lightbox.min.css" />
<script type="text/javascript" src="${SHOP_CONTENS}/js/lightbox/js/lightbox.min.js"></script>
<script type="text/javascript" src="${SHOP_CONTENS}/js/jquery/jquery.leanModal.min.js"></script>
<script type="text/javascript" src="${SHOP_CONTENS}/js/checkConfFlg.js"></script>
<script type="text/javascript" src="${SHOP_CONTENS}/js/promise-7.0.4.min.js"></script>

