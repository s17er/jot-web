/**
 *
 */
package com.gourmetcaree.shop.pc.preview.form.listPreview;

import com.gourmetcaree.common.form.BaseForm;

/**
 * プレビュー基底フォーム
 * @author TETSUYA KANEKO
 * @version 1.0
 */
public abstract class PreviewBaseForm extends BaseForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 7674306633131330625L;

	/** 仙台版用 prefix */
	public static final String SENDAI_PREFIX = "/sendai";

	/** 首都圏版CSSパス */
	public static final String SYUTOKEN_CSS = "/css";

	/** 首都圏版imgパス */
	public static final String SYUTOKEN_IMG = "/img";

	/** 首都圏版imagesパス */
	public static final String SYUTOKEN_IMAGES = "/images";

	/** 首都圏版incパス */
	public static final String SYUTOKEN_INC = "/inc";

	/** 首都圏版helpパス */
	public static final String SYUTOKEN_HELP = "/help";

	/** 適応するCSS */
	public String cssLocation = SYUTOKEN_CSS;

	/** 適応するimg */
	public String imgLocation = SYUTOKEN_IMG;

	/** 適応するimages */
	public String imagesLocation = SYUTOKEN_IMAGES;

	/** 適応するinc */
	public String incLocation = SYUTOKEN_INC;

	/** 適応するhelp */
	public String helpLocation = SYUTOKEN_HELP;

	/**
	 * 仙台版用のパスを返却する
	 * @param sytokenPath
	 * @return
	 */
	public String getSendaiPath (String sytokenPath) {
		return SENDAI_PREFIX + sytokenPath;
	}

}
