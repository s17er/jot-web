package com.gourmetcaree.shop.pc.preview.form.shopListPreview;

import com.gourmetcaree.shop.pc.preview.form.listPreview.ListForm.PreviewMethodKbn;
import com.gourmetcaree.shop.pc.preview.form.listPreview.PreviewBaseForm;

/**
 * スマホ用店舗一覧ドラフトプレビューフォーム
 * @author Takehiro Nakamori
 *
 */
public class SmartDraftListForm extends PreviewBaseForm {


	/**
	 *
	 */
	private static final long serialVersionUID = 3761461075414558482L;

	/** 顧客ID */
	public Integer customerId;

	public String accessCd;

	public String areaCd;

	/** webID */
	public String webId;

	/** 原稿名 */
	public String manuscriptName;

	/** 入力フォーム区分 */
	public String inputFormKbn;

	/** 対象ページ */
	public String targetPage;

    /** 掲載開始日時 */
    public String postStartDatetime;

    /** 掲載終了日時 */
    public String postEndDatetime;

    /** プレビュー区分 */
	public PreviewMethodKbn previewMethodKbn;

	/** アルバイトプレビューパス */
	public String arbeitPreviewPath;

	/** 公開側httpdURL */
	public String frontHttpUrl;

	public String recruitmentJob;

	public String salary;
}
