package com.gourmetcaree.admin.pc.webdata.form.lumpDelete;

import java.io.Serializable;
import java.util.List;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.admin.pc.webdata.dto.webdata.ListDto;
import com.gourmetcaree.common.form.BaseForm;

/**
 * WEBデータ一括削除のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
@Component(instance = InstanceType.REQUEST)
public class DeleteForm extends BaseForm implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 5042208377097061886L;

	/** WEBデータID */
	public String[] webId;

	/** 一覧表示用リスト */
	public List<ListDto> dtoList;

	/**
	 * リセットを行う
	 */
	public void resetForm() {

		super.resetBaseForm();
		webId = null;
		dtoList = null;
	}

}
