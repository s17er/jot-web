package com.gourmetcaree.db.common.service;

import javax.persistence.NoResultException;

import org.seasar.extension.jdbc.AutoSelect;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.entity.MAdvancedRegistration;
import com.gourmetcaree.db.common.entity.MAdvancedRegistration.TestFlgValue;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

import jp.co.whizz_tech.commons.WztStringUtil;

/**
 * 事前登録マスタサービス
 * @author Takehiro Nakamori
 *
 */
public class AdvancedRegistrationService extends AbstractGroumetCareeBasicService<MAdvancedRegistration> {

	/**
	 * 指定の事前登録が期間内かどうかを返す
	 * @param id 事前登録マスタID
	 * @return 指定されたIDの登録が期間内であればtrue、そうでなければfalse
	 * @throws WNoResultException データが存在しない場合はエラーを返す
	 */
	public boolean isPeriod(int id) throws WNoResultException {

		MAdvancedRegistration entity;

		try {
			entity = findById(id);
		} catch (NoResultException e) {
			throw new WNoResultException();
		}

		// 現在の期間内かどうかを返す
		return DateUtils.isPeriodJustDateTime(entity.termStartDatetime, entity.termEndDatetime);
	}
	
	/**
     * 指定の事前登録が期間内かどうかを返す
     * @param id 事前登録マスタID
     * @return 指定されたIDの登録が期間内であればtrue、そうでなければfalse
     * @throws WNoResultException データが存在しない場合はエラーを返す
     */
    public boolean isPeriod(MAdvancedRegistration entity) {
        // 現在の期間内かどうかを返す
        return DateUtils.isPeriodJustDateTime(entity.termStartDatetime, entity.termEndDatetime);
    }


	/**
	 * 一覧セレクトを取得
	 * @return 一覧セレクト
	 */
	public AutoSelect<MAdvancedRegistration> selectAll() {
		SimpleWhere where = new SimpleWhere();
		where.eq(WztStringUtil.toCamelCase(MAdvancedRegistration.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		return jdbcManager.from(MAdvancedRegistration.class)
						.where(where)
						.orderBy(String.format("%s, %s", SqlUtils.asc(MAdvancedRegistration.TEST_FLG), SqlUtils.asc(MAdvancedRegistration.DISP_ORDER)));
	}


	@Override
	public int insert(MAdvancedRegistration entity) {
		synchronized (this) {
			SimpleWhere where = new SimpleWhere();
			where.eq(WztStringUtil.toCamelCase(MAdvancedRegistration.TEST_FLG), TestFlgValue.NORMAL);
			where.eq(WztStringUtil.toCamelCase(MAdvancedRegistration.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);
			MAdvancedRegistration maxOrder = jdbcManager.from(MAdvancedRegistration.class)
									.where(where)
									.orderBy(SqlUtils.desc(MAdvancedRegistration.DISP_ORDER))
									.limit(1)
									.getSingleResult();

			if (maxOrder == null) {
				entity.dispOrder = 1;
			} else {
				entity.dispOrder = maxOrder.dispOrder.intValue() + 1;
			}
		}
		return super.insert(entity);
	}

}
