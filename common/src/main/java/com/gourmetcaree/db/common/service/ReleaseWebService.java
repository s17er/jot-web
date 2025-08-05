package com.gourmetcaree.db.common.service;

import static com.gourmetcaree.common.util.SqlUtils.*;
import static jp.co.whizz_tech.commons.WztStringUtil.*;
import static org.seasar.framework.util.StringUtil.*;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.seasar.extension.jdbc.AutoSelect;
import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.constants.MAreaConstants.AreaCdEnum;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.VRReleaseWeb;
import com.gourmetcaree.db.common.entity.VReleaseWeb;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.common.property.ReleaseWebSearchProperty;

/**
 * 公開可能なWEBデータ一覧ビューのサービスクラスです。
 * @author Takahiro Ando
 * @version 1.0
 */
public class ReleaseWebService extends AbstractGroumetCareeReferenceService<VReleaseWeb> {

	/**
	 * WebIdとエリアコードを元に公開可能なWebデータを取得します。
	 * データが存在しない場合はWNoResultExceptionをスローします。
	 * 1件取得できることが保証されているので1件目を取得。
	 * @param webId
	 * @param areaCd
	 * @return
	 * @throws WNoResultException
	 */
	public VReleaseWeb getEntity(int webId, int areaCd)
	throws WNoResultException {

		SimpleWhere where = getWhereById(webId, areaCd);
		return findByCondition(where).get(0);
	}

	/**
	 * WEBIdのリストを元に公開可能なWEBデータを取得します。
	 * データが存在しない場合はWNoResultExceptionをスローします。
	 * @param webIdList
	 * @return 公開WEBデータのリスト
	 * @throws WNoResultException
	 */
	public List<VReleaseWeb> getEntityList(List<Integer> webIdList)
	throws WNoResultException {

		SimpleWhere where = new SimpleWhere()
			.in(toCamelCase(VReleaseWeb.ID), webIdList.toArray())
		;
		return findByCondition(where);
	}


	/**
	 * 公開可能なWEBデータのセレクトを取得します。
	 * @param areaCd エリアコード
	 * @return 公開WEBデータのリスト
	 */
	public AutoSelect<VReleaseWeb> getEntitySelect(int areaCd) {
		SimpleWhere where = new SimpleWhere()
		.eq(toCamelCase(VReleaseWeb.AREA_CD), areaCd);

		return jdbcManager.from(VReleaseWeb.class)
							.where(where);
	}

	/**
	 * WebdataIdとエリアを特定した検索条件です。
	 * 「エンティティの削除フラグ=0」の条件はVIEW内で処理されているので検索条件には入れていません。
	 * @param webId
	 * @param materialKbn
	 * @return
	 */
	private SimpleWhere getWhereById(int webId, int areaCd) {
		SimpleWhere where = new SimpleWhere()
		.eq(toCamelCase(VReleaseWeb.ID), webId)
		.eq(toCamelCase(VReleaseWeb.AREA_CD), areaCd)
		;
		return where;
	}


	/**
	 * Webデータが応募フォームを表示可能かを取得します。
	 * @param webId
	 * @param areaCd
	 * @return true:表示可能、false:表示不可
	 * @throws WNoResultException
	 */
	public boolean isWebdataAllowApplication(int webId, int areaCd) {
		SimpleWhere where = getWhereApplicationForm(webId, areaCd, MTypeConstants.ApplicationFormKbn.EXIST);
		return (countRecords(where) > 0 ? true : false);
	}

	/**
	 * 応募可能なWebデータかどうかを指定する条件です。
	 * エンティティに削除フラグはVIEW内で処理されているので検索条件には入れていません。
	 * @param webId
	 * @param materialKbn
	 * @return
	 */
	private SimpleWhere getWhereApplicationForm(int webId, int areaCd,  int applicationFormKbn) {
		SimpleWhere where = new SimpleWhere()
		.eq(toCamelCase(VReleaseWeb.ID), webId)
		.eq(toCamelCase(VReleaseWeb.AREA_CD), areaCd)
		.eq(toCamelCase(VReleaseWeb.APPLICATION_FORM_KBN), applicationFormKbn)
		;
		return where;
	}

