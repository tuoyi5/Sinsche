package arvin.com.test.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import arvin.com.test.R;
import arvin.com.test.data.DeviceData;
import arvin.com.test.data.DeviceSubItemData;
import arvin.com.test.databinding.DtRecyclerItemBinding;

/**
 * Created by tuoyi on 2017/9/25 0025.
 */

public class DtBindAdapter extends RecyclerView.Adapter<DtBindAdapter.DtBindingHolder> {

    private Context context;
    private List<DeviceSubItemData> subItemDataList;

    public DtBindAdapter(Context context, DeviceData deviceData) {
        this.context = context;
        this.subItemDataList = deviceData.getDeviceSubItemDatas();
    }

    @Override
    public DtBindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DtRecyclerItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dt_recycler_item, parent, false);
        return new DtBindingHolder(binding);
    }

    @Override
    public void onBindViewHolder(DtBindingHolder holder, int position) {
        holder.getBinding().setSubItem(subItemDataList.get(position));
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return subItemDataList == null ? 0 : subItemDataList.size();
    }

    public class DtBindingHolder extends RecyclerView.ViewHolder {
        private DtRecyclerItemBinding binding;

        public DtBindingHolder(DtRecyclerItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public DtRecyclerItemBinding getBinding() {
            return binding;
        }

        public void setBinding(DtRecyclerItemBinding binding) {
            this.binding = binding;
        }
    }

}
