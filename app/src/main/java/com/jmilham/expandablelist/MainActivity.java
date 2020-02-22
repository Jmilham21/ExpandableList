package com.jmilham.expandablelist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jmilham.expandinglist.ExpandingList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ExpandingList expand = findViewById(R.id.expand);
        expand.setTitle("Testing this out");

        for(int i=0; i < 5; i++){
            LinearLayout ll = new LinearLayout(this);
            TextView textView = new TextView(this);
            textView.setText("Item: " + i);

            Button button = new Button(this);
            button.setText("Click me");

            final int finalI = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(),"test: " + finalI, Toast.LENGTH_LONG).show();
                }
            });

            ll.addView(textView);
            ll.addView(button);
            expand.addLayout(ll);
        }
        LinearLayout ll = new LinearLayout(this);
        TextView textView = new TextView(this);
        textView.setText("Item");

        Button button = new Button(this);
        button.setText("Don't Click me");

        ll.addView(textView);
        ll.addView(button);
        ExpandingList expandingList = new ExpandingList(this);
        expandingList.addLayout(ll);
        expandingList.setTitle("anotha one");

        LinearLayout mainLayou = findViewById(R.id.mainLayou);
        mainLayou.addView(expandingList);
    }
}
