package com.example.grpc;


//import com.example.CMPE275.GreetingServiceOuterClass;
//import com.example.reader.Station;
import com.sleepycat.je.utilint.Stat;

import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CsvReader {
    public List<String> processInputFile(String inputFile){
        List<String> inputList = new ArrayList<>();
        try{
            File inputF = new File(inputFile);
            InputStream inputFS = new FileInputStream(inputF);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));

            inputList = br.lines().skip(1).map(mapToItem).collect(Collectors.toList());
            br.close();
        }catch(IOException e){
            System.out.println(e);
        }
        return inputList;
    }

    private static Function<String, String> mapToItem = (line) -> {
        String[] p = line.split(",");
//        GreetingServiceOuterClass.HelloResponse station = GreetingServiceOuterClass.HelloResponse.newBuilder().setId(p[0]).setName(p[1]).setMesonet(p[2]).build();
          String station = p[0] + p[1] + p[2];
//        station.setId(p[0]);
//        station.setName(p[1]);
//        station.setMesonet(p[2]);

        return station;
    };
}