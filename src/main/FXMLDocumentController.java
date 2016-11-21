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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
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
    private TextArea text;
    @FXML
    private void create() throws FileNotFoundException, IOException
    {
        select = new FileChooser();
        ExtensionFilter a = new ExtensionFilter("Plik pbk (*.pbk)","*.pbk");
        select.getExtensionFilters().add(a);
        out = new ObjectOutputStream(new FileOutputStream((select.showSaveDialog(null))));
        menu1_1.setDisable(true);
        menu1_2.setDisable(true);
        menu1_4.setDisable(true);
        menu1_3.setDisable(false);
        menu2_1.setDisable(false);
        menu2_2.setDisable(false);
        menu2_3.setDisable(false);
    }
    @FXML
    private void open() throws FileNotFoundException, IOException, ClassNotFoundException
    {
        select = new FileChooser();
        ExtensionFilter a = new ExtensionFilter("Plik pbk (*.pbk)","*.pbk");
        select.getExtensionFilters().add(a);
        File plik = select.showOpenDialog(null);
        in = new ObjectInputStream(new FileInputStream(plik));
        try{
            while(true) 
            {
                data.add((Person) in.readObject());
            }
        }catch (EOFException e) {
        }finally{
            in.close();
        }
        for (int i=0;i<data.size();i++){
                 text.appendText(data.get(i).printPerson());
            }
        out = new ObjectOutputStream(new FileOutputStream((plik)));
        menu1_1.setDisable(true);
        menu1_2.setDisable(true);
        menu1_3.setDisable(false);
        menu1_4.setDisable(true);
        menu2_1.setDisable(false);
        menu2_2.setDisable(false);
        menu2_3.setDisable(false);
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
    }
    @FXML
    private void create1() throws ParseException
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
         entry.setContentText("Enter birtday(Month DD, YYYY):");
         DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
         Boolean ex=false;
         do{
         try{   birth = format.parse(entry.showAndWait().get());
                ex=true;
         }
         catch (ParseException e){
             inf = new Alert(Alert.AlertType.ERROR);
             inf.setTitle("Phone Book");
             inf.setHeaderText("Add");
             inf.setContentText("Wrong data entered");
             inf.showAndWait();
             ex=false;
         }}
         while (ex==false);
         String email;
         ex=false;
         do{
             entry.setContentText("Entry e-mail adress:");
             email = entry.showAndWait().get();
             int i = email.indexOf('.');
             int a = email.indexOf("@");
             if (i!=-1&&a!=-1) ex=true;
         } while (ex==false);
         data.add(new Person(String.valueOf(data.size()+1),name,birth,sex,email));
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
    private void modify() throws ParseException{
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
        ButtonType buttonTypeTwo = new ButtonType("BirthDay");
        ButtonType buttonTypeThree = new ButtonType("Sex");
        ButtonType buttonTypeFour = new ButtonType("E-Mail");
        ButtonType buttonTypeFive = new ButtonType("End");
        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree, buttonTypeFour, buttonTypeFive);
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
         else if (anser.get()==buttonTypeTwo){
            entry.setTitle("Phone Book");
            entry.setHeaderText("Add");
            entry.setContentText("Enter a birthday:");
            Person temp = data.get(Integer.parseInt(ind));
            Boolean ex=false;
            entry.setContentText("Enter birtday(Month DD, YYYY):");
         DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
         do{
         try{   birth = format.parse(entry.showAndWait().get());
                ex=true;
         }
         catch (ParseException e){
             inf = new Alert(Alert.AlertType.ERROR);
             inf.setTitle("Phone Book");
             inf.setHeaderText("Add");
             inf.setContentText("Wrong data entered");
             inf.showAndWait();
             ex=false;
         }}
         while (ex==false);
            temp.setBirthday(birth);
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
             text.appendText(data.get(i).printPerson());
         }
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
