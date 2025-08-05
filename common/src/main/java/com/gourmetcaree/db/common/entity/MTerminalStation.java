package com.gourmetcaree.db.common.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.seasar.extension.jdbc.annotation.ReferentialConstraint;

/**
 * ターミナルステーションマスタのエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="m_terminal_station")
public class MTerminalStation extends AbstractCommonEntity implements Serializable {

    /** シリアルバージョンUID */
    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="m_terminal_station_id_gen")
    @SequenceGenerator(name="m_terminal_station_id_gen", sequenceName="m_terminal_station_id_seq", allocationSize=1)
    @Column(name="id")
    public Integer id;

    /**ターミナルID */
    @Column(name="terminal_id")
    public Integer terminalId;

    /** 駅コード */
    @Column(name="station_cd")
    public Integer stationCd;

    /** 表示順 */
    @Column(name="display_order")
    public Integer displayOrder;

    @ManyToOne
    @JoinColumn(name="id")
    public MTerminal mTerminal;

    /** 駅グループビュー */
	@ReferentialConstraint(enable = false)
	@OneToOne
	@JoinColumn(name = STATION_CD, referencedColumnName = STATION_CD)
    public VStationGroup vStationGroup;

    public static final String ID = "id";

    public static final String TERMINAL_ID = "terminal_id";

    public static final String STATION_CD = "station_cd";

    public static final String DISPLAY_ORDER = "display_order";

}
