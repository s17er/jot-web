package com.gourmetcaree.admin.pc.juskill.action.juskillMember;

import static com.gourmetcaree.common.constants.GourmetCareeConstants.*;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.util.StringUtil;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.util.MessageResourcesUtil;
import org.seasar.struts.util.ResponseUtil;

import com.gourmetcaree.admin.pc.juskill.form.juskillMember.EditForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.logic.JuskillLogic;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.dto.MaterialDto;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.MJuskillMember;
import com.gourmetcaree.db.common.entity.MJuskillMemberCareerHistory;
import com.gourmetcaree.db.common.entity.TJuskillMemberMaterial;
import com.gourmetcaree.db.common.service.JuskillMemberService;

import jp.co.whizz_tech.common.sastruts.converter.ZenkakuKanaConverter;
import net.arnx.jsonic.JSON;

/**
 * ジャスキル会員管理編集アクションクラス
 * @version 1.0
 *
 */
@ManageLoginRequired(authLevel= {ManageAuthLevel.ADMIN, ManageAuthLevel.STAFF, ManageAuthLevel.SALES})
public class EditAction extends JuskillMemberBaseAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(EditAction.class);

	/** 編集フォーム */
	@ActionForm
	@Resource
	protected EditForm editForm;

	/** ジャスキル会員サービス */
	@Resource
	private JuskillMemberService juskillMemberService;

	/** ジャスキルロジック */
	@Resource
	private JuskillLogic juskillLogic;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", urlPattern = "index/{id}", input = TransitionConstants.Juskill.JSP_APQ01R01)
	@MethodAccess(accessCode="JUSKILL_MEMBER_EDIT_INDEX")
	public String index() {
		return show();
	}

	/**
	 * 確認
	 * @return
	 */
	@Execute(validator = true, validate = "validate", input = TransitionConstants.Juskill.JSP_APQ01E01)
	@MethodAccess(accessCode="JUSKILL_MEMBER_EDIT_CONF")
	public String conf() {

		// パラメータが空の場合はエラー
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, editForm.id);

		// idが数値かどうかチェック
		if (!StringUtils.isNumeric(editForm.id)) {
			throw new FraudulentProcessException("不正な操作が行われました。");
		}

		// 編集データを取得できているかチェック
		if (!editForm.existDataFlg) {
			throw new FraudulentProcessException("不正な操作が行われました。");
		}

		// Listの空文字を削除する
		editForm.careerHistoryList.removeAll(Collections.singleton(""));
		// Listのnullを削除する
		editForm.careerHistoryList.removeAll(Collections.singleton(null));

		// プロセスフラグを確認済みに設定
		editForm.setProcessFlgOk();

		// 確認画面へ遷移
		return TransitionConstants.Juskill.JSP_APQ01E02;
	}

	/**
	 * 戻る
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="JUSKILL_MEMBER_EDIT_BACK")
	public String back() {
		// 確認画面へ遷移
		return TransitionConstants.Juskill.REDIRECT_JUSKILL_DETAIL_INDEX + editForm.id + TransitionConstants.REDIRECT_STR;
	}

	/**
	 * 訂正
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="JUSKILL_MEMBER_EDIT_CORRECT")
	public String correct() {

		// パラメータが空の場合はエラー
		checkArgsNullPageNotFound(NO_BLANK_FLG_NG, editForm.id);

		// プロセスフラグがfalseの場合、未確認としてエラー
		if (!editForm.processFlg) {
			throw new FraudulentProcessException("不正な操作が行われました。" + editForm);
		}

		// プロセスフラグを未確認に設定
		editForm.setProcessFlgNg();

		// 登録画面へ遷移
		return TransitionConstants.Juskill.JSP_APQ01E01;
	}

	/**
	 * 登録
	 * @return
	 */
	@Execute(validator = false, removeActionForm = true, input = TransitionConstants.Juskill.JSP_APQ01E01)
	@MethodAccess(accessCode="JUSKILL_MEMBER_EDIT_SUBMIT")
	public String submit() {

		// プロセスフラグがfalseの場合、未確認としてエラー
		if (!editForm.processFlg) {
			throw new FraudulentProcessException("不正な操作が行われました。" + editForm);
		}

		// セッションのIdと、画面で保持するIdが違う場合エラー
		if (!editForm.id.equals(editForm.hiddenId)) {
			throw new FraudulentProcessException("不正な操作が行われました。" + editForm);
		}

		// 登録処理
		doUpdate();

		log.debug("ジャスキル会員データをUPDATEしました。");

		return TransitionConstants.Juskill.REDIRECT_JUSKILL_EDIT_COMP;
	}

	/**
	 * 完了
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="JUSKILL_MEMBER_EDIT_COMP")
	public String comp() {
		// 完了画面へ遷移
		return TransitionConstants.Juskill.JSP_APQ01E03;
	}

	/**
	 * 素材のアップロードを行う。
	 * @return 登録画面
	 */
	@Execute(validator = false, input = TransitionConstants.Juskill.JSP_APQ01E01)
	public String upAjaxMaterial() {
		Map<String, String> resMap = new HashMap<>();

		String errorMsg = doUploadMaterial();
		if(StringUtil.isNotEmpty(errorMsg)) {
			resMap.put("error", errorMsg);
			ResponseUtil.write(JSON.encode(resMap), JSON_CONTENT_TYPE, ENCODING);
			return null;
		}


		resMap.put("fileName", editForm.pdfFileName);
		ResponseUtil.write(JSON.encode(resMap), JSON_CONTENT_TYPE, ENCODING);

		return null;

	}

	/**
	 * アップロードされた素材の削除処理
	 * @return 登録画面
	 */
	@Execute(validator=false, input = TransitionConstants.Webdata.JSP_APC01C01)
	public String deleteAjaxMaterial() {
		Map<String, String> resMap = new HashMap<>();

		// パラメータが空の場合はエラー
		if (StringUtil.isEmpty(editForm.hiddenMaterialKbn)) {
			resMap.put("error", "削除に失敗しました");
			ResponseUtil.write(JSON.encode(resMap), JSON_CONTENT_TYPE, ENCODING);
            return null;
		}

		// 画像の削除処理
		doDeleteMaterial(editForm);
		ResponseUtil.write(JSON.encode(resMap), JSON_CONTENT_TYPE, ENCODING);
		return null;
	}

	/**
	 * 素材アップロードのメイン処理
	 */
	protected String doUploadMaterial() {

		FormFile formFile = editForm.pdfFile;

		if (formFile == null || formFile.getFileSize() <= 0) {
			editForm.hiddenMaterialKbn = null;
			editForm.deletePdfFile();
			return MessageResourcesUtil.getMessage("errors.app.upload");
		}

		if (!GourmetCareeConstants.MEDIA_CONTENT_TYPE_PDF.equals(formFile.getContentType())) {
			editForm.deletePdfFile();
			return MessageResourcesUtil.getMessage("errors.app.imageNotPdf");
		}

		String dirPath = getCommonProperty("gc.juskillMember.pdfUpload.dir");
		StringBuilder dirName = new StringBuilder();
		dirName.append(editForm.getIdForDirName());
		dirName.append("_");
		dirName.append(session.getId());

		MaterialDto dto = new MaterialDto();
		// 素材区分をセット
		dto.materialKbn = editForm.hiddenMaterialKbn;
		// ファイル名をセット
		dto.fileName = formFile.getFileName();
		// コンテントタイプをセット
		dto.contentType = formFile.getContentType();
		// マテリアルデータをセット
		dto.materialData = session.getId().getBytes();

		// Mapの初期処理を行う
		editForm.initMaterialMap();
		// 素材をマップにセット
		editForm.materialMap.put(editForm.hiddenMaterialKbn, dto);

		String fileName = "resume.pdf";

		editForm.pdfFileName = formFile.getFileName();

		try {
			createWebdataFile(dirPath, dirName.toString(), fileName, formFile.getFileData());
		} catch (FileNotFoundException e) {
			log.warn(e);
		} catch (IOException e) {
			log.warn(e);
		}finally {
			formFile.destroy();
		}

		// Mapの初期処理を行う
		editForm.initMaterialMap();

		//hiddenをリセット
		editForm.hiddenMaterialKbn = null;
		return null;
	}


	/**
	 * 画像削除のメインロジック
	 */
	private void doDeleteMaterial(EditForm form) {

		// フォームをリセット
		form.deleteMaterial(form.hiddenMaterialKbn);

		//hiddenをリセット
		form.hiddenMaterialKbn = null;
	}



	/**
	 * 初期表示遷移用
	 * @return 登録画面のパス
	 */
	private String show() {

		initUploadMaterial(editForm);

		// idがnullかチェック
		checkArgsNull(NO_BLANK_FLG_NG, editForm.id);

		// 表示データ取得
		convertDispData(editForm);

		// チェック用のIdに値を保持
		editForm.hiddenId = editForm.id;

		log.debug("表示データ取得");

		// 登録画面へ遷移
		return TransitionConstants.Juskill.JSP_APQ01E01;
	}

	/**
	 * ジャスキル会員データ更新処理を行う
	 */
	private void doUpdate() {

		MJuskillMember mJuskillMember = createUpdateData();

		TJuskillMemberMaterial tJuskillMemberMaterial = createMarerialData();

		List<MJuskillMemberCareerHistory> careerHistoryList = createUpdateCareerHistoryList();

		juskillLogic.updateJuskillMember(mJuskillMember, careerHistoryList, tJuskillMemberMaterial);
	}

	/**
	 * 更新ジャスキル会員データを作成
	 * @return MJuskillMember
	 */
	private MJuskillMember createUpdateData() {
		MJuskillMember mJuskillMember =
				Beans.createAndCopy(MJuskillMember.class, editForm).converter(new ZenkakuKanaConverter()).execute();
		mJuskillMember.birthday = StringUtil.isNotEmpty(editForm.birthDate) ? convertBirthDayData() : null;

		return mJuskillMember;
	}

	/**
	 * ジャスキル会員の素材データを作成
	 * @return
	 */
	private TJuskillMemberMaterial createMarerialData() {


		if(editForm.materialMap == null || editForm.materialMap.isEmpty()) {
			return null;
		}

		TJuskillMemberMaterial tJuskillMemberMarerial = new TJuskillMemberMaterial();
		tJuskillMemberMarerial.juskillMemberId = Integer.parseInt(editForm.id);
		tJuskillMemberMarerial.materialKbn = Integer.parseInt(editForm.materialMap.get(MTypeConstants.JuskillMemberMaterialKbn.RESUME).materialKbn);
		tJuskillMemberMarerial.fileName = editForm.materialMap.get(MTypeConstants.JuskillMemberMaterialKbn.RESUME).fileName;
		tJuskillMemberMarerial.contentType = editForm.materialMap.get(MTypeConstants.JuskillMemberMaterialKbn.RESUME).contentType;
		tJuskillMemberMarerial.materialData = new String(editForm.materialMap.get(MTypeConstants.JuskillMemberMaterialKbn.RESUME).materialData);


		return tJuskillMemberMarerial;
	}

	/**
	 * 更新職歴データを作成
	 * @return
	 */
	private List<MJuskillMemberCareerHistory> createUpdateCareerHistoryList() {
		List<MJuskillMemberCareerHistory> list = new ArrayList<>();
		if (CollectionUtils.isEmpty(editForm.careerHistoryList)) {
			return list;
		}
		for (String career : editForm.careerHistoryList) {
			if (StringUtil.isEmpty(career)) {
				continue;
			}
			MJuskillMemberCareerHistory entity = new MJuskillMemberCareerHistory();
			entity.careerHistory = career;
			entity.juskillMemberId = Integer.parseInt(editForm.id);
			list.add(entity);
		}
		return list;
	}

	/**
	 * 入力された生年月日を変換
	 * @return
	 */
	private Date convertBirthDayData() {

		Calendar cal = Calendar.getInstance();
		cal.set(NumberUtils.toInt(editForm.birthYear), NumberUtils.toInt(editForm.birthMonth)-1, NumberUtils.toInt(editForm.birthDate));
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return new Date(cal.getTimeInMillis());
	}

	private void createWebdataFile(String dirPath, String dirName, String fileName, byte[] pdf)throws IOException {

		File dir = new File(dirPath, dirName);
		if (!dir.exists() && !dir.isDirectory()) {
			if (!dir.mkdirs()) {
				throw new IOException("PDFファイルのmkdirs()に失敗しました。");
			}
		}

		File file = new File(dirPath + System.getProperty("file.separator") + dirName, fileName);
		try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));){

			bos.write(pdf);
			bos.flush();
			bos.close();
		} catch (FileNotFoundException e) {
			 throw new IOException("PDFファイルの生成時に例外FileNotFoundExceptionが発生しました。 ファイルパス：" + file.getAbsolutePath(), e);
		} catch (IOException e) {
			 throw new IOException("PDFファイルの生成時に例外が発生しました。 ファイルパス：" + file.getAbsolutePath(), e);
		}
	}



}