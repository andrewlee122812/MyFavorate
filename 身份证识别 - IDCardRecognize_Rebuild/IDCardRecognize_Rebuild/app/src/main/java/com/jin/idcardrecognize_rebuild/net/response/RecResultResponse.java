package com.jin.idcardrecognize_rebuild.net.response;


import com.jin.idcardrecognize_rebuild.domain.RetData;

import java.util.ArrayList;

/**
 * Created by YaLin on 2015/8/27.
 */
public class RecResultResponse extends BaseResponse {
    public String querySign;
    public ArrayList<RetData> retData;
}
