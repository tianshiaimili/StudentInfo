package com.aixuexiao.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.aixuexiao.model.Message;
/**
 * 注解可参考 http://www.cnblogs.com/xdp-gacl/p/3495887.html
 * @author zero
 *不建议使用@Component   可以使用@Repository
 */
@Component("messageDao")
public class MessageDao extends BaseDao {

	public int addMessage(Message message) {
		return this.writerSqlSession.insert("com.aixuexiao.dao.MessageDao.addMessage", message);
	}
	
	public List<Message> findMessage(int start,int size) {
		Map<String,Integer> map = new HashMap<String,Integer>();
		map.put("start", start);
		map.put("size", size);
		return this.readSqlSession.selectList("com.aixuexiao.dao.MessageDao.selectMessage",map);
	}
	
}
