package com.gourmetcaree.admin.service.logic;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import jp.co.whizz_tech.commons.WztStringUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.seasar.extension.jdbc.where.SimpleWhere;
import org.seasar.framework.beans.util.Beans;

import com.gourmetcaree.admin.service.connection.IntecAuthConnection;
import com.gourmetcaree.admin.service.connection.IntecFindClientConnection;
import com.gourmetcaree.admin.service.connection.IntecCreateConnection;
import com.gourmetcaree.admin.service.connection.IntecCreateConnection.ClientData;
import com.gourmetcaree.admin.service.connection.IntecUpdateConnection;
import com.gourmetcaree.admin.service.connection.parser.IPPhoneAuthParser;
import com.gourmetcaree.admin.service.connection.parser.IPPhoneAuthParser.IntecAuthDto;
import com.gourmetcaree.admin.service.connection.parser.IPPhoneClientParser;
import com.gourmetcaree.admin.service.connection.parser.IPPhoneClientListParser.IntecClientRegisterDto;
import com.gourmetcaree.admin.service.dto.IPPhoneDataCopyDto;
import com.gourmetcaree.common.connection.AbstractBaseConnection;
import com.gourmetcaree.common.exception.AjaxException;
import com.gourmetcaree.common.exception.GourmetCareeServiceException;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.db.common.entity.TWebIpPhone;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.exception.WNoResultException;
import com.gourmetcaree.db.common.service.CustomerService;
import com.gourmetcaree.db.common.service.WebIpPhoneService;



/**
 * IP電話ロジック
 * @author nakamori
 *
 */
public class IPPhoneLogic extends AbstractAdminLogic {

	/** ログオブジェクト */
	private static Logger log = Logger.getLogger(IPPhoneLogic.class);

	/** コールノートの独自ステータスコードのキー */
	private static String X_CT_STATUS_KEY = "X-Ct-Status";

	/** IP電話の最大登録数 */
	private static int IP_PHONE_MAX_COUNT = 3;

	/** IP電話サービス */
	@Resource
	private WebIpPhoneService webIpPhoneService;

	@Resource
	private CustomerService customerService;

	/**
	 * IP電話番号のコピー作成
	 *
	 * @param dtoList
	 * @return
	 *
	 * @author hara
	 */
	public List<TWebIpPhone> copyIpPhoneNumber(IPPhoneDataCopyDto dto) {

		List<IPPhoneDataCopyDto> dtoList = new ArrayList<IPPhoneDataCopyDto>();
		dtoList.add(dto);
		return copyIpPhoneNumber(dtoList);

	}
	/**
	 * IP電話番号のコピー作成
	 *
	 * @param dtoList
	 * @return
	 *
	 * @author hara
	 */
	public List<TWebIpPhone> copyIpPhoneNumber(List<IPPhoneDataCopyDto> dtoList) {

		for (IPPhoneDataCopyDto dto : dtoList) {

			if (dto.sourceWebId == null
					|| dto.customerId == null
					|| dto.webId == null) {
				continue;
			}

			// コピー元の原稿データの電話番号から、新規にIP電話をインテックAPIより取得する。
			try {
				List<TWebIpPhone> list = null;
				list = webIpPhoneService.findByCondition(new SimpleWhere()
															.eq(WztStringUtil.toCamelCase(TWebIpPhone.WEB_ID), dto.sourceWebId)
															.eq(WztStringUtil.toCamelCase(TWebIpPhone.CUSTOMER_ID), dto.customerId)
															.eq(WztStringUtil.toCamelCase(TWebIpPhone.DELETE_FLG), DeleteFlgKbn.NOT_DELETED),
															TWebIpPhone.EDA_NO
															);

				for (TWebIpPhone entity : list) {
					TWebIpPhone newEntity = new TWebIpPhone();

					newEntity.webId = dto.webId;
					newEntity.customerId = dto.customerId;
					newEntity.customerTel = entity.customerTel;
					newEntity.ipTel = "";

					ClientData data = createClientData(dto.webId, dto.customerId, entity.edaNo, entity.customerTel);
					newEntity.clientCd = data.clientCd;

					newEntity.edaNo = entity.edaNo;

					webIpPhoneService.insert(newEntity);
				}

			} catch (WNoResultException e) {
				continue;
			}
		}

		return null;
	}

