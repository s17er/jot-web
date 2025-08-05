package com.gourmetcaree.admin.pc.preview.form.shopListPreview;

import com.gourmetcaree.admin.pc.preview.form.listPreview.ListForm.ImgMethodKbn;
import com.gourmetcaree.admin.pc.preview.form.listPreview.PreviewBaseForm;


/**
 * 店舗一覧スマホ用プレビューフォーム
 * @author Takehiro Nakamori
 *
 */
public class SmartListForm extends PreviewBaseForm{

	/**
	 *
	 */
	private static final long serialVersionUID = 6005075108940950330L;

	/** 顧客ID */
	public Integer customerId;

	/** webID */
	public String webId;

	/** エリアコード */
	public String areaCd;

	/** 原稿名 */
	public String manuscriptName;

	/** 対象ページ */
	public String targetPage;

	/** 画像メソッド区分 */
	public ImgMethodKbn imgMethodKbn;

    /** 掲載開始日時 */
    public String postStartDatetime;

    /** 掲載終了日時 */
    public String postEndDatetime;

	public String frontHttpUrl;

	public String recruitmentJob;

	public String salary;
}
