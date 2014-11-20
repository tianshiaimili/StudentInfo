package com.aixuexiao.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

/**
 * 图文消息

 *关于Xstream的使用 可以参考 http://blog.csdn.net/gaozhlzh/article/details/6826140
 *注解
@XStreamAlias("message") 别名注解
作用目标: 类,字段

@XStreamImplicit 隐式集合
@XStreamImplicit(itemFieldName="part")
作用目标: 集合字段

@XStreamConverter(SingleValueCalendarConverter.class) 注入转换器
作用目标: 对象

@XStreamAsAttribute 转换成属性
作用目标: 字段
 *
 */
public class Article{
	
	@XStreamOmitField
	private int id;//数据库存储的id
	
    // 图文消息名称  
	@XStreamAlias("Title")
    private String title;  
    // 图文消息描述  
	@XStreamAlias("Description")
    private String description;  
    // 图片链接，支持JPG、PNG格式，较好的效果为大图640*320，小图80*80，限制图片链接的域名需要与开发者填写的基本资料中的Url一致  
	@XStreamAlias("PicUrl")
    private String picUrl;  
    // 点击图文消息跳转链接  
	@XStreamAlias("Url")
    private String url;
	
	@XStreamOmitField
	private int replyId;
	
//	@XStreamOmitField
//	private Reply reply;
	
	public Article() {}
	
	public Article(String title, String description, String picUrl, String url) {
		this.title = title;
		this.description = description;
		this.picUrl = picUrl;
		this.url = url;
	}
	
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public int getReplyId() {
		return replyId;
	}

	public void setReplyId(int replyId) {
		this.replyId = replyId;
	}

//	public Reply getReply() {
//		return reply;
//	}
//
//	public void setReply(Reply reply) {
//		this.reply = reply;
//	}
	
    
    
}
