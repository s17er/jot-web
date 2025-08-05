
-- レポート一覧の詳細(営業担当者なし)で使用するSQL
-- explain analyze
SELECT
        COM.id AS company_id,
        null   AS sales_id,
        SUB.display_status,
        SUB.status_count
FROM
	m_company AS COM
	INNER JOIN (
		SELECT
		        VWEB.display_status,
		        VWEB.company_id,
		        COUNT(VWEB.display_status) AS status_count
		    FROM
		        v_web_list AS VWEB
		    WHERE
		        VWEB.volume_id = /*volumeId*/1
		        AND VWEB.sales_id IS NULL
		        AND VWEB.company_id = 1 -- ジェイオフィス固定
		    GROUP BY
		    	VWEB.company_id,
		    	VWEB.display_status
	) AS SUB ON SUB.company_id = COM.id
WHERE
	COM.id = 1 -- ジェイオフィス固定
	AND COM.delete_flg = /*deleteFlg*/0

ORDER BY
	COM.display_order DESC,
	COM.id DESC,
	SUB.display_status ASC