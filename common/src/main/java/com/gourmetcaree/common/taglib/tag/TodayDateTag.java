package com.gourmetcaree.common.taglib.tag;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;

/**
 * 本日の日付を取得して出力します。
 * @author Katsutoshi Hasegawa
 * @version 1.0
 */
public class TodayDateTag extends TagSupport{

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -5698646257105956738L;

	/** フォーマットパターン */
	private String formatPattern;

	/**
	 * フォーマットパターンを設定します。
	 * @param formatPattern フォーマットパターン
	 */
	public void setFormatPattern(String formatPattern) {
		this.formatPattern = formatPattern;
	}

	/*
	 * (非 Javadoc)
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {

		if (StringUtils.isBlank(formatPattern)) {
			formatPattern = "yyyy/MM/dd";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(formatPattern);
		Date todayDate = new Date();
		String todayDateStr = sdf.format(todayDate);

		try {
			pageContext.getOut().write(todayDateStr);
		} catch (Exception e) {
			throw new JspException(e);
		}

		return EVAL_PAGE;
	}
}
