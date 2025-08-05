package com.gourmetcaree.admin.pc.maintenance.dto.company;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.gourmetcaree.common.dto.BaseDto;

/**
 *
 * 会社一覧のDTO
 * @author Makoto Otani
 * @version 1.0
 */
public class ListDto extends BaseDto implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -5776127974084636474L;

	/** 会社ID */
	public int id;

	/** 社名 */
	public String companyName;

	/** 社名(カナ) */
	public String companyNameKana;

	/** 担当者 */
	public String contactName;

	/** エリアコード */
	public List<String> areaCd = new ArrayList<String>();

	/** メインアドレス */
	public String mainMail;

	/** 電話番号1 */
	public String phoneNo1;

	/** 電話番号2 */
	public String phoneNo2;

	/** 電話番号3 */
	public String phoneNo3;

	/** 詳細のパス */
	public String detailPath;

}