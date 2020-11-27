package com;

import arc.func.Cons;
import arc.func.Prov;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.ButtonGroup;
import arc.scene.ui.ImageButton;
import arc.scene.ui.ScrollPane;
import arc.scene.ui.layout.Scl;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import com.interfaces.LogicStatement;
import mindustry.Vars;
import mindustry.ctype.UnlockableContent;
import mindustry.gen.LogicIO;
import mindustry.logic.LAssembler;
import mindustry.ui.Cicon;
import mindustry.ui.Styles;

/**
 * @author bin
 */
public final class Tools {
  public static String ModName = "��������mod";


  public static <T extends UnlockableContent> void buildItemSelectTable(
      Table table, Seq<T> items, Prov<T> holder, Cons<T> changed
  ) {
    Tools.buildItemSelectTable(table, items, holder, changed, true);
  }

  /**
   * �Զ�����ѡ���
   *
   * @param table ��
   * @param items ��Ʒ����
   * @param holder ��Ҫ���ص�ǰѡ�����Ը���
   * @param changed �ı�ʱ����
   * @param closeSelected ѡ����Զ��ر��б�
   * @param <T> �ɽ�������
   */
  public static <T extends UnlockableContent> void buildItemSelectTable(
      Table table, Seq<T> items, Prov<T> holder, Cons<T> changed, boolean closeSelected
  ) {
    ButtonGroup<ImageButton> group = new ButtonGroup<>();
    group.setMinCheckCount(0);
    Table cont = new Table();
    cont.defaults().size(40.0F);
    int i = 0;

    for (T t : items) {
      if (t.unlockedNow()) {
        ImageButton button = cont.button(
            new TextureRegionDrawable(t.icon(Cicon.small)),
            Styles.clearToggleTransi,
            24.0F, () -> {
              if (closeSelected) {
                Vars.control.input.frag.config.hideConfig();
              }
            }
        ).group(group).tooltip(t.localizedName).get();
        button.changed(() -> changed.get(button.isChecked() ? t : null));
        button.update(() -> button.setChecked(holder.get() == t));
        if (i++ >= 3) {
          cont.row();
          i = 0;
        }
      }
    }

    for (; i < 4; ++i) {
      cont.image(Styles.black6);
    }

    ScrollPane pane = new ScrollPane(cont, Styles.smallPane);
    pane.setScrollingDisabled(true, false);
    pane.setOverscroll(false, false);
    table.add(pane).maxHeight(Scl.scl(200.0F));
  }

  public static <T extends UnlockableContent> void buildItemImageSelectTable(
      Table t, Seq<T> items, int rowItems, Cons<T> click
  ) {
    int c = 0;
    for (T item : items) {
      if (item.unlockedNow()) {
        t.button(new TextureRegionDrawable(item.icon(Cicon.small)), Styles.cleari, () -> click.get(item)).size(40.0F);

        if (++c % rowItems == 0) {
          t.row();
        }
      }
    }
  }

  public static void addLogicStatement(LogicStatement statement) {
    LogicIO.allStatements.add(statement::create);
    LAssembler.customParsers.put(statement.getId(), statement::read);
  }
}
