package edu.nyu;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import edu.nyu.Validator.Location;

public class Plotter extends JFrame {
  private static final int SCALE = 1;
  public static void main(String[] args){
    Plotter p = new Plotter();
    p.draw();
  }
  private static final long serialVersionUID = 1L;
  List<Location> people;
  List<Location> hospitals;

  public Plotter() throws HeadlessException {
    super("Scatterplot");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBackground(Color.white);
    people = new ArrayList<Location>();
    hospitals = new ArrayList<Location>();
  }
  public void setUp(List<Location> people, List<Location> hospitals){
    this.people = people;
    this.hospitals = hospitals;
  }
  public void draw(){
    JPanel panel = new JPanel() { 
      private static final long serialVersionUID = 1L;
      public void paintComponent(Graphics g) {
        g.setColor(Color.red);
        for(Location l:people){
          g.drawString("x", l.getX()*SCALE, l.getY()*SCALE);
        }
        g.setColor(Color.blue);
        for(Location h: hospitals){
          g.drawString("o", h.getX()*SCALE, h.getY()*SCALE);
        }
      }
    };
    setContentPane(panel);
    int maxX = 0, maxY = 0;
    for(Location l:people){
      maxX = maxX < l.getX()? l.getX(): maxX;
      maxY = maxY<l.getY()? l.getY():  maxY;
    }
    for(Location l: hospitals){
      maxX = maxX < l.getX()? l.getX(): maxX;
      maxY = maxY<l.getY()? l.getY():  maxY;
    }
    setBounds(20, 20, maxX*SCALE+10, maxY*SCALE+25);
    setVisible(true);
  }


}
