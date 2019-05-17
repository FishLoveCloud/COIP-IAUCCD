package edu.seu.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import edu.seu.base.CodeEnum;
import edu.seu.dao.CalculateDao;
import edu.seu.exceptions.IAUCCDException;
import edu.seu.model.CalculateObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @Author: yxl
 * @Date: 2019-05-15 14:23
 */
@Service
public class CalculateService {

    @Autowired
    private CalculateDao calculateDao;

    public HashMap<String, Object> calculate(CalculateObject calculateObject, String selectValue) throws IAUCCDException {
        JSONArray jsonArray = (JSONArray) JSONArray.parse(selectValue);
        int len = jsonArray.size();
        ArrayList<Integer> ids = new ArrayList<>(8);
        for (int i = 0; i < len; i++) {
            JSONObject info = jsonArray.getJSONObject(i);
            String value = info.getString("value");
            ids.add(Integer.valueOf(value));
        }

        ArrayList<CalculateObject> allData = new ArrayList<>(8);
        allData.add(calculateObject);
        for (int id : ids) {
            CalculateObject data = calculateDao.selectCalculateObjectById(id);
            allData.add(data);
        }
        final int allLength = allData.size();
        final int leastSize = 2;
        if (allLength < leastSize) {
            throw new IAUCCDException(CodeEnum.CALCULATE_ERROR, "至少选择一个数据组");
        }

        ArrayList<Double> employmentAggDegreeList = new ArrayList<>(16);
        ArrayList<Double> totalEmploymentList = new ArrayList<>(16);
        ArrayList<Double> actualOperatingIncomeList = new ArrayList<>(16);
        ArrayList<Double> actualTotalEnterprisesList = new ArrayList<>(16);
        ArrayList<Double> siteAreaList = new ArrayList<>(16);
        ArrayList<Double> urbanPopulationList = new ArrayList<>(16);
        ArrayList<Double> urbanizationRateList = new ArrayList<>(16);
        ArrayList<Double> gdpProportionList = new ArrayList<>(16);
        ArrayList<Double> gdpPerCapitaList = new ArrayList<>(16);

        for (CalculateObject data : allData) {
            employmentAggDegreeList.add(data.getTotalEmployment() / data.getIndustrialEmployment());
            totalEmploymentList.add(data.getTotalEmployment());
            actualOperatingIncomeList.add(data.getActualOperatingIncome());
            actualTotalEnterprisesList.add(data.getActualTotalEnterprises());
            siteAreaList.add(data.getSiteArea());
            urbanPopulationList.add(data.getUrbanPopulation());
            urbanizationRateList.add(data.getUrbanizationRate());
            gdpProportionList.add(data.getGdpProportion());
            gdpPerCapitaList.add(data.getGdpPerCapita());
        }

        // 计算每组数据的Z-Score值
        ArrayList<Double> employmentAggDegreeZScore = zScore(employmentAggDegreeList);
        ArrayList<Double> totalEmploymentZScore = zScore(totalEmploymentList);
        ArrayList<Double> actualOperatingIncomeZScore = zScore(actualOperatingIncomeList);
        ArrayList<Double> actualTotalEnterprisesZScore = zScore(actualTotalEnterprisesList);
        ArrayList<Double> siteAreaZScore = zScore(siteAreaList);
        ArrayList<Double> urbanPopulationZScore = zScore(urbanPopulationList);
        ArrayList<Double> urbanizationRateZScore = zScore(urbanizationRateList);
        ArrayList<Double> gdpProportionZScore = zScore(gdpProportionList);
        ArrayList<Double> gdpPerCapitaZScore = zScore(gdpPerCapitaList);

        // 计算每组数据的序变量有序度值
        ArrayList<Double> employmentAggDegreeOrderVariable = orderVariable(employmentAggDegreeZScore);
        ArrayList<Double> totalEmploymentOrderVariable = orderVariable(totalEmploymentZScore);
        ArrayList<Double> actualOperatingIncomeVariable = orderVariable(actualOperatingIncomeZScore);
        ArrayList<Double> actualTotalEnterprisesVariable = orderVariable(actualTotalEnterprisesZScore);
        ArrayList<Double> siteAreaOrderVariable = orderVariable(siteAreaZScore);
        ArrayList<Double> urbanPopulationOrderVariable = orderVariable(urbanPopulationZScore);
        ArrayList<Double> urbanizationRateOrderVariable = orderVariable(urbanizationRateZScore);
        ArrayList<Double> gdpProportionOrderVariable = orderVariable(gdpProportionZScore);
        ArrayList<Double> gdpPerCapitaOrderVariable = orderVariable(gdpPerCapitaZScore);

        // 计算每组数据的序参数有序度的值
        ArrayList<Double> employmentAggDegreeOrderParameter = employmentAggDegreeOrderVariable;
        ArrayList<Double> firstOrderParameter = new ArrayList<>(16);
        ArrayList<Double> secondOrderParameter = new ArrayList<>(16);
        ArrayList<Double> thirdOrderParameter = new ArrayList<>(16);
        for (int i = 0; i < allLength; i++) {
            double sqrt = Math.sqrt(Math.sqrt(totalEmploymentOrderVariable.get(i) * actualOperatingIncomeVariable.get(i) *
                    actualTotalEnterprisesVariable.get(i) * siteAreaOrderVariable.get(i)));
            firstOrderParameter.add(sqrt);

            double sqrt1 = Math.sqrt(urbanPopulationOrderVariable.get(i) * urbanizationRateOrderVariable.get(i));
            secondOrderParameter.add(sqrt1);

            double sqrt2 = Math.sqrt(gdpProportionOrderVariable.get(i) * gdpPerCapitaOrderVariable.get(i));
            thirdOrderParameter.add(sqrt2);
        }

        // 计算每组数据的子系统有序度
        ArrayList<Double> firstChildOrder = new ArrayList<>(16);
        ArrayList<Double> secondChildOrder = new ArrayList<>(16);
        for (int i = 0; i < allLength; i++) {
            double sqrt = Math.sqrt(employmentAggDegreeOrderParameter.get(i) * firstOrderParameter.get(i));
            firstChildOrder.add(sqrt);

            double sqrt1 = Math.sqrt(secondOrderParameter.get(i) * thirdOrderParameter.get(i));
            secondChildOrder.add(sqrt1);
        }

        // 系统耦合度计算
        ArrayList<Double> systemCouplingDegree = new ArrayList<>(16);
        for (int i = 0; i < allLength; i++) {
            double numerator = firstChildOrder.get(i) * secondChildOrder.get(i);
            double denominator = firstChildOrder.get(i) + secondChildOrder.get(i);
            double sqrt = Math.sqrt(numerator / (denominator * denominator));
            systemCouplingDegree.add(sqrt);
        }

        // 耦合协调度计算
        double degree = Math.sqrt((0.5 * firstChildOrder.get(0) + 0.5 * secondChildOrder.get(0)) * systemCouplingDegree.get(0));
        String level = "";
        if (degree < 0.2 && degree >= 0) {
            level = "很低";
        } else if (degree >= 0.2 && degree < 0.4) {
            level = "较低";
        } else if (degree >= 0.4 && degree < 0.6) {
            level = "一般";
        } else if (degree >= 0.6 && degree < 0.8) {
            level = "较高";
        } else if (degree >= 8 && degree <= 1) {
            level = "很高";
        } else {
            level = "耦合协调度不在0到1之间";
        }

        HashMap<String, Object> map = new HashMap<>(8);
        map.put("degree", degree);
        map.put("level", level);
        return map;
    }

