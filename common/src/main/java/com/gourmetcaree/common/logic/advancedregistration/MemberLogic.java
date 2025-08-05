package com.gourmetcaree.common.logic.advancedregistration;

import java.text.ParseException;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.NoResultException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.accessor.tempMember.CareerHistoryDtoAccessor;
import com.gourmetcaree.common.builder.dto.DtoBuilder;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.form.advancedregistration.MemberForm;
import com.gourmetcaree.common.logic.AbstractGourmetCareeLogic;
import com.gourmetcaree.common.logic.ValueToNameConvertLogic;
import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.common.util.DigestUtil;
import com.gourmetcaree.common.util.GcCollectionUtils;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.MMember;
import com.gourmetcaree.db.common.entity.TAdvancedRegistrationEntry;
import com.gourmetcaree.db.common.entity.TAdvancedRegistrationEntryAttribute;
import com.gourmetcaree.db.common.entity.TAdvancedRegistrationEntryCareerHistory;
import com.gourmetcaree.db.common.entity.TAdvancedRegistrationEntryCareerHistoryAttribute;
import com.gourmetcaree.db.common.entity.TAdvancedRegistrationEntryLoginHistory;
import com.gourmetcaree.db.common.entity.TAdvancedRegistrationEntrySchoolHistory;
import com.gourmetcaree.db.common.service.AdvancedRegistrationEntryAttributeService;
import com.gourmetcaree.db.common.service.AdvancedRegistrationEntryCareerHistoryAttributeService;
import com.gourmetcaree.db.common.service.AdvancedRegistrationEntryCareerHistoryService;
import com.gourmetcaree.db.common.service.AdvancedRegistrationEntryLoginHistoryService;
import com.gourmetcaree.db.common.service.AdvancedRegistrationEntrySchoolHistoryService;
import com.gourmetcaree.db.common.service.AdvancedRegistrationEntryService;
import com.gourmetcaree.db.common.service.MemberService;

/**
 * 事前登録会員の処理を行うロジック
 * Created by ZRX on 2017/06/08.
 */
public class MemberLogic extends AbstractGourmetCareeLogic {

    @Resource(name = "advancedRegistrationEntryService")
    private AdvancedRegistrationEntryService service;

    @Resource(name = "advancedRegistrationEntryAttributeService")
    private AdvancedRegistrationEntryAttributeService attributeService;

    @Resource(name = "advancedRegistrationEntryLoginHistoryService")
    private AdvancedRegistrationEntryLoginHistoryService loginHistoryService;

    @Resource(name = "advancedRegistrationEntrySchoolHistoryService")
    private AdvancedRegistrationEntrySchoolHistoryService schoolHistoryService;

    @Resource
    private MemberService memberService;

    @Resource(name = "advancedRegistrationEntryCareerHistoryService")
    private AdvancedRegistrationEntryCareerHistoryService careerHistoryService;

    @Resource(name = "advancedRegistrationEntryCareerHistoryAttributeService")
    private AdvancedRegistrationEntryCareerHistoryAttributeService careerHistoryAttributeService;

    @Resource
    private ValueToNameConvertLogic valueToNameConvertLogic;

