package com.aixuexiao.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.aixuexiao.model.Student;
/**
 * 注解可参考 http://www.cnblogs.com/xdp-gacl/p/3495887.html
 * @author zero
 *不建议使用@Component   可以使用@Repository
 */
//@Component("studentDao")
@Repository("studentDao")
public class StudentDao extends BaseDao {


	public Student findStudentById(int id) {
		return this.readSqlSession.selectOne("com.aixuexiao.dao.StudentDao.selectStudentById",id);
	}
	
	/**
	 * 
	 * 将数据库中Student数据分页查出
	 * @param start 其实数据条数
	 * @param size  展示数据每页的大小
	 * @return
	 */
	public List<Student> findStudent(int start,int size,Student student) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("start", start);
		map.put("size", size);
		map.put("student", student);
		return this.readSqlSession.selectList("com.aixuexiao.dao.StudentDao.selectStudent",map);
	}
	
	public List<Student> findStudentByClassesId(int classesid) {
		return this.readSqlSession.selectList("com.aixuexiao.dao.StudentDao.selectStudentByClassesId",classesid);
	}
	
	public int addStudent(Student student) {
		return this.writerSqlSession.insert("com.aixuexiao.dao.StudentDao.addStudent", student);
	}
	
	public int deleteStudentById(int studentid) {
		return this.writerSqlSession.delete("com.aixuexiao.dao.StudentDao.deleteStudentById", studentid);
	}
	
	public int updateStudent(Student student) {
		return this.writerSqlSession.update("com.aixuexiao.dao.StudentDao.updateStudent", student);
	}
}
