package com.gourmetcaree.db.common.service;

import static com.gourmetcaree.common.util.SqlUtils.asc;
import static jp.co.whizz_tech.commons.WztStringUtil.toCamelCase;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.extension.jdbc.SqlBatchUpdate;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.db.common.entity.AbstractCommonEntity;
import com.gourmetcaree.db.common.entity.MMemberAttribute;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

import jp.co.whizz_tech.commons.WztStringUtil;

/**
 * 会員属性マスタのサービスクラスです。
 * @version 1.0
 */
public class MemberAttributeService extends AbstractGroumetCareeBasicService<MMemberAttribute> {


	/**
	 * 会員IDをキーに会員属性データを物理削除
	 * @param memberId 会員ID
	 */
	public void deleteMemberAttributeByMemberId(int memberId) {

		SqlBatchUpdate batchUpdate = jdbcManager.updateBatchBySql(
			    "DELETE FROM m_member_attribute WHERE member_id = ? "
				, Integer.class);

		    batchUpdate.params(memberId);

		    batchUpdate.execute();
	}

	/**
	 * 会員IDと属性コードをキーに会員属性データを物理削除
	 * @param memberId 会員ID
	 * @param attributeCd 属性コード
	 */
	public void deleteMemberAttibuteByMemberIdAndAttributeCd(int memberId, String attributeCd) {
		SqlBatchUpdate batchUpdate = jdbcManager.updateBatchBySql(
				"DELETE FROM m_member_attribute WHERE member_id = ? AND attribute_cd = ?", Integer.class, String.class);

		batchUpdate.params(memberId, attributeCd);
		batchUpdate.execute();
	}

	/**
	 * 属性コードを指定して属性値の配列を取得します。
	 * @param memberId
	 * @param attributeCd
	 * @return
	 */
	public int[] getWebAttributeValueArray(int memberId, String attributeCd) {

		try {
			List<Integer> retList = new ArrayList<Integer>();

			SimpleWhere where = getWhereByAttributeCd(memberId, attributeCd);
			List<MMemberAttribute> list = findByCondition(where, asc(toCamelCase(MMemberAttribute.ATTRIBUTE_VALUE)));

			for (MMemberAttribute entity : list) {
				retList.add(entity.attributeValue);
			}

			return ArrayUtils.toPrimitive(retList.toArray(new Integer[0]));

		} catch (WNoResultException e) {
			//未取得の場合は空を返す
			return new int[0];
		}
	}

	/**
	 * 属性コードを指定したWebデータ属性テーブル用の検索条件です。
	 * @param memberId
	 * @param attributeCd
	 * @return
	 */
	private SimpleWhere getWhereByAttributeCd(int memberId, String attributeCd) {
		SimpleWhere where = new SimpleWhere()
					.eq(toCamelCase(MMemberAttribute.MEMBER_ID), memberId)
					.eq(toCamelCase(MMemberAttribute.ATTRIBUTE_CD), attributeCd)
					.eq(toCamelCase(AbstractCommonEntity.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);
		return where;
	}


	/**
	 * 会員属性のStringリストを取得します。
	 * @param memberId 会員ID
	 * @param attributeCd 属性コード
	 * @return 会員属性のStringリスト
	 */
	public List<String> getMemberAttributeValueStringList(int memberId, String attributeCd) {
		List<String> valueList = jdbcManager.from(MMemberAttribute.class)
				.where(getWhereByAttributeCd(memberId, attributeCd))
				.orderBy(asc(toCamelCase(MMemberAttribute.ATTRIBUTE_VALUE)))
				.iterate(new IterationCallback<MMemberAttribute, List<String>>() {
					List<String> list = new ArrayList<String>();
					@Override
					public List<String> iterate(MMemberAttribute entity, IterationContext context) {
						if (entity != null) {
							list.add(String.valueOf(entity.attributeValue));
						}
						return list;
					}
				});

		if (valueList == null) {
			return new ArrayList<String>();
		}

		return valueList;
	}

	/**
	 * 会員属性値を取得します
	 * @param memberId 会員ID
	 * @param attributeCd 属性コード
	 * @return 会員属性値
	 */
	public List<Integer> getMemberAttributeValueList(int memberId, String attributeCd) {
		List<Integer> valueList = jdbcManager.from(MMemberAttribute.class)
				.where(getWhereByAttributeCd(memberId, attributeCd))
				.orderBy(asc(toCamelCase(MMemberAttribute.ATTRIBUTE_VALUE)))
				.iterate(new IterationCallback<MMemberAttribute, List<Integer>>() {
					List<Integer> list = new ArrayList<Integer>();
					@Override
					public List<Integer> iterate(MMemberAttribute entity, IterationContext context) {
						if (entity != null) {
							list.add(Integer.valueOf(entity.attributeValue));
						}
						return list;
					}
				});

		if (valueList == null) {
			return new ArrayList<Integer>();
		}

		return valueList;
	}


	/**
	 * 会員属性リストを取得します。
	 * @param memberId 会員ID
	 * @return 会員属性リスト
	 */
	public List<MMemberAttribute> getMemberAttributeList(int memberId) {
		return jdbcManager.from(MMemberAttribute.class)
							.where(new SimpleWhere()
							.eq(toCamelCase(MMemberAttribute.MEMBER_ID), memberId))
							.getResultList();
	}
	
   /**
     * 会員属性リストを属性コードを指定して取得します。
     * @param memberId 会員ID
     * @return 会員属性リスト
     */
    public List<MMemberAttribute> getMemberAttributeList(int memberId, String... attributeCds) {
        SimpleWhere where = new SimpleWhere();
        where.eq(toCamelCase(MMemberAttribute.MEMBER_ID), memberId);
        if (!ArrayUtils.isEmpty(attributeCds)) {
            where.in(WztStringUtil.toCamelCase(MMemberAttribute.ATTRIBUTE_CD), (Object[]) attributeCds);
        }
                                
        return jdbcManager.from(MMemberAttribute.class)
                            .where(where)
                            .getResultList();
    }


	/**
	 * ソートされた属性値リストを取得します。
	 * @param memberId 会員ID
	 * @param attributeCd 属性コード
	 * @return ソートされた属性値リスト
	 */
	public List<Integer> getSortedAttributeValueList(int memberId, String attributeCd) {
		StringBuffer sql = new StringBuffer(0);
		sql.append(" SELECT ");
		sql.append("     ATT.attribute_value ");
		sql.append(" FROM ");
		sql.append("     m_member_attribute ATT INNER JOIN m_type TY ");
		sql.append("         ON ATT.member_id = ? ");
		sql.append("         AND ATT.attribute_cd = ? ");
		sql.append("         AND ATT.attribute_cd = TY.type_cd ");
		sql.append("         AND ATT.attribute_value = TY.type_value ");
		sql.append(" WHERE ");
		sql.append("     ATT.delete_flg = ? ");
		sql.append("     AND TY.delete_flg = ? ");
		sql.append(" ORDER BY ");
		sql.append("     TY.display_order ");


		return jdbcManager.selectBySql(Integer.class, sql.toString(), memberId, attributeCd, DeleteFlgKbn.NOT_DELETED, DeleteFlgKbn.NOT_DELETED)
							.getResultList();
	}

}