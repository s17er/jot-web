package com.gourmetcaree.db.common.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * WEBデータのエンティティクラスです。
 *
 * @version 1.0
 */
@Entity
@Table(name = "t_temp_web")
public class TTempWeb extends AbstractCommonEntity {

	private static final long serialVersionUID = 3468644982798582202L;

    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_temp_web_id_gen")
    @SequenceGenerator(name = "t_temp_web_id_gen", sequenceName = "t_temp_web_id_seq", allocationSize = 1)
    @Column(name = "id")
    public Integer id;

    /**
     * WEB ID
     */
    @Column(name = "web_id")
    public Integer webId;

    /**
     * JSONデータ
     */
    @Column(name = "json_data")
    public String jsonData;

    /**
     * 保存日時
     */
    @Column(name = "save_datetime")
    public Timestamp saveDatetime;

    /**
     * テーブル名
     */
    public static final String TABLE_NAME = "t_temp_web";

    /**
     * ID
     */
    public static final String ID = "id";

    /**
     * web_id
     */
    public static final String WEB_ID = "web_id";

    /**
     * json_data
     */
    public static final String JSON_DATA = "json_data";

    /**
     * save_datetime
     */
    public static final String SAVE_DATETIME = "save_datetime";
}
