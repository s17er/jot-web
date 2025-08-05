package com.gourmetcaree.common.dto;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.InternetAddress;

import com.gourmetcaree.common.constants.GourmetCareeConstants;


/**
 * システムから送信される通知メールのBaseDto
 * @author Makoto Otani
 * @version 1.0
 */
public class BaseSysMaiDto extends BaseDto {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = -7919426283933374211L;

	/** 送信先 */
	private List<String> to = new ArrayList<String>();

	/** 送信元 */
	private InternetAddress from;

	/**
	 * 送信先を取得します。
	 * @return 送信先
	 */
	public List<String> getTo() {
		return to;
	}

	/**
	 * 送信先を設定します。
	 * @param to 送信先リスト
	 */
	public void setTo(List<String> to) {
		this.to = to;
	}

	/**
	 * 送信先リストに宛先を追加します。
	 * @param address 送信先
	 */
	public void addTo(String address) {
		this.to.add(address);
	}

	/**
	 * 送信先リストを空にします。
	 */
	public void resetTo() {
		this.to = new ArrayList<String>();
	}

	/**
	 * 送信元を取得します。
	 * @return 送信元
	 */
	public InternetAddress getFrom() {
	    return from;
	}

	/**
	 * 送信元を設定します。
	 * @param from 送信元
	 */
	public void setFrom(InternetAddress from) {
	    this.from = from;
	}


	/**
	 * 送信元をセットします。
	 * @param address メールアドレス
	 * @param personal メール名
	 * @throws UnsupportedEncodingException メールが正しくセットできない場合のエラー
	 */
	public void setFrom(String address, String personal) throws UnsupportedEncodingException {
		InternetAddress internetAddress = new InternetAddress(address, personal, GourmetCareeConstants.MAIL_CHARSET);
		setFrom(internetAddress);
	}


}
