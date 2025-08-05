package com.gourmetcaree.shop.logic.dto;

import java.util.ArrayList;
import java.util.List;

import org.seasar.extension.jdbc.SqlSelect;

import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.dto.BaseDto;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;

/**
 * 応募メールの検索結果を保持するクラスです。
 * @author Takahiro Ando
 */
public class ApplicationMailRetDto extends BaseDto {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -220011933299944723L;

	/** 応募エンティティのリスト */
	public List<MailSelectDto> retList = new ArrayList<MailSelectDto>();

	/** ページナビゲータ */
	public PageNavigateHelper pageNavi = new PageNavigateHelper(GourmetCareeConstants.DEFAULT_MAX_ROW_INT);


	/** SQLセレクト */
	public SqlSelect<MailSelectDto> select;
}
