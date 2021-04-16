package ua.com.tsisar.nazk.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cursoradapter.widget.CursorAdapter;

import ua.com.tsisar.nazk.R;

public class CursorExAdapter extends CursorAdapter {
    private static final String QUERY = "history_query";

    private TextView text;

    public CursorExAdapter(Context context, Cursor cursor) {
        super(context, cursor, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        text.setText(cursor.getString(cursor.getColumnIndex(QUERY)));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.history_item, parent, false);
        text = view.findViewById(R.id.item);
        return view;
    }
}