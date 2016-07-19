package com.lishate.activity.renwu;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.lishate.R;
import com.lishate.activity.BaseActivity;
import com.lishate.data.model.DeviceItemModel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangeLampProperty extends BaseActivity implements OnClickListener {

	private DeviceItemModel deviceitemmodel;
	private Dao<DeviceItemModel, Integer> devicedatadao = null;
	private EditText renameinput;
	private Button Okbutton;
	private String lastname = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.renamedevice);
		Intent mIntent = getIntent();
		deviceitemmodel = (DeviceItemModel) mIntent.getSerializableExtra("renamedevice");
		initView();
	}

	private void initView() {

		renameinput = (EditText) findViewById(R.id.renameinput);
		renameinput.setText(deviceitemmodel.getDeviceName());
		renameinput.requestFocus();
		renameinput.setSelection(deviceitemmodel.getDeviceName().length());
		Okbutton = (Button) findViewById(R.id.save_name);
		Okbutton.setOnClickListener(this);
		lastname = renameinput.getText().toString();
		try {
			devicedatadao = getHelper().getDao(DeviceItemModel.class);
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {

		if (v == Okbutton) {
			if ("".equalsIgnoreCase(renameinput.getText().toString())) {
				Toast.makeText(getApplicationContext(), "请输入名字", Toast.LENGTH_SHORT);
				return;
			} else {
				// 判断当前数据是否有做修改
				if (lastname.equalsIgnoreCase(renameinput.getText().toString())) {
					;
				} else {
					// 存储数据
					deviceitemmodel.setDeviceName(renameinput.getText().toString());
					Toast.makeText(getApplicationContext(), "设备名正在修改中...", Toast.LENGTH_SHORT);
					try {
						devicedatadao.update(deviceitemmodel);
					} catch (SQLException e) {

						e.printStackTrace();
					}
				}
				finish();
			}
		}
	}

}
