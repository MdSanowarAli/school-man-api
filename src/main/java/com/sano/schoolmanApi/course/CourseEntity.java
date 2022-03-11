package com.sano.schoolmanApi.course;

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
@Table(name = "course")
public class CourseEntity implements Serializable{

	private static final long serialVersionUID = -4538971070304943261L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	@Column(name = "id")
	private Long id;
	
	@NotNull
	@Column(name = "course_name")
	private String courseName;
	
	@Column(name = "course_duration")
	private String courseDuration;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "active_status")
	private Boolean activeStatus;
	  
}
