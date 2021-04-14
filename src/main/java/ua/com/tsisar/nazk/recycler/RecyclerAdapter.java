package ua.com.tsisar.nazk.recycler;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import ua.com.tsisar.nazk.DBHelper;
import ua.com.tsisar.nazk.R;
import ua.com.tsisar.nazk.dto.Item;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.itemViewHolder> {
    private static final String TAG = "MyLog";
    private final LayoutInflater inflater;
    private final List<Item> items;
    private final Context context;
    private final DBHelper dbHelper;

    public interface onItemClickListener {
        void onItemClick(Item item);
    }

    private onItemClickListener listener;

    public RecyclerAdapter(Context context, List<Item> items){
        this.context = context;
        this.items = items;
        inflater = LayoutInflater.from(context);
        dbHelper = new DBHelper(context);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public itemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.row_item_list, parent, false);
        return new itemViewHolder(view, dbHelper);
    }

    @Override
    public void onBindViewHolder(@NonNull itemViewHolder holder, int position) {
        if(items == null)
            return;

        Item item = items.get(position);

        holder.setItem(context, item);
        if(listener != null){
            holder.itemView.setOnClickListener(view -> listener.onItemClick(item));
        }
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
        private static final String NAME_FORMAT = "%s %s %s";
        private Item item;
        private final TextView name;
        private final TextView document;
        private final TextView year;
        private final TextView workPlace;
        private final TextView workPost;
        private final TextView date;
        private final ImageButton star;
        private final DBHelper dbHelper;

        private itemViewHolder(View itemView, DBHelper dbHelper) {
            super(itemView);
            this.dbHelper = dbHelper;

            name = itemView.findViewById(R.id.text_view_item_name);
            document = itemView.findViewById(R.id.text_view_item_document);
            year = itemView.findViewById(R.id.text_view_item_year);
            workPlace = itemView.findViewById(R.id.text_view_item_work_place);
            workPost = itemView.findViewById(R.id.text_view_item_work_post);
            date = itemView.findViewById(R.id.text_view_item_date);
            star = itemView.findViewById(R.id.image_button_star);
            star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("MyLog", "Star " + item.getId());
                    v.setSelected(!v.isSelected());
                    if(dbHelper.isSaved(item.getId())){
                        dbHelper.delete(item.getId());
                    }else {
                        dbHelper.save(item);
                    }
                }
            });
        }

        void setItem(Context context, Item item){
            this.item = item;
            star.setSelected(dbHelper.isSaved(item.getId()));

            String documentType = context.getResources()
                    .getStringArray(R.array.array_document_type)[item.getDocumentType()];
            String declarationType = context.getResources()
                    .getStringArray(R.array.array_declaration_type)[item.getDeclarationType()];

            name.setText(String.format(NAME_FORMAT,
                    item.getLastName(), item.getFirstName(), item.getMiddleName()));
            document.setText(item.getDeclarationType() != 0?
                    String.format("%s (%s)", documentType, declarationType):documentType);
            year.setText(String.format("Рік: %s", item.getDeclarationYear()));
            workPlace.setText(String.format("Місце роботи: %s", item.getWorkPlace()));
            workPost.setText(String.format("Посада: %s", item.getWorkPost()));
            date.setText(String.format("Дата та час подання: %s", dateFormat(item.getDate())));
        }

        private String dateFormat(String date) {
            try {
                SimpleDateFormat inputDateFormat =
                        new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault());
                SimpleDateFormat outputDateFormat =
                        new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
                return outputDateFormat.format(Objects.requireNonNull(inputDateFormat.parse(date)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return "";
        }
    }
}