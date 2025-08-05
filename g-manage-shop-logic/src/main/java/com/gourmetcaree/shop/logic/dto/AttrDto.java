package com.gourmetcaree.shop.logic.dto;

import java.io.Serializable;

/**
 * 職種DTOクラス
 * @author Takahiro Kimura
 * @version 1.0
 */
public class AttrDto implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -6229690764250176852L;

	/** 属性コード */
	public String attributeCd;

	/** 属性値 */
	public String attributeValue;

}