
import org.junit.jupiter.api.*;
public class JUnitCycleTest {
    @BeforeAll // Run this before starting test. Use it when initializing database or something.
    static void beforeAll() {
        System.out.println("@BeforeAll");
    }

    @BeforeEach // Run this before everytime a test runs. Use it to initialize object or input value before testing.
    public void beforeEach() {
        System.out.println("@BeforeEach");
    }

    @Test
    void test1() {
        System.out.println("@Test1");
    }

    @Test
    void test2() {
        System.out.println("@Test2");
    }

   @AfterAll // Run this after starting test. Use it to delete value or exit database.
   static void afterAll() {
        System.out.println("@AfterAll");
   }

   @AfterEach // Run this after everytime a test runs. Use it delete data.
   public void afterEach() {
        System.out.println("@AfterEach");
   }
}
