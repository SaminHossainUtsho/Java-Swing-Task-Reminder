package reminder;
import com.toedter.calendar.JDateChooser;import java.awt.AWTException;import java.awt.Color;import java.awt.Container;import java.awt.Font;import java.awt.Image;import java.awt.SystemTray;import java.awt.Toolkit;import java.awt.TrayIcon;import java.awt.event.ActionEvent;import java.io.BufferedWriter;import java.io.File;import java.io.FileNotFoundException;import java.io.FileWriter;import java.io.IOException;import java.text.SimpleDateFormat;import java.util.Date;import java.util.Scanner;import javax.swing.ImageIcon;import javax.swing.JButton;import javax.swing.JComboBox;import javax.swing.JFrame;import javax.swing.JLabel;import javax.swing.JOptionPane;import javax.swing.JScrollPane;import javax.swing.JTable;import javax.swing.JTextField;import javax.swing.Timer;import javax.swing.table.DefaultTableModel;
class Reminder extends JFrame 
{     
JTextField task; JLabel top_date,top_time;  JTable table,tb; DefaultTableModel model,mo; JScrollPane scroll,sc; JButton ab,db,clr,done,view,cancel; JDateChooser jdc; JComboBox hh_box,mm_box,box; Font font = new Font("Digital-7",Font.PLAIN,20),time_font = new Font("Digital-7",Font.PLAIN,38);Date dt = new Date(); SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); int i ; String col[ ] = new String[3] ,current_ap,current_time,get_ap,get_time,row[] = {"Upcoming","Date (yy-mm-dd)","Schedule"},am_pm[] = {"AM","PM"},mm[] = {"00","05","10","15","20","25","30","35","40","45","50","55"},hh[]={"12","01","02","03","04","05","06","07","08","09","10","11"};
Reminder()
   {  
      Container con = this.getContentPane();  con.setLayout(null);  con.setBackground(Color.GRAY); 
      top_time = new JLabel(); top_time.setBounds(90,0,300,60);top_time.setFont(time_font);top_time.setForeground(Color.CYAN);con.add(top_time);     

      new Timer(1000, (ActionEvent ae) -> 
      {
          Date date = new Date();
          SimpleDateFormat time = new SimpleDateFormat("hh:mm:ss a");
          top_time.setText(time.format(date));
          if(top_time.getText().compareTo("12:00:00 PM") == 0)
          {  
              try
              {  
                  Reminder ob = new Reminder(); 
                  ob.display(); 
              } catch (AWTException ex) { } 
          }
      }).start();      
      
      view = new JButton();view.setBounds(370,20,50,30);con.add(view);
      notify_me();      
      top_date = new JLabel();top_date.setBounds(30,60,200,30);top_date.setForeground(Color.WHITE);top_date.setFont(font);top_date.setText(sdf.format(dt));con.add(top_date);                  
      task = new JTextField();task.setBounds(260,80,160,35);task.setBackground(Color.DARK_GRAY);task.setFont(font);task.setForeground(Color.CYAN);con.add(task);      
      jdc = new JDateChooser(dt);jdc.setBounds(260,130,163,36);jdc.setBackground(Color.GRAY);con.add(jdc); 
      hh_box = new JComboBox(hh);hh_box.setBounds(260,180,50,30);hh_box.setMaximumRowCount(hh_box.getModel().getSize());con.add(hh_box);mm_box = new JComboBox(mm);mm_box.setBounds(318,180,50,30);mm_box.setMaximumRowCount(mm_box.getModel().getSize());con.add(mm_box);box = new JComboBox(am_pm);box.setBounds(374,180,50,30);con.add(box);      
      clr = new JButton("\u2716"); clr.setBounds(260,220,50,30); con.add(clr); db = new JButton("\u2707"); db.setBounds(318,220,50,30); con.add(db);  ab = new JButton("\u271A");ab.setBounds(374,220,50,30);con.add(ab);     
      table = new JTable();model = new DefaultTableModel();model.setColumnIdentifiers(row);table.setModel(model);table.setBackground(Color.BLACK);table.setForeground(Color.WHITE);table.setFont(font);table.setRowHeight(26);scroll = new JScrollPane(table);scroll.setBounds(0,260,434,360);con.add(scroll);    
      File file = new File("upcoming.txt");
      try {
                Scanner read = new Scanner(file);
                while(read.hasNext())
                    {
                     col[0] = read.next(); 
                     col[1] = read.next(); 
                     col[2] = read.next();                   
                    if(col[1].compareTo(top_date.getText()) > 0)                       
                         model.addRow(col) ;
                    else  
                      todo_file(col[0],col[1],col[2]);                           
                    }       
            } catch (FileNotFoundException ex){ } 
      
update_upcoming_file();
clr.addActionListener((ActionEvent ae) -> {  task.setText("");  table.clearSelection();  task.requestFocusInWindow(); });    
ab.addActionListener((ActionEvent ae) ->
{ 
               col[0] = task.getText().replace(' ', '-');
               col[1] = sdf.format(jdc.getDate());
               col[2]=hh_box.getSelectedItem()+ ":" + mm_box.getSelectedItem()+"("+box.getSelectedItem()+")";                 
               
               if(task.getText().isEmpty())                
               {
                   ImageIcon similar = new ImageIcon("Task.jpg");
                   JOptionPane.showMessageDialog(null,"Enter Task Name"," Addition Failed",JOptionPane.INFORMATION_MESSAGE,similar);
               }
               else if(check_data(col[0],col[1],col[2]) == 0) 
               {
                   ImageIcon similar = new ImageIcon("Task.jpg");
                   JOptionPane.showMessageDialog(null,"Similar Task Found","Addition Failed",JOptionPane.INFORMATION_MESSAGE,similar);
               }                    
               else {                             
                      if(col[1].compareTo(top_date.getText()) <= 0)
                                 todo_file(col[0],col[1],col[2]);
                      else { 
                                 model.addRow(col); 
                                 try {   
                                          FileWriter fw  = new FileWriter("upcoming.txt",true);
                                         try (BufferedWriter bw = new BufferedWriter(fw))
                                              {
                                                  bw.write(col[0]+" "+col[1]+" "+col[2]+"\n"); 
                                              } 
                                       } catch (IOException ex){ } 
                             } 
                   } 
task.requestFocusInWindow();
notify_me();
});     

db.addActionListener((ActionEvent ae) -> { 
        while(table.getSelectedRow() >= 0)
                {            
                  model.removeRow(table.getSelectedRow());                        
                }  
             task.requestFocusInWindow();
             update_upcoming_file(); 
}); 

view.addActionListener((ActionEvent ae1) ->
{         
            String r[]= {"Todo","Date (yy-mm-dd)","Schedule"} , kol[ ] = new String[3];           
            JFrame ob = new JFrame();ob.setVisible(true);ob.setBounds(460,20,440,650);ob.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);ob.setResizable(false);
            Container c = ob.getContentPane();c.setLayout(null); c.setBackground(Color.GRAY);
            Image icon = Toolkit.getDefaultToolkit().getImage("alarm.png");
            ob.setIconImage(icon);
            cancel = new JButton("\u2716"); cancel.setBounds(50,10,50,30); c.add(cancel); 
            done = new JButton("\u2714");done.setBounds(330,10,50,30);c.add(done);
            tb= new JTable(); mo= new DefaultTableModel();mo.setColumnIdentifiers(r);tb.setModel(mo);tb.setFont(font);tb.setRowHeight(26);tb.setBackground(Color.BLACK);tb.setForeground(Color.WHITE);sc = new JScrollPane(tb);sc.setBounds(0,50,434,570);c.add(sc);
    try   {
               File ftodo = new File("todo.txt");
               Scanner scan = new Scanner(ftodo);
       
            if(ftodo.length() != 0)
               {
                   while(scan.hasNext())
                    {
                     kol[0] = scan.next();
                     kol[1] = scan.next(); 
                     kol[2] = scan.next();
         if(kol[1].compareTo(top_date.getText()) < 0 )
                   {  
                       kol[2] = "Overdue";
                   }
         else if(kol[1].compareTo(top_date.getText()) == 0  &&  kol[2].compareTo("Overdue") != 0)
                   {
                             get_ap = kol[2].substring(6, 8);                
                             current_ap = top_time.getText().substring(9, 11);                                               
                     if(kol[2].substring(0, 2).compareTo("12") == 0)                        
                             get_time = "00"+ kol[2].substring(2,5);                     
                      else 
                             get_time = kol[2].substring(0, 5);
                      if(top_time.getText().substring(0, 2).compareTo("12") == 0)                        
                             current_time = "00"+ top_time.getText().substring(2, 5);
                       else 
                             current_time = top_time.getText().substring(0, 5);
                       if(get_ap.equals(current_ap)  && get_time.compareTo(current_time) < 0)
                              kol[2] = "Overdue";               
                       else if(get_ap.equals("AM")  &&  current_ap.equals("PM"))                       
                              kol[2] = "Overdue";                       
                   }
                   mo.addRow(kol);
            }  
          }
          else
               {
                   ImageIcon chill = new ImageIcon("CHILL.jpg");
JOptionPane.showMessageDialog(null,"Nothing Todo Today","Good News \u263A \u263A \u263A",JOptionPane.INFORMATION_MESSAGE,chill);                   
               }
         }
     catch (FileNotFoundException ex){ }                      
sc.requestFocusInWindow();           
done.addActionListener((ActionEvent ae) -> {
          while(tb.getSelectedRow()>=0) 
                {
                    mo.removeRow(tb.getSelectedRow());
                }
          try {
                    FileWriter fw  = new FileWriter("todo.txt",false);
                try (BufferedWriter bw = new BufferedWriter(fw)) {
                        for(i=0; i<tb.getRowCount();i++)
                        {
                            bw.write((tb.getValueAt(i, 0))+" "+(tb.getValueAt(i, 1))+" "+(tb.getValueAt(i, 2))+"\n");
                        }
                    }
                } catch (IOException ex) { }  sc.requestFocusInWindow();
                notify_me();
});  
cancel.addActionListener((ActionEvent ae) -> {  tb.clearSelection();  sc.requestFocusInWindow();  });  
task.requestFocusInWindow(); });
}  
private int check_data(String task,String date,String time) {    // SIMPLE AI TECHNOLOGY
            for(i=0; i<table.getRowCount();i++)
                { 
                   if(task.equals(table.getValueAt(i, 0))  &&  date.equals(table.getValueAt(i, 1))  && time.equals(table.getValueAt(i, 2)) )
                   return 0;
                }
    return 1;
}
private void notify_me() 
{
    File look = new File("todo.txt");
    if(look.length() == 0)
        view.setText("\u279C");
    else
        view.setText("\u24C2");
}

