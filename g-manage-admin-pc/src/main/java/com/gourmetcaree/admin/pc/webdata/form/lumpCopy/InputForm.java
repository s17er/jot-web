package com.gourmetcaree.admin.pc.webdata.form.lumpCopy;

import java.io.Serializable;
import java.util.List;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.admin.pc.webdata.dto.webdata.ListDto;
import com.gourmetcaree.common.form.BaseForm;

/**
 * WEBデータ一括コピーのフォーム
 * @author Makoto Otani
 * @version 1.0
 */
@Component(instance = InstanceType.SESSION)
public class InputForm extends BaseForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -2164219508861723838L;

	/** WEBデータID */
	public String[] webId;

	/** エリアコード */
	public String areaCd;

	/** 号数 */
	public String volumeAll;

	/** 一覧表示用リスト */
	public List<ListDto> dtoList;


	/**
	 * リセットを行う
	 */
	public void resetForm() {
		super.resetBaseForm();
		webId = null;
		areaCd = null;
		volumeAll = null;
		dtoList = null;

	}
}
