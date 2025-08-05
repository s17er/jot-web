package com.gourmetcaree.admin.pc.customer.action.customer;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.exception.IORuntimeException;
import org.seasar.framework.util.ResourceUtil;
import org.seasar.framework.util.StringUtil;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.customer.dto.customer.CompanySalesDto;
import com.gourmetcaree.admin.pc.customer.form.customer.CustomerForm.SubMailDto;
import com.gourmetcaree.admin.pc.customer.form.customer.InputForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.service.property.CustomerProperty;
import com.gourmetcaree.common.annotation.ManageLoginRequired;
import com.gourmetcaree.common.annotation.MethodAccess;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.common.util.DigestUtil;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.MCustomer;
import com.gourmetcaree.db.common.entity.MCustomerAccount;
import com.gourmetcaree.db.common.entity.MCustomerCompany;
import com.gourmetcaree.db.common.entity.MSentence;
import com.gourmetcaree.db.common.entity.TScoutMailLog;
import com.gourmetcaree.db.common.entity.TScoutMailManage;
import com.gourmetcaree.db.common.exception.ExistDataException;
import com.gourmetcaree.db.common.exception.NoExistCompanyDataException;
import com.gourmetcaree.db.common.exception.NoExistSalesDataException;

/**
 * 顧客登録アクションクラス
 * @author Takahiro Kimura
 * @version 1.0
 *
 */
