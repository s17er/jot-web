package com.gourmetcaree.admin.pc.customer.dto.customer;

import java.io.Serializable;
import java.sql.Timestamp;

import com.gourmetcaree.common.dto.BaseDto;

/**
 * スカウトメールの残りのDTOです。
 * @author Takehiro Nakamori
 *
 */
public class ScoutMailRemainDto extends BaseDto implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 835781307879382587L;

	/** スカウトメール残数 */
	public int scoutRemainCount;

	/** スカウトメール期限 */
	public Timestamp useEndDatetime;

}
