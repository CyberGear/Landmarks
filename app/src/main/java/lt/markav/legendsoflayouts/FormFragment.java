package lt.markav.legendsoflayouts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class FormFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_form, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentFormLegend legend = new FragmentFormLegend(this);

        new FormFillListener(
                legend.submitButton,
                legend.inputName,
                legend.inputSurname,
                legend.inputAge);
    }

    private static class FormFillListener implements TextWatcher {

        private Button submitButton;
        private EditText[] inputs;

        public FormFillListener(Button submitButton, EditText... inputs) {
            this.submitButton = submitButton;
            this.inputs = inputs;
            for (EditText input : inputs) {
                input.addTextChangedListener(this);
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            boolean isAllFilled = true;
            for (EditText input : inputs) {
                isAllFilled = isAllFilled && !input.getText().toString().isEmpty();
            }
            submitButton.setEnabled(isAllFilled);
        }
    }

}
