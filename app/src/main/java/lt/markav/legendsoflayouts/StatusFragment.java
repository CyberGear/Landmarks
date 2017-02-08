package lt.markav.legendsoflayouts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class StatusFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_status, container, false);
        final FragmentStatusLegend legend = new FragmentStatusLegend(view);

        legend.firstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                legend.firstButton.setEnabled(false);
                legend.secondButton.setEnabled(true);
            }
        });
        legend.secondButton.setEnabled(false);
        legend.secondButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                legend.secondButton.setEnabled(false);
                legend.firstButton.setEnabled(true);
            }
        });

        return view;
    }

}
