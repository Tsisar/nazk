package ua.com.tsisar.nazk.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import ua.com.tsisar.nazk.R;
import ua.com.tsisar.nazk.dto.Item;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.itemViewHolder> {
    private static final String TAG = "MyLog";
    private final LayoutInflater inflater;
    private final List<Item> items;
    Context context;

    public RecyclerAdapter(Context context, List<Item> items){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.items = items;
    }

    public Item getItem(int position){
        return items.get(position);
    }
    
    @NonNull
    @Override
    public itemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.row_item_list, parent, false);
        return new itemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull itemViewHolder holder, int position) {
        if(items == null)
            return;

        Item item = items.get(position);

        String documentType = context.getResources()
                .getStringArray(R.array.array_document_type)[item.getDocumentType()];
        String declarationType = context.getResources()
                .getStringArray(R.array.array_declaration_type)[item.getDeclarationType()];

        holder.setName(item.getFirstName(), item.getMiddleName(), item.getLastName());
        holder.setDocument(item.getDeclarationType() != 0?
                String.format("%s (%s)", documentType, declarationType):documentType);
        holder.setYear(String.format("Рік: %s", item.getDeclarationYear()));
        holder.setWorkPlace(String.format("Місце роботи: %s", item.getWorkPlace()));
        holder.setWorkPost(String.format("Посада: %s", item.getWorkPost()));
        holder.setDate(String.format("Дата та час подання: %s", dateFormat(item.getDate())));
    }

    @Override
    public int getItemCount() {
        if(items == null) {
            return 0;
        }else{
            return items.size();
        }
    }

    public String dateFormat(String date) {
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

    static class itemViewHolder extends RecyclerView.ViewHolder {
        private static final String NAME_FORMAT = "%s %s %s";
        private final TextView name;
        private final TextView document;
        private final TextView year;
        private final TextView workPlace;
        private final TextView workPost;
        private final TextView date;

        private itemViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.text_view_item_name);
            document = itemView.findViewById(R.id.text_view_item_document);
            year = itemView.findViewById(R.id.text_view_item_year);
            workPlace = itemView.findViewById(R.id.text_view_item_work_place);
            workPost = itemView.findViewById(R.id.text_view_item_work_post);
            date = itemView.findViewById(R.id.text_view_item_date);
        }

        void setName(String firstName, String middleName, String lastName){
            name.setText(String.format(NAME_FORMAT, lastName, firstName, middleName));
        }

        void setDocument(String string){
            document.setText(string);
        }

        void setYear(String string){
            year.setText(string);
        }

        void setWorkPlace(String string){
            workPlace.setText(string);
        }

        void setWorkPost(String string){
            workPost.setText(string);
        }

        void setDate(String string){
            date.setText(string);
        }
    }
}