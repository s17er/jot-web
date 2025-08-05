package com.gourmetcaree.admin.pc.webdata.action.lumpDecide;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.pc.sys.dto.mai.FixedMaiDto;
import com.gourmetcaree.admin.pc.sys.mai.GourmetcareeMai;
import com.gourmetcaree.admin.pc.webdata.action.webdata.WebdataBaseAction;
import com.gourmetcaree.admin.pc.webdata.dto.webdata.ListDto;
import com.gourmetcaree.admin.pc.webdata.form.lumpDecide.EditForm;
import com.gourmetcaree.admin.service.property.WebdataProperty;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MStatusConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TWeb;
import com.gourmetcaree.db.common.entity.VWebList;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.WebListService;

/**
 * WEBデータ一括確定アクションクラス
 * @author Takahiro Kimura
 * @version 1.0
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN,ManageAuthLevel.STAFF, ManageAuthLevel.OTHER, ManageAuthLevel.SALES})
public class EditAction extends WebdataBaseAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(EditAction.class);

	/** 編集フォーム */
	@ActionForm
	@Resource
	protected EditForm editForm;

	/** WEBリストサービス */
	@Resource
	protected WebListService webListService;

	/** メール送信 */
	@Resource
	protected GourmetcareeMai gourmetCareeMai;

	/** WEBデータID */
	public String[] webId;

	/** エリアコード */
	public String areaCd;

	/** 一覧表示用リスト */
	public List<ListDto> dtoList;


	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset = "resetForm", input=TransitionConstants.Webdata.JSP_APC04E02)
	@MethodAccess(accessCode="LUMPDECIDE_EDIT_INDEX")
	public String index() {
		return show();
	}

	/**
	 * 戻る
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="LUMPDECIDE_EDIT_BACK")
	public String back() {
		// 一覧画面へ遷移
		session.removeAttribute("webId");
		session.removeAttribute("areaCd");
		return TransitionConstants.Webdata.REDIRECT_WEBDATA_LIST_BACKSEARCH;
	}

	/**
	 * 登録
	 * @return
	 */
	@Execute(validator = false, removeActionForm = true, input = TransitionConstants.Webdata.JSP_APC04E02)
	@MethodAccess(accessCode="LUMPDECIDE_EDIT_SUBMIT")
	public String submit() {

		convertSessionData();
		checkSessionData();


		try {
			createDtoList();
		} catch (NumberFormatException e) {
			throw new FraudulentProcessException("不正な操作が行われました。" + editForm);
		} catch (WNoResultException e) {
			throw new FraudulentProcessException("不正な操作が行われました。" + editForm);
		}

		// 一括確定できるかどうかチェック
		if (!checkEnableDecide(webId)) {
			return TransitionConstants.Webdata.JSP_APC04E02;
		}

		if (!checkLumpDecideData(dtoList)) {
			return TransitionConstants.Webdata.JSP_APC04E02;
		}

		// 一括確定処理
		WebdataProperty property = new WebdataProperty();
		property.webId = webId;
		doLumpDecide(property);

		log.debug("一括確定を実行しました。");

		// 代理店へ確定のメールを送信
		if (CollectionUtils.isNotEmpty(property.entityList)) {
			sendMailToAgency(property);

			log.debug("代理店へ確定メールを送信しました。");
		}

		// セッションを破棄
		session.removeAttribute("webId");
		session.removeAttribute("areaCd");

		return TransitionConstants.Webdata.REDIRECT_LUMPDECIDE_EDIT_COMP;
	}

	/**
	 * 一覧へ戻る
	 * @return 一覧画面の検索メソッド
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="LUMPDECIDE_EDIT_BACKLIST")
	public String backList() {
		// 一覧画面へ遷移
		session.removeAttribute("webId");
		session.removeAttribute("areaCd");
		return TransitionConstants.Webdata.REDIRECT_WEBDATA_LIST_SEARCHAGAIN;
	}

	/**
	 * 一括確定処理
	 * @throws WNoResultException
	 * @throws NumberFormatException
	 */
	private void doLumpDecide(WebdataProperty property) {

		// 一括確定用データ生成
		convertLumpDecideData(property);

		// 一括確定
		try {
			webdataLogic.doLumpDecide(property);

		} catch (NumberFormatException e) {
			throw new ActionMessagesException("errors.app.failGetWebId");
		}

		// 楽観的排他制御が発生していればエラーを返す
		if (property.failWebId != null && property.failWebId.size() > 0) {
			throw new ActionMessagesException("errors.app.dinableLumpOptimisticLockException",
					 GourmetCareeUtil.createDelimiterStr(property.failWebId));
		}
	}

	/**
	 * 一括確定用データ生成
	 * @param property WEBデータプロパティ
	 */
	private void convertLumpDecideData(WebdataProperty property) {

		List<TWeb> entityList = new ArrayList<TWeb>();
		for (ListDto dto : dtoList) {
			TWeb entity = new TWeb();
			entity.id = NumberUtils.toInt(dto.id);
			entity.status = MStatusConstants.DBStatusCd.POST_FIXED;
			entity.customerId = dto.customerId;
			// 掲載確定担当者をセット
			entity.fixedUserId = Integer.parseInt(userDto.userId);
			// 掲載確定日時をセット
			entity.fixedDatetime = new Date();
			entity.version = dto.version;

			entityList.add(entity);
		}

		property.entityList = entityList;

		property.addScoutMailCount = NumberUtils.toInt(
										getCommonProperty("gc.scoutMail.webFix.add"),
										GourmetCareeConstants.FREE_SCOUT_MAIL_COUNT);
	}

	/**
	 * 代理店へ確定通知を送信
	 * @param property WEBデータプロパティ
	 */
	private void sendMailToAgency(WebdataProperty property) {

		FixedMaiDto maiDto = new FixedMaiDto();
		String kanriMailAddress = ResourceUtil.getProperties("sendMail.properties").getProperty("gc.mail.kanriAddress");
		String kanriName = ResourceUtil.getProperties("sendMail.properties").getProperty("gc.mail.kanriName");

		for (TWeb entity : property.entityList) {
			// メインアドレスへ送信(代理店)
			maiDto.resetTo();
			maiDto.addTo(entity.mCompany.mainMail);						// 送信先
			try {
				maiDto.setFrom(kanriMailAddress, kanriName);			// 送信元
			// アドレスがセットできない場合、初期値で送信
			} catch (UnsupportedEncodingException e) {
				// 送信元のセットに失敗した場合はエラーを送信する。（mailProperties.diconの値がセットされる）
				log.fatal("代理店へ確定通知を送信時、送信元がセットできませんでした。" + e);
			}
			maiDto.setId(String.valueOf(entity.id));					// 原稿番号
			maiDto.setCustomerName(entity.mCustomer.customerName);		// 顧客名
			maiDto.setCustomerId(String.valueOf(entity.customerId));	// 顧客ID
			maiDto.setCompanyName(entity.mCompany.companyName);			// 担当会社名
			gourmetCareeMai.sendFixedMail(maiDto);

			// サブアドレスへ送信(代理店)
			if (Integer.valueOf(MTypeConstants.SubmailReceptionFlg.RECEIVE).equals(entity.mCompany.submailReceptionFlg)
					&& StringUtils.isNotEmpty(entity.mCompany.subMail)) {
				maiDto.resetTo();
				maiDto.addTo(entity.mCompany.subMail);					// 送信先
				gourmetCareeMai.sendFixedMail(maiDto);
			}

			// メインアドレスへ送信(営業担当者)
			maiDto.resetTo();
			maiDto.addTo(entity.mSales.mainMail);						// 送信先
			gourmetCareeMai.sendFixedMail(maiDto);

			// サブアドレスへ送信(営業担当者)
			if (Integer.valueOf(MTypeConstants.SubmailReceptionFlg.RECEIVE).equals(entity.mSales.submailReceptionFlg)
					&& StringUtils.isNotEmpty(entity.mSales.subMail)) {
				maiDto.resetTo();
				maiDto.addTo(entity.mSales.subMail);					// 送信先
				gourmetCareeMai.sendFixedMail(maiDto);
			}

			// 管理者へ送信
			maiDto.resetTo();
			maiDto.addTo(kanriMailAddress);
			gourmetCareeMai.sendFixedMail(maiDto);
		}

	}

	/**
	 * 完了
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="LUMPDECIDE_EDIT_COMP")
	public String comp() {
		// 完了画面へ遷移
		return TransitionConstants.Webdata.JSP_APC04E03;
	}


	/**
	 * 初期表示遷移用
	 * @return 登録画面のパス
	 */
	private String show() {

		// セッションからデータを取得
		convertSessionData();

		if (ArrayUtils.isEmpty(webId)) {
			editForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.dataNotFound");
		}

		if (StringUtils.isEmpty(areaCd)) {
			editForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.required", "エリア");
		}

		// 初期表示データを取得
		try {
			createDtoList();
		} catch (NumberFormatException e) {
			editForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.fraudulentProcessError");
		} catch (WNoResultException e) {
			editForm.setExistDataFlgNg();
			throw new ActionMessagesException("errors.app.dataNotFound");
		}

		// 確認画面へ遷移
		return TransitionConstants.Webdata.JSP_APC04E02;
	}

	/**
	 * 表示用リストを生成
	 * @param entityList WEBデータ一覧リスト
	 * @return WEBデータ一覧Dtoをセットしたリスト
	 */
	private void convertShowList(List<VWebList> entityList) {

		List<ListDto> dtoList = new ArrayList<ListDto>();

		// Dtoにコピー
		for (VWebList entity : entityList) {

			ListDto dto = new ListDto();
			Beans.copy(entity, dto).execute();

			int[] serialArray = webAttributeService.getWebAttributeValueArray(entity.id, MTypeConstants.SERIAL_PUBLICATION_KBN.TYPE_CD);
			if (!ArrayUtils.isEmpty(serialArray)) {
				dto.serialPublicationKbn = String.valueOf(serialArray[0]);
			}

			dtoList.add(dto);
		}

		this.dtoList = dtoList;
	}

	/**
	 * セッションに保持しているデータの変換
	 */
	private void convertSessionData() {
		webId = (String[]) session.getAttribute("webId");
		areaCd = (String) session.getAttribute("areaCd");
	}


	/**
	 * セッションに保持しているデータをチェックします。
	 */
	private void checkSessionData() {
		if (ArrayUtils.isEmpty(webId)
				|| StringUtils.isBlank(areaCd)) {
			throw new FraudulentProcessException("不正な操作が行われました。" + editForm);
		}
	}

	/**
	 * DTOリストの作成
	 * @throws NumberFormatException
	 * @throws WNoResultException
	 */
	private void createDtoList() throws NumberFormatException, WNoResultException {
		List<VWebList> entityList = webListService.getVWebListById(webId);
		convertShowList(entityList);
	}

}