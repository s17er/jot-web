package com.gourmetcaree.shop.logic.logic;

import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.util.List;

import javax.annotation.Resource;

import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.common.exception.LoginFailedException;
import com.gourmetcaree.common.exception.LoginIdDuplicateException;
import com.gourmetcaree.common.util.DigestUtil;
import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.entity.MSales;
import com.gourmetcaree.db.common.entity.MSales.AuthLevelValue;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.service.SalesService;
import com.gourmetcaree.shop.logic.dto.SalesDto;

/**
 * 営業ロジック
 * @author Yamane
 *
 */
@Component(instance=InstanceType.SINGLETON)
public class SalesLogic extends AbstractShopLogic {

	/** 営業担当者サービス */
	@Resource
	protected SalesService salesService;

	/**
	 * 営業担当者のログイン（管理者のみ）
	 * @param loginId
	 * @param password
	 * @return
	 */
	public SalesDto login(String loginId, String password) throws LoginFailedException, LoginIdDuplicateException {

		if (loginId == null || password == null) {
			throw new IllegalArgumentException("ログインIDまたはパスワードがnullです");
		}

		String autorityKey = toCamelCase(MSales.AUTHORITY_CD) + "_IN";
		String[] autorityList = {AuthLevelValue.OWNER_ADMIN, AuthLevelValue.OWNER_STAF, AuthLevelValue.OWNER_SALES};
		BeanMap map = SqlUtils.createBeanMap();
		map.put(toCamelCase(MSales.LOGIN_ID), loginId);
		map.put(toCamelCase(MSales.PASSWORD), DigestUtil.createDigest(password));
		map.put(autorityKey, autorityList);
		map.put(toCamelCase(MSales.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		// ログインIDとパスワードで検索
		List<MSales> list = salesService.findByConditionWithCompany(map);
		if (list == null || list.size() == 0) {
			throw new LoginFailedException("ログインIDまたはパスワードに誤りがあるか、または権限がないか、会社情報がありません");
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

}
