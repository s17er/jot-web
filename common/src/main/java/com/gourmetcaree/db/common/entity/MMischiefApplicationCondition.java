package com.gourmetcaree.db.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * いたずら応募条件のエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="m_mischief_application_condition")
public class MMischiefApplicationCondition extends AbstractCommonEntity implements Serializable {

    /** シリアルバージョンUID */
    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="m_mischief_application_condition_id_gen")
    @SequenceGenerator(name="m_mischief_application_condition_id_gen", sequenceName="m_mischief_application_condition_id_seq", allocationSize=1)
    @Column(name="id")
    public Integer id;

    /** 氏名 */
    @Column(name="name")
    public String name;

    /** 氏名（カナ） */
    @Column(name="name_kana")
    public String nameKana;

    /** 都道府県コード */
    @Column(name="prefectures_cd")
    public Integer prefecturesCd;

    /** 市区町村 */
    @Column(name="municipality")
    public String municipality;

    /** 住所 */
    @Column(name="address")
    public String address;

    /** 電話番号1 */
    @Column(name="phone_no1")
    public String phoneNo1;

    /** 電話番号2 */
    @Column(name="phone_no2")
    public String phoneNo2;

    /** 電話番号3 */
    @Column(name="phone_no3")
    public String phoneNo3;

    /** メールアドレス */
    @Column(name="mail_address")
    public String mailAddress;

    /** 会員フラグ */
    @Column(name="member_flg")
    public Integer memberFlg;

    /** 端末区分 */
    @Column(name="terminal_kbn")
    public Integer terminalKbn;

    /** 自己PR */
    @Column(name="application_self_pr")
    public String applicationSelfPr;

    /** テーブル名 */
    public static final String TABLE_NAME = "m_mischief_application_condition";

    /** ID */
    public static final String ID ="id";

    /** 氏名 */
    public static final String NAME = "name";

    /** 氏名（カナ） */
    public static final String NAME_KANA = "name_kana";

    /** 都道府県コード */
    public static final String PREFECTURES_CD = "prefectures_cd";

    /** 市区町村 */
    public static final String MUNICIPALITY = "municipality";

    /** 住所 */
    public static final String ADDRESS = "address";

    /** 電話番号1 */
    public static final String PHONE_NO1 = "phone_no1";

    /** 電話番号2 */
    public static final String PHONE_NO2 = "phone_no2";

    /** 電話番号3 */
    public static final String PHONE_NO3 = "phone_no3";

    /** メールアドレス */
    public static final String MAIL_ADDRESS = "mail_address";

    /** 会員フラグ */
    public static final String MEMBRE_FLG = "member_flg";

    /** 端末区分 */
    public static final String TERMINAL_KBN = "terminal_kbn";

    /** 自己PR */
    public static final String APPLICATION_SELF_PR = "application_self_pr";

}