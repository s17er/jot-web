package com.gourmetcaree.admin.pc.customerImage.dto.customerImage;

import java.io.Serializable;

import com.gourmetcaree.common.dto.customerImage.ImageDto;

public class UpImageResponse implements Serializable {

	private static final long serialVersionUID = -2430549783880834030L;

	/** 失敗フラグ */
	public Boolean errorFlg;

	/** メッセージ */
	public String message;

	/** アップした画像DTO */
	public ImageDto imageDto;
}
