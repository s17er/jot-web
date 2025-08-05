package com.gourmetcaree.shop.pc.application.dto.arbeit;

import com.gourmetcaree.shop.pc.application.dto.application.ApplicationListDto;

/**
 * グルメdeバイトのDto
 * @author Yamane
 *
 */
public class ArbeitApplicationListDto extends ApplicationListDto {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -5399292786105512256L;


	/** 応募職種 */
	public String applicationJob;

	public Integer shopListId;

	/** チェックされたIDに含まれるかのフラグ */
	public boolean containsCheckedIdFlg;

}
