package com.aixuexiao.util;


import java.io.InputStream;
import java.io.Writer;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.aixuexiao.message.resp.NewsMessage;
import com.aixuexiao.model.Article;
import com.aixuexiao.model.ExamMark;
import com.aixuexiao.model.Message;
import com.aixuexiao.model.Reply;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.thoughtworks.xstream.mapper.FieldAliasingMapper;

/**
 * 微信相关工具类
 */
public class WeixinUtil {
	
	private static WeixinUtil mWeixinUtil;
	
	public WeixinUtil(){
		
	}
	
	public static WeixinUtil instance(){
		if(mWeixinUtil == null){
			mWeixinUtil = new WeixinUtil();
		}
		
		return mWeixinUtil;
	}
	
	public static String singleExamMarkToString(ExamMark em){
		if(null==em || em.getExam()==null)
			return null;
		StringBuilder sb = new StringBuilder();
		return sb.toString();
	}
	
	private static XStream xstream = new XStream(new XppDriver() {  
        public HierarchicalStreamWriter createWriter(Writer out) {  
            return new PrettyPrintWriter(out) {  
                // 对所有xml节点的转换都增加CDATA标记  
                boolean cdata = true;  
                @SuppressWarnings("unchecked")  
                public void startNode(String name, Class clazz) {  
                    super.startNode(name, clazz);  
                }  
                protected void writeText(QuickWriter writer, String text) {  
                    if (cdata) {  
                        writer.write("<![CDATA[");  
                        writer.write(text);  
                        writer.write("]]>");  
                    } else {  
                        writer.write(text);  
                    }  
                }  
            };  
        }  
    });  
	
	/**
	 * 将回复消息对象转换成xml字符串
	 * @param reply 回复消息对象
	 * @return 返回符合微信接口的xml字符串
	 */
	public static String replyToXml(Reply reply){
		Logger mLogger = Logger.getLogger(WeixinUtil.class);
		mLogger.error("******replyToXml*********");
		
		String type = reply.getMsgType();
		
		mLogger.error("******type********* == "+type);
		
		if(Reply.TEXT.equals(type)){
			xstream.omitField(Reply.class, "articles");
			xstream.omitField(Reply.class, "articleCount");
			xstream.omitField(Reply.class, "musicUrl");
			xstream.omitField(Reply.class, "hQMusicUrl");
		}else if(Reply.MUSIC.equals(type)){
			xstream.omitField(Reply.class, "articles");
			xstream.omitField(Reply.class, "articleCount");
			xstream.omitField(Reply.class, "content"); 
		}else if(Reply.NEWS.equals(type)){
			xstream.omitField(Reply.class, "content");
			xstream.omitField(Reply.class, "musicUrl");
			xstream.omitField(Reply.class, "hQMusicUrl");
			
		}else if(Reply.IMAGE.equals(type)){
			mLogger.error("-----------------");
			xstream.omitField(Reply.class, "musicUrl");
			xstream.omitField(Reply.class, "hQMusicUrl");
			
		}
		xstream.autodetectAnnotations(true);  
		xstream.alias("xml", reply.getClass());
//		xstream.alias("item", new Article().getClass());
		
		String content = xstream.toXML(reply);
		
		mLogger.error("cont==== "+content);
		
		return content;
	}
	
	
	   /** 
     * 图文消息对象转换成xml 
     *  
     * @param newsMessage 图文消息对象 
     * @return xml 
     */  
    public static String replyNewsMessageToXml(NewsMessage newsMessage) {  
    	Logger mLogger = Logger.getLogger(WeixinUtil.class);
		mLogger.error("******replyToXml*********");
    	xstream.autodetectAnnotations(true);
    	xstream.alias("xml", newsMessage.getClass());  
        xstream.alias("item", new Article().getClass());  
        
        String content = xstream.toXML(newsMessage);
		
		mLogger.error("cont==== "+content);
		
		return content;  
    } 
	
	/**
	 * 存储数据的Map转换为对应的Message对象
	 * @param map 存储数据的map
	 * @return 返回对应Message对象
	 */
	public static  Message  mapToMessage(Map<String,String> map){
		if(map == null) return null;
		String msgType = map.get("MsgType");
		Message message = new Message();
		message.setToUserName(map.get("ToUserName"));
		message.setFromUserName(map.get("FromUserName"));
		message.setCreateTime(new Date(Long.parseLong(map.get("CreateTime"))));
		message.setMsgType(msgType);
		message.setMsgId(map.get("MsgId"));
		if(msgType.equals(Message.TEXT)){
			message.setContent(map.get("Content"));
		}else if(msgType.equals(Message.IMAGE)){
			message.setPicUrl(map.get("PicUrl"));
		}else if(msgType.equals(Message.LINK)){
			message.setTitle(map.get("Title"));
			message.setDescription(map.get("Description"));
			message.setUrl(map.get("Url"));
		}else if(msgType.equals(Message.LOCATION)){
			message.setLocationX(map.get("Location_X"));
			message.setLocationY(map.get("Location_Y"));
			message.setScale(map.get("Scale"));
			message.setLabel(map.get("Label"));
		}else if(msgType.equals(Message.EVENT)){
			message.setEvent(map.get("Event"));
			message.setEventKey(map.get("EventKey"));
		}
		return message;
	}
	
