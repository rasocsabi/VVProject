import javax.swing.*;

public class RegistrationPanel extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton registerButton;

    public RegistrationPanel() {
        setTitle("Regisztrációs Panel");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2));

        JLabel usernameLabel = new JLabel("Felhasználónév:");
        usernameField = new JTextField();

        JLabel passwordLabel = new JLabel("Jelszó:");
        passwordField = new JPasswordField();

        registerButton = new JButton("Regisztráció");
        registerButton.addActionListener(e -> register());

        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(new JLabel()); // Üres címke a panel egyenletes elrendezéséhez
        add(registerButton);

        setVisible(true);
    }

    private void register() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // Itt hajtsd végre a regisztrációs logikát

        JOptionPane.showMessageDialog(this, "Regisztráció sikeres!");
        clearFields();
    }

    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegistrationPanel::new);
    }
}