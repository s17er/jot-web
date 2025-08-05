package com.gourmetcaree.common.builder.dto;

import com.gourmetcaree.accessor.tempMember.CareerHistoryDtoAccessor;
import com.gourmetcaree.common.builder.dto.impl.careerhistory.AdvancedRegistrationMemberDtoBuilder;

import java.util.List;

/**
 * DTOを作成するためのビルダ
 * Created by ZRX on 2017/06/08.
 */
public interface DtoBuilder<DTO> {

    /**
     * DTO作成
     * @return DTO
     */
    DTO build();


    class Factory {
        public static <T extends CareerHistoryDtoAccessor> DtoBuilder<List<T>> getAdvancedRegistrationMemberDtoBuilder(Integer id, Class<T> dtoClass) {
            return new AdvancedRegistrationMemberDtoBuilder<T>(id, dtoClass);
        }

    }
}
