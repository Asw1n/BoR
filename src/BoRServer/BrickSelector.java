package BoRServer;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.SwingConstants;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.List;

public class BrickSelector extends JDialog {

    /**
     * 
     */
    private static final long serialVersionUID = -7036780725072760676L;
    private final JPanel contentPanel = new JPanel();
    private JTable table;
    static BrickTableModel brickTableModel = new BrickTableModel();

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        try {
            BrickSelector dialog = new BrickSelector();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            dialog.setVisible(true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the dialog.
     */
    public BrickSelector() {
        setBounds(100, 100, 450, 300);
        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        GridBagLayout gbl_contentPanel = new GridBagLayout();
        gbl_contentPanel.columnWidths = new int[]{0, 0};
        gbl_contentPanel.rowHeights = new int[]{0, 0};
        gbl_contentPanel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gbl_contentPanel.rowWeights = new double[]{1.0, Double.MIN_VALUE};
        contentPanel.setLayout(gbl_contentPanel);
        {
            JScrollPane scrollPane = new JScrollPane();
            GridBagConstraints gbc_scrollPane = new GridBagConstraints();
            gbc_scrollPane.fill = GridBagConstraints.BOTH;
            gbc_scrollPane.gridx = 0;
            gbc_scrollPane.gridy = 0;
            contentPanel.add(scrollPane, gbc_scrollPane);
            {
                table = new JTable();
                table.setModel(brickTableModel);
                scrollPane.setViewportView(table);
            }
        }
        {
            JPanel buttonPane = new JPanel();
            buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
            getContentPane().add(buttonPane, BorderLayout.SOUTH);
            {
                JButton btnSearch = new JButton("Search");
                btnSearch.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent arg0) {
                        ((BrickTableModel)table.getModel()).resfresh();
                    }
                });
                btnSearch.setHorizontalAlignment(SwingConstants.LEFT);
                buttonPane.add(btnSearch);
            }
            {
                JButton okButton = new JButton("OK");
                okButton.setActionCommand("OK");
                buttonPane.add(okButton);
                getRootPane().setDefaultButton(okButton);
            }
        }
    }

    protected JTable getTable() {
        return table;
    }

    public void setSelectedBricks(List<Brick> selectedBricks) {
        brickTableModel.setSelectedBricks(selectedBricks);
        
    }

    public List<Brick> getSelectedBricks() {
        return brickTableModel.getSelectedBricks();
    }
}
