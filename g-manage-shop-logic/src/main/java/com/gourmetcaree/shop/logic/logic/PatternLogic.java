package com.gourmetcaree.shop.logic.logic;

import static com.gourmetcaree.common.util.SqlUtils.desc;
import static com.gourmetcaree.common.util.SqlUtils.asc;
import static org.seasar.framework.util.StringUtil.camelize;

import java.util.List;

import javax.annotation.Resource;

import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.db.common.entity.MSentence;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.SentenceService;

/**
 * 定型文に関するロジッククラスです。
 * @author Makoto Otani
 *
 */
@Component(instance=InstanceType.SINGLETON)
public class PatternLogic extends AbstractShopLogic {

	/** 定型文サービス */
	@Resource
	protected SentenceService sentenceService;

	/**
	 * 定型文一覧のデータを取得します。
	 * @return 定型文一覧のリスト
	 * @throws WNoResultException データが取得できない場合はエラー
	 */
	public List<MSentence> getSentenceList() throws WNoResultException {

		// 一覧の検索
		return sentenceService.findByCondition(createListWhere(), createListSort());

	}

	/**
	 * 定型文一覧の検索条件を取得します。
	 * @return 定型文一覧の検索条件
	 */
	private Where createListWhere() {

		SimpleWhere where = new SimpleWhere();
		// 顧客ID
		where.eq(camelize(MSentence.CUSTOMER_ID), getCustomerId());
		// 削除フラグ = 0(未削除)
		where.eq(camelize(MSentence.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		return where;
	}

	/**
	 * 定型文一覧の表示順を取得します。
	 * @return 定型文一覧の表示順
	 */
	private String createListSort() {

		// 登録日時の降順
		StringBuilder sort = new StringBuilder("");
		sort.append(desc(camelize(MSentence.REGISTRATION_DATETIME)));
		sort.append(",");
		sort.append(asc(camelize(MSentence.ID)));
		return sort.toString();
	}

	/**
	 * 定型文詳細を取得します。<br />
	 * 取得したデータがログイン顧客以外の場合はデータ未取得エラーを返します。
	 * @param id 定型文ID
	 * @return 定型文エンティティ
	 * @throws WNoResultException データが見つからない場合はエラー
	 */
	public MSentence getDetailSentence(int id) throws WNoResultException {

		try {
			// 定型文マスタからデータの取得
			MSentence mSentence = sentenceService.findById(id);

			// 取得した定型文がログイン顧客のデータかチェックする
			if (mSentence.customerId != getCustomerId()) {
				// ログイン顧客のデータで無い場合はエラー
				throw new WNoResultException();
			}

			// 定型文詳細を返却
			return mSentence;

		// データが見つからない場合はエラー
		} catch (SNoResultException e) {
			throw new WNoResultException();
		}

	}
}
