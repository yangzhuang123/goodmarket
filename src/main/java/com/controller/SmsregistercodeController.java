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

import com.entity.SmsregistercodeEntity;
import com.entity.view.SmsregistercodeView;

import com.service.SmsregistercodeService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.EncryptUtil;
import com.utils.MPUtil;
import com.utils.MapUtils;
import com.utils.CommonUtil;
import java.io.IOException;

/**
 * 短信验证码
 * 后端接口
 * @author 
 * @email 
 * @date 2025-02-11 00:17:05
 */
@RestController
@RequestMapping("/smsregistercode")
public class SmsregistercodeController {
    @Autowired
    private SmsregistercodeService smsregistercodeService;






    



    /**
     * 后台列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,SmsregistercodeEntity smsregistercode,
		HttpServletRequest request){
        EntityWrapper<SmsregistercodeEntity> ew = new EntityWrapper<SmsregistercodeEntity>();



		PageUtils page = smsregistercodeService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, smsregistercode), params), params));
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }
    
    /**
     * 前台列表
     */
	@IgnoreAuth
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,SmsregistercodeEntity smsregistercode, 
		HttpServletRequest request){
        EntityWrapper<SmsregistercodeEntity> ew = new EntityWrapper<SmsregistercodeEntity>();

		PageUtils page = smsregistercodeService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, smsregistercode), params), params));
		
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(page,deSens);
        return R.ok().put("data", page);
    }



	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( SmsregistercodeEntity smsregistercode){
       	EntityWrapper<SmsregistercodeEntity> ew = new EntityWrapper<SmsregistercodeEntity>();
      	ew.allEq(MPUtil.allEQMapPre( smsregistercode, "smsregistercode")); 
        return R.ok().put("data", smsregistercodeService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(SmsregistercodeEntity smsregistercode){
        EntityWrapper< SmsregistercodeEntity> ew = new EntityWrapper< SmsregistercodeEntity>();
 		ew.allEq(MPUtil.allEQMapPre( smsregistercode, "smsregistercode")); 
		SmsregistercodeView smsregistercodeView =  smsregistercodeService.selectView(ew);
		return R.ok("查询短信验证码成功").put("data", smsregistercodeView);
    }
	
    /**
     * 后台详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        SmsregistercodeEntity smsregistercode = smsregistercodeService.selectById(id);
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(smsregistercode,deSens);
        return R.ok().put("data", smsregistercode);
    }

    /**
     * 前台详情
     */
	@IgnoreAuth
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        SmsregistercodeEntity smsregistercode = smsregistercodeService.selectById(id);
				Map<String, String> deSens = new HashMap<>();
				DeSensUtil.desensitize(smsregistercode,deSens);
        return R.ok().put("data", smsregistercode);
    }
    



    /**
     * 后台保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody SmsregistercodeEntity smsregistercode, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(smsregistercode);
        smsregistercodeService.insert(smsregistercode);
        return R.ok().put("data",smsregistercode.getId());
    }
    
    /**
     * 前台保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody SmsregistercodeEntity smsregistercode, HttpServletRequest request){
    	//ValidatorUtils.validateEntity(smsregistercode);
        smsregistercodeService.insert(smsregistercode);
        return R.ok().put("data",smsregistercode.getId());
    }



     /**
     * 获取用户密保
     */
    @RequestMapping("/security")
    @IgnoreAuth
    public R security(@RequestParam String username){
        SmsregistercodeEntity smsregistercode = smsregistercodeService.selectOne(new EntityWrapper<SmsregistercodeEntity>().eq("", username));
        return R.ok().put("data", smsregistercode);
    }


    /**
     * 修改
     */
    @RequestMapping("/update")
    @Transactional
    @IgnoreAuth
    public R update(@RequestBody SmsregistercodeEntity smsregistercode, HttpServletRequest request){
        //ValidatorUtils.validateEntity(smsregistercode);
        //全部更新
        smsregistercodeService.updateById(smsregistercode);

        return R.ok();
    }



    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        smsregistercodeService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
	
	/**
     * 前台智能排序
     */
	@IgnoreAuth
    @RequestMapping("/autoSort")
    public R autoSort(@RequestParam Map<String, Object> params,SmsregistercodeEntity smsregistercode, HttpServletRequest request,String pre){
        EntityWrapper<SmsregistercodeEntity> ew = new EntityWrapper<SmsregistercodeEntity>();
        Map<String, Object> newMap = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
		Iterator<Map.Entry<String, Object>> it = param.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Object> entry = it.next();
			String key = entry.getKey();
			String newKey = entry.getKey();
			if (pre.endsWith(".")) {
				newMap.put(pre + newKey, entry.getValue());
			} else if (StringUtils.isEmpty(pre)) {
				newMap.put(newKey, entry.getValue());
			} else {
				newMap.put(pre + "." + newKey, entry.getValue());
			}
		}
		params.put("sort", "clicktime");
        params.put("order", "desc");
		PageUtils page = smsregistercodeService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, smsregistercode), params), params));
        return R.ok().put("data", page);
    }











}
