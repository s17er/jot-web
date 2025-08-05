package com.gourmetcaree.arbeitsys.constants;

import com.gourmetcaree.db.common.constants.MstTodouhukenConstants;
import org.apache.commons.lang.math.NumberUtils;

import com.gourmetcaree.common.util.GourmetCareeUtil;


/**
 * アルバイトマスタの定義ファイル
 *
 * @author Tetsuya Kaneko
 * @version 1.0
 */
public interface MArbeitConstants {


    /**
     * アルバイトリンクのサイト定数の保持するクラス
     *
     * @author Tetsuya Kaneko
     */
    enum ArbeitSite {

        /**
         * 首都圏
         */
        SHUTOKEN_SITE_CONST("G000"),

        /**
         * 関西
         */
        KANSAI_SITE_CONST("G001"),

        /**
         * 仙台
         */
        SENDAI_SITE_CONST("G002"),;

        private String value;

        ArbeitSite(String value) {
            this.value = value;
        }


        public String getValue() {
            return value;
        }

        /**
         * アルバイトリンク用サイト定数を返却する
         */
        public static String getArbeitSiteConstValue(int pref) {
            return getArbeitSiteEnum(pref).getValue();
        }


        /**
         * アルバイトリンク用サイト定数を返却する
         */
        public static ArbeitSite getArbeitSiteEnum(Object pref) {
            if (pref == null) {
                return SHUTOKEN_SITE_CONST;
            }
            if (MArbeitConstants.ShutokenSiteEnum.isShutokenSite(pref)) {
                return SHUTOKEN_SITE_CONST;
            } else if (MArbeitConstants.KansaiSiteEnum.isKansaiSite(pref)) {
                return KANSAI_SITE_CONST;
            } else if (MArbeitConstants.SendaiSiteEnum.isSendaiSite(pref)) {
                return SENDAI_SITE_CONST;
            }
            return SHUTOKEN_SITE_CONST;
        }

        /**
         * アルバイトリンク用サイト定数を返却する
         */
        public static String getArbeitSiteConst(Object pref) {
            return getArbeitSiteEnum(pref).value;
        }


        /**
         * 値からenumを取得
         */
        public static ArbeitSite getEnumFromValue(String value) {
            for (ArbeitSite en : ArbeitSite.values()) {
                if (en.value.equals(value)) {
                    return en;
                }
            }

            return null;
        }


        /**
         * エリアの種類に含まれるかどうか
         *
         * @param todouhukenId 都道府県ID
         * @return 含まれる場合にtrue
         */
        public static boolean isKindOfArea(String todouhukenId) {
            return isKindOfArea(NumberUtils.toInt(todouhukenId));
        }

        /**
         * エリアの種類に含まれるかどうか
         *
         * @param todouhukenId 都道府県ID
         * @return 含まれる場合にtrue
         */
        public static boolean isKindOfArea(int todouhukenId) {
            return ShutokenSiteEnum.isShutokenSite(todouhukenId)
                    || KansaiSiteEnum.isKansaiSite(todouhukenId)
                    || SendaiSiteEnum.isSendaiSite(todouhukenId);
        }
    }

    /**
     * アルバイトリンクの店舗が属するエリア定数を保持するクラス
     *
     * @author Mutsumi Inada
     */

    enum ArbeitShopArea {
        /**
         * 首都圏コード
         */
        SHUTOKEN_SHOP_AREA_CONST("0"),

        /**
         * 関西コード
         */
        KANSAI_SHOP_AREA_CONST("1"),

        /**
         * 仙台コード
         */
        SENDAI_SHOP_AREA_CONST("2"),;

        private String value;

