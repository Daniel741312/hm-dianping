package com.hmdp.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.Shop;
import com.hmdp.entity.ShopType;
import com.hmdp.mapper.ShopTypeMapper;
import com.hmdp.service.IShopTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public Result listShopType() {
        String shopTypeJson = stringRedisTemplate.opsForValue().get("cache:shop-type");
        if (StrUtil.isNotBlank(shopTypeJson)) {
            List<ShopType> shoptype = JSONUtil.toList(shopTypeJson, ShopType.class);
            return Result.ok(shoptype);
        }

        List<ShopType> shoptype = query().orderByAsc("sort").list();
        stringRedisTemplate.opsForValue().set("cache:shop-type", JSONUtil.toJsonStr(shoptype));
        return Result.ok(shoptype);
    }
}
