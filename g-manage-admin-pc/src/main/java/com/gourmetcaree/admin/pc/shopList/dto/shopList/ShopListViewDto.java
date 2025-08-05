package com.gourmetcaree.admin.pc.shopList.dto.shopList;

import java.util.List;

import com.gourmetcaree.common.dto.BaseDto;
import com.gourmetcaree.common.dto.customerImage.ImageDto;

public class ShopListViewDto extends BaseDto {

	private static final long serialVersionUID = 7069778818586603879L;

	public String previewUrl;

	public List<ImageDto> imageList;
}
