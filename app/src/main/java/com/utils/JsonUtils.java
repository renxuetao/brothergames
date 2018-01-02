package com.utils;

import com.common.JsonConstant;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by g on 2016/2/18.
 */
public class JsonUtils {
    public static JsonUtils jsonUtils = null;
    public static JsonUtils getInstance(){
        if(jsonUtils == null){
            jsonUtils = new JsonUtils();
        }
        return jsonUtils;
    }

    /**
     * 绑定手机号需要的JSON数据
     * {"NickName":'',"Gender":'',"BirthYear":'',"BirthMonth":'',"BirthDay":'',
     * "LoginType":'',"OpenID":'',"SessionToken":''
     * }
     * @param nikname 昵称
     * @param sex 性别
     * @param head_url 头像下载地址
     * @param yer 出生年
     * @param month 出生月
     * @param day 出生日
     * @param login_type 登陆类型 0:weixin 1:qq 2:weibo
     * @param openid OpenID
     * @param token SessionToken
     * @return
     */
    public String getBindingPhoneJson(String nikname,String sex,String head_url,String yer,String month,String day,String login_type,String openid,String token, String unionId){
        String bp_json = "";
        try {
            JSONObject json = new JSONObject();
            json.put("NickName", nikname);
            json.put("Gender", sex);
            json.put("HeadUrl", head_url);
            json.put("BirthYear", yer);
            json.put("BirthMonth", month);
            json.put("BirthDay", day);
            json.put("LoginType", login_type);
            json.put("OpenID", openid);
            json.put("OpenToken", token);
            json.put("UnionId", unionId);
            bp_json = json.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            bp_json = "";
        }
        return bp_json;
    }

    /**
     * 判断请求fid解析格式是否正确
     * @param json
     * @return
     */
    public boolean isRequestFIDFormat(String json){
//		boolean b_format = false;
//		try {
//			JSONObject jsonObject = new JSONObject(json);
//			if(!jsonObject.isNull(JsonConstant.UF_FID)
//					&&!jsonObject.isNull(JsonConstant.UF_URL)
//					&&!jsonObject.isNull(JsonConstant.UF_PUBLICURL)
//					&&!jsonObject.isNull(JsonConstant.UF_COUNT)){
//				b_format = true;
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//			b_format = false;
//		}
//		return b_format;
        return true;
    }

    /**
     * 根据json获得errcode -1时为解析json失败
     * @param json
     * @return
     */
    public int getUploadFileErrcode(String json){
        int errcode = -1;
        try {
            JSONObject jsonObject = new JSONObject(json);
            if(!jsonObject.isNull(JsonConstant.UF_ERRCODE)){
                errcode = jsonObject.getInt(JsonConstant.UF_ERRCODE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            errcode = -1;
        }
        return errcode;
    }
}
