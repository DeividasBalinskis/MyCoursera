package webControllers;

import com.google.gson.Gson;
import model.Course;
import model.Folder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import utils.DbOperations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Properties;

@Controller
//.../folder
@RequestMapping(value = "/folder")
public class WebFolderController {
    //READ  ..../folder/getAllFolders
    @RequestMapping(value = "/getAllFolders", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAllFolders(@RequestParam("course") int course) {
        ArrayList<Folder> allFolders = new ArrayList<>();
        Gson parser = new Gson();
        try {
            allFolders = DbOperations.getAllFoldersFromDb(course);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parser.toJson(allFolders);
    }

    @RequestMapping(value = "/getFolder/{name}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getFolder(@PathVariable("name") String name) {
        Gson parser = new Gson();

        try {
            return parser.toJson(DbOperations.getFolderByName(name));
        } catch (Exception e) {
            e.printStackTrace();
            return "Error selecting";
        }
    }

    //INSERT
    @RequestMapping(value = "/insertFolder", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String insertFolder(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String name = data.getProperty("name");
        LocalDate date_modified =  LocalDate.parse(data.getProperty("date_modified"));
        int course_id = Integer.parseInt(data.getProperty("course_id"));
        Double price = Double.parseDouble(data.getProperty("price"));
        try {
            DbOperations.insertRecordFolder(name, date_modified, course_id);
            return parser.toJson(DbOperations.getFolderByName(name));
        } catch (Exception e) {
            return "There were errors during insert operation";
        }
    }

    //UPDATE
    @RequestMapping(value = "/updFolder", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updFolder(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        int folderId = Integer.parseInt(data.getProperty("id"));
        String name = data.getProperty("name");
        LocalDate date_modified = LocalDate.now();
        try {
            DbOperations.updateDbRecord(folderId, "name", name);
            DbOperations.updateDbRecord(folderId, "date_modified", date_modified);
            return parser.toJson(DbOperations.getFolderByName(name));
        } catch (Exception e) {
            return "There were errors during update operation";
        }
    }

    //DELETE
    @RequestMapping(value = "/delFolderName", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String deleteFolderName(@RequestParam("name") String name) {
        try {
            DbOperations.deleteFolderRecord(name);
            return "Record deleted";
        } catch (Exception e) {
            return "There were errors during insert operation";
        }
    }

    @RequestMapping(value = "/delFolderId", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String deleteFolderId(@RequestParam("id") int id) {
        try {
            DbOperations.deleteFolderRecord(id);
            return "Record deleted";
        } catch (Exception e) {
            return "There were errors during delete operation";
        }
    }
}
