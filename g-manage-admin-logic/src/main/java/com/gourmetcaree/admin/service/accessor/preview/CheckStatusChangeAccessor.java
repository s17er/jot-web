package com.gourmetcaree.admin.service.accessor.preview;

/**
 * WEBデータのチェックステータスの変更ができるか判定するためのアクセサ
 * @author nakamori
 *
 */
public interface CheckStatusChangeAccessor {

	/** WEBデータIDの取得 */
	String getId();

	/** チェックステータスの変更が可能かどうかをセット */
	void setChangeableCheckStatus(boolean isChangeableCheckStatus);


}
