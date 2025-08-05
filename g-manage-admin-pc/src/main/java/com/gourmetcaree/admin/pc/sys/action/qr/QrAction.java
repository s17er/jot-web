package com.gourmetcaree.admin.pc.sys.action.qr;

import java.io.BufferedOutputStream;
import java.io.IOException;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;

import com.google.zxing.WriterException;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.form.util.QrForm;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.constants.Constants;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.util.QRCodeUtil;
import com.gourmetcaree.db.common.constants.MAreaConstants;
import com.gourmetcaree.db.common.entity.TWeb;
import com.gourmetcaree.db.common.service.WebService;

/**
 * QRコードAction
 * @author Whizz
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN,ManageAuthLevel.STAFF,ManageAuthLevel.AGENCY, ManageAuthLevel.OTHER, ManageAuthLevel.SALES})
public class QrAction extends PcAdminAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(QrAction.class);

	@Resource
	private WebService webService;

	@Resource
	@ActionForm
	public QrForm qrForm;

	/** 応募用URL */
	private final String APLLICATION_REGISTER_PATH = "application/qr/register?web_id=";

	/**
	 * QRコードを作成
	 * @return
	 */
	@Execute(validator = false, urlPattern = "application/{width}/{height}/{webId}")
	public String applicationAction() {

		handleImage();

		return null;
	}

	/**
	 * QRの作成
	 */
	protected void handleImage() {

		BufferedOutputStream bos = null;
		response.setContentType(Constants.MEDIA_CONTENT_TYPE_PNG);
		try {

			bos = new BufferedOutputStream(response.getOutputStream());
			bos.write(QRCodeUtil.createQrCode(createApplicationUrl(qrForm.webId),
					Integer.parseInt(qrForm.width), Integer.parseInt(qrForm.height)));
			bos.flush();
			log.trace("QRコードを出力します.");

		} catch (IOException e) {
			log.warn(e.getMessage());
		} catch (WriterException e) {
			log.warn(e.getMessage());
		} finally {
			try {
				bos.close();
			} catch (Exception e) {
				log.warn("QRコードの出力に失敗しました" + e.getMessage());
			}
		}
	}

	/**
	 * WEBIDのエリアを取得する
	 * @param webId
	 * @return
	 */
	private String createAreaNameForWebId(String webId) {

		TWeb tWeb = webService.findById(Integer.parseInt(webId));
		return MAreaConstants.Prefix.getRenewalAreaName(tWeb.areaCd);
	}

	/**
	 * QRコードの埋め込むURLを作成
	 * @param webId
	 * @return
	 */
	private String createApplicationUrl(String webId) {

		/* QRコードに埋め込む内容 */
		StringBuilder qrUrl = new StringBuilder();
		qrUrl.append(ResourceUtil.getProperties("gourmetcaree.properties").getProperty("gc.sslDomain"));
		qrUrl.append(Constants.SLASH_STR);
		qrUrl.append(createAreaNameForWebId(qrForm.webId));
		qrUrl.append(Constants.SLASH_STR);
		qrUrl.append(APLLICATION_REGISTER_PATH);
		qrUrl.append(webId);

		return qrUrl.toString();
	}
}
