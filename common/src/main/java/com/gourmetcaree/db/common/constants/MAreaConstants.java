package com.gourmetcaree.db.common.constants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.math.NumberUtils;

/**
 * エリアマスタの定義ファイル
 * @author Makoto Otani
 * @version 1.0
 *
 */
public interface MAreaConstants {

	/**
	 * エリアコードの初期値を保持するクラス
	 * @author Makoto Otani
	 */
	public static final class AreaCd {


		/** 初期値(首都圏) */
		public static final Integer SHUTOKEN_AREA = 1;

		public static final Integer SENDAI_AREA = 2;

		public static final Integer KANSAI_AREA = 3;

		public static final Integer TOKAI_AREA = 4;

		public static final Integer KYUSHU_AREA = 5;

		public static final List<Integer> AREA_CD_LIST = Arrays.asList(new Integer[]{SHUTOKEN_AREA, SENDAI_AREA, KANSAI_AREA, TOKAI_AREA, KYUSHU_AREA});

		public static final List<Integer> EAST_AREA_CD_LIST = Arrays.asList(new Integer[]{SHUTOKEN_AREA, SENDAI_AREA});

		public static final List<Integer> WEST_AREA_CD_LIST = Arrays.asList(new Integer[]{KANSAI_AREA, TOKAI_AREA, KYUSHU_AREA});

		/**
		 * 仙台かどうか
		 * @param areaCd エリアコード
		 * @return 仙台の場合にtrue
		 */
		public static boolean isSendai(String areaCd) {
			try {
				return isSendai(Integer.parseInt(areaCd));
			} catch (Exception e) {
				return false;
			}
		}


		/**
		 * 仙台かどうか
		 * @param areaCd エリアコード
		 * @return 仙台の場合にtrue
		 */
		public static boolean isSendai(Integer areaCd) {
			if (areaCd == null) {
				return false;
			}

			return areaCd.equals(SENDAI_AREA);
		}

		/**
		 * 東日本かどうか
		 * @param areaCd エリアコード
		 * @return 東日本の場合true, 西日本の場合false
		 */
		public static boolean isEastArea(Integer areaCd) {
			return EAST_AREA_CD_LIST.contains(areaCd);
		}
	}

	/**
	 * エリアコードのEnum
	 * @author ando
	 *
	 */
	public enum AreaCdEnum {
		SHUTOKEN_AREA(MAreaConstants.AreaCd.SHUTOKEN_AREA),
		SENDAI_AREA(AreaCd.SENDAI_AREA),
		KANSAI_AREA(AreaCd.KANSAI_AREA),
		TOKAI_AREA(AreaCd.TOKAI_AREA),
		KYUSHU_AREA(AreaCd.KYUSHU_AREA);


		/** エリアコード */
		private int value;

		/** コンストラクタ */
		private AreaCdEnum(int areaCd) {
			this.value = areaCd;
		};

		public int getValue() {
			return value;
		}

		/**
		 * 値からEnumを取得
		 * @param areaCd
		 * @return
		 */
		public static AreaCdEnum getEnum(int areaCd) {
			for (AreaCdEnum enm : AreaCdEnum.values()) {
				if (enm.value == areaCd) {
					return enm;
				}
			}
			return AreaCdEnum.SHUTOKEN_AREA;
		}
	}


	public static final class Prefix {

		/** 仙台 */
		public static final String SENDAI = "sendai";

		/** 仙台パス */
		public static final String SENDAI_PATH = "/".concat(SENDAI);



		public static final String getPrefix(int areaCd) {
			if (areaCd == AreaCd.SHUTOKEN_AREA.intValue()) {
				return "";
			}

			if (areaCd == AreaCd.SENDAI_AREA.intValue()) {
				return SENDAI;
			}

			return "";
		}

		public static final String getPrefixPath(String areaCd) {
			return getPrefixPath(NumberUtils.toInt(areaCd));
		}

		public static final String getPrefixPath(int areaCd) {
			if (areaCd == AreaCd.SHUTOKEN_AREA.intValue()) {
				return "";
			}

			if (areaCd == AreaCd.SENDAI_AREA.intValue()) {
				return SENDAI_PATH;
			}

			return "";
		}

		public static final String getRenewalAreaName(int areaCd) {
			if (areaCd == AreaCd.SHUTOKEN_AREA.intValue()) {
				return "kanto";
			}

			if (areaCd == AreaCd.SENDAI_AREA.intValue()) {
				return "tohoku";
			}

			if (areaCd == AreaCd.KANSAI_AREA.intValue()) {
				return "kansai";
			}

			if (areaCd == AreaCd.TOKAI_AREA.intValue()) {
				return "tokai";
			}

			if (areaCd == AreaCd.KYUSHU_AREA.intValue()) {
				return "kyushu-okinawa";
			}

			return "";
		}
	}

