package com.gourmetcaree.admin.service.logic;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.seasar.framework.beans.util.Beans;
import org.seasar.s2csv.csv.S2CSVWriteCtrl;
import org.seasar.s2csv.csv.factory.S2CSVCtrlFactory;

import com.gourmetcaree.common.builder.sql.SqlBuilder.SqlCondition;
import com.gourmetcaree.common.builder.sql.PublishedShopList.PublishedShopListSqlBuilder;
import com.gourmetcaree.common.constants.Constants;
import com.gourmetcaree.common.csv.PublishedShopListCsv;
import com.gourmetcaree.common.dto.BaseDto;
import com.gourmetcaree.common.logic.ValueToNameConvertLogic;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants.DomesticKbn;
import com.gourmetcaree.db.common.constants.MTypeConstants.IndustryKbn;
import com.gourmetcaree.db.common.constants.MTypeConstants.JobOfferFlg;
import com.gourmetcaree.db.common.constants.MTypeConstants.SalesPerCustomerKbn;
import com.gourmetcaree.db.common.constants.MTypeConstants.SeatKbn;
import com.gourmetcaree.db.common.constants.MTypeConstants.ShutokenForeignAreaKbn;
import com.gourmetcaree.db.common.entity.MPrefectures;
import com.gourmetcaree.db.common.service.CityService;
import com.gourmetcaree.db.common.service.PrefecturesService;
import com.gourmetcaree.db.common.service.TypeService;

/**
 * 掲載中の店舗用のロジックです
 * @author yamane
 *
 */
public class PublishedShopListLogic extends AbstractAdminLogic {

	/** リミット：100 */
	private static final int LIMIT = 100;

	/** ファイル名 */
	private static final String FILE_NAME = "ShopList";

	/** CSVコントローラファクトリ */
	@Resource
	private S2CSVCtrlFactory s2CSVCtrlFactory;

	/** 名称変換ロジック */
	@Resource
	private ValueToNameConvertLogic valueToNameConvertLogic;

	@Resource
	private TypeService typeService;

	@Resource
	private PrefecturesService prefecturesService;

	@Resource
	private CityService cityService;

	/** 変換する値を保持するMap */
	private Map<String, Map<Integer, String>> convertValueMap = new HashMap<>();

