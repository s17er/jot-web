package com.gourmetcaree.common.logic;

import static com.gourmetcaree.common.util.GourmetCareeUtil.*;
import static jp.co.whizz_tech.commons.WztStringUtil.*;
import static org.seasar.extension.jdbc.operation.Operations.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.seasar.extension.jdbc.IterationCallback;
import org.seasar.extension.jdbc.IterationContext;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.util.ArrayUtil;
import org.seasar.framework.util.StringUtil;

import com.gourmetcaree.common.util.GcCollectionUtils;
import com.gourmetcaree.db.common.constants.MAreaConstants;
import com.gourmetcaree.db.common.constants.MAreaConstants.AreaCdEnum;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.entity.AbstractCommonEntity;
import com.gourmetcaree.db.common.entity.MAdvancedRegistration;
import com.gourmetcaree.db.common.entity.MArea;
import com.gourmetcaree.db.common.entity.MCity;
import com.gourmetcaree.db.common.entity.MCompany;
import com.gourmetcaree.db.common.entity.MCustomer;
import com.gourmetcaree.db.common.entity.MPrefectures;
import com.gourmetcaree.db.common.entity.MRailroad;
import com.gourmetcaree.db.common.entity.MRoute;
import com.gourmetcaree.db.common.entity.MSales;
import com.gourmetcaree.db.common.entity.MStation;
import com.gourmetcaree.db.common.entity.MType;
import com.gourmetcaree.db.common.entity.TWebAttribute;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.CustomerService;

import jp.co.whizz_tech.commons.WztStringUtil;


/**
 * 値から名前の文字列に変換するクラスです。
 * @author Hiroyuki Sugimoto
 * @version 1.0
 */
public class ValueToNameConvertLogic extends AbstractGourmetCareeLogic {

	/** デフォルトのデリミタ */
	public static final String DELIMITER = ", ";

	/** 顧客サービス */
	@Resource
	CustomerService customerService;

	/** ラベルとバリューのリストを生成するロジック */
	@Resource
	LabelValueListLogic labelValueListLogic;

	/**
	 * 単純ラベル変換の対象を持つEnum
	 * @author ando
	 */
	public enum LabelTarget {
		SEX,
		EMPLOY_PTN_KBN,
		PREFECTURE_CD
		;
	};

	/**
	 * エリアコードから名前に変換します。
	 * @param areaCode エリアコード
	 * @return エリア名文字列
	 */
	public String convertToAreaName(String[] areaCd) {
		return convertToAreaName(DELIMITER, areaCd);
	}

	/**
	 * エリアコードから名前に変換します。
	 * @param delimiter デリミタ
	 * @param areaCode エリアコード
	 * @return エリアコード文字列
	 */
	public String convertToAreaName(String delimiter, String[] areaCd) {
		List<MArea> entityList =
			jdbcManager
				.from(MArea.class)
				.where(new SimpleWhere()
				.excludesWhitespace()
				.in("areaCd", (Object[]) areaCd))
				.orderBy("displayOrder")
				.getResultList();

		StringBuilder buf = new StringBuilder("");
		if (!entityList.isEmpty()) {
			buf.append(entityList.get(0).areaName);
			for (int i = 1; i < entityList.size(); i++) {
				buf.append(delimiter);
				buf.append(entityList.get(i).areaName);
			}
		}

		return buf.toString();
	}

	/**
	 * 市区町村コードから名前に変換します。
	 * @param cd 市区町村コード
	 * @return 市区町村文字列
	 */
	public String convertToCityName(String[] cd) {
		return convertToCityName(DELIMITER, cd);
	}

	/**
	 * 市区町村コードから名前に変換します。
	 * @param cd 市区町村コード
	 * @return 市区町村文字列
	 */
	public String convertToCityName(String cd) {
		return convertToCityName(DELIMITER, new String[]{cd});
	}