    /**
     * 詳細データの作成
     */
    public <T extends CareerHistoryDtoAccessor> void createDetailData(MemberForm<T> detailForm) {
        try {

            TAdvancedRegistrationEntry entry = service.findById(NumberUtils.toInt(detailForm.id));
            Beans.copy(entry, detailForm)
                    .excludes("registrationDatetime", "password")
                    .execute();

            detailForm.registrationDatetime = DateUtils.getDateStr(entry.registrationDatetime, GourmetCareeConstants.DATE_TIME_FORMAT);

            detailForm.formatBirthDay(entry.birthday);

            detailForm.careerList = createCareerHistory(entry.id, detailForm.getCareerDtoClass());
            createSchoolHistory(detailForm, entry.id);


            createAttribute(detailForm, entry);

            TAdvancedRegistrationEntryLoginHistory history
                    = loginHistoryService.findByAdvancedRegistrationEntryId(entry.id);

            if (history != null) {
                detailForm.lastLoginDatetime = DateUtils.getDateStr(history.lastLoginDatetime, GourmetCareeConstants.DATE_TIME_FORMAT);
            }


            Integer advancedMemberKbn = null;
            if (entry.advancedRegistrationUserId == null) {
                detailForm.memberKbn = MTypeConstants.MemberKbn.ADVANCED_REGISTRATION_MEMBER;
                advancedMemberKbn = MTypeConstants.MemberKbn.ADVANCED_REGISTRATION_MEMBER;
            } else {
                try {
                    MMember member = memberService.findById(entry.advancedRegistrationUserId);
                    detailForm.memberKbn = member.memberKbn;
                    advancedMemberKbn = MTypeConstants.MemberKbn.ADVANCED_REGISTRATION_NEW_MEMBER;
                } catch (SNoResultException e) {
                    detailForm.memberKbn = MTypeConstants.MemberKbn.ADVANCED_REGISTRATION_MEMBER;
                    advancedMemberKbn = MTypeConstants.MemberKbn.ADVANCED_REGISTRATION_MEMBER;
                }
            }

            detailForm.advancedRegistrationKbn = GourmetCareeUtil.convertMemberKbnToAdvancedStatusKbn(advancedMemberKbn);




            detailForm.setExistDataFlgOk();
        } catch (SNoResultException e) {
            detailForm.setExistDataFlgNg();
            throw new ActionMessagesException("errors.app.dataNotFound");
        }
    }




    /**
     * 学歴を作成します。
     * @param advancedRegistrationEntryId 事前登録エントリID
     */
    private <T extends CareerHistoryDtoAccessor> void createSchoolHistory(MemberForm<T> detailForm, Integer advancedRegistrationEntryId) {

        try {
            TAdvancedRegistrationEntrySchoolHistory history = schoolHistoryService.findByAdvancedRegistrationEntryId(advancedRegistrationEntryId);
            Beans.copy(history, detailForm).excludes("id", "version").execute();
        } catch (SNoResultException e) {
            // 何もしない
        }
    }


    /**
     * 属性を作成します。
     */
    private <T extends CareerHistoryDtoAccessor> void createAttribute(MemberForm<T> detailForm, TAdvancedRegistrationEntry entry) {
        final Integer id = entry.id;

        detailForm.industryKbnList = GcCollectionUtils.toStringArray(
                attributeService.selectAttributeValueList(id, MTypeConstants.IndustryKbn.TYPE_CD));
        detailForm.jobKbnList = GcCollectionUtils.toStringArray(
                attributeService.selectAttributeValueList(id, MTypeConstants.JobKbn.TYPE_CD));
        detailForm.qualificationKbnList = GcCollectionUtils.toStringArray(
                attributeService.selectAttributeValueList(id, MTypeConstants.QualificationKbn.TYPE_CD));

        List<Integer> hopeList = attributeService.selectAttributeValueList(id, MTypeConstants.HopeCareerChangeTerm.TYPE_CD);
        detailForm.hopeCareerChangeStr = createHopeTermStr(entry.hopeCareerChangeYear, entry.hopeCareerChangeMonth, hopeList);
        detailForm.hopeCareerTermList = attributeService.selectAttributeValueStringArray(id, MTypeConstants.HopeCareerChangeTerm.TYPE_CD);

        detailForm.currentSituationKbn = String.valueOf(attributeService.selectAttributeValue(
                id,
                MTypeConstants.CurrentSituationKbn.TYPE_CD));
    }


    /**
     * 転職希望時期文字列作成
     */
    public String createHopeTermStr(String year, String month, List<Integer> kbnList) {
        if (StringUtils.isNotBlank(year)
                && StringUtils.isNotBlank(month)) {
            return String.format("%s年 %s月", year, month);
        }

        if (CollectionUtils.isNotEmpty(kbnList)) {
            return valueToNameConvertLogic.convertToTypeName(MTypeConstants.HopeCareerChangeTerm.TYPE_CD, kbnList.get(0));
        }

        return "";
    }


    /**
     * 職歴を作成します。
     * @param id 事前登録ID
     */
    public <T extends CareerHistoryDtoAccessor> List<T> createCareerHistory(Integer id, Class<T> dtoClass) {
        return DtoBuilder.Factory.getAdvancedRegistrationMemberDtoBuilder(id, dtoClass)
                .build();
    }


