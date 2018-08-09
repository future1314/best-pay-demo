package com.github.lly835.bean.baidu;

import lombok.Data;

import java.util.List;
@Data
public class Status{
    private String error;
    private String status;
    private String date;
    private List<Results> results;
    //由于篇幅，省略set/get方法，可以使用IDE自动生成它们
}
