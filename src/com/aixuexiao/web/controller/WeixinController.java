package com.aixuexiao.web.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.aixuexiao.dao.ReplyDao;
import com.aixuexiao.message.resp.NewsMessage;
import com.aixuexiao.model.Article;
import com.aixuexiao.model.ExamMark;
import com.aixuexiao.model.Message;
import com.aixuexiao.model.Reply;
import com.aixuexiao.service.WeixinService;
import com.aixuexiao.util.WeixinUtil;

@Controller()
public class WeixinController {

	private static final String TOKEN = "huyue520";

	public static int pagesize = 10;
	private static Logger mLogger = Logger.getLogger(WeixinController.class);

	@Resource(name = "weixinService")
	private WeixinService weixinService;

	@Resource(name = "replyDao")
	private ReplyDao replyDao;

	/**
	 * @responsebody表示该方法的返回结果直接写入HTTP response body中
	 *                                 一般在异步获取数据时使用，在使用@RequestMapping后，
	 *                                 返回值通常解析为跳转路径
	 *                                 ，加上@responsebody后返回结果不会被解析为跳转路径，
	 *                                 而是直接写入HTTP response
	 *                                 body中。比如异步获取json数据，加上@responsebody后，
	 *                                 会直接返回json数据。
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/test", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String test(HttpServletRequest request) {
		return weixinService.getStudentMessageHistoryByStudentId(30202);
	}

	// 接收微信公众号接收的消息，处理后再做相应的回复
	@RequestMapping(value = "/weixin", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String replyMessage(HttpServletRequest request) throws Exception {

		// 将请求、响应的编码均设置为UTF-8（防止中文乱码）
		request.setCharacterEncoding("UTF-8");
		// request.setCharacterEncoding("UTF-8");

		// 仅处理微信服务端发的请求
		// WeixinUtil mWeixinUtil = new WeixinUtil();
		if (checkWeixinReques(request)) {
			Map<String, String> requestMap = WeixinUtil.parseXml(request);
			Message message = WeixinUtil.mapToMessage(requestMap);
			weixinService.addMessage(message);// 保存接受消息到数据库
			String replyContent = Reply.WELCOME_CONTENT;
			String type = message.getMsgType();
			//
			List<Article> list = new ArrayList<Article>();
			/**
			 * 仅处理文本回复内容
			 */
			if (type.equals(Message.TEXT)) {
				String content = message.getContent();// 消息内容
				String[] cs = content.split("_");// 消息内容都以下划线_分隔
				if (cs.length == 2) {
					int studentid;// 学生编号
					String process = cs[1];// 操作
					try {
						studentid = Integer.parseInt(cs[0]);
						if ("考试".equals(process)) {
							replyContent = weixinService
									.getSingleExamMarkStringByStudentId(studentid);
						} else if ("考试历史".equals(process)) {
							replyContent = weixinService
									.getExamMarkHistoryStringByStudentId(studentid);
						} else if ("留言".equals(process)) {
							replyContent = weixinService
									.getSingleStudentMessageByStudentId(studentid);
						} else if ("留言历史".equals(process)) {
							replyContent = weixinService
									.getStudentMessageHistoryByStudentId(studentid);
						} else if ("动态".equals(process)) {
							replyContent = weixinService
									.getSingleClassesNewsByStudentId(studentid);
						} else if ("动态历史".equals(process)) {
							replyContent = weixinService
									.getClassesNewsHistoryByStudentId(studentid);
						}
					} catch (NumberFormatException e) {
						replyContent = Reply.ERROR_CONTENT;
					}
				}
				// 回复表情
				else if (WeixinUtil.isQqFace(content)) {
					Logger mLogger = Logger.getLogger(getClass());
					mLogger.info("lalallalala");
					mLogger.debug("****************");
					mLogger.error("oooooooooooooooo");
					replyContent = content;

				} else if (content.trim().equals("?")) {

					replyContent = Reply.WELCOME_CONTENT;

				} else if (content.equals("0")) {
					String picUrl = "http://img.zongyijia.com/images/20140630152830_4047.jpg";
					String url = "http://m.baidu.com";

					Article article1 = new Article();
					article1.setTitle("德玛西亚\n");
					article1.setDescription("啦啦啦啦德玛西亚");
					article1.setPicUrl("");
					article1.setUrl(url);
					
					Article article2 = new Article();
					article2.setTitle("德玛西亚111\n");
					article2.setDescription("啦啦啦啦德玛西亚1111");
					article2.setPicUrl("");
					article2.setUrl(url);

					list.add(article1);
					list.add(article2);

					Reply reply = new Reply();
					reply.setToUserName(message.getFromUserName());
					reply.setFromUserName(message.getToUserName());
					reply.setCreateTime(new Date());
					reply.setMsgType(Reply.NEWS);
					reply.setArticleCount(list.size());
					mLogger.error("list.size()== " + list.size());
					reply.setArticles(list);
//					weixinService.addReply(reply);// 保存回复消息到数据库
					
					NewsMessage mNewsMessage = new NewsMessage();
					mNewsMessage.setToUserName(message.getFromUserName());
					mNewsMessage.setFromUserName(message.getToUserName());
					mNewsMessage.setCreateTime(System.currentTimeMillis());
					mNewsMessage.setMsgType(Reply.NEWS);
					mNewsMessage.setArticleCount(list.size());
					mNewsMessage.setArticles(list);
					 // 将回复消息序列化为xml形式
					 String back = WeixinUtil.replyNewsMessageToXml(mNewsMessage);
					 System.out.println(back);
					 return back;

				} else if (content.equals("1")) {

					replyContent = Reply.WELCOME_CONTENT1;

				} else if (content.equals("2")) {

					replyContent = Reply.WELCOME_CONTENT2;

				}

			} else if (type.equals(Message.EVENT)) {

				replyContent = Reply.WELCOME_ATTENTION;

			}
			// 拼装回复消息
			Reply reply = new Reply();
			reply.setToUserName(message.getFromUserName());
			reply.setFromUserName(message.getToUserName());
			reply.setCreateTime(new Date());
			reply.setMsgType(Reply.TEXT);
			reply.setContent(replyContent);

