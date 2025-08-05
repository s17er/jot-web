package com.gourmetcaree.db.common.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * ターミナルマスタのエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="m_terminal")
public class MTerminal extends AbstractCommonEntity implements Serializable {

    /** シリアルバージョンUID */
    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="m_terminal_id_gen")
    @SequenceGenerator(name="m_terminal_id_gen", sequenceName="m_terminal_id_seq", allocationSize=1)
    @Column(name="id")
    public Integer id;

    /** ターミナル名 */
    @Column(name="terminal_name")
    public String terminalName;

    /** 都道府県コード */
    @Column(name="prefectures_cd")
    public Integer prefecturesCd;

    /** ターミナル駅 */
    @OneToMany(mappedBy = "mTerminal")
    public List<MTerminalStation> mTerminalStation;

    public static final String ID = "id";

    public static final String TERMINAL_NAME = "terminal_name";

    public static final String PREFECTURES_CD = "prefectures_cd";

    public static final String M_TERMINAL_STATION = "mTerminalStation";


}