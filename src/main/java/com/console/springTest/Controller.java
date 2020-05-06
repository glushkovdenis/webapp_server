package com.console.springTest;

    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.web.bind.annotation.*;
    import springfox.documentation.swagger2.annotations.EnableSwagger2;

    import javax.sql.DataSource;
import java.sql.*;

/**
 * Контроллер РЕСТ-запросов.
 */

@RestController
public class Controller {
    /**
     * dataSource - это класс настроек для доступа к БД.
     */
    @Autowired
    DataSource dataSource;

    /**
     * Метод реализует HTTP-запрос GET с параметром surname.
     * @param surname - значение параметра, по которому будет выполнен поиск по фамилиям в БД.
     * @return отправляет набор записей по Http протоколу в клиентскую программу.
     * @throws SQLException - в отличие от большинства ошибок, SQLException возвращает не только ошибки,
     * но и информацию об успешном завершении запроса.
     */
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String get(@RequestParam String surname) throws SQLException {
        return substringSearch(surname);
    }

    /**
     * Метод реализует поисковой запрос по подстроке по фамилиям в таблице БД в рамках метода {@link Controller#get(String)}.
     * @return возвращает набор записей, удовлетворяющих поисковым параметрам.
     */
    public String substringSearch(String substring) throws SQLException {
        StringBuilder result = new StringBuilder();
        ResultSet rs = null;
        Statement stmt = null;
        Connection con = null;
        try {
            con = dataSource.getConnection();
            stmt = con.createStatement();

            rs = stmt.executeQuery(sqlSearchQuery(substring));
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();

            result.append("\n[");
            while (rs.next()) {
                result.append("\n{");
                for (int i = 1; i < columnsNumber + 1; i++) {
                    if (i > 1) result.append("\n");
                    result
                            .append("\"")
                            .append(rsmd.getColumnName(i))
                            .append("\"")
                            .append(" : ")
                            .append("\"")
                            .append(rs.getString(i))
                            .append("\"");
                    if(i != columnsNumber) result.append(",");
                }
                result.append("\n}");
                if (!rs.isLast()) result.append(",");
            }
            result.append("\n]");
        } catch (SQLException e) {
            System.out.println("Error in get SQLException, code = " + e.getErrorCode());
        } finally {
            assert rs != null;
            rs.close();
            stmt.close();
            con.close();
        }
        return result.toString();
    }

    /**
     * Метод создаёт поисковой запрос для метода {@link Controller#substringSearch(String)}.
     * @return возвращает запрос к базе данных в виде строки.
     */
    public String sqlSearchQuery(String substring) {
        return "SELECT * " +
                "FROM users " +
                "WHERE LOWER(surname) LIKE LOWER('%" + substring +"%');";
    }

    /**
     * Метод реализует HTTP-запрос GET без параметров.
     * @return отправляет набор записей по Http протоколу в клиентскую программу.
     * @throws SQLException - в отличие от большинства ошибок, SQLException возвращает не только ошибки,
     * но и информацию об успешном завершении запроса.
     */
    @RequestMapping(value = "/showall", method = RequestMethod.GET)
    public String getAll() throws SQLException {
        return showAll();
    }

    /**
     * Метод реализует поисковой запрос всех имеющихся записей в таблице БД для метода {@link Controller#getAll()}.
     * @return возвращает набор всех записей, имеющихся в таблице.
     */
    public String showAll() throws SQLException {
        StringBuilder result = new StringBuilder();
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            con = dataSource.getConnection();
            stmt = con.createStatement();

            rs = stmt.executeQuery(sqlShowAllQuery());
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();

            result.append("\n[");
            while (rs.next()) {
                result.append("\n{");
                for (int i = 1; i < columnsNumber + 1; i++) {
                    if (i > 1) result.append("\n");
                    result
                            .append("\"")
                            .append(rsmd.getColumnName(i))
                            .append("\"")
                            .append(" : ")
                            .append("\"")
                            .append(rs.getString(i))
                            .append("\"");
                    if(i != columnsNumber) result.append(",");
                }
                result.append("\n}");
                if (!rs.isLast()) result.append(",");
            }
            result.append("\n]");
        } catch (SQLException e) {
            System.out.println("Error in showAll SQLException, code = " + e.getErrorCode());
        } finally {
            assert rs != null;
            rs.close();
            stmt.close();
            con.close();
        }
        System.out.println(result.toString());
        return result.toString();
    }

    /**
     * Метод создаёт поисковой запрос для метода {@link Controller#showAll()}.
     * @return возвращает поисковой запрос в виде строки.
     */
    public String sqlShowAllQuery() {
        return "SELECT * FROM users;";
    }

    /**
     * Метод реализует HTTP-запрос POST с телом, в котором содержаться параметры, необходимые для формирования
     * записи в таблице БД.
     * @param user - это класс, который содержит параметры, необходимые для формрования записи в таблице БД.
     * @throws SQLException - в отличие от большинства ошибок, SQLException возвращает не только ошибки,
     * но и информацию об успешном завершении запроса.
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public void post(@RequestBody User user) throws SQLException {
        addNewUser(user);
    }

    /**
     * Метод реализует запрос в таблице БД, который добавляет запись по заданным из класса User параметрам.
     * Реализуется в рамках метода {@link Controller#post(User)}.
     * Метод ничего не возвращает.
     */
    public void addNewUser(User user) throws SQLException {
        Connection con = null;
        Statement stmt = null;
        try {
            con = dataSource.getConnection();
            stmt = con.createStatement();
            stmt.executeQuery(sqlAddUserQuery(user));
        } catch (SQLException e) {
            System.out.println("Error in add SQLException, code = " + e.getErrorCode());
        } finally {
            assert stmt != null;
            stmt.close();
            con.close();
        }
    }

    /**
     * Метод создаёт запрос для таблице в БД с учётом параметров из класса User.
     * @return возвращает запрос на добавление записи в виде строки.
     */
    public String sqlAddUserQuery(User user) {
        return "INSERT INTO users (name, surname, patronymic, birth) VALUES (" +
                "'"+ user.getName() + "', " +
                "'"+ user.getSurname() + "', " +
                "'"+ user.getPatronymic() + "', " +
                "'"+ user.getBirth() + "'" +
                ");";
    }

    /**
     * Метод реализует HTTP-запрос DELETE с параметром id.
     * @param id - уникальное значение для каждой записи в таблице БД.
     * @throws SQLException - в отличие от большинства ошибок, SQLException возвращает не только ошибки,
     * но и информацию об успешном завершении запроса.
     */
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public void delete(@RequestParam int id) throws SQLException {
        deleteUser(id);
    }

    /**
     * Метод реализует запрос на удаление в таблице БД в рамках метода {@link Controller#delete(int)}.
     * Метод ничего не возвращает.
     */
    public void deleteUser(int id) throws SQLException {
        Connection con = null;
        Statement stmt = null;
        try {
            con = dataSource.getConnection();
            stmt = con.createStatement();
            stmt.executeQuery(sqlDeleteQuery(id));
            stmt.close();
            con.close();
        } catch (SQLException e) {
            System.out.println("Error in delete SQLException, code = " + e.getErrorCode());
        } finally {
            assert stmt != null;
            stmt.close();
            con.close();
        }
    }

    /**
     * Метод создаёт запрос на удаление записи в таблице БД для метода {@link Controller}.
     * @return возвращает запрос на удаление записи в виде строки.
     */
    public String sqlDeleteQuery(int id) {
        return "DELETE FROM users WHERE id = " + id + ";";
    }
}
