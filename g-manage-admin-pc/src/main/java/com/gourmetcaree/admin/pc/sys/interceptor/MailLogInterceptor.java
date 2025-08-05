package com.gourmetcaree.admin.pc.sys.interceptor;

import javax.servlet.http.HttpSession;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.seasar.framework.aop.interceptors.AbstractInterceptor;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;

import com.gourmetcaree.admin.pc.sys.dto.UserDto;
import com.gourmetcaree.common.dto.BaseSysMaiDto;

/**
 * s2maiへのログ制御を処理するインターセプターです。
 *
 * @author Katsutoshi Hasegawa
 * @version 1.0
 */
public class MailLogInterceptor extends AbstractInterceptor {

    /**
     * シリアルバージョンUID
     */
    private static final long serialVersionUID = -6940803373888783057L;

    /**
     * ログオブジェクト
     */
    private static final Logger sysMaillog = Logger.getLogger("sysMailLog");

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        // 引数から送信先アドレスの取得
        Object[] args = invocation.getArguments();
        BaseSysMaiDto dto = null;
        if (args != null && args.length > 0) {
            for (int i = 0; i < args.length; ++i) {
                if (args[i] instanceof BaseSysMaiDto) {
                    dto = (BaseSysMaiDto) args[i];
                }
            }
        }

        // userDtoから送信者の取得
        HttpSession session = (HttpSession) SingletonS2ContainerFactory.getContainer().getExternalContext().getSession();
        UserDto userDto = (UserDto) session.getAttribute("userDto");

        // ログを出力
        if (dto == null) {
            sysMaillog.fatal("BaseSysMaiDtoがnullです。");
        } else if (userDto == null) {
            sysMaillog.fatal("userDtoがnullです。");
        } else {
            sysMaillog.info(String.format("メールが送信されました。処理担当者：[%s]  送信元：[%s] 送信先アドレス：[%s] \nメール内容：[%s]",
                    userDto.userId,
                    dto.getFrom(),
                    dto.getTo(),
                    ToStringBuilder.reflectionToString(dto)));
        }


        return invocation.proceed();
    }
}
