package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.comon.CostomException;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import com.itheima.reggie.mapper.SetmealMapper;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;
    /**
     * 新增套餐 同时需要保存套餐和菜品的关联关系
     * @param setmealDto
     */
    @Transactional //事务的注解  因为关联两张表  为保持数据的一致性
    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        //保存套餐是基本信息 操作setmeal 执行insert
        this.save(setmealDto);


        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        //保存套餐和菜品的关联关系 操作setmeal_dish 执行insert操作
         setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 删除套餐 同时删除和套餐相关的菜品
     * @param ids
     */
    @Override
    @Transactional  //保证数据一致性  采用事务注解
    public void removeWithDish(List<Long> ids) {
        //select count(*) from setmeal where id in (1,2,3) and status=1;
        //停售状态下的套餐才可以删除 因此需要查询状态
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper();
        //查询条件
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);

        int count = this.count(queryWrapper);
        if(count>0){
            //如果不能删除则抛出业务异常 “正在售卖中不可删除”
            throw new CostomException("正在售卖中,不可删除");
        }

        //如果可以删除 则先删除套餐表数据
        this.removeByIds(ids);


        //delete from setmeal_dish where setmeal_id in (1,2,3)
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SetmealDish::getDishId,ids);

        //再删除关系表中的数据 setmeal_dish
        setmealDishService.remove(lambdaQueryWrapper);

    }
}
