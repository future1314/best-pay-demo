package com.github.lly835.bean.baidu;

import lombok.Data;

import java.util.List;
@Data
public class Results {

    private String currentCity;
    private List<Weather> weather_data;
    //由于篇幅，省略set/get方法，可以使用IDE自动生成它们
}