package wdsr.exercise2.counter;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Marek on 05.03.2016.
 * 
 * Task: use {@see java.util.concurrent.locks.Lock} to make
 * CountingFacadeWithLockTest pass.
 */
public class CountingFacadeWithLock implements CountingFacade {
	private final BusinessService businessService;

	private int invocationCounter;

	private Lock lock = new ReentrantLock();

	public CountingFacadeWithLock(BusinessService businessService) {
		this.businessService = businessService;
	}

	public void countAndInvoke() {
		
		try {
			if (lock.tryLock(1, TimeUnit.SECONDS)) {
				invocationCounter++;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
		businessService.executeAction();
	}

	public int getCount() {
		return invocationCounter;
	}
}
