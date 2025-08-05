package com.gourmetcaree.admin.pc.information.action.information;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.seasar.framework.beans.util.Beans;

import com.gourmetcaree.admin.pc.information.dto.information.InformationListDto;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.MArea;
import com.gourmetcaree.db.common.entity.TInformation;
import com.gourmetcaree.db.common.service.InformationService;

abstract public class InformationBaseAction extends PcAdminAction {

	/** お知らせのサービスクラス */
	@Resource
	protected InformationService informationService;

	/**
	 * お知らせのエンティティのリストから画面表示用のリストを生成して取得します。
	 * 引数でDtoが持つパスの作成を行います。
	 * @param entityList
	 * @param actionPath
	 * @return
	 */
	protected List<InformationListDto> createInformationList(List<TInformation> entityList, List<MArea> areaList, String actionPath, int managementScreenKbn) {

		if (entityList == null) {
			return new ArrayList<InformationListDto>();
		}

		List<InformationListDto> informationList = new ArrayList<InformationListDto>();


		if(managementScreenKbn == MTypeConstants.ManagementScreenKbn.MY_PAGE_SCREEN) {
			for (MArea area : areaList) {
				InformationListDto dto = new InformationListDto();

				for(TInformation entity : entityList) {
					if(area.areaCd.equals(entity.areaCd)) {
						Beans.copy(entity, dto).execute();
						dto.editPagePath = getEditPath(actionPath, entity.managementScreenKbn, entity.areaCd);
					}else {
						dto.areaCd = area.areaCd;
						dto.managementScreenKbn = MTypeConstants.ManagementScreenKbn.MY_PAGE_SCREEN;
						dto.editPagePath = getEditPath(actionPath, dto.managementScreenKbn, dto.areaCd);
					}
				}
				informationList.add(dto);
			}
		}else {
			for(TInformation entity : entityList) {
				InformationListDto dto = new InformationListDto();
				Beans.copy(entity, dto).execute();
				dto.editPagePath = getEditPath(actionPath, entity.managementScreenKbn, entity.areaCd);
				informationList.add(dto);
			}
		}
		return  informationList;
	}

	/**
	 * 編集画面へのパスを取得
	 * @param actionPath
	 * @param managementScreenKbn
	 * @param areaCd
	 * @return
	 */
	public String getEditPath(String actionPath, Integer managementScreenKbn, Integer areaCd) {
		return GourmetCareeUtil.makePath(actionPath, "index",
				Integer.toString(managementScreenKbn),
				Integer.toString(areaCd));
	}
}
