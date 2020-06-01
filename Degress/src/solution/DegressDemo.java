package solution;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DegressDemo {

    private Map<String, List<Integer>> names = new HashMap<>();
    private Map<Integer, String> namesExt = new HashMap<>();
    private Map<Integer, String> movies = new HashMap<>();
    private Map<Integer, List<Integer>> actorsToMovies = new HashMap<>();
    private Map<Integer, List<Integer>> moviesToActors = new HashMap<>();
    private BufferedReader reader;
    private Integer source, target;

    public DegressDemo(String folder) {
        try {
            reader = new BufferedReader(new FileReader("C:\\Development\\CS50\\src0\\src0\\degrees\\" + folder + "\\people.csv"));
            reader.readLine();//skip header
            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                if (!currentLine.contains("\"")) {
                    if (currentLine.split(",").length == 3) {
                        if (!names.containsKey(currentLine.split(",")[1].toLowerCase()))
                            names.put(currentLine.split(",")[1].toLowerCase(), Collections.singletonList(Integer.parseInt(currentLine.split(",")[0])));
                        else {
                            List<Integer> valueAsList = new ArrayList<>(names.get(currentLine.split(",")[1].toLowerCase()));
                            valueAsList.add(Integer.parseInt(currentLine.split(",")[0]));
                            names.replace(currentLine.split(",")[1].toLowerCase(), valueAsList);
                        }
                        namesExt.put(Integer.parseInt(currentLine.split(",")[0]), currentLine.split(",")[1] + ";" + currentLine.split(",")[2]);
                    }
                    if (currentLine.split(",").length == 2) {
                        if (!names.containsKey(currentLine.split(",")[1].toLowerCase()))
                            names.put(currentLine.split(",")[1].toLowerCase(), Collections.singletonList(Integer.parseInt(currentLine.split(",")[0])));
                        else {
                            List<Integer> valueAsList = new ArrayList<>(names.get(currentLine.split(",")[1].toLowerCase()));
                            valueAsList.add(Integer.parseInt(currentLine.split(",")[0]));
                            names.replace(currentLine.split(",")[1].toLowerCase(), valueAsList);
                        }
                        namesExt.put(Integer.parseInt(currentLine.split(",")[0]), currentLine.split(",")[1] + ";");
                    }
                }
                if (currentLine.contains("\"")) {
                    String name = "", yearOfBirth = "";
                    String nameAndYearOfBirth = currentLine.split(",\"")[1];
                    String personId = currentLine.split(",\"")[0];
                    if (!nameAndYearOfBirth.endsWith(",")) {
                        if (!nameAndYearOfBirth.contains(",")) {
                            name = nameAndYearOfBirth;
                            yearOfBirth = "";
                        } else {
                            name = nameAndYearOfBirth.split("\",")[0];
                            yearOfBirth = nameAndYearOfBirth.split("\",")[1];
                        }
                    } else {
                        name = nameAndYearOfBirth.replace("\",", "");
                        yearOfBirth = "";
                    }
                    if (!names.containsKey(name.toLowerCase()))
                        names.put(name.toLowerCase(), Collections.singletonList(Integer.parseInt(personId)));
                    else {
                        List<Integer> valueAsList = new ArrayList<>(names.get(name.toLowerCase()));
                        valueAsList.add(Integer.parseInt(personId));
                        names.replace(name.toLowerCase(), valueAsList);
                    }
                    namesExt.put(Integer.parseInt(personId), String.format("%s;%s", name.replace("\"", ""), yearOfBirth));
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
            reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("data loaded");
            /*System.out.println("source ?");
            currentLine = reader.readLine();
            source = confirmAgents(currentLine);
            System.out.println("target ?");
            currentLine = reader.readLine();
            target = confirmAgents(currentLine);
            computeShortestPath(source, target);*/
            computeShortestPath(102, 163);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void computeShortestPath(Integer source, Integer target) {
        boolean isPathFound = false;
        List<Integer> moviesStarringSource, moviesStarringTarget;
        Set<Integer> otherActorsInMoviesStarringSource, otherActorsInMoviesStarringTarget, commonActors = null;
        while (!isPathFound) {
            moviesStarringSource = actorsToMovies.get(source);
            moviesStarringTarget = actorsToMovies.get(target);
            Set<Integer> intersect = computeIntersect(moviesStarringSource, moviesStarringTarget);
            if (intersect.isEmpty()) {
                otherActorsInMoviesStarringSource = moviesStarringSource.stream()
                        .map(each -> moviesToActors.get(each))
                        .flatMap(Collection::stream)
                        .collect(Collectors.toSet());
                otherActorsInMoviesStarringTarget = moviesStarringTarget.stream()
                        .map(each -> moviesToActors.get(each))
                        .flatMap(Collection::stream)
                        .collect(Collectors.toSet());
                commonActors = computeIntersect(otherActorsInMoviesStarringSource, otherActorsInMoviesStarringTarget);
                System.out.println(commonActors);
            }
            if(!(commonActors.isEmpty()))
                isPathFound = true;
        }
    }

    public Set<Integer> computeIntersect(Collection<Integer> sourceList, Collection<Integer> targetList) {
        return sourceList.stream()
                .distinct()
                .filter(targetList::contains)
                .collect(Collectors.toSet());
    }


    public Integer confirmAgents(String input) {
        List<Integer> personIds;
        Integer personId = 0;
        try {
            personIds = names.get(input);
            for (Integer thisPersonId : personIds) {
                String nameAndYearOfBirth = namesExt.get(thisPersonId);
                System.out.printf("Are you referring to %s?(y/n)%n", nameAndYearOfBirth);
                String response = reader.readLine();
                if (response.equalsIgnoreCase("y")) {
                    personId = thisPersonId;
                    break;
                }
            }
            if (personId == 0) {
                System.out.printf("person not found. exiting");
                System.exit(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return personId;
    }

    public static void main(String[] args) {
        DegressDemo degressDemo = new DegressDemo(args[0]);
    }
}
