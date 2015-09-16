package com.jin.idcardrecognize_rebuild.utils;

import android.support.v4.util.ArrayMap;

import com.jin.idcardrecognize_rebuild.domain.RetData;

import java.util.Iterator;
import java.util.List;

/**
 * Created by YaLin on 2015/9/15.
 */
public class IdCardUtil {
    public static final String addressField = "住址";
    public static final String dateField = "出生";
    public static final String idField = "公民身份号码";
    public static final String nameField = "姓名";
    public static final String nationField = "民族";
    public static final String sexField = "性别";
    public static final String sexFieldSub = "性";

    public static boolean containIdCardField(List<RetData> paramList) {
        StringBuffer localStringBuffer = new StringBuffer();
        Iterator iterator = paramList.iterator();
        while (iterator.hasNext()) {
            localStringBuffer.append(((RetData) iterator.next()).word);
        }
        String result = localStringBuffer.toString();
        return (result.contains(nameField)) && (result.contains(sexField)) && (result.contains(nationField)) && (result.contains(dateField)) && (result.contains(addressField)) && (result.contains(idField));
    }

    public static ArrayMap<String, String> dealIdCardFiled(List<RetData> paramList) {
        ArrayMap dataMap = new ArrayMap();
        StringBuffer addressStr = new StringBuffer();
        Iterator iterator = paramList.iterator();
        String otherStr = null;
        while (iterator.hasNext()) {
            RetData data = (RetData) iterator.next();
            if (data.word.contains(nameField)) {
                dataMap.put(nameField, data.word.trim().substring(nameField.length()));
            } else if (data.word.contains(sexFieldSub)) {
                try {
                    String sexStr = data.word.trim().substring(sexField.length(), data.word.indexOf(nationField));
                    String nationStr = data.word.trim().substring(data.word.indexOf(nationField) + nationField.length());
                    dataMap.put(sexField, sexStr);
                    dataMap.put(nationField, nationStr);
                } catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
                    return dealIdCardFiled2(paramList);
                }
            } else if (data.word.contains(dateField)) {
                dataMap.put(dateField, data.word.trim().substring(dateField.length()));
            } else if (data.word.contains(idField)) {
                dataMap.put(idField, data.word.trim().substring(idField.length()));
            } else if (data.word.contains(addressField)) {
                addressStr.append(data.word.trim().substring(addressField.length()));
            } else {
                otherStr = data.word.trim();
            }
        }
        addressStr.append(otherStr);
        dataMap.put(addressField, addressStr.toString());
        return dataMap;
    }

    private static ArrayMap<String, String> dealIdCardFiled2(List<RetData> paramList) {
        ArrayMap dataMap = new ArrayMap();
        StringBuffer addressStr = new StringBuffer();
        Iterator iterator = paramList.iterator();
        String otherStr = null;
        while (iterator.hasNext()) {
            RetData data = (RetData) iterator.next();
            if (data.word.contains(nameField)) {
                dataMap.put(nameField, data.word.trim().substring(nameField.length()));
            } else if (data.word.contains(sexFieldSub)) {
                dataMap.put(sexField, data.word.trim().substring(sexField.length()));
            } else if (data.word.contains(nationField)) {
                dataMap.put(nationField, data.word.trim().substring(nationField.length()));
            } else if (data.word.contains(dateField)) {
                dataMap.put(dateField, data.word.trim().substring(dateField.length()));
            } else if (data.word.contains(idField)) {
                dataMap.put(idField, data.word.trim().substring(idField.length()));
            } else if (data.word.contains(addressStr)) {
                addressStr.append(data.word.trim().substring(addressField.length()));
            } else {
                otherStr = data.word.trim();
            }
        }
        addressStr.append(otherStr);
        dataMap.put(addressField, addressStr.toString());
        return dataMap;
    }
}
