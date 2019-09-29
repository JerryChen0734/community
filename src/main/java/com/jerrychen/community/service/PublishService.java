package com.jerrychen.community.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PublishService {

    /*
        递归分割字符串
        1.将字符串分割为处理过的和没处理过的
        2.添加处理过的进list，判断没处理过的是否还有逗号
        3.有就递归一下，没有就添加剩余字符串进list并返回
     */
    public List<String> segmentationTags(String tag) {
        List<String> stringList = new ArrayList<>();
        StringBuffer stringBuffer = new StringBuffer(tag);
        String doesStr = stringBuffer.substring(0, stringBuffer.indexOf("，"));
        String prepareStr = stringBuffer.substring(stringBuffer.indexOf("，") + 1, stringBuffer.length());
        stringList.add(doesStr);
        if (prepareStr.contains("，")) {
            stringList.addAll(segmentationTags(prepareStr));
        } else {
            prepareStr = prepareStr.replaceAll("，", null);
            stringList.add(prepareStr);
        }
        return stringList;

    }

    /*
        1.转换为中文逗号并判断标签是否重复
        2.逐个比对stringList，删除重复的
        3.转换为String返回
     */
    public String edit(String tag) {

        tag = tag.replace(",", "，");
        List<String> stringList = segmentationTags(tag);
        for (int i = 0; i < stringList.size(); i++) {
            for (int j = i + 1; j < stringList.size(); j++) {
                if (stringList.get(i).equals(stringList.get(j))) {
                    j -= 1;
                    stringList.remove(j);
                }
            }
        }
        StringBuffer stringBuffer = new StringBuffer();

        for (int i = 0; i < stringList.size(); i++) {
            if (stringList.get(i) != null && stringList.get(i) != "") {

                stringBuffer.append(stringList.get(i));
                stringBuffer.append("，");
            }

        }

        return stringBuffer.toString();
    }
}
