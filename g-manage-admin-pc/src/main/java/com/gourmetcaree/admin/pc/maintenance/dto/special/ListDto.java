package com.gourmetcaree.admin.pc.maintenance.dto.special;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gourmetcaree.common.dto.BaseDto;

/**
 * 特集データ一覧のDTO
 * @author Makoto Otani
 * @version 1.0
 */
public class ListDto extends BaseDto implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -5170528352246542889L;

	/** 特集ID */
	public int id;

	/** 特集名 */
	public String specialName;

	/** 表示名 */
	public String displayName;

	/** エリアコード */
	public List<String> areaCd = new ArrayList<String>();

	/** 掲載開始日時 */
	public Date postStartDatetime;

	/** 掲載終了日時 */
	public Date postEndDatetime;

	/** 説明 */
	public String explanation;

	/** アドレス */
	public String url;

	/** 編集のパス */
	public String editPath;

	/** 削除のパス */
	public String deletePath;

}