			weixinService.addReply(reply);// 保存回复消息到数据库
			// 将回复消息序列化为xml形式
			String back = WeixinUtil.replyToXml(reply);
			
			return back;
		} else {
			return "error";
		}
	}

	// 微信公众平台验证url是否有效使用的接口
	@RequestMapping(value = "/weixin", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String initWeixinURL(HttpServletRequest request) {
		String echostr = request.getParameter("echostr");
		if (checkWeixinReques(request) && echostr != null) {
			return echostr;
		} else {
			return "error";
		}
	}

	/**
	 * 根据token计算signature验证是否为weixin服务端发送的消息
	 */
	private static boolean checkWeixinReques(HttpServletRequest request) {

		/** 微信加密签名 */
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");// 随机数
		if (signature != null && timestamp != null && nonce != null) {
			String[] strSet = new String[] { TOKEN, timestamp, nonce };
			java.util.Arrays.sort(strSet);
			String key = "";
			for (String string : strSet) {
				key = key + string;
			}
			// String pwd = WeixinUtil.sha1(key);
			// WeixinUtil mWeixinUtil = new WeixinUtil();
			String pwd = WeixinUtil.sha1(key);
			// String pwd = mSha1(key);
			mLogger.info("pwd      == " + pwd);
			mLogger.info("signature== " + signature);

			return pwd.equals(signature);
		} else {
			return false;
		}
	}

	/**
	 * 自己来写个 sha1加密算法
	 * 
	 * @param key需要加密的字符串
	 * @return 加密后的结果
	 */
	public static String mSha1(String key) {
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
	 * 收到消息列表页面
	 */
	@RequestMapping(value = "/manager/messages", method = RequestMethod.GET)
	public ModelAndView listMessage(String pagenum) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("sidebar", "messages");
		mv.setViewName("messages");
		int num = 1;
		if (null != pagenum) {
			num = Integer.parseInt(pagenum);
		}
		List<Message> list = weixinService.listMessage((num - 1) * pagesize,
				pagesize);
		mv.addObject("messageList", list);
		mv.addObject("pagenum", num);
		mv.addObject("length", list.size());
		return mv;
	}

	/**
	 * 回复消息列表页面
	 */
	@RequestMapping(value = "/manager/replys", method = RequestMethod.GET)
	public ModelAndView listReply(String pagenum) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("sidebar", "replys");
		mv.setViewName("replys");
		int num = 1;
		if (null != pagenum) {
			num = Integer.parseInt(pagenum);
		}
		List<Reply> list = weixinService.listReply((num - 1) * pagesize,
				pagesize);
		mv.addObject("replyList", list);
		mv.addObject("pagenum", num);
		mv.addObject("length", list.size());
		return mv;
	}

}
