package ua.com.tsisar.nazk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import ua.com.tsisar.nazk.R;
import ua.com.tsisar.nazk.filters.Type;

public class SearchFiltersView extends LinearLayout implements View.OnClickListener{

    private Listener listener;
    private TextView name;
    private Type type;

    public interface Listener {
        void removeView(SearchFiltersView view);
    }

    public SearchFiltersView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (context instanceof Listener) {
            this.listener = (Listener) context;
        }
        initView();
    }

    public SearchFiltersView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (context instanceof Listener) {
            this.listener = (Listener) context;
        }
        initView();
    }

    public SearchFiltersView(Context context) {
        super(context);
        if (context instanceof Listener) {
            this.listener = (Listener) context;
        }
        initView();
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.item_filters, null);
        ImageButton cleanButton = view.findViewById(R.id.image_button_item_filters_clear);
        cleanButton.setOnClickListener(this);
        name = view.findViewById(R.id.text_view_item_filters_name);
        addView(view);
    }

    public SearchFiltersView setName(String name) {
        this.name.setText(name);
        return this;
    }

    public SearchFiltersView setType(Type type) {
        this.type = type;
        return this;
    }

    public Type getType() {
        return type;
    }

    @Override
    public void onClick(View v) {
        listener.removeView(this);
    }
}