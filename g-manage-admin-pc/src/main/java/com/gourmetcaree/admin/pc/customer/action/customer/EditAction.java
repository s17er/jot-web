package com.gourmetcaree.admin.pc.customer.action.customer;

import java.io.UnsupportedEncodingException;
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
import org.seasar.framework.util.ResourceUtil;
import org.seasar.struts.annotation.ActionForm;
import org.seasar.struts.annotation.Execute;
import org.seasar.struts.exception.ActionMessagesException;

import com.gourmetcaree.admin.pc.customer.dto.customer.CompanySalesDto;
import com.gourmetcaree.admin.pc.customer.form.customer.CustomerForm.SubMailDto;
import com.gourmetcaree.admin.pc.customer.form.customer.EditForm;
import com.gourmetcaree.admin.pc.sys.constants.TransitionConstants;
import com.gourmetcaree.admin.pc.sys.dto.mai.ScoutMailAddMaiDto;
import com.gourmetcaree.admin.pc.sys.mai.GourmetcareeMai;
import com.gourmetcaree.admin.service.property.CustomerProperty;
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
import com.gourmetcaree.db.common.entity.TScoutMailLog;
import com.gourmetcaree.db.common.entity.TScoutMailManage;
import com.gourmetcaree.db.common.entity.VActiveScoutMail;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.ExistDataException;
import com.gourmetcaree.db.common.exception.NoExistCompanyDataException;
import com.gourmetcaree.db.common.exception.NoExistSalesDataException;
import com.gourmetcaree.db.common.service.ActiveScoutMailService;
import com.gourmetcaree.db.common.service.CustomerCompanyService;

/**
 * 顧客編集アクションクラス
 * @author Takahiro Kimuara
 * @version 1.0
 *
 */
public class EditAction extends CustomerBaseAction {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(EditAction.class);

	/** 編集フォーム */
	@ActionForm
	@Resource
	protected EditForm editForm;

	/** 顧客担当会社マスタサービス */
	@Resource
	protected CustomerCompanyService customerCompanyService;

	/** メール送信用インタフェース */
	@Resource
	protected GourmetcareeMai gourmetCareeMail;

	/** 担当会社・営業担当者削除用パス */
	private static String DELETE_ASSIGNED_PATH = "/customer/edit/";

	@Resource
	private ActiveScoutMailService activeScoutMailService;

	private boolean createScoutMailLogFlg = false;

	/**
	 * 初期表示
	 * @return
	 */
	@Execute(validator = false, reset="resetForm", urlPattern = "index/{id}", input = TransitionConstants.Customer.JSP_APD01E01)
	@MethodAccess(accessCode="CUSTOMER_EDIT_INDEX")
	public String index() {
		return show();
	}

	/**
	 * 担当会社・営業担当者追加
	 * @return
	 */
	@Execute(validator = false, validate = "companySalesValidate", reset = "resetMultibox", input = TransitionConstants.Customer.JSP_APD01E01)
	@MethodAccess(accessCode="CUSTOMER_EDIT_ADDASSIGNED")
	public String addAssigned() {

		// 追加された担当会社・営業担当者を表示用データに変更
		convertAssignedData(editForm, DELETE_ASSIGNED_PATH);


		return TransitionConstants.Customer.JSP_APD01E01;
	}

	/**
	 * 担当会社・営業担当者削除
	 * @return
	 */
	@Execute(validator = false, reset = "resetMultibox", input = TransitionConstants.Customer.JSP_APD01E01)
	@MethodAccess(accessCode="CUSTOMER_EDIT_DELETEASSIGNED")
	public String deleteAssigned() {

		// 選択された担当会社・営業担当者を削除
		deleteAssignedSales(editForm);

		return TransitionConstants.Customer.JSP_APD01E01;
	}

	/**
	 * 確認
	 * @return
	 */
	@Execute(validator = true, validate = "validate", reset = "resetMultibox", input = TransitionConstants.Customer.JSP_APD01E01)
	@MethodAccess(accessCode="CUSTOMER_EDIT_CONF")
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

