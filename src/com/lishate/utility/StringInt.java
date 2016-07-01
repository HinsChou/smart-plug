package com.lishate.utility;

import android.widget.Toast;

import com.lishate.R;
import com.lishate.activity.renwu.MoreListActivity;

public class StringInt {

	private int strLen = 0;
	private String content = "";
	
	public int getStrLen() {
		return strLen;
	}

	public void setStrLen(int strLen) {
		this.strLen = strLen;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContentString(){
		int length = content.length();
		String result = String.valueOf(length);
		while(result.length() < 4){
			result = "0" + result;
		}
		result = result + content;
		//Toast.makeText(MoreListActivity.this, MoreListActivity.this.getString(R.String.renwu_more_recv_fail), Toast.LENGTH_SHORT).show();
		//Toast.makeText(MoreListActivity.this, MoreListActivity.this.getString(R.String.renwu_more_recv_fail), Toast.LENGTH_SHORT).show();Toast.makeText(MoreListActivity.this, MoreListActivity.this.getString(R.String.renwu_more_recv_fail), Toast.LENGTH_SHORT).show();
		return result;
	}
	
	public int parseContent(String s){
		String slen = s.substring(0, 4);
		strLen = Integer.parseInt(slen);
		content = s.substring(4,4 + strLen);
		return strLen + 4;
		
	}
}
