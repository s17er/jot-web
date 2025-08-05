package com.gourmetcaree.shop.logic.property;

import java.util.List;

import com.gourmetcaree.common.property.BaseProperty;

/**
 *
 * 応募メールのデータを受け渡しするクラス
 * @author Takahiro Ando
 *
 */
public class ApplicationMailProperty extends BaseProperty {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -9084101640052178952L;

	/** 返信元のメールID */
	public int originalMailId;

	/** 件名 */
	public String subject;

	/** 本文 */
	public String body;


	/** 受信メールID */
	public List<Integer> mailIdList;
}
