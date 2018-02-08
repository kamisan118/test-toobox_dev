package util.tests;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TimeoutDemo {
	
	public static void main(String[] args) throws Exception {
		TimeoutDemo cl = new TimeoutDemo();
		System.out.println("Start");
		cl.doWorkWithTimeout(5);
		System.out.println("End");
    }

    //maintains a thread for executing the doWork method
    private ExecutorService executor = Executors.newFixedThreadPool(1);
/*
    public void doWork() throws InterruptedException {
        //perform some long running task here...
    	Thread.sleep(10000);
    	System.out.println("End in doWork");
    }*/

    public void doWorkWithTimeout(int timeoutSecs) {

        //set the executor thread working
        final Future<?> future = executor.submit(new Runnable() {
            public void run() {
                try {
                	Thread.sleep(3000);
                	System.out.println("End in doWork");
                    //doWork();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        //check the outcome of the executor thread and limit the time allowed for it to complete
        try {
            future.get(timeoutSecs, TimeUnit.SECONDS);
            
            
        } catch (Exception e) {
            //ExecutionException: deliverer threw exception
            //TimeoutException: didn't complete within downloadTimeoutSecs
            //InterruptedException: the executor thread was interrupted

            //interrupts the worker thread if necessary
        	
            
            if(future.cancel(true))System.out.println("END~~~!!");

            //log.warn("encountered problem while doing some work", e);
        }
    }
}