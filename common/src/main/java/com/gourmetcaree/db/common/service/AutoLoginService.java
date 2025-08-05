package com.gourmetcaree.db.common.service;

import static jp.co.whizz_tech.commons.WztStringUtil.toCamelCase;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.common.exception.AutoLoginFailedException;
import com.gourmetcaree.common.util.DigestUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.constants.MAreaConstants.AreaCdEnum;
import com.gourmetcaree.db.common.entity.TAutoLogin;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.enums.MTypeEnum.UserKbnEnum;

/**
 * 自動ログインのサービスクラスです。
 * @version 1.0
 */
public class AutoLoginService extends AbstractGroumetCareeBasicService<TAutoLogin> {

	/**
	 * 自動ログイン情報を論理削除します。
	 * @param terminalInfo
	 * @param identifiedCd
	 * @param userKbnEnum
	 * @param saveDays
	 */
	public void logicalDeleteIfExist(String terminalInfo, String identifiedCd, UserKbnEnum userKbnEnum, AreaCdEnum areaCdEnum, int saveDays) {

		SimpleWhere where = createIdentifiyWhere(terminalInfo, identifiedCd, userKbnEnum, areaCdEnum, saveDays);

		List<TAutoLogin> retList = jdbcManager
									.from(TAutoLogin.class)
									.where(where)
									.getResultList();

		//複数件存在する場合もすべて論理削除
		if (retList != null && retList.size() != 0) {
			logicalDeleteBatch(retList);
		}
	}

	/**
	 * 自動ログイン情報をログインIDをもとに論理削除します。
	 * @param loginId
	 * @param userKbnEnum
	 * @param saveDays
	 */
	public void logicalDeleteByLoginId(String loginId, UserKbnEnum userKbnEnum, AreaCdEnum areaCdEnum, int saveDays) {

		Date targetDate = DateUtils.addDays(new Date(), (-1) * saveDays);

		SimpleWhere where = new SimpleWhere()
									.eq(toCamelCase(TAutoLogin.AREA_CD), areaCdEnum.getValue())
									.eq(toCamelCase(TAutoLogin.LOGIN_ID), loginId)
									.eq(toCamelCase(TAutoLogin.USER_KBN), userKbnEnum.getValue())
									.eq(toCamelCase(TAutoLogin.DELETE_FLG), DeleteFlgKbn.NOT_DELETED)
									.ge(toCamelCase(TAutoLogin.AUTO_LOGIN_UPDATE_DATETIME), targetDate)
									;

		List<TAutoLogin> retList = jdbcManager
									.from(TAutoLogin.class)
									.where(where)
									.getResultList();

		//複数件存在する場合もすべて論理削除
		if (retList != null && retList.size() != 0) {
			logicalDeleteBatch(retList);
		}
	}

	/**
	 * 自動ログインテーブルを端末情報、識別ID、ユーザー区分で特定するWhere句を作成します。
	 * @param terminalInfo
	 * @param identifiedCd
	 * @param userKbnEnum
	 * @param saveDays
	 * @return
	 */
	private SimpleWhere createIdentifiyWhere(String terminalInfo, String identifiedCd, UserKbnEnum userKbnEnum, AreaCdEnum areaCdEnum, int saveDays) {

		Date targetDate = DateUtils.addDays(new Date(), (-1) * saveDays);

		SimpleWhere where = new SimpleWhere()
								.eq(toCamelCase(TAutoLogin.AREA_CD), areaCdEnum.getValue())
								.eq(toCamelCase(TAutoLogin.TERMINAL_INFO), terminalInfo)
								.eq(toCamelCase(TAutoLogin.IDENTIFIED_CD), DigestUtil.createDigest(identifiedCd))
								.eq(toCamelCase(TAutoLogin.USER_KBN), userKbnEnum.getValue())
								.eq(toCamelCase(TAutoLogin.DELETE_FLG), DeleteFlgKbn.NOT_DELETED)
								.ge(toCamelCase(TAutoLogin.AUTO_LOGIN_UPDATE_DATETIME), targetDate)
								.eq(toCamelCase(TAutoLogin.VALID_FLG), MTypeConstants.ValidFlg.VALID)
								;
		return where;
	}

	/**
	 * 自動ログインテーブルにデータが存在するかをチェックします。
	 * @param terminalInfo
	 * @param identifiedCd
	 * @param userKbnEnum
	 * @param saveDays
	 * @return
	 * @throws AutoLoginFailedException
	 */
	public boolean isAutoLoginExist(String terminalInfo, String identifiedCd, UserKbnEnum userKbnEnum, AreaCdEnum areaCdEnum, int saveDays)
	throws AutoLoginFailedException {

		SimpleWhere where = createIdentifiyWhere(terminalInfo, identifiedCd, userKbnEnum, areaCdEnum, saveDays);
		long count = countRecords(where);

		if (count < 1) {
			return false;
		} else if (count > 1) {
			//複数件取得できる場合はデータの不整合が発生しているためエラー。
			throw new AutoLoginFailedException("[isAutoLoginExist()]自動ログイン可能期間に複数のレコードが登録されています。terminalInfo:" + terminalInfo + ", identifiedCd:" + identifiedCd);
		}

		return true;
	}

	/**
	 * 自動ログインテーブルのエンティティを取得します。
	 * @param terminalInfo
	 * @param identifiedCd
	 * @param userKbnEnum
	 * @param saveDays
	 * @return
	 * @throws AutoLoginFailedException
	 */
	public TAutoLogin getAutoLoginEntity(String terminalInfo, String identifiedCd, UserKbnEnum userKbnEnum, AreaCdEnum areaCdEnum, int saveDays)
	throws AutoLoginFailedException {

		SimpleWhere where = createIdentifiyWhere(terminalInfo, identifiedCd, userKbnEnum, areaCdEnum, saveDays);

		List<TAutoLogin> retList = jdbcManager
										.from(TAutoLogin.class)
										.where(where)
										.getResultList();

		if (retList == null || retList.size() == 0) {
			throw new AutoLoginFailedException("[getAutoLoginEntity()]自動ログイン可能期間にレコードが存在しませんでした。terminalInfo:" + terminalInfo + ", identifiedCd:" + identifiedCd);
		}

		return retList.get(0);
	}

}

