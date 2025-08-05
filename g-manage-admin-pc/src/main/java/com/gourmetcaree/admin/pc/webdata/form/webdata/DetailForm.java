package com.gourmetcaree.admin.pc.webdata.form.webdata;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.admin.service.dto.WebdataControlDto;

/**
 * WEBデータ詳細のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
@Component(instance = InstanceType.REQUEST)
public class DetailForm extends WebdataForm {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 6222157013184557085L;

	/** 画面制御を行うためのフラグを保持するDto */
	public WebdataControlDto controlDto = new WebdataControlDto();

	/** 編集のパス */
	public String editPath;

	/** 応募テストのパス */
	public String appTestPath;

	/** メーラのsubject */
	public String mailerSubject;

	/** メーラの本文 */
	public String mailerBody;

	/** プレビューURL */
	public String previewUrl;

	/** プレビューURL */
	public String listPreviewUrl;

	public String previewMessage;

	/** ライト版プレビューURL */
	public String lightPreviewUrl;

	/**
	 * フォームのリセットを行う
	 */
	@Override
	public void resetForm() {

		super.resetBaseForm();
		super.resetForm();

		// 画面制御を行うためのフラグを保持するDto
		controlDto = new WebdataControlDto();

		// 編集のパス
		editPath = null;

		// 応募テストのパス
		appTestPath = null;

		// メーラのsubject
		mailerSubject = null;

		// メーラの本文
		mailerBody = null;
	}

}
