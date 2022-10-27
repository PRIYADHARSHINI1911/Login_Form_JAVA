import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginForm extends JDialog{
    private JPanel loginPanel;
    private JTextField tfemail;
    private JButton loginButton;
    private JButton cancelButton;
    private JPasswordField pfpassword;

    public LoginForm(JFrame parent){
        super(parent);
        setTitle("Login");
        setContentPane(loginPanel);
        setMinimumSize(new Dimension(450,474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);





        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = tfemail.getText();
                String password = String.valueOf(pfpassword.getPassword());
                user =getAuthentication(name,password);
                if(user !=null){
                    dispose();
                }
                else{
                    JOptionPane.showMessageDialog(LoginForm.this,
                            "Email or password is incorrect",
                            "Try again",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        setVisible(true);
    }
    public User user;
    private User getAuthentication(String name,String password){
        User newuser = null;

        final String DB_URL = "jdbc:mysql://localhost/user?serverTimezone=UTC";
        final String USERNAME ="root";
        final String PASSWORD ="Priya@123";

        try{
            Connection con = DriverManager.getConnection(DB_URL, USERNAME,PASSWORD);
            Statement stat = con.createStatement();
            String sql = "SELECT * FROM users WHERE name =? AND password=?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                newuser = new User();
                newuser.name = resultSet.getString("name");
                newuser.email = resultSet.getString("email");
                newuser.address=resultSet.getString("address");
                newuser.password = resultSet.getString("password");
            }


        }
        catch (Exception e){
            e.printStackTrace();
        }
        return newuser;

    }

    public static void main(String[] args) {
        LoginForm loginForm = new LoginForm(null);
        User newuser = loginForm.user;
        if(newuser != null){
            System.out.println("Successful Authentication of : "+newuser.name);
            System.out.println("         Email : "+ newuser.email);
            System.out.println("         Address : "+newuser.address);
        }
        else{
            System.out.println("Authentication canceled");
        }
    }
}