        ArbeitShopArea(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        /**
         * アルバイトリンク用店舗エリア定数を返却する
         */
        public static String getArbeitShopAreaConstValue(int pref) {
            return getArbeitShopAreaEnum(pref).getValue();
        }

        /**
         * アルバイトリンク用店舗エリア定数を返却する
         *
         * @param pref
         * @return
         */
        public static ArbeitShopArea getArbeitShopAreaEnum(Object pref) {
            if (pref == null) {
                return SHUTOKEN_SHOP_AREA_CONST;
            }
            if (MArbeitConstants.ShutokenSiteEnum.isShutokenSite(pref)) {
                return SHUTOKEN_SHOP_AREA_CONST;
            } else if (MArbeitConstants.KansaiSiteEnum.isKansaiSite(pref)) {
                return KANSAI_SHOP_AREA_CONST;
            } else if (MArbeitConstants.SendaiSiteEnum.isSendaiSite(pref)) {
                return SENDAI_SHOP_AREA_CONST;
            }
            return SHUTOKEN_SHOP_AREA_CONST;
        }

    }

    /**
     * 首都圏サイトのEnum
     *
     * @author Tetsuya Kaneko
     */
    enum ShutokenSiteEnum {

        SAITAMA("11"),
        CHIBA("12"),
        TOKYO("13"),
        KANAGAWA("14");

        /**
         * サイト定数
         */
        private String value;

        /**
         * コンストラクタ
         */
        ShutokenSiteEnum(String pref) {
            this.value = pref;
        }


        public String getValue() {
            return value;
        }

        /**
         * 値からEnumを取得
         */
        public static ShutokenSiteEnum getEnum(Object pref) {
            for (ShutokenSiteEnum enm : ShutokenSiteEnum.values()) {
                if (GourmetCareeUtil.eqString(enm.value, pref)) {
                    return enm;
                }
            }
            return null;
        }

        /**
         * 首都圏に属する都道府県か判定する
         */
        public static boolean isShutokenSite(Object pref) {
            if (getEnum(pref) != null) {
                return true;
            }
            return false;
        }
    }

    /**
     * 関西サイトのEnum
     *
     * @author Tetsuya Kaneko
     */
    public enum KansaiSiteEnum {

        SHIGA("25"),
        KYOTO("26"),
        OSAKA("27"),
        HYOGO("28"),
        NARA("29"),
        WAKAYAMA("30");

        /**
         * サイト定数
         */
        private String value;

        /**
         * コンストラクタ
         */
        private KansaiSiteEnum(String pref) {
            this.value = pref;
        }

        ;

        public String getValue() {
            return value;
        }

        /**
         * 値からEnumを取得
         */
        public static KansaiSiteEnum getEnum(Object pref) {
            for (KansaiSiteEnum enm : KansaiSiteEnum.values()) {
                if (GourmetCareeUtil.eqString(enm.value, pref)) {
                    return enm;
                }
            }
            return null;
        }

        /**
         * 関西に属する都道府県か判定する
         */
        public static boolean isKansaiSite(Object pref) {
            if (getEnum(pref) != null) {
                return true;
            }
            return false;
        }

    }

    /**
     * 仙台サイトのEnum
     *
     * @author Tetsuya Kaneko
     */
    enum SendaiSiteEnum {

        HOKKAIDO(String.valueOf(MstTodouhukenConstants.HOKKAIDO)),
        AOMORI(String.valueOf(MstTodouhukenConstants.AOMORI)),
        IWATE(String.valueOf(MstTodouhukenConstants.IWATE)),
        MIYAGI(String.valueOf(MstTodouhukenConstants.MIYAGI)),
        AKITA(String.valueOf(MstTodouhukenConstants.AKITA)),
        YAMAGATA(String.valueOf(MstTodouhukenConstants.YAMAGATA)),
        FUKUSHIMA(String.valueOf(MstTodouhukenConstants.FUKUSHIMA)),
        ;

        /**
         * サイト定数
         */
        private String value;

        /**
         * コンストラクタ
         */
        SendaiSiteEnum(String pref) {
            this.value = pref;
        }

        public String getValue() {
            return value;
        }

        /**
         * 値からEnumを取得
         */
        public static SendaiSiteEnum getEnum(Object pref) {
            for (SendaiSiteEnum enm : SendaiSiteEnum.values()) {
                if (GourmetCareeUtil.eqString(enm.value, pref)) {
                    return enm;
                }
            }
            return null;
        }

        /**
         * 仙台に属する都道府県か判定する
         */
        public static boolean isSendaiSite(Object pref) {
            if (getEnum(pref) != null) {
                return true;
            }
            return false;
        }
    }
}
