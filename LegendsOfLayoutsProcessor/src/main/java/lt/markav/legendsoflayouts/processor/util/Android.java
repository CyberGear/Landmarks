package lt.markav.legendsoflayouts.processor.util;

import com.squareup.javapoet.ClassName;

import java.util.HashMap;
import java.util.Map;

public final class Android {

    private static Map<String, ClassName> views = new HashMap<>();

    static {
        put("android.widget", "AbsListView");
        put("android.widget", "AbsoluteLayout");
        put("android.widget", "AbsSeekBar");
        put("android.widget", "AbsSpinner");
        put("android.widget", "ActionMenuView");
        put("android.widget", "Adapter");
        put("android.widget", "AdapterView");
        put("android.widget", "AdapterViewAnimator");
        put("android.widget", "AdapterViewFlipper");
        put("android.widget", "Advanceable");
        put("android.widget", "AlphabetIndexer");
        put("android.widget", "AnalogClock");
        put("android.widget", "ArrayAdapter");
        put("android.widget", "AutoCompleteTextView");
        put("android.widget", "BaseAdapter");
        put("android.widget", "BaseExpandableListAdapter");
        put("android.widget", "Button");
        put("android.widget", "CalendarView");
        put("android.widget", "Checkable");
        put("android.widget", "CheckBox");
        put("android.widget", "CheckedTextView");
        put("android.widget", "Chronometer");
        put("android.widget", "CompoundButton");
        put("android.widget", "CursorAdapter");
        put("android.widget", "CursorTreeAdapter");
        put("android.widget", "DatePicker");
        put("android.widget", "DialerFilter");
        put("android.widget", "DigitalClock");
        put("android.widget", "EdgeEffect");
        put("android.widget", "EditText");
        put("android.widget", "ExpandableListAdapter");
        put("android.widget", "ExpandableListView");
        put("android.widget", "Filter");
        put("android.widget", "Filterable");
        put("android.widget", "FilterQueryProvider");
        put("android.widget", "FrameLayout");
        put("android.widget", "Gallery");
        put("android.widget", "GridLayout");
        put("android.widget", "GridView");
        put("android.widget", "HeaderViewListAdapter");
        put("android.widget", "HeterogeneousExpandableList");
        put("android.widget", "HorizontalScrollView");
        put("android.widget", "ImageButton");
        put("android.widget", "ImageSwitcher");
        put("android.widget", "ImageView");
        put("android.widget", "LinearLayout");
        put("android.widget", "ListAdapter");
        put("android.widget", "ListPopupWindow");
        put("android.widget", "ListView");
        put("android.widget", "MediaController");
        put("android.widget", "MultiAutoCompleteTextView");
        put("android.widget", "NumberPicker");
        put("android.widget", "OverScroller");
        put("android.widget", "PopupMenu");
        put("android.widget", "PopupWindow");
        put("android.widget", "ProgressBar");
        put("android.widget", "QuickContactBadge");
        put("android.widget", "RadioButton");
        put("android.widget", "RadioGroup");
        put("android.widget", "RatingBar");
        put("android.widget", "RelativeLayout");
        put("android.widget", "RemoteViews");
        put("android.widget", "RemoteViewsService");
        put("android.widget", "ResourceCursorAdapter");
        put("android.widget", "ResourceCursorTreeAdapter");
        put("android.widget", "Scroller");
        put("android.widget", "ScrollView");
        put("android.widget", "SearchView");
        put("android.widget", "SectionIndexer");
        put("android.widget", "SeekBar");
        put("android.widget", "ShareActionProvider");
        put("android.widget", "SimpleAdapter");
        put("android.widget", "SimpleCursorAdapter");
        put("android.widget", "SimpleCursorTreeAdapter");
        put("android.widget", "SimpleExpandableListAdapter");
        put("android.widget", "SlidingDrawer");
        put("android.widget", "Space");
        put("android.widget", "Spinner");
        put("android.widget", "SpinnerAdapter");
        put("android.widget", "StackView");
        put("android.widget", "Switch");
        put("android.widget", "TabHost");
        put("android.widget", "TableLayout");
        put("android.widget", "TableRow");
        put("android.widget", "TabWidget");
        put("android.widget", "TextClock");
        put("android.widget", "TextSwitcher");
        put("android.widget", "TextView");
        put("android.widget", "ThemedSpinnerAdapter");
        put("android.widget", "TimePicker");
        put("android.widget", "Toast");
        put("android.widget", "ToggleButton");
        put("android.widget", "Toolbar");
        put("android.widget", "TwoLineListItem");
        put("android.widget", "VideoView");
        put("android.widget", "ViewAnimator");
        put("android.widget", "ViewFlipper");
        put("android.widget", "ViewSwitcher");
        put("android.widget", "WrapperListAdapter");
        put("android.widget", "ZoomButton");
        put("android.widget", "ZoomButtonsController");
        put("android.widget", "ZoomControls");

        put("android.view", "SurfaceView");
        put("android.view", "SurfaceView");
        put("android.view", "TextureView");
        put("android.view", "View");
        put("android.view", "ViewGroup");
    }

    public static final ClassName ACTIVITY = ClassName.get("android.app", "Activity");
    public static final ClassName VIEW = views.get("View");

    private Android() {
        //prevent creation
    }

    private static void put(String packageName, String className) {
        views.put(className, ClassName.get(packageName, className));
    }

    public static ClassName getClassName(String name) {
        return views.get(name);
    }
}
