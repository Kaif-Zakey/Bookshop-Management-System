package lk.ijse.windsor.Util;

import com.jfoenix.controls.JFXTextField;
import javafx.scene.paint.Paint;
import lk.ijse.windsor.Util.TextField;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Regex {
    public static boolean isTextFieldValid(TextField textField, String text){
        String filed = "";

        switch (textField){
            case ID:
                filed = "^[0-9]{1,5}$";
                break;
            case NAME:
                filed = "^[A-Za-z]+(?: [A-Za-z]+)*$";
                break;
            case EMAIL:
                filed = "^([A-z])([A-z0-9.]){1,}[@]([A-z0-9]){1,10}[.]([A-z]){2,5}$";
                break;
            case ADDRESS:
                filed = "^([A-z0-9]|[-\\,.@+]|\\\\s){4,}$";
                break;
            case QTYONHAND:
                filed = "^[0-9]{1,5}$";
                break;
            case UNITPRICE:
                filed = "^([0-9]){1,}[.]([0-9]){1,}$";
                break;
            case DESCRIPTION:
                filed = "^([A-z0-9]|[-\\,.@+]|\\\\s){4,}$";
                break;

            case CODE:
                filed ="^[0-9]{1,5}$" ;
                break;
        }

        Pattern pattern = Pattern.compile(filed);

        if (text != null){
            if (text.trim().isEmpty()){
                return false;
            }
        }else {
            return false;
        }

        Matcher matcher = pattern.matcher(text);

        if (matcher.matches()){
            return true;
        }
        return false;
    }

    public static boolean setTextColor(TextField location, javafx.scene.control.TextField textField){
        if (Regex.isTextFieldValid(location, textField.getText())){
            textField.setStyle("-fx-focus-color: #00FF00;");
            return true;
        }else {
            textField.setStyle("-fx-border-color: red;-fx-border-radius: 5;-fx-border-width: 3;");
            return false;
        }
    }
}
