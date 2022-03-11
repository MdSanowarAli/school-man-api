package com.sano.schoolmanApi.CourseAssign;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sano.schoolmanApi.util.Response;

/**
 * @author Md. Sanowar Ali
 *
 */

@RestController
@RequestMapping("/api/course-assign")
public class CourseAssignController{
	
	@Autowired
	private CourseAssignService courseAssignService;
	
	@GetMapping("/grid-list")
	public Response gridList(HttpServletRequest request) {
		return courseAssignService.gridList(request);
	}
	
	@GetMapping("/list")
	public Response getAllList(@RequestBody(required = false) String reqObj) {
		return courseAssignService.getAllList(reqObj);
	}
	
	@GetMapping("/find-by-id")
	public Response findById(Long id) {
		return courseAssignService.findById(id);
	}
	
	@PostMapping("/create")
    public Response save(@RequestBody  String reqObj) {
        return courseAssignService.save(reqObj);
    }
	
	@PostMapping("/update")
    public Response update(@RequestBody  String reqObj) {
        return courseAssignService.update(reqObj);
    }
	
	@DeleteMapping("/delete")
	public Response delete(@RequestParam("id") long reqObj) {
		return courseAssignService.delete(reqObj);
	}
	
}
