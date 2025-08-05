package com.gourmetcaree.admin.pc.ajax.action.ajax;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ResponseUtil;

import com.gourmetcaree.admin.pc.ajax.form.ajax.ArbeitForm;
import com.gourmetcaree.arbeitsys.logic.ArbeitLabelValueListLogic;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.dto.BaseDto;
import com.gourmetcaree.common.dto.LabelValueDto;
import com.gourmetcaree.common.enums.ManageAuthLevel;

import net.arnx.jsonic.JSON;

/**
 * バイト用ajaxアクション
 * @author Takehiro Nakamori
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN, ManageAuthLevel.STAFF, ManageAuthLevel.AGENCY, ManageAuthLevel.OTHER, ManageAuthLevel.SALES})
public class ArbeitAction {

	public static final String AREA_BLANK_LABEL = "選択してください";

	public static final String TRAIN_BLANK_LABEL = "指定しない";

	private static final String CONTENT_TYPE = "application/json";

	private static final String ENCODING = "UTF-8";

	@ActionForm
	@Resource
	private ArbeitForm arbeitForm;

	@Resource
	private ArbeitLabelValueListLogic arbeitLabelValueListLogic;

	private void writeJson(Object obj) {
		ResponseUtil.write(JSON.encode(obj), CONTENT_TYPE, ENCODING);
	}

	@Execute(validator = false)
	public String areaRailloadSelect() {
		List<LabelValueDto> areaList = arbeitLabelValueListLogic.getAreaList(arbeitForm.todouhukenId, AREA_BLANK_LABEL, "");
		List<LabelValueDto> railList = arbeitLabelValueListLogic.getRailloadList(TRAIN_BLANK_LABEL, "", NumberUtils.toInt(arbeitForm.todouhukenId));

		writeJson(new AreaRailloadDto(areaList, railList));
		return null;
	}


	@Execute(validator = false)
	public String subAreaSelect() {
		List<LabelValueDto> dtoList = arbeitLabelValueListLogic.getSubAreaList(arbeitForm.todouhukenId, arbeitForm.value, AREA_BLANK_LABEL, "");

		writeJson(dtoList);

		return null;
	}

	@Execute(validator = false)
	public String routeSelect() {
		List<LabelValueDto> routeList = arbeitLabelValueListLogic.getRouteList(arbeitForm.value, TRAIN_BLANK_LABEL, "", NumberUtils.toInt(arbeitForm.todouhukenId));
		writeJson(routeList);
		return null;
	}


	@Execute(validator = false)
	public String stationSelect() {
		List<LabelValueDto> dtoList = arbeitLabelValueListLogic.getStationList(arbeitForm.value, TRAIN_BLANK_LABEL, "", NumberUtils.toInt(arbeitForm.todouhukenId));
		writeJson(dtoList);
		return null;
	}






	public static class AreaRailloadDto extends BaseDto {

		/**
		 *
		 */
		private static final long serialVersionUID = 6188501237851325433L;

		public AreaRailloadDto(){}

		public AreaRailloadDto(List<LabelValueDto> areaList, List<LabelValueDto> railloadList) {
			this.areaList = areaList;
			this.railloadList = railloadList;
		}

		public List<LabelValueDto> areaList;

		public List<LabelValueDto> railloadList;
	}
}