    /**
     * 计算序变量有序度
     *
     * @param list Z-Score集合
     * @return 返回序变量有序度集合
     */
    private ArrayList<Double> orderVariable(ArrayList<Double> list) {
        ArrayList<Double> orderVariableList = new ArrayList<>(16);
        for (double num : list) {
            double orderVariable = num * 1.1;
            orderVariableList.add(orderVariable);
        }
        int len = list.size();
        ArrayList<Double> orderVariable = new ArrayList<>(16);
        for (int i = 0; i < len; i++) {
            double min = min(orderVariableList, orderVariable, i);
            double max = max(orderVariableList, orderVariable, i);
            double v = (list.get(i) - min) / (max - min);
            orderVariable.add(v);
        }
        return orderVariable;
    }

    private double min(ArrayList<Double> orderVariableList, ArrayList<Double> orderVariable, int i) {
        double min = Double.MAX_VALUE;
        int len = orderVariableList.size();
        for (int j = i; j < len; j++) {
            min = Math.min(orderVariableList.get(j), min);
        }
        for (double v : orderVariable) {
            min = Math.min(v, min);
        }
        return min;
    }

    private double max(ArrayList<Double> orderVariableList, ArrayList<Double> orderVariable, int i) {
        double max = Double.MIN_VALUE;
        int len = orderVariableList.size();
        for (int j = i; j < len; j++) {
            max = Math.max(orderVariableList.get(j), max);
        }
        for (double v : orderVariable) {
            max = Math.max(v, max);
        }
        return max;
    }

    /**
     * 计算数据的Z-Score值
     *
     * @param list 数据集合
     * @return 返回Z-Score集合
     */
    private ArrayList<Double> zScore(ArrayList<Double> list) {
        double sum = 0;
        for (double num : list) {
            sum += num;
        }
        int len = list.size();
        // 数据平均值
        double ave = sum / len;
        // 数据标准差
        double deviationSquare = 0;
        for (double num : list) {
            deviationSquare += (num - ave) * (num - ave);
        }
        double standardDeviation = Math.sqrt((1.0 / (len - 1)) * deviationSquare);

        ArrayList<Double> zScoreList = new ArrayList<>(16);
        for (double num : list) {
            zScoreList.add((num - ave) / standardDeviation);
        }
        return zScoreList;
    }

    public ArrayList<CalculateObject> getDataGroup() throws Exception {
        return calculateDao.selectDataGroup();
    }
}
