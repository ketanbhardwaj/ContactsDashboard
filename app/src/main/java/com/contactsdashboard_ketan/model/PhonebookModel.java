package com.contactsdashboard_ketan.model;

/**
 * Created by Ketan on 3/19/17.
 */

public class PhonebookModel {

    private String name;

    private String contactId;

    private String mobile;

    private String id;

    private String picUri;

    private String lastTimeContact;

    public PhonebookModel(String name, String contactId, String mobile, String id, String picUri, String lastTimeContact) {
        this.name = name;
        this.contactId = contactId;
        this.mobile = mobile;
        this.id = id;
        this.picUri = picUri;
        this.lastTimeContact = lastTimeContact;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPicUri() {
        return picUri;
    }

    public void setPicUri(String picUri) {
        this.picUri = picUri;
    }

    public String getLastTimeContact() {
        return lastTimeContact;
    }

    public void setLastTimeContact(String lastTimeContact) {
        this.lastTimeContact = lastTimeContact;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String pic) {
        this.contactId = pic;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

}
