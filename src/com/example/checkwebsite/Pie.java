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
	private static int[] COLORS = new int[] { Color.GREEN, Color.BLUE, Color.MAGENTA, Color.CYAN };//цвета серий
	private CategorySeries mSeries = new CategorySeries("");//категории на диаграме
	private DefaultRenderer mRenderer = new DefaultRenderer();//главный рендер
	private GraphicalView mChartView;//вьюв (оксно) инкапсулирует диаграммы
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
        mSeries.add("Ok", ok_status_count());//добавляем новую категорию(номер + значение)
        SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();//рендер серий
        renderer.setColor(Color.GREEN);//присваиваем серии цвет из массива
        mRenderer.addSeriesRenderer(renderer);//основному передаем рендер серии
        
        if (mChartView != null) {
          mChartView.repaint();//если не пусто, то перерисовуем (если бред ввели, то пусто и не стои рисовать)
        }
        SimpleSeriesRenderer renderer1 = new SimpleSeriesRenderer();//рендер серий
        mSeries.add("Error", error_status_count());//добавляем новую категорию(номер + значение)
      
        renderer1.setColor(Color.RED);//присваиваем серии цвет из массива
        mRenderer.addSeriesRenderer(renderer1);//основному передаем рендер серии       

        if (mChartView != null) {
          mChartView.repaint();//если не пусто, то перерисовуем (если бред ввели, то пусто и не стои рисовать)
        }
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.pie);
	   
	    textOk = (TextView) findViewById(R.id.textOk);
	    textError = (TextView) findViewById(R.id.textError);
	    //Настрокай рендера
	    mRenderer.setApplyBackgroundColor(true);
	    mRenderer.setBackgroundColor(Color.argb(100, 245, 245, 245));
	    mRenderer.setLabelsTextSize(16);
	    mRenderer.setLabelsColor(Color.BLACK);
	    mRenderer.setShowLegend(false);
	    mRenderer.setMargins(new int[] { 20, 30, 15, 0 });
	    mRenderer.setStartAngle(90);
	    //Считывание данных

	    add_pie_slice();
	}

  @Override
  protected void onResume() {
    super.onResume();
    
    if (mChartView == null) {
      LinearLayout layout = (LinearLayout) findViewById(R.id.chart);//лайоут в котором будем рисовать
      mChartView = ChartFactory.getPieChartView(this, mSeries, mRenderer);//создание пие-диаграммы
      mRenderer.setClickEnabled(true);
      mRenderer.setSelectableBuffer(1);

      layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
    } else {
      mChartView.repaint();
    }
  }
}