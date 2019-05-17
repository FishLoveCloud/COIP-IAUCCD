package edu.seu.controller;

import edu.seu.base.CodeEnum;
import edu.seu.base.CommonResponse;
import edu.seu.exceptions.IAUCCDException;
import edu.seu.service.CalculateService;
import edu.seu.model.CalculateObject;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @Author: yxl
 * @Date: 2019-05-15 13:55
 */
@Controller
@RequestMapping("/main")
public class IndexController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    @Autowired
    private CalculateService calculateService;

    @RequestMapping("/calculate")
    @ResponseBody
    public String calculate(CalculateObject calculateObject, @Param("selectValue") final String selectValue){
        try{
            HashMap<String, Object> data = calculateService.calculate(calculateObject, selectValue);
            return new CommonResponse(CodeEnum.SUCCESS.getValue(), "数据计算成功", data).toJSONString();
        }catch (IAUCCDException e){
            LOGGER.info(e.getMessage() + "parameter: calculate={}", calculateObject);
            return new CommonResponse(CodeEnum.CALCULATE_ERROR.getValue(), e.getMessage()).toJSONString();
        }catch (Exception e){
            LOGGER.error("/main/calculate" + "parameter: calculate={}", calculateObject, e);
            return new CommonResponse(CodeEnum.UNKNOWN_ERROR.getValue(), e.getMessage()).toJSONString();
        }
    }

    @RequestMapping("/dataGroup")
    @ResponseBody
    public String dataGroup(){
        try{
            ArrayList<CalculateObject> dataGroup = calculateService.getDataGroup();
            HashMap<String, Object> data = new HashMap<>(8);
            data.put("dataGroup", dataGroup);
            return new CommonResponse(CodeEnum.SUCCESS.getValue(), "数据组获取成功", data).toJSONString();
        }catch (Exception e){
            LOGGER.error("/main/dataGroup", e);
            return new CommonResponse(CodeEnum.UNKNOWN_ERROR.getValue(), e.getMessage()).toJSONString();
        }
    }
}
