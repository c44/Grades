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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;


public class Semesters extends ActionBarActivity {

	DBHelper db;
	private List<Course> courses = new ArrayList<>();

	private List<Semester> semesters = new ArrayList<>();
	private ListView semestersListView;
	private SemesterAdapter adapter;

	private HashSet<String> set = new HashSet<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_semesters);

		semestersListView = (ListView) findViewById(R.id.semestersListView);
		adapter = new SemesterAdapter(this, R.layout.semester_row, semesters);
		semestersListView.setAdapter(adapter);

		semestersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(Semesters.this, Courses.class);
				intent.putExtra("semester", semesters.get(position));
				startActivity(intent);
			}
		});

		db = new DBHelper(this);
	}

	@Override
	protected void onResume() {
		super.onResume();

		getCouresAndpopulate();
	}

	private void getCouresAndpopulate() {
		courses = db.getCourses();

		for (Course course : courses) {
			set.add(course.getYear() + ";" + course.getSemester());
		}

		semesters.clear();

		for (String s : set) {
			String[] info = s.split(";");
			semesters.add(new Semester(Integer.valueOf(info[0]), Integer.valueOf(info[1])));
		}

		Collections.sort(semesters, new Comparator<Semester>() {
			@Override
			public int compare(Semester o1, Semester o2) {
				if (o1.getYear() == o2.getYear())
					return o1.getSemester() - o2.getSemester();
				return o1.getYear() - o2.getYear();
			}
		});

		for (Semester semester : semesters) {
			for (Course course : courses) {
				if (course.getYear() == semester.getYear() && course.getSemester() == semester.getSemester())
					semester.addCourse(course);
			}
		}

		double gradesTotal = 0;
		double creditsTotal = 0;
		for (Semester semester : semesters) {
			double credits = 0;
			double grade = 0;

			for (Course course : semester.getCourses()) {
				if (course.getGrade() != 0) {
					credits += course.getCredits();
					grade += (course.getGrade() * course.getCredits());
					Log.d("ss", course.getTitle() + course.getGrade() + "," + course.getCredits());
				}
			}
			semester.setCredits(credits);
//			semester.setGrade(semester.getCourses().size());
			try {
				semester.setGrade(grade / credits);
			} catch (Exception e) {
				semester.setGrade(0);
			}

			gradesTotal += grade;
			creditsTotal += credits;
		}

		double gradeAverage;
		try {
			gradeAverage = gradesTotal / creditsTotal;
		} catch (Exception e) {
			gradeAverage = 0;
		}

//		adapter.clear();
//		adapter.addAll(semesters);
		adapter.notifyDataSetChanged();

		((TextView) findViewById(R.id.gradeAverage)).setText("Average Grade: " + gradeAverage);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_semesters, menu);
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
				startActivity(intent);
				return true;
			case R.id.action_settings:
				return true;
		}

		return super.onOptionsItemSelected(item);
	}


	private class SemesterAdapter extends ArrayAdapter<Semester> {

		private List<Semester> semesters;
		private int resource;
		private Context context;

		public SemesterAdapter(Context context, int resource, List<Semester> objects) {
			super(context, resource, objects);

			this.context = context;
			this.resource = resource;
			this.semesters = objects;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View itemView = convertView;

			if (itemView == null) {
				itemView = LayoutInflater.from(context).inflate(resource, parent, false);
			}

			Semester semester = semesters.get(position);

			TextView semesterTextView = (TextView) itemView.findViewById(R.id.semesterTextView);
			TextView gradeTextView = (TextView) itemView.findViewById(R.id.gradeTextView);

			semesterTextView.setText("Year " + semester.getYear() + ", Semester " + semester.getSemester());
			gradeTextView.setText(semester.getGrade() + "");

			return itemView;
		}
	}
}
