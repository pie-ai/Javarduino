package com.shigeodayo.javarduino.sample;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.shigeodayo.javarduino.Arduino;

public class ArduinoInput extends JFrame{

	private static final long serialVersionUID = 1L;
	
	private static final Color on=new Color(4, 79, 111);
	private static final Color off=new Color(84, 145, 158);
	
	private Arduino arduino=null;
	
	public ArduinoInput(){
		initialize();
	}
	
	private void initialize(){
		setTitle("arduino_input");
		setSize(470, 280);
		
		initArduino();
		
		final MyPanel myPanel=new MyPanel();
		add(myPanel);
		
		new Thread(){
			public void run(){
				while(true){
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					myPanel.repaint();
				}
			}
		}.start();
	}
	
	private void initArduino(){
		arduino=new Arduino(Arduino.list()[0], 57600);
		
		for(int i=0; i<13; i++)
			arduino.pinMode(i, Arduino.INPUT);
	}
	
	private class MyPanel extends JPanel {

		private static final long serialVersionUID = 1L;

		@Override
		public void paint(Graphics g){
			g.setColor(Color.white);
			g.fillRect(0, 0, getWidth(), getHeight());
			
			for(int i=0; i<=13; i++){
				if(arduino.digitalRead(i)==Arduino.HIGH)
					g.setColor(on);
				else
					g.setColor(off);
				g.fillRect(420-i*30, 30, 20, 20);
			}
			for(int i=0; i<=5; i++){
				g.fillOval(280+i*30, 200, arduino.analogRead(i)/16, arduino.analogRead(i)/16);
			}
		}
	}
	
	public static void main(String args[]){
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final ArduinoInput thisClass=new ArduinoInput();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent e){
						thisClass.dispose();
					}
				});
				thisClass.setVisible(true);
			}
		});
	}
}