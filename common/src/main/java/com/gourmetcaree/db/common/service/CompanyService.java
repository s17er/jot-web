package com.gourmetcaree.db.common.service;

import org.seasar.extension.jdbc.where.SimpleWhere;

import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.db.common.entity.MCompany;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;

/**
 * 会社マスタのサービスクラスです。
 * @version 1.0
 */
public class CompanyService extends AbstractGroumetCareeBasicService<MCompany> {

	/**
	 * 会社のデータを取得
	 * @param id 会社ID
	 * @return 会社エンティティ
	 */
	public MCompany getCompanyData(int id) {

		MCompany entity = jdbcManager.from(MCompany.class)
						.where(new SimpleWhere()
						.eq("id", id)
						.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
						.getSingleResult();

		return entity;
	}

	/**
	 * INSERT時の共通カラムにデータをセットします。
	 * @param Entity エンティティ
	 */
	@Override
	protected void setCommonInsertColmun(MCompany entity) {

		// 共通項目をセット
		super.setCommonInsertColmun(entity);

		// 登録日時に、insert日時と同じ日時をセット
		entity.registrationDatetime = entity.insertDatetime;

		// 表示順の初期値をセット
		entity.displayOrder = GourmetCareeConstants.DEFAULT_DISP_NO;
	}

}