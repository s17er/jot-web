package com.gourmetcaree.common.taglib.tag;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import jp.co.whizz_tech.commons.WztCp932Util;
import jp.co.whizz_tech.commons.WztKatakanaUtil;

/**
 * ケータイ用の全角カナ→半角カナ変換、WAVE DASH問題を変換するカスタムタグです。
 * @author Takahiro Ando
 * @version 1.0
 */
public class MobileConverterTag extends BodyTagSupport {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 8228572285794698978L;

	public MobileConverterTag() {
		super();
	}

	/**
	 * カスタムタグ読み込み時に呼ばれる
	 */
	@Override
	public int doStartTag() throws JspTagException {

		// 結果が存在する場合はボディを評価する
		return EVAL_BODY_AGAIN;
	}

	/**
	 * BODY読み込み後処理。
	 */
	@Override
	public int doAfterBody() throws JspTagException {

		//ボディの内容を取得
		BodyContent body = getBodyContent();

		//全角カタカナを半角カタカナに、WAVE DASH問題の文字コードを変換します。
		String convertedStr = WztCp932Util.toJIS(WztKatakanaUtil.zenkakuToHankaku(body.getString()));

		try {
			//ボディを出力
			body.getEnclosingWriter().write(convertedStr);
		} catch (IOException e) {
			throw new JspTagException(e.getMessage());
		}

		body.clearBody();

		return SKIP_BODY;
	}

	/**
	 * 後処理
	 */
	@Override
	public int doEndTag() {

		return EVAL_PAGE;
	}
}
