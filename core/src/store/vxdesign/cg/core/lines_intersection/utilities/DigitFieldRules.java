package store.vxdesign.cg.core.lines_intersection.utilities;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import java.util.stream.Stream;

public class DigitFieldRules {
    private static class DigitFieldFilter implements TextField.TextFieldFilter {
        private static final Character[] CHARS = new Character[]{'-', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.'};

        @Override
        public boolean acceptChar(TextField textField, char c) {
            return Stream.of(CHARS).anyMatch(symbol -> symbol == c) &&
                    !((c == '.' && textField.getText().chars().filter(symbol -> symbol == '.').count() > 0) ||
                            (c == '-' && textField.getText().chars().filter(symbol -> symbol == '-').count() > 0));
        }
    }

    public static final DigitFieldFilter DIGIT_FIELD_FILTER = new DigitFieldFilter();
    public static final TextField.TextFieldListener DIGIT_FIELD_LISTENER = (textField, symbol) ->
            textField.setText(textField.getText()
                    .replaceAll("^(-?)$", "$10")
                    .replaceAll("^(\\d*)(\\.?)([-])?(\\.?)(\\d*)([-])?$", "$3$6$1$2$4$5"));
}