		// 担当会社・営業担当者存在チェック、ログインID重複チェック
		try {
			customerLogic.checkInputData(editForm.id, editForm.loginId, editForm.assignedCompanyId, editForm.assignedSalesId);
		} catch (ExistDataException e) {
			throw new ActionMessagesException("errors.app.existLoginId");
		} catch (NoExistCompanyDataException e) {
			throw new ActionMessagesException("errors.app.noAssignedCompanyData");
		} catch (NoExistSalesDataException e) {
			throw new ActionMessagesException("errors.app.noAssignedSalesData");
		}

		// パスワードを確認画面表示用に変換
		editForm.dispPassword = GourmetCareeUtil.convertPassword(editForm.password);

		// プロセスフラグを確認済みに設定
		editForm.setProcessFlgOk();

		// 確認画面へ遷移
		return TransitionConstants.Customer.JSP_APD01E02;
	}

	/**
	 * 戻る
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="CUSTOMER_EDIT_BACK")
	public String back() {
		// 詳細画面へ遷移
		return TransitionConstants.Customer.REDIRECT_CUSTOMER_DETAIL_INDEX + editForm.id + TransitionConstants.REDIRECT_STR;
	}

	/**
	 * 一覧へ戻る
	 * @return 一覧画面の検索メソッド
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="CUSTOMER_EDIT_BACKLIST")
	public String backList() {
		// 一覧画面へ遷移
		return TransitionConstants.Customer.REDIRECT_CUSTOMER_LIST_SEARCHAGAIN;
	}

	/**
	 * 訂正
	 * @return
	 */
	@Execute(validator = false)
	@MethodAccess(accessCode="CUSTOMER_EDIT_CORRECT")
	public String correct() {

		// パラメータが空の場合はエラー
		checkArgsNull(NO_BLANK_FLG_NG, editForm.id);

		// プロセスフラグを未確認に設定
		editForm.setProcessFlgNg();

		// 登録画面へ遷移
		return TransitionConstants.Customer.JSP_APD01E01;
	}

	/**
	 * 登録
	 * @return
	 */
	@Execute(validator = false, removeActionForm = true, input = TransitionConstants.Customer.JSP_APD01E01)
	@MethodAccess(accessCode="CUSTOMER_EDIT_SUBMIT")
	public String submit() {

		// プロセスフラグがfalseの場合、未確認としてエラー
		if (!editForm.processFlg) {
			throw new FraudulentProcessException("不正な操作が行われました。" + editForm);
		}

		// セッションのIdと、画面で保持するIdが違う場合エラー
		if (!editForm.id.equals(editForm.hiddenId)) {
			throw new FraudulentProcessException("不正な操作が行われました。" + editForm);
		}

		// 担当会社・営業担当者存在チェック、ログインID重複チェック
		try {
			customerLogic.checkInputData(editForm.id, editForm.loginId, editForm.assignedCompanyId, editForm.assignedSalesId);
		} catch (ExistDataException e) {
			throw new ActionMessagesException("errors.app.existLoginId");
		} catch (NoExistCompanyDataException e) {
			throw new ActionMessagesException("errors.app.noAssignedCompanyData");
		} catch (NoExistSalesDataException e) {
			throw new ActionMessagesException("errors.app.noAssignedSalesData");
		}

		// 登録処理
		try {
			doUpdateCustomer();
		} catch (ParseException e) {
			throw new ActionMessagesException("errors.app.dateFailed");
		}

		log.debug("営業担当者をUPDATEしました");

		// スカウトメール追加メール送信
		// スカウトメールが追加されている場合のみ、メールを送信
		sendMailToCustomer();

		// WEBデータ詳細から遷移した時のことを想定し、顧客IDを渡す
		return GourmetCareeUtil.makePath(TransitionConstants.Customer.CUSTOMER_EDIT_COMP, editForm.id, TransitionConstants.REDIRECT_STR);
	}

	/**
	 * 完了
	 * @return
	 */
	@Execute(validator = false, urlPattern = "comp/{id}")
	@MethodAccess(accessCode="CUSTOMER_EDIT_COMP")
	public String comp() {

		// 完了画面へ遷移
		return TransitionConstants.Customer.JSP_APD01E03;
	}

	/**
	 * 初期表示遷移用
	 * @return 登録画面のパス
	 */
	private String show() {

		// idがnullかチェック
		checkArgsNull(NO_BLANK_FLG_NG, editForm.id);

		// 自社スタッフが編集可能かチェック
		if (ManageAuthLevel.STAFF.value().equals(userDto.authLevel)) {
			if (!customerCompanyService.isCustomerDataExistByCustomerId(NumberUtils.toInt(editForm.id))) {
				editForm.setExistDataFlgNg();
				throw new ActionMessagesException("errors.notEditByStaffCustomerError");
			}
			// 代理店が表示可能顧客かチェック
 		} else if (ManageAuthLevel.AGENCY.value().equals(userDto.authLevel)) {
 			if (!customerCompanyService.isCustomerDataExistByCustomerIdSalesId(NumberUtils.toInt(editForm.id), NumberUtils.toInt(userDto.userId))) {
 				editForm.setExistDataFlgNg();
				throw new ActionMessagesException("errors.noHandlingCustomerError");
 			}
 		}

		// 表示データ取得
		convertDispData(editForm);

		// 初期値をセット
		editForm.scoutAddCount = "0";
		editForm.scoutRemoveCount = "0";
		editForm.scoutUseEndDatetime = activeScoutMailService.getUnlimitScoutMailUseEndDateTime(NumberUtils.toInt(editForm.id));

		// サブメールのフォームをセット
		editForm.setSubMailEntryForm();
		// ホームページのフォームをセット
		editForm.setHopepageEntryForm();

		// チェック用のIdに値を保持
		editForm.hiddenId = editForm.id;

		// 登録画面へ遷移
		return TransitionConstants.Customer.JSP_APD01E01;
	}

	/**
	 * 顧客データ更新処理
	 * @throws ParseException
	 */
	private void doUpdateCustomer() throws ParseException {

		CustomerProperty property = new CustomerProperty();

		// 更新用データ生成
		createUpdateData(property);

		// 顧客情報更新
		customerLogic.updateCustomerData(property);

	}

	/**
	 * 登録用データ生成
	 * @param property 顧客プロパティ
	 * @throws ParseException
	 */
	private void createUpdateData(CustomerProperty property) throws ParseException {

		// 顧客マスタ更新データ生成
		property.mCustomer = convertToCustomerData();

		// 顧客アカウントマスタ更新データ生成
		property.mCustomerAccount = convertToCustomerAccount();

		// 顧客担当会社マスタ更新データ生成
		property.mCustomerCompanyList = convertToCustomerCompany();

		// スカウトメール数の更新がある場合
		// スカウトメール数テーブル更新データ生成
		// スカウトメール追加履歴テーブル更新データ生成
		createScoutMailManageEntity(property, editForm.scoutAddCount);
		if(createScoutMailLogFlg) {
			createScoutMailLogEntiy(property);
		}

		// サブメールの登録
		property.customerSubMailList = convertToSubMailList(editForm);

		// ホームページの登録
		property.customerHomepageList = convertToHomepageList(editForm);

		// メルマガ受信エリアの登録
		property.mailMagazineAreaCdList = convertToIntegerMailManazineAreaCd(editForm);
	}

	/**
	 * スカウトメール管理エンティティ作成
	 * @param property
	 * @param scoutMailCount
	 * @throws ParseException
	 */
	private void createScoutMailManageEntity(CustomerProperty property, String scoutMailCount) throws ParseException {

		TScoutMailManage updateEntity = new TScoutMailManage();

		if(StringUtils.isNotBlank(editForm.scoutUseEndDatetime)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			VActiveScoutMail entity = activeScoutMailService.getUnlimitScoutMail(NumberUtils.toInt(editForm.id));

			if(entity != null) {
				Beans.copy(entity, updateEntity).execute();
				updateEntity.useEndDatetime = new Timestamp(sdf.parse(editForm.scoutUseEndDatetime + " 10:29:59").getTime());
				property.insertScoutManageFlg = false;
			} else {
				updateEntity.customerId = NumberUtils.toInt(editForm.id);
				updateEntity.scoutRemainCount = 10000;
				updateEntity.scoutMailKbn = MTypeConstants.ScoutMailKbn.UNLIMITED;
				updateEntity.useEndDatetime = new Timestamp(sdf.parse(editForm.scoutUseEndDatetime + " 10:29:59").getTime());
				updateEntity.useStartDatetime = new Timestamp(DateUtils.getJustDate().getTime());
				createScoutMailLogFlg = true;
				property.insertScoutManageFlg = true;
			}
		} else if (StringUtils.isNotBlank(editForm.scoutAddCount) && !EditForm.eqZero(editForm.scoutAddCount)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
			updateEntity.customerId = NumberUtils.toInt(editForm.id);
			updateEntity.scoutRemainCount = Integer.parseInt(editForm.scoutAddCount);
			updateEntity.scoutMailKbn = MTypeConstants.ScoutMailKbn.BOUGHT;
			updateEntity.useEndDatetime = createHalfYearLatorTimestamp(sdf.parse(editForm.scoutUseStartDatetime));
			updateEntity.useStartDatetime = new Timestamp(sdf.parse(editForm.scoutUseStartDatetime).getTime());
			createScoutMailLogFlg = true;
			property.insertScoutManageFlg = true;
		} else if(!EditForm.eqZero(editForm.scoutRemoveCount)){
			VActiveScoutMail entity = activeScoutMailService.getLastedBoughtScoutMail(NumberUtils.toInt(editForm.id));
			if(entity != null) {
				Beans.copy(entity, updateEntity).execute();
				updateEntity.customerId = entity.customerId;
				int count = entity.scoutRemainCount - Integer.parseInt(editForm.scoutRemoveCount);
				updateEntity.scoutRemainCount = count > 0 ? count : 0;
				createScoutMailLogFlg = true;
				property.insertScoutManageFlg = false;
			}
		} else if (StringUtils.isBlank(editForm.scoutUseEndDatetime)) {
			VActiveScoutMail entity = activeScoutMailService.getUnlimitScoutMail(NumberUtils.toInt(editForm.id));

			if(entity != null) {
				Beans.copy(entity, updateEntity).execute();
				updateEntity.deleteFlg = DeleteFlgKbn.DELETED;
				property.insertScoutManageFlg = false;
			} else {
				updateEntity = null;
			}

		}

		property.tScoutMailManage = updateEntity;
	}

	/**
	 * スカウトメールログエンティティ作成
	 * @param property
	 * @param scoutMailLogKbn
	 */
	private void createScoutMailLogEntiy(CustomerProperty property) {
		TScoutMailLog entity = new TScoutMailLog();
		if(StringUtils.isNotBlank(editForm.scoutUseEndDatetime)) {
			entity.addScoutCount = 10000;
			entity.scoutMailLogKbn = MTypeConstants.ScoutMailLogKbn.ADD;
		} else if(StringUtils.isNotBlank(editForm.scoutAddCount) && !EditForm.eqZero(editForm.scoutAddCount)){
			entity.addScoutCount = Integer.parseInt(editForm.scoutAddCount);
			entity.scoutMailLogKbn = MTypeConstants.ScoutMailLogKbn.ADD;
		} else {
			entity.addScoutCount =  - Integer.parseInt(editForm.scoutRemoveCount);
			entity.scoutMailLogKbn = MTypeConstants.ScoutMailLogKbn.ADD_MANUAL;
		}

		property.tScoutMailLog = entity;
	}




	/**
	 * スカウトメール使用期限のチェック
	 */
	private void checkScoutUseEndDatetime() {
		if (editForm.scoutUseEndDatetime == null) {
			throw new FraudulentProcessException("スカウトメール使用期限がありません");
		}
	}

	/**
	 * 顧客マスタ更新データ生成
	 * @return 顧客マスタエンティティ
	 */
	private MCustomer convertToCustomerData() {

		MCustomer mCustomer = new MCustomer();
		Beans.copy(editForm, mCustomer).excludes("registrationDatetime", "contactNameKana", "customerNameKana").execute();
		mCustomer.version = editForm.customerVersion;
		mCustomer.contactNameKana = GourmetCareeUtil.superTrim(editForm.contactNameKana);
		mCustomer.customerNameKana = GourmetCareeUtil.superTrim(editForm.customerNameKana);

		// サブメールの1つ目を親テーブルに設定しておく
		editForm.packSubMailDtoList();
		if (CollectionUtils.isNotEmpty(editForm.subMailDtoList)) {
			SubMailDto subMailDto = editForm.subMailDtoList.get(0);
			if (StringUtils.isNotEmpty(subMailDto.subMail)) {
				mCustomer.subMail = subMailDto.subMail;
				mCustomer.submailReceptionFlg = NumberUtils.toInt(subMailDto.submailReceptionFlg, 0);
			}
		} else {
			mCustomer.subMail = "";
			mCustomer.submailReceptionFlg = 0;
		}

		return mCustomer;
	}

	/**
	 * 顧客アカウントマスタ更新データ生成
	 * @return 顧客アカウントマスタエンティティ
	 */
	private MCustomerAccount convertToCustomerAccount() {

		MCustomerAccount mCustomerAccount = new MCustomerAccount();
		mCustomerAccount.id = editForm.customerAccountId;
		mCustomerAccount.loginId = editForm.loginId;
		if (!StringUtils.isEmpty(editForm.password)) {
			mCustomerAccount.password = DigestUtil.createDigest(editForm.password);
		} else {
			mCustomerAccount.password = null;
		}

		mCustomerAccount.version = editForm.customerAccountVersion;

		return mCustomerAccount;
	}

	/**
	 * 顧客担当会社マスタ更新データ生成
	 */
	private List<MCustomerCompany> convertToCustomerCompany() {

		List<MCustomerCompany> entityList = new ArrayList<MCustomerCompany>();
		MCustomerCompany entity = new MCustomerCompany();
		entity.companyId = NumberUtils.toInt(editForm.assignedCompanyId);
		entity.salesId = NumberUtils.toInt(editForm.assignedSalesId);

		entityList.add(entity);

		for (CompanySalesDto dto : editForm.companySalesList) {
			MCustomerCompany mCompany = new MCustomerCompany();

			Beans.copy(dto, mCompany).execute();
			entityList.add(mCompany);
		}

		return entityList;

	}


	/**
	 * スカウトメールを追加した場合に、顧客へお知らせするメールを送信
	 */
	private void sendMailToCustomer() {

		final int mailCount = Integer.valueOf(editForm.scoutAddCount);
		if (mailCount <= 0) {
			log.info("スカウトメールカウントが0件以下なのでメールは送信しません。");
			return;
		}
		ScoutMailAddMaiDto maiDto = new ScoutMailAddMaiDto();
		Properties properties = ResourceUtil.getProperties("sendMail.properties");
		String kanriAddress = properties.getProperty("gc.mail.kanriAddress");
		String kanriName = properties.getProperty("gc.mail.kanriName");

		// 宛先の生成
		List<String> toList = new ArrayList<String>();
		toList.add(editForm.mainMail);

		// サブメール取得
		for (SubMailDto subMailDto : editForm.subMailDtoList) {
			if (String.valueOf(MTypeConstants.SubmailReceptionFlg.RECEIVE).equals(subMailDto.submailReceptionFlg) && StringUtils.isNotEmpty(subMailDto.subMail)) {
				toList.add(subMailDto.subMail);
			}
		}
		toList.add(kanriAddress);							// 管理者宛て

		// メールの内容をセット
		try {
			maiDto.setFrom(kanriAddress, kanriName);
		// アドレスがセットできない場合、初期値で送信
		} catch (UnsupportedEncodingException e) {
			// 送信元のセットに失敗した場合はエラーを送信する。（mailProperties.diconの値がセットされる）
			log.fatal("スカウトメールを追加した場合に、顧客へお知らせするメールを送信時、送信元がセットできませんでした。" + e);
		}
		// 送信元アドレス
		maiDto.setCustomerName(editForm.customerName);		// 顧客名
		maiDto.setScoutCount(String.valueOf(mailCount));			// スカウトメール追加数

		for (String toStr : toList) {
			maiDto.resetTo();
			maiDto.addTo(toStr);							// 宛先
			gourmetCareeMail.sendScoutMailAddMail(maiDto);
		}
		log.debug("スカウトメールの追加メールを送信しました。");

	}
}