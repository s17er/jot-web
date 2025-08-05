package com.gourmetcaree.admin.service.property;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.gourmetcaree.db.common.entity.MCompany;
import com.gourmetcaree.db.common.entity.MCompanyArea;

/**
 *
 * 会社管理のデータを受け渡しするクラス
 * @author Makoto Otani
 *
 */
public class CompanyProperty extends BaseProperty implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 3873069515509387878L;

	/** 会社マスタエンティティ */
	public MCompany mCompany;

	/** エリアコード */
	public List<String> areaCd = new ArrayList<String>();

	/** 一覧の検索結果を保持するリスト */
	public List<MCompany> entityList;

	/** 削除する会社エリアマスタを保持するリスト */
	public List<MCompanyArea> mCompanyAreaList;

}
