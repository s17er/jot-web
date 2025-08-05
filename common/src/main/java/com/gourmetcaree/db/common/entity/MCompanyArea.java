package com.gourmetcaree.db.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 会社エリアマスタのエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="m_company_area")
public class MCompanyArea extends AbstractCommonEntity implements Serializable {

    /** シリアルバージョンUID */
    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="m_company_area_id_gen")
    @SequenceGenerator(name="m_company_area_id_gen", sequenceName="m_company_area_id_seq", allocationSize=1)
    @Column(name="id")
    public Integer id;

    /** 会社ID */
    @Column(name="company_id")
    public Integer companyId;

    /** エリアコード */
    @Column(name="area_cd")
    public Integer areaCd;

    @ManyToOne
    @JoinColumn(name="company_id")
    public MCompany mCompany;

    /** テーブル名 */
    public static final String TABLE_NAME = "m_company_area";

    /** ID */
    public static final String ID ="id";

    /** 会社ID */
    public static final String COMPANY_ID ="company_id";

    /** エリアコード */
    public static final String AREA_CD ="area_cd";
}