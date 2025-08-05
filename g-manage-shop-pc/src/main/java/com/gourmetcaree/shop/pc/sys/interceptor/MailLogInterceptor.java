package com.gourmetcaree.shop.pc.sys.interceptor;
import javax.servlet.http.HttpSession;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.seasar.framework.aop.interceptors.AbstractInterceptor;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;

import com.gourmetcaree.common.dto.BaseSysMaiDto;
import com.gourmetcaree.shop.pc.sys.dto.UserDto;


/**
 * s2maiへのログ制御を処理するインターセプターです。
 * @author Katsutoshi Hasegawa
 * @version 1.0
 */
public class MailLogInterceptor extends AbstractInterceptor {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 522396163463142167L;

	/** ログオブジェクト */
	private static final Logger sysMaillog = Logger.getLogger("sysMailLog");

	/** ログオブジェクト（なりすまし） */
	private static final Logger sysMasqueradeLog = Logger.getLogger("sysMasqueradeLog");

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
		UserDto userDto = (UserDto)session.getAttribute("userDto");

		// ログを出力
		if (dto == null) {
			sysMaillog.fatal("BaseSysMaiDtoがnullです。");
			if (userDto.isMasqueradeFlg()) {
				sysMasqueradeLog.fatal(String.format("BaseSysMaiDtoがnullです。営業ID：%s, 顧客ID：%s", userDto.masqueradeUserId, userDto.customerId));
			}
		} else if (userDto == null) {
			sysMaillog.fatal("userDtoがnullです。");
		} else {
			sysMaillog.info("メールが送信されました。処理担当者：[" + userDto.userId + "] 送信元：[" + dto.getFrom() +
					"] 送信先アドレス：" + dto.getTo() +
					" DTO: " + ToStringBuilder.reflectionToString(dto));
			if (userDto.isMasqueradeFlg()) {
				sysMasqueradeLog.info(String.format("メールが送信されました。営業ID：%s, 顧客ID：%s", userDto.masqueradeUserId, userDto.customerId));
				sysMasqueradeLog.info("メールが送信されました。処理担当者：[" + userDto.userId + "] 送信元：[" + dto.getFrom() + "] 送信先アドレス：" + dto.getTo());
			}
		}

		return invocation.proceed();
	}
}
