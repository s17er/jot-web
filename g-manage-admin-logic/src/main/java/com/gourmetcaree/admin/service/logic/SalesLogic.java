package com.gourmetcaree.admin.service.logic;

import static jp.co.whizz_tech.commons.WztStringUtil.toCamelCase;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;

import com.gourmetcaree.admin.service.dto.SalesDto;
import com.gourmetcaree.common.exception.LoginIdDuplicateException;
import com.gourmetcaree.common.exception.LoginFailedException;
import com.gourmetcaree.common.util.DigestUtil;
import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.entity.MCompany;
import com.gourmetcaree.db.common.entity.MSales;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;

import com.gourmetcaree.db.common.exception.AgencyAuthLevelException;
import com.gourmetcaree.db.common.exception.AuthLevelException;
import com.gourmetcaree.db.common.exception.ExistDataException;

import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.CompanyService;
import com.gourmetcaree.db.common.service.SalesService;

/**
 * 営業担当者に関するロジッククラスです。
 * @author Hiroyuki Sugimoto
 *
 */
public class SalesLogic {

	/** 営業担当者サービス */
	@Resource
	protected SalesService salesService;


	/** 会社マスタサービス */
	@Resource
	protected CompanyService companyService;

	/**
	 * 営業担当者のログイン処理を行います。
	 * @param loginId
	 * @param password
	 * @return
	 * @throws LoginFailedException
	 * @throws LoginIdDuplicateException
	 */
	public SalesDto login(String loginId, String password) throws LoginFailedException, LoginIdDuplicateException {

		if (loginId == null || password == null) {
			throw new IllegalArgumentException("ログインIDまたはパスワードがnullです");
		}

		BeanMap map = SqlUtils.createBeanMap();
		map.put(toCamelCase(MSales.LOGIN_ID), loginId);
		map.put(toCamelCase(MSales.PASSWORD), DigestUtil.createDigest(password));
		map.put(toCamelCase(MSales.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		// ログインIDとパスワードで検索
		List<MSales> list = salesService.findByConditionWithCompany(map);
		if (list == null || list.size() == 0) {
			throw new LoginFailedException("ログインIDまたはパスワードに誤りがあるか、会社情報がありません");
		}

		// 2件以上取得できると不正
		if (list.size() > 1) {
			throw new LoginIdDuplicateException(loginId + "が2件以上取得されました");
		}

		MSales salesEntity = list.get(0);
		SalesDto salesDto = new SalesDto();

		// データをコピー
		Beans.copy(salesEntity, salesDto).execute();
		salesDto.companyName = salesEntity.mCompany.companyName;

		return salesDto;
	}

	/**
	 * ログインIDの重複、会社の存在をチェック、権限チェック
	 * @param loginId ログインID
	 * @param companyId 会社ID
	 * @throws ExistDataException
	 * @throws WNoResultException
	 * @throws AgencyAuthLevelException
	 * @throws AuthLevelException
	 */
	public void checkInputData(String id, String loginId, String companyId, String authorityCd)
	throws ExistDataException, WNoResultException, AgencyAuthLevelException, AuthLevelException {

		if (StringUtils.isBlank(id)) {
			// ログインIDの重複チェック
			if (!salesService.existSalesDataByLoginId(loginId)) {
				throw new ExistDataException("顧客マスタでログインIDが重複しています。loginId："+ loginId);
			}
		} else {
			// ログインIDの重複チェック
			if (!salesService.existSalesDataByIdLoginId(NumberUtils.toInt(id), loginId)) {
				throw new ExistDataException("顧客マスタでログインIDが重複しています。id:"+ id +"loginId："+ loginId);
			}
		}

		// 会社存在チェック
		MCompany entity = companyService.getCompanyData(NumberUtils.toInt(companyId));

		if (entity == null) {
			throw new WNoResultException();
		} else {
			// 権限チェック
			if (MCompany.AgencyFlgValue.AGENCY == entity.agencyFlg) {
				if (!MSales.AuthLevelValue.OWNER_AGENCY.equals(authorityCd)) {
					throw new AgencyAuthLevelException("MCompanyのagencyFlgが代理店の場合に権限が代理店ではありません。authorityCd：" + authorityCd);
				}
			} else {
				if (MSales.AuthLevelValue.OWNER_AGENCY.equals(authorityCd)) {
					throw new AuthLevelException("MCompanyのagencyFlgが代理店以外の場合に権限が代理店ではありません。authorityCd：" + authorityCd);
				}
			}
		}

	}
}
