package examples.counter;

import org.junit.*;

public class ThreadSafeCounterTest extends AbstractCounterTest {

	@Before
	public void init() {
		counter = new ThreadSafeCounter();
	}

}
