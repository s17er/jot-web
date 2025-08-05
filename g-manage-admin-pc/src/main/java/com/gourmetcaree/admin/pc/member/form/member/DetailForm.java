package com.gourmetcaree.admin.pc.member.form.member;

import java.io.Serializable;

/**
 * 会員詳細のフォーム
 * @author Makoto Otani
 * @version 1.0
 */
public class DetailForm extends MemberForm implements Serializable {


	/** シリアルバージョンUID */
	private static final long serialVersionUID = 2354980046909897059L;


	/** 編集パス */
	public String editPath;

	/** 削除パス */
	public String deletePath;

	/** 気になる一覧からの遷移フラグ */
	public boolean interestListFlg;

	/**
	 * リセットを行う
	 */
	public void resetForm() {

		super.resetForm();
		editPath = null;

		deletePath = null;

		interestListFlg = false;

		gourmetMagazineReceptionDisplayFlg = false;

	}

}