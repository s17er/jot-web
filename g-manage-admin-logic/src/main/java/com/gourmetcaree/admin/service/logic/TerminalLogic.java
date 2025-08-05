package com.gourmetcaree.admin.service.logic;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.gourmetcaree.common.dto.TerminalDto;
import com.gourmetcaree.db.common.entity.MTerminal;
import com.gourmetcaree.db.common.entity.MTerminalStation;
import com.gourmetcaree.db.common.service.TerminalService;
import com.gourmetcaree.db.common.service.TerminalStationService;

/**
 * 駅グループのロジッククラス
 * @author yamane
 */
public class TerminalLogic extends AbstractAdminLogic {

	/** ターミナルサービス */
	@Resource
	protected TerminalService terminalService;

	/** ターミナル駅サービス */
	@Resource
	protected TerminalStationService terminalStationService;

	/**
	 * ターミナルの登録
	 * @param mTerminal ターミナルエンティティ
	 * @param mTerminalStation ターミナル駅エンティティ
	 */
	public void insert(MTerminal mTerminal, List<MTerminalStation> mTerminalStation) {

		/* ターミナルの登録 */
		terminalService.insert(mTerminal);

		/* ターミナル駅の登録 */
		for (MTerminalStation entity : mTerminalStation) {
			/* 紐づけ */
			entity.terminalId = mTerminal.id;
			terminalStationService.insert(entity);
		}

	}

	/**
	 * ターミナルを削除する
	 * @param terminalId ターミナルID
	 */
	public boolean allDelete(Integer terminalId) {

		terminalService.logicalDelete(terminalService.findById(terminalId));
		for (String s : terminalStationService.getTerminalStationId(String.valueOf(terminalId))) {
			terminalStationService.logicalDelete(terminalStationService.findById(Integer.parseInt(s)));
		}

		return true;
	}

	/**
	 * ターミナルを更新
	 * @param mTerminal
	 * @param mTerminalStationList
	 */
	public void update(MTerminal mTerminal, List<MTerminalStation> mTerminalStationList) {

		terminalService.update(mTerminal);

		terminalStationService.updateBatch(mTerminalStationList);

	}

	/**
	 * ターミナルIDとエリアコードを取得する
	 * @return
	 */
	public Map<String, String> getTerminalMap() {

		return terminalService.getTerminalMap();
	}

	/**
	 * 各駅グループのタイトルを取得する
	 * @param id
	 * @return
	 */
	public String getTitle(String id) {
		return terminalService.findById(Integer.parseInt(id)).terminalName;
	}

	/**
	 * 駅グループの情報を取得する
	 * @return
	 */
	public List<TerminalDto> getTerminalDto(String id, String prefecturesCd) {

		return  terminalStationService.getTerminalIdList(id, prefecturesCd);
	}

	public void deleteTerminalData(String id) {

		MTerminal mTerminal = terminalService.findById(Integer.parseInt(id));
		for (String str : terminalStationService.getTerminalStationId(String.valueOf(mTerminal.id))) {
			terminalStationService.logicalDelete(terminalStationService.findById(Integer.parseInt(str)));
		}
	}

	/**
	 * ターミナル駅の登録
	 * @param mTerminal ターミナルエンティティ
	 * @param mTerminalStationList ターミナル駅エンティティ
	 */
	public void insertTerminalStation(String id, List<MTerminalStation> mTerminalStationList) {

		/* ターミナル駅の登録 */
		for (MTerminalStation entity : mTerminalStationList) {
			/* 紐づけ */
			entity.terminalId = Integer.parseInt(id);
			terminalStationService.insert(entity);
		}

	}
}
