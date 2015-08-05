package com.pearlmutter.grades;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {


	public static final String TAG = DBHelper.class.getName();

	// courses table
	public static final String TABLE_COURSES = "courses";

	public static final String KEY_COURSE_ID = "_id";
	public static final String KEY_TITLE = "title";
	public static final String KEY_NUMBER = "number";
	public static final String KEY_CRESDITS = "credits";
	public static final String KEY_GRADE = "grade";
	public static final String KEY_YEAR = "year";
	public static final String KEY_SEMESTER = "semester";

	private static final String TABLE_COURSESS_CREATE = "create table " + TABLE_COURSES + "(" + KEY_COURSE_ID + " integer primary key autoincrement, " + KEY_TITLE + " text, "
			+ KEY_NUMBER + " real, " + KEY_CRESDITS + " integer, " + KEY_GRADE + " integer, " + KEY_YEAR + " integer, " + KEY_SEMESTER + " integer" + ")";

	public static final String[] All_COURSE_KEYS = new String[]{KEY_COURSE_ID, KEY_TITLE, KEY_NUMBER, KEY_CRESDITS, KEY_GRADE, KEY_YEAR,
			KEY_SEMESTER};

	public static final int COL_ID = 0;
	public static final int COL_TITLE = 1;
	public static final int COL_NUMBER = 2;
	public static final int COL_CRESDITS = 3;
	public static final int COL_GRADE = 4;
	public static final int COL_YEAR = 5;
	public static final int COL_SEMESTER = 6;


	private static final String DATABASE_NAME = "courses.db";
	private static final int DATABASE_VERSION = 1;

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_COURSESS_CREATE);

		Log.d(TAG, "Created DB");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
		onCreate(db);
	}

	public void saveCourse(Course course) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_TITLE, course.getTitle());
		values.put(KEY_NUMBER, course.getNumber());
		values.put(KEY_CRESDITS, course.getCredits());
		values.put(KEY_GRADE, course.getGrade());
		values.put(KEY_YEAR, course.getYear());
		values.put(KEY_SEMESTER, course.getSemester());

		Cursor c = db.query(true, TABLE_COURSES, All_COURSE_KEYS, KEY_COURSE_ID + "=" + course.get_id(), null, null, null, null, null);

		if (c.moveToFirst())
			db.update(TABLE_COURSES, values, KEY_COURSE_ID + " = " + course.get_id(), null);
		else
			db.insert(TABLE_COURSES, null, values);

		c.close();
		db.close();
	}

	public List<Course> getCourses() {
		SQLiteDatabase db = this.getWritableDatabase();

		Cursor c = db.query(true, TABLE_COURSES, All_COURSE_KEYS, null, null, null, null, null, null);

		if (c != null) {
			c.moveToFirst();
		}

		db.close();

		List<Course> courses = new ArrayList<>();

		if (c != null && c.moveToFirst()) {
			do {
				Course course = new Course();
				course = setCourseDataFromDB(c, course);
				courses.add(course);
			} while (c.moveToNext());
			c.close();
		}


		return courses;
	}

	public List<Course> getCousesBySemester(Semester semester) {
		SQLiteDatabase db = this.getWritableDatabase();

		Cursor c = db.query(true, TABLE_COURSES, All_COURSE_KEYS, KEY_YEAR + " = " + semester.getYear() + " and " + KEY_SEMESTER + " = " + semester.getSemester() , null, null, null, null, null);

		if (c != null) {
			c.moveToFirst();
		}

		db.close();

		List<Course> courses = new ArrayList<>();

		if (c != null && c.moveToFirst()) {
			do {
				Course course = new Course();
				course = setCourseDataFromDB(c, course);
				courses.add(course);
			} while (c.moveToNext());
			c.close();
		}

		return courses;
	}

	public void deleteCourse(Course course) {
		int id = course.get_id();
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_COURSES, KEY_COURSE_ID + " = " + id, null);
		db.close();
	}

	private Course setCourseDataFromDB(Cursor c, Course course) {

		course.set_id(c.getInt(DBHelper.COL_ID));
		course.setTitle(c.getString(DBHelper.COL_TITLE));
		course.setNumber(c.getInt(DBHelper.COL_NUMBER));
		course.setCredits(c.getDouble(DBHelper.COL_CRESDITS));
		course.setGrade(c.getInt(DBHelper.COL_GRADE));
		course.setYear(c.getInt(DBHelper.COL_YEAR));
		course.setSemester(c.getInt(DBHelper.COL_SEMESTER));

		return course;
	}
}