	/**
	 * 市区町村コードから名前に変換します。
	 * @param delimiter デリミタ
	 * @param cd 市区町村コード
	 * @return 市区町村名文字列
	 */
	public String convertToCityName(String delimiter, String[] cd) {
		List<MCity> entityList =
			jdbcManager
				.from(MCity.class)
				.where(new SimpleWhere()
				.excludesWhitespace()
				.in("cityCd", (Object[]) cd))
				.orderBy("displayOrder")
				.getResultList();

		StringBuilder buf = new StringBuilder("");
		if (!entityList.isEmpty()) {
			buf.append(entityList.get(0).cityName);
			for (int i = 1; i < entityList.size(); i++) {
				buf.append(delimiter);
				buf.append(entityList.get(i).cityName);
			}
		}

		return buf.toString();
	}

	/**
	 * 海外エリアコードから海外エリア名を取得
	 * @param cd 海外エリアコード
	 * @return
	 */
	public String convertToForeginAreaName(String cd) {
		List<MType> entityList =
			jdbcManager
				.from(MType.class)
				.where(new SimpleWhere()
				.excludesWhitespace()
				.eq("typeCd", "shutoken_foreign_area_kbn")
				.eq("typeValue", cd))
				.getResultList();

		StringBuilder buf = new StringBuilder("");
		if (!entityList.isEmpty()) {
			buf.append(entityList.get(0).typeName);
			for (int i = 1; i < entityList.size(); i++) {
				buf.append(entityList.get(i).typeName);
			}
		}

		return buf.toString();
	}

	/**
	 * 会社IDから名前に変換します。
	 * @param id 会社ID
	 * @return 会社名文字列
	 */
	public String convertToCompanyName(String[] id) {
		return convertToCompanyName(DELIMITER, id);
	}

	/**
	 * 会社IDから名前に変換します。
	 * @param delimiter デリミタ
	 * @param id 会社ID
	 * @return 会社名文字列
	 */
	public String convertToCompanyName(String delimiter, String[] id) {
		List<MCompany> entityList =
			jdbcManager
				.from(MCompany.class)
				.where(new SimpleWhere()
				.excludesWhitespace()
				.in("id", (Object[]) id))
				.orderBy("displayOrder")
				.getResultList();

		StringBuilder buf = new StringBuilder("");
		if (!entityList.isEmpty()) {
			buf.append(entityList.get(0).companyName);
			for (int i = 1; i < entityList.size(); i++) {
				buf.append(delimiter);
				buf.append(entityList.get(i).companyName);
			}
		}

		return buf.toString();
	}

	/**
	 * 区分コード、区分値から区分名に変換します。
	 * @param typeCd 区分コード
	 * @param typeValue 区分値
	 * @return 区分名文字列
	 */
	public String convertToTypeName(String typeCd, String... typeValue) {
		return convertToTypeName(DELIMITER, typeCd, typeValue);
	}

	/**
	 * 区分コード、区分値から区分名に変換します。
	 * @param typeCd 区分コード
	 * @param typeValue 区分値
	 * @return 区分名文字列
	 */
	public String convertToTypeName(String typeCd, int typeValue) {
		return convertToTypeName(DELIMITER, typeCd, new String[]{Integer.toString(typeValue)});
	}

	/**
	 * 区分コード、区分値から区分名に変換します。
	 * @param delimiter デリミタ
	 * @param typeCd 区分コード
	 * @param typeValue 区分値
	 * @return 区分名名文字列
	 */
	public String convertToTypeName(String delimiter, String typeCd, String[] typeValue) {

		if (ArrayUtil.isEmpty(typeValue)) {
			return "";
		}

		List<Integer> tmpList = new ArrayList<Integer>(typeValue.length);

		for (String tmpValue : typeValue) {
			tmpList.add(NumberUtils.toInt(tmpValue));
		}

		List<MType> entityList =
			jdbcManager
				.from(MType.class)
				.where(new SimpleWhere()
				.excludesWhitespace()
				.eq("typeCd", typeCd)
				.in("typeValue", tmpList.toArray(new Object[0])))
				.orderBy("displayOrder")
				.getResultList();

		StringBuilder buf = new StringBuilder("");
		if (!entityList.isEmpty()) {
			buf.append(entityList.get(0).typeName);
			for (int i = 1; i < entityList.size(); i++) {
				buf.append(delimiter);
				buf.append(entityList.get(i).typeName);
			}
		}

		return buf.toString();
	}

