package com.gourmetcaree.admin.service.property;

import java.util.ArrayList;
import java.util.List;

import com.gourmetcaree.db.common.entity.MSpecial;

/**
 *
 * 特集データを受け渡しするクラス
 * @author Makoto Otani
 *
 */
public class SpecialProperty extends BaseProperty {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -7739344294565305343L;

	/** 特集マスタエンティティ */
	public MSpecial mSpecial;

	/** エリアコード */
	public List<Integer> areaCd = new ArrayList<Integer>();
}
