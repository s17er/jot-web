package com.gourmetcaree.common.taglib.tag;

import com.google.common.collect.Lists;
import com.gourmetcaree.common.dto.LabelValueDto;
import com.gourmetcaree.common.util.GcCollectionUtils;
import com.gourmetcaree.common.util.Utils;
import com.gourmetcaree.db.common.entity.MType;
import com.gourmetcaree.db.common.service.TypeService;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.seasar.framework.container.SingletonS2Container;

import javax.servlet.jsp.JspException;
import java.util.Collection;
import java.util.List;

/**
 * 区分を絞り込んだMTypeの一覧をセレクトしてLabelValueDtoのリストにして返す
 */
@Setter
public class TargetTypeListTag extends BaseTagSupport {


    private static final long serialVersionUID = -1472708071804545480L;
    /**
     * 名前
     */
    private String name;

    /**
     * 区分マスタコード
     */
    private String typeCd;



    /**
     * 非表示値
     */
    private Object target;

    /**
     * 非表示リスト
     */
    private Collection<?> targetList;

    /** 初期ラベル */
    private String blankLineLabel;

    /**
     * スコープ
     */
    private String scope = "";


    @Override
    public int doEndTag() throws JspException {

        TypeService service = SingletonS2Container.getComponent(TypeService.class);

        List<Integer> list = Lists.newArrayList();
        if (target != null) {
            list.add(Integer.parseInt(target.toString()));
        }

        if (CollectionUtils.isNotEmpty(targetList)) {
            list.addAll(GcCollectionUtils.toIntegerList(targetList));
        }

        List<MType> typeList = service.findByTypeCdAndTypeValues(typeCd, list);

        List<LabelValueDto> dtoList = LabelValueDto.newInstanceList(typeList);
        LabelValueDto.addBlankLineLabel(dtoList, blankLineLabel);

        setAttribute(name, dtoList, scope);
        return super.doEndTag();
    }
}