	/**
	 * 解析request中的xml 并将数据存储到一个Map中返回
	 * 至于消息返回的xml类型可以参考 ： http://mp.weixin.qq.com/wiki/index.php?title=%E6%8E%A5%E6%94%B6%E6%99%AE%E9%80%9A%E6%B6%88%E6%81%AF
	 * @param request
	 */
	public static Map<String, String> parseXml(HttpServletRequest request){
		Map<String, String> map = new HashMap<String, String>();
		try {
			InputStream inputStream = request.getInputStream();
			SAXReader reader = new SAXReader();
			Document document = reader.read(inputStream);
			Element root = document.getRootElement();
			List<Element> elementList = root.elements();
			for (Element e : elementList)
				//遍历xml将数据写入map
				map.put(e.getName(), e.getText());
			inputStream.close();
			inputStream = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	
	/**
	 * sha1加密算法
	 * @param key需要加密的字符串
	 * @return 加密后的结果
	 */
	public static String sha1(String key) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA1");
			md.update(key.getBytes());
			String pwd = new BigInteger(1, md.digest()).toString(16);
			return pwd;
		} catch (Exception e) {
			e.printStackTrace();
			return key;
		}
	}
	
	
	/** 
	 * 判断是否是QQ表情 
	 *  简单的模仿设计 用户发什么qq表情就回复什么表情
	 * @param content 
	 * @return 
	 */  
	public static boolean isQqFace(String content) {  
	    boolean result = false;  
	  
	    // 判断QQ表情的正则表达式  
	    String qqfaceRegex = "/::\\)|/::~|/::B|/::\\||/:8-\\)|/::<|/::$|/::X|/::Z|"
	    		+ "/::'\\(|/::-\\||/::@|/::P|/::D|/::O|/::\\(|/::\\+|/:--b|/::Q|/::T|"
	    		+ "/:,@P|/:,@-D|/::d|/:,@o|/::g|/:\\|-\\)|/::!|/::L|/::>|/::,@|/:,@f|"
	    		+ "/::-S|/:\\?|/:,@x|/:,@@|/::8|/:,@!|/:!!!|/:xx|/:bye|/:wipe|"
	    		+ "/:dig|/:handclap|/:&-\\(|/:B-\\)|/:<@|/:@>|/::-O|/:>-\\||/:P-\\(|/::'\\||/:X-\\)|"
	    		+ "/::\\*|/:@x|/:8\\*|/:pd|/:<W>|/:beer|/:basketb|/:oo|/:coffee|/:eat|/:pig|/:rose|"
	    		+ "/:fade|/:showlove|/:heart|/:break|/:cake|/:li|/:bome|/:kn|/:footb|/:ladybug|"
	    		+ "/:shit|/:moon|/:sun|/:gift|/:hug|/:strong|/:weak|/:share|/:v|/:@\\)|"
	    		+ "/:jj|/:@@|/:bad|/:lvu|/:no|/:ok|/:love|/:<L>|/:jump|/:shake|/:<O>|"
	    		+ "/:circle|/:kotow|/:turn|/:skip|/:oY|/:#-0|/:hiphot|/:kiss|/:<&|/:&>";  
	    Pattern p = Pattern.compile(qqfaceRegex);  
	    Matcher m = p.matcher(content);  
	    if (m.matches()) {  
	        result = true;  
	    }  
	    return result;  
	} 
	
	/**
	 * 转换微信中creatime的时间
	 * @param createTime
	 * @return
	 */
	public static String formatTime(String createTime){
		//微信中的createTime是从1970年到当前间隔的秒数 不是毫秒数，故我们这里乘以1000的话就可以转换成毫秒 就方便计算
		long msgCreateTime = Long.parseLong(createTime) *1000L;
		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		return format.format(new Date(msgCreateTime));
		
	}
	
	
	   /** 
     * emoji表情转换(hex -> utf-16) 
     *  这是一种qq的另一种表情
     *  注意啊 这种方法 不是很好了 请参考
     *  http://blog.csdn.net/lyq8479/article/details/9393097
     * @param hexEmoji 
     * @return 
     */  
    public static String emoji(int hexEmoji) {  
        return String.valueOf(Character.toChars(hexEmoji));  
    }  
	
}
