package com.gourmetcaree.admin.pc.sys.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.seasar.struts.exception.ActionMessagesException;
import org.seasar.struts.util.ResponseUtil;

import com.gourmetcaree.admin.pc.sys.dto.UserDto;
import com.gourmetcaree.common.action.BaseAction;
import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.exception.FraudulentProcessException;
import com.gourmetcaree.common.exception.PageNotFoundException;
import com.gourmetcaree.common.form.BaseForm;
import com.gourmetcaree.common.util.GourmetCareeUtil;

import net.arnx.jsonic.JSON;

/**
 * PC版管理画面の共通アクションです。<br>
 * このクラスを継承して下さい。
 * @author Takahiro Ando
 * @version 1.0
 */
public abstract class PcAdminAction extends BaseAction {

	/** エラーログ */
	protected final Logger errorLog = Logger.getLogger("errorLog");

	/** ログ */
	private static final Logger log = Logger.getLogger(PcAdminAction.class);

	/** ユーザDTO */
	@Resource
	public UserDto userDto;

	/** 引数チェックでブランクを許容するかどうかのフラグ - 許容しない */
	protected final boolean NO_BLANK_FLG_NG = true;

	/** 引数チェックでブランクを許容するかどうかのフラグ - 許容する */
	protected final boolean NO_BLANK_FLG_ADMIT = false;




	/**
	 * 渡された引数がひとつでもnullかどうかをチェックする。<br />
	 * また、IDが不正で無いかチェックする。
	 * ひとつでもnull、不正なIDがあればページが見つからない旨のエラーをthrow
	 * @param noBlankFlg trueであればブランク("")もエラーとする。
	 * @param args
	 */
	protected void checkArgsNullPageNotFound(boolean noBlankFlg, String... args) {

		if (args == null) {
			throw new PageNotFoundException();
		}

		for (String str : args) {
			if (str == null) {
				throw new PageNotFoundException();
			} else if (noBlankFlg) {

				if (StringUtils.isEmpty(str)) {
					throw new PageNotFoundException();
				}
			}
		}

	}

	/**
	 * 渡された引数がひとつでもnullかどうかをチェックする。
	 * ひとつでもnullがあれば不正な操作である旨のエラーをthrow
	 * @param noBlankFlg trueであればブランク("")もエラーとする。
	 * @param args
	 */
	protected void checkArgsNull(boolean noBlankFlg, String... args) {

		if (args == null) {
			throw new FraudulentProcessException();
		}

		for (String str : args) {
			if (str == null) {
				throw new FraudulentProcessException();
			} else if (noBlankFlg) {
				if (StringUtils.isEmpty(str)) {
					throw new FraudulentProcessException();
				}
			}
		}
	}

	/**
	 * IDに不正が無いかチェックする。<br />
	 * 不正な場合は、データが見つからないエラーメッセージを返却する。
	 * @param form 画面表示フラグの設定用フォーム
	 * @param id チェックするID
	 */
	protected void checkId(BaseForm form, String id) {

		try {
			// IDが不正かどうかチェック
			Integer.parseInt(id);

		} catch (NumberFormatException e) {

			// 画面を非表示に設定
			form.setExistDataFlgNg();

			// idが数値でない場合、データなしと同じエラーを返す。
			throw new ActionMessagesException("errors.app.dataNotFound");
		}
	}

	/**
	 * IDが不正かどうかチェックする
	 * IDが数値でない場合は、不正な操作に遷移
	 */
	protected void checkId(String id) {
		try {
			// IDが不正かどうかチェック
			Integer.parseInt(id);

		} catch (NumberFormatException e) {
			// idが数値でない場合、データなしと同じエラーを返す。
			throw new FraudulentProcessException();
		}
	}

	/**
	 * JSONを書き出します。
	 * @param json JSON
	 * @author Takehiro Nakamori
	 */
	protected void writeJson(Object obj) {
		ResponseUtil.write(JSON.encode(obj), GourmetCareeConstants.JSON_CONTENT_TYPE, GourmetCareeConstants.ENCODING);
		writeJson(obj, HttpServletResponse.SC_OK);
	}

	/**
	 * JSONを書き出します。
	 * @param obj
	 * @param statusCode
	 */
	protected void writeJson(Object obj, int statusCode) {
		response.setStatus(statusCode);
		ResponseUtil.write(JSON.encode(obj), GourmetCareeConstants.JSON_CONTENT_TYPE, GourmetCareeConstants.ENCODING);
	}

	/**
	 * IP電話番号の無視リストを読み込みます。
	 * @return
	 */
	protected List<String> readIgnoreIpPhoneList() {

		synchronized (log) {
			BufferedReader reader = null;
			File file = new File(getCommonProperty("gc.ipPhone.ignore.filePath"));
			if (!file.exists()) {
				log.fatal("ファイルが見つかりません" + file.getAbsolutePath());
				return new ArrayList<String>(0);
			}

			try {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

				List<String> list = new ArrayList<String>();
				String val;
				while((val = reader.readLine()) != null) {
					if (StringUtils.isNotBlank(val)) {
						list.add(GourmetCareeUtil.superTrim(val));
					}
				}
				return list;
			} catch (IOException e) {
				log.fatal("IP電話の無視リストファイルの読込に失敗しました。 " + file.getAbsolutePath());
				return new ArrayList<String>(0);
			} finally {
				IOUtils.closeQuietly(reader);
			}
		}
	}
}
