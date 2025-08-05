<%@page import="com.gourmetcaree.common.constants.GourmetCareeConstants"%>
<%@page import="com.gourmetcaree.common.constants.GeneralPropertiesKey"%>
<%@page import="com.gourmetcaree.db.common.constants.MTypeConstants"%>
<c:set var="SIZE_TEXT" value="<%=MTypeConstants.SizeKbn.TEXT_WEB %>" scope="request"/>
<c:set var="SIZE_A" value="<%=MTypeConstants.SizeKbn.A %>" scope="request"/>
<c:set var="SIZE_B" value="<%=MTypeConstants.SizeKbn.B %>" scope="request"/>
<c:set var="SIZE_C" value="<%=MTypeConstants.SizeKbn.C %>" scope="request"/>
<c:set var="SIZE_D" value="<%=MTypeConstants.SizeKbn.D %>" scope="request"/>
<c:set var="SIZE_E" value="<%=MTypeConstants.SizeKbn.E %>" scope="request"/>
<c:set var="GOOGLE_MAP_PROP_KEY" value="<%=GeneralPropertiesKey.GOOGLE_MAP_API_KEY %>" scope="request" />

<c:set var="JOT_LATITUDE" value="<%= GourmetCareeConstants.JOT_LATITUDE %>" />
<c:set var="JOT_LONGITUDE" value="<%= GourmetCareeConstants.JOT_LONGITUDE %>" />

<% /* CSSファイルを設定 */ %>
<link rel="stylesheet" type="text/css" href="${ADMIN_CONTENS}/js/lightbox/css/lightbox.min.css" />
<link rel="stylesheet" type="text/css" href="${ADMIN_CONTENS}/js/fancybox/jquery.fancybox.css" media="screen" />
<% /* javascriptファイルを設定 */ %>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/jquery/ui.core.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/lightbox/js/lightbox.min.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/fancybox/jquery.fancybox.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/checkConfFlg.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/preview.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/setAjax.js"></script>
<script type="text/javascript" src="${ADMIN_CONTENS}/js/focus.js"></script>

<script>

$(function(){

	changeWebSize(${sizeKbn});
});

// 表示切替のテンプレ
var sizeDisplay = {
	'main1Img_tr'				: [${SIZE_A}, ${SIZE_B}, ${SIZE_C}, ${SIZE_D}, ${SIZE_E}],
	'main2Img_tr'				: [${SIZE_D}, ${SIZE_E}],
	'main3Img_tr'				: [${SIZE_D}, ${SIZE_E}],
	'rightImg_tr'				: [${SIZE_D}, ${SIZE_E}],
	'photoAImg_tr'				: [${SIZE_C}, ${SIZE_D}, ${SIZE_E}],
	'photoBImg_tr'				: [${SIZE_C}, ${SIZE_D}, ${SIZE_E}],
	'photoCImg_tr'				: [${SIZE_C}, ${SIZE_D}, ${SIZE_E}],
	'freeImg_tr'				: [${SIZE_D}, ${SIZE_E}],
	'movieListDisplayFlg_tr'	: [${SIZE_D}, ${SIZE_E}],
	'sentence1_tr'				: [${SIZE_A}, ${SIZE_B}, ${SIZE_C}, ${SIZE_D}, ${SIZE_E}, ${SIZE_TEXT}],
	'sentence2_tr'				: [${SIZE_C}, ${SIZE_D}, ${SIZE_E}],
	'sentence3_tr'				: [${SIZE_D}, ${SIZE_E}],
	'captionA_tr'				: [${SIZE_C}, ${SIZE_D}, ${SIZE_E}],
	'captionB_tr'				: [${SIZE_C}, ${SIZE_D}, ${SIZE_E}],
	'captionC_tr'				: [${SIZE_C}, ${SIZE_D}, ${SIZE_E}],
	'attentionHereImg_tr'		: [${SIZE_B}, ${SIZE_C}, ${SIZE_D}, ${SIZE_E}],
	'attentionHereTitle'		: [${SIZE_B}, ${SIZE_C}, ${SIZE_D}, ${SIZE_E}],
	'attentionHereSentence_tr'	: [${SIZE_B}, ${SIZE_C}, ${SIZE_D}, ${SIZE_E}]
};

// サイズ変更処理
function changeWebSize(sizeKbn) {
	if (!sizeKbn) {
		return;
	}
    $.each( this.sizeDisplay, function( key, value ) {
		if ($.inArray(sizeKbn - 0, value) >= 0){
			  $('#' + key).show();
		} else {
			  $('#' + key).hide();
		}
    });
}

$("[data-fancybox]").fancybox({
	 arrows : false,
	 modal  : true,
});

</script>



