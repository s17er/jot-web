package com.gourmetcaree.admin.service.accessor.tempMember;

/**
 * 仮会員を検索するためのデータにアクセスするインタフェース
 * @author nakamori
 *
 */
public interface SearchAccessor {
	/** ページ遷移用に選択されたページ数 */
	public String getPageNum();

	/** 最大表示件数 */
	public String getMaxRow();

	/** ID */
	public String getWhere_id();

	/** 氏名 */
	public String getWhere_name();

	/** 氏名フリガナ */
	public String getWhere_nameKana();

	/** エリア */
	public String getWhere_areaCd();

	/** 都道府県 */
	public String getWhere_prefecturesCd();

	/** 希望業種 */
	public String getWhere_industryCd();

	/** 雇用形態 */
	public String getWhere_empPtnKbn();

	/** 性別 */
	public String getWhere_sexKbn();

	/** 下限年齢 */
	public String getWhere_lowerAge();

	/** 上限年齢 */
	public String getWhere_upperAge();

	/** 登録日時(開始) */
	public String getWhere_fromRegistratedDate();

	/** 登録日時(終了) */
	public String getWhere_toRegistratedDate();

	/** メールアドレス */
	public String getWhere_mail();
	
	/** 会員本登録フラグ */
	public String getWhere_memberRegisteredFlg();
	
	
}
