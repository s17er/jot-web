package com.gourmetcaree.admin.pc.advancedRegistration.form.advancedRegistrationMember;

import com.gourmetcaree.admin.pc.member.dto.member.CareerDto;
import com.gourmetcaree.common.form.advancedregistration.MemberForm;

/**
 * 事前登録会員詳細アクションフォーム
 *
 * @author Takehiro Nakamori
 */
public class DetailForm extends MemberForm<CareerDto> {

    /**
     * シリアルバージョンUID
     */
    private static final long serialVersionUID = -6483307592918859879L;


    @Override
    public Class<CareerDto> getCareerDtoClass() {
        return CareerDto.class;
    }
}
