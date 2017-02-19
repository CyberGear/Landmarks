package lt.markav.legendsoflayouts.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class SupportFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState) {
        TextView textView = new TextView(getActivity());
        textView.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
        textView.setGravity(Gravity.CENTER);
        textView.setText(getClass().getSimpleName());
        return textView;
    }

}
