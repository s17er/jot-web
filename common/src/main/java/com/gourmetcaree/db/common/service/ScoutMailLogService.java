package com.gourmetcaree.db.common.service;

import java.util.List;

import jp.co.whizz_tech.commons.WztStringUtil;

import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.entity.TScoutMailLog;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;

/**
 * スカウトメールログサービス
 * @author Takehiro Nakamori
 *
 */
public class ScoutMailLogService extends AbstractGroumetCareeBasicService<TScoutMailLog>{

	/** ログ */
	private static final Logger log = Logger.getLogger(ScoutMailLogService.class);

	/**
	 * メモを変更します。
	 * @param id ID
	 * @param memo メモ
	 * @author Takehiro Nakamori
	 */
	public void changeMemo(int id, String memo) {

		try {
			TScoutMailLog entity = findById(id);
			entity.memo = memo;

			update(entity);
			log.info(String.format("スカウトメールのメモを更新しました。ID:[%d] メモ：[%s]", id, memo));
		} catch (SNoResultException e) {
			// 何もしない
		}
	}


	/**
	 * 選考フラグを変更します
	 * @param id ID
	 * @param selectionFlg 選考フラグ
	 * @author Takehiro Nakamori
	 */
	public void changeSelectionFlg(int id, Integer selectionFlg) {
		try {
			TScoutMailLog entiLog = findById(id);
			entiLog.selectionFlg = selectionFlg;
			updateWithNull(entiLog, WztStringUtil.toCamelCase(TScoutMailLog.SELECTION_FLG));
			log.info(String.format("スカウトメールの選考フラグを更新しました。ID:[%d] 選考フラグ：[%s]", id, String.valueOf(selectionFlg)));
		} catch (SNoResultException e) {
			// 何もしない。
		}
	}



	/**
	 * メールログのIDを会員IDに変換します。
	 * @param scoutMailLogIdList
	 * @return
	 */
	public String convertIdToMemberId(List<Integer> scoutMailLogIdList) {
		SimpleWhere where = new SimpleWhere();
		where.in(WztStringUtil.toCamelCase(TScoutMailLog.ID), scoutMailLogIdList);
		where.eq(WztStringUtil.toCamelCase(TScoutMailLog.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);
		final StringBuilder sb = new StringBuilder();

		jdbcManager.from(TScoutMailLog.class)
					.where(where)
					.orderBy(SqlUtils.desc(TScoutMailLog.SEND_DATETIME))
					.iterate(new IterationCallback<TScoutMailLog, Void>() {
						@Override
						public Void iterate(TScoutMailLog entity, IterationContext context) {
							if (entity == null) {
								return null;
							}

							if (sb.length() > 0) {
								sb.append(",");
							}

							sb.append(entity.memberId);

							return null;
						}
					});

		return sb.toString();
	}
}
