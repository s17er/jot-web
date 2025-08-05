package com.gourmetcaree.common.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * WEB・系列店舗のタグDto
 * @author yamane
 */
public class TagListDto extends BaseDto implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 8115238871021252094L;

	/** ID */
	public String id;

	/** タグ名（WEBもしくは系列店舗） */
	public String tagName;

	/** タグ名前の一覧 */
	public List<String> tagNameList = new ArrayList<>();

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}