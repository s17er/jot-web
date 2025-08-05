package com.gourmetcaree.admin.service.logic.tempWebdata;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.gourmetcaree.admin.service.logic.AbstractAdminLogic;
import com.gourmetcaree.admin.service.property.tempWebdata.TempWebdataProperty;
import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.db.common.entity.TTempWeb;
import com.gourmetcaree.db.common.service.TempWebService;

public class TempWebdataLogic extends AbstractAdminLogic {

	private static Logger log = Logger.getLogger(TempWebdataLogic.class);

	@Resource
	private TempWebService tempWebService;

	public TTempWeb register(TempWebdataProperty property) {

		// tempWebdataPropertyをJSONに変換
		String text = property.toJson();

		// 保存処理
		TTempWeb entity = new TTempWeb();
		entity.jsonData = text;
		entity.saveDatetime = DateUtils.getJustTimestamp();
		tempWebService.insert(entity);
		log.info("登録しました");

		return entity;
	}
}
