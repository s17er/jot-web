package com.gourmetcaree.admin.pc.maintenance.dto.volume;

import java.io.Serializable;
import java.util.Date;

import com.gourmetcaree.common.dto.BaseDto;

/**
 *
 * 号数データ一覧のDTO
 * @author Makoto Otani
 * @version 1.0
 */
public class ListDto extends BaseDto implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 8214210193326381337L;

	/** 号数ID */
	public int id;

	/** 号数 */
	public String volume;

	/** 締切日時 */
	public Date deadlineDatetime;

	/** 確定締切日時 */
	public Date fixedDeadlineDatetime;

	/** 掲載開始日時 */
	public Date postStartDatetime;

	/** 掲載終了日時 */
	public Date postEndDatetime;

	/** 編集のパス */
	public String editPath;

	/** 削除のパス */
	public String deletePath;

}