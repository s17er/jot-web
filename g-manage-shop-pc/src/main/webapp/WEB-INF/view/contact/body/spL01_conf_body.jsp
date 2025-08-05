<link rel="stylesheet" type="text/css" href="${SHOP_CONTENS}/css/webdata.css" />

<div id="main">

<div id="wrap_contact">
	<h2>お問い合わせ</h2>
	<p class="explanation">
		お問い合わせ内容をご確認し、「完了」へお進みください。
	</p>
	<div id="wrap_cont_content">
		<div class="tab_area">
			<div class="tab_contents tab_active" id="">
				<table cellpadding="0" cellspacing="0" border="0" class="detail_table report_table">
					<tbody>
						<tr>
							<th>企業名</th>
							<td>${userDto.customerName}</td>
						</tr>
						<tr>
							<th class="mandatory">お名前</th>
							<td>${f:h(contactName)}</td>
						</tr>
						<tr>
							<th class="mandatory">電話番号</th>
							<td>${f:h(phoneNo1)}-${f:h(phoneNo2)}-${f:h(phoneNo3)}</td>
						</tr>
						<tr>
							<th class="mandatory">メールアドレス</th>
							<td>${f:h(sender)}</td>
						</tr>
						<tr>
							<th class="mandatory">内容</th>
							<td>${f:br(f:h(contents))}</td>
						</tr>
					</tbody>
				</table>
				<s:form action="${f:h(actionPath)}">
				<div class="wrap_btn">
					<html:submit property="back" class="btn__white" value="戻る"/>
					<html:submit property="submit" class="btn__orenge" value="送信"/>
				</div>
				</s:form>
			</div>
		</div>
	</div>
</div>

</div>