package com.example.checkwebsite;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Pie extends Activity {
	
	public static final String TYPE = "type";
	private static int[] COLORS = new int[] { Color.GREEN, Color.BLUE, Color.MAGENTA, Color.CYAN };//����� �����
	private CategorySeries mSeries = new CategorySeries("");//��������� �� ��������
	private DefaultRenderer mRenderer = new DefaultRenderer();//������� ������
	private GraphicalView mChartView;//���� (�����) ������������� ���������
	TextView textOk;
	TextView textError;
	
	DBHelper dbHelper;
	public int ok_status_count(){
	      dbHelper = new DBHelper(this);
	      String str = "";
	      SQLiteDatabase db = dbHelper.getWritableDatabase();
	      Cursor c = db.rawQuery("SELECT COUNT(*) FROM response WHERE answer = 200", null);
	      c.moveToFirst();
	      str = c.getString(0);
	      int count_ok = Integer.parseInt(str);
	      textOk.setText("Ok - " + str);
	      return count_ok;
	}
	
	public int error_status_count(){
		  dbHelper = new DBHelper(this);
	      String str = "";
	      SQLiteDatabase db = dbHelper.getWritableDatabase();
	      Cursor c = db.rawQuery("SELECT COUNT(*) FROM response WHERE answer != 200", null);
	      c.moveToFirst();
	      str = c.getString(0);
	      int count_error = Integer.parseInt(str);
	      textError.setText("Error - " + str);
	      return count_error;
	}
	public void add_pie_slice(){	   
	        mSeries.add("Ok", ok_status_count());//��������� ����� ���������(����� + ��������)
	        SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();//������ �����
	        renderer.setColor(Color.GREEN);//����������� ����� ���� �� �������
	        mRenderer.addSeriesRenderer(renderer);//��������� �������� ������ �����
	        
	        if (mChartView != null) {
	          mChartView.repaint();//���� �� �����, �� ������������ (���� ���� �����, �� ����� � �� ���� ��������)
	        }
	        SimpleSeriesRenderer renderer1 = new SimpleSeriesRenderer();//������ �����
	        mSeries.add("Error", error_status_count());//��������� ����� ���������(����� + ��������)
	      
	        renderer1.setColor(Color.RED);//����������� ����� ���� �� �������
	        mRenderer.addSeriesRenderer(renderer1);//��������� �������� ������ �����       
	
	        if (mChartView != null) {
	          mChartView.repaint();//���� �� �����, �� ������������ (���� ���� �����, �� ����� � �� ���� ��������)
	        }
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.pie);
	   
	    textOk = (TextView) findViewById(R.id.textOk);
	    textError = (TextView) findViewById(R.id.textError);
	    //��������� �������
	    mRenderer.setApplyBackgroundColor(true);
	    mRenderer.setBackgroundColor(Color.argb(100, 245, 245, 245));
	    mRenderer.setLabelsTextSize(16);
	    mRenderer.setLabelsColor(Color.BLACK);
	    mRenderer.setShowLegend(false);
	    mRenderer.setMargins(new int[] { 20, 30, 15, 0 });
	    mRenderer.setStartAngle(90);
	    //���������� ������

	    add_pie_slice();
	}

  @Override
  protected void onResume() {
    super.onResume();
    
    if (mChartView == null) {
      LinearLayout layout = (LinearLayout) findViewById(R.id.chart);//������ � ������� ����� ��������
      mChartView = ChartFactory.getPieChartView(this, mSeries, mRenderer);//�������� ���-���������
      mRenderer.setClickEnabled(true);
      mRenderer.setSelectableBuffer(1);

      layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
    } else {
      mChartView.repaint();
    }
  }
}
