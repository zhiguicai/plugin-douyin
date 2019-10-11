package com.hb.douyinplugin.hook.douyin.entity;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * createTime: 2019/10/11.16:16
 * updateTime: 2019/10/11.16:16
 * author: singleMan.
 * desc:
 */

public class MobileEntity {


    @SerializedName("addresses")
    public List<String> addresses = new ArrayList<>();
    @SerializedName("birthday")
    public String birthday;
    @SerializedName("department_name")
    public String department;
    @SerializedName("emails")
    public List<String> emails = new ArrayList<>();
    public String familyName;
    public String givenName = "";
    @SerializedName("instant_message_addresses")
    public Map<String, String> instantMessageAddresses = new HashMap<>();
    @SerializedName("job_desc")
    public String jobDesc;
    @SerializedName("image_url")
    public String mImageUrl;
    @SerializedName("modification_date")
    public String modificationDate = "2019-04-02 11:49:51";
    @SerializedName("name")
    public String name;
    @SerializedName("nick_name")
    public String nickName;
    @SerializedName("note")
    public String note;
    @SerializedName("organization_name")
    public String organization;
    @SerializedName("phone_number")
    public List<String> phoneNumber = new ArrayList<>();
    public String section;
    @SerializedName("urls")
    public List<String> urls = new ArrayList<>();

    public List<String> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public Map<String, String> getInstantMessageAddresses() {
        return instantMessageAddresses;
    }

    public void setInstantMessageAddresses(Map<String, String> instantMessageAddresses) {
        this.instantMessageAddresses = instantMessageAddresses;
    }

    public String getJobDesc() {
        return jobDesc;
    }

    public void setJobDesc(String jobDesc) {
        this.jobDesc = jobDesc;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(String modificationDate) {
        this.modificationDate = modificationDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public List<String> getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(List<String> phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
}
