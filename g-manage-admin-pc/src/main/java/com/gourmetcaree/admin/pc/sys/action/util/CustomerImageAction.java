package com.gourmetcaree.admin.pc.sys.action.util;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.framework.util.Base64Util;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.form.util.CustomerImageForm;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.db.common.entity.TCustomerImage;
import com.gourmetcaree.db.common.service.CustomerImageService;

/**
 * 画像処理を扱うユーティリティアクションクラス
 * @author hara
 * @version 1.0
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN,ManageAuthLevel.STAFF,ManageAuthLevel.AGENCY, ManageAuthLevel.OTHER, ManageAuthLevel.SALES})
public class CustomerImageAction extends PcAdminAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(CustomerImageAction.class);

	/** 素材サービス */
	@Resource
	protected CustomerImageService customerImageService;

	public static final String PATH = "/util/customerImage";

	/**
	 * 画像ユーティリティフォーム
	 */
	@Resource
	@ActionForm
	protected CustomerImageForm customerImageForm;

	/**
	 * 画像イメージのバイトのデータを出力ストリームにセットする。
	 *
	 * @return
	 */
	@Execute(validator = false, urlPattern = "displayImage/{id}")
	public String displayImage() {
		handleDbImage();
		return null;
	}

	/**
	 * 求人ID、画像区分、画像No、ハッシュを指定し、検索を行います。
	 * 取得したファイルをDBからストリームに出力
	 */
	protected void handleDbImage() {
		try {
			int id = NumberUtils.toInt(customerImageForm.id);
			TCustomerImage imageEntity = customerImageService.findById(id);

			//ストリームに出力
			outputToStream(imageEntity);

			//GCを促すため、明示的にnullを代入
			imageEntity = null;

		} catch (Exception e) {
			 log.warn("素材データの操作時に例外が発生しました。" + e.getMessage());
		}
	}

	/**
	 * responseのアウトプットストリームに書き出します。
	 * @param res
	 * @param binArray
	 */
	private void outputToStream(TCustomerImage entity) {

		BufferedOutputStream bos = null;
		try {
			//コンテンツのタイプを指定
			response.setContentType(entity.contentType);

			//MIMEの基準では76文字ごとに改行コードが入るため改行コードを除去
			bos = new BufferedOutputStream(response.getOutputStream());
			bos.write(Base64Util.decode(entity.materialData.replaceAll("\\n", "")));

			bos.flush();
			log.trace("素材を出力します。");
		} catch (FileNotFoundException e) {
			log.warn(e.getMessage());
		} catch (IOException e) {
			log.warn(e.getMessage());
		} finally {
			try {
				if (bos != null) {
					bos.close();
				}
			} catch (Exception e) {
				log.warn("素材の出力に失敗しました。" + e.getMessage());
			}
		}
	}

	public static String createImagePath(int id) {
		return PATH + "/displayImage/" + id;
	}

}
