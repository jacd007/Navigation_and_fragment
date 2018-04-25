package com.zippyttech.navigation_and_fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zippyttech.navigation_and_fragment.common.Utils;

public class ScrollingActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView text, titleToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        titleToolbar = (TextView) findViewById(R.id.titleToolbar);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");

      String content = intent.getStringExtra("content")
              /*  .replace("<p>","")
                .replace("</p>","")
                .replace("/","")
                .replace("[&hellip;]","")
                .replace("</p>","")*/;
       // String content = Utils.RegexReplaceSimbol(intent.getStringExtra("content"));
        text = (TextView) findViewById(R.id.text);
        text.setText(Utils.ReplaceSimbol(content));
        getSupportActionBar().setTitle("");
        titleToolbar.setTypeface(null, Typeface.BOLD_ITALIC);
        titleToolbar.setText(Utils.ReplaceSimbol(title));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View view) {

    }
}
