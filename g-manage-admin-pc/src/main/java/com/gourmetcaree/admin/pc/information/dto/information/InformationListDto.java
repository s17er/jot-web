package com.gourmetcaree.admin.pc.information.dto.information;

import com.gourmetcaree.common.dto.BaseDto;

/**
 * お知らせの一覧用Dtoです。
 * @version 1.0
 */
public class InformationListDto extends BaseDto {

    /** シリアルバージョンUID */
	private static final long serialVersionUID = -1785601517547913816L;

	/** ID */
    public Integer id;

    /** 管理画面区分 */
    public Integer managementScreenKbn;

    /** エリアコード */
    public Integer areaCd;

    /** 本文 */
    public String body;

    /** 編集ページのパス */
    public String editPagePath;

}