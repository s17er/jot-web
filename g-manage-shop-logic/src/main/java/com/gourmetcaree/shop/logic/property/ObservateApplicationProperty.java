package com.gourmetcaree.shop.logic.property;

import java.io.Serializable;

import com.gourmetcaree.common.property.BaseProperty;
import com.gourmetcaree.db.common.entity.TObservateApplication;

/**
 *
 * 店舗見学・質問メールのプロパティ
 * @author Yamane
 *
 */
public class ObservateApplicationProperty extends BaseProperty implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -2371680057812949447L;

	/** 店舗見学・質問メールのエンティティ */
	public TObservateApplication tObservateApplication;
}
