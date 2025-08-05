package com.gourmetcaree.db.webdata.dto.webdata;

import com.gourmetcaree.common.dto.BaseDto;

/**
 * 画像が存在するかどうかを保持するDto
 * @author Makoto Otani
 * @version 1.0
 */
public class MaterialExistsDto extends BaseDto {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 6002498142202397114L;

	/** 素材区分_メイン1(1) */
	public boolean isMain1ExistFlg = false;
	/** 素材区分_メイン2(2) */
	public boolean isMain2ExistFlg = false;
	/** 素材区分_メイン3(3) */
	public boolean isMain3ExistFlg = false;
	/** 素材区分_ロゴ(4) */
	public boolean isLogoExistFlg = false;
	/** 素材区分_右画像(5) */
	public boolean isRightExistFlg = false;
	/** 素材区分_写真A(6) */
	public boolean isPhotoAExistFlg = false;
	/** 素材区分_写真B(7) */
	public boolean isPhotoBExistFlg = false;
	/** 素材区分_写真C(8) */
	public boolean isPhotoCExistFlg = false;
	/** 素材区分_フリー(9) */
	public boolean isFreeExistFlg = false;
	/** 素材区分_注目店舗(10) */
	public boolean isAttentionShopExistFlg = false;
	/** 素材区分_ここに注目(11) */
	public boolean isAttentionHereExistFlg = false;

	/** 素材区分_メイン１サムネイル(21) */
	public boolean isMain1ThumbExistFlg = false;
	/** 素材区分_メイン２サムネイル(22) */
	public boolean isMain2ThumbExistFlg = false;
	/** 素材区分_メイン３サムネイル(23) */
	public boolean isMain3ThumbExistFlg = false;
	/** 素材区分_ロゴサムネイル(24) */
	public boolean isLogoThumbExistFlg = false;
	/** 素材区分_右画像サムネイル(25) */
	public boolean isRightThumbExistFlg = false;
	/** 素材区分_写真Aサムネイル(26) */
	public boolean isPhotoAThumbExistFlg = false;
	/** 素材区分_写真Bサムネイル(27) */
	public boolean isPhotoBThumbExistFlg = false;
	/** 素材区分_写真Cサムネイル(28) */
	public boolean isPhotoCThumbExistFlg = false;
	/** 素材区分_フリーサムネイル(29) */
	public boolean isFreeThumbExistFlg = false;
	/** 素材区分_注目店舗サムネイル(30) */
	public boolean isAttentionShopThumbExistFlg = false;
	/** 素材区分_ここに注目サムネイル(31) */
	public boolean isAttentionHereThumbExistFlg = false;

	/** 素材区分_動画(WM)(41) */
	public boolean isMovieWmExistFlg = false;
	/** 素材区分_動画(QT)(42) */
	public boolean isMovieQtExistFlg = false;
}