package com.gourmetcaree.admin.pc.ajax.form.ajax;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

/**
 * AjaxのForm
 * @author Katsutoshi Hasegawa
 * @version 1.0
 */
@Component(instance = InstanceType.REQUEST)
public class IndexForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -5592467385759532594L;

	/** Ajax用 限定値 */
	public String limitValue;

	/** Ajax用 区分値 */
	public String typeCd;

	/** Ajax用 区分値2 */
	public String typeCd2;

	/** Ajax用 権限 */
	public String authLevel;

	/** Ajax用 disabledフラグ */
	public boolean disabledFlg;

	/** プロパティ名 */
	public String propertyName;

	/** 番号 */
	public String number;

	/** 非表示リスト */
	public List<Integer> noDisplayList = new ArrayList<Integer>();

	/**
	 * リセットを行う
	 */
	public void resetForm() {
		limitValue = null;
		typeCd = null;
		typeCd2 = null;
		authLevel = null;
		disabledFlg = false;
		noDisplayList = new ArrayList<Integer>();
		propertyName = null;
		number = null;
	}
}
