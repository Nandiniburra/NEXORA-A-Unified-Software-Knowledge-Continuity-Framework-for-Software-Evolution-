package com.nexora.git;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@Slf4j
public class GitRepositoryCloner {

    private static final String TEMP_DIR = System.getProperty("java.io.tmpdir") + "/nexora-repos/";

    static {
        try {
            Files.createDirectories(Paths.get(TEMP_DIR));
        } catch (IOException e) {
            log.error("Failed to create temp directory", e);
        }
    }

    public String cloneRepository(String repositoryUrl, String repositoryName) throws IOException, InterruptedException {
        Path clonePath = Paths.get(TEMP_DIR, repositoryName);
        
        if (Files.exists(clonePath)) {
            log.info("Repository already exists, pulling latest changes: {}", repositoryName);
            pullRepository(clonePath);
        } else {
            log.info("Cloning repository: {} to {}", repositoryUrl, clonePath);
            executeGitCommand("clone", repositoryUrl, clonePath.toString());
        }
        
        return clonePath.toString();
    }

    private void pullRepository(Path repositoryPath) throws IOException, InterruptedException {
        log.info("Pulling latest changes from: {}", repositoryPath);
        executeGitCommandInDir("pull", repositoryPath.toString());
    }

    public String getRepositoryMetadata(String repositoryPath) throws IOException, InterruptedException {
        StringBuilder metadata = new StringBuilder();
        
        // Get remote URL
        String remoteUrl = executeGitCommandInDir("config", "--get", "remote.origin.url", repositoryPath).toString();
        metadata.append("Remote: ").append(remoteUrl).append("\n");
        
        // Get current branch
        String branch = executeGitCommandInDir("rev-parse", "--abbrev-ref", "HEAD", repositoryPath).toString();
        metadata.append("Branch: ").append(branch).append("\n");
        
        // Get commit count
        String commitCount = executeGitCommandInDir("rev-list", "--count", "HEAD", repositoryPath).toString();
        metadata.append("Commits: ").append(commitCount).append("\n");
        
        // Get contributor count
        String contributors = executeGitCommandInDir("shortlog", "-sn", repositoryPath).toString();
        long contributorCount = contributors.split("\n").length;
        metadata.append("Contributors: ").append(contributorCount).append("\n");
        
        return metadata.toString();
    }

    public void cleanupRepository(String repositoryPath) throws IOException {
        Path path = Paths.get(repositoryPath);
        if (Files.exists(path)) {
            deleteDirectory(path.toFile());
            log.info("Cleaned up repository: {}", repositoryPath);
        }
    }

    private void executeGitCommand(String... commands) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder("git", commands);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        int exitCode = process.waitFor();
        
        if (exitCode != 0) {
            throw new IOException("Git command failed with exit code: " + exitCode);
        }
    }

    private StringBuilder executeGitCommandInDir(String... commands) throws IOException, InterruptedException {
        String dir = commands[commands.length - 1];
        String[] gitCommands = new String[commands.length - 1];
        System.arraycopy(commands, 0, gitCommands, 0, commands.length - 1);
        
        ProcessBuilder pb = new ProcessBuilder("git", gitCommands);
        pb.directory(new File(dir));
        pb.redirectErrorStream(true);
        
        Process process = pb.start();
        StringBuilder output = new StringBuilder();
        
        try (var reader = new java.io.BufferedReader(new java.io.InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            log.warn("Git command returned exit code: {}", exitCode);
        }
        
        return output;
    }

    private void deleteDirectory(File directory) throws IOException {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    Files.delete(file.toPath());
                }
            }
        }
        Files.delete(directory.toPath());
    }
}
