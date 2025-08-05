package com.gourmetcaree.common.env;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.gourmetcaree.common.dto.BaseDto;

/**
 * 公開側のアプリケーションの基本情報を持つクラスです。
 * 各プロジェクトでコンポーネント化処理をapp.diconに記述し、
 * インジェクションをして使用してください。
 * ※自動的にコンポーネント化されるのを防ぐためにdtoパッケージ外に置いています。
 * @author Takahiro Ando
 * @version 1.0
 */
public class EnvDto extends BaseDto {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -6446267537186252781L;

	/** エリアコード */
	private int areaCd;

	/** 端末区分 */
	private int terminalKbn;

	/**
	 * エリアコードを取得します。
	 * @return エリアコード
	 */
	public int getAreaCd() {
	    return areaCd;
	}

	/**
	 * エリアコードを設定します。
	 * @param areaCd エリアコード
	 */
	public void setAreaCd(int areaCd) {
	    this.areaCd = areaCd;
	}

	/**
	 * 端末区分を取得します。
	 * @return 端末区分
	 */
	public int getTerminalKbn() {
	    return terminalKbn;
	}

	/**
	 * 端末区分を設定します。
	 * @param terminalKbn 端末区分
	 */
	public void setTerminalKbn(int terminalKbn) {
	    this.terminalKbn = terminalKbn;
	}

	/*
	 * (非 Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return ToStringBuilder
				.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
	}
}
