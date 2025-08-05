package com.gourmetcaree.admin.service.dto;

import com.gourmetcaree.common.dto.BaseDto;

/**
 * IP電話番号コピー用のDto
 * @author hara
 *
 */
public class IPPhoneDataCopyDto extends BaseDto {

	private static final long serialVersionUID = 9001431853505351513L;

	/** 登録先の原稿ID */
	public Integer webId;

	/** コピー元の原稿ID */
	public Integer sourceWebId;

	/** 顧客ID */
	public Integer customerId;
}
