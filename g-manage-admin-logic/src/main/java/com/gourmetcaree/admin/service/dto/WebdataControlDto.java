package com.gourmetcaree.admin.service.dto;

import java.io.Serializable;

import com.gourmetcaree.common.dto.BaseDto;
/**
 * WEBデータ処理可否の制御設定を保持するDto
 * @author Makoto Otani
 *
 */
public class WebdataControlDto extends BaseDto implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 8836953051716343932L;

	/** 画面表示ステータス */
	public int displayStatus;

	/** 編集可否 */
	public boolean editFlg;

	/** 削除可否 */
	public boolean deleteFlg;

	/** コピー可否 */
	public boolean copyFlg;

	/** 応募テスト可否 */
	public boolean appTestFlg;

	/** 掲載確定可否 */
	public boolean fixedFlg;

	/** 確定取消可否 */
	public boolean cancelFlg;

	/** 掲載依頼可否 */
	public boolean postRequestFlg;

	/** 掲載終了可否 */
	public boolean postEndFlg;

	/** WEB非表示可否 */
	public boolean hiddenFlg;

	/**
	 * 掲載確定・応募テスト両方がtrue
	 * @return
	 */
	public boolean isFixedAndAppTest() {
		return fixedFlg && appTestFlg;
	}

	/**
	 * 掲載確定がtrue、応募テストがfalse
	 * @return
	 */
	public boolean isFixedAndNotApptest() {
		return fixedFlg && !appTestFlg;
	}

	/**
	 * 掲載確定がfalse、応募テストがtrue
	 * @return
	 */
	public boolean isNotFixedAndAppTest() {
		return !fixedFlg && appTestFlg;
	}

	/**
	 * 掲載確定・応募テスト両方がfalse
	 * @return
	 */
	public boolean isNotFixedAndNotAppTest() {
		return !fixedFlg && !appTestFlg;
	}


}
