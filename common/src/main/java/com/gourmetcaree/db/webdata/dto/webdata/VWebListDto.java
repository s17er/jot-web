package com.gourmetcaree.db.webdata.dto.webdata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gourmetcaree.db.common.entity.TWebIpPhone;
import com.gourmetcaree.db.common.entity.VWebList;

/**
 * 運営管理画面でWEBデータ一覧を検索する時に使う検索条件DTO(多分
 * WEBデータ一覧、WEBデータCSV、プレビュー(検索結果一覧のWEB掲載確認)に使う。
 * XXX VWebListを継承しているので他の用途(検索結果など)でも使われているかも。どちらにせよ、このクラスの使用目的がよくわからない。
 *
 * WEBデータ一覧、WEBデータCSV、プレビューは同じ条件で検索をするが
 * それぞれアクションが違うためデータの詰め方が違うので注意。
 * XXX というか検索用のロジックを作ってそれを各アクションで使うべき。
 *
 * @author Makoto Otani
 * @version 1.0
 */
public class VWebListDto extends VWebList implements Serializable {

    /**
     * シリアルバージョンUID
     */
    private static final long serialVersionUID = -7161157199121543333L;

    /**
     * 掲載開始日
     */
    public Date postFromDate;

    /**
     * 掲載終了日
     */
    public Date postToDate;

    /**
     * 業種区分
     */
    public Integer industryKbn;

    /**
     * ステータス
     */
    public List<String> displayStatusList = new ArrayList<String>();

    /**
     * 特集ID
     */
    public Integer specialId;

    /**
     * 勤務地エリア(WEBエリアから名称変更)区分
     */
    public Integer webAreaKbn;

    /**
     * 海外エリア区分
     */
    public Integer foreignAreaKbn;

    /**
     * WEBデータID(検索用)
     */
    public Integer whereWebId;

    /**
     * 鉄道会社
     */
    public Integer railroadId;

    /**
     * 路線
     */
    public Integer routeId;

    /**
     * 駅
     */
    public Integer stationId;

    /**
     * キーワード
     */
    public String keyword;

    /**
     * 連載区分
     */
    public Integer serialPublicationKbn;

    /**
     * IP電話番号
     */
    public String ipPhone;

    /**
     * 注文店舗フラグ
     */
    public String attentionShopFlg;

    /**
     * 検索優先フラグ
     */
    public String searchPreferentialFlg;

    /**
     * {@link TWebIpPhone}のIDリスト
     */
    public List<Integer> webIpPhoneIdList;

    /** 店舗区分リスト*/
    public List<String> shopsKbnList;

    /** 職種リスト */
    public List<String> jobKbnList;

    /** 雇用形態リスト */
    public List<String> employPtnKbnList;

    /** 詳細エリアリスト */
    public List<String> detailAreaList;

    /** 待遇リスト */
    public List<String> treatmentList;

    /** その他条件リスト */
    public List<String> otherConditionList;

    /** WEBデータタグID */
    public String webDataTagId;

}