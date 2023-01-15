package robdarrough.searchjars;

import static org.junit.jupiter.api.Assertions.*;

//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SearchJarsTest {

//	@BeforeAll
//	static void setUpBeforeClass() throws Exception {
//	}
//
//	@AfterAll
//	static void tearDownAfterClass() throws Exception {
//	}
//
//	@BeforeEach
//	void setUp() throws Exception {
//	}
//
//	@AfterEach
//	void tearDown() throws Exception {
//	}
//
//	@Test
//	void testMain() {
//		
//	}
//
//	@Test
//	void testCheckPaths() {
//		
//	}
//
//	@Test
//	void testSearch() {
//	
//	}
//
//	@Test
//	void testCheckFolder() {
//		
//	}
//
//	@Test
//	void testCheckZipFile() {
//		
//	}

	@Test
	void testContain() {
		
		SearchJars app = new SearchJars();
		
		assertEquals(true, app.contain("", ""));
		assertEquals(false, app.contain("text", ""));
		assertEquals(true, app.contain("", "text"));
		assertEquals(true,  app.contain("text", "text"));
		
		assertEquals(true, app.contain("Object", "some Object or another"));
		assertEquals(false, app.contain("Object", "some Objekt or another"));
		assertEquals(true, app.contain("Object", "is it Objekt or Object"));
		assertEquals(true, app.contain("Object", "Object is the word"));		
		
	}

	@Test
	void testCompare() {
		
		SearchJars app = new SearchJars();
		
		assertEquals(false, app.compare('.', '/'));
		assertEquals(true, app.compare('.', '.'));
		assertEquals(true, app.compare('/', '.'));
		assertEquals(true, app.compare('/', '/'));
	}

//	@Test
//	void testPrint() {
//	
//	}

}
