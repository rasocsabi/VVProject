//package com.example.signup;
//
////EZ MÉG JAViTVA LESZ
//import com.example.signup.SignUpController;
//import javafx.event.ActionEvent;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Button;
//import javafx.scene.control.RadioButton;
//import javafx.scene.control.TextField;
//import javafx.scene.control.ToggleGroup;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.Mockito;
//
//public class SignUpControllerTest {
//
//    private SignUpController signUpController;
//    private Button mockButtonSignUp;
//    private Button mockButtonLogIn;
//
//    @Before
//    public void setUp() {
//        signUpController = new SignUpController();
//
//        // Mock ; gombok
//        mockButtonSignUp = Mockito.mock(Button.class);
//        mockButtonLogIn = Mockito.mock(Button.class);
//        signUpController.Button_SignUp = mockButtonSignUp;
//        signUpController.Button_LogIn = mockButtonLogIn;
//    }
//
//    @Test
//    public void testSignUpButtonAction() {
//        // Mock ; szöveg
//        TextField mockTextFieldUsername = Mockito.mock(TextField.class);
//        TextField mockTextFieldPassword = Mockito.mock(TextField.class);
//        signUpController.TextField_Username = mockTextFieldUsername;
//        signUpController.TextField_Password = mockTextFieldPassword;
//
//        //mock ; gombok2
//        ToggleGroup mockToggleGroup = Mockito.mock(ToggleGroup.class);
//        RadioButton mockRadioButtonCreator = Mockito.mock(RadioButton.class);
//        RadioButton mockRadioButtonConsumer = Mockito.mock(RadioButton.class);
//        Mockito.when(mockRadioButtonCreator.isSelected()).thenReturn(true);
//        Mockito.when(mockToggleGroup.getSelectedToggle()).thenReturn(mockRadioButtonCreator);
//        signUpController.RadioButton_Creator = mockRadioButtonCreator;
//        signUpController.RadioButton_Consumer = mockRadioButtonConsumer;
//
//        // Mock DatabaseUtils class
//        ActionEvent mockActionEvent = Mockito.mock(ActionEvent.class);
//        Mockito.doAnswer(invocation -> {
//            String toggleName = ((RadioButton) mockToggleGroup.getSelectedToggle()).getText();
//            if (!mockTextFieldUsername.getText().trim().isEmpty() && !mockTextFieldPassword.getText().trim().isEmpty()) {
//                return null;
//            } else {
//                System.out.println("Please fill all information!");
//                Alert alert = new Alert(Alert.AlertType.ERROR);
//                alert.setContentText("Please fill all information!");
//                alert.show();
//                return null;
//            }
//        }).when(mockButtonSignUp).setOnAction(Mockito.any());
//
//        mockButtonSignUp.getOnAction().handle(mockActionEvent);
//
//
//    }
//
//    @Test
//    public void testLogInButtonAction() {
//        // Mock  DatabaseUtils class
//        ActionEvent mockActionEvent = Mockito.mock(ActionEvent.class);
//        Mockito.doAnswer(invocation -> {
//            return null;
//        }).when(mockButtonLogIn).setOnAction(Mockito.any());
//
//
//        mockButtonLogIn.getOnAction().handle(mockActionEvent);
//
//    }
//}
