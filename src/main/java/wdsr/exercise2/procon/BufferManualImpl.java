package wdsr.exercise2.procon;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Task: implement Buffer interface without using any *Queue classes from
 * java.util.concurrent package. Any combination of "synchronized", *Lock,
 * *Semaphore, *Condition, *Barrier, *Latch is allowed.
 */
public class BufferManualImpl implements Buffer {

	final Lock lock = new ReentrantLock();
	final Condition notFull = lock.newCondition();
	final Condition notEmpty = lock.newCondition();

	final Order[] orders = new Order[100];
	int putptr, takeptr, count;

	public void submitOrder(Order order) throws InterruptedException {
		lock.lock();
		try {
			while (count == orders.length) {
				notFull.await();
			}
			orders[putptr] = order;
			if (++putptr == orders.length) {
				putptr = 0;
			}
			++count;
			notEmpty.signal();
		} finally {
			lock.unlock();
		}
	}

	public Order consumeNextOrder() throws InterruptedException {
		lock.lock();
		try {
			while (count == 0) {
				notEmpty.await();
			}
			Order order = orders[takeptr];
			if (++takeptr == orders.length) {
				takeptr = 0;
			}
			--count;
			notFull.signal();
			return order;
		} finally {
			lock.unlock();
		}
	}
}