	/**
	 * CSVを出力します
	 * @param areaCd エリアコード
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public void outputCsv(Integer areaCd) throws UnsupportedEncodingException, IOException {

		response.setContentType("application/octet-stream;charset=Windows-31J");
		response.setHeader("Content-Disposition","attachment; filename=" + createOutputFilePath(FILE_NAME));

		PrintWriter out = null;

		S2CSVWriteCtrl<PublishedShopListCsv> csv_writer = null;

		try {
			out = new PrintWriter(new OutputStreamWriter(response.getOutputStream(), Constants.CSV_ENCODING));

			csv_writer = s2CSVCtrlFactory.getWriteController(PublishedShopListCsv.class, out);

			SqlCondition condition = new PublishedShopListSqlBuilder(areaCd).build();

			List<ShopListDto> list;
			int offset = 0;

			// 業態
			convertValueMap.put(MTypeConstants.IndustryKbn.TYPE_CD, typeService.getMTypeValueMapWithoutThrow(MTypeConstants.IndustryKbn.TYPE_CD));
			// 席数
			convertValueMap.put(MTypeConstants.SeatKbn.TYPE_CD, typeService.getMTypeValueMapWithoutThrow(MTypeConstants.SeatKbn.TYPE_CD));
			// 客単価
			convertValueMap.put(MTypeConstants.SalesPerCustomerKbn.TYPE_CD, typeService.getMTypeValueMapWithoutThrow(MTypeConstants.SalesPerCustomerKbn.TYPE_CD));
			// 国内区分
			convertValueMap.put(MTypeConstants.DomesticKbn.TYPE_CD, typeService.getMTypeValueMapWithoutThrow(MTypeConstants.DomesticKbn.TYPE_CD));
			// 海外エリア
			convertValueMap.put(MTypeConstants.ShutokenForeignAreaKbn.TYPE_CD, typeService.getMTypeValueMapWithoutThrow(MTypeConstants.ShutokenForeignAreaKbn.TYPE_CD));
			// 求人募集フラグ
			convertValueMap.put(MTypeConstants.JobOfferFlg.TYPE_CD, typeService.getMTypeValueMapWithoutThrow(MTypeConstants.JobOfferFlg.TYPE_CD));
			// 都道府県
			convertValueMap.put(MPrefectures.TABLE_NAME, prefecturesService.getCdNameMap());

			while (CollectionUtils.isNotEmpty(
					list = jdbcManager.selectBySql(
							ShopListDto.class,
							condition.getSql(),
							condition.getParamsArray()
							)
					.limit(LIMIT)
					.offset(offset)
					.getResultList())) {

				for (ShopListDto dto : list) {

					PublishedShopListCsv csv = Beans.createAndCopy(PublishedShopListCsv.class, dto).execute();

					convertItem(dto, csv);

					csv_writer.write(csv);
				}

				offset += LIMIT;

				out.flush();
			}


		} finally {

			if (csv_writer != null) {
				csv_writer.close();
				csv_writer = null;
			}

			if (out != null) {
				out.close();
				out = null;
			}
		}

		return;
	}

	/**
	 * 項目を名称に変更する
	 * @param dto
	 * @param csv
	 */
	private void convertItem(ShopListDto dto, PublishedShopListCsv csv) {

		/* 原稿 */
		/* 原稿番号 */
		csv.webId = String.valueOf(dto.webId);
		/* エリア名 */
		csv.webArea = valueToNameConvertLogic.convertToAreaName(new String[] {String.valueOf(dto.webAreaCd)});

		/* 顧客 */
		/* 顧客ID */
		csv.customerId = String.valueOf(dto.customerId);
		/* 県 */
		csv.customerPrefectures = Objects.nonNull(dto.customerPrefecturesCd) ? convertValueMap.get(MPrefectures.TABLE_NAME).get(dto.customerPrefecturesCd) : "";
		/* 住所1 */
		if (Objects.nonNull(dto.customerMunicipality)) {
			csv.customerAddress1 = dto.customerMunicipality;
		} else {
			csv.customerAddress1 = "";
		}
		/* 住所2 */
		if (Objects.nonNull(dto.customerAddress)) {
			csv.customerAddress2 = dto.customerAddress;
		} else {
			csv.customerAddress2 = "";
		}

		/* 電話番号 */
		csv.customerTelNo = getPhoneNo(dto.customerPhoneNo1, dto.customerPhoneNo2, dto.customerPhoneNo3);

		/* 店舗 */
		csv.shopId = String.valueOf(dto.shopId);

		/* 店舗業種区分 */
		csv.shopIndustryKbnName1 = convertName(dto.shopIndustryKbn1, IndustryKbn.TYPE_CD);
		csv.shopIndustryKbnName2 = convertName(dto.shopIndustryKbn2, IndustryKbn.TYPE_CD);

		/* 電話番号 */
		csv.shopTelNo = StringUtils.defaultString(dto.shopCsvPhoneNo, "");

		// 国内、海外
		csv.shopDomesticKbn = convertName(dto.shopDomesticKbn, DomesticKbn.TYPE_CD);

		// 国内の場合
		if (GourmetCareeUtil.eqInt(MTypeConstants.DomesticKbn.DOMESTIC, dto.shopDomesticKbn)) {
			csv.shopArea = convertName(dto.shopPrefecturesCd, MPrefectures.TABLE_NAME);
			csv.shopAddress = (StringUtils.isNotEmpty(dto.shopCityCd) ? cityService.getName(dto.shopCityCd): "")
								+ (StringUtils.defaultString(dto.shopAddress, ""));
		} else {
			csv.shopArea = convertName(dto.shopShutokenForeignAreaKbn, ShutokenForeignAreaKbn.TYPE_CD);
			csv.shopAddress = StringUtils.defaultString(dto.shopForeignAddress, "");
		}
		// 座席数
		csv.shopSeating = convertName(dto.shopSeatKbn, SeatKbn.TYPE_CD);
		// 客単価
		csv.shopUnitPrice = convertName(dto.shopSalesPerCustomerKbn, SalesPerCustomerKbn.TYPE_CD);
		// 求人掲載
		csv.jobOffer = convertName(dto.shopJobOfferFlg, JobOfferFlg.TYPE_CD);
	}

