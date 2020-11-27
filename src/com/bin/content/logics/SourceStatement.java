package com.bin.content.logics;

import arc.graphics.Color;
import arc.math.Mathf;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Table;
import com.bin.Tools;
import com.bin.interfaces.LogicStatement;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.gen.Icon;
import mindustry.logic.LAssembler;
import mindustry.logic.LExecutor;
import mindustry.logic.LStatement;
import mindustry.logic.Ranged;
import mindustry.type.Item;
import mindustry.type.Liquid;
import mindustry.ui.Styles;


/**
 * @author bin
 * @version 1.0.0
 */
public class SourceStatement extends LStatement implements LogicStatement {
  public static String ID = "Source";

  public SourceType type = SourceType.addItem;
  public String target = "block1";
  public String itemType="@copper";

  private transient TextField tField;

  public SourceStatement() {
  }

  public SourceStatement(String[] s) {
    int i = 1;
    if (s.length > i) {
      this.type = SourceType.valueOf(s[i++]);
    }
    if (s.length > i) {
      this.target = s[i++];
    }
    if (s.length > i) {
      this.itemType = s[i];
    }
  }

  @Override public void build(Table table) {
    table.clearChildren();
    table.left();
    table.button(b -> {
      b.label(() -> this.type.name());
      b.clicked(() -> this.showSelect(
          b, SourceType.all, this.type, t -> {
            this.type = t;
            if (t == SourceType.addItem) {
              this.setType("@copper");
            } else if (t == SourceType.addLiquid) {
              this.setType("@water");
            }
            this.build(table);
          },
          1, (cell) -> cell.size(140, 50)
      ));
    }, Styles.logict, () -> {
    }).size(140, 40).color(table.color).left().padLeft(2);
    this.row(table);

    table.add(" to ");
    this.field(table, this.target, (v) -> this.target = v);
    if (this.type != SourceType.addPower) {
      this.row(table);
      table.add(" with ");
      this.tField = this.field(table, this.itemType, s -> this.itemType = s).get();
      table.button(b -> {
        b.image(Icon.pencilSmall);
        b.clicked(() -> this.showSelectTable(b, (t, hide) -> t.table((ix) -> {
          if (this.type == SourceType.addItem) {
            Tools.buildItemImageSelectTable(ix.left(), Vars.content.items(), 6, item -> {
              this.setType("@" + item.name);
              hide.run();
            });
          } else if (this.type != SourceType.addLiquid) {
            Tools.buildItemImageSelectTable(ix.left(), Vars.content.liquids(), 6, item -> {
              this.setType("@" + item.name);
              hide.run();
            });
          }
        }).colspan(3).width(240).left()));
      }, Styles.logict, () -> {
      }).size(40).padLeft(-1).color(table.color);
    }
  }

  private void setType(String text) {
    this.tField.setText(text);
    this.itemType = text;
  }

  private void addItem(LExecutor exec, int[] ints) {
    Building thisBuild = (Building)exec.obj(3);
    Object target = exec.obj(ints[0]);
    Object sense = exec.obj(ints[1]);
    if (target instanceof Building && sense instanceof Item) {
      Building b = (Building)target;
      Item item = (Item)sense;
      if (b.acceptItem(thisBuild, item)) {
        b.handleItem(thisBuild, item);
      }
    }
  }

  private void addLiquid(LExecutor exec, int[] ints) {
    Building thisBuild = (Building)exec.obj(3);
    Object sense = exec.obj(ints[0]);
    Object target = exec.obj(ints[1]);
    if (target instanceof Building && sense instanceof Liquid) {
      Building b = (Building)target;
      Liquid liquid = (Liquid)sense;
      if (b.acceptLiquid(thisBuild, liquid)) {
        b.handleLiquid(thisBuild, liquid, 0.1F);
      }
    }
  }

  private void addPower(LExecutor exec, int[] ints) {
    Building thisBlock = (Building)exec.obj(3);
    Object target = exec.obj(ints[0]);
    if (thisBlock instanceof Ranged && target instanceof Building) {
      Building b = (Building)target;
      if (b.block.consumes.hasPower() && b.power.status < 1) {
        float amount = ((Ranged)thisBlock).range() / b.block.consumes.getPower().capacity;
        b.power.status = Mathf.clamp(amount + b.power.status);
      }
    }
  }

  @Override public Color color() {
    return Color.purple;
  }

  @Override public LExecutor.LInstruction build(LAssembler builder) {
    switch (this.type) {
      case addItem:
        return new MyLogicInstruction(
            this::addItem,
            builder.var(this.target),
            builder.var(this.itemType)
        );
      case addLiquid:
        return new MyLogicInstruction(
            this::addLiquid,
            builder.var(this.target),
            builder.var(this.itemType)
        );
      case addPower:
        return new MyLogicInstruction(
            this::addPower,
            builder.var(this.target)
        );
      default:
        return new LExecutor.NoopI();
    }
  }

  @Override public String name() {
    return "´¦ÀíÆ÷Ô´";
  }

  @Override public String getId() {
    return ID;
  }

  @Override public SourceStatement create() {
    return new SourceStatement();
  }

  @Override public SourceStatement read(String[] strings) {
    return new SourceStatement(strings);
  }

  @Override public void write(StringBuilder builder) {
    builder.append(this.getId());
    builder.append(" ").append(this.type);
    builder.append(" ").append(this.target);
    builder.append(" ").append(this.itemType);
  }


  private enum SourceType {
    /**
     *
     */
    addItem,
    addLiquid,
    addPower;

    public static SourceType[] all = values();
  }
}
