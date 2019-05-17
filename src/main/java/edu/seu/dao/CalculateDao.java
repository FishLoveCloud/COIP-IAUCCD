package edu.seu.dao;

import edu.seu.model.CalculateObject;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;

/**
 * @Author: yxl
 * @Date: 2019-05-16 22:20
 */
@Mapper
public interface CalculateDao {

    String TABLE_NAME = " calculate_object ";

    String FILED_NAME = " id, name, total_employment, actual_operating_income, actual_total_enterprises, " +
            "site_area, urban_population, urbanization_rate, industrial_employment, gdp_proportion, gdp_per_capita ";

    /**
     * 获取所有数据组信息
     *
     * @return 返回所有数据组集合
     */
    @Select({"select ", FILED_NAME ," from ", TABLE_NAME})
    ArrayList<CalculateObject> selectDataGroup();

    /**
     * 根据id获取数据信息
     * @param id 数据id
     * @return 返回数据对象
     */
    @Select({"select ", FILED_NAME ," from ", TABLE_NAME, " where id=#{id}"})
    CalculateObject selectCalculateObjectById(@Param("id") Integer id);
}
