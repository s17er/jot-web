package com.gourmetcaree.common.builder.dto.impl.careerhistory;

import com.google.common.collect.Lists;
import com.gourmetcaree.accessor.tempMember.CareerHistoryDtoAccessor;
import com.gourmetcaree.common.builder.dto.DtoBuilder;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TAdvancedRegistrationEntryCareerHistory;
import com.gourmetcaree.db.common.service.AdvancedRegistrationEntryCareerHistoryAttributeService;
import com.gourmetcaree.db.common.service.AdvancedRegistrationEntryCareerHistoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.container.SingletonS2Container;

import java.util.List;

/**
 * Created by ZRX on 2017/06/08.
 */
public class AdvancedRegistrationMemberDtoBuilder<E extends CareerHistoryDtoAccessor> implements DtoBuilder<List<E>> {

    private final Logger log = Logger.getLogger(this.getClass());

    private final Integer id;

    private final Class<E> dtoClass;

    public AdvancedRegistrationMemberDtoBuilder(Integer id, Class<E> dtoClass) {
        this.id = id;
        this.dtoClass = dtoClass;
    }

    @Override
    public List<E> build() {
        List<TAdvancedRegistrationEntryCareerHistory> entityList = SingletonS2Container.getComponent(AdvancedRegistrationEntryCareerHistoryService.class)
                .createSelectFromAdvancedRegistrationEntryId(id)
                .getResultList();

        List<E> list = Lists.newArrayList();
        if (CollectionUtils.isEmpty(entityList)) {
            try {
                list.add(dtoClass.newInstance());
            } catch (Exception e) {
                log.error(
                        String.format(
                                "クラス[%s]のnewInstanceに失敗しました。引数なしのコンストラクタがpublicで宣言されているか確認してください。",
                                dtoClass.getName()),
                        e);
            }
            return list;
        }

        for (TAdvancedRegistrationEntryCareerHistory entity : entityList) {
            E dto = Beans.createAndCopy(dtoClass, entity)
                    .execute();

            AdvancedRegistrationEntryCareerHistoryAttributeService attributeService = SingletonS2Container.getComponent(AdvancedRegistrationEntryCareerHistoryAttributeService.class);
            dto.setJobKbnList(attributeService
                    .selectAttributeValueStringListFromCareerHistoryIdAndAttributeCd(entity.id, MTypeConstants.JobKbn.TYPE_CD));

            dto.setIndustryKbnList(attributeService
                    .selectAttributeValueStringListFromCareerHistoryIdAndAttributeCd(entity.id, MTypeConstants.IndustryKbn.TYPE_CD));

            list.add(dto);
        }


        return list;
    }
}
