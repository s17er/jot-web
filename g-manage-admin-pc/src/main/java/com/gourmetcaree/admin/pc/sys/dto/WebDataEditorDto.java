package com.gourmetcaree.admin.pc.sys.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.common.dto.BaseDto;

/**
 * WEBデータの編集中ユーザ用DTOです。<br />
 * アプリケーションスコープなので注意
 *
 * @author nakamori
 *
 */
@Component(instance = InstanceType.APPLICATION)
public class WebDataEditorDto extends BaseDto {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 2681206370101701611L;

	/** WEBデータのエディットマップ */
	private volatile Map<Integer, WebDataAccessDto> webDataEditMap = new HashMap<Integer, WebDataAccessDto>();


	/**
	 * 営業担当者のアクセスDTOをWEBデータIDをキーにマップに追加します。
	 *
	 * @param webId
	 *            webId
	 * @param salesId
	 *            営業担当者ID
	 * @param salesName
	 *            営業担当者名
	 *
	 * @author nakamori
	 */
	public WebDataAccessDto addWebDataAccessDto(int webId, int salesId, String salesName) {
		synchronized (webDataEditMap) {
			remove(webId);
			WebDataAccessDto dto = new WebDataAccessDto(salesId, salesName);
			webDataEditMap.put(webId, dto);
			return dto;
		}
	}


	public WebDataAccessDto getWebDataAccessDto(int webId) {
		Integer key = Integer.valueOf(webId);
		synchronized (webDataEditMap) {
			if (webDataEditMap.containsKey(key)) {
				return webDataEditMap.get(key);
			}
			return null;
		}
	}



	/**
	 * 対象webデータのアクセスDTOを削除します。
	 *
	 * @param webId
	 *            webId
	 *
	 * @author nakamori
	 */
	public void remove(Integer webId) {
		if (webId == null) {
			return;
		}
		synchronized (webDataEditMap) {
			if (webDataEditMap.containsKey(webId)) {
				webDataEditMap.remove(webId);
			}
		}
	}

	public boolean containsWebId(Integer webId) {
		synchronized (webDataEditMap) {
			return webDataEditMap.containsKey(webId);
		}
	}

	/**
	 * WEBデータのエディットマップを取得します。
	 * @return WEBデータのエディットマップ
	 * @author Makoto Otani
	 */
	public Map<Integer, WebDataAccessDto> getWebDataEditMap() {
		return webDataEditMap;
	}


	/**
	 * WEBデータ・アクセス用DTO
	 *
	 * @author nakamori
	 *
	 */
	public static class WebDataAccessDto implements Serializable {

		/**
		 *
		 */
		private static final long serialVersionUID = -2906670263503146989L;

		/** 最終アクセス時間 */
		private long lastAccessTimestamp;

		/** 営業ID */
		private int salesId;

		/** 営業担当者名 */
		private String salesName;

		/**
		 * 営業IDを使用してインスタンスを生成します。
		 *
		 * @param salesId
		 *            営業ID
		 * @param salesName
		 *            営業担当者名
		 */
		public WebDataAccessDto(int salesId, String salesName) {
			this.salesId = salesId;
			this.salesName = salesName;
			this.lastAccessTimestamp = System.currentTimeMillis();
		}

		/**
		 * 最終アクセス時間を取得します。
		 *
		 * @return 最終アクセス時間
		 * @author nakamori
		 */
		public long getLastAccessTimestamp() {
			return lastAccessTimestamp;
		}

		/**
		 * 営業IDを取得します。
		 *
		 * @return 営業ID
		 * @author nakamori
		 */
		public int getSalesId() {
			return salesId;
		}

		/**
		 * 営業担当者名を取得します。
		 * @return 営業担当者名
		 * @author Makoto Otani
		 */
		public String getSalesName() {
			return salesName;
		}

		/**
		 * 最終アクセス時間を更新します。
		 *
		 *
		 * @author nakamori
		 */
		public void updateLastAccessTimestamp() {
			lastAccessTimestamp = System.currentTimeMillis();
		}

	}

}
