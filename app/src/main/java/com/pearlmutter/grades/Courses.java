package com.pearlmutter.grades;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class Courses extends ActionBarActivity {

	DBHelper db;
	private List<Course> courses = new ArrayList<>();
	Semester semester;

	private ListView coursesListView;
	private CourseAdapter adapter;

	TextView semesterAverageTextView;
	TextView totalAverageTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_courses);

		db = new DBHelper(this);
		semester = (Semester) getIntent().getSerializableExtra("semester");
		courses = semester.getCourses();

		coursesListView = (ListView) findViewById(R.id.coursesListView);
		adapter = new CourseAdapter(this, R.layout.semester_row, courses);
		coursesListView.setAdapter(adapter);

		coursesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(Courses.this, EditCourse.class);
				intent.putExtra("course", courses.get(position));
				startActivity(intent);
			}
		});

		semesterAverageTextView = (TextView) findViewById(R.id.semesterAverageTextView);
		totalAverageTextView = (TextView) findViewById(R.id.totalAverageTextView);

		calculateAverages();
	}

	@Override
	protected void onResume() {
		super.onResume();

		courses = db.getCousesBySemester(semester);
		adapter.clear();
		adapter.addAll(courses);
		adapter.notifyDataSetChanged();
		Log.d("ssdf", "resumed");
	}

	private void calculateAverages() {
		// TODO ??
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_courses, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		switch (id) {
			case R.id.action_add:
				Intent intent = new Intent(this, EditCourse.class);
				intent.putExtra("semester", semester);
				startActivity(intent);
				return true;
			case R.id.action_settings:
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private class CourseAdapter extends ArrayAdapter<Course> {

		private List<Course> courses;
		private int resource;
		private Context context;

		public CourseAdapter(Context context, int resource, List<Course> objects) {
			super(context, resource, objects);

			this.context = context;
			this.resource = resource;
			this.courses = objects;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View itemView = convertView;

			if (itemView == null) {
				itemView = LayoutInflater.from(context).inflate(resource, parent, false);
			}

			Course course = courses.get(position);

			TextView semesterTextView = (TextView) itemView.findViewById(R.id.semesterTextView);
			TextView gradeTextView = (TextView) itemView.findViewById(R.id.gradeTextView);

			semesterTextView.setText(course.getTitle());
			gradeTextView.setText(course.getGrade() + "");

			Log.d("sf", "getView(): " + course.getTitle());

			return itemView;
		}
	}
}
