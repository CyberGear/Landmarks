# LoL (Legends of Layouts)

*Version:* ***0.1.0***

 - feature
 - feature
 - feature

---

## Description

Description

## Including to your project

No way yet

## Usage Example

add later

## What it generates

```Java
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;

public final class FragmentFormLegend {
  public final EditText age;
  public final EditText surname;
  public final EditText name;

  public FragmentFormLegend(Activity activity) {
    age = (EditText) activity.findViewById(R.id.age);
    surname = (EditText) activity.findViewById(R.id.surname);
    name = (EditText) activity.findViewById(R.id.name);
  }

  public FragmentFormLegend(View view) {
    age = (EditText) view.findViewById(R.id.age);
    surname = (EditText) view.findViewById(R.id.surname);
    name = (EditText) view.findViewById(R.id.name);
  }

  public FragmentFormLegend(Fragment fragment) {
    this(fragment.getView());
  }
}
```
