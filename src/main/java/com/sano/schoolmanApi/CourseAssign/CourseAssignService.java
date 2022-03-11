package com.sano.schoolmanApi.CourseAssign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sano.schoolmanApi.util.Response;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Md. Sanowar Ali
 *
 */

@Service
public class CourseAssignService {

	@Autowired
	private CourseAssignRepository courseAssignRepository;


	public Response gridList(HttpServletRequest request) {
		return courseAssignRepository.gridList(request);
	}
	
	public Response getAllList(String reqObj) {
		return courseAssignRepository.getAllList(reqObj);
	}
	
	public Response save(String reqObj) {
		return courseAssignRepository.save(reqObj);
	}
	
	public Response update(String reqObj) {
		return courseAssignRepository.update(reqObj);
	}
	
	public Response findById(Long id) {
		return courseAssignRepository.findById(id);

	}
	
	public Response delete(Long id) {
		return courseAssignRepository.delete(id);
	}
	
}
