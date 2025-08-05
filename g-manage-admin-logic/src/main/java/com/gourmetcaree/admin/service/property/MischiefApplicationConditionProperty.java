package com.gourmetcaree.admin.service.property;

import java.io.Serializable;
import java.util.List;

import com.gourmetcaree.db.common.entity.MMischiefApplicationCondition;

/**
 *
 * いたずら応募条件のデータを受け渡しするクラス
 * @author Aquarius
 *
 */
public class MischiefApplicationConditionProperty extends BaseProperty implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -6458410002088108405L;


	/** いたずら応募条件マスタエンティティ */
	public MMischiefApplicationCondition mMischiefApplicationCondition;

	/** 一覧の検索結果を保持するリスト */
	public List<MMischiefApplicationCondition> entityList;


}
