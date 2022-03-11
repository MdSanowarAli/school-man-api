package com.sano.schoolmanApi.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sano.schoolmanApi.util.Response;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Md. Sanowar Ali
 *
 */

@Service
public class CourseService {

	@Autowired
	private CourseRepository courseRepository;


	public Response gridList(HttpServletRequest request) {
		return courseRepository.gridList(request);
	}
	
	public Response getAllList(String reqObj) {
		return courseRepository.getAllList(reqObj);
	}
	
	public Response save(String reqObj) {
		return courseRepository.save(reqObj);
	}
	
	public Response update(String reqObj) {
		return courseRepository.update(reqObj);
	}
	
	public Response findById(Long id) {
		return courseRepository.findById(id);

	}
	
	public Response delete(Long id) {
		return courseRepository.delete(id);
	}
	
}
