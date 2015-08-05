package com.pearlmutter.grades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Semester implements Serializable {

	private int year;
	private int semester;

	private double credits;
	private double grade;


	private List<Course> courses = new ArrayList<>();

	public Semester() {

	}

	public Semester(int year, int semester) {
		this.year = year;
		this.semester = semester;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getSemester() {
		return semester;
	}

	public void setSemester(int semester) {
		this.semester = semester;
	}

	public double getCredits() {
		return credits;
	}

	public void setCredits(double credits) {
		this.credits = credits;
	}

	public double getGrade() {
		return grade;
	}

	public void setGrade(double grade) {
		this.grade = grade;
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void addCourse(Course course) {
		this.courses.add(course);
	}

}
