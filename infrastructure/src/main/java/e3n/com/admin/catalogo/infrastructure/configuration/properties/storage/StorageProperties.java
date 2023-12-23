package e3n.com.admin.catalogo.infrastructure.configuration.properties.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class StorageProperties implements InitializingBean {

    private static final Logger logger =
            LoggerFactory.getLogger(StorageProperties.class);

    private String locationPattern;
    private String filenamePattern;

    public StorageProperties() {
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.debug(toString());
    }

    @Override
    public String toString() {
        return "StorageProperties{" +
                "locationPattern='" + locationPattern + '\'' +
                ", filenamePattern='" + filenamePattern + '\'' +
                '}';
    }

    public String getLocationPattern() {
        return locationPattern;
    }

    public void setLocationPattern(String locationPattern) {
        this.locationPattern = locationPattern;
    }

    public String getFilenamePattern() {
        return filenamePattern;
    }

    public void setFilenamePattern(String filenamePattern) {
        this.filenamePattern = filenamePattern;
    }
}