    public <T extends CareerHistoryDtoAccessor> void update(MemberForm<T> form) {
        TAdvancedRegistrationEntry entity = Beans.createAndCopy(TAdvancedRegistrationEntry.class, form)
                                            .excludes("registrationDatetime", "birthday", "password")
                                            .execute();

        try {
            entity.birthday = form.parseBirthDay();
        } catch (ParseException e) {
            throw new FraudulentProcessException("日付変換に失敗しました。", e);
        }

        if (StringUtils.isNotBlank(form.password)) {
            entity.password = DigestUtil.createDigest(form.password);
        }

        logicLog.info(String.format("version:%s", entity.version));
        service.update(entity);
        updateAttribute(form, entity.id);
        updateSchoolHistory(form, entity.id);
        updateCareerHistories(form, entity.id);


    }


    /**
     * 事前登録会員の属性を更新
     * @param form フォーム
     * @param entryId 事前登録会員ID
     */
    private void updateAttribute(MemberForm form, Integer entryId) {
        attributeService.deleteEntityForadvancedRegistrationEntryId(entryId);
        insertAttribute(form, entryId);
    }


    /**
     * 事前登録会員の属性をインサート
     * @param form フォーム
     * @param entryId 事前登録会員ID
     */
    private void insertAttribute(MemberForm form, Integer entryId) {
        if (!ArrayUtils.isEmpty(form.industryKbnList)) {
            for (String value : form.industryKbnList) {
                insertAttribute(entryId,
                        value,
                        MTypeConstants.IndustryKbn.TYPE_CD);
            }
        }

        if (!ArrayUtils.isEmpty(form.jobKbnList)) {
            for (String value : form.jobKbnList) {
                insertAttribute(entryId,
                        value,
                        MTypeConstants.JobKbn.TYPE_CD);
            }
        }

        if (!ArrayUtils.isEmpty(form.qualificationKbnList)) {
            for (String value : form.qualificationKbnList) {
                insertAttribute(entryId,
                        value,
                        MTypeConstants.QualificationKbn.TYPE_CD);
            }
        }

        // 属性コードに転職希望時期をセット
        if (!ArrayUtils.isEmpty(form.hopeCareerTermList)) {
            for (String kbn : form.hopeCareerTermList) {
                TAdvancedRegistrationEntryAttribute attribute = new TAdvancedRegistrationEntryAttribute();
                attribute.attributeCd = MTypeConstants.HopeCareerChangeTerm.TYPE_CD;
                attribute.advancedRegistrationEntryId = entryId;
                try {
                    attribute.attributeValue = Integer.parseInt(kbn);
                } catch (NumberFormatException e) {
                    throw new FraudulentProcessException("転職希望時期の値が不正です。", e);
                }
                insertAttribute(attribute);
            }

        }

        insertAttribute(
                entryId,
                form.currentSituationKbn,
                MTypeConstants.CurrentSituationKbn.TYPE_CD);
    }


    /**
     * 事前登録会員の属性をインサート
     * @param entryId 事前登録会員ID
     * @param value 属性値
     * @param code 属性コード
     */
    private void insertAttribute(Integer entryId, String value, String code) {
        TAdvancedRegistrationEntryAttribute entity =
                new TAdvancedRegistrationEntryAttribute(
                        entryId,
                        NumberUtils.toInt(value),
                        code);
        insertAttribute(entity);
    }

    /**
     * 事前登録会員の属性をインサート
     */
    private void insertAttribute(TAdvancedRegistrationEntryAttribute entity) {
        attributeService.insert(entity);

        logicLog.info(String.format("事前登録会員ID[%d]の属性をインサートしました。エンティティ：[%s]",
                entity.advancedRegistrationEntryId,
                entity));
    }


    /**
     * 事前登録の学歴を更新(DEL-IN)
     * @param form フォーム
     * @param entryId 事前登録会員ID
     */
    private void updateSchoolHistory(MemberForm form, Integer entryId) {
        schoolHistoryService.deleteSchoolHistoryByAdvancedRegistrationEntryId(entryId);
        insertSchoolHistory(form, entryId);
        logicLog.info("事前登録会員の学歴を更新(DEL-IN)しました。");
    }


    /**
     * 事前登録の学歴をインサート
     * @param form フォーム
     * @param entryId 事前登録会員ID
     */
    private void insertSchoolHistory(MemberForm form, Integer entryId) {
        TAdvancedRegistrationEntrySchoolHistory entity =
                Beans.createAndCopy(TAdvancedRegistrationEntrySchoolHistory.class, form)
                        .excludes("id")
                        .execute();
        entity.advancedRegistrationEntryId = entryId;
        schoolHistoryService.insert(entity);

        logicLog.info(String.format("事前登録の学歴をインサートしました。　エンティティ：[%s]",
                entity));
    }



