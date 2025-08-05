package com.gourmetcaree.shop.logic.dto;

import java.util.ArrayList;
import java.util.List;

import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.dto.BaseDto;
import com.gourmetcaree.db.common.entity.TObservateApplication;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;

/**
 * 店舗見学・質問メールの検索結果を保持するクラス
 * @author Yamane
 */
public class ObservateApplicationRetDto extends BaseDto {

	/** serialVersionUID */
	private static final long serialVersionUID = 4814170949410341477L;

	/** 店舗見学・質問メール者のリスト */
	public List<TObservateApplication> retList = new ArrayList<TObservateApplication>();

	/** ページナビゲータ */
	public PageNavigateHelper pageNavi = new PageNavigateHelper(GourmetCareeConstants.DEFAULT_MAX_ROW_INT);
}