	/**
	 * 区分コード、区分値から区分名に変換します。
	 * @param typeCd 区分コード
	 * @return 区分名文字列
	 */
	public String convertToTypeNameForAllArea(String[] areaValue) {
		return convertToTypeNameForAllArea(DELIMITER, areaValue);
	}

	/**
	 * エリア値からエリア名に変換します。
	 * @param delimiter デリミタ
	 * @param typeValue エリア値
	 * @return エリア名文字列
	 */
	public String convertToTypeNameForAllArea(String delimiter, String[] areaValue) {
		if (ArrayUtil.isEmpty(areaValue)) {
			return "";
		}

		List<Integer> tmpList = new ArrayList<Integer>(areaValue.length);

		for (String tmpValue : areaValue) {
			tmpList.add(NumberUtils.toInt(tmpValue));
		}

		List<MType> entityList =
			jdbcManager
				.from(MType.class)
				.where(new SimpleWhere()
				.excludesWhitespace()
				.in("typeCd", new Object[]{MTypeConstants.ShutokenWebAreaKbn.TYPE_CD, MTypeConstants.SendaiWebAreaKbn.TYPE_CD, MTypeConstants.ShutokenForeignAreaKbn.TYPE_CD })
				.in("typeValue", tmpList.toArray(new Object[0])))
				.orderBy("typeValue")
				.getResultList();

		StringBuilder buf = new StringBuilder("");
		if (!entityList.isEmpty()) {
			buf.append(entityList.get(0).typeName);
			for (int i = 1; i < entityList.size(); i++) {
				buf.append(delimiter);
				buf.append(entityList.get(i).typeName);
			}
		}

		return buf.toString();
	}
	/**
	 * 営業担当者IDから名前に変換します。
	 * @param id 営業担当者ID
	 * @return 営業担当者名文字列
	 */
	public String convertToSalesName(String[] id) {
		return convertToSalesName(DELIMITER, id);
	}

	/**
	 * 営業担当者IDから名前に変換します。
	 * @param delimiter デリミタ
	 * @param id 営業担当者ID
	 * @return 営業担当者名文字列
	 */
	public String convertToSalesName(String delimiter, String[] id) {
		List<MSales> entityList =
			jdbcManager
				.from(MSales.class)
				.where(new SimpleWhere()
				.excludesWhitespace()
				.in("id", (Object[]) id))
				.orderBy(asc("companyId"), asc("authorityCd"), asc("salesNameKana"))
				.getResultList();

		StringBuilder buf = new StringBuilder("");
		if (!entityList.isEmpty()) {
			buf.append(entityList.get(0).salesName);
			for (int i = 1; i < entityList.size(); i++) {
				buf.append(delimiter);
				buf.append(entityList.get(i).salesName);
			}
		}

		return buf.toString();
	}

