package configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


public class ConfigurationTest {

    static Configuration configuration;
    static String configurationFilePath = "configuration/default.yml";

    @BeforeAll
    static void setUp() {
        // Read configuration file
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        try {
            configuration =
                    mapper.readValue(
                            new File(configurationFilePath),
                            Configuration.class);
        } catch (Exception e) {
            // TODO: should we handle the rest of the exceptions separately?
            e.printStackTrace();
        }
    }

    @Test
    void testGetPolicies() {
        String[] policies = configuration.getPolicies();

        assertEquals(2, policies.length);
        assertEquals("LeastRecentlyUsed", policies[0]);
        assertEquals("LeastFrequentlyUsed", policies[1]);
    }

    @Test
    void testGetCacheSize() {
        assertEquals(20000, configuration.getCacheSize());
    }

    @Test
    void testIsSizeInBytes() {
        assertTrue(configuration.isSizeInBytes());
    }

    @Test
    void testGetTrace() {
        assertEquals(Trace.Cambridge, Trace.valueOf(configuration.getTrace()));
    }
}
