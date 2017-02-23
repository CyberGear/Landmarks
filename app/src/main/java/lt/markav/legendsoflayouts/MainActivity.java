package lt.markav.legendsoflayouts;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;

import lt.markav.legendsoflayouts.annotation.FragmentsType;
import lt.markav.legendsoflayouts.annotation.LegendsOfLayouts;

@LegendsOfLayouts(R.class)
public class MainActivity extends AppCompatActivity {

    private String field0;
    String field1;
    public String field2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        ActivityMainLegend legend = new ActivityMainLegend(this);

//        setSupportActionBar(legend.toolbar);
//        legend.viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
//            @Override
//            public Fragment getItem(int position) {
//                switch (position) {
//                    case 0: return new FormFragment();
//                    case 1: return new ListFragment();
//                }
//                return null;
//            }
//
//            @Override
//            public int getCount() {
//                return 2;
//            }
//        });
    }

}
