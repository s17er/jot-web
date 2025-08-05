package com.gourmetcaree.admin.pc.customer.action.customerMailMag;

import static com.gourmetcaree.common.constants.GourmetCareeConstants.*;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.TokenProcessor;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.MessageResourcesUtil;
import org.seasar.struts.util.RequestUtil;
import org.seasar.struts.util.ResponseUtil;

import com.gourmetcaree.admin.pc.customer.form.customerMailMag.InputForm;
import com.gourmetcaree.admin.pc.customer.form.customerMailMag.ListForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.pc.sys.mai.GourmetcareeMai;
import com.gourmetcaree.admin.service.logic.MailMagazineLogic;
import com.gourmetcaree.admin.service.property.MailMagazineProperty;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.MCustomer;
import com.gourmetcaree.db.common.entity.TMailMagazine;
import com.gourmetcaree.db.common.entity.TMailMagazineDelivery;
import com.gourmetcaree.db.common.entity.TMailMagazineDetail;
import com.gourmetcaree.db.common.service.CustomerService;
import com.gourmetcaree.db.common.service.CustomerSubMailService;

import net.arnx.jsonic.JSON;
/**
 * 顧客向けメルマガ配信アクションクラス
 * @author Makoto Otani
 * @version 1.0
 *
 */
