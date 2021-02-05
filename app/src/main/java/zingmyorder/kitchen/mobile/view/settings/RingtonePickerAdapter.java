package zingmyorder.kitchen.mobile.view.settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import zingmyorder.kitchen.mobile.R;
import zingmyorder.kitchen.mobile.view.settings.models.RingtoneModel;

public class RingtonePickerAdapter extends RecyclerView.Adapter<RingtonePickerAdapter.ViewHolder> {

    private ArrayList<RingtoneModel> ringtoneModel;
    private Context mContext;
    private RingtoneInterface listener;
    private String selected_ring;

    public RingtonePickerAdapter(ArrayList<RingtoneModel> ringtoneModel, Context mContext, RingtoneInterface listener, String selected_ring) {
        this.ringtoneModel = ringtoneModel;
        this.mContext = mContext;
        this.listener = listener;
        this.selected_ring = selected_ring;
    }

    @NonNull
    @Override
    public RingtonePickerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.ring_tone_itemview,parent,false);
        return new RingtonePickerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RingtonePickerAdapter.ViewHolder holder, int position) {
        holder.ringtoneName.setText(ringtoneModel.get(position).getRingName());
        if (ringtoneModel.get(position).getRingID().equalsIgnoreCase(selected_ring)) {
            holder.selectionTick.setVisibility(View.VISIBLE);
        } else {
            holder.selectionTick.setVisibility(View.INVISIBLE);
        }
        holder.itemView.setOnClickListener(v -> {
            listener.ringtoneSelected(ringtoneModel.get(position));
            selected_ring=ringtoneModel.get(position).getRingID();
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return ringtoneModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView ringtoneName;
        private AppCompatImageButton selectionTick;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ringtoneName=itemView.findViewById(R.id.ringtoneName);
            selectionTick=itemView.findViewById(R.id.selectionTick);
        }
    }

    public interface RingtoneInterface {
       void ringtoneSelected(RingtoneModel ringtoneModel);
    }
}
