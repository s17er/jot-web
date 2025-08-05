Subject: グルメキャリー【${data.observationName}送信通知】

※こちらのメールアドレスは送信専用ですので返信出来ません。

──────────
◆${data.observationName}メール送信通知
──────────

応募メールの送信がありました。
[送信者]
${data.customerName}(${data.customerId})

[宛先]
<#if data.applicantName??>
${data.applicantName}(応募ID: ${data.applicationId}、メールID：${data.mailId})
<#else>
匿名(応募ID: ${data.applicationId}、メールID：${data.mailId})
</#if>