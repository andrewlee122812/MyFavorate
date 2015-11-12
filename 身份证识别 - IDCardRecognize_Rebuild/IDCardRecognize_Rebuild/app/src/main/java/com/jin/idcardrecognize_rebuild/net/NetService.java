package com.jin.idcardrecognize_rebuild.net;

import android.support.v4.util.ArrayMap;

import java.util.Map;

/**
 * Created by YaLin on 2015/9/15.
 */
public class NetService {
    public static final String DETECT_URL = "http://apis.baidu.com/apistore/idlocr/ocr";

    public static Map<String, String> detect(String content) {
        ArrayMap params = new ArrayMap();
        params.put("fromdevice", "pc");
        params.put("clientip", "10.10.10.0");
        params.put("detecttype", "LocateRecognize");
        params.put("languagetype", "CHN_ENG");
        params.put("imagetype", "1");
        params.put("image", content);
        return params;
    }

    public static Map<String, String> getDetectHeader(String apiKey) {
        ArrayMap beader = new ArrayMap();
        beader.put("apikey", apiKey);
        return beader;
    }
}
