package com.gourmetcaree.shop.logic.logic;

import static com.gourmetcaree.common.util.SqlUtils.desc;
import static org.seasar.framework.util.StringUtil.camelize;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.seasar.extension.jdbc.exception.SOptimisticLockException;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.common.exception.InternalGourmetCareeSystemErrorException;
import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.TFootprint;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.FootprintService;
/**
 * 足あとに関するロジッククラスです。
 * @author Takahiro Kimura
 * @version 1.0
 *
 */
@Component(instance=InstanceType.SINGLETON)
public class FootprintLogic  extends AbstractShopLogic {

	/** 足あとサービス */
	@Resource
	protected FootprintService footprintService;

	/**
	 * 足あと処理
	 */
	public void doFootPrint(int customerId, int memberId) {

		try {
			// 当日の足あとを取得
			List<TFootprint> entityList = doSearchTodayFootprint(customerId, memberId);

			// 当日の足あとがある場合
			if (entityList.size() > 0) {

				// 最新の足あとを更新(通常は1件のみ)
				updateFootPrint(entityList.get(0));

			// 当日の足あとがない場合
			} else {
				// 足あとの登録
				insertFootprint(customerId, memberId);
			}

		// 当日の足あとがない場合
		} catch (WNoResultException e) {
			// 足あとの登録
			insertFootprint(customerId, memberId);
		}
	}

	/**
	 * 足あとの登録
	 * @param customerId 顧客ID
	 * @param memberId 会員ID
	 */
	private void insertFootprint(int customerId, int memberId) {

		// 足あとデータ生成
		TFootprint tFootprint = createFootPrintData(customerId, memberId);

		// 足あとをつける
		footprintService.insert(tFootprint);
	}

	/**
	 * 足あとの更新
	 * @param entity 足あとエンティティ
	 */
	private void updateFootPrint(TFootprint entity) {

		TFootprint updateEntity = new TFootprint();
		// IDをセット
		updateEntity.id = entity.id;
		// バージョンをセット
		updateEntity.version = entity.version;
		// 訪問日時をセット
		updateEntity.accessDatetime = new Timestamp(new Date().getTime());
		// 閲覧フラグに未閲覧をセット
		updateEntity.readFlg = MTypeConstants.ReadFlg.NEVER;

		try {
			// 足あとの更新
			footprintService.update(updateEntity);

		// 楽観的排他制御でエラーが出た場合は、同時に更新されたとして処理しない
		} catch (SOptimisticLockException e) {
			return;
		}
	}

	/**
	 * 当日の足あとを取得する
	 * @param customerId 顧客ID
	 * @param memberId 会員ID
	 * @return 当日の足あとリスト
	 * @throws WNoResultException データが存在しない場合はエラー
	 */
	private List<TFootprint> doSearchTodayFootprint(int customerId, int memberId) throws WNoResultException {

		SimpleWhere where = new SimpleWhere();
		try {
			// 検索条件の設定
			where.eq(camelize(TFootprint.CUSTOMER_ID), customerId);
			where.eq(camelize(TFootprint.MEMBER_ID), memberId);
			// 本日の日付
			where.ge(camelize(TFootprint.ACCESS_DATETIME), DateUtils.getStartDatetime(new Date()));
			where.le(camelize(TFootprint.ACCESS_DATETIME), DateUtils.getEndDatetime(new Date()));
			where.eq(camelize(TFootprint.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		// フォーマットに失敗した場合はシステムエラー
		} catch (ParseException e) {
			throw new InternalGourmetCareeSystemErrorException("当日の足あとの取得時に日付の変換に失敗しました。");
		}

		// ソート順
		String sortKey = desc(camelize(TFootprint.ACCESS_DATETIME));

		// データの検索結果を返す
		return footprintService.findByCondition(where, sortKey);

	}

	/**
	 * 足あとデータ生成
	 */
	private TFootprint createFootPrintData(int customerId, int memberId) {

		TFootprint tFootprint = new TFootprint();
		tFootprint.customerId = customerId;
		tFootprint.memberId = memberId;
		tFootprint.accessDatetime = new Timestamp(new Date().getTime());
		// 閲覧フラグに未閲覧をセット
		tFootprint.readFlg = MTypeConstants.ReadFlg.NEVER;

		return tFootprint;
	}
}
