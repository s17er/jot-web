package com.gourmetcaree.common.logic;

import static org.seasar.extension.jdbc.operation.Operations.*;
import static org.seasar.framework.util.StringUtil.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.beans.util.BeanMap;
import org.seasar.framework.util.StringUtil;

import com.google.common.collect.Lists;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.dto.LabelValueDto;
import com.gourmetcaree.common.enums.ManageAuthLevel;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.logic.StationLogic.CdNameDto;
import com.gourmetcaree.common.taglib.function.GourmetCareeFunctions;
import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.constants.MAreaConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.AbstractCommonEntity;
import com.gourmetcaree.db.common.entity.MAdvancedRegistration;
import com.gourmetcaree.db.common.entity.MArea;
import com.gourmetcaree.db.common.entity.MCity;
import com.gourmetcaree.db.common.entity.MCompany;
import com.gourmetcaree.db.common.entity.MPrefectures;
import com.gourmetcaree.db.common.entity.MRailroad;
import com.gourmetcaree.db.common.entity.MRoute;
import com.gourmetcaree.db.common.entity.MSales;
import com.gourmetcaree.db.common.entity.MSentence;
import com.gourmetcaree.db.common.entity.MSpecial;
import com.gourmetcaree.db.common.entity.MStation;
import com.gourmetcaree.db.common.entity.MStatus;
import com.gourmetcaree.db.common.entity.MTerminal;
import com.gourmetcaree.db.common.entity.MType;
import com.gourmetcaree.db.common.entity.MVolume;
import com.gourmetcaree.db.common.entity.MWebTag;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.CityService;
import com.gourmetcaree.db.common.service.CompanyAreaService;
import com.gourmetcaree.db.common.service.StatusService;
import com.gourmetcaree.db.common.service.TerminalService;
import com.gourmetcaree.db.common.service.dto.ServiceAdminAccessUser;
import com.gourmetcaree.valueobject.TypePrefectureInfo;

import jp.co.whizz_tech.commons.WztStringUtil;

/**
 * ラベルとバリューのリストを生成するロジッククラスです。<br>
 * 主にマスタから画面のセレクトボックス用データを作ります。
 * @author Katsutoshi Hasegawa
 * @version 1.0
 */
public class LabelValueListLogic extends AbstractGourmetCareeLogic {

	/** ステータスサービス */
	@Resource
	protected StatusService statusService;

	/** 会社エリアサービス */
	@Resource
	protected CompanyAreaService companyAreaService;

	@Resource
	private TypeLogic typeLogic;

	@Resource
	private StationLogic stationLogic;

	@Resource
	private CityService cityService;

	@Resource
	private TerminalService terminalService;

	/**
     * 区分マスタからプルダウンを取得します。
     * @param typeCd 区分マスタコード
     * @param blankLineLabel 初期ラベル
     * @param suffix 接尾辞
     * @param noDisplayValue 非表示値
     * @return List<LabelValueDto> ラベルと値を保持するDtoのリスト
     */
    public List<LabelValueDto> getTypeList(String typeCd, String blankLineLabel, String suffix, List<Integer> noDisplayValue) {
        List<MType> entityList
            = jdbcManager
                .from(MType.class)
                .where(new SimpleWhere()
                .eq("typeCd", typeCd)
                .eq("deleteFlg", DeleteFlgKbn.NOT_DELETED)
                .notIn("typeValue", noDisplayValue.toArray()))
                .orderBy("displayOrder")
                .getResultList();

        if (entityList == null || entityList.size() == 0) {
            return new ArrayList<LabelValueDto>(0);
        }

        List<LabelValueDto> labelValueList = new ArrayList<LabelValueDto>(entityList.size() + 1);

        if (StringUtils.isNotEmpty(blankLineLabel)) {
            LabelValueDto dto = new LabelValueDto();
            dto.setLabel(blankLineLabel);
            dto.setValue("");
            labelValueList.add(dto);
        }

        for (MType entity : entityList) {
            LabelValueDto dto = new LabelValueDto();
            dto.setLabel(entity.typeName + suffix);
            dto.setValue(String.valueOf(entity.typeValue));
            labelValueList.add(dto);
        }

        return labelValueList;
    }

	/**
	 * 区分マスタからプルダウンを取得します。
	 * @param typeCd 区分マスタコード
	 * @param blankLineLabel 初期ラベル
	 * @param suffix 接尾辞
	 * @param noDisplayValue 非表示値
	 * @return List<LabelValueDto> ラベルと値を保持するDtoのリスト
	 */
	public List<LabelValueDto> getTypeList(String typeCd, String blankLineLabel, Integer minValue, Integer maxValue, String suffix, List<Integer> noDisplayValue) {
		SimpleWhere where = new SimpleWhere().eq("typeCd", typeCd)
				.notIn("typeValue", noDisplayValue.toArray());

		if (minValue != null) {
			where.ge(WztStringUtil.toCamelCase(MType.TYPE_VALUE), minValue);
		}

		if (maxValue != null) {
			where.le(WztStringUtil.toCamelCase(MType.TYPE_VALUE), maxValue);
		}
		where.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED);
		List<MType> entityList
			= jdbcManager
				.from(MType.class)
				.where(where)
				.orderBy("displayOrder")
				.getResultList();

		if (entityList == null || entityList.size() == 0) {
			return new ArrayList<LabelValueDto>(0);
		}

		List<LabelValueDto> labelValueList = new ArrayList<LabelValueDto>(entityList.size() + 1);

