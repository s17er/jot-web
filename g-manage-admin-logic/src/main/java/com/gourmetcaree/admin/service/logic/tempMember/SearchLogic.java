package com.gourmetcaree.admin.service.logic.tempMember;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.seasar.framework.beans.util.Beans;

import com.google.common.collect.Lists;
import com.gourmetcaree.admin.service.accessor.tempMember.SearchAccessor;
import com.gourmetcaree.admin.service.dto.tempMember.DisplayListDto;
import com.gourmetcaree.admin.service.logic.AbstractAdminLogic;
import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.common.util.GourmetCareeUtil;
import com.gourmetcaree.common.util.SqlUtils;
import com.gourmetcaree.db.common.constants.MTypeConstants;
import com.gourmetcaree.db.common.constants.MTypeConstants.IndustryKbn;
import com.gourmetcaree.db.common.entity.member.TTempMember;
import com.gourmetcaree.db.common.entity.member.TTempMemberAttribute;
import com.gourmetcaree.db.common.enums.DeleteFlgKbn;
import com.gourmetcaree.db.common.helper.PageNavigateHelper;
import com.gourmetcaree.db.common.service.MemberService;
import com.gourmetcaree.db.common.service.member.TempMemberAreaService;
import com.gourmetcaree.db.common.service.member.TempMemberAttributeService;
import com.gourmetcaree.db.common.service.member.TempMemberService;

/**
 * 仮会員検索ロジック
 * @author nakamori
 *
 */
public class SearchLogic extends AbstractAdminLogic {

	@Resource
	private TempMemberService tempMemberService;

	@Resource
	private TempMemberAreaService tempMemberAreaService;

	@Resource
	private TempMemberAttributeService tempMemberAttributeService;

	public List<DisplayListDto> search(SearchAccessor accessor, PageNavigateHelper pageNavi, int targetPage) throws ParseException {

		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();

		createSql(sql, params, accessor);

		// ページナビゲータを使用
		int count = (int)jdbcManager.getCountBySql(sql.toString(), params.toArray());
		pageNavi.changeAllCount(count);
		pageNavi.setPage(targetPage);

		if (count == 0) {
			return Lists.newArrayList();
		}
		sql.append(" ORDER BY m.id DESC");

		List<Integer> memberIdList = jdbcManager.selectBySql(Integer.class, sql.toString(), params.toArray())
											.limit(pageNavi.limit)
											.offset(pageNavi.offset)
											.getResultList();

		return convertMemberList(memberIdList);
	}




	private List<DisplayListDto> convertMemberList(List<Integer> memberIdList) {
		List<TTempMember> memberList = tempMemberService.findByIds("id DESC", memberIdList.toArray(new Integer[0]));
		// TODO エリア・属性をつける。その他にアクション側でやっているものがあるので、それもつける。

		List<DisplayListDto> list = Lists.newArrayList();
		for (TTempMember member : memberList) {
			DisplayListDto dto = Beans.createAndCopy(DisplayListDto.class, member)
										.execute();

			dto.memberAreaList = tempMemberAreaService.findAreaListByTempMemberId(member.id);
			convertAttributes(dto, member.id);
			dto.age = String.valueOf(GourmetCareeUtil.convertToAge(member.birthday));
			dto.detailPath = String.format("/tempMember/detail/%d", member.id);
			list.add(dto);
		}

		return list;


	}

	private void convertAttributes(DisplayListDto dto, Integer tempMmeberId) {
		List<TTempMemberAttribute> attrList = tempMemberAttributeService.findByTempMemberId(tempMmeberId);

		dto.industryList = Lists.newArrayList();
		dto.jobList = Lists.newArrayList();
		for (TTempMemberAttribute attr : attrList) {
			if (MTypeConstants.IndustryKbn.TYPE_CD.equals(attr.attributeCd)) {
				dto.industryList.add(attr.attributeValue);
			} else if (MTypeConstants.JobKbn.TYPE_CD.equals(attr.attributeCd)) {
				dto.jobList.add(attr.attributeValue);
			}
		}

	}




