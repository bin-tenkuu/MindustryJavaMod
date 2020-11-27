package com.ui;

import arc.scene.ui.Label;
import arc.scene.ui.ScrollPane;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import mindustry.gen.Icon;
import mindustry.ui.dialogs.BaseDialog;

/**
 * @author bin
 * @version 1.0.0
 */
public class HelpDialog extends BaseDialog {

  public HelpDialog(String title) {
    super(title);
    this.shouldPause = true;
    this.buttons.defaults().size(160.0F, 64.0F);
    this.buttons.button("@back", Icon.left, this::hide).name("back");
  }

  @Override public Cell<Label> add(CharSequence text) {
    return super.add(text);
  }
}
