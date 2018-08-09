package com.github.lly835.bean.baidu;

import lombok.Data;

@Data
public class Weather {

    private String date;
    private String dayPictureUrl;
    private String nightPictureUrl;
    private String weather;
    private String wind;
    private String temperature;
    //由于篇幅，省略set/get方法，可以使用IDE自动生成它们
}