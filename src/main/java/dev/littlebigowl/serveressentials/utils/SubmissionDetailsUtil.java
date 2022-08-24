package dev.littlebigowl.serveressentials.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.Gson;

import dev.littlebigowl.serveressentials.ServerEssentials;
import dev.littlebigowl.serveressentials.models.Submission;
import dev.littlebigowl.serveressentials.models.SubmissionDetails;

public class SubmissionDetailsUtil {

    public static ArrayList<SubmissionDetails> submissions = new ArrayList<>();

    public static SubmissionDetails createSubmissionDetails(Submission submission, String id) {
        SubmissionDetails newSubmission = new SubmissionDetails(submission, id);
        submissions.add(newSubmission);
        
        try {
            saveSubmissions();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return newSubmission;
    }

    public static void loadSubmissions() throws IOException {

        Gson gson = new Gson();
        File file = new File(ServerEssentials.getPlugin().getDataFolder().getAbsolutePath() + "/submissions.json");
        if(file.exists()) {
            Reader reader = new FileReader(file);
            try { SubmissionDetails[] h = gson.fromJson(reader, SubmissionDetails[].class); submissions = new ArrayList<>(Arrays.asList(h)); } catch(Exception e) { submissions = new ArrayList<>();}
        }
    }

    public static void saveSubmissions() throws IOException {

        Gson gson = new Gson();
        File file = new File(ServerEssentials.getPlugin().getDataFolder().getAbsolutePath() + "/submissions.json");
        file.getParentFile().mkdir();
        file.createNewFile();

        Writer writer = new FileWriter(file, false);
        gson.toJson(submissions, writer);
        writer.flush();
        writer.close();
    }
    
}
