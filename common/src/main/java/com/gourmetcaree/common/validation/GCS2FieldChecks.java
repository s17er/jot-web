package com.gourmetcaree.common.validation;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.Validator;
import org.apache.commons.validator.ValidatorAction;
import org.apache.struts.action.ActionMessages;
import org.seasar.struts.validator.S2FieldChecks;

import com.gourmetcaree.common.util.GourmetCareeUtil;

public class GCS2FieldChecks extends S2FieldChecks {

	private static final long serialVersionUID = -6189751073654643052L;

	public static boolean validateMailAddress(Object bean, ValidatorAction validatorAction, Field field,
			ActionMessages errors, Validator validator, HttpServletRequest request) {
		String value = getValueAsString(bean, field);

		boolean notWhiteSpace = Boolean.valueOf(field.getVarValue("notWhiteSpace")).booleanValue();

		if (!GenericValidator.isBlankOrNull(value)) {
			if (!GourmetCareeUtil.checkMailAddress(value)) {
				addError(errors, field, validator, validatorAction, request);
				return false;
			}

		} else if (notWhiteSpace && !StringUtils.isEmpty(value) && !GourmetCareeUtil.checkMailAddress(value)) {

			addError(errors, field, validator, validatorAction, request);
			return false;

		}

		return true;
	}

}
