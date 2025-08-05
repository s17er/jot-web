
-- レポート一覧で使用するSQL
-- explain analyze
SELECT
        VOL.id,
        VOL.volume,
        VOL.post_start_datetime,
        VOL.post_end_datetime,
        SUB.display_status,
        SUB.status_count
FROM
	m_volume AS VOL
INNER JOIN (
SELECT
        VWEB.volume_id,
        VWEB.display_status,
        COUNT(VWEB.display_status) AS status_count
    FROM
        v_web_list AS VWEB
    WHERE
        VWEB.volume_id IS NOT NULL
        AND VWEB.area_cd = /*areaCd*/1
    GROUP BY
    	VWEB.volume_id,
    	VWEB.display_status
) AS SUB ON SUB.volume_id = VOL.id
WHERE
	VOL.delete_flg = /*deleteFlg*/0
ORDER BY
	VOL.volume DESC,
	SUB.display_status ASC
/*IF noLimitFlg != true*/
LIMIT /*limitNum*/20
/*END*/