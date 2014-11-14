package com.aixuexiao.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.aixuexiao.model.Classes;
import com.aixuexiao.model.ClassesNews;
/**
 * 注解可参考 http://www.cnblogs.com/xdp-gacl/p/3495887.html
 * @author zero
 *不建议使用@Component   可以使用@Repository
 */
@Component("classesNewsDao")
public class ClassesNewsDao extends BaseDao {


	public List<ClassesNews> findClassesNewsByClassId(int classid,int limit) {
		Map<String,Integer> map = new HashMap<String,Integer>();
		map.put("classid", classid);
		map.put("limit", limit);
		return this.readSqlSession.selectList("com.aixuexiao.dao.ClassesNewsDao.selectClassesNewsByClassId",map);
	}
	
	
	public void addClassesNews(ClassesNews classesNews){
		writerSqlSession.insert("com.aixuexiao.dao.ClassesNewsDao.addClassesNews", classesNews);
	}
	
	public void deleteClassesNewsById(int id){
		writerSqlSession.delete("com.aixuexiao.dao.ClassesNewsDao.deleteClassesNewsById", id);
	}
	
}
