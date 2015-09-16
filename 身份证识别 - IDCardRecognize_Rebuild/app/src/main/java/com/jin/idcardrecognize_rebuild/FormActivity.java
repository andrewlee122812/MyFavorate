package com.jin.idcardrecognize_rebuild;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.jin.idcardrecognize_rebuild.domain.RetData;
import com.jin.idcardrecognize_rebuild.utils.IdCardUtil;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by YaLin on 2015/9/15.
 */
public class FormActivity extends BaseLoadActivity {
    public static final String FIELDS_KEY = "fields";
    EditText etAddress;
    EditText etDate;
    EditText etName;
    EditText etNation;
    EditText etNum;
    EditText etSex;
    ArrayList<RetData> idCardFields;
    Map<String, String> idCardMap;

    public static void open(Activity paramActivity, ArrayList<RetData> paramArrayList) {
        Intent localIntent = new Intent(paramActivity, FormActivity.class);
        localIntent.putParcelableArrayListExtra(FIELDS_KEY, paramArrayList);
        paramActivity.startActivity(localIntent);
    }

    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_form);
        this.idCardFields = getIntent().getParcelableArrayListExtra(FIELDS_KEY);
        this.idCardMap = IdCardUtil.dealIdCardFiled(this.idCardFields);
        initView();
        initData();
    }

    void initData() {
        etName.setText(idCardMap.get(IdCardUtil.nameField));
        etSex.setText(idCardMap.get(IdCardUtil.sexField));
        etNation.setText(idCardMap.get(IdCardUtil.nationField));
        etDate.setText(idCardMap.get(IdCardUtil.dateField));
        etAddress.setText(idCardMap.get(IdCardUtil.addressField));
        etNum.setText(idCardMap.get(IdCardUtil.idField));
    }

    void initView() {
        etName = ((EditText) findViewById(R.id.et_name));
        etSex = ((EditText) findViewById(R.id.et_sex));
        etNation = ((EditText) findViewById(R.id.et_nation));
        etDate = ((EditText) findViewById(R.id.et_date));
        etAddress = ((EditText) findViewById(R.id.et_address));
        etNum = ((EditText) findViewById(R.id.et_id_num));
    }
}