import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

final class Validator{
  final List<Person> injured;
  final List<Integer> hospitals;
  public static void main(String args[]){
    Validator v = new Validator();
    Scanner input = null;
    try {
      input = new Scanner(new File(args[0]));
      v.buildInput(input);
      input.close();
      if(args.length == 1) input = new Scanner(System.in);
      else input = new Scanner(new File(args[1]));
    } catch (FileNotFoundException e) {
      System.err.println("Input file DNE");
      e.printStackTrace();
    }
     //validate!
     v.validate(input);
     
    
  }
  /**
   * the core method. Reads result line by line and validate it.
   */
  private void validate(Scanner in){
    
  }

  private Validator(){
    injured = new ArrayList<Person>();
    hospitals = new ArrayList<Integer>();
  }

  private void buildInput(Scanner in){
    in.nextLine();//let the first line go
    while(in.hasNextLine()){
      String[] ints = in.nextLine().split(",");
      if(ints.length==3){
        Person p = new Person(Integer.parseInt(ints[0]), 
            Integer.parseInt(ints[1]), Integer.parseInt(ints[2]));
        //  System.out.println(p.toString());
        injured.add(p);  
      }
      else if(ints[0].length()==1){
        hospitals.add(Integer.parseInt(ints[0]));
      }
    }
  }

  private class Person{
    final int x;
    final int y;
    final int time;
    Person(int x, int y, int time){
      this.x = x;
      this.y = y;
      this.time = time;
    }
    boolean isAlive(int now){
      return time<now; 
    }
    public String toString(){
      return "I am at ("+x+","+y+") with time:" + time;
    }
  }


}