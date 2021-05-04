package webControllers;

import com.google.gson.Gson;
import model.File;
import model.Folder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import utils.DbOperations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Properties;

@Controller
//.../file
@RequestMapping(value = "/file")
public class WebFileController {
    //READ  ..../file/getAllFiles
    @RequestMapping(value = "/getAllFiles", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAllFiles(@RequestParam("folder") int folder) {
        ArrayList<File> allFiles = new ArrayList<>();
        Gson parser = new Gson();
        try {
            allFiles = DbOperations.getAllFilesFromDb(folder);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parser.toJson(allFiles);
    }

    @RequestMapping(value = "/getFile/{name}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getFile(@PathVariable("name") String name) {
        Gson parser = new Gson();

        try {
            return parser.toJson(DbOperations.getFileByName(name));
        } catch (Exception e) {
            e.printStackTrace();
            return "Error selecting";
        }
    }

    //INSERT
    @RequestMapping(value = "/insertFile", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String insertFile(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        String name = data.getProperty("name");
        LocalDate date_added =  LocalDate.now();
        int folder_id = Integer.parseInt(data.getProperty("folder_id"));
        String linkToFile = data.getProperty("linkToFile");
        try {
            DbOperations.insertRecordFile(name, date_added, linkToFile, folder_id);
            return parser.toJson(DbOperations.getFileByName(name));
        } catch (Exception e) {
            return "There were errors during insert operation";
        }
    }

    //UPDATE
    @RequestMapping(value = "/updFile", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updFile(@RequestBody String request) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(request, Properties.class);
        int fileId = Integer.parseInt(data.getProperty("id"));
        String name = data.getProperty("name");
        LocalDate date_added =  LocalDate.parse(data.getProperty("date_added"));
        String linkToFile = data.getProperty("linkToFile");
        try {
            DbOperations.updateDbRecord(fileId, "name", name);
            if (!data.getProperty("date_added").equals(""))
                DbOperations.updateDbRecord(fileId, "date_added", LocalDate.parse(data.getProperty("date_added")));
            DbOperations.updateDbRecord(fileId, "linkToFile", linkToFile);
            return parser.toJson(DbOperations.getFileByName(name));
        } catch (Exception e) {
            return "There were errors during update operation";
        }
    }

    //DELETE
    @RequestMapping(value = "/delFileName", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String deleteFileName(@RequestParam("name") String name) {
        try {
            DbOperations.deleteFileRecord(name);
            return "Record deleted";
        } catch (Exception e) {
            return "There were errors during insert operation";
        }
    }

    @RequestMapping(value = "/delFileId", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String deleteFileId(@RequestParam("id") int id) {
        try {
            DbOperations.deleteFileRecord(id);
            return "Record deleted";
        } catch (Exception e) {
            return "There were errors during delete operation";
        }
    }
}
