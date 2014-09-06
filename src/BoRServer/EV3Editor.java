package BoRServer;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class EV3Editor extends AbstractCellEditor implements TableCellEditor, ActionListener {

    /**
     * 
     */
    private static final long serialVersionUID = 4302822754971536777L;
    List<Brick>                   selectedBricks;
    JButton                       button;
    BrickSelector                 brickSelector;
    JDialog                       dialog;
    protected static final String EDIT = "edit";

    public EV3Editor() {
        button = new JButton();
        button.setActionCommand(EDIT);
        button.addActionListener(this);
        button.setBorderPainted(false);

        // Set up the dialog that the button brings up.
        brickSelector = new BrickSelector();
    }

    public void actionPerformed(ActionEvent e) {
        if (EDIT.equals(e.getActionCommand())) {
            // The user has clicked the cell, so
            // bring up the dialog.
            brickSelector.setSelectedBricks(selectedBricks);
            brickSelector.setVisible(true);

            fireEditingStopped(); // Make the renderer reappear.

        }
        else { // User pressed dialog's "OK" button.
            selectedBricks = brickSelector.getSelectedBricks();
        }
    }

    // Implement the one CellEditor method that AbstractCellEditor doesn't.
    public Object getCellEditorValue() {
        return selectedBricks;
    }

    // Implement the one method defined by TableCellEditor.
    @SuppressWarnings("unchecked")
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        selectedBricks =  (List<Brick>) value;
        return button;
    }

}
