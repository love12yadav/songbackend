package com.example.song.Controller;

import com.example.song.Model.Song;
import com.example.song.service.SongService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/api/songs")
@CrossOrigin(origins = "http://localhost:3000")
public class SongController {

    private static final String UPLOAD_DIR = "uploads/";

    @Autowired
    private SongService songService;

    @GetMapping
    public List<Song> getAllSongs() {
        List<Song> songs = songService.getAllSongs();

        for (Song song : songs) {
            song.setPlayUrl("http://localhost:8080/api/songs/play/" + song.getUrl());
        }
        return songs;
    }



    @PostMapping("/upload")
    public ResponseEntity<String> uploadSong(@RequestParam("file") MultipartFile file,
                                             @RequestParam("title") String title,
                                             @RequestParam("artist") String artist,
                                             @RequestParam(value = "genre", required = false) String genre) {
        try {
            // Save file locally
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(UPLOAD_DIR + fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());

            Song song = new Song();
            song.setTitle(title);
            song.setArtist(artist);
            song.setGenre(genre);
            song.setUrl(fileName);
            songService.saveSong(song);

            return ResponseEntity.ok("Song uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to upload song");
        }
    }

    @GetMapping("/play/{fileName}")
    public ResponseEntity<Resource> playSong(@PathVariable String fileName) {
        try {
            Path filePath = Paths.get(UPLOAD_DIR + fileName);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace(); // Log the error for debugging
            return ResponseEntity.status(500).build();
        }
    }

}
