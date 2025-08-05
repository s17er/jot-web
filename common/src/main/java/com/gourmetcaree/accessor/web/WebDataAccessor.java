package com.gourmetcaree.accessor.web;

import java.util.List;

/**
 * WEBデータの情報にアクセスするためのインタフェイス
 * @author nakamori
 *
 */
public interface WebDataAccessor {

	/** webIdの取得 */
	Integer getId();

	/** 業種リストの取得 */
	List<Integer> getIndustryList();

	String getManuscriptName();


	Integer getVolumeId();

	String getWorkingPlace();
}
