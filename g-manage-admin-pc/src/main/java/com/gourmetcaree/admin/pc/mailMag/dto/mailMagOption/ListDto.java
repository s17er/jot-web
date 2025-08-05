package com.gourmetcaree.admin.pc.mailMag.dto.mailMagOption;

import java.io.Serializable;
import java.util.Date;

import com.gourmetcaree.common.dto.BaseDto;

public class ListDto extends BaseDto implements Serializable{

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 5285452924184016895L;

	/** ID */
	public int id;

	/** エリアコード */
	public int areaCd;

	/** メルマガID */
	public Integer mailMagazineId;

	/** 配信予定日 */
	public Date deliveryScheduleDatetime;

	/** 本文 */
	public String optionValue;

	/** 詳細パス */
	public String detailPath;

	/** 編集のパス */
	public String editPath;

	/** 削除のパス */
	public String deletePath;

	/** 編集できるかどうかのフラグ */
	public boolean isEditFlg() {
		if (mailMagazineId == null) {
			return true;
		}
		return false;
	}

}
