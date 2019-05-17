package edu.seu.model;

/**
 * @Author: yxl
 * @Date: 2019-05-15 14:11
 */
public class CalculateObject {

    private int id;

    /**
     * 数据组名称
     */
    private String name;

    /**
     * 园区就业总人数
     */
    private double totalEmployment;

    /**
     * 实际实现营业收入
     */
    private double actualOperatingIncome;

    /**
     * 实际入园企业总数
     */
    private double actualTotalEnterprises;

    /**
     * 园区已开发工业用地面积
     */
    private double siteArea;

    /**
     * 城镇人口数
     */
    private double urbanPopulation;

    /**
     * 人口城镇化率
     */
    private double urbanizationRate;

    /**
     * 城镇工业部门从业人数
     */
    private double industrialEmployment;

    /**
     * 二三产业增加值占GDP比重
     */
    private double gdpProportion;

    /**
     * 人均GDP
     */
    private double gdpPerCapita;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTotalEmployment() {
        return totalEmployment;
    }

    public void setTotalEmployment(double totalEmployment) {
        this.totalEmployment = totalEmployment;
    }

    public double getActualOperatingIncome() {
        return actualOperatingIncome;
    }

    public void setActualOperatingIncome(double actualOperatingIncome) {
        this.actualOperatingIncome = actualOperatingIncome;
    }

    public double getActualTotalEnterprises() {
        return actualTotalEnterprises;
    }

    public void setActualTotalEnterprises(double actualTotalEnterprises) {
        this.actualTotalEnterprises = actualTotalEnterprises;
    }

    public double getSiteArea() {
        return siteArea;
    }

    public void setSiteArea(double siteArea) {
        this.siteArea = siteArea;
    }

    public double getUrbanPopulation() {
        return urbanPopulation;
    }

    public void setUrbanPopulation(double urbanPopulation) {
        this.urbanPopulation = urbanPopulation;
    }

    public double getUrbanizationRate() {
        return urbanizationRate;
    }

    public void setUrbanizationRate(double urbanizationRate) {
        this.urbanizationRate = urbanizationRate;
    }

    public double getIndustrialEmployment() {
        return industrialEmployment;
    }

    public void setIndustrialEmployment(double industrialEmployment) {
        this.industrialEmployment = industrialEmployment;
    }

    public double getGdpProportion() {
        return gdpProportion;
    }

    public void setGdpProportion(double gdpProportion) {
        this.gdpProportion = gdpProportion;
    }

    public double getGdpPerCapita() {
        return gdpPerCapita;
    }

    public void setGdpPerCapita(double gdpPerCapita) {
        this.gdpPerCapita = gdpPerCapita;
    }

    @Override
    public String toString() {
        return "CalculateObject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", totalEmployment=" + totalEmployment +
                ", actualOperatingIncome=" + actualOperatingIncome +
                ", actualTotalEnterprises=" + actualTotalEnterprises +
                ", siteArea=" + siteArea +
                ", urbanPopulation=" + urbanPopulation +
                ", urbanizationRate=" + urbanizationRate +
                ", industrialEmployment=" + industrialEmployment +
                ", gdpProportion=" + gdpProportion +
                ", gdpPerCapita=" + gdpPerCapita +
                '}';
    }
}