		if (StringUtils.isNotEmpty(blankLineLabel)) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(blankLineLabel);
			dto.setValue("");
			labelValueList.add(dto);
		}

		for (MType entity : entityList) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(entity.typeName + suffix);
			dto.setValue(String.valueOf(entity.typeValue));
			dto.setSort(entity.displayOrder);
			labelValueList.add(dto);
		}

		return labelValueList;
	}


	/**
	 * タイプ値を指定して {@link LabelValueDto} のリストを作成します。
	 * @param typeCd タイプコード
	 * @param blankLineLabel 空ラベル
	 * @param suffix サフィックス
	 * @param typeValueList 値リスト
	 * @return @link LabelValueDto} のリスト
	 */
	public List<LabelValueDto> getSpecifiedTypeList(String typeCd, String blankLineLabel, String suffix, List<Integer> typeValueList) {

		final List<LabelValueDto> list = new ArrayList<LabelValueDto>();

		// 空ラベル
		if (StringUtils.isNotEmpty(blankLineLabel)) {
			list.add(LabelValueDto.createBlankLabelValueDto(blankLineLabel));
		}

		// 指定の値がなければリターン
		if (CollectionUtils.isEmpty(typeValueList)) {
			return list;
		}

		SimpleWhere where = new SimpleWhere();
		where.eq(WztStringUtil.toCamelCase(MType.TYPE_CD), typeCd)
			.in(WztStringUtil.toCamelCase(MType.TYPE_VALUE), typeValueList)
			.eq(WztStringUtil.toCamelCase(MType.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);


		final String suf;
		if (suffix == null) {
			suf = "";
		} else {
			suf = suffix;
		}

		jdbcManager.from(MType.class)
			.where(where)
			.orderBy(MType.DISPLAY_ORDER)
			.iterate(new IterationCallback<MType, Void>() {
				@Override
				public Void iterate(MType entity, IterationContext context) {
					if (entity != null) {
						list.add(new LabelValueDto(entity.typeName.concat(suf), entity.typeValue));
					}
					return null;
				}
			});

		return list;
	}



	/**
	 * エリアマスタからラベルとバリューのリストを取得します。
	 * @param blankLineLabel 初期ラベル
	 * @param suffix 接尾辞
	 * @param authLevel 権限
	 * @param noDisplayValue 非表示値
	 * @return List<LabelValueDto> ラベルと値を保持するDtoのリスト
	 */
	public List<LabelValueDto> getAreaList(String blankLineLabel, String suffix, String authLevel, List<Integer> noDisplayValue) {

		List<LabelValueDto> labelValueList = new ArrayList<LabelValueDto>();

		// 検索条件の設定
		SimpleWhere where = new SimpleWhere()
				.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED)
				.notIn("id", noDisplayValue.toArray());

		// 権限が代理店で指定がある場合
		if (!StringUtil.isEmpty(authLevel) && ManageAuthLevel.AGENCY.value().equals(authLevel)) {

			// 運営側から呼び出されたかをチェック
			if (isAdminDto()) {
				ServiceAdminAccessUser userDto = getAdminDto();

				try {
					// エリアコードを取得
					List<Integer> areaCdList;
					areaCdList = companyAreaService.getCompanyAreaCd(userDto.getCompanyId());

					// 検索条件にエリアコードを設定
					where.in(camelize(MArea.AREA_CD), areaCdList);

				// データが取得できない場合、検索を行わずに返却
				} catch (WNoResultException e) {

					if (StringUtils.isNotEmpty(blankLineLabel)) {
						LabelValueDto dto = new LabelValueDto();
						dto.setLabel(blankLineLabel);
						dto.setValue("");
						labelValueList.add(dto);
					}

					return labelValueList;
				}
			}
		}

		List<MArea> entityList
			= jdbcManager
				.from(MArea.class)
				.where(where)
				.orderBy("displayOrder")
				.getResultList();

		if (StringUtils.isNotEmpty(blankLineLabel)) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(blankLineLabel);
			dto.setValue("");
			labelValueList.add(dto);
		}

		for (MArea entity : entityList) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(entity.areaName + suffix);
			dto.setValue(String.valueOf(entity.areaCd));
			labelValueList.add(dto);
		}

		return labelValueList;
	}

	/**
	 * 号数マスタからラベルとバリューのリストを取得します。
	 * @param blankLineLabel 初期ラベル
	 * @param suffix 接尾辞
	 * @param noDisplayValue 非表示値
	 * @param limitValue 限定値
	 * @param authLevel 権限
	 * @return List<LabelValueDto> ラベルと値を保持するDtoのリスト
	 */
	public List<LabelValueDto> getVolumeList(String blankLineLabel, String suffix, List<Integer> noDisplayValue, String limitValue, String authLevel) {

		List<MVolume> entityList = new ArrayList<MVolume>();

		// エリアが指定されている場合のみ、設定する
		if (StringUtils.isNotEmpty(limitValue)) {

			try {
				// エリアコードが正しいかどうかチェック
				Integer.parseInt(limitValue);

				SimpleWhere where = new SimpleWhere()
						.eq("areaCd", limitValue)
						.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED)
						.notIn("id", noDisplayValue.toArray());

				// 権限が管理者以外は締切日時移行のデータが選択可能
				if (!StringUtil.isEmpty(authLevel) && !ManageAuthLevel.ADMIN.value().equals(authLevel) && !ManageAuthLevel.SALES.value().equals(authLevel)) {
					where.ge("deadlineDatetime", DateUtils.getJustDateTime());
				}

				entityList = jdbcManager
				.from(MVolume.class)
				.where(where)
				.orderBy("volume DESC")
				.getResultList();

			// 値がエリアコードでない場合は処理を行わない
			} catch (NumberFormatException e) {
				// 検索実行なし
			}
		}

		List<LabelValueDto> labelValueList = new ArrayList<LabelValueDto>();

		if (StringUtils.isNotEmpty(blankLineLabel)) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(blankLineLabel);
			dto.setValue("");
			labelValueList.add(dto);
		}

		for (MVolume entity : entityList) {
			LabelValueDto dto = new LabelValueDto();

			dto.setLabel(entity.volume + " : " + DateUtils.getDateStr(entity.postStartDatetime, "yyyy/MM/dd HH:mm")
					+  " ～ " + DateUtils.getDateStr(entity.postEndDatetime, "yyyy/MM/dd HH:mm") + suffix);
			dto.setValue(String.valueOf(entity.id));
			labelValueList.add(dto);
		}

		return labelValueList;
	}

	/**
	 * 号数マスタからラベルとバリューのリストを取得します。
	 * @param blankLineLabel 初期ラベル
	 * @param suffix 接尾辞
	 * @param noDisplayValue 非表示値
	 * @param limitValue 限定値
	 * @param authLevel 権限
	 * @return List<LabelValueDto> ラベルと値を保持するDtoのリスト
	 */
	public List<LabelValueDto> getSimpleVolumeList(String blankLineLabel, String suffix, List<Integer> noDisplayValue, String limitValue, String authLevel) {

		List<MVolume> entityList = new ArrayList<MVolume>();

		SimpleWhere where = new SimpleWhere()
				.eq("areaCd", limitValue)
				.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED)
				.notIn("id", noDisplayValue.toArray());

		// 権限が管理者以外は締切日時移行のデータが選択可能
		if (!StringUtil.isEmpty(authLevel) && !ManageAuthLevel.ADMIN.value().equals(authLevel)) {
			where.ge("deadlineDatetime", DateUtils.getJustDateTime());
		}

		// エリアが指定されている場合のみ、設定する
		if (StringUtils.isNotEmpty(limitValue)) {
			entityList = jdbcManager
			.from(MVolume.class)
			.where(where)
			.orderBy("volume DESC")
			.getResultList();
		}

		List<LabelValueDto> labelValueList = new ArrayList<LabelValueDto>();

		if (StringUtils.isNotEmpty(blankLineLabel)) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(blankLineLabel);
			dto.setValue("");
			labelValueList.add(dto);
		}

		for (MVolume entity : entityList) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(entity.volume + suffix);
			dto.setValue(String.valueOf(entity.id));
			labelValueList.add(dto);
		}

		return labelValueList;
	}

	/**
	 * 特集マスタからラベルとバリューのリストを取得します。
	 * @param blankLineLabel 初期ラベル
	 * @param suffix 接尾辞
	 * @param noDisplayValue 非表示値
	 * @return List<LabelValueDto> ラベルと値を保持するDtoのリスト
	 */
    public List<LabelValueDto> getSpecialList(String blankLineLabel, String suffix, List<Integer> noDisplayValue, String limitValue) {

		StringBuilder sqlStr = new StringBuilder();
		List<Object> params = new ArrayList<Object>();

		sqlStr.append("SELECT m_special.* FROM m_special JOIN m_special_display ON m_special_display.special_id = m_special.id AND m_special_display.area_cd = ?");
		params.add(Integer.parseInt(limitValue));
		sqlStr.append(" WHERE m_special.delete_flg = ?");
		params.add(DeleteFlgKbn.NOT_DELETED);
		sqlStr.append(" AND m_special.post_end_datetime > ?");
		params.add(new Date());
		if (CollectionUtils.isNotEmpty(noDisplayValue)) {
			sqlStr.append(SqlUtils.andNotInNoCamelize("id", noDisplayValue.size()));
			params.addAll(noDisplayValue);
		}

		sqlStr.append(" ORDER BY m_special.post_start_datetime, m_special.post_end_datetime DESC, m_special,id");
		List<MSpecial> entityList = jdbcManager.selectBySql(MSpecial.class, sqlStr.toString(), params.toArray()).getResultList();

		List<LabelValueDto> labelValueList = new ArrayList<LabelValueDto>();

		if (StringUtils.isNotEmpty(blankLineLabel)) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(blankLineLabel);
			dto.setValue("");
			labelValueList.add(dto);
		}

		for (MSpecial entity : entityList) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(entity.specialName + suffix);
			dto.setValue(String.valueOf(entity.id));
			labelValueList.add(dto);
		}

		return labelValueList;
	}

	/**
	 * 特集マスタから表示ラベルとバリューのリストを取得します。
	 * @param blankLineLabel 初期ラベル
	 * @param suffix 接尾辞
	 * @param noDisplayValue 非表示値
	 * @return List<LabelValueDto> ラベルと値を保持するDtoのリスト
	 */
	public List<LabelValueDto> getSpecialDisplayList(String blankLineLabel, String suffix, List<Integer> noDisplayValue) {
		List<MSpecial> entityList
			= jdbcManager
				.from(MSpecial.class)
				.where(new SimpleWhere()
				.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED)
				.notIn("id", noDisplayValue.toArray()))
				.orderBy(desc("postStartDatetime"), desc("postEndDatetime"), asc("id"))
				.getResultList();

		List<LabelValueDto> labelValueList = new ArrayList<LabelValueDto>();

		if (StringUtils.isNotEmpty(blankLineLabel)) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(blankLineLabel);
			dto.setValue("");
			labelValueList.add(dto);
		}

		for (MSpecial entity : entityList) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(entity.displayName + suffix);
			dto.setValue(String.valueOf(entity.id));
			labelValueList.add(dto);
		}

		return labelValueList;
	}

	/**
	 * 会社マスタからラベルとバリューのリストを取得します。
	 * @param blankLineLabel 初期ラベル
	 * @param suffix 接尾辞
	 * @param noDisplayValue 非表示値
	 * @return List<LabelValueDto> ラベルと値を保持するDtoのリスト
	 */
	public List<LabelValueDto> getCompanyList(String blankLineLabel, String suffix, List<Integer> noDisplayValue) {

		// 検索条件の設定
		SimpleWhere where = new SimpleWhere();

		// 運営側から呼び出された場合
		if (isAdminDto()) {
			ServiceAdminAccessUser userDto = getAdminDto();

			// 権限が代理店の場合
			if(ManageAuthLevel.AGENCY.value().equals(userDto.getAuthLevel())) {
				where.eq(StringUtil.camelize(MCompany.ID), userDto.getCompanyId());
			}
		}
		where.notIn("id", noDisplayValue.toArray());
		where.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED);

		List<MCompany> entityList
			= jdbcManager
				.from(MCompany.class)
				.where(where)
				.orderBy(desc("displayOrder"),asc("companyNameKana"),asc("companyName"))
				.getResultList();

		List<LabelValueDto> labelValueList = new ArrayList<LabelValueDto>();

		if (StringUtils.isNotEmpty(blankLineLabel)) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(blankLineLabel);
			dto.setValue("");
			labelValueList.add(dto);
		}

		for (MCompany entity : entityList) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(entity.companyName + suffix);
			dto.setValue(String.valueOf(entity.id));
			labelValueList.add(dto);
		}

		return labelValueList;
	}

	/**
	 * 会社マスタからラベルとバリューのリストを取得します。
	 * @param blankLineLabel 初期ラベル
	 * @param suffix 接尾辞
	 * @param noDisplayValue 非表示値
	 * @param limitValue 限定値
	 * @return List<LabelValueDto> ラベルと値を保持するDtoのリスト
	 */
	public List<LabelValueDto> getAssignedCompanyList(String blankLineLabel, String suffix, List<Integer> noDisplayValue,
			String limitValue, String authLevel, String companyId) {

		StringBuilder sqlStr = new StringBuilder();
		List<Object> params = new ArrayList<Object>();

		sqlStr.append("SELECT c.id, c.company_name, c.display_order  FROM m_company c INNER JOIN m_company_area ca ON c.id = ca.company_id ");
		sqlStr.append("WHERE c.delete_flg = ? AND ca.delete_flg = ? ");


		params.add(DeleteFlgKbn.NOT_DELETED);
		params.add(DeleteFlgKbn.NOT_DELETED);

		if (StringUtils.isNotBlank(limitValue)) {
			sqlStr.append("AND ca.area_cd = ? ");
			params.add(NumberUtils.toInt(limitValue));
		}

		// 運営側から呼び出された場合
		if (isAdminDto()) {
			ServiceAdminAccessUser userDto = getAdminDto();
			// 権限が代理店の場合、会社IDを自身のIDにする
			if(ManageAuthLevel.AGENCY.value().equals(userDto.getAuthLevel())) {
				companyId = userDto.getCompanyId();
			}
		}

		if (StringUtils.isNotBlank(companyId)) {

			sqlStr.append("AND c.id = ? ");
			params.add(NumberUtils.toInt(companyId));
		}

		if (ManageAuthLevel.STAFF.value().equals(authLevel)) {
			sqlStr.append("AND c.agency_flg = ? ");
			params.add(MCompany.AgencyFlgValue.NOT_AGENCY);
		}

		if (noDisplayValue.size() > 0) {
			sqlStr.append("AND id NOT IN (" + SqlUtils.getQMarks(noDisplayValue.size()) + "");
			for (int i : noDisplayValue) {
				params.add(i);
			}
		}

		sqlStr.append("GROUP BY c.id, c.company_name, c.display_order, c.company_name_kana ");
		sqlStr.append("ORDER BY c.display_order DESC, c.company_name_kana, c.company_name");

		List<MCompany> entityList = jdbcManager.selectBySql(MCompany.class, sqlStr.toString(), params.toArray()).getResultList();

		List<LabelValueDto> labelValueList = new ArrayList<LabelValueDto>();

		if (StringUtils.isNotEmpty(blankLineLabel)) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(blankLineLabel);
			dto.setValue("");
			labelValueList.add(dto);
		}

		for (MCompany entity : entityList) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(entity.companyName + suffix);
			dto.setValue(String.valueOf(entity.id));
			labelValueList.add(dto);
		}

		return labelValueList;
	}

	/**
	 * 営業担当者マスタからラベルとバリューのリストを取得します。
	 * @param blankLineLabel 初期ラベル
	 * @param suffix 接尾辞
	 * @param noDisplayValue 非表示値
	 * @param limitValue 限定値
	 * @return List<LabelValueDto> ラベルと値を保持するDtoのリスト
	 */
	public List<LabelValueDto> getAssignedSalesList(String blankLineLabel, String suffix, List<Integer> noDisplayValue, String limitValue) {

		List<MSales> entityList = new ArrayList<MSales>();

		// 会社IDが設定されている場合のみ実行
		if (!StringUtil.isEmpty(limitValue)) {

			// 運営側から呼び出された場合
			if (isAdminDto()) {
				ServiceAdminAccessUser userDto = getAdminDto();
				// 権限が代理店の場合、会社IDを自身のIDにする
				if(ManageAuthLevel.AGENCY.value().equals(userDto.getAuthLevel())) {
					limitValue =  userDto.getCompanyId();
				}
			}

			entityList
				= jdbcManager
					.from(MSales.class)
					.where(new SimpleWhere()
					.eq("companyId", limitValue)
					.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED)
					.notIn("id", noDisplayValue.toArray()))
					.orderBy(desc("displayOrder"), desc("companyId"), asc("authorityCd"), asc("salesNameKana"), asc("salesNameKana"))
					.getResultList();

		}

		List<LabelValueDto> labelValueList = new ArrayList<LabelValueDto>();

		if (StringUtils.isNotEmpty(blankLineLabel)) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(blankLineLabel);
			dto.setValue("");
			labelValueList.add(dto);
		}

		for (MSales entity : entityList) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(entity.salesName + suffix);
			dto.setValue(String.valueOf(entity.id));
			labelValueList.add(dto);
		}

		return labelValueList;
	}

	/**
	 * 営業担当者マスタからラベルとバリューのリストを取得します。
	 * @param blankLineLabel 初期ラベル
	 * @param suffix 接尾辞
	 * @param noDisplayValue 非表示値
	 * @return List<LabelValueDto> ラベルと値を保持するDtoのリスト
	 */
	public List<LabelValueDto> getSalesList(String blankLineLabel, String suffix, List<Integer> noDisplayValue) {

		// 検索条件の設定
		SimpleWhere where = new SimpleWhere();

		// 運営側から呼び出された場合
		if (isAdminDto()) {
			ServiceAdminAccessUser userDto = getAdminDto();

			// 権限が代理店の場合
			if(ManageAuthLevel.AGENCY.value().equals(userDto.getAuthLevel())) {
				where.eq(StringUtil.camelize(MSales.COMPANY_ID), userDto.getCompanyId());
			}
		}
		where.notIn("id", noDisplayValue.toArray());
		where.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED);

		List<MSales> entityList
			= jdbcManager
				.from(MSales.class)
				.where(where)
				.orderBy(desc("displayOrder"), asc("companyId"), asc("authorityCd"), asc("salesNameKana"), asc("salesName"))
				.getResultList();

		List<LabelValueDto> labelValueList = new ArrayList<LabelValueDto>();

		if (StringUtils.isNotEmpty(blankLineLabel)) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(blankLineLabel);
			dto.setValue("");
			labelValueList.add(dto);
		}

		for (MSales entity : entityList) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(entity.salesName + suffix);
			dto.setValue(String.valueOf(entity.id));
			labelValueList.add(dto);
		}

		return labelValueList;
	}

	/**
	 * 都道府県マスタからラベルとバリューのリストを取得します。
	 * @param blankLineLabel 初期ラベル
	 * @param suffix 接尾辞
	 * @param noDisplayValue 非表示値
	 * @return List<LabelValueDto> ラベルと値を保持するDtoのリスト
	 */
	public List<LabelValueDto> getPrefecturesList(String blankLineLabel, String suffix, List<Integer> noDisplayValue) {
		List<MPrefectures> entityList
			= jdbcManager
				.from(MPrefectures.class)
				.where(new SimpleWhere()
				.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED)
				.notIn("prefecturesCd", noDisplayValue.toArray()))
				.orderBy("displayOrder")
				.getResultList();

		List<LabelValueDto> labelValueList = new ArrayList<LabelValueDto>();

		if (StringUtils.isNotEmpty(blankLineLabel)) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(blankLineLabel);
			dto.setValue("");
			labelValueList.add(dto);
		}

		for (MPrefectures entity : entityList) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(entity.prefecturesName + suffix);
			dto.setValue(String.valueOf(entity.prefecturesCd));
			labelValueList.add(dto);
		}

		return labelValueList;
	}

	/**
	 * 鉄道会社マスタからラベルとバリューのリストを取得します。
	 * @param blankLineLabel 初期ラベル
	 * @param suffix 接尾辞
	 * @param noDisplayValue 非表示値
	 * @param limitValue 限定値
	 * @return List<LabelValueDto> ラベルと値を保持するDtoのリスト
	 */
	@Deprecated
	public List<LabelValueDto> getRailroadList(String blankLineLabel, String suffix, List<Integer> noDisplayValue, String limitValue) {

		List<MRailroad> entityList = new ArrayList<MRailroad>();

		// エリアが指定されている場合のみ実行
		if (StringUtils.isNotEmpty(limitValue)) {
			entityList = jdbcManager
			.from(MRailroad.class)
			.where(new SimpleWhere()
			.eq("areaCd", limitValue)
			.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED)
			.notIn("id", noDisplayValue.toArray()))
			.orderBy("displayOrder")
			.getResultList();
		}

		List<LabelValueDto> labelValueList = new ArrayList<LabelValueDto>();

		if (StringUtils.isNotEmpty(blankLineLabel)) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(blankLineLabel);
			dto.setValue("");
			labelValueList.add(dto);
		}

		for (MRailroad entity : entityList) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(entity.railroadName + suffix);
			dto.setValue(String.valueOf(entity.id));
			labelValueList.add(dto);
		}

		return labelValueList;
	}

	/**
	 * 鉄道会社マスタから全データのラベルとバリューのリストを取得します。
	 * @param blankLineLabel 初期ラベル
	 * @param suffix 接尾辞
	 * @param noDisplayValue 非表示値
	 * @return List<LabelValueDto> ラベルと値を保持するDtoのリスト
	 */
	@Deprecated
	public List<LabelValueDto> getRailroadAllList(String blankLineLabel, String suffix, List<Integer> noDisplayValue) {

		List<MRailroad> entityList
			= jdbcManager
				.from(MRailroad.class)
				.where(new SimpleWhere()
				.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED)
				.notIn("id", noDisplayValue.toArray()))
				.orderBy("displayOrder")
				.getResultList();

		List<LabelValueDto> labelValueList = new ArrayList<LabelValueDto>();

		if (StringUtils.isNotEmpty(blankLineLabel)) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(blankLineLabel);
			dto.setValue("");
			labelValueList.add(dto);
		}

		for (MRailroad entity : entityList) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(entity.railroadName + suffix);
			dto.setValue(String.valueOf(entity.id));
			labelValueList.add(dto);
		}

		return labelValueList;
	}

	/**
	 * 路線マスタからラベルとバリューのリストを取得します。
	 * @param blankLineLabel 初期ラベル
	 * @param suffix 接尾辞
	 * @param noDisplayValue 非表示値
	 * @param limitValue 限定値
	 * @return List<LabelValueDto> ラベルと値を保持するDtoのリスト
	 */
	@Deprecated
	public List<LabelValueDto> getRouteList(String blankLineLabel, String suffix, List<Integer> noDisplayValue, String limitValue) {

		List<MRoute> entityList = new ArrayList<MRoute>();

		// 鉄道会社が指定されている場合のみ実行
		if (StringUtils.isNotBlank(limitValue)) {

			// 数値でない場合は選択不可
			if (!StringUtils.isNumeric(limitValue)) {
				List<LabelValueDto> labelValueList = new ArrayList<LabelValueDto>();
				LabelValueDto dto = new LabelValueDto();
				dto.setLabel("お使いの環境ではご利用できません");
				dto.setValue("");
				labelValueList.add(dto);
				return labelValueList;
			}


			entityList = jdbcManager
			.from(MRoute.class)
			.where(new SimpleWhere()
			.eq("railroadId", limitValue)
			.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED)
			.notIn("id", noDisplayValue.toArray()))
			.orderBy("displayOrder")
			.getResultList();
		}

		List<LabelValueDto> labelValueList = new ArrayList<LabelValueDto>();

		if (StringUtils.isNotEmpty(blankLineLabel)) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(blankLineLabel);
			dto.setValue("");
			labelValueList.add(dto);
		}

		for (MRoute entity : entityList) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(entity.routeName + suffix);
			dto.setValue(String.valueOf(entity.id));
			labelValueList.add(dto);
		}

		return labelValueList;
	}

	/**
	 * 路線マスタから全データのラベルとバリューのリストを取得します。
	 * @param blankLineLabel 初期ラベル
	 * @param suffix 接尾辞
	 * @param noDisplayValue 非表示値
	 * @return List<LabelValueDto> ラベルと値を保持するDtoのリスト
	 */
	@Deprecated
	public List<LabelValueDto> getRouteAllList(String blankLineLabel, String suffix, List<Integer> noDisplayValue) {

		List<MRoute> entityList
			= jdbcManager
				.from(MRoute.class)
				.where(new SimpleWhere()
				.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED)
				.notIn("id", noDisplayValue.toArray()))
				.orderBy("displayOrder")
					.getResultList();

		List<LabelValueDto> labelValueList = new ArrayList<LabelValueDto>();

		if (StringUtils.isNotEmpty(blankLineLabel)) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(blankLineLabel);
			dto.setValue("");
			labelValueList.add(dto);
		}

		for (MRoute entity : entityList) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(entity.routeName + suffix);
			dto.setValue(String.valueOf(entity.id));
			labelValueList.add(dto);
		}

		return labelValueList;
	}

	/**
	 * 駅マスタからラベルとバリューのリストを取得します。
	 * @param blankLineLabel 初期ラベル
	 * @param suffix 接尾辞
	 * @param noDisplayValue 非表示値
	 * @param limitValue 限定値
	 * @return List<LabelValueDto> ラベルと値を保持するDtoのリスト
	 */
	@Deprecated
	public List<LabelValueDto> getStationList(String blankLineLabel, String suffix, List<Integer> noDisplayValue, String limitValue) {

		List<MStation> entityList = new ArrayList<MStation>();

		// 路線が指定されている場合のみ実行
		if (StringUtils.isNotBlank(limitValue)) {

			// 数値でない場合は選択不可
			if (!StringUtils.isNumeric(limitValue)) {
				List<LabelValueDto> labelValueList = new ArrayList<LabelValueDto>();
				LabelValueDto dto = new LabelValueDto();
				dto.setLabel("お使いの環境ではご利用できません");
				dto.setValue("");
				labelValueList.add(dto);
				return labelValueList;
			}

			entityList = jdbcManager
			.from(MStation.class)
			.where(new SimpleWhere()
			.eq("routeId", limitValue)
			.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED)
			.notIn("id", noDisplayValue.toArray()))
			.orderBy("displayOrder")
			.getResultList();
		}

		List<LabelValueDto> labelValueList = new ArrayList<LabelValueDto>();

		if (StringUtils.isNotEmpty(blankLineLabel)) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(blankLineLabel);
			dto.setValue("");
			labelValueList.add(dto);
		}

		for (MStation entity : entityList) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(entity.stationName + suffix);
			dto.setValue(String.valueOf(entity.id));
			labelValueList.add(dto);
		}

		return labelValueList;
	}

	/**
	 * 駅マスタから全データのラベルとバリューのリストを取得します。
	 * @param blankLineLabel 初期ラベル
	 * @param suffix 接尾辞
	 * @param noDisplayValue 非表示値
	 * @return List<LabelValueDto> ラベルと値を保持するDtoのリスト
	 */
	@Deprecated
	public List<LabelValueDto> getStationAllList(String blankLineLabel, String suffix, List<Integer> noDisplayValue) {

		List<MStation> entityList
			= jdbcManager
				.from(MStation.class)
				.where(new SimpleWhere()
				.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED)
				.notIn("id", noDisplayValue.toArray()))
				.orderBy("displayOrder")
				.getResultList();

		List<LabelValueDto> labelValueList = new ArrayList<LabelValueDto>();

		if (StringUtils.isNotEmpty(blankLineLabel)) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(blankLineLabel);
			dto.setValue("");
			labelValueList.add(dto);
		}

		for (MStation entity : entityList) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(entity.stationName + suffix);
			dto.setValue(String.valueOf(entity.id));
			labelValueList.add(dto);
		}

		return labelValueList;
	}

	/**
	 * リニューアル後の鉄道会社マスタからラベルとバリューのリストを取得します。
	 * @param blankLineLabel 初期ラベル
	 * @param suffix 接尾辞
	 * @param noDisplayValue 非表示値
	 * @param prefCd 都道府県
	 * @return List<LabelValueDto> ラベルと値を保持するDtoのリスト
	 */
	public List<LabelValueDto> getRRailroadList(String blankLineLabel, String suffix, List<Integer> noDisplayValue, String prefCd) {

		List<CdNameDto> list = new ArrayList<>();
		// 都道府県が指定されている場合のみ実行
		if (StringUtils.isNotEmpty(prefCd)) {
			list = stationLogic.getRailroadCdName(Integer.parseInt(prefCd));
		}
		return convertCdNameDto(list, blankLineLabel, suffix, noDisplayValue);
	}

	/**
	 * リニューアル後の路線マスタからラベルとバリューのリストを取得します。
	 * @param blankLineLabel 初期ラベル
	 * @param suffix 接尾辞
	 * @param noDisplayValue 非表示値
	 * @param companyCd 鉄道事業者コード
	 * @return List<LabelValueDto> ラベルと値を保持するDtoのリスト
	 */
	public List<LabelValueDto> getRRouteList(String blankLineLabel, String suffix, List<Integer> noDisplayValue, String companyCd) {

		List<CdNameDto> list = new ArrayList<>();
		// 都道府県が指定されている場合のみ実行
		if (StringUtils.isNotEmpty(companyCd)) {
			list = stationLogic.getRouteCdName(Integer.parseInt(companyCd));
		}
		return convertCdNameDto(list, blankLineLabel, suffix, noDisplayValue);
	}


	/**
	 * リニューアル後の駅マスタからラベルとバリューのリストを取得します。
	 * @param blankLineLabel 初期ラベル
	 * @param suffix 接尾辞
	 * @param noDisplayValue 非表示値
	 * @param companyCd 鉄道事業者コード
	 * @return List<LabelValueDto> ラベルと値を保持するDtoのリスト
	 */
	public List<LabelValueDto> getRStationList(String blankLineLabel, String suffix, List<Integer> noDisplayValue, String lineCd) {

		List<CdNameDto> list = new ArrayList<>();
		// 都道府県が指定されている場合のみ実行
		if (StringUtils.isNotEmpty(lineCd)) {
			list = stationLogic.getStationCdName(Integer.parseInt(lineCd));
		}
		return convertCdNameDto(list, blankLineLabel, suffix, noDisplayValue);
	}

	/**
	 * CdNameDtoをLabelValueDtoに変換
	 * @param list 変換前のCｄNameDtoのリスト
	 * @param blankLineLabel
	 * @param suffix
	 * @param noDisplayValue
	 * @return
	 */
	private List<LabelValueDto> convertCdNameDto(List<CdNameDto> list, String blankLineLabel, String suffix, List<Integer> noDisplayValue) {

		List<LabelValueDto> labelValueList = new ArrayList<LabelValueDto>();

		if (StringUtils.isNotEmpty(blankLineLabel)) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(blankLineLabel);
			dto.setValue("");
			labelValueList.add(dto);
		}

		for (CdNameDto cdNameDto : list) {
			if (noDisplayValue.contains(cdNameDto.cd)) {
				continue;
			}
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(cdNameDto.name + suffix);
			dto.setValue(String.valueOf(cdNameDto.cd));
			labelValueList.add(dto);
		}
		return labelValueList;
	}

	/**
	 * 市区町村のリストを取得する
	 * @param blankLineLabel 初期ラベル
	 * @param suffix 接尾辞
	 * @param noDisplayValue 非表示値
	 * @param prefecturesCd 都道府県コード
	 * @return List<LabelValueDto> ラベルと値を保持するDtoのリスト
	 */
	public List<LabelValueDto> getCityList(String blankLineLabel, String suffix, List<Integer> noDisplayValue, String prefecturesCd) {

		List<MCity> entityList = new ArrayList<>();
		try {
			entityList = cityService.findByPrefecturesCd(Integer.parseInt(prefecturesCd));
		} catch (NumberFormatException | WNoResultException e) {}

		List<LabelValueDto> labelValueList = new ArrayList<LabelValueDto>();

		if (StringUtils.isNotEmpty(blankLineLabel)) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(blankLineLabel);
			dto.setValue("");
			labelValueList.add(dto);
		}

		for (MCity entity : entityList) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(entity.cityName + suffix);
			dto.setValue(String.valueOf(entity.cityCd));
			labelValueList.add(dto);
		}

		return labelValueList;
	}

	/**
	 * 権限レベルリストを取得します。
	 * @return 権限レベルリスト
	 */
	public List<LabelValueDto> getAuthLevelList() {
		List<LabelValueDto> labelValueList = new ArrayList<LabelValueDto>();

		Set<String> keyset = MSales.AUTH_LEVEL_MAP.keySet();
		for (String key : keyset) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(MSales.AUTH_LEVEL_MAP.get(key));
			dto.setValue(key);
			labelValueList.add(dto);
		}

		return labelValueList;
	}

	/**
	 * 最大表示件数の文字列を<br />
	 * ラベルとバリューが格納されたリストにして返します。
	 * @param value 対象の文字列
	 * @param suffix 接尾辞
	 * @return List<LabelValueDto> 変換したリスト
	 */
	public List<LabelValueDto> getMaxRowList(String value, String suffix) {
		return getMaxRowList(value, suffix, null);
	}

	/**
	 * 最大表示件数の文字列を<br />
	 * ラベルとバリューが格納されたリストにして返します。
	 * @param value 対象の文字列
	 * @param suffix 接尾辞
	 * @param allLabel 全件用ラベル
	 * @return List<LabelValueDto> 変換したリスト
	 */
	public List<LabelValueDto> getMaxRowList(String value, String suffix, String allLabel) {
		// 値が取得できない場合は、初期値で表示
		if (StringUtil.isEmpty(value)) {
			value = GourmetCareeConstants.DEFAULT_MAX_ROW;
		}

		// 取得した値を、カンマで分割
		String[] maxRowAry = value.split(",");

		// 表示件数の切り替え
		List<LabelValueDto> maxRowList = new ArrayList<LabelValueDto>(maxRowAry.length);

		// カンマで区切られた値分、リストを作成
		for (String maxRow : maxRowAry) {

			LabelValueDto labelDto = new LabelValueDto();

			// 接尾辞を設定
			labelDto.setLabel(maxRow + suffix);

			try {
				// 値が数値かどうかチェック
				Integer.parseInt(maxRow);
				labelDto.setValue(maxRow);

				// 数値で無い場合は、valueにブランクをセット（"全"用）
			} catch (NumberFormatException e) {
				labelDto.setValue("");
			}
			maxRowList.add(labelDto);
		}

		if (StringUtils.isNotBlank(allLabel)) {
			maxRowList.add(LabelValueDto.createBlankLabelValueDto(allLabel));
		}

		return maxRowList;
	}

	/**
	 * ステータスマスタからプルダウンを取得します。
	 * @param statusKbn ステータス区分
	 * @param blankLineLabel 初期ラベル
	 * @param suffix 接尾辞
	 * @param noDisplayValue 非表示値
	 * @return List<LabelValueDto> ラベルと値を保持するDtoのリスト
	 */
	public List<LabelValueDto> getStatusList(String statusKbn, String statusCd, String blankLineLabel, String suffix, List<Integer> noDisplayValue) {

		// 検索
		List<MStatus> entityList;
		try {
			entityList = statusService.getStatusList(statusKbn, statusCd, noDisplayValue);

		// 取得できなければエラー
		} catch (WNoResultException e) {
			throw new FraudulentProcessException("画面表示で不正な操作が行われました。");
		}

		List<LabelValueDto> labelValueList = new ArrayList<LabelValueDto>();

		if (StringUtils.isNotEmpty(blankLineLabel)) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(blankLineLabel);
			dto.setValue("");
			labelValueList.add(dto);
		}

		for (MStatus entity : entityList) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(entity.statusName + suffix);
			dto.setValue(String.valueOf(entity.statusCd));
			labelValueList.add(dto);
		}

		return labelValueList;
	}

	public List<LabelValueDto> getAdvancedRegistrationYearList() {
		BeanMap result =
				jdbcManager.selectBySql(
						BeanMap.class,
						"SELECT MAX(year) AS max_year, MIN(year) AS min_year FROM m_advanced_registration_year;"
						)
						.getSingleResult();

		int maxYear;
		int minYear;
		int defaultYear = NumberUtils.toInt(DateUtils.getDateStr(new Date(), "yyyy"));
		if (result == null || result.isEmpty()) {
			maxYear = defaultYear;
			minYear = defaultYear;
		} else {
			maxYear = GourmetCareeUtil.toObjectToInt(result.get("maxYear"), defaultYear);
			minYear = GourmetCareeUtil.toObjectToInt(result.get("minYear"), defaultYear);
		}

		List<LabelValueDto> dtoList = new ArrayList<LabelValueDto>();
		for (int i = minYear; i <= maxYear; i++) {
			LabelValueDto dto = new LabelValueDto();
			String year = String.valueOf(i);
			dto.setLabel(year);
			dto.setValue(year);
			dtoList.add(dto);
		}
		return dtoList;
	}

	/**
	 * 定型文マスタからラベルとバリューのリストを取得します。
	 * @param blankLineLabel 初期ラベル
	 * @param limitValue 限定値
	 * @return List<LabelValueDto> ラベルと値を保持するDtoのリスト
	 */
	public List<LabelValueDto> getSentenceList(String blankLineLabel, String limitValue) {

		List<MSentence> entityList = new ArrayList<MSentence>();

		// 顧客IDが設定されている場合のみ実行
		if (!StringUtil.isEmpty(limitValue)) {
			entityList
				= jdbcManager
					.from(MSentence.class)
					.where(new SimpleWhere()
					.eq("customerId", limitValue)
					.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
					.orderBy(desc("registrationDatetime"), desc("id"))
					.getResultList();

		}

		List<LabelValueDto> labelValueList = new ArrayList<LabelValueDto>();

		if (StringUtils.isNotEmpty(blankLineLabel)) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(blankLineLabel);
			dto.setValue("");
			labelValueList.add(dto);
		}

		for (MSentence entity : entityList) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(entity.sentenceTitle);
			dto.setValue(String.valueOf(entity.id));
			labelValueList.add(dto);
		}

		return labelValueList;
	}



	/**
	 * 事前登録リストを作成します。
	 * @param blankLineLabel 初期ラベル
	 * @return 事前登録リスト
	 */
	public List<LabelValueDto> getAdvancedRegistrationList(String blankLineLabel, final int nameKbn) {
		SimpleWhere where = new SimpleWhere();
		where.eq(WztStringUtil.toCamelCase(AbstractCommonEntity.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);
		List<LabelValueDto> dtoList =
				jdbcManager.from(MAdvancedRegistration.class)
					.where(where)
					.orderBy(MAdvancedRegistration.DISP_ORDER)
					.iterate(new IterationCallback<MAdvancedRegistration, List<LabelValueDto>>() {
						List<LabelValueDto> list = new ArrayList<LabelValueDto>(0);
						@Override
						public List<LabelValueDto> iterate(MAdvancedRegistration entity, IterationContext context) {
							if (entity != null) {
								LabelValueDto dto = new LabelValueDto();

								if (nameKbn == MAdvancedRegistration.NameKbn.SHORT_NAME) {
									dto.setLabel(entity.advancedRegistrationShortName);
								} else {
									dto.setLabel(entity.advancedRegistrationName);
								}

								dto.setValue(String.valueOf(entity.id));
								list.add(dto);
							}
							return list;
						}
					});

		// 初期ラベルがある場合は先頭に挿入
		if (StringUtils.isNotBlank(blankLineLabel)) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(blankLineLabel);
			dto.setValue("");
			dtoList.add(0, dto);
		}

		return dtoList;
	}

	/**
	 * 区分マスタから全エリアのプルダウンを取得します。
	 * @param blankLineLabel 初期ラベル
	 * @param suffix 接尾辞
	 * @param noDisplayValue 非表示値
	 */
	public List<LabelValueDto> getAllAreaList(String blankLineLabel, String suffix, List<Integer> noDisplayValue) {
		// 首都圏WEBエリア・海外エリア・仙台エリア
		// TODO 仙台が表示されるようになったら条件分岐は消す。
		Object[] allArea;

		if (GourmetCareeFunctions.isInvisibleArea()) {
			allArea = new Object[]{MTypeConstants.ShutokenWebAreaKbn.TYPE_CD,
					MTypeConstants.ShutokenForeignAreaKbn.TYPE_CD,
					};
		} else {
			allArea = new Object[]{MTypeConstants.ShutokenWebAreaKbn.TYPE_CD,
					MTypeConstants.ShutokenForeignAreaKbn.TYPE_CD,
					MTypeConstants.SendaiWebAreaKbn.TYPE_CD};
		}

		List<MType> entityList
			= jdbcManager
				.from(MType.class)
				.where(new SimpleWhere()
				.in("typeCd", allArea)
				.notIn("typeValue", noDisplayValue.toArray())
				.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
				.orderBy("typeCd DESC, displayOrder")
				.getResultList();

		if (entityList == null || entityList.size() == 0) {
			return new ArrayList<LabelValueDto>(0);
		}

		List<LabelValueDto> labelValueList = new ArrayList<LabelValueDto>(entityList.size() + 1);

		if (StringUtils.isNotEmpty(blankLineLabel)) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(blankLineLabel);
			dto.setValue("");
			labelValueList.add(dto);
		}

		for (MType entity : entityList) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(entity.typeName + suffix);
			dto.setValue(String.valueOf(entity.typeValue));
			labelValueList.add(dto);
		}

		return labelValueList;
	}

	/**
	 * 区分マスタから全エリアのプルダウンを取得します。
	 * @param blankLineLabel 初期ラベル
	 * @param suffix 接尾辞
	 * @param noDisplayValue 非表示値
	 */
	public List<LabelValueDto> getAllAreaListForFront(String blankLineLabel, String suffix, List<Integer> noDisplayValue) {
		// 首都圏WEBエリア・海外エリア・仙台エリア
		// TODO 仙台が表示されるようになったら条件分岐は消す。
		Object[] allArea;

		if (GourmetCareeFunctions.isInvisibleArea()) {
			allArea = new Object[]{MTypeConstants.ShutokenWebAreaKbn.TYPE_CD,
					MTypeConstants.ShutokenForeignAreaKbn.TYPE_CD,
					};
		} else {
			allArea = new Object[]{MTypeConstants.ShutokenWebAreaKbn.TYPE_CD,
					MTypeConstants.SendaiWebAreaKbn.TYPE_CD,
					MTypeConstants.ShutokenForeignAreaKbn.TYPE_CD,
			};
		}

		/* ソート順生成 */
		StringBuffer sb = new StringBuffer(0);
		for (Object obj : allArea) {
			sb.append(MType.TYPE_CD.concat(" = '").concat(obj.toString())).append("' DESC, ");
		}

		List<MType> entityList
			= jdbcManager
				.from(MType.class)
				.where(new SimpleWhere()
				.in("typeCd", allArea)
				.notIn("typeValue", noDisplayValue.toArray())
				.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
				.orderBy(sb.toString().concat(" displayOrder"))
				.getResultList();

		if (entityList == null || entityList.size() == 0) {
			return new ArrayList<LabelValueDto>(0);
		}

		List<LabelValueDto> labelValueList = new ArrayList<LabelValueDto>(entityList.size() + 1);

		if (StringUtils.isNotEmpty(blankLineLabel)) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(blankLineLabel);
			dto.setValue("");
			labelValueList.add(dto);
		}

		for (MType entity : entityList) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(entity.typeName + suffix);
			dto.setValue(String.valueOf(entity.typeValue));
			labelValueList.add(dto);
		}

		return labelValueList;
	}


	/**
	 * エリア別に詳細エリアのリストを生成
	 * 仙台の場合は、都道府県 + 詳細エリアグループ (ただし、都道府県フラグがfalseの場合は詳細エリアグループのみ)
	 * その他は、詳細エリアをそのままリストに詰めて返す
	 * @param areaCd エリアコード
	 * @param prefFlg 都道府県フラグ 仙台版のみ使う。trueの場合はエリアグループの前に都道府県を設定
	 * @param delimiter 都道府県とエリアグループのデリミタ。仙台のみ適応。
	 * @return エリア別にまとめた詳細エリアのリスト
	 */
	public List<LabelValueDto> eachAreaDetailAreaList(int areaCd, boolean prefFlg, String delimiter) {
		if (MAreaConstants.AreaCd.isSendai(areaCd)) {
			List<TypePrefectureInfo> list = typeLogic.selectDetailAreaKbnGroupPrefectures(areaCd);
			return convertTypePrefectureInfos(list, prefFlg, delimiter);
		}
		return getTypeList(MTypeConstants.DetailAreaKbn.getTypeCd(areaCd),
				null,
				"",
				Collections.<Integer>emptyList());
	}


	/**
	 * 都道府県タイプリストを変換
	 * @param infos 都道府県タイプのコレクション
	 * @param prefFlg 都道府県表示フラグ
	 * @param delimiter 都道府県とタイプ名のデリミタ
	 * @return LabelValueDtoのリスト
	 */
	private List<LabelValueDto> convertTypePrefectureInfos(Collection<TypePrefectureInfo> infos,
														   boolean prefFlg,
														   String delimiter) {

		List<LabelValueDto> list = Lists.newArrayList();
		for (TypePrefectureInfo info : infos) {
			final String pref;
			if (prefFlg) {
				if (StringUtils.isBlank(info.getPrefectureName())) {
					pref = "";
				} else {
					pref = info.getPrefectureName().concat(StringUtils.defaultString(delimiter));
				}
			} else {
				pref = "";
			}
			for (MType type : info.getTypeList()) {
				if (CollectionUtils.isEmpty(type.getChildrenTypeList())) {
					LabelValueDto dto = new LabelValueDto(pref.concat(type.typeName), type.typeValue);
					list.add(dto);
				} else {
					for (MType child : type.childrenTypeList) {
						LabelValueDto dto = new LabelValueDto(pref.concat(child.typeName), child.typeValue);
						list.add(dto);
					}
				}
			}
		}
		return list;
	}

	/**
	 * WEBデータ用タグからラベルとバリューのリストを取得します。
	 * @param blankLineLabel 初期ラベル
	 * @param suffix 接尾辞
	 * @param authLevel 権限
	 * @param noDisplayValue 非表示値
	 * @return List<LabelValueDto> ラベルと値を保持するDtoのリスト
	 */
	public List<LabelValueDto> getWebDataTagList(String blankLineLabel, String suffix, String authLevel, List<Integer> noDisplayValue) {

		List<LabelValueDto> labelValueList = new ArrayList<LabelValueDto>();

		// 検索条件の設定
		SimpleWhere where = new SimpleWhere()
				.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED)
				.notIn("id", noDisplayValue.toArray());

		List<MWebTag> entityList
			= jdbcManager
				.from(MWebTag.class)
				.where(where)
				.orderBy(MWebTag.ID)
				.getResultList();

		if (StringUtils.isNotEmpty(blankLineLabel)) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(blankLineLabel);
			dto.setValue("");
			labelValueList.add(dto);
		}

		for (MWebTag entity : entityList) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(entity.webTagName + suffix);
			dto.setValue(String.valueOf(entity.id));
			labelValueList.add(dto);
		}

		return labelValueList;
	}

	/**
	 * ターミナルのラベルとバリューのリストを取得します。
	 * @param blankLineLabel 初期ラベル
	 * @param suffix 接尾辞
	 * @param noDisplayValue 非表示値
	 * @param prefecturesCd 都道府県コード
	 * @return List<LabelValueDto> ラベルと値を保持するDtoのリスト
	 */
	public List<LabelValueDto> getTerminalList(String blankLineLabel, String suffix, List<Integer> noDisplayValue, String prefecturesCd) {

		List<MTerminal> entityList = new ArrayList<>();
		try {
			if (StringUtils.isNotEmpty(prefecturesCd)) {
				entityList = terminalService.findByPrefecturesCd(Integer.parseInt(prefecturesCd));
			} else {
				entityList = terminalService.getAllList();
			}
		} catch (NumberFormatException | WNoResultException e) {
			return new ArrayList<>(0);
		}

		List<LabelValueDto> labelValueList = new ArrayList<LabelValueDto>();
		if (StringUtils.isNotEmpty(blankLineLabel)) {
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(blankLineLabel);
			dto.setValue("");
			labelValueList.add(dto);
		}

		for (MTerminal entity : entityList) {
			if (noDisplayValue.contains(entity.id)) {
				continue;
			}
			LabelValueDto dto = new LabelValueDto();
			dto.setLabel(entity.terminalName + suffix);
			dto.setValue(String.valueOf(entity.id));
			labelValueList.add(dto);
		}
		return labelValueList;
	}

}
