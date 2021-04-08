package ua.com.tsisar.nazk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ua.com.tsisar.nazk.R;
import ua.com.tsisar.nazk.dto.Item;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.itemViewHolder> {
    private final LayoutInflater inflater;
    private final List<Item> items;

    public RecyclerAdapter(Context context, List<Item> items){
        inflater = LayoutInflater.from(context);
        this.items = items;
    }

    public Item getItem(int position){
        return items.get(position);
    }
    
    @Override
    public itemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.row_item_list, parent, false);
        return new itemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(itemViewHolder holder, int position) {
        if(items == null)
            return;

        Item item = items.get(position);

        holder.setLastName(item.getLastname());
        holder.setFirstName(item.getFirstname());
//        holder.setPosition(item.getPosition());
        holder.setPlaceOfWork(item.getWorkPlace());
    }

    @Override
    public int getItemCount() {
        if(items == null) {
            return 0;
        }else{
            return items.size();
        }
    }

    static class itemViewHolder extends RecyclerView.ViewHolder {

        private final TextView lastname;
        private final TextView firstname;
//        private TextView position;
        private final TextView placeOfWork;

        private itemViewHolder(View itemView) {
            super(itemView);

            lastname = itemView.findViewById(R.id.text_view_item_lastname);
            firstname = itemView.findViewById(R.id.text_view_item_firstname);
//            position = itemView.findViewById(R.id.position_textView);
            placeOfWork = itemView.findViewById(R.id.text_view_item_place_of_work);
        }

        void setLastName(String string){
            lastname.setText(string);
        }

        void setFirstName(String string){
            firstname.setText(string);
        }

//        void setPosition(String string){
//            position.setText(string);
//        }

        void setPlaceOfWork(String string){
            placeOfWork.setText(string);
        }
    }
}