package webControllers;

import com.google.gson.Gson;
import model.Administrator;
import model.Course;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import utils.DbOperations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Properties;

@Controller
@RequestMapping(value = "/users")
public class WebUserController {

    //READ
    @RequestMapping(value = "/getAllAdmins", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAllAdmins(@RequestParam("courseIs") int courseIs) {
        ArrayList<Administrator> allAdmins = new ArrayList<>();
        Gson parser = new Gson();
        try {
            allAdmins = DbOperations.getAllAdminsFromDb(courseIs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parser.toJson(allAdmins);
    }

    @RequestMapping(value = "/getUser/{login}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getUser(@PathVariable("login") String name) {
        Gson parser = new Gson();

        try {
            return parser.toJson(DbOperations.getUserByName(name));
        } catch (Exception e) {
            e.printStackTrace();
            return "Error selecting";
        }

    }

    //Authorization
    @RequestMapping(value = "/adminLogin", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String loginEmployee(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String loginName = data.getProperty("login");
        String password = data.getProperty("psw");
        int courseIs = Integer.parseInt(data.getProperty("courseIs"));
        Administrator administrator = null;
        try {
            administrator = DbOperations.getAdmin(loginName, password, courseIs);
        } catch (Exception e) {
            return "Error";
        }
        if (administrator == null) {
            return "Wrong credentials";
        }
        return Integer.toString(administrator.getId());
    }

    //INSERT
    @RequestMapping(value = "/insertAdministrator", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String insertAdministrator(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String login = data.getProperty("login");
        String psw = data.getProperty("psw");
        String email = data.getProperty("email");
        String phoneNum = data.getProperty("phoneNum");
        int courseIs = Integer.parseInt(data.getProperty("courseIs"));
        try {
            DbOperations.insertRecordAdmin(login, psw, email, phoneNum, courseIs);
            return parser.toJson(DbOperations.getUserByName(login));
        } catch (Exception e) {
            return "There were errors during insert operation";
        }
    }

    @RequestMapping(value = "/insertStudent", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String insertStudent(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String login = data.getProperty("login");
        String psw = data.getProperty("psw");
        String email = data.getProperty("email");
        int courseIs = Integer.parseInt(data.getProperty("courseIs"));
        try {
            DbOperations.insertRecordStudent(login, psw, email, courseIs);
            return parser.toJson(DbOperations.getUserByName(login));
        } catch (Exception e) {
            return "There were errors during insert operation";
        }
    }

    //UPDATE
    @RequestMapping(value = "/updAdmin", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateAdmin(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);

        int adminId = Integer.parseInt(data.getProperty("id"));
        String login = data.getProperty("login");
        String psw = data.getProperty("psw");
        String email = data.getProperty("email");
        String phone_number = data.getProperty("phone_number");
        try {
            DbOperations.updateUserRecord(adminId, "login", login);
            DbOperations.updateUserRecord(adminId, "psw", psw);
            DbOperations.updateUserRecord(adminId, "email", email);
            DbOperations.updateUserRecord(adminId, "phone_number", phone_number);
            return parser.toJson(DbOperations.getUserByName(login));
        } catch (Exception e) {
            return "There were errors during update operation";
        }
    }

    @RequestMapping(value = "/updStudent", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateStudent(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);

        int id = Integer.parseInt(data.getProperty("id"));
        String login = data.getProperty("login");
        String psw = data.getProperty("psw");
        String email = data.getProperty("email");
        try {
            DbOperations.updateUserRecord(id, "login", login);
            DbOperations.updateUserRecord(id, "psw", psw);
            DbOperations.updateUserRecord(id, "email", email);
            return parser.toJson(DbOperations.getUserByName(login));
        } catch (Exception e) {
            return "There were errors during update operation";
        }
    }

    //DELETE
    @RequestMapping(value = "/delUserId", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String deleteUserId(@RequestParam("id") int id) {
        try {
            DbOperations.deleteUserRecord(id);
            return "Record deleted";
        } catch (Exception e) {
            return "There were errors during delete operation";
        }
    }
}
