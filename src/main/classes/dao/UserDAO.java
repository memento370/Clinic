package main.classes.dao;

import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import main.classes.DBClass;
import main.classes.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;

@Component
public class UserDAO {
    @Autowired()
    private DBClass useDB;
    public int save(User user){
        useDB.checkDB();

        try {
            PreparedStatement state1 = useDB.getPrepareDB();
            Connection conn = useDB.getConn();
            state1 = conn.prepareStatement(
                    "INSERT INTO ClinicUsers (login,password,firstName,lastName,surName)"
                            + " VALUES ((?),(?),(?),(?),(?))");
            state1.setString(1, user.getLogin());
            state1.setString(2, user.getPass());
            state1.setString(3, user.getName());
            state1.setString(4, user.getLastname());
            state1.setString(5, user.getSurname());
            state1.executeUpdate();
            return 0;
        }catch(MysqlDataTruncation a) {
            System.out.println("Некоректный логин или пароль,попробуйте еще раз!");
            return 1;
        }
        catch(SQLIntegrityConstraintViolationException a) {
            System.out.println("Такой логин уже сущесвтует!Придумайте другой!");
            return 2;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return 3;
        }catch(Exception a) {
            a.printStackTrace();
            return 4;
        }
    }
    public int login(User user) {
        boolean loginOk=false;
        boolean passOk=false;
        try {
            Statement state1=useDB.getStateDB();
            Statement state2=useDB.getStateDB();
            Connection conn=useDB.getConn();
            state1=conn.createStatement();
            state2=conn.createStatement();

            ResultSet resultSet=state1.executeQuery("SELECT login FROM ClinicUsers");
            ResultSet resultSet1=state2.executeQuery(
                    "SELECT password FROM ClinicUsers"
                            + " where login = '"+user.getLogin()+"'");
            while(resultSet.next()) {
                String loginCheckLogin= resultSet.getString("login");
                if(user.getLogin().equals(loginCheckLogin)) {
                    loginOk=true;
                    System.out.println("Логин совпал");
                    break;
                }
            }
            while(resultSet1.next()) {
                String loginCheckPassword = resultSet1.getString("password");
                if(user.getPass().equals(loginCheckPassword)) {
                    System.out.println("Пароль совпал");
                    passOk=true;
                    break;
                }
            }
            if(loginOk==true&&passOk==true) {
                System.out.println("Поздравляю с успешной авторизацией");
                state1.close();
                state2.close();
                resultSet.close();
                resultSet.close();
                return 0;
//                Statement statId=conn.createStatement();
//                ResultSet resSetId=statId.executeQuery("select id from ClinicUsers"
//                        + " where login ='"+loginLogin+"'");
//                while(resSetId.next()) {
//                    int loginId = resSetId.getInt("id");
//                    Clinic clinic = new Clinic(loginId);
//                    clinic.MainMenu();
//                    break;
//                }

            }else {
                System.out.println("Введен не правильный логин,или пароль.Вовзрат к авторизации.");
                state1.close();
                state2.close();
                resultSet.close();
                resultSet.close();
               // LoginUser();
                return 1;
            }
        }catch (MysqlDataTruncation a) {
            System.out.println("Некоректный логин или пароль , попробуйте еще раз!");
            //LoginUser();
            return 2;
        }catch(SQLException a) {
            a.printStackTrace();
            return 3;
        }

        catch(Exception c) {
            c.printStackTrace();
            return 4;
        }

    }
}
