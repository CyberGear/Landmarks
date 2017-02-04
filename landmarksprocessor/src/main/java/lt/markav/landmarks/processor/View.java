package lt.markav.landmarks.processor;

import org.w3c.dom.Element;

import java.util.Arrays;
import java.util.Objects;

public class View {

    private String type;
    private String id = "";
    private String layout = "";
    private String tag = "";

    public View(Element element) {
        type = element.getTagName();
        id = element.getAttribute("android:id");
        layout = element.getAttribute("layout");
        tag = element.getAttribute("android:tag");
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isUseful() {
        return !(id + layout + tag).isEmpty() && !layout.startsWith("@android:layout/");
    }

    public View prepare() {
        if (!id.isEmpty()) {
            id = id.replace("@+id/", "R.id.")
                    .replace("@id/", "R.id.")
                    .replace("@android:id/", "android.R.id.");
        }
        if (!layout.isEmpty()) {
            layout = layout.replace("@layout/", "") + ".xml";
        }
        if (!tag.isEmpty()) {
            tag = tag.replace("@string/", "R.string.");
        }
        return this;
    }

    public String getName() {
        return id.replace("android.R.id.", "").replace("R.id.", "");
    }

    @Override
    public String toString() {
        return type + "{ " + id + layout + tag + " }";
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        String other = "" + obj;
        return toString().equals(other);
    }
}
