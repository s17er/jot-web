package com.gourmetcaree.shop.logic.dto;

import java.util.ArrayList;
import java.util.List;

import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.dto.BaseDto;
import com.gourmetcaree.db.common.entity.TApplication;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;

/**
 * 応募者一覧の検索結果を保持するクラスです。
 * @author Takahiro Ando
 */
public class ApplicationListRetDto extends BaseDto {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -4406911295670501577L;

	/** 応募エンティティのリスト */
	public List<TApplication> retList = new ArrayList<TApplication>();

	/** ページナビゲータ */
	public PageNavigateHelper pageNavi = new PageNavigateHelper(GourmetCareeConstants.DEFAULT_MAX_ROW_INT);
}
