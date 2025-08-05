package com.gourmetcaree.admin.pc.maintenance.action.advancedRegistration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.maintenance.form.advancedRegistration.ListForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.constants.MAreaConstants;
import com.gourmetcaree.db.common.entity.MAdvancedRegistration;

@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN})
public class ListAction extends AbstractAdvancedRegistrationBaseAction {

	private static final String INPUT_PATH_FORMAT = "/%s/advancedRegistration/temp/%d";

	private static final String EDIT_PATH_FORMAT = "/%s/advancedRegistration/editLogin/%d";

	@Resource
	@ActionForm
	private ListForm listForm;

	/** 事前登録のリスト */
	private List<AdvancedRegistrationListDto> registrationList;


	@Execute(validator = false, input = TransitionConstants.Maintenance.JSP_APJ06L01)
	@MethodAccess(accessCode = "ADVANCE_LIST_INDEX")
	public String index() {
		return show();
	}

	private String show() {
		createRegistrationList();
		return TransitionConstants.Maintenance.JSP_APJ06L01;
	}





	/**
	 * 事前登録のリストの作成
	 */
	private void createRegistrationList() {

		final String contextName = getCommonProperty("gc.front-pc.context");
		List<AdvancedRegistrationListDto> list =
				advancedRegistrationService.selectAll().orderBy(SqlUtils.desc(MAdvancedRegistration.ID))
				.iterate(new IterationCallback<MAdvancedRegistration, List<AdvancedRegistrationListDto>>() {
					List<AdvancedRegistrationListDto> retList = new ArrayList<ListAction.AdvancedRegistrationListDto>();

					@Override
					public List<AdvancedRegistrationListDto> iterate(MAdvancedRegistration entity,
							IterationContext context) {
						if (entity == null) {
							return retList;
						}

						AdvancedRegistrationListDto dto = Beans.createAndCopy(AdvancedRegistrationListDto.class, entity).execute();
						dto.inputPath = String.format(INPUT_PATH_FORMAT, MAreaConstants.Prefix.getRenewalAreaName(dto.areaCd), entity.id);
						dto.editPath = String.format(EDIT_PATH_FORMAT,  MAreaConstants.Prefix.getRenewalAreaName(dto.areaCd), entity.id);

						retList.add(dto);
						return retList;
					}
				});

		if (CollectionUtils.isEmpty(list)) {
			listForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.dataNotFound");
		}

		listForm.setExistDataFlgOk();
		registrationList = list;
	}




	/**
	 * 事前登録リストの取得
	 * @return 事前登録リスト
	 */
	public List<AdvancedRegistrationListDto> getRegistrationList() {
		if (registrationList == null) {
			return new ArrayList<ListAction.AdvancedRegistrationListDto>(0);
		}
		return registrationList;
	}




	/**
	 * 事前登録のリスト用DTO
	 * @author Takehiro Nakamori
	 *
	 */
	public static class AdvancedRegistrationListDto extends MAdvancedRegistration {

		/**
		 *
		 */
		private static final long serialVersionUID = -6545768454875031886L;

		/** 日付フォーマット */
		private static SimpleDateFormat dateFormat = new SimpleDateFormat(GourmetCareeConstants.DATE_TIME_FORMAT);

		/** 仮登録画面パス */
		public String inputPath;

		/** 編集ログインパス */
		public String editPath;

		/**
		 * 開始日時文字列の取得
		 * @return 開始日時文字列
		 */
		public String getTermStartDatetimeStr() {
			if (termStartDatetime == null) {
				return "";
			}

			return dateFormat.format(termStartDatetime);
		}


		/**
		 * 終了日時文字列の取得
		 * @return 終了日時文字列
		 */
		public String getTermEndDatetimeStr() {
			if (termEndDatetime == null) {
				return "";
			}

			return dateFormat.format(termEndDatetime);
		}
	}
}
