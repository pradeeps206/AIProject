package solution;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class DegressDemo {

    private Map<String, Integer> names = new HashMap<>();
    private Map<Integer, String> namesExt = new HashMap<>();
    private Map<Integer, String> movies = new HashMap<>();
    private Map<Integer, List<Integer>> actorsToMovies = new HashMap<>();
    private Map<Integer, List<Integer>> moviesToActors = new HashMap<>();
    private String start, target;

    public DegressDemo(String folder) {
        String currentLine;
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("C:\\Development\\CS50\\src0\\src0\\degrees\\" + folder + "\\people.csv"));
            reader.readLine();//skip header
            while ((currentLine = reader.readLine()) != null) {
                if (!currentLine.contains("\"")) {
                    if (currentLine.split(",").length == 3) {
                        names.put(currentLine.split(",")[1], Integer.parseInt(currentLine.split(",")[0]));
                        namesExt.put(Integer.parseInt(currentLine.split(",")[0]), currentLine.split(",")[1] + ";" + currentLine.split(",")[2]);
                    }
                    if (currentLine.split(",").length == 2) {
                        names.put(currentLine.split(",")[1], Integer.parseInt(currentLine.split(",")[0]));
                        namesExt.put(Integer.parseInt(currentLine.split(",")[0]), currentLine.split(",")[1] + ";");
                    }
                }
                if (currentLine.contains("\"")) {
                    String name_DOB = currentLine.split(",\"")[1];
                    String personId = currentLine.split(",\"")[0];
                    String name = name_DOB.split("\",")[0];
                    names.put(name, Integer.parseInt(personId));
                    namesExt.put(Integer.parseInt(personId), name_DOB.replace("\",", ";"));
                }
            }

            reader = new BufferedReader(new FileReader("C:\\Development\\CS50\\src0\\src0\\degrees\\" + folder + "\\movies.csv"));
            reader.readLine();//skip header
            while ((currentLine = reader.readLine()) != null) {
                movies.put(Integer.parseInt(currentLine.split(",")[0]),
                        String.format("%s; %s", currentLine.split(",")[1].replaceAll("\"", ""), currentLine.split(",")[2]));
            }

            reader = new BufferedReader(new FileReader("C:\\Development\\CS50\\src0\\src0\\degrees\\" + folder + "\\stars.csv"));
            reader.readLine();//skip header
            while ((currentLine = reader.readLine()) != null) {
                if (!actorsToMovies.containsKey(Integer.parseInt(currentLine.split(",")[0])))
                    actorsToMovies.putIfAbsent(Integer.parseInt(currentLine.split(",")[0]), Collections.singletonList(Integer.parseInt(currentLine.split(",")[1])));
                else {
                    List<Integer> valueAsList = new ArrayList<>(actorsToMovies.get(Integer.parseInt(currentLine.split(",")[0])));
                    valueAsList.add(Integer.parseInt(currentLine.split(",")[1]));
                    actorsToMovies.remove(Integer.parseInt(currentLine.split(",")[0]));
                    actorsToMovies.put(Integer.parseInt(currentLine.split(",")[0]), valueAsList);
                }
                if (!moviesToActors.containsKey(Integer.parseInt(currentLine.split(",")[1])))
                    moviesToActors.putIfAbsent(Integer.parseInt(currentLine.split(",")[1]), Collections.singletonList(Integer.parseInt(currentLine.split(",")[0])));
                else {
                    List<Integer> valueAsList = new ArrayList<>(moviesToActors.get(Integer.parseInt(currentLine.split(",")[1])));
                    valueAsList.add(Integer.parseInt(currentLine.split(",")[0]));
                    moviesToActors.remove(Integer.parseInt(currentLine.split(",")[1]));
                    moviesToActors.put(Integer.parseInt(currentLine.split(",")[1]), valueAsList);
                }
            }
            System.out.println("all file data loaded into memory");
            System.out.println("Enter the source");
            System.out.printf("%s %s", names.get(reader.readLine()));
            System.out.println("Enter the target");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        DegressDemo degressDemo = new DegressDemo(args[0]);
    }
}
