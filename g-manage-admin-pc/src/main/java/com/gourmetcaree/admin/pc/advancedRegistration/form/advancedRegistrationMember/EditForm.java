package com.gourmetcaree.admin.pc.advancedRegistration.form.advancedRegistrationMember;

import com.gourmetcaree.admin.pc.member.dto.member.CareerDto;
import com.gourmetcaree.common.form.advancedregistration.MemberForm;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * 事前登録会員変更フォーム
 * Created by ZRX on 2017/06/08.
 */
@Component(instance = InstanceType.SESSION)
public class EditForm extends MemberForm<CareerDto> {



    /**
     * シリアルバージョンUID
     */
    private static final long serialVersionUID = 3006793751332736989L;

    @Override
    public Class<CareerDto> getCareerDtoClass() {
        return CareerDto.class;
    }
}
