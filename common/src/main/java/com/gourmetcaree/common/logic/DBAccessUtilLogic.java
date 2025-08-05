package com.gourmetcaree.common.logic;

import static org.seasar.framework.util.StringUtil.camelize;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.MType;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.service.TypeService;


/**
 * DBアクセスを伴うユーティリティクラス
 * @author Makoto Otani
 *
 */
public class DBAccessUtilLogic extends AbstractGourmetCareeLogic {

	/** 区分マスタサービス */
	@Resource
	protected  TypeService typeService;

	/**
	 * 携帯メールアドレスのドメインかどうかチェックします。
	 * 空であればfalseとする。
	 * @param mobileAddress 携帯メールアドレス
	 * @return 携帯メールアドレスであればtrue、そうでなければfalseを返します。
	 */
	public boolean checkMobileDomain(String mobileAddress) {

		if (StringUtils.isBlank(mobileAddress)) {
			return false;
		}

		// @マーク以下を保持
		String domain = StringUtils.substringAfter(mobileAddress.trim(), "@");

		// 検索条件の設定
		SimpleWhere where = new SimpleWhere()
								.eq(camelize(MType.TYPE_CD), MTypeConstants.MobileDomainKbn.TYPE_CD)	// 区分コード = ドメイン区分
								.eq(camelize(MType.TYPE_NAME), domain)									// 区分名 = チェックするドメイン
								.eq(camelize(MType.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);				// 削除フラグ

		// 0以上の場合は存在するドメイン
		return (int)typeService.countRecords(where) > 0;
	}

}
