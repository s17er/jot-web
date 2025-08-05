package com.gourmetcaree.db.common.service;

import static com.gourmetcaree.common.util.SqlUtils.orEq;
import static com.gourmetcaree.common.util.SqlUtils.andEq;
import static com.gourmetcaree.common.util.SqlUtils.eq;

import java.util.ArrayList;
import java.util.List;

import org.seasar.framework.util.StringUtil;

import com.gourmetcaree.db.common.entity.MMischiefApplication;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;

/**
 * いたずら応募マスタのサービスクラスです。
 * @version 1.0
 */
public class MischiefApplicationService extends AbstractGroumetCareeBasicService<MMischiefApplication> {

	/**
	 * いたずら応募かどうかを返します。電話番号はハイフン抜きで検索する。
	 * @param pcMail PCメールアドレス
	 * @param mobileMail 携帯メールアドレス
	 * @param phoneNo 電話番号
	 * @return いたずらメールの場合はtrue、そうで無い場合はfalse
	 */
	public boolean isMischiefApplication(String pcMail, String mobileMail, String phoneNo) {

		// 値がセットされていなければ処理しない
		if (StringUtil.isBlank(pcMail) && StringUtil.isBlank(mobileMail) && StringUtil.isBlank(phoneNo)) {
			return false;
		}

		// 検索パラメータ
		List<Object> params = new ArrayList<Object>();

		// 検索条件の作成
		StringBuilder whereStr = createSql(pcMail, mobileMail, phoneNo, params);

		// 件数の検索
		long count = countRecords(whereStr.toString(), params.toArray());

		// 存在すればtrue
		return count > 0;
	}

	/**
	 * 検索条件を作成します。
	 * @param pcMail PCメールアドレス
	 * @param mobileMail 携帯メールアドレス
	 * @param phoneNo 電話番号
	 * @param params 検索値を保持するリスト
	 * @return 検索条件をセットしたStringBuilder
	 */
	public StringBuilder createSql(String pcMail, String mobileMail, String phoneNo, List<Object> params) {

		// 検索条件の設定
		StringBuilder whereStr = new StringBuilder();

		// 削除フラグ
		whereStr.append(eq(MMischiefApplication.DELETE_FLG));
		params.add(DeleteFlgKbn.NOT_DELETED);

		// 初めの検索条件かどうか
		boolean isFirstWhere = true;

		// PCメールが入力されていれば条件に追加
		if (!StringUtil.isBlank(pcMail)) {
			// 初めの検索条件の場合はAND検索
			if (isFirstWhere) {
				whereStr.append(andEq(MMischiefApplication.MAIL_ADDRESS));
				isFirstWhere = false;
			// 初めの検索条件でない場合はOR検索
			} else {
				whereStr.append(orEq(MMischiefApplication.MAIL_ADDRESS));
			}
			params.add(pcMail);
		}

		// 携帯メールが入力されていれば条件に追加
		if (!StringUtil.isBlank(mobileMail)) {
			// 初めの検索条件の場合はAND検索
			if (isFirstWhere) {
				whereStr.append(andEq(MMischiefApplication.MAIL_ADDRESS));
				isFirstWhere = false;
			// 初めの検索条件でない場合はOR検索
			} else {
				whereStr.append(orEq(MMischiefApplication.MAIL_ADDRESS));
			}
			params.add(mobileMail);
		}

		// 電話番号が入力されていれば条件に追加
		if (!StringUtil.isBlank(phoneNo)) {
			// 初めの検索条件の場合はAND検索
			if (isFirstWhere) {
				// 電話番号は桁を結合して検索
				whereStr.append(andEq(MMischiefApplication.PHONE_NO1 + " || " + MMischiefApplication.PHONE_NO2 + " || " + MMischiefApplication.PHONE_NO3));
				isFirstWhere = false;
			// 初めの検索条件でない場合はOR検索
			} else {
				// 電話番号は桁を結合して検索
				whereStr.append(orEq(MMischiefApplication.PHONE_NO1 + " || " + MMischiefApplication.PHONE_NO2 + " || " + MMischiefApplication.PHONE_NO3));
			}

			params.add(phoneNo);
		}

		return whereStr;
	}
}