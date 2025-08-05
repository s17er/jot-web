<%@page pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="${SHOP_CONTENS}/css/scout.css" />
<gt:sentenceList name="sentenceList" limitValue="${userDto.customerId}" blankLineLabel="定型文を選択" />

<script type="text/javascript" src="${SHOP_CONTENS}/js/setAjax.js"></script>
<c:if test="${interestFlg eq true}">
	<script type="text/javascript" src="${SHOP_CONTENS}/js/modal_box.js"></script>
</c:if>
<script type="text/javascript">
<!--

	$(function(){
		var submitFlg = true;
		$("#btn_conf").on('click', function(){
			if(submitFlg) {
				submitFlg = false;
				return ture;
			} else {
				return false;
			}
		});
	});

	function selectPattern() {

		setAjaxPartsWithAfter('${f:url(selectPatternAjaxPath)}', $("#patternId").val(), sendPattern);
		setAjaxPartsWithAfter('${f:url(selectTitleAjaxPath)}', $("#patternId").val(), sendTitle);
	};

	function addUrl() {
		setAjaxPartsWithAfter('${f:url(addUrlAjaxPath)}', '', addPattern);
	};

	function addPattern(str) {
		if (str == "errorSentence") {
			alert("定型文が削除されている可能性があります。");
		} else if (str == "errorNoSelect") {
			alert("定型文を選択してください。");
		} else if (str == "errorAddUrl") {
			alert("現在公開中の求人原稿はありません。");
		} else {
			insert_textarea($('#mailBody'), str);
		}
	}

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

	function insert_textarea(target, str) {
	  var obj = $(target);
	  obj.focus();
	  if(navigator.userAgent.match(/MSIE/)) {
	    var r = document.selection.createRange();
	    r.text = str;
	    r.select();
	  } else {
	    var s = obj.val();
	    var p = obj.get(0).selectionStart;
	    var np = p + str.length;
	    obj.val(s.substr(0, p) + str + s.substr(p));
	    obj.get(0).setSelectionRange(np, np);
	  }
	}

	function sendPattern(str) {
		if (str == "errorSentence") {
			alert("定型文が削除されている可能性があります。");
		} else if (str == "errorNoSelect") {
			alert("定型文を選択してください。");
		} else if (str == "errorAddUrl") {
			alert("現在公開中の求人原稿はありません。");
		} else {
		$('#mailBody').val(str);
		}
	};


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

// -->
</script>

<!-- #main# -->
<div id="main">

<div id="wrap_mail">
	<h2>スカウトメール入力</h2>
	<p>下記の項目を入力の上、「送信」ボタンを押してください。<br>「宛先」のリンクをクリックすると、そのユーザーのプロフィールが閲覧できます。</p>

	<html:errors/>
	<div id="wrap_mail_content">
		<!-- 応募メール -->
		<s:form action="${f:h(actionPath)}">
		<html:hidden property="memberId"/>
		<div class="tab_contents" id="detail_Mail">
			<div class="message_input_area">
				<div class="sub_fiph">
					<div class="title">宛先</div>
					<ul class="senduser">
						<c:forEach var="m" items="${memberIdList}" varStatus="status">
							<c:set var="url" value="${f:url(gf:makePathConcat1Arg( '/member/detail/index', m))}" scope="page" />
							<li><a href="${url}" target="_blank">${f:h(m)}</a></li>
						</c:forEach>
					</ul>
				</div>
				<div class="sub_fiph">
					<div class="messinp_fiph">
						<div class="selectbox">
							<html:select property="sentenceId"  styleId="patternId" >
								<html:optionsCollection name="sentenceList"/>
							</html:select>
						</div>
						<input type="button" name="" value="決定" onclick="selectPattern(); return false;" id="btn_selectPattern">
					</div>
					<div class="messinp_sub">
						<html:text property="subject" placeholder="件名" id="subject" />
					</div>
				</div>
				<div class="matx_send">
					<div class="messinp_matx">
						<html:textarea property="body" rows="4" styleId="mailBody" placeholder="本文"></html:textarea>
					</div>
					<div class="messinp_send">
						<input type="submit" name="conf" value="送信" id="btn_conf">
					</div>
				</div>
			</div>
		</div>
		</s:form>
	</div>
</div>
</div>
<!-- #main# -->

<jsp:include page="/WEB-INF/view/common/backgroundAccess_js.jsp"></jsp:include>