/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ezautomator.main;

/**
 *
 * @author akmonius
 */
import java.util.function.Function;

import javafx.scene.control.TableRow;
import javafx.scene.control.Tooltip;

public class TooltipTableRow<T> extends TableRow<T> {

  private final Function<T, String> toolTipStringFunction;

  public TooltipTableRow(Function<T, String> toolTipStringFunction) {
    this.toolTipStringFunction = toolTipStringFunction;
  }

  @Override
  protected void updateItem(T item, boolean empty) {
    super.updateItem(item, empty);
    if(item == null) {
      setTooltip(null);
    } else {
      Tooltip tooltip = new Tooltip(toolTipStringFunction.apply(item));
      setTooltip(tooltip);
    }
  }
}
