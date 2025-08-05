<link rel="stylesheet" type="text/css" href="${SHOP_CONTENS}/css/webdata.css" />

<div id="main">

<div id="wrap_contact">
	<h2>お問い合わせ</h2>
	<p class="explanation">
		お問い合わせ内容をご入力し、「確認」へお進みください。<br>
		「<span style="color: red;">＊</span>」マークは<span style="color: red;">必須項目</span>となります。
	</p>
	<html:errors />
	<div id="wrap_cont_content">
		<div class="tab_area">
			<div class="tab_contents tab_active" id="">
				<s:form action="${f:h(actionPath)}">
				<table cellpadding="0" cellspacing="0" border="0" class="detail_table report_table"><tbody>
							<tr>
								<th>企業名</th>
								<td>
									${userDto.customerName}
									<html:hidden property="customerName" value="${userDto.customerName}"/>
								</td>
							</tr>
							<tr>
								<th class="mandatory">お名前</th>
								<td><html:text property="contactName" class="text"/></td>
							</tr>
							<tr>
								<th class="mandatory">電話番号</th>
								<td>
									<html:text property="phoneNo1" styleClass="disabled tel" /> -
									<html:text property="phoneNo2" size="5" styleClass="disabled tel" /> -
									<html:text property="phoneNo3" size="5" styleClass="disabled tel" />
								</td>
							</tr>
							<tr>
								<th class="mandatory">メールアドレス</th>
								<td><html:text property="sender"></html:text></td>
							</tr>
							<tr>
								<th class="mandatory">内容</th>
								<td>
									<html:textarea property="contents" cols="60" rows="10" placeholder="例）〇〇の画面が表示されない。求人掲載を再開したい。"></html:textarea>
								</td>
							</tr>
				</tbody></table>
				<div class="wrap_btn">
					<html:submit property="conf" value="確認"/>
				</div>
				</s:form>
			</div>
		</div>
	</div>
</div>

</div>