private void update_upcoming_file() {
          try {
              FileWriter fw  = new FileWriter("upcoming.txt",false);
              try (BufferedWriter bw = new BufferedWriter(fw)) 
                 {
                  for(i=0; i<table.getRowCount();i++)                                          
                       { 
                           bw.write((table.getValueAt(i, 0))+" "+(table.getValueAt(i, 1))+" "+(table.getValueAt(i, 2))+"\n");
                       }
                 }
              } catch (IOException ex){ }
} 
private void todo_file(String name,String date,String time) {
        try {
              FileWriter fw  = new FileWriter("todo.txt",true);
              try (BufferedWriter bw = new BufferedWriter(fw))
                     {
                          bw.write(name+" "+date+" "+time+"\n")  ;
                     }
             } catch (IOException ex){ }
}
public void display() throws AWTException 
{
        SystemTray tray  =  SystemTray.getSystemTray();
        TrayIcon trayicon = new TrayIcon(Toolkit.getDefaultToolkit().getImage(""),"");
        tray.add(trayicon);  
        File file = new File("todo.txt"); 
        if(file.length() == 0)
               trayicon.displayMessage("Nothing ToDo Today","\u263A \u263A \u263A", TrayIcon.MessageType.INFO);
        else
               trayicon.displayMessage("You Have Incomplete Task","\u231A \u231A \u231A", TrayIcon.MessageType.INFO);       
} 

public static void main(String[ ] args) throws Exception 
{       
      for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) 
            {
                 if ("Nimbus".equals(info.getName())) 
                    {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());  
                        break; 
                    }
            }
      Reminder ob = new Reminder();
      ob.setBounds(460,20,440,650);
      ob.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      ob.setResizable(false);
      Image icon = Toolkit.getDefaultToolkit().getImage("alarm.png");
      ob.setIconImage(icon);
      Thread.sleep(2000);
      ob.setVisible(true);
      ob.task.requestFocusInWindow();
      ob.display(); 
} 

}      
/*
   Calendar cal = new GregorianCalendar();                       
   day = cal.get(Calendar.DAY_OF_MONTH);
   month = cal.get(Calendar.MONTH) + 1;
   year = cal.get(Calendar.YEAR); 
   second = cal.get(Calendar.SECOND);
   minute = cal.get(Calendar.MINUTE);
   hour = cal.get(Calendar.HOUR);
   System.out.println();
   ImageIcon image = new ImageIcon("alarm.png");ob.setIconImage(image.getImage());
*/