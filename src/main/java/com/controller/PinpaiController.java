package com.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;
import java.util.List;
import java.util.Collections;

import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import com.utils.ValidatorUtils;
import com.utils.DeSensUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.annotation.IgnoreAuth;

import com.entity.PinpaiEntity;
import com.entity.view.PinpaiView;

import com.service.PinpaiService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.EncryptUtil;
import com.utils.MPUtil;
import com.utils.MapUtils;
import com.utils.CommonUtil;
import java.io.IOException;

/**
 * 品牌
 * 后端接口
 * @author 
 * @email 
 * @date 2025-02-11 00:17:04
 */
@RestController
@RequestMapping("/pinpai")
public class PinpaiController {
    @Autowired
    private PinpaiService pinpaiService;






    



    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,PinpaiEntity pinpai,
		HttpServletRequest request){
        EntityWrapper<PinpaiEntity> ew = new EntityWrapper<PinpaiEntity>();



		PageUtils page = pinpaiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, pinpai), params), params));
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }
    
    /**
     * 前台列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,PinpaiEntity pinpai, 
		HttpServletRequest request){
        EntityWrapper<PinpaiEntity> ew = new EntityWrapper<PinpaiEntity>();

		PageUtils page = pinpaiService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, pinpai), params), params));
		
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }



	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( PinpaiEntity pinpai){
       	EntityWrapper<PinpaiEntity> ew = new EntityWrapper<PinpaiEntity>();
      	ew.allEq(MPUtil.allEQMapPre( pinpai, "pinpai")); 
        return R.ok().put("data", pinpaiService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(PinpaiEntity pinpai){
        EntityWrapper< PinpaiEntity> ew = new EntityWrapper< PinpaiEntity>();
 		ew.allEq(MPUtil.allEQMapPre( pinpai, "pinpai")); 
		PinpaiView pinpaiView =  pinpaiService.selectView(ew);
		return R.ok("查询品牌成功").put("data", pinpaiView);
    }
	
    /**
     * 后台详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        PinpaiEntity pinpai = pinpaiService.selectById(id);
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(pinpai,deSens);
        return R.ok().put("data", pinpai);
    }

    /**
     * 前台详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        PinpaiEntity pinpai = pinpaiService.selectById(id);
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(pinpai,deSens);
        return R.ok().put("data", pinpai);
    }
    



    /**
     * 后台保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody PinpaiEntity pinpai, HttpServletRequest request){
        if(pinpaiService.selectCount(new EntityWrapper<PinpaiEntity>().eq("pinpai", pinpai.getPinpai()))>0) {
            return R.error("品牌已存在");
        }
    	//ValidatorUtils.validateEntity(pinpai);
        pinpaiService.insert(pinpai);
        return R.ok().put("data",pinpai.getId());
    }
    
    /**
     * 前台保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody PinpaiEntity pinpai, HttpServletRequest request){
        if(pinpaiService.selectCount(new EntityWrapper<PinpaiEntity>().eq("pinpai", pinpai.getPinpai()))>0) {
            return R.error("品牌已存在");
        }
    	//ValidatorUtils.validateEntity(pinpai);
        pinpaiService.insert(pinpai);
        return R.ok().put("data",pinpai.getId());
    }





    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    public R update(@RequestBody PinpaiEntity pinpai, HttpServletRequest request){
        //ValidatorUtils.validateEntity(pinpai);
        if(pinpaiService.selectCount(new EntityWrapper<PinpaiEntity>().ne("id", pinpai.getId()).eq("pinpai", pinpai.getPinpai()))>0) {
            return R.error("品牌已存在");
        }
        //全部更新
        pinpaiService.updateById(pinpai);

        return R.ok();
    }



    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        pinpaiService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
	











}
