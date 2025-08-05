package com.gourmetcaree.admin.pc.member.action.memberMailMag;

import static com.gourmetcaree.common.constants.GourmetCareeConstants.*;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.TokenProcessor;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.MessageResourcesUtil;
import org.seasar.struts.util.RequestUtil;
import org.seasar.struts.util.ResponseUtil;

import com.gourmetcaree.admin.pc.member.form.member.ListForm;
import com.gourmetcaree.admin.pc.member.form.memberMailMag.InputForm;
import com.gourmetcaree.admin.pc.sys.action.PcAdminAction;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.pc.sys.dto.mai.MailMagazineTriggerMaiDto;
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
import com.gourmetcaree.db.common.entity.MMember;
import com.gourmetcaree.db.common.entity.TMailMagazine;
import com.gourmetcaree.db.common.entity.TMailMagazineDelivery;
import com.gourmetcaree.db.common.entity.TMailMagazineDetail;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.MemberService;

import net.arnx.jsonic.JSON;

/**
 * 会員向けメルマガ配信アクションクラス
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

	/** 会員サービス */
	@Resource
	protected MemberService memberService;

	/** メルマガロジック */
	@Resource
	protected MailMagazineLogic mailMagazineLogic;

	/** mai */
	@Resource
	protected GourmetcareeMai gourmetcareeMai;

	/** メルマガIDを保持 */
	private Integer mailMagazineId;

	/** メルマガに添付する画像名を保持 */
	private String imageFileName;

	/**
	 * 初期表示
	 * @return 登録画面
	 */
	@Execute(validator = false, reset = "resetForm", input=TransitionConstants.Member.JSP_APH02C01)
	@MethodAccess(accessCode="MEMBERMAILMAG_INPUT_INDEX")
	public String index() {
		return show();
	}

	/**
	 * 確認
	 * @return 確認画面
	 */
	@Execute(validator = true, validate = "validate", input = TransitionConstants.Member.JSP_APH02C01)
	@MethodAccess(accessCode="MEMBERMAILMAG_INPUT_CONF")
	public String conf() {

		// プロセスフラグを確認済みに設定
		inputForm.setProcessFlgOk();

		//トークンを設定
        TokenProcessor.getInstance().saveToken(RequestUtil.getRequest());

        // 確認画面へ遷移
		return TransitionConstants.Member.JSP_APH02C02;
	}

	/**
	 * 戻る
	 * @return 会員一覧再検索メソッド
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="MEMBERMAILMAG_INPUT_BACK")
	public String back() {

		// 一覧再検索メソッドへ遷移
		return TransitionConstants.Member.REDIRECT_MEMBER_SEARCH_AGAIN;
	}

	/**
	 * 訂正
	 * @return 登録画面
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="MEMBERMAILMAG_INPUT_CORRECT")
	public String correct() {

		// プロセスフラグを未確認に設定
		inputForm.setProcessFlgNg();

		// 登録画面へ遷移
		return TransitionConstants.Member.JSP_APH02C01;
	}

	/**
	 * 登録
	 * @return 完了メソッド
	 */
	@Execute(validator = false, input = TransitionConstants.Member.JSP_APH02C01)
	@MethodAccess(accessCode="MEMBERMAILMAG_INPUT_SUBMIT")
	public String submit() {

		// プロセスフラグがfalseの場合、未確認としてエラー
		if (!inputForm.processFlg) {
			throw new FraudulentProcessException("不正な操作が行われました。" + inputForm);
		}

		//トークン判定処理。2度目のリクエストが来た場合は完了メソッドへ遷移
		if (!TokenProcessor.getInstance().isTokenValid(RequestUtil.getRequest(), true)) {
			return TransitionConstants.Member.REDIRECT_MEMBERMAILMAG_INPUT_COMP;
		}

		// 登録処理の呼び出し
		int mailMagazineId = insert();
		log.debug("メルマガの登録を行いました。");

		// メルマガ送信処理の呼び出し
		mailMagazineLogic.sendMailMagazineBackground(mailMagazineId);

		log.debug("メルマガの送信を行いました。");

		// 完了メソッドへ遷移
		return TransitionConstants.Member.REDIRECT_MEMBERMAILMAG_INPUT_COMP;
	}

	/**
	 * 完了
	 * @return 完了画面
	 */
	@Execute(validator = false, removeActionForm = true)
	@MethodAccess(accessCode="MEMBERMAILMAG_INPUT_COMP")
	public String comp() {

		listForm = null;

		// 完了画面へ遷移
		return TransitionConstants.Member.JSP_APH02C03;
	}

	/**
	 * 初期表示遷移用
	 * @return 登録画面のパス
	 */
	private String show() {

		// 検索条件が取得できない場合はエラー
		if (listForm.whereMap == null) {
			throw new FraudulentProcessException("不正な操作が行われました。" + inputForm);
		}

		// 検索条件を詰め替える
		inputForm.whereMap = listForm.whereMap;

		// テキストエリアに初期値をセット
		Properties properties = ResourceUtil.getProperties("mailMagazine.properties");
		if (properties != null) {
			inputForm.htmlBody = properties.getProperty("gc.mailMagazine.pcMemberHeader.html", "");
			inputForm.textBody = properties.getProperty("gc.mailMagazine.pcMemberHeader", "");
		}

		// 配信形式をHTMLにセット
		inputForm.deliveryType = MTypeConstants.deliveryTypeKbn.HTML;

		// 入力フォームを表示
		inputForm.setExistDataFlgOk();

		// 登録画面へ遷移
		return TransitionConstants.Member.JSP_APH02C01;
	}

	/**
	 * 登録処理
	 * @return メルマガID
	 */
	private int insert() {

		// 受け渡し用プロパティに値をセットする(初期値になるデータはロジックでセット)
		MailMagazineProperty property = new MailMagazineProperty();

		// メルマガテーブルに登録する値をセット
		property.mailMagazine = new TMailMagazine();
		// 配信先(会員)
		property.mailMagazine.deliveryKbn = MTypeConstants.DeliveryKbn.MEMBER;
		property.mailMagazine.mailmagazineKbn = MTypeConstants.MailmagazineKbn.CUSTOMER_MEMEBER;

		// メルマガ詳細テーブルに登録する値をセット
		property.mailMagazine.tMailMagazineDetailList = new ArrayList<TMailMagazineDetail>();
		TMailMagazineDetail mailMagazineDetail = new TMailMagazineDetail();
		// PC版をセット(リニューアル後はPCのみ有効にする)
		mailMagazineDetail.terminalKbn = MTypeConstants.TerminalKbn.PC_VALUE;
		mailMagazineDetail.mailMagazineTitle = inputForm.mailMagazineTitle;
		mailMagazineDetail.deliveryType = inputForm.deliveryType;
		if(inputForm.deliveryType.equals(MTypeConstants.deliveryTypeKbn.HTML)) {
			mailMagazineDetail.body = inputForm.htmlBody;
		} else {
			mailMagazineDetail.body = inputForm.textBody;
		}

		property.mailMagazine.tMailMagazineDetailList.add(mailMagazineDetail);

		// メルマガ配信先に登録する値をセット
		property.mailMagazine.tMailMagazineDeliveryList = new ArrayList<TMailMagazineDelivery>();
		try {
			// メルマガ配信する会員情報を取得
			List<MMember> memberList = memberService.getSendMailMagazineMember(inputForm.whereMap);

			for (MMember mMember : memberList) {

				TMailMagazineDelivery defaultEntity = new TMailMagazineDelivery();
				// エリアコード
				defaultEntity.areaCd = mMember.areaCd;
				// ユーザー区分(会員)
				defaultEntity.userKbn = MTypeConstants.UserKbn.MEMBER;
				// 会員ID
				defaultEntity.deliveryId = mMember.id;
				// 会員名
				defaultEntity.deliveryName = mMember.memberName;

				TMailMagazineDelivery mailMagazineDelivery;


				mailMagazineDelivery = new TMailMagazineDelivery();
				Beans.copy(defaultEntity, mailMagazineDelivery).execute();
				// 端末区分(PC)
				mailMagazineDelivery.terminalKbn = MTypeConstants.TerminalKbn.PC_VALUE;
				// メールアドレス
				mailMagazineDelivery.mail = mMember.loginId;
				// リストに保持
				property.mailMagazine.tMailMagazineDeliveryList.add(mailMagazineDelivery);

				// サブメールが登録されていて、配信が可能な場合（リニューアル後はサブメールで対応）
				if (StringUtil.isNotBlank(mMember.subMailAddress) &&
						!GourmetCareeUtil.eqInt(MTypeConstants.MobileMailStopFlg.STOP, mMember.subMailStopFlg)) {

					mailMagazineDelivery = new TMailMagazineDelivery();
					Beans.copy(defaultEntity, mailMagazineDelivery).execute();
					// 端末区分(モバイル)
					mailMagazineDelivery.terminalKbn = MTypeConstants.TerminalKbn.PC_VALUE;
					// メールアドレス
					mailMagazineDelivery.mail = mMember.subMailAddress;
					// リストに保持
					property.mailMagazine.tMailMagazineDeliveryList.add(mailMagazineDelivery);
				}
			}

		// 検索条件が不正な場合
		} catch (NumberFormatException e) {
			throw new FraudulentProcessException("不正な操作が行われました。" + listForm);
		} catch (ParseException e) {
			throw new FraudulentProcessException("不正な操作が行われました。" + listForm);

		// データが存在しない場合
		} catch (WNoResultException e) {

			// 「{配信拒否}または、{削除}されている可能性があるため、{メルマガ配信}することはできません。」
			throw new ActionMessagesException("errors.canNotTwoProcess",
					MessageResourcesUtil.getMessage("msg.deliverReject"),
					MessageResourcesUtil.getMessage("msg.delete"),
					MessageResourcesUtil.getMessage("labels.mailStopFlg"));
		}

		// 条件をログに出力
		log.info("会員向けメルマガ登録するForm情報：" + inputForm);

		// メルマガ関連データを登録
		mailMagazineId = mailMagazineLogic.insertMailMag(property);

		// 不要なオブジェクトを空にしておく
		property = null;

		return mailMagazineId;
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
		resMap.put("fileDirectory", baseUrl + "/mailmagazine/member/" + imageFileName);
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

		String dirPath = getCommonProperty("gc.mailmagazine.member.imageUpload.dir");

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
	 * メルマガ送信処理
	 * メールを送信することでJamesをキックします。
	 */
	private void sendMailMagazine() {

		MailMagazineTriggerMaiDto maiDto = new MailMagazineTriggerMaiDto();
		maiDto.setMailMagazineSuject(getCommonProperty("gc.mm.customer.prefix") + mailMagazineId);
		maiDto.addTo(getCommonProperty("gc.mm.trigger.address"));
		try {
			maiDto.setFrom(getCommonProperty("gc.mm.admin.address"), getCommonProperty("gc.mm.admin.Name"));
		// アドレスがセットできない場合、初期値で送信
		} catch (UnsupportedEncodingException e) {
			// 送信元のセットに失敗した場合はエラーを送信する。（mailProperties.diconの値がセットされる）
			log.fatal("会員宛メルマガ送信処理時、送信元がセットできませんでした。" + e);
		}
		gourmetcareeMai.sendMailMagazineTriggerMail(maiDto);
	}
}