package com.ynzz.lbstest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

public class Ratingbardemo extends AppCompatActivity {
    private RatingBar rb_normal;
    private EditText edit_evaluate;
    private Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratingbardemo);
        rb_normal = (RatingBar)findViewById(R.id.rb_normal);
        edit_evaluate = (EditText)findViewById(R.id.edit_evaluate);
        submit = (Button)findViewById(R.id.submit);
        initToolbar();
        rb_normal.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener(){
            @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser){
                Toast.makeText(Ratingbardemo.this, "rating:" + String.valueOf(rating),
                        Toast.LENGTH_LONG).show();
                edit_evaluate.setText(String.valueOf(rating));
            }
        });
        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(Ratingbardemo.this,"提交成功",Toast.LENGTH_LONG).show();
            }
        });
    }
    //initToolbar
    private void initToolbar() {

        View rootView = findViewById(R.id.activity_about_toolbar);

        if (rootView != null) {

            Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);

            toolbar.setTitle("评价");

            setSupportActionBar(toolbar);

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Ratingbardemo.this.finish();
                }
            });
        }
    }
}