	/**
	 * 顧客IDに紐づく掲載中のWEBデータが存在するかどうか判定します。
	 * @param customerId 顧客ID
	 * @return 掲載中のデータが存在すればtrue、なければfalse
	 */
	public boolean isPsotCustomerExists(int customerId) {

		// 件数検索
		int count = (int) countRecords(getWhereByCustomerId(customerId));

		return count > 0 ? true : false;

	}

	/**
	 * 顧客IDに紐づく掲載中のWEBデータリストを返します。
	 * @param customerId 顧客ID
	 * @return 顧客に紐付く掲載中WEBデータの一覧
	 * @throws WNoResultException データがない場合はエラー
	 */
	public List<VReleaseWeb> getPostCustomerEntity(int customerId) throws WNoResultException {

		// 検索結果を返却
		return findByCondition(getWhereByCustomerId(customerId), getSortKeyCustomer());
	}

	/**
	 * 顧客IDに紐づく掲載中のWEBデータリストを返します。
	 * @param property 検索用プロパティ
	 * @return
	 * @throws WNoResultException データがない場合はエラー
	 */
	public void getPostCustomerEntity(ReleaseWebSearchProperty property) throws WNoResultException {

		try {
			PageNavigateHelper pageNavi = new PageNavigateHelper(getSortKeyCustomer(), property.maxRow);
			Where where = getWhereByCustomerId(property.customerId);

			pageNavi.changeAllCount((int) countRecords(where));
			pageNavi.setPage(property.targetPage);

			if (pageNavi.allCount == 0) {
				throw new WNoResultException();
			}



			property.vReleaseWebList = findByCondition(where, pageNavi);

			property.pageNavi = pageNavi;


		} catch (SNoResultException e) {
			throw new WNoResultException(e);
		}
	}

	/**
	 * 顧客IDに紐づく掲載中で、店舗一覧掲載が「あり」のWEBデータリストを返します。
	 * @param customerId
	 * @param areaCd
	 * @return
	 * @throws WNoResultException
	 */
	public List<VReleaseWeb> getPostShopListCustomerEntity(int customerId, int areaCd) throws WNoResultException {
		SimpleWhere where = new SimpleWhere()
					.eq(toCamelCase(VReleaseWeb.CUSTOMER_ID), customerId)
					.eq(toCamelCase(VReleaseWeb.AREA_CD), areaCd)
					.eq(toCamelCase(VReleaseWeb.SHOP_LIST_DISPLAY_KBN), MTypeConstants.ShopListDisplayKbn.ARI)
					// 顧客が物理削除されていた場合の対応
					.isNotNull(toCamelCase(VReleaseWeb.CUSTOMER_NAME), true);

		return findByCondition(where);
	}

	/**
	 * 顧客IDに紐づく、誌面データ以外の掲載中WEBデータが存在するか判定します。
	 * @param customerId 顧客ID
	 * @return 掲載中のデータが存在すればtrue、なければfalse
	 */
	public boolean isExceptMagazineExists(int customerId) {

		SimpleWhere where = getWhereByCustomerId(customerId);
		// 誌面データ以外の条件を追加
		where.eq(camelize(VReleaseWeb.MAGAZINE_FLG), MTypeConstants.MagazineFlg.NOT_TARGET);
		// 件数検索
		int count = (int) countRecords(where);

		return count > 0;
	}


	/**
	 * 顧客に紐付く検索条件を返します。
	 * @param customerId 顧客ID
	 * @return 顧客に紐付く検索条件
	 */
	private SimpleWhere getWhereByCustomerId(int customerId) {

		SimpleWhere where = new SimpleWhere()
		.eq(toCamelCase(VRReleaseWeb.CUSTOMER_ID), customerId)
		// 顧客が物理削除されていた場合の対応
		.isNotNull(toCamelCase(VRReleaseWeb.CUSTOMER_NAME), true);

		return where;
	}