    /**
     * 事前登録会員の職歴を更新(DEL-IN)
     * @param form フォーム
     * @param entryId 事前登録ID
     */
    private <T extends CareerHistoryDtoAccessor> void updateCareerHistories(MemberForm<T> form, Integer entryId) {
        careerHistoryService.deleteByCareerHistoryId(entryId);
        insertCareerHistories(form, entryId);
    }


    /**
     * 事前登録会員の職歴をインサート
     * @param form フォーム
     * @param entryId 事前登録ID
     */
    private <T extends CareerHistoryDtoAccessor> void insertCareerHistories(MemberForm<T> form, Integer entryId) {
        for (T accessor : form.careerList) {
            TAdvancedRegistrationEntryCareerHistory entity =
                    Beans.createAndCopy(TAdvancedRegistrationEntryCareerHistory.class, accessor)
                    .execute();
            entity.advancedRegistrationEntryId = entryId;
            careerHistoryService.insert(entity);

            insertCareerHistoryAttributes(accessor, entity.id);
            logicLog.info(String.format("事前登録ID[%d]の職歴(ID:[%d])をインサートしました。エンティティ：[%s]",
                    entryId,
                    entity.id,
                    entity));

        }
    }


    /**
     * 事前登録職歴の属性をインサート
     * @param accessor 職歴DTOアクセサ
     * @param careerHistoryId 職歴ID
     */
    private void insertCareerHistoryAttributes(CareerHistoryDtoAccessor accessor, Integer careerHistoryId) {
        if (CollectionUtils.isNotEmpty(accessor.getJobKbnList())) {
            for (String value : accessor.getJobKbnList()) {
                insertCareerHistoryAttribute(careerHistoryId,
                                value,
                                MTypeConstants.JobKbn.TYPE_CD);
            }
        }

        if (CollectionUtils.isNotEmpty(accessor.getIndustryKbnList())) {
            for (String value : accessor.getIndustryKbnList()) {
                insertCareerHistoryAttribute(careerHistoryId,
                        value,
                        MTypeConstants.IndustryKbn.TYPE_CD);
            }
        }

    }

    /**
     * 事前登録職歴の属性をインサート
     * @param careerId 職歴ID
     * @param value 属性値
     * @param code 属性コード
     */
    private void insertCareerHistoryAttribute(Integer careerId, String value, String code) {
        TAdvancedRegistrationEntryCareerHistoryAttribute entity = new TAdvancedRegistrationEntryCareerHistoryAttribute();
        entity.advancedRegistrationEntryCareerHistoryId = careerId;
        entity.attributeValue = Integer.parseInt(value);
        entity.attributeCd = code;
        careerHistoryAttributeService.insert(entity);

        logicLog.info(String.format("事前登録職歴ID[%d]の属性をインサートしました。エンティティ：[%s]",
                careerId,
                entity));
    }

    /**
     * 来場ステータスをトグル
     * @param id 事前登録ID
     * @return トグル後のステータス
     */
    public String toggleAttendedStatus(Integer id) {
        if (id == null) {
            return null;
        }

        try {
            TAdvancedRegistrationEntry entity = service.findById(id);

            if (entity.attendedStatus == MTypeConstants.AdvancedRegistrationAttendedStatus.ATTENDED) {
                entity.attendedStatus = MTypeConstants.AdvancedRegistrationAttendedStatus.NOT_ATTENDED;
            } else {
                entity.attendedStatus = MTypeConstants.AdvancedRegistrationAttendedStatus.ATTENDED;
            }

            // 必要なところだけをアップデート
            TAdvancedRegistrationEntry updateEntity = new TAdvancedRegistrationEntry();
            updateEntity.id = entity.id;
            updateEntity.attendedStatus = entity.attendedStatus;
            updateEntity.version = entity.version;
            service.updateIncludesVersion(updateEntity);

            return valueToNameConvertLogic.convertToTypeName(MTypeConstants.AdvancedRegistrationAttendedStatus.TYPE_CD, entity.attendedStatus);
        } catch (NoResultException e) {
            return null;
        }

    }
}
