package com.gourmetcaree.shop.logic.dto;

import java.util.ArrayList;
import java.util.List;

import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.dto.BaseDto;
import com.gourmetcaree.db.common.entity.TPreApplication;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;

/**
 * プレ応募者一覧の検索結果を保持するクラスです。
 */
public class PreApplicationListRetDto extends BaseDto {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -4406911295670501577L;

	/** プレ応募エンティティのリスト */
	public List<TPreApplication> retList = new ArrayList<TPreApplication>();

	/** ページナビゲータ */
	public PageNavigateHelper pageNavi = new PageNavigateHelper(GourmetCareeConstants.DEFAULT_MAX_ROW_INT);
}
