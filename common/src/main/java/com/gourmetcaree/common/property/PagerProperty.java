package com.gourmetcaree.common.property;


/**
 * ページングを行う画面で共通で使用するプロパティ
 * @author Takahiro Ando
 */
public class PagerProperty extends BaseProperty {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -5123667496601233408L;

	/** 最大表示件数 */
	public int maxRow;

	/** 表示ページ */
	public int targetPage;
}
