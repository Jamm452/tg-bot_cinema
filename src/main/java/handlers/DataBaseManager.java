package handlers;

import properties.Film;
import properties.Session;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class DataBaseManager {
    private String URL;
    private String LOGIN;
    private String PASSWORD;
    private Connection connection;
    private Scanner scanner;
    private static DataBaseManager instance;
    private List<Film> listOfFilms = new LinkedList<>();
    private List<Session> listOfSessions = new LinkedList<>();
    private List<Film> filmsNotInRent = new LinkedList<>();

    private DataBaseManager() {
        try {
            String DB_DRIVER = "org.postgresql.Driver";
            Class.forName(DB_DRIVER);
            System.out.println("PostgreSQL JDBC Driver успешно поключен");
            String dir = "c:\\sql\\sql.txt";
            scanner = new Scanner(new FileReader(dir));
        } catch (FileNotFoundException | ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("Файл с данными для входа в базу данных не найден");
            System.exit(-1);
        }
        try {
            this.LOGIN = scanner.nextLine().trim();
            this.PASSWORD = scanner.nextLine().trim();
            this.URL = scanner.nextLine().trim();
        } catch (NoSuchElementException e) {
            System.out.println("В файле не найдены данные для входа. Обновите файл.");
            System.exit(-1);
        }
    }

    public static DataBaseManager getInstance() {
        if (instance == null) {
            instance = new DataBaseManager();
        }
        return instance;
    }

    public void connectToDB() {
        try {
            connection = DriverManager.getConnection(URL, LOGIN, PASSWORD);
            System.out.println("Подключение к базе данных установлено");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Не удалось подключиться к базе данных");
            System.exit(-1);
        }
    }

    public List<Film> getFilmsFromDB() {
        try {
            listOfFilms.clear();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM FILM");
            ResultSet result = preparedStatement.executeQuery();
            if (result != null) {
                while (result.next()) {
                    Film film = handleDBResultAsFilm(result);
                    if (film.isInRent()) listOfFilms.add(film);
                    else filmsNotInRent.add(film);
                }
                preparedStatement.close();
            }
            return listOfFilms;
        } catch (SQLException e) {
            System.out.println("Error while accessing DB");
            return null;
        }
    }

    public List<Session> getSessionsFromDB() {
        try {
            listOfSessions.clear();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM SEANCE");
            ResultSet result = preparedStatement.executeQuery();
            if (result != null) {
                while (result.next()) {
                    Session session = handleDBResultAsSession(result);
                    listOfSessions.add(session);
                }
                preparedStatement.close();
            }
            return listOfSessions;
        } catch (SQLException e) {
            System.out.println("Error while accessing DB");
            return null;
        }
    }

    public Film handleDBResultAsFilm(ResultSet result) {
        try {
            String description = result.getString("description");
            String posterName = result.getString("poster_name");
            String title = result.getString("title");
            String trailer = result.getString("trailer_url");
            Boolean isInRent = result.getBoolean("in_rent");
            int id = result.getInt("id");
            return new Film(id, title, description, posterName, trailer, isInRent);
        } catch (SQLException e) {
            System.out.println("Ошибка при чтении коллекции с базы данных.");
            return null;
        }
    }

    private Session handleDBResultAsSession(ResultSet result) {
        try {
            int filmId = result.getInt("film_id");
            Timestamp date = result.getTimestamp("date");
            int id = result.getInt("id");
            return new Session(id, date.toLocalDateTime(), filmId);
        } catch (SQLException e) {
            System.out.println("Ошибка при чтении коллекции с базы данных.");
            return null;
        }
    }

    public List<Film> getListOfFilms() {
        return listOfFilms;
    }
}