package com.gourmetcaree.admin.pc.shopList.action.shopList;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.shopList.form.shopList.AjaxForm;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.util.ResponseUtils;
import com.gourmetcaree.db.common.entity.VStationGroup;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.StationGroupService;

@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN, ManageAuthLevel.STAFF, ManageAuthLevel.AGENCY, ManageAuthLevel.OTHER, ManageAuthLevel.SALES})
public class AjaxAction extends ShopListBaseAction {

	@ActionForm
	@Resource
	private AjaxForm ajaxForm;

	@Resource
	private StationGroupService stationGroupService;

	@Execute(validator = false)
	public String searchStation() {
		String searchWord = ajaxForm.searchWord;
		String prefCd = ajaxForm.prefCd;
		List<VStationGroup> list = new ArrayList<>();

		try {
			list = stationGroupService.findByPrefecturesCdAndStationName(prefCd, searchWord);
		} catch (WNoResultException e) {
			//何も処理をしない
		}
        ResponseUtils.writeJson(list);
		return null;
	}
}
