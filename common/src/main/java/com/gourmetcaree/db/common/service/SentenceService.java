package com.gourmetcaree.db.common.service;

import com.gourmetcaree.db.common.entity.MSentence;

/**
 * 定型文マスタのサービスクラスです。
 * @version 1.0
 */
public class SentenceService extends AbstractGroumetCareeBasicService<MSentence> {

	/**
	 * INSERT時の共通カラムにデータをセットします。
	 * @param Entity エンティティ
	 */
	@Override
	protected void setCommonInsertColmun(MSentence entity) {

		super.setCommonInsertColmun(entity);

		// 登録日時をセット
		entity.registrationDatetime = entity.insertDatetime;
	}
}
