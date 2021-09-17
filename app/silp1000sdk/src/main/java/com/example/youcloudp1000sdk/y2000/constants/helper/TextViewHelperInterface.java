package com.example.youcloudp1000sdk.y2000.constants.helper;




/**
 * 
 * 
 * @author hz
 * @date 20160504
 * @version 1.0.0
 * @function
 * @lastmodify
 */
public interface TextViewHelperInterface {
	public void add(String tx);
	public void addPins(int len, int key);
	public void back();
	public boolean isFinished();
	public void clean();
	public boolean isPwdCorrect(String correct);
}
