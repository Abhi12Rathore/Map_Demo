package klientotech.com.mapdemo.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

import klientotech.com.mapdemo.Adaper.NearByAdapter;
import klientotech.com.mapdemo.R;
import klientotech.com.mapdemo.Response.Result;

public class NearByActivity extends AppCompatActivity {
    private List<Result> resultList;
    private RecyclerView recylerView;
    private int pos;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_by);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Near By");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        resultList = getIntent().getParcelableArrayListExtra("data");
        recylerView = findViewById(R.id.recylerView);
        recylerView.hasFixedSize();
        recylerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recylerView.setAdapter(new NearByAdapter(NearByActivity.this, resultList));

    }

    public void loadPhoto(int pos) {
        this.pos = pos;
    }
}