	/**
	 * IP電話の登録用データを作成します。
	 *
	 * @author hara
	 */
	private ClientData createClientData(Integer webId, Integer customerId, Integer edaNo, String phoneNo) {

		ClientData data = new ClientData();
		data.webId = webId;
		data.customerId = customerId;
		data.customerName =  customerService.getCustomerName(customerId);

		data.edaNo = edaNo;
		if(GourmetCareeUtil.checkPhoneNo(phoneNo)){
			data.customerTel = phoneNo.replaceAll("-", "");
		}

		data.createClientCd();

		return data;
	}

	/**
	 * IP電話番号作成
	 * @param dataList
	 * @throws DocumentException
	 * @throws IOException
	 * @throws GourmetCareeServiceException
	 *
	 * @Return 最新のIP電話番号リスト
	 */
	public List<TWebIpPhone> createIpPhoneNumber(List<ClientData> dataList) {

		int webId = -1;
		int customerId = -1;

		List<String> noDeleteIpTelList = new ArrayList<String>(3);
		List<ClientData> newDataList = new ArrayList<ClientData>(3);

		// 電話番号のないデータカウント
		int noDataCount = 0;

		// 既存データをチェックする。
		for (ClientData data : dataList) {

			try {
				webId = data.webId;
				customerId = data.customerId;

				List<TWebIpPhone> list = webIpPhoneService.findByCondition(new SimpleWhere()
									.eq(WztStringUtil.toCamelCase(TWebIpPhone.WEB_ID), data.webId)
									.eq(WztStringUtil.toCamelCase(TWebIpPhone.CUSTOMER_ID), data.customerId)
									.eq(WztStringUtil.toCamelCase(TWebIpPhone.EDA_NO), data.edaNo)
									.eq(WztStringUtil.toCamelCase(TWebIpPhone.DELETE_FLG), DeleteFlgKbn.NOT_DELETED));

				if (CollectionUtils.isNotEmpty(list) && 0 < list.size()) {
					if (StringUtils.isNotBlank(data.ipTel)
							&& list.get(0).ipTel.equals(data.ipTel)
							&& list.get(0).customerTel.equals(data.customerTel)) {

						noDeleteIpTelList.add(data.ipTel);
						log.info(String.format("登録済みの番号のためスキップします。tel:[%s] ipTel:[%s]", data.customerTel, data.ipTel));
						continue;
					}

					data.id = list.get(0).id;
					data.version = list.get(0).version;
				}

			} catch (WNoResultException e) {
				// 見つからなければ取得対象なので何もしない
			}

			if (StringUtils.isBlank(data.customerTel)) {
				noDataCount++;
				continue;
			}

			newDataList.add(data);
		}

		if (noDataCount == IP_PHONE_MAX_COUNT) {
			webIpPhoneService.deleteByWebId(webId);
			return getIpPhoneData(dataList);
		}

		// 既存データ以外の削除
		webIpPhoneService.deleteOtherData(webId, customerId, noDeleteIpTelList);

		// 登録変更データがなければ終了
		if (CollectionUtils.isEmpty(newDataList)) {
			return getIpPhoneData(dataList);
		}

		// 認証 トークンを取得
		IntecAuthDto authDto = getAuthCd();

		List<TWebIpPhone> insertList = new ArrayList<TWebIpPhone>();
		for (ClientData newData : newDataList) {

			// インテックDBから検索
			IntecClientRegisterDto dto = findClientData(newData.clientCd, authDto.getToken());

			// まだ登録されていなければ登録
			if (dto == null || dto.getClientCd() == null) {
				insertList.add(regesterClientData(newData, authDto.getToken()));

			// 登録済みの場合は変更
			} else {

				insertList.add(updateClientData(newData, authDto.getToken()));
			}

		}

		return getIpPhoneData(dataList);
	}

	/**
	 * 最新のIPPhoneデータを取得します
	 *
	 * @param dataList
	 * @return
	 *
	 * @author hara
	 */
	private List<TWebIpPhone> getIpPhoneData(List<ClientData> dataList) {
		List<TWebIpPhone> ipPhoneList = new ArrayList<TWebIpPhone>();
		// 最新データをチェックする。
		for (ClientData data : dataList) {
			List<TWebIpPhone> list = null;
			try {
				list = webIpPhoneService.findByCondition(new SimpleWhere()
															.eq(WztStringUtil.toCamelCase(TWebIpPhone.WEB_ID), data.webId)
															.eq(WztStringUtil.toCamelCase(TWebIpPhone.CUSTOMER_ID), data.customerId)
															.eq(WztStringUtil.toCamelCase(TWebIpPhone.EDA_NO), data.edaNo)
															.eq(WztStringUtil.toCamelCase(TWebIpPhone.DELETE_FLG), DeleteFlgKbn.NOT_DELETED));
				if (CollectionUtils.isNotEmpty(list) && 0 < list.size()) {
					ipPhoneList.add(list.get(0));
				}
			} catch (WNoResultException e) {
				ipPhoneList.add(new TWebIpPhone());
			}
		}

		return ipPhoneList;
	}

