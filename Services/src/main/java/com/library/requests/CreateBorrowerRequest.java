package com.library.requests;

import jakarta.validation.constraints.NotNull;

public class CreateBorrowerRequest {

    @NotNull(message = "name is required")
    private String name;
    @NotNull(message = "address is required")
    private String address;
    @NotNull(message = "phone is required")
    private String phone;
    @NotNull(message = "ssn is required")
    private String ssn;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }


}
