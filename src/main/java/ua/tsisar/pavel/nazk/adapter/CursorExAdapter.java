package ua.tsisar.pavel.nazk.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cursoradapter.widget.CursorAdapter;

import ua.tsisar.pavel.nazk.R;

public class CursorExAdapter extends CursorAdapter {
    private static final String TAG = "MyLog";

    private static final String QUERY = "history_query";
    private onItemClickListener listener;
    private View view;
    private View dropDownView;

    public interface onItemClickListener {
        void onItemClick(String query);

        void onItemDelete(String query);
    }

    public CursorExAdapter(Context context, Cursor cursor) {
        super(context, cursor, false);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndex(QUERY));

        TextView textView = view.findViewById(R.id.text_view_history_item);
        textView.setText(name);

        if (listener != null) {
            view.setOnClickListener(v -> listener.onItemClick(name));
            view.findViewById(R.id.image_button_history_clear).
                    setOnClickListener(v -> listener.onItemDelete(name));
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(R.layout.item_history, parent, false);
    }

//    @Override
//    public Object getItem(int position) {
//        if (super.getCursor() != null && !super.getCursor().isClosed()) {
//            return super.getItem(position);
//        } else {
//            return null;
//        }
//    }
//
//    @Override
//    public long getItemId(int position) {
//        if (super.getCursor() != null && !super.getCursor().isClosed()) {
//            return super.getItemId(position);
//        } else {
//            return 0;
//        }
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        if(super.getCursor() != null && !super.getCursor().isClosed()) {
//            view = super.getView(position, convertView, parent);
//        }
////        Log.i(TAG, "getView: Cursor closed " + super.getCursor());
////        Log.i(TAG, "getView: view " + view);
//        return view;
//    }
//
//    @Override
//    public View getDropDownView(int position, View convertView, ViewGroup parent) {
//        if (super.getCursor() != null && !super.getCursor().isClosed()) {
//            dropDownView = super.getDropDownView(position, convertView, parent);
//        }
////        Log.i(TAG, "getDropDownView: Cursor closed");
//        return dropDownView;
//    }
}