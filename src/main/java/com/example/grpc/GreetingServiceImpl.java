package com.example.grpc;

import io.grpc.stub.StreamObserver;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileFilter;
import java.lang.management.ManagementFactory;
import java.util.Date;
import java.util.List;
import java.util.Set;
import com.sun.management.OperatingSystemMXBean;

public class GreetingServiceImpl extends GreetingServiceGrpc.GreetingServiceImplBase {
  @Override
  public void greeting(GreetingServiceOuterClass.HelloRequest request,
        StreamObserver<GreetingServiceOuterClass.HelloResponse> responseObserver) {
  // HelloRequest has toString auto-generated.
    System.out.println(request);

    // You must use a builder to construct a new Protobuffer object
//    GreetingServiceOuterClass.HelloResponse response = GreetingServiceOuterClass.HelloResponse.newBuilder()
//      .setGreeting("Hello there, " + request.getName())
//      .build();

    // Use responseObserver to send a multiple response back
   
  //  responseObserver.onNext(response);
//    responseObserver.onNext(response);
//    responseObserver.onNext(response);

    // When you are done, you must call onCompleted.
  //  responseObserver.onCompleted();
    
    

String dataSourcePath = "C:\\Users\\Harshala\\Desktop\\EclipseWorkSpace\\datasets\\mesonet\\9-days\\";
String catFPath = "C:\\Users\\Harshala\\Desktop\\EclipseWorkSpace\\grpc-hello-server\\catalog.csv";
String outdirPath = "C:\\Users\\Harshala\\Desktop\\EclipseWorkSpace\\grpc-hello-server\\output.txt";


String inpdate = request.getInputdate();

String inpstation = request.getInputstation();

System.out.println("input date is: " + inpdate);
System.out.println("input station is: " + inpstation);

String updatepath = dataSourcePath + inpdate + "\\" + inpdate + "_" + inpstation;

System.out.println("updated path is: " + updatepath);

File dataSource = new File(updatepath);
File catF = new File(catFPath);
File outdir = new File(outdirPath);

// filters
Date startDate = null;
Date endDate = null;
Rectangle region = null;
Set<String> stationIDs = null;

System.out.println("\n\nSource: " + dataSource + "\nCatalog: " + catF.getAbsolutePath() + "\nOutput: "
		+ outdir.getAbsolutePath());

long startTime = System.currentTimeMillis();
try {
	Catalog cat = new Catalog();
	if (!cat.load(catF)) {
		System.out.println("-- new catalog file created");
	}

	MesonetReader rawReader = new MesonetReader();

	/**
	 * note use readFile() to perform the same steps
	 */

	if (!dataSource.exists()) {
		System.out.println("datasource does not exits");
		return;
	}
	System.out.println("data source exixts ");
		
	if (dataSource.isFile()) {
		System.out.println("is file");
		List<Station> stations = rawReader.extractCatalog(dataSource);
		if (stations != null) {
			System.out.println("stations not null");
			for (Station s : stations)
				cat.addStation(s);
		}
		System.out.println("statiions addded");

		List<MesonetData> data = rawReader.extract(dataSource, startDate, endDate, region, stationIDs);
		System.out.println("processed " + data.size() + " entries");

		// now do something with the data
		// 1. send to the cluster or cloud

		for (MesonetData d : data) {
			// TODO do something other than print!
//			System.out.println("Obs: " + d.getStationID() + " T = " + d.getTemperature() + ", WS = "
//					+ d.getWindSpeed() + ", WD = " + d.getWindDir() + ", RH = " + d.getRelHumidity());
		}
	} else {
		FileFilter filter = new FileFilter() {
			public boolean accept(File pathname) {
				return (pathname.isFile() && !pathname.getName().startsWith(".")
						&& !pathname.getName().endsWith(".gz"));
			}
		};

		// TODO walk through accepted files and process
		System.out.println("TODO: process files");

	}

	// save catalog
	cat.save(catF);

	long stopTime = System.currentTimeMillis();
	System.out.println(
			"MADIS Mesonet - total processing time is " + ((stopTime - startTime) / 1000.0) + " seconds");
} catch (Throwable t) {
	System.out.println(
			"Unable to process mesowest data in " + dataSource.getAbsolutePath() + ": " + t.getMessage());
}

CsvReader read = new CsvReader();

List<String> csvData = read.processInputFile(catFPath);

System.out.println("data reading done " + csvData.size());




//// You must use a builder to construct a new Protobuffer object
//GreetingServiceOuterClass.HelloResponse response = GreetingServiceOuterClass.HelloResponse.newBuilder()
//		.addAllResponsemessage(csvData).build();
//
//// Use responseObserver to send a single response back
//responseObserver.onNext(response);

for (int i = 0; i < csvData.size(); i++) {
	GreetingServiceOuterClass.HelloResponse response = GreetingServiceOuterClass.HelloResponse.newBuilder()
            .setResponsemessage(csvData.get(i))
            .build();
    responseObserver.onNext(response);
}


OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(
                OperatingSystemMXBean.class);
// What % CPU load this current JVM is taking, from 0.0-1.0
System.out.println("jvm cpu load "+ osBean.getProcessCpuLoad());

// What % load the overall system is at, from 0.0-1.0
System.out.println("overall CPU load" + osBean.getCpuLoad());


// When you are done, you must call onCompleted.
responseObserver.onCompleted();
    
    
    
  
  }
}