package com.gourmetcaree.admin.pc.juskill.action.juskillMember;


import java.util.HashMap;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.juskill.form.juskillMember.EditForm;
import com.gourmetcaree.admin.pc.juskill.form.juskillMember.JuskillMemberForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.dto.MaterialDto;
import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.db.common.entity.TJuskillMemberMaterial;
import com.gourmetcaree.db.common.entity.VJuskillMemberList;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.JuskillMemberListService;
import com.gourmetcaree.db.common.service.JuskillMemberMaterialService;
import com.gourmetcaree.db.common.service.JuskillPreviewAccessService;


/**
 * ジャスキル会員アクションクラス
 * @author Makoto Otani
 * @version 1.0
 *
 */
abstract public class JuskillMemberBaseAction extends PcAdminAction {


	/** ジャスキル会員サービス */
	@Resource
	protected JuskillMemberListService juskillMemberListService;

	@Resource
	protected JuskillMemberMaterialService juskillMemberMaterialService;

	@Resource
	protected JuskillPreviewAccessService juskillPreviewAccessService;

	/**
	 * 表示データをフォームにセットする
	 * @param form ジャスキル会員フォーム
	 */
	protected void convertDispData(JuskillMemberForm form) {

		try {
			// フォームにセット
			convertData(form);

		} catch (NumberFormatException e) {
			form.setExistDataFlgNg();
			// idが数値でない場合、データなしと同じエラーを返す。
			throw new ActionMessagesException("errors.app.dataNotFound");
		} catch (WNoResultException e) {
			form.setExistDataFlgNg();
			// データなしのエラーを返す。
			throw new ActionMessagesException("errors.app.dataNotFound");
		}
	}

	/**
	 * ジャスキル会員情報をフォームにセット
	 * @param form ジャスキル会員フォーム
	 * @throws WNoResultException
	 * @throws NumberFormatException
	 */
	private void convertData(JuskillMemberForm form) throws NumberFormatException, WNoResultException {

		VJuskillMemberList vJuskillMember = juskillMemberListService.getDetail(Integer.parseInt(form.id));

		TJuskillMemberMaterial tJuskillMemberMaterial = juskillMemberMaterialService.findJuskillMemberMaterialByJuskillMemberId(form.id);

		//Dateは下で変換
		Beans.copy(vJuskillMember, form).execute();
		if (vJuskillMember.birthday != null) {
			form.birthYear = DateUtils.getDateStr(vJuskillMember.birthday, GourmetCareeConstants.YEAR_FORMAT);
			form.birthMonth = DateUtils.getDateStr(vJuskillMember.birthday, GourmetCareeConstants.SINGLE_MONTH_FORMAT);
			form.birthDate = DateUtils.getDateStr(vJuskillMember.birthday, GourmetCareeConstants.SINGLE_DAY_FORMAT);
		}

		// 職歴リストをセット
		if (CollectionUtils.isNotEmpty(vJuskillMember.mJuskillMemberCareerHistoryList)) {
			form.careerHistoryList = vJuskillMember.mJuskillMemberCareerHistoryList.stream()
					.map(entity -> entity.careerHistory).collect(Collectors.toList());
		}

		if(tJuskillMemberMaterial != null) {
			form.pdfFileName = tJuskillMemberMaterial.fileName;
			StringBuilder fileUrl = new StringBuilder("");
			fileUrl.append(getCommonProperty("gc.sslDomain"));
			fileUrl.append("/kanto");
			fileUrl.append(String.format(getCommonProperty("gc.url.juskill.resume"), tJuskillMemberMaterial.juskillMemberId, tJuskillMemberMaterial.materialData));
			form.resumeFilePath = fileUrl.toString();
		}

		form.existDataFlg = true;

		StringBuilder previewUrl = new StringBuilder("");
		previewUrl.append(getCommonProperty("gc.sslDomain"));
		previewUrl.append(getCommonProperty("gc.preview.url.juskillMember.detail"));
		previewUrl.append(String.format("?id=%s",vJuskillMember.id));
		previewUrl.append(String.format("&access_cd=%s", juskillPreviewAccessService.findPreviewAccessCd(vJuskillMember.id)));
		form.previewUrl = previewUrl.toString();

	}

	protected void initUploadMaterial(EditForm editForm) {
		TJuskillMemberMaterial tJuskillMemberMaterial = juskillMemberMaterialService.findJuskillMemberMaterialByJuskillMemberId(editForm.id);
		if(tJuskillMemberMaterial != null) {
			editForm.pdfFileName = tJuskillMemberMaterial.fileName;

			HashMap<String, MaterialDto> map = new HashMap<String, MaterialDto>();
			MaterialDto dto = new MaterialDto();
			dto.fileName = tJuskillMemberMaterial.fileName;
			dto.contentType = tJuskillMemberMaterial.contentType;
			dto.materialData = tJuskillMemberMaterial.materialData.getBytes();
			dto.materialKbn = String.valueOf(tJuskillMemberMaterial.materialKbn);

			map.put(String.valueOf(tJuskillMemberMaterial.materialKbn), dto);
			editForm.materialMap = map;
		}
	}
}