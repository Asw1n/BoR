package PC;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.sound.midi.MidiDevice;
import javax.swing.DefaultCellEditor;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * IMTableEditor lets you edit and maintain the InstrumentMusician Mapping
 * table;
 * 
 * @author Aswin
 * 
 */
public class BoR extends JFrame {

	private final BoRController myController = new BoRController();

	private static final long serialVersionUID = 6318810593044644641L;
	private final JPanel contentPane;
	private final JTextField MidiFile;
	// Create a file chooser
	final JFileChooser fc = new JFileChooser(new File(
			"C:/Users/Bert/development/git/BoR/MIDI"));
	private File sequenceFile;
	private final JTable table;

	private final SongTableModel songModel = new SongTableModel();

	/**
	 * Launch the application.
	 */
	public static void main(final String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (final Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					final BoR frame = new BoR();
					frame.setVisible(true);
				} catch (final Exception e) {
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

		final JToolBar toolBar = new JToolBar();

		final JButton btnPlay = new JButton("");
		btnPlay.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent arg0) {
				myController.open();
				myController.play();
			}
		});
		btnPlay.setIcon(new ImageIcon(BoR.class
				.getResource("/icons/play-icon.png")));
		toolBar.add(btnPlay);

		final JButton btnNewButton = new JButton("");
		btnNewButton.setIcon(new ImageIcon(BoR.class
				.getResource("/icons/pause-icon.png")));
		toolBar.add(btnNewButton);

		final JButton btnTest = new JButton("");
		btnTest.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent arg0) {
				myController.stop();
				myController.close();
			}
		});
		btnTest.setIcon(new ImageIcon(BoR.class
				.getResource("/icons/stop-icon.png")));
		btnTest.setSelectedIcon(new ImageIcon(BoR.class
				.getResource("/icons/stop-icon.png")));
		toolBar.add(btnTest);

		final JLabel lblFile = new JLabel("File");

		MidiFile = new JTextField();
		MidiFile.setColumns(10);

		final JButton btnFile = new JButton("");
		btnFile.setIcon(fc.getIcon(new File("Test.mid")));
		btnFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				{
					if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
						sequenceFile = fc.getSelectedFile();
						MidiFile.setText(sequenceFile.getAbsolutePath());
						songModel.setFile(sequenceFile);
						myController.setSong(songModel.getSong());
					}
				}
			}
		});

		final JLabel lblSequencer = new JLabel("Sequencer");

		final JComboBox<MidiDevice.Info> selectSequencer = new JComboBox<MidiDevice.Info>(
				new ComboSequencers());
		myController.setSequencer((MidiDevice.Info) selectSequencer
				.getSelectedItem());
		selectSequencer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				myController.setSequencer((MidiDevice.Info) ((JComboBox) arg0
						.getSource()).getSelectedItem());
			}
		});

		final JLabel lblSynthesizer = new JLabel("Synthesizer");

		final JComboBox<MidiDevice.Info> SelectSynthesizer = new JComboBox<MidiDevice.Info>(
				new ComboSynthesizers());
		myController.setSynthesizer((MidiDevice.Info) SelectSynthesizer
				.getSelectedItem());
		selectSequencer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				myController.setSynthesizer((MidiDevice.Info) ((JComboBox) arg0
						.getSource()).getSelectedItem());
			}
		});

		final JPanel panel_1 = new JPanel();
		panel_1.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"), }, new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"), }));

		final JScrollPane scrollPane = new JScrollPane();
		panel_1.add(scrollPane, "2, 2, 3, 1, fill, fill");

		table = new JTable(songModel);
		table.setRowSelectionAllowed(false);
		scrollPane.setViewportView(table);
		table.getColumnModel()
				.getColumn(2)
				.setCellEditor(
						new DefaultCellEditor(new JComboBox<BrickHub>(
								new ComboBrick())));

		final JButton scan = new JButton("");
		scan.setBackground(UIManager.getColor("Button.darkShadow"));
		scan.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(final MouseEvent arg0) {
				ComboBrick.scan();
			}
		});
		final GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane
				.setHorizontalGroup(gl_contentPane
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_contentPane
										.createSequentialGroup()
										.addGroup(
												gl_contentPane
														.createParallelGroup(
																Alignment.LEADING)
														.addGroup(
																gl_contentPane
																		.createSequentialGroup()
																		.addGap(5)
																		.addGroup(
																				gl_contentPane
																						.createParallelGroup(
																								Alignment.LEADING)
																						.addComponent(
																								panel_1,
																								Alignment.TRAILING,
																								GroupLayout.PREFERRED_SIZE,
																								660,
																								GroupLayout.PREFERRED_SIZE)
																						.addGroup(
																								gl_contentPane
																										.createSequentialGroup()
																										.addComponent(
																												toolBar,
																												GroupLayout.PREFERRED_SIZE,
																												GroupLayout.DEFAULT_SIZE,
																												GroupLayout.PREFERRED_SIZE)
																										.addGap(18)
																										.addComponent(
																												scan,
																												GroupLayout.PREFERRED_SIZE,
																												55,
																												GroupLayout.PREFERRED_SIZE))))
														.addGroup(
																gl_contentPane
																		.createSequentialGroup()
																		.addGroup(
																				gl_contentPane
																						.createParallelGroup(
																								Alignment.LEADING)
																						.addComponent(
																								lblFile,
																								GroupLayout.PREFERRED_SIZE,
																								46,
																								GroupLayout.PREFERRED_SIZE)
																						.addComponent(
																								lblSequencer)
																						.addComponent(
																								lblSynthesizer))
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addGroup(
																				gl_contentPane
																						.createParallelGroup(
																								Alignment.LEADING,
																								false)
																						.addComponent(
																								SelectSynthesizer,
																								0,
																								GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE)
																						.addComponent(
																								selectSequencer,
																								0,
																								GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE)
																						.addComponent(
																								MidiFile,
																								GroupLayout.DEFAULT_SIZE,
																								400,
																								Short.MAX_VALUE))
																		.addPreferredGap(
																				ComponentPlacement.RELATED)
																		.addComponent(
																				btnFile)))
										.addContainerGap(
												GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));
		gl_contentPane
				.setVerticalGroup(gl_contentPane
						.createParallelGroup(Alignment.LEADING)
						.addGroup(
								gl_contentPane
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												gl_contentPane
														.createParallelGroup(
																Alignment.TRAILING)
														.addComponent(
																toolBar,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE)
														.addComponent(
																scan,
																GroupLayout.PREFERRED_SIZE,
																55,
																GroupLayout.PREFERRED_SIZE))
										.addGap(18)
										.addGroup(
												gl_contentPane
														.createParallelGroup(
																Alignment.TRAILING,
																false)
														.addComponent(btnFile,
																0, 0,
																Short.MAX_VALUE)
														.addGroup(
																gl_contentPane
																		.createParallelGroup(
																				Alignment.BASELINE)
																		.addComponent(
																				lblFile)
																		.addComponent(
																				MidiFile,
																				GroupLayout.PREFERRED_SIZE,
																				GroupLayout.DEFAULT_SIZE,
																				GroupLayout.PREFERRED_SIZE)))
										.addPreferredGap(
												ComponentPlacement.UNRELATED)
										.addGroup(
												gl_contentPane
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																lblSequencer)
														.addComponent(
																selectSequencer,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												ComponentPlacement.UNRELATED)
										.addGroup(
												gl_contentPane
														.createParallelGroup(
																Alignment.BASELINE)
														.addComponent(
																lblSynthesizer)
														.addComponent(
																SelectSynthesizer,
																GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												ComponentPlacement.UNRELATED)
										.addComponent(panel_1,
												GroupLayout.PREFERRED_SIZE,
												245, GroupLayout.PREFERRED_SIZE)
										.addGap(19)));
		contentPane.setLayout(gl_contentPane);

	}
}
