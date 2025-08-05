package com.gourmetcaree.admin.service.logic;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.seasar.extension.jdbc.Where;
import org.seasar.extension.jdbc.exception.SNoResultException;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.util.StringUtil;

import com.gourmetcaree.admin.service.property.CompanyProperty;
import com.gourmetcaree.db.common.entity.MCompany;
import com.gourmetcaree.db.common.entity.MCompanyArea;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.CompanyAreaService;
import com.gourmetcaree.db.common.service.CompanyService;

/**
 * 会社データロジッククラス
 * @author Makoto Otani
 * @version 1.0
 *
 */
public class CompanyLogic extends AbstractAdminLogic {

	/** 会社マスタのサービス */
	@Resource
	protected CompanyService companyService;

	/** 会社エリアマスタのサービス */
	@Resource
	protected CompanyAreaService companyAreaService;

	/**
	 * 会社一覧を表示するためのデータを取得する。
	 * @param property
	 * @throws WNoResultException 検索結果が無い場合はエラー
	 */
	public void getCompanyList(CompanyProperty property) throws WNoResultException {

		// プロパティチェック
		checkEmptyProperty(property);

		// 会社データと、会社エリアマスタを結合して一覧データを取得する。
		// データが無い場合は、エラーを投げる
		List<MCompany> searchList = companyService.findByConditionInnerJoin(
								StringUtil.camelize(MCompany.M_COMPANYAREA_LIST), createInputValueWhere(property), createListSort());

		// 取得した条件から、画面表示に適したデータを再度IDで取得する。
		// データが無い場合は、エラーを投げる
		property.entityList =  companyService.findByConditionInnerJoin(
				StringUtil.camelize(MCompany.M_COMPANYAREA_LIST), createListWhere(searchList), createListSort());

	}

