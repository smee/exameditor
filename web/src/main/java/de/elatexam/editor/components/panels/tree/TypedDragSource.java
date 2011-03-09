package de.elatexam.editor.components.panels.tree;

import wicketdnd.DragSource;
import wicketdnd.Operation;

/**
   * Preconfigured {@link DragSource} that starts for one subtaskdef type only.
   *
   * @author Steffen Dienst
   *
   */
  class TypedDragSource extends DragSource {

    private final String[] types;

    public TypedDragSource(Class<?> type, Operation... operations) {
      super(operations);
      this.types = new String[] { ComplexTaskDefTree.tranferTypes.get(type) };
      drag(ComplexTaskDefTree.dragStarts.get(type));
    }

    @Override
    public String[] getTypes() {
      return types;
    }
  }