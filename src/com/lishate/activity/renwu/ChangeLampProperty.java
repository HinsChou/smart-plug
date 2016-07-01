package com.lishate.activity.renwu;


import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.lishate.R;
import com.lishate.activity.BaseActivity;
import com.lishate.data.model.DeviceItemModel;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ChangeLampProperty extends BaseActivity implements OnClickListener{

	private DeviceItemModel deviceitemmodel;
	private Dao<DeviceItemModel, Integer> devicedatadao = null;
	private EditText renameinput;
	private Button Okbutton;
	private String lastname = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.renamedevice);
		Intent mIntent = getIntent();
		deviceitemmodel = (DeviceItemModel) mIntent.getSerializableExtra("renamedevice");
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		renameinput = (EditText)findViewById(R.id.renameinput);
		renameinput.setText(deviceitemmodel.getDeviceName());
		Okbutton = (Button)findViewById(R.id.save_name);
		Okbutton.setOnClickListener(this);
		lastname = renameinput.getText().toString();
		try {
			devicedatadao = getHelper().getDao(DeviceItemModel.class);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == Okbutton)
		{
			if("".equalsIgnoreCase(renameinput.getText().toString()))
			{
				Toast.makeText(getApplicationContext(), "����������", Toast.LENGTH_SHORT);
				return ;
			}else{
				//�жϵ�ǰ�����Ƿ������޸�
				if(lastname.equalsIgnoreCase(renameinput.getText().toString()))
				{
						;
				}
				else
				{
					//�洢����
					deviceitemmodel.setDeviceName(renameinput.getText().toString());
					Toast.makeText(getApplicationContext(), "�豸�������޸���...", Toast.LENGTH_SHORT);
					try {
						devicedatadao.update(deviceitemmodel);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				finish();
			}
		}
	}

}
