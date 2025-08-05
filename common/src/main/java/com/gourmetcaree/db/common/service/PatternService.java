package com.gourmetcaree.db.common.service;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.entity.MSentence;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;

import jp.co.whizz_tech.commons.WztStringUtil;

/**
 * 定型文一覧サービス
 * @author Daiki Uchida
 *
 */
public class PatternService extends AbstractGroumetCareeBasicService<MSentence> {

	/** アプリケーションオブジェクト */
	@Resource
	protected ServletContext application;

	/**
	 * 一括削除を行います。
	 * 他の顧客のものを消さないように、顧客IDを含めた検索を行います。
	 * @param customerId
	 * @param targetIds
	 */
	public void lumpDelete(int customerId, String... targetIds) {
		try {
			List<MSentence> entityList = findByCondition(new SimpleWhere()
																.in("id", Arrays.asList(GourmetCareeUtil.toIntegerArray(targetIds)))
																.eq(WztStringUtil.toCamelCase(MSentence.CUSTOMER_ID), customerId)
																.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED));

			logicalDeleteBatch(entityList);
		} catch (Exception e) {
			// 見つからなければ何もしない。
		}
	}

}
