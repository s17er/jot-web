package com.gourmetcaree.admin.pc.mailMag.action.mailMagOption;

import static com.gourmetcaree.common.constants.GourmetCareeConstants.*;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.ActionMessagesUtil;
import org.seasar.struts.util.MessageResourcesUtil;
import org.seasar.struts.util.ResponseUtil;

import com.gourmetcaree.admin.pc.mailMag.form.mailMagOption.HeaderInputForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TMailMagazineOption;

import net.arnx.jsonic.JSON;

/**
 * メールマガジンのオプションを入力するアクションです。
 * @author Takehiro Nakamori
 *
 */
@ManageLoginRequired(authLevel=ManageAuthLevel.ADMIN)
public class HeaderInputAction extends BaseMailMagOptionAction {

	/** ログオブジェクト */
	Logger log = Logger.getLogger(HeaderInputAction.class);

	/**
	 * アクションフォーム
	 */
	@Resource
	@ActionForm
	public HeaderInputForm headerInputForm;

	/** メルマガに添付する画像名を保持 */
	private String imageFileName;


	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.MailMag.JSP_APK02C01, reset = "resetForm")
	@MethodAccess(accessCode="MAILMAGOPT_HEADER_INPUT_INDEX")
	public String index() {
		headerInputForm.deliveryType = MTypeConstants.deliveryTypeKbn.HTML;
		return show();
	}

	/**
	 * 初期表示遷移
	 * @return
	 */
	private String show() {
		return TransitionConstants.MailMag.JSP_APK02C01;
	}

	/**
	 * メルマガヘッダメッセージへ遷移
	 * @return
	 */
	@Execute(validator = false, removeActionForm = true)
	@MethodAccess(accessCode="MAILMAGOPT_HEADER_LIST")
	public String backToList() {
		return TransitionConstants.MailMag.FORWORD_HEADER_LIST;
	}

	/**
	 * 確認画面
	 * @return
	 */
	@Execute(validator = true, validate = "validate", input = TransitionConstants.MailMag.JSP_APK02C01)
	@MethodAccess(accessCode="MAILMAGOPT_HEADER_INPUT_CONF")
	public String conf() {
		// すでに登録されていないかチェック
		if (isAlreadyResiteredDate()) {
			return TransitionConstants.MailMag.JSP_APK02C01;
		}

		headerInputForm.setProcessFlgOk();
		return TransitionConstants.MailMag.JSP_APK02C02;
	}

	/**
	 * 修正ボタン押下時に呼ばれるメソッド
	 * @return
	 */
	@Execute(validator = false, input = TransitionConstants.MailMag.JSP_APK02C01)
	@MethodAccess(accessCode="MAILMAGOPT_HEADER_INPUT_INDEX")
	public String correct() {
		headerInputForm.setProcessFlgNg();
		return TransitionConstants.MailMag.JSP_APK02C01;
	}

	/**
	 * 登録
	 */
	@Execute(validator = false, input = TransitionConstants.MailMag.JSP_APK02C01)
	@MethodAccess(accessCode="MAILMAGOPT_HEADER_INPUT_SUBMIT")
	public String submit() {
		// プロセスフラグがfalseの場合、未確認としてエラー
		if (!headerInputForm.processFlg) {
			callFraudulentProcessError(headerInputForm);
		}

		// すでに登録されていないかチェック
		if (isAlreadyResiteredDate()) {
			return TransitionConstants.MailMag.JSP_APK02C01;
		}
		insert();
		return TransitionConstants.MailMag.REDIRECT_MAILMAG_HEADER_INPUT_COMP;
	}

	/**
	 * 登録完了
	 */
	@Execute(validator = false, removeActionForm = true, input = TransitionConstants.MailMag.JSP_APK02C01)
	@MethodAccess(accessCode="MAILMAGOPT_HEADER_INPUT_COMP")
	public String comp() {
		return TransitionConstants.MailMag.JSP_APK02C03;
	}

	/**
	 * 登録処理
	 */
	private void insert() {
		for(String areaCd : headerInputForm.areaCd) {
			TMailMagazineOption entity = new TMailMagazineOption();
			Beans.copy(headerInputForm, entity).excludes("areaCd").dateConverter(GourmetCareeConstants.DATE_FORMAT_SLASH, "deliveryScheduleDatetime").execute();
			entity.mailmagazineKbn = MTypeConstants.MailmagazineKbn.NEW_INFORMATION;
			entity.mailmagazineOptionKbn = MTypeConstants.MailmagazineOptionKbn.HEADER_MESSAGE;
			entity.areaCd = Integer.parseInt(areaCd);
			if(headerInputForm.deliveryType.equals(MTypeConstants.deliveryTypeKbn.HTML)) {
				entity.optionValue = headerInputForm.htmlBody;
			} else {
				entity.optionValue = headerInputForm.textBody;
			}
			mailMagazineOptionService.insert(entity);

			log.debug("メルマガヘッダメッセージを登録しました。" + headerInputForm);
		}
	}

	/**
	 * すでに登録されているかどうか
	 * @return
	 */
	private boolean isAlreadyResiteredDate() {
		Date deliveryScheduleDatetime;
		try {
			deliveryScheduleDatetime = DateUtils.formatDate(headerInputForm.deliveryScheduleDatetime, GourmetCareeConstants.DATE_FORMAT_SLASH);
		if( mailMagazineOptionService.isAlreadyResisteredDate(deliveryScheduleDatetime, headerInputForm.areaCd,
				MTypeConstants.MailmagazineKbn.NEW_INFORMATION, MTypeConstants.MailmagazineOptionKbn.HEADER_MESSAGE)) {

			ActionMessages errors = new ActionMessages();
			errors.add("errors", new ActionMessage("errors.app.SameDataExists"));
			ActionMessagesUtil.addErrors(request, errors);

			return true;
		}

		return false;

		} catch (ParseException e) {
			throw new FraudulentProcessException("日付の変更に失敗しました。");
		} catch (NumberFormatException e) {
			throw new FraudulentProcessException("不正な値が入力されました。");
		}
	}

	/**
	 * 素材のアップロードを行う。
	 * @return 登録画面
	 */
	@Execute(validator = false, input = TransitionConstants.MailMag.JSP_APK02C01)
	public String upAjaxMaterial() {
		Map<String, String> resMap = new HashMap<>();

		String errorMsg = doUploadMaterial();
		if(StringUtil.isNotEmpty(errorMsg)) {
			resMap.put("error", errorMsg);
			ResponseUtil.write(JSON.encode(resMap), JSON_CONTENT_TYPE, ENCODING);
			return null;
		}

		String baseUrl = ResourceUtil.getProperties("gourmetcaree.properties").getProperty("gc.realPath");
		resMap.put("fileDirectory", baseUrl + "/mailmagazine/batch/" + imageFileName);
		ResponseUtil.write(JSON.encode(resMap), JSON_CONTENT_TYPE, ENCODING);
		imageFileName = null;

		return null;

	}

	/**
	 * 素材アップロードのメイン処理
	 */
	protected String doUploadMaterial() {

		FormFile formFile = headerInputForm.imageFile;

		if (formFile == null || formFile.getFileSize() <= 0) {
			return MessageResourcesUtil.getMessage("errors.app.upload");
		}

		if (!(GourmetCareeConstants.MEDIA_CONTENT_TYPE_JPEG.equals(formFile.getContentType()) ||
				GourmetCareeConstants.MEDIA_CONTENT_TYPE_PNG.equals(formFile.getContentType()))) {
			return MessageResourcesUtil.getMessage("errors.app.notImage");
		}

		String dirPath = getCommonProperty("gc.mailmagazine.batch.imageUpload.dir");

		SimpleDateFormat sdf = new SimpleDateFormat("MMdd_HHmmssSSS");

		imageFileName = sdf.format(new Date());

		if(GourmetCareeConstants.MEDIA_CONTENT_TYPE_JPEG.equals(formFile.getContentType())) {
			imageFileName += ".jpg";
		} else {
			imageFileName += ".png";
		}

		try {
			createImageFile(dirPath, formFile.getFileData());
		} catch (FileNotFoundException e) {
			log.warn(e);
		} catch (IOException e) {
			log.warn(e);
		}finally {
			formFile.destroy();
		}
		return null;
	}

	private void createImageFile(String dirPath, byte[] image)throws IOException {

		File dir = new File(dirPath);
		if (!dir.exists() && !dir.isDirectory()) {
			if (!dir.mkdirs()) {
				throw new IOException("画像ファイルのmkdirs()に失敗しました。");
			}
		}

		File file = new File(dirPath + System.getProperty("file.separator"), imageFileName);
		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));){

			bos.write(image);
			bos.flush();
			bos.close();
		} catch (FileNotFoundException e) {
			 throw new IOException("画像ファイルの生成時に例外FileNotFoundExceptionが発生しました。 ファイルパス：" + file.getAbsolutePath(), e);
		} catch (IOException e) {
			 throw new IOException("画像ファイルの生成時に例外が発生しました。 ファイルパス：" + file.getAbsolutePath(), e);
		}
	}
}
