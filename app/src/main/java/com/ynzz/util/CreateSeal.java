package com.ynzz.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;

/**
 * Created by ynzz on 2017/8/29.
 */

public class CreateSeal {
    private int roundWidth = 10; //边框宽度
    float padding = 50; //文字所占角度
    private String str = "云南中智物联网科技有限公司";

    private Canvas canvas;
    private Paint paint;
    private Bitmap bitmap;
    private int centre;
    private int radius;

    public CreateSeal(Bitmap bitmap) {
        // TODO Auto-generated constructor stub
        this.bitmap = bitmap;
        canvas = new Canvas(bitmap);
        paint = new Paint();
    }

    private void getSeal(){

        getRing();
        drawStar();
        drawText();
    }
    //绘制圆环
    private void getRing() {

        centre = canvas.getWidth() / 2; // 获取圆心的x坐标
        radius = (int) (centre - roundWidth / 2); // 圆环的半径
        paint.setColor(Color.RED); // 设置圆环的颜色
        paint.setStyle(Paint.Style.STROKE); // 设置空心
        paint.setStrokeWidth(roundWidth); // 设置圆环的宽度
        paint.setAntiAlias(true); // 消除锯齿
        canvas.drawCircle(centre, centre, radius, paint); // 画出圆环

    }
    //绘制五角星
    private void drawStar(){

        float start_radius = (float) ((radius / 2)*1.1);
        int x = centre, y = centre;
        float x1,y1,x2,y2,x3,y3,x4,y4,x5,y5;

        float r72 = (float) Math.toRadians(72);
        float r36 = (float) Math.toRadians(36);
        //顶点
        x1 = x;
        y1 = y - start_radius;
        //左1
        x2 = (float) (x - start_radius*Math.sin(r72));
        y2 = (float) (y - start_radius*Math.cos(r72));
        //右1
        x3 = (float) (x + start_radius*Math.sin(r72));
        y3 = (float) (y - start_radius*Math.cos(r72));
        //左2
        x4 = (float) (x - start_radius*Math.sin(r36));
        y4 = (float) (y + start_radius*Math.cos(r36));
        //右2
        x5 = (float) (x + start_radius*Math.sin(r36));
        y5 = (float) (y + start_radius*Math.cos(r36));

        //连接各个节点，绘制五角星
        Path path = new Path();
        path.moveTo(x1, y1);
        path.lineTo(x5, y5);
        path.lineTo(x2, y2);
        path.lineTo(x3, y3);
        path.lineTo(x4, y4);
        path.close();

        Paint paint = new Paint();
        paint.setColor(Color.RED);

        canvas.drawPath(path, paint);
    }
    private void drawText(){

        //设置文字属性
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setTextSize(radius/5+5);
        paint.setTextAlign(Paint.Align.CENTER);
        //圆弧文字所在矩形范围
        RectF oval=new RectF(0, 0, 2*radius, (float) (2*radius));
        //第一个文字偏移角度，其中padding/2为文字间距
        float firstrad = 90 + padding * (str.length()) / 4 - padding/8;
        for(int i = 0; i < str.length(); i++){
            Path path = new Path();
            //根据角度生成弧线路径
            path.addArc(oval,-(firstrad-padding*i/2), padding);
            canvas.drawTextOnPath(String.valueOf(str.charAt(i)), path, -(float) (radius/3),(float) (radius/3), paint);
        }
    }
    public Bitmap getBitmap(){
        getSeal();
        return bitmap;
    }
    public int getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(int width) {
        this.roundWidth = roundWidth;
    }
}