	/**
	 * インテックAPIの認証コードを取得します
	 *
	 *
	 * @author hara
	 */
	private IntecAuthDto getAuthCd() {
		IntecAuthConnection connection = new IntecAuthConnection();

		// 接続
		Document doc;
		try {
			doc = connection.connect();

			int httpStatus = connection.getHttpStatus();

			if(200 <= httpStatus && httpStatus <= 399) {
				IntecAuthDto authDto = new IntecAuthDto();
				// 正常時
				IPPhoneAuthParser parser = new IPPhoneAuthParser();
				parser.parse(doc, authDto);

				log.debug("認証トークン:"+authDto.getToken());
				return authDto;
			} else {
				throw new AjaxException(String.format("異常なHTTPステータスです。[%d]", httpStatus));
			}

		} catch (IOException e) {
			throw new AjaxException("認証トークン取得時にエラーが発生しました。", e);
		} catch (DocumentException e) {
			throw new AjaxException("認証トークン取得時にエラーが発生しました。", e);
		} catch (ParseException e) {
			throw new AjaxException("日付のパース時にエラーが発生しました。", e);
		}

	}

	/**
	 * クライアントコードに一致するデータが登録済みか確認します
	 *
	 * @return
	 *
	 * @author hara
	 */
	private IntecClientRegisterDto findClientData(String clientCd, String token) {
		IntecFindClientConnection connection = new IntecFindClientConnection(clientCd, token);

		// 接続
		Document doc;
		try {
			doc = connection.connect();

			int httpStatus = connection.getHttpStatus();

			if(200 <= httpStatus && httpStatus <= 399) {
				IntecClientRegisterDto dto = new IntecClientRegisterDto();
				// 正常時
				IPPhoneClientParser parser = new IPPhoneClientParser();
				parser.parse(doc, dto);
				log.debug(String.format("クライアントデータが見つかりました。クライアントコード:[%s]", dto.getClientCd()));

				return dto;
			} else {
				throw new AjaxException(String.format("異常なHTTPステータスです。[%d]", httpStatus));
			}

		} catch (IOException e) {
			int httpStatus = connection.getHttpStatus();

			if (httpStatus == 404) {
				log.debug(String.format(" クライアントデータが見つかりませんでした。クライアントコード:[%s]", clientCd));
				return null;
			}

			throw new AjaxException(String.format("クライアントデータ取得時にエラーが発生しました。HTTPステータス[%d] X-Ct-Status[%s] ", httpStatus, getXctstatus(connection)), e);

		} catch (DocumentException e) {
			int httpStatus = connection.getHttpStatus();

			throw new AjaxException(String.format("クライアントデータ取得時にエラーが発生しました。HTTPステータス[%d] X-Ct-Status[%s] ", httpStatus, getXctstatus(connection)), e);

		}
	}

	/**
	 * クライアントデータを新規登録します
	 *
	 * @param clientCd
	 * @param token
	 *
	 * @return 登録したデータ
	 *
	 * @author hara
	 */
	private TWebIpPhone regesterClientData(ClientData newData, String token) {

		// コネクション作成
		IntecCreateConnection connection = new IntecCreateConnection(newData, token);

		// 接続
		Document doc;
		try {
			doc = connection.connect();

			int httpStatus = connection.getHttpStatus();

			if(200 <= httpStatus && httpStatus <= 399) {
				log.debug("インテックDBにデータを登録しました。");
				// 正常時
				return insertNewClientData(doc, newData, false);

			} else {
				// 異常時
				throw new AjaxException(String.format("異常なHTTPステータスです。[%d] クライアントコード[%s]", httpStatus, newData.clientCd));
			}

		} catch (IOException e) {
			int httpStatus = connection.getHttpStatus();
			throw new AjaxException(String.format("IP電話番号の登録時にエラーが発生しました。HTTPステータス[%d] X-Ct-Status[%s] ", httpStatus, getXctstatus(connection)), e);

		} catch (DocumentException e) {
			int httpStatus = connection.getHttpStatus();
			throw new AjaxException(String.format("IP電話番号の登録時にエラーが発生しました。HTTPステータス[%d] X-Ct-Status[%s] ", httpStatus, getXctstatus(connection)), e);

		}

	}

