package com.gourmetcaree.common.logic.customerImage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts.upload.FormFile;
import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.util.Base64Util;
import org.seasar.struts.util.MessageResourcesUtil;

import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.dto.customerImage.ImageDto;
import com.gourmetcaree.common.exception.FileUploadException;
import com.gourmetcaree.common.logic.AbstractGourmetCareeLogic;
import com.gourmetcaree.db.common.entity.TCustomerImage;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.CustomerImageService;

import jp.co.whizz_tech.commons.WztStringUtil;

public class CustomerImageLogic extends AbstractGourmetCareeLogic {

	/** アップ画像の最大サイズ（2MB） */
	protected static final int MAX_FILESIZE = 1024 * 1024 * 2;

	@Resource
	private CustomerImageService customerImageService;

	/**
	 * フォームデータをDBに登録します
	 *
	 * @param customerId
	 * @param formFile
	 * @return
	 */
	public ImageDto registerFormFile(Integer customerId, FormFile formFile) {

		if (formFile == null || formFile.getFileSize() == 0) {
			// アップロードされたファイルが存在しません。
			throw new FileUploadException(MessageResourcesUtil.getMessage("errors.app.noExistFile"));
		}

		if (formFile.getFileSize() > MAX_FILESIZE) {
			formFile.destroy();
			throw new FileUploadException(MessageResourcesUtil.getMessage("errors.upload.size", "2MB"));
		}

		//JPEG以外の場合はエラー
		if (!GourmetCareeConstants.MEDIA_CONTENT_TYPE_JPEG.equals(formFile.getContentType()) &&
			!GourmetCareeConstants.MEDIA_CONTENT_TYPE_PJPEG.equals(formFile.getContentType())) {
			throw new FileUploadException(MessageResourcesUtil.getMessage("errors.app.imageNotJpeg"));
		}

		// 同じファイル名だったら削除
		String fileName = formFile.getFileName();

		Where where = new SimpleWhere()
				.eq(WztStringUtil.toCamelCase(TCustomerImage.CUSTOMER_ID), customerId)
				.eq(WztStringUtil.toCamelCase(TCustomerImage.FILE_NAME), fileName);
		try {
			List<TCustomerImage> list = customerImageService.findByCondition(where);
			customerImageService.deleteBatch(list);
		} catch (WNoResultException e1) {
			//
		}

		TCustomerImage entity = new TCustomerImage();
		entity.customerId = customerId;
		entity.fileName = formFile.getFileName();

		// コンテントタイプがpjpegの場合は、jpegで登録
		if (GourmetCareeConstants.MEDIA_CONTENT_TYPE_PJPEG.equals(formFile.getContentType())) {
			entity.contentType = GourmetCareeConstants.MEDIA_CONTENT_TYPE_JPEG;
		} else  {
			entity.contentType = formFile.getContentType();
		}

		try {
			entity.materialData = Base64Util.encode(formFile.getFileData());
			customerImageService.insert(entity);
		} catch (FileNotFoundException e) {
			// アップロードされたファイルが存在しません。
			throw new FileUploadException(MessageResourcesUtil.getMessage("errors.app.noExistFile"));
		} catch (IOException e) {
			// ファイルのアップロードに失敗しました。もう一度操作をやり直してください。
			throw new FileUploadException(MessageResourcesUtil.getMessage("errors.app.FileUpload"));
		}

		// 画像DTOを作る
		ImageDto imageDto = new ImageDto();
		Beans.copy(entity, imageDto).excludes("materialData").execute();

		return imageDto;
	}

	/**
	 * 画像削除
	 */
	public void deleteImage(int customerImageId) {
		TCustomerImage customerImage = customerImageService.findById(customerImageId);

		if (customerImage != null) {
			if (customerImageId == customerImage.id.intValue()) {
				customerImageService.delete(customerImage);
			}
		}
	}

}
