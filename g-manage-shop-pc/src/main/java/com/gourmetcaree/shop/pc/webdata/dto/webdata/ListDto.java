package com.gourmetcaree.shop.pc.webdata.dto.webdata;

import com.gourmetcaree.db.common.constants.MTypeConstants.ApplicationFormKbn;
import com.gourmetcaree.db.common.entity.MArea;
import com.gourmetcaree.shop.logic.dto.WebdataDto;

/**
 *
 * 求人原稿一覧のDTO
 * @author Makoto Otani
 * @version 1.0
 */
public class ListDto extends WebdataDto {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -1613752855343464405L;

	/** 応募のパス */
	public String applicationPath;

	/** 電話応募のパス */
	public String phoneApplicationPath;

	/** 気になるのパス */
	public String interestPath;

	/** プレ応募のパス */
	public String preApplicationPath;

	/** PCプレビューのパス */
	public String pcPreviewPath;

	/** モバイルプレビューのパス */
	public String mbPreviewPath;

	/** プレビューのURL */
	public String listPreviewUrl;

	/** エリア */
	public MArea MArea;

	/**
	 * 応募ができるかどうかを取得します。
	 * @return
	 */
	public boolean isApplicationOkFlg (){
		if (applicationFormKbn == ApplicationFormKbn.EXIST) {
			return true;
		}
		return false;
	}

}