package com.bin.content.blocks;

import arc.func.Prov;
import arc.graphics.g2d.TextureRegion;
import com.bin.Tools;
import com.bin.interfaces.LogicStatement;
import mindustry.ctype.ContentType;
import mindustry.ctype.UnlockableContent;
import mindustry.gen.Icon;
import mindustry.ui.Cicon;

/**
 * @author bin
 * @version 1.0.0
 */
public class LogicStatementUnlock extends UnlockableContent {
  public Prov<LogicStatement> statementProv;

  public LogicStatementUnlock(String name) {
    super(name);
  }

  public LogicStatementUnlock(String name, String description, Prov<LogicStatement> statementProv) {
    this(name);
    this.description = description;
    this.statementProv = statementProv;
  }

  @Override public void onUnlock() {
    Tools.addLogicStatement(this.statementProv.get());
  }

  @Override public TextureRegion icon(Cicon c) {
    return Icon.terrain.getRegion();
  }

  @Override public ContentType getContentType() {
    return ContentType.error;
  }
}
