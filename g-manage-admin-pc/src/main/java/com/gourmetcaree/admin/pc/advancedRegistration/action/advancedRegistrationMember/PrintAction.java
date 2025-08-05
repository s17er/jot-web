package com.gourmetcaree.admin.pc.advancedRegistration.action.advancedRegistrationMember;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.advancedRegistration.form.advancedRegistrationMember.ListForm;
import com.gourmetcaree.admin.pc.member.dto.member.PrintMemberDto;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.dto.AdvancedRegistrationSearchResultDto;
import com.gourmetcaree.admin.service.logic.AdvancedRegistrationLogic.SearchProperty;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TAdvancedRegistrationEntrySchoolHistory;
import com.gourmetcaree.db.common.service.TypeService;

/**
 * 印刷アクション
 * @author Takehiro Nakamori
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN, ManageAuthLevel.STAFF, ManageAuthLevel.SALES} )
public class PrintAction extends AdvancedRegisterdMemberBaseAction {

	/** リストフォーム(非アクションフォーム) */
	@Resource
	private ListForm listForm;

	/** タイプサービス */
	@Resource
	private TypeService typeService;

	/** 印刷する会員リスト */
	private List<PrintMemberDto> printMemberDtoList;


	/**
	 * 初期表示画面
	 * @return JSPパス
	 */
	@Execute(validator = false, input = TransitionConstants.AdvancedRegistration.JSP_APH01L01)
	public String index() {
		return show();
	}

	/**
	 * 表示内容を作成します。
	 * @return JSPパス
	 */
	private String show() {

		// 検索プロパティ。 印刷は全件出すので、表示件数はなんでもいい。
		SearchProperty property = createProperty(0, listForm);

		advancedRegistrationLogic.doSearchPrint(property);

		printMemberDtoList = property.getSelect()
			.iterate(new IterationCallback<AdvancedRegistrationSearchResultDto,
					List<PrintMemberDto>>() {

				private List<PrintMemberDto> list = new ArrayList<PrintMemberDto>(0);

				@Override
				public List<PrintMemberDto> iterate(AdvancedRegistrationSearchResultDto entity, IterationContext context) {
					if (entity != null) {
						list.add(createPrintMemberDto(entity));
					}
					return list;
				}
		});

		if (CollectionUtils.isEmpty(printMemberDtoList)) {
			throw new ActionMessagesException("errors.app.dataNotFound");
		}

		return TransitionConstants.AdvancedRegistration.JSP_APH02L01;
	}



	/**
	 * 印刷する会員DTOを作成します
	 * @param resultDto 検索結果DTO
	 * @return 印刷する会員DTO
	 */
	private PrintMemberDto createPrintMemberDto(AdvancedRegistrationSearchResultDto resultDto) {
		PrintMemberDto dto = Beans.createAndCopy(PrintMemberDto.class, resultDto)
									.execute();


		dto.industryKbnList = advancedRegistrationEntryAttributeService.selectAttributeValueList(resultDto.id, MTypeConstants.IndustryKbn.TYPE_CD);
		dto.jobKbnList = advancedRegistrationEntryAttributeService.selectAttributeValueList(resultDto.id, MTypeConstants.JobKbn.TYPE_CD);
		dto.qualificationKbnList = advancedRegistrationEntryAttributeService.selectAttributeValueList(resultDto.id, MTypeConstants.QualificationKbn.TYPE_CD);


		dto.careerDtoList = createCareerHistory(resultDto.id);

		List<Integer> hopeList = advancedRegistrationEntryAttributeService.selectAttributeValueList(resultDto.id, MTypeConstants.HopeCareerChangeTerm.TYPE_CD);
		dto.hopeCareerChangeStr = createHopeTermStr(resultDto.hopeCareerChangeYear, resultDto.hopeCareerChangeMonth, hopeList);

		dto.currentSituationKbn = advancedRegistrationEntryAttributeService.selectAttributeValue(resultDto.id, MTypeConstants.CurrentSituationKbn.TYPE_CD);

		convertSchoolHistoryData(dto);

		dto.advancedRegistrationName = advancedRegistrationService.findById(resultDto.advancedRegistrationId).advancedRegistrationName;
		return dto;
	}



//	/**
//	 * 印刷用に職歴DTOリストを作成します
//	 * @param id 会員ID XXX 分離後は、事前登録IDに変更。ロジックも変わる
//	 * @return 印刷用職歴DTOリスト
//	 */
//	private List<CareerDto> createCareerHistoryForPrint(Integer id) {
//		List<TCareerHistory> careerList;
//		try {
//			careerList = careerHistoryService.getCareerHistoryDataByMemberId(id);
//		} catch (WNoResultException e) {
//          TODO ここで何かしているので、調査。 必要であれば、このメソッドを編集して使用する。
//			List<CareerDto> dtoList = new ArrayList<CareerDto>();
//			dtoList.add(new CareerDto());
//			return dtoList;
//		}
//
//		List<CareerDto> dtoList = new ArrayList<CareerDto>();
//		for (TCareerHistory history : careerList) {
//			if (dtoList.size() >= 2) {
//				break;
//			}
//			CareerDto dto = new CareerDto();
//			Beans.copy(history, dto).execute();
//
//			List<String> jobList = new ArrayList<String>();
//			for (TCareerHistoryAttribute attrEntity : history.tCareerHistoryAttributeList) {
//				jobList.add(String.valueOf(attrEntity.attributeValue));
//			}
//
//			dto.job = jobList.toArray(new String[0]);
//			dtoList.add(dto);
//		}
//
//		return dtoList;
//	}




	/**
	 * 学歴情報をフォームにセット
	 * @param form 会員フォーム
	 */
	private void convertSchoolHistoryData(PrintMemberDto dto) throws NumberFormatException {

		try {
			TAdvancedRegistrationEntrySchoolHistory history = advancedRegistrationEntrySchoolHistoryService.findByAdvancedRegistrationEntryId(dto.id);
			Beans.copy(history, dto).excludes("id", "version").execute();

		} catch (SNoResultException e) {
			// 学歴情報がない場合は、何も処理しない。
		}
	}


	/**
	 * 印刷用DTOリストを取得します
	 * @return 印刷用DTOリスト
	 */
	public List<PrintMemberDto> getPrintMemberDtoList() {
		return printMemberDtoList;
	}
}
