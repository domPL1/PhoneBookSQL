package main;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import main.Person.Sex;
import static main.Person.Sex.FEMALE;
import static main.Person.Sex.MALE;

public class FXMLDocumentController implements Initializable {
    ArrayList<Person> data = new ArrayList();
    SingletonSQL database;
    Alert inf;
    Alert alert;
    Date birth;
    TextInputDialog entry;
    ObjectInputStream in;
    ObjectOutputStream out;
    FileChooser select;
    @FXML
    private MenuItem menu1_1;
    @FXML
    private MenuItem menu1_2;
    @FXML
    private MenuItem menu1_4;
    @FXML
    private MenuItem menu1_3;
    @FXML
    private MenuItem menu2_1;
    @FXML
    private MenuItem menu2_2;
    @FXML
    private MenuItem menu2_3;
    @FXML
    private MenuItem menu3_1;
    @FXML
    private MenuItem menu3_2;
    @FXML
    private TextArea text;
    @FXML
    private void about(){
        Alert asd = new Alert(Alert.AlertType.INFORMATION);
        asd.setTitle("PhoneBook");
        asd.setContentText("To start you need to create or open file. Then you can add,modify or delete entries."
                + " You can also import and export from and to mysql database. To close you have to save file and use exit in menu.");
        asd.showAndWait();
    }
    @FXML
    private void create(){
        select = new FileChooser();
        ExtensionFilter a = new ExtensionFilter("Plik pbk (*.pbk)","*.pbk");
        select.getExtensionFilters().add(a);
        try {
            out = new ObjectOutputStream(new FileOutputStream((select.showSaveDialog(null))));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        menu1_1.setDisable(true);
        menu1_2.setDisable(true);
        menu1_4.setDisable(true);
        menu1_3.setDisable(false);
        menu2_1.setDisable(false);
        menu2_2.setDisable(false);
        menu2_3.setDisable(false);
        menu3_1.setDisable(false);
    }
    @FXML
    private void open()
    {
        select = new FileChooser();
        ExtensionFilter a = new ExtensionFilter("Plik pbk (*.pbk)","*.pbk");
        select.getExtensionFilters().add(a);
        File plik = select.showOpenDialog(null);
        try {
            in = new ObjectInputStream(new FileInputStream(plik));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try{
            while(true) 
            {
                data.add((Person) in.readObject());
            }
        }catch (EOFException e) {
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Collections.sort(data, (Person o1, Person o2) -> o1.compareTo(o2));
        for (int i=0;i<data.size();i++){
                 data.get(i).setIndex(Integer.toString(i));
                 text.appendText(data.get(i).printPerson());
            }
        try {
            out = new ObjectOutputStream(new FileOutputStream((plik)));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        menu1_1.setDisable(true);
        menu1_2.setDisable(true);
        menu1_3.setDisable(false);
        menu1_4.setDisable(true);
        menu2_1.setDisable(false);
        menu2_2.setDisable(false);
        menu2_3.setDisable(false);
        menu3_1.setDisable(false);
    }
    @FXML
    private void save() throws IOException
    {
        for (int i=0;i<data.size();i++)
        {
            out.writeObject(data.get(i));
        }
        data = new ArrayList();
        out.close();
        text.setText("");
        menu1_1.setDisable(false);
        menu1_2.setDisable(false);
        menu1_3.setDisable(true);
        menu1_4.setDisable(false);
        menu2_1.setDisable(true);
        menu2_2.setDisable(true);
        menu2_3.setDisable(true);
        menu3_1.setDisable(true);
    }
    @FXML
    private void create1()
    {
        if (out==null)
        {
            inf=new Alert(Alert.AlertType.ERROR);
            inf.setTitle("Phone Book");
            inf.setHeaderText("Add");
            inf.setContentText("There is no .pbk file open");
            inf.showAndWait();
            
        }
        else
        {
         entry = new TextInputDialog();
         entry.setTitle("Phone Book");
         entry.setHeaderText("Add");
         entry.setContentText("Enter a name:");
         String name = entry.showAndWait().get();
         alert = new Alert(Alert.AlertType.CONFIRMATION);
         alert.setTitle("Phone Book");
         alert.setHeaderText("Add");
         alert.setContentText("Choose Sex");
         ButtonType buttonTypeOne = new ButtonType("Male");
         ButtonType buttonTypeTwo = new ButtonType("Female");
         alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
         Optional<ButtonType> anser = alert.showAndWait();
         Sex sex = null;
         if (anser.get()==buttonTypeOne)
         {
             sex = MALE;
         }
         else if (anser.get()==buttonTypeTwo)
         {
             sex = FEMALE;
         }
         String email;
            boolean ex = false;
         do{
             entry.setContentText("Entry e-mail adress:");
             email = entry.showAndWait().get();
             int i = email.indexOf('.');
             int a = email.indexOf("@");
             if (i!=-1&&a!=-1) ex=true;
         } while (ex==false);
         data.add(new Person(String.valueOf(data.size()+1),name,sex,email));
         Collections.sort(data, (Person o1, Person o2) -> o1.compareTo(o2));
         text.setText("");
         for (int i=0;i<data.size();i++)
         {
             data.get(i).setIndex(String.valueOf(i));
             text.appendText(data.get(i).printPerson());
         }
    }}
    @FXML
    private void delete()
    {
        entry = new TextInputDialog();
        if (data==null)
        {
            inf = new Alert(Alert.AlertType.ERROR);
            inf.setTitle("Phone Book");
            inf.setHeaderText("Delete");
            inf.setContentText("There is no contacts");
            inf.showAndWait();
        }
      entry.setTitle("Phone Book");
      entry.setHeaderText("Delete");
      entry.setContentText("Enter a index:");
      String index = entry.showAndWait().get();
      int ind = 0;
      for (int i=0;i<data.size();i++)
      {
          if (data.get(i).getIndex().equals(index)){
              data.remove(i);
              ind=1;
          }
      }
      if (ind==0)
      {
         inf = new Alert(Alert.AlertType.ERROR);
         inf.setTitle("Phone Book");
         inf.setHeaderText("Delete");
         inf.setContentText("There is no contact with " + Integer.toString(ind) + " index");
      }
      text.setText("");
             Collections.sort(data, Person::compareTo);

         for (int i=0;i<data.size();i++)
         {
             data.get(i).setIndex(String.valueOf(i));
             text.appendText(data.get(i).printPerson());
         }
    }
    
    @FXML
    private void modify(){
        entry = new TextInputDialog();
        entry.setTitle("Phone Book");
        entry.setHeaderText("Modify");
        entry.setContentText("Enter index of entry to modify:");
        String ind = entry.showAndWait().get();
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Phone Book");
        alert.setHeaderText("Modify");
        alert.setContentText("Which information would you like to modify?");
        ButtonType buttonTypeOne = new ButtonType("Name");
        ButtonType buttonTypeThree = new ButtonType("Sex");
        ButtonType buttonTypeFour = new ButtonType("E-Mail");
        ButtonType buttonTypeFive = new ButtonType("End");
        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeThree, buttonTypeFour, buttonTypeFive);
        boolean loop=false;
        do{
        Optional<ButtonType> anser = alert.showAndWait();
        if (anser.get()==buttonTypeOne){
            entry.setTitle("Phone Book");
            entry.setHeaderText("Add");
            entry.setContentText("Enter a name:");
            Person temp = data.get(Integer.parseInt(ind));
            temp.setName(entry.showAndWait().get());
            data.set(Integer.parseInt(ind), temp);
         }
         else if (anser.get()==buttonTypeThree){
             alert.setTitle("Phone Book");
             alert.setHeaderText("Add");
             alert.setContentText("Choose Sex");
             ButtonType buttonType1 = new ButtonType("Male");
             ButtonType buttonType2 = new ButtonType("Female");
             alert.getButtonTypes().setAll(buttonType1, buttonType2);
             Optional<ButtonType> anser1 = alert.showAndWait();
             Sex sex = null;
         if (anser1.get()==buttonType1)
         {
             sex = MALE;
         }
         else if (anser1.get()==buttonType2)
         {
             sex = FEMALE;
         }
         Person temp = data.get(Integer.parseInt(ind));
         temp.setGender(sex);
         data.set(Integer.parseInt(ind),temp);
         }
         else if (anser.get()==buttonTypeFour){
             entry.setContentText("Entry e-mail adress:");
             String email;
         Boolean ex=false;
         do{
             entry.setContentText("Entry e-mail adress:");
             email = entry.showAndWait().get();
             int i = email.indexOf('.');
             int a = email.indexOf("@");
             if (i!=-1&&a!=-1) ex=true;
         } while (ex==false);
             Person temp = data.get(Integer.parseInt(ind));
             temp.setEmailAddress(email);
             data.set(Integer.parseInt(ind),temp);
         }
         else if (anser.get()==buttonTypeFive){
             break;
         }
    }while (loop==true);
         text.setText("");
         Collections.sort(data, (Person o1, Person o2) -> o1.compareTo(o2));
         text.setText("");
         for (int i=0;i<data.size();i++)
         {
             data.get(i).setIndex(Integer.toString(i));
             text.appendText(data.get(i).printPerson());
         }
    }
    @FXML
    private synchronized void impor(){
        boolean flag=false;
        ResultSet result = null;
        int fl=0;
        do{
        entry = new TextInputDialog();
        entry.setTitle("PhoneBook");
        entry.setHeaderText("Database URL");
        entry.setContentText("Entry database url(example jdbc:mysql://localhost:3306/test) :");
        String url = entry.showAndWait().get();
        entry.setHeaderText("Database Login");
        entry.setContentText("Entry database login:");
        String login = entry.showAndWait().get();
        entry.setHeaderText("Database Password");
        entry.setContentText("Entry database password:");
        String password = entry.showAndWait().get();
        database = SingletonSQL.access();        
            fl = database.createConnection(url, login, password);
            if (fl!=1){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("PhoneBook");
                alert.setHeaderText("Connection Error");
                alert.showAndWait();
            }
        }while(fl!=1);
        try {PreparedStatement p = database.conn.prepareStatement("SELECT * FROM phonebook");
            result = p.executeQuery();
        } catch (SQLException ex) {
            System.out.println("Error in query" + ex.getMessage());
        }
        try {
        while(result.next()){
        data.add(new Person(String.valueOf(data.size()+1),result.getString(2),Sex.valueOf(result.getString(3)),result.getString(4)));
        }
        } catch (SQLException ex) {
        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        flag=true;
        }
        try {
        result.close();
        } catch (SQLException ex) {
        Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Collections.sort(data, (Person o1, Person o2) -> o1.compareTo(o2));
        text.setText("");
        for (int i=0;i<data.size();i++)
        {
        data.get(i).setIndex(Integer.toString(i));
        text.appendText(data.get(i).printPerson());
        }
        if (flag==false){
         menu3_1.setDisable(true);
         menu3_2.setDisable(false);}
    }
    @FXML
    private void export(){
        database.executeUpdate("TRUNCATE phonebook;");
        for (int i=0;i<data.size();i++){
            database.executeUpdate("INSERT INTO phonebook VALUES('" + data.get(i).getIndex()+"','" + data.get(i).getName()+"','"+data.get(i).getGender()+"','"+data.get(i).getEmailAddress()+"');");
        }
        menu3_1.setDisable(false);
        menu3_2.setDisable(true);
    }
   @FXML
   private void ex(){
       System.exit(0);
   }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
