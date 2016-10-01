package drast.views.gui.guicomponent.inputfields;

import javafx.scene.control.TextField;

/**
 * Created by gda10jth on 11/27/15.
 * <p>
 * An TextField that only accepts Integer numbers.
 */
public class NumberField extends TextField {
  /**
   * When text is added to the field, this method is called.
   * It checks if the field text follows a Integer number (e.g. 5), and ignore the new changes otherwise.
   */
  @Override public void replaceText(int start, int end, String text) {
    String old = this.getText();
    super.replaceText(start, end, text);
    if (!this.getText().matches("-?[0-9]*")) {
      this.setText(old);
      positionCaret(start);
      if (end != start) {
        selectPositionCaret(end);
      }
    }
  }

  @Override public void replaceSelection(String text) {
    String old = this.getText();
    super.replaceSelection(text);
    if (!this.getText().matches("-?[0-9]*")) {
      this.setText(old);
    }
  }
}
