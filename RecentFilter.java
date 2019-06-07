import java.io.IOException;
import java.util.Calendar;
import java.io.FileFilter;
import java.io.File;

import java.util.*; 

public class RecentFilter implements FileFilter {

    private Queue<File> recentFiles;

    public RecentFilter(Queue<File> recent){
        recentFiles = recent;
    }

    public boolean accept(File file){
        if (recentFiles.contains(file)){
            return true;
        } 
        return false;
    }


    public String getDescription(){
        return "Returns true if a file has last been modified within a certain time period in days";
    }
}