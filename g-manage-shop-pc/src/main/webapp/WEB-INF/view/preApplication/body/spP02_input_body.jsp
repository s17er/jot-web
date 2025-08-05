<%@page pageEncoding="UTF-8"%>

<gt:sentenceList name="sentenceList" limitValue="${userDto.customerId}" blankLineLabel="${common['gc.pullDown']}" />
<script type="text/javascript" src="${SHOP_CONTENS}/js/setAjax.js"></script>
<script type="text/javascript">
<!--

	function selectPattern() {

		if ($("#patternId").val() == "") {
			alert("定型文を選択してください。");
			return;
		}

		setAjaxPartsWithAfter('${f:url(selectPatternAjaxPath)}', $("#patternId").val(), sendPattern);
		setAjaxPartsWithAfter('${f:url(selectTitleAjaxPath)}', $("#patternId").val(), sendTitle);

	};

	function sendPattern(str) {
		if (str == "errorSentence") {
			alert("定型文が削除されている可能性があります。");
		} else if (str == "errorNoSelect") {
			alert("定型文を選択してください。");
		} else if (str == "errorAddUrl") {
			alert("現在公開中の求人原稿はありません。");
		} else if (str == "errorMember") {
			alert("会員名の取得に失敗しました。");
		} else {
			$('#mailBody').val(str);
		}
	};

	function sendTitle(str) {
		if (str == "errorSentence") {
			alert("定型文が削除されている可能性があります。");
		} else if (str == "errorNoSelect") {
			alert("定型文を選択してください。");
		} else if (str == "errorAddUrl") {
			alert("現在公開中の求人原稿はありません。");
		} else if (str == "errorMember") {
			alert("会員名の取得に失敗しました。");
		} else {
			$('input[name="subject"]').val(str);
		}
	};

	$.fn.extend({
	    insertAtCaret: function(v) {
	      var o = this.get(0);
	      o.focus();
	      if (jQuery.browser.msie) {
	        var r = document.selection.createRange();
	        r.text = v;
	        r.select();
	      } else {
	        var s = o.value;
	        var p = o.selectionStart;
	        var np = p + v.length;
	        o.value = s.substr(0, p) + v + s.substr(p);
	        o.setSelectionRange(np, np);
	      }
	    }
	});

	/**
	 * 会員詳細を別ウィンドウで開く
	 */
	var windowArray = new Object();
	function detailWindow(url, id) {
		windowArray[id] = window.open(url, id,'width=800,height=700,scrollbars=yes');
	}

	/**
	 * 画面のunload時に開いている会員詳細ウィンドウを閉じるイベント待ちfunction
	 */
	$(function(){
		$(window).unload(function(ev){
			for(var key in  windowArray){
				windowArray[key].close();
			}
		});
	});

// -->
</script>

<!-- #main# -->
<div id="main">

<h2 title="${f:h(pageTitle)}" class="title" id="${f:h(pageTitleId)}" >${f:h(pageTitle) }</h2>

<hr />

<p>${f:h(defaultMsg)}</p>

<html:errors />
<hr />

<c:if test="${existDataFlg}">


<s:form action="${f:h(actionPath)}">

<div id="wrap_form">
<table cellpadding="0" cellspacing="0" border="0" class="cmn_table detail_table">
	<tr>
		<th width="150">宛先</th>
		<td>
			<c:forEach var="m" items="${applicationId}" varStatus="status">
			<c:set var="url" value="${f:url(gf:makePathConcat1Arg( '/application/detail/subApplicationDetail', m))}" scope="page" />
			<c:set var="name" value="${fromName[status.index]}" />
				${f:h(name)}&nbsp;
			</c:forEach>
		</td>
	</tr>

	<tr>
		<th>件名</th>
		<td><html:text property="subject" /></td>
	</tr>
	<tr>
		<th class="bdrs_bottom">本文</th>
		<td class="bdrs_bottom"><div class="clear">
		<div class="wrap_ptnSelect">
			<html:select property="sentenceId"  styleId="patternId" >
				<html:optionsCollection name="sentenceList"/>
			</html:select>
		<html:button property="" value="　" onclick="selectPattern(); return false;" styleId="btn_selectPattern" /></div>
		</div>
		<html:textarea property="mailBody" cols="60" rows="10" styleId="mailBody"></html:textarea>
		</td>
	</tr>

</table>
<hr />

<div class="wrap_btn">
<html:submit property="conf" styleId="btn_conf"  value="　"  />
<html:submit property="back" value="　" styleId="btn_back" />


</div>

</div>
</s:form>

</c:if>

</div>
<!-- #main# -->
<hr />
<jsp:include page="/WEB-INF/view/common/backgroundAccess_js.jsp"></jsp:include>