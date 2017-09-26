package arvin.com.test.android;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import arvin.com.test.R;
import arvin.com.test.data.DeviceData;
import arvin.com.test.data.DeviceSubItemData;
import arvin.com.test.databinding.ActivityMainBinding;
import arvin.com.test.view.DtRecyclerView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        List<DeviceSubItemData> subItemDatas = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            DeviceSubItemData subItemData = new DeviceSubItemData("111", "测试", "11.7S", "s", true, true);
            if (i % 2 == 0)
                subItemData.setSubItemDataVisibility(false);

            subItemDatas.add(subItemData);
        }

        DeviceData data = new DeviceData("", "", "", true, true, subItemDatas);

        DtRecyclerView dtRecyclerView1 = new DtRecyclerView(this);
        dtRecyclerView1.setData(data);
        binding.linearLayout.addView(dtRecyclerView1);

        DtRecyclerView dtRecyclerView2 = new DtRecyclerView(this);
        dtRecyclerView2.setData(data);
        binding.linearLayout.addView(dtRecyclerView2);
    }
}
