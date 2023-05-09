package main.classes;

import org.springframework.stereotype.Component;

import java.sql.*;

@Component
public class DBClass {

    private String userName = "root";
    private String password = "pass";
    private String connectionUrl = "jdbc:mysql://localhost:3306/ClinicDB";
    private Connection conn;
    private Statement stat;
    private PreparedStatement prepareStat;
    private boolean initialize = false;


    public void checkDB()  {

        if (initialize = false) {
            initializeDB();
        }
    }

    private void initializeDB() {
        System.out.println("InitDB");
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection connection = DriverManager.getConnection(connectionUrl, userName, password);
                conn=connection;
                stat = conn.createStatement();
                stat.executeUpdate("create table IF NOT EXISTS ClinicUsers"
                        + "(id int not null auto_increment,"
                        + "login VARCHAR(16)not null UNIQUE,"
                        + "password VARCHAR(16) not null,"
                        + "firstName VARCHAR(16) not null,"
                        + "lastName VARCHAR(16) not null,"
                        + "surName VARCHAR(16) not null,"
                        + "primary key (id));");

                stat.executeUpdate("create table IF NOT EXIST ClinicStaff"
                        + " (idStaff int ,"
                        + "firstName VARCHAR(16) not null,"
                        + "lastName VARCHAR(16) not null,"
                        + "surName VARCHAR(16) not null,"
                        + "profession VARCHAR(16) not null,"
                        + "Specialty  VARCHAR(16) not null,"
                        + ""
                        + "primary key (id));");
                System.out.println("s");

                initialize = true;

            } catch (ClassNotFoundException e) {
                System.out.println("Ошибка инициализации драйвера JDBC");
                e.printStackTrace();
            } catch (SQLException a) {
                System.out.println();
            }
        }
        public Statement getStateDB () {
            System.out.println("Инициализирую дб");
            //checkDB();
            initializeDB();
            System.out.println("Вовзращаю стейтмент");
            return stat;
        }
        public PreparedStatement getPrepareDB () {
            //checkDB();
            initializeDB();
            return prepareStat;
        }
        public Connection getConn () {
            //checkDB();
            initializeDB();
            System.out.println("Вовзращаю конекшен");
            return conn;
        }

    }