	/**
	 * 検索用SQLを生成
	 * @param sqlStr SQL文字列
	 * @param params パラメータ
	 * @throws ParseException
	 */
	private void createSql(StringBuilder sqlStr, List<Object> params, SearchAccessor accessor ) throws ParseException {

		sqlStr.append("SELECT m.id  FROM t_temp_member m LEFT JOIN t_temp_member_attribute ma ON m.id = ma.temp_member_id ");
		sqlStr.append(" AND ma.delete_flg = ? ");
		params.add(DeleteFlgKbn.NOT_DELETED);

//		// 事前登録のみの会員は検索しない
//		sqlStr.append("WHERE m.member_kbn != ? ");
//		params.add(MTypeConstants.MemberKbn.ADVANCED_REGISTRATION_MEMBER);


		sqlStr.append(" WHERE m.delete_flg = ? ");
		params.add(DeleteFlgKbn.NOT_DELETED);

		// 会員IDが選択されている場合
		if (StringUtils.isNotBlank(accessor.getWhere_id())) {
			sqlStr.append("AND m.id = ? ");
			params.add(NumberUtils.toInt(accessor.getWhere_id()));
		}

		if (NumberUtils.isDigits(accessor.getWhere_memberRegisteredFlg())) {
			sqlStr.append(" AND m.")
				.append(TTempMember.MEMBER_REGISTERED_FLG)
				.append(" = ? ");
			params.add(Integer.parseInt(accessor.getWhere_memberRegisteredFlg()));
		}

		// 会員名が選択されている場合
		if (StringUtils.isNotBlank(accessor.getWhere_name())) {
			String name = accessor.getWhere_name().replaceAll("　| ", "");

			sqlStr.append("AND REPLACE(REPLACE(m.member_name, '　', ''), ' ', '') like ? ");
            params.add("%" + name + "%");
		}

		// 会員名フリガナが選択されている場合
		if (StringUtils.isNotBlank(accessor.getWhere_nameKana())) {
			String nameKana = accessor.getWhere_nameKana().replaceAll("　| ", "");

			sqlStr.append("AND REPLACE(REPLACE(m.member_name_kana, '　', ''), ' ', '') like ? ");
            params.add("%" + nameKana + "%");
		}

		// エリアが選択されている場合
		if (StringUtils.isNotBlank(accessor.getWhere_areaCd())) {
			sqlStr.append("AND m.id IN (SELECT mma.temp_member_id FROM t_temp_member_area AS mma WHERE mma.area_cd = ? ) ");
			params.add(NumberUtils.toInt(accessor.getWhere_areaCd()));
		}

		// 都道府県が選択されている場合
		if (StringUtils.isNotBlank(accessor.getWhere_prefecturesCd())) {
			sqlStr.append("AND m.prefectures_cd = ? ");
			params.add(NumberUtils.toInt(accessor.getWhere_prefecturesCd()));
		}

		// 業種が選択されている場合
		if (StringUtils.isNotBlank(accessor.getWhere_industryCd())) {
			sqlStr.append("AND (ma.attribute_cd = ? AND ma.attribute_value = ? ) ");
			params.add(IndustryKbn.TYPE_CD);
			params.add(NumberUtils.toInt(accessor.getWhere_industryCd()));
		}

		// 雇用形態が選択されている場合
		if (StringUtils.isNotBlank(accessor.getWhere_empPtnKbn())) {
			sqlStr.append("AND m.employ_ptn_kbn = ? ");
			params.add(NumberUtils.toInt(accessor.getWhere_empPtnKbn()));
		}

		// 性別が選択されている場合
		if (StringUtils.isNotBlank(accessor.getWhere_sexKbn())) {
			sqlStr.append("AND m.sex_kbn = ? ");
			params.add(NumberUtils.toInt(accessor.getWhere_sexKbn()));
		}

		// 下限年齢が選択されている場合
		if (StringUtils.isNotBlank(accessor.getWhere_lowerAge())) {

			sqlStr.append("AND m.birthday <= ? ");
			params.add(MemberService.convertToTimestampDivYear(NumberUtils.toInt(accessor.getWhere_lowerAge()), 0));
		}

		// 上限年齢が選択されている場合
		if (StringUtils.isNotBlank(accessor.getWhere_upperAge())) {

			sqlStr.append("AND m.birthday > ? ");
			params.add(MemberService.convertToTimestampDivYear(NumberUtils.toInt(accessor.getWhere_upperAge()), 1));
		}

		// 登録日(開始)が選択されている場合
		if (StringUtils.isNotBlank(accessor.getWhere_fromRegistratedDate())) {
			sqlStr.append(" AND m.insert_datetime >= ? ");
			params.add(DateUtils.convertDateStrToTimestampAddDate(accessor.getWhere_fromRegistratedDate(), 0));
		}

		// 登録日(終了)が選択されている場合
		if (StringUtils.isNotBlank(accessor.getWhere_toRegistratedDate())) {
			sqlStr.append("AND m.insert_datetime < ? ");
			params.add(DateUtils.convertDateStrToTimestampAddDate(accessor.getWhere_toRegistratedDate(), 1));
		}



		// メールが選択されている場合
		if (StringUtils.isNotBlank(accessor.getWhere_mail())) {
			sqlStr.append("AND m.login_id like ? ");
			params.add(SqlUtils.containPercent(accessor.getWhere_mail()));
		}


		sqlStr.append("GROUP BY m.id, ma.temp_member_id ");

	}
}
