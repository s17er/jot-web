
-- レポート一覧の詳細で使用するSQL
-- explain analyze
SELECT
        COM.id AS company_id,
        SALES.id AS sales_id,
        SUB.display_status,
        SUB.status_count
FROM
	m_sales AS SALES
	INNER JOIN m_company AS COM ON COM.id = SALES.company_id
INNER JOIN (
SELECT
        VWEB.display_status,
        VWEB.sales_id,
        COUNT(VWEB.display_status) AS status_count
    FROM
        v_web_list AS VWEB
    WHERE
        VWEB.volume_id = /*volumeId*/5
    GROUP BY
    	VWEB.sales_id,
    	VWEB.display_status
) AS SUB ON SUB.sales_id = SALES.id
WHERE
	COM.delete_flg = /*deleteFlg*/0
	AND SALES.delete_flg = /*deleteFlg*/0
ORDER BY
	COM.display_order DESC,
	COM.id DESC,
	SUB.sales_id DESC,
	SUB.display_status ASC
