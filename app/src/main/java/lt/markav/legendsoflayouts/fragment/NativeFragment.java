package lt.markav.legendsoflayouts.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class NativeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedState) {
        TextView textView = new TextView(getActivity());
        textView.setLayoutParams(new LayoutParams(MATCH_PARENT, MATCH_PARENT));
        textView.setGravity(Gravity.CENTER);
        textView.setText(getClass().getSimpleName());
        return textView;
    }

}
