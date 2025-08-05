package com.gourmetcaree.db.common.service;

import static com.gourmetcaree.common.util.SqlUtils.*;
import static org.seasar.framework.util.StringUtil.*;

import java.util.Date;
import java.util.List;

import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.entity.MSpecial;
import com.gourmetcaree.db.common.entity.MSpecialDisplay;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;

/**
 * 特集マスタのサービスクラスです。
 * @version 1.0
 */
public class SpecialService extends AbstractGroumetCareeBasicService<MSpecial> {

	/**
	 * 特集一覧を取得
	 * @return 特集リスト
	 * @throws WNoResultException データが取得できない場合はエラー
	 */
	public List<MSpecial> getSpecialList() throws SNoResultException, WNoResultException {

		SimpleWhere where = new SimpleWhere().
				eq(camelize(MSpecial.DELETE_FLG), DeleteFlgKbn.NOT_DELETED); // 削除フラグ

		// 特集情報を取得する
		return findByConditionLeftJoin(camelize(MSpecial.M_SPECIAL_DISPLAY_LIST), where, createListSort());

	}

	/**
	 * 特集一覧の表示順を返します
	 * @return 特集一覧の表示順
	 */
	private String createListSort(){

		String[] sortKey = new String[]{
				//ソート順を設定
				desc(camelize(MSpecial.POST_START_DATETIME)),		// 掲載開始日時の降順
				desc(camelize(MSpecial.POST_END_DATETIME)),			// 掲載終了日時の降順
				desc(camelize(MSpecial.ID)),						// IDの降順
				asc(dot(camelize(MSpecial.M_SPECIAL_DISPLAY_LIST), camelize(MSpecialDisplay.AREA_CD)))	// エリアコード
		};

		//カンマ区切りにして返す
		return SqlUtils.createCommaStr(sortKey);
	}

	/**
	 * 公開側のTOPページに表示する特集一覧を取得します。
	 * @param areaCd エリアコード
	 * @return 特集情報のリスト
	 * @throws WNoResultException データが無い場合はエラー
	 */
	public List<MSpecial> getFrontDisplaySpecial(Integer areaCd) throws WNoResultException {

		// エリアの指定が無い場合は検索しない
		if (areaCd == null) {
			throw new WNoResultException();
		}

		Date today = new Date();
		SimpleWhere where = new SimpleWhere()
					.eq(camelize(MSpecial.DELETE_FLG), DeleteFlgKbn.NOT_DELETED)		// 特集マスタ.削除フラグ
					.le(camelize(MSpecial.POST_START_DATETIME), today)					// 特集マスタ.掲載開始日時
					.ge(camelize(MSpecial.POST_END_DATETIME), today)					// 特集マスタ.掲載終了日時
					.eq(dot(camelize(MSpecial.M_SPECIAL_DISPLAY_LIST), camelize(MSpecialDisplay.DELETE_FLG)), DeleteFlgKbn.NOT_DELETED)	// 特集表示マスタ.削除フラグ
					.eq(dot(camelize(MSpecial.M_SPECIAL_DISPLAY_LIST), camelize(MSpecialDisplay.AREA_CD)), areaCd)						// 特集表示マスタ.エリアコード
					;

		// 検索実行
		return findByConditionInnerJoin(camelize(MSpecial.M_SPECIAL_DISPLAY_LIST), where, createFrontDisplaySort());
	}

	/**
	 * 公開側の特集表示順を返します
	 * @return 公開側の特集表示順
	 */
	private String createFrontDisplaySort(){

		String[] sortKey = new String[]{
				//ソート順を設定
				asc(dot(camelize(MSpecial.M_SPECIAL_DISPLAY_LIST), camelize(MSpecialDisplay.DISPLAY_ORDER))),	// 表示順
				desc(camelize(MSpecial.POST_START_DATETIME)),		// 掲載開始日時の降順
				desc(camelize(MSpecial.POST_END_DATETIME)),			// 掲載終了日時の降順
				desc(camelize(MSpecial.ID))							// IDの降順
		};

		//カンマ区切りにして返す
		return SqlUtils.createCommaStr(sortKey);
	}

}