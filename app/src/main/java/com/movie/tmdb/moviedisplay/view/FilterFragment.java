package com.movie.tmdb.moviedisplay.view;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.movie.tmdb.moviedisplay.R;

/**
 * Created by rohit.khurana on 6/3/2017.
 */

public class FilterFragment extends DialogFragment {

    private FilterCallback mCallBack;

    public interface FilterCallback {
        public void onFilterApply(String fromYear, String toYear);

        public void onFilterClear();
    }

    public void setmCallBack(FilterCallback callBack) {
        mCallBack = callBack;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.filter_fragment, container, false);
        Window window = getDialog().getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setGravity(Gravity.RIGHT | Gravity.TOP);
        WindowManager.LayoutParams p = window.getAttributes();
        p.width = ViewGroup.LayoutParams.MATCH_PARENT;
        p.height = ViewGroup.LayoutParams.WRAP_CONTENT;

        window.setAttributes(p);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        final Spinner fromYear = (Spinner) view.findViewById(R.id.fromYear);
        fromYear.setAdapter(getAdapter());

        final Spinner toYear = (Spinner) view.findViewById(R.id.toYear);
        toYear.setAdapter(getAdapter());

        Button apply = (Button) view.findViewById(R.id.applyfilter);
        Button clear = (Button) view.findViewById(R.id.clearfilter);

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String from = fromYear.getSelectedItem().toString();
                String to = toYear.getSelectedItem().toString();

                int fromYear = Integer.parseInt(from);
                int toYear = Integer.parseInt(to);
                if (fromYear <= toYear) {
                    mCallBack.onFilterApply(from, to);
                    dismiss();
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.onFilterClear();
                dismiss();
            }
        });
    }

    private ArrayAdapter<CharSequence> getAdapter() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.year, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

}
