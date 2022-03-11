package com.sano.schoolmanApi.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sano.schoolmanApi.util.Response;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Md. Sanowar Ali
 *
 */

@Service
public class StudentService {

	@Autowired
	private StudentRepository studentRepository;


	public Response gridList(HttpServletRequest request) {
		return studentRepository.gridList(request);
	}
	
	public Response getAllList(String reqObj) {
		return studentRepository.getAllList(reqObj);
	}
	
	public Response save(String reqObj) {
		return studentRepository.save(reqObj);
	}
	
	public Response update(String reqObj) {
		return studentRepository.update(reqObj);
	}
	
	public Response findById(Long id) {
		return studentRepository.findById(id);

	}
	
	public Response delete(Long id) {
		return studentRepository.delete(id);
	}
	
}
