package com.gourmetcaree.admin.service.property.tempWebdata;

import java.io.Serializable;
import java.util.List;

/**
 * 一時Webデータに保存する職種のプロパティ
 * PHP側に対応するため意図的にスネークケースで定義している。
 *
 * @author hara
 *
 */
public class TempWebJobPropterty implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 435933628733011346L;

	/** 職種 */
	public String job_kbn;
	/** 掲載フラグ */
	public String publication_flg;
	/** 雇用形態 */
	public String employ_ptn_kbn;
	/** 仕事内容 */
	public String work_contents;
	/** 給与体系区分 */
	public String salery_structure_kbn;
	/** 給与金額（下限） */
	public String lower_salary_price;
	/** 給与金額（上限） */
	public String upper_salary_price;
	/** 給与 */
	public String salary;
	/** 給与詳細 */
	public String salary_detail;
	/** 想定初年度年収給与下限 */
	public String annual_lower_salary_price;
	/** 想定初年度年収給与上限 */
	public String annual_upper_salary_price;
	/** 想定初年度年収フリー欄 */
	public String annual_salary;
	/** 想定初年度年収給与詳細 */
	public String annual_salary_detail;
	/** 想定初年度月収給与下限 */
	public String monthly_lower_salary_price;
	/** 想定初年度月収給与上限 */
	public String monthly_upper_salary_price;
	/** 想定初年度月収フリー欄 */
	public String monthly_salary;
	/** 想定初年度月収給与詳細 */
	public String monthly_salary_detail;
	/** その他条件区分 */
	public List<String> other_condition_kbn_list;
	/** 求める人物像 */
	public String person_hunting;
	/** 勤務時間 */
	public String working_hours;
	/** 待遇選択 */
	public List<String> treatment_kbn_list;
	/** 待遇 */
	public String treatment;
	/** 休日・休暇 */
	public String holiday;
	/** 表示順 */
	public String display_order;
	/** 登録済みかどうか */
	public String edit_flg;
	/** 店舗 */
	public List<String> shop_id_list;

}
