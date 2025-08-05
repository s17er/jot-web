package com.gourmetcaree.shop.logic.property;

import java.util.List;

import com.gourmetcaree.common.property.BaseProperty;
import com.gourmetcaree.db.common.entity.TMail;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.scoutFoot.dto.scoutMail.ScoutMailListDto;

/**
 *
 * スカウトメールのデータを受け渡しするクラス
 * @author Takahiro Kimura
 *
 */
public class ScoutMailProperty extends BaseProperty {


	/** シリアルバージョンUID */
	private static final long serialVersionUID = -4170366710310223259L;

	/** スカウトメールリスト */
	public List<ScoutMailListDto> scoutMailListDtoList;

	/** メールエンティティ */
	public TMail tMail;

	/** 送受信区分 */
	public int sendKbn;

	/** スカウトメール数 */
	public int scoutCount;

	/** メールID */
	public int mailId;

	/** 会員ID */
	public int memberId;

	/** 顧客ID */
	public int customerId;

	/** 件名 */
	public String subject;

	/** 本文 */
	public String body;

	/** 会員IDリスト */
	public List<Integer> memberIdList;

	/** メールステータス */
	public String mailStatus;

	/** ページナビゲーター */
	public PageNavigateHelper pageNavi;

	/** スカウトメールログID */
	public Integer scoutMailLogId;

	/** 表示ページ */
	public int targetPage;


}
