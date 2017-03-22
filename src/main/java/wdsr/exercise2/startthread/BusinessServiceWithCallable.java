package wdsr.exercise2.startthread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class BusinessServiceWithCallable {
	private final ExecutorService executorService;
	private final NumericHelper helper;

	public BusinessServiceWithCallable(ExecutorService executorService, NumericHelper helper) {
		this.executorService = executorService;
		this.helper = helper;
	}

	/**
	 * Calculates a sum of 100 random numbers. Random numbers are returned by
	 * helper.nextRandom method. Each random number is calculated
	 * asynchronously.
	 * 
	 * @return sum of 100 random numbers.
	 */
	public long sumOfRandomInts() throws InterruptedException, ExecutionException {

		long result = 0;

		// 1. create 100 Callable objects that invoke helper.nextRandom in their
		// call() method.
		MyCallable[] myCallableArray = new MyCallable[100];
		Arrays.fill(myCallableArray, new MyCallable());
		List<MyCallable> myCallableList = Arrays.asList(myCallableArray);

		List<Future<Integer>> futures = new ArrayList<Future<Integer>>();
		Future<Integer> f = null;
		for (MyCallable myCallable : myCallableList) {
			// 2. submit all Callable objects to executorService
			// (executorService.submit or executorService.invokeAll)
			f = executorService.submit(myCallable);
			futures.add(f);
		}

		// 3. sum up the results - each random number can be retrieved using
		// future.get() method.
		List<Integer> l = futures.stream().map(p -> {
			try {
				return p.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			return null;
		}).collect(Collectors.toList());

		result = l.stream().mapToInt(Integer::intValue).sum();

		// 4. return the computed result.s
		return result;
	}

	class MyCallable implements Callable<Integer> {
		@Override
		public Integer call() throws Exception {
			return helper.nextRandom();
		}

	}
}
