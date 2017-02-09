# LoL (Legends of Layouts)

*Version:* ***0.1.0***

 - Creates legend class for all layouts, and resolves all view with ids

---

## Description

Creates legend class for all layouts, and resolves all view with ids. Imagine it's like maps legend.

![description][https://raw.githubusercontent.com/CyberGear/LegendsOfLayouts/master/layout_example.png]

## Including to your project

```Groovy
    repositories {
        maven {
            url "https://github.com/CyberGear/LegendsOfLayouts/raw/master/repo/"
        }
    }
```

```Groovy
    dependencies {
        provided 'lt.markav:legends-of-layouts:{version}'

        apt 'lt.markav:legends-of-layouts-processor:{version}'
    }
```

## Usage Example

Annotation `@LegendsOfLayouts(R.class)` requires to be added once on Application or on MainActivity.

#### Outcome
Lets say you have `activity_main.xml`

```Java
@LegendsOfLayouts(R.class)
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityMainLegend legend = new ActivityMainLegend(this);
    }

}
```

## What it generates

```Java
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public final class FragmentFormLegend {
  public final EditText inputAge;
  public final Button submitButton;
  public final EditText inputName;
  public final EditText inputSurname;

  public FragmentFormLegend(Activity activity) {
    inputAge = (EditText) activity.findViewById(R.id.input_age);
    submitButton = (Button) activity.findViewById(R.id.submitButton);
    inputName = (EditText) activity.findViewById(R.id.input_name);
    inputSurname = (EditText) activity.findViewById(R.id.input_surname);
  }

  public FragmentFormLegend(View view) {
    inputAge = (EditText) view.findViewById(R.id.input_age);
    submitButton = (Button) view.findViewById(R.id.submitButton);
    inputName = (EditText) view.findViewById(R.id.input_name);
    inputSurname = (EditText) view.findViewById(R.id.input_surname);
  }

  public FragmentFormLegend(Fragment fragment) {
    this(fragment.getView());
  }
}
```
