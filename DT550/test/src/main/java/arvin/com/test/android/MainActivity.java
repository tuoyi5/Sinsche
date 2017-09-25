package arvin.com.test.android;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import arvin.com.test.R;
import arvin.com.test.data.DtData;
import arvin.com.test.databinding.ActivityMainBinding;
import arvin.com.test.view.DtRecyclerView;

public class MainActivity extends AppCompatActivity {

    private List<String> names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        names = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            names.add("hahahahah" + i);
        }

        DtData data = new DtData(names);
        binding.dtRecyclerView.setData(data);
    }
}
