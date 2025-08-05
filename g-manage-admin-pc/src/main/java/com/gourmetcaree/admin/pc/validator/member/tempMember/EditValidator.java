package com.gourmetcaree.admin.pc.validator.member.tempMember;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionMessages;

import com.gourmetcaree.admin.pc.member.dto.member.CareerDto;
import com.gourmetcaree.admin.service.accessor.tempMember.DetailAccessor;
import com.gourmetcaree.common.util.ActionMessageUtil;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;

public class EditValidator {

	private final ActionMessages errors;

	private final DetailAccessor<CareerDto> accessor;

	public EditValidator(ActionMessages errors, DetailAccessor<CareerDto> accessor) {
		this.errors = errors;
		this.accessor = accessor;
	}


	public void validate() {
		validateAreaList();
	}

	private void validateAreaList() {
		if (ArrayUtils.isEmpty(accessor.getAreaList())) {
			ActionMessageUtil.addActionMessage(errors, "errors.required", "labels.memberAreaList");
		}
	}

	private void checkLoginId(DetailAccessor<CareerDto> accessor, String mail) {
		if (StringUtils.isBlank(mail)) {
			final String mailLabel = GourmetCareeUtil.eqInt(MTypeConstants.TerminalKbn.MOBILE_VALUE, accessor.getTerminalKbn()) ?
													"labels.mobileMail" : "labels.pcMail";

			ActionMessageUtil.addActionMessage(errors, "errors.loginId", mailLabel);
		}
	}
}
