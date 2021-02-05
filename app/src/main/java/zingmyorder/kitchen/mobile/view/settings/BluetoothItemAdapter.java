package zingmyorder.kitchen.mobile.view.settings;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import zingmyorder.kitchen.mobile.R;

import java.util.ArrayList;

public class BluetoothItemAdapter extends RecyclerView.Adapter<BluetoothItemAdapter.PairedItemHolder> {

    private ArrayList<BluetoothDevice> pairedDevices;
    private Context context;
//    MainActivity.PairedDeviceInteraction listener;
    private BluetoothDeviceInterface listener;
    private int defaultPrinterPos;

    public BluetoothItemAdapter(Context context, ArrayList<BluetoothDevice> pairedDevices, BluetoothDeviceInterface listener,int defaultPrinterPos) {
        this.pairedDevices=pairedDevices;
        this.context=context;
        this.listener=listener;
        this.defaultPrinterPos=defaultPrinterPos;
    }

    @NonNull
    @Override
    public PairedItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.bluetooth_item_view,parent,false);
        return new PairedItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PairedItemHolder holder, final int position) {
        holder.deviceName.setText(pairedDevices.get(position).getName());
        holder.deviceMac.setText(pairedDevices.get(position).getAddress());

        if (position==defaultPrinterPos) {
            holder.deviceInfo.setText("This is your default printer");
//            holder.deviceMac.setTextColor(context.getResources().getColor(R.color.mood_green));
            holder.btCard.setCardBackgroundColor(context.getResources().getColor(R.color.zing_light_green));
        } else {
            holder.deviceInfo.setText("Click to set this as default printer");
//            holder.deviceMac.setTextColor(context.getResources().getColor(R.color.light_grey));
            holder.btCard.setCardBackgroundColor(context.getResources().getColor(R.color.vrob_clr_white));
        }

        if (pairedDevices.get(position).getBondState()==BluetoothDevice.BOND_BONDED) {
            holder.pairButton.setText("UNPAIR");
        }else {
            holder.pairButton.setText("PAIR");
        }




        holder.pairButton.setOnClickListener(v -> {
            if (pairedDevices.get(position).getBondState()==BluetoothDevice.BOND_BONDED)
            listener.toggleBluetoothPair(pairedDevices.get(position),false);
            else
                listener.toggleBluetoothPair(pairedDevices.get(position),true);
        });

        holder.itemView.setOnClickListener(v -> {
            listener.setAsDefaultPrinter(pairedDevices.get(position));
            holder.btCard.setCardBackgroundColor(context.getResources().getColor(R.color.zing_light_green));
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return pairedDevices.size();
    }

    public class PairedItemHolder extends RecyclerView.ViewHolder {
        private TextView deviceName,deviceMac,pairButton,deviceInfo;
        private CardView btCard;
        public PairedItemHolder(@NonNull View itemView) {
            super(itemView);
            btCard=itemView.findViewById(R.id.btCard);
            deviceName=itemView.findViewById(R.id.deviceName);
            deviceMac=itemView.findViewById(R.id.deviceMac);
            pairButton=itemView.findViewById(R.id.pairButton);
            deviceInfo=itemView.findViewById(R.id.deviceInfo);
        }
    }
}
