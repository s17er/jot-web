package com.gourmetcaree.db.common.service;

import static org.seasar.framework.util.StringUtil.camelize;

import java.util.Calendar;
import java.util.Date;

import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.beans.util.Beans;
import org.seasar.framework.util.UUID;

import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TTemporaryRegistration;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;

import jp.co.whizz_tech.commons.WztStringUtil;

/**
 * 仮登録のサービスクラスです。
 * @version 1.0
 */
public class TemporaryRegistrationService extends AbstractGroumetCareeBasicService<TTemporaryRegistration> {

	public boolean isAccessCodeExist(String accessCode) {
		TTemporaryRegistration entity = jdbcManager.from(TTemporaryRegistration.class)
			.where(new SimpleWhere()
			.eq("accessCd", accessCode))
			.getSingleResult();

		return entity != null;
	}
	
	
	
	/**
	 * IDとアクセスコードをキーに仮登録をセレクト
	 * @param id ID
	 * @param accessCd アクセスコード
	 * @return 仮登録データ。存在しない場合はnull
	 */
	public TTemporaryRegistration findByIdAndAccessCd(Integer id, String accessCd) {
	    if (id == null) {
	        return null;
	    }
	    
	    return jdbcManager.from(entityClass)
	            .where(new SimpleWhere()
	                    .eq(TTemporaryRegistration.ID, id)
	                    .eq(WztStringUtil.toCamelCase(TTemporaryRegistration.ACCESS_CD), accessCd))
	            .getSingleResult();
	}

	/**
	 * 指定したアクセスコードとIDでデータが存在するかチェックします。
	 * @param entity 仮登録エンティティ
	 * @return データが存在すればtrue、存在しなければfalse
	 */
	public boolean isAccessRecordExists(TTemporaryRegistration entity) {

		// 引数のチェック
		checkEmptyEntity(entity);

		return isRecordExists(createConfWhere(entity));
	}

	/**
	 * 指定したアクセスコードとIDでデータが存在するかチェックします。
	 * @param entity 仮登録エンティティ
	 * @return データが存在すればtrue、存在しなければfalse
	 */
	public boolean isAccessRecordExistsForCustomer(TTemporaryRegistration entity) {

		// 引数のチェック
		checkEmptyEntity(entity);

		SimpleWhere where = new SimpleWhere();

		// ID
		where.eq(camelize(TTemporaryRegistration.ID), entity.id);
		// アクセスコード
		where.eq(camelize(TTemporaryRegistration.ACCESS_CD), entity.accessCd);
		// アクセスフラグ = 0(未アクセス)
		where.eq(camelize(TTemporaryRegistration.ACCESS_FLG), MTypeConstants.AccessFlg.NEVER);
		// 削除フラグ
		where.eq(camelize(TTemporaryRegistration.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		return isRecordExists(where);
	}


	/**
	 * データが存在するかチェックします。
	 * @param where 検索条件
	 * @return データが存在すればtrue、存在しなければfalse
	 */
	private boolean isRecordExists(Where where) {

		// 仮登録を検索
		int count = (int) countRecords(where);

		// データが存在すればtrueを返却
		if (count > 0) {
			return true;
		}

		return false;
	}

	/**
	 * 仮登録存在チェックの検索条件を返します。
	 * @param entity 仮登録エンティティ
	 * @return 仮登録存在チェックの検索条件
	 */
	private Where createConfWhere(TTemporaryRegistration entity) {

		SimpleWhere where = new SimpleWhere();

		// ID
		where.eq(camelize(TTemporaryRegistration.ID), entity.id);
		// エリアコード
		where.eq(camelize(TTemporaryRegistration.AREA_CD), entity.areaCd);
		// アクセスコード
		where.eq(camelize(TTemporaryRegistration.ACCESS_CD), entity.accessCd);
		// アクセスフラグ = 0(未アクセス)
		where.eq(camelize(TTemporaryRegistration.ACCESS_FLG), MTypeConstants.AccessFlg.NEVER);
		// 削除フラグ
		where.eq(camelize(TTemporaryRegistration.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		return where;
	}

	/**
	 * アクセスが可能な日かどうかチェックします。<br />
	 * 仮登録エンティティに値をセットします。
	 * @param entity 仮登録エンティティ
	 * @param limitDay 有効日
	 * @return アクセス可能な場合はtrue、不可能な場合はfalse
	 */
	public boolean canAccessDate(TTemporaryRegistration entity, int limitDay) {

		// 引数チェック
		checkEmptyEntity(entity);

		try {
			// データを検索
			TTemporaryRegistration selectEntity = findById(entity.id);
			// 参照渡しのためコピー
			Beans.copy(selectEntity, entity).execute();

			// 認証可能かどうかチェックする
			return isLimitDate(selectEntity.publishDatetime, limitDay);

		// データが存在しない場合は認証不可
		} catch (SNoResultException e) {
			return false;
		}
	}

	/**
	 * 日付が期限内かチェックします。
	 * @param date チェックする日付
	 * @param limitDay 有効日
	 * @return 期限内の場合はtrue、期限を過ぎている場合はfalse
	 */
	private boolean isLimitDate(Date date, int limitDay) {

		// 認証有効日(発行日+有効期限)
		Calendar limitCal = Calendar.getInstance();
		limitCal.setTime(date);
		limitCal.add(Calendar.DATE, limitDay);

		// 有効期限を過ぎている場合は認証不可
		if (Calendar.getInstance().compareTo(limitCal) > 0) {
			return false;
		}

		// 期限内であれば認証可
		return true;
	}

	/**
	 * 仮登録をアクセス済みに変更する
	 * @param id 仮登録ID
	 * @param version 仮登録バージョン
	 */
	public void updateAlreadyAccessed(Integer id, Long version) {
		if (id == null || version == null) {
			throw new NullPointerException(String.format("id もしくは version がnullです。 id:%s version:%s", id, version));
		}

		TTemporaryRegistration entity = new TTemporaryRegistration();
		// IDをセット
		entity.id = id;
		// バージョンをセット
		entity.version = version;

		updateAlreadyeAccess(entity);
	}

	/**
	 * 仮登録をアクセス済みに変更する
	 * @param entity 仮登録エンティティ
	 */
	public void updateAlreadyeAccess(TTemporaryRegistration entity) {

		// アクセスフラグをアクセス済み
		entity.accessFlg = MTypeConstants.AccessFlg.ALREADY;
		// アクセス日時
		entity.accessDatetime = new Date();

		// データを更新
		update(entity);
	}

	/**
	 * 仮登録のデータを初期値をセットして登録する
	 * @param entity 仮登録エンティティ
	 */
	public void insertTTemporaryRegistration(TTemporaryRegistration entity) {

		// 初期値をセット
		setDefaultInsertData(entity);

		// データの登録
		insert(entity);

	}

	/*:
	 * 仮登録に登録するデータの初期値をエンティティセットする
	 */
	private void setDefaultInsertData(TTemporaryRegistration entity) {

		// 発行日時
		entity.publishDatetime = new Date();
		// アクセスコード
		entity.accessCd = UUID.create();
		// アクセスフラグ（未アクセス）
		entity.accessFlg = MTypeConstants.AccessFlg.NEVER;

	}

}
