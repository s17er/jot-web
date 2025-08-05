package com.gourmetcaree.db.common.entity.accessor;

/**
 * 仮会員のサブデータエンティティに対してアクセスをするインターフェイス
 * @author nakamori
 *
 */
public interface TempMemberSubDataAccessor {

	Integer getId();

	void setTempMemberId(Integer tempMemberId);


	Integer getTempMemberId();
}
