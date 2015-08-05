package com.pearlmutter.grades;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;


public class EditCourse extends ActionBarActivity {

	private Course course;
	private Semester semesterInfo;

	private EditText name;
	private EditText number;
	private EditText credits;
	private EditText grade;
	private EditText year;
	private EditText semester;

	private DBHelper db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_course);

		course = (Course) getIntent().getSerializableExtra("course");
		semesterInfo = (Semester) getIntent().getSerializableExtra("semester");

		this.name = (EditText) findViewById(R.id.courseNameEditText);
		this.number = (EditText) findViewById(R.id.courseNumberEditText);
		this.credits = (EditText) findViewById(R.id.courseCreditsEditText);
		this.grade = (EditText) findViewById(R.id.courseGradeEditText);
		this.year = (EditText) findViewById(R.id.courseYearEditText);
		this.semester = (EditText) findViewById(R.id.courseSemesterEditText);

		db = new DBHelper(this);

		if (course != null) {
			name.setText(course.getTitle());
			number.setText(course.getNumber() + "");
			credits.setText(course.getCredits() + "");
			grade.setText(course.getGrade() + "");
			year.setText(course.getYear() + "");
			semester.setText(course.getSemester() + "");
		}

		if (semesterInfo != null) {
			year.setText(semesterInfo.getYear() + "");
			semester.setText(semesterInfo.getSemester() + "");
		}
	}

	public void saveCourse() {
		if (course == null) {
			course = new Course();
			course.set_id(-1);
		}

		course.setTitle(name.getText().toString());
		course.setNumber(Integer.valueOf(number.getText().toString()));
		course.setCredits(Double.valueOf(credits.getText().toString()));
		course.setGrade(Double.valueOf(grade.getText().toString()));
		course.setYear(Integer.valueOf(year.getText().toString()));
		course.setSemester(Integer.valueOf(semester.getText().toString()));

		db.saveCourse(course);

		finish();
	}

	public void deleteClick(View view) {
		if (course != null)
			db.deleteCourse(course);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_edit_course, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		switch (id) {
			case R.id.action_save:
				saveCourse();
				return true;
			case R.id.action_cancel:
				finish();
				return true;
			case R.id.action_settings:
				return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
