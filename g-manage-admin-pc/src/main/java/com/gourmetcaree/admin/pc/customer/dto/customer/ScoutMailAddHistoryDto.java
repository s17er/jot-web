package com.gourmetcaree.admin.pc.customer.dto.customer;

import java.io.Serializable;

/**
 * スカウトメール追加履歴DTOクラス
 * @author Takahiro Kimura
 * @version 1.0
 */
public class ScoutMailAddHistoryDto implements Serializable {


	/** シリアルバージョンUID */
	private static final long serialVersionUID = 3339149459295704497L;

	/** スカウトメール追加日時 */
	public String addDate;

	/** スカウトメール追加件数 */
	public Integer addCount;

	/** スカウトメール区分 */
	public int scoutMailKbn;

	/** スカウトメールログ区分 */
    public Integer scoutMailLogKbn;

}