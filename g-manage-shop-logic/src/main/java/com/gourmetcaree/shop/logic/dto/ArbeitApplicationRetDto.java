package com.gourmetcaree.shop.logic.dto;

import org.seasar.extension.jdbc.SqlSelect;

import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.dto.BaseDto;
import com.gourmetcaree.db.common.entity.TArbeitApplication;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;

/**
 * グルメdeバイトの検索結果を保持するクラス
 * @author Yamane
 */
public class ArbeitApplicationRetDto extends BaseDto {

	/** serialVersionUID */
	private static final long serialVersionUID = 4814170949410341477L;

	/** 店舗見学・質問メール者のリスト */
	public SqlSelect<TArbeitApplication> applicationSelect;

	/** ページナビゲータ */
	public PageNavigateHelper pageNavi = new PageNavigateHelper(GourmetCareeConstants.DEFAULT_MAX_ROW_INT);
}