	/**
	 * 都道府県のエリア
	 * @author whizz
	 */
	public static final class AreaGroup {

		/** 都道府県をエリア単位でグループ化したMAP */
		public static final HashMap<String, HashMap<Integer, String>> areaGroupMap;
		public static final HashMap<Integer, String> prefecturesMap;
		static {
			HashMap<String, HashMap<Integer, String>> areaMap = new LinkedHashMap<>();
			HashMap<Integer, String> prefMap = new LinkedHashMap<>();

			HashMap<Integer, String> tohokufMap = new LinkedHashMap<>();
			tohokufMap.put(1, "北海道");
			tohokufMap.put(2, "青森県");
			tohokufMap.put(3, "岩手県");
			tohokufMap.put(4, "宮城県");
			tohokufMap.put(5, "秋田県");
			tohokufMap.put(6, "山形県");
			tohokufMap.put(7, "福島県");
			areaMap.put("北海道・東北", tohokufMap);
			prefMap.putAll(tohokufMap);

			HashMap<Integer, String> kantoMap = new LinkedHashMap<>();
			kantoMap.put(8, "茨城県");
			kantoMap.put(9, "栃木県");
			kantoMap.put(10, "群馬県");
			kantoMap.put(11, "埼玉県");
			kantoMap.put(12, "千葉県");
			kantoMap.put(13, "東京都");
			kantoMap.put(14, "神奈川県");
			areaMap.put("関東", kantoMap);
			prefMap.putAll(kantoMap);

			HashMap<Integer, String> tokaiMap = new LinkedHashMap<>();
			tokaiMap.put(15, "新潟県");
			tokaiMap.put(16, "富山県");
			tokaiMap.put(17, "石川県");
			tokaiMap.put(18, "福井県");
			tokaiMap.put(19, "山梨県");
			tokaiMap.put(20, "長野県");
			tokaiMap.put(21, "岐阜県");
			tokaiMap.put(22, "静岡県");
			tokaiMap.put(23, "愛知県");
			tokaiMap.put(24, "三重県");
			areaMap.put("東海・北陸", tokaiMap);
			prefMap.putAll(tokaiMap);

			HashMap<Integer, String> kansaiMap = new LinkedHashMap<>();
			kansaiMap.put(25,"滋賀県");
			kansaiMap.put(26,"京都府");
			kansaiMap.put(27,"大阪府");
			kansaiMap.put(28,"兵庫県");
			kansaiMap.put(29,"奈良県");
			kansaiMap.put(30,"和歌山県");
			areaMap.put("関西", kansaiMap);
			prefMap.putAll(kansaiMap);

			HashMap<Integer, String> chugokuMap = new LinkedHashMap<>();
			chugokuMap.put(31, "鳥取県");
			chugokuMap.put(32, "島根県");
			chugokuMap.put(33, "岡山県");
			chugokuMap.put(34, "広島県");
			chugokuMap.put(35, "山口県");
			chugokuMap.put(36, "徳島県");
			chugokuMap.put(37, "香川県");
			chugokuMap.put(38, "愛媛県");
			chugokuMap.put(39, "高知県");
			areaMap.put("中国・四国", chugokuMap);
			prefMap.putAll(chugokuMap);

			HashMap<Integer, String> kyushuMap = new LinkedHashMap<>();
			kyushuMap.put(40, "福岡県");
			kyushuMap.put(41, "佐賀県");
			kyushuMap.put(42, "長崎県");
			kyushuMap.put(43, "熊本県");
			kyushuMap.put(44, "大分県");
			kyushuMap.put(45, "宮崎県");
			kyushuMap.put(46, "鹿児島県");
			kyushuMap.put(47, "沖縄県");
			areaMap.put("九州・沖縄", kyushuMap);
			prefMap.putAll(kyushuMap);

			areaGroupMap = areaMap;
			prefecturesMap = prefMap;
		}

		/**
		 * 都道府県名を検索
		 * @param name 都道府県名(部分一致)
		 * @return 最初に一致する都道府県コード
		 */
		public static Integer getPrefCode(final String name) {
			for (Entry<Integer, String> entry : prefecturesMap.entrySet()) {
				String val = entry.getValue();
				if (entry.getKey() == 13) {
					val = "東京";
				} else if(entry.getKey() == 26) {
					val = "京都";
				} else {
					val = val.replaceAll("[府県]$", "");
				}
				if (val.equals(name)) {
					return entry.getKey();
				}
			}
			return null;
		}
	}
}
