package com.content.logics;

import arc.graphics.Color;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import com.interfaces.LogicStatement;
import mindustry.Vars;
import mindustry.ctype.UnlockableContent;
import mindustry.gen.Building;
import mindustry.gen.Icon;
import mindustry.logic.LAssembler;
import mindustry.logic.LExecutor;
import mindustry.logic.LStatement;
import mindustry.type.Item;
import mindustry.ui.Cicon;
import mindustry.ui.Styles;

/**
 * @author bin
 * @version 1.0.0
 */
public class ItemControlStatement extends LStatement implements LogicStatement {
  public static String ID = "ItemControl";
  public String type = "@copper";
  public String target = "block1";
  public String to = "result";
  private transient TextField tField;

  public ItemControlStatement() {
  }

  public ItemControlStatement(String[] s) {
    int i = 1;
    if (s.length > i) {
      this.type = s[i++];
    }
    if (s.length > i) {
      this.target = s[i++];
    }
    if (s.length > i) {
      this.to = s[i];
    }
  }

  @Override public void build(Table table) {
    table.add(" set ");
    this.tField = this.field(table, this.type, (str) -> this.type = str).width(90).get();
    table.button(b -> {
      b.image(Icon.pencilSmall);
      b.clicked(() -> this.showSelectTable(b, (t, hide) -> t.table((ix) ->
          this.buildSelectTable(ix.left(), Vars.content.items(), hide)
      ).colspan(3).width(240.0F).left()));
    }, Styles.logict, () -> {
    }).size(40.0F).color(table.color);
    this.row(table);
    this.fields(table, " of ", this.target, (v) -> this.target = v).width(90);
    this.row(table);
    this.fields(table, " to ", this.to, (v) -> this.to = v).width(90);
  }

  private <T extends UnlockableContent> void buildSelectTable(Table t, Seq<T> items, Runnable hide) {
    int c = 0;
    for (T item : items) {
      if (item.unlockedNow()) {
        t.button(new TextureRegionDrawable(item.icon(Cicon.small)), Styles.cleari, () -> {
          this.setType("@" + item.name);
          hide.run();
        }).size(40.0F);
        ++c;
        if (c % 6 == 0) {
          t.row();
        }
      }
    }
  }

  private void setType(String text) {
    this.tField.setText(text);
    this.type = text;
  }

  @Override public Color color() {
    return Color.purple;
  }

  @Override public LExecutor.LInstruction build(LAssembler builder) {
    return new MyLogicI(builder.var(this.type), builder.var(this.target), builder.var(this.to));
  }

  @Override public String name() {
    return "×ÊÔ´ÐÞ¸ÄÆ÷";
  }

  @Override public String getID() {
    return ID;
  }

  @Override public ItemControlStatement create() {
    return new ItemControlStatement();
  }

  @Override public ItemControlStatement read(String[] strings) {
    return new ItemControlStatement(strings);
  }

  @Override public void write(StringBuilder builder) {
    builder.append(ID);
    builder.append(" ").append(this.type);
    builder.append(" ").append(this.target);
    builder.append(" ").append(this.to);
  }

  public static class MyLogicI implements LExecutor.LInstruction {
    private final int type;
    private final int target;
    private final int to;

    public MyLogicI(int type, int target, int to) {
      this.type = type;
      this.target = target;
      this.to = to;
    }

    @Override public void run(LExecutor exec) {
      Object sense = exec.obj(this.type);
      Object target = exec.obj(this.target);
      if (target instanceof Building && sense instanceof UnlockableContent) {
        int num = exec.numi(this.to);
        if (sense instanceof Item) {
          ((Building)target).items.set(((Item)sense), num);
        }
      }
    }
  }


}
