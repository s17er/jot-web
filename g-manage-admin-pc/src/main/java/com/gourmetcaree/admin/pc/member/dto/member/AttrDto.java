package com.gourmetcaree.admin.pc.member.dto.member;

import java.io.Serializable;

/**
 * 職種DTOクラス
 * @author Takahiro Kimura
 * @version 1.0
 */
public class AttrDto implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 3150932127972924622L;

	/** 属性コード */
	public String attributeCd;

	/** 属性値 */
	public String attributeValue;

}