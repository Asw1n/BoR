package PC;

import java.awt.EventQueue;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.Sequencer;
import javax.sound.midi.Synthesizer;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JToolBar;
import javax.swing.ImageIcon;
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

import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


/** IMTableEditor lets you edit and maintain the InstrumentMusician Mapping table;
 * 
 * @author Aswin
 *
 */
public class BoR extends JFrame {

  private BoRController myController=new BoRController();

  private static final long serialVersionUID = 6318810593044644641L;
  private JPanel contentPane;
  private JTextField MidiFile;
//Create a file chooser
  final JFileChooser fc = new JFileChooser(new File("C:/Users/Aswin/git/BoR/MIDI"));
  private File sequenceFile;
  private JTable table;

  private SongTableModel songModel = new SongTableModel();
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
          BoR frame = new BoR();
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
  public BoR() {
    setTitle("Band of Robots");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 696, 553);
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);

    
    JToolBar toolBar = new JToolBar();
    
    
    JButton btnPlay = new JButton("");
    btnPlay.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent arg0) {
        myController.open();
        myController.play();
      }
    });
    btnPlay.setIcon(new ImageIcon(BoR.class.getResource("/icons/play-icon.png")));
    toolBar.add(btnPlay);
    
    JButton btnNewButton = new JButton("");
    btnNewButton.setIcon(new ImageIcon(BoR.class.getResource("/icons/pause-icon.png")));
    toolBar.add(btnNewButton);
    
    JButton btnTest = new JButton("");
    btnTest.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent arg0) {
        myController.stop();
        myController.close();
      }
    });
    btnTest.setIcon(new ImageIcon(BoR.class.getResource("/icons/stop-icon.png")));
    btnTest.setSelectedIcon(new ImageIcon(BoR.class.getResource("/icons/stop-icon.png")));
    toolBar.add(btnTest);
            
            JLabel lblFile = new JLabel("File");
            
            MidiFile = new JTextField();
            MidiFile.setColumns(10);
            
            JButton btnFile = new JButton("");
            btnFile.setIcon(fc.getIcon(new File("Test.mid")));
            btnFile.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent arg0) {
                {
                   if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    sequenceFile=fc.getSelectedFile();
                      MidiFile.setText(sequenceFile.getAbsolutePath());
                      songModel.setFile(sequenceFile);
                      myController.setSong(songModel.getSong());
                }
                }
              }
            });
            
            JLabel lblSequencer = new JLabel("Sequencer");
            
            JComboBox<MidiDevice.Info> selectSequencer = new JComboBox<MidiDevice.Info>(new ComboSequencers());
            myController.setSequencer((MidiDevice.Info) selectSequencer.getSelectedItem());
            selectSequencer.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent arg0) {
                myController.setSequencer((MidiDevice.Info) ((JComboBox)arg0.getSource()).getSelectedItem());
              }
            });
            
                        
                        JLabel lblSynthesizer = new JLabel("Synthesizer");
            
            JComboBox<MidiDevice.Info> SelectSynthesizer = new JComboBox<MidiDevice.Info>(new ComboSynthesizers());
            myController.setSynthesizer((MidiDevice.Info) SelectSynthesizer.getSelectedItem());
            selectSequencer.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent arg0) {
                myController.setSynthesizer((MidiDevice.Info) ((JComboBox)arg0.getSource()).getSelectedItem());
              }
            });
            
            JPanel panel_1 = new JPanel();
            panel_1.setLayout(new FormLayout(new ColumnSpec[] {
                FormFactory.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("default:grow"),
                FormFactory.RELATED_GAP_COLSPEC,
                ColumnSpec.decode("default:grow"),},
              new RowSpec[] {
                FormFactory.RELATED_GAP_ROWSPEC,
                RowSpec.decode("default:grow"),}));
            
            JScrollPane scrollPane = new JScrollPane();
            panel_1.add(scrollPane, "2, 2, 3, 1, fill, fill");
            
            table = new JTable(songModel);
            table.setRowSelectionAllowed(false);
            scrollPane.setViewportView(table);
            table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(new JComboBox<BrickHub>(new ComboBrick())));
            
            JButton scan = new JButton("");
            scan.setBackground(UIManager.getColor("Button.darkShadow"));
            scan.addMouseListener(new MouseAdapter() {
              @Override
              public void mouseClicked(MouseEvent arg0) {
                ComboBrick.scan();
              }
            });
            GroupLayout gl_contentPane = new GroupLayout(contentPane);
            gl_contentPane.setHorizontalGroup(
              gl_contentPane.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_contentPane.createSequentialGroup()
                  .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                    .addGroup(gl_contentPane.createSequentialGroup()
                      .addGap(5)
                      .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                        .addComponent(panel_1, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 660, GroupLayout.PREFERRED_SIZE)
                        .addGroup(gl_contentPane.createSequentialGroup()
                          .addComponent(toolBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                          .addGap(18)
                          .addComponent(scan, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE))))
                    .addGroup(gl_contentPane.createSequentialGroup()
                      .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                        .addComponent(lblFile, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblSequencer)
                        .addComponent(lblSynthesizer))
                      .addPreferredGap(ComponentPlacement.RELATED)
                      .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
                        .addComponent(SelectSynthesizer, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(selectSequencer, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(MidiFile, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE))
                      .addPreferredGap(ComponentPlacement.RELATED)
                      .addComponent(btnFile)))
                  .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            gl_contentPane.setVerticalGroup(
              gl_contentPane.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_contentPane.createSequentialGroup()
                  .addContainerGap()
                  .addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
                    .addComponent(toolBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(scan, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE))
                  .addGap(18)
                  .addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
                    .addComponent(btnFile, 0, 0, Short.MAX_VALUE)
                    .addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
                      .addComponent(lblFile)
                      .addComponent(MidiFile, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                  .addPreferredGap(ComponentPlacement.UNRELATED)
                  .addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
                    .addComponent(lblSequencer)
                    .addComponent(selectSequencer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                  .addPreferredGap(ComponentPlacement.UNRELATED)
                  .addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
                    .addComponent(lblSynthesizer)
                    .addComponent(SelectSynthesizer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                  .addPreferredGap(ComponentPlacement.UNRELATED)
                  .addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 245, GroupLayout.PREFERRED_SIZE)
                  .addGap(19))
            );
            contentPane.setLayout(gl_contentPane);
    

  }
}
