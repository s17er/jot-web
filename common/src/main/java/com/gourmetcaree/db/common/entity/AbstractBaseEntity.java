package com.gourmetcaree.db.common.entity;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

/**
 * エンティティの共通項目を保持するクラスです。
 * @author Takahiro Ando
 * @version 1.0
 */
@MappedSuperclass
public abstract class AbstractBaseEntity implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -7913861099182506304L;

}
