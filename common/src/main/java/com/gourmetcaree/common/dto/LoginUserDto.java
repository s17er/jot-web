package com.gourmetcaree.common.dto;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * ユーザ情報・店舗情報ビュー用Dto
 * @author Takahiro Ando
 * @version 1.0
 *
 */
public class LoginUserDto implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -4975837232196076018L;

	/** ユーザID */
	public String userId;

	/** パスワード */
	public String password;

	/** 名前 */
	public String name;

	/** ユーザ区分 */
	public int userKbn;

	/** 権限レベル */
	public String authLevel;

	/** 店舗ID */
	public int shopId;

	/** ログイン可否フラグ */
	public int loginKahiFlg;

	/** 有効開始日付 */
	public Date yukoStartDate;

	/** 有効終了日付 */
	public Date yukoEndDate;

	/** ユーザ最終更新日時 */
	public Timestamp userLastUpdate;

	/** 店舗名 */
	public String shopName;

	/** 営業担当者ID */
	public String salesId;

	/** 編集担当者ID */
	public String editorId;

	/** 請求書番号 */
	public String invoiceNo;

	/** 管理用電話番号 */
	public String adminTelephoneNo;

	/** 管理用メールアドレス */
	public String adminMailAddress;

	/** 店舗最終更新日時 */
	public Timestamp shopLastUpdate;

	/** 事前期間フラグ */
	public boolean beforeTermFlg = false;

	/** 契約状態(事前期間の場合は未来の契約で上位の契約) */
	public int contractKbn;

	/** 実際の契約 */
	public int nowContractKbn;

}
