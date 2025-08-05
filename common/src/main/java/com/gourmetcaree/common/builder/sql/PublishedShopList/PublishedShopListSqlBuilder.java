package com.gourmetcaree.common.builder.sql.PublishedShopList;

import com.gourmetcaree.common.builder.sql.SqlBuilder;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;

/**
 * 掲載中の店舗情報を取得するSQLを作成する
 *
 * @author ZRX
 *
 */
public class PublishedShopListSqlBuilder implements SqlBuilder {

    /** エリアコード */
    private final int areaCd;

    public PublishedShopListSqlBuilder(int areaCd) {
        super();
        this.areaCd = areaCd;
    }

    @Override
    public SqlCondition build() {
        StringBuilder sql = new StringBuilder();

		sql.append("  SELECT ");
		sql.append("      vrw.id as web_id ");
		sql.append("      , vrw.manuscript_name as web_manuscript_name ");
		sql.append("      , vrw.area_cd as web_area_cd ");
		sql.append("      , mcus.id as customer_id ");
		sql.append("      , mcus.customer_name as customer_name ");
		sql.append("      , mcus.zip_cd as customer_zip_cd ");
		sql.append("      , mcus.prefectures_cd as customer_prefectures_cd ");
		sql.append("      , mcus.municipality as customer_municipality ");
		sql.append("      , mcus.address as customer_address ");
		sql.append("      , mcus.phone_no1 as customer_phone_no1 ");
		sql.append("      , mcus.phone_no2 as customer_phone_no2 ");
		sql.append("      , mcus.phone_no3 as customer_phone_no3 ");
		sql.append("      , tshp.id as shop_id ");
		sql.append("      , tshp.shop_name ");
		sql.append("      , tshp.industry_kbn1 as shop_industry_kbn1 ");
		sql.append("      , tshp.industry_kbn2 as shop_industry_kbn2 ");
		sql.append("      , tshp.domestic_kbn as shop_domestic_kbn ");
		sql.append("      , tshp.shutoken_foreign_area_kbn as shop_shutoken_foreign_area_kbn ");
		sql.append("      , tshp.foreign_address as shop_foreign_address ");
		sql.append("      , tshp.prefectures_cd as shop_prefectures_cd ");
		sql.append("      , tshp.city_cd as shop_city_cd ");
		sql.append("      , tshp.address as shop_address ");
		sql.append("      , tshp.csv_phone_no as shop_csv_phone_no ");
		sql.append("      , tshp.transit as shop_transit ");
		sql.append("      , tshp.shop_information ");
		sql.append("      , tshp.holiday as shop_holiday ");
		sql.append("      , tshp.business_hours as shop_business_hours ");
		sql.append("      , tshp.seat_kbn as shop_seat_kbn ");
		sql.append("      , tshp.sales_per_customer_kbn as shop_sales_per_customer_kbn ");
		sql.append("      , tshp.job_offer_flg as shop_job_offer_flg ");
		sql.append("  FROM ");
		sql.append("      v_release_web as vrw  ");
		sql.append("      INNER JOIN t_web_shop_list AS twsl ");
		sql.append("        ON (vrw.id = twsl.web_id) ");
		sql.append("      INNER JOIN t_shop_list as tshp  ");
		sql.append("        ON (twsl.shop_list_id::integer = tshp.id )  ");
		sql.append("      INNER JOIN m_customer as mcus  ");
		sql.append("        ON (tshp.customer_id = mcus.id)  ");
		sql.append("  WHERE ");
		sql.append("      twsl.delete_flg = ? AND ");
		sql.append("      vrw.delete_flg = ? AND ");
		sql.append("      mcus.delete_flg = ? AND ");
		sql.append("      tshp.delete_flg = ? AND ");
		sql.append("      vrw.area_cd = ? AND ");
		sql.append("      mcus.area_cd = ? AND ");
		sql.append("      tshp.area_cd = ? ");
		sql.append("  ORDER BY customer_id, web_id, shop_id ");

        return new SqlCondition(sql,
                DeleteFlgKbn.NOT_DELETED,
                DeleteFlgKbn.NOT_DELETED,
                DeleteFlgKbn.NOT_DELETED,
                DeleteFlgKbn.NOT_DELETED,
                areaCd,
                areaCd,
                areaCd
                );
    }

}
