package com.gourmetcaree.admin.service.dto;

import java.io.Serializable;
import java.util.List;

import com.gourmetcaree.common.dto.BaseDto;
import com.gourmetcaree.db.common.entity.VActiveScoutMail;

/**
 * リターン用スカウトメール残数DTO
 * @author BALIUS
 *
 */
public class ScoutMailRemainRetDto extends BaseDto implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -6988734951958377736L;

	/** 無料スカウトメール数 */
	public Long freeScoutMailCount;

	/** 購入スカウトメール管理リスト */
	public List<VActiveScoutMail> boughtScoutMailManageList;

	/** 無期限スカウトメール数 */
	public Long unlimitedScoutMailCount;
}
