package com.gourmetcaree.db.maintenance.dto.volume;

import java.io.Serializable;

import com.gourmetcaree.db.common.entity.MVolume;

/**
 * 号数データ詳細のDTO
 * @author Makoto Otani
 * @version 1.0
 */
public class DetailDto extends MVolume implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 7506993621693763931L;

	/** エリア名 */
	public String areaName;

}