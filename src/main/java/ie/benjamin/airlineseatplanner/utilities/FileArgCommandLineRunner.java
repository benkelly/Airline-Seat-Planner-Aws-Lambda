package ie.benjamin.airlineseatplanner.utilities;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.File;

@Slf4j
@Profile({"local", "dev"})
@Component
public class FileArgCommandLineRunner implements CommandLineRunner {
    @Value("${f:}")
    private String fileName;

    @Autowired
    ShutdownManager shutdownManager;

    @Override
    public void run(String... args) throws Exception {
        if (args != null) {
            if (args.length > 0) {
                // Checks if the input file is an actual file.
                if (fileName != null) {
                    if (!fileName.equals("")) {
                        File file = new File(fileName);
                        if (file.exists() && file.canRead()) {
                            log.info("Processing file: {}", file.getName());
                            log.debug("file path: {}, file Size: {}Kb", file.getPath(), file.getTotalSpace()/1000);
                            return;
                        }
                    }
                }
            }
        }
        log.warn("No input filename specified 🙃");
        shutdownManager.initiateShutdown(128); }}
