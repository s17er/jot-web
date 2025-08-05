package com.gourmetcaree.shop.pc.pattern.dto.pattern;

import java.util.Date;

import com.gourmetcaree.common.dto.BaseDto;

/**
 *
 * 定型文一覧のDTO
 * @author Makoto Otani
 * @version 1.0
 */
public class ListDto extends BaseDto {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 6441408053897462715L;

	/** ID */
	public int id;

	/** 登録日時 */
	public Date registrationDatetime;

	/** 定型文タイトル */
	public String sentenceTitle;

	/** 詳細のパス */
	public String detailPath;

}