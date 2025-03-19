package com.rabbiter.em.controller;

import com.rabbiter.em.annotation.Authority;
import com.rabbiter.em.constants.Constants;
import com.rabbiter.em.common.Result;
import com.rabbiter.em.entity.AuthorityType;
import com.rabbiter.em.entity.Address;
import com.rabbiter.em.service.AddressService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Authority(AuthorityType.requireLogin) // 需要登录权限
@RestController // 声明这是一个RESTful控制器
@RequestMapping("/api/address") // 将所有请求映射到/api/address路径下
public class AddressController {
    @Resource // 注入AddressService实例
    private AddressService addressService;

    /*
    查询
    */
    @GetMapping("/{userId}") // 处理GET请求，查询指定用户ID的所有地址
    public Result findAllById(@PathVariable Long userId) {
        return Result.success(addressService.findAllById(userId)); // 返回指定用户ID的所有地址
    }

    @GetMapping // 处理GET请求，查询所有地址
    public Result findAll() {
        List<Address> list = addressService.list(); // 查询所有地址
        return Result.success(list); // 返回所有地址列表
    }

    /*
    保存
    */
    @PostMapping // 处理POST请求，保存地址
    public Result save(@RequestBody Address address) {
        boolean b = addressService.saveOrUpdate(address); // 保存或更新地址
        if(b){
            return Result.success(); // 保存成功返回结果
        }else{
            return Result.error(Constants.CODE_500, "保存地址失败"); // 保存失败返回错误
        }
    }

    @PutMapping // 处理PUT请求，更新地址
    public Result update(@RequestBody Address address) {
        addressService.updateById(address); // 更新地址
        return Result.success(); // 返回成功结果
    }

    /*
    删除
    */
    @DeleteMapping("/{id}") // 处理DELETE请求，删除指定ID的地址
    public Result delete(@PathVariable Long id) {
        addressService.removeById(id); // 删除地址
        return Result.success(); // 返回成功结果
    }
}
