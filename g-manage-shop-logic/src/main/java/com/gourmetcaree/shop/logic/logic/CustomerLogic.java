package com.gourmetcaree.shop.logic.logic;

import static com.gourmetcaree.common.util.SqlUtils.*;
import static jp.co.whizz_tech.commons.WztStringUtil.*;
import static org.seasar.framework.util.StringUtil.*;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.common.exception.LoginFailedException;
import com.gourmetcaree.common.exception.LoginIdDuplicateException;
import com.gourmetcaree.common.util.DigestUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.MCustomer;
import com.gourmetcaree.db.common.entity.MCustomerAccount;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.CustomerAccountService;
import com.gourmetcaree.db.common.service.CustomerService;
import com.gourmetcaree.db.common.service.CustomerSubMailService;
import com.gourmetcaree.shop.logic.dto.CustomerAccountDto;
import com.gourmetcaree.shop.logic.property.CustomerProperty;

/**
 * 顧客に関するロジッククラスです。
 * @author Takahiro Ando
 *
 */
@Component(instance=InstanceType.SINGLETON)
public class CustomerLogic extends AbstractShopLogic {

	/** 顧客アカウントサービス */
	@Resource
	protected CustomerAccountService customerAccountService;

	/** 顧客サービス */
	@Resource
	protected CustomerService customerService;

	@Resource
	private CustomerSubMailService customerSubMailService;

	/**
	 * 顧客アカウントについてのログイン処理です。
	 * @param loginId
	 * @param password
	 * @return
	 * @throws LoginFailedException
	 * @throws LoginIdDuplicateException
	 */
	public CustomerAccountDto login(String loginId, String password) throws LoginFailedException, LoginIdDuplicateException {

		if (loginId == null || password == null) {
			throw new IllegalArgumentException("ログインIDまたはパスワードがnullです");
		}

		Where where = new SimpleWhere()
								.eq(toCamelCase(MCustomerAccount.LOGIN_ID), loginId)
								.eq(toCamelCase(MCustomerAccount.PASSWORD), DigestUtil.createDigest(password))
								.eq(toCamelCase(MCustomerAccount.DELETE_FLG), DeleteFlgKbn.NOT_DELETED)
								.eq(dot(camelize(MCustomerAccount.M_CUSTOMER), toCamelCase(MCustomer.LOGIN_FLG)), MTypeConstants.LoginFlg.OK)
								.eq(dot(camelize(MCustomerAccount.M_CUSTOMER), toCamelCase(MCustomer.DELETE_FLG)), DeleteFlgKbn.NOT_DELETED)
								;

		// ログインIDとパスワードで検索
		List<MCustomerAccount> list;
		try {
			list = customerAccountService.findByConditionInnerJoin(
											camelize(MCustomerAccount.M_CUSTOMER), where,
											asc(toCamelCase(MCustomerAccount.LOGIN_ID)));

		} catch (WNoResultException e) {
			throw new LoginFailedException("ログインIDまたはパスワードに誤りがあるか、顧客情報がありません");

		}

		// 2件以上取得できると不正
		if (list.size() > 1) {
			throw new LoginIdDuplicateException(loginId + "が2件以上取得されました");
		}

		CustomerAccountDto customerAccountDto = new CustomerAccountDto();
		MCustomerAccount entity = list.get(0);

		// データをコピー
		Beans.copy(entity, customerAccountDto).execute();
		customerAccountDto.customerName = entity.mCustomer.customerName;
		customerAccountDto.areaCd = entity.mCustomer.areaCd;

		return customerAccountDto;
	}

	/**
	 * CustomerAccountDtoの取得
	 * @param loginId
	 * @return
	 * @throws WNoResultException
	 * @throws LoginIdDuplicateException
	 */
	public CustomerAccountDto getCustomerAccountDto(String loginId) throws WNoResultException, LoginIdDuplicateException {
		if (loginId == null) {
			throw new IllegalArgumentException("ログインIDがnullです");
		}

		Where where = new SimpleWhere()
					.eq(toCamelCase(MCustomerAccount.LOGIN_ID), loginId)
					.eq(toCamelCase(MCustomerAccount.DELETE_FLG), DeleteFlgKbn.NOT_DELETED)
					.eq(dot(camelize(MCustomerAccount.M_CUSTOMER), toCamelCase(MCustomer.LOGIN_FLG)), MTypeConstants.LoginFlg.OK)
					.eq(dot(camelize(MCustomerAccount.M_CUSTOMER), toCamelCase(MCustomer.DELETE_FLG)), DeleteFlgKbn.NOT_DELETED);

		// ログインIDとパスワードで検索
		List<MCustomerAccount> list;
			list = customerAccountService.findByConditionInnerJoin(
											camelize(MCustomerAccount.M_CUSTOMER), where,
											asc(toCamelCase(MCustomerAccount.LOGIN_ID)));


		// 2件以上取得できると不正
		if (list.size() > 1) {
			throw new LoginIdDuplicateException(loginId + "が2件以上取得されました");
		}

		CustomerAccountDto customerAccountDto = new CustomerAccountDto();
		MCustomerAccount entity = list.get(0);

		// データをコピー
		Beans.copy(entity, customerAccountDto).execute();
		customerAccountDto.customerName = entity.mCustomer.customerName;
		customerAccountDto.areaCd = entity.mCustomer.areaCd;
		customerAccountDto.mainMail = entity.mCustomer.mainMail;

		return customerAccountDto;
	}


