package com.inventory_forcasting.DEMO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Practice {

    public static void main(String[] args) {
        Practice p=new Practice();
//        p.test1();
//        p.test2();
//        p.test3();
//        p.test4();
//        p.test5();

//        p.test6();
        p.test7();
    }

    //Use Stream API to find all even numbers greater than 15 and collect them into a new list.
    private void test1(){
        List<Integer> numbers = Arrays.asList(3, 7, 10, 15, 20, 22, 35, 40);
        System.out.println(numbers.stream().filter(k->k>15).toList());
    }

    //Use streams to calculate the total number of characters in all words combined.
    private void test2(){
        List<String> words = Arrays.asList("java", "stream", "api", "practice", "questions");
        System.out.println(words.stream().map(k-> k.length()).reduce(0,(a,b)-> a+b));
    }

    //Use streams to sort the names by length in descending order and print the top 3 longest names.
    private void test3(){
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David", "Eve", "Frank");
        System.out.println(names.stream().sorted((a,b)->b.length()-a.length()).limit(3).toList());
    }

    @Data
    @AllArgsConstructor
    class Employee {
        String name;
        String department;
        int salary;
    }
    //Use streams to group employees by department and print the result.
    private void test4(){
        List<Employee> employees = Arrays.asList(
                new Employee("Alice", "IT", 70000),
                new Employee("Bob", "HR", 50000),
                new Employee("Charlie", "IT", 80000),
                new Employee("David", "Finance", 60000),
                new Employee("Eve", "HR", 55000)
        );

        System.out.println(employees.stream().collect(Collectors.groupingBy(e->e.department))
//                .entrySet().stream().filter(k->k.getKey().equalsIgnoreCase("HR"))
//                .toList()
        );
    }

    //Use flatMap to convert this into a single list of integers, then filter only the numbers greater than 4.
    private void test5(){
        List<List<Integer>> nestedNumbers = Arrays.asList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5),
                Arrays.asList(6, 7, 8, 9)
        );

        System.out.println(nestedNumbers.stream().flatMap(k->k.stream()).toList());
    }

    //Split each sentence into words, into a single list of words.
    //Keep only words longer than 5 characters. Collect them as unique words.
    private void test6(){
        List<String> sentences = Arrays.asList(
                "Java stream API is powerful",
                "Parallel streams can improve performance",
                "Practice makes perfect",
                "Stream operations are lazy"
        );
        Random r= new Random();
//        int[] i=r.ints().toArray();

        System.out.println(sentences.stream().map(k->k.split(" ")).toList()
                .stream().flatMap(k-> Arrays.stream(k))
                .filter(k->k.length()>5).collect(Collectors.toSet()));
    }

    //You are given a list of integers. Write a Java Stream-based solution to find the first element in the list that
    // is divisible by 5, but not divisible by 2 or 3. If no such element exists, return Optional.empty().
    private void test7(){
        List<Integer> numbers = Arrays.asList(10, 15, 20, 25, 30, 35, 40);
        System.out.println(numbers.stream().filter(k->k%5==0 && k%2!=0 && k%3!=0).limit(1).toList());
    }

    //You are tasked with computing the sum of squares of all even numbers from a large list using parallel streams
    // for performance optimization. Use the ParallelStream to speed up the computation.
    private void test8(){

    }

    //Write a Java Stream solution that takes a list of strings and returns a new list where each string is reversed,
    // but only if it contains at least one vowel. The solution should exclude strings that contain no vowels.
    private void test9(){

    }
}
