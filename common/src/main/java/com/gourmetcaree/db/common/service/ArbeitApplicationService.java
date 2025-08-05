package com.gourmetcaree.db.common.service;

import java.util.List;

import jp.co.whizz_tech.commons.WztStringUtil;

import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.TArbeitApplication;

/**
 * グルメdeバイト用応募サービス
 * @author Takehiro Nakamori
 *
 */
public class ArbeitApplicationService extends AbstractGroumetCareeBasicService<TArbeitApplication> {

	/**
	 * 応募者名を結合して作成します。
	 * @param applicationIdList 応募IDリスト
	 * @return
	 */
	public String createJoinedApplicantName(List<Integer> applicationIdList) {
		return createJoinedApplicantName(applicationIdList, ",");
	}

	/**
	 * 応募者名を結合して作成します。
	 * @param applicationIdList 応募IDリスト
	 * @param delimiter デリミタ
	 * @return
	 */
	public String createJoinedApplicantName(List<Integer> applicationIdList, final String delimiter) {
		final StringBuffer sb = new StringBuffer(0);

		// 削除したものも含める
		SimpleWhere where = new SimpleWhere();
		where.in(WztStringUtil.toCamelCase(TArbeitApplication.ID), applicationIdList);

		jdbcManager.from(TArbeitApplication.class)
				.where(where)
				.orderBy(TArbeitApplication.ID)
				.iterate(new IterationCallback<TArbeitApplication, Void>() {

					@Override
					public Void iterate(TArbeitApplication entity, IterationContext context) {
						if (entity == null) {
							return null;
						}

						if (sb.length() > 0) {
							sb.append(delimiter);
						}

						sb.append(entity.name);
						return null;
					}
				});

		return sb.toString();
	}
}
