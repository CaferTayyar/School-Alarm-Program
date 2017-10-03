/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alarm;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.LayoutStyle;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Cafer Tayyar YORUK
 */
public class Alarm extends JFrame implements ActionListener{

    public Alarm(){
        
        super("Anons Sistemi");
        
        initilizeComponents();
        makeFunctionality();
        makeFunctionalityOfSaveButton();
        makeFunctionalityOfResetButton();
        databaseSetting();
        databaseInstallation();
        zamanlama();
        musicSettings();
        
        // Ekranın boyutlarını hesaplar.
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        // Uygulamayı ekranın oratsında gösterir.
        setLocation( ( screenSize.width - getWidth() ) / 2, ( screenSize.height - getHeight() ) / 2 );
        
    }
    
    public void initilizeComponents(){
        
        for( int a = 0; a < 7; a++ ){
            
            outerPanel[ a ] = new JPanel();
            musicScrollPane[ a ] = new JScrollPane();
            middlePanel[ a ] = new JPanel();
            musicDayGroupLayout[ a ] = new GroupLayout( middlePanel[ a ] );
            outerPanelLayout[ a ] = new GroupLayout( outerPanel[ a ] );
            
            for( int b = 0; b < 40; b++ ){
                innerPanel[ a ][ b ] = new JPanel();
                musicCheckBox[ a ][ b ] = new JCheckBox();
                
                musicSpinner[ a ][ b ][ 0 ] = new JSpinner();
                musicSpinner[ a ][ b ][ 1 ] = new JSpinner();
                musicSpinner[ a ][ b ][ 2 ] = new JSpinner();
                
                spinnerLabel[ a ][ b ][ 0 ] = new JLabel();
                spinnerLabel[ a ][ b ][ 1 ] = new JLabel();
                
                soundSpinnerLabel[ a ][ b ] = new JLabel();
                soundSpinner[ a ][ b ] = new JSpinner();
                
                musicChoseButton[ a ][ b ] = new JButton();
                musicNameLabel[ a ][ b ] = new JLabel();
                
                musicGroupLayout[ a ][ b ] = new GroupLayout( innerPanel[ a ][ b ] );
            }
        }
        
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
        
        for( int a = 0; a < 7; a++){
            for( int i = 0; i < 40; i++ ){

                middlePanel[ a ].setBorder(BorderFactory.createTitledBorder(""));

                innerPanel[ a ][ i ].setBorder(BorderFactory.createTitledBorder( (i+1) + ". Zil"));

                musicSpinner[ a ][ i ][ 0 ].setModel(new SpinnerNumberModel(0, 0, 23, 1));

                spinnerLabel[ a ][ i ][ 0 ].setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
                spinnerLabel[ a ][ i ][ 0 ].setText(":");

                musicSpinner[ a ][ i ][ 1 ].setModel(new SpinnerNumberModel(0, 0, 59, 1));

                spinnerLabel[ a ][ i ][ 1 ].setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
                spinnerLabel[ a ][ i ][ 1 ].setText(":");
                
                soundSpinnerLabel[ a ][ i ].setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
                soundSpinnerLabel[ a ][ i ].setText( "Ses:" );
                soundSpinner[ a ][ i ].setModel(new SpinnerNumberModel(0, 0, 5, 1));
                
                musicSpinner[ a ][ i ][ 2 ].setModel(new SpinnerNumberModel(0, 0, 59, 1));
                
                musicChoseButton[ a ][ i ].setText("Müzik Seç");

                musicNameLabel[ a ][ i ].setText("Seçilen Müzik");


                innerPanel[ a ][ i ].setLayout(musicGroupLayout[ a ][ i ]);
                musicGroupLayout[ a ][ i ].setHorizontalGroup(
                    musicGroupLayout[ a ][ i ].createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(musicGroupLayout[ a ][ i ].createSequentialGroup()
                        .addGap(0, 0, 0)
                        .addComponent(musicCheckBox[ a ][ i ])
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(musicSpinner[ a ][ i ][ 0 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spinnerLabel[ a ][ i ][ 0 ])
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(musicSpinner[ a ][ i ][ 1 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spinnerLabel[ a ][ i ][ 1 ])
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(musicSpinner[ a ][ i ][ 2 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(soundSpinnerLabel[ a ][ i ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)    
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(soundSpinner[ a ][ i ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)    
                        .addGap(18, 18, 18)
                        .addComponent(musicChoseButton[ a ][ i ])
                        .addGap(18, 18, 18)
                        .addComponent(musicNameLabel[ a ][ i ])
                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
                musicGroupLayout[ a ][ i ].setVerticalGroup(
                    musicGroupLayout[ a ][ i ].createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(musicCheckBox[ a ][ i ])
                    .addGroup(musicGroupLayout[ a ][ i ].createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(musicSpinner[ a ][ i ][ 0 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(spinnerLabel[ a ][ i ][ 0 ])
                        .addComponent(musicSpinner[ a ][ i ][ 1 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(spinnerLabel[ a ][ i ][ 1 ])
                        .addComponent(soundSpinnerLabel[ a ][ i ])
                        .addComponent(soundSpinner[ a ][ i ])
                        .addComponent(musicSpinner[ a ][ i ][ 2 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(musicChoseButton[ a ][ i ])
                        .addComponent(musicNameLabel[ a ][ i ]))
                );

            }


            middlePanel[ a ].setLayout(musicDayGroupLayout[ a ]);
            musicDayGroupLayout[ a ].setHorizontalGroup(
                musicDayGroupLayout[ a ].createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(musicDayGroupLayout[ a ].createSequentialGroup()
                    .addContainerGap()
                    .addGroup(musicDayGroupLayout[ a ].createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(innerPanel[ a ][ 39 ], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(innerPanel[ a ][ 38 ], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(innerPanel[ a ][ 37 ], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(innerPanel[ a ][ 36 ], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(innerPanel[ a ][ 35 ], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(innerPanel[ a ][ 34 ], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(innerPanel[ a ][ 33 ], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(innerPanel[ a ][ 32 ], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(innerPanel[ a ][ 31 ], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(innerPanel[ a ][ 30 ], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(innerPanel[ a ][ 29 ], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(innerPanel[ a ][ 28 ], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(innerPanel[ a ][ 27 ], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(innerPanel[ a ][ 26 ], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(innerPanel[ a ][ 25 ], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(innerPanel[ a ][ 24 ], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(innerPanel[ a ][ 23 ], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(innerPanel[ a ][ 22 ], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(innerPanel[ a ][ 21 ], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(innerPanel[ a ][ 20 ], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(innerPanel[ a ][ 19 ], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(innerPanel[ a ][ 18 ], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(innerPanel[ a ][ 17 ], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(innerPanel[ a ][ 16 ], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(innerPanel[ a ][ 15 ], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(innerPanel[ a ][ 14 ], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(innerPanel[ a ][ 13 ], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(innerPanel[ a ][ 12 ], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(innerPanel[ a ][ 11 ], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(innerPanel[ a ][ 10 ], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(innerPanel[ a ][ 9 ], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(innerPanel[ a ][ 8 ], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(innerPanel[ a ][ 7 ], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(innerPanel[ a ][ 6 ], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(innerPanel[ a ][ 5 ], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(innerPanel[ a ][ 4 ], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(innerPanel[ a ][ 3 ], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(innerPanel[ a ][ 2 ], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(innerPanel[ a ][ 1 ], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(innerPanel[ a ][ 0 ], GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap())
            );
            musicDayGroupLayout[ a ].setVerticalGroup(
                musicDayGroupLayout[ a ].createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(musicDayGroupLayout[ a ].createSequentialGroup()
                    .addGap(20, 20, 20)
                    .addComponent(innerPanel[ a ][ 0 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(innerPanel[ a ][ 1 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(innerPanel[ a ][ 2 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(innerPanel[ a ][ 3 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(innerPanel[ a ][ 4 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(innerPanel[ a ][ 5 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(innerPanel[ a ][ 6 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(innerPanel[ a ][ 7 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(innerPanel[ a ][ 8 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(innerPanel[ a ][ 9 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(innerPanel[ a ][ 10 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(innerPanel[ a ][ 11 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(innerPanel[ a ][ 12 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(innerPanel[ a ][ 13 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(innerPanel[ a ][ 14 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(innerPanel[ a ][ 15 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(innerPanel[ a ][ 16 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(innerPanel[ a ][ 17 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(innerPanel[ a ][ 18 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(innerPanel[ a ][ 19 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(innerPanel[ a ][ 20 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(innerPanel[ a ][ 21 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(innerPanel[ a ][ 22 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(innerPanel[ a ][ 23 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(innerPanel[ a ][ 24 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(innerPanel[ a ][ 25 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(innerPanel[ a ][ 26 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(innerPanel[ a ][ 27 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(innerPanel[ a ][ 28 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(innerPanel[ a ][ 29 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(innerPanel[ a ][ 30 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(innerPanel[ a ][ 31 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(innerPanel[ a ][ 32 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(innerPanel[ a ][ 33 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(innerPanel[ a ][ 34 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(innerPanel[ a ][ 35 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(innerPanel[ a ][ 36 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(innerPanel[ a ][ 37 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(innerPanel[ a ][ 38 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(innerPanel[ a ][ 39 ], GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(203, Short.MAX_VALUE))
            );

            musicScrollPane[ a ].setViewportView(middlePanel[ a ]);
            musicScrollPane[ a ].getVerticalScrollBar().setUnitIncrement(40);   

            outerPanel[ a ].setLayout(outerPanelLayout[ a ]);
            outerPanelLayout[ a ].setHorizontalGroup(
                outerPanelLayout[ a ].createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(outerPanelLayout[ a ].createSequentialGroup()
                    .addContainerGap()
                    .addComponent(musicScrollPane[ a ], GroupLayout.DEFAULT_SIZE, 556, Short.MAX_VALUE)
                    .addContainerGap())
            );
            outerPanelLayout[ a ].setVerticalGroup(
                outerPanelLayout[ a ].createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(GroupLayout.Alignment.TRAILING, outerPanelLayout[ a ].createSequentialGroup()
                    .addContainerGap()
                    .addComponent(musicScrollPane[ a ], GroupLayout.DEFAULT_SIZE, 408, Short.MAX_VALUE)
                    .addContainerGap())
            );
        }
        
        // Pazartesi günü ilk tab'da olur
        
        for( int i = 1; i < 7; i++ ){
            musicTabbedPane.addTab( dateNames[ i ], outerPanel[ i ]);
        }
        musicTabbedPane.addTab( dateNames[ 0 ], outerPanel[ 0 ]);   // Pazar günü son tab'da olur
        
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent( musicTabbedPane )
                    .addGroup(layout.createSequentialGroup()
                        .addComponent( saveButton )
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent( resetButton )
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent( musicTabbedPane )
                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup( GroupLayout.Alignment.BASELINE )
                    .addComponent( saveButton )
                    .addComponent( resetButton ))
                .addGap(6, 6, 6))
        );

        pack();
        
        fileChooser.setFileSelectionMode( JFileChooser.FILES_AND_DIRECTORIES );
        fileChooser.setDialogTitle( "Müzik seçin" );
        fileChooser.setApproveButtonText( "Seç" );

        fileChooser.setCurrentDirectory( fDirectory );
    }
   
    public void musicSettings(){
        
        activeMusic = null;
        
        for( int a = 0; a < 7; a++ ){
            for( int b = 0; b < 40; b++ ){
                if( ( day + ":" + hour + ":" + minute + ":" + second ).equals( (a+1)+":"+Integer.parseInt( musicSpinner[a][b][0].getValue().toString())+":"+Integer.parseInt( musicSpinner[a][b][1].getValue().toString())+":"+Integer.parseInt( musicSpinner[a][b][2].getValue().toString())) ){

                    if( musicCheckBox[ a ][ b ].isSelected() ){
                        activeMusic = musicFiles[ a ][ b ];
                        currentSoundVolume =  soundVolumes[ Integer.parseInt( soundSpinner[ a ][ b ].getValue().toString() ) ];
                    }
                }
            }
        }
    }
    
    public void zamanlama(){
        
        ourTimer = new Timer( 1000, 
            new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    cal = new GregorianCalendar();
                    second = cal.get( Calendar.SECOND );
                    minute = cal.get( Calendar.MINUTE );
                    hour = cal.get( Calendar.HOUR );
                    day = cal.get( Calendar.DAY_OF_WEEK );
                    
                    if( cal.get( Calendar.AM_PM ) == 1 ){  // 0-> AM, 1->PM
                        hour = ( hour % 12 ) + 12;
                    }
                    
                    musicSettings();
                    
                    if( activeMusic != null ){
                        // Music is played
                        
                        if( clip != null && clip.isOpen() ){
                            clip.close();
                        }
                        
                        // Müzik çalar
                        try {
                            audioIn = AudioSystem.getAudioInputStream( activeMusic );
                            clip = AudioSystem.getClip();
                            clip.open(audioIn);
                            gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                            gainControl.setValue( currentSoundVolume ); 

                            clip.start();

                        } catch (LineUnavailableException ex) {
                            Logger.getLogger(Alarm.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (UnsupportedAudioFileException ex) {
                            new Thread(){
                                public void run(){
                                    JOptionPane.showMessageDialog( rootPane, "Maalesef program \"" + activeMusic.getName() + "\" isimli müzik dosyasını oynatamıyor. Lütfen bu dosyayı siliniz." );
                                }
                            }.start();
                            Logger.getLogger(Alarm.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(Alarm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        );
        
        ourTimer.start();
        
    }
   
    public void databaseSetting(){
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        } catch (ClassNotFoundException cnfe) {
            System.err.println("Derby driver not found.");
        }
        try {
            
            // Database ilk defa oluşturulacağı zaman şu yapılır:
            // conn = DriverManager.getConnection("jdbc:derby:./database;create=true;user=cty;pass=1");
            
            conn = DriverManager.getConnection("jdbc:derby:./database;user=cty;pass=1");
            s = conn.createStatement();
            
            //s.execute( "DROP TABLE Alarms");
            /*
            s.execute( "CREATE TABLE Alarms(" +
                            "Day int," +
                            "ZilNo int," +
                            "Saat int," +
                            "Dakika int," +
                            "Saniye int," +
                            "SesSeviye int," +
                            "MusicFilePath varchar(100)," +
                            "Aktif int )"
            );
            
            
            for( int a = 0; a < 7; a++ ){
                for( int b = 0; b < 40; b++ ){
                    
                //    sqlQuery = String.format( "UPDATE Alarms SET Saat = %d, Dakika = %d, Saniye = %d, SesSeviye = %d, MusicFilePath = '%s', Aktif = %d WHERE Day = %d AND ZilNo = %d", 
                //                              0, 0, 0, 5, "***",0, a, b );
                    
                    
                    sqlQuery = String.format( "INSERT INTO Alarms VALUES ( %d, %d, %d, %d, %d, %d, '%s', %d )", 
                                            a, b, 0, 0, 0, 5, "***",0 );
                    
                    s.execute(sqlQuery);
                    
                }
            }
            
            */
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void databaseInstallation(){
        
        for( int a = 0; a < 7; a++ ){
            for( int b = 0; b < 40; b++ ){
            
                sqlQuery = String.format( "SELECT * FROM Alarms WHERE Day = %d AND ZilNo = %d", a, b );
                
                try {
                    s.execute( sqlQuery );
                    rs = s.getResultSet();
                    while (rs.next()) {
                        
                        musicSpinner[ a ][ b ][ 0 ].setValue( rs.getInt("Saat") );
                        musicSpinner[ a ][ b ][ 1 ].setValue( rs.getInt("Dakika") );
                        musicSpinner[ a ][ b ][ 2 ].setValue( rs.getInt("Saniye") );
                        
                        soundSpinner[ a ][ b ].setValue( rs.getInt("SesSeviye") );
                        
                        // Seçilmiş bir dosya varsa
                        if( !rs.getString("MusicFilePath").equals( "***" ) ){
                            musicFiles[ a ][ b ] = new File( rs.getString("MusicFilePath") );
                            
                            musicNameLabel[ a ][ b ].setText( musicFiles[ a ][ b ].getName() );
                        }
                        else {  // Seçilmiş bir dosya yoksa
                            musicFiles[ a ][ b ] = null;
                            musicNameLabel[ a ][ b ].setText( "Seçilen Müzik" );
                        }
                        
                        ////////////
                        
                        if( rs.getInt("Aktif") == 1 ){
                            musicCheckBox[ a ][ b ].setSelected( true );
                        }
                        else {
                            musicCheckBox[ a ][ b ].setSelected( false );
                        }
                    }
                } catch (SQLException ex) {
                    Logger.getLogger( Alarm.class.getName()).log(Level.SEVERE, null, ex );
                }
            }
        }    
    }
    
    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger( Alarm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger( Alarm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger( Alarm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger( Alarm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Alarm().setVisible(true);
            }
        });
    }
    
    
    // DESIGN
    private JButton saveButton = new JButton( "Kaydet" );
    private JButton resetButton = new JButton( "Seçili Olmayanları Sıfırla" );
    private JTabbedPane musicTabbedPane = new JTabbedPane();
    private JPanel[] outerPanel = new JPanel[ 7 ];
    private JScrollPane[] musicScrollPane = new JScrollPane[ 7 ];
    private JPanel[] middlePanel = new JPanel[ 7 ];
    private JPanel[][] innerPanel = new JPanel[ 7 ][ 40 ];
    private JCheckBox[][] musicCheckBox = new JCheckBox[ 7 ][ 40 ];
    private JSpinner[][][] musicSpinner = new JSpinner[ 7 ][ 40 ][ 3 ];
    private JLabel[][][] spinnerLabel = new JLabel[ 7 ][ 40 ][ 2 ];
    private JLabel[][] soundSpinnerLabel = new JLabel[ 7 ][ 40 ];
    private JSpinner[][] soundSpinner = new JSpinner[ 7 ][ 40 ];
    private JButton[][] musicChoseButton = new JButton[ 7 ][ 40 ];
    private JLabel[][] musicNameLabel = new JLabel[ 7 ][ 40 ];
    private GroupLayout[][] musicGroupLayout = new GroupLayout[ 7 ][ 40 ];
    private GroupLayout[] musicDayGroupLayout = new GroupLayout[ 7 ];
    private GroupLayout[] outerPanelLayout = new GroupLayout[ 7 ];
    // END of DESIGN
    
    // MUSIC
    AudioInputStream audioIn;
    Clip clip;
    FloatControl gainControl;
    // MUSIC
    
    private JFileChooser fileChooser = new JFileChooser();
    private File[][] musicFiles = new File[ 7 ][ 40 ];
    private String[][] musicFilePaths = new String[ 7 ][ 40 ];
    private File activeMusic = null;
    private File tempMusicFile = null;
    private File fDirectory = new File( "musics" );
    private Calendar cal;
    private int second;
    private int minute;
    private int hour;
    private int day;
    private String[] dateNames = { "Pazar", "Pazartesi", "Salı", "Çarşamba", "Perşembe", "Cuma", "Cumartesi" };
    private Timer ourTimer;
    private Connection conn;
    private Statement s;
    private String sqlQuery = "";
    private ResultSet rs;
    private float[] soundVolumes = { -80f, -30f, -20f, -15f, -10f, 0f };
    private float currentSoundVolume;
    
    // They are used in for-loop in makeFunctionality() method.
    int dayNumber;
    int zilNumber;
    
    // Pattern Matching
    Pattern expression = Pattern.compile( "(musics\\\\.*)" );
    Matcher matcher;
    String musicFilePlace = "";
    
    public String patternMatching( File file ){
        matcher = expression.matcher( file.getPath() );
        while ( matcher.find() ){
            musicFilePlace = matcher.group();
        }
        
        return musicFilePlace;
    }
    
    public boolean patternMatchingOfMusicFile( String s ){
        return s.matches( "(.+[.]au)|(.+[.]aif)|(.+[.]wav)" );
    }
    
    public void makeFunctionalityOfSaveButton(){
        saveButton.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent evt ) {

                // Data are updated in database
                for( int a = 0; a < 7; a++ ){
                    for( int b = 0; b < 40; b++ ){
                        
                        // Müzik dosyası seçilmiş ise
                        if( !musicNameLabel[ a ][ b ].getText().equals( "Seçilen Müzik" ) ){
                                
                            if( musicCheckBox[ a ][ b ].isSelected() ){
                                
                                sqlQuery = String.format( "UPDATE Alarms SET Saat = %d, Dakika = %d, Saniye = %d, SesSeviye = %d, MusicFilePath = '%s', Aktif = %d WHERE Day = %d AND ZilNo = %d", 
                                        Integer.parseInt( musicSpinner[ a ][ b ][ 0 ].getValue().toString() ), Integer.parseInt( musicSpinner[ a ][ b ][ 1 ].getValue().toString() ), Integer.parseInt( musicSpinner[ a ][ b ][ 2 ].getValue().toString() ), Integer.parseInt( soundSpinner[ a ][ b ].getValue().toString() ), patternMatching( musicFiles[ a ][ b ] ), 1, a, b );
                            }
                            else {
                                
                                sqlQuery = String.format( "UPDATE Alarms SET Saat = %d, Dakika = %d, Saniye = %d, SesSeviye = %d, MusicFilePath = '%s', Aktif = %d WHERE Day = %d AND ZilNo = %d", 
                                        Integer.parseInt( musicSpinner[ a ][ b ][ 0 ].getValue().toString() ), Integer.parseInt( musicSpinner[ a ][ b ][ 1 ].getValue().toString() ), Integer.parseInt( musicSpinner[ a ][ b ][ 2 ].getValue().toString() ), Integer.parseInt( soundSpinner[ a ][ b ].getValue().toString() ), patternMatching( musicFiles[ a ][ b ] ), 0, a, b );
                            }
                        }
                        else {  // Müzik dosyası seçilmemiş ise
                           if( musicCheckBox[ a ][ b ].isSelected() ){
                               
                                sqlQuery = String.format( "UPDATE Alarms SET Saat = %d, Dakika = %d, Saniye = %d, SesSeviye = %d, MusicFilePath = '%s', Aktif = %d WHERE Day = %d AND ZilNo = %d", 
                                        Integer.parseInt( musicSpinner[ a ][ b ][ 0 ].getValue().toString() ), Integer.parseInt( musicSpinner[ a ][ b ][ 1 ].getValue().toString() ), Integer.parseInt( musicSpinner[ a ][ b ][ 2 ].getValue().toString() ), Integer.parseInt( soundSpinner[ a ][ b ].getValue().toString() ), "***", 1, a, b );
                            }
                            else {
                               
                                sqlQuery = String.format( "UPDATE Alarms SET Saat = %d, Dakika = %d, Saniye = %d, SesSeviye = %d, MusicFilePath = '%s', Aktif = %d WHERE Day = %d AND ZilNo = %d", 
                                        Integer.parseInt( musicSpinner[ a ][ b ][ 0 ].getValue().toString() ), Integer.parseInt( musicSpinner[ a ][ b ][ 1 ].getValue().toString() ), Integer.parseInt( musicSpinner[ a ][ b ][ 2 ].getValue().toString() ), Integer.parseInt( soundSpinner[ a ][ b ].getValue().toString() ), "***", 0, a, b );
                            } 
                        }
                        try {
                            s.execute( sqlQuery );
                        } catch (SQLException ex) {
                            Logger.getLogger(Alarm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                
                JOptionPane.showMessageDialog( rootPane, "Değişiklikler kaydedilmiştir" );
            }
        });
    }
    
    public void makeFunctionalityOfResetButton(){
        resetButton.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent evt ) {
                
                // Data are updated in database
                for( int a = 0; a < 7; a++ ){
                    for( int b = 0; b < 40; b++ ){
                                
                        if( !musicCheckBox[ a ][ b ].isSelected() ){
                            sqlQuery = String.format( "UPDATE Alarms SET Saat = %d, Dakika = %d, Saniye = %d, SesSeviye = %d, MusicFilePath = '%s', Aktif = %d WHERE Day = %d AND ZilNo = %d", 
                                              0, 0, 0, 5, "***", 0, a, b ); 
                            
                            musicSpinner[ a ][ b ][ 0 ].setValue( 0 );
                            musicSpinner[ a ][ b ][ 1 ].setValue( 0 );
                            musicSpinner[ a ][ b ][ 2 ].setValue( 0 );
                            
                            soundSpinner[ a ][ b ].setValue( 5 );
                            
                            musicNameLabel[ a ][ b ].setText( "Seçilen Müzik" );
                        }
                        
                        try {
                            s.execute( sqlQuery );
                        } catch (SQLException ex) {
                            Logger.getLogger(Alarm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }    
                JOptionPane.showMessageDialog( rootPane, "Veriler sıfırlanıp, değişiklikler kaydedilmiştir" );
            }
        });    
    }
    
    public void makeFunctionality(){
        
        for( dayNumber = 0; dayNumber < 7; dayNumber++ ){
            for( zilNumber = 0; zilNumber < 40; zilNumber++ ){
                musicChoseButton[ dayNumber ][ zilNumber ].addActionListener( this );
                musicChoseButton[ dayNumber ][ zilNumber ].setActionCommand( "" + ( dayNumber * 40 + zilNumber ) );
            }
        }    
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        int action = Integer.parseInt( e.getActionCommand() );
        
        if( fileChooser.showOpenDialog( rootPane  ) == JFileChooser.APPROVE_OPTION ){

            tempMusicFile = fileChooser.getSelectedFile();

            if( patternMatchingOfMusicFile( tempMusicFile.getName() ) ){
                
                musicFiles[ action / 40 ][ action % 40 ] = tempMusicFile;
                musicNameLabel[ action / 40 ][ action % 40 ].setText( tempMusicFile.getName() ) ;
            }

            else{
                JOptionPane.showMessageDialog( rootPane, "Seçilen müzik dosyası maalesef desteklenmemektedir."
                        + " Sadece \".au\", \".aif\" ve \".wav\" uzantılı dosyalar desteklenmektedir." );
            }
        }

    }
}
