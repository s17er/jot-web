package com.gourmetcaree.shop.pc.scoutFoot.form.member;

import org.apache.commons.collections.CollectionUtils;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.shop.logic.dto.MemberDetailDto;



/**
 * 求職者詳細フォームです。
 * @author Takahiro Kimura
 * @version 1.0
 */
@Component(instance = InstanceType.REQUEST)
public class DetailForm extends MemberForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 272023645700170268L;

	/** 会員ID */
	public String id;

	/** 会員詳細DTO */
	public MemberDetailDto dto;

	/**
	 * 職務履歴があるかどうか
	 * @return
	 */
	public boolean isCareerListExist() {
		if (dto != null && CollectionUtils.isNotEmpty(dto.careerList)) {
			return true;
		}

		return false;
	}

	/**
	 * リセットを行う
	 */
	@Override
	public void resetForm() {
		super.resetForm();
		id = null;
		dto = null;
	}
}
