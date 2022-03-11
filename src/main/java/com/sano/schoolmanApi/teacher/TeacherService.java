package com.sano.schoolmanApi.teacher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sano.schoolmanApi.util.Response;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Md. Sanowar Ali
 *
 */

@Service
public class TeacherService {

	@Autowired
	private TeacherRepository teacherRepository;


	public Response gridList(HttpServletRequest request) {
		return teacherRepository.gridList(request);
	}
	
	public Response getAllList(String reqObj) {
		return teacherRepository.getAllList(reqObj);
	}
	
	public Response save(String reqObj) {
		return teacherRepository.save(reqObj);
	}
	
	public Response update(String reqObj) {
		return teacherRepository.update(reqObj);
	}
	
	public Response findById(Long id) {
		return teacherRepository.findById(id);

	}
	
	public Response delete(Long id) {
		return teacherRepository.delete(id);
	}
	
}