	/**
	 * IPPhoneテーブルにデータを登録・更新します。
	 *
	 * @param doc
	 * @param data 更新対象のデータ
	 * @param updateFlg 更新処理フラグ。trueの場合は更新
	 * @return
	 *
	 * @author hara
	 */
	private TWebIpPhone insertNewClientData(Document doc, ClientData data, boolean updateFlg) {

		IPPhoneClientParser parser = new IPPhoneClientParser();
		IntecClientRegisterDto dto = new IntecClientRegisterDto();
		parser.parse(doc, dto);

		if (dto == null || dto.getIpTel() == null) {
			throw new AjaxException("登録データが不正です。");
		}

		// 新規取得したデータをテーブルに登録
		TWebIpPhone entity = Beans.createAndCopy(TWebIpPhone.class, data).execute();
		entity.ipTel = dto.getIpTel();


		boolean existDataFlg = existData(entity);

		if (updateFlg && existDataFlg) {
			int count = webIpPhoneService.update(entity);
			log.debug(String.format("グルメDBのデータを[%d]件　更新しました。", count));

		} else {
			int count = webIpPhoneService.insert(entity);
			log.debug(String.format("グルメDBにデータを[%d]件　登録しました。", count));
		}

		return entity;
	}


	/**
	 * グルメDBにデータが存在するか確認します
	 *
	 * @param entity
	 * @return 存在したらtrue
	 *
	 * @author hara
	 */
	private boolean existData(TWebIpPhone entity) {
		SimpleWhere where = new SimpleWhere();
		where.eq(WztStringUtil.toCamelCase(TWebIpPhone.WEB_ID), entity.webId);
		where.eq(WztStringUtil.toCamelCase(TWebIpPhone.CUSTOMER_ID), entity.customerId);
		where.eq(WztStringUtil.toCamelCase(TWebIpPhone.EDA_NO), entity.edaNo);
		where.eq(WztStringUtil.toCamelCase(TWebIpPhone.DELETE_FLG), DeleteFlgKbn.NOT_DELETED);

		return jdbcManager.from(TWebIpPhone.class)
					.where(where)
					.getCount() > 0l;


	}

	/**
	 * クライアントデータを更新します
	 *
	 * @param clientCd
	 * @param token
	 * @return
	 *
	 * @author hara
	 */
	private TWebIpPhone updateClientData(ClientData updateData, String token) {

		// コネクション作成
		IntecUpdateConnection connection = new IntecUpdateConnection(updateData, token);

		// 接続
		Document doc;
		try {
			doc = connection.connect();

			int httpStatus = connection.getHttpStatus();

			if(200 <= httpStatus && httpStatus <= 399) {
				// 正常時
				log.debug("インテックDBのデータを更新しました。");
				return insertNewClientData(doc, updateData ,true);

			} else {
				// 異常時
				throw new AjaxException(String.format("異常なHTTPステータスです。[%d] クライアントコード[%s]", httpStatus, updateData.clientCd));

			}

		} catch (IOException e) {
			int httpStatus = connection.getHttpStatus();
			throw new AjaxException(String.format("IP電話番号の更新時にエラーが発生しました。HTTPステータス[%d] X-Ct-Status[%s] ", httpStatus, getXctstatus(connection)), e);

		} catch (DocumentException e) {
			int httpStatus = connection.getHttpStatus();
			throw new AjaxException(String.format("IP電話番号の登録時にエラーが発生しました。HTTPステータス[%d] X-Ct-Status[%s] ", httpStatus, getXctstatus(connection)), e);

		}
	}

	/**
	 * コネクションからコールノートの独自ステータスコードを取得します
	 *
	 * @param connection
	 * @return
	 *
	 * @author hara
	 */
	private String getXctstatus (Object connection) {

		if (connection == null) {
			return null;
		}

		if (connection instanceof AbstractBaseConnection) {
			AbstractBaseConnection baseCon = (AbstractBaseConnection) connection;

			List<String> xctstatusList = baseCon.getResponseHeaders().get(X_CT_STATUS_KEY);
			if (CollectionUtils.isNotEmpty(xctstatusList) && 0 < xctstatusList.size()) {
				return xctstatusList.get(0);
			}
		}

		return null;
	}
}