	/**
	 * 区分値を名前に変換する
	 * @param value
	 * @param typeCd
	 * @return
	 */
	private String convertName(Integer value, String typeCd) {
		return Objects.nonNull(value) ? (convertValueMap.get(typeCd) != null ? convertValueMap.get(typeCd).get(value) : "") : "";
	}

    /**
     * 電話番号の取得
     *
     * @return 電話番号
     */
	private String getPhoneNo(String phoneNo1, String phoneNo2, String phoneNo3) {

		if (StringUtils.isBlank(phoneNo1)
                || StringUtils.isBlank(phoneNo2)
                || StringUtils.isBlank(phoneNo3)) {
            return "";
        }

        StringBuilder sb = new StringBuilder(0);
        sb.append(phoneNo1)
                .append("-")
                .append(phoneNo2)
                .append("-")
                .append(phoneNo3);

        return sb.toString();
    }


	/**
	 * ファイル名を生成する
	 * @return ファイル名
	 */
	private static String createOutputFilePath(String fileName) {

		String dateStr = null;

		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

		dateStr = sdf.format(date);

		String outputFileName = new StringBuilder()
				.append(fileName)
				.append("_")
				.append(dateStr)
				.append(".csv")
				.toString();

		return outputFileName;
	}

	public static class ShopListDto extends BaseDto {

		/** シリアルバージョンUID */
		private static final long serialVersionUID = -8198751972423188870L;

		/** 掲載ID */
		public Integer webId;

		/** 原稿名 */
		public String webManuscriptName;

		/** エリアコード */
		public Integer webAreaCd;

		/** 顧客ID */
		public Integer customerId;

		/** 顧客名 */
		public String customerName;

		/** 郵便番号 */
		public String customerZipCd;

		/** 都道府県 */
		public Integer customerPrefecturesCd;

		/** 住所1 */
		public String customerMunicipality;

		/** 住所2 */
		public String customerAddress;

		/** 電話番号1 */
		public String customerPhoneNo1;

		/** 電話番号2 */
		public String customerPhoneNo2;

		/** 電話番号3 */
		public String customerPhoneNo3;

		/** 店舗ID */
		public Integer shopId;

		/** 店舗名 */
		public String shopName;

		/** 業種区分1 */
		public Integer shopIndustryKbn1;

		/** 業種区分2 */
		public Integer shopIndustryKbn2;

		/** 海外エリア区分 */
		public Integer shopShutokenForeignAreaKbn;

		/** 国内外区分 */
		public Integer shopDomesticKbn;

		/** 海外住所 */
		public String shopForeignAddress;

		/** 都道府県 */
		public Integer shopPrefecturesCd;

		/** 市区町村 */
		public String shopCityCd;

		/** 住所 */
		public String shopAddress;

		/** 電話番号 */
		public String shopCsvPhoneNo;

		/** 交通 */
		public String shopTransit;

		/** 店舗情報 */
		public String shopInformation;

		/** 休み */
		public String shopHoliday;

		/** 営業時間 */
		public String shopBusinessHours;

		/** 席数 */
		public Integer shopSeatKbn;

		/** 客単価 */
		public Integer shopSalesPerCustomerKbn;

		/** ライト版表示フラグ */
		public Integer shopJobOfferFlg;

	}
}