	/**
	 * 顧客に紐づくWEBデータ一覧の表示順を返します。
	 * @return 顧客に紐づく表示順
	 */
	private String getSortKeyCustomer() {

		String[] sortKey = new String[]{
				//ソート順を設定
				desc(camelize(VReleaseWeb.POST_END_DATETIME)),	//掲載開始日時の降順
				desc(camelize(VReleaseWeb.ID))					//IDの降順
		};

		//カンマ区切りにして返す
		return SqlUtils.createCommaStr(sortKey);
	}

	/**
	 * 現在公開可能なWebdate数を取得します。
	 * @param areaCd
	 * @param date
	 * @return
	 */
	public int countReleaseWebdata(AreaCdEnum areaCd, Date date) {
		SimpleWhere where = new SimpleWhere()
								.eq(toCamelCase(VReleaseWeb.AREA_CD), areaCd.getValue())
								.le(toCamelCase(VReleaseWeb.POST_START_DATETIME), date)
								.ge(toCamelCase(VReleaseWeb.POST_END_DATETIME), date)
								;

		long count = countRecords(where);
		return (int)count;
	}

	/**
	 * 注目店舗のデータをランダムに取得します。
	 * @param areaCd エリアコード
	 * @param pageNavi ページナビ
	 * @return 掲載中WEBデータのリスト
	 * @throws WNoResultException データが取得できない場合はエラー
	 */
	public List<VReleaseWeb> getAttentionShopList(AreaCdEnum areaCd, PageNavigateHelper pageNavi) throws WNoResultException {

		// 検索条件
		SimpleWhere where = new SimpleWhere()
							.eq(camelize(VReleaseWeb.AREA_CD), areaCd.getValue())									// エリアコード
							.eq(camelize(VReleaseWeb.ATTENTION_SHOP_FLG), MTypeConstants.AttentionShopFlg.TARGET);	// 注目企業対象

		// ランダムで取得
		pageNavi.setSortKey(SqlUtils.RANDOM);

		try {
			// データの取得
			return findByCondition(where, pageNavi);

		// データがない場合は、WNoResultExceptionに変更して投げる
		} catch (SNoResultException e) {
			throw new WNoResultException("注目店舗表示対象のデータがありません。");
		}
	}

	/**
	 * 注目店舗のデータをランダムに取得します。
	 * LIMITの指定は無し
	 * @param areaCd エリアコード
	 * @param pageNavi ページナビ
	 * @return 掲載中WEBデータのリスト
	 * @throws WNoResultException データが取得できない場合はエラー
	 */
	public List<VReleaseWeb> getAttentionShopList(AreaCdEnum areaCd) throws WNoResultException {

		// 検索条件
		SimpleWhere where = new SimpleWhere()
							.eq(camelize(VReleaseWeb.AREA_CD), areaCd.getValue())									// エリアコード
							.eq(camelize(VReleaseWeb.ATTENTION_SHOP_FLG), MTypeConstants.AttentionShopFlg.TARGET);	// 注目企業対象


		try {
			// データの取得
			return findByCondition(where, SqlUtils.RANDOM);

		// データがない場合は、WNoResultExceptionに変更して投げる
		} catch (SNoResultException e) {
			throw new WNoResultException("注目店舗表示対象のデータがありません。");
		}
	}





	/**
	 * 公開中店舗一覧の顧客IDリストを取得
	 * @throws WNoResultException
	 */
	public List<Integer> getCustomerIdListOfReleaseShopList(int areaCd) throws WNoResultException {
		String sql = String.format("SELECT DISTINCT %s FROM %s WHERE %s = ? AND %s = ?",
				VReleaseWeb.CUSTOMER_ID,
				VReleaseWeb.TABLE_NAME,
				VReleaseWeb.SHOP_LIST_DISPLAY_KBN,
				VReleaseWeb.AREA_CD);

		List<Integer> list = jdbcManager.selectBySql(Integer.class, sql, MTypeConstants.ShopListDisplayKbn.ARI, areaCd)
						.getResultList();

		if (CollectionUtils.isEmpty(list)) {
			throw new WNoResultException("公開中店舗一覧の顧客IDリストが空です。");
		}
		return list;
	}
}