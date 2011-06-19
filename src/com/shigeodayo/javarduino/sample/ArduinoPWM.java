package com.shigeodayo.javarduino.sample;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.shigeodayo.javarduino.Arduino;

public class ArduinoPWM extends JFrame{

	private static final long serialVersionUID = 1L;
	
	private Arduino arduino=null;
	
	public ArduinoPWM(){
		initialize();
	}
	
	private void initialize(){
		setTitle("arduino_pwm");
		setSize(510, 200);
		
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
	}
	
	private class MyPanel extends JPanel {

		private static final long serialVersionUID = 1L;

		private int mouseX=0;
		
		public MyPanel(){
			addMouseMotionListener(new MouseMotionAdapter() {
				public void mouseMoved(MouseEvent e){
					mouseX=constrain(e.getX(), 0, 255);
				}
			});
		}
		
		@Override
		public void paint(Graphics g){
			g.setColor(new Color(mouseX));
			g.fillRect(0, 0, getWidth(), getHeight());
			
			arduino.analogWrite(9, mouseX);
			arduino.analogWrite(11, 255-mouseX);
		}
		
		private int constrain(int val, int min, int max){
			if(val>max)
				return max;
			if(val<min)
				return min;
			return val;
		}
	}
	
	public static void main(String args[]){
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				final ArduinoPWM thisClass=new ArduinoPWM();
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