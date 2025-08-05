package com.gourmetcaree.admin.pc.customerImage.action.customerImage;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.Beans;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.MessageResourcesUtil;

import com.gourmetcaree.admin.pc.customerImage.dto.customerImage.UpImageResponse;
import com.gourmetcaree.admin.pc.customerImage.form.customerImage.ListForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.action.util.CustomerImageAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.dto.customerImage.ImageDto;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.FileUploadException;
import com.gourmetcaree.common.logic.customerImage.CustomerImageLogic;
import com.gourmetcaree.db.common.entity.TCustomerImage;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.CustomerImageService;

@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN, ManageAuthLevel.STAFF, ManageAuthLevel.AGENCY, ManageAuthLevel.OTHER, ManageAuthLevel.SALES})
public class ListAction extends PcAdminAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(ListAction.class);

	@ActionForm
	@Resource
	public ListForm listForm;

	@Resource
	private CustomerImageLogic customerImageLogic;

	@Resource
	private CustomerImageService customerImageService;

	public List<ImageDto> imageList;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset="resetForm", urlPattern = "index/{customerId}", input = TransitionConstants.Customer.JSP_APD03L01)
	public String index() {
		try {
			imageList = createImageDtoList(NumberUtils.toInt(listForm.customerId));
		} catch (WNoResultException e) {
			imageList = new ArrayList<>();
		}

		return TransitionConstants.Customer.JSP_APD03L01;
	}

	/**
	 * 画像のアップロードを行う。
	 * @return 登録画面
	 */
	@Execute(validator = false, reset="resetForm", urlPattern = "upImage/{customerId}", input = TransitionConstants.Customer.CUSTOMER_IMAGE_BACK_TO_NULL)
	public String upImage() {
		try {
			// 画像のアップロード処理
			writeJson(doUploadImage(listForm));
		} catch (FileUploadException e) {

			UpImageResponse imageResponse = new UpImageResponse();
			imageResponse.errorFlg = true;
			imageResponse.message = e.getMessage();

			writeJson(imageResponse);

		} catch (Exception e) {
			writeJson("画像アップロード時にエラーが発生しました。", HttpServletResponse.SC_BAD_REQUEST);
		}

		return null;
	}

	/**
	 * 画像の削除を行う。
	 * @return 登録画面
	 */
	@Execute(validator = false, reset="resetForm", urlPattern = "delImage/{customerId}/{customerImageId}", input = TransitionConstants.Customer.CUSTOMER_IMAGE_BACK_TO_NULL)
	public String delImage() {
		try {
			// 画像のアップロード処理
			customerImageLogic.deleteImage(NumberUtils.toInt(listForm.customerImageId));
			writeJson("success");
		} catch (Exception e) {
			//
		}

		return null;
	}

	/**
	 * NULLを返す。
	 * ※画像アップに失敗した時用
	 * @return
	 */
	@Execute(validator = false)
	public String backToNull() {
		return null;
	}

	/**
	 * 素材アップロードのメイン処理
	 */
	private UpImageResponse doUploadImage(ListForm form) {

		if (form.formFile == null) {
			throw new FileUploadException(MessageResourcesUtil.getMessage("errors.app.noExistFile"));
		}

		ImageDto imageDto = customerImageLogic.registerFormFile(NumberUtils.toInt(form.customerId), form.formFile);
		imageDto.filePath = CustomerImageAction.createImagePath(imageDto.id);

		UpImageResponse imageResponse = new UpImageResponse();
		imageResponse.imageDto = imageDto;
		imageResponse.errorFlg = false;
		imageResponse.message  = "アップロードが完了しました。";

		return imageResponse;
	}


	private List<ImageDto> createImageDtoList(int customerId) throws WNoResultException {
		List<ImageDto> imageDtoList = new ArrayList<>();

		List<TCustomerImage> imageEntityList = customerImageService.findListByCustomerId(customerId);
		for (TCustomerImage imageEntity : imageEntityList) {
			if (imageEntity.materialData == null) {
				continue;
			}

			ImageDto imageDto = new ImageDto();
			Beans.copy(imageEntity, imageDto).excludes("materialData").execute();
			imageDto.filePath = CustomerImageAction.createImagePath(imageEntity.id);

			imageDtoList.add(imageDto);
		}

		return imageDtoList;
	}

}
