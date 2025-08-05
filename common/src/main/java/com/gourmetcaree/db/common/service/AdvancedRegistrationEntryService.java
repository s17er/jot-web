package com.gourmetcaree.db.common.service;

import static jp.co.whizz_tech.commons.WztStringUtil.toCamelCase;

import java.util.List;

import org.apache.log4j.Logger;
import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.TAdvancedRegistrationEntry;
import com.gourmetcaree.db.common.entity.TAdvancedRegistrationEntryAttribute;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

import jp.co.whizz_tech.commons.WztStringUtil;

/**
 * 事前登録エントリサービス
 * @author Takehiro Nakamori
 *
 */
public class AdvancedRegistrationEntryService extends AbstractGroumetCareeBasicService<TAdvancedRegistrationEntry>{

	private static final Logger log = Logger.getLogger(AdvancedRegistrationEntryService.class);


	/**
	 * IDと事前登録IDから事前登録エントリを取得します。
	 * @param id ID
	 * @param advancedRegistrationId 事前登録ID
	 * @return 事前登録エントリ
	 * @throws WNoResultException 見つからなかった場合にスロー
	 * @author nakamori
	 */
	public TAdvancedRegistrationEntry findByIdAndAdvancedRegistrationId(int id, int advancedRegistrationId) throws WNoResultException {
		SimpleWhere where = new SimpleWhere();
		where.eq(WztStringUtil.toCamelCase(TAdvancedRegistrationEntry.ID), id);
		where.eq(WztStringUtil.toCamelCase(TAdvancedRegistrationEntry.ADVANCED_REGISTRATION_ID), advancedRegistrationId);
		where.eq(WztStringUtil.toCamelCase(TAdvancedRegistrationEntry.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		try {
			return jdbcManager.from(TAdvancedRegistrationEntry.class)
						.where(where)
						.disallowNoResult()
						.getSingleResult();
		} catch (SNoResultException e) {
			throw new WNoResultException(e);
		}

	}

	/**
	 * 事前登録IDと事前登録ユーザIDで事前登録エントリ属性を結合した事前登録エントリを取得します
	 * @param advancedRegistrationId 事前登録ID
	 * @param advancedRegistrationUserId 事前登録ユーザID
	 * @return 事前登録エントリエンティティ
	 * @throws WNoResultException データが取得出来ない場合のエラー
	 */
	public TAdvancedRegistrationEntry getTAdvancedRegistrationEntry(int advancedRegistrationId, int advancedRegistrationUserId) throws WNoResultException {

		try {
			// データの検索
			TAdvancedRegistrationEntry entity = jdbcManager.from(TAdvancedRegistrationEntry.class)
					.where(new SimpleWhere()
					// 事前登録ユーザID
					.eq(toCamelCase(TAdvancedRegistrationEntry.ADVANCED_REGISTRATION_USER_ID), advancedRegistrationUserId)
					// 事前登録ID
					.eq(toCamelCase(TAdvancedRegistrationEntry.ADVANCED_REGISTRATION_ID), advancedRegistrationId)
					// 事前登録エントリが未削除
					.eq(toCamelCase(TAdvancedRegistrationEntry.DELETE_FLG), DeleteFlgKbn.NOT_DELETED))
					// 属性のID順で並び変え
					.orderBy(TAdvancedRegistrationEntryAttribute.ID)
					.disallowNoResult()
					.getSingleResult();

			return entity;

		// データが取得出来ない場合はエラーを返す
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}
	
	/**
	 * ユーザが登録されているかどうか
	 * @param advancedRegistrationId 事前登録ID
	 * @param advancedRegistrationUserId 事前登録ユーザID
	 * @return ユーザが登録されていればtrue
	 */
	public boolean isRegistered(int advancedRegistrationId, int advancedRegistrationUserId) {
	    Where where = new SimpleWhere()
            // 事前登録ユーザID
            .eq(toCamelCase(TAdvancedRegistrationEntry.ADVANCED_REGISTRATION_USER_ID), advancedRegistrationUserId)
            // 事前登録ID
            .eq(toCamelCase(TAdvancedRegistrationEntry.ADVANCED_REGISTRATION_ID), advancedRegistrationId)
            // 事前登録エントリが未削除
            .eq(toCamelCase(TAdvancedRegistrationEntry.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);
	    
	    final long count = jdbcManager.from(entityClass)
	        .where(where)
	        .getCount();
	    
	    return count > 0L;
	}



	/**
	 * 事前登録IDとログインIDに紐付くデータが存在するかどうかを返します
	 * @param advancedRegistrationId 事前登録ID
	 * @param loginId ログインID
	 * @return データが存在する場合はtrue,ない場合はfalse
	 */
	public boolean isAdvancedRegistrationEntryExsist(int advancedRegistrationId, String loginId) {

		SimpleWhere where = new SimpleWhere();
		// 事前登録ID
		where.eq(toCamelCase(TAdvancedRegistrationEntry.ADVANCED_REGISTRATION_ID), advancedRegistrationId);
		// ログインID
		where.eq(toCamelCase(TAdvancedRegistrationEntry.LOGIN_ID), loginId);
		// 事前登録エントリが未削除
		where.eq(toCamelCase(TAdvancedRegistrationEntry.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		return countRecords(where) > 0;
	}


	/**
	 * 指定された事前登録ユーザID(2014/1現在は会員ID)のログインIDを更新します。
	 * @param advancedRegistrationUserId 事前登録ユーザID
	 * @param newLoginId 更新するログインID
	 * @throws WNoResultException 更新対象が無い場合はエラー
	 */
	public void updateLoginId(int advancedRegistrationUserId, String newLoginId) throws WNoResultException {

		// 更新対象データの取得
		SimpleWhere where = new SimpleWhere();
		// 事前登録ユーザID
		where.eq(toCamelCase(TAdvancedRegistrationEntry.ADVANCED_REGISTRATION_USER_ID), advancedRegistrationUserId);
		// 事前登録エントリが未削除
		where.eq(toCamelCase(TAdvancedRegistrationEntry.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		List<TAdvancedRegistrationEntry> advancedRegistrationEntryList = findByCondition(where);

		// ログインIDをセットして更新
		for (TAdvancedRegistrationEntry entity :advancedRegistrationEntryList) {
			entity.loginId = newLoginId;
			update(entity);
		}
	}

	/**
	 * 事前登録ユーザIDを条件に事前登録エントリを削除します
	 * @param advancedRegistrationUserId 事前登録エントリID
	 */
	public void deleteByAdvancedRegistrationUserId(int advancedRegistrationUserId) {

		try {
			// 事前登録エントリデータを取得する
			SimpleWhere where = new SimpleWhere();
			where.eq(toCamelCase(TAdvancedRegistrationEntry.ADVANCED_REGISTRATION_USER_ID), advancedRegistrationUserId);
			where.eq(toCamelCase(TAdvancedRegistrationEntry.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

			List<TAdvancedRegistrationEntry> entityList = findByCondition(where);

			// レコードを削除する
			for (TAdvancedRegistrationEntry entity : entityList) {
				logicalDeleteIncludesVersion(entity);
			}

		// 値が無い場合は処理しない
		} catch (WNoResultException e) {
			return;
		}
	}


	/**
	 * 会員が存在するかどうか
	 * @param advancedRegistrationUserId 事前登録ユーザID
	 * @param advancedRegistrationId 事前登録マスタID
	 * @return 存在すればtrue
	 */
	public boolean existsMember(int advancedRegistrationUserId, int advancedRegistrationId) {
		SimpleWhere where = new SimpleWhere();
		where.eq(WztStringUtil.toCamelCase(TAdvancedRegistrationEntry.ADVANCED_REGISTRATION_ID), advancedRegistrationId);
		where.eq(WztStringUtil.toCamelCase(TAdvancedRegistrationEntry.ID), advancedRegistrationUserId);
		where.eq(WztStringUtil.toCamelCase(TAdvancedRegistrationEntry.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		long count = countRecords(where);

		return count > 0l;
	}


	/**
	 * ログインIDをキーに会員が存在するかどうかを取得します。
	 * @param loginId ログインID
	 * @return 存在すればtrue
	 * @author Takehiro Nakamori
	 */
	public boolean existsMember(String loginId) {
		SimpleWhere where = new SimpleWhere();
		where.eq(WztStringUtil.toCamelCase(TAdvancedRegistrationEntry.LOGIN_ID), loginId);
		where.eq(WztStringUtil.toCamelCase(TAdvancedRegistrationEntry.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		long count = countRecords(where);

		return count > 0l;
	}


	/**
	 * 事前登録エントリユーザIDを削除します
	 * @param advancedRegistrationUserId 事前登録エントリユーザID
	 * @author Takehiro Nakamori
	 */
	public void removeAdvancedRegistrationUserId(int advancedRegistrationUserId) {
		SimpleWhere where = new SimpleWhere();
		where.eq(WztStringUtil.toCamelCase(TAdvancedRegistrationEntry.ADVANCED_REGISTRATION_USER_ID), advancedRegistrationUserId);
		where.eq(WztStringUtil.toCamelCase(TAdvancedRegistrationEntry.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		jdbcManager.from(TAdvancedRegistrationEntry.class)
			.where(where)
			.iterate(new IterationCallback<TAdvancedRegistrationEntry, Void>() {
				@Override
				public Void iterate(TAdvancedRegistrationEntry entity, IterationContext context) {
					if (entity == null) {
						return null;
					}

					entity.advancedRegistrationUserId = null;

					updateWithNull(entity, "advancedRegistrationUserId");
					log.info("事前登録エントリの会員紐付けをなくしました。ID:" + entity.id);
					return null;
				}
			});
	}
}
