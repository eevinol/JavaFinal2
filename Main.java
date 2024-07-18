import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class CustomerInfoJDBC extends JFrame implements ActionListener {
    private JTextField idField, lastNameField, firstNameField, phoneField;
    private JButton prevButton, nextButton;
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    public CustomerInfoJDBC() {
        setTitle("Customer Information");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel idLabel = new JLabel("ID:");
        idLabel.setBounds(50, 50, 100, 30);
        add(idLabel);

        idField = new JTextField();
        idField.setBounds(150, 50, 150, 30);
        idField.setEditable(false);
        add(idField);

        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameLabel.setBounds(50, 100, 100, 30);
        add(lastNameLabel);

        lastNameField = new JTextField();
        lastNameField.setBounds(150, 100, 150, 30);
        lastNameField.setEditable(false);
        add(lastNameField);

        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameLabel.setBounds(50, 150, 100, 30);
        add(firstNameLabel);

        firstNameField = new JTextField();
        firstNameField.setBounds(150, 150, 150, 30);
        firstNameField.setEditable(false);
        add(firstNameField);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setBounds(50, 200, 100, 30);
        add(phoneLabel);

        phoneField = new JTextField();
        phoneField.setBounds(150, 200, 150, 30);
        phoneField.setEditable(false);
        add(phoneField);

        prevButton = new JButton("Previous");
        prevButton.setBounds(50, 250, 100, 30);
        prevButton.addActionListener(this);
        add(prevButton);

        nextButton = new JButton("Next");
        nextButton.setBounds(200, 250, 100, 30);
        nextButton.addActionListener(this);
        add(nextButton);

        connectToDatabase();
        displayCustomer();
    }

    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Eevin", "root", "1234");
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery("SELECT * FROM customers");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayCustomer() {
        try {
            if (resultSet.next()) {
                idField.setText(resultSet.getString("customer_id"));
                lastNameField.setText(resultSet.getString("customer_last_name"));
                firstNameField.setText(resultSet.getString("customer_first_name"));
                phoneField.setText(resultSet.getString("customer_phone"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getSource() == prevButton) {
                if (!resultSet.previous()) {
                    resultSet.last();
                }
            } else if (e.getSource() == nextButton) {
                if (!resultSet.next()) {
                    resultSet.first();
                }
            }
            displayCustomer();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CustomerInfoJDBC customerInfo = new CustomerInfoJDBC();
            customerInfo.setVisible(true);
        });
    }
}
