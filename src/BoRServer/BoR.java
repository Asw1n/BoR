package BoRServer;

import java.awt.EventQueue;

import javax.sound.midi.MidiDevice;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Insets;

import javax.swing.JComboBox;

import java.awt.Color;

public class BoR {
    static Controller controller=new Controller();
    static JFileChooser chooser = new JFileChooser();
    static BrickSelector brickChooser = new BrickSelector();
    SongTableModel model;


    private JFrame frame;
    private JTable table;
    private JLabel songName;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    BoR window = new BoR();
                    window.frame.setVisible(true);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public BoR() {
        initialize();
        model=new SongTableModel(controller.getSong());
        table.setModel(model);
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 1030, 672);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        
        JMenu mnFile = new JMenu("File");
        menuBar.add(mnFile);
        
        JMenuItem mntmNewMenuItem = new JMenuItem("New...");
        mntmNewMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                selectMidiFile();
            }
        });
        mnFile.add(mntmNewMenuItem);
        
        JMenuItem mntmOpen = new JMenuItem("Open...");
        mnFile.add(mntmOpen);
        
        JMenuItem mntmClose = new JMenuItem("Close");
        mnFile.add(mntmClose);
        
        JMenuItem mntmSave = new JMenuItem("Save");
        mnFile.add(mntmSave);
        
        JMenu mnMusic = new JMenu("Music");
        menuBar.add(mnMusic);
        
        JMenuItem mntmPlay = new JMenuItem("Play");
        mntmPlay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                controller.play();
            }
        });
        mnMusic.add(mntmPlay);
        
        JMenuItem mntmStop = new JMenuItem("Stop"); 
        mntmStop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                controller.stop();
            }
        });
        mnMusic.add(mntmStop);
        
        JMenu mnEv = new JMenu("EV3");
        menuBar.add(mnEv);
        
        JMenuItem mntmSearch = new JMenuItem("Search");
        mnEv.add(mntmSearch);

        
        frame.getContentPane().setLayout(new BorderLayout(0, 0));
        
        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.NORTH);
        GridBagLayout gbl_panel = new GridBagLayout();
        gbl_panel.columnWidths = new int[]{0, 0, 0, 0, 0};
        gbl_panel.rowHeights = new int[]{0, 0};
        gbl_panel.columnWeights = new double[]{0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
        gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
        panel.setLayout(gbl_panel);
        
        JLabel lblSong = new JLabel("Song");
        lblSong.setForeground(Color.BLUE);
        lblSong.setFont(new Font("Tahoma", Font.PLAIN, 22));
        GridBagConstraints gbc_lblSong = new GridBagConstraints();
        gbc_lblSong.gridwidth = 4;
        gbc_lblSong.insets = new Insets(0, 0, 5, 5);
        gbc_lblSong.gridx = 0;
        gbc_lblSong.gridy = 0;
        panel.add(lblSong, gbc_lblSong);
        
        JLabel lblSequencer = new JLabel("Sequencer");
        GridBagConstraints gbc_lblSequencer = new GridBagConstraints();
        gbc_lblSequencer.insets = new Insets(0, 0, 5, 5);
        gbc_lblSequencer.gridx = 0;
        gbc_lblSequencer.gridy = 1;
        panel.add(lblSequencer, gbc_lblSequencer);
        
        JComboBox<MidiDevice.Info> selSequencer = new JComboBox<MidiDevice.Info>(new ComboSequencers());
        GridBagConstraints gbc_selSequencer = new GridBagConstraints();
        gbc_selSequencer.anchor = GridBagConstraints.WEST;
        gbc_selSequencer.insets = new Insets(0, 0, 5, 5);
        gbc_selSequencer.gridx = 1;
        gbc_selSequencer.gridy = 1;
        panel.add(selSequencer, gbc_selSequencer);
        
        controller.setSequencer( (MidiDevice.Info) selSequencer.getSelectedItem());
        selSequencer.addActionListener(new ActionListener() {
          @SuppressWarnings("unchecked")
        public void actionPerformed(ActionEvent arg0) {
            controller.setSequencer((MidiDevice.Info) ((JComboBox<MidiDevice.Info>)arg0.getSource()).getSelectedItem());
          }
        });

        
        JLabel lblSynthesizer = new JLabel("Synthesizer");
        GridBagConstraints gbc_lblSynthesizer = new GridBagConstraints();
        gbc_lblSynthesizer.anchor = GridBagConstraints.EAST;
        gbc_lblSynthesizer.insets = new Insets(0, 0, 0, 5);
        gbc_lblSynthesizer.gridx = 0;
        gbc_lblSynthesizer.gridy = 2;
        panel.add(lblSynthesizer, gbc_lblSynthesizer);
        
        JComboBox<MidiDevice.Info> selSynthesizer = new JComboBox<MidiDevice.Info>(new ComboSynthesizers());
        GridBagConstraints gbc_selSynthesizer = new GridBagConstraints();
        gbc_selSynthesizer.anchor = GridBagConstraints.WEST;
        gbc_selSynthesizer.insets = new Insets(0, 0, 0, 5);
        gbc_selSynthesizer.gridx = 1;
        gbc_selSynthesizer.gridy = 2;
        panel.add(selSynthesizer, gbc_selSynthesizer);

        controller.setSynthesizer( (MidiDevice.Info) selSynthesizer.getSelectedItem());
        selSynthesizer.addActionListener(new ActionListener() {
          @SuppressWarnings("unchecked")
        public void actionPerformed(ActionEvent arg0) {
            controller.setSynthesizer((MidiDevice.Info) ((JComboBox<MidiDevice.Info>)arg0.getSource()).getSelectedItem());
          }
        });

        
        JButton btnPlay = new JButton("");
        btnPlay.setIcon(new ImageIcon(BoR.class.getResource("/icons/play-icon.png")));
        frame.getContentPane().add(btnPlay, BorderLayout.SOUTH);
        
        JScrollPane scrollPane = new JScrollPane();

        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        table = new JTable(model);
        scrollPane.setViewportView(table);
        table.setCellEditor(new EV3Editor());
        table.getColumnModel().getColumn(2).setCellEditor(new EV3Editor());
    }

    protected void selectMidiFile() {
        chooser.setFileFilter(new FileNameExtensionFilter("Midi files", "mid", "midi"));
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            controller.setFile(chooser.getSelectedFile().getAbsolutePath());
        }
     
    }

    protected JLabel getSongName() {
        return songName;
    }
}
