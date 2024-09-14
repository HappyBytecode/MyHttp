package com.sesxh.myapplication;

/**
 * @author LYH
 * @date 2021/3/16
 * @time 11:31
 * @desc
 **/
public class TestDTO {
    private AddressDTO mAddressDTO;
    private PhoneDTO mPhoneDTO;
    private String mName;

    public AddressDTO getAddressDTO() {
        return mAddressDTO;
    }

    public void setAddressDTO(AddressDTO addressDTO) {
        mAddressDTO = addressDTO;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public PhoneDTO getPhoneDTO() {
        return mPhoneDTO;
    }

    public void setPhoneDTO(PhoneDTO phoneDTO) {
        mPhoneDTO = phoneDTO;
    }
}
