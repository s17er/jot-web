package com.gourmetcaree.shop.pc.shopList.form.shopList;

import java.io.Serializable;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.db.shopList.dto.shopList.ShopListMaterialExistsDto;

/**
 * 店舗一覧詳細画面用アクションフォーム
 * @author Takehiro Nakamori
 *
 */
@Component(instance=InstanceType.REQUEST)
public class DetailForm extends ShopListBaseForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 8319730663691310132L;

	/** バージョン */
	public Long version;

	public String deleteVersion;

	/** 素材有り無しのDTO */
	public ShopListMaterialExistsDto materialExistsDto;

	/**
	 * フォームのリセット
	 */
	@Override
	public void resetForm() {
		super.resetForm();
		id = null;
		version = null;
		deleteVersion = null;
	}

	/**
	 * 店舗IDのリセット
	 */
	public void resetShopId() {
		id = null;
	}

}
