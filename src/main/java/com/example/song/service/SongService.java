package com.example.song.service;

import com.example.song.Model.Song;
import com.example.song.Repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongService {

    @Autowired
    private SongRepository songRepository;

    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    public Song saveSong(Song song) {
        return songRepository.save(song);
    }

    public Song getSongById(Long id) {
        return songRepository.findById(id).orElseThrow(() -> new RuntimeException("Song not found"));
    }
}
