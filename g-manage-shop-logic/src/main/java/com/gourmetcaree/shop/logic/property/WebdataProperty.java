package com.gourmetcaree.shop.logic.property;

import java.util.List;
import com.gourmetcaree.common.property.BaseProperty;
import com.gourmetcaree.db.common.entity.VWebList;

/**
 *
 * WEBデータを受け渡しするクラス
 * @author Makoto Otani
 *
 */
public class WebdataProperty extends BaseProperty {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -8633574147467408779L;

	/** WEB一覧ビューエンティティ */
	public List<VWebList> vWebListList;

}
