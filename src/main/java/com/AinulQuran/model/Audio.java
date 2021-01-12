package com.AinulQuran.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.cache.annotation.Cacheable;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
@Cacheable("Audio")
public class Audio {
    private int code;
    private String status;
    private ArrayList<Data> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }
}
