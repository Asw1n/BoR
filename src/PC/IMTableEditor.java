package PC;

import java.awt.EventQueue;

import javax.sound.midi.MidiDevice;
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


/** IMTableEditor lets you edit and maintain the InstrumentMusician Mapping table;
 * 
 * @author Aswin
 *
 */
public class IMTableEditor extends JFrame {


  private static final long serialVersionUID = 6318810593044644641L;
  private JPanel contentPane;
  private JTextField MidiFile;
//Create a file chooser
  final JFileChooser fc = new JFileChooser();
  private File sequenceFile;
  private JTable table;

  private IMmapModel mapModel=new  IMmapModel();
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
          IMTableEditor frame = new IMTableEditor();
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
  public IMTableEditor() {
    setTitle("Band of Robots");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 696, 553);
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);

    
    JToolBar toolBar = new JToolBar();
    
    
    JButton btnPlay = new JButton("");
    btnPlay.setIcon(new ImageIcon(IMTableEditor.class.getResource("/icons/play-icon.png")));
    toolBar.add(btnPlay);
    
    JButton btnNewButton = new JButton("");
    btnNewButton.setIcon(new ImageIcon(IMTableEditor.class.getResource("/icons/pause-icon.png")));
    toolBar.add(btnNewButton);
    
    JButton btnTest = new JButton("");
    btnTest.setIcon(new ImageIcon(IMTableEditor.class.getResource("/icons/stop-icon.png")));
    btnTest.setSelectedIcon(new ImageIcon(IMTableEditor.class.getResource("/icons/stop-icon.png")));
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
                      mapModel.setFile(sequenceFile);
                }
                }
              }
            });
            
            JLabel lblSequencer = new JLabel("Sequencer");
            
            JComboBox<MidiDevice.Info> selectSequencer = new JComboBox<MidiDevice.Info>(new ComboSequencers());
            
                        
                        JLabel lblSynthesizer = new JLabel("Synthesizer");
            
            JComboBox<MidiDevice.Info> SelectSynthesizer = new JComboBox<MidiDevice.Info>(new ComboSynthesizers());
            
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
            panel_1.add(scrollPane, "2, 2, fill, fill");
            
            table = new JTable(mapModel);
            table.setRowSelectionAllowed(false);
            scrollPane.setViewportView(table);
            table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(new JComboBox<BrickHub>(new ComboBrick())));
            GroupLayout gl_contentPane = new GroupLayout(contentPane);
            gl_contentPane.setHorizontalGroup(
              gl_contentPane.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_contentPane.createSequentialGroup()
                  .addGap(5)
                  .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                    .addComponent(panel_1, Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 660, GroupLayout.PREFERRED_SIZE)
                    .addComponent(toolBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                  .addContainerGap())
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
                  .addComponent(btnFile)
                  .addContainerGap(155, Short.MAX_VALUE))
            );
            gl_contentPane.setVerticalGroup(
              gl_contentPane.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_contentPane.createSequentialGroup()
                  .addGap(5)
                  .addComponent(toolBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                  .addGap(18)
                  .addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
                    .addComponent(btnFile, 0, 0, Short.MAX_VALUE)
                    .addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
                      .addComponent(lblFile)
                      .addComponent(MidiFile)))
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
