package ua.tsisar.pavel.nazk.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ua.tsisar.pavel.nazk.R;
import ua.tsisar.pavel.nazk.dto.ItemDTO;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.itemViewHolder> {


    private final LayoutInflater inflater;

    private List<ItemDTO> list;

    public RecyclerAdapter(Context context, List<ItemDTO> list){
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    public ItemDTO getItemDTO(int position){
        return list.get(position);
    }
    
    @Override
    public itemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.row_item_list, parent, false);
        return new itemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(itemViewHolder holder, int position) {
        if(list == null)
            return;

        ItemDTO item = list.get(position);

        holder.setLastname(item.getLastname());
        holder.setFirstname(item.getFirstname());
//        holder.setPosition(item.getPosition());
        holder.setPlaceOfWork(item.getPlaceOfWork());
    }

    @Override
    public int getItemCount() {
        if(list == null) {
            return 0;
        }else{
            return list.size();
        }
    }

    class itemViewHolder extends RecyclerView.ViewHolder {

        private TextView lastname;
        private TextView firstname;
//        private TextView position;
        private TextView placeOfWork;

        private itemViewHolder(View itemView) {
            super(itemView);

            lastname = itemView.findViewById(R.id.lastname_textView);
            firstname = itemView.findViewById(R.id.firstname_textView);
//            position = itemView.findViewById(R.id.position_textView);
            placeOfWork = itemView.findViewById(R.id.placeOfWork_textView);
        }

        void setLastname(String string){
            lastname.setText(string);
        }

        void setFirstname(String string){
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