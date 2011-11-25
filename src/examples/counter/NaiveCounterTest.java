package examples.counter;

import org.junit.Before;

public class NaiveCounterTest extends AbstractCounterTest {

	@Before
	public void init() {
		counter = new NaiveCounter();
	}

}
