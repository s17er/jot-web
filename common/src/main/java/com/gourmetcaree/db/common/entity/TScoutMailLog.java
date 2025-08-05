package com.gourmetcaree.db.common.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="t_scout_mail_log")
public class TScoutMailLog extends AbstractCommonMasqueradeEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -1065812504659591526L;

	 /** ID */
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_scout_mail_log_id_gen")
    @SequenceGenerator(name="t_scout_mail_log_id_gen", sequenceName="t_scout_mail_log_id_seq", allocationSize=1)
    @Column(name="id")
    public Integer id;

    /** スカウト管理ID */
    @Column(name="scout_manage_id")
    public Integer scoutManageId;

    /** 会員ID */
    @Column(name="member_id")
    public Integer memberId;

    /** 送信日時 */
    @Column(name="send_datetime")
    public Timestamp sendDatetime;

    /** 追加数 */
    @Column(name="add_scout_count")
    public Integer addScoutCount;

    /** 追加日時 */
    @Column(name="add_datetime")
    public Timestamp addDatetime;

    /** スカウトメールログ区分 */
    @Column(name="scout_mail_log_kbn")
    public Integer scoutMailLogKbn;

    /** メモ */
    @Column(name = MEMO)
    public String memo;

    /** メモ更新日時 */
    @Column(name = MEMO_UPDATE_DATETIME)
    public Timestamp memoUpdateDatetime;

    /** 選考フラグ */
    @Column(name = SELECTION_FLG)
    public Integer selectionFlg;



    /** テーブル名 */
    public static final String TABLE_NAME = "t_scout_mail_log";

    /** ID */
    public static final String ID = "id";

    /** スカウト管理ID */
    public static final String SCOUT_MANAGE_ID = "scout_manage_id";

    /** 会員ID */
    public static final String MEMBER_ID = "member_id";

    /** 送信日時 */
    public static final String SEND_DATETIME = "send_datetime";

    /** 追加数 */
    public static final String ADD_SCOUT_COUNT = "add_scout_count";

    /** 追加日時 */
    public static final String ADD_DATETIME = "add_datetime";

    /** ログ区分 */
    public static final String SCOUT_MAIL_LOG_KBN = "scout_mail_log_kbn";

    /** メモ */
    public static final String MEMO = "memo";

    /** メモ更新日時 */
    public static final String MEMO_UPDATE_DATETIME = "memo_update_datetime";

    /** 選考フラグ */
    public static final String SELECTION_FLG = "selection_flg";


}
