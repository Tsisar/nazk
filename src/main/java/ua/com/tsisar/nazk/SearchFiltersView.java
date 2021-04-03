package ua.com.tsisar.nazk;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SearchFiltersView extends LinearLayout implements View.OnClickListener{

    private Listener listener;
    private TextView itemName;
    private int itemType;

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
        View view = inflate(getContext(), R.layout.item_search_filters, null);
        ImageButton cleanButton = view.findViewById(R.id.sf_clean_button);
        cleanButton.setOnClickListener(this);
        itemName = view.findViewById(R.id.sf_item_name_textView);
        addView(view);
    }

    public SearchFiltersView setItemName(String name) {
        itemName.setText(name);
        return this;
    }

    public SearchFiltersView setItemType(int type) {
        itemType = type;
        return this;
    }

    public int getItemType() {
        return itemType;
    }

    @Override
    public void onClick(View v) {
        listener.removeView(this);
    }
}