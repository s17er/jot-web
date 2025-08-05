package com.gourmetcaree.db.common.entity;

import com.google.common.collect.Lists;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

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
 * WEBデータ属性のエンティティクラスです。
 * @version 1.0
 */
@Entity
@Table(name="t_web_attribute")
public class TWebAttribute extends AbstractCommonEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** ID */
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="t_web_attribute_id_gen")
	@SequenceGenerator(name="t_web_attribute_id_gen", sequenceName="t_web_attribute_id_seq", allocationSize=1)
	@Column(name="id")
	public Integer id;

	/** WEBデータID */
	@Column(name="web_id")
	public Integer webId;

	/** 属性コード */
	@Column(name="attribute_cd")
	public String attributeCd;

	/** 属性値 */
	@Column(name="attribute_value")
	public Integer attributeValue;

	/** 対象外フラグ */
	@Column(name="not_target_flg")
	public Integer notTargetFlg;

	/** WEBデータ */
	@ManyToOne
	@JoinColumn(name="web_id")
	public TWeb tWeb;


	/** テーブル名 */
	public static final String TABLE_NAME = "t_web_attribute";

	/** ID */
	public static final String ID ="id";

	/** WEBデータID */
	public static final String WEB_ID ="web_id";

	/** 属性コード */
	public static final String ATTRIBUTE_CD ="attribute_cd";

	/** 属性値 */
	public static final String ATTRIBUTE_VALUE ="attribute_value";

	/** 対象外フラグ */
	public static final String NOT_TARGET_FLG = "not_target_flg";

	/** WEBデータ */
	public static final String T_WEB ="t_web";


	/**
	 * 属性コード・属性値から新しいインスタンスを生成
	 * @param attributeCd 属性コード
	 * @param attributeValue 属性値
	 * @return インスタンス
	 */
	public static TWebAttribute newInstance(String attributeCd, int attributeValue) {
		TWebAttribute attribute = new TWebAttribute();
		attribute.attributeCd = attributeCd;
		attribute.attributeValue = attributeValue;
		return attribute;
	}

    /**
     * 属性コード・属性値のコレクションから新しいインスタンスを生成
     * @param attributeCd 属性コード
     * @param attributeValues 属性値のコレクション
     * @return インスタンス
     */
    public static List<TWebAttribute> newInstanceList(String attributeCd, Collection<Integer> attributeValues) {
	    List<TWebAttribute> list = Lists.newArrayList();
	    for (Integer value : attributeValues) {
	        list.add(newInstance(attributeCd, value));
        }
        return list;
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}