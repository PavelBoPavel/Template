package com.epam.impl;

import com.epam.api.GpsNavigator;
import com.epam.api.Path;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Stub2GpsNavigator implements GpsNavigator {
    String road [] = new String[4];
    static List<String []> roadsList = new ArrayList<>();
    static List<List<String>> rezPathsList = new ArrayList<>();
    private static String START;
    private static String END;
    FileInputStream fstream;
    Graph graph = new Graph();
    @Override
    public void readData(String filePath) {
        try{
            fstream = new FileInputStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
            String strLine;
            while ((strLine = br.readLine()) != null){
                road =  strLine.split(" ").clone();
                roadsList.add(road);
                graph.addEdge(road[0], road[1]);
            }
            fstream.close();
        }catch (IOException e){
            try {
                fstream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            System.out.println("Ошибка чтения файла!");
        }
    }

    @Override
    public Path findPath(String pointA, String pointB) {
        LinkedList<String> visited = new LinkedList();
        int cost[];
        START = pointA;
        END = pointB;
        visited.add(START);
        new Stub2GpsNavigator().searchPath(graph, visited);
        if (rezPathsList != null) {
            cost = new Stub2GpsNavigator().allCost(rezPathsList).clone();
            int minCost = cost[0];
            Path path = null;
            if (rezPathsList.size() != 1) {
                for (int i = 1; i < cost.length; i++) {
                    if (minCost > cost[i]) {
                        minCost = cost[i];
                        path = new Path(rezPathsList.get(i), minCost);
                    } else if (minCost == cost[i]) {
                        path = null;
                    }
                }
            } else {
                path = new Path(rezPathsList.get(0), minCost);
            }
            return path;
        } else {
            return null;
        }
    }

    public int[] allCost (List<List<String>> paths){
        int num = paths.size();
        int cost[] = new int[num];
        for (int i = 0; i < num; i++){
            cost[i] = 0;
            for (int j = 1; j < paths.get(i).size(); j++){
                for (String[] road: roadsList){
                    String s = paths.get(i).get(j-1);
                    if (paths.get(i).get(j-1).equals(road[0]) && paths.get(i).get(j).equals(road[1])){
                        cost[i] += Integer.parseInt(road[2])*Integer.parseInt(road[3]);
                        break;
                    }
                }
            }
        }
        return cost;
    }

    private void searchPath(Graph graph, LinkedList<String> visited) {
        LinkedList<String> nodes = graph.adjacentNodes(visited.getLast());
        // examine adjacent nodes
        for (String node : nodes) {
            if (visited.contains(node)) {
                continue;
            }
            if (node.equals(END)) {
                visited.add(node);
                printPath(visited);
                visited.removeLast();
                break;
            }
        }
        for (String node : nodes) {
            if (visited.contains(node) || node.equals(END)) {
                continue;
            }
            visited.addLast(node);
            searchPath(graph, visited);
            visited.removeLast();
        }
    }

    private void printPath(LinkedList<String> visited) {
        int num = visited.size();
        List<String> str = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            str.add(visited.get(i));
           /* System.out.print(visited.get(i));
            System.out.print(" ");*/
        }
        rezPathsList.add(str);
       // System.out.println();
    }

    public class Graph {
        private Map<String, LinkedHashSet<String>> map = new HashMap();

        public void addEdge(String node1, String node2) {
            LinkedHashSet<String> adjacent = map.get(node1);
            if(adjacent==null) {
                adjacent = new LinkedHashSet();
                map.put(node1, adjacent);
            }
            adjacent.add(node2);
        }

        public LinkedList<String> adjacentNodes(String last) {
            LinkedHashSet<String> adjacent = map.get(last);
            if(adjacent==null) {
                return new LinkedList();
            }
            return new LinkedList<String>(adjacent);
        }
    }
}