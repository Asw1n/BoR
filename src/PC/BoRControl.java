package PC;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.List;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiDevice.Info;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JToolBar;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.JTabbedPane;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JFileChooser;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JTextField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.SpringLayout;
import java.awt.GridLayout;
import javax.swing.JTable;
import javax.swing.JScrollPane;

public class BoRControl extends JFrame {

  private JPanel contentPane;
  private JTextField MidiFile;
//Create a file chooser
  final JFileChooser fc = new JFileChooser();
  private File sequenceFile;
  private JTable table;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Throwable e) {
      e.printStackTrace();
    }
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          BoRControl frame = new BoRControl();
          frame.setVisible(true);
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }
  
  

  /**
   * Create the frame.
   */
  public BoRControl() {
    setTitle("Band of Robots");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 696, 553);
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);

    
    JToolBar toolBar = new JToolBar();
    
    
    JButton btnPlay = new JButton("");
    btnPlay.setIcon(new ImageIcon(BoRControl.class.getResource("/icons/play-icon.png")));
    toolBar.add(btnPlay);
    
    JButton btnNewButton = new JButton("");
    btnNewButton.setIcon(new ImageIcon(BoRControl.class.getResource("/icons/pause-icon.png")));
    toolBar.add(btnNewButton);
    
    JButton btnTest = new JButton("");
    btnTest.setIcon(new ImageIcon(BoRControl.class.getResource("/icons/stop-icon.png")));
    btnTest.setSelectedIcon(new ImageIcon(BoRControl.class.getResource("/icons/stop-icon.png")));
    toolBar.add(btnTest);
        
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        
        JPanel panel =  new JPanel();
        tabbedPane.addTab("MIDI", null, panel, null);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[]{127, 391, 28, 28, 0};
        gbl_panel.rowHeights = new int[]{0, 20, 0, 0};
        gbl_panel.columnWeights = new double[]{1.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
        panel.setLayout(gbl_panel);
        
        JLabel lblFile = new JLabel("File");
        GridBagConstraints gbc_lblFile = new GridBagConstraints();
        gbc_lblFile.insets = new Insets(0, 0, 5, 5);
        gbc_lblFile.anchor = GridBagConstraints.WEST;
        gbc_lblFile.gridx = 0;
        gbc_lblFile.gridy = 0;
        panel.add(lblFile, gbc_lblFile);
        
        MidiFile = new JTextField();
        GridBagConstraints gbc_MidiFile = new GridBagConstraints();
        gbc_MidiFile.insets = new Insets(0, 0, 5, 5);
        gbc_MidiFile.fill = GridBagConstraints.HORIZONTAL;
        gbc_MidiFile.gridx = 1;
        gbc_MidiFile.gridy = 0;
        panel.add(MidiFile, gbc_MidiFile);
        MidiFile.setColumns(10);
        
        JButton btnFile = new JButton("File");
        btnFile.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent arg0) {
            {
               if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                sequenceFile=fc.getSelectedFile();
                  MidiFile.setText(sequenceFile.getAbsolutePath());
            }
            }
          }
        });
        
            GridBagConstraints gbc_btnFile = new GridBagConstraints();
            gbc_btnFile.insets = new Insets(0, 0, 5, 5);
            gbc_btnFile.gridx = 2;
            gbc_btnFile.gridy = 0;
            panel.add(btnFile, gbc_btnFile);
            
            JLabel lblSequencer = new JLabel("sequencer");
            GridBagConstraints gbc_lblSequencer = new GridBagConstraints();
            gbc_lblSequencer.anchor = GridBagConstraints.WEST;
            gbc_lblSequencer.insets = new Insets(0, 0, 5, 5);
            gbc_lblSequencer.gridx = 0;
            gbc_lblSequencer.gridy = 1;
            panel.add(lblSequencer, gbc_lblSequencer);
            
            JComboBox<Info> selectSequencer = new JComboBox<Info>(new ComboSequencers());
            GridBagConstraints gbc_selectSequencer = new GridBagConstraints();
            gbc_selectSequencer.fill = GridBagConstraints.HORIZONTAL;
            gbc_selectSequencer.anchor = GridBagConstraints.NORTH;
            gbc_selectSequencer.insets = new Insets(0, 0, 5, 5);
            gbc_selectSequencer.gridx = 1;
            gbc_selectSequencer.gridy = 1;
            panel.add(selectSequencer, gbc_selectSequencer);

            
            JLabel lblSynthesizer = new JLabel("synthesizer");
            GridBagConstraints gbc_lblSynthesizer = new GridBagConstraints();
            gbc_lblSynthesizer.anchor = GridBagConstraints.WEST;
            gbc_lblSynthesizer.insets = new Insets(0, 0, 0, 5);
            gbc_lblSynthesizer.gridx = 0;
            gbc_lblSynthesizer.gridy = 2;
            panel.add(lblSynthesizer, gbc_lblSynthesizer);
            
            JComboBox<Info> SelectSynthesizer = new JComboBox<Info>(new ComboSynthesizers());
            GridBagConstraints gbc_SelectSynthesizer = new GridBagConstraints();
            gbc_SelectSynthesizer.fill = GridBagConstraints.HORIZONTAL;
            gbc_SelectSynthesizer.insets = new Insets(0, 0, 0, 5);
            gbc_SelectSynthesizer.anchor = GridBagConstraints.NORTH;
            gbc_SelectSynthesizer.gridx = 1;
            gbc_SelectSynthesizer.gridy = 2;
            panel.add(SelectSynthesizer, gbc_SelectSynthesizer);
        
        JPanel panel_1 = new JPanel();
        tabbedPane.addTab("Instruments", null, panel_1, null);
        panel_1.setLayout(new FormLayout(new ColumnSpec[] {
            FormFactory.RELATED_GAP_COLSPEC,
            ColumnSpec.decode("default:grow"),},
          new RowSpec[] {
            FormFactory.RELATED_GAP_ROWSPEC,
            RowSpec.decode("default:grow"),}));
        
        table = new JTable(new InstrumentTableModel());
        panel_1.add(table, "2, 2, fill, fill");
            GroupLayout gl_contentPane = new GroupLayout(contentPane);
            gl_contentPane.setHorizontalGroup(
              gl_contentPane.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_contentPane.createSequentialGroup()
                  .addGap(5)
                  .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                    .addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 665, Short.MAX_VALUE)
                    .addGroup(gl_contentPane.createSequentialGroup()
                      .addComponent(toolBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                      .addGap(415))))
            );
            gl_contentPane.setVerticalGroup(
              gl_contentPane.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_contentPane.createSequentialGroup()
                  .addGap(5)
                  .addComponent(toolBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                  .addGap(18)
                  .addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 408, GroupLayout.PREFERRED_SIZE)
                  .addGap(19))
            );
            contentPane.setLayout(gl_contentPane);
    

  }
}
