package com.example.grpc;

import java.util.concurrent.TimeUnit;

import io.grpc.*;
import io.grpc.stub.StreamObserver;

public class Client
{
    public static void main( String[] args ) throws Exception
    {
      // Channel is the abstraction to connect to a service endpoint
      // Let's use plaintext communication because we don't have certs
      final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8080")
        .usePlaintext()
        .build();

      // It is up to the client to determine whether to block the call
      // Here we create a blocking stub, but an async stub,
      // or an async stub with Future are always possible.
      System.out.println("calling greeting method");
      GreetingServiceGrpc.GreetingServiceBlockingStub  stub = GreetingServiceGrpc.newBlockingStub(channel);
      String inputDate = "20120201";
		String inputStation = "2300";
		 long start = System.currentTimeMillis();
		
      GreetingServiceOuterClass.HelloRequest request =
        GreetingServiceOuterClass.HelloRequest.newBuilder().setInputdate(inputDate).setInputstation(inputStation).build();
      stub.greeting(request)
      .forEachRemaining(response -> {
//          System.out.println("request 1");
      });
      System.out.println("request 1");
      stub.greeting(request)
      .forEachRemaining(response -> {
//          System.out.println("request 3");
      });
      System.out.println("request 2");
      stub.greeting(request)
      .forEachRemaining(response -> {
//          System.out.println("request 2");
      });
      System.out.println("request 3");
      
      long end = System.currentTimeMillis();
  	System.out.println(
			" total processing time is " + ((end - start) / 1000.0) + " seconds");
      channel.shutdownNow();

    }
}