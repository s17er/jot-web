package com.gourmetcaree.admin.pc.preview.action.listPreview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;

import com.gourmetcaree.admin.pc.preview.form.listPreview.PreviewBaseForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.webdata.form.webdata.EditForm;
import com.gourmetcaree.admin.pc.webdata.form.webdata.InputForm;
import com.gourmetcaree.admin.pc.webdata.form.webdata.WebdataForm;
import com.gourmetcaree.common.dto.PreviewDto;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.db.common.constants.MAreaConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants.SizeKbn;
import com.gourmetcaree.db.common.service.MaterialService;
import com.gourmetcaree.db.common.service.TypeService;

public abstract class PreviewBaseAction extends PcAdminAction {

	@Resource
	protected TypeService typeService;

	/** 素材サービス */
	@Resource
	protected MaterialService materialService;

	/**
	 * 入力フォームを作成します。
	 * @return
	 */
	private WebdataForm createInputForm(String inputFormKbn) {
		S2Container container = SingletonS2ContainerFactory.getContainer();
		WebdataForm form;
		if (PreviewBaseForm.INPUT_FORM_KBN_INPUT.equals(inputFormKbn)) {
			form = (WebdataForm) container.getComponent(InputForm.class);
		} else if (PreviewBaseForm.INPUT_FORM_KBN_EDIT.equals(inputFormKbn)) {
			form = (WebdataForm) container.getComponent(EditForm.class);
		} else {
			throw new FraudulentProcessException("入力フォームが不正です。");
		}
		return form;
	}

	/**
	 * 入力フォームからプレビューDTOを作成します。
	 * @return
	 */
	protected PreviewDto createPreviewDtoFromInputForm(String inputFormKbn) {
		WebdataForm form = createInputForm(inputFormKbn);

		PreviewDto dto = new PreviewDto();
		Beans.copy(form, dto)
				.excludes("otherConditionKbnList", "treatmentKbnList")
				.execute();

		sortKbnList(form, dto);

		dto.employPtnList = form.employPtnKbnList;
		if (MAreaConstants.AreaCd.SHUTOKEN_AREA.toString().equals(form.areaCd)) {
			dto.areaCd = MAreaConstants.AreaCd.SHUTOKEN_AREA;
		}


		dto.fromInputFlg = true;

		//画像アップ情報を保持
		if (form.materialMap != null) {

			Map<String, Boolean> map = new HashMap<String, Boolean>();

			Set<String> keySet = form.materialMap.keySet();
			for (String mateerialKbn : keySet) {
				map.put(mateerialKbn, true);
			}

			dto.imageCheckMap = map;
			//一覧プレビュー用情報をセット
			if (String.valueOf(SizeKbn.D).equals(form.sizeKbn) || String.valueOf(SizeKbn.E).equals(form.sizeKbn)) {
				dto.materialSearchListExistFlg = form.materialMap.containsKey(MTypeConstants.MaterialKbn.FREE);
			} else if (String.valueOf(SizeKbn.A ).equals(form.sizeKbn) || String.valueOf(SizeKbn.B).equals(form.sizeKbn) || String.valueOf(SizeKbn.C).equals(form.sizeKbn)) {
				dto.materialSearchListExistFlg = form.materialMap.containsKey(MTypeConstants.MaterialKbn.MAIN_1);
			} else {
				dto.materialSearchListExistFlg = false;
			}
			dto.materialCaptionAExistFlg = form.materialMap.containsKey(MTypeConstants.MaterialKbn.PHOTO_A);
			dto.materialCaptionBExistFlg = form.materialMap.containsKey(MTypeConstants.MaterialKbn.PHOTO_B);
		}

		if (form.customerDto != null) {
			dto.customerId = form.customerDto.id;
		}
		return dto;
	}


	/**
	 * 区分リストをソートします。
	 * @param form
	 * @param dto
	 */
	private void sortKbnList(WebdataForm form, PreviewDto dto) {
		dto.otherConditionKbnList = new ArrayList<String>();
		if (CollectionUtils.isNotEmpty(form.otherConditionKbnList)) {
			List<Integer> otherConditionList = typeService.getTypeValueList(MTypeConstants.OtherConditionKbn.TYPE_CD);
			for (Integer otherConditionKbn : otherConditionList) {
				String kbn = String.valueOf(otherConditionKbn);
				if (form.otherConditionKbnList.contains(kbn)) {
					dto.otherConditionKbnList.add(kbn);
				}
			}
		}

		dto.treatmentKbnList = new ArrayList<String>();
		if (CollectionUtils.isNotEmpty(form.treatmentKbnList)) {
			List<Integer> treatmentList = typeService.getTypeValueList(MTypeConstants.TreatmentKbn.TYPE_CD);
			for (Integer treatmentKbn : treatmentList) {
				String kbn = String.valueOf(treatmentKbn);
				if (form.treatmentKbnList.contains(kbn)) {
					dto.treatmentKbnList.add(kbn);
				}
			}
		}
	}
}
