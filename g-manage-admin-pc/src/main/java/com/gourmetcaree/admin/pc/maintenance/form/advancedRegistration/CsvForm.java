package com.gourmetcaree.admin.pc.maintenance.form.advancedRegistration;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * CSVフォーム
 * @author nakamori
 *
 */
public class CsvForm implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -1371865936977824459L;

	/** 事前登録ID */
	public String advancedRegistrationId;


	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
