package com.bin.content.logics;

import arc.graphics.Color;
import arc.scene.ui.layout.Table;
import arc.util.Interval;
import com.bin.interfaces.LogicStatement;
import mindustry.content.Items;
import mindustry.gen.Building;
import mindustry.logic.LAssembler;
import mindustry.logic.LExecutor;
import mindustry.logic.LStatement;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.ui.Styles;
import mindustry.world.modules.ItemModule;


/**
 * @author bin
 * @version 1.0.0
 */
public class ItemFactoryStatement extends LStatement implements LogicStatement {
  public static String ID = "ItemFactory";

  public ItemFactory type = ItemFactory.Default;
  public String target = "block1";

  private final Interval timer = new Interval(1);

  public ItemFactoryStatement() {
  }

  public ItemFactoryStatement(String[] s) {
    int i = 1;
    if (s.length > i) {
      this.type = ItemFactory.valueOf(s[i++]);
    }
    if (s.length > i) {
      this.target = s[i];
    }
  }

  @Override public void build(Table table) {
    table.clearChildren();
    table.left();
    table.button(b -> {
      b.label(() -> this.type.name());
      b.clicked(() -> this.showSelect(
          b, ItemFactory.all, this.type, t -> {
            this.type = t;
            this.build(table);
          },
          1, (cell) -> cell.size(140, 50)
      ));
    }, Styles.logict, () -> {
    }).size(140, 40).color(table.color).left().padLeft(2);
    this.row(table);
    table.add(" target: ");
    this.field(table, this.target, (v) -> this.target = v);
  }

  @Override public Color color() {
    return Color.purple;
  }

  private void factory(LExecutor exec, int[] ints) {
    Object obj = exec.obj(ints[0]);
    if (obj instanceof Building) {
      Building building = (Building)obj;
      ItemModule items = building.items;
      if (
          items.get(this.type.output.item) < building.block.itemCapacity &&
          this.timer.get(0, 90)
      ) {
        if (items.has(this.type.input)) {
          items.remove(this.type.input);
          for (int i = 0; i < this.type.output.amount; i++) {
            building.offload(this.type.output.item);
          }
        }
      }
    }
  }

  @Override public LExecutor.LInstruction build(LAssembler builder) {
    return new MyLogicInstruction(
        this::factory,
        builder.var(target)
    );
  }

  @Override public String name() {
    return "处理器工厂";
  }

  @Override public String getId() {
    return ID;
  }

  @Override public ItemFactoryStatement create() {
    return new ItemFactoryStatement();
  }

  @Override public ItemFactoryStatement read(String[] strings) {
    return new ItemFactoryStatement(strings);
  }

  @Override public void write(StringBuilder builder) {
    builder.append(this.getId());
    builder.append(" ").append(this.type.name());
    builder.append(" ").append(this.target);
  }

  private enum ItemFactory {
    /**
     *
     */
    Default(),
    Graphite(Items.graphite, ItemStack.with(Items.coal, 2));

    public static ItemFactory[] all = values();

    public ItemStack output;
    public ItemStack[] input;

    ItemFactory(ItemStack output, ItemStack... input) {
      this.output = output;
      this.input = input;
    }

    ItemFactory() {
      this.output = new ItemStack(Items.copper, 1);
      this.input = new ItemStack[]{this.output};
    }

    ItemFactory(Item output, ItemStack... input) {
      this(new ItemStack(output, 1), input);
    }
  }
}
