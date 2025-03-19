package com.rabbiter.em.controller;

import com.rabbiter.em.annotation.Authority;
import com.rabbiter.em.constants.Constants;
import com.rabbiter.em.common.Result;
import com.rabbiter.em.entity.AuthorityType;
import com.rabbiter.em.entity.Good;
import com.rabbiter.em.entity.Standard;
import com.rabbiter.em.service.GoodService;
import com.rabbiter.em.service.StandardService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController // 声明这是一个RESTful控制器
@RequestMapping("/api/good") // 将所有请求映射到/api/good路径下
public class GoodController {
    @Resource // 注入GoodService实例
    private GoodService goodService;

    @Resource // 注入StandardService实例
    private StandardService standardService;

    @Authority(AuthorityType.requireAuthority) // 需要权限验证
    @PostMapping // 处理POST请求，保存商品
    public Result save(@RequestBody Good good) {
        System.out.println(good); // 打印商品信息
        return Result.success(goodService.saveOrUpdateGood(good)); // 保存或更新商品并返回结果
    }

    @Authority(AuthorityType.requireAuthority) // 需要权限验证
    @PutMapping // 处理PUT请求，更新商品
    public Result update(@RequestBody Good good) {
        goodService.update(good); // 更新商品
        return Result.success(); // 返回成功结果
    }

    @Authority(AuthorityType.requireAuthority) // 需要权限验证
    @DeleteMapping("/{id}") // 处理DELETE请求，删除指定ID的商品
    public Result delete(@PathVariable Long id) {
        goodService.deleteGood(id); // 删除商品
        return Result.success(); // 返回成功结果
    }

    @GetMapping("/{id}") // 处理GET请求，查询指定ID的商品
    public Result findById(@PathVariable Long id) {
        return Result.success(goodService.getGoodById(id)); // 返回商品信息
    }

    // 获取商品的规格信息
    @GetMapping("/standard/{id}")
    public Result getStandard(@PathVariable int id) {
        return Result.success(goodService.getStandard(id)); // 返回商品规格信息
    }

    // 查询推荐商品，即recommend=1
    @GetMapping
    public Result findAll() {
        return Result.success(goodService.findFrontGoods()); // 返回推荐商品列表
    }

    // 查询销量排行
    @GetMapping("/rank")
    public Result getSaleRank(@RequestParam int num) {
        return Result.success(goodService.getSaleRank(num)); // 返回销量排行
    }

    // 保存商品的规格信息
    @PostMapping("/standard")
    public Result saveStandard(@RequestBody List<Standard> standards, @RequestParam int goodId) {
        standardService.deleteAll(goodId); // 先删除全部旧记录
        for (Standard standard : standards) {
            standard.setGoodId(goodId); // 设置商品ID
            if (!standardService.save(standard)) { // 插入新记录
                return Result.error(Constants.CODE_500, "保存失败"); // 保存失败返回错误
            }
        }
        return Result.success(); // 返回成功结果
    }

    // 删除商品的规格信息
    @Authority(AuthorityType.requireAuthority) // 需要权限验证
    @DeleteMapping("/standard")
    public Result delStandard(@RequestBody Standard standard) {
        boolean delete = standardService.delete(standard); // 删除规格信息
        if (delete) {
            return Result.success(); // 删除成功返回结果
        } else {
            return Result.error(Constants.CODE_500, "删除失败"); // 删除失败返回错误
        }
    }

    // 修改商品的推荐字段
    @Authority(AuthorityType.requireAuthority) // 需要权限验证
    @GetMapping("/recommend")
    public Result setRecommend(@RequestParam Long id, @RequestParam Boolean isRecommend) {
        return Result.success(goodService.setRecommend(id, isRecommend)); // 设置推荐字段并返回结果
    }

    @GetMapping("/page") // 处理分页查询请求
    public Result findPage(
            @RequestParam(required = false, defaultValue = "1") Integer pageNum, // 页码，默认值为1
            @RequestParam(required = false, defaultValue = "10") Integer pageSize, // 每页大小，默认值为10
            @RequestParam(required = false, defaultValue = "") String searchText, // 搜索文本，默认值为空
            @RequestParam(required = false) Integer categoryId) { // 分类ID，可选参数

        return Result.success(goodService.findPage(pageNum, pageSize, searchText, categoryId)); // 返回分页查询结果
    }

    @GetMapping("/fullPage") // 处理完整分页查询请求
    public Result findFullPage(
            @RequestParam(required = false, defaultValue = "1") Integer pageNum, // 页码，默认值为1
            @RequestParam(required = false, defaultValue = "10") Integer pageSize, // 每页大小，默认值为10
            @RequestParam(required = false, defaultValue = "") String searchText, // 搜索文本，默认值为空
            @RequestParam(required = false) Integer categoryId) { // 分类ID，可选参数

        return Result.success(goodService.findFullPage(pageNum, pageSize, searchText, categoryId)); // 返回完整分页查询结果
    }
}
