/*
 * Copyright 2013 Qunar.com All right reserved. This software is the confidential and proprietary information of
 * Qunar.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with Qunar.com.
 */
package cn.zzfyip.search.controller.anonymous;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * 实现描述：匿名用户订单相关数据接口
 * 
 * @author simon
 * @version v1.0.0
 * @see
 * @since 2013-10-16 下午3:30:29
 */
@Controller("anonymousOrderDataController")
@RequestMapping("/anonymous/order")
public class OrderDataController {

    /**
     * 查询一笔订单最大可使用的优惠券金额
     * 
     * @param cardNo 优惠券号
     * @param productId 产品ID（加密）
     * @param quantity 订单数
     * @return
     */
//    @ResponseBody
//    @RequestMapping(params = "method=queryCardCanUseAmount", method = { RequestMethod.GET })
//    public StandardJsonObject queryCardCanUseAmount(QueryCardCanUserAmountInputDto queryCardCanUserAmountInputDto)
//            throws Exception {
//        if (queryCardCanUserAmountInputDto.checkParamInvaild()) {
//            return StandardJsonObject.newErrorJsonObject("优惠码查询参数错误");
//        }
//
//        QueryCardCanUserAmountOutputDto queryCardCanUserAmountOutputDto;
//
//        Integer priceId = IDEncryptor.getInstance().decrypt(queryCardCanUserAmountInputDto.getPriceId());
//        Integer productId = IDEncryptor.getInstance().decrypt(queryCardCanUserAmountInputDto.getProductId());
//        Integer quantity = Integer.valueOf(queryCardCanUserAmountInputDto.getQuantity());
//        OrderSource orderSource = OrderSource.UNKNOWN;
//        if (StringUtils.isNotEmpty(queryCardCanUserAmountInputDto.getOrderSource())) {
//            orderSource = OrderSource.create(queryCardCanUserAmountInputDto.getOrderSource());
//        }
//        queryCardCanUserAmountOutputDto = orderQueryService.findCardUserAmount(
//                queryCardCanUserAmountInputDto.getCardNo(), productId, priceId, quantity, orderSource);
//
//        return StandardJsonObject.newCorrectJsonObject(queryCardCanUserAmountOutputDto);
//    }


}
