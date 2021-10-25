package com.example.grpc;

import java.util.concurrent.TimeUnit;

import io.grpc.*;
import io.grpc.stub.StreamObserver;

public class ClientAsync
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
      
      GreetingServiceGrpc.GreetingServiceStub stub = GreetingServiceGrpc.newStub(channel);
      String inputDate = "20120201";
		String inputStation = "2300";
		
      GreetingServiceOuterClass.HelloRequest request1 =
        GreetingServiceOuterClass.HelloRequest.newBuilder().setInputdate(inputDate).setInputstation(inputStation).build();
      GreetingServiceOuterClass.HelloRequest request2 =
    	        GreetingServiceOuterClass.HelloRequest.newBuilder().setInputdate(inputDate).setInputstation(inputStation).build();
      GreetingServiceOuterClass.HelloRequest request3 =
    	        GreetingServiceOuterClass.HelloRequest.newBuilder().setInputdate(inputDate).setInputstation(inputStation).build();
//      stub.greeting(request)
//      .forEachRemaining(response -> {
//          System.out.println(response.getResponsemessage());
//      });
      
      System.out.println("calling greeting method");
//      stub.greeting(request, new StreamObserver<GreetingServiceOuterClass.HelloResponse>() {
//          public void onNext(GreetingServiceOuterClass.HelloResponse response) {
//            System.out.println(response);
//          }
//          public void onError(Throwable t) {
//          }
//          public void onCompleted() {
//            // Typically you'll shutdown the channel somewhere else.
//            // But for the purpose of the lab, we are only making a single
//            // request. We'll shutdown as soon as this request is done.
//            channel.shutdownNow();
//          }
//        });
      long start = System.currentTimeMillis();
//      long totalTime = 0;
      
      StreamObserver<GreetingServiceOuterClass.HelloResponse> streamObserver = new StreamObserver<GreetingServiceOuterClass.HelloResponse>() {
          public void onNext(final GreetingServiceOuterClass.HelloResponse response) {
//              System.out.println("async call result: ");
          }

          public void onError(final Throwable throwable) {
              System.out.println(throwable);
          }

          public void onCompleted() {
        	  long end = System.currentTimeMillis();
        	  	System.out.println(
        				" total processing time per thread is " + (((end - start) / 1000.0)) + " seconds");
        	    
//        	  channel.shutdownNow();
          }
      };
      
      
      
      stub.greeting(request1, streamObserver);
      stub.greeting(request2, streamObserver);
      stub.greeting(request3, streamObserver);
      
      
     
      
//      long end = System.currentTimeMillis();
//  	System.out.println(
//			" total processing time is " + ((end - start) / 1000.0) + " seconds");
//      channel.shutdownNow();
      channel.awaitTermination(100, TimeUnit.SECONDS);
      
    }
}