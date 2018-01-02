package com.common;

public class EventBusConstant {
	/**
	 * 定位成功
	 */
	public static final String LOCATION_OK 			= "location_ok";
	/**
	 * 定位失败
	 */
	public static final String LOCATION_ERROR 		= "location_error";
	public static final String LATITYDE 			= "latitude";
	public static final String LONGITUDE 			= "longitude";
	public static final String PROVINCE				= "province";
	public static final String CITY 				= "city";
	public static final String LOCATION_ADDRESS     = "LocationAddress";
	/**
	 * 刷新搜索状态
	 */
	public static final String REFRESH_SEARCH 		= "refresh_search";
	/**
	 * 设置搜索条件
	 */
	public static final String REFRESH_SEARCH_SET 		= "refresh_search_set";
	/**
	 * 消息列表刷新
	 */
	public static final String REFRESH_MSG 			= "refresh_msg";
	/**
	 * 系统消息推送
	 */
	public static final String PUSH_MSG 			= "push_msg";
	/**
	 * 系统消息推送类型
	 */
	public static final String PUSH_MSG_TYPE 		= "push_msg_type";
	/**
	 * 系统消息推送内容
	 */
	public static final String PUSH_MSG_INFO 		= "push_msg_info";
	/**
	 * 88推送具体类型
	 */
	public static final String PUSH_88_TYPE 		= "push_88_type";
	/**
	 * 通讯录有修改
	 */
	public static final String CHANGE_CONTACT 		= "change_contact";
	/**
	 * 黑名单操作
	 */
	public static final String OP_BLACK             = "op_black" ;
	/**
	 *  黑名单操作具体布尔值
	 */
	public static final String OP_BLACK_VALUER      = "op_black_valuer" ;
	/**
	 * 刷新界面的filter
	 */
	public static final String REFRESH_PHOTO_VIEW_FILTER = "refresh_photo_view_filter";
	
	/**
	 * 权限更新推送
	 */
	public static final String PUSH_PERMISSION  = "push_permission";

	/**
	 * 聊天销毁
	 */
	public static final String CHAT_DES = "chat_des";
	public static final String CHAT_DES_MD5 = "chat_des_md5";
//	public static final String CHAT_DES_MSG = "chat_des_msg";
	public static final String CHAT_DES_EXT = "chat_des_ext";
	
	
	public static final String LOGOUT = "logout";//注销登陆
	
	public static final String CREATE_MESSAGE = "create_message";//创建聊天入口
	public static final String CHANGE_MESSAGE = "change_message";//聊天如何改变 从过去移到现在
	public static final String MESSARE_FROM_ACCOUNT = "message_from_Account";//聊天入口对方账号
	
	/**缘分指数 头像审核  城市 等扩展信息 真是日！！*/
	public static final String CHAT_EXTEND = "chat_extend";
	public static final String CHAT_EXTEND_YUANFEN = "chat_extend_yuanfen";
	public static final String CHAT_EXTEND_COVER_STETE = "chat_extend_cover_state";
	public static final String EXT_ACCOUNT = "ext_accoutn";
	public static final String YUANFEN = "yuanfen";
	public static final String COVERSTATE = "coverstate";
	
	/**说说点赞和评论*/
	public static final String MOOD_PUSH 				= "MOOD_PUSH";
	public static final String MOOD_PUSH_LOVE 			= "MOOD_PUSH_LOVE";
	public static final String MOOD_PUSH_REPLY 			= "MOOD_PUSH_REPLY";
	public static final String MOOD_PUSH_CLEAR 			= "MOOD_PUSH_CLEAR";
	
	/**伪造聊天*/
	public static final String CHAT_FORGE 				= "chat_forge";
	public static final String CHAT_FORGE_FROM			= "chat_forge_from";
	public static final String CHAT_CLEAR				= "chat_clear";
	
	/**举报/申诉处理通知推送*/
	public static final String APPEAL_FORGR 			= "appeal_forge";
	
	/**话题点赞和评论*/
	public static final String ARTICLE_PUSH 			= "ARTICLE_PUSH";
	public static final String ARTICLE_PUSH_REPLY 		= "ARTICLE_PUSH_REPLY";
	public static final String ARTICLE_PUSH_CLEAR 		= "ARTICLE_PUSH_CLEAR";
	/**心跳*/
	public static final String REQUEST_HEARTBEAT_OK     = "request_heartbeat_ok";
	/**搜索提示刷新*/
	public static final String SEARCH_TIPS             = "refresh_search_tips";
	/**修改用户信息(对方)*/
	public static final String MODIFY_INFO             = "modify_info";
	public static final String MODIFY_ACCOUNT          =  "modify_account";
	public static final String MODIFY_NICKNAME          =  "modify_nickname";
	public static final String MODIFY_COVER          =  "modify_cover";
	public static final String MODIFY_GENDER          =  "modify_gender";
	/**广告页 type1 匿名说说 type2 城市列表*/
	public static final String PAGE_TYPE1_SUCCESS = "page_type1_success";
	public static final String PAGE_TYPE1_FAIL = "page_type1_fail";
	public static final String PAGE_TYPE2_SUCCESS = "page_type2_success";
	public static final String PAGE_TYPE2_FAIL = "page_type2_fail";
	public static final String PAGE_JSON = "page_json";
	/**操作账号*/
	public static final String FROM_ACCOUNT = "from_account";
	/**拉黑*/
	public static final String BLACK_OP1 = "black_op1";
	
	
	/**模拟地理位置改变*/
	public static final String CHANGE_LOCATION = "change_location";
	
}
