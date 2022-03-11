package com.sano.schoolmanApi.CourseAssign;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Md. Sanowar Ali
 *
 */

@Getter
@Setter
@Entity
@Table(name = "course_assign")
public class CourseAssignEntity implements Serializable{

	private static final long serialVersionUID = -4538971070304943261L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	@Column(name = "id")
	private Long id;
	
	@Column(name = "course_id")
	private Long courseId;
	
	@Column(name = "course_name")
	private String courseName;
	 
	@Column(name = "assign_id")
	private Long assignId;
	
	@Column(name = "assign_name")
	private String assignName;
	
	@Column(name = "teacher_flag")
	private Long teacherFlag;
	
	@Column(name = "student_Flag")
	private Long studentFlag;
	  
}
