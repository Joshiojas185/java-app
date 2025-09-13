package com.example.javabackend;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.io.*;
import java.util.concurrent.TimeUnit;
import java.nio.charset.StandardCharsets;

@RestController
@CrossOrigin(origins = "*")
public class JavaCodeController {

    @PostMapping("/run-java")
    public String runJavaCode(@RequestBody String javaCode) {
        // Basic validation
        if (!javaCode.contains("public class Main")) {
            return "Error: Java code must contain a 'public class Main'.";
        }

        File javaFile = new File("Main.java");
        File classFile = new File("Main.class");
        String result;

        try (FileWriter writer = new FileWriter(javaFile)) {
            writer.write(javaCode);
            writer.flush();

            // 1. Compile the Java file
            Process compileProcess = new ProcessBuilder("javac", "Main.java").start();
            if (!compileProcess.waitFor(10, TimeUnit.SECONDS)) {
                compileProcess.destroy();
                return "Error: Compilation timed out.";
            }

            if (compileProcess.exitValue() != 0) {
                String compileError = readInputStream(compileProcess.getErrorStream());
                return "Compilation error:\n" + compileError;
            }

            // 2. Run the compiled Java class
            Process runProcess = new ProcessBuilder("java", "Main").start();
            if (!runProcess.waitFor(10, TimeUnit.SECONDS)) {
                runProcess.destroy();
                return "Error: Execution timed out.";
            }

            String runOutput = readInputStream(runProcess.getInputStream());
            String runError = readInputStream(runProcess.getErrorStream());

            if (runProcess.exitValue() != 0) {
                return "Runtime error:\n" + runError;
            }

            result = runOutput;

        } catch (IOException | InterruptedException e) {
            result = "Server error: " + e.getMessage();
        } finally {
            // 3. Clean up the files
            javaFile.delete();
            classFile.delete();
        }

        return result;
    }

    private String readInputStream(InputStream is) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString(StandardCharsets.UTF_8);
    }
}