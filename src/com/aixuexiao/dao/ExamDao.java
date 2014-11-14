package com.aixuexiao.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.aixuexiao.model.Classes;
import com.aixuexiao.model.Exam;
import com.aixuexiao.model.ExamMark;
/**
 * 注解可参考 http://www.cnblogs.com/xdp-gacl/p/3495887.html
 * @author zero
 *不建议使用@Component   可以使用@Repository
 */
@Component("examDao")
public class ExamDao extends BaseDao {


	public List<ExamMark> findExamMarkByStudentId(int studentid,int limit) {
		Map<String,Integer> map = new HashMap<String,Integer>();
		map.put("studentid", studentid);
		map.put("limit", limit);
		return this.readSqlSession.selectList("com.aixuexiao.dao.ExamDao.selectExamMarkByStudentId",map);
	}
	
	public List<Exam> findExam(int start,int size,Exam exam){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("start", start);
		map.put("size", size);
		map.put("exam", exam);
		return this.readSqlSession.selectList("com.aixuexiao.dao.ExamDao.selectExam",map);
	}
	
	public void addExam(Exam exam){
		writerSqlSession.insert("com.aixuexiao.dao.ExamDao.addExam", exam);
	}
	
	public void addExamMark(ExamMark examMark){
		writerSqlSession.insert("com.aixuexiao.dao.ExamDao.addExamMark", examMark);
	}
	
	public Exam findExamById(int id){
		return this.readSqlSession.selectOne("com.aixuexiao.dao.ExamDao.selectExamById",id);
	}
	
}
