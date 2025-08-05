package com.gourmetcaree.admin.pc.maintenance.dto.mischief;

import java.io.Serializable;

import com.gourmetcaree.common.dto.BaseDto;

/**
 * いたずら応募条件一覧Dto
 * @author Aquarius
 *
 */
public class ListDto extends BaseDto implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -5776127974084636474L;

	/** ID */
	public int id;

	/** 氏名 */
	public String name;

	/** 氏名(カナ) */
	public String nameKana;

	/** 都道府県 */
	public int prefecturesCd;

	/** 住所 */
	public String address;

	/** 電話番号1 */
	public String phoneNo1;

	/** 電話番号2 */
	public String phoneNo2;

	/** 電話番号3 */
	public String phoneNo3;

	/** メールアドレス */
	public String mailAddress;

	/** 会員フラグ */
	public Integer memberFlg;

	/** 端末区分 */
	public Integer terminalKbn;

	/** 詳細のパス */
	public String detailPath;

}