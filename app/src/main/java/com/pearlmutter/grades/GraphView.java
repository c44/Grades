package com.pearlmutter.grades;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class GraphView extends View {

	Random rand = new Random();

	private List<Course> courses = new ArrayList<>();
	private HashSet<String> set = new HashSet<>();
	private List<Semester> semesters = new ArrayList<>();

	private int scale = 8;
	private int width = 50;
	private double gradeAverage;
	private int amountOfCourses = 0;
	int[] colors = new int[0];

	Rect rect = new Rect();
	Paint fillpaint = new Paint();
	Paint textPaint = new Paint();
	Paint linePaint = new Paint();

	private int progress = 0;

	public GraphView(Context context) {
		super(context);
	}

	public GraphView(Context context, AttributeSet attrs) {
		super(context, attrs);

		fillpaint.setColor(Color.RED);
		fillpaint.setStyle(Paint.Style.FILL);

		textPaint.setTextSize(30);
		textPaint.setStyle(Paint.Style.FILL);
		textPaint.setColor(Color.BLACK);

		linePaint.setColor(Color.BLACK);
		linePaint.setStyle(Paint.Style.FILL_AND_STROKE);
		linePaint.setStrokeWidth(5);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (progress > 75)
			textPaint.setAlpha(((progress - 75) * 255) / 25);
		else
			textPaint.setAlpha(0);

//		canvas.drawColor(Color.WHITE);

		canvas.drawLine((width * 5) / 2, canvas.getHeight() - 100, (width * 5) / 2, canvas.getHeight() - 100 - ((100 * scale * progress) / 100), linePaint);

		for (int j = 1; j <= 10; j++) {
			canvas.drawLine((width * 5) / 2 - 5, canvas.getHeight() - 100 - ((j * 10 * scale * progress) / 100), (width * 5) / 2 + 5, canvas.getHeight() - 100 - ((j * 10 * scale * progress) / 100), linePaint);
		}

		textPaint.setTextAlign(Paint.Align.RIGHT);
		for (int j = 1; j <= 10; j++) {
			canvas.drawText(j * 10 + "", (width * 5) / 2 - 10, canvas.getHeight() - 100 - ((j * 10 * scale * progress) / 100) + 10, textPaint);
		}

		int left, top, right, bottom;

		int i = 1;

		for (Semester semester : semesters) {
			int start = i;
			for (Course course : semester.getCourses()) {

				fillpaint.setColor(colors[semesters.indexOf(semester)]);

				left = (i * width * 2) + width;
				top = canvas.getHeight() - 100 - (((int) course.getGrade() * scale * progress) / 100);
				right = (i * width * 2) + width * 2;
				bottom = canvas.getHeight() - 100;

				rect.set(left, top, right, bottom);
				canvas.drawRect(rect, fillpaint);

				canvas.save();
				canvas.rotate(45, left, bottom + 20);
				textPaint.setTextAlign(Paint.Align.LEFT);
				canvas.drawText(course.getTitle(), left, bottom + 20, textPaint);
				canvas.restore();

				textPaint.setTextAlign(Paint.Align.CENTER);
				canvas.drawText((int) course.getGrade() + "", (left + right) / 2, (bottom + top) / 2, textPaint);

				i++;
			}

			canvas.drawLine((start * width * 2) + (width / 2), canvas.getHeight() - 100 - (((int) semester.getGrade() * scale * progress) / 100), ((i - 1) * width * 2) + (width * 5) / 2, canvas.getHeight() - 100 - (((int) semester.getGrade() * scale * progress) / 100), linePaint);
		}

		canvas.drawLine((width * 5) / 2, canvas.getHeight() - 100 - (((int) gradeAverage * scale * progress) / 100), (i * width * 2) + width / 2, canvas.getHeight() - 100 - (((int) gradeAverage * scale * progress) / 100), linePaint);

		canvas.drawLine((width * 5) / 2, canvas.getHeight() - 100, ((amountOfCourses + 1) * width * 2) + width / 2, canvas.getHeight() - 100, linePaint);

		if (progress < 100) {
			progress++;
			invalidate();
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		setMeasuredDimension(((amountOfCourses + 1) * width * 2) + width * 2, View.MeasureSpec.getSize(heightMeasureSpec));
	}

	public void redraw() {
		progress = 0;
		randColors();
		invalidate();
	}

	private void randColors() {
		for (int c = 0; c < colors.length; c++) {
			int r = rand.nextInt(256) / 2 + 128;
			int g = rand.nextInt(256) / 2 + 128;
			int b = rand.nextInt(256) / 2 + 128;
			colors[c] = Color.rgb(r, g, b);
		}
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;

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


		try {
			gradeAverage = gradesTotal / creditsTotal;
		} catch (Exception e) {
			gradeAverage = 0;
		}


		for (Semester semester : semesters) {
			amountOfCourses += semester.getCourses().size();
		}

		colors = new int[semesters.size()];
		randColors();

		invalidate();
	}
}
