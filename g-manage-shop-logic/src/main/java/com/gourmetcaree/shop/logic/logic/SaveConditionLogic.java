package com.gourmetcaree.shop.logic.logic;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.seasar.framework.container.annotation.tiger.Component;
import org.seasar.framework.container.annotation.tiger.InstanceType;

import com.gourmetcaree.common.util.DateUtils;
import com.gourmetcaree.db.common.entity.TCustomerSearchCondition;
import com.gourmetcaree.db.common.service.CustomerSearchConditionService;

import net.arnx.jsonic.JSON;


@Component(instance=InstanceType.SINGLETON)
public class SaveConditionLogic extends AbstractShopLogic {

	/** 顧客検索条件サービス */
	@Resource
	protected CustomerSearchConditionService customerSearchConditionService;


	/**
	 * 顧客の検索条件を保存する
	 * @param customerId
	 * @param dto
	 */
	public void saveCondition(int customerId, CustomerSearchConditionDto dto) {
		List<TCustomerSearchCondition> list = customerSearchConditionService.findByCustomerId(customerId);

		if(CollectionUtils.isNotEmpty(list)) {
			updateCustomerSeaechCondiiton(dto, list.get(0));
		}else {
			saveCustomerSearchCondition(customerId, dto);
		}
	}

	/**
	 * 顧客の検索条件を更新する
	 * @param dto
	 * @param entity
	 */
	private void updateCustomerSeaechCondiiton(CustomerSearchConditionDto dto, TCustomerSearchCondition entity) {
		entity.searchCondition  = JSON.encode(dto, true);
		entity.saveDatetime = DateUtils.getJustTimestamp();
		customerSearchConditionService.update(entity);
	}

	/**
	 * 顧客の検索条件を登録する
	 * @param customerId
	 * @param dto
	 */
	private void saveCustomerSearchCondition(int customerId, CustomerSearchConditionDto dto) {
		TCustomerSearchCondition entity = new TCustomerSearchCondition();
		entity.customerId = customerId;
		entity.searchCondition  = JSON.encode(dto, true);
		entity.saveDatetime = DateUtils.getJustTimestamp();
		customerSearchConditionService.insert(entity);
	}

	/**
	 *顧客が検索条件を保存しているか確認する
	 * @param customerId
	 * @return
	 */
	public boolean existSearchCondition(int customerId) {
		return CollectionUtils.isNotEmpty(customerSearchConditionService.findByCustomerId(customerId));
	}

	/**
	 * 企業が登録している検索条件をDTOで取得する
	 * @param customerId
	 * @return
	 */
	public CustomerSearchConditionDto getSaveConditions(int customerId) {
		List<TCustomerSearchCondition> list = customerSearchConditionService.findByCustomerId(customerId);
		if (CollectionUtils.isEmpty(list)) {
			return new CustomerSearchConditionDto();
		}
		TCustomerSearchCondition conditions = list.get(0);
		return JSON.decode(conditions.searchCondition, CustomerSearchConditionDto.class);
	}
}
