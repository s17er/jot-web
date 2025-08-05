package com.gourmetcaree.valueobject;

import org.seasar.framework.container.SingletonS2Container;

import com.gourmetcaree.accessor.web.PublicationEndAccessor;
import com.gourmetcaree.common.env.EnvDto;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;

/**
 * 掲載終了ステータスを管理するためのValueObject
 * @author nakamori
 *
 */
public class PublicationEndStatus {

	/**
	 * 掲載終了判定をするために必要なアクセサ
	 */
	private final PublicationEndAccessor accessor;

	/** エリアコード */
	private final int areaCd;

	public PublicationEndStatus(PublicationEndAccessor accessor) {
		this.accessor = accessor;
		EnvDto env = SingletonS2Container.getComponent(EnvDto.class);
		this.areaCd = env.getAreaCd();
	}

	/**
	 * アクセサに必要な情報が保持されていない場合にtrueとなる。
	 */
	public boolean isIllegalAccessor() {
		if (accessor.getPublicationEndDisplayFlg() == null
				|| accessor.getCustomerId() == null
				|| accessor.getPostStartDatetime() == null
				|| accessor.getPostEndDatetime() == null) {

			return true;
		}

		if (!GourmetCareeUtil.eqInt(areaCd, accessor.getAreaCd())) {
			return true;
		}

		return false;
	}


	/**
	 * 掲載終了表示をするかどうか
	 */
	public boolean isDisplayEndData() {
		return GourmetCareeUtil.eqInt(MTypeConstants.PublicationEndDisplayFlg.DISPLAY_OK,
				accessor.getPublicationEndDisplayFlg());
	}
}
