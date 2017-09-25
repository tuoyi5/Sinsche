package arvin.com.test.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import arvin.com.test.R;
import arvin.com.test.data.DtData;
import arvin.com.test.databinding.DtRecyclerItemBinding;

/**
 * Created by tuoyi on 2017/9/25 0025.
 */

public class DtBindAdapter extends RecyclerView.Adapter<DtBindAdapter.DtBindingHolder> {

    private DtData dtdata;
    private Context context;

    public DtBindAdapter(Context context, DtData dtdata) {
        this.context = context;
        this.dtdata = dtdata;
    }

    @Override
    public DtBindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DtRecyclerItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dt_recycler_item, parent, false);
        return new DtBindingHolder(binding);
    }

    @Override
    public void onBindViewHolder(DtBindingHolder holder, int position) {
        holder.getBinding().setName(dtdata.get(position));
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return dtdata == null ? 0 : dtdata.size();
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