@ManageLoginRequired(authLevel=ManageAuthLevel.ADMIN)
public class InputAction extends PcAdminAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(InputAction.class);

	/** 登録フォーム */
	@ActionForm
	@Resource
	protected InputForm inputForm;

	/** 一覧フォーム */
	@Resource
	protected ListForm listForm;

	/** 顧客サービス */
	@Resource
	protected CustomerService customerService;

	/** メルマガロジック */
	@Resource
	protected MailMagazineLogic mailMagazineLogic;

	/** mai */
	@Resource
	protected GourmetcareeMai gourmetcareeMai;

	@Resource
	private CustomerSubMailService customerSubMailService;

	/** メルマガIDを保持 */
	private Integer mailMagazineId;

	/** メルマガに添付する画像名を保持 */
	private String imageFileName;

	/**
	 * 初期表示
	 * @return 登録画面
	 */
	@Execute(validator = false, reset = "resetForm", input=TransitionConstants.Customer.JSP_APD02C01)
	@MethodAccess(accessCode = "CUSTOMERMAILMAG_INPUT_INDEX")
	public String index() {
		return show();
	}

	/**
	 * 確認
	 * @return 確認画面
	 */
	@Execute(validator = true, validate = "validate", input = TransitionConstants.Customer.JSP_APD02C01)
	@MethodAccess(accessCode = "CUSTOMERMAILMAG_INPUT_CONF")
	public String conf() {

		// プロセスフラグを確認済みに設定
		inputForm.setProcessFlgOk();

		//トークンを設定
        TokenProcessor.getInstance().saveToken(RequestUtil.getRequest());

        // 確認画面へ遷移
		return TransitionConstants.Customer.JSP_APD02C02;
	}

	/**
	 * 戻る
	 * @return 送信先一覧確認画面
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode = "CUSTOMERMAILMAG_INPUT_BACK")
	public String back() {

		// 送信先一覧確認画面再表示メソッドへ遷移
		return TransitionConstants.Customer.REDIRECT_CUSTOMERMAILMAG_BACKMAILMAG;
	}

	/**
	 * 訂正
	 * @return 登録画面
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode = "CUSTOMERMAILMAG_INPUT_CORRECT")
	public String correct() {

		// プロセスフラグを未確認に設定
		inputForm.setProcessFlgNg();

		// 登録画面へ遷移
		return TransitionConstants.Customer.JSP_APD02C01;
	}

	/**
	 * 登録
	 * @return 完了メソッド
	 */
	@Execute(validator = false, input = TransitionConstants.Customer.JSP_APD02C01)
	@MethodAccess(accessCode = "CUSTOMERMAILMAG_INPUT_SUBMIT")
	public String submit() {

		// プロセスフラグがfalseの場合、未確認としてエラー
		if (!inputForm.processFlg) {
			throw new FraudulentProcessException("不正な操作が行われました。" + inputForm);
		}

		//トークン判定処理。2度目のリクエストが来た場合は完了メソッドへ遷移
		if (!TokenProcessor.getInstance().isTokenValid(RequestUtil.getRequest(), true)) {
			return TransitionConstants.Customer.REDIRECT_CUSTOMERMAILMAG_INUT_COMP;
		}

		// 登録処理の呼び出し
		int mailMagazineId = insert();
		log.debug("メルマガの登録を行いました。");

		// メルマガ送信処理の呼び出し
		mailMagazineLogic.sendMailMagazineBackground(mailMagazineId);
		log.debug("メルマガの送信を行いました。");

		// 完了メソッドへ遷移
		return TransitionConstants.Customer.REDIRECT_CUSTOMERMAILMAG_INUT_COMP;
	}

	/**
	 * 完了
	 * @return 完了画面
	 */
	@Execute(validator = false, removeActionForm = true)
	@MethodAccess(accessCode = "CUSTOMERMAILMAG_INPUT_COMP")
	public String comp() {

		listForm = null;

		// 完了画面へ遷移
		return TransitionConstants.Customer.JSP_APD02C03;
	}

	/**
	 * 素材のアップロードを行う。
	 * @return 登録画面
	 */
	@Execute(validator = false, input = TransitionConstants.Member.JSP_APH02C01)
	public String upAjaxMaterial() {
		Map<String, String> resMap = new HashMap<>();

		String errorMsg = doUploadMaterial();
		if(StringUtil.isNotEmpty(errorMsg)) {
			resMap.put("error", errorMsg);
			ResponseUtil.write(JSON.encode(resMap), JSON_CONTENT_TYPE, ENCODING);
			return null;
		}

		String baseUrl = ResourceUtil.getProperties("gourmetcaree.properties").getProperty("gc.realPath");
		resMap.put("fileDirectory", baseUrl + "/mailmagazine/customer/" + imageFileName);
		ResponseUtil.write(JSON.encode(resMap), JSON_CONTENT_TYPE, ENCODING);
		imageFileName = null;

		return null;

	}

	/**
	 * 素材アップロードのメイン処理
	 */
	protected String doUploadMaterial() {

		FormFile formFile = inputForm.imageFile;

		if (formFile == null || formFile.getFileSize() <= 0) {
			return MessageResourcesUtil.getMessage("errors.app.upload");
		}

		if (!(GourmetCareeConstants.MEDIA_CONTENT_TYPE_JPEG.equals(formFile.getContentType()) ||
				GourmetCareeConstants.MEDIA_CONTENT_TYPE_PNG.equals(formFile.getContentType()))) {
			return MessageResourcesUtil.getMessage("errors.app.notImage");
		}

		String dirPath = getCommonProperty("gc.mailmagazine.customer.imageUpload.dir");

		SimpleDateFormat sdf = new SimpleDateFormat("MMdd_HHmmssSSS");

		imageFileName = sdf.format(new Date());

		if(GourmetCareeConstants.MEDIA_CONTENT_TYPE_JPEG.equals(formFile.getContentType())) {
			imageFileName += ".jpg";
		} else {
			imageFileName += ".png";
		}

		try {
			createWebdataFile(dirPath, formFile.getFileData());
		} catch (FileNotFoundException e) {
			log.warn(e);
		} catch (IOException e) {
			log.warn(e);
		}finally {
			formFile.destroy();
		}
		return null;
	}

	private void createWebdataFile(String dirPath, byte[] image)throws IOException {

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

	/**
	 * 初期表示遷移用
	 * @return 登録画面のパス
	 */
	private String show() {

		// 遷移チェック
		if (listForm.customerIdList == null || listForm.customerIdList.isEmpty()) {
			inputForm.setExistDataFlgNg();
			// 「該当するデータが見つかりませんでした。」
			throw new ActionMessagesException("errors.app.dataNotFound");
		}

		inputForm.deliveryType = MTypeConstants.deliveryTypeKbn.HTML;

		// テキストエリアに初期値をセット
		Properties properties = ResourceUtil.getProperties("mailMagazine.properties");
		if (properties != null) {
			inputForm.htmlBody = properties.getProperty("gc.mailMagazine.pcCustomerHeader.html", "");
			inputForm.textBody = properties.getProperty("gc.mailMagazine.pcCustomerHeader", "");
		}

		// 入力フォームを表示
		inputForm.setExistDataFlgOk();

		// 登録画面へ遷移
		return TransitionConstants.Customer.JSP_APD02C01;
	}

	/**
	 * 登録処理
	 * @return int メールマガジンID
	 */
	private int insert() {

		// 受け渡し用プロパティに値をセットする(初期値になるデータはロジックでセット)
		MailMagazineProperty property = new MailMagazineProperty();

		// メルマガテーブルに登録する値をセット
		property.mailMagazine = new TMailMagazine();
		// 配信先(顧客)
		property.mailMagazine.deliveryKbn = MTypeConstants.DeliveryKbn.CUSTOMER;
		property.mailMagazine.mailmagazineKbn = MTypeConstants.MailmagazineKbn.CUSTOMER_MEMEBER;

		// メルマガ詳細テーブルに登録する値をセット
		TMailMagazineDetail mailMagazineDetail = Beans.createAndCopy(TMailMagazineDetail.class, inputForm).execute();
		// 端末区分にPCをセット
		mailMagazineDetail.terminalKbn = MTypeConstants.TerminalKbn.PC_VALUE;
		mailMagazineDetail.deliveryType = inputForm.deliveryType;
		property.mailMagazine.tMailMagazineDetailList = new ArrayList<TMailMagazineDetail>();
		property.mailMagazine.tMailMagazineDetailList.add(mailMagazineDetail);
		if(inputForm.deliveryType.equals(MTypeConstants.deliveryTypeKbn.HTML)) {
			mailMagazineDetail.body = inputForm.htmlBody;
		} else {
			mailMagazineDetail.body = inputForm.textBody;
		}

		// メルマガ配信先に登録する値をセット
		property.mailMagazine.tMailMagazineDeliveryList = new ArrayList<TMailMagazineDelivery>();
		// エラーId
		List<String> errorIdList = new ArrayList<String>();

		// 選択した顧客が削除されていないかチェックする
		for (String id : listForm.customerIdList) {

			try {
				// データの検索
				MCustomer entity = customerService.findById(Integer.parseInt(id));

				// メルマガを受信しない顧客は処理をしない。
				if (GourmetCareeUtil.eqInt(
						MTypeConstants.CustomerMailMagazineReceptionFlg.NOT_RECEIVE,
						entity.mailMagazineReceptionFlg)) {

					log.info("メルマガ受信が拒否されています。" + ToStringBuilder.reflectionToString(entity).toString());
					continue;
				}

				TMailMagazineDelivery defaultEntity = new TMailMagazineDelivery();
				// エリアコード
				defaultEntity.areaCd = entity.areaCd;
				// ユーザー区分(顧客)
				defaultEntity.userKbn = MTypeConstants.UserKbn.CUSTOMER;
				// 顧客ID
				defaultEntity.deliveryId = entity.id;
				// 顧客名
				defaultEntity.deliveryName = entity.customerName;
				// 端末区分
				defaultEntity.terminalKbn = MTypeConstants.TerminalKbn.PC_VALUE;

				// メインメールアドレスをセット
				TMailMagazineDelivery mailMagazineDelivery = new TMailMagazineDelivery();
				Beans.copy(defaultEntity, mailMagazineDelivery).execute();
				mailMagazineDelivery.mail = entity.mainMail;

				// リストに保持
				property.mailMagazine.tMailMagazineDeliveryList.add(mailMagazineDelivery);

				// サブメールが登録されている場合
				List<String> subMailList = customerSubMailService.getReceptionSubMail(entity.id);
				for (String subMail : subMailList) {
					// サブメールアドレスをセット
					mailMagazineDelivery = new TMailMagazineDelivery();
					Beans.copy(defaultEntity, mailMagazineDelivery).execute();
					mailMagazineDelivery.mail = subMail;
					// リストに保持
					property.mailMagazine.tMailMagazineDeliveryList.add(mailMagazineDelivery);
				}

			// データが存在しない場合は存在しないIDを保持
			} catch (SNoResultException e) {
				errorIdList.add(id);
			}
		}


		// IDが削除されている場合は、エラーメッセージを表示
		if (errorIdList != null && !errorIdList.isEmpty()) {

			// IDをカンマ区切りで表示
			String errorId = GourmetCareeUtil.createKanmaSpaceStr(errorIdList);
			// 「{顧客iD：id, ...}が削除されている可能性があるため、{メルマガ配信}することはできません。」
			throw new ActionMessagesException("errors.canNotProcessDel",
					MessageResourcesUtil.getMessage("labels.customerId") + "：" + errorId,
					MessageResourcesUtil.getMessage("labels.mailStopFlg"));
		}

		// メルマガ送信先がない場合は、エラーメッセージを表示
		if (CollectionUtils.isEmpty(property.mailMagazine.tMailMagazineDeliveryList)) {
			throw new ActionMessagesException("errors.app.notExistMailMagCustomerData");
		}

		// 条件をログに出力
		log.info("顧客向けメルマガ登録するForm情報：inputForm" + inputForm + "選択した顧客ID" + listForm.customerIdList);

		// メルマガ関連データを登録
		mailMagazineId = mailMagazineLogic.insertMailMag(property);

		// 不要なオブジェクトを空にしておく
		property = null;

		return mailMagazineId;
	}
}



