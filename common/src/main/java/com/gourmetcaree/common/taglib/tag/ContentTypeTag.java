package com.gourmetcaree.common.taglib.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.seasar.framework.container.factory.SingletonS2ContainerFactory;

import com.gourmetcaree.common.util.UserAgentUtils;

/**
 * ユーザエージェントを元に対応するcontentTypeをレスポンスヘッダーに設定します。
 *
 * @author Katsutoshi Hasegawa
 * @version 1.0
 */
public class ContentTypeTag extends TagSupport {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -4848078154115779486L;

	/** ContentType docomo用 */
	private static final String CONTENT_TYPE_FOR_DOCOMO = "application/xhtml+xml; charset=Shift_JIS";

	/** ContentType */
	private static final String CONTENT_TYPE = "text/html; charset=Shift_JIS";

	/*
	 * (非 Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {

		HttpServletRequest request = (HttpServletRequest)SingletonS2ContainerFactory.getContainer().getExternalContext().getRequest();
		HttpServletResponse response = (HttpServletResponse)SingletonS2ContainerFactory.getContainer().getExternalContext().getResponse();

		if (request == null || response == null) {
			return EVAL_PAGE;
		}

		String userAgent = request.getHeader("User-Agent");

		try {

			switch (UserAgentUtils.getUserAgentKbn(userAgent)) {
			case DOCOMO:
				response.setContentType(CONTENT_TYPE_FOR_DOCOMO);
				break;

			default:
				response.setContentType(CONTENT_TYPE);
			}

		} catch (Exception e) {
			throw new JspException(e);
		}
		return EVAL_PAGE;
	}
}
