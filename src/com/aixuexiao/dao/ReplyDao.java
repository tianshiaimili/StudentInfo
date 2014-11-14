package com.aixuexiao.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.aixuexiao.model.Article;
import com.aixuexiao.model.Reply;
/**
 * 注解可参考 http://www.cnblogs.com/xdp-gacl/p/3495887.html
 * @author zero
 *不建议使用@Component   可以使用@Repository
 */
@Component("replyDao")
public class ReplyDao extends BaseDao {

	public void addReply(Reply reply) {
		this.writerSqlSession.insert("com.aixuexiao.dao.ReplyDao.addReply", reply);
	}
	
	public void addArticle(Article article){
		this.writerSqlSession.insert("com.aixuexiao.dao.ReplyDao.addArticle", article);
	}
	
	public List<Reply> findReply(int start,int size) {
		Map<String,Integer> map = new HashMap<String,Integer>();
		map.put("start", start);
		map.put("size", size);
		return this.readSqlSession.selectList("com.aixuexiao.dao.ReplyDao.selectReply",map);
	}
	
}
