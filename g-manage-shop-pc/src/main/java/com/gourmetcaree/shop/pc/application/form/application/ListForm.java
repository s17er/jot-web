package com.gourmetcaree.shop.pc.application.form.application;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;
import org.seasar.struts.util.MessageResourcesUtil;

/**
 * 応募者一覧フォームです。
 * @author Takahiro Kimura
 * @version 1.0
 */
@Component(instance=InstanceType.SESSION)
public class ListForm extends ApplicationForm {

	/**
	 * 一覧を全て表示するか、Webデータで絞るかどうかのフラグを持つ列挙型
	 * @author ando
	 */
	public enum ListDisplayKbn {
		/** WebDataで絞る */
		WEB_DATA,
		/** 全てを表示する */
		ALL;
	}

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -3996070001396205172L;

	/** メモ(一覧部分のサブミット用) */
	public String memo;

	/** 原稿ID(原稿番号のみで絞った表示用) */
	public String webId;

	/** ページ番号 */
	public String pageNum;

	/** 全てを表示するか、原稿IDで絞って表示するかを保持する列挙型 */
	public ListDisplayKbn listDisplayKbn = ListDisplayKbn.ALL;

	/** 一括送信IDリスト */
	public String lumpSendIds;

	/** 検索用 */
	public String search;

	/**
	 * リセットを行う
	 */
	public void resetForm() {
		super.resetBaseForm();
		memo = null;
		webId = null;
		pageNum = null;
		listDisplayKbn = ListDisplayKbn.ALL;
		lumpSendIds = null;
		search = null;
	}

	/**
	 *  独自チェックを行う
	 * @return ActionMessages
	 */
	public ActionMessages validateLumpSend() {

		// エラー情報
		ActionMessages errors = new ActionMessages();



		// FIXME 会員ではないため修正
		//会員が選択されているかチェック
		if (StringUtils.isBlank(lumpSendIds)) {
			errors.add("errors", new ActionMessage("errors.noCheckMember",
					MessageResourcesUtil.getMessage("labels.lumpSend"),
					MessageResourcesUtil.getMessage("labels.member")));
		}

		return errors;
	}



	/**
	 * 一括送信用リセット
	 */
	public void resetForLumpSend() {
		lumpSendIds = null;
	}
}
