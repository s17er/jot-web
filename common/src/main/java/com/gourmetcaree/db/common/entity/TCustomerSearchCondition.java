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
@Table(name="t_customer_search_condition")
public class TCustomerSearchCondition extends AbstractCommonEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_customer_search_condition_id_gen")
	@SequenceGenerator(name="t_customer_search_condition_id_gen", sequenceName="t_customer_search_condition_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** 顧客ID */
	@Column(name="customer_id")
	public Integer customerId;

	/** 検索条件 */
	@Column(name="search_condition")
	public String searchCondition;

	/** 保存日時 */
	@Column(name="save_datetime")
	public Timestamp saveDatetime;

	/** テーブル名 */
	public static final String TABLE_NAME = "t_customer_search_condition";

	/** 顧客ID */
	public static final String CUSTOMER_ID = "customer_id";

	/** 検索条件 */
	public static final String SEARCH_CONDITION = "search_consition";

	/** 保存日時 */
	public static final String SAVE_DATETIME = "save_datetime";

}
