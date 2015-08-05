package com.pearlmutter.grades;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

	DBHelper db;

	List<Course> courses = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		db = new DBHelper(this);

//		courses = db.getCourses();
	}

	@Override
	protected void onResume() {
		super.onResume();

//		courses = db.getCourses();
	}

	public void addCourseClick(View view) {
		startActivity(new Intent(this, EditCourse.class));
	}

	public void semestersClick(View view) {
		Intent intent = new Intent(this, Semesters.class);
//		intent.putExtra(courses,"courses");
		startActivity(intent);

	}

	public void graphClick(View view) {
		startActivity(new Intent(this, Graph.class));
	}

	public void wipeDB(View view) {
		deleteDatabase("courses.db");
		db = new DBHelper(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
