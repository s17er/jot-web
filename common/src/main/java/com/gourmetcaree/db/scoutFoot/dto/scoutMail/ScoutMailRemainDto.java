package com.gourmetcaree.db.scoutFoot.dto.scoutMail;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.time.DateUtils;

import com.gourmetcaree.common.constants.GourmetCareeConstants;
import com.gourmetcaree.common.dto.BaseDto;

/**
 * スカウトメール残数DTO
 * @author Keita Yamane
 *
 */
public class ScoutMailRemainDto extends BaseDto implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -2953050457749460246L;

	/** 有料スカウトメール残数 */
	public int remainPaidScoutCount;

	/** 使用期限(有料) */
	public Timestamp useEndDatetime;

	/** 無料スカウトメール残数 */
	public int remainFreeScoutCount;

	/** 有効期限スカウトのみのフラグ */
	public boolean onlyUnlimitedScoutFlg;

	/**
	 * スカウトメールが残っているかどうか
	 * @return
	 */
	public boolean isExistRemainScoutMail() {
		return remainFreeScoutCount > 0
				|| remainPaidScoutCount > 0;
	}


	/**
	 * 使用期限(有料)の文字列取得
	 * @return
	 */
	public String getUseEndDatetimeStr() {
		if (useEndDatetime == null) {
			return "";
		}

		return new SimpleDateFormat(GourmetCareeConstants.DATE_FORMAT_SLASH).format(DateUtils.addDays(useEndDatetime, -1));
	}

	/**
	 * 有料スカウトメール残数を取得
	 * @return
	 */
	public int getRemainPaidScoutCount() {
		return remainPaidScoutCount;
	}

	/**
	 * 無料スカウトメール残数を取得
	 */
	public int getRemainFreeScoutCount() {
		return remainFreeScoutCount;
	}

	/**
	 * 有効期限スカウトのみのフラグを取得
	 */
	public boolean getOnlyUnlimitedScoutFlg() {
		return onlyUnlimitedScoutFlg;
	}
}
