package org.vaadin.tatu;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.NativeButton;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;
import java.util.Collections;
import java.util.stream.Stream;

@Route("")
public class View extends Div {

  public View() {
    DepartmentData departmentData = new DepartmentData();
    TextArea message = new TextArea("");
    message.setHeight("100px");
    message.setReadOnly(true);

    // begin-source-example
    // source-example-heading: TreeGrid Basics
    Tree<Department> tree = new Tree<>(Department::getName);

    tree.setItems(departmentData.getRootDepartments(),
        departmentData::getChildDepartments);

    tree.setItemIconProvider(item -> getIcon(item));
    tree.setItemTitleProvider(Department::getManager);

    tree.addExpandListener(event -> message.setValue(
        String.format("Expanded %s item(s)", event.getItems().size())
            + "\n" + message.getValue()));
    tree.addCollapseListener(event -> message.setValue(
        String.format("Collapsed %s item(s)", event.getItems().size())
            + "\n" + message.getValue()));

    tree.asSingleSelect().addValueChangeListener(event -> {
      if (event.getValue() != null) {
        System.out.println(event.getValue().getName() + " selected");
      }
    });

    // end-source-example
    tree.setId("treegridbasic");
    tree.setHeightByRows(true);
    setSizeFull();
    add(withTreeToggleButtons(
        departmentData.getRootDepartments().get(0), tree, message));
  }

  private VaadinIcon getIcon(Department item) {
    if (item.getParent() == null) {
      return VaadinIcon.BUILDING;
    } else {
      return VaadinIcon.USER;
    }
  }

  private <T> Component[] withTreeToggleButtons(T root, Tree<T> tree,
      Component... other) {
    NativeButton toggleFirstItem = new NativeButton("Toggle first item",
        evt -> {
          if (tree.isExpanded(root)) {
            tree.collapseRecursively(Collections.singleton(root),
                0);
          } else {
            tree.expandRecursively(Collections.singleton(root), 0);
          }
        });
    toggleFirstItem.setId("treegrid-toggle-first-item");
    Div div1 = new Div(toggleFirstItem);

    NativeButton toggleRecursivelyFirstItem = new NativeButton(
        "Toggle first item recursively", evt -> {
      if (tree.isExpanded(root)) {
        tree.collapseRecursively(Collections.singleton(root),
            2);
      } else {
        tree.expandRecursively(Collections.singleton(root), 2);
      }
    });
    toggleFirstItem.setId("treegrid-toggle-first-item-recur");
    Div div3 = new Div(toggleRecursivelyFirstItem);

    return Stream.concat(Stream.of(tree, div1, div3), Stream.of(other))
        .toArray(Component[]::new);
  }

}