	/**
	 * 会社一覧で入力された検索条件をセットして返す。
	 * @param property 会社一覧の検索条件プロパティ
	 * @return 検索条件
	 */
	private Where createInputValueWhere(CompanyProperty property) {

		// エリアが指定されていれば、検索条件に追加
		String areaCd = null;
		if (!property.areaCd.isEmpty()) {
			areaCd = property.areaCd.get(0);
		}

		// 検索条件の作成
		Where where =	new SimpleWhere()
								.eq(StringUtil.camelize(MCompany.AGENCY_FLG), property.mCompany.agencyFlg)									// 代理店フラグ
								.eq(StringUtil.camelize(MCompany.M_COMPANYAREA_LIST)+"."+StringUtil.camelize(MCompanyArea.AREA_CD), areaCd)	// エリアコード
								.contains(StringUtil.camelize(MCompany.COMPANY_NAME), property.mCompany.companyName)						// %会社名%
								.eq(StringUtil.camelize(MCompany.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);									// 削除フラグ
		return where;
	}

	/**
	 * 会社一覧を表示するIdを検索条件にセットして返す。
	 * @param property 会社一覧の検索条件プロパティ
	 * @return 検索条件
	 */
	private Where createListWhere(List<MCompany> list) {

		// エンティティからIdを取得してリストへセット
		List<Integer> idList = new ArrayList<Integer>();
		for (MCompany entity : list) {
			int id = entity.id;
			idList.add(id);
		}

		// 検索条件の作成
		Where where =	new SimpleWhere()
								.in(StringUtil.camelize(MCompany.ID), idList.toArray());	 // ID
		return where;
	}


	/**
	 * 会社一覧のソート順を返す。
	 * @return 会社一覧のソート順
	 */
	private String createListSort(){

		StringBuilder sortKey = new StringBuilder();
		// ソート順を設定
		sortKey.append(MCompany.ID).append(" DESC").append(", ");	// 会社マスタ.IDの降順
		sortKey.append(createDetailSort());							// 会社エリアマスタ.エリアコードの昇順

		return sortKey.toString();

	}

	/**
	 * 会社詳細のソート順を返す。
	 * @return 会社詳細のソート順
	 */
	private String createDetailSort(){

		StringBuilder sortKey = new StringBuilder();
		// ソート順を設定
		sortKey.append(StringUtil.camelize(MCompany.M_COMPANYAREA_LIST)).append(".");
		sortKey.append(StringUtil.camelize(MCompanyArea.AREA_CD));	// 会社エリアマスタ.エリアコードの昇順

		return sortKey.toString();

	}
	/**
	 * 会社詳細を表示するためのデータを取得する。
	 * @param property 会社詳細の検索条件プロパティ
	 * @throws WNoResultException 検索結果が無い場合はエラー
	 */
	public void getCompanyDetail(CompanyProperty property) throws WNoResultException {

		// プロパティチェック
		checkEmptyProperty(property);

		try {
			// 会社データと、会社エリアマスタを結合して詳細データを取得する。
			property.mCompany = companyService.findByIdInnerJoin(
									StringUtil.camelize(MCompany.M_COMPANYAREA_LIST), property.mCompany.id, createDetailSort());

		// データが無い場合は、エラーを返す
		}catch (SNoResultException e) {
			throw new WNoResultException();
		}
	}

	/**
	 * 会社の登録を行う。
	 * @param property 登録するプロパティ
	 */
	public void insertCompay(CompanyProperty property) {

		// プロパティの中身チェック
		checkEmptyPropContents(property);

		// プロパティから値を取得
		MCompany companyEntity = property.mCompany;

		// 会社マスタの登録
		companyService.insert(companyEntity);

		// 会社エリアマスタ登録のため、入力されたエリアコードの数だけレコードを作成
		List<MCompanyArea> list = setAreaCd(property, companyEntity);

		// 会社エリアマスタの登録
		companyAreaService.insertBatch(list);
	}

	/**
	 * 会社の更新を行う。
	 * @param property 更新するプロパティ
	 */
	public void updateCompany(CompanyProperty property) {

		// プロパティの中身チェック
		checkEmptyPropContents(property);

		// 削除対象の会社エリアマスタが空の場合はエラー
		if (property.mCompanyAreaList == null || property.mCompanyAreaList.isEmpty()) {
			throw new IllegalArgumentException("引数の値が空です。");
		}

		// プロパティから値を取得
		MCompany companyEntity = property.mCompany;

		// 会社マスタの登録
		companyService.update(companyEntity);

		// 会社エリアマスタ削除のため、値を取得（DeleteInsert）
		List<MCompanyArea> delMCompanyAreaList = property.mCompanyAreaList;
		// 会社エリアマスタ登録のため、入力されたエリアコードの数だけレコードを作成
		List<MCompanyArea> insMCompanyAreaList = setAreaCd(property, companyEntity);

		// 会社エリアマスタの削除
		companyAreaService.deleteBatch(delMCompanyAreaList);
		// 会社エリアマスタの登録
		companyAreaService.insertBatch(insMCompanyAreaList);
	}

	/**
	 * プロパティがnullの場合は、エラーを返す。
	 * @param property チェックするプロパティ
	 */
	private void checkEmptyProperty(CompanyProperty property) {

		// プロパティがnullの場合はエラー
		if (property == null || property.mCompany == null) {
			throw new IllegalArgumentException("引数がnullです。");
		}
	}

	/**
	 * プロパティの中身が空の場合は、エラーを返す。
	 * @param property チェックするプロパティ
	 */
	private void checkEmptyPropContents(CompanyProperty property) {

		checkEmptyProperty(property);

		// 中身が空の場合はエラー
		if (property.areaCd == null || property.areaCd.isEmpty()) {
			throw new IllegalArgumentException("引数の値が空です。");
		}
	}

	/**
	 * 会社エリアマスタ登録のため、エンティティのリストを作成する。
	 * @param property 登録するプロパティ
	 * @param companyEntity 親となるエンティティ
	 * @return 会社エリアマスタのエンティティリスト
	 */
	private List<MCompanyArea> setAreaCd(CompanyProperty property, MCompany companyEntity) {

		// 会社エリアマスタ登録のため、入力されたエリアコードの数だけレコードを作成
		List<MCompanyArea> list = new ArrayList<MCompanyArea>();

		for (String areaCd : property.areaCd) {

			MCompanyArea companyAreaEntity = new MCompanyArea();
			// 会社IDをセット
			companyAreaEntity.companyId = companyEntity.id;
			// エリアコードをセット
			companyAreaEntity.areaCd = Integer.parseInt(areaCd);
			// 削除フラグ = 未削除
			companyAreaEntity.deleteFlg =  DeleteFlgKbn.NOT_DELETED;

			list.add(companyAreaEntity);
		}

		return list;
	}
}
