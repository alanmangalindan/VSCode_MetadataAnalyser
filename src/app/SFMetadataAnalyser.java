package app;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SFMetadataAnalyser {

    List<File> filesToProcess = new ArrayList<>();
    int fileCount = 0;
    // int fieldCount = 0;

    public static void main(String[] args) {
        new SFMetadataAnalyser().start();
//        new SFMetadataAnalyser().test();
    }

    public void start() {

        // specify field that contains field names here (e.g. field_names.txt)
        String file = "field_names.txt";
        String csvOutput = "Field,Used in APEX,Used in VF,Used in LX,Used in WF/PB,Used in Reports / Dashboards,Used in Template\n";

        boolean apex = false, vf = false, lx = false, wfpb = false, repDash = false, template = false;

        try (BufferedReader bR = new BufferedReader(new FileReader(file))) {

            // specify folder that contains metadata here
            File folder = new File("C:\\Users\\Alan Mangalindan\\OneDrive - Davanti Consulting\\work\\EROAD\\VSCode_Projects\\Old_Legacy_FSB_Metadata");

            getFilesToProcess(folder);
            
            System.out.println("Number of files to search: " + fileCount);

            for (String field = bR.readLine(); field != null; field = bR.readLine()) {

                csvOutput += field + ",";
                // fieldCount++;

                // reset each boolen to false before searching for each field
                apex = false;
                vf = false;
                lx = false;
                wfpb = false;
                repDash = false;
                template = false;


                for (File thisFile: filesToProcess) {
                    System.out.println("Finding " + field + " in " + thisFile.getName());
                    try (BufferedReader bRFile = new BufferedReader(new FileReader(thisFile))) {
                        for (String line = bRFile.readLine(); line != null; line = bRFile.readLine()) {
                            if (line.contains(field)) {
                                String fileName = thisFile.getName();
                                int theDot = fileName.indexOf(".");
                                String ext = fileName.substring(theDot + 1);
                                switch (ext) {
                                    case "cls":
                                        apex = true;
                                        break;
                                    case "trigger":
                                        apex = true;
                                        break;
                                    case "page":
                                        vf = true;
                                        break;
                                    case "cmp":
                                        lx = true;
                                        break;
                                    case "js":
                                        lx = true;
                                        break;
                                    case "workflow":
                                        wfpb = true;
                                        break;
                                    case "flow":
                                        wfpb = true;
                                        break;
                                    case "report":
                                        repDash = true;
                                        break;
                                    case "email":
                                        template = true;
                                        break;
                                }
                            }
                        }
                    }
                }

                csvOutput += (apex) ? "Y," : "N,";
                csvOutput += (vf) ? "Y," : "N,";
                csvOutput += (lx) ? "Y," : "N,";
                csvOutput += (wfpb) ? "Y," : "N,";
                csvOutput += (repDash) ? "Y," : "N,";
                csvOutput += (template) ? "Y\n" : "N\n";

                // if (fieldCount == 10) {
                //     break;
                // }

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String resultsFile = "results.csv";

        File saveFile = new File(resultsFile);

        try (BufferedWriter bW = new BufferedWriter(new FileWriter(saveFile))) {

            bW.write(csvOutput);
            System.out.println("Search completed. Results saved in " + resultsFile);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    // public void test() {
//        File folder = new File("C:\\Users\\Alan Mangalindan\\OneDrive - Davanti Consulting\\work\\EROAD\\test_data\\4_Jul_Task_Event");
//        File[] listOfFiles = folder.listFiles();
//        getFilesToProcess(folder);
//        for (int i = 0; i < listOfFiles.length; i++) {
//            if (listOfFiles[i].isFile()) {
//                System.out.println("    File: " + listOfFiles[i].getName());
//            } else if (listOfFiles[i].isDirectory()) {
//                System.out.println("Directory: " + listOfFiles[i].getName());
//                processFolder(listOfFiles[i]);
//            }
//        }
//        String filename = "cat.mouse.dog";
//        int theDot = filename.indexOf(".");
//        String ext = filename.substring(theDot+1);
    // }

    private void getFilesToProcess(File directory) {
        File[] filesInThisFolder = directory.listFiles();
        for (int i = 0; i < filesInThisFolder.length; i++) {
            if (filesInThisFolder[i].isFile()) {
                // System.out.println("    File: " + filesInThisFolder[i].getName() + "    count: " + count);
                filesToProcess.add(filesInThisFolder[i]);
                fileCount++;
            } else if (filesInThisFolder[i].isDirectory()) {
                // System.out.println("Directory: " + filesInThisFolder[i].getName());
                getFilesToProcess(filesInThisFolder[i]);
            }
        }
    }
}