@ManageLoginRequired(authLevel={ManageAuthLevel.ADMIN, ManageAuthLevel.STAFF, ManageAuthLevel.OTHER, ManageAuthLevel.SALES})
public class InputAction extends CustomerBaseAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(InputAction.class);

	/** 登録フォーム */
	@ActionForm
	@Resource
	protected InputForm inputForm;

	/** 担当会社・営業担当者削除用パス */
	private static String DELETE_ASSIGNED_PATH = "/customer/input/";

	/** 定型文を取得するためのループ回数 */
	private static final int DEFAULT_SENTENCE_CNT = 10;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset="resetForm", input = TransitionConstants.Customer.JSP_APD01C01)
	@MethodAccess(accessCode="CUSTOMER_INPUT_INDEX")
	public String index() {

		return show();
	}

	/**
	 * 担当会社・営業担当者追加
	 * @return
	 */
	@Execute(validator = false, validate = "companySalesValidate", reset = "resetMultibox", input = TransitionConstants.Customer.JSP_APD01C01)
	@MethodAccess(accessCode="CUSTOMER_INPUT_ADDASSIGNED")
	public String addAssigned() {

		// 追加された担当会社・営業担当者を表示用データに変更
		convertAssignedData(inputForm, DELETE_ASSIGNED_PATH);


		return TransitionConstants.Customer.JSP_APD01C01;
	}

	/**
	 * 担当会社・営業担当者削除
	 * @return
	 */
	@Execute(validator = false, reset = "resetMultibox", input = TransitionConstants.Customer.JSP_APD01C01)
	@MethodAccess(accessCode="CUSTOMER_INPUT_DELETEASSIGNED")
	public String deleteAssigned() {

		// 選択された担当会社・営業担当者を削除
		deleteAssignedSales(inputForm);

		return TransitionConstants.Customer.JSP_APD01C01;
	}

	/**
	 * 確認
	 * @return
	 */
	@Execute(validator = true, validate="validate", reset = "resetMultibox", input = TransitionConstants.Customer.JSP_APD01C01)
	@MethodAccess(accessCode="CUSTOMER_INPUT_CONF")
	public String conf() {

		// 担当会社・営業担当者存在チェック、ログインID重複チェック
		try {
			customerLogic.checkInputData("", inputForm.loginId, inputForm.assignedCompanyId, inputForm.assignedSalesId);
		} catch (ExistDataException e) {
			throw new ActionMessagesException("errors.app.existLoginId");
		} catch (NoExistCompanyDataException e) {
			throw new ActionMessagesException("errors.app.noAssignedCompanyData");
		} catch (NoExistSalesDataException e) {
			throw new ActionMessagesException("errors.app.noAssignedSalesData");
		}

		// パスワードを確認画面表示用に変換
		inputForm.dispPassword = GourmetCareeUtil.convertPassword(inputForm.password);

		// プロセスフラグを確認済みに設定
		inputForm.setProcessFlgOk();

		// 確認画面へ遷移
		return TransitionConstants.Customer.JSP_APD01C02;
	}

	/**
	 * 訂正
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="CUSTOMER_INPUT_CORRECT")
	public String correct() {

		// プロセスフラグを未確認に設定
		inputForm.setProcessFlgNg();

		// 各フラグ・スカウトメール数に値がなければ初期値をセット
		setFirstData(inputForm);
		if (StringUtils.isBlank(inputForm.scoutAddCount)) {
			inputForm.scoutAddCount = "0";
		}

		// 登録画面へ遷移
		return TransitionConstants.Customer.JSP_APD01C01;
	}

	/**
	 * 登録
	 * @return
	 */
	@Execute(validator = false, removeActionForm = true, input = TransitionConstants.Customer.JSP_APD01C01)
	@MethodAccess(accessCode="CUSTOMER_INPUT_SUBMIT")
	public String submit() {

		// プロセスフラグがfalseの場合、未確認としてエラー
		if (!inputForm.processFlg) {
			throw new FraudulentProcessException("不正な操作が行われました。" + inputForm);
		}

		// 担当会社・営業担当者存在チェック、ログインID重複チェック
		try {
			customerLogic.checkInputData("", inputForm.loginId, inputForm.assignedCompanyId, inputForm.assignedSalesId);
		} catch (ExistDataException e) {
			throw new ActionMessagesException("errors.app.existLoginId");
		} catch (NoExistCompanyDataException e) {
			throw new ActionMessagesException("errors.app.noAssignedCompanyData");
		} catch (NoExistSalesDataException e) {
			throw new ActionMessagesException("errors.app.noAssignedSalesData");
		}

		// 登録処理
		doInsertCustomer();

		log.debug("顧客データを登録しました。");

		return TransitionConstants.Customer.REDIRECT_CUSTOMER_INPUT_COMP;
	}

	/**
	 * 完了
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="CUSTOMER_INPUT_COMP")
	public String comp() {
		// 完了画面へ遷移
		return TransitionConstants.Customer.JSP_APD01C03;
	}

	/**
	 * 初期表示遷移用
	 * @return 登録画面のパス
	 */
	private String show() {

		// 初期値をセット
		inputForm.submailReceptionFlg = String.valueOf(MCustomer.SubMailReceptionFlgKbn.RECEIVE);
		inputForm.loginFlg = String.valueOf(MCustomer.LoginFlgKbn.LOGIN_OK);
		inputForm.publicationFlg = String.valueOf(MCustomer.PublicationFlg.PUBLICATION_OK);
		inputForm.scoutUseFlg = String.valueOf(MCustomer.ScoutUseFlg.SCOUT_USE_OK);
		inputForm.scoutAddCount = "0";
		inputForm.publicationEndDisplayFlg = String.valueOf(MCustomer.PublicationEndDisplayFlg.DISPLAY_OK);
		inputForm.mailMagazineReceptionFlg = String.valueOf(MTypeConstants.CustomerMailMagazineReceptionFlg.RECEIVE);
		inputForm.setSubMailEntryForm();
		inputForm.setHopepageEntryForm();

		// 登録画面へ遷移
		return TransitionConstants.Customer.JSP_APD01C01;
	}

	/**
	 * 顧客データ登録処理
	 */
	private void doInsertCustomer() {

		CustomerProperty property = new CustomerProperty();

		// 登録用データ生成
		createInsertData(property);

		// 顧客登録
		customerLogic.insertCustomerData(property);

	}

	/**
	 * 登録用データ生成
	 * @param property 顧客プロパティ
	 */
	private void createInsertData(CustomerProperty property) {

		// 顧客マスタエンティティ生成
		property.mCustomer = convertToCustomer();

		// 顧客アカウントマスタ登録データ生成
		property.mCustomerAccount = convertToMCustomerAccount();

		// 顧客担当会社マスタ登録データ生成
		property.mCustomerCompanyList = convertToCustomerCompanyData();


		// スカウトメール数テーブル登録データ生成
		if (isScoutMailAdded()) {
			property.tScoutMailManage = convertToTScoutMailCount();
			property.tScoutMailLog = convertToTScoutMailAddLog();
		}

		// 定型文マスタ登録データ生成
		property.mSentenceList = convertToSentence();

		// サブメールの登録
		property.customerSubMailList = convertToSubMailList(inputForm);

		// ホームページの登録
		property.customerHomepageList = convertToHomepageList(inputForm);

		// メルマガ受信エリアの登録
		property.mailMagazineAreaCdList = convertToIntegerMailManazineAreaCd(inputForm);
	}

	/**
	 * スカウトメールが追加されているかどうか
	 * @return
	 */
	private boolean isScoutMailAdded() {

		if(StringUtils.isNotBlank(inputForm.scoutUseEndDatetime)) {
			return true;
		}

		if (inputForm.scoutAddCount == null) {
			throw new FraudulentProcessException("スカウトメール数がnullです。");
		}

		if ("0".equals(inputForm.scoutAddCount)) {
			return false;
		}
		try {
			if (Integer.parseInt(inputForm.scoutAddCount) > 0) {
				return true;
			}

			// 0は先に判定しているため、残りはマイナスのみ
			throw new FraudulentProcessException("スカウトメール数がマイナスです。");

		} catch (NumberFormatException e) {
			throw new FraudulentProcessException(e);
		}
	}

	/**
	 * 顧客マスタ登録データ生成
	 * @return 顧客マスタエンティティ
	 */
	private MCustomer convertToCustomer() {

		MCustomer mCustomer = new MCustomer();
		Beans.copy(inputForm, mCustomer).excludes("customerNameKana", "contactNameKana").execute();
		mCustomer.customerNameKana = GourmetCareeUtil.superTrim(inputForm.customerNameKana);
		mCustomer.contactNameKana = GourmetCareeUtil.superTrim(inputForm.contactNameKana);

		// サブメールの1つ目を親テーブルに設定しておく
		if (CollectionUtils.isNotEmpty(inputForm.subMailDtoList)) {
			SubMailDto subMailDto = inputForm.subMailDtoList.get(0);
			if (StringUtils.isNotEmpty(subMailDto.subMail)) {
				mCustomer.subMail = subMailDto.subMail;
				mCustomer.submailReceptionFlg = NumberUtils.toInt(subMailDto.submailReceptionFlg, 0);
			}
		}

		return mCustomer;
	}

	/**
	 * 顧客アカウントマスタ登録データ生成
	 * @return 顧客アカウントマスタエンティティ
	 */
	private MCustomerAccount convertToMCustomerAccount() {

		MCustomerAccount mCustomerAccount = new MCustomerAccount();
		Beans.copy(inputForm, mCustomerAccount).excludes("password").execute();
		mCustomerAccount.password = DigestUtil.createDigest(inputForm.password);
		return mCustomerAccount;
	}

	/**
	 * 顧客担当会社マスタ登録データ生成
	 * @param list 顧客担当会社マスタエンティティリスト
	 */
	private List<MCustomerCompany> convertToCustomerCompanyData() {

		List<MCustomerCompany> entityList = new ArrayList<MCustomerCompany>();
		MCustomerCompany entity = new MCustomerCompany();
		entity.companyId = NumberUtils.toInt(inputForm.assignedCompanyId);
		entity.salesId = NumberUtils.toInt(inputForm.assignedSalesId);
		entityList.add(entity);

		for (CompanySalesDto dto : inputForm.companySalesList) {
			MCustomerCompany mCompany = new MCustomerCompany();

			Beans.copy(dto, mCompany).execute();
			entityList.add(mCompany);
		}

		return entityList;

	}

	/**
	 * スカウトメール数テーブル登録データ生成
	 * @return スカウトメール数テーブルエンティティ
	 * @throws ParseException
	 */
	private TScoutMailManage convertToTScoutMailCount() {
		TScoutMailManage entity = new TScoutMailManage();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		try {
			if(StringUtils.isNotBlank(inputForm.scoutUseEndDatetime)) {
				entity.scoutRemainCount = 10000;
				entity.scoutMailKbn = MTypeConstants.ScoutMailKbn.UNLIMITED;
				entity.useEndDatetime = new Timestamp(sdf.parse(inputForm.scoutUseEndDatetime + " 10:29:59").getTime());
				entity.useStartDatetime = new Timestamp(DateUtils.getJustDate().getTime());
			} else {
				entity.scoutRemainCount = Integer.parseInt(inputForm.scoutAddCount);
				entity.scoutMailKbn = MTypeConstants.ScoutMailKbn.BOUGHT;
				entity.useEndDatetime = createHalfYearLatorTimestamp(sdf.parse(inputForm.scoutUseStartDatetime));
				entity.useStartDatetime = new Timestamp(sdf.parse(inputForm.scoutUseStartDatetime).getTime());
			}
		}catch (ParseException e) {
			throw new ActionMessagesException("errors.app.dateFailed");
		}
		return entity;
	}

	/**
	 * スカウトメール追加履歴テーブル登録データ生成
	 * @return スカウトメール追加履歴テーブルエンティティ
	 */
	private TScoutMailLog convertToTScoutMailAddLog() {
		TScoutMailLog entity = new TScoutMailLog();
		if(StringUtils.isNotBlank(inputForm.scoutUseEndDatetime)) {
			entity.addScoutCount = 10000;
		} else {
			entity.addScoutCount = Integer.parseInt(inputForm.scoutAddCount);
		}
		entity.scoutMailLogKbn = MTypeConstants.ScoutMailLogKbn.ADD;
		return entity;
	}

	/**
	 * 定型文マスタ登録データを生成
	 * @return 定型文マスタエンティティリスト
	 */
	private List<MSentence> convertToSentence() {
		List<MSentence> entityList = new ArrayList<MSentence>();

		try {
			Properties sentence = ResourceUtil.getProperties("sentence.properties");
			// プロパティが取得できなければリストをそのまま返す
			if (sentence == null) {
				return entityList;
			}

			// 定型文の初期値を登録する（プロパティに登録されている連番を取得する）
			for (int i = 1; i < DEFAULT_SENTENCE_CNT; i++) {

				MSentence ouboEntity = new MSentence();
				// タイトル
				String title = sentence.getProperty("gc.title.initSentence" + i);
				// 本文
				String body = sentence.getProperty("gc.body.initSentence" + i);

				// 値が取得できなければ、処理終了
				if (StringUtil.isEmpty(title) || StringUtil.isEmpty(body)) {
					break;
				}
				// エンティティにセット
				ouboEntity.sentenceTitle = title;
				ouboEntity.body = body;

				entityList.add(ouboEntity);
			}

			return entityList;

		// プロパティが取得できなければリストをそのまま返す
		} catch (IORuntimeException e) {
			return entityList;
		}
	}

}