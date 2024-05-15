package com.example.controller;

import com.example.anno.OperationLogRecord;
import com.example.anno.RoleCheck;
import com.example.pojo.*;
import com.example.service.MailService;
import com.example.service.OrderService;
import com.example.service.UserService;
import com.example.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private MailService mailService;

    @RoleCheck(User.roleValue)
    @PostMapping("/sendEmail")
    public void sendTextMail(@RequestBody List<Order> orderlist) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userID = (Integer) map.get("id");
        User userData = userService.selectEmail(userID);
        String accountName = userData.getAccountName();
        String to = userData.getEmail();
        String subject = "订单信息";
        String text = "";
        StringBuilder goodslist = new StringBuilder();
        String buyTime = orderlist.get(0).getBuyTime();
        for (Order order : orderlist) {
            goodslist.append(order.getProductName()).append(" ").append(order.getQuantity()).append(" 件\n");
        }
        text = String.format("用户" + accountName + "您在" + buyTime + "总共购买了 %s 种商品,分别为:\n"
                + goodslist, orderlist.size());
        mailService.sendTextMailMessage(to, subject, text);
    }

    @RoleCheck(User.roleValue)
    // 增加新订单信息
    @PostMapping("/add")
    public Result add(@RequestBody List<Order> orderList) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        for (Order order : orderList) {
            order.setUserID(userId);
        }
        orderService.add_Order(orderList);
        return Result.success("增加订单信息成功");
    }

    @RoleCheck(User.roleValue)
    // 用户查询自己的订单信息
    @GetMapping ("/getInfoByUser")
    public Result getInfoByUser() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userID = (Integer) map.get("id");
        List<Order> order_List = orderService.getInfoBy_userId(userID);
        return Result.success(order_List);
    }

    @RoleCheck(SalesMan.roleValue)
    @OperationLogRecord("查询自己商品的订单信息")
    // 销售人员查询自己商品的订单信息
    @GetMapping("/getInfoBySalesMan")
    public Result getInfoBySalesMan() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer salesManID = (Integer) map.get("id");
        List<Order> order_List = orderService.getInfoBy_salesManId(salesManID);
        return Result.success(order_List);
    }

    @RoleCheck(SalesMan.roleValue)
    @OperationLogRecord("销售人员查询自己商品订单的统计信息")
    // 销售人员查询自己商品订单的统计信息
    @GetMapping("/getStatisticsBySalesMan")
    public Result getStatisticsBySalesMan() {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer salesManID = (Integer) map.get("id");
        List<Order> order_List = orderService.getStatisticsBySalesMan(salesManID);
        if (order_List != null) {
            return Result.success(order_List);
        } else {
            return Result.error("查询失败");
        }
    }

    @RoleCheck(Manager.roleValue)
    @OperationLogRecord("管理者根据商品类别查询所有订单的统计信息")
    // 管理者根据商品类别查询所有订单的统计信息
    @GetMapping("/getStatisticsByManager")
    public Result getStatisticsByManager() {
        List<Product> productList = orderService.getStatisticsByManager();
        if (productList != null) {
            return Result.success(productList);
        } else {
            return Result.error("查询失败");
        }
    }

    @RoleCheck({SalesMan.roleValue, Manager.roleValue})
    @OperationLogRecord("删除用户订单信息")
    // 删除用户订单信息
    @PostMapping("/deleteById")
    public Result deleteOrder(@RequestBody List<Integer> ID_List) {
        //需要考虑销售人员只能删属于自己旗下商品订单的问题
        orderService.delete_Order(ID_List);
        return Result.success("删除用户订单成功");
    }
}