	/**
	 * 顧客アカウントマスタを顧客マスタを結合して取得します。
	 * @param customerAccountId 顧客アカウントID
	 * @return 顧客アカウントエンティティ
	 * @throws WNoResultException データが取得できない場合のエラー
	 */
	public MCustomerAccount getCustomerAccontData(int customerAccountId) throws WNoResultException {

		// 検索した結果を返却
		MCustomerAccount entity = customerAccountService.findByIdInnerJoin(camelize(MCustomerAccount.M_CUSTOMER), customerAccountId, "");
		entity.mCustomer.mCustomerSubMailList = customerSubMailService.findByCustomerId(entity.customerId);
		return entity;
	}

	/**
	 * 顧客データを取得し、パスワードが同じかどうかチェックします。<br />
	 * プロパティの顧客アカウントエンティティにID、パスワードにチェックする値をセットして呼び出します。
	 * @param property 顧客プロパティ
	 * @return パスワードが同じ場合はtrue、違う場合はfalse
	 * @throws WNoResultException データが取得できない場合のエラー
	 */
	public boolean isSamePassword(CustomerProperty property) throws WNoResultException {

		// 引数のチェック
		checkEmptyPropCumstomerAccount(property);

		// 顧客データの取得
		MCustomerAccount mCustomerAccount = getCustomerAccontData(property.mCustomerAccount.id);

		// 取得した顧客データとパスワードが同じかどうかチェック(顧客の存在チェックをするため、結合して取得)
		return DigestUtil.isSame(mCustomerAccount.password, property.password);
	}

	/**
	 * 顧客データを取得し、パスワードが同じかどうかチェックします。<br />
	 * プロパティの顧客アカウントエンティティにID、パスワードにチェックする値をセットして呼び出します。
	 * @param property 顧客プロパティ
	 * @return パスワードが同じ場合はtrue、違う場合はfalse
	 * @throws WNoResultException データが取得できない場合のエラー
	 */
	public boolean isSamePassword(int customerAccountId, String password) throws WNoResultException {
		if (StringUtils.isBlank(password)) {
			return false;
		}

		// 顧客データの取得
		MCustomerAccount entity = getCustomerAccontData(customerAccountId);

		// 取得した顧客データとパスワードが同じかどうかチェック(顧客の存在チェックをするため、結合して取得)
		return DigestUtil.isSame(entity.password, password);
	}


	/**
	 * 顧客アカウントマスタのパスワードを暗号化して更新します。
	 * プロパティの顧客アカウントエンティティに更新対象、パスワードに更新する値をセットして呼び出します。
	 * @param property 顧客プロパティ
	 */
	public void updateCustomerPassword(CustomerProperty property) {

		// 引数のチェック
		checkEmptyPropCumstomerAccount(property);

		MCustomerAccount mCustomerAccount = property.mCustomerAccount;
		// パスワードを暗号化してセット
		mCustomerAccount.password = DigestUtil.createDigest(property.password);

		// 更新
		customerAccountService.update(mCustomerAccount);
	}

	/**
	 * 顧客の情報を更新します。
	 * @param mCustomerAccount
	 * @param password
	 */
	public void updateCustomerInfo(MCustomerAccount mCustomerAccount, String password) {
		if (mCustomerAccount == null) {
			throw new IllegalArgumentException("引数がnullです。");
		}

		if (StringUtils.isNotBlank(password)) {
			mCustomerAccount.password = DigestUtil.createDigest(password);
			customerAccountService.update(mCustomerAccount);
		}

		customerService.update(mCustomerAccount.mCustomer);


	}

	/**
	 * プロパティがnullの場合は、エラーを返す。
	 * @param property チェックするプロパティ
	 */
	private void checkEmptyProperty(CustomerProperty property) {

		// プロパティがnullの場合はエラー
		if (property == null) {
			throw new IllegalArgumentException("引数がnullです。");
		}
	}

	/**
	 * プロパティの顧客アカウントエンティティがnullの場合は、エラーを返す。
	 * @param property チェックするプロパティ
	 */
	private void checkEmptyPropCumstomerAccount(CustomerProperty property) {

		checkEmptyProperty(property);

		// プロパティがnullの場合はエラー
		if (property.mCustomerAccount == null) {
			throw new IllegalArgumentException("引数がnullです。");
		}
	}

}
