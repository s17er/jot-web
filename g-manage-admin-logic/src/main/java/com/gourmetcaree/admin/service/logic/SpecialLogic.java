package com.gourmetcaree.admin.service.logic;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.util.StringUtil;

import com.gourmetcaree.admin.service.property.SpecialProperty;
import com.gourmetcaree.db.common.entity.MSpecial;
import com.gourmetcaree.db.common.entity.MSpecialDisplay;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.SpecialDisplayService;
import com.gourmetcaree.db.common.service.SpecialService;


/**
 * 特集ロジッククラス
 * @author Takahiro Kimura
 * @version 1.0
 *
 */
public class SpecialLogic extends AbstractAdminLogic {

	/** 特集のサービス */
	@Resource
	protected SpecialService specialService;

	/** 特集表示のサービス */
	@Resource
	protected SpecialDisplayService specialDisplayService;

	/**
	 * 特集情報を登録します。<br />
	 * 特集プロパティに、特集データをセットしたエンティティ、<br />
	 * エリアコードをセットして呼び出します。
	 * @param property 特集プロパティ
	 */
	public void insertSpecial(SpecialProperty property) {

		// プロパティがnullの場合はエラー
		if (property == null) {
			throw new IllegalArgumentException("引数がnullです。");
		}

		// 特集マスタに登録
		specialService.insert(property.mSpecial);

		// エリアが登録されていれば特集表示マスタに登録
		if (property.areaCd != null && !property.areaCd.isEmpty()) {
			insertMspecialDisplay(property);
		}
	}

	/**
	 * 特集情報を更新します。<br />
	 * 特集プロパティに、特集データをセットしたエンティティ、<br />
	 * エリアコードをセットして呼び出します。
	 * @param property
	 */
	public void updateSpecial(SpecialProperty property) {

		// プロパティがnullの場合はエラー
		if (property == null) {
			throw new IllegalArgumentException("引数がnullです。");
		}

		// 特集マスタを更新
		specialService.update(property.mSpecial);

		// 特集表示マスタを削除
		specialDisplayService.deleteMSpecialDisplayBySpecialId(property.mSpecial.id);

		// エリアが登録されていればDB登録
		if (property.areaCd != null && !property.areaCd.isEmpty()) {
			insertMspecialDisplay(property);
		}
	}

	/**
	 * 特集を検索します
	 * @param property
	 * @return
	 * @throws WNoResultException
	 */
	public List<MSpecial> searchSpecial(SpecialProperty property) throws WNoResultException {

		SimpleWhere where = new SimpleWhere();
		if(!property.areaCd.isEmpty()) {
			where.eq(StringUtil.camelize(MSpecial.M_SPECIAL_DISPLAY_LIST) + "." + StringUtil.camelize(MSpecialDisplay.AREA_CD), property.areaCd.get(0))
			.eq(StringUtil.camelize(MSpecial.DELETE_FLG), DeleteFlgKbn.NOT_DELETED)
			.ge(StringUtil.camelize(MSpecial.POST_START_DATETIME), property.mSpecial.postStartDatetime)
			.le(StringUtil.camelize(MSpecial.POST_END_DATETIME), property.mSpecial.postEndDatetime);
		}


		return specialService.findByConditionInnerJoin(StringUtil.camelize(MSpecial.M_SPECIAL_DISPLAY_LIST), where, "id DESC");
	}

	/**
	 * 登録されたエリア分、特集表示マスタにデータを登録します。
	 * @param property 特集プロパティ
	 */
	private void insertMspecialDisplay(SpecialProperty property) {

		List<MSpecialDisplay> entityList = new ArrayList<MSpecialDisplay>();

		// セットされたエリア分登録する
		for (int areaCd : property.areaCd) {
			MSpecialDisplay entity = new MSpecialDisplay();
			// 特集ID
			entity.specialId = property.mSpecial.id;
			// エリアコード
			entity.areaCd = areaCd;
			// 削除フラグ
			entity.deleteFlg = DeleteFlgKbn.NOT_DELETED;

			entityList.add(entity);
		}

		// 特集表示マスタに登録
		specialDisplayService.insertBatch(entityList);
	}

}
