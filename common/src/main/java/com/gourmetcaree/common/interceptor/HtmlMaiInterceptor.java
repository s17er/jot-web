package com.gourmetcaree.common.interceptor;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.seasar.framework.aop.interceptors.AbstractInterceptor;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.util.MethodUtil;
import org.seasar.mai.mail.SendMail;
import org.seasar.mai.mail.Transport;
import org.seasar.mai.meta.MaiMetaData;
import org.seasar.mai.meta.MaiMetaDataFactory;
import org.seasar.mai.property.PropertyWriterForBean;
import org.seasar.mai.template.ContextHelper;
import org.seasar.mai.template.TemplateProcessor;
import org.seasar.mai.util.MailTextUtil;

import com.gourmetcaree.common.taglib.function.GourmetCareeFunctions;
import com.ozacc.mail.Mail;

public class HtmlMaiInterceptor extends AbstractInterceptor {
    private static final long serialVersionUID = -3003054499497347849L;

    private MaiMetaDataFactory maiMetaDataFactory;

    private SendMail sendMail;

    private PropertyWriterForBean propertyWriter;

    private ContextHelper contextHelper;

    private TemplateProcessor templateProcessor;

    private Transport transport;

    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        if (!MethodUtil.isAbstract(method)) {
            return invocation.proceed();
        }
        if (isGetSendMail(method)) {
            return getSendMail().clone();
        }
        sendMail(invocation);
        return null;
    }

    private void sendMail(MethodInvocation invocation) {
        Method method = invocation.getMethod();
        init();
        MaiMetaData metaData = getMaiMetaDataFactory().getMaiMetaData(getTargetClass(invocation));
        Object bean = getBean(invocation);
        Object context = getContextHelper().createContext(bean);
        Mail mail = createMail(method, context, metaData);
        getPropertyWriter().setMailProperty(mail, bean);
        SendMail mailSender = (SendMail) getSendMail().clone();
        getPropertyWriter().setServerProperty(mailSender, bean);
        send(mail, mailSender);
    }

    private boolean isGetSendMail(Method method) {
        if ("getSendMail".equals(method.getName()) && SendMail.class.equals(method.getReturnType())) {
            return true;
        } else {
            return false;
        }
    }

    private Object getBean(MethodInvocation invocation) {
        Object[] arguments = invocation.getArguments();
        if (arguments == null || arguments.length == 0) {
            return null;
        }
        return arguments[0];
    }

    private void send(Mail mail, SendMail sendMail) {
        getTransport().send(mail, sendMail);
    }

    private Mail createMail(Method method, Object context, MaiMetaData metaData) {
        Mail mail = metaData.getMail(method);
        String path = metaData.getTemplatePath(method);
        String text = getTemplateProcessor().processResource(path, context);
        String subject = MailTextUtil.getSubject(text);
        text = MailTextUtil.getText(text);
        if (subject != null) {
            mail.setSubject(subject);
        }
        mail.setHtmlText(convertToHtml(text));
        return mail;
    }

    private void init() {
        getTemplateProcessor().init();
    }


    public MaiMetaDataFactory getMaiMetaDataFactory() {
        if (this.maiMetaDataFactory == null) {
            this.maiMetaDataFactory = SingletonS2Container.getComponent(MaiMetaDataFactory.class);
        }
        return maiMetaDataFactory;
    }

    public SendMail getSendMail() {
        if (sendMail == null) {
            sendMail = SingletonS2Container.getComponent(SendMail.class);
        }
        return sendMail;
    }

    public PropertyWriterForBean getPropertyWriter() {
        if (propertyWriter == null) {
            this.propertyWriter = SingletonS2Container.getComponent(PropertyWriterForBean.class);
        }
        return propertyWriter;
    }

    public ContextHelper getContextHelper() {
        if (contextHelper == null) {
            contextHelper = SingletonS2Container.getComponent(ContextHelper.class);
        }
        return contextHelper;
    }

    public TemplateProcessor getTemplateProcessor() {
        if (templateProcessor == null) {
            templateProcessor = SingletonS2Container.getComponent(TemplateProcessor.class);
        }
        return templateProcessor;
    }

    public Transport getTransport() {
        if (transport == null) {
            transport = SingletonS2Container.getComponent(Transport.class);
        }
        return transport;
    }

    private static String convertToHtml(String body) {
        final String brBody = body.replaceAll("\\r\\n", "<br>");
        return String.format("<html><body>%s</body></html>", GourmetCareeFunctions.editUrlLink(brBody));
    }
}