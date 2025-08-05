package com.gourmetcaree.db.common.service.member;

import static jp.co.whizz_tech.commons.WztStringUtil.*;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.seasar.extension.jdbc.AutoSelect;
import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.util.StringUtil;

import com.google.common.collect.Lists;
import com.gourmetcaree.common.util.DigestUtil;
import com.gourmetcaree.db.common.constants.DbFlgValue;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.member.TTempMember;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.AbstractGroumetCareeBasicService;

import jp.co.whizz_tech.commons.WztStringUtil;

/**
 * 仮会員サービス
 * @author nakamori
 *
 */
public class TempMemberService extends AbstractGroumetCareeBasicService<TTempMember>{


	/** ランダムコードを生成するときに、UNIXタイムの後ろにつけるランダム文字列の桁数 */
	private static final int RANDOM_ACCESS_CODE_LENGTH = 3;


	/**
	 * IDを複数指定してデータを取得
	 */
	public List<TTempMember> findByIds(String orderBy, Integer... ids) {
		if (ArrayUtils.isEmpty(ids)) {
			return Lists.newArrayList();
		}

		SimpleWhere where = new SimpleWhere();
		where.in(WztStringUtil.toCamelCase(TTempMember.ID), (Object[]) ids)
			.eq(WztStringUtil.toCamelCase(TTempMember.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		AutoSelect<TTempMember> select = jdbcManager.from(entityClass)
										.where(where);

		if (StringUtil.isNotBlank(orderBy)) {
			select.orderBy(orderBy);
		}

		return select.getResultList();

	}


	/**
	 * 本登録がされておらず、削除もされていない仮会員をIDをキーに取得
	 */
	public TTempMember findNotRegisteredAndNotDeletedById(Integer id) {
		if (id == null) {
			throw new SNoResultException("id is null");
		}
		SimpleWhere where = new SimpleWhere();
		where.eq(WztStringUtil.toCamelCase(TTempMember.ID), id)
			.eq(WztStringUtil.toCamelCase(TTempMember.MEMBER_REGISTERED_FLG), DbFlgValue.FALSE)
			.eq(WztStringUtil.toCamelCase(TTempMember.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		return jdbcManager.from(entityClass)
							.where(where)
							.disallowNoResult()
							.getSingleResult();
	}

	/**
	 * IDとアクセスコードをキーに仮会員を検索
	 */
	public TTempMember findByIdAndAccessCd(Integer id, String accessCd) {
		if (id == null || accessCd == null) {
			return null;
		}

		Where where = new SimpleWhere()
							.eq(TTempMember.ID, id)
							.eq(WztStringUtil.toCamelCase(TTempMember.ACCESS_CD), accessCd);


		return jdbcManager.from(entityClass)
							.where(where)
							.getSingleResult();
	}

	/**
	 * 会員マスタにデータを登録します。<br />
	 * パスワードの変換は登録時に行うため、変換前の値を渡します。
	 * @param entity
	 */
	public void saveMemberData(TTempMember entity) {

		// 登録の初期値をセットする
		setInitData(entity);

		final Integer id = findIdByLoginId(entity.loginId);

		if (id == null) {
			// 登録
			final int result = insert(entity);
			serviceLog.info(String.format("仮会員をインサートしました。 ID:[%d] result:[%d] エンティティ:[%s]",
							entity.id, result, entity));
			return;
		}

		entity.id = id;
		final int result = updateIncludesVersion(entity);
		serviceLog.info(String.format("仮会員をUPDATEしました。 ID:[%d] result:[%d] エンティティ:[%s]",
				entity.id, result, entity));


	}


	/**
	 * 本会員登録済みに変更
	 */
	public void updateMemberRegisteredTrue(Integer id) {
		TTempMember entity = new TTempMember();
		entity.id = id;
		entity.memberRegisteredFlg = DbFlgValue.TRUE;
		updateIncludesVersion(entity);
	}


	/**
	 * 自分のID以外に同じログインIDが存在するかどうか
	 * @param id ID
	 * @param loginId ログインID
	 * @return 自分以外のIDで同じログインIDが存在しているとtrue
	 */
	public boolean existsLoginIdAndNotEqId(Integer id, String loginId) {
		SimpleWhere where = new SimpleWhere();
		where.eq(WztStringUtil.toCamelCase(TTempMember.LOGIN_ID), loginId);
		where.ne(TTempMember.ID, id);
		where.eq(WztStringUtil.toCamelCase(TTempMember.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		long count = jdbcManager.from(entityClass)
								.where(where)
								.getCount();

		return count > 0L;
	}

	/**
	 * 退会会員の仮登録会員を取得する
	 * 運営側から削除されているケースを考慮し、listで取得
	 * @param loginId
	 * @return 仮会員リスト
	 * @throws WNoResultException
	 */
	public List<TTempMember> getWithdrawalMember(String loginId) throws WNoResultException {

		List<TTempMember> list = jdbcManager.from(entityClass)
				.where(new SimpleWhere().eq(toCamelCase(TTempMember.LOGIN_ID), loginId))
				.getResultList();

		if (CollectionUtils.isEmpty(list)) {
			throw new WNoResultException();
		}
		return list;
	}

	/**
	 * 会員マスタを登録する初期データをセットします。
	 * @param entity 会員エンティティ
	 */
	private void setInitData(TTempMember entity) {

		// パスワードを変換
		if (StringUtil.isNotBlank(entity.password)) {
			entity.password = DigestUtil.createDigest(entity.password);
		}

		entity.registrationDatetime = new Date();
		generateAccessCd(entity);

		// メルマガ配信停止フラグを「配信」にする
		entity.pcMailStopFlg = MTypeConstants.PcMailStopFlg.DELIVERY;
		entity.mobileMailStopFlg = MTypeConstants.MobileMailStopFlg.DELIVERY;
		entity.memberRegisteredFlg = DbFlgValue.FALSE;

		// メール配送状況を1からやり直すためにリセットする。
		entity.remindMailStatus = null;
		entity.nextRemindMailDatetime = null;
		entity.incrementRemindMailStatusAndDatetime();

	}

	/**
	 * アクセスコードの生成
	 * @param entity
	 */
	private void generateAccessCd(TTempMember entity) {

		TTempMember dbEntity = findByLoginId(entity.loginId);
		// 仮登録の複数登録時はアクセスコードが変わらないようにする
		if (dbEntity != null) {
			entity.accessCd = dbEntity.accessCd;
			return;
		}
		// ミリ秒の36進数 + ランダム文字列3桁
		entity.accessCd = Long.toString(System.currentTimeMillis(), Character.MAX_RADIX)
								.concat(RandomStringUtils.randomAlphanumeric(RANDOM_ACCESS_CODE_LENGTH));
	}

	/**
	 * ログインIDが存在するかどうか
	 * @param loginId ログインID
	 * @return 存在する場合true
	 */
	private TTempMember findByLoginId(String loginId) {

		return jdbcManager.from(entityClass)
								.where(createLoginIdWhere(loginId))
								.limit(1)
								.getSingleResult();

	}

	/**
	 * ログインIDが存在するかどうか
	 * @param loginId ログインID
	 * @return 存在する場合true
	 */
	private Integer findIdByLoginId(String loginId) {

		TTempMember entity = jdbcManager.from(entityClass)
								.where(createLoginIdWhere(loginId))
								.getSingleResult();

		if (entity == null) {
			return null;
		}
		return entity.id;
	}


	/**
	 * ログインIDで検索をするWhereを作成
	 */
	private static Where createLoginIdWhere(String loginId) {
		SimpleWhere where = new SimpleWhere();
		where.eq(WztStringUtil.toCamelCase(TTempMember.LOGIN_ID), loginId);
		where.eq(WztStringUtil.toCamelCase(TTempMember.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);
		return where;
	}

	/**
	 * 仮会員IDを条件に、データを物理削除します。
	 * @param tempMemberId 仮会員ID
	 */
	public void deleteByTempMemberId(int tempMemberId) {
		TTempMember entity = new TTempMember();
		entity.id = tempMemberId;
		deleteIgnoreVersion(entity);
		serviceLog.info(String.format("テーブル[%s]のデータを削除しました。仮会員ID:[%d] 結果:[1]", getTableName(), tempMemberId));
	}
}