	/**
	 * 職種区分から名前に変換します。
	 * @param kbn 職種区分
	 * @return 職種名文字列
	 */
	public String[] convertToJobNameArray(String[] kbn) {
		List<String> list = new ArrayList<String>();
		List<MType> entityList =
				jdbcManager
					.from(MType.class)
					.where(new SimpleWhere()
					.excludesWhitespace()
					.eq("typeCd", MTypeConstants.JobKbn.TYPE_CD)
					.in("typeValue", (Object[]) kbn)
					.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
					.orderBy("displayOrder")
					.getResultList();

		if (entityList == null || entityList.isEmpty()) {
			return new String[0];
		}

		for (MType entity : entityList) {
			list.add(entity.typeName);
		}

		return list.toArray(new String[0]);

	}

	/**
	 * 職種区分から名前に変換します。
	 * @param kbn 職種区分
	 * @return 職種名文字列
	 */
	public String convertToJobName(String[] kbn) {
		return convertToJobName(DELIMITER, kbn);
	}

	/**
	 * 職種区分から名前に変換します。
	 * @param delimiter デリミタ
	 * @param kbn 職種区分
	 * @return 職種名文字列
	 */
	public String convertToJobName(String delimiter, String[] kbn) {
		List<MType> entityList =
			jdbcManager
				.from(MType.class)
				.where(new SimpleWhere()
				.excludesWhitespace()
				.eq("typeCd", MTypeConstants.JobKbn.TYPE_CD)
				.in("typeValue", (Object[]) kbn)
				.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
				.orderBy("displayOrder")
				.getResultList();

		StringBuilder buf = new StringBuilder("");
		if (!entityList.isEmpty()) {
			buf.append(entityList.get(0).typeName);
			for (int i = 1; i < entityList.size(); i++) {
				buf.append(delimiter);
				buf.append(entityList.get(i).typeName);
			}
		}

		return buf.toString();
	}

	/**
	 * 雇用形態区分から名前に変換します。
	 * @param kbn 雇用形態区分
	 * @return 雇用形態名文字列
	 */
	public String convertToEmployName(String[] kbn) {
		return convertToEmployName(DELIMITER, kbn);
	}

	/**
	 * 雇用形態区分から名前に変換します。
	 * @param delimiter デリミタ
	 * @param kbn 雇用形態区分
	 * @return 雇用形態名文字列
	 */
	public String convertToEmployName(String delimiter, String[] kbn) {
		List<MType> entityList =
			jdbcManager
				.from(MType.class)
				.where(new SimpleWhere()
				.excludesWhitespace()
				.eq("typeCd", MTypeConstants.EmployPtnKbn.TYPE_CD)
				.in("typeValue", (Object[]) kbn)
				.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
				.orderBy("displayOrder")
				.getResultList();

		StringBuilder buf = new StringBuilder("");
		if (!entityList.isEmpty()) {
			buf.append(entityList.get(0).typeName);
			for (int i = 1; i < entityList.size(); i++) {
				buf.append(delimiter);
				buf.append(entityList.get(i).typeName);
			}
		}

		return buf.toString();
	}

	/**
	 * 資格区分から名前に変換します。
	 * @param kbn 資格区分
	 * @return 資格名文字列
	 */
	public String convertToQualificationName(String[] kbn) {
		return convertToQualificationName(DELIMITER, kbn);
	}

	/**
	 * 資格区分から名前に変換します。
	 * @param delimiter デリミタ
	 * @param kbn 資格区分
	 * @return 資格名文字列
	 */
	public String convertToQualificationName(String delimiter, String[] kbn) {
		List<MType> entityList =
			jdbcManager
				.from(MType.class)
				.where(new SimpleWhere()
				.excludesWhitespace()
				.eq("typeCd", MTypeConstants.QualificationKbn.TYPE_CD)
				.in("typeValue", (Object[]) kbn)
				.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
				.orderBy("displayOrder")
				.getResultList();

		StringBuilder buf = new StringBuilder("");
		if (!entityList.isEmpty()) {
			buf.append(entityList.get(0).typeName);
			for (int i = 1; i < entityList.size(); i++) {
				buf.append(delimiter);
				buf.append(entityList.get(i).typeName);
			}
		}

		return buf.toString();
	}


	/**
	 * 業種区分から名前に変換します。
	 * @param kbn 業種区分
	 * @return 業種名文字列
	 */
	public String[] convertToIndustryNameArray(Integer[] kbn) {
		List<String> list = new ArrayList<String>();
		List<MType> entityList = jdbcManager
									.from(MType.class)
									.where(new SimpleWhere()
									.excludesWhitespace()
									.eq("typeCd", MTypeConstants.IndustryKbn.TYPE_CD)
									.in("typeValue", (Object[]) kbn)
									.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
									.orderBy("displayOrder")
									.getResultList();

		if (entityList == null || entityList.isEmpty()) {
			return new String[0];
		}

		for (MType entity : entityList) {
			list.add(entity.typeName);
		}


		return list.toArray(new String[0]);

	}

	/**
	 * 業種区分から名前に変換します。
	 * @param kbn 業種区分
	 * @return 業種名文字列
	 */
	public String convertToIndustryName(String[] kbn) {
		return convertToIndustryName(DELIMITER, kbn);
	}

	/**
	 * 業種区分から名前に変換します。
	 * @param kbn 業種区分
	 * @return 業種名文字列
	 */
	public String convertToIndustryName(Integer[] kbn) {
		return convertToIndustryName(DELIMITER, kbn);
	}

	/**
	 * 業種区分から名前に変換します。
	 * @param delimiter デリミタ
	 * @param kbn 業種区分
	 * @return 業種名文字列
	 */
	public String convertToIndustryName(String delimiter, String[] kbn) {
		List<MType> entityList =
			jdbcManager
				.from(MType.class)
				.where(new SimpleWhere()
				.excludesWhitespace()
				.eq("typeCd", MTypeConstants.IndustryKbn.TYPE_CD)
				.in("typeValue", (Object[]) kbn)
				.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
				.orderBy("displayOrder")
				.getResultList();

		StringBuilder buf = new StringBuilder("");
		if (!entityList.isEmpty()) {
			buf.append(entityList.get(0).typeName);
			for (int i = 1; i < entityList.size(); i++) {
				buf.append(delimiter);
				buf.append(entityList.get(i).typeName);
			}
		}

		return buf.toString();
	}

	/**
	 * 業種区分から名前に変換します。
	 * @param delimiter デリミタ
	 * @param kbn 業種区分
	 * @return 業種名文字列
	 */
	public String convertToIndustryName(String delimiter, Integer[] kbn) {
		List<MType> entityList =
			jdbcManager
				.from(MType.class)
				.where(new SimpleWhere()
				.excludesWhitespace()
				.eq("typeCd", MTypeConstants.IndustryKbn.TYPE_CD)
				.in("typeValue", (Object[]) kbn)
				.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
				.orderBy("displayOrder")
				.getResultList();

		StringBuilder buf = new StringBuilder("");
		if (!entityList.isEmpty()) {
			buf.append(entityList.get(0).typeName);
			for (int i = 1; i < entityList.size(); i++) {
				buf.append(delimiter);
				buf.append(entityList.get(i).typeName);
			}
		}

		return buf.toString();
	}

	/**
	 * 勤務地エリア区分から名前に変換します。
	 * @param kbn 勤務地エリア区分(WEBエリアから名称変更)
	 * @return 勤務地エリア名文字列
	 */
	public String convertToWebAreaName(String[] kbn, String typeCd) {
		return convertToWebAreaName(DELIMITER, kbn, typeCd);
	}

	/**
	 * 勤務地エリア区分から名前に変換します。
	 * @param delimiter デリミタ
	 * @param kbn 勤務地エリア区分(WEBエリアから名称変更)
	 * @return 勤務地エリア名文字列
	 */
	public String convertToWebAreaName(String delimiter, String[] kbn, String typeCd) {
		List<MType> entityList =
			jdbcManager
				.from(MType.class)
				.where(new SimpleWhere()
				.excludesWhitespace()
				.eq("typeCd", typeCd)
				.in("typeValue", (Object[]) kbn)
				.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
				.orderBy("displayOrder")
				.getResultList();

		StringBuilder buf = new StringBuilder("");
		if (!entityList.isEmpty()) {
			buf.append(entityList.get(0).typeName);
			for (int i = 1; i < entityList.size(); i++) {
				buf.append(delimiter);
				buf.append(entityList.get(i).typeName);
			}
		}

		return buf.toString();
	}

	/**
	 * 都道府県コードから名前に変換します。
	 * @param cd 都道府県コード
	 * @return 都道府県文字列
	 */
	public String convertToPrefecturesName(String[] cd) {
		return convertToPrefecturesName(DELIMITER, cd);
	}

	/**
	 * 都道府県コードから名前に変換します。
	 * @param cd 都道府県コード
	 * @return 都道府県文字列
	 */
	public String convertToPrefecturesName(int cd) {
		return convertToPrefecturesName(DELIMITER, new String[]{Integer.toString(cd)});
	}

	/**
	 * 都道府県コードから名前に変換します。
	 * @param delimiter デリミタ
	 * @param cd 都道府県コード
	 * @return 都道府県名文字列
	 */
	public String convertToPrefecturesName(String delimiter, String[] cd) {
		List<MPrefectures> entityList =
			jdbcManager
				.from(MPrefectures.class)
				.where(new SimpleWhere()
				.excludesWhitespace()
				.in("prefecturesCd", (Object[]) cd))
				.orderBy("displayOrder")
				.getResultList();

		StringBuilder buf = new StringBuilder("");
		if (!entityList.isEmpty()) {
			buf.append(entityList.get(0).prefecturesName);
			for (int i = 1; i < entityList.size(); i++) {
				buf.append(delimiter);
				buf.append(entityList.get(i).prefecturesName);
			}
		}

		return buf.toString();
	}

	/**
	 * 顧客IDから名前に変換します。
	 * @param id 顧客ID
	 * @return 顧客名文字列
	 * @throws WNoResultException
	 * @throws NumberFormatException
	 */
	public String convertToCustomerName(String[] id) throws NumberFormatException, WNoResultException {
		return convertToCustomerName(DELIMITER, id);
	}

	/**
	 * 顧客IDから名前に変換します。
	 * @param delimiter デリミタ
	 * @param id 顧客ID
	 * @return 顧客名文字列
	 * @throws WNoResultException
	 * @throws NumberFormatException
	 */
	public String convertToCustomerName(String delimiter, String[] id) throws NumberFormatException, WNoResultException {

		// 	idがnullの場合ブランクを返す
		if (ArrayUtil.isEmpty(id)) {
			return "";
		}

		// データの取得
		List<MCustomer> entityList = customerService.findById(toIntegerArray(id));

		List<String> nameList = new ArrayList<String>();
		// 名称をリストにセット
		for (MCustomer entity : entityList) {
			// nullの場合はブランクを入れる
			if (StringUtil.isEmpty(entity.customerName)) {
				nameList.add("");
			} else {
				nameList.add(entity.customerName);
			}
		}
		// デリミタ区切りにして返す
		return createDelimiterStr(nameList);
	}

	/**
	 * 顧客名を取得します。
	 * @param id
	 * @return
	 * @throws WNoResultException
	 */
	public String convertToCustomerName(int id) throws WNoResultException {
		return convertToCustomerName(DELIMITER, new Integer[]{id});
	}

	/**
	 * 顧客IDから名前に変換します。
	 * @param delimiter デリミタ
	 * @param id 顧客ID
	 * @return 顧客名文字列
	 * @throws WNoResultException
	 * @throws NumberFormatException
	 */
	public String convertToCustomerName(String delimiter, Integer[] id)
	throws WNoResultException {

		// 	idがnullの場合ブランクを返す
		if (ArrayUtil.isEmpty(id)) {
			return "";
		}

		// データの取得
		List<MCustomer> entityList = customerService.findById(id);

		List<String> nameList = new ArrayList<String>();
		// 名称をリストにセット
		for (MCustomer entity : entityList) {
			// nullの場合はブランクを入れる
			if (StringUtil.isEmpty(entity.customerName)) {
				nameList.add("");
			} else {
				nameList.add(entity.customerName);
			}
		}
		// デリミタ区切りにして返す
		return createDelimiterStr(nameList);
	}

	/**
	 * 鉄道会社コードから名前に変換します。
	 * @param id 鉄道会社コード
	 * @return 鉄道会社文字列
	 */
	@Deprecated
	public String convertToRailroadName(Integer[] id, AreaCdEnum areaCd) {
		return convertToRailroadName(DELIMITER, id, areaCd);
	}

	/**
	 * 鉄道会社コードから名前に変換します。
	 * @param delimiter デリミタ
	 * @param id 鉄道会社コード
	 * @return 鉄道会社名文字列
	 */
	@Deprecated
	public String convertToRailroadName(String delimiter, Integer[] id, AreaCdEnum areaCd) {
		List<MRailroad> entityList =
			jdbcManager
				.from(MRailroad.class)
				.where(new SimpleWhere()
				.excludesWhitespace()
				.in(toCamelCase(MRailroad.ID), (Object[])id)
				.eq(toCamelCase(MRailroad.AREA_CD), areaCd.getValue())
				.eq(toCamelCase(MRailroad.DELETE_FLG), DeleteFlgKbn.NOT_DELETED))
				.orderBy(asc(toCamelCase(MRailroad.DISPLAY_ORDER)))
				.getResultList();

		StringBuilder buf = new StringBuilder("");
		if (!entityList.isEmpty()) {
			buf.append(entityList.get(0).railroadName);
			for (int i = 1; i < entityList.size(); i++) {
				buf.append(delimiter);
				buf.append(entityList.get(i).railroadName);
			}
		}

		return buf.toString();
	}

	/**
	 * 路線コードから名前に変換します。
	 * @param id 路線コード
	 * @return 路線文字列
	 */
	@Deprecated
	public String convertToRouteName(Integer[] id) {
		return convertToRouteName(DELIMITER, id);
	}

	/**
	 * 路線コードから名前に変換します。
	 * @param delimiter デリミタ
	 * @param id 路線コード
	 * @return 路線名文字列
	 */
	@Deprecated
	public String convertToRouteName(String delimiter, Integer[] id) {
		List<MRoute> entityList =
			jdbcManager
				.from(MRoute.class)
				.where(new SimpleWhere()
				.excludesWhitespace()
				.in(toCamelCase(MRoute.ID), (Object[])id)
				.eq(toCamelCase(MRoute.DELETE_FLG), DeleteFlgKbn.NOT_DELETED))
				.orderBy(asc(toCamelCase(MRoute.DISPLAY_ORDER)))
				.getResultList();

		StringBuilder buf = new StringBuilder("");
		if (!entityList.isEmpty()) {
			buf.append(entityList.get(0).routeName);
			for (int i = 1; i < entityList.size(); i++) {
				buf.append(delimiter);
				buf.append(entityList.get(i).routeName);
			}
		}

		return buf.toString();
	}

	/**
	 * 駅IDから名前に変換します。
	 * @param id 駅ID
	 * @return 駅名
	 */
	public String convertToStationName(Integer[] id) {
		return convertToStationName(DELIMITER, id);
	}

	/**
	 * 駅IDから名前に変換します。
	 * @param delimiter デリミタ
	 * @param id 駅ID
	 * @return 駅名
	 */
	public String convertToStationName(String delimiter, Integer[] id) {
		List<MStation> entityList =
			jdbcManager
				.from(MStation.class)
				.where(new SimpleWhere()
				.excludesWhitespace()
				.in(toCamelCase(MStation.ID), (Object[])id)
				.eq(toCamelCase(MStation.DELETE_FLG), DeleteFlgKbn.NOT_DELETED))
				.orderBy(asc(toCamelCase(MStation.DISPLAY_ORDER)))
				.getResultList();

		StringBuilder buf = new StringBuilder("");
		if (!entityList.isEmpty()) {
			buf.append(entityList.get(0).stationName);
			for (int i = 1; i < entityList.size(); i++) {
				buf.append(delimiter);
				buf.append(entityList.get(i).stationName);
			}
		}

		return buf.toString();
	}


	/**
	 * web属性区分から、タイプ名に変換します。
	 * @param webId
	 * @param typeCd
	 * @return
	 */
	public String convertWebAttributeKbnToName(int webId, String typeCd) {
		return convertWebAttributeKbnToName(DELIMITER, webId, typeCd);
	}

	/**
	 * web属性区分から、タイプ名に変換します。
	 * @param delimiter
	 * @param webId
	 * @param typeCd
	 * @return
	 */
	public String convertWebAttributeKbnToName(final String delimiter, int webId, String typeCd) {
		List<Integer> valueList =
				jdbcManager.from(TWebAttribute.class)
				.where(new SimpleWhere()
				.eq(toCamelCase(TWebAttribute.WEB_ID), webId)
				.eq(toCamelCase(TWebAttribute.ATTRIBUTE_CD), typeCd)
				.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
				.iterate(new IterationCallback<TWebAttribute, List<Integer>>() {
					private List<Integer> list = new ArrayList<Integer>();
					@Override
					public List<Integer> iterate(TWebAttribute entity, IterationContext context) {
						if (entity != null) {
							list.add(entity.attributeValue);
						}
						return list;
					}
				});

		if (CollectionUtils.isEmpty(valueList)) {
			return "";
		}

		StringBuffer buffer =
				jdbcManager.from(MType.class)
					.where(new SimpleWhere()
					.eq(toCamelCase(MType.TYPE_CD), typeCd)
					.in(toCamelCase(MType.TYPE_VALUE), valueList)
					.eq("deleteFlg", DeleteFlgKbn.NOT_DELETED))
					.orderBy(MType.DISPLAY_ORDER)
					.iterate(new IterationCallback<MType, StringBuffer>() {
						StringBuffer sb = new StringBuffer(0);
						@Override
						public StringBuffer iterate(MType entity, IterationContext context) {
							if (entity != null) {
								if (sb.length() > 0) {
									sb.append(delimiter);
								}
								sb.append(entity.typeName);
							}
							return sb;
						}
					});

		if (buffer == null) {
			return "";
		}

		return buffer.toString();
	}


	/**
	 * 事前登録短縮名に変換します。
	 * @param advancedRegistrationId 事前登録ID
	 * @return 事前登録短縮名
	 */
	public String convertToAdvancedRegistrationShortName(int advancedRegistrationId) {

		List<Integer> idList = new ArrayList<Integer>(0);
		idList.add(advancedRegistrationId);
		return convertToAdvancedRegistrationShortName(idList);
	}


	/**
	 * 事前登録短縮名に変換します。
	 * @param advancedRegistrationId 事前登録ID
	 * @return 事前登録短縮名
	 */
	public String convertToAdvancedRegistrationShortName(String... advancedRegistrationIds) {
		if (ArrayUtil.isEmpty(advancedRegistrationIds)) {
			return "";
		}

		List<Integer> list = new ArrayList<Integer>(0);
		for (String id : advancedRegistrationIds) {
			list.add(NumberUtils.toInt(id));
		}

		return convertToAdvancedRegistrationShortName(list);
	}

	/**
	 * 事前登録短縮名に変換します。
	 * @param advancedRegistrationIdList 事前登録IDリスト
	 * @return 事前登録短縮名
	 */
	public String convertToAdvancedRegistrationShortName(List<Integer> advancedRegistrationId) {
		return convertToAdvancedRegistrationShortName(DELIMITER, advancedRegistrationId);
	}
	/**
	 * 事前登録短縮名に変換します。
	 * @param delimiter 区切り文字
	 * @param advancedRegistrationIdリスト 事前登録IDリスト
	 * @return 事前登録短縮名
	 */
	public String convertToAdvancedRegistrationShortName(final String delimiter, List<Integer> advancedRegistrationId) {
		if (CollectionUtils.isEmpty(advancedRegistrationId)) {
			return "";
		}
		SimpleWhere where = new SimpleWhere();
		where.in(WztStringUtil.toCamelCase(MAdvancedRegistration.ID), advancedRegistrationId);
		where.eq(WztStringUtil.toCamelCase(AbstractCommonEntity.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		StringBuffer sb =
				jdbcManager.from(MAdvancedRegistration.class)
					.where(where)
					.orderBy(MAdvancedRegistration.ID)
					.iterate(new IterationCallback<MAdvancedRegistration, StringBuffer>() {
						private StringBuffer buff = new StringBuffer(0);
						@Override
						public StringBuffer iterate(MAdvancedRegistration entity, IterationContext context) {
							if (entity == null) {
								return buff;
							}

							if (buff.length() > 0) {
								buff.append(delimiter);
							}

							buff.append(entity.advancedRegistrationShortName);

							return buff;
						}
					});

		if (sb == null) {
			return "";
		}

		return sb.toString();
	}


    public String convertToDetailAreaName(int areaCd, Collection<?> values) {
        return convertToDetailAreaName(DELIMITER, areaCd, values);
    }
    public String convertToDetailAreaName(String delimiter, int areaCd, Collection<?> values) {
        if (MAreaConstants.AreaCd.isSendai(areaCd)) {
            return convertToTypeName(delimiter,
                    MTypeConstants.DetailAreaKbnGroup.getTypeCd(areaCd),
                    GcCollectionUtils.toStringList(values).toArray(new String[0]));
        }


        return convertToTypeName(delimiter,
                MTypeConstants.DetailAreaKbn.getTypeCd(areaCd),
                GcCollectionUtils.toStringList(values).toArray(new String[0]));
    }
}
