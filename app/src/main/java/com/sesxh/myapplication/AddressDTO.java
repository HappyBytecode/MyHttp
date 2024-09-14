package com.sesxh.myapplication;

/**
 * @author LYH
 * @date 2021/1/12
 * @time 10:01
 * @desc
 **/
public class AddressDTO {


    /**
     * Country : 中国
     * Province : 江苏省
     * City : 无锡市
     * Isp : 电信
     */

    private String Country;
    private String Province;
    private String City;
    private String Isp;

    public String getCountry() {
        return Country;
    }

    public void setCountry(String Country) {
        this.Country = Country;
    }

    public String getProvince() {
        return Province;
    }

    public void setProvince(String Province) {
        this.Province = Province;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String City) {
        this.City = City;
    }

    public String getIsp() {
        return Isp;
    }

    public void setIsp(String Isp) {
        this.Isp = Isp;
    }

    @Override
    public String toString() {
        return "AddressDTO{" +
                "Country='" + Country + '\'' +
                ", Province='" + Province + '\'' +
                ", City='" + City + '\'' +
                ", Isp='" + Isp + '\'' +
                '}';
    }